package com.beetech.finalproject.web.dtos.order;

import lombok.Data;

@Data
public class OrderCreateDto {
    private String phoneNumber;
    private String address;
    private String district;
    private String city;
    private Double versionNo;
}
