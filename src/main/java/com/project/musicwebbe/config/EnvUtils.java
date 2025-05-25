package com.project.musicwebbe.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Arrays;
import java.util.List;

public class EnvUtils {
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    public static String getEnv(String key) {
        String value = System.getenv(key);
        return value != null ? value : dotenv.get(key);
    }


    public static List<String> getAllowedOrigins() {
        String origins = dotenv.get("CORS_ALLOWED_ORIGINS", "");
        return Arrays.stream(origins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
