package com.beetech.finalproject.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity customHandleException(Exception ex) {
        log.error("Exception: ", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
