package com.esufam.megami.dto;

public record UserDTO (String username, String email, String password) {
    public boolean missingLoginInfo() {
        return username == null || password == null;
    }
    public boolean missingRegisterInfo() {
        return username == null || email == null || password == null;
    }
}