package com.beetech.finalproject.web.dtos.cart;

import lombok.Data;

@Data
public class CartUpdateDto {
    private String authenticationToken;
    private String cartToken;
    private Long cartDetailId;
    private int quantity;
    private Double versionNo;
}
