package com.beetech.finalproject.web.dtos.order;

import lombok.Data;

@Data
public class OrderDetailRetrieveDto {
    private Long orderDetailId;
    private Long productId;
    private String productName;
    private String imagePath;
    private String imageName;
    private int quantity;
    private Double price;
    private Double totalPrice;
}
