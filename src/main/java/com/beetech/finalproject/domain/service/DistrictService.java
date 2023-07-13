package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.District;
import com.beetech.finalproject.domain.repository.DistrictRepository;
import com.beetech.finalproject.web.dtos.district.DistrictDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public Iterable<DistrictDto> findAllDistrictsByCity(Long cityId) {
        List<District> districts = districtRepository.findAllDistrictByCityId(cityId).orElseThrow(
                () -> {
                    log.error("Not found this city");
                    return new NullPointerException("Not found this city: " + cityId);
                }
        );

        List<DistrictDto> districtDtos = new ArrayList<>();
        for(District d: districts) {
            DistrictDto districtDto = new DistrictDto();
            districtDto.setDistrictId(d.getDistrictId());
            districtDto.setDistrictName(d.getDistrictName());

            districtDtos.add(districtDto);
        }

        log.info("Find all districts success!");
        return districtDtos;
    }
}
