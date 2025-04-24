package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI projectServiceOpenApi() {
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server().url("http://localhost:8082/swagger-ui/index.html#/")
                        )
                )
                .info(new Info()
                        .title("N-th Minimum Service")
                        .version("1.0")
                        .description("API для поиска N-ного минимального числа в XLSX"));
    }
}
