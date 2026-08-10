package com.maint.industrial_backend.service.impl;

import com.maint.industrial_backend.entity.TicketMantenimiento;
import com.maint.industrial_backend.repository.TicketMantenimientoRepository;
import com.maint.industrial_backend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TickerServiceImpl implements TicketService {

    @Autowired
    private TicketMantenimientoRepository repository;

    @Override
    public TicketMantenimiento registraTicket(TicketMantenimiento obj) {
        // En industria, un ticket nuevo siempre se guarda con el ID en 0 para asegurar INSERT.
        return repository.save(obj);
    }

    @Override
    public List<TicketMantenimiento> consultaDinamica(String desc, int idActivo, int idPrioridad, int idEstado) {
        return repository.consultaDinamica(desc, idActivo, idPrioridad, idEstado);
    }
}
