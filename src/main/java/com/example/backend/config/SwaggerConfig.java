package com.example.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hazar Backend API")
                        .description("REST API for Hazar marketplace application")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@hazar.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                // BURAYI GÜNCELLEDİK:
                .servers(List.of(
                        // 1. Azure Sunucusu (Varsayılan olarak bu seçili gelir)
                        new Server()
                                .url("https://hazar-b9hzdcdwbjdneaga.canadacentral-01.azurewebsites.net")
                                .description("Azure Production Server"),
                        
                        // 2. Localhost (Kendi bilgisayarında geliştirme yaparken kullanırsın)
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development")
                ));
    }
}