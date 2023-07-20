package com.beetech.finalproject.web.dtos.cart;

import com.beetech.finalproject.domain.entities.CartDetail;
import lombok.Data;

import java.util.List;

@Data
public class CartRetrieveDto {
    private Long cartId;
    private Double totalPrice;
    private Double versionNo;
    private List<CartDetailRetrieveDto> cartDetailRetrieveDtos;
}
