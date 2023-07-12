package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserRepository extends CrudRepository<User, String>, ListCrudRepository<User, String> {
    /**
     * find by email
     * @param loginId - input email
     * @return - user
     */
    User findByLoginId(String loginId);

    /**
     * find all user within date of birth and total price > input total price
     *
     * @param startDate - input startDate
     * @param endDate - input endDate
     * @param totalPrice - input totalPrice
     * @param pageable - input pageable properties
     * @return
     */
    @Query(value = "SELECT u.user_id AS id, \n" +
            "u.login_id AS email ,u.username AS username ,u.birth_day AS birthday,\n" +
            "SUM(od.total_price) AS totalPrice\n" +
            "FROM User u \n" +
            "LEFT JOIN order_product op ON u.user_id = op.user_id\n" +
            "LEFT JOIN order_detail od ON op.order_id = od.order_id\n" +
            "WHERE (:startDate IS NULL OR u.birth_day >= :startDate)\n" +
            "AND (:username IS NULL OR username = :username)\n" +
            "AND (:loginId IS NULL OR username = :loginId)\n" +
            "AND (:endDate IS NULL OR u.birth_day <= :endDate)\n" +
            "AND (:totalPrice IS NULL OR od.total_price > :totalPrice)\n" +
            "GROUP BY u.user_id", nativeQuery = true)
    Page<User> searchAllUsersByConditionAndPagination(@Param("startDate") LocalDate startDate,
                                                      @Param("loginId") String loginId,
                                                      @Param("username") String username,
                                                      @Param("endDate") LocalDate endDate,
                                                      @Param("totalPrice") Double totalPrice,
                                                      Pageable pageable);
}
