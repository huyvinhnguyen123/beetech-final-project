package com.beetech.finalproject.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "image_for_category")
public class ImageForCategory {
    @Id
    @Column(name = "image_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long imageId;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "imageForCategory")
    @JsonIgnoreProperties
    private List<CategoryImage> categoryImages;
}
