package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.ImageForCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImageForCategoryRepository extends CrudRepository<ImageForCategory, Long>,
        ListCrudRepository<ImageForCategory, Long> {
}
