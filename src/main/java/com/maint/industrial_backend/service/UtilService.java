package com.maint.industrial_backend.service;

import com.maint.industrial_backend.entity.DataCatalogo;

import java.util.List;

public interface UtilService {

    // Recupera opciones para selects (ej: prioridades, tipos de activos).
    public abstract List<DataCatalogo> listaDataCatalogo(int idCatalogo);
}
