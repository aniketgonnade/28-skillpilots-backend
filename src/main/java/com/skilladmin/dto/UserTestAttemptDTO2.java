package com.skilladmin.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserTestAttemptDTO2 {

    private Long userId;
    private String username;
    @JsonManagedReference
    private List<UserTestAttemptDTO> attempts; // This must exist

}
