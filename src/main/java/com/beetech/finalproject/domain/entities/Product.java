package com.beetech.finalproject.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int productId;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "detail_info")
    private String detailInfo;

    @Column(name = "price")
    private double price;

    @Column(name = "delete_flag")
    private int deleteFlag = 0;

    @ManyToMany(mappedBy = "products")
    private List<ImageProduct> imageProducts ;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
            name="product_category",
            joinColumns=@JoinColumn(name="product_id"),
            inverseJoinColumns=@JoinColumn(name="category_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Category> categories;

    @ManyToMany(mappedBy = "products")
    private List<Cart> carts ;
}
