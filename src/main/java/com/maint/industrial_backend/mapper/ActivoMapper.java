package com.maint.industrial_backend.mapper;

import com.maint.industrial_backend.dto.*;
import com.maint.industrial_backend.entity.Activo;
import com.maint.industrial_backend.entity.DataCatalogo;
import com.maint.industrial_backend.entity.TicketMantenimiento;
import com.maint.industrial_backend.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ActivoMapper {

    public Activo toEntity(ActivoCreateRequestDTO dto) {
        Activo activo = new Activo();
        activo.setNombre(normalizar(dto.nombre()));
        activo.setNumeroSerie(normalizar(dto.numeroSerie()));
        activo.setTipoActivo(referenciaCatalogo(dto.idTipoActivo()));
        activo.setUsuarioRegistro(referenciaUsuario(dto.idUsuarioRegistro()));
        return activo;
    }

    public Activo toEntity(ActivoUpdateRequestDTO dto) {
        Activo activo = new Activo();
        activo.setIdActivo(dto.idActivo());
        activo.setNombre(normalizar(dto.nombre()));
        activo.setNumeroSerie(normalizar(dto.numeroSerie()));
        activo.setTipoActivo(referenciaCatalogo(dto.idTipoActivo()));
        activo.setEstado(dto.estado());
        return activo;
    }

    public ActivoResponseDTO toResponse(Activo activo) {
        if (activo == null) {
            return null;
        }
        return new ActivoResponseDTO(
                activo.getIdActivo(),
                activo.getNombre(),
                activo.getNumeroSerie(),
                toCatalogoDto(activo.getTipoActivo()),
                activo.getFechaRegistro(),
                activo.getFechaActualizacion(),
                toUsuarioResumen(activo.getUsuarioRegistro()),
                toUsuarioResumen(activo.getUsuarioActualiza()),
                activo.getEstado()
        );
    }

    public ActivoResumenDTO toResumen(Activo activo) {
        if (activo == null) {
            return null;
        }
        return new ActivoResumenDTO(
                activo.getIdActivo(),
                activo.getNombre(),
                activo.getNumeroSerie(),
                toCatalogoDto(activo.getTipoActivo()),
                activo.getEstado()
        );
    }

    private DataCatalogoDTO toCatalogoDto(DataCatalogo catalogo) {
        if (catalogo == null) {
            return null;
        }
        return new DataCatalogoDTO(catalogo.getIdDataCatalogo(), catalogo.getDescripcion());
    }

    private UsuarioResumenDTO toUsuarioResumen(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioResumenDTO(
                usuario.getIdUsuario(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getNombreCompleto()
        );
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
