package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.ShiftCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.ShiftPersonListDto;
import com.cassinisys.plm.model.mes.MESShift;
import com.cassinisys.plm.model.mes.MESShiftPerson;
import com.cassinisys.plm.model.pm.PLMProjectMember;
import com.cassinisys.plm.service.mes.ShiftService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Suresh CassiniPLM on 27-10-2020.
 */
@RestController
@RequestMapping("/mes/shifts")
@Api(tags = "PLM.MES", description = "MES Related")
public class ShiftController extends BaseController {
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESShift create(@RequestBody MESShift shift) {
        return shiftService.create(shift);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESShift update(@PathVariable("id") Integer id,
                           @RequestBody MESShift shift) {
        return shiftService.update(shift);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        shiftService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESShift get(@PathVariable("id") Integer id) {
        return shiftService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESShift> getAll() {
        return shiftService.getAll();
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MESShift> filterShifts(PageRequest pageRequest, ShiftCriteria shiftCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return shiftService.getAllShiftsByPageable(pageable, shiftCriteria);
    }


    @RequestMapping(value = "/{shiftId}/person", method = RequestMethod.POST)
    public MESShiftPerson createShiftPerson(@PathVariable("shiftId") Integer id, @RequestBody MESShiftPerson shiftPerson) {
        return shiftService.createShiftPerson(id, shiftPerson);
    }

    @RequestMapping(value = "/{shiftId}/person/{id}", method = RequestMethod.PUT)
    public MESShiftPerson updateShiftPerson(@PathVariable("id") Integer id,
                                                 @RequestBody MESShiftPerson shiftPerson) {
        return shiftService.updateShiftPerson(id, shiftPerson);
    }

    @RequestMapping(value = "/{shiftId}/person/multiple", method = RequestMethod.POST)
    public List<MESShiftPerson> createShiftPersons(@PathVariable("shiftId") Integer Id,
                                                        @RequestBody List<MESShiftPerson> shiftPersons) {
        return shiftService.createShiftPersons(Id, shiftPersons);
    }

    @RequestMapping(value = "/{shiftId}/persons", method = RequestMethod.GET)
    public Page<MESShiftPerson> getShiftPersons(@PathVariable("shiftId") Integer shiftId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return shiftService.getShiftPersons(shiftId, pageable);
    }

    @RequestMapping(value = "/{shiftId}/person/{personId}", method = RequestMethod.DELETE)
    public void deleteShiftPerson(@PathVariable("shiftId") Integer shiftId, @PathVariable("personId") Integer personId)  {
        shiftService.deleteShiftPerson(shiftId, personId);
    }

    @RequestMapping(value = "/{id}/counts", method = RequestMethod.GET)
    public ItemDetailsDto getTabCounts(@PathVariable("id") Integer id) {
        return shiftService.getShiftTabCount(id);
    }

    @RequestMapping(value = "/persons", method = RequestMethod.GET)
    public List<ShiftPersonListDto> getShiftPersonList() {
        return shiftService.getShiftPersonList();
    }

}