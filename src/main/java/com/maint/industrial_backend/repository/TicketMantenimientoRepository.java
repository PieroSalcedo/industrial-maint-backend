package com.maint.industrial_backend.repository;

import com.maint.industrial_backend.entity.TicketMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketMantenimientoRepository extends JpaRepository<TicketMantenimiento, Integer> {

    // Filtramos la operatividad diaria.
    // Ordenamos por fechaRegistro DESC porque en industria lo más reciente es lo más urgente.
    @Query("select t from TicketMantenimiento t where " +
            "(LOWER(t.descripcion) like ?1) and " +
            "(?2 = -1 or t.activo.idActivo = ?2) and " +
            "(?3 = -1 or t.prioridad.idDataCatalogo = ?3) and " +
            "(?4 = -1 or t.estadoTicket.idDataCatalogo = ?4) " +
            "order by t.fechaRegistro desc")
    public abstract List<TicketMantenimiento> consultaDinamica(String descripcion, int idActivo, int idPrioridad, int idEstado);
}
