package com.beetech.finalproject.common;

import lombok.Getter;

/**
 * Define enum for Delete flag
 */
@Getter
public enum DeleteFlag {
    NON_DELETE(0),
    DELETED(1);

    private int code;

    DeleteFlag(int code) {
        this.code = code;
    }
}
