package com.beetech.finalproject.web.dtos.cart;

import lombok.Data;

@Data
public class CartDetailRetrieveDto {
    private Long cartDetailId;
    private Long productId;
    private String productName;
    private String imagePath;
    private String imageName;
    private int quantity;
    private Double price;
    private Double totalPrice;
    private String status;
}
