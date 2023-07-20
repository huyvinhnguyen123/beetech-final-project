package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.common.AuthException;
import com.beetech.finalproject.domain.service.CartService;
import com.beetech.finalproject.web.common.ResponseDto;
import com.beetech.finalproject.web.dtos.cart.*;
import com.beetech.finalproject.web.response.CartResponse;
import com.beetech.finalproject.web.response.CartResponseCreate;
import com.beetech.finalproject.web.response.CartResponseSync;
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
            CartResponseCreate cartResponse = CartResponseCreate.builder()
                    .cartRetrieveCreateDto(cartRetrieveCreateDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(cartResponse));
        } catch (AuthenticationException e) {
            log.error("Add product failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @GetMapping("/sync-cart")
    public ResponseEntity<ResponseDto<Object>> syncCartAfterLogin(@RequestBody CartSyncDto cartSyncDto) {
        log.info("request syncing cart");

        try {
            CartRetrieveSyncDto cartRetrieveSyncDto = cartService.syncCartAfterLogin(cartSyncDto);
            CartResponseSync cartResponse = CartResponseSync.builder()
                    .cartRetrieveSyncDto(cartRetrieveSyncDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(cartResponse));
        } catch (AuthenticationException e) {
            log.error("Sync cart failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @PostMapping("/cart-info")
    public ResponseEntity<ResponseDto<Object>> displayCart(@RequestBody TokenInputDto tokenInputDto) {
        log.info("request displaying cart");

        try {
            CartRetrieveDto cartRetrieveDto = cartService.displayCart(tokenInputDto);
            CartResponse cartResponse = CartResponse.builder()
                    .cartRetrieveDto(cartRetrieveDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(cartResponse));
        } catch (AuthenticationException e) {
            log.error("Display cart failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @PostMapping("/delete-cart")
    public ResponseEntity<ResponseDto<Object>> deleteCart(@RequestBody CartDeleteDto cartDeleteDto) {
        log.info("request deleting cart");

        try {
            CartRetrieveSyncDto cartRetrieveSyncDto = cartService.deleteCart(cartDeleteDto);
            CartResponseSync cartResponse = CartResponseSync.builder()
                    .cartRetrieveSyncDto(cartRetrieveSyncDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(cartResponse));
        } catch (AuthenticationException e) {
            log.error("Delete cart failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }
}
