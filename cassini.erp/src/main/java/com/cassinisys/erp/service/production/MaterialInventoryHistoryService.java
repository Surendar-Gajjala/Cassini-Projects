package com.cassinisys.erp.service.production;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.model.production.ERPMaterialInventoryHistory;
import com.cassinisys.erp.repo.production.MaterialInventoryHistoryRepository;

@Service
@Transactional
public class MaterialInventoryHistoryService {

	@Autowired
	MaterialInventoryHistoryRepository materialInvrHistoryRepository;


	public ERPMaterialInventoryHistory getMaterialInvHistory(Integer id) {
		return materialInvrHistoryRepository.findOne(id);
	}

	public void delete(Integer id) {
		checkNotNull(id);
		materialInvrHistoryRepository.delete(id);
	}

	public List<ERPMaterialInventoryHistory> getAllMaterialInvHistorys() {
		return materialInvrHistoryRepository.findAll();

	}

}
