package com.maint.industrial_backend.service.impl;

import com.maint.industrial_backend.entity.Opcion;
import com.maint.industrial_backend.entity.Rol;
import com.maint.industrial_backend.entity.Usuario;
import com.maint.industrial_backend.repository.UsuarioRepository;
import com.maint.industrial_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public List<Usuario> listaUsuario() {
        return repository.findAll();
    }

    @Override
    public List<Usuario> listaTecnico() {
        return repository.traerTecnicos();
    }

    @Override
    public Optional<Usuario> buscaPorLogin(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public List<Rol> traerRolesDeUsuario(int idUsuario) {
        return repository.traerRolesDeUsuario(idUsuario);
    }

    @Override
    public List<Opcion> traerEnlacesDeUsuario(int idUsuario) {
        return repository.traerEnlacesDeUsuario(idUsuario);
    }
}
