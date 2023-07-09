package com.beetech.finalproject.web.dtos.user;

import com.beetech.finalproject.validate.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.N;

@Getter
@Setter
public class UserLoginDto {
    @NotNull
    @Email
    private String loginId; // loginId = email

    @NotNull
    @ValidPassword
    private String password;
}
