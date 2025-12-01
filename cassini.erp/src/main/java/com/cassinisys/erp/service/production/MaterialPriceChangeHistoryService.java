package com.cassinisys.erp.service.production;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.production.ERPMaterialPriceChangeHistory;
import com.cassinisys.erp.repo.production.MaterialPriceChangeHistoryRepository;

@Service
@Transactional
public class MaterialPriceChangeHistoryService {

	@Autowired
	MaterialPriceChangeHistoryRepository materialPriceChgHistoryRepository;

	public ERPMaterialPriceChangeHistory createMaterialPriceChgHistory(
			ERPMaterialPriceChangeHistory materialPriceChgHistory) {
		return materialPriceChgHistoryRepository.save(materialPriceChgHistory);
	}

	public ERPMaterialPriceChangeHistory updateMaterialPriceChgHistory(
			ERPMaterialPriceChangeHistory materialPriceChgHistory) {

		checkNotNull(materialPriceChgHistory);
		checkNotNull(materialPriceChgHistory.getRowId());
		if (materialPriceChgHistoryRepository.findOne(materialPriceChgHistory
				.getRowId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return materialPriceChgHistoryRepository.save(materialPriceChgHistory);

	}

	public ERPMaterialPriceChangeHistory getMaterialPriceChangeHistory(
			Integer id) {
		return materialPriceChgHistoryRepository.findOne(id);
	}


	public void deleteMaterialPriceChangeHistory(Integer id) {
		checkNotNull(id);
		materialPriceChgHistoryRepository.delete(id);
	}

	public List<ERPMaterialPriceChangeHistory> getAllMaterialPriceChangeHistorys() {
		return materialPriceChgHistoryRepository.findAll();
	}

}
