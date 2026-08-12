package com.maint.industrial_backend.service.impl;

import com.maint.industrial_backend.entity.Activo;
import com.maint.industrial_backend.entity.DataCatalogo;
import com.maint.industrial_backend.entity.TicketMantenimiento;
import com.maint.industrial_backend.entity.Usuario;
import com.maint.industrial_backend.repository.ActivoRepository;
import com.maint.industrial_backend.repository.TicketMantenimientoRepository;
import com.maint.industrial_backend.security.SecurityUtils;
import com.maint.industrial_backend.security.UsuarioPrincipal;
import com.maint.industrial_backend.service.TicketService;
import com.maint.industrial_backend.util.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TickerServiceImpl implements TicketService {

    @Autowired
    private TicketMantenimientoRepository repository;

    @Autowired
    private ActivoRepository activoRepository;

    @Override
    @Transactional
    public TicketMantenimiento registraTicket(TicketMantenimiento obj) {
        if (!SecurityUtils.isAdmin()) {
            throw new IllegalStateException("Solo el supervisor puede registrar tickets.");
        }

        if (obj.getEstadoTicket() == null || obj.getEstadoTicket().getIdDataCatalogo() == null) {
            DataCatalogo abierto = new DataCatalogo();
            abierto.setIdDataCatalogo(AppSettings.ESTADO_TICKET_ABIERTO);
            obj.setEstadoTicket(abierto);
        }

        TicketMantenimiento saved = repository.save(obj);
        marcarActivoFueraDeServicio(saved.getActivo().getIdActivo());
        return saved;
    }

    @Override
    @Transactional
    public TicketMantenimiento actualizaTicket(TicketMantenimiento obj) {
        TicketMantenimiento existente = repository.findById(obj.getIdTicket())
                .orElseThrow(() -> new IllegalArgumentException("Ticket no encontrado."));

        UsuarioPrincipal usuario = SecurityUtils.getUsuarioActual();
        boolean admin = SecurityUtils.isAdmin();

        if (!admin) {
            validarTicketAsignadoAlTecnico(existente, usuario.getIdUsuario());
            if (obj.getEstadoTicket() == null || obj.getEstadoTicket().getIdDataCatalogo() == null) {
                throw new IllegalArgumentException("Debe indicar el estado del ticket.");
            }
            DataCatalogo nuevoEstado = new DataCatalogo();
            nuevoEstado.setIdDataCatalogo(obj.getEstadoTicket().getIdDataCatalogo());
            existente.setEstadoTicket(nuevoEstado);
        } else {
            if (obj.getDescripcion() != null) {
                existente.setDescripcion(obj.getDescripcion());
            }
            if (obj.getPrioridad() != null && obj.getPrioridad().getIdDataCatalogo() != null) {
                existente.setPrioridad(obj.getPrioridad());
            }
            if (obj.getEstadoTicket() != null && obj.getEstadoTicket().getIdDataCatalogo() != null) {
                existente.setEstadoTicket(obj.getEstadoTicket());
            }
            if (obj.getTecnico() != null && obj.getTecnico().getIdUsuario() != null
                    && obj.getTecnico().getIdUsuario() > 0) {
                existente.setTecnico(obj.getTecnico());
            } else {
                existente.setTecnico(null);
            }
        }

        existente.setUsuarioActualiza(crearReferenciaUsuario(usuario.getIdUsuario()));
        TicketMantenimiento saved = repository.save(existente);
        sincronizarEstadoActivo(saved.getActivo().getIdActivo());
        return saved;
    }

    @Override
    @Transactional
    public void eliminaTicket(int id) {
        if (!SecurityUtils.isAdmin()) {
            throw new IllegalStateException("Solo el supervisor puede eliminar tickets.");
        }

        TicketMantenimiento ticket = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket no encontrado."));
        int idActivo = ticket.getActivo().getIdActivo();
        repository.deleteById(id);
        sincronizarEstadoActivo(idActivo);
    }

    @Override
    public List<TicketMantenimiento> consultaDinamica(
            String desc, int idActivo, int idPrioridad, int idEstado, int idTecnico,
            int idTipoActivo, int soloPendientes, String fechaDesde, String fechaHasta) {
        int filtroTecnico = idTecnico;
        if (!SecurityUtils.isAdmin()) {
            filtroTecnico = SecurityUtils.getUsuarioActual().getIdUsuario();
        }
        return repository.consultaDinamica(
                desc, idActivo, idPrioridad, idEstado, filtroTecnico,
                idTipoActivo, soloPendientes, fechaDesde, fechaHasta);
    }

    private void validarTicketAsignadoAlTecnico(TicketMantenimiento ticket, int idTecnico) {
        if (ticket.getTecnico() == null || ticket.getTecnico().getIdUsuario() == null
                || ticket.getTecnico().getIdUsuario() != idTecnico) {
            throw new IllegalStateException("No autorizado: el ticket no está asignado a usted.");
        }
    }

    private void marcarActivoFueraDeServicio(int idActivo) {
        Activo activo = activoRepository.findById(idActivo)
                .orElseThrow(() -> new IllegalArgumentException("Activo no encontrado."));
        activo.setEstado(AppSettings.ACTIVO_FUERA_SERVICIO);
        activoRepository.save(activo);
    }

    private void sincronizarEstadoActivo(int idActivo) {
        int ticketsActivos = repository.countTicketsActivosPorActivo(idActivo, -1);
        Activo activo = activoRepository.findById(idActivo).orElse(null);
        if (activo == null) {
            return;
        }
        activo.setEstado(ticketsActivos > 0
                ? AppSettings.ACTIVO_FUERA_SERVICIO
                : AppSettings.ACTIVO_OPERATIVO);
        activoRepository.save(activo);
    }

    private Usuario crearReferenciaUsuario(int idUsuario) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        return usuario;
    }
}
