package com.maint.industrial_backend.dto;

public record UsuarioResumenDTO(
        Integer idUsuario,
        String nombres,
        String apellidos,
        String nombreCompleto
) {}
