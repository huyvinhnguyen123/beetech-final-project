package com.beetech.finalproject.web.dtos.cart;

import lombok.Data;

@Data
public class CartCreateDto {
    private String token;
    private Long productId;
    private int quantity;
}
