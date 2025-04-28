package com.skilladmin.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.skilladmin.dto.TestDTO;
import com.skilladmin.dto.UserTestAttemptDTO;
import com.skilladmin.dto.UserTestAttemptDTO2;
import com.skilladmin.enumclass.QuestionLevel;
import com.skilladmin.model.Question;
import com.skilladmin.model.Section;
import com.skilladmin.model.Test;
import com.skilladmin.model.UserTestPreference;
import org.springframework.web.multipart.MultipartFile;

public interface TestService {

	  public Test createTest(Test test);
	  public Section addSectionToTest(Long testId, Section section);
	  public Question addQuestionToSection(Long sectionId,Long testId, Question question);
	  public List<Test> getAllTests() ;
	  public Test getTestById(Long testId);
	  public String saveQuestionsFromExcel(MultipartFile file, Long testId, Long sectionId) throws IOException;

	public TestDTO getTestWithSectionsAndQuestions(Long testId, QuestionLevel level);
	  public UserTestPreference addUserPreference(UserTestPreference userTestPreference);

	public List<UserTestAttemptDTO2> getUserTestAttempts();
}
