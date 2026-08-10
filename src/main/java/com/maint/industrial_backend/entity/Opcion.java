package com.maint.industrial_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter 
@Entity
@Table(name = "opcion")
public class Opcion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion")
    private Integer idOpcion;
    private String nombre;
    private String ruta;
    private Integer tipo;
    private Integer estado;
}