package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.City;
import com.beetech.finalproject.domain.repository.CityRepository;
import com.beetech.finalproject.web.dtos.city.CityDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public Iterable<CityDto> findAllCities() {
        List<CityDto> cityDtos = new ArrayList<>();

        List<City> cities = cityRepository.findAll();
        for(City c: cities) {
            CityDto cityDto = new CityDto();
            cityDto.setCityId(c.getCityId());
            cityDto.setCityName(c.getCityName());

            cityDtos.add(cityDto);
        }

        log.info("Find all cities success!");
        return cityDtos;
    }
}
