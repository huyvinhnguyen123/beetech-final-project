package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.domain.entities.City;
import com.beetech.finalproject.domain.entities.District;
import com.beetech.finalproject.domain.service.CityService;
import com.beetech.finalproject.domain.service.DistrictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {
    private final CityService cityService;
    private final DistrictService districtService;

    @GetMapping("/cities")
    public ResponseEntity<Iterable<City>> findAllCities() {
        log.info("request finding all cities");
        return new ResponseEntity<>(cityService.findAllCities(), HttpStatus.OK);
    }

    @GetMapping("/districts")
    public ResponseEntity<Iterable<District>> findAllDistricts() {
        log.info("request finding all cities");
        return new ResponseEntity<>(districtService.findAllDistricts(), HttpStatus.OK);
    }
}
