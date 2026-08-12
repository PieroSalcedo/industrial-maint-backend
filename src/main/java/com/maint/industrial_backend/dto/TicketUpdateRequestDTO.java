package com.maint.industrial_backend.dto;

import jakarta.validation.constraints.NotNull;

public record TicketUpdateRequestDTO(
        @NotNull(message = "El ID del ticket es obligatorio")
        Integer idTicket,

        String descripcion,
        Integer idPrioridad,
        Integer idEstadoTicket,
        Integer idUsuarioTecnico,
        Integer idUsuarioActualiza
) {}
