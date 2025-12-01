package com.cassinisys.erp.service.production;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cassinisys.erp.model.production.ERPWorkShift;
import com.cassinisys.erp.repo.production.WorkShiftRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;

@Service
@Transactional
public class WorkShiftService implements CrudService<ERPWorkShift, Integer>,PageableService<ERPWorkShift, Integer> {

	@Autowired
    private WorkShiftRepository workShiftRepository;
	
	@Override
	public Page<ERPWorkShift> findAll(Pageable pageable) {
		return workShiftRepository.findAll(pageable);
		
	}

	@Override
	public ERPWorkShift create(ERPWorkShift t) {
		
		return workShiftRepository.save(t);
	}

	@Override
	public ERPWorkShift update(ERPWorkShift t) {
		
		return workShiftRepository.save(t);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ERPWorkShift get(Integer id) {
		
		return workShiftRepository.findOne(id);
	}

	@Override
	public List<ERPWorkShift> getAll() {
		
		return workShiftRepository.findAll();
	}

}
