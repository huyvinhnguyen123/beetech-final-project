package com.beetech.finalproject.domain.enums;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE(1, "Active"),
    PENDING(2, "Pending"),
    CHECKED_OUT(3, "Checked Out"),
    EXPIRED(4, "Expired"),
    ABANDONED(5, "Abandoned"),
    SAVED(6, "Saved"),
    DELETED(7, "Deleted");

    private int code;
    private String status;

    Status(int code, String status){
        this.code = code;
        this.status = status;
    }
}
