package com.beetech.finalproject.web.dtos.product;

import lombok.Data;

@Data
public class ProductSearchInputDto {
    private Long categoryId;
    private String searchKey;
}
