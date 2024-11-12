package com.example.tarefas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                // Obtendo o domínio permitido das variáveis de ambiente
                String allowedOrigin = System.getenv("CORS_ALLOWED_ORIGIN");

                registry.addMapping("/api/**")
                        .allowedOrigins(
                            allowedOrigin != null ? allowedOrigin : "https://rodgers0091-7359ca357275.herokuapp.com", 
                            "http://localhost:3000" // Permite desenvolvimento local
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
