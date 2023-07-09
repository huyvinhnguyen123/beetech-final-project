package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.User;
import com.beetech.finalproject.domain.enums.Roles;
import com.beetech.finalproject.domain.repository.UserRepository;
import com.beetech.finalproject.utils.CustomDateTimeFormatter;
import com.beetech.finalproject.web.dtos.user.UserCreateDto;
import com.beetech.finalproject.web.security.PasswordEncrypt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("Riley");
        userCreateDto.setLoginId("huy.vinhnguyen@outlook.com");
        userCreateDto.setPassword(PasswordEncrypt.bcryptPassword("user"));
        userCreateDto.setBirthDay("19960515");

        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setBirthDay(CustomDateTimeFormatter.dateOfBirthFormatter(userCreateDto.getBirthDay()));
        user.setLoginId(userCreateDto.getLoginId());
        user.setPassword(userCreateDto.getPassword());
        user.setRole(Roles.USER.getRole());

        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        User serviceRetrieveUser = userService.createUser(userCreateDto);

        Assertions.assertEquals(user.getLoginId(), serviceRetrieveUser.getLoginId());
        Assertions.assertEquals(user.getRole(), serviceRetrieveUser.getRole());

        verify(userRepository, times(1)).save(Mockito.any());
    }
}
