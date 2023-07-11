package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.domain.entities.Category;
import com.beetech.finalproject.domain.entities.City;
import com.beetech.finalproject.domain.entities.District;
import com.beetech.finalproject.domain.service.CategoryService;
import com.beetech.finalproject.domain.service.CityService;
import com.beetech.finalproject.domain.service.DistrictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api")
public class CategoryController {
    private final CityService cityService;
    private final DistrictService districtService;
    private final CategoryService categoryService;

    @GetMapping("/categories/cities")
    public ResponseEntity<Iterable<City>> findAllCities() {
        log.info("request finding all cities");
        return new ResponseEntity<>(cityService.findAllCities(), HttpStatus.OK);
    }

    @GetMapping("/categories/districts")
    public ResponseEntity<Iterable<District>> findAllDistricts() {
        log.info("request finding all cities");
        return new ResponseEntity<>(districtService.findAllDistricts(), HttpStatus.OK);
    }

    @PostMapping("/add-category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(@RequestParam("categoryName") String categoryName,
                                                   @RequestParam("image") MultipartFile file) {
        log.info("request creating category");
        return new ResponseEntity<>(categoryService.createCategory(categoryName, file), HttpStatus.OK);
    }
}
