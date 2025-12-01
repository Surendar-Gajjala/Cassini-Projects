package com.cassinisys.erp.service.production;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cassinisys.erp.model.production.ERPMachineSparePart;
import com.cassinisys.erp.repo.production.MachineSparePartRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;

@Service
@Transactional
public class MachineSparePartService implements CrudService<ERPMachineSparePart, Integer>,PageableService<ERPMachineSparePart, Integer> {

	@Autowired
	MachineSparePartRepository machineSparePartRepository;
	
	@Override
	public Page<ERPMachineSparePart> findAll(Pageable pageable) {
		
		return machineSparePartRepository.findAll(pageable);
	}

	@Override
	public ERPMachineSparePart create(ERPMachineSparePart t) {
		
		return machineSparePartRepository.save(t);
	}

	@Override
	public ERPMachineSparePart update(ERPMachineSparePart t) {
		
		return machineSparePartRepository.save(t);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ERPMachineSparePart get(Integer id) {
		
		return machineSparePartRepository.findOne(id);
	}

	@Override
	public List<ERPMachineSparePart> getAll() {
		
		return machineSparePartRepository.findAll();
	}

}
