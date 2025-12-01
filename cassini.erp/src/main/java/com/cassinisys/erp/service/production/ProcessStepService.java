package com.cassinisys.erp.service.production;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cassinisys.erp.model.production.ERPProcessStep;
import com.cassinisys.erp.repo.production.ProcessStepRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;

@Service
@Transactional
public class ProcessStepService implements CrudService<ERPProcessStep, Integer>,PageableService<ERPProcessStep, Integer> {

	@Autowired 
	private ProcessStepRepository processStepRepository;
	
	@Override
	public Page<ERPProcessStep> findAll(Pageable pageable) {
		
		return processStepRepository.findAll(pageable);
	}

	@Override
	public ERPProcessStep create(ERPProcessStep t) {
		
		return processStepRepository.save(t);
	}

	@Override
	public ERPProcessStep update(ERPProcessStep t) {
		
		return processStepRepository.save(t);
	}

	@Override
	public void delete(Integer id) {

		processStepRepository.delete(id);
		
	}

	@Override
	public ERPProcessStep get(Integer id) {
		
		return processStepRepository.findOne(id);
	}

	@Override
	public List<ERPProcessStep> getAll() {

		return processStepRepository.findAll();
	}

}
