package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.ImageForProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface ImageForProductRepository extends CrudRepository<ImageForProduct, Long>,
        ListCrudRepository<ImageForProduct, Long> {
}
