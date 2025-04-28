package com.skilladmin.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class    CourseJourney {

    private String phaseITitle;
    private String phaseIDesc1;
    private String phaseIITitle;
    private String phaseIIDesc2;
    private String phaseIIITitle;
    private String phaseIIIDesc3;
    private String phaseIVTitle;
    private String phaseIVDesc4;
}
