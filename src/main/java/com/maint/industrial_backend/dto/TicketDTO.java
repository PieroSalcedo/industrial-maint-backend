package com.maint.industrial_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketDTO(
        Integer idTicket,

        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,

        @NotNull(message = "Debe asignar un activo")
        Integer idActivo,

        @NotNull(message = "Debe definir la prioridad")
        Integer idPrioridad, // FK a DataCatalogo

        @NotNull(message = "Debe definir el estado inicial")
        Integer idEstadoTicket, // FK a DataCatalogo

        Integer idUsuarioTecnico, // Puede ser nulo al inicio
        Integer idUsuarioRegistro // Auditoría: quién reporta
) {}
