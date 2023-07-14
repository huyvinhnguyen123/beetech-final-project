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
@Table(name = "category_image")
public class CategoryImage {
    @Id
    @Column(name = "category_image_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long categoryImageId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Category category;

    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private ImageForCategory imageForCategory;
}
