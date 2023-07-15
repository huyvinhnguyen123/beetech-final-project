package com.beetech.finalproject.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image_for_product")
public class ImageForProduct {
    @Id
    @Column(name = "image_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long imageProductId;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "thumbnail_flag", nullable = false, columnDefinition = "TINYINT")
    private int thumbnailFlag;

    @OneToMany(mappedBy = "imageForProduct")
    private List<ProductImage> productImages;
}

