package com.fifa.footballApp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Component
public class WebConfig extends OncePerRequestFilter {

//    @Value( "${cors.origins}" )
//    private String corsOrigins;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain )
            throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000" );
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE" );
        response.setHeader("Access-Control-Max-Age", "3600" );
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Content-Length, X-Requested-With" );
        response.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter( request, response );
    }
}

