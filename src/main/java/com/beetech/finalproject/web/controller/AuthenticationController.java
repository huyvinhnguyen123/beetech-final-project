package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.domain.entities.User;
import com.beetech.finalproject.domain.service.UserService;
import com.beetech.finalproject.exception.LockedAccountException;
import com.beetech.finalproject.web.dtos.user.UserCreateDto;
import com.beetech.finalproject.web.dtos.user.UserLoginDto;
import com.beetech.finalproject.web.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils JwtUtils;
    private final UserService userService;

    /**
     * request create user
     *
     * @param userCreateDto - input user information
     * @return - token when use is created
     */
    @PostMapping("/register")
    public ResponseEntity createUser(@Valid @RequestBody UserCreateDto userCreateDto, BindingResult bindingResult) {
        log.info("Request creating user...");

        // Check for validation errors in the input
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            User createUser = userService.createUser(userCreateDto);
            String token = JwtUtils.createToken(createUser); // for email verification
            return ResponseEntity.status(HttpStatus.CREATED).body(token);
        } catch (LockedAccountException e) {
            return ResponseEntity.badRequest().body("The email is already registered and the account is locked.");
        }
    }

    /**
     * request login authentication
     *
     * @param userLoginDto - input user login information
     * @return - token when authentication is passed
     */
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody UserLoginDto userLoginDto, BindingResult bindingResult) {
        log.info("Request authenticating user...");

        // Check for validation errors in the input
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }


        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userLoginDto.getLoginId(),
                    userLoginDto.getPassword()
            );

            Authentication login = authenticationManager.authenticate(authentication);

            // Check if the user is deleted or locked
            User user = (User) login.getPrincipal();
            if (user.getDeleteFlag() == 9 || !user.isAccountNonLocked()) {
                log.error("User has been deleted or locked.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User has been deleted or locked.");
            }

            String token = JwtUtils.createToken(user);

            log.info("create token success");
            log.info("token: " + token);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            log.error("authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
        }
    }
}
