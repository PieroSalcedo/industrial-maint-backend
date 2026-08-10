package com.maint.industrial_backend.service;

import com.maint.industrial_backend.entity.Opcion;
import com.maint.industrial_backend.entity.Rol;
import com.maint.industrial_backend.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    // Recupera todos los usuarios.
    public abstract List<Usuario> listaUsuario();

    // Para el flujo de autenticación de Spring Security.
    public abstract Optional<Usuario> buscaPorLogin(String login);

    // Recupera la matriz de accesos para el JWT y el menú de Angular.
    public abstract List<Rol> traerRolesDeUsuario(int idUsuario);
    public abstract List<Opcion> traerEnlacesDeUsuario(int idUsuario);
}
