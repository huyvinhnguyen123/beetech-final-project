package com.beetech.finalproject.web.dtos.order;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderSearchInputDto {
    private String userId;
    private String username;
    private Long orderId;
    private LocalDate orderDate;
    private String sku;
    private String productName;
    private int statusCode;
}
