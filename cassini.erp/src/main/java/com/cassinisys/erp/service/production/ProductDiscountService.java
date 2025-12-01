package com.cassinisys.erp.service.production;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.production.ERPProductDiscount;
import com.cassinisys.erp.repo.production.ProductDiscountRepository;

@Service
@Transactional
public class ProductDiscountService {

	@Autowired
	ProductDiscountRepository productDiscountRepository;
	
	public ERPProductDiscount createProductDiscount(ERPProductDiscount productDiscount) {
		return productDiscountRepository.save(productDiscount);

	}
	
	public ERPProductDiscount updateProductDiscount(ERPProductDiscount productDiscount) {
		checkNotNull(productDiscount);
		checkNotNull(productDiscount.getRowId());
		if (productDiscountRepository.findOne(productDiscount.getRowId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return productDiscountRepository.save(productDiscount);
	}

	
	public ERPProductDiscount getProductDiscountById(Integer id) {
		return productDiscountRepository.findOne(id);

	}

	
	public void deleteProductDiscount(Integer id) {
		checkNotNull(id);
		productDiscountRepository.delete(id);
	}

	
	public List<ERPProductDiscount> getAllProductDiscounts() {
		return productDiscountRepository.findAll();
	}
}
