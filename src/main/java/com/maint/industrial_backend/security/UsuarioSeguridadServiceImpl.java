package com.maint.industrial_backend.security;

import com.maint.industrial_backend.entity.Opcion;
import com.maint.industrial_backend.entity.Rol;
import com.maint.industrial_backend.entity.Usuario;
import com.maint.industrial_backend.repository.UsuarioRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CommonsLog
public class UsuarioSeguridadServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        log.info(">>> loadUserByUsername >>> Solicitando login para: " + login);

        try {
            Optional<Usuario> optUsuario = usuarioRepository.findByLogin(login);
            if (optUsuario.isPresent()) {
                Usuario user = optUsuario.get();
                log.info("==> Usuario encontrado en BD: " + user.getLogin());

                // Cargamos Roles y Opciones dinámicamente según el estilo del profesor
                List<Rol> lstRol = usuarioRepository.traerRolesDeUsuario(user.getIdUsuario());
                List<Opcion> lstOpciones = usuarioRepository.traerEnlacesDeUsuario(user.getIdUsuario());

                return UsuarioPrincipal.build(user, lstRol, lstOpciones);
            } else {
                throw new UsernameNotFoundException("Usuario no existe: " + login);
            }
        } catch (Exception e) {
            log.error("Error crítico en autenticación: " + e.getMessage());
            throw new UsernameNotFoundException("Error en el sistema de seguridad");
        }
    }
}
