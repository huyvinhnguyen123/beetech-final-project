package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.*;
import com.beetech.finalproject.domain.enums.Status;
import com.beetech.finalproject.domain.repository.*;
import com.beetech.finalproject.utils.CustomDateTimeFormatter;
import com.beetech.finalproject.web.dtos.order.*;
import com.beetech.finalproject.web.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderShippingDetailRepository orderShippingDetailRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final JwtUtils jwtUtils;

    /**
     * Extract login id(email) from token when user authentication
     *
     * @param token - token from authentication
     * @return - user
     */
    private User extractUserFromToken(String token) {
        String loginId = jwtUtils.extractUsername(token);
        User user = userRepository.findByLoginId(loginId);
        log.info("Extract user from token success");
        return user;
    }


    /**
     * create order from cart
     *
     * @param orderCreateDto - input orderCreateDto's properties
     * @return - orderRetrieveCreateDto
     */
    @Transactional
    public OrderRetrieveCreateDto createOrderFromCart(OrderCreateDto orderCreateDto) {
        User existingUser = extractUserFromToken(orderCreateDto.getAuthenticationToken());
        if(existingUser == null) {
            log.error("Not found this user");
            throw new NullPointerException("Not found this user");
        }
        log.info("Found user");

        Cart existingCart = existingUser.getCart();
        if(existingCart == null) {
            log.error("Not found this cart");
            throw new NullPointerException("Not found this cart");
        }
        log.info("Found cart");

        Order order = new Order();
        Random rnd = new Random();
        int id = rnd.nextInt(4,10000);
        order.setDisplayId(Integer.parseInt(String.format("%04d", id)));
        order.setUser(existingUser);
        order.setStatusCode(1);
        order.setOrderDate(CustomDateTimeFormatter.dateOfOrder());
        order.setUserNote(existingCart.getUserNote());
        orderRepository.save(order);
        log.info("Save order success");

        double totalPriceSum = 0.0;
        for(CartDetail cartDetail: existingCart.getCartDetails()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartDetail.getProduct());
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setPrice(cartDetail.getProduct().getPrice());
            orderDetail.setTotalPrice(orderDetail.getPrice() * orderDetail.getQuantity());
            orderDetailRepository.save(orderDetail);
            log.info("Save order detail success");

            totalPriceSum += orderDetail.getTotalPrice();
        }

        order.setTotalPrice(totalPriceSum);
        orderRepository.save(order);
        log.info("Update order success");

        OrderShippingDetail orderShippingDetail = new OrderShippingDetail();
        orderShippingDetail.setOrder(order);
        for(City city: cityRepository.findAll()) {
            if(city.getCityName().equals(orderCreateDto.getCity())){
                orderShippingDetail.setCity(city);
            }
        }
        for(District district: districtRepository.findAll()) {
            if(district.getDistrictName().equals(orderCreateDto.getDistrict())) {
                orderShippingDetail.setDistrict(district);
            }
        }
        orderShippingDetail.setAddress(orderCreateDto.getAddress());
        orderShippingDetail.setPhoneNumber(orderCreateDto.getPhoneNumber());
        orderShippingDetailRepository.save(orderShippingDetail);
        log.info("Save order shipping detail success");

        OrderRetrieveCreateDto orderRetrieveCreateDto = new OrderRetrieveCreateDto();
        orderRetrieveCreateDto.setDisplayId(order.getDisplayId());
        orderRetrieveCreateDto.setTotalPrice(order.getTotalPrice());

        return orderRetrieveCreateDto;
    }

    /**
     * search order and pagination
     *
     * @param orderSearchInputDto -input orderSearchInputDto's properties
     * @param page - input page
     * @return - list orders
     */
    public Page<OrderRetrieveSearchDto> searchOrdersAndPagination(OrderSearchInputDto orderSearchInputDto, Pageable page) {
        User existingUser = extractUserFromToken(orderSearchInputDto.getToken());
        if(existingUser == null) {
            log.info("Not authentication");
            userRepository.findById(orderSearchInputDto.getUserId()).orElseThrow(
                    () -> {
                        log.error("Not found this user");
                        return new NullPointerException("Not found this user");
                    }
            );
        }
        log.info("Found user");

        Page<Order> orders = orderRepository.searchOrdersAndPagination(orderSearchInputDto.getUsername(),
                orderSearchInputDto.getProductName(), orderSearchInputDto.getSku(), existingUser.getUserId(),
                orderSearchInputDto.getOrderId(), orderSearchInputDto.getStatusCode(), orderSearchInputDto.getOrderDate(),
                page);

        return orders.map(order -> {
            OrderRetrieveSearchDto orderRetrieveSearchDto = new OrderRetrieveSearchDto();
            orderRetrieveSearchDto.setOrderId(order.getOrderId());
            orderRetrieveSearchDto.setUsername(order.getUser().getUsername());
            orderRetrieveSearchDto.setTotalPrice(order.getTotalPrice());
            orderRetrieveSearchDto.setOrderDate(order.getOrderDate());

            switch (order.getStatusCode()) {
                case 8 -> orderRetrieveSearchDto.setOrderStatus(Status.PLACED.getStatus());
                case 9 -> orderRetrieveSearchDto.setOrderStatus(Status.PROCESSING.getStatus());
                case 10 -> orderRetrieveSearchDto.setOrderStatus(Status.CONFIRMED.getStatus());
                case 11 -> orderRetrieveSearchDto.setOrderStatus(Status.SHIPPED.getStatus());
                case 12 -> orderRetrieveSearchDto.setOrderStatus(Status.DELIVERED.getStatus());
                case 13 -> orderRetrieveSearchDto.setOrderStatus(Status.CANCELLED.getStatus());
                case 14 -> orderRetrieveSearchDto.setOrderStatus(Status.REFUNDED.getStatus());
                case 15 -> orderRetrieveSearchDto.setOrderStatus(Status.ON_HOLD.getStatus());
                case 16 -> orderRetrieveSearchDto.setOrderStatus(Status.RETURNED.getStatus());
                case 17 -> orderRetrieveSearchDto.setOrderStatus(Status.PARTIALLY_SHIPPED.getStatus());
                case 18 -> orderRetrieveSearchDto.setOrderStatus(Status.PARTIALLY_DELIVERED.getStatus());
                default -> {
                    log.error("Not found this status");
                    throw new RuntimeException("This status is not existed");
                }
            }

            for(OrderShippingDetail orderShippingDetail: order.getOrderShippingDetails()) {
                orderRetrieveSearchDto.setShippingAddress(orderShippingDetail.getAddress());
                orderRetrieveSearchDto.setShippingCity(orderShippingDetail.getCity().getCityName());
                orderRetrieveSearchDto.setShippingDistrict(orderShippingDetail.getDistrict().getDistrictName());
                orderRetrieveSearchDto.setShippingPhoneNumber(orderShippingDetail.getPhoneNumber());
            }

            List<OrderDetailRetrieveDto> orderDetailRetrieveDtos = new ArrayList<>();
            for(OrderDetail orderDetail: order.getOrderDetails()) {
                OrderDetailRetrieveDto orderDetailRetrieveDto = new OrderDetailRetrieveDto();
                orderDetailRetrieveDto.setOrderDetailId(orderDetail.getOrderDetailId());
                orderDetailRetrieveDto.setProductId(orderDetail.getProduct().getProductId());
                orderDetailRetrieveDto.setProductName(orderDetail.getProduct().getProductName());
                orderDetailRetrieveDto.setQuantity(orderDetail.getQuantity());
                orderDetailRetrieveDto.setPrice(orderDetail.getProduct().getPrice());
                orderDetailRetrieveDto.setTotalPrice(orderDetail.getTotalPrice());

                for(ProductImage productImage: orderDetail.getProduct().getProductImages()) {
                    orderDetailRetrieveDto.setImagePath(productImage.getImageForProduct().getPath());
                    orderDetailRetrieveDto.setImageName(productImage.getImageForProduct().getName());
                }
                orderDetailRetrieveDtos.add(orderDetailRetrieveDto);
            }

            orderRetrieveSearchDto.setOrderDetailRetrieveDtos(orderDetailRetrieveDtos);

            log.info("Search orders success");
            return orderRetrieveSearchDto;
        });
    }

    public Order updateOrder(OrderUpdateDto orderUpdateDto) {
        Order existingOrder = orderRepository.findById(orderUpdateDto.getOrderId()).orElseThrow(
                ()->{
                    log.error("Not found this order");
                    return new NullPointerException("Not found this order");
                }
        );
        log.info("Found order");

        existingOrder.setStatusCode(orderUpdateDto.getStatusCode());
        existingOrder.setDisplayId(orderUpdateDto.getDisplayId());
        orderRepository.save(existingOrder);
        log.info("update order success");

        return existingOrder;
    }
}
