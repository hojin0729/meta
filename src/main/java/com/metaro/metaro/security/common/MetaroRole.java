package com.metaro.metaro.security.common;

public enum MetaroRole {

    USER("USER"),
    ADMIN("ADMIN"),
    ALL("USER,ADMIN");

    private String role;

    MetaroRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }

}
