package com.bootifulmicropizza.gateway.website.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedMethods(HttpMethod.GET.name(),
                            HttpMethod.PUT.name(),
                            HttpMethod.DELETE.name(),
                            HttpMethod.OPTIONS.name(),
                            HttpMethod.POST.name(),
                            HttpMethod.HEAD.name())
            .allowedOrigins("*");
//            .allowedOrigins("http://localhost:4200", "https://www.bootifulmicropizza.com");
    }
}
