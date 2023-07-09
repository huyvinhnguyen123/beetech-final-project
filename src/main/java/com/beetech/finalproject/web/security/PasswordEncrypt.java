package com.beetech.finalproject.web.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncrypt {
    /**
     * encode password with BcryptPassword
     *
     * @return - password encode
     */
    public static String bcryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
