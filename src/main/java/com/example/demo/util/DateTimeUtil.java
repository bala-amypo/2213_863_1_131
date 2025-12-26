package com.example.demo.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC"));

    public static String format(Instant instant) {
        if (instant == null) return null;
        return ISO_FORMATTER.format(instant);
    }
    
    public static Instant now() {
        return Instant.now();
    }
}
