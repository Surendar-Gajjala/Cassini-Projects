package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.PlantCriteria;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESPlant;
import com.cassinisys.plm.service.mes.PlantService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * Created by Suresh CassiniPLM on 27-10-2020.
 */
@RestController
@RequestMapping("/mes/plants")
@Api(tags = "PLM.MES", description = "MES Related")
public class PlantController extends BaseController {
    @Autowired
    private PlantService plantService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESPlant create(@RequestBody MESPlant plant) {
        return plantService.create(plant);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESPlant update(@PathVariable("id") Integer id,
                           @RequestBody MESPlant plant) {
        return plantService.update(plant);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        plantService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESPlant get(@PathVariable("id") Integer id) {
        return plantService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESPlant> getAll() {
        return plantService.getAll();
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MESPlant> filterPlants(PageRequest pageRequest, PlantCriteria plantCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return plantService.getAllPlantsByPageable(pageable, plantCriteria);
    }

    @RequestMapping(value = "/create/attributes/multiple", method = RequestMethod.POST)
    public void savePlantAttributes(@RequestBody List<MESObjectAttribute> attributes) {
        plantService.savePlantAttributes(attributes);
    }

    @RequestMapping(value = "/update/attributes", method = RequestMethod.PUT)
    public MESObjectAttribute updatePlantAttribute(@PathVariable("id") Integer id,
                                                   @RequestBody MESObjectAttribute attribute) {
        return plantService.updatePlantAttribute(attribute);
    }

    @RequestMapping(value = "/uploadimageattribute/{objectid}/{attributeid}", method = RequestMethod.POST)
    public MESPlant saveImageAttributeValue(@PathVariable("objectid") Integer objectId,
                                            @PathVariable("attributeid") Integer attributeId, MultipartHttpServletRequest request) {
        return plantService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MESPlant> getPlantsByType(@PathVariable("typeId") Integer id,
                                                  PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return plantService.getPlantsByType(id, pageable);
    }

}