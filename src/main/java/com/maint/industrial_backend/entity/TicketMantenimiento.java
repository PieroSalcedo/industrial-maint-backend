package com.maint.industrial_backend.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ticket_mantenimiento")
public class TicketMantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Integer idTicket;
    
    private String descripcion;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_activo")
    private Activo activo;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prioridad")
    private DataCatalogo prioridad;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_ticket")
    private DataCatalogo estadoTicket;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_tecnico")
    private Usuario tecnico;

    // AUDITORÍA
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro")
    private Usuario usuarioRegistro;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_actualiza")
    private Usuario usuarioActualiza;

    @PrePersist
    public void prePersist() { fechaRegistro = LocalDateTime.now(); fechaActualizacion = LocalDateTime.now(); }
    
    @PreUpdate
    public void preUpdate() { fechaActualizacion = LocalDateTime.now(); }
}