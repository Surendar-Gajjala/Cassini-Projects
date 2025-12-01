package com.cassinisys.erp.service.production;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cassinisys.erp.model.production.ERPWorkCenterJob;
import com.cassinisys.erp.repo.production.WorkCenterJobRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;

@Service
@Transactional
public class WorkCenterJobService implements CrudService<ERPWorkCenterJob, Integer>,PageableService<ERPWorkCenterJob, Integer> {

	@Autowired
	private WorkCenterJobRepository workCenterJobRepository;
	
	@Override
	public Page<ERPWorkCenterJob> findAll(Pageable pageable) {
		
		return workCenterJobRepository.findAll(pageable);
	}

	@Override
	public ERPWorkCenterJob create(ERPWorkCenterJob t) {
		
		return workCenterJobRepository.save(t);
	}

	@Override
	public ERPWorkCenterJob update(ERPWorkCenterJob t) {
		
		return workCenterJobRepository.save(t);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ERPWorkCenterJob get(Integer id) {
		
		return workCenterJobRepository.findOne(id);
	}

	@Override
	public List<ERPWorkCenterJob> getAll() {
		
		return workCenterJobRepository.findAll();
	}

}
