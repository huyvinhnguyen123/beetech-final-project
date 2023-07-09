package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.User;
import com.beetech.finalproject.domain.enums.Roles;
import com.beetech.finalproject.domain.repository.UserRepository;
import com.beetech.finalproject.utils.CustomDateTimeFormatter;
import com.beetech.finalproject.web.dtos.user.UserCreateDto;
import com.beetech.finalproject.web.security.PasswordEncrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * create user
     *
     * @param userCreateDto - input userCreateDTO properties
     * @return - user
     */
    @Transactional
    public User createUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setLoginId(userCreateDto.getLoginId());
        user.setUsername(userCreateDto.getUsername());
        user.setBirthDay(CustomDateTimeFormatter.dateOfBirthFormatter(userCreateDto.getBirthDay()));
        user.setPassword(PasswordEncrypt.bcryptPassword(userCreateDto.getPassword()));
        user.setRole(Roles.USER.getRole());

        // check case if loginId is duplicate with current loginId(email)
        // and this user is locked
        User existingUser = userRepository.findByLoginId(userCreateDto.getLoginId());
        if (existingUser != null && !existingUser.isAccountNonLocked()) {
            log.error("email is existed and this email has been locked");
        }

        userRepository.save(user);
        log.info("create user success");

        return user;
    }
}
