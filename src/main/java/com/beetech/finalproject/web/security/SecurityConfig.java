package com.beetech.finalproject.web.security;

import com.beetech.finalproject.domain.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtFilter jwtFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // disable csrf when connect with browser
                .authorizeHttpRequests((requests) -> requests // allow for public api: login api, homepage api, blog api, register api
                        // for all request url inside this app
                        // for permitAll() any user, guest or customer can access this url
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/upload/**").permitAll()
                        .requestMatchers("/api/v1/auth/register").permitAll()
                        .requestMatchers("/api/v1/auth/login").permitAll()
                )
                .authorizeHttpRequests((requests) -> requests // allow for login authentication & for ROLE_USER and ROLE_ADMIN
                        // for all request in this url has role admin & user will be access
                        .requestMatchers("/api/v1/users/**").hasAnyRole("USER", "ADMIN") // prepare for prefix ROLE_
                )
                .authorizeHttpRequests((requests) -> requests // allow for login authentication & for specific ROLE_
                        // for all request in this url has role admin will be access
                        // prepare for prefix ROLE_
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/add-category").hasRole("ADMIN")
                        .requestMatchers("/api/v1/delete-category").hasRole("ADMIN")
                        .requestMatchers("/api/v1/create-product").hasRole("ADMIN")
                        .requestMatchers("/api/v1/delete-product").hasRole("ADMIN")
                        .requestMatchers("/api/v1/create-order").hasRole("USER")
                )
                // For all others url need to be authenticated
                .authorizeHttpRequests((requests) -> requests.anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // create session
                .authenticationProvider(authenticationProvider()) // provide username and password for authentication
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // filter username and password with jwt token

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService); // provide username to authenticate
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // provide password to authenticate
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // encode password with BcryptPassword
    }
}
