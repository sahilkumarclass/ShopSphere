package com.shopsphere.notification.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ShopSphere Notification Service API")
                        .description("Consumes Kafka events and sends emails via SMTP (MailHog in dev).")
                        .version("v1"));
    }
}
