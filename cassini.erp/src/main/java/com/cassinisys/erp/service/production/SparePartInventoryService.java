package com.cassinisys.erp.service.production;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cassinisys.erp.model.production.ERPSparePartsInventory;
import com.cassinisys.erp.repo.production.SparePartsInventoryRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;

@Service
@Transactional
public class SparePartInventoryService implements CrudService<ERPSparePartsInventory, Integer>,PageableService<ERPSparePartsInventory, Integer> {

	@Autowired
	SparePartsInventoryRepository sparePartsInventoryRepository;
	
	@Override
	public Page<ERPSparePartsInventory> findAll(Pageable pageable) {
		
		return sparePartsInventoryRepository.findAll(pageable);
	}

	@Override
	public ERPSparePartsInventory create(ERPSparePartsInventory t) {
		
		return sparePartsInventoryRepository.save(t);
	}

	@Override
	public ERPSparePartsInventory update(ERPSparePartsInventory t) {
		
		return sparePartsInventoryRepository.save(t);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ERPSparePartsInventory get(Integer id) {
		
		return sparePartsInventoryRepository.findOne(id);
	}

	@Override
	public List<ERPSparePartsInventory> getAll() {
		
		return sparePartsInventoryRepository.findAll();
	}

}
