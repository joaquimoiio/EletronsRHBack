package com.empresa.sistemarh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Servir arquivos de upload
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        // Servir arquivos estáticos do frontend
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}