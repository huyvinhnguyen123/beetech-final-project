package com.beetech.finalproject.web.response;

import com.beetech.finalproject.web.dtos.order.OrderRetrieveSearchDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSearchResponse {
    private List<OrderRetrieveSearchDto> orderRetrieveSearchDtos;
}
