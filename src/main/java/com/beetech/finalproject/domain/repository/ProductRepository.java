package com.beetech.finalproject.domain.repository;

import com.beetech.finalproject.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>, ListCrudRepository<Product, Long> {
    @Query(value="SELECT p.product_id, p.sku, p.old_sku, p.product_name, p.detail_info, p.price, p.delete_flag,\n" +
            "ifp.name, ifp.path, c.category_id, COUNT(p.product_id) AS total_items\n" +
            "FROM product p\n" +
            "LEFT JOIN product_image pro_img ON pro_img.product_id = p.product_id\n" +
            "LEFT JOIN image_for_product ifp ON ifp.image_id = pro_img.image_id\n" +
            "LEFT JOIN product_category pc ON pc.product_id = p.product_id\n" +
            "LEFT JOIN category c ON c.category_id  = pc.category_id\n" +
            "WHERE c.category_id = :categoryId AND\n" +
            "(p.sku LIKE %:sku% OR p.product_name LIKE %:productName%)\n" +
            "GROUP BY p.product_id",
            nativeQuery = true)
    Page<Product> searchProductsAndPagination(@Param("categoryId") Long categoryId, @Param("sku") String sku,
                                               @Param("productName") String productName,
                                               Pageable pageable);
    @Query(value="SELECT p.product_id, p.sku, p.old_sku, p.product_name, p.detail_info, p.price, p.delete_flag,\n" +
            "ifp.name, ifp.path\n" +
            "FROM product p\n" +
            "LEFT JOIN product_image pro_img ON pro_img.product_id = p.product_id\n" +
            "LEFT JOIN image_for_product ifp ON ifp.image_id = pro_img.image_id\n" +
            "WHERE p.sku LIKE %:sku% OR p.product_name LIKE %:productName%",
            nativeQuery = true)
    List<Product> searchProducts(@Param("sku") String sku, @Param("productName") String productName);

    @Query(value="SELECT p.product_id, p.sku, p.old_sku, p.product_name, p.detail_info, p.price, p.delete_flag,\n" +
            "ifp.name, ifp.path\n" +
            "FROM product p\n" +
            "LEFT JOIN product_image pro_img ON pro_img.product_id = p.product_id\n" +
            "LEFT JOIN image_for_product ifp ON ifp.image_id = pro_img.image_id\n" +
            "WHERE p.sku LIKE %:sku%",
            nativeQuery = true)
    List<Product> searchProducts(@Param("sku") String sku);
}
