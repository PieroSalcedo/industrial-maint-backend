package com.maint.industrial_backend.security;

import com.maint.industrial_backend.entity.Opcion;
import com.maint.industrial_backend.entity.Rol;
import com.maint.industrial_backend.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@CommonsLog
@ToString
public class UsuarioPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;
    private int idUsuario;
    private String login;
    private String password;
    private String nombreCompleto;
    private Collection<? extends GrantedAuthority> authorities;
    private List<Opcion> opciones;

    public static UsuarioPrincipal build(Usuario usuario, List<Rol> roles, List<Opcion> opciones) {
        log.info(">>> UsuarioPrincipal >> Construyendo sesión para: " + usuario.getLogin());

        List<GrantedAuthority> authorities = roles.stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                .collect(Collectors.toList());

        return new UsuarioPrincipal(
                usuario.getIdUsuario(),
                usuario.getLogin(),
                usuario.getPassword(),
                usuario.getNombres() + " " + usuario.getApellidos(),
                authorities,
                opciones
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return login; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}