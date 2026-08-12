package com.maint.industrial_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    private String nombres;
    private String apellidos;
    private String dni;
    private String login;
    @JsonIgnore
    private String password;
    private String correo;
    private Integer estado;
    
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}