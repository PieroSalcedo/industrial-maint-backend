package com.maint.industrial_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketCreateRequestDTO(
        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,

        @NotNull(message = "Debe asignar un activo")
        Integer idActivo,

        @NotNull(message = "Debe definir la prioridad")
        Integer idPrioridad,

        Integer idEstadoTicket,

        Integer idUsuarioTecnico,

        @NotNull(message = "El usuario de registro es obligatorio")
        Integer idUsuarioRegistro
) {}
