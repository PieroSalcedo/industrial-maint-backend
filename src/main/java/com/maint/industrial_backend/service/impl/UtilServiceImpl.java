package com.maint.industrial_backend.service.impl;

import com.maint.industrial_backend.entity.DataCatalogo;
import com.maint.industrial_backend.repository.DataCatalogoRepository;
import com.maint.industrial_backend.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilServiceImpl implements UtilService {

    @Autowired
    private DataCatalogoRepository repository;

    @Override
    public List<DataCatalogo> listaDataCatalogo(int idCatalogo) {
        return repository.listaDataCatalogo(idCatalogo);
    }

}
