package com.beetech.finalproject.web.dtos.user;

import com.beetech.finalproject.validate.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDto {
    @NotNull(message = "User.username.NotNull")
    @Size(max = 255)
    private String username;

    @NotNull(message = "User.birthDay.NotNull")
    private String birthDay;

    @NotNull(message = "User.loginId.NotNull")
    @Size(max = 255)
    @Email
    private String loginId; // loginId = email

    @NotNull(message = "User.password.NotNull")
    @ValidPassword
    private String password;
}
