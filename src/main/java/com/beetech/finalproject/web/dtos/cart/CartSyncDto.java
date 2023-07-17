package com.beetech.finalproject.web.dtos.cart;

import lombok.Data;

@Data
public class CartSyncDto {
    private String authenticationToken;
    private String cartToken;
}
