package com.rider.companion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI riderCompanionOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Rider Companion API")
                .version("v1")
                .description("REST API for managing motorcycles, maintenance, and rides.")
                .contact(new Contact().name("Rider Companion team"))
                .license(new License().name("Private")));
  }
}
