package com.maint.industrial_backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API MaintPro - Gestión de Mantenimiento Industrial",
                version = "1.0",
                description = "Plataforma para el control de activos críticos y órdenes de mantenimiento preventivo.",
                contact = @Contact(name = "Piero Salcedo", email = "piero.salcedo@maintpro.com")
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Ingrese el token JWT obtenido en el login para acceder a los endpoints protegidos."
)
public class OpenApiConfig {
}