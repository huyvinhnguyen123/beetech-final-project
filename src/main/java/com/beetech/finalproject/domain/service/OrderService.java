package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.repository.OrderDetailRepository;
import com.beetech.finalproject.domain.repository.OrderRepository;
import com.beetech.finalproject.domain.repository.OrderShippingDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderShippingDetailRepository orderShippingDetailRepository;
}
