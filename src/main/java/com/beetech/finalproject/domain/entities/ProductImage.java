package com.beetech.finalproject.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_image")
public class ProductImage {
    @Id
    @Column(name = "product_image_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long productImageId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private ImageForProduct imageForProduct;
}
