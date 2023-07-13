package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.Category;
import com.beetech.finalproject.domain.entities.CategoryImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>, ListCrudRepository<Category, Long> {
    @Query(value = "SELECT c.category_id, c.category_name, \n" +
            "ifc.name, ifc.path \n" +
            "FROM category c\n" +
            "LEFT JOIN category_image ci ON c.category_id = ci.category_id \n" +
            "LEFT JOIN image_for_category ifc ON ifc.image_id = ci.image_id", nativeQuery = true)
    List<Category> findAllCategories();
}
