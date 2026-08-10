package com.maint.industrial_backend.repository;

import com.maint.industrial_backend.entity.DataCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DataCatalogoRepository extends JpaRepository<DataCatalogo, Integer> {

    // Recupera las opciones de un catálogo específico (ej: todas las 'Prioridades').
    // Esto garantiza que el Frontend no tenga valores "quemados" y todo venga de la BD.
    @Query("select d from DataCatalogo d where d.catalogo.idCatalogo = ?1 order by d.descripcion asc")
    public abstract List<DataCatalogo> listaDataCatalogo(int idCatalogo);
}
