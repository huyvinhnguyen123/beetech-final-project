package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.common.AccountException;
import com.beetech.finalproject.common.DeleteFlag;
import com.beetech.finalproject.common.LockFlag;
import com.beetech.finalproject.domain.entities.User;
import com.beetech.finalproject.domain.enums.Roles;
import com.beetech.finalproject.domain.repository.UserRepository;
import com.beetech.finalproject.utils.CustomDateTimeFormatter;
import com.beetech.finalproject.web.dtos.user.UserChangePasswordDto;
import com.beetech.finalproject.web.dtos.user.UserCreateDto;
import com.beetech.finalproject.web.dtos.user.UserRetrieveDto;
import com.beetech.finalproject.web.dtos.user.UserSearchDto;
import com.beetech.finalproject.web.security.PasswordEncrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

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
    public User createUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setLoginId(userCreateDto.getLoginId());
        user.setUsername(userCreateDto.getUsername());
        user.setBirthDay(CustomDateTimeFormatter.dateOfBirthFormatter(userCreateDto.getBirthDay()));
        user.setPassword(PasswordEncrypt.bcryptPassword(userCreateDto.getPassword()));
        user.setLogFlag(LockFlag.NON_LOCK.getCode());
        user.setDeleteFlag(DeleteFlag.NON_DELETE.getCode());

        if(user.getLoginId().equals("huy.vinhnguyen@outlook.com")) {
            user.setRole(Roles.ADMIN.getRole());
        } else {
            user.setRole(Roles.USER.getRole());
        }

        userRepository.save(user);
        log.info("create user success");

        log.info("Role: " + user.getRole());

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

    /**
     * change password
     *
     * @param userChangePasswordDto - input password
     * @param user - authentication
     * @return - user
     */
    public User changePassword(UserChangePasswordDto userChangePasswordDto, User user) {
        User existingUser = user;
        String oldPassword = userChangePasswordDto.getOldPassword();

        // Use BCrypt's built-in method to verify the old password
        if (BCrypt.checkpw(oldPassword, existingUser.getPassword())) {
            String newPassword = userChangePasswordDto.getPassword();
            String encodedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            existingUser.setPassword(encodedNewPassword);
            userRepository.save(existingUser);
            log.info("Change password success");
        } else {
            log.error("Old password is not correct");
            throw new RuntimeException("Old password is not correct");
        }
        return existingUser;
    }

    /**
     * search user and pagination with condition success
     *
     * @param userSearchDto
     * @param pageable
     * @return
     */
    public Page<UserRetrieveDto> searchUserAndPagination(UserSearchDto userSearchDto, Pageable pageable) {
        Page<User> users = userRepository.searchListOfUserWithCondition(userSearchDto.getStartDate(),
                userSearchDto.getEndDate(), userSearchDto.getTotalPrice(), userSearchDto.getLoginId(),
                userSearchDto.getUsername(), pageable);

        return users.map(user -> {
            UserRetrieveDto userRetrieveDto = new UserRetrieveDto();
            userRetrieveDto.setUserId(user.getUserId());
            userRetrieveDto.setLoginId(user.getLoginId());
            userRetrieveDto.setUsername(user.getUsername());
            userRetrieveDto.setBirthDay(user.getBirthDay());
            userRetrieveDto.setTotalPrice(user.getCart().getTotalPrice());

            log.info("Search user and pagination with condition success");
            return userRetrieveDto;
        });
    }
}
