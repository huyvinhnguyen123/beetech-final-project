package com.beetech.finalproject.utils;

import java.util.UUID;

public class CustomGenerate {
    public static String generateRandomString(int length) {
        String randomString = UUID.randomUUID().toString().replace("-", "");
        return randomString.substring(0, length);
    }
}
