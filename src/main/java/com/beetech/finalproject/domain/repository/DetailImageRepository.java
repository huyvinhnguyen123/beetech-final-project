package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.DetailImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailImageRepository extends CrudRepository<DetailImage, Long>, ListCrudRepository<DetailImage, Long> {
}
