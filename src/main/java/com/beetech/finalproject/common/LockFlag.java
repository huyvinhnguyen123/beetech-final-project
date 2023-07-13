package com.beetech.finalproject.common;

import lombok.Getter;

/**
 * Define enum for Delete flag
 */
@Getter
public enum LockFlag {
    NON_LOCK(0),
    LOCKED(1);

    private int code;

    LockFlag(int code) {
        this.code = code;
    }
}
