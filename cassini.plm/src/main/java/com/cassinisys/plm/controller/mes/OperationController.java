package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.OperationCriteria;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESOperation;
import com.cassinisys.plm.model.mes.MESOperationResources;
import com.cassinisys.plm.model.mes.dto.OperationResourceTypeDto;
import com.cassinisys.plm.service.mes.OperationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * Created by Lenovo on 29-10-2020.
 */
@RestController
@RequestMapping("/mes/operations")
@Api(tags = "PLM.MES", description = "MES Related")
public class OperationController extends BaseController {

    @Autowired
    private OperationService operationService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESOperation create(@RequestBody MESOperation operation) {
        return operationService.create(operation);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESOperation update(@PathVariable("id") Integer id,
                               @RequestBody MESOperation operation) {
        return operationService.update(operation);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        operationService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESOperation get(@PathVariable("id") Integer id) {
        return operationService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESOperation> getAll() {
        return operationService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MESOperation> getAllOperations(PageRequest pageRequest, OperationCriteria operationCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return operationService.getAllOperationsByPageable(pageable, operationCriteria);
    }


    @RequestMapping(value = "/{id}/attributes/multiple", method = RequestMethod.POST)
    public void saveOperationAttributes(@PathVariable("id") Integer id,
                                        @RequestBody List<MESObjectAttribute> attributes) {
        operationService.saveOperationAttributes(attributes);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public MESObjectAttribute updateOperationAttribute(@PathVariable("id") Integer id,
                                                       @RequestBody MESObjectAttribute attribute) {
        return operationService.updateOperationAttribute(attribute);
    }


    @RequestMapping(value = "/uploadimageattribute/{objectId}/{attributeId}", method = RequestMethod.POST)
    public MESOperation saveImageAttributeValue(@PathVariable("objectId") Integer objectId,
                                                @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return operationService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MESOperation> getOperationsByType(@PathVariable("typeId") Integer id,
                                                  PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return operationService.getOperationsByType(id, pageable);
    }

    @RequestMapping(value = "/resources", method = RequestMethod.POST)
    public MESOperationResources createOperationResources(@RequestBody MESOperationResources operationResources) {
        return operationService.createOperationResources(operationResources);
    }

    @RequestMapping(value = "/{id}/resources", method = RequestMethod.GET)
    public List<MESOperationResources> getOperationResources(@PathVariable("id") Integer id) {
        return operationService.getOperationResources(id);
    }

    @RequestMapping(value = "/{id}/resources/{planId}/list", method = RequestMethod.GET)
    public List<OperationResourceTypeDto> getOperationResourceList(@PathVariable("id") Integer id, @PathVariable("planId") Integer planId) {
        return operationService.getOperationResourceList(id, planId);
    }

    @RequestMapping(value = "/{operationID}/resource/{resourceId}", method = RequestMethod.DELETE)
    public void MESOperationResources(@PathVariable("operationID") Integer operationID, @PathVariable("resourceId") Integer resourceId) {
        operationService.deleteOperationResource(resourceId);
    }

    @RequestMapping(value = "/resources/{resourceId}", method = RequestMethod.PUT)
    public MESOperationResources updateOperationResources(@PathVariable("resourceId") Integer resourceId,
                                                          @RequestBody MESOperationResources resources) {
        return operationService.updateOperationResources(resourceId, resources);
    }

}
