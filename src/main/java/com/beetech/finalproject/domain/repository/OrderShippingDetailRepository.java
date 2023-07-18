package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.OrderShippingDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderShippingDetailRepository extends CrudRepository<OrderShippingDetail, Long>,
        ListCrudRepository<OrderShippingDetail, Long> {
}
