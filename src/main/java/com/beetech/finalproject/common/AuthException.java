package com.beetech.finalproject.common;

public class AuthException extends RuntimeException {
    public enum ErrorStatus {
        INVALID_ACCESS_TOKEN,
        EXPIRED_ACCESS_TOKEN,
        EXPIRED_REFRESH_TOKEN,
        USER_DO_NOT_HAVE_PERMISSION,
        SOURCE_IP_ADDRESS_LIMIT,
        INVALID_GRANT;
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
    public AuthException(final ErrorStatus errorStatus) {
        super("401 Unauthorized Exception");
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
