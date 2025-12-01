package com.cassinisys.erp.service.production;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.production.ERPProductBom;
import com.cassinisys.erp.repo.production.ProductBomRepository;


@Service
@Transactional
public class ProductBOMService {

	@Autowired
	ProductBomRepository productBOMRepository;
	
	public ERPProductBom createProductBOM(ERPProductBom productBOM) {
		return productBOMRepository.save(productBOM);

	}
	
	public ERPProductBom updateProductBOM(ERPProductBom productBOM) {
		checkNotNull(productBOM);
		checkNotNull(productBOM.getRowId());
		if (productBOMRepository.findOne(productBOM.getRowId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return productBOMRepository.save(productBOM);

	}

	
	public ERPProductBom getProductBOMById(Integer id) {
		return productBOMRepository.findOne(id);
	}

	
	public void deleteProductBOM(Integer id) {
		checkNotNull(id);
		productBOMRepository.delete(id);
	}

	
	public List<ERPProductBom> getAllProductBOMs() {
		return productBOMRepository.findAll();
	}
	
	
	public List<ERPProductBom> findByProduct(Integer product) {
		return productBOMRepository.findByProduct(product);
	}
	
	public List<ERPProductBom> findByMaterial(Integer material) {
		return productBOMRepository.findByMaterial(material);
	}
}
