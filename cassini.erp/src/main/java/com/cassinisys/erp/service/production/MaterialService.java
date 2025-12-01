package com.cassinisys.erp.service.production;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.api.filtering.MaterialPredicateBuilder;
import com.cassinisys.erp.model.api.criteria.MaterialCriteria;
import com.cassinisys.erp.model.production.*;
import com.cassinisys.erp.repo.production.MaterialRepository;
import com.cassinisys.erp.repo.production.MaterialSupplierRepository;
import com.cassinisys.erp.repo.production.SupplierRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import com.mysema.query.types.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class MaterialService implements CrudService<ERPMaterial, Integer>,
        PageableService<ERPMaterial, Integer> {

    @Autowired
    MaterialRepository materialRepository;


    @Autowired
    private MaterialPredicateBuilder predicateBuilder;

    @Autowired
    private MaterialCategoryService materialCategoryService;

    @Autowired
    private MaterialSupplierRepository materialSupplierRepository;

    @Autowired
    private SupplierRepository supplierRepository;


    @Override
    public ERPMaterial create(ERPMaterial material) {

        ERPMaterial mat = materialRepository.save(material);
        for (ERPSupplier supplier : material.getSuppliers()) {

            ERPMaterialSupplier matSup = new ERPMaterialSupplier();
            matSup.setSupplierId(supplier.getId());
            matSup.setMaterialId(mat.getId());
            materialSupplierRepository.save(matSup);

        }

        return mat;
    }

    @Override
    public Page<ERPMaterial> findAll(Pageable pageable) {

        Page<ERPMaterial> mats = materialRepository.findAll(pageable);

        for (ERPMaterial erpMaterial : mats) {

            List<ERPMaterialSupplier> materialSuppliers = materialSupplierRepository.findByMaterialSupplierIdMaterialId(erpMaterial.getId());

            Set<ERPSupplier> suppliers = new HashSet<>();
            for (ERPMaterialSupplier matSupp : materialSuppliers) {

                suppliers.add(supplierRepository.findOne(matSupp.getSupplierId()));
            }
            erpMaterial.setSuppliers(suppliers);

        }
        return materialRepository.findAll(pageable);
    }

    @Override
    public ERPMaterial update(ERPMaterial material) {
        checkNotNull(material);
        checkNotNull(material.getId());

        if (materialRepository.findOne(material.getId()) == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }

        /*List<ERPMaterialSupplier> materialSuppliers = materialSupplierRepository.findByMaterialSupplierIdMaterialId(material.getId());
        for (ERPMaterialSupplier matSupp : materialSuppliers) {
            materialSupplierRepository.delete(matSupp);
        }*/

//        materialSupplierRepository.save(material.getMaterialSuppliers());

        return materialRepository.save(material);
    }

    public ERPMaterial getBySku(String sku) {
        return materialRepository.findBySku(sku);
    }


    @Override
    public void delete(Integer materialId) {
        checkNotNull(materialId);
        materialRepository.delete(materialId);
    }

    public List<ERPMaterial> findMultiple(List<Integer> ids) {
        return materialRepository.findByIdIn(ids);
    }

    @Override
    public ERPMaterial get(Integer materialId) {
        ERPMaterial mat = materialRepository.findOne(materialId);

        List<ERPMaterialSupplier> materialSuppliers = materialSupplierRepository.findByMaterialSupplierIdMaterialId(materialId);

        Set<ERPSupplier> suppliers = new HashSet<>();
        for (ERPMaterialSupplier matSupp : materialSuppliers) {
            matSupp.setSupplierName(supplierRepository.findOne(matSupp.getSupplierId()).getName());
            suppliers.add(supplierRepository.findOne(matSupp.getSupplierId()));
        }
        mat.setSuppliers(suppliers);
        mat.setMaterialSuppliers(materialSuppliers);

        return mat;
    }

    public Page<ERPMaterial> getByCategory(Integer catid, Pageable pageable) {
        ERPMaterialCategory cat = materialCategoryService.getMaterialCategory(catid);
        List<Integer> cats = materialCategoryService.flattenCategoryPath(cat);
        if (cats.size() > 0) {
            return materialRepository.findInCategories(cats, pageable);
        } else {
            return new PageImpl<ERPMaterial>(new ArrayList<ERPMaterial>(), pageable, 0);
        }
    }


    public Page<ERPMaterial> findAll(MaterialCriteria criteria, Pageable pageable) {
        Predicate predicate = predicateBuilder.build(criteria, QERPMaterial.eRPMaterial);
        return materialRepository.findAll(predicate, pageable);
    }

    @Override
    public List<ERPMaterial> getAll() {
        return materialRepository.findAll();
    }


    public List<ERPMaterial> findByType(Integer id) {
        ERPMaterialType type = new ERPMaterialType();
        type.setId(id);
        return materialRepository.findByType(type);
    }

    public List<ERPMaterial> findByCategory(Integer id) {
        ERPMaterialCategory category = new ERPMaterialCategory();
        category.setId(id);
        return materialRepository.findByCategory(category);
    }

    public List<ERPMaterialSupplier> saveMaterialSuppliers(List<ERPMaterialSupplier> erpMaterialSuppliers) {
        List<ERPMaterialSupplier> materialSuppliers = materialSupplierRepository.save(erpMaterialSuppliers);
        return materialSuppliers;
    }

    public List<ERPMaterialSupplier> getMaterialSuppliersByMaterialId(Integer materialId) {
        return materialSupplierRepository.findByMaterialSupplierIdMaterialId(materialId);
    }

    public List<ERPMaterialSupplier> getMaterialSuppliersBySupplierId(Integer supplierId) {
        return materialSupplierRepository.findByMaterialSupplierIdSupplierId(supplierId);
    }

    public void deleterMaterialSupplier(Integer materialId, Integer supplierId) {
        materialSupplierRepository.delete(new ERPMaterialSupplier.MaterialSupplierId(materialId, supplierId));

    }

}
