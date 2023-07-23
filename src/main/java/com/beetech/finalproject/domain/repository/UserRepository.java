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
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String>, ListCrudRepository<User, String> {
    /**
     * find by email
     * @param loginId - input email
     * @return - user
     */
    User findByLoginId(String loginId);

    /**
     * find by username
     * @param username - input username
     * @return - user
     */
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT u.user_id, \n" +
            "u.login_id, u.username,u.birth_day," +
            "u.delete_flag, u.log_flag, u.old_login_id, u.password, u.role,\n" +
            "SUM(od.total_price) AS totalPrice\n" +
            "FROM User u \n" +
            "LEFT JOIN order_product op ON u.user_id = op.user_id\n" +
            "LEFT JOIN order_detail od ON op.order_id = od.order_id\n" +
            "WHERE (:startDate IS NULL OR u.birth_day >= :startDate)\n" +
            "AND (:endDate IS NULL OR u.birth_day <= :endDate)\n" +
            "AND (:totalPrice IS NULL OR od.total_price > :totalPrice)\n" +
            "AND u.login_id LIKE %:loginId%\n"+
            "AND u.username LIKE %:username%\n"+
            "GROUP BY u.user_id",
            nativeQuery = true)
    Page<User> searchListOfUserWithCondition(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate,
                                             @Param("totalPrice") Double totalPrice,
                                             @Param("loginId") String loginId,
                                             @Param("username") String username,
                                             Pageable pageable);
}
