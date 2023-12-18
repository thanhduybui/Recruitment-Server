package com.edu.hcmute.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private Environment env;
    @Value("${app.security.cors.allowedMethods}")
    private String allowedMethods;

    @Value("${app.security.cors.allowedHeaders}")
    private String allowedHeaders;

    @Value("${app.security.cors.corsConfiguration}")
    private String corsConfiguration;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(corsConfiguration)
                .allowedMethods(allowedMethods)
                .allowedHeaders(allowedHeaders);
    }

}
