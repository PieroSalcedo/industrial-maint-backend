package com.maint.industrial_backend.mapper;

import com.maint.industrial_backend.dto.*;
import com.maint.industrial_backend.entity.Activo;
import com.maint.industrial_backend.entity.DataCatalogo;
import com.maint.industrial_backend.entity.TicketMantenimiento;
import com.maint.industrial_backend.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    @Autowired
    private ActivoMapper activoMapper;

    public TicketMantenimiento toEntity(TicketCreateRequestDTO dto) {
        TicketMantenimiento ticket = new TicketMantenimiento();
        ticket.setDescripcion(normalizar(dto.descripcion()));
        ticket.setActivo(referenciaActivo(dto.idActivo()));
        ticket.setPrioridad(referenciaCatalogo(dto.idPrioridad()));
        if (dto.idEstadoTicket() != null) {
            ticket.setEstadoTicket(referenciaCatalogo(dto.idEstadoTicket()));
        }
        if (dto.idUsuarioTecnico() != null && dto.idUsuarioTecnico() > 0) {
            ticket.setTecnico(referenciaUsuario(dto.idUsuarioTecnico()));
        }
        ticket.setUsuarioRegistro(referenciaUsuario(dto.idUsuarioRegistro()));
        return ticket;
    }

    public TicketMantenimiento toEntity(TicketUpdateRequestDTO dto) {
        TicketMantenimiento ticket = new TicketMantenimiento();
        ticket.setIdTicket(dto.idTicket());
        if (dto.descripcion() != null) {
            ticket.setDescripcion(normalizar(dto.descripcion()));
        }
        if (dto.idPrioridad() != null) {
            ticket.setPrioridad(referenciaCatalogo(dto.idPrioridad()));
        }
        if (dto.idEstadoTicket() != null) {
            ticket.setEstadoTicket(referenciaCatalogo(dto.idEstadoTicket()));
        }
        if (dto.idUsuarioTecnico() != null) {
            if (dto.idUsuarioTecnico() > 0) {
                ticket.setTecnico(referenciaUsuario(dto.idUsuarioTecnico()));
            } else {
                Usuario sinAsignar = new Usuario();
                sinAsignar.setIdUsuario(0);
                ticket.setTecnico(sinAsignar);
            }
        }
        if (dto.idUsuarioActualiza() != null) {
            ticket.setUsuarioActualiza(referenciaUsuario(dto.idUsuarioActualiza()));
        }
        return ticket;
    }

    public TicketResponseDTO toResponse(TicketMantenimiento ticket) {
        if (ticket == null) {
            return null;
        }
        return new TicketResponseDTO(
                ticket.getIdTicket(),
                ticket.getDescripcion(),
                activoMapper.toResumen(ticket.getActivo()),
                toCatalogoDto(ticket.getPrioridad()),
                toCatalogoDto(ticket.getEstadoTicket()),
                toUsuarioResumen(ticket.getTecnico()),
                ticket.getFechaRegistro(),
                ticket.getFechaActualizacion(),
                toUsuarioResumen(ticket.getUsuarioRegistro()),
                toUsuarioResumen(ticket.getUsuarioActualiza())
        );
    }

    public DataCatalogoDTO toCatalogoDto(DataCatalogo catalogo) {
        if (catalogo == null) {
            return null;
        }
        return new DataCatalogoDTO(catalogo.getIdDataCatalogo(), catalogo.getDescripcion());
    }

    public UsuarioResumenDTO toUsuarioResumen(Usuario usuario) {
        if (usuario == null || usuario.getIdUsuario() == null) {
            return null;
        }
        return new UsuarioResumenDTO(
                usuario.getIdUsuario(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getNombreCompleto()
        );
    }

    private Activo referenciaActivo(Integer id) {
        Activo activo = new Activo();
        activo.setIdActivo(id);
        return activo;
    }

    private DataCatalogo referenciaCatalogo(Integer id) {
        DataCatalogo catalogo = new DataCatalogo();
        catalogo.setIdDataCatalogo(id);
        return catalogo;
    }

    private Usuario referenciaUsuario(Integer id) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        return usuario;
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
