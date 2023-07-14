package com.beetech.finalproject.web.dtos.category;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class CategoryUpdateDto {
    private String categoryName;
    private MultipartFile image;
}
