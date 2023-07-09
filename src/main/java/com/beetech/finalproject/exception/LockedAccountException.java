package com.beetech.finalproject.exception;

public class LockedAccountException extends RuntimeException {
    public LockedAccountException(String message) {
        super(message);
    }
}
