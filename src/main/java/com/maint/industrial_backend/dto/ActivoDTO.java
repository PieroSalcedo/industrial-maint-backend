package com.maint.industrial_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Usado para Registrar y Actualizar activos.
// Solo enviamos los IDs de las relaciones para no sobrecargar el JSON.
public record ActivoDTO(
        Integer idActivo,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El número de serie es obligatorio")
        String numeroSerie,

        @NotNull(message = "El tipo de activo es obligatorio")
        Integer idTipoActivo, // FK a DataCatalogo

        Integer idUsuarioRegistro // Para la auditoría
) {}
