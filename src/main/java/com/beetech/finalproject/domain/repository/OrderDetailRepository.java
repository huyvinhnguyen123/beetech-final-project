package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.OrderDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends CrudRepository<OrderDetail, Long>, ListCrudRepository<OrderDetail, Long> {
}
