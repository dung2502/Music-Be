package com.project.musicwebbe;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicWebBeApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        // Đẩy các biến env vào System properties
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(MusicWebBeApplication.class, args);
    }
}
