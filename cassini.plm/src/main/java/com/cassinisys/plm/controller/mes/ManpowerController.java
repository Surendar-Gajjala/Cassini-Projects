package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.filtering.PersonCriteria;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.ManpowerContactCriteria;
import com.cassinisys.plm.filtering.ManpowerCriteria;
import com.cassinisys.plm.model.mes.MESManpower;
import com.cassinisys.plm.model.mes.MESManpowerContact;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.service.mes.ManpowerService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by aredl on 28-10-2020.
 */
@RestController
@RequestMapping("/mes/manpowers")
@Api(tags = "PLM.MES", description = "MES Related")
public class ManpowerController extends BaseController {
    @Autowired
    private ManpowerService manpowerService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESManpower create(@RequestBody MESManpower manpower) {
        return manpowerService.create(manpower);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESManpower update(@PathVariable("id") Integer id,
                           @RequestBody MESManpower manpower) {
        return manpowerService.update(manpower);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        manpowerService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESManpower get(@PathVariable("id") Integer id) {
        return manpowerService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESManpower> getAll() {
        return manpowerService.getAll();
    }


    @RequestMapping(value = "/{id}/attributes/multiple", method = RequestMethod.POST)
    public void saveManpowerAttributes(@PathVariable("id") Integer id,
                                       @RequestBody List<MESObjectAttribute> attributes) {
        manpowerService.saveManpowerAttributes(attributes);
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MESManpower> allManpowers(PageRequest pageRequest, ManpowerCriteria manpowerCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manpowerService.getAllManpowersByPageable(pageable, manpowerCriteria);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MESManpower> getManpowerByType(@PathVariable("typeId") Integer id,
                                              PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manpowerService.getManpowerByType(id, pageable);
    }


    @RequestMapping(value = "/filtered", method = RequestMethod.GET)
    public Page<MESManpower> filterManpowers(PageRequest pageRequest, ManpowerCriteria manpowerCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manpowerService.getAllFilteredManpowers(pageable, manpowerCriteria);
    }
    /*
     * Manpower Persons
     */

    @RequestMapping(value = "/{manpowerid}/contacts", method = RequestMethod.GET)
    public List<MESManpowerContact> getManpowerContacts(@PathVariable("manpowerid") Integer manpowerId) {
        return manpowerService.getManpowerContacts(manpowerId);
    }

    @RequestMapping(value = "/{manpowerid}/contacts", method = RequestMethod.POST)
    public MESManpowerContact createContact(@RequestBody MESManpowerContact contact) throws JsonProcessingException {
        return manpowerService.createContactManpower(contact);
    }

    @RequestMapping(value = "/{manpowerid}/contacts/{id}", method = RequestMethod.PUT)
    public MESManpowerContact updateContact(@PathVariable("id") Integer id,
                                            @RequestBody MESManpowerContact contact) {
        return manpowerService.updateManpowerContact(contact);
    }

    @RequestMapping(value = "/{manpowerid}/contacts/{id}", method = RequestMethod.GET)
    public MESManpowerContact getContact(@PathVariable("id") Integer id) {
        return manpowerService.getContact(id);
    }

    @RequestMapping(value = "/{manpowerid}/contacts/{id}", method = RequestMethod.DELETE)
    public void deleteContact(@PathVariable("id") Integer id) throws JsonProcessingException {
        manpowerService.deleteContact(id);

    }

    @RequestMapping(value = "/contacts/all", method = RequestMethod.GET)
    public Page<Person> getAllManpowerContacts(PageRequest pageRequest, ManpowerContactCriteria manpowerContactCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manpowerService.getAllManpowerContacts(pageable, manpowerContactCriteria);
    }

    @RequestMapping(value = "/contacts/{id}", method = RequestMethod.GET)
    public Integer getAllManpowerContactExitOrNot(@PathVariable("id") Integer id) {
        return manpowerService.getAllManpowerContactExitOrNot(id);
    }

}
