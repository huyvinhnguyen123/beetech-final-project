package com.beetech.finalproject.web.dtos.cart;

import lombok.Data;

@Data
public class CartRetrieveCreateDto {
    private String token;
    private Double totalPrice;
    private Double versionNo;
}
