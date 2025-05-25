package com.project.musicwebbe.config;

import java.util.Arrays;
import java.util.List;

public class EnvUtils {

    public static String getEnv(String key, String defaultValue) {
        return System.getenv().getOrDefault(key, defaultValue);
    }

    public static List<String> getAllowedOrigins() {
        String origins = getEnv("CORS_ALLOWED_ORIGINS", "");
        return Arrays.stream(origins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
