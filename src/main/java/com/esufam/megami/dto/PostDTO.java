package com.esufam.megami.dto;

public record PostDTO(String title, String filename, char status) {
    public boolean missingCreateInfo() {
        return title == null || filename == null;
    }
}
