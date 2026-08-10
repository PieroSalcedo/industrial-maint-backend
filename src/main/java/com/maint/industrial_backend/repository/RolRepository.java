package com.maint.industrial_backend.repository;

import com.maint.industrial_backend.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    // Buscamos un rol por su nombre (ej: 'ROLE_ADMIN').
    // Es útil cuando registramos un usuario nuevo y queremos asignarle un rol por defecto.
    @Query("select r from Rol r where r.nombre = ?1")
    public abstract Optional<Rol> findByNombre(String nombre);
}
