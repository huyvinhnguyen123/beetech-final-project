package com.beetech.finalproject.domain.enums;

public enum Roles {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String role;

    Roles(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
