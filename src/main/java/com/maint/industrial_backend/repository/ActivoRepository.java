package com.maint.industrial_backend.repository;

import com.maint.industrial_backend.entity.Activo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivoRepository extends JpaRepository<Activo, Integer> {

    @Query("select count(a) > 0 from Activo a where trim(a.numeroSerie) = ?1")
    boolean existsByNumeroSerieNormalizado(String numeroSerie);

    @Query("select count(a) > 0 from Activo a where trim(a.numeroSerie) = ?1 and a.idActivo <> ?2")
    boolean existsByNumeroSerieNormalizadoAndIdActivoNot(String numeroSerie, Integer idActivo);

    @Query("select a from Activo a where " +
            "(LOWER(a.nombre) like ?1) and " +
            "(?2 = '-1' or a.numeroSerie = ?2) and " +
            "(?3 = -1 or a.tipoActivo.idDataCatalogo = ?3) and " +
            "(?4 = -1 or a.estado = ?4) " +
            "order by a.idActivo asc")
    public abstract List<Activo> consultaDinamica(String nombre, String serie, int tipo, int estado);
}