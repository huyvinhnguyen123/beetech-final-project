package com.beetech.finalproject.web.dtos.product;

import lombok.Data;

import java.util.List;

@Data
public class ProductRetrieveSearchDetailDto {
    private Long productId;
    private String sku;
    private String productName;
    private String detailInfo;
    private Double price;
    private String name;
    private String path;
    private List<ImageRetrieveDto> detailImages;
}
