package com.maint.industrial_backend.service;

import com.maint.industrial_backend.entity.DataCatalogo;
import com.maint.industrial_backend.entity.Usuario;

import java.util.List;

public interface UtilService {

    // Recupera opciones para selects (ej: prioridades, tipos de activos).
    public abstract List<DataCatalogo> listaDataCatalogo(int idCatalogo);

    // Recupera técnicos activos para asignación de tickets.
    public abstract List<Usuario> listaTecnico();
}
