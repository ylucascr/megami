package com.esufam.megami.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;
    private boolean followed;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
