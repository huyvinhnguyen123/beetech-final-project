package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.District;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends CrudRepository<District, Long>, ListCrudRepository<District, Long> {
}
