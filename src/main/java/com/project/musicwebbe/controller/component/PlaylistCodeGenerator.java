package com.project.musicwebbe.controller.component;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PlaylistCodeGenerator {
    private static final String PREFIX = "MUSIC-";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String generate() {
        Random random = new Random();
        StringBuilder result = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            result.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return PREFIX + result;
    }
}
