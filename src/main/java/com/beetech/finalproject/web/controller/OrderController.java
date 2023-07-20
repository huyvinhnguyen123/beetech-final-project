package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.common.AuthException;
import com.beetech.finalproject.domain.service.OrderService;
import com.beetech.finalproject.web.common.ResponseDto;
import com.beetech.finalproject.web.dtos.cart.CartCreateDto;
import com.beetech.finalproject.web.dtos.cart.CartRetrieveCreateDto;
import com.beetech.finalproject.web.dtos.order.OrderCreateDto;
import com.beetech.finalproject.web.dtos.order.OrderRetrieveCreateDto;
import com.beetech.finalproject.web.response.CartResponseCreate;
import com.beetech.finalproject.web.response.OrderResponseCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create-order")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDto<Object>> createOrderFromCart(@RequestBody OrderCreateDto orderCreateDto) {
        log.info("request creating order");

        try {
            OrderRetrieveCreateDto orderRetrieveCreateDto = orderService.createOrderFromCart(orderCreateDto);
            OrderResponseCreate orderResponse = OrderResponseCreate.builder()
                    .orderRetrieveCreateDto(orderRetrieveCreateDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(orderResponse));
        } catch (AuthenticationException e) {
            log.error("Create order failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }
}
