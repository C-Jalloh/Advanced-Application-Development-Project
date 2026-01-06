package com.example.Backend.Security.Configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Access images via: http://localhost:8080/images/poster.jpg
        // Ensure you create a folder named "uploads" in your project root
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:./uploads/", "file:./uploads/movies/", "file:./uploads/music/")
                .setCachePeriod(0);
    }
}
