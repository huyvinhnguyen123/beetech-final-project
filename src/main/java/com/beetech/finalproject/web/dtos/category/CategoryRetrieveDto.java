package com.beetech.finalproject.web.dtos.category;

import lombok.Data;

import java.util.List;

@Data
public class CategoryRetrieveDto {
    private Long categoryId;
    private String categoryName;
    private List<ImageRetrieveDto> imageRetrieveDtos;
}
