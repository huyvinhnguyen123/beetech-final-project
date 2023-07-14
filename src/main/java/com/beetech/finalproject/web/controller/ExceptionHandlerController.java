package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.common.AccountException;
import com.beetech.finalproject.common.ValidationException;
import com.beetech.finalproject.web.common.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    /**
     * Handle for validate exception, return error message
     * Example for return response from BindingResult
     *
     * @param ex exception
     * @return response
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(ValidationException ex) {
        log.error("ValidationException: ", ex);
        BindingResult bindingResult = ex.getBindingResult();

        ErrorResponseDto re = new ErrorResponseDto("400",
                ex.getMessage(),
                "Invalid input");

        List<ErrorResponseDto> details = new ArrayList<>();
        bindingResult.getAllErrors().forEach((objectError -> {
            ErrorResponseDto newDetail;
            if (objectError instanceof FieldError fieldError) {
                newDetail = new ErrorResponseDto(fieldError.getCode(), fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                newDetail = new ErrorResponseDto(objectError.getCode(), "", objectError.getDefaultMessage());
            }

            details.add(newDetail);
        }));
        re.withDetails(details);
        return new ResponseEntity<>(re, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle for exception relate to Account
     *
     * @param ex exception
     * @return response
     */
    @ExceptionHandler(AccountException.class)
    public ResponseEntity handleAccountException(AccountException ex) {
        log.error("AccountException: ", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Final chain to handle exception.
     *
     * @param ex exception
     * @return response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity customHandleException(Exception ex) {
        log.error("Exception: ", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
