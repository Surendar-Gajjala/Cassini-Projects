package com.cassinisys.tm.service;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.tm.filtering.DepartmentCriteria;
import com.cassinisys.tm.filtering.DepartmentPersonCriteria;
import com.cassinisys.tm.filtering.DepartmentPersonPredicateBuilder;
import com.cassinisys.tm.filtering.DepartmentPredicateBuilder;
import com.cassinisys.tm.model.QTMDepartment;
import com.cassinisys.tm.model.QTMDepartmentPerson;
import com.cassinisys.tm.model.TMDepartment;
import com.cassinisys.tm.model.TMDepartmentPerson;
import com.cassinisys.tm.repo.DepartmentPersonRepository;
import com.cassinisys.tm.repo.DepartmentRepository;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by CassiniSystems on 03-08-2016.
 */
@Service
@Transactional
public class DepartmentService implements CrudService<TMDepartment, Integer>, PageableService<TMDepartment, Integer> {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentPersonRepository personRepository;

    @Autowired
    private DepartmentPredicateBuilder predicateBuilder;

    @Autowired
    private DepartmentPersonPredicateBuilder personPredicateBuilder;


    @Override
    public TMDepartment create(TMDepartment tmDepartment) {
        checkNotNull(tmDepartment);
        return departmentRepository.save(tmDepartment);
    }

    @Override
    public TMDepartment update(TMDepartment tmDepartment) {
        checkNotNull(tmDepartment);
        return departmentRepository.save(tmDepartment);
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        TMDepartment department = departmentRepository.findOne(id);
        if (department == null) {
            throw new ResourceNotFoundException();
        }
        departmentRepository.delete(id);
    }


    @Override
    public TMDepartment get(Integer id) {
        checkNotNull(id);
        TMDepartment department = departmentRepository.findOne(id);
        if (department == null) {
            throw new ResourceNotFoundException();
        }
        return department;
    }

    @Override
    public List<TMDepartment> getAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Page<TMDepartment> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<TMDepartment> findMultiple(List<Integer> ids) {
        return departmentRepository.findByIdIn(ids);
    }

    public Page<TMDepartment> findAll(DepartmentCriteria criteria, Pageable pageable) {
        Predicate predicate = predicateBuilder.build(criteria, QTMDepartment.tMDepartment);
        Page<TMDepartment> departments = departmentRepository.findAll(predicate, pageable);
        return departments;
    }

    public TMDepartmentPerson createDepartmentPerson(TMDepartmentPerson tMDepartmentPerson) {
        checkNotNull(tMDepartmentPerson);
        return personRepository.save(tMDepartmentPerson);
    }

    public TMDepartmentPerson updateDepartmentPerson(TMDepartmentPerson tMDepartmentPerson) {
        checkNotNull(tMDepartmentPerson);
        checkNotNull(tMDepartmentPerson.getRowId());
        return personRepository.save(tMDepartmentPerson);
    }

    public void deleteDepartmentPerson(Integer personId, Integer deptId) {
        checkNotNull(personId);
        checkNotNull(deptId);
        TMDepartmentPerson tMDepartmentPerson = personRepository.findByPersonAndDepartment(personId, deptId);
        if (tMDepartmentPerson == null) {
            throw new ResourceNotFoundException();
        }
        personRepository.delete(tMDepartmentPerson.getRowId());
    }

    public TMDepartmentPerson getDepartmentPerson(Integer Id) {
        checkNotNull(Id);
        TMDepartmentPerson tMDepartmentPerson = personRepository.findOne(Id);
        if (tMDepartmentPerson == null) {
            throw new ResourceNotFoundException();
        }
        return tMDepartmentPerson;
    }

    public List<TMDepartmentPerson> getAllDepartmentPersons() {
        return personRepository.findAll();
    }

    public Page<TMDepartmentPerson> findAllDepartmentPersons(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    public List<TMDepartmentPerson> savePersonsByDepartment(List<TMDepartmentPerson> departmentPersons) {

        return personRepository.save(departmentPersons);
    }

    public List<TMDepartmentPerson> getPersonsByDepartment(Integer deptId) {

        return personRepository.findByDepartment(deptId);
    }

    public Page<TMDepartmentPerson> findAllDepartmentPersons(DepartmentPersonCriteria criteria, Pageable pageable) {
        Predicate predicate = personPredicateBuilder.build(criteria, QTMDepartmentPerson.tMDepartmentPerson);
        Page<TMDepartmentPerson> departmentPersons = personRepository.findAll(predicate, pageable);
        return departmentPersons;
    }

    @Transactional(readOnly = true)
    public List<TMDepartmentPerson> findMultiplePersons(List<Integer> ids) {
        return personRepository.findByPersonIn(ids);
    }

}
