package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String>, ListCrudRepository<User, String> {
    /**
     * find by email
     * @param loginId - input email
     * @return - user
     */
    User findByLoginId(String loginId);
}
