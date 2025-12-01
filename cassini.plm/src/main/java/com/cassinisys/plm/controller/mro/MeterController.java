package com.cassinisys.plm.controller.mro;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.MeterCriteria;
import com.cassinisys.plm.model.mro.MROMeter;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.service.mro.MeterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Lenovo on 18-11-2020.
 */
@RestController
@RequestMapping("/mro/meters")
@Api(tags = "PLM.MRO", description = "MRO Related")
public class MeterController extends BaseController{

    @Autowired
    private MeterService meterService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MROMeter create(@RequestBody MROMeter meter) {
        return meterService.create(meter);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MROMeter update(@PathVariable("id") Integer id,
                           @RequestBody MROMeter meter) {
        return meterService.update(meter);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        meterService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MROMeter get(@PathVariable("id") Integer id) {
        return meterService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MROMeter> getAll() {
        return meterService.getAll();
    }

    @RequestMapping(value = "/create/attributes/multiple", method = RequestMethod.POST)
    public void saveObjectAttributes(@RequestBody List<MROObjectAttribute> attributes) {
        meterService.savemroObjectAttributes(attributes);
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MROMeter> filterMeters(PageRequest pageRequest, MeterCriteria meterCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return meterService.getAllMetersByPageable(pageable, meterCriteria);
    }

   @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MROMeter> getMetersByType(@PathVariable("typeId") Integer id,
                                             PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return meterService.getMetersByType(id, pageable);
    }



}
