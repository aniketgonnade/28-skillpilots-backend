package com.skilladmin.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.skilladmin.dto.*;
import com.skilladmin.enumclass.QuestionLevel;
import com.skilladmin.model.*;
import com.skilladmin.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skilladmin.enumclass.QuestionType;
import com.skilladmin.service.ExamService;
import com.skilladmin.service.QuestionService;
import com.skilladmin.service.TestService;
import com.skilladmin.serviceImpl.TestServiceImpl;

@RestController
@CrossOrigin(origins = { "*" })
public class ExamController {

	@Autowired
	private ExamService examService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private TestService testService;
	@Autowired
	private TestServiceImpl testServiceImpl;
	@Autowired
	private SectionRepository sectionRepository;
    @Autowired
    private TestRepository testRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserTestResultRepository userTestResultRepository;
	@Autowired
	private QuestionRepo questionRepo;
	@Autowired
	private UserTestPreferenceRepo  userTestPreferenceRepo;
	@Autowired
	private TestCertificateRepo testCertificateRepo;


//	@GetMapping("/testAll/{userId}")
//	public ResponseEntity<List<CodingLanguageDTO>> getUserSections(@PathVariable Long userId) {
//		List<CodingLanguageDTO> responseList = new ArrayList<>();
//
//		// Fetch all tests
//		List<Test> tests = testRepository.findAll();
//
//		for (Test test : tests) {
//			List<SectionDTO> sectionDTOs = new ArrayList<>();
//
//			for (Section section : test.getSections()) {
//				boolean BASIC = true; // Basic level is always allowed
//				boolean INTERMEDIATE = userTestResultRepository.existsByUserIdAndTestIdAndQuestionLevelAndPassed(userId, test.getId(), QuestionLevel.BASIC, true);
//				boolean ADVANCED = userTestResultRepository.existsByUserIdAndTestIdAndQuestionLevelAndPassed(userId, test.getId(), QuestionLevel.INTERMEDIATE, true);
//
//				sectionDTOs.add(new SectionDTO(
//						section.getId(),
//						section.getSectionName(),
//						BASIC,
//						INTERMEDIATE,
//						ADVANCED
//				));
//			}
//
//			// **Exclude test if it has no sections**
//			if (!sectionDTOs.isEmpty()) {
//				CodingLanguageDTO languageDTO = new CodingLanguageDTO();
//				languageDTO.setId(test.getId());
//				languageDTO.setName(test.getTestName());
//				languageDTO.setDescription(test.getDescription());
//				languageDTO.setSections(sectionDTOs);
//				responseList.add(languageDTO);
//			}
//		}
//
//		return ResponseEntity.ok(responseList);
//	}



	// start test api - for new portal /26/03
    @GetMapping("/questions/{testId}")
    public TestDTO getTest(
            @PathVariable Long testId,
            @RequestParam QuestionLevel level
           ) {

        return testService.getTestWithSectionsAndQuestions(testId, level);
    }

    // old api
//    @GetMapping("/questions")
//    public ResponseEntity<?> getTestQuestions(
//            @RequestParam Long testId,
//            @RequestParam Long sectionId,
//            @RequestParam QuestionLevel level) {
//
//        Test test = testRepository.findById(testId)
//                .orElseThrow(() -> new RuntimeException("Test not found"));
//
//        Section section = sectionRepository.findById(sectionId)
//                .orElseThrow(() -> new RuntimeException("Section not found"));
//
//        int questionLimit = test.getNoOfQuestion();
//        int marksPerQuestion = test.getMarksPerQuestion();
//        int passingMarks = test.getPassingMarks();
//		int noOfQuestion=test.getNoOfQuestion();
//
//        List<Question> allQuestions = questionRepo.findBySectionIdAndQuestionLevel(sectionId, level)
//                .stream()
//                .filter(q -> q.getMarks() == marksPerQuestion) // Direct comparison (int)
//                .collect(Collectors.toList());
//
//        if (allQuestions.size() < questionLimit) {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No valid section available.");
//        }
//
//        Collections.shuffle(allQuestions);
//        List<Question> selectedQuestions = allQuestions.stream()
//                .limit(questionLimit)
//                .collect(Collectors.toList());
//
//        // Convert questions to DTO format
//        List<QuestionDTO> questionDTOs = selectedQuestions.stream()
//                .map(q -> new QuestionDTO(
//                        q.getId(),
//                        q.getQuestionText(),
//                        Map.of(
//                                "A", Optional.ofNullable(q.getOptionA()).orElse(""),
//                                "B", Optional.ofNullable(q.getOptionB()).orElse(""),
//                                "C", Optional.ofNullable(q.getOptionC()).orElse(""),
//                                "D", Optional.ofNullable(q.getOptionD()).orElse("")
//                        ),
//                        Optional.ofNullable(q.getQuestionType()).map(Enum::name).orElse("UNKNOWN"),
//                        q.getMarks(),
//                        marksPerQuestion
//                ))
//                .collect(Collectors.toList());
//
//        // Create Section DTO
//        SectionTest sectionDTO = new SectionTest(
//                section.getId(),
//                section.getSectionName(),
//                test.getTimeLimit(),
//                test.getPassingMarks(),
//                questionDTOs,
//				noOfQuestion
//        );
//
//        // If no valid sections, do not return the test
//        if (questionDTOs.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No valid sections available.");
//        }
//
//        // Create Test DTO
//        TestDTO testDTO = new TestDTO(
//                test.getId(),
//                test.getTestName(),
//                List.of(sectionDTO),
//                passingMarks
//        );
//
//        return ResponseEntity.ok(testDTO);
//    }

	// test-attempts history fir admin 26/03  - shrunkhal
    @GetMapping("/test-attempts")
    public ResponseEntity<List<UserTestAttemptDTO2>> getUserTestAttempts() {
        List<UserTestAttemptDTO2> userTestAttempts = testService.getUserTestAttempts();
        return ResponseEntity.status(200).body(userTestAttempts);
    }

	// submit test api - for new portal 26/03- shrunkhal
//    @PostMapping("/submit-test")
//    public TestResultResponse processTestSubmission(@RequestBody TestSubmission request) {
//        Long userId = request.getUserId();
//        Long testId = request.getTestId();
//        String status = request.getStatus();
//        String level = request.getLevel();
//
//       request.getLevel();
//
//        System.out.println("Received test submission request for User ID: " + userId + ", Test ID: " + testId);
//        Test test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test Not found"));
//        int totalMarks = test.getNoOfQuestion() * test.getMarksPerQuestion();
//
//        // Disqualify logic
//        if ("D".equalsIgnoreCase(status)) {
//            System.out.println("User ID " + userId + " is DISQUALIFIED for Test ID " + testId);
//            UserTestResult testResult = new UserTestResult();
//            testResult.setUserId(userId);
//            testResult.setTest(test);
//            testResult.setScore(0);
//            testResult.setStatus("DISQUALIFIED");
//            testResult.setOutOfMarks(totalMarks);
//            testResult.setPassed(false);
//            testResult.setQuestionLevel(QuestionLevel.valueOf(level));
//            testResult.setCorrectAnswers(0);
//            testResult.setIncorrectAnswers(0);
//            testResult.setAttemptedQuestions(0);
//            testResult.setNotAttemptedQuestions(test.getNoOfQuestion());
//
//            userTestResultRepository.save(testResult);
//
//            return new TestResultResponse(userId, testId, 0, test.getNoOfQuestion(), 0, test.getPassingMarks(),
//                    "DISQUALIFIED", testResult.getCreatedAt(), 0, test.getNoOfQuestion(), 0);        }
//
//        // Answer processing
//        TestDTO questions = testService.getTestWithSectionsAndQuestions(testId, QuestionLevel.valueOf(level));
//        Map<Long, List<String>> answerMapByQuestionId = new HashMap<>();
//
//        List<Map<String, Object>> answers = request.getAnswers();
//        int totalScore = 0;
//        int totalQuestions = answers.size();
//        int correctAnswersCount = 0;
//        int incorrectAnswersCount = 0;
//        int attemptedQuestions = 0;
//        int notAttemptedQuestions = 0;
//
//        System.out.println("Processing " + totalQuestions + " questions for User ID: " + userId);
//
//        for (Map<String, Object> answerMap : answers) {
//
//
//            Long questionId = ((Number) answerMap.get("questionId")).longValue();
//            List<String> selectedAnswers = (List<String>) answerMap.get("selectedAnswers");
//
//            Question question = questionRepo.findById(questionId)
//                    .orElseThrow(() -> new RuntimeException("Invalid question ID: " + questionId));
//
//            if (selectedAnswers == null || selectedAnswers.isEmpty()) {
//                notAttemptedQuestions++;
//                continue;
//            }
//
//            attemptedQuestions++;
//            boolean isCorrect = validateAnswer(question, selectedAnswers);
//            if (isCorrect) {
//                totalScore += test.getMarksPerQuestion();
//                correctAnswersCount++;
//            } else {
//                incorrectAnswersCount++;
//            }
//
//
//        }
//
//        int cutoffMarks = test.getPassingMarks();
//        String finalStatus = totalScore >= cutoffMarks ? "PASSED" : "FAILED";
//        boolean statusTest = totalScore >= cutoffMarks;
//
//        // Save result
//        UserTestResult testResult = new UserTestResult();
//        testResult.setUserId(userId);
//        testResult.setTest(test);
//        testResult.setScore(totalScore);
//        testResult.setOutOfMarks(totalMarks);
//        testResult.setStatus(finalStatus);
//        testResult.setPassed(statusTest);
//        testResult.setQuestionLevel(QuestionLevel.valueOf(level));
//        testResult.setCorrectAnswers(correctAnswersCount);
//        testResult.setIncorrectAnswers(incorrectAnswersCount);
//        testResult.setAttemptedQuestions(attemptedQuestions);
//        testResult.setNotAttemptedQuestions(notAttemptedQuestions);
//
//        userTestResultRepository.save(testResult);
//
//        return new TestResultResponse(userId, testId, totalScore, totalQuestions, correctAnswersCount,
//                cutoffMarks, finalStatus, testResult.getCreatedAt(), attemptedQuestions, notAttemptedQuestions, incorrectAnswersCount);
//    }
//
//	private boolean validateAnswer(Question question, List<String> selectedAnswers) {
//		if (selectedAnswers == null || selectedAnswers.isEmpty()) {
//			System.out.println("No answer selected for Question ID: " + question.getId());
//			return false;
//		}
//
//		if (question.getQuestionType() == QuestionType.SINGLE_ANSWER) {
//			boolean isCorrect = question.getAnswer().equalsIgnoreCase(selectedAnswers.get(0));
//			System.out.println("Single Answer Question - User Answer: " + selectedAnswers.get(0) + ", Correct? " + isCorrect);
//			return isCorrect;
//		} else if (question.getQuestionType() == QuestionType.TRUE_FALSE) {
//			boolean isCorrect =question.getAnswer().equalsIgnoreCase(selectedAnswers.get(0));
//			System.out.println("True/False Question - User Answer: " + selectedAnswers + ", Correct? " + isCorrect);
//			return isCorrect;
//		} else {
//			List<String> correctAnswers = question.getCorrectAnswers();
//			List<String> userAnswers = selectedAnswers;
//			correctAnswers = correctAnswers.stream()
//					.map(String::trim)
//					.map(String::toUpperCase)
//					.collect(Collectors.toList());
//
//			selectedAnswers = selectedAnswers.stream()
//					.map(String::trim)
//					.map(String::toUpperCase)
//					.collect(Collectors.toList());
//			Collections.sort(correctAnswers);
//			Collections.sort(userAnswers);
//			boolean isCorrect = correctAnswers.equals(userAnswers);
//			System.out.println("Multiple Choice Question - User Answers: " + selectedAnswers + ", Correct? " + isCorrect);
//			System.out.println("Multiple Choice Question -  Answers: " + correctAnswers + ", Correct? " + isCorrect);
//
//			return isCorrect;
//		}
//	}


	@GetMapping("/score/{userId}")
	public ResponseEntity<Object> getCertificateScore(@PathVariable Long userId){
		TestCertificate certificate = testCertificateRepo.findByUserId(userId);
		return  ResponseEntity.status(200).body(certificate);
	}


	// 25/04/25
	@PostMapping("/submit-test")
	public TestResultResponse processTestSubmission(@RequestBody TestSubmission request) {
		Long userId = request.getUserId();
		Long testId = request.getTestId();
		String status = request.getStatus();
		String level = request.getLevel();

		System.out.println("Received test submission request for User ID: " + userId + ", Test ID: " + testId);
		Test test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test Not found"));
		int totalMarks = test.getNoOfQuestion() * test.getMarksPerQuestion();

		// Disqualify logic
		if ("D".equalsIgnoreCase(status)) {
			System.out.println("User ID " + userId + " is DISQUALIFIED for Test ID " + testId);
			UserTestResult testResult = new UserTestResult();
			testResult.setUserId(userId);
			testResult.setTest(test);
			testResult.setScore(0);
			testResult.setStatus("DISQUALIFIED");
			testResult.setOutOfMarks(totalMarks);
			testResult.setPassed(false);
			testResult.setQuestionLevel(QuestionLevel.valueOf(level));
			testResult.setCorrectAnswers(0);
			testResult.setIncorrectAnswers(0);
			testResult.setAttemptedQuestions(0);
			testResult.setNotAttemptedQuestions(test.getNoOfQuestion());

			userTestResultRepository.save(testResult);

			return new TestResultResponse(userId, testId, 0, test.getNoOfQuestion(), 0, test.getPassingMarks(),
					"DISQUALIFIED", testResult.getCreatedAt(), 0, test.getNoOfQuestion(), 0);
		}

		// Answer processing
		TestDTO questions = testService.getTestWithSectionsAndQuestions(testId, QuestionLevel.valueOf(level));
		List<Map<String, Object>> answers = request.getAnswers();
		int totalScore = 0;
		int correctAnswersCount = 0;
		int incorrectAnswersCount = 0;
		int attemptedQuestions = 0;
		int notAttemptedQuestions = 0;

		for (Map<String, Object> answerMap : answers) {
			Long questionId = ((Number) answerMap.get("questionId")).longValue();
			List<String> selectedAnswers = (List<String>) answerMap.get("selectedAnswers");

			Question question = questionRepo.findById(questionId)
					.orElseThrow(() -> new RuntimeException("Invalid question ID: " + questionId));

			if (selectedAnswers == null || selectedAnswers.isEmpty()) {
				notAttemptedQuestions++;
				continue;
			}

			attemptedQuestions++;
			boolean isCorrect = validateAnswer(question, selectedAnswers);
			if (isCorrect) {
				totalScore += test.getMarksPerQuestion();
				correctAnswersCount++;
			} else {
				incorrectAnswersCount++;
			}
		}

		int cutoffMarks = test.getPassingMarks();
		String finalStatus = totalScore >= cutoffMarks ? "PASSED" : "FAILED";
		boolean statusTest = totalScore >= cutoffMarks;

		// Save User Test Result
		UserTestResult testResult = new UserTestResult();
		testResult.setUserId(userId);
		testResult.setTest(test);
		testResult.setScore(totalScore);
		testResult.setOutOfMarks(totalMarks);
		testResult.setStatus(finalStatus);
		testResult.setPassed(statusTest);
		testResult.setQuestionLevel(QuestionLevel.valueOf(level));
		testResult.setCorrectAnswers(correctAnswersCount);
		testResult.setIncorrectAnswers(incorrectAnswersCount);
		testResult.setAttemptedQuestions(attemptedQuestions);
		testResult.setNotAttemptedQuestions(notAttemptedQuestions);

		userTestResultRepository.save(testResult);

		// Save or Update Certificate
		if ("PASSED".equals(finalStatus)) {
			// Fetch all existing certificates for this user and test
			List<TestCertificate> allCerts = testCertificateRepo.findAllByUserIdAndTestId(userId, testId);

			// Find the one with the highest score
			Optional<TestCertificate> highestScoreCertOpt = allCerts.stream()
					.max(Comparator.comparingInt(TestCertificate::getScore));

			if (highestScoreCertOpt.isPresent()) {
				TestCertificate existingCert = highestScoreCertOpt.get();
				int previousScore = existingCert.getScore();

				if (totalScore > previousScore) {
					// Update the existing certificate with higher score
					existingCert.setScore(totalScore);
					existingCert.setIssuedAt(LocalDateTime.now());
					existingCert.setLevel(level);
					existingCert.setUserTestResult(testResult);
					testCertificateRepo.save(existingCert);
					System.out.println("Certificate updated with a higher score.");
				} else {
					System.out.println("Certificate not updated. Existing score is higher or equal.");
				}

			} else {
				// No certificate exists at all for this test
				TestCertificate certificate = new TestCertificate();
				certificate.setUserId(userId);
				certificate.setTestId(testId);
				certificate.setScore(totalScore);
				certificate.setIssuedAt(LocalDateTime.now());
				certificate.setLevel(level);
				certificate.setUserTestResult(testResult);
				testCertificateRepo.save(certificate);
				System.out.println("Certificate created for first pass.");
			}
		}


		return new TestResultResponse(
				userId,
				testId,
				totalScore,
				answers.size(),
				correctAnswersCount,
				cutoffMarks,
				finalStatus,
				testResult.getCreatedAt(),
				attemptedQuestions,
				notAttemptedQuestions,
				incorrectAnswersCount
		);
	}

	// Answer checking logic
	private boolean validateAnswer(Question question, List<String> selectedAnswers) {
		if (selectedAnswers == null || selectedAnswers.isEmpty()) return false;

		if (question.getQuestionType() == QuestionType.SINGLE_ANSWER ||
				question.getQuestionType() == QuestionType.TRUE_FALSE) {
			return question.getAnswer().equalsIgnoreCase(selectedAnswers.get(0));
		} else {
			List<String> correctAnswers = question.getCorrectAnswers().stream()
					.map(String::trim)
					.map(String::toUpperCase)
					.sorted()
					.collect(Collectors.toList());

			List<String> userAnswers = selectedAnswers.stream()
					.map(String::trim)
					.map(String::toUpperCase)
					.sorted()
					.collect(Collectors.toList());

			return correctAnswers.equals(userAnswers);
		}
	}



	// CREATE TEST 20/03 - shrunkhal
    @PostMapping("/tests")
    public ResponseEntity<?> createTest(@RequestBody Test test) {
        try {
            Test createdTest = testService.createTest(test);
            return ResponseEntity.ok(createdTest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("msg",e.getMessage()));
        }
    }

	// ADD SECTION 20/03/ - shrunkhal
	@PostMapping("/add/{testId}")
	public ResponseEntity<Section> addSection(@PathVariable Long testId, @RequestBody Section section) {
		Section newSection = testService.addSectionToTest(testId, section);
		return ResponseEntity.ok(newSection);
	}

	// update section 12/03/25
	@PutMapping("/section/{sectionId}")
	public ResponseEntity<?> updateSection(@PathVariable Long sectionId, @RequestBody Section updatedSection) {
		Section existingSection = sectionRepository.findById(sectionId)
				.orElseThrow(() -> new RuntimeException("Section not found"));

		existingSection.setSectionName(updatedSection.getSectionName());

		sectionRepository.save(existingSection);
		return ResponseEntity.ok(Map.of("msg","Section updated successfully!",
				"section",existingSection));	}

	// ADD QUESTION
	@PostMapping("/addQuestion/{sectionId}/{testId}")
	public ResponseEntity<Question> addQuestion(@PathVariable Long sectionId,@PathVariable Long testId, @RequestBody Question question) {
		Question newQuestion = testService.addQuestionToSection(sectionId,testId, question);
		return ResponseEntity.ok(newQuestion);
	}


	// add skill preference for students
	@PostMapping("/addPreference")
	public ResponseEntity<Object> addUserSkillPreference(@RequestBody UserTestPreference userTestPreference){

		return 	ResponseEntity.ok(testService.addUserPreference(userTestPreference));
	}
//	@GetMapping("/testAll")
//	public List<CodingLanguageDTO> getTestsByStudentPreference(@RequestParam Long studentId) {
//		List<CodingLanguageDTO> responseList = new ArrayList<>();
//
//		// Fetch student's preferred skills
//		List<UserTestPreference> preferences = userTestPreferenceRepo.findByStudentId(studentId);
//		List<String> skillPreferences = preferences.stream()
//				.map(UserTestPreference::getPreferredSkill)
//				.collect(Collectors.toList());
//
//		// Fetch tests based on preferred skills
//		List<Test> tests = skillPreferences.stream()
//				.flatMap(skill -> testRepository.findByTestNamePriority(skill).stream())
//				.distinct()
//				.collect(Collectors.toList());
//
//		for (Test test : tests) {
//			List<SectionDTO> sectionDTOs = new ArrayList<>();
//
//			for (Section section : test.getSections()) {
//				// **Exclude sections that have no questions**
//				if (section.getQuestions() == null || section.getQuestions().isEmpty()) {
//					continue;
//				}
//
//				boolean BASIC = true; // Basic level is always accessible
//				boolean INTERMEDIATE = userTestResultRepository.existsByUserIdAndTestIdAndQuestionLevelAndPassed(studentId, test.getId(), QuestionLevel.BASIC, true);
//				boolean ADVANCED = userTestResultRepository.existsByUserIdAndTestIdAndQuestionLevelAndPassed(studentId, test.getId(), QuestionLevel.INTERMEDIATE, true);
//
//				sectionDTOs.add(new SectionDTO(
//						section.getId(),
//						section.getSectionName(),
//						BASIC,
//						INTERMEDIATE,
//						ADVANCED
//				));
//			}
//
//			// **Exclude tests if they have no valid sections**
//			if (!sectionDTOs.isEmpty()) {
//				CodingLanguageDTO languageDTO = new CodingLanguageDTO();
//				languageDTO.setId(test.getId());
//				languageDTO.setName(test.getTestName());
//				languageDTO.setDescription(test.getDescription());
//				languageDTO.setSections(sectionDTOs);
//				responseList.add(languageDTO);
//			}
//		}
//
//		return responseList;
//	}


	// get tests for student as per skill preference 22/03 - shrunkhal
    @GetMapping("/testAll")
    public List<CodingLanguageDTO> getTestsByStudentPreference(@RequestParam Long studentId) {
        List<CodingLanguageDTO> responseList = new ArrayList<>();

        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Invalid student ID");
        }

        // Fetch student's preferred skills
        List<UserTestPreference> preferences = userTestPreferenceRepo.findByStudentId(studentId);
        if (preferences.isEmpty()) {
            System.out.println("No preferences found for Student ID: " + studentId);
            return responseList;
        }

        List<String> skillPreferences = preferences.stream()
                .map(UserTestPreference::getPreferredSkill)
                .collect(Collectors.toList());

        System.out.println("Student ID: " + studentId + " | Preferred Skills: " + skillPreferences);

        List<Test> tests = skillPreferences.stream()
                .flatMap(skill -> testRepository.findByTestNamePriority(skill).stream())
                .distinct()
                .collect(Collectors.toList());

        if (tests.isEmpty()) {
            System.out.println("No tests found for preferred skills.");
            return responseList;
        }

        System.out.println("Tests found based on preferences: " + tests.size());

		for (Test test : tests) {
			System.out.println("Processing Test: " + test.getTestName() + " (ID: " + test.getId() + ")");
			List<CodingDto> sectionDTOs = new ArrayList<>();
			List<AttemptDTO> attemptHistory = new ArrayList<>();

			boolean BASIC = false;
			boolean INTERMEDIATE = false;
			boolean ADVANCED = false;

			if (test.getSections() == null || test.getSections().isEmpty()) {
				System.out.println("Skipping Test: " + test.getTestName() + " due to no sections.");
				continue;
			}

			int totalValidQuestions = 0;
			int questionLimit = test.getNoOfQuestion();
			int marksPerQuestion = test.getMarksPerQuestion();

			for (Section section : test.getSections()) {
				System.out.println("Evaluating Section: " + section.getSectionName() + " (ID: " + section.getId() + ")");

				if (section.getQuestions() == null || section.getQuestions().isEmpty()) {
					System.out.println("Skipping Section: " + section.getSectionName() + " due to no questions.");
					continue;
				}

				List<Question> validQuestions = section.getQuestions().stream()
						.filter(q -> q.getMarks() == marksPerQuestion)
						.collect(Collectors.toList());

				System.out.println("Valid Questions Found: " + validQuestions.size());

				totalValidQuestions += validQuestions.size(); // Add to total count

				sectionDTOs.add(new CodingDto(
						section.getId(),
						section.getSectionName()
				));
			}

			if (totalValidQuestions < questionLimit) {
				System.out.println("Skipping Test: " + test.getTestName() + " (Total Valid Questions: " + totalValidQuestions + " < Required: " + questionLimit + ")");
				continue;
			}

			BASIC = true; // Basic level is always accessible
			INTERMEDIATE = userTestResultRepository.existsByUserIdAndTestIdAndQuestionLevelAndPassed(studentId, test.getId(), QuestionLevel.BASIC, true);
			ADVANCED = userTestResultRepository.existsByUserIdAndTestIdAndQuestionLevelAndPassed(studentId, test.getId(), QuestionLevel.INTERMEDIATE, true);

			System.out.println("Section Levels - BASIC: " + BASIC + ", INTERMEDIATE: " + INTERMEDIATE + ", ADVANCED: " + ADVANCED);

			// **Fetch attempt history for this test**
			List<UserTestResult> results = userTestResultRepository.findByUserIdAndTestId(studentId, test.getId());
			for (UserTestResult result : results) {
				attemptHistory.add(new AttemptDTO(
						result.getQuestionLevel().name(),
						result.getScore(),
						result.getOutOfMarks(),
						result.getCreatedAt(),
						result.getStatus()
				));
			}

			System.out.println("Test Included: " + test.getTestName());

			CodingLanguageDTO languageDTO = new CodingLanguageDTO();
			languageDTO.setId(test.getId());
			languageDTO.setName(test.getTestName());
			languageDTO.setSections(sectionDTOs);
			languageDTO.setDescription(test.getDescription());
			languageDTO.setBasic(BASIC);
			languageDTO.setIntermediate(INTERMEDIATE);
			languageDTO.setAdvanced(ADVANCED);
			languageDTO.setAttempts(attemptHistory);
			languageDTO.setPassingMarks(test.getPassingMarks());
			responseList.add(languageDTO);
		}

		System.out.println("Final Response List Size: " + responseList.size());
		return responseList;

	}



	// ALL TEST
	@GetMapping("/all-test")
	public ResponseEntity<List<Map<String, Object>>> getAllTests() {
		List<Test> tests = testService.getAllTests();

		List<Map<String, Object>> responseList = tests.stream().map(test -> {
			Map<String, Object> response = new HashMap<>();
			response.put("id", test.getId());
			response.put("testName", test.getTestName());
			response.put("time", test.getTimeLimit());
			response.put("passingMarks",test.getPassingMarks());
			response.put("marksPerQuestion",test.getMarksPerQuestion());
            response.put("timeLimit",test.getTimeLimit());
            response.put("noOfQuestions",test.getNoOfQuestion());
			response.put("sections", test.getSections());
			response.put("status", test.isStatus());
			response.put("description",test.getDescription());
			return response;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(responseList);
	}

	// TEST-BY -ID
//	@GetMapping("/test/{testId}")
//	public ResponseEntity<Map<String, Object>> getTestById(@PathVariable Long testId) {
//		Test test = testService.getTestById(testId);
//
//
//		int totalMarks = test.getSections().stream().mapToInt(Section::getSectionMarks).sum();
//
//		Map<String, Object> response = new HashMap<>();
//		response.put("id", test.getId());
//		response.put("testName", test.getTestName());
//		response.put("totalMarks", totalMarks);
//		response.put("sections", test.getSections());
//
//		return ResponseEntity.ok(response);
//	}

	// test
	@PutMapping("/test/{testId}/status/{status}")
	public ResponseEntity<?> changeTestStatus(@PathVariable Long testId, @PathVariable boolean status) {
		Test test = testRepository.findById(testId)
				.orElseThrow(() -> new RuntimeException("Test not found"));

		test.setStatus(status);
		testRepository.save(test);

		return ResponseEntity.ok("Test " + (status ? "activated" : "deactivated") + " successfully!");
	}


	// update test /21/03 - shrunkhal
	@PutMapping("/updateTest/{testId}")
	public ResponseEntity<?> updateTest(@PathVariable Long testId, @RequestBody Test updatedTest) {
		Test existingTest = testRepository.findById(testId)
				.orElseThrow(() -> new RuntimeException("Test not found"));

		existingTest.setTestName(updatedTest.getTestName());
		existingTest.setMarksPerQuestion(updatedTest.getMarksPerQuestion());
		existingTest.setPassingMarks(updatedTest.getPassingMarks());
		existingTest.setTimeLimit(updatedTest.getTimeLimit());
		existingTest.setNoOfQuestion(updatedTest.getNoOfQuestion());
		existingTest.setUpdatedAt(LocalDateTime.now());
		existingTest.setDescription(updatedTest.getDescription());
		testRepository.save(existingTest);
		return ResponseEntity.ok(existingTest);
	}


	// UPLOAD EXCEL SHEET
	@PostMapping("/upload/{testId}/{sectionId}")
	public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file,
											  @PathVariable Long testId,
											  @PathVariable Long sectionId) {
		try {
			String response = testService.saveQuestionsFromExcel(file, testId, sectionId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error processing file: " + e.getMessage());
		}
	}


	//check test level status by user and test 12/03/25
	@GetMapping("/unlock/{userId}/{testId}")
	public ResponseEntity<?> checkUserTestEligibility(@PathVariable Long userId,@PathVariable Long testId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));
		//boolean basicCleared = userTestResultRepository.existsByUserIdAndTest_IdAndTest_TestLevelAndPassed(userId,testId, TestLevel.BASIC, true);
		//boolean intermediateCleared = userTestResultRepository.existsByUserIdAndTest_IdAndTest_TestLevelAndPassed(userId,testId, TestLevel.INTERMEDIATE, true);


		Map<String, Object> unlockStatus = new HashMap<>();
        unlockStatus.put("testName", test.getTestName());

        unlockStatus.put("BASIC", true); // BASIC is always available
//		unlockStatus.put("INTERMEDIATE", basicCleared);
//		unlockStatus.put("ADVANCED", intermediateCleared);

		return ResponseEntity.ok(unlockStatus);
	}

	// old test
	@PostMapping("/createTest")
	public ResponseEntity<Object> creatTest(@RequestBody AssessmentTest assessmentTest) {
		HashMap<String, Object> response = new HashMap<>();

		AssessmentTest createExam = examService.createExam(assessmentTest);

		response.put("msg", "Success");
		response.put("test", createExam);

		return new ResponseEntity<Object>(response, HttpStatus.CREATED);

	}

	//old test question add
	@PostMapping("/addQues")
	public ResponseEntity<Object> addQuestions(@RequestBody Questions questions, @RequestParam Long testId) {
		HashMap<String, Object> response = new HashMap<>();

		questions.setExamId(testId);
		Questions addQuestions = questionService.addQuestions(questions, testId);

		response.put("msg", "Success");
		response.put("Question", addQuestions);

		return new ResponseEntity<Object>(response, HttpStatus.CREATED);
	}

	// update question 12/03/25
	@PutMapping("/questionUpdate/{questionId}")
	public ResponseEntity<?> updateQuestion(@PathVariable Long questionId, @RequestBody Question updatedQuestion) {
		Question existingQuestion = questionRepo.findById(questionId)
				.orElseThrow(() -> new RuntimeException("Question not found"));

		existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
		existingQuestion.setOptionA(updatedQuestion.getOptionA());
		existingQuestion.setOptionB(updatedQuestion.getOptionB());
		existingQuestion.setOptionC(updatedQuestion.getOptionC());
		existingQuestion.setOptionD(updatedQuestion.getOptionD());
		existingQuestion.setMarks(updatedQuestion.getMarks());
		existingQuestion.setQuestionType(updatedQuestion.getQuestionType());

		// Handling correct answers
		if (updatedQuestion.getQuestionType() == QuestionType.SINGLE_ANSWER) {
			existingQuestion.setAnswer(updatedQuestion.getAnswer());
		} else {
			existingQuestion.setCorrectAnswers(updatedQuestion.getCorrectAnswers());
		}

		questionRepo.save(existingQuestion);
		return ResponseEntity.ok(Map.of("msg","Question updated successfully!",
				"question",existingQuestion));
	}

	@GetMapping("/getTest")
	public ResponseEntity<Object> getQuestions() {
		HashMap<String, Object> response = new HashMap<>();
		List<AssessmentTest> allTest = examService.getAllTest();

		if (allTest != null && !allTest.isEmpty()) {
			response.put("msg", "Success");
			response.put("Test", allTest);
		} else {

			response.put("msg", "Not found");

		}
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@DeleteMapping("/ques/{id}")
	public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
		Optional<Questions> questionOptional = questionService.getQuestionsById(id);

		if (questionOptional.isPresent()) {
			questionService.deleteQuesById(id);
			return ResponseEntity.ok("Question deleted successfully");
		} else {
			return ResponseEntity.status(404).body("Question not found");
		}
	}

	@PutMapping("/editQuestion")
	public ResponseEntity<String> editQuestions(@RequestParam("id") Long id, @RequestBody Questions question) {
		return questionService.getQuestionsById(id).map(questions -> {
			questions.setText(question.getText());
			questions.setOption1(question.getOption1());
			questions.setOption2(question.getOption2());
			questions.setOption3(question.getOption3());
			questions.setOption4(question.getOption4());
			questions.setCorrectAnswer(question.getCorrectAnswer());
			questions.setDifficultyLevel(question.getDifficultyLevel());
			questions.setType(question.getType());
			questionService.updateQuestion(questions);
			return ResponseEntity.ok("question updated successfully");

		}).orElseGet(() -> ResponseEntity.status(404).body("question not found"));

	}

	@GetMapping("/getQuestions")
	public ResponseEntity<Object> getAllQuestions() {
		HashMap<String, Object> response = new HashMap<>();

		List<Object[]> allQuestions = questionService.getAllQuestions();
		if (allQuestions != null && !allQuestions.isEmpty()) {
			response.put("msg", "Success");
			response.put("Questions", allQuestions);
		} else {
			response.put("msg", "Not found");
		}
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@PatchMapping("/activate")
	public ResponseEntity<String> activateTest(@RequestParam Long testId) {
		try {
			boolean newStatus = examService.activateTest(testId);

			String message = newStatus ? "Test activated successfully." : "Test deactivated successfully.";
			return ResponseEntity.ok(message);
		} catch (RuntimeException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + ex.getMessage());
		}
	}

	@DeleteMapping("/deleteTest")
	public ResponseEntity<Object> deleteTest(@RequestParam Long testId) {
		HashMap<String, Object> response = new HashMap<>();

		AssessmentTest test = examService.getExmById(testId);

		if (test != null) {
			examService.deleteTest(testId);
			response.put("msg", "deleted successfully");
		} else {
			response.put("msg", "Not found");

		}
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@GetMapping("/{testId}/questions")
	public ResponseEntity<List<Questions>> getQuestionsByTestId(@PathVariable Long testId) {
		List<Questions> questions = questionService.getQuestionByTestId(testId);
		if (questions.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(questions);
	}

	@PutMapping("/editTest")
	@Transactional
	public ResponseEntity<Object> editTest(@RequestParam Long testId, @RequestBody AssessmentTest assessmentTest) {
		HashMap<String, Object> response = new HashMap<>();

		try {
			AssessmentTest test = examService.getExmById(testId);
			if (test == null) {
				response.put("status", "error");
				response.put("message", "Test not found");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			test.setTestName(assessmentTest.getTestName());
			test.setTime(assessmentTest.getTime());
			test.setCut_off(assessmentTest.getCut_off());
			test.setTotal_mark(assessmentTest.getTotal_mark());
			test.setTotal_que(assessmentTest.getTotal_que());
			test.setUpdation_date(new Date());
			test.setStatus(assessmentTest.isStatus());

			examService.createExam(test);

			response.put("status", "success");
			response.put("message", "Test updated successfully");
			response.put("data", test);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/upload")
	public ResponseEntity<Object> uploadQuestions(@RequestParam("file") MultipartFile file, @RequestParam Long testId) {
		try (InputStream inputStream = file.getInputStream()) {
			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			// Skip the header row
			if (rowIterator.hasNext()) {
				rowIterator.next();
			}

			List<Questions> questionsList = new ArrayList<>();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Questions question = new Questions();

				// Handle each cell and check for null values
				Cell cell;

				cell = row.getCell(0);
				String text = (cell == null) ? "" : getCellValueAsString(cell);
				if (text.isEmpty()) {
					continue; // Skip row if text is empty
				}
				question.setText(text);

				cell = row.getCell(1);
				String option1 = (cell == null) ? "" : getCellValueAsString(cell);
				question.setOption1(option1);

				cell = row.getCell(2);
				String option2 = (cell == null) ? "" : getCellValueAsString(cell);
				question.setOption2(option2);

				cell = row.getCell(3);
				String option3 = (cell == null) ? "" : getCellValueAsString(cell);
				question.setOption3(option3);

				cell = row.getCell(4);
				String option4 = (cell == null) ? "" : getCellValueAsString(cell);
				question.setOption4(option4);

				cell = row.getCell(5);
				String correctAnswer = (cell == null) ? "" : getCellValueAsString(cell);
				question.setCorrectAnswer(correctAnswer);
//
//	             cell = row.getCell(6);
//	             List<String> correctAnswers = (cell == null) ? new ArrayList<>() : Arrays.asList(getCellValueAsString(cell).split(","));
//	             question.setCorrectAnswers(correctAnswers);

				cell = row.getCell(7);
				int marksPerQues = (cell == null) ? 0 : (int) getNumericCellValue(cell);
				question.setMarksques(marksPerQues);

				cell = row.getCell(8);
				String type = (cell == null) ? "" : getCellValueAsString(cell);
				question.setType(QuestionType.valueOf(type));

				if (type.equals("MULTIPLE_CHOICE")) {
					cell = row.getCell(7); // Assuming correct answers are in column 7
					List<String> correctAnswers = (cell == null || getCellValueAsString(cell).isEmpty())
							? new ArrayList<>()
							: Arrays.asList(getCellValueAsString(cell).split(","));

					if (!correctAnswers.isEmpty()) {
						question.setCorrectAnswers(correctAnswers); // Only set for multiple choice
					}
				} else {
					question.setCorrectAnswers(new ArrayList<>()); // Clear correctAnswers if not multiple choice
				}

				cell = row.getCell(9);
				String difficultyLevel = (cell == null) ? "" : getCellValueAsString(cell);
				question.setDifficultyLevel(difficultyLevel);

				// Set testId or other fields if necessary
				question.setExamId(testId);

				questionsList.add(question);
			}

			questionService.addQuestions(questionsList, testId);

			return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded and questions added successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the file");
		}
	}

	private String getCellValueAsString(Cell cell) {
		if (cell == null) {
			return "";
		}
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			// Handle both integer and floating-point values
			if (DateUtil.isCellDateFormatted(cell)) {
				return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
			}
			double numericValue = cell.getNumericCellValue();
			if (numericValue == (int) numericValue) {
				return String.valueOf((int) numericValue); // Integer values
			} else {
				return String.valueOf(numericValue); // Floating-point values
			}
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			try {
				return cell.getStringCellValue(); // For formulas returning strings
			} catch (IllegalStateException e) {
				return String.valueOf(cell.getNumericCellValue()); // For formulas returning numbers
			}
		case BLANK:
			return "";
		default:
			return "";
		}
	}

	private double getNumericCellValue(Cell cell) {
		if (cell == null || cell.getCellType() != CellType.NUMERIC) {
			return 0;
		}
		return cell.getNumericCellValue();
	}

}
