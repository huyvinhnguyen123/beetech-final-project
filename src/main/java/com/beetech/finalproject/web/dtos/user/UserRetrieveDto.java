package com.beetech.finalproject.web.dtos.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRetrieveDto {
    private String userId;
    private String username;
    private String loginId;
    private LocalDate birthDay;
    private Double totalPrice;
}
