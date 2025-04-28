package com.skilladmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionDTO {

    private Long id;
    private String name;
    private boolean BASIC;
    private boolean INTERMEDIATE;
    private boolean ADVANCED;
}
