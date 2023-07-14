package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.District;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends ListCrudRepository<District, Long> {
    @Query(value = "SELECT d.district_id, d.district_name, c.city_id  \n" +
            "FROM district d \n" +
            "LEFT JOIN city c ON c.city_id = d.city_id \n" +
            "WHERE c.city_id = ?1", nativeQuery = true)
    Optional<List<District>> findAllDistrictByCityId(Long cityId);
}
