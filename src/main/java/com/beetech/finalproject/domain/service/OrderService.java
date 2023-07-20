package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.*;
import com.beetech.finalproject.domain.repository.*;
import com.beetech.finalproject.utils.CustomDateTimeFormatter;
import com.beetech.finalproject.web.dtos.order.OrderCreateDto;
import com.beetech.finalproject.web.dtos.order.OrderRetrieveCreateDto;
import com.beetech.finalproject.web.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        order.setStatus(1);
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
        orderShippingDetail.setPhoneNumber(orderShippingDetail.getPhoneNumber());
        orderShippingDetailRepository.save(orderShippingDetail);
        log.info("Save order shipping detail success");

        OrderRetrieveCreateDto orderRetrieveCreateDto = new OrderRetrieveCreateDto();
        orderRetrieveCreateDto.setDisplayId(order.getDisplayId());
        orderRetrieveCreateDto.setTotalPrice(order.getTotalPrice());

        return orderRetrieveCreateDto;
    }
}
