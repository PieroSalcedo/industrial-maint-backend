package com.maint.industrial_backend.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "rol_has_opcion")
public class RolHasOpcion {

    @EmbeddedId
    private RolHasOpcionPK rolHasOpcionPK;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false, insertable = false, updatable = false)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "id_opcion", nullable = false, insertable = false, updatable = false)
    private Opcion opcion;
}
