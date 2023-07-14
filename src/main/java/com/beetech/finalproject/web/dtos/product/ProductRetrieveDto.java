package com.beetech.finalproject.web.dtos.product;

import lombok.Data;

import java.util.List;

@Data
public class ProductRetrieveDto {
    private Long productId;
    private String productName;
    private String sku;
    private String detailInfo;
    private Double price;
    private List<ImageRetrieveDto> imageRetrieveDtos;
}
