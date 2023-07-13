package com.beetech.finalproject.web.response;

import com.beetech.finalproject.web.dtos.district.DistrictDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DistrictResponse {
    private List<DistrictDto> districts;
}
