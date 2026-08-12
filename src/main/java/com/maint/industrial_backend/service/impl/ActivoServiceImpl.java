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
        validarNumeroSerieUnico(obj);
        return repository.save(obj);
    }

    private void validarNumeroSerieUnico(Activo obj) {
        String serie = obj.getNumeroSerie();
        if (serie == null || serie.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el número de serie.");
        }

        serie = serie.trim();
        obj.setNumeroSerie(serie);

        Integer id = obj.getIdActivo();
        boolean duplicado = (id == null || id == 0)
                ? repository.existsByNumeroSerieNormalizado(serie)
                : repository.existsByNumeroSerieNormalizadoAndIdActivoNot(serie, id);

        if (duplicado) {
            throw new IllegalStateException("Ya existe un activo con ese número de serie.");
        }
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
