package com.skilladmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestAttemptDTO {
    private int obtained;
    private int total;
    private String date;
}