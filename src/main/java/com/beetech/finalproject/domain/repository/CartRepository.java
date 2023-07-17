package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long>, ListCrudRepository<Cart, Long> {
    Cart findByToken(String token);
}
