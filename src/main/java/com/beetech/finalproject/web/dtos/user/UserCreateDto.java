package com.beetech.finalproject.web.dtos.user;

import com.beetech.finalproject.validate.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreateDto {
    @NotNull(message = "{User.username.notNull}")
    @NotEmpty(message = "{User.username.notEmpty}")
    private String username;

    @NotNull(message = "{User.birthDay.notNull}")
    @NotEmpty(message = "{User.birthDay.notEmpty}")
    private String birthDay;

    @NotNull(message = "{User.loginId.notNull}")
    @NotEmpty(message = "{User.loginId.notEmpty}")
    @Email
    private String loginId; // loginId = email

    @NotNull(message = "{User.password.notNull}")
    @NotEmpty(message = "{User.password.notEmpty}")
    @ValidPassword
    private String password;
}
