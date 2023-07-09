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
@Table(name = "image_for_category")
public class ImageCategory {
    @Id
    @Column(name = "image_category_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long imageCategoryId;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
            name="category_image",
            joinColumns=@JoinColumn(name="image_category_id"),
            inverseJoinColumns=@JoinColumn(name="category_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Category> categories;
}
