package com.beetech.finalproject.domain.repository;


import com.beetech.finalproject.domain.entities.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>, ListCrudRepository<Order, Long> {
}
