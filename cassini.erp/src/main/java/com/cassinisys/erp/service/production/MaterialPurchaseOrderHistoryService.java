package com.cassinisys.erp.service.production;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.model.production.ERPMaterialPurchaseOrderHistory;
import com.cassinisys.erp.repo.production.MaterialPurchaseOrderHistoryRepository;

@Service
@Transactional
public class MaterialPurchaseOrderHistoryService {

	@Autowired
	MaterialPurchaseOrderHistoryRepository mPurchaseOrderHistoryRepository;

	public ERPMaterialPurchaseOrderHistory getMaterialPurchaseOrderHistory(
			Integer id) {
		return mPurchaseOrderHistoryRepository.findOne(id);
	}

	public void delete(Integer id) {
		checkNotNull(id);
		mPurchaseOrderHistoryRepository.delete(id);
	}


	public List<ERPMaterialPurchaseOrderHistory> getAllMaterialPurchaseOrderHistorys() {
		return mPurchaseOrderHistoryRepository.findAll();
	}

}
