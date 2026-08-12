package com.maint.industrial_backend.repository;

import com.maint.industrial_backend.entity.TicketMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketMantenimientoRepository extends JpaRepository<TicketMantenimiento, Integer> {

    @Query("select t from TicketMantenimiento t where " +
            "(LOWER(t.descripcion) like ?1) and " +
            "(?2 = -1 or t.activo.idActivo = ?2) and " +
            "(?3 = -1 or t.prioridad.idDataCatalogo = ?3) and " +
            "(?4 = -1 or t.estadoTicket.idDataCatalogo = ?4) and " +
            "(?5 = -1 or t.tecnico.idUsuario = ?5) and " +
            "(?6 = -1 or t.activo.tipoActivo.idDataCatalogo = ?6) and " +
            "(?7 = -1 or (?7 = 1 and t.estadoTicket.idDataCatalogo in (7, 8)) or (?7 = 0 and t.estadoTicket.idDataCatalogo = 9)) and " +
            "(t.fechaRegistro >= ?8) and " +
            "(t.fechaRegistro <= ?9) " +
            "order by t.fechaRegistro desc")
    public abstract List<TicketMantenimiento> consultaDinamica(
            String descripcion, int idActivo, int idPrioridad, int idEstado, int idTecnico,
            int idTipoActivo, int soloPendientes, LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    @Query("select count(t) from TicketMantenimiento t where t.activo.idActivo = ?1 " +
            "and t.estadoTicket.idDataCatalogo in (7, 8) " +
            "and (?2 = -1 or t.idTicket <> ?2)")
    public abstract int countTicketsActivosPorActivo(int idActivo, int excludeTicketId);
}
