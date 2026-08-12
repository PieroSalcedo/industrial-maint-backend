package com.maint.industrial_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ActivoResponseDTO(
        Integer idActivo,
        String nombre,
        String numeroSerie,
        DataCatalogoDTO tipoActivo,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime fechaRegistro,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime fechaActualizacion,
        UsuarioResumenDTO usuarioRegistro,
        UsuarioResumenDTO usuarioActualiza,
        Integer estado
) {}
