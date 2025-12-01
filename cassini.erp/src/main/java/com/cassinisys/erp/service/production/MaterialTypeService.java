package com.cassinisys.erp.service.production;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.production.ERPMaterialType;
import com.cassinisys.erp.repo.production.MaterialTypeRepository;

@Service
@Transactional
public class MaterialTypeService {

	@Autowired
	MaterialTypeRepository materialTypeRepository;

	public ERPMaterialType createMaterialType(ERPMaterialType materialType) {
		return materialTypeRepository.save(materialType);
	}

	public ERPMaterialType updateMaterialType(ERPMaterialType materialType) {
		checkNotNull(materialType);
		checkNotNull(materialType.getId());
		if (materialTypeRepository.findOne(materialType.getId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return materialTypeRepository.save(materialType);

	}

	public ERPMaterialType getMaterialType(Integer id) {
		return materialTypeRepository.findOne(id);

	}

	public void deleteMaterialType(Integer id) {
		checkNotNull(id);
		materialTypeRepository.delete(id);
	}

	public List<ERPMaterialType> getAllMaterialTypes() {
		return materialTypeRepository.findAll();
	}

}
