package com.beetech.finalproject.web.dtos.product;

import java.util.List;

public interface IProductRetrieveDto {
    Long getProductId();
    String getProductName();
    String getSku();
    String getDetailInfo();
    Double getPrice();
    List<ImageRetrieveDto> getImageRetrieveDtos();
    Long getTotalItems();
}
