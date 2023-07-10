package com.beetech.finalproject.web.controller;

import com.beetech.finalproject.web.dtos.user.UserLoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    public void testLogin() throws Exception {
        // Create a mock UserLoginDto object
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setLoginId("riley@example.com");
        userLoginDto.setPassword("huyNV123");

        // Create a mock Authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userLoginDto.getLoginId(),
                userLoginDto.getPassword()
        );

        // Mock the call to the authenticationManager.authenticate() method
        given(authenticationManager.authenticate(authentication))
                .willReturn(authentication);

        // Send a mock POST request to the /login endpoint with the UserLoginDto object as the request body
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userLoginDto)))
                .andExpect(status().isOk());
    }
}
