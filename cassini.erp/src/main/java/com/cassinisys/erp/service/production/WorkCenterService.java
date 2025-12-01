package com.cassinisys.erp.service.production;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cassinisys.erp.model.production.ERPWorkCenter;
import com.cassinisys.erp.repo.production.WorkCenterRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;

@Service
@Transactional
public class WorkCenterService implements CrudService<ERPWorkCenter, Integer>,PageableService<ERPWorkCenter, Integer> {

	@Autowired
	private WorkCenterRepository workCenterRepository;
	
	@Override
	public Page<ERPWorkCenter> findAll(Pageable pageable) {
		
		return workCenterRepository.findAll(pageable);
	}

	@Override
	public ERPWorkCenter create(ERPWorkCenter t) {
		
		return workCenterRepository.save(t);
	}

	@Override
	public ERPWorkCenter update(ERPWorkCenter t) {
		
		return workCenterRepository.save(t);
	}

	@Override
	public void delete(Integer id) {
		workCenterRepository.delete(id);
		
	}

	@Override
	public ERPWorkCenter get(Integer id) {
		
		return workCenterRepository.findOne(id);
	}

	@Override
	public List<ERPWorkCenter> getAll() {
		
		return workCenterRepository.findAll();
	}

}
