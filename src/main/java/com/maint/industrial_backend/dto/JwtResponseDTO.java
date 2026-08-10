package com.maint.industrial_backend.dto;

import com.maint.industrial_backend.entity.Opcion;

import java.util.List;

// Esta es la "llave" que el Backend le entrega al Frontend.
// Incluimos las opciones para que Angular construya el menú dinámicamente.
public record JwtResponseDTO(
        String token,
        String bearer,
        String login,
        String nombreCompleto,
        List<String> roles,
        List<Opcion> opciones
) {}
