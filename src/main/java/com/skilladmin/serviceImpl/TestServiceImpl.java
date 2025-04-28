package com.skilladmin.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import com.skilladmin.dto.*;
import com.skilladmin.enumclass.QuestionLevel;
import com.skilladmin.enumclass.TestLevel;
import com.skilladmin.model.*;
import com.skilladmin.repository.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.skilladmin.enumclass.QuestionType;
import com.skilladmin.service.TestService;

@Service
public class TestServiceImpl implements TestService {


    @Autowired
    private TestRepository testRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private UserTestPreferenceRepo userTestPreferenceRepo;
    @Autowired
    private UserTestResultRepository userTestResultRepository;

    @Override
    public Test createTest(Test test) {

       // boolean exists = testRepository.existsByTestNameIgnoreCaseAndTestLevel(test.getTestName().trim(), test.getTestLevel());

//        if (exists) {
//            throw new IllegalArgumentException("Test with name '" + test.getTestName() + "' and level '" + test.getTestLevel() + "' already exists.");
//        }

        return testRepository.save(test);
    }

    @Override
    public Section addSectionToTest(Long testId, Section section) {
        Test test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test not found"));
        section.setTest(test);
        return sectionRepository.save(section);
    }

    @Override
    public Question addQuestionToSection(Long sectionId,Long testId, Question question) {
        Section section = sectionRepository.findById(sectionId).orElseThrow(() -> new RuntimeException("Section not found"));
        question.setSection(section);
        question.setTestId(testId);
        return questionRepo.save(question);
    }

    @Override
    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    @Override
    public Test getTestById(Long testId) {
        return testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test not found"));

    }

//    @Override
//    public Map<String, Object> createTestWithRandomQuestions(Long testId) {
//        Test test = testRepository.findById(testId)
//                .orElseThrow(() -> new RuntimeException("Test not found"));
//
//        List<Section> sections = test.getSections().stream()
//                .filter(section -> !questionRepo.findBySection(section).isEmpty()) // Exclude empty sections
//                .collect(Collectors.toList());
//
//        if (sections.isEmpty()) {
//            throw new RuntimeException("No sections with available questions found for this test.");
//        }
//
//        List<Question> selectedQuestions = new ArrayList<>();
//        int remainingQuestions = test.getNoOfQuestion();
//        Map<Section, List<Question>> sectionQuestionsMap = new HashMap<>();
//
//        // Step 1: Fetch and shuffle questions for each section
//        for (Section section : sections) {
//            List<Question> allQuestions = new ArrayList<>(questionRepo.findBySection(section));
//            Collections.shuffle(allQuestions);
//            sectionQuestionsMap.put(section, allQuestions);
//        }
//
//        // Step 2: Distribute questions equally across sections
//        int perSectionQuota = remainingQuestions / sections.size();
//        for (Section section : sections) {
//            List<Question> availableQuestions = sectionQuestionsMap.get(section);
//            int questionsToPick = Math.min(availableQuestions.size(), perSectionQuota);
//
//            selectedQuestions.addAll(availableQuestions.subList(0, questionsToPick));
//            remainingQuestions -= questionsToPick;
//        }
//
//        // Step 3: Fill remaining questions randomly
//        while (selectedQuestions.size() < test.getNoOfQuestion()) {
//            boolean addedQuestion = false;
//
//            for (Section section : sections) {
//                if (selectedQuestions.size() >= test.getNoOfQuestion()) break;
//
//                List<Question> availableQuestions = new ArrayList<>(sectionQuestionsMap.get(section));
//                availableQuestions.removeAll(selectedQuestions); // Remove already selected ones
//
//                if (!availableQuestions.isEmpty()) {
//                    selectedQuestions.add(availableQuestions.get(new Random().nextInt(availableQuestions.size())));
//                    addedQuestion = true;
//                }
//            }
//
//            if (!addedQuestion) break; // Prevent infinite loop if no more questions available
//        }
//
//        // Prepare response as HashMap
//        Map<String, Object> response = new HashMap<>();
//        response.put("testId", test.getId());
//        response.put("testName", test.getTestName());
//        response.put("status", test.isStatus());
//        response.put("testLevel", test.getTestLevel().toString());
//        response.put("noOfQuestion", test.getNoOfQuestion());
//        response.put("totalQuestionsSelected", selectedQuestions.size());
//        response.put("passingMarks", test.getPassingMarks());
//        response.put("creationDate", test.getCreationDate());
//        response.put("updatedAt", test.getUpdatedAt());
//
//        // Organize sections with selected questions
//        List<Map<String, Object>> sectionList = new ArrayList<>();
//        for (Section section : sections) {
//            List<Question> sectionQuestions = selectedQuestions.stream()
//                    .filter(q -> q.getSection().equals(section))
//                    .collect(Collectors.toList());
//
//            if (!sectionQuestions.isEmpty()) { // Only include sections with questions
//                Map<String, Object> sectionMap = new HashMap<>();
//                sectionMap.put("sectionId", section.getId());
//                sectionMap.put("sectionName", section.getSectionName());
//
//                List<Map<String, Object>> questionsList = new ArrayList<>();
//                for (Question question : sectionQuestions) {
//                    Map<String, Object> questionMap = new HashMap<>();
//                    questionMap.put("questionId", question.getId());
//                    questionMap.put("questionText", question.getQuestionText());
//                    questionMap.put("optionA", question.getOptionA());
//                    questionMap.put("optionB", question.getOptionB());
//                    questionMap.put("optionC", question.getOptionC());
//                    questionMap.put("optionD", question.getOptionD());
//                    questionMap.put("questionType", question.getQuestionType().toString());
//                    questionMap.put("marks", question.getMarks());
//
//                    questionsList.add(questionMap);
//                }
//
//                sectionMap.put("questions", questionsList);
//                sectionList.add(sectionMap);
//            }
//        }
//
//        response.put("sections", sectionList);
//
//        return response;
//    }


    public String saveQuestionsFromExcel(MultipartFile file, Long testId, Long sectionId) throws IOException {
        Optional<Test> testOptional = testRepository.findById(testId);
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);

        if (!testOptional.isPresent()) {
            return "Test with ID " + testId + " not found!";
        }
        if (!sectionOptional.isPresent()) {
            return "Section with ID " + sectionId + " not found!";
        }

        Test test = testOptional.get();
        Section section = sectionOptional.get();

        List<Question> questions = parseExcel(file.getInputStream(), test, section);
        questionRepo.saveAll(questions);

        return "Successfully uploaded questions for Test ID: " + testId + " and Section ID: " + sectionId;
    }




    @Override
    public TestDTO getTestWithSectionsAndQuestions(Long testId, QuestionLevel level) {
        Optional<Test> testOptional = testRepository.findById(testId);

        if (testOptional.isEmpty()) {
            throw new RuntimeException("Test not found");
        }

        Test test = testOptional.get();
        int totalQuestionsRequired = test.getNoOfQuestion();
        int marksPerQuestion = test.getMarksPerQuestion();

        List<SectionResponse> sectionResponses = new ArrayList<>();
        Map<Section, List<Question>> sectionQuestionMap = new LinkedHashMap<>();

        // Track selected question IDs to avoid duplicates
        Set<Long> selectedQuestionIds = new HashSet<>();

        for (Section section : test.getSections()) {
            List<Question> filteredQuestions = section.getQuestions().stream()
                    .filter(q -> q.getQuestionLevel() == level && q.getMarks() == marksPerQuestion)
                    .collect(Collectors.toList());

            sectionQuestionMap.put(section, filteredQuestions);
        }

        List<Question> finalSelectedQuestions = new ArrayList<>();
        int questionsPerSection = totalQuestionsRequired / sectionQuestionMap.size();
        int remainingQuestions = totalQuestionsRequired;

        for (Map.Entry<Section, List<Question>> entry : sectionQuestionMap.entrySet()) {
            List<Question> sectionQuestions = entry.getValue();

            int toSelect = Math.min(questionsPerSection, sectionQuestions.size());
            List<Question> selected = getRandomQuestionsWithoutDuplicates(sectionQuestions, toSelect, selectedQuestionIds);

            finalSelectedQuestions.addAll(selected);
            remainingQuestions -= selected.size();
        }

        for (Map.Entry<Section, List<Question>> entry : sectionQuestionMap.entrySet()) {
            if (remainingQuestions == 0) break;

            List<Question> sectionQuestions = entry.getValue();
            List<Question> additionalQuestions = getRandomQuestionsWithoutDuplicates(sectionQuestions, remainingQuestions, selectedQuestionIds);

            finalSelectedQuestions.addAll(additionalQuestions);
            remainingQuestions -= additionalQuestions.size();
        }

        for (Section section : test.getSections()) {
            List<Question> selectedQuestions = finalSelectedQuestions.stream()
                    .filter(q -> q.getSection().getId().equals(section.getId()))
                    .collect(Collectors.toList());

            List<QuestionResponse> questionResponses = selectedQuestions.stream()
                    .map(q -> new QuestionResponse(
                            q.getId(),
                            q.getQuestionText(),
                            q.getOptionA(),
                            q.getOptionB(),
                            q.getOptionC(),
                            q.getOptionD(),
                            q.getQuestionType(),
                            q.getMarks(),
                            q.getQuestionLevel()))
                    .collect(Collectors.toList());

            if (!questionResponses.isEmpty()) {
                sectionResponses.add(new SectionResponse(section.getId(), section.getSectionName(), questionResponses));
            }
        }

        return new TestDTO(test.getId(), test.getTestName(), test.getMarksPerQuestion(), test.getTimeLimit(), sectionResponses, test.getNoOfQuestion(), test.getPassingMarks());
    }

    private List<Question> getRandomQuestionsWithoutDuplicates(List<Question> questions, int limit, Set<Long> selectedQuestionIds) {
        List<Question> uniqueQuestions = questions.stream()
                .filter(q -> !selectedQuestionIds.contains(q.getId()))
                .collect(Collectors.toList());

        Collections.shuffle(uniqueQuestions);

        List<Question> selected = uniqueQuestions.subList(0, Math.min(limit, uniqueQuestions.size()));

        // Add selected question IDs to the set
        selected.forEach(q -> selectedQuestionIds.add(q.getId()));

        return selected;
    }



//    public Test getTestWithSectionsAndQuestions(Long testId, QuestionLevel level) {
//        Optional<Test> testOptional = testRepository.findById(testId);
//
//        if (testOptional.isEmpty()) {
//            throw new RuntimeException("Test not found");
//        }
//
//        Test test = testOptional.get();
//
//        // Filter sections with valid questions and exclude empty sections
//        List<Section> filteredSections = test.getSections().stream()
//                .map(section -> {
//                    List<Question> filteredQuestions = section.getQuestions().stream()
//                            .filter(q -> q.getQuestionLevel() == level && q.getMarks() == test.getMarksPerQuestion())
//                            .collect(Collectors.toList());
//
//                    List<Question> randomQuestions = getRandomQuestions(filteredQuestions, test.getNoOfQuestion());
//                    section.setQuestions(randomQuestions);
//
//                    return randomQuestions.isEmpty() ? null : section; // Exclude empty sections
//                })
//                .filter(Objects::nonNull) // Remove null sections
//                .collect(Collectors.toList());
//
//        test.setSections(filteredSections);
//        return test;
//    }
//
//
//    // Function to return only the required number of random questions
//    private List<Question> getRandomQuestions(List<Question> questions, int limit) {
//        if (questions.size() <= limit) {
//            return questions;
//        }
//        return new Random().ints(0, questions.size())
//                .distinct()
//                .limit(limit)
//                .mapToObj(questions::get)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<UserTestAttemptDTO2> getUserTestAttempts() {
        List<Object[]> results = userTestResultRepository.findUserTestAttempts();
        Map<Long, UserTestAttemptDTO2> userMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            Long userId = (Long) row[0];
            String username = (String) row[1];
            UserTestResult userTestResult = (UserTestResult) row[2];
            String testName = (String) row[3]; // Extracting test name

            userMap.putIfAbsent(userId, new UserTestAttemptDTO2(userId, username, new ArrayList<>()));

            UserTestAttemptDTO attemptDTO = new UserTestAttemptDTO(
                    userTestResult.getId(),
                    userTestResult.getUserId(),
                    testName, // Test name from Test entity
                    userTestResult.getScore(),
                    userTestResult.isPassed(),
                    userTestResult.getQuestionLevel(),
                    userTestResult.getStatus(),
                    userTestResult.getOutOfMarks(),
                    userTestResult.getCreatedAt() != null ? userTestResult.getCreatedAt() : "N/A" // Handle null values
            );

            userMap.get(userId).getAttempts().add(attemptDTO);
        }

        // Sort each user's test attempts by ID in descending order (latest first)
        userMap.values().forEach(dto -> {
            List<UserTestAttemptDTO> attempts = dto.getAttempts();
            if (attempts != null) {
                attempts.sort(Comparator.comparing(UserTestAttemptDTO::getId).reversed());
            }
        });

        return new ArrayList<>(userMap.values());
    }


    @Override
    public UserTestPreference addUserPreference(UserTestPreference userTestPreference) {
        UserTestPreference student = userTestPreferenceRepo.getStudent(userTestPreference.getStudentId());

        if (student == null) {
            student = new UserTestPreference();
            student.setLoginStatus(false);
            student.setStudentId(userTestPreference.getStudentId());
        }

        student.setPreferredSkill(userTestPreference.getPreferredSkill().trim());
        student.setLoginStatus(false);
        return userTestPreferenceRepo.save(student);
    }



    private List<Question> parseExcel(InputStream is, Test test, Section section) throws IOException {
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        List<Question> questions = new ArrayList<>();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row

            try {
                String questionText = getStringCellValue(row.getCell(0));
                String optionA = getStringCellValue(row.getCell(1));
                String optionB = getStringCellValue(row.getCell(2));
                String optionC = getStringCellValue(row.getCell(3));
                String optionD = getStringCellValue(row.getCell(4));
                String correctOptions = getStringCellValue(row.getCell(5));
                String questionType = getStringCellValue(row.getCell(6));
                int marks = getIntCellValue(row.getCell(7));
                String questionLevel = getStringCellValue(row.getCell(8));

                Question question = new Question();
                question.setQuestionText(questionText);
                question.setMarks(marks);
                question.setTestId(test.getId());
                question.setSection(section);

                try {
                    question.setQuestionType(QuestionType.valueOf(questionType.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid Question Type at row " + row.getRowNum() + ": " + questionType);
                }

                try {
                    question.setQuestionLevel(QuestionLevel.valueOf(questionLevel.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid Question Level at row " + row.getRowNum() + ": " + questionLevel);
                }

                if (questionType.equalsIgnoreCase("SINGLE_ANSWER")) {
                    question.setOptionA(optionA);
                    question.setOptionB(optionB);
                    question.setOptionC(optionC);
                    question.setOptionD(optionD);
                    question.setAnswer(correctOptions);
                } else if (questionType.equalsIgnoreCase("MULTIPLE_ANSWER")) {
                    question.setOptionA(optionA);
                    question.setOptionB(optionB);
                    question.setOptionC(optionC);
                    question.setOptionD(optionD);
                    question.setCorrectAnswers(Arrays.asList(correctOptions.split(",")));
                } else if (questionType.equalsIgnoreCase("TRUE_FALSE")) {
                    // Set TRUE_FALSE options as "A" -> "True", "B" -> "False"
                    question.setOptionA("True");
                    question.setOptionB("False");
                    question.setOptionC(null);
                    question.setOptionD(null);

                    // Convert correctOptions ("True, False", "A, B") into "A, B"
                    List<String> correctAnswersList = Arrays.stream(correctOptions.split(","))
                            .map(String::trim)
                            .map(opt -> {
                                if (opt.equalsIgnoreCase("True") || opt.equalsIgnoreCase("A")) return "A";
                                else if (opt.equalsIgnoreCase("False") || opt.equalsIgnoreCase("B")) return "B";
                                else throw new RuntimeException("Invalid Correct Option for TRUE_FALSE at row " + row.getRowNum() + ": " + opt);
                            })
                            .collect(Collectors.toList());
                    String correctAnswersString = correctAnswersList.stream().collect(Collectors.joining(","));
                    question.setAnswer(correctAnswersString);

                 //   question.setCorrectAnswers(correctAnswersList);
                }

                questions.add(question);

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error processing row " + row.getRowNum() + ": " + e.getMessage());
            }
        }

        workbook.close();
        return questions;
    }


//    private List<Question> parseExcel(InputStream is, Test test, Section section) throws IOException {
//        Workbook workbook = new XSSFWorkbook(is);
//        Sheet sheet = workbook.getSheetAt(0);
//        List<Question> questions = new ArrayList<>();
//
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) continue; // Skip header row
//
//            try {
//                String questionText = getStringCellValue(row.getCell(0));
//                String optionA = getStringCellValue(row.getCell(1));
//                String optionB = getStringCellValue(row.getCell(2));
//                String optionC = getStringCellValue(row.getCell(3));
//                String optionD = getStringCellValue(row.getCell(4));
//                String correctOptions = getStringCellValue(row.getCell(5));
//                String questionType = getStringCellValue(row.getCell(6));
//                int marks = getIntCellValue(row.getCell(7));
//                String questionLevel = getStringCellValue(row.getCell(8));
//
//                Question question = new Question();
//                question.setQuestionText(questionText);
//                question.setOptionA(optionA);
//                question.setOptionB(optionB);
//                question.setOptionC(optionC);
//                question.setOptionD(optionD);
//                question.setMarks(marks);
//                question.setTestId(test.getId());
//                question.setSection(section);
//
//                try {
//                    question.setQuestionType(QuestionType.valueOf(questionType.toUpperCase()));
//                } catch (IllegalArgumentException e) {
//                    throw new RuntimeException("Invalid Question Type at row " + row.getRowNum() + ": " + questionType);
//                }
//
//                try {
//                    question.setQuestionLevel(QuestionLevel.valueOf(questionLevel.toUpperCase()));
//                } catch (IllegalArgumentException e) {
//                    throw new RuntimeException("Invalid Question Level at row " + row.getRowNum() + ": " + questionLevel);
//                }
//
//                if (questionType.equalsIgnoreCase("SINGLE_ANSWER")) {
//                    question.setAnswer(correctOptions);
//                } else {
//                    question.setCorrectAnswers(Arrays.asList(correctOptions.split(",")));
//                }
//
//                questions.add(question);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new RuntimeException("Error processing row " + row.getRowNum() + ": " + e.getMessage());
//            }
//        }
//
//        workbook.close();
//        return questions;
//    }

    // Utility Methods
    private static String getStringCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    private static int getIntCellValue(Cell cell) {
        if (cell == null) return 0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else {
            try {
                return Integer.parseInt(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }
}

