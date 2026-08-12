package com.maint.industrial_backend.service;

import com.maint.industrial_backend.entity.TicketMantenimiento;

import java.util.List;

public interface TicketService {

    public abstract TicketMantenimiento registraTicket(TicketMantenimiento obj);
    public abstract TicketMantenimiento actualizaTicket(TicketMantenimiento obj);
    public abstract void eliminaTicket(int id);
    public abstract List<TicketMantenimiento> consultaDinamica(
            String desc, int idActivo, int idPrioridad, int idEstado, int idTecnico,
            int idTipoActivo, int soloPendientes, String fechaDesde, String fechaHasta);
}
