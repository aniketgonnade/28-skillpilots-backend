package com.skilladmin.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.AssessmentTest;
import com.skilladmin.model.Questions;
import com.skilladmin.repository.QuestionRepository;
import com.skilladmin.service.ExamService;
import com.skilladmin.service.QuestionService;
@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private ExamService examService;
	@Override
	public Questions addQuestions(Questions questions, Long testId) {
		
		AssessmentTest test = examService.getExmById(testId);
		questions.setAssessmentTest(test);
		return questionRepository.save(questions);
	}
	@Override
	public Optional<Questions> getQuestionsById(Long id) {
		return questionRepository.findById(id);
	}
	
	@Override
	public void deleteQuesById(Long id) {
		questionRepository.deleteById(id);
	}
	@Override
	public Questions updateQuestion(Questions questions) {
		return questionRepository.save(questions);
	}
	@Override
	public List<Object[]> getAllQuestions() {
		return questionRepository.getQuestions();}
	@Override
	public List<Questions> getQuestionByTestId(Long testId) {
		return questionRepository.findByAssessmentTestTesId(testId);
	}
	@Override
	public List<Questions> addQuestions(List<Questions> questions, Long testId) {
        // Find the test by its ID
        AssessmentTest test = examService.getExmById(testId);

        // Set the test for each question and save it
        for (Questions question : questions) {
            question.setAssessmentTest(test);
            questionRepository.save(question);
        }

        return questions;
    }
}
