package com.cassinisys.plm.controller.mro;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.MROObjectCriteria;
import com.cassinisys.plm.model.mes.MESObjectTypesDto;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.mro.dto.MROObjectTypesDto;
import com.cassinisys.plm.service.classification.MROObjectTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/mro/objecttypes")
@Api(tags = "PLM.MES", description = "MES Related")
public class MROObjectTypeController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private MROObjectTypeService mroObjectTypeService;


    @RequestMapping(method = RequestMethod.POST)
    public MROObjectType create(@RequestBody MROObjectType itemType) {
        return mroObjectTypeService.create(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MROObjectType update(@PathVariable("id") Integer id,
                                @RequestBody MROObjectType itemType) {
        return mroObjectTypeService.update(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        mroObjectTypeService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MROObjectType get(@PathVariable("id") Integer id) {
        return mroObjectTypeService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MROObjectType> getAll() {
        return mroObjectTypeService.getRootTypes();
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<MROObjectType> getMROObjectTypeTree() {
        return mroObjectTypeService.getMROObjectTypeTree();
    }

    /*------------------------ sparePartType ---------------------------*/
    @RequestMapping(value = "/spareparttype", method = RequestMethod.POST)
    public MROSparePartType createSparePartType(@RequestBody MROSparePartType sparePartType) {
        return mroObjectTypeService.createSparePartType(sparePartType);
    }

    @RequestMapping(value = "/spareparttype/{id}", method = RequestMethod.PUT)
    public MROSparePartType updateSparePartType(@PathVariable("id") Integer id, @RequestBody MROSparePartType sparePartType) {
        return mroObjectTypeService.updateSparePartType(id, sparePartType);
    }

    @RequestMapping(value = "/spareparttype/{id}", method = RequestMethod.DELETE)
    public void deleteSparePartType(@PathVariable("id") Integer id) {
        mroObjectTypeService.deleteSparePartType(id);
    }

    @RequestMapping(value = "/spareparttype/{id}", method = RequestMethod.GET)
    public MROSparePartType getSparePartType(@PathVariable("id") Integer id) {
        return mroObjectTypeService.getSparePartType(id);
    }

    @RequestMapping(value = "/spareparttype", method = RequestMethod.GET)
    public List<MROSparePartType> getAllSparePartTypes() {
        return mroObjectTypeService.getAllSparePartTypes();
    }

    @RequestMapping(value = "/spareparttype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MROSparePartType> findMultipleSparePartTypes(@PathVariable Integer[] ids) {
        return mroObjectTypeService.findMultipleSparePartTypes(Arrays.asList(ids));
    }


    @RequestMapping(value = "/spareparttype/tree", method = RequestMethod.GET)
    public List<MROSparePartType> getSparePartTypeTree() {
        return mroObjectTypeService.getSparePartTypeTree();
    }

    /*-------------------------- WorkRequest -------------------------------- */

    @RequestMapping(value = "/workrequesttype", method = RequestMethod.POST)
    public MROWorkRequestType createWorkRequestType(@RequestBody MROWorkRequestType WorkRequest) {
        return mroObjectTypeService.createWorkRequestType(WorkRequest);
    }

    @RequestMapping(value = "/workrequesttype/{id}", method = RequestMethod.PUT)
    public MROWorkRequestType updateWorkRequestType(@PathVariable("id") Integer id, @RequestBody MROWorkRequestType WorkRequest) {
        return mroObjectTypeService.updateWorkRequestType(id, WorkRequest);
    }

    @RequestMapping(value = "/workrequesttype/{id}", method = RequestMethod.DELETE)
    public void deleteWorkRequestType(@PathVariable("id") Integer id) {
        mroObjectTypeService.deleteWorkRequestType(id);
    }

    @RequestMapping(value = "/workrequesttype/{id}", method = RequestMethod.GET)
    public MROWorkRequestType getWorkRequestType(@PathVariable("id") Integer id) {
        return mroObjectTypeService.getWorkRequestType(id);
    }

    @RequestMapping(value = "/workrequesttype", method = RequestMethod.GET)
    public List<MROWorkRequestType> getAllWorkRequestTypes() {
        return mroObjectTypeService.getAllWorkRequestTypes();
    }

    @RequestMapping(value = "/workrequesttype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MROWorkRequestType> getMultipleWorkRequestTypes(@PathVariable Integer[] ids) {
        return mroObjectTypeService.findMultipleWorkRequestTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/workrequesttype/tree", method = RequestMethod.GET)
    public List<MROWorkRequestType> getWorkRequestTypeTree() {
        return mroObjectTypeService.getWorkRequestTypeTree();
    }

    /*--------------------- workOrder Type ------------------------ */

    @RequestMapping(value = "/workordertype", method = RequestMethod.POST)
    public MROWorkOrderType createWorkOrderType(@RequestBody MROWorkOrderType workOrder) {

        return mroObjectTypeService.createWorkOrderType(workOrder);
    }

    @RequestMapping(value = "/workordertype/{id}", method = RequestMethod.PUT)
    public MROWorkOrderType updateWorkOrderType(@PathVariable("id") Integer id, @RequestBody MROWorkOrderType workOrder) {
        return mroObjectTypeService.updateWorkOrderType(id, workOrder);
    }

    @RequestMapping(value = "/workordertype/{id}", method = RequestMethod.DELETE)
    public void deleteWorkOrderType(@PathVariable("id") Integer id) {
        mroObjectTypeService.deleteWorkOrderType(id);
    }

    @RequestMapping(value = "/workordertype/{id}", method = RequestMethod.GET)
    public MROWorkOrderType getWorkOrderType(@PathVariable("id") Integer id) {
        return mroObjectTypeService.getWorkOrderType(id);
    }

    @RequestMapping(value = "/workordertype", method = RequestMethod.GET)
    public List<MROWorkOrderType> getAllWorkOrderTypes() {
        return mroObjectTypeService.getAllWorkOrderTypes();
    }

    @RequestMapping(value = "/workordertype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MROWorkOrderType> getMultipleWorkOrderTypes(@PathVariable Integer[] ids) {
        return mroObjectTypeService.findMultipleWorkOrderTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/workordertype/tree", method = RequestMethod.GET)
    public List<MROWorkOrderType> getWorkOrderTypeTree() {
        return mroObjectTypeService.getWorkOrderTypeTree();
    }

    @RequestMapping(value = "/repairworkordertype/tree", method = RequestMethod.GET)
    public List<MROWorkOrderType> getRepairWorkOrderTypeTree() {
        return mroObjectTypeService.getRepairWorkOrderTypeTree();
    }


        /*-------------------------- assetType -------------------------------- */

    @RequestMapping(value = "/assettype", method = RequestMethod.POST)
    public MROAssetType createAssetType(@RequestBody MROAssetType assetType) {
        return mroObjectTypeService.createAssetType(assetType);
    }

    @RequestMapping(value = "/assettype/{id}", method = RequestMethod.PUT)
    public MROAssetType updateAssetType(@PathVariable("id") Integer id, @RequestBody MROAssetType assetType) {
        return mroObjectTypeService.updateAssetType(id, assetType);
    }

    @RequestMapping(value = "/assettype/{id}", method = RequestMethod.DELETE)
    public void deleteAssetType(@PathVariable("id") Integer id) {
        mroObjectTypeService.deleteAssetType(id);
    }

    @RequestMapping(value = "/assettype/{id}", method = RequestMethod.GET)
    public MROAssetType getAssetType(@PathVariable("id") Integer id) {
        return mroObjectTypeService.getAssetType(id);
    }

    @RequestMapping(value = "/assettype", method = RequestMethod.GET)
    public List<MROAssetType> getAllAssetTypes() {
        return mroObjectTypeService.getAllAssetTypes();
    }

    @RequestMapping(value = "/assettype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MROAssetType> getMultipleAssetTypes(@PathVariable Integer[] ids) {
        return mroObjectTypeService.findMultipleAssetTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/assettype/tree", method = RequestMethod.GET)
    public List<MROAssetType> getAssetTypeTree() {
        return mroObjectTypeService.getAssetTypeTree();
    }


        /*-------------------------- Meter Type -------------------------------- */

    @RequestMapping(value = "/metertype", method = RequestMethod.POST)
    public MROMeterType createMeterType(@RequestBody MROMeterType meterType) {
        return mroObjectTypeService.createMeterType(meterType);
    }

    @RequestMapping(value = "/metertype/{id}", method = RequestMethod.PUT)
    public MROMeterType updateMeterType(@PathVariable("id") Integer id, @RequestBody MROMeterType meterType) {
        return mroObjectTypeService.updateMeterType(id, meterType);
    }

    @RequestMapping(value = "/metertype/{id}", method = RequestMethod.DELETE)
    public void deleteMeterType(@PathVariable("id") Integer id) {
        mroObjectTypeService.deleteMeterType(id);
    }

    @RequestMapping(value = "/metertype/{id}", method = RequestMethod.GET)
    public MROMeterType getMeterType(@PathVariable("id") Integer id) {
        return mroObjectTypeService.getMeterType(id);
    }

    @RequestMapping(value = "/metertype", method = RequestMethod.GET)
    public List<MROMeterType> getAllMeterTypes() {
        return mroObjectTypeService.getAllMeterTypes();
    }

    @RequestMapping(value = "/metertype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MROMeterType> getMultipleMeterTypes(@PathVariable Integer[] ids) {
        return mroObjectTypeService.findMultipleMeterTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/metertype/tree", method = RequestMethod.GET)
    public List<MROMeterType> getMeterTypeTree() {
        return mroObjectTypeService.getMeterTypeTree();
    }


    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public MROObjectAttribute createMESObjectAttribute(@PathVariable("id") Integer id,
                                                       @RequestBody MROObjectAttribute attribute) {
        return mroObjectTypeService.createMROObjectAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public MROObjectAttribute updateMESObjectAttribute(@PathVariable("id") Integer id,
                                                       @RequestBody MROObjectAttribute attribute) {
        return mroObjectTypeService.updateMROObjectAttribute(attribute);
    }


    @RequestMapping(value = "/type/{typeid}/attributes", method = RequestMethod.GET)
    public List<MROObjectTypeAttribute> getHierarchicalTypeAttributes(@PathVariable("typeid") Integer typeId,
                                                                      @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return mroObjectTypeService.getAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "/all/tree", method = RequestMethod.GET)
    public MESObjectTypesDto getAllObjectTypesTree() {
        return mroObjectTypeService.getAllMROObjectTypesTree();
    }

    @RequestMapping(value = "/attributes/{attributeId}", method = RequestMethod.GET)
    public List<MROObjectAttribute> getUsedMROObjectTypeAttributes(@PathVariable("attributeId") Integer attributeId) {
        return mroObjectTypeService.getUsedMROObjectAttributes(attributeId);
    }

    @RequestMapping(value = "/all/objects", method = RequestMethod.GET)
    public Page<MROObject> getMROObjects(PageRequest pageRequest, MROObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return mroObjectTypeService.getMROObjects(pageable, objectCriteria);
    }

    @RequestMapping(value = "/object/{id}", method = RequestMethod.GET)
    public Object getMROObjectById(@PathVariable("id") Integer id) {
        return mroObjectTypeService.getMROObjectById(id);
    }

    @RequestMapping(value = "/all/type/tree", method = RequestMethod.GET)
    public MROObjectTypesDto getMROObjectsTypeTree() {
        return mroObjectTypeService.getMROObjectsTypeTree();
    }

}
