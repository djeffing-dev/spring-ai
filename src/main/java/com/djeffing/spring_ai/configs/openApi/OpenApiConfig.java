package com.djeffing.spring_ai.configs.openApi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact =  @Contact(
                        name = "Jefferson Tsafack",
                        email = "tsafackjefferson2001@gmail.com",
                        url = "https://www.exemple.com"
                ),
                description = "Documentation openApi pour VIBRA AI",
                title = "VIBRA AI - Jefferson",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://www.licence.com"
                ),
                termsOfService = "Terms of services"
        ),
        servers = {
                @Server( // Environement Local
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @Server( // Environemnt de production
                        description = "PROD ENV",
                        url = "https://custom-spring-ai-api-service-828991456458.us-central1.run.app/"
                )
        },
        security = @SecurityRequirement(
                name = "bearerAuth"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type= SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
