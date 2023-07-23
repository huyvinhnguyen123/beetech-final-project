package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.common.AuthException;
import com.beetech.finalproject.domain.entities.User;
import com.beetech.finalproject.domain.service.OrderService;
import com.beetech.finalproject.web.common.ResponseDto;
import com.beetech.finalproject.web.dtos.order.*;
import com.beetech.finalproject.web.response.OrderResponseCreate;
import com.beetech.finalproject.web.response.OrderSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create-order")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDto<Object>> createOrderFromCart(@RequestBody OrderCreateDto orderCreateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("request creating order");
        try {
            User currentUser = (User) authentication.getPrincipal();

            OrderRetrieveCreateDto orderRetrieveCreateDto = orderService.createOrderFromCart(orderCreateDto, currentUser);
            OrderResponseCreate orderResponse = OrderResponseCreate.builder()
                    .orderRetrieveCreateDto(orderRetrieveCreateDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(orderResponse));
        } catch (AuthenticationException e) {
            log.error("Create order failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<ResponseDto<Object>> searchOrdersAndPagination(@RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size,
                                                                           @RequestBody
                                                                         OrderSearchInputDto orderSearchInputDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Pageable pageable = PageRequest.of(page, size);
        log.info("request searching orders");
        try {
            User currentUser = (User) authentication.getPrincipal();

            Page<OrderRetrieveSearchDto> orderRetrieveSearchDtoPage = orderService.searchOrdersAndPagination(orderSearchInputDto, pageable, currentUser);
            List<OrderRetrieveSearchDto> orderRetrieveSearchDtos = orderRetrieveSearchDtoPage.getContent();

            // add result inside response
            List<OrderSearchResponse> orderSearchResponses = new ArrayList<>();
            OrderSearchResponse orderSearchResponse =  OrderSearchResponse.builder()
                    .orderRetrieveSearchDtos(orderRetrieveSearchDtos)
                    .build();

            orderSearchResponses.add(orderSearchResponse);

            return ResponseEntity.ok(ResponseDto.build().withData(orderSearchResponses));
        } catch (AuthenticationException e) {
            log.error("Search orders failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @PostMapping("/update-order")
    public ResponseEntity<ResponseDto<Object>> updateOrder(@RequestBody OrderUpdateDto orderUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("request creating order");
        try {
            User currentUser = (User) authentication.getPrincipal();

            OrderRetrieveCreateDto orderRetrieveCreateDto = orderService.updateOrder(orderUpdateDto, currentUser);
            OrderResponseCreate orderResponse = OrderResponseCreate.builder()
                    .orderRetrieveCreateDto(orderRetrieveCreateDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(orderResponse));
        } catch (AuthenticationException e) {
            log.error("Search orders failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }
}
