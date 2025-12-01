package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.MESObjectCriteria;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.service.classification.MESObjectTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/mes/objecttypes")
@Api(tags = "PLM.MES", description = "MES Related")
public class MESObjectTypeController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private MESObjectTypeService mesObjectTypeService;

    @RequestMapping(method = RequestMethod.POST)
    public MESObjectType create(@RequestBody MESObjectType itemType) {
        return mesObjectTypeService.create(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESObjectType update(@PathVariable("id") Integer id,
                                @RequestBody MESObjectType itemType) {
        return mesObjectTypeService.update(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        mesObjectTypeService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESObjectType get(@PathVariable("id") Integer id) {
        return mesObjectTypeService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESObjectType> getAll() {
        return mesObjectTypeService.getRootTypes();
    }

    @RequestMapping(value = "/{id}/{type}", method = RequestMethod.GET)
    public Object getMESObjectTypeIdAndType(@PathVariable("id") Integer id, @PathVariable("type") PLMObjectType objectType) {
        return mesObjectTypeService.getMESObjectTypeIdAndType(id, objectType);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<MESObjectType> getMesObjectTypeTree() {
        return mesObjectTypeService.getMesObjectTypeTree();
    }

    /*-------------------------- plantType -------------------------------- */

    @RequestMapping(value = "/planttype", method = RequestMethod.POST)
    public MESPlantType createPlantType(@RequestBody MESPlantType plantType) {
        return mesObjectTypeService.createPlantType(plantType);
    }

    @RequestMapping(value = "/planttype/{id}", method = RequestMethod.PUT)
    public MESPlantType updatePlantType(@PathVariable("id") Integer id, @RequestBody MESPlantType plantType) {
        return mesObjectTypeService.updatePlantType(id, plantType);
    }

    @RequestMapping(value = "/planttype/{id}", method = RequestMethod.DELETE)
    public void deletePlantType(@PathVariable("id") Integer id) {
        mesObjectTypeService.deletePlantType(id);
    }

    @RequestMapping(value = "/planttype/{id}", method = RequestMethod.GET)
    public MESPlantType getPlantType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getPlantType(id);
    }

    @RequestMapping(value = "/planttype", method = RequestMethod.GET)
    public List<MESPlantType> getAllPlantTypes() {
        return mesObjectTypeService.getAllPlantType();
    }

    @RequestMapping(value = "/planttype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESPlantType> getMultiplePlantTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultiplePlantTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/planttype/tree", method = RequestMethod.GET)
    public List<MESPlantType> getPlantTypeTree() {
        return mesObjectTypeService.getPlantTypeTree();
    }

    /*--------------------- machine Type ------------------------ */

    @RequestMapping(value = "/machinetype", method = RequestMethod.POST)
    public MESMachineType createMachineType(@RequestBody MESMachineType machineType) {
        return mesObjectTypeService.createMachineType(machineType);
    }

    @RequestMapping(value = "/machinetype/{id}", method = RequestMethod.PUT)
    public MESMachineType updateMachineType(@PathVariable("id") Integer id, @RequestBody MESMachineType machineType) {
        return mesObjectTypeService.updateMachineType(id, machineType);
    }

    @RequestMapping(value = "/machinetype/{id}", method = RequestMethod.DELETE)
    public void deleteMachineType(@PathVariable("id") Integer id) {
        mesObjectTypeService.delete(id);
    }

    @RequestMapping(value = "/machinetype/{id}", method = RequestMethod.GET)
    public MESMachineType getMachineType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getMachineType(id);
    }

    @RequestMapping(value = "/machinetype", method = RequestMethod.GET)
    public List<MESMachineType> getAllMachineTypes() {
        return mesObjectTypeService.getAllMachineTypes();
    }

    @RequestMapping(value = "/machinetype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESMachineType> getMultipleMachineTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultipleMachineTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/machinetype/tree", method = RequestMethod.GET)
    public List<MESMachineType> getMachineTypeTree() {
        return mesObjectTypeService.getMachineTypeTree();
    }

    /*------------------------- workcenter ------------------- */

    @RequestMapping(value = "/workcentertype", method = RequestMethod.POST)
    public MESWorkCenterType createWorkCenter(@RequestBody MESWorkCenterType workCenterType) {
        return mesObjectTypeService.createWorkCenter(workCenterType);
    }

    @RequestMapping(value = "/workcentertype/{id}", method = RequestMethod.PUT)
    public MESWorkCenterType updateWorkCenter(@PathVariable("id") Integer id, @RequestBody MESWorkCenterType workCenterType) {
        return mesObjectTypeService.updateWorkCenterType(id, workCenterType);
    }

    @RequestMapping(value = "/workcentertype/{id}", method = RequestMethod.DELETE)
    public void deleteWorkCenter(@PathVariable("id") Integer id) {
        mesObjectTypeService.deleteWorkCenterType(id);
    }

    @RequestMapping(value = "/workcentertype/{id}", method = RequestMethod.GET)
    public MESWorkCenterType getWorkCenter(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getWorkCenterType(id);
    }

    @RequestMapping(value = "'/workcentertype", method = RequestMethod.GET)
    public List<MESWorkCenterType> getAllWorkCenter() {
        return mesObjectTypeService.getAllWorkCenterType();
    }

    @RequestMapping(value = "/workcentertype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESWorkCenterType> getMultipleWorkCenterTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultipleWorkCenterTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/workcentertype/tree", method = RequestMethod.GET)
    public List<MESWorkCenterType> getWorkCenterTypeTree() {
        return mesObjectTypeService.getWorkCenterTypeTree();
    }

      /*---------------------- Material Type --------------------------*/

    @RequestMapping(value = "/materialtype", method = RequestMethod.POST)
    public MESMaterialType createMaterialType(@RequestBody MESMaterialType materialType) {
        return mesObjectTypeService.createMaterialType(materialType);
    }

    @RequestMapping(value = "/materialtype/{id}", method = RequestMethod.PUT)
    public MESMaterialType updateMaterialType(@PathVariable("id") Integer id, @RequestBody MESMaterialType materialType) {
        return mesObjectTypeService.updateMaterialType(id, materialType);
    }

    @RequestMapping(value = "/materialtype/{id}", method = RequestMethod.DELETE)
    public void deleteMaterialType(@PathVariable("id") Integer id) {
        mesObjectTypeService.deleteMaterialType(id);
    }

    @RequestMapping(value = "/materialtype/{id}", method = RequestMethod.GET)
    public MESMaterialType getMaterialType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getMaterialType(id);
    }

    @RequestMapping(value = "/materialtype", method = RequestMethod.GET)
    public List<MESMaterialType> getAllMaterialTypes() {
        return mesObjectTypeService.getAllMaterialTypes();
    }

    @RequestMapping(value = "/materialtype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESMaterialType> getMultiplePrTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultipleMaterialTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/materialtype/tree", method = RequestMethod.GET)
    public List<MESMaterialType> getMaterialTypeTree() {
        return mesObjectTypeService.getMaterialTypeTree();
    }

     /*---------------------- Tool Type --------------------------*/

    @RequestMapping(value = "/tooltype", method = RequestMethod.POST)
    public MESToolType createToolType(@RequestBody MESToolType toolType) {
        return mesObjectTypeService.createToolType(toolType);
    }

    @RequestMapping(value = "/tooltype/{id}", method = RequestMethod.PUT)
    public MESToolType updateToolType(@PathVariable("id") Integer id, @RequestBody MESToolType toolType) {
        return mesObjectTypeService.updateToolType(id, toolType);
    }

    @RequestMapping(value = "/tooltype/{id}", method = RequestMethod.DELETE)
    public void deleteToolType(@PathVariable("id") Integer id) {
        mesObjectTypeService.deleteToolType(id);
    }

    @RequestMapping(value = "/tooltype/{id}", method = RequestMethod.GET)
    public MESToolType getToolType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getToolType(id);
    }

    @RequestMapping(value = "/tooltype", method = RequestMethod.GET)
    public List<MESToolType> getAllToolTypes() {
        return mesObjectTypeService.getAllToolTypes();
    }

    @RequestMapping(value = "/tooltype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESToolType> getMultipleToolTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultipleToolTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/tooltype/tree", method = RequestMethod.GET)
    public List<MESToolType> getToolTypeTree() {
        return mesObjectTypeService.getToolTypeTree();
    }

     /*---------------------- JigsFixtures Type --------------------------*/

    @RequestMapping(value = "/jigsfixtype", method = RequestMethod.POST)
    public MESJigsFixtureType createJigsFixType(@RequestBody MESJigsFixtureType jigsFixtureType) {
        return mesObjectTypeService.createJigsFixType(jigsFixtureType);
    }

    @RequestMapping(value = "/jigsfixtype/{id}", method = RequestMethod.PUT)
    public MESJigsFixtureType updateJigsFixType(@PathVariable("id") Integer id, @RequestBody MESJigsFixtureType jigsFixtureType) {
        return mesObjectTypeService.updateJigsFixType(id, jigsFixtureType);
    }

    @RequestMapping(value = "/jigsfixtype/{id}", method = RequestMethod.DELETE)
    public void deleteJigsFixType(@PathVariable("id") Integer id) {
        mesObjectTypeService.deleteJigsFixType(id);
    }

    @RequestMapping(value = "/jigsfixtype/{id}", method = RequestMethod.GET)
    public MESJigsFixtureType getJigsFixType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getJigsFixType(id);
    }

    @RequestMapping(value = "/jigsfixtype", method = RequestMethod.GET)
    public List<MESJigsFixtureType> getAllJigsFixTypes() {
        return mesObjectTypeService.getAllJigsFixTypes();
    }

    @RequestMapping(value = "/jigsfixtype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESJigsFixtureType> getMultipleJigsFixTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultipleJigsFixTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/jigsfixtype/tree", method = RequestMethod.GET)
    public List<MESJigsFixtureType> getJigsFixTypeTree() {
        return mesObjectTypeService.getJigsFixTypeTree();
    }

    /*----------------------- operation Type --------------------*/
    @RequestMapping(value = "/operationtype", method = RequestMethod.POST)
    public MESOperationType createOperationType(@RequestBody MESOperationType mesOperationType) {
        return mesObjectTypeService.createOperationType(mesOperationType);
    }

    @RequestMapping(value = "/operationtype/{id}", method = RequestMethod.PUT)
    public MESOperationType updateOperationType(@PathVariable("id") Integer id, @RequestBody MESOperationType mesOperationType) {
        return mesObjectTypeService.updateOperationType(id, mesOperationType);
    }

    @RequestMapping(value = "/operationtype/{id}", method = RequestMethod.DELETE)
    public void deleteOperationType(@PathVariable("id") Integer id) {
        mesObjectTypeService.deleteOperationType(id);
    }

    @RequestMapping(value = "/operationtype/{id}", method = RequestMethod.GET)
    public MESOperationType getOperationType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getOperationType(id);
    }

    @RequestMapping(value = "/operationtype", method = RequestMethod.GET)
    public List<MESOperationType> getAllOperationType() {
        return mesObjectTypeService.getAllOperationType();
    }

    @RequestMapping(value = "/operationtype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESOperationType> getMultipleOperationTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultipleOperationTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/operationtype/tree", method = RequestMethod.GET)
    public List<MESOperationType> getOperationTypeTree() {
        return mesObjectTypeService.getOperationTypeTree();
    }

    /*------------------------ serviceOrderType ---------------------------*/
    @RequestMapping(value = "/serviceordertype", method = RequestMethod.POST)
    public MESServiceOrderType createServiceOrder(@RequestBody MESServiceOrderType plantType) {
        return mesObjectTypeService.createServiceOrderType(plantType);
    }

    @RequestMapping(value = "/serviceordertype/{id}", method = RequestMethod.PUT)
    public MESServiceOrderType update(@PathVariable("id") Integer id, @RequestBody MESServiceOrderType plantType) {
        return mesObjectTypeService.updateServiceOrderType(id, plantType);
    }

    @RequestMapping(value = "/serviceordertype/{id}", method = RequestMethod.DELETE)
    public void deleteServiceOrderType(@PathVariable("id") Integer id) {
        mesObjectTypeService.deleteServiceOrderType(id);
    }

    @RequestMapping(value = "/serviceordertype/{id}", method = RequestMethod.GET)
    public MESServiceOrderType getServiceOrderType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getServiceOrderType(id);
    }

    @RequestMapping(value = "/serviceordertype", method = RequestMethod.GET)
    public List<MESServiceOrderType> getAllServiceOrderTypes() {
        return mesObjectTypeService.getAllServiceOrderTypes();
    }

    @RequestMapping(value = "/serviceordertype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESServiceOrderType> getMultipleServiceOrderTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.getMultipleServiceOrderTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/serviceordertype/tree", method = RequestMethod.GET)
    public List<MESServiceOrderType> getServiceOrderTypeTree() {
        return mesObjectTypeService.getServiceOrderTypeTree();

    } /*------------------------ productionOrderType ---------------------------*/

    @RequestMapping(value = "/productiontype", method = RequestMethod.POST)
    public MESProductionOrderType createProductionOrderType(@RequestBody MESProductionOrderType plantType) {
        return mesObjectTypeService.createProductionOrderType(plantType);
    }

    @RequestMapping(value = "/productiontype/{id}", method = RequestMethod.PUT)
    public MESProductionOrderType updateProductionOrderType(@PathVariable("id") Integer id, @RequestBody MESProductionOrderType plantType) {
        return mesObjectTypeService.updateProductionOrderType(id, plantType);
    }

    @RequestMapping(value = "/productiontype/{id}", method = RequestMethod.DELETE)
    public void deleteProductionOrderType(@PathVariable("id") Integer id) {
        mesObjectTypeService.deleteProductionOrderType(id);
    }

    @RequestMapping(value = "/productiontype/{id}", method = RequestMethod.GET)
    public MESProductionOrderType getProductionOrderType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getProductionOrderType(id);
    }

    @RequestMapping(value = "/productiontype", method = RequestMethod.GET)
    public List<MESProductionOrderType> getAllProductionOrderTypes() {
        return mesObjectTypeService.getAllProductionOrderTypes();
    }

    @RequestMapping(value = "/productiontype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESProductionOrderType> getMultipleProductionTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultipleProductionOrderTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/productiontype/tree", method = RequestMethod.GET)
    public List<MESProductionOrderType> getProductionTypeTree() {
        return mesObjectTypeService.getProductionOrderTypeTree();
    }

    /*------------------------ manpowerType ---------------------------*/
    @RequestMapping(value = "/manpowertype", method = RequestMethod.POST)
    public MESManpowerType createManpowerType(@RequestBody MESManpowerType manpowerType) {
        return mesObjectTypeService.createManpowerType(manpowerType);
    }

    @RequestMapping(value = "/manpowertype/{id}", method = RequestMethod.PUT)
    public MESManpowerType updateManpowerType(@PathVariable("id") Integer id, @RequestBody MESManpowerType manpowerType) {
        return mesObjectTypeService.updateManpowerType(id, manpowerType);
    }

    @RequestMapping(value = "/manpowertype/{id}", method = RequestMethod.DELETE)
    public void deleteManpowerType(@PathVariable("id") Integer id) {
        mesObjectTypeService.deleteManpowerType(id);
    }

    @RequestMapping(value = "/manpowertype/{id}", method = RequestMethod.GET)
    public MESManpowerType getManpowerType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getManpowerType(id);
    }

    @RequestMapping(value = "/manpowertype", method = RequestMethod.GET)
    public List<MESManpowerType> getAllManpowerTypes() {
        return mesObjectTypeService.getAllManpowerTypes();
    }

    @RequestMapping(value = "/manpowertype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESManpowerType> findMultipleManpowerTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultipleManpowerTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/manpowertype/tree", method = RequestMethod.GET)
    public List<MESManpowerType> getManpowerTypeTree() {
        return mesObjectTypeService.getManpowerTypeTree();
    }


    /*------------------------ equipmentType ---------------------------*/
    @RequestMapping(value = "/equipmenttype", method = RequestMethod.POST)
    public MESEquipmentType createEquipmentType(@RequestBody MESEquipmentType equipmentType) {
        return mesObjectTypeService.createEquipmentType(equipmentType);
    }

    @RequestMapping(value = "/equipmenttype/{id}", method = RequestMethod.PUT)
    public MESEquipmentType updateEquipmentTyp(@PathVariable("id") Integer id, @RequestBody MESEquipmentType equipmentType) {
        return mesObjectTypeService.updateEquipmentType(id, equipmentType);
    }

    @RequestMapping(value = "/equipmenttype/{id}", method = RequestMethod.DELETE)
    public void deleteEquipmentTyp(@PathVariable("id") Integer id) {
        mesObjectTypeService.deleteEquipmentType(id);
    }

    @RequestMapping(value = "/equipmenttype/{id}", method = RequestMethod.GET)
    public MESEquipmentType getEquipmentType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getEquipmentType(id);
    }

    @RequestMapping(value = "/equipmenttype", method = RequestMethod.GET)
    public List<MESEquipmentType> getAllEquipmentTypes() {
        return mesObjectTypeService.getAllEquipmentTypes();
    }

    @RequestMapping(value = "/equipmenttype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESEquipmentType> findMultipleEquipmentTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultipleEquipmentTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/equipmenttype/tree", method = RequestMethod.GET)
    public List<MESEquipmentType> getEquipmentTypeTree() {
        return mesObjectTypeService.getEquipmentTypeTree();
    }


    /*------------------------ instrumentType ---------------------------*/
    @RequestMapping(value = "/instrumenttype", method = RequestMethod.POST)
    public MESInstrumentType createInstrumentType(@RequestBody MESInstrumentType instrumentType) {
        return mesObjectTypeService.createInstrumentType(instrumentType);
    }

    @RequestMapping(value = "/instrumenttype/{id}", method = RequestMethod.PUT)
    public MESInstrumentType updateInstrumentType(@PathVariable("id") Integer id, @RequestBody MESInstrumentType instrumentType) {
        return mesObjectTypeService.updateInstrumentType(id, instrumentType);
    }

    @RequestMapping(value = "/instrumenttype/{id}", method = RequestMethod.DELETE)
    public void deleteInstrumentType(@PathVariable("id") Integer id) {
        mesObjectTypeService.deleteInstrumentType(id);
    }

    @RequestMapping(value = "/instrumenttype/{id}", method = RequestMethod.GET)
    public MESInstrumentType getInstrumentType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getInstrumentType(id);
    }

    @RequestMapping(value = "/instrumenttype", method = RequestMethod.GET)
    public List<MESInstrumentType> getAllInstrumentTypes() {
        return mesObjectTypeService.getAllInstrumentTypes();
    }

    @RequestMapping(value = "/instrumenttype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESInstrumentType> findMultipleInstrumentTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultipleInstrumentTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/instrumenttype/tree", method = RequestMethod.GET)
    public List<MESInstrumentType> getInstrumentTypeTree() {
        return mesObjectTypeService.getInstrumentTypeTree();
    }

    @RequestMapping(value = "/all/tree", method = RequestMethod.GET)
    public MESObjectTypesDto getAllObjectTypesTree() {
        return mesObjectTypeService.getAllObjectTypesTree();
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public MESObjectAttribute createMESObjectAttribute(@PathVariable("id") Integer id,
                                                       @RequestBody MESObjectAttribute attribute) {
        return mesObjectTypeService.createMESObjectAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public MESObjectAttribute updateMESObjectAttribute(@PathVariable("id") Integer id,
                                                       @RequestBody MESObjectAttribute attribute) {
        return mesObjectTypeService.updateMESObjectAttribute(attribute);
    }

    @RequestMapping(value = "/attributes/{attributeId}", method = RequestMethod.GET)
    public List<MESObjectAttribute> getUsedMESObjectTypeAttributes(@PathVariable("attributeId") Integer attributeId) {
        return mesObjectTypeService.getUsedMESObjectTypeAttributes(attributeId);
    }

    /*
    * Get All   Hierarchical Attributes By Type
    * */
    @RequestMapping(value = "/type/{typeid}/attributes", method = RequestMethod.GET)
    public List<MESObjectTypeAttribute> getHierarchicalTypeAttributes(@PathVariable("typeid") Integer typeId,
                                                                      @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return mesObjectTypeService.getAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "/{id}/{type}/count", method = RequestMethod.GET)
    public Integer getObjectsByType(@PathVariable("id") Integer id, @PathVariable("type") PLMObjectType objectType) {
        return mesObjectTypeService.getObjectsByType(id, objectType);
    }

      /*-------------------------- assemblyLine Type -------------------------------- */

    @RequestMapping(value = "/assemblylinetype", method = RequestMethod.POST)
    public MESAssemblyLineType createAssemblyLineType(@RequestBody MESAssemblyLineType assemblyLineType) {
        return mesObjectTypeService.createAssemblyLineType(assemblyLineType);
    }

    @RequestMapping(value = "/assemblylinetype/{id}", method = RequestMethod.PUT)
    public MESAssemblyLineType updateAssemblyLineType(@PathVariable("id") Integer id, @RequestBody MESAssemblyLineType assemblyLineType) {
        return mesObjectTypeService.updateAssemblyLineType(id, assemblyLineType);
    }

    @RequestMapping(value = "/assemblylinetype/{id}", method = RequestMethod.DELETE)
    public void deleteAssemblyLineType(@PathVariable("id") Integer id) {
        mesObjectTypeService.deleteAssemblyLineType(id);
    }

    @RequestMapping(value = "/assemblylinetype/{id}", method = RequestMethod.GET)
    public MESAssemblyLineType getAssemblyLineType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getAssemblyLineType(id);
    }

    @RequestMapping(value = "/assemblylinetype", method = RequestMethod.GET)
    public List<MESAssemblyLineType> getAllAssemblyLineTypes() {
        return mesObjectTypeService.getAllAssemblyLineTypes();
    }

    @RequestMapping(value = "/assemblylinetype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESAssemblyLineType> getMultipleAssemblyLineTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultipleAssemblyLineTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/assemblylinetype/tree", method = RequestMethod.GET)
    public List<MESAssemblyLineType> getAssemblyLineTypeTree() {
        return mesObjectTypeService.getAssemblyLineTypeTree();
    }

    @RequestMapping(value = "/all/objects", method = RequestMethod.GET)
    public Page<MESObject> getMESObjects(PageRequest pageRequest, MESObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return mesObjectTypeService.getMESObjects(pageable, objectCriteria);
    }

    @RequestMapping(value = "/object/{id}", method = RequestMethod.GET)
    public Object getMESObjectById(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getMESObjectById(id);
    }

/*-------------------------- MBOM Type -------------------------------- */

    @RequestMapping(value = "/mbomtype", method = RequestMethod.POST)
    public MESMBOMType createMBOMType(@RequestBody MESMBOMType mbomType) {
        return mesObjectTypeService.createMBOMType(mbomType);
    }

    @RequestMapping(value = "/mbomtype/{id}", method = RequestMethod.PUT)
    public MESMBOMType updateMBOMType(@PathVariable("id") Integer id, @RequestBody MESMBOMType mbomType) {
        return mesObjectTypeService.updateMBOMType(id, mbomType);
    }

    @RequestMapping(value = "/mbomtype/{id}", method = RequestMethod.DELETE)
    public void deleteMBOMType(@PathVariable("id") Integer id) {
        mesObjectTypeService.deleteMBOMType(id);
    }

    @RequestMapping(value = "/mbomtype/{id}", method = RequestMethod.GET)
    public MESMBOMType getMBOMType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getMBOMType(id);
    }

    @RequestMapping(value = "/mbomtype", method = RequestMethod.GET)
    public List<MESMBOMType> getAllMBOMTypes() {
        return mesObjectTypeService.getAllMBOMTypes();
    }

    @RequestMapping(value = "/mbomtype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESMBOMType> getMultipleMBOMTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultipleMBOMTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/mbomtype/tree", method = RequestMethod.GET)
    public List<MESMBOMType> getMBOMTypeTree() {
        return mesObjectTypeService.getMBOMTypeTree();
    }

    /*-------------------------- BOP Type -------------------------------- */

    @RequestMapping(value = "/boptype", method = RequestMethod.POST)
    public MESBOPType createBOPType(@RequestBody MESBOPType bopType) {
        return mesObjectTypeService.createBOPType(bopType);
    }

    @RequestMapping(value = "/boptype/{id}", method = RequestMethod.PUT)
    public MESBOPType updateBOPType(@PathVariable("id") Integer id, @RequestBody MESBOPType bopType) {
        return mesObjectTypeService.updateBOPType(id, bopType);
    }

    @RequestMapping(value = "/boptype/{id}", method = RequestMethod.DELETE)
    public void deleteBOPType(@PathVariable("id") Integer id) {
        mesObjectTypeService.deleteBOPType(id);
    }

    @RequestMapping(value = "/boptype/{id}", method = RequestMethod.GET)
    public MESBOPType getBOPType(@PathVariable("id") Integer id) {
        return mesObjectTypeService.getBOPType(id);
    }

    @RequestMapping(value = "/boptype", method = RequestMethod.GET)
    public List<MESBOPType> getAllBOPTypes() {
        return mesObjectTypeService.getAllBOPTypes();
    }

    @RequestMapping(value = "/boptype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESBOPType> getMultipleBOPTypes(@PathVariable Integer[] ids) {
        return mesObjectTypeService.findMultipleBOPTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/boptype/tree", method = RequestMethod.GET)
    public List<MESBOPType> getBOPTypeTree() {
        return mesObjectTypeService.getBOPTypeTree();
    }

    @RequestMapping(value = "/mbomtype/lifecycles", method = RequestMethod.GET)
    public List<PLMLifeCycle> getMBOMTypeLifecycles() {
        return mesObjectTypeService.getMBOMTypeLifecycles();
    }
}
