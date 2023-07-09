package com.beetech.finalproject.domain.enums;

public enum Roles {
    ADMIN(1, "ROLE_ADMIN"),
    USER(2, "ROLE_USER");

    private int code;
    private String role;

    Roles(int code, String role){
        this.code = code;
        this.role = role;
    }

    public int getCode() {
        return code;
    }

    public String getRole() {
        return role;
    }
}
