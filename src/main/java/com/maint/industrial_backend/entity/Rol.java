package com.maint.industrial_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter 
@Entity 
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;
    private String nombre;
    private String descripcion;
    private Integer estado;
}