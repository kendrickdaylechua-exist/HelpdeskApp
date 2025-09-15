package com.exist.HelpdeskApp.util;

public class StringConverters {
    public static String likePattern(String value) {
        return "%" + value.toLowerCase() + "%";
    }
}
