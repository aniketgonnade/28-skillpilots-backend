package com.skilladmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LevelDTO {
    private String level;
    private List<TestAttemptDTO> history;
}
