package com.beetech.finalproject.domain.enums;

import lombok.Getter;

@Getter
public enum Status {
    // cart status
    ACTIVE(1, "Active"),
    PENDING(2, "Pending"),
    CHECKED_OUT(3, "Checked Out"),
    EXPIRED(4, "Expired"),
    ABANDONED(5, "Abandoned"),
    SAVED(6, "Saved"),
    DELETED(7, "Deleted"),
    // order status
    PLACED(8, "Placed"),
    PROCESSING(9, "Processing"),
    CONFIRMED(10, "Confirmed"),
    SHIPPED(11, "Shipped"),
    DELIVERED(12, "Delivered"),
    CANCELLED(13, "Cancelled"),
    REFUNDED(14, "Refunded"),
    ON_HOLD(15, "On Hold"),
    RETURNED(16, "Returned"),
    PARTIALLY_SHIPPED(17, "Partially Shipped"),
    PARTIALLY_DELIVERED(18, "Partially Delivered");

    private int code;
    private String status;

    Status(int code, String status){
        this.code = code;
        this.status = status;
    }
}
