package com.beetech.finalproject.common;

public class AccountException extends RuntimeException {
    public enum ErrorStatus {
        LOCKED_ACCOUNT,
        DELETED_ACCOUNT,
        ALREADY_REGISTERED
    }

    /**
     * Error status code
     */
    private final ErrorStatus status;

    /**
     * Constructor
     *
     * @param errorStatus ErrorStatus
     */
    public AccountException(final ErrorStatus errorStatus, String message) {
        super(message);
        this.status = errorStatus;
    }

    /**
     * Get status code in lower case;
     *
     * @return status code
     */
    public String getStatusCode() {
        return this.status.toString().toLowerCase();
    }

    /**
     * Message Key
     *
     * @return message key
     */
    public String getMessageKey() {
        return this.status.toString().toLowerCase();
    }
}
