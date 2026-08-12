package com.maint.industrial_backend.dto;

public record ActivoResumenDTO(
        Integer idActivo,
        String nombre,
        String numeroSerie,
        DataCatalogoDTO tipoActivo,
        Integer estado
) {}
