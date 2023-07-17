package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.CartDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends CrudRepository<CartDetail, Long>, ListCrudRepository<CartDetail, Long> {
}
