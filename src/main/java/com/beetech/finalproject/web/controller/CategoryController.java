package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.common.AuthException;
import com.beetech.finalproject.domain.service.CategoryService;
import com.beetech.finalproject.domain.service.CityService;
import com.beetech.finalproject.domain.service.DistrictService;
import com.beetech.finalproject.web.common.ResponseDto;
import com.beetech.finalproject.web.dtos.category.CategoryCreateDto;
import com.beetech.finalproject.web.dtos.category.CategoryRetrieveDto;
import com.beetech.finalproject.web.dtos.category.CategoryUpdateDto;
import com.beetech.finalproject.web.dtos.city.CityDto;
import com.beetech.finalproject.web.dtos.district.DistrictDto;
import com.beetech.finalproject.web.response.CategoryResponse;
import com.beetech.finalproject.web.response.CityResponse;
import com.beetech.finalproject.web.response.DistrictResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CategoryController {
    private final CityService cityService;
    private final DistrictService districtService;
    private final CategoryService categoryService;

    @GetMapping("/categories/cities")
    public ResponseEntity<ResponseDto<Object>> findAllCities() {
        log.info("request finding all cities");

        try {
            List<CityDto> cityDtos = (List<CityDto>) cityService.findAllCities();

            // add result inside response
            List<CityResponse> cityResponses = new ArrayList<>();
            CityResponse cityResponse =  CityResponse.builder()
                    .cities(cityDtos)
                    .build();

            cityResponses.add(cityResponse);

            return ResponseEntity.ok(ResponseDto.build().withData(cityResponses));
        } catch (AuthenticationException e) {
            log.error("Find all cities failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @GetMapping("/categories/districts")
    public ResponseEntity<ResponseDto<Object>> findAllDistrictsByCity(@RequestParam Long cityId) {
        log.info("request finding all districts");

        try {
            List<DistrictDto> districtDtos = (List<DistrictDto>) districtService.findAllDistrictsByCity(cityId);

            // add result inside response
            List<DistrictResponse> districtResponses = new ArrayList<>();
            DistrictResponse districtResponse = DistrictResponse.builder()
                    .districts(districtDtos)
                    .build();

            districtResponses.add(districtResponse);

            return ResponseEntity.ok(ResponseDto.build().withData(districtResponses));
        } catch (AuthenticationException e) {
            log.error("Find all districts failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @PostMapping(value = "/add-category",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> createCategory(@RequestBody @ModelAttribute
                                                                  CategoryCreateDto categoryCreateDto) {
        log.info("request creating category");

        try {
            categoryService.createCategory(categoryCreateDto);
            return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
        } catch (AuthenticationException e) {
            log.error("Create category failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }


    @GetMapping("/categories")
    public ResponseEntity<ResponseDto<Object>> findAllCategories() {
        log.info("request finding all categories");
        try {
            List<CategoryRetrieveDto> categoryRetrieveDtos = (List<CategoryRetrieveDto>)
                    categoryService.findAllCategories();

            // add result inside response
            List<CategoryResponse> categoryResponses = new ArrayList<>();
            CategoryResponse categoryResponse =  CategoryResponse.builder()
                    .categoryRetrieveDtos(categoryRetrieveDtos)
                    .build();

            categoryResponses.add(categoryResponse);

            return ResponseEntity.ok(ResponseDto.build().withData(categoryResponses));
        } catch (AuthenticationException e) {
            log.error("Find all categories failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @PutMapping(value = "/delete-category",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> updateCategory(@RequestBody @ModelAttribute
                                                              CategoryUpdateDto categoryUpdateDto,
                                                              @RequestParam Long categoryId) {
        log.info("request updating category");

        try {
            categoryService.updateCategory(categoryId, categoryUpdateDto);
            return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
        } catch (AuthenticationException e) {
            log.error("Update category failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @DeleteMapping("/delete-category")
    public ResponseEntity<ResponseDto<Object>> deleteCategory(@RequestParam Long categoryId) {
        log.info("request deleting category");

        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
        } catch (AuthenticationException e) {
            log.error("Delete category failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

}
