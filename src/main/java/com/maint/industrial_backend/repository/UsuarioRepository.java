package com.maint.industrial_backend.repository;

import com.maint.industrial_backend.entity.Opcion;
import com.maint.industrial_backend.entity.Rol;
import com.maint.industrial_backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Este método es vital para que Spring Security verifique si el usuario existe durante el login.
    // Usamos Optional para evitar los tediosos NullPointerException.
    @Query("select u from Usuario u where u.login = ?1")
    public abstract Optional<Usuario> findByLogin(String login);

    // Consulta de validación de credenciales (Lógica de negocio del profe).
    // Compara login y password directamente en la base de datos.
    @Query("select u from Usuario u where u.login = ?1 and u.password = ?2")
    public abstract Optional<Usuario> login(String login, String password);

    // Aquí aplicamos un JOIN implícito entre 4 tablas para obtener las rutas del menú.
    // Sirve para que el Frontend sepa qué módulos "pintar" según el rol del usuario.
    @Query("select p from Opcion p, RolHasOpcion ro, Rol r, UsuarioHasRol ur " +
            "where p.idOpcion = ro.opcion.idOpcion " +
            "and ro.rol.idRol = r.idRol " +
            "and r.idRol = ur.rol.idRol " +
            "and ur.usuario.idUsuario = ?1")
    public abstract List<Opcion> traerEnlacesDeUsuario(int idUsuario);

    // Trae la lista de roles (ADMIN/TECH) del usuario.
    // Sin esto, el JWT no tendría las "authorities" necesarias para pasar los filtros de seguridad.
    @Query("select r from Rol r, UsuarioHasRol ur " +
            "where r.idRol = ur.rol.idRol " +
            "and ur.usuario.idUsuario = ?1")
    public abstract List<Rol> traerRolesDeUsuario(int idUsuario);

    @Query("select u from Usuario u, UsuarioHasRol ur, Rol r " +
            "where u.idUsuario = ur.usuario.idUsuario " +
            "and ur.rol.idRol = r.idRol " +
            "and r.nombre = 'ROLE_TECH' " +
            "and u.estado = 1")
    public abstract List<Usuario> traerTecnicos();
}
