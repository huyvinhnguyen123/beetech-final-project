package com.beetech.finalproject.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidate.class)
public @interface ValidPassword {
    String message() default "{ValidPassword}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

