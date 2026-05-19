package com.carla.contentinsighttool.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Auth handled by Spring Security + JWT — see SecurityConfig
}