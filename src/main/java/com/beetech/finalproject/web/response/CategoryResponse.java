package com.beetech.finalproject.web.response;

import com.beetech.finalproject.web.dtos.category.CategoryRetrieveDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private List<CategoryRetrieveDto> categoryRetrieveDtos;
}
