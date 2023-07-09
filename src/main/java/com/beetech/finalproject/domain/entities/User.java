package com.beetech.finalproject.domain.entities;

import com.google.common.collect.Lists;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId = UUID.randomUUID().toString();

    @Column(name = "username", length = 255, nullable = false)
    private String username;

    @Column(name = "birth_day", nullable = false)
    private LocalDate birthDay;

    @Column(name = "login_id", length = 255, nullable = false, unique = true)
    private String loginId; // loginId = email

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    private int deleteFlag = 1;

    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> carts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (StringUtils.hasText(role)) {
            return Lists.newArrayList(new SimpleGrantedAuthority(role));
        } else {
            return Lists.newArrayList();
        }
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
