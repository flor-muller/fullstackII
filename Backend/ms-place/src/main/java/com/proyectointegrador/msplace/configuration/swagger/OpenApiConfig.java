package com.proyectointegrador.msplace.configuration.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API de Microservicio de Estadio")
                        .version("1.0")
                        .description("Documentación de la API para el Microservicio de Eventos")
                );
    }
}
