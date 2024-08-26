package com.bible.app;

import java.util.EnumSet;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.SessionTrackingMode;

@Configuration
public class SessionConfig {

    @Bean
    public ServletContextInitializer initializer() {
        return servletContext -> {
            servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
        };
    }
}