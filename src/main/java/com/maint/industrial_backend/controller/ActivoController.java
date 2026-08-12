package com.maint.industrial_backend.controller;

import com.maint.industrial_backend.dto.ActivoCreateRequestDTO;
import com.maint.industrial_backend.dto.ActivoResponseDTO;
import com.maint.industrial_backend.dto.ActivoUpdateRequestDTO;
import com.maint.industrial_backend.dto.MensajeDTO;
import com.maint.industrial_backend.mapper.ActivoMapper;
import com.maint.industrial_backend.service.ActivoService;
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
@Tag(name = "02. Activos", description = "CRUD y Consultas de Maquinaria Industrial")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/url/activo")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class ActivoController {

    @Autowired
    private ActivoService activoService;

    @Autowired
    private ActivoMapper activoMapper;

    @Operation(summary = "Listar todos los activos")
    @GetMapping("/listaTodos")
    public ResponseEntity<List<ActivoResponseDTO>> listaTodos() {
        List<ActivoResponseDTO> lista = activoService.listaTodos().stream()
                .map(activoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Registrar nuevo activo")
    @PostMapping("/registraActivo")
    public ResponseEntity<MensajeDTO> registra(@Valid @RequestBody ActivoCreateRequestDTO dto) {
        log.info(">>> registraActivo >>> " + dto.nombre());
        var guardado = activoService.insertaActualizaActivo(activoMapper.toEntity(dto));
        return ResponseEntity.ok(new MensajeDTO(
                "Activo '" + guardado.getNombre() + "' registrado correctamente."));
    }

    @Operation(summary = "Actualizar activo existente")
    @PutMapping("/actualizaActivo")
    public ResponseEntity<MensajeDTO> actualiza(@Valid @RequestBody ActivoUpdateRequestDTO dto) {
        log.info(">>> actualizaActivo >>> ID: " + dto.idActivo());
        var actualizado = activoService.insertaActualizaActivo(activoMapper.toEntity(dto));
        return ResponseEntity.ok(new MensajeDTO(
                "Activo '" + actualizado.getNombre() + "' actualizado correctamente."));
    }

    @Operation(summary = "Consulta Dinámica")
    @GetMapping("/consultaDinamica")
    public ResponseEntity<List<ActivoResponseDTO>> consulta(
            @Parameter(description = "Nombre parcial") @RequestParam(defaultValue = "") String vnombre,
            @Parameter(description = "Número de serie exacto") @RequestParam(defaultValue = "-1") String vserie,
            @Parameter(description = "ID del Tipo (DataCatalogo)") @RequestParam(defaultValue = "-1") int vtipo,
            @Parameter(description = "Estado (1: Operativo, 0: Fuera de servicio)") @RequestParam(defaultValue = "-1") int vestado) {

        log.info(">>> consultaDinamica Activo >>> nombre: " + vnombre);
        List<ActivoResponseDTO> lista = activoService.consultaDinamica(
                        "%" + vnombre.toLowerCase() + "%", vserie, vtipo, vestado).stream()
                .map(activoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Eliminar activo")
    @DeleteMapping("/eliminaActivo/{id}")
    public ResponseEntity<MensajeDTO> elimina(@PathVariable int id) {
        log.info(">>> eliminaActivo >>> ID: " + id);
        activoService.eliminaActivo(id);
        return ResponseEntity.ok(new MensajeDTO("Eliminación exitosa."));
    }
}
