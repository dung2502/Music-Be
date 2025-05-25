package com.project.musicwebbe.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Arrays;
import java.util.List;

public class EnvUtils {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    public static String getEnv(String key, String defaultValue) {
        String value = dotenv.get(key);
        return value != null ? value : System.getenv().getOrDefault(key, defaultValue);
    }

    public static List<String> getAllowedOrigins() {
        String origins = getEnv("CORS_ALLOWED_ORIGINS", "");
        return Arrays.stream(origins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
