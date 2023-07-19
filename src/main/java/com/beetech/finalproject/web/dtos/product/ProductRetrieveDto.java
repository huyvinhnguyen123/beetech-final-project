package com.beetech.finalproject.web.dtos.product;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
public class ProductRetrieveDto {
    private Long productId;
    private String productName;
    private String sku;
    private String detailInfo;
    private Double price;
    private String name;
    private String path;
    private Long totalPage;
}
