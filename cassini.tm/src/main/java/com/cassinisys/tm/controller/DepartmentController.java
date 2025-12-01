package com.cassinisys.tm.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.tm.filtering.DepartmentCriteria;
import com.cassinisys.tm.filtering.DepartmentPersonCriteria;
import com.cassinisys.tm.model.TMDepartment;
import com.cassinisys.tm.model.TMDepartmentPerson;
import com.cassinisys.tm.service.DepartmentService;
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

@Api(name = "Department",
        description = "Department endpoint")
@RestController
@RequestMapping("/departments")
public class DepartmentController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(method = RequestMethod.POST)
    public TMDepartment create(@RequestBody TMDepartment accommodation) {

        return departmentService.create(accommodation);
    }

    @RequestMapping(value = "/{departmentId}", method = RequestMethod.PUT)
    public TMDepartment update(@PathVariable("departmentId") Integer departmentId,
                               @RequestBody TMDepartment department) {
        department.setId(departmentId);
        return departmentService.update(department);
    }

    @RequestMapping(value = "/{departmentId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("departmentId") Integer departmentId) {
        departmentService.delete(departmentId);
    }

    @RequestMapping(value = "/{departmentId}", method = RequestMethod.GET)
    public TMDepartment get(@PathVariable("departmentId") Integer departmentId) {
        return departmentService.get(departmentId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<TMDepartment> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return departmentService.findAll(pageable);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<TMDepartment> getMultiple(@PathVariable Integer[] ids) {
        return departmentService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<TMDepartment> getAllDepartments(DepartmentCriteria criteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);

        return departmentService.findAll(criteria, pageable);
    }

    @RequestMapping(value = "/{departmentId}/persons", method = RequestMethod.POST)
    public TMDepartmentPerson createDepartmentPerson(@PathVariable("departmentId") Integer departmentId,
                                                     @RequestBody TMDepartmentPerson departmentPerson) {
        return departmentService.createDepartmentPerson(departmentPerson);
    }

    @RequestMapping(value = "/{departmentId}/persons/{personId}", method = RequestMethod.PUT)
    public TMDepartmentPerson updateDepartmentPerson(@PathVariable("departmentId") Integer departmentId,
                                                     @PathVariable("personId") Integer personId,
                                                     @RequestBody TMDepartmentPerson departmentPerson) {
        return departmentService.updateDepartmentPerson(departmentPerson);
    }


    @RequestMapping(value = "/{departmentId}/persons/{personId}", method = RequestMethod.DELETE)
    public void deleteDepartmentPerson(@PathVariable("personId") Integer personId,
                                       @PathVariable("departmentId") Integer departmentId) {
        departmentService.deleteDepartmentPerson(personId, departmentId);
    }


    @RequestMapping(value = "/{departmentId}/persons/{personId}", method = RequestMethod.GET)
    public TMDepartmentPerson getDepartmentPerson(@PathVariable("personId") Integer personId) {
        return departmentService.getDepartmentPerson(personId);
    }

    @RequestMapping(value = "/persons/all", method = RequestMethod.GET)
    public List<TMDepartmentPerson> getAllDepartmentPersons() {
        return departmentService.getAllDepartmentPersons();
    }

    @RequestMapping(value = "/{departmentId}/persons", method = RequestMethod.GET)
    public List<TMDepartmentPerson> getPersonsByDepartment(@PathVariable("departmentId") Integer departmentId) {
        return departmentService.getPersonsByDepartment(departmentId);
    }

    @RequestMapping(value = "/{departmentId}/persons", method = RequestMethod.PUT)
    public List<TMDepartmentPerson> savePersonsByDepartment(@RequestBody TMDepartmentPerson[] persons) {
        return departmentService.savePersonsByDepartment(Arrays.asList(persons));
    }

    @RequestMapping(value = "/persons/pageable", method = RequestMethod.GET)
    public Page<TMDepartmentPerson> getAllPersons(DepartmentPersonCriteria criteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return departmentService.findAllDepartmentPersons(criteria, pageable);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<TMDepartment> freeTextSearch(DepartmentCriteria criteria,
                                             PageRequest pageRequest) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return departmentService.findAll(criteria, pageable);
    }

    @RequestMapping(value = "/multiple/persons/[{ids}]", method = RequestMethod.GET)
    public List<TMDepartmentPerson> getMultiplePersons(@PathVariable Integer[] ids ) {
        return departmentService.findMultiplePersons(Arrays.asList(ids));
    }
}
