package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.common.AuthException;
import com.beetech.finalproject.domain.service.ProductService;
import com.beetech.finalproject.web.common.ResponseDto;
import com.beetech.finalproject.web.dtos.product.ProductCreateDto;
import com.beetech.finalproject.web.dtos.product.ProductRetrieveDto;
import com.beetech.finalproject.web.dtos.product.ProductSearchInputDto;
import com.beetech.finalproject.web.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<ResponseDto<Object>> findAllProductsAndPagination(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size,
                                                                            @RequestBody @ModelAttribute
                                                                                ProductSearchInputDto productSearchInputDto) {

        Pageable pageable = PageRequest.of(page, size);
        log.info("request finding all products");
        try {
            Page<ProductRetrieveDto> productRetrievePage = productService.findAllProductsAndPagination(productSearchInputDto, pageable);
            List<ProductRetrieveDto> productRetrieveDtos = productRetrievePage.getContent();

            // add result inside response
            List<ProductResponse> productResponses = new ArrayList<>();
            ProductResponse productResponse =  ProductResponse.builder()
                    .productRetrieveDtos(productRetrieveDtos)
                    .build();

            productResponses.add(productResponse);

            return ResponseEntity.ok(ResponseDto.build().withData(productResponses));
        } catch (AuthenticationException e) {
            log.error("Find all products failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }
}
