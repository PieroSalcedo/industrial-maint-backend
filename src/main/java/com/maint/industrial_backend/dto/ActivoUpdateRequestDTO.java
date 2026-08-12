package com.maint.industrial_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActivoUpdateRequestDTO(
        @NotNull(message = "El ID del activo es obligatorio")
        Integer idActivo,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El número de serie es obligatorio")
        String numeroSerie,

        @NotNull(message = "El tipo de activo es obligatorio")
        Integer idTipoActivo,

        @NotNull(message = "El estado es obligatorio")
        Integer estado
) {}
