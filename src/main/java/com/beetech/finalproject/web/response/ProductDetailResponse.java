package com.beetech.finalproject.web.response;

import com.beetech.finalproject.web.dtos.product.ProductRetrieveSearchDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {
    private List<ProductRetrieveSearchDetailDto> productRetrieveSearchDetailDtos;
}
