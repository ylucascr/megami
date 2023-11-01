package com.esufam.megami.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostPostDTO {
    private String title;
    private String description;
    private MultipartFile file;
}
