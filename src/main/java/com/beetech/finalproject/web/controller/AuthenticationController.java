package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.domain.entities.User;
import com.beetech.finalproject.domain.service.UserService;
import com.beetech.finalproject.web.dtos.user.UserCreateDto;
import com.beetech.finalproject.web.dtos.user.UserLoginDto;
import com.beetech.finalproject.web.security.JwtUtils;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils JwtUtils;
    private final UserService userService;
    private final MessageSource messageSource;

    /**
     * request create user
     *
     * @param userCreateDto - input user information
     * @return - token when use is created
     */
    @PostMapping("/register")
    public ResponseEntity createUser(@Valid @RequestBody UserCreateDto userCreateDto,
                                     BindingResult bindingResult,
                                     Locale locale) {
        log.info("Request creating user...");
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> messageSource.getMessage(error.getCode(), null, locale))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        User createUser = userService.createUser(userCreateDto);
        String token = JwtUtils.createToken(createUser); // for email verification
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    /**
     * request login authentication
     *
     * @param userLoginDto - input user login information
     * @return - token when authentication is passed
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserLoginDto userLoginDto) {
        log.info("Request authenticating user...");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userLoginDto.getLoginId(),
                userLoginDto.getPassword()
        );

        // check error 403 & 401 status code when authenticate fail
        try {
            // Use AuthenticationManager authenticate Authentication object
            Authentication login = authenticationManager.authenticate(authentication);

            // Prepare to create JWT token from username or email
            User user = (User) login.getPrincipal();

            log.info("role:" + user.getAuthorities());

            // Create JWT token
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
