package com.beetech.finalproject.common;

import org.springframework.validation.BindingResult;

import java.io.Serial;

public class ValidationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6785420639768008206L;
    private BindingResult bindingResult;
    private String detailMessage;

    public ValidationException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }

    public ValidationException(BindingResult bindingResult) {
        super();
        this.bindingResult = bindingResult;
    }

    public ValidationException(String message, String detailMessage) {
        super(message);
        this.detailMessage = detailMessage;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
}
