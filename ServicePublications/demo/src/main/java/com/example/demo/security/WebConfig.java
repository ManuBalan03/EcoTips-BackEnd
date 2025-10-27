package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permitir solicitudes de cualquier origen
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:5173",  // Frontend web (Vite/React)
                        "http://localhost:8081",  // React Native/Expo (Metro Bundler)
                        "exp://192.168.x.x:8081",  // Para dispositivos en red local (opcional)
                        "http://localhost:8080"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Los métodos que deseas permitir
                .allowedHeaders("*") // Permitir todos los encabezados
                .allowCredentials(true);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173"); // Origen de tu frontend
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
