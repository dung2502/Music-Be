package com.project.musicwebbe.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Arrays;
import java.util.List;

public class EnvUtils {
    private static final Dotenv dotenv = Dotenv.load();

    public static List<String> getAllowedOrigins() {
        String origins = dotenv.get("CORS_ALLOWED_ORIGINS", "");
        return Arrays.stream(origins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
