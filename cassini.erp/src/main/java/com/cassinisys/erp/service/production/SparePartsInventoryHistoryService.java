package com.cassinisys.erp.service.production;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cassinisys.erp.model.production.ERPSparePartsInventoryHistory;
import com.cassinisys.erp.repo.production.SparePartsInventoryHistoryRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;

@Service
@Transactional
public class SparePartsInventoryHistoryService implements CrudService<ERPSparePartsInventoryHistory, Integer>,PageableService<ERPSparePartsInventoryHistory, Integer>  {

	@Autowired
	private SparePartsInventoryHistoryRepository sparePartsInventoryHistoryRepository;

	@Override
	public Page<ERPSparePartsInventoryHistory> findAll(Pageable pageable) {
		
		return sparePartsInventoryHistoryRepository.findAll(pageable);
	}

	@Override
	public ERPSparePartsInventoryHistory create(ERPSparePartsInventoryHistory t) {
		
		return sparePartsInventoryHistoryRepository.save(t);
	}

	@Override
	public ERPSparePartsInventoryHistory update(ERPSparePartsInventoryHistory t) {
		
		return sparePartsInventoryHistoryRepository.save(t);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ERPSparePartsInventoryHistory get(Integer id) {
		
		return sparePartsInventoryHistoryRepository.findOne(id);
	}

	@Override
	public List<ERPSparePartsInventoryHistory> getAll() {
		
		return sparePartsInventoryHistoryRepository.findAll();
	}
}
