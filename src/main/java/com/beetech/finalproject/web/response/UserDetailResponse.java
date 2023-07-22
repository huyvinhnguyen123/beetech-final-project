package com.beetech.finalproject.web.response;

import com.beetech.finalproject.web.dtos.user.UserDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {
    private UserDetailDto userDetailDto;
}
