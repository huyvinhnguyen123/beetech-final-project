package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.common.AccountException;
import com.beetech.finalproject.common.DeleteFlag;
import com.beetech.finalproject.common.LockFlag;
import com.beetech.finalproject.domain.entities.User;
import com.beetech.finalproject.domain.enums.Roles;
import com.beetech.finalproject.domain.repository.UserRepository;
import com.beetech.finalproject.exception.LockedAccountException;
import com.beetech.finalproject.utils.CustomDateTimeFormatter;
import com.beetech.finalproject.web.dtos.user.UserCreateDto;
import com.beetech.finalproject.web.security.PasswordEncrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * Check valid new user information, if ok register to system
     *
     * @param userCreateDto new user information
     * @return registered data
     */
    public User registerNewUser(UserCreateDto userCreateDto) {
        validUserId(userCreateDto.getLoginId());
        return createUser(userCreateDto);
    }

    /**
     * create user
     *
     * @param userCreateDto - input userCreateDTO properties
     * @return - user
     */
//    @Transactional // not need to add Transaction here because there is only 1 action that change db
    public User createUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setLoginId(userCreateDto.getLoginId());
        user.setUsername(userCreateDto.getUsername());
        user.setBirthDay(CustomDateTimeFormatter.dateOfBirthFormatter(userCreateDto.getBirthDay()));
        user.setPassword(PasswordEncrypt.bcryptPassword(userCreateDto.getPassword()));
        user.setLogFlag(LockFlag.NON_LOCK.getCode());
        user.setDeleteFlag(DeleteFlag.NON_DELETE.getCode());
        user.setRole(Roles.USER.getRole());

        userRepository.save(user);
        log.info("create user success");

        return user;
    }

    /**
     * Check is loginId can be used to register new Account
     * if not, an Exception will be thrown
     * @param loginId loginId
     */
    public void validUserId(String loginId) {
        // Check if loginId is duplicate with current loginId (email) and if this user is locked
        User existingUser = userRepository.findByLoginId(loginId);
        if (existingUser != null) {
            if (!existingUser.isAccountNonLocked()) {
                log.error("This email is already locked: {}", loginId);
                throw new AccountException(AccountException.ErrorStatus.ALREADY_REGISTERED, "The email is already used.");
            } else {
                log.error("This email is already exists: {}", loginId);
                throw new AccountException(AccountException.ErrorStatus.LOCKED_ACCOUNT, "The email is already registered and the account is locked.");
            }
        }
    }
}
