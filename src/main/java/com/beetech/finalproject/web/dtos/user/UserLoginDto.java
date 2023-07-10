package com.beetech.finalproject.web.dtos.user;

import com.beetech.finalproject.validate.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserLoginDto {
    @NotNull(message = "{User.loginId.notNull}")
    @NotEmpty(message = "{User.loginId.notEmpty}")
    @Email
    private String loginId; // loginId = email

    @NotNull(message = "{User.password.notNull}")
    @NotEmpty(message = "{User.password.notEmpty}")
    @ValidPassword
    private String password;
}
