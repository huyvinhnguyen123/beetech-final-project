package com.beetech.finalproject.web.dtos.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserSearchDto {
    private String username;
    private String loginId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalPrice;
}
