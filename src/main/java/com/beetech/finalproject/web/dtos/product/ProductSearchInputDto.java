package com.beetech.finalproject.web.dtos.product;

import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class ProductSearchInputDto {
    private Long categoryId;
    private String sku;
    private String productName;
}
