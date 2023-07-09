package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.User;
import com.beetech.finalproject.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * load user by email
     * we will retrieve data from database to get username/email
     *
     * @param loginId - input email
     * @return - user
     * @throws UsernameNotFoundException - error if user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(loginId);
        log.info("load user success: " + user);
        return user;
    }
}
