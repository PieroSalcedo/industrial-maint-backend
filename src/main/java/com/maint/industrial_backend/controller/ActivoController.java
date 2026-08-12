package com.maint.industrial_backend.controller;

import com.maint.industrial_backend.entity.Activo;
import com.maint.industrial_backend.service.ActivoService;
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
@Tag(name = "02. Activos", description = "CRUD y Consultas de Maquinaria Industrial")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/url/activo")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class ActivoController {

    @Autowired
    private ActivoService activoService;

    @Operation(summary = "Listar todos los activos", description = "Retorna una lista completa de equipos sin filtros.")
    @GetMapping("/listaTodos")
    public ResponseEntity<List<Activo>> listaTodos() {
        return ResponseEntity.ok(activoService.listaTodos());
    }

    @Operation(summary = "Registrar nuevo activo", description = "Crea un equipo en la base de datos con auditoría inicial.")
    @PostMapping("/registraActivo")
    public ResponseEntity<Map<String, Object>> registra(@RequestBody Activo obj) {
        log.info(">>> registraActivo >>> " + obj.getNombre());
        Map<String, Object> salida = new HashMap<>();
        try {
            obj.setIdActivo(0); // Forzamos creación
            Activo objSalida = activoService.insertaActualizaActivo(obj);
            salida.put("mensaje", "Activo '" + objSalida.getNombre() + "' registrado correctamente.");
        } catch (Exception e) {
            log.error("Error en registro: " + e.getMessage());
            salida.put("mensaje", "Error: " + e.getMessage());
        }
        return ResponseEntity.ok(salida);
    }

    @Operation(summary = "Actualizar activo existente", description = "Actualiza los datos de un equipo. Se requiere el ID del activo.")
    @PutMapping("/actualizaActivo")
    public ResponseEntity<Map<String, Object>> actualiza(@RequestBody Activo obj) {
        log.info(">>> actualizaActivo >>> ID: " + obj.getIdActivo());
        Map<String, Object> salida = new HashMap<>();
        try {
            // Validamos que el ID exista para no crear uno nuevo por error
            if (obj.getIdActivo() == null || obj.getIdActivo() == 0) {
                salida.put("mensaje", "Error: ID de activo no válido.");
                return ResponseEntity.badRequest().body(salida);
            }

            Activo objSalida = activoService.insertaActualizaActivo(obj);
            salida.put("mensaje", "Activo '" + objSalida.getNombre() + "' actualizado correctamente.");
        } catch (Exception e) {
            log.error("Error en actualización: " + e.getMessage());
            salida.put("mensaje", "Error al actualizar: " + e.getMessage());
        }
        return ResponseEntity.ok(salida);
    }

    @Operation(summary = "Consulta Dinámica", description = "Búsqueda avanzada usando parámetros opcionales. Use -1 para omitir un filtro.")
    @GetMapping("/consultaDinamica")
    public ResponseEntity<List<Activo>> consulta(
            @Parameter(description = "Nombre parcial") @RequestParam(defaultValue = "") String vnombre,
            @Parameter(description = "Número de serie exacto") @RequestParam(defaultValue = "-1") String vserie,
            @Parameter(description = "ID del Tipo (DataCatalogo)") @RequestParam(defaultValue = "-1") int vtipo,
            @Parameter(description = "Estado (1: Activo, 0: Inactivo)") @RequestParam(defaultValue = "-1") int vestado){

        log.info(">>> consultaDinamica Activo >>> nombre: " + vnombre);
        List<Activo> lista = activoService.consultaDinamica("%" + vnombre.toLowerCase() + "%", vserie, vtipo, vestado);
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Eliminar activo", description = "Borrado físico del equipo por su ID.")
    @DeleteMapping("/eliminaActivo/{id}")
    public ResponseEntity<Map<String, Object>> elimina(@PathVariable int id) {
        log.info(">>> eliminaActivo >>> ID: " + id);
        Map<String, Object> salida = new HashMap<>();
        try {
            activoService.eliminaActivo(id);
            salida.put("mensaje", "Eliminación exitosa.");
            return ResponseEntity.ok(salida);
        } catch (Exception e) {
            log.error("Error al eliminar activo: " + e.getMessage());
            salida.put("mensaje", "No se puede eliminar: el activo tiene tickets registrados.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(salida);
        }
    }
}