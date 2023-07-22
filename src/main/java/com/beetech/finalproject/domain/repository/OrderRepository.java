package com.beetech.finalproject.domain.repository;


import com.beetech.finalproject.domain.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>, ListCrudRepository<Order, Long> {
    @Query(value = "SELECT op.order_id, op.display_id, op.order_date, op.status_code, op.user_note, op.total_price AS total_price,\n" +
            "od.order_detail_id, od.quantity, od.price, od.total_price AS orderDetailTotalPrice,\n" +
            "osd.address, osd.phone_number, \n" +
            "c.city_name AS city, d.district_name AS district,\n" +
            "p.product_name, p.sku, p.detail_info, p.price,\n" +
            "ifp.name, ifp.path, ifp.thumbnail_flag,\n" +
            "u.user_id, u.username \n" +
            "FROM order_product op\n" +
            "LEFT JOIN order_detail od ON od.order_id = op.order_id\n" +
            "LEFT JOIN order_shipping_detail osd ON osd.order_id = op.order_id\n" +
            "LEFT JOIN city c ON c.city_id = osd.city_id \n" +
            "LEFT JOIN district d ON d.district_id = osd.district_id\n" +
            "LEFT JOIN product p ON p.product_id = od.product_id\n" +
            "LEFT JOIN product_image pro_img ON pro_img.product_id = p.product_id\n" +
            "LEFT JOIN image_for_product ifp ON ifp.image_id = pro_img.image_id\n" +
            "LEFT JOIN user u ON u.user_id = op.user_id\n" +
            "WHERE u.username LIKE %:username% \n" +
            "AND (p.product_name LIKE %:productName% OR p.sku LIKE %:sku%)\n" +
            "AND u.user_id = :userId\n" +
            "AND op.order_id = :orderId\n" +
            "AND op.status_code = :statusCode\n" +
            "AND op.order_date = :orderDate",
            nativeQuery = true)
    Page<Order> searchOrdersAndPagination(@Param("username") String username,
                                         @Param("productName") String productName,
                                         @Param("sku") String sku,
                                         @Param("userId") String userId,
                                         @Param("orderId") Long orderId,
                                         @Param("statusCode") int statusCode,
                                         @Param("orderDate") LocalDate orderDate,
                                         Pageable pageable);
}
