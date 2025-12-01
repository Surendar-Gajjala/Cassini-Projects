package com.cassinisys.tm.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.tm.filtering.AccommodationCriteria;
import com.cassinisys.tm.model.*;
import com.cassinisys.tm.model.dto.KeyAndNumber;
import com.cassinisys.tm.service.AccommodationService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by CassiniSystems on 03-08-2016.
 */
@Api(name = "Accommodation",
        description = "Accommodation endpoint")
@RestController
@RequestMapping("/accommodations")
public class AccommodationController extends BaseController {


    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private AccommodationService accommodationService;

    @RequestMapping(method = RequestMethod.POST)
    public TMAccommodation create(@RequestBody TMAccommodation accommodation) {

        return accommodationService.create(accommodation);
    }

    @RequestMapping(value = "/{accommodationId}", method = RequestMethod.PUT)
    public TMAccommodation update(@PathVariable("accommodationId") Integer accommodationId,
                                  @RequestBody TMAccommodation accommodation) {
        accommodation.setId(accommodationId);
        return accommodationService.update(accommodation);
    }

    @RequestMapping(value = "/{accommodationId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("accommodationId") Integer accommodationId) {
        accommodationService.delete(accommodationId);
    }

    @RequestMapping(value = "/{accommodationId}", method = RequestMethod.GET)
    public TMAccommodation get(@PathVariable("accommodationId") Integer accommodationId) {
        return accommodationService.get(accommodationId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<TMAccommodation> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return accommodationService.findAll(pageable);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<TMAccommodation> getMultiple(@PathVariable Integer[] ids) {
        return accommodationService.findMultipleAccommodations(Arrays.asList(ids));
    }

    @RequestMapping(value = "/{accommodationId}/suits",method = RequestMethod.POST)
    public TMAccommodationSuit createSuit( @PathVariable("accommodationId") Integer  accommodationId,
                                           @RequestBody TMAccommodationSuit accommodationSuit) {
        accommodationSuit.setAccommodation(accommodationId);
        return accommodationService.createAccommodationSuit(accommodationSuit);
    }

    @RequestMapping(value = "/{accommodationId}/suits/{suitId}/beds",method = RequestMethod.POST)
    public TMBed createBed( @PathVariable("suitId") Integer  suitId, @RequestBody TMBed tmBed) {
        tmBed.setSuite(suitId);
        return accommodationService.createSuitBed(tmBed);
    }

    @RequestMapping(value = "/{accommodationId}/suits/{suitId}/beds/{bedId}",method = RequestMethod.PUT)
    public TMBed updateBed( @PathVariable("suitId") Integer  suitId, @RequestBody TMBed tmBed) {
        tmBed.setSuite(suitId);
        return accommodationService.updateSuitBed(tmBed);
    }

    @RequestMapping(value = "/{accommodationId}/suits/{suitId}/beds",method = RequestMethod.GET)
    public List<TMBed> getBedsBySuiteId( @PathVariable("suitId") Integer  suitId) {
        return accommodationService.getBedsBySuitId(suitId);
    }

    @RequestMapping(value = "/{accommodationId}/suits/{suitId}", method = RequestMethod.PUT)
    public TMAccommodationSuit updateSuit(@PathVariable("suitId") Integer suitId,
                                  @RequestBody TMAccommodationSuit accommodationSuit) {
        accommodationSuit.setSuitId(suitId);
        return accommodationService.updateAccommodationSuit(accommodationSuit);
    }

    @RequestMapping(value = "/{accommodationId}/suits/{suitId}", method = RequestMethod.DELETE)
    public void deleteSuit(@PathVariable("suitId") Integer suitId) {
        accommodationService.deleteAccommodationSuit(suitId);
    }

    @RequestMapping(value = "/{suitId}/beds/{bedId}", method = RequestMethod.DELETE)
    public void deleteBed(@PathVariable("bedId") Integer bedId) {
        accommodationService.deleteBed(bedId);
    }

    @RequestMapping(value = "/{accommodationId}/suits/{suitId}", method = RequestMethod.GET)
    public TMAccommodationSuit getSuit(@PathVariable("suitId") Integer suitId) {
        return accommodationService.getAccommodationSuit(suitId);
    }

    @RequestMapping(value = "/suits/all",method = RequestMethod.GET)
    public List<TMAccommodationSuit> getAllSuits() {
        return accommodationService.getAllSuits();
    }
    @RequestMapping(value="/suits", method = RequestMethod.GET)
    public Page<TMAccommodationSuit> getAllSuits(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return accommodationService.findAllSuits(pageable);
    }

    @RequestMapping(value = "/{accommodationId}/suits",method = RequestMethod.GET)
    public List<TMAccommodationSuit> getSuitsByAccommodation(@PathVariable("accommodationId") Integer accommodationId){
        return accommodationService.getSuitsByAccommodation(accommodationId);
    }


    @RequestMapping( value = "/{accommodationId}/suits/{suitId}/persons",method = RequestMethod.POST)
    public TMSuit createSuitPerson(@RequestBody TMSuit suitPerson) {
        return accommodationService.createSuitPerson(suitPerson);
    }

    @RequestMapping(value = "/{accommodationId}/suits/{suitId}/persons/{personId}", method = RequestMethod.PUT)
    public TMSuit updateSuitPerson(@PathVariable("personId") Integer personId,
                                @RequestBody TMSuit suitPerson) {
        return accommodationService.updateSuitPerson(suitPerson);
    }


    @RequestMapping(value = "/{accommodationId}/suits/{suitId}/persons/{personId}", method = RequestMethod.DELETE)
    public void deleteSuitPerson(@PathVariable("personId") Integer personId){
        accommodationService.deleteSuitPerson(personId);
    }

    @RequestMapping(value = "/{accommodationId}/suits/{suitId}/persons/{personId}", method = RequestMethod.GET)
    public TMSuit getSuitPerson(@PathVariable("personId") Integer personId) {
        return accommodationService.getSuitPerson(personId);
    }

    @RequestMapping(value = "/suits/persons",method = RequestMethod.GET)
    public List<TMSuit> getAllSuitPersons() {
        return accommodationService.getAllSuitPersons();
    }

    @RequestMapping(value = "/suitPersons/all", method = RequestMethod.GET)
    public Page<TMSuit> getAllSuitPersons(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return accommodationService.findAllSuitPersons(pageable);
    }

    @RequestMapping(value = "/{accommodationId}/suits/{suitId}/persons", method = RequestMethod.GET)
    public List<TMSuit> getPersonsBySuit(@PathVariable("suitId") Integer suitId) {
        return accommodationService.getPersonsBySuit(suitId);
    }

    @RequestMapping(value = "/suits/{suitId}/counts", method = RequestMethod.GET)
    public List<KeyAndNumber> getSuiteAssignmentCounts(@PathVariable("suitId") Integer suitId) {
        return accommodationService.getSuiteCounts(suitId);
    }

    @RequestMapping(value = "/suits/beds/counts", method = RequestMethod.GET)
    public List<KeyAndNumber> getBedAssignmentCounts() {
        return accommodationService.getBedCounts();
    }

    @RequestMapping(value = "/suits/beds/{personId}", method = RequestMethod.GET)
    public TMBed getBedByAssignedTo(@PathVariable("personId") Integer personId) {
        return accommodationService.getBedByAssignedTo(personId);
    }

    @RequestMapping(value = "/suits/beds/occupied", method = RequestMethod.GET)
    public List<TMBed> getOccupiedBeds() {
        return accommodationService.getOccupiedBeds();
    }

    @RequestMapping(value = "/suits/beds/available", method = RequestMethod.GET)
    public List<TMBed> getAvailableBeds() {
        return accommodationService.getAvailableBeds();
    }

    @RequestMapping(value = "/suits/{suitId}/occupied", method = RequestMethod.GET)
    public List<TMBed> getSuiteBedsOccupied(@PathVariable("suitId") Integer suitId) {
        return accommodationService.getSuiteBedsOccupied(suitId);
    }

    @RequestMapping(value = "/suits/{suitId}/available", method = RequestMethod.GET)
    public List<TMBed> getSuiteBedsAvailable(@PathVariable("suitId") Integer suitId) {
        return accommodationService.getSuiteBedsAvailable(suitId);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<TMAccommodation> freeTextSearch(AccommodationCriteria criteria,
                                              PageRequest pageRequest) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return accommodationService.findAll(criteria, pageable);
    }


    @RequestMapping(value = "/counts", method = RequestMethod.GET)
    public List<KeyAndNumber> getCounts() {
        return accommodationService.getCounts();
    }

}
