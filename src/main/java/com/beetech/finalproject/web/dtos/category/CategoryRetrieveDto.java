package com.beetech.finalproject.web.dtos.category;

import com.beetech.finalproject.domain.entities.CategoryImage;
import com.beetech.finalproject.domain.entities.ImageForCategory;
import lombok.Data;

import java.util.List;

@Data
public class CategoryRetrieveDto {
    private Long categoryId;
    private String categoryName;
//    private List<CategoryImage> categoryImages;
    private List<ImageForCategory> imageForCategories;
}
