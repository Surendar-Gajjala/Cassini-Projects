package com.cassinisys.irste.service;

import com.cassinisys.irste.model.IRSTEGroupLocation;
import com.cassinisys.irste.model.IRSTELocation;
import com.cassinisys.irste.model.IRSTEUtilityLocation;
import com.cassinisys.irste.model.UtilityLocationId;
import com.cassinisys.irste.repo.IRSTEGroupLocationRepository;
import com.cassinisys.irste.repo.IRSTELocationRepository;
import com.cassinisys.irste.repo.IRSTEUtilityLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Nageshreddy on 13-11-2018.
 */
@Service
public class IRSTELocationService {

    @Autowired
    private IRSTELocationRepository irsteLocationRepository;

    @Autowired
    private IRSTEGroupLocationRepository groupLocationRepository;

    @Autowired
    private IRSTEUtilityLocationRepository utilityLocationRepository;

    @Transactional(readOnly = true)
    public IRSTEGroupLocation get(Integer id) {
        IRSTEGroupLocation groupLocation = groupLocationRepository.getOne(id);
        groupLocation.setLocations(irsteLocationRepository.findByGroupId(id));
        return groupLocation;
    }

    @Transactional(readOnly = true)
    public List<IRSTEGroupLocation> getAll() {
        List<IRSTEGroupLocation> groupLocations = groupLocationRepository.findAll();
        for (IRSTEGroupLocation groupLocation : groupLocations) {
            List<IRSTELocation> locations = irsteLocationRepository.findByGroupId(groupLocation.getId());
            Iterator<IRSTELocation> locationIterator = locations.iterator();
            while ((locationIterator.hasNext())) {
                IRSTELocation location = locationIterator.next();
                List<IRSTEUtilityLocation> utilityLocations = utilityLocationRepository.findByLocationId(location.getId());
                List<String> strings = new ArrayList();
                for (IRSTEUtilityLocation utilityLocation : utilityLocations) {
                    strings.add(utilityLocation.getId().getUtility());
                }
                location.setUtilities(strings);
            }
            groupLocation.setLocations(irsteLocationRepository.findByGroupId(groupLocation.getId()));
        }
        return groupLocations;
    }

    @Transactional(readOnly = false)
    public IRSTEGroupLocation create(IRSTEGroupLocation groupLocation) {
        List<IRSTELocation> locations = groupLocation.getLocations();
        locations = irsteLocationRepository.save(locations);
        IRSTEGroupLocation group1 = groupLocationRepository.save(groupLocation);
        group1.setLocations(locations);
        return group1;
    }

    @Transactional(readOnly = false)
    public IRSTEGroupLocation update(IRSTEGroupLocation groupLocation) {
        List<IRSTELocation> locations1 = irsteLocationRepository.findByGroupId(groupLocation.getId());
        List<IRSTELocation> locations = groupLocation.getLocations();
        if (locations != null) {
            if (locations1.size() > locations.size()) {
                for (IRSTELocation location : locations1) {
                    if (!locations.contains(location)) {
                        irsteLocationRepository.delete(location.getId());
                    }
                }
            }
        }
        locations = irsteLocationRepository.save(locations);
        IRSTEGroupLocation group1 = groupLocationRepository.save(groupLocation);
        group1.setLocations(locations);
        return group1;
    }

    @Transactional(readOnly = false)
    public void delete(Integer id) {
        if (id != null) {
            irsteLocationRepository.deleteByGroupId(id);
            groupLocationRepository.delete(id);
        }
    }

    @Transactional(readOnly = true)
    public List<IRSTELocation> getLocationsMultiple(List<Integer> integers) {
        return irsteLocationRepository.findByIdIn(integers);
    }


    public List<IRSTEUtilityLocation> getIRSTEUtilityLocations() {
        return utilityLocationRepository.findAll();
    }

    public IRSTEUtilityLocation addUtilityLocation(String utility, Integer location) {
        IRSTEUtilityLocation irsteUtilityLocation = new IRSTEUtilityLocation(new UtilityLocationId(utility, location));
        return utilityLocationRepository.save(irsteUtilityLocation);
    }

    public void deleteUtilityLocation(String utility, Integer location) {
        IRSTEUtilityLocation irsteUtilityLocation = new IRSTEUtilityLocation(new UtilityLocationId(utility, location));
        utilityLocationRepository.delete(irsteUtilityLocation);
    }


    /*@Autowired
    

    public List<IRSTELocation> findAllLocations() {
        return irsteLocationRepository.findAll();
    }

    public IRSTELocation createLocation(IRSTELocation irsteLocation) {
        return irsteLocationRepository.save(irsteLocation);
    }

    public IRSTELocation updateLocation(IRSTELocation irsteLocation) {
        return irsteLocationRepository.save(irsteLocation);
    }

    public void deleteLocation(Integer id) {
        irsteLocationRepository.delete(id);
    }

    public IRSTELocation getLocation(Integer integer) {
        return irsteLocationRepository.findOne(integer);
    }


    public List<IRSTEGroupLocation> findAllGroupLocations() {
        return groupLocationRepository.findAll();
    }

    public IRSTEGroupLocation createGroupLocation(IRSTEGroupLocation groupLocation) {
        return groupLocationRepository.save(groupLocation);
    }

    public IRSTEGroupLocation updateGroupLocation(IRSTEGroupLocation groupLocation) {
        return groupLocationRepository.save(groupLocation);
    }

    public void deleteGroupLocation(Integer id) {
        groupLocationRepository.delete(id);
    }

    public IRSTEGroupLocation getGroupLocation(Integer integer) {
        return groupLocationRepository.findOne(integer);
    }

    public List<IRSTEGroupLocation> getGroupLocationsWithAllValues() {
        List<IRSTEGroupLocation> groupLocations = groupLocationRepository.findAll();
        Iterator<IRSTEGroupLocation> irsteGroupLocationIterator = groupLocations.iterator();
        while (irsteGroupLocationIterator.hasNext()) {
            IRSTEGroupLocation irsteGroupLocation = irsteGroupLocationIterator.next();
            Set<IRSTELocation> locations = irsteGroupLocation.getLocations();
            Iterator<IRSTELocation> locationIterator = locations.iterator();
            while (locationIterator.hasNext()) {
                IRSTELocation location = locationIterator.next();
                List<Integer> utilityIds = utilityLocationRepository.findUtilitiesByLocationId(location.getId());
                List<IRSTEUtility> utilities = irsteUtilityRepository.findByIdIn(utilityIds);
                location.setUtilities(utilities);
            }
        }

        return groupLocations;
    }*/


}
