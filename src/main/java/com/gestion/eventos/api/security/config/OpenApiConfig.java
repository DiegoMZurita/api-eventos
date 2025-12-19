package com.gestion.eventos.api.security.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestión de Eventos API")
                        .version("1.0")
                        .description("API RESTful para le gestión de eventos, categorías y ponenetes")
                        .contact(new Contact()
                                .name("Diego Martínez")
                                .email("diego.marzurita@gmail.com")
                                .url("https://www.linkedin.com/in/diego-mart%C3%ADnez-zurita-0b8200367/"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")
                        )

                );
    }
}
