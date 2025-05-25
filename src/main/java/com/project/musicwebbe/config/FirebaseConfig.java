package com.project.musicwebbe.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Configuration
//public class FirebaseConfig {
//
//    @PostConstruct
//    public void initialize() throws IOException {
//        FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-key/logingo-4d97c-firebase-adminsdk-666z6-63f42e4eb8.json");
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .build();
//        FirebaseApp.initializeApp(options);
//    }
//}

//public class FirebaseConfig {
//
//    @PostConstruct
//    public void initialize() throws IOException {
//        Dotenv dotenv = Dotenv.load();  // Load biến từ file .env
//        String firebasePath = dotenv.get("FIREBASE_CREDENTIAL_PATH");
//
//        FileInputStream serviceAccount = new FileInputStream(firebasePath);
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .build();
//
//        FirebaseApp.initializeApp(options);
//    }
//}

public class FirebaseConfig {

    @PostConstruct
    public void initialize() throws Exception {
        String firebaseJson = EnvUtils.getEnv("FIREBASE_CREDENTIAL", null);
        if (firebaseJson == null) {
            throw new RuntimeException("FIREBASE_CREDENTIAL env var not found");
        }

        InputStream serviceAccount = new ByteArrayInputStream(firebaseJson.getBytes(StandardCharsets.UTF_8));
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }
}



