package com.maint.industrial_backend.service;

import com.maint.industrial_backend.entity.Activo;

import java.util.List;

public interface ActivoService {

    public abstract List<Activo> listaTodos();
    public abstract Activo insertaActualizaActivo(Activo obj);
    public abstract void eliminaActivo(int id);

    // consulta dinámica con filtros opcionales.
    public abstract List<Activo> consultaDinamica(String nombre, String serie, int tipo, int estado);
}
