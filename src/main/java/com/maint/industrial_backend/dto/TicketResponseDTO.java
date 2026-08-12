package com.maint.industrial_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TicketResponseDTO(
        Integer idTicket,
        String descripcion,
        ActivoResumenDTO activo,
        DataCatalogoDTO prioridad,
        DataCatalogoDTO estadoTicket,
        UsuarioResumenDTO tecnico,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime fechaRegistro,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime fechaActualizacion,
        UsuarioResumenDTO usuarioRegistro,
        UsuarioResumenDTO usuarioActualiza
) {}
