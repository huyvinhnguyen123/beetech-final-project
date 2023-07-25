package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.common.AuthException;
import com.beetech.finalproject.domain.entities.User;
import com.beetech.finalproject.domain.service.CartService;
import com.beetech.finalproject.web.common.ResponseDto;
import com.beetech.finalproject.web.dtos.cart.*;
import com.beetech.finalproject.web.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class CartController {
    private final CartService cartService;

    @PostMapping("/add-cart")
    public ResponseEntity<ResponseDto<Object>> addProductToCart(@RequestBody CartCreateDto cartCreateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = (User) authentication.getPrincipal();

            log.info("request adding product to cart");
            CartRetrieveCreateDto cartRetrieveCreateDto = cartService.addProductToCart(cartCreateDto, currentUser);
            CartResponseCreate cartResponse = CartResponseCreate.builder()
                    .cartRetrieveCreateDto(cartRetrieveCreateDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(cartResponse));
        } else {
            log.info("request adding product to cart without authentication");
            CartRetrieveCreateDto cartRetrieveCreateDto = cartService.addProductToCartWithoutLogin(cartCreateDto);
            CartResponseCreate cartResponse = CartResponseCreate.builder()
                    .cartRetrieveCreateDto(cartRetrieveCreateDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(cartResponse));
        }
    }

    @GetMapping("/sync-cart")
    public ResponseEntity<ResponseDto<Object>> syncCartAfterLogin(@RequestBody String token, Authentication authentication) {
        log.info("request syncing cart");
        User currentUser = (User) authentication.getPrincipal();

        CartRetrieveSyncDto cartRetrieveSyncDto = cartService.syncCartAfterLogin(token, currentUser);
        CartResponseSync cartResponse = CartResponseSync.builder()
                .cartRetrieveSyncDto(cartRetrieveSyncDto)
                .build();

        return ResponseEntity.ok(ResponseDto.build().withData(cartResponse));
    }

    @PostMapping("/cart-info")
    public ResponseEntity<ResponseDto<Object>> displayCart(@RequestBody String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("request displaying cart");
        try {
            User currentUser = (User) authentication.getPrincipal();

            CartRetrieveDto cartRetrieveDto = cartService.displayCart(token, currentUser);
            CartResponse cartResponse = CartResponse.builder()
                    .cartRetrieveDto(cartRetrieveDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(cartResponse));
        } catch (AuthenticationException e) {
            log.error("Display cart failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @GetMapping("/cart-quantity")
    public ResponseEntity<ResponseDto<Object>> getTotalQuantityInCart(@RequestBody String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("request displaying cart");
        try {
            User currentUser = (User) authentication.getPrincipal();

            CartSumQuantityDto cartSumQuantityDto = cartService.getTotalQuantityInCart(token, currentUser);
            CartQuantitySumResponse cartResponse = CartQuantitySumResponse.builder()
                    .cartSumQuantityDto(cartSumQuantityDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(cartResponse));
        } catch (AuthenticationException e) {
            log.error("Display cart failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @PostMapping("/update-cart")
    public ResponseEntity<ResponseDto<Object>> updateCart(@RequestBody CartUpdateDto cartUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("request updating cart");
        try {
            User currentUser = (User) authentication.getPrincipal();

            CartRetrieveUpdateDto cartRetrieveUpdateDto = cartService.updateCart(cartUpdateDto, currentUser);
            CartUpdateResponse cartResponse = CartUpdateResponse.builder()
                    .cartRetrieveUpdateDto(cartRetrieveUpdateDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(cartResponse));
        } catch (AuthenticationException e) {
            log.error("Update cart failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @DeleteMapping("/delete-cart")
    public ResponseEntity<ResponseDto<Object>> deleteCart(@RequestBody CartDeleteDto cartDeleteDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("request deleting cart");
        try {
            User currentUser = (User) authentication.getPrincipal();

            CartRetrieveSyncDto cartRetrieveSyncDto = cartService.deleteCart(cartDeleteDto, currentUser);
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
