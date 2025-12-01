package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.MachineCriteria;
import com.cassinisys.plm.model.mes.MESMachine;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.service.mes.MachineService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Hello on 10/27/2020.
 */
@RestController
@RequestMapping("/mes/machines")
@Api(tags = "PLM.MES", description = "MES Related")
public class MachineController extends BaseController {
    @Autowired
    private MachineService machineService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESMachine create(@RequestBody MESMachine machine) {
        return machineService.create(machine);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESMachine update(@PathVariable("id") Integer id,
                             @RequestBody MESMachine machine) {
        return machineService.update(machine);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        machineService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESMachine get(@PathVariable("id") Integer id) {
        return machineService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESMachine> getAll() {
        return machineService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MESMachine> getAllMachines(PageRequest pageRequest, MachineCriteria machineCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return machineService.getAllMachinesByPageable(pageable, machineCriteria);
    }

    @RequestMapping(value = "/{id}/attributes/multiple", method = RequestMethod.POST)
    public void saveMachineAttributes(@PathVariable("id") Integer id,
                                      @RequestBody List<MESObjectAttribute> attributes) {
        machineService.saveMachineAttributes(attributes);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public MESObjectAttribute updateMachineAttribute(@PathVariable("id") Integer id,
                                                     @RequestBody MESObjectAttribute attribute) {
        return machineService.updateMachineAttribute(attribute);
    }

    @RequestMapping(value = "/uploadimageattribute/{objectId}/{attributeId}", method = RequestMethod.POST)
    public MESMachine saveImageAttributeValue(@PathVariable("objectId") Integer objectId,
                                              @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return machineService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MESMachine> getMachinesByType(@PathVariable("typeId") Integer id,
                                              PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return machineService.getMachinesByType(id, pageable);
    }

    @RequestMapping(value = "/{machineId}/image", method = RequestMethod.POST)
    public MESMachine uploadImage(@PathVariable("machineId") Integer machineId, MultipartHttpServletRequest request) {
        return machineService.uploadImage(machineId, request);
    }

    @RequestMapping(value = "/{machineId}/image/download", method = RequestMethod.GET)
    public void downloadImage(@PathVariable("machineId") Integer machineId,
                              HttpServletResponse response) {
        machineService.downloadImage(machineId, response);
    }
}
