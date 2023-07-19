package com.beetech.finalproject.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_product")
public class Order {
    @Id
    @Column(name = "order_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long orderId;

    @Column(name = "display_id", nullable = false)
    private int displayId; // if 1 is display if 0 it's not

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "user_note")
    private String userNote;

    @Column(name = "total price")
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}
