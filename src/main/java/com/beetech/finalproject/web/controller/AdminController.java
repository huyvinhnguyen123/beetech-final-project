package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.common.AuthException;
import com.beetech.finalproject.domain.entities.User;
import com.beetech.finalproject.domain.service.AdminService;
import com.beetech.finalproject.web.common.ResponseDto;
import com.beetech.finalproject.web.dtos.cart.CartRetrieveUpdateDto;
import com.beetech.finalproject.web.dtos.cart.CartUpdateDto;
import com.beetech.finalproject.web.dtos.user.UserCreateDto;
import com.beetech.finalproject.web.dtos.user.UserDetailDto;
import com.beetech.finalproject.web.response.CartUpdateResponse;
import com.beetech.finalproject.web.response.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users/")
    public ResponseEntity<ResponseDto<Object>> findAndDisplayUserDetailById(@RequestParam String userId) {
        log.info("request finding user");

        try {
            UserDetailDto userDetailDto = adminService.findAndDisplayUserDetailById(userId);
            UserDetailResponse userDetailResponse = UserDetailResponse.builder()
                    .userDetailDto(userDetailDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(userDetailResponse));
        } catch (AuthenticationException e) {
            log.error("Find user failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseDto<Object>> updateUser(@RequestBody UserCreateDto userCreateDto) {
        log.info("request updating user");

        try {
            UserDetailDto userDetailDto = adminService.updateUser(userCreateDto);
            UserDetailResponse userDetailResponse = UserDetailResponse.builder()
                    .userDetailDto(userDetailDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(userDetailResponse));
        } catch (AuthenticationException e) {
            log.error("Update user failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @PostMapping("/user/delete-user")
    public ResponseEntity<ResponseDto<Object>> deleteUser(@RequestParam String userId) {
        log.info("request deleting user");

        try {
            UserDetailDto userDetailDto = adminService.deleteUser(userId);
            UserDetailResponse userDetailResponse = UserDetailResponse.builder()
                    .userDetailDto(userDetailDto)
                    .build();

            return ResponseEntity.ok(ResponseDto.build().withData(userDetailResponse));
        } catch (AuthenticationException e) {
            log.error("Delete user failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }
}
