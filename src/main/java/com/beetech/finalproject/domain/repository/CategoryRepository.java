package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>, ListCrudRepository<Category, Long> {
}
