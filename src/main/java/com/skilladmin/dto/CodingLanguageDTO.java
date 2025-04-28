package com.skilladmin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodingLanguageDTO {

    private Long id;
    private String name;
    private String description;
    private List<CodingDto> sections;
    private boolean basic;
    private boolean intermediate;
    private boolean advanced;

    private List<AttemptDTO> attempts;
    private int passingMarks;

}
