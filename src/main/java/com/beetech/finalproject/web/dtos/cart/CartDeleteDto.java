package com.beetech.finalproject.web.dtos.cart;

import lombok.Data;

@Data
public class CartDeleteDto {
    private String token;
    private int clearCart;
    private Long detailId;
    private Double versionNo;
}
