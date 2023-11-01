package com.esufam.megami.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterDTO {
    private String username;
    private String password;
    private String question;
    private String answer;
}