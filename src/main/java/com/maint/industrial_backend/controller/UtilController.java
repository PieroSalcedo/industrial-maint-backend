package com.maint.industrial_backend.controller;

import com.maint.industrial_backend.dto.DataCatalogoDTO;
import com.maint.industrial_backend.dto.UsuarioResumenDTO;
import com.maint.industrial_backend.mapper.TicketMapper;
import com.maint.industrial_backend.service.UtilService;
import com.maint.industrial_backend.util.AppSettings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "04. Utilidades", description = "Carga de catálogos para formularios")
@RestController
@RequestMapping("/url/util")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class UtilController {

    @Autowired
    private UtilService service;

    @Autowired
    private TicketMapper ticketMapper;

    @Operation(summary = "Listar tipo de activo")
    @GetMapping("/listaTipoActivo")
    public ResponseEntity<List<DataCatalogoDTO>> listaTipoActivo() {
        return ResponseEntity.ok(service.listaDataCatalogo(AppSettings.CATALOGO_TIPO_ACTIVO).stream()
                .map(ticketMapper::toCatalogoDto)
                .toList());
    }

    @Operation(summary = "Listar prioridad")
    @GetMapping("/listaPrioridad")
    public ResponseEntity<List<DataCatalogoDTO>> listaPrioridad() {
        return ResponseEntity.ok(service.listaDataCatalogo(AppSettings.CATALOGO_PRIORIDAD).stream()
                .map(ticketMapper::toCatalogoDto)
                .toList());
    }

    @Operation(summary = "Listar estado del ticket")
    @GetMapping("/listaEstadoTicket")
    public ResponseEntity<List<DataCatalogoDTO>> listaEstadoTicket() {
        return ResponseEntity.ok(service.listaDataCatalogo(AppSettings.CATALOGO_ESTADO_TICKET).stream()
                .map(ticketMapper::toCatalogoDto)
                .toList());
    }

    @Operation(summary = "Listar técnicos")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/listaTecnico")
    public ResponseEntity<List<UsuarioResumenDTO>> listaTecnico() {
        return ResponseEntity.ok(service.listaTecnico().stream()
                .map(ticketMapper::toUsuarioResumen)
                .toList());
    }
}
