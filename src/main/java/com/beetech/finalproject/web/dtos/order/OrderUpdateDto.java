package com.beetech.finalproject.web.dtos.order;

import lombok.Data;

@Data
public class OrderUpdateDto {
    private Long orderId;
    private int displayId;
    private int statusCode;
}
