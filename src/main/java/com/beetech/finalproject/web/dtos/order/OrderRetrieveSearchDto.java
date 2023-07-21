package com.beetech.finalproject.web.dtos.order;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRetrieveSearchDto {
    private Long orderId;
    private int displayId;
    private String username;
    private Double totalPrice;
    private LocalDate orderDate;
    private String orderStatus;
    private String shippingAddress;
    private String shippingDistrict;
    private String shippingCity;
    private String shippingPhoneNumber;
    private List<OrderDetailRetrieveDto> orderDetailRetrieveDtos;
}
