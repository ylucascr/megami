package com.esufam.megami.dto;

public record UserDTO (String username, String email, String password) {
    public boolean isMissingData() {
        return username == null || email == null || password == null;
    }
}