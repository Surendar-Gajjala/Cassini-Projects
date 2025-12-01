package com.cassinisys.erp.service.production;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cassinisys.erp.model.production.ERPMachine;
import com.cassinisys.erp.repo.production.MachineRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;

@Service
@Transactional
public class MachineService implements CrudService<ERPMachine, Integer>,PageableService<ERPMachine, Integer> {

	@Autowired
	private MachineRepository machineRepository;
	
	@Override
	public Page<ERPMachine> findAll(Pageable pageable) {
		
		return machineRepository.findAll(pageable);
	}

	@Override
	public ERPMachine create(ERPMachine t) {
		
		return machineRepository.save(t);
	}

	@Override
	public ERPMachine update(ERPMachine t) {
		
		return machineRepository.save(t);
	}

	@Override
	public void delete(Integer id) {
		machineRepository.delete(id);
		
	}

	@Override
	public ERPMachine get(Integer id) {
		
		return machineRepository.findOne(id);
	}

	@Override
	public List<ERPMachine> getAll() {
		
		return machineRepository.findAll();
	}

}
