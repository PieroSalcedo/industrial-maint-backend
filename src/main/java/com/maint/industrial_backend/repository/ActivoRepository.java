package com.maint.industrial_backend.repository;

import com.maint.industrial_backend.entity.Activo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivoRepository extends JpaRepository<Activo, Integer> {

    // Aplicamos LOWER para que la búsqueda por nombre sea case-insensitive (ignore mayúsculas).
    // La lógica (?X = -1 or ...) permite que el parámetro sea opcional.
    // Si desde el controller llega -1, la condición se anula y el filtro no afecta el resultado.
    @Query("select a from Activo a where " +
            "(LOWER(a.nombre) like ?1) and " +
            "(?2 = '-1' or a.numeroSerie = ?2) and " +
            "(?3 = -1 or a.tipoActivo.idDataCatalogo = ?3) and " +
            "(?4 = -1 or a.estado = ?4) " +
            "order by a.idActivo asc")
    public abstract List<Activo> consultaDinamica(String nombre, String serie, int tipo, int estado);
}