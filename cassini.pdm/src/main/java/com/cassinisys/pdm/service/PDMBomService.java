package com.cassinisys.pdm.service;

import com.cassinisys.pdm.model.PDMBom;
import com.cassinisys.pdm.repo.BomRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Service
@Transactional
public class PDMBomService implements CrudService<PDMBom, Integer>{

    @Autowired
    private BomRepository bomRepository;

    @Override
    public PDMBom create(PDMBom pdmBom) {
        checkNotNull(pdmBom);
        return bomRepository.save(pdmBom);
    }

    @Override
    public PDMBom update(PDMBom pdmBom) {
        checkNotNull(pdmBom);
        return bomRepository.save(pdmBom);
    }

    @Override
    public void delete(Integer bomId) {
        checkNotNull(bomId);
        PDMBom pdmBom = bomRepository.findOne(bomId);
        if(pdmBom == null){
            throw new ResourceNotFoundException();
        }
        bomRepository.delete(bomId);
    }

    @Override
    public PDMBom get(Integer bomId) {
        checkNotNull(bomId);
        return bomRepository.findOne(bomId);
    }

    @Override
    public List<PDMBom> getAll() {
        return bomRepository.findAll();
    }

    public Page<PDMBom> findAll(Pageable pageable){
        return bomRepository.findAll(pageable);
    }
}
