package com.esufam.megami.dto;

import lombok.NoArgsConstructor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPatchDTO {
    private String username;
    private String password;
    private String question;
    private String answer;
}