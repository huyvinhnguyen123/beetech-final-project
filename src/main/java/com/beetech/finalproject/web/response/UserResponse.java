package com.beetech.finalproject.web.response;

import com.beetech.finalproject.web.dtos.user.UserRetrieveDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private List<UserRetrieveDto> userRetrieveDtos;
}
