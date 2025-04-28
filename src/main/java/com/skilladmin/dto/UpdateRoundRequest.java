package com.skilladmin.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;
@Data
public class UpdateRoundRequest {

    private List<Long> studentIds;
    private Long recruitmentId;
    private String round;
    private String roundTime;
    private String roundDate;
    private Long driveId;
    private List<Long> id;
    @Column(length = 600)
    private String meetLink;

}
