package com.maint.industrial_backend.service.impl;

import com.maint.industrial_backend.entity.DataCatalogo;
import com.maint.industrial_backend.entity.Usuario;
import com.maint.industrial_backend.repository.DataCatalogoRepository;
import com.maint.industrial_backend.service.UsuarioService;
import com.maint.industrial_backend.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilServiceImpl implements UtilService {

    @Autowired
    private DataCatalogoRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public List<DataCatalogo> listaDataCatalogo(int idCatalogo) {
        return repository.listaDataCatalogo(idCatalogo);
    }

    @Override
    public List<Usuario> listaTecnico() {
        return usuarioService.listaTecnico();
    }

}
