package com.cassinisys.erp.service.production;

import com.cassinisys.erp.api.filtering.EmpWorkShiftPredicateBuilder;
import com.cassinisys.erp.model.api.criteria.EmpWorkShiftCriteria;
import com.cassinisys.erp.model.production.ERPWorkShiftEmployee;
import com.cassinisys.erp.model.production.QERPWorkShiftEmployee;
import com.cassinisys.erp.repo.production.WorkShiftEmployeeRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class WorkShiftEmployeeService implements CrudService<ERPWorkShiftEmployee, Integer>,PageableService<ERPWorkShiftEmployee, Integer> {

	@Autowired
	private WorkShiftEmployeeRepository workShiftEmployeeRepository;

	@Autowired
	EmpWorkShiftPredicateBuilder predicateBuilder;


	public List<ERPWorkShiftEmployee> findByShift(Integer shiftId) {
		
		return workShiftEmployeeRepository.findByShiftId(shiftId);
	}


	public Page<ERPWorkShiftEmployee> find(EmpWorkShiftCriteria criteria, Pageable pageable) {
		Predicate predicate = predicateBuilder.build(criteria, QERPWorkShiftEmployee.eRPWorkShiftEmployee);
		return workShiftEmployeeRepository.findAll(predicate, pageable);
	}

	public List<ERPWorkShiftEmployee> createEmpWorkList(List<ERPWorkShiftEmployee> t) {

		return workShiftEmployeeRepository.save(t);
	}

	@Override
	public ERPWorkShiftEmployee create(ERPWorkShiftEmployee t) {
		
		return workShiftEmployeeRepository.save(t);
	}

	@Override
	public ERPWorkShiftEmployee update(ERPWorkShiftEmployee t) {
		
		return workShiftEmployeeRepository.save(t);
	}

	@Override
	public void delete(Integer id) {
		workShiftEmployeeRepository.delete(id);
		
	}

	@Override
	public ERPWorkShiftEmployee get(Integer id) {
		
		return workShiftEmployeeRepository.findOne(id);
	}

	@Override
	public List<ERPWorkShiftEmployee> getAll() {
		
		return workShiftEmployeeRepository.findAll();
	}

	@Override
	public Page<ERPWorkShiftEmployee> findAll(Pageable pageable) {
		return null;
	}
}
