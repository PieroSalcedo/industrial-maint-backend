package com.maint.industrial_backend.exception;

import com.maint.industrial_backend.dto.MensajeDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensajeDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Datos de entrada inválidos.");
        return ResponseEntity.badRequest().body(new MensajeDTO(mensaje));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MensajeDTO> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new MensajeDTO(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<MensajeDTO> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MensajeDTO("No se puede eliminar: el activo tiene tickets registrados."));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<MensajeDTO> handleIllegalState(IllegalStateException ex) {
        HttpStatus status = esErrorAutorizacion(ex.getMessage())
                ? HttpStatus.FORBIDDEN
                : HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new MensajeDTO(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensajeDTO> handleAllExceptions(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(new MensajeDTO("Error interno en el servidor."));
    }

    private boolean esErrorAutorizacion(String mensaje) {
        if (mensaje == null) {
            return false;
        }
        return mensaje.contains("No autorizado")
                || mensaje.contains("Solo el supervisor");
    }
}
