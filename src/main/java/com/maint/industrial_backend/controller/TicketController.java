package com.maint.industrial_backend.controller;

import com.maint.industrial_backend.entity.TicketMantenimiento;
import com.maint.industrial_backend.service.TicketService;
import com.maint.industrial_backend.util.AppSettings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CommonsLog
@Tag(name = "03. Mantenimiento", description = "Gestión de Tickets y Órdenes de Trabajo")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/url/ticket")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Operation(summary = "Registrar Ticket", description = "Crea una orden de mantenimiento para un activo crítico.")
    @PostMapping("/registraTicket")
    public ResponseEntity<Map<String, Object>> registra(@RequestBody TicketMantenimiento obj) {
        Map<String, Object> salida = new HashMap<>();
        try {
            TicketMantenimiento objSalida = ticketService.registraTicket(obj);
            salida.put("mensaje", "Ticket ID " + objSalida.getIdTicket() + " creado exitosamente.");
        } catch (Exception e) {
            salida.put("mensaje", "Error: " + e.getMessage());
        }
        return ResponseEntity.ok(salida);
    }

    @Operation(summary = "Consulta avanzada de Tickets", description = "Filtra el historial de mantenimiento por diversos criterios.")
    @GetMapping("/consultaDinamica")
    public ResponseEntity<List<TicketMantenimiento>> consulta(
            @Parameter(description = "Texto descriptivo") @RequestParam(defaultValue = "") String vdesc,
            @Parameter(description = "ID del Activo") @RequestParam(defaultValue = "-1") int vactivo,
            @Parameter(description = "ID de Prioridad (DataCatalogo)") @RequestParam(defaultValue = "-1") int vprioridad,
            @Parameter(description = "ID de Estado (DataCatalogo)") @RequestParam(defaultValue = "-1") int vestado) {

        List<TicketMantenimiento> lista = ticketService.consultaDinamica("%" + vdesc.toLowerCase() + "%", vactivo, vprioridad, vestado);
        return ResponseEntity.ok(lista);
    }
}