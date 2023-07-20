package com.beetech.finalproject.web.dtos.cart;

import lombok.Data;

@Data
public class TokenInputDto {
    private String authenticationToken;
    private String cartToken;
}
