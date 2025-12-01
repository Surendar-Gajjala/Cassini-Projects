package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.EquipmentCriteria;
import com.cassinisys.plm.model.mes.MESEquipment;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.service.mes.EquipmentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Lenovo on 28-10-2020.
 */

@RestController
@RequestMapping("/mes/equipments")
@Api(tags = "PLM.MES", description = "MES Related")
public class EquipmentController extends BaseController {

    @Autowired(required = true)
    private EquipmentService equipmentService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESEquipment create(@RequestBody MESEquipment mesEquipment) {
        return equipmentService.create(mesEquipment);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESEquipment update(@PathVariable("id") Integer id,
                               @RequestBody MESEquipment mesEquipment) {
        return equipmentService.update(mesEquipment);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        equipmentService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESEquipment get(@PathVariable("id") Integer id) {
        return equipmentService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESEquipment> getAll() {
        return equipmentService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MESEquipment> getAllEquipments(PageRequest pageRequest, EquipmentCriteria equipmentCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return equipmentService.getAllEquipmentsByPageable(pageable, equipmentCriteria);
    }

    @RequestMapping(value = "/{id}/image", method = RequestMethod.POST)
    public MESEquipment uploadImage(@PathVariable("id") Integer id, MultipartHttpServletRequest request) {
        return equipmentService.uploadImage(id, request);
    }

    @RequestMapping(value = "/{equipmentId}/image/download", method = RequestMethod.GET)
    public void downloadImage(@PathVariable("equipmentId") Integer equipmentId,
                              HttpServletResponse response) {
        equipmentService.downloadImage(equipmentId, response);
    }

    @RequestMapping(value = "/{id}/attributes/multiple", method = RequestMethod.POST)
    public void saveEquipmentAttributes(@PathVariable("id") Integer id,
                                        @RequestBody List<MESObjectAttribute> attributes) {
        equipmentService.saveEquipmentAttributes(attributes);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public MESObjectAttribute updateEquipmentAttribute(@PathVariable("id") Integer id,
                                                       @RequestBody MESObjectAttribute attribute) {
        return equipmentService.updateEquipmentAttribute(attribute);
    }


    @RequestMapping(value = "/uploadimageattribute/{objectId}/{attributeId}", method = RequestMethod.POST)
    public MESEquipment saveImageAttributeValue(@PathVariable("objectId") Integer objectId,
                                                @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return equipmentService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MESEquipment> getEquipmentsByType(@PathVariable("typeId") Integer id,
                                                  PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return equipmentService.getEquipmentsByType(id, pageable);
    }

}