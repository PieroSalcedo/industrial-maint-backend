package com.maint.industrial_backend.service.impl;

import com.maint.industrial_backend.entity.Activo;
import com.maint.industrial_backend.repository.ActivoRepository;
import com.maint.industrial_backend.service.ActivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivoServiceImpl implements ActivoService {

    @Autowired
    private ActivoRepository repository;

    @Override
    public List<Activo> listaTodos() {
        return repository.findAll();
    }

    @Override
    public Activo insertaActualizaActivo(Activo obj) {
        // Al usar save(), JPA detecta si el ID existe para hacer UPDATE o INSERT.
        return repository.save(obj);
    }

    @Override
    public void eliminaActivo(int id) {
        repository.deleteById(id);
    }

    @Override
    public List<Activo> consultaDinamica(String nombre, String serie, int tipo, int estado) {
        // Delegamos al repo la consulta con la lógica del -1.
        return repository.consultaDinamica(nombre, serie, tipo, estado);
    }
}
