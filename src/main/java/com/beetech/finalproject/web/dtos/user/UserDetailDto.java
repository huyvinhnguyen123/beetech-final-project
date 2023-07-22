package com.beetech.finalproject.web.dtos.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDetailDto {
    private String userId;
    private String username;
    private LocalDate birthDay;
}
