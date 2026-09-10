package org.example.inplrz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // Разрешаем запросы с любых адресов (localhost:3000, 5173 и т.д.)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // Разрешаем все базовые HTTP-методы
                .allowedHeaders("*") // Разрешаем любые заголовки
                .allowCredentials(true); // Разрешаем передачу куки и токенов авторизации
    }
}