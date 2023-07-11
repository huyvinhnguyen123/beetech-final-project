package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.City;
import com.beetech.finalproject.domain.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    /**
     * find all cities
     *
     * @return list city
     */
    public Iterable<City> findAllCities() {
        List<City> cities = cityRepository.findAll();
        log.info("Find all cities success!");
        return cities;
    }
}
