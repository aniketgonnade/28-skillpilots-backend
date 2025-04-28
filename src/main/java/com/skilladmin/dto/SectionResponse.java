package com.skilladmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionResponse  {

    private Long sectionId;
    private String sectionName;
    private List<QuestionResponse> questions;

}
