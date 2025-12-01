package com.cassinisys.plm.controller.mro;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.SparePartCriteria;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.model.mro.MROSparePart;
import com.cassinisys.plm.service.mro.SparePartsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * Created by Suresh Cassini on 18-11-2020.
 */
@RestController
@RequestMapping("/mro/spareparts")
@Api(tags = "PLM.MRO", description = "MRO Related")
public class SparePartsController extends BaseController {
    @Autowired
    private SparePartsService sparePartsService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MROSparePart create(@RequestBody MROSparePart sparePart) {
        return sparePartsService.create(sparePart);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MROSparePart update(@PathVariable("id") Integer id,
                               @RequestBody MROSparePart sparePart) {
        return sparePartsService.update(sparePart);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        sparePartsService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MROSparePart get(@PathVariable("id") Integer id) {
        return sparePartsService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MROSparePart> getAll() {
        return sparePartsService.getAll();
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MROSparePart> filterSpareParts(PageRequest pageRequest, SparePartCriteria sparePartCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sparePartsService.getAllPartsByPageable(pageable, sparePartCriteria);
    }

    @RequestMapping(value = "/create/attributes/multiple", method = RequestMethod.POST)
    public void saveObjectAttributes(@RequestBody List<MROObjectAttribute> attributes) {
        sparePartsService.savemroObjectAttributes(attributes);
    }

    @RequestMapping(value = "/update/attributes", method = RequestMethod.PUT)
    public MROObjectAttribute updateObjectAttribute(@PathVariable("id") Integer id,
                                                    @RequestBody MROObjectAttribute attribute) {
        return sparePartsService.updateObjectAttribute(attribute);
    }

    @RequestMapping(value = "/uploadimageattribute/{objectid}/{attributeid}", method = RequestMethod.POST)
    public MROSparePart saveImageAttributeValue(@PathVariable("objectid") Integer objectId,
                                                @PathVariable("attributeid") Integer attributeId, MultipartHttpServletRequest request) {
        return sparePartsService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MROSparePart> getPartsByType(@PathVariable("typeId") Integer id,
                                             PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sparePartsService.getSparePartsByType(id, pageable);
    }

}