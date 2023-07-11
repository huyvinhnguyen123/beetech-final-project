package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.City;
import com.beetech.finalproject.domain.entities.District;
import com.beetech.finalproject.domain.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DistrictService {
    private final DistrictRepository districtRepository;

    /**
     * find all districts
     *
     * @return list districts
     */
    public Iterable<District> findAllDistricts() {
        List<District> districts = districtRepository.findAll();
        log.info("Find all districts success!");
        return districts;
    }
}
