package com.maint.industrial_backend.controller;

import com.maint.industrial_backend.entity.TicketMantenimiento;
import com.maint.industrial_backend.security.SecurityUtils;
import com.maint.industrial_backend.service.TicketService;
import com.maint.industrial_backend.util.AppSettings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Registrar Ticket", description = "Solo supervisor. Crea ticket y marca activo fuera de servicio.")
    @PostMapping("/registraTicket")
    public ResponseEntity<Map<String, Object>> registra(@RequestBody TicketMantenimiento obj) {
        Map<String, Object> salida = new HashMap<>();
        try {
            TicketMantenimiento objSalida = ticketService.registraTicket(obj);
            salida.put("mensaje", "Ticket ID " + objSalida.getIdTicket() + " creado exitosamente.");
            return ResponseEntity.ok(salida);
        } catch (IllegalStateException e) {
            salida.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(salida);
        } catch (Exception e) {
            salida.put("mensaje", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(salida);
        }
    }

    @Operation(summary = "Consulta avanzada de Tickets", description = "Supervisor ve todos; técnico solo sus asignados.")
    @GetMapping("/consultaDinamica")
    public ResponseEntity<List<TicketMantenimiento>> consulta(
            @Parameter(description = "Texto descriptivo") @RequestParam(defaultValue = "") String vdesc,
            @Parameter(description = "ID del Activo") @RequestParam(defaultValue = "-1") int vactivo,
            @Parameter(description = "ID de Prioridad (DataCatalogo)") @RequestParam(defaultValue = "-1") int vprioridad,
            @Parameter(description = "ID de Estado (DataCatalogo)") @RequestParam(defaultValue = "-1") int vestado,
            @Parameter(description = "ID Técnico (-1 = todos, solo supervisor)") @RequestParam(defaultValue = "-1") int vtecnico,
            @Parameter(description = "ID Tipo de Activo (-1 = todos)") @RequestParam(defaultValue = "-1") int vtipoActivo,
            @Parameter(description = "1=solo pendientes, 0=solo cerrados, -1=todos") @RequestParam(defaultValue = "-1") int vpendientes,
            @Parameter(description = "Fecha desde (yyyy-MM-dd) o -1") @RequestParam(defaultValue = "-1") String vfechaDesde,
            @Parameter(description = "Fecha hasta (yyyy-MM-dd) o -1") @RequestParam(defaultValue = "-1") String vfechaHasta) {

        int filtroTecnico = SecurityUtils.isAdmin() ? vtecnico : -1;

        List<TicketMantenimiento> lista = ticketService.consultaDinamica(
                "%" + vdesc.toLowerCase() + "%", vactivo, vprioridad, vestado, filtroTecnico,
                vtipoActivo, vpendientes, vfechaDesde, vfechaHasta);
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Actualizar ticket", description = "Supervisor: todos los campos. Técnico: solo estado en tickets asignados.")
    @PutMapping("/actualizaTicket")
    public ResponseEntity<Map<String, Object>> actualiza(@RequestBody TicketMantenimiento obj) {
        log.info(">>> actualizaTicket >>> ID: " + obj.getIdTicket());
        Map<String, Object> salida = new HashMap<>();
        try {
            if (obj.getIdTicket() == null || obj.getIdTicket() == 0) {
                salida.put("mensaje", "Error: ID de ticket no válido.");
                return ResponseEntity.badRequest().body(salida);
            }
            TicketMantenimiento objSalida = ticketService.actualizaTicket(obj);
            salida.put("mensaje", "Ticket ID " + objSalida.getIdTicket() + " actualizado correctamente.");
            return ResponseEntity.ok(salida);
        } catch (IllegalStateException e) {
            salida.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(salida);
        } catch (Exception e) {
            log.error("Error en actualización: " + e.getMessage());
            salida.put("mensaje", "Error al actualizar: " + e.getMessage());
            return ResponseEntity.badRequest().body(salida);
        }
    }

    @Operation(summary = "Eliminar ticket", description = "Solo supervisor. Reevalúa disponibilidad del activo.")
    @DeleteMapping("/eliminaTicket/{id}")
    public ResponseEntity<Map<String, Object>> elimina(@PathVariable int id) {
        log.info(">>> eliminaTicket >>> ID: " + id);
        Map<String, Object> salida = new HashMap<>();
        try {
            ticketService.eliminaTicket(id);
            salida.put("mensaje", "Eliminación exitosa.");
            return ResponseEntity.ok(salida);
        } catch (IllegalStateException e) {
            salida.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(salida);
        } catch (Exception e) {
            salida.put("mensaje", "Error: No se pudo eliminar el ticket.");
            return ResponseEntity.badRequest().body(salida);
        }
    }
}
