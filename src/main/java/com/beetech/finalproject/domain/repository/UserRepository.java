package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String>, ListCrudRepository<User, String> {
    /**
     * find by email
     * @param loginId - input email
     * @return - user
     */
    User findByLoginId(String loginId);

    /**
     * find by username
     * @param username - input username
     * @return - user
     */
    Optional<User> findByUsername(String username);
}
