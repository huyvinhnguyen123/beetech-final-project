package com.beetech.finalproject.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "order_shipping_detail")
public class OrderShippingDetail {
    @Id
    @Column(name = "order_shipping_detail_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long orderShippingDetailId;

    @Column(name = "phone_number", length = 11, nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties
    private Order order;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties
    private City city;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties
    private District district;
}
