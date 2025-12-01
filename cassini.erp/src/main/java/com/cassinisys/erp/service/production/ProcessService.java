package com.cassinisys.erp.service.production;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cassinisys.erp.model.production.ERPProcess;
import com.cassinisys.erp.repo.production.ProcessRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;

@Service
@Transactional
public class ProcessService implements CrudService<ERPProcess, Integer>,PageableService<ERPProcess, Integer>  {
	
	@Autowired
	private ProcessRepository processRepository;

	@Override
	public Page<ERPProcess> findAll(Pageable pageable) {
		
		return processRepository.findAll(pageable);
	}

	@Override
	public ERPProcess create(ERPProcess t) {
		
		return processRepository.save(t);
	}

	@Override
	public ERPProcess update(ERPProcess t) {
		
		return processRepository.save(t);
	}

	@Override
	public void delete(Integer id) {
		processRepository.delete(id);
	}

	@Override
	public ERPProcess get(Integer id) {
		
		return processRepository.findOne(id);
	}

	@Override
	public List<ERPProcess> getAll() {
		
		return processRepository.findAll();
	}

}
