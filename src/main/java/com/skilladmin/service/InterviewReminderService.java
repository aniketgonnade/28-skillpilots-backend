package com.skilladmin.service;

import com.skilladmin.model.CompanyDrive;
import com.skilladmin.model.Recruitment;
import com.skilladmin.model.RecruitmentStatus;
import com.skilladmin.model.User;
import com.skilladmin.repository.CompanyDriveRepository;
import com.skilladmin.repository.RecruitmentRepository;
import com.skilladmin.repository.RecruitmentStatusRepo;
import com.skilladmin.repository.UserRepository;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InterviewReminderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecruitmentStatusRepo recruitmentStatusRepo;
    @Autowired
    private  JavaMailSender javaMailSender;
    @Autowired
    private CompanyDriveRepository companyDriveRepository;
    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void sendInterviewReminders() {
        LocalDateTime now = LocalDateTime.now();
        String start = now.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String end = now.plusHours(1).plusMinutes(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        List<RecruitmentStatus> interviews = recruitmentStatusRepo.findDueInterviews(start, end);

        for (RecruitmentStatus status : interviews) {
            String studentIdStr = String.valueOf(status.getStudentId());
            User user = userRepository.findById(status.getStudentId()).orElseThrow();

            CompanyDrive drive = null;
            if (status.getDriveId() != null) {
                drive = companyDriveRepository.findById(status.getDriveId()).orElse(null);
            }
            if (user != null && drive != null) {
                sendEmail(user.getUsername(), drive, status);
                recruitmentStatusRepo.save(status); // Mark as sent
            }
        }
    }



    private void sendEmail(String email, CompanyDrive drive, RecruitmentStatus status) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Interview Reminder - " + drive.getCompanyName());
            message.setText(generateBody(drive, status));
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateBody(CompanyDrive drive, RecruitmentStatus status) {
        return String.format("""
            Dear Candidate,

            This is a reminder for your upcoming interview.

            🏢 Company: %s
            📅 Interview Date: %s
            🎯 Round: %s
            📍 Location: %s
            💼 Job Role: %s

            Please be prepared and join on time.
            Best of luck!

            - SkillPilots Team
            """, drive.getCompanyName(), drive.getInterviewDate(), status.getRound(),
                drive.getLocation(), drive.getJobRole());
    }


    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    // 22-042025 shrunkhal
    //@Scheduled(cron = "0 * * * * *") // Runs every minute
    public void checkAndSendReminders() throws MessagingException {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("🕒 Scheduler run at: " + now);

        // Loop through all the recruitment statuses to find the interviews 1 hour ahead
        List<RecruitmentStatus> all = recruitmentStatusRepo.findAll();
        System.out.println("📦 Total records in DB: " + all.size());

        List<RecruitmentStatus> reminders = all.stream()
                .filter(rs -> {
                    try {
                        LocalDateTime interviewTime = rs.getRoundDateTime();

                        if (interviewTime == null) {
                            System.out.println("⚠️ Skipping studentId " + rs.getStudentId() + " due to null interview time.");
                            return false;
                        }

                        System.out.println("📌 Student ID: " + rs.getStudentId() + " | Interview Time: " + interviewTime);

                        // Check if the current time is exactly 1 hour before the interview time
                        LocalDateTime reminderTime = interviewTime.minusHours(1);

                        System.out.println("SSSSSSSSS"+ reminderTime);
                        boolean isReminderTime = now.isAfter(reminderTime.minusMinutes(1)) && now.isBefore(reminderTime.plusMinutes(1));

                        System.out.println("ddddddddddddd"+ isReminderTime);

                        if (isReminderTime) {
                            System.out.println("✅ Sending reminder email to studentId: " + rs.getStudentId());
                        }

                        return isReminderTime;

                    } catch (Exception e) {
                        System.out.println("❌ Error for studentId: " + rs.getStudentId());
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());

        System.out.println("📧 Total emails to send: " + reminders.size());

        for (RecruitmentStatus rs : reminders) {
            System.out.println("dddddddddddd"+ rs);
            sendReminderEmail(rs);  // Send email reminder
        }
    }


    public void sendReminderEmail(RecruitmentStatus status) {
        try {
            Optional<User> userOpt = userRepository.findById(status.getStudentId());
            if (userOpt.isEmpty()) {
                System.out.println("User not found with ID: " + status.getStudentId());
                return;
            }

            User user = userOpt.get();
            String studentEmail = "shrunkhal4@gmail.com";
            String studentName = user.getName();

            // Get company name from either CompanyDrive or Recruitment
            String companyName = "Company";
            String jobTitle="jobTitle";
            try {
                CompanyDrive drive = companyDriveRepository.findById(status.getDriveId())
                        .orElseThrow(() -> new RuntimeException("Drive not found"));
                companyName = drive.getCompanyName();
                 jobTitle=  drive.getJobRole();
            } catch (Exception e) {
                try {
                    Recruitment recruitment = recruitmentRepository.findById(status.getReqruitmentId())
                            .orElseThrow(() -> new RuntimeException("Recruitment not found"));
                    companyName = recruitment.getCompanyName();
                    jobTitle= recruitment.getProfile();
                } catch (Exception ex) {
                    System.out.println("Both CompanyDrive and Recruitment not found for ID: " + status.getDriveId());
                }
            }

            // Prepare and send HTML email
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(studentEmail);
            helper.setSubject("Reminder: Your Interview Scheduled with " + companyName);

            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px;'>"
                    + "<h1 style='text-align:center; color:#1974c3;'>Skill"
                    + "<span style='color:#01af36;'>Pilots</span></h1>"
                    + "<p>Hi " + studentName + ",</p>"
                    + "<p>This is a reminder for your upcoming interview with <strong>" + companyName + "</strong>.</p>"
                    + "<p><strong>Position:</strong> " + jobTitle + "<br>"
                    + "<strong>Date:</strong> " + status.getRoundDate() + "<br>"
                    + "<strong>Time:</strong> " + status.getRoundTime() + "<br>"
                    + "<p>Please ensure you're prepared and log in at least 10 minutes early.</p>"
                    + "<p>Best of luck!</p>"
                    + "<br><p>Warm regards,<br>"
                    + "<strong>SkillPilots Team</strong><br>"
                    + "<a href='mailto:support@skillpilots.in'>support@skillpilots.in</a><br>"
                    + "<a href='https://www.skillpilots.in'>www.skillpilots.in</a></p>"
                    + "</div>";
            helper.setText(htmlContent, true);

            // Create .ics calendar invite
            String icsContent = "BEGIN:VCALENDAR\n" +
                    "METHOD:REQUEST\n" +
                    "PRODID:-//SkillPilots//Interview Reminder//EN\n" +
                    "VERSION:2.0\n" +
                    "CALSCALE:GREGORIAN\n" +
                    "BEGIN:VEVENT\n" +
                    "DTSTAMP:" + ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")) + "\n" +
                    "DTSTART;TZID=Asia/Kolkata:" + status.getRoundDate().toString().replace("-", "") +
                    "T" + status.getRoundTime().replace(":", "") + "00\n" +
                    "DTEND;TZID=Asia/Kolkata:" + status.getRoundDate().toString().replace("-", "") +
                    "T" + getEndTime(status.getRoundTime()) + "00\n" +
                    "SUMMARY:Interview with " + companyName + "\n" +
                    "DESCRIPTION:Interview for position: " + jobTitle +

                    "ORGANIZER;CN=SkillPilots:mailto:support@skillpilots.com\n" +
                    "STATUS:CONFIRMED\n" +
                    "SEQUENCE:0\n" +
                    "BEGIN:VALARM\n" +
                    "TRIGGER:-PT10M\n" +
                    "ACTION:DISPLAY\n" +
                    "DESCRIPTION:Reminder\n" +
                    "END:VALARM\n" +
                    "END:VEVENT\n" +
                    "END:VCALENDAR";


            InputStream icsStream = new ByteArrayInputStream(icsContent.getBytes(StandardCharsets.UTF_8));
            DataSource dataSource = new ByteArrayDataSource(icsStream, "text/calendar");
            helper.addAttachment("interview-invite.ics", dataSource);

            javaMailSender.send(message);
            System.out.println("Reminder email with .ics calendar invite sent to " + studentEmail);

            System.out.println("Reminder email sent to " + studentEmail);

        } catch (Exception e) {
            System.err.println("Error sending reminder email: " + e.getMessage());
            e.printStackTrace(); // Optional: Log to a logger if available
        }
    }

    private String getEndTime(String startTime) {
        LocalTime time = LocalTime.parse(startTime);
        LocalTime endTime = time.plusMinutes(30);
        return endTime.format(DateTimeFormatter.ofPattern("HHmm"));
    }

    @Scheduled(cron = "0 * * * * *")  // Every minute
    public void checkAndSendReminder() {
        System.out.println("✅ Scheduler is running...");
    }
}
