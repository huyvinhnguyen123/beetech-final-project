package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.CategoryImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryImageRepository extends CrudRepository<CategoryImage, Long>,
        ListCrudRepository<CategoryImage, Long> {
}
