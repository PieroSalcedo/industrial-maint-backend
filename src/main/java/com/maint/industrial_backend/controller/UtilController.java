package com.maint.industrial_backend.controller;

import com.maint.industrial_backend.entity.DataCatalogo;
import com.maint.industrial_backend.entity.Usuario;
import com.maint.industrial_backend.service.UtilService;
import com.maint.industrial_backend.util.AppSettings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "04. Utilidades", description = "Carga de catálogos para formularios")
@RestController
@RequestMapping("/url/util")
public class UtilController {

    @Autowired
    private UtilService service;

    @Operation(summary = "Listar tipo de activo", description = "Lista los tipos de activos.")
    @GetMapping("/listaTipoActivo")
    public ResponseEntity<List<DataCatalogo>> listaTipoActivo() {
        return ResponseEntity.ok(service.listaDataCatalogo(AppSettings.CATALOGO_TIPO_ACTIVO));
    }

    @Operation(summary = "Listar prioridad ", description = "Lista prioridades.")
    @GetMapping("/listaPrioridad")
    public ResponseEntity<List<DataCatalogo>> listaPrioridad() {
        return ResponseEntity.ok(service.listaDataCatalogo(AppSettings.CATALOGO_PRIORIDAD));
    }

    @Operation(summary = "Listar estado del ticket", description = "Lista estado del ticker.")
    @GetMapping("/listaEstadoTicket")
    public ResponseEntity<List<DataCatalogo>> listaEstadoTicket() {
        return ResponseEntity.ok(service.listaDataCatalogo(AppSettings.CATALOGO_ESTADO_TICKET));
    }

    @Operation(summary = "Listar técnicos", description = "Lista técnicos activos para asignación de tickets.")
    @GetMapping("/listaTecnico")
    public ResponseEntity<List<Usuario>> listaTecnico() {
        return ResponseEntity.ok(service.listaTecnico());
    }
}