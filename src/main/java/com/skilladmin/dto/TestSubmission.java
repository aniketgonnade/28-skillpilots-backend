package com.skilladmin.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class TestSubmission {

    private Long userId;
    private Long testId;
    private String status;
    private String level;
    private List<Map<String, Object>> answers;
}


