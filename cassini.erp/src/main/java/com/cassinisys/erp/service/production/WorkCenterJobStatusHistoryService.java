package com.cassinisys.erp.service.production;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cassinisys.erp.model.production.ERPWorkCenterJobStatusHistory;
import com.cassinisys.erp.repo.production.WorkCenterJobStatusHistoryRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;

@Service
@Transactional
public class WorkCenterJobStatusHistoryService implements CrudService<ERPWorkCenterJobStatusHistory, Integer>,PageableService<ERPWorkCenterJobStatusHistory, Integer> {
	
	@Autowired
	private WorkCenterJobStatusHistoryRepository workCenterJobStatusHistoryRepository;

	@Override
	public Page<ERPWorkCenterJobStatusHistory> findAll(Pageable pageable) {
		
		return workCenterJobStatusHistoryRepository.findAll(pageable);
	}

	@Override
	public ERPWorkCenterJobStatusHistory create(ERPWorkCenterJobStatusHistory t) {
		
		return workCenterJobStatusHistoryRepository.save(t);
	}

	@Override
	public ERPWorkCenterJobStatusHistory update(ERPWorkCenterJobStatusHistory t) {
		
		return workCenterJobStatusHistoryRepository.save(t);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ERPWorkCenterJobStatusHistory get(Integer id) {
		
		return workCenterJobStatusHistoryRepository.findOne(id);
	}

	@Override
	public List<ERPWorkCenterJobStatusHistory> getAll() {
		
		return workCenterJobStatusHistoryRepository.findAll();
	}

}
