package com.geovannycode.nisum.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPI3Configuration {

    @Value("${swagger.api-url}")
    String apiUrl;

    @Bean
    OpenAPI openApi() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(
                                "JWT",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Nisum APIs")
                        .description("Nisum Users Service APIs")
                        .version("v1.0.0")
                        .contact(new Contact().name("Geovanny Mendoza").email("me@geovannycode.com")))
                .addSecurityItem(new SecurityRequirement().addList("JWT", Arrays.asList("read", "write")))
                .servers(List.of(new Server().url(apiUrl)));
    }
}
