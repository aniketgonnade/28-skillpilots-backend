package com.skilladmin.service;

import java.util.List;
import java.util.Optional;

import com.skilladmin.model.Questions;

public interface QuestionService {
	
	public Questions addQuestions(Questions questions,Long testId);
	
	public Optional<Questions> getQuestionsById(Long id);

	
	public void deleteQuesById(Long id);

	public Questions updateQuestion(Questions questions);

	public List<Object[]> getAllQuestions();
	
	public List<Questions> getQuestionByTestId(Long testId);
    public List<Questions> addQuestions(List<Questions> questions, Long testId) ;

}
