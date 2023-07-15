package com.beetech.finalproject.web.dtos.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductCreateDto {
    private String sku;
    private String productName;
    private String detailInfo;
    private Double price;
    private Long categoryId;
    private MultipartFile thumbnailImage;
    private List<MultipartFile> detailImages;
}
