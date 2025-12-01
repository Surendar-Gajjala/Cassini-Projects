package com.cassinisys.erp.service.production;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.production.ERPMaterial;
import com.cassinisys.erp.model.production.ERPSupplier;
import com.cassinisys.erp.repo.production.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class SupplierService {

    @Autowired
    SupplierRepository materialSupplierRepository;

    public ERPSupplier create(ERPSupplier materialSupplier) {
        return materialSupplierRepository.save(materialSupplier);
    }

    public ERPSupplier update(ERPSupplier materialSupplier) {
        checkNotNull(materialSupplier);
        checkNotNull(materialSupplier.getId());
        if (materialSupplierRepository.findOne(materialSupplier.getId()) == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        return materialSupplierRepository.save(materialSupplier);

    }

    public ERPSupplier get(Integer supplierId) {
        return materialSupplierRepository.findOne(supplierId);
    }

    public List<ERPSupplier> findMultiple(List<Integer> ids) {
        return materialSupplierRepository.findByIdIn(ids);
    }

    public List<ERPSupplier> getByNameAndOfficePhone(String name,String officePhone) {
        return materialSupplierRepository.findByNameAndOfficePhone(name, officePhone);
    }

    public void delete(Integer supplierId) {
        checkNotNull(supplierId);
        materialSupplierRepository.delete(supplierId);
    }

    public List<ERPSupplier> getAll() {
        return materialSupplierRepository.findAll();
    }

    public Page<ERPSupplier> findAll(Pageable pagable) {
        return materialSupplierRepository.findAll(pagable);
    }
}
