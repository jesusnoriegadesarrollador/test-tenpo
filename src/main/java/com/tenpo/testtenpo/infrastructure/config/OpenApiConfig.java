package com.tenpo.testtenpo.infrastructure.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Test Tenpo API")
                        .version("1.0")
                        .description("API REST para realizar cálculos de porcentaje dinámico, basandose en caché e historial de llamadas.")
                        .contact(new Contact()
                                .name("Backend Tenpo")
                                .email("backend_tempo@tenpo.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositorio GitHub")
                        .url("https://github.com/tu-repo/test-tenpo"));
    }
}
