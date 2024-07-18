package com.proyectointegrador.msplace.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakJwtAuthenticationConverter());

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("city/public/**").permitAll()
                        .requestMatchers("city/private/**").authenticated()
                        .requestMatchers("place/public/**").permitAll()
                        .requestMatchers("place/private/**").authenticated()
                        .requestMatchers("zone/public/**").permitAll()
                        .requestMatchers("zone/private/**").authenticated()
                        .requestMatchers("seat/public/**").permitAll()
                        .requestMatchers("seat/private/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter))
                );

        return http.build();
    }
}

