package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.common.AuthException;
import com.beetech.finalproject.domain.entities.Cart;
import com.beetech.finalproject.domain.service.CartService;
import com.beetech.finalproject.web.common.ResponseDto;
import com.beetech.finalproject.web.dtos.cart.CartCreateDto;
import com.beetech.finalproject.web.dtos.cart.CartRetrieveCreateDto;
import com.beetech.finalproject.web.dtos.product.ProductCreateDto;
import com.beetech.finalproject.web.response.CartResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class CartController {
    private final CartService cartService;

    @PostMapping("/add-cart")
    public ResponseEntity<ResponseDto<Object>> addProductToCart(@RequestBody CartCreateDto cartCreateDto) {
        log.info("request adding product to cart");

        try {
            CartRetrieveCreateDto cartRetrieveCreateDto = cartService.addProductToCart(cartCreateDto);
            CartResponse cartResponse = CartResponse.builder()
                    .cartRetrieveCreateDto(cartRetrieveCreateDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(cartResponse));
        } catch (AuthenticationException e) {
            log.error("Create product failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }
}
