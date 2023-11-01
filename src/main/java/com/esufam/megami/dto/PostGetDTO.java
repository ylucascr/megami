package com.esufam.megami.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostGetDTO {
    private String title;
    private String description;
    private String filename;
    private Integer userId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
