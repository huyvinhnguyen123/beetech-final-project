package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.common.AuthException;
import com.beetech.finalproject.domain.entities.User;
import com.beetech.finalproject.domain.service.UserService;
import com.beetech.finalproject.web.common.ResponseDto;
import com.beetech.finalproject.web.dtos.user.*;
import com.beetech.finalproject.web.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<ResponseDto<Object>> searchProductsAndPagination(@RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size,
                                                                           @RequestBody @ModelAttribute
                                                                           UserSearchDto userSearchDto) {

        Pageable pageable = PageRequest.of(page, size);
        log.info("request searching products");
        try {
            Page<UserRetrieveDto> userRetrievePage = userService.searchUserAndPagination(userSearchDto, pageable);
            List<UserRetrieveDto> userRetrieveDtos = userRetrievePage.getContent();

            // add result inside response
            List<UserResponse> userResponses = new ArrayList<>();
            UserResponse userResponse =  UserResponse.builder()
                    .userRetrieveDtos(userRetrieveDtos)
                    .build();

            userResponses.add(userResponse);

            return ResponseEntity.ok(ResponseDto.build().withData(userResponses));
        } catch (AuthenticationException e) {
            log.error("Search products failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }

    @PutMapping("/users/{loginId}/change-password")
    public ResponseEntity<ResponseDto<Object>> changePassword(@PathVariable String loginId,
            @Valid @RequestBody UserChangePasswordDto userChangePasswordDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("request changing password");

        try {
            User currentUser = (User) authentication.getPrincipal();
            userService.changePassword(userChangePasswordDto, currentUser);
            return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
        } catch (AuthenticationException e) {
            log.error("Change password failed: " + e.getMessage());
            throw new AuthException(AuthException.ErrorStatus.INVALID_GRANT);
        }
    }
}
