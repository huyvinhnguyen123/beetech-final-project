package com.beetech.finalproject.exception;

public class DuplicateException extends RuntimeException{
    private final String errorStatus;

    public DuplicateException(final String errorStatus) {
        super("400 Duplicate Exception");
        this.errorStatus = errorStatus;
    }

    public String getStatusCode() {
        return this.errorStatus.toString().toLowerCase();
    }
}
