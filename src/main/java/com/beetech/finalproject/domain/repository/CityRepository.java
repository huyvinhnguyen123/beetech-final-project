package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends CrudRepository<City, Integer>, ListCrudRepository<City, Integer> {
}
