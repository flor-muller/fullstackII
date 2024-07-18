package com.proyectointegrador.msusers.configuration;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakClient {
    @Value("${backend.keycloak.serverUrl}")
    private String serverUrl;

    @Value("${backend.keycloak.realm}")
    private String realm;

    @Value("${backend.keycloak.clientId}")
    private String clientId;

    @Value("${backend.keycloak.clientSecret}")
    private String clientSecret;

    @Bean
    public Keycloak buildClient(){
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
}
