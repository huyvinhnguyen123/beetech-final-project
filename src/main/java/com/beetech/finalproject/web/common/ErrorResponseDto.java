package com.beetech.finalproject.web.common;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample return error message that can be used to FE to display error on a target
 */
@Data
@Builder
public class ErrorResponseDto {
    private final String statusCode;
    private final String code;
    private final String message;
    private final String target;
    private final List<ErrorResponseDto> details = new ArrayList<>();

    public ErrorResponseDto(String statusCode, String code, String message) {
        this(statusCode, code, message, null);
    }

    public ErrorResponseDto(String statusCode, String code, String message, String target) {
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
        this.target = target;
    }

    public void withDetails(List<ErrorResponseDto> details) {
        this.details.clear();
        this.details.addAll(details);
    }
}
