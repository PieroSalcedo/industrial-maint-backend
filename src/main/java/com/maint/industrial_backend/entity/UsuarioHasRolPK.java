package com.maint.industrial_backend.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class UsuarioHasRolPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "id_rol")
    private int idRol;
}