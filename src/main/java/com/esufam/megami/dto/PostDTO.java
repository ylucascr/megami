package com.esufam.megami.dto;

public record PostDTO(String title, String filename, Integer userId, char status) {
    public boolean isMissingData() {
        return title == null || filename == null || userId == null;
    }
}
