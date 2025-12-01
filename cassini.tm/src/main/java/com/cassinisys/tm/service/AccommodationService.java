package com.cassinisys.tm.service;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.tm.filtering.AccommodationCriteria;
import com.cassinisys.tm.filtering.AccommodationPredicateBuilder;
import com.cassinisys.tm.model.*;
import com.cassinisys.tm.model.dto.KeyAndNumber;
import com.cassinisys.tm.repo.AccommodationRepository;
import com.cassinisys.tm.repo.AccommodationSuitRepository;
import com.cassinisys.tm.repo.BedRepository;
import com.cassinisys.tm.repo.SuitRepository;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by CassiniSystems on 03-08-2016.
 */
@Service
@Transactional
public class AccommodationService implements CrudService<TMAccommodation, Integer>, PageableService<TMAccommodation, Integer> {

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private AccommodationSuitRepository accommodationSuitRepository;

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private AccommodationPredicateBuilder accommodationPredicateBuilder;


    @Autowired
    private SuitRepository suitRepository;

    @Override
    public TMAccommodation create(TMAccommodation accommodation) {
        checkNotNull(accommodation);
        return accommodationRepository.save(accommodation);
    }

    @Override
    public TMAccommodation update(TMAccommodation accommodation) {
        checkNotNull(accommodation);
        checkNotNull(accommodation.getId());
        return accommodationRepository.save(accommodation);
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        TMAccommodation accommodation = accommodationRepository.findOne(id);
        if (accommodation == null) {
            throw new ResourceNotFoundException();
        }
        accommodationRepository.delete(id);
    }

    @Override
    public TMAccommodation get(Integer id) {
        checkNotNull(id);
        TMAccommodation accommodation = accommodationRepository.findOne(id);
        if (accommodation == null) {
            throw new ResourceNotFoundException();
        }
        return accommodation;
    }

    @Override
    public List<TMAccommodation> getAll() {
        return accommodationRepository.findAll();
    }

    @Override
    public Page<TMAccommodation> findAll(Pageable pageable) {
        checkNotNull(pageable);
        return accommodationRepository.findAll(pageable);
    }

    public Page<TMAccommodation> findAll(AccommodationCriteria criteria, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                    new Sort(new Sort.Order(Sort.Direction.DESC, "name")));
        }
        Predicate predicate = accommodationPredicateBuilder.build(criteria, QTMAccommodation.tMAccommodation);
        Page<TMAccommodation> accs = accommodationRepository.findAll(predicate, pageable);
        return accs;
    }

    @Transactional(readOnly = true)
    public List<TMAccommodation> findMultipleAccommodations(List<Integer> ids) {
        return accommodationRepository.findByIdIn(ids);
    }

    public TMAccommodationSuit createAccommodationSuit(TMAccommodationSuit accommodationSuit) {
        return accommodationSuitRepository.save(accommodationSuit);
    }

    public TMBed createSuitBed(TMBed tmBed) {
        return bedRepository.save(tmBed);
    }

    public TMBed updateSuitBed(TMBed tmBed) {
        return bedRepository.save(tmBed);
    }

    public TMAccommodationSuit updateAccommodationSuit(TMAccommodationSuit accommodationSuit) {
        checkNotNull(accommodationSuit);
        checkNotNull(accommodationSuit.getSuitId());
        return accommodationSuitRepository.save(accommodationSuit);
    }

    public void deleteAccommodationSuit(Integer suitId) {
        checkNotNull(suitId);
        TMAccommodationSuit accommodationSuit = accommodationSuitRepository.findOne(suitId);
        if (accommodationSuit == null) {
            throw new ResourceNotFoundException();
        }
        accommodationSuitRepository.delete(accommodationSuit.getSuitId());
    }

     public void deleteBed(Integer bedId) {
        checkNotNull(bedId);
        bedRepository.delete(bedId);
    }

    public TMAccommodationSuit getAccommodationSuit(Integer Id) {
        checkNotNull(Id);
        TMAccommodationSuit accommodationSuit = accommodationSuitRepository.findOne(Id);
        if (accommodationSuit == null) {
            throw new ResourceNotFoundException();
        }
        return accommodationSuit;
    }

    public List<TMAccommodationSuit> getAllSuits() {
        return accommodationSuitRepository.findAll();
    }

    public Page<TMAccommodationSuit> findAllSuits(Pageable pageable) {
        return accommodationSuitRepository.findAll(pageable);
    }

    public List<TMAccommodationSuit> getSuitsByAccommodation(Integer accomId) {

        return accommodationSuitRepository.findByAccommodation(accomId);
    }

    public List<TMBed> getBedsBySuitId(Integer suitId) {
        return bedRepository.findBySuite(suitId);
    }



    public TMSuit createSuitPerson(TMSuit suitPerson) {
        checkNotNull(suitPerson);
        return suitRepository.save(suitPerson);
    }

    public TMSuit updateSuitPerson(TMSuit suitPerson) {
        checkNotNull(suitPerson);
        checkNotNull(suitPerson.getRowId());
        return suitRepository.save(suitPerson);
    }

    public void deleteSuitPerson(Integer id) {
        checkNotNull(id);
        accommodationSuitRepository.delete(id);
    }

    public TMSuit getSuitPerson(Integer Id) {
        checkNotNull(Id);
        TMSuit suitPerson = suitRepository.findOne(Id);
        if (suitPerson == null) {
            throw new ResourceNotFoundException();
        }
        return suitPerson;
    }

    public List<TMSuit> getAllSuitPersons() {
        return suitRepository.findAll();
    }

    public Page<TMSuit> findAllSuitPersons(Pageable pageable) {
        return suitRepository.findAll(pageable);
    }

    public List<TMSuit> getPersonsBySuit(Integer suitId) {

        return suitRepository.findBySuite(suitId);
    }

    public List<KeyAndNumber> getCounts() {
        List<KeyAndNumber> counts = new ArrayList<>();
        counts.add(new KeyAndNumber("accommodations", accommodationRepository.count()));
        counts.add(new KeyAndNumber("suites", accommodationSuitRepository.count()));
        counts.add(new KeyAndNumber("beds", bedRepository.count()));
        return counts;
    }

    public List<KeyAndNumber> getSuiteCounts(Integer suiteId) {
        List<KeyAndNumber> counts = new ArrayList<>();
        counts.add(new KeyAndNumber("occupied", bedRepository.findBySuiteAndAssignedToIsNotNull(suiteId).size()));
        counts.add(new KeyAndNumber("available", bedRepository.findBySuiteAndAssignedToIsNull(suiteId).size()));
        return counts;
    }

    public List<TMBed> getSuiteBedsOccupied(Integer suiteId) {
        return bedRepository.findBySuiteAndAssignedToIsNotNull(suiteId);
    }

    public List<TMBed> getSuiteBedsAvailable(Integer suiteId) {
        return bedRepository.findBySuiteAndAssignedToIsNull(suiteId);
    }

    public List<KeyAndNumber> getBedCounts() {
        List<KeyAndNumber> counts = new ArrayList<>();
        counts.add(new KeyAndNumber("occupied", bedRepository.findByAssignedToIsNotNull().size()));
        counts.add(new KeyAndNumber("available", bedRepository.findByAssignedToIsNull().size()));
        return counts;
    }

    public List<TMBed> getOccupiedBeds() {
        return bedRepository.findByAssignedToIsNotNull();
    }

    public List<TMBed> getAvailableBeds() {
        return bedRepository.findByAssignedToIsNull();
    }

    public TMBed getBedByAssignedTo(Integer personId) {
        return bedRepository.findByAssignedTo(personId);
    }
}
