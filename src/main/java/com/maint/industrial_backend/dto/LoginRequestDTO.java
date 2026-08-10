package com.maint.industrial_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

// Captura las credenciales del formulario de Angular.
public record LoginRequestDTO(
        @NotBlank(message = "El login no puede estar vacío")
        String login,

        @NotBlank(message = "La contraseña no puede estar vacía")
        String password
) {}
