package com.cassinisys.tm.service;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.tm.filtering.ShiftCriteria;
import com.cassinisys.tm.filtering.ShiftPersonCriteria;
import com.cassinisys.tm.filtering.ShiftPersonPredicateBuilder;
import com.cassinisys.tm.filtering.ShiftPredicateBuilder;
import com.cassinisys.tm.model.QTMShift;
import com.cassinisys.tm.model.QTMShiftPerson;
import com.cassinisys.tm.model.TMShift;
import com.cassinisys.tm.model.TMShiftPerson;
import com.cassinisys.tm.repo.ShiftPersonRepository;
import com.cassinisys.tm.repo.ShiftRepository;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Rajabrahmachary on 21-07-2016.
 */
@Service
@Transactional
public class ShiftService implements CrudService<TMShift, Integer>, PageableService<TMShift, Integer> {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private ShiftPersonRepository shiftPersonRepository;

    @Autowired
    private ShiftPredicateBuilder shiftPredicateBuilder;

    @Autowired
    private ShiftPersonPredicateBuilder shiftPersonPredicateBuilder;

    @Override
    public TMShift create(TMShift tmShift) {
        checkNotNull(tmShift);
        return shiftRepository.save(tmShift);
    }

    @Override
    public TMShift update(TMShift tmShift) {
        checkNotNull(tmShift);
        checkNotNull(tmShift.getEndTime());
        return shiftRepository.save(tmShift);
    }

    @Override
    public void delete(Integer Id) {

        checkNotNull(Id);
        TMShift shift = shiftRepository.findOne(Id);
        if (shift == null) {
            throw new ResourceNotFoundException();
        }
        shiftRepository.delete(shift);
    }

    @Override
    public TMShift get(Integer Id) {
        checkNotNull(Id);
        TMShift shift = shiftRepository.findOne(Id);
        if (shift == null) {
            throw new ResourceNotFoundException();
        }
        return shift;
    }

    @Override
    public List<TMShift> getAll() {
        return shiftRepository.findAll();
    }


    @Transactional(readOnly = true)
    public List<TMShift> findMultipleShifts(List<Integer> ids) {
        return shiftRepository.findByShiftIdIn(ids);
    }

    public TMShift getShiftById(Integer shiftId) {
        return shiftRepository.findOne(shiftId);
    }


    @Override
    public Page<TMShift> findAll(Pageable pageable) {
        return shiftRepository.findAll(pageable);
    }

    public Page<TMShiftPerson> findAllShifts(Pageable pageable) {
        return shiftPersonRepository.findAll(pageable);
    }

    public Page<TMShift> freeTextSearch(ShiftCriteria criteria, Pageable pageable) {
        /*if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                    new Sort(new Sort.Order(Sort.Direction.DESC, "name")));
        }*/
        Predicate predicate = shiftPredicateBuilder.build(criteria, QTMShift.tMShift);
        Page<TMShift> shifts = shiftRepository.findAll(predicate, pageable);
        return shifts;
    }

    public TMShiftPerson create(TMShiftPerson tmShiftPerson) {
        checkNotNull(tmShiftPerson);
        return shiftPersonRepository.save(tmShiftPerson);
    }

    public TMShiftPerson update(TMShiftPerson tmShiftPerson) {
        checkNotNull(tmShiftPerson);
        return shiftPersonRepository.save(tmShiftPerson);
    }

    public void deleteShiftPerson(Integer personId, Integer shiftId) {
        checkNotNull(personId);
        checkNotNull(shiftId);
        TMShiftPerson tmShiftPerson = shiftPersonRepository.findByPersonAndShift(personId, shiftId);
        if (tmShiftPerson == null) {
            throw new ResourceNotFoundException();
        }
        shiftPersonRepository.delete(tmShiftPerson.getRowId());
    }

    public TMShiftPerson getShift(Integer Id) {
        checkNotNull(Id);
        TMShiftPerson tmShiftPerson = shiftPersonRepository.findOne(Id);
        if (tmShiftPerson == null) {
            throw new ResourceNotFoundException();
        }
        return tmShiftPerson;
    }

    public List<TMShiftPerson> getAllShifts() {
        return shiftPersonRepository.findAll();
    }


    public List<TMShiftPerson> savePersonsBasedOnShift(List<TMShiftPerson> shiftPersons) {

        return shiftPersonRepository.save(shiftPersons);
    }

    public List<TMShiftPerson> getPersonsBasedOnShift(Integer shiftId) {

        return shiftPersonRepository.findByShift(shiftId);
    }

    public Page<TMShiftPerson> findAll(ShiftPersonCriteria criteria, Pageable pageable) {
        Predicate predicate = shiftPersonPredicateBuilder.build(criteria, QTMShiftPerson.tMShiftPerson);
        Page<TMShiftPerson> shiftPersons = shiftPersonRepository.findAll(predicate, pageable);
        return shiftPersons;
    }
}

