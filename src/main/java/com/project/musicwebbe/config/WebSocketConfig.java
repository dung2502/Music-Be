package com.project.musicwebbe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebSocketConfig implements WebMvcConfigurer {

    /**
     * Configures CORS mapping to allow cross-origin requests to the WebSocket endpoints.
     * This method maps the specified path pattern to the allowed origins, methods, headers,
     * and credentials for cross-origin requests.
     *
     * @param registry the CorsRegistry to configure.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/ws/**")
                .allowedOrigins(EnvUtils.getAllowedOrigins().toArray(new String[0]))
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
