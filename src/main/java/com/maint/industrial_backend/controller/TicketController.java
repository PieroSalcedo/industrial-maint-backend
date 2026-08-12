package com.maint.industrial_backend.controller;

import com.maint.industrial_backend.dto.MensajeDTO;
import com.maint.industrial_backend.dto.TicketCreateRequestDTO;
import com.maint.industrial_backend.dto.TicketResponseDTO;
import com.maint.industrial_backend.dto.TicketUpdateRequestDTO;
import com.maint.industrial_backend.mapper.TicketMapper;
import com.maint.industrial_backend.security.SecurityUtils;
import com.maint.industrial_backend.service.TicketService;
import com.maint.industrial_backend.util.AppSettings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CommonsLog
@Tag(name = "03. Mantenimiento", description = "Gestión de Tickets y Órdenes de Trabajo")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/url/ticket")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketMapper ticketMapper;

    @Operation(summary = "Registrar Ticket")
    @PostMapping("/registraTicket")
    public ResponseEntity<MensajeDTO> registra(@Valid @RequestBody TicketCreateRequestDTO dto) {
        var guardado = ticketService.registraTicket(ticketMapper.toEntity(dto));
        return ResponseEntity.ok(new MensajeDTO(
                "Ticket ID " + guardado.getIdTicket() + " creado exitosamente."));
    }

    @Operation(summary = "Consulta avanzada de Tickets")
    @GetMapping("/consultaDinamica")
    public ResponseEntity<List<TicketResponseDTO>> consulta(
            @Parameter(description = "Texto descriptivo") @RequestParam(defaultValue = "") String vdesc,
            @Parameter(description = "ID del Activo") @RequestParam(defaultValue = "-1") int vactivo,
            @Parameter(description = "ID de Prioridad") @RequestParam(defaultValue = "-1") int vprioridad,
            @Parameter(description = "ID de Estado") @RequestParam(defaultValue = "-1") int vestado,
            @Parameter(description = "ID Técnico") @RequestParam(defaultValue = "-1") int vtecnico,
            @Parameter(description = "ID Tipo de Activo") @RequestParam(defaultValue = "-1") int vtipoActivo,
            @Parameter(description = "1=pendientes, 0=cerrados, -1=todos") @RequestParam(defaultValue = "-1") int vpendientes,
            @Parameter(description = "Fecha desde (yyyy-MM-dd) o -1") @RequestParam(defaultValue = "-1") String vfechaDesde,
            @Parameter(description = "Fecha hasta (yyyy-MM-dd) o -1") @RequestParam(defaultValue = "-1") String vfechaHasta) {

        int filtroTecnico = SecurityUtils.isAdmin() ? vtecnico : -1;

        List<TicketResponseDTO> lista = ticketService.consultaDinamica(
                "%" + vdesc.toLowerCase() + "%", vactivo, vprioridad, vestado, filtroTecnico,
                vtipoActivo, vpendientes, vfechaDesde, vfechaHasta).stream()
                .map(ticketMapper::toResponse)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Actualizar ticket")
    @PutMapping("/actualizaTicket")
    public ResponseEntity<MensajeDTO> actualiza(@Valid @RequestBody TicketUpdateRequestDTO dto) {
        log.info(">>> actualizaTicket >>> ID: " + dto.idTicket());
        var actualizado = ticketService.actualizaTicket(ticketMapper.toEntity(dto));
        return ResponseEntity.ok(new MensajeDTO(
                "Ticket ID " + actualizado.getIdTicket() + " actualizado correctamente."));
    }

    @Operation(summary = "Eliminar ticket")
    @DeleteMapping("/eliminaTicket/{id}")
    public ResponseEntity<MensajeDTO> elimina(@PathVariable int id) {
        log.info(">>> eliminaTicket >>> ID: " + id);
        ticketService.eliminaTicket(id);
        return ResponseEntity.ok(new MensajeDTO("Eliminación exitosa."));
    }
}
