package com.skilladmin.dto;


import lombok.Data;

import java.util.List;

@Data
    public class TestSubmissionDTO {
        private Long testId;
        private Long sectionId;
        private Long studentId;
        private List<AnswerDTO> answers;
        private boolean disqualified;
    }



