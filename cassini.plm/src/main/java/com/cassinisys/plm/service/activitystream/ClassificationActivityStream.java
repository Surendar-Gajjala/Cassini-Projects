package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.model.cm.PLMChangeType;
import com.cassinisys.plm.model.cm.PLMChangeTypeAttribute;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.pgc.*;
import com.cassinisys.plm.model.plm.PLMItemType;
import com.cassinisys.plm.model.plm.PLMItemTypeAttribute;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PMObjectType;
import com.cassinisys.plm.model.pm.PMObjectTypeAttribute;
import com.cassinisys.plm.model.pqm.PQMMaterialInspectionPlanType;
import com.cassinisys.plm.model.pqm.PQMProductInspectionPlanType;
import com.cassinisys.plm.model.pqm.PQMQualityType;
import com.cassinisys.plm.model.pqm.PQMQualityTypeAttribute;
import com.cassinisys.plm.model.req.PLMRequirementDocumentType;
import com.cassinisys.plm.model.req.PLMRequirementObjectTypeAttribute;
import com.cassinisys.plm.model.req.PLMRequirementType;
import com.cassinisys.plm.model.rm.RequirementType;
import com.cassinisys.plm.model.rm.RmObjectTypeAttribute;
import com.cassinisys.plm.model.rm.SpecificationType;
import com.cassinisys.plm.model.wf.PLMWorkflowType;
import com.cassinisys.plm.model.wf.PLMWorkflowTypeAttribute;
import com.cassinisys.plm.repo.cm.ChangeTypeRepository;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mfr.ManufacturerPartTypeRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerTypeRepository;
import com.cassinisys.plm.repo.mfr.SupplierTypeAttributeRepository;
import com.cassinisys.plm.repo.mfr.SupplierTypeRepository;
import com.cassinisys.plm.repo.mro.*;
import com.cassinisys.plm.repo.pgc.*;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.cassinisys.plm.repo.pm.PMObjectTypeRepository;
import com.cassinisys.plm.repo.pqm.QualityTypeRepository;
import com.cassinisys.plm.repo.req.PLMRequirementDocumentTypeRepository;
import com.cassinisys.plm.repo.req.PLMRequirementObjectTypeRepository;
import com.cassinisys.plm.repo.req.PLMRequirementTypeRepository;
import com.cassinisys.plm.repo.rm.RequirementTypeRepository;
import com.cassinisys.plm.repo.rm.RmObjectTypeRepository;
import com.cassinisys.plm.repo.rm.SpecificationTypeRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ClassificationActivityStream extends BaseActivityStream implements ActivityStreamConverter {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private ChangeTypeRepository changeTypeRepository;
    @Autowired
    private RequirementTypeRepository requirementTypeRepository;
    @Autowired
    private SpecificationTypeRepository specificationTypeRepository;
    @Autowired
    private ManufacturerTypeRepository manufacturerTypeRepository;
    @Autowired
    private ManufacturerPartTypeRepository manufacturerPartTypeRepository;
    @Autowired
    private QualityTypeRepository qualityTypeRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private RmObjectTypeRepository objectTypeRepository;
    @Autowired
    private PlantTypeRepository plantTypeRepository;
    @Autowired
    private MachineTypeRepository machineTypeRepository;
    @Autowired
    private WorkCenterTypeRepository workCenterTypeRepository;
    @Autowired
    private ToolTypeRepository toolTypeRepository;
    @Autowired
    private MaterialTypeRepository materialTypeRepository;
    @Autowired
    private JigsFixtureTypeRepository jigsFixtureTypeRepository;
    @Autowired
    private OperationTypeRepository operationTypeRepository;
    @Autowired
    private ProductionOrderTypeRepository productionOrderTypeRepository;
    @Autowired
    private ServiceOrderTypeRepository serviceOrderTypeRepository;
    @Autowired
    private ManpowerTypeRepository manpowerTypeRepository;
    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;
    @Autowired
    private InstrumentTypeRepository instrumentTypeRepository;
    @Autowired
    private PMObjectTypeRepository pmObjectTypeRepository;
    @Autowired
    private PLMRequirementDocumentTypeRepository requirementDocumentTypeRepository;
    @Autowired
    private PLMRequirementObjectTypeRepository requirementObjectTypeRepository;
    @Autowired
    private PLMRequirementTypeRepository plmRequirementTypeRepository;
    @Autowired
    private MROSparePartTypeRepository sparePartTypeRepository;
    @Autowired
    private MROWorkRequestTypeRepository workRequestTypeRepository;
    @Autowired
    private MROWorkOrderTypeRepository workOrderTypeRepository;
    @Autowired
    private MROAssetTypeRepository assetTypeRepository;
    @Autowired
    private MROMeterTypeRepository meterTypeRepository;

    @Autowired
    private PGCSubstanceTypeRepository substanceTypeRepository;

    @Autowired
    private PGCSubstanceGroupTypeRepository substanceGroupTypeRepository;

    @Autowired
    private PGCSpecificationTypeRepository pgcSpecificationTypeRepository;

    @Autowired
    private PGCDeclarationTypeRepository declarationTypeRepository;

    @Autowired
    private PGCObjectTypeAttributeRepository pgcObjectTypeAttributeRepository;

    @Autowired
    private SupplierTypeRepository supplierTypeRepository;
    @Autowired
    private AssemblyLineTypeRepository assemblyLineTypeRepository;

    @Autowired
    private SupplierTypeAttributeRepository supplierTypeAttributeRepository;

    @Autowired
    private BOPTypeRepository bopTypeRepository; 

    @Autowired
    private MBOMTypeRepository mbomTypeRepository;

    @Async
    @EventListener
    public void classificationTypeCreated(ClassificationEvents.ClassificationTypeCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        ActivityStream as = new ActivityStream();
        if (event.getType().equals(PLMObjectType.ITEMTYPE)) {
            PLMItemType itemType = (PLMItemType) event.getObject();
            object.setObject(itemType.getId());
            as.setActivity("plm.classification.item.create");
            object.setType("itemType");
        }
        if (event.getType().equals(PLMObjectType.CHANGETYPE)) {
            PLMChangeType changeType = (PLMChangeType) event.getObject();
            object.setObject(changeType.getId());
            as.setActivity("plm.classification.change.create");
            object.setType("changeType");
        }
        if (event.getType().equals(PLMObjectType.QUALITY_TYPE)) {
            PQMQualityType qualityType = (PQMQualityType) event.getObject();
            object.setObject(qualityType.getId());
            as.setActivity("plm.classification.quality.create");
            object.setType("qualityType");
        }
        if (event.getType().equals(PLMObjectType.SPECIFICATIONTYPE)) {
            SpecificationType specificationType = (SpecificationType) event.getObject();
            object.setObject(specificationType.getId());
            as.setActivity("plm.classification.requirements.specification.create");
            object.setType("specType");
        }
        if (event.getType().equals(PLMObjectType.MANUFACTURERTYPE)) {
            PLMManufacturerType manufacturerType = (PLMManufacturerType) event.getObject();
            object.setObject(manufacturerType.getId());
            as.setActivity("plm.classification.oem.manufacturer.create");
            object.setType("manufacturerType");
        }
        if (event.getType().equals(PLMObjectType.MANUFACTURERPARTTYPE)) {
            PLMManufacturerPartType manufacturerPartType = (PLMManufacturerPartType) event.getObject();
            object.setObject(manufacturerPartType.getId());
            as.setActivity("plm.classification.oem.manufacturerpart.create");
            object.setType("manufacturerPartType");
        }
        if (event.getType().equals(PLMObjectType.SUPPLIERTYPE)) {
            PLMSupplierType supplierType = (PLMSupplierType) event.getObject();
            object.setObject(supplierType.getId());
            as.setActivity("plm.classification.oem.supplier.create");
            object.setType("supplierType");
        }
        if (event.getType().equals(PLMObjectType.PGCSUBSTANCETYPE)) {
            PGCSubstanceType substanceType = (PGCSubstanceType) event.getObject();
            object.setObject(substanceType.getId());
            as.setActivity("plm.classification.compliance.substance.create");
            object.setType("substanceType");
        }
        if (event.getType().equals(PLMObjectType.PGCSUBSTANCEGROUPTYPE)) {
            PGCSubstanceGroupType substanceGroupType = (PGCSubstanceGroupType) event.getObject();
            object.setObject(substanceGroupType.getId());
            as.setActivity("plm.classification.compliance.substanceGroup.create");
            object.setType("substanceGroupType");
        }
        if (event.getType().equals(PLMObjectType.PGCSPECIFICATIONTYPE)) {
            PGCSpecificationType specificationType = (PGCSpecificationType) event.getObject();
            object.setObject(specificationType.getId());
            as.setActivity("plm.classification.compliance.specification.create");
            object.setType("specificationType");
        }
        if (event.getType().equals(PLMObjectType.PGCDECLARATIONTYPE)) {
            PGCDeclarationType declarationType = (PGCDeclarationType) event.getObject();
            object.setObject(declarationType.getId());
            as.setActivity("plm.classification.compliance.declaration.create");
            object.setType("declarationType");
        }
        if (event.getType().equals(PLMObjectType.PLANTTYPE)) {
            MESPlantType plantType = (MESPlantType) event.getObject();
            object.setObject(plantType.getId());
            as.setActivity("plm.classification.manufacturing.plant.create");
            object.setType("plantType");
        }
        if (event.getType().equals(PLMObjectType.ASSEMBLYLINETYPE)) {
            MESAssemblyLineType assemblyLineType = (MESAssemblyLineType) event.getObject();
            object.setObject(assemblyLineType.getId());
            as.setActivity("plm.classification.manufacturing.assemblyline.create");
            object.setType("assemblyLineType");
        }
        if (event.getType().equals(PLMObjectType.MANPOWERTYPE)) {
            MESManpowerType manpowerType = (MESManpowerType) event.getObject();
            object.setObject(manpowerType.getId());
            as.setActivity("plm.classification.manufacturing.manpower.create");
            object.setType("manpowerType");
        }
        if (event.getType().equals(PLMObjectType.MACHINETYPE)) {
            MESMachineType machineType = (MESMachineType) event.getObject();
            object.setObject(machineType.getId());
            as.setActivity("plm.classification.manufacturing.machine.create");
            object.setType("machineType");
        }
        if (event.getType().equals(PLMObjectType.EQUIPMENTTYPE)) {
            MESEquipmentType equipmentType = (MESEquipmentType) event.getObject();
            object.setObject(equipmentType.getId());
            as.setActivity("plm.classification.manufacturing.equipment.create");
            object.setType("equipmentType");
        }
        if (event.getType().equals(PLMObjectType.INSTRUMENTTYPE)) {
            MESInstrumentType instrumentType = (MESInstrumentType) event.getObject();
            object.setObject(instrumentType.getId());
            as.setActivity("plm.classification.manufacturing.instrument.create");
            object.setType("instrumentType");
        }
        if (event.getType().equals(PLMObjectType.WORKCENTERTYPE)) {
            MESWorkCenterType workCenterType = (MESWorkCenterType) event.getObject();
            object.setObject(workCenterType.getId());
            as.setActivity("plm.classification.manufacturing.workCenter.create");
            object.setType("workCenterType");
        }
        if (event.getType().equals(PLMObjectType.MATERIALTYPE)) {
            MESMaterialType materialType = (MESMaterialType) event.getObject();
            object.setObject(materialType.getId());
            as.setActivity("plm.classification.manufacturing.material.create");
            object.setType("materialType");
        }
        if (event.getType().equals(PLMObjectType.ASSETTYPE)) {
            MROAssetType assetType = (MROAssetType) event.getObject();
            object.setObject(assetType.getId());
            as.setActivity("plm.classification.manufacturing.mro.asset.create");
            object.setType("assetType");
        }
        if (event.getType().equals(PLMObjectType.METERTYPE)) {
            MROMeterType meterType = (MROMeterType) event.getObject();
            object.setObject(meterType.getId());
            as.setActivity("plm.classification.manufacturing.mro.meter.create");
            object.setType("meterType");
        }
        if (event.getType().equals(PLMObjectType.SPAREPARTTYPE)) {
            MROSparePartType sparePartType = (MROSparePartType) event.getObject();
            object.setObject(sparePartType.getId());
            as.setActivity("plm.classification.manufacturing.mro.sparePart.create");
            object.setType("sparePartType");
        }
        if (event.getType().equals(PLMObjectType.WORKREQUESTTYPE)) {
            MROWorkRequestType workRequestType = (MROWorkRequestType) event.getObject();
            object.setObject(workRequestType.getId());
            as.setActivity("plm.classification.manufacturing.mro.workRequest.create");
            object.setType("workRequestType");
        }
        if (event.getType().equals(PLMObjectType.WORKORDERTYPE)) {
            MROWorkOrderType workOrderType = (MROWorkOrderType) event.getObject();
            object.setObject(workOrderType.getId());
            as.setActivity("plm.classification.manufacturing.mro.workOrder.create");
            object.setType("workOrderType");
        }
        if (event.getType().equals(PLMObjectType.PMOBJECTTYPE)) {
            PMObjectType objectType = (PMObjectType) event.getObject();
            object.setObject(objectType.getId());
            as.setActivity("plm.classification.projectManagement.create");
            object.setType("pmType");
        }
        if (event.getType().equals(PLMObjectType.REQUIREMENTDOCUMENTTYPE)) {
            PLMRequirementDocumentType materialType = (PLMRequirementDocumentType) event.getObject();
            object.setObject(materialType.getId());
            as.setActivity("plm.classification.projectManagement.requirementDoc.create");
            object.setType("reqDocType");
        }
        if (event.getType().equals(PLMObjectType.REQUIREMENTTYPE)) {
            PLMRequirementType materialType = (PLMRequirementType) event.getObject();
            object.setObject(materialType.getId());
            as.setActivity("plm.classification.projectManagement.requirement.create");
            object.setType("reqType");
        }
        if (event.getType().equals(PLMObjectType.JIGFIXTURETYPE)) {
            MESJigsFixtureType jigsFixtureType = (MESJigsFixtureType) event.getObject();
            object.setObject(jigsFixtureType.getId());
            as.setActivity("plm.classification.manufacturing.jigsFix.create");
            object.setType("jigsFixType");
        }
        if (event.getType().equals(PLMObjectType.TOOLTYPE)) {
            MESToolType mesToolType = (MESToolType) event.getObject();
            object.setObject(mesToolType.getId());
            as.setActivity("plm.classification.manufacturing.tool.create");
            object.setType("toolType");
        }
        if (event.getType().equals(PLMObjectType.OPERATIONTYPE)) {
            MESOperationType mesOperationType = (MESOperationType) event.getObject();
            object.setObject(mesOperationType.getId());
            as.setActivity("plm.classification.manufacturing.operation.create");
            object.setType("operationType");
        }
        if (event.getType().equals(PLMObjectType.WORKFLOWTYPE)) {
            PLMWorkflowType workflowType = (PLMWorkflowType) event.getObject();
            object.setObject(workflowType.getId());
            as.setActivity("plm.classification.workflow.create");
            object.setType("workflowType");
        }
        if (event.getType().equals(PLMObjectType.PRODUCTIONORDERTYPE)) {
            MESProductionOrderType mesProductionOrderType = (MESProductionOrderType) event.getObject();
            object.setObject(mesProductionOrderType.getId());
            as.setActivity("plm.classification.manufacturing.productionOrder.create");
            object.setType("productionOrderType");
        }
        if (event.getType().equals(PLMObjectType.SERVICEORDERTYPE)) {
            MESServiceOrderType mesServiceOrderType = (MESServiceOrderType) event.getObject();
            object.setObject(mesServiceOrderType.getId());
            as.setActivity("plm.classification.manufacturing.serviceOrder.create");
            object.setType("serviceOrderType");
        }
        if (event.getType().equals(PLMObjectType.MBOMTYPE)) {
            MESMBOMType mesMBOMType = (MESMBOMType) event.getObject();
            object.setObject(mesMBOMType.getId());
            as.setActivity("plm.classification.manufacturing.mbom.create");
            object.setType("mbomType");
        }
        if (event.getType().equals(PLMObjectType.BOPTYPE)) {
            MESBOPType mesBOPType = (MESBOPType) event.getObject();
            object.setObject(mesBOPType.getId());
            as.setActivity("plm.classification.manufacturing.bop.create");
            object.setType("bopType");
        }

        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void classificationTypeDeleted(ClassificationEvents.ClassificationTypeDeletedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        ActivityStream as = new ActivityStream();
        if (event.getType().equals(PLMObjectType.ITEMTYPE)) {
            PLMItemType itemType = (PLMItemType) event.getObject();
            object.setObject(itemType.getParentType());
            as.setData(itemType.getName());
            as.setActivity("plm.classification.item.delete");
            object.setType("itemType");
        }
        if (event.getType().equals(PLMObjectType.CHANGETYPE)) {
            PLMChangeType changeType = (PLMChangeType) event.getObject();
            object.setObject(changeType.getParentType());
            as.setData(changeType.getName());
            as.setActivity("plm.classification.change.delete");
            object.setType("changeType");
        }
        if (event.getType().equals(PLMObjectType.QUALITY_TYPE)) {
            PQMQualityType qualityType = (PQMQualityType) event.getObject();
            object.setObject(qualityType.getParentType());
            as.setData(qualityType.getName());
            as.setActivity("plm.classification.quality.delete");
            object.setType("qualityType");
        }
        if (event.getType().equals(PLMObjectType.MATERIALTYPE)) {
            MESMaterialType materialType = (MESMaterialType) event.getObject();
            object.setObject(materialType.getParentType());
            as.setData(materialType.getName());
            as.setActivity("plm.classification.manufacturing.material.delete");
            object.setType("materialType");
        }
        if (event.getType().equals(PLMObjectType.ASSETTYPE)) {
            MROAssetType assetType = (MROAssetType) event.getObject();
            object.setObject(assetType.getParentType());
            as.setData(assetType.getName());
            as.setActivity("plm.classification.manufacturing.mro.asset.delete");
            object.setType("assetType");
        }
        if (event.getType().equals(PLMObjectType.METERTYPE)) {
            MROMeterType meterType = (MROMeterType) event.getObject();
            object.setObject(meterType.getParentType());
            as.setData(meterType.getName());
            as.setActivity("plm.classification.manufacturing.mro.meter.delete");
            object.setType("meterType");
        }
        if (event.getType().equals(PLMObjectType.SPAREPARTTYPE)) {
            MROSparePartType sparePartType = (MROSparePartType) event.getObject();
            object.setObject(sparePartType.getParentType());
            as.setData(sparePartType.getName());
            as.setActivity("plm.classification.manufacturing.mro.sparePart.delete");
            object.setType("sparePartType");
        }
        if (event.getType().equals(PLMObjectType.WORKREQUESTTYPE)) {
            MROWorkRequestType workRequestType = (MROWorkRequestType) event.getObject();
            object.setObject(workRequestType.getParentType());
            as.setData(workRequestType.getName());
            as.setActivity("plm.classification.manufacturing.mro.workRequest.delete");
            object.setType("workRequestType");
        }
        if (event.getType().equals(PLMObjectType.WORKORDERTYPE)) {
            MROWorkOrderType workOrderType = (MROWorkOrderType) event.getObject();
            object.setObject(workOrderType.getParentType());
            as.setData(workOrderType.getName());
            as.setActivity("plm.classification.manufacturing.mro.workOrder.delete");
            object.setType("workOrderType");
        }
        if (event.getType().equals(PLMObjectType.JIGFIXTURETYPE)) {
            MESJigsFixtureType jigsFixtureType = (MESJigsFixtureType) event.getObject();
            object.setObject(jigsFixtureType.getParentType());
            as.setData(jigsFixtureType.getName());
            as.setActivity("plm.classification.manufacturing.jigsFix.delete");
            object.setType("jigsFixType");
        }
        if (event.getType().equals(PLMObjectType.SPECIFICATIONTYPE)) {
            SpecificationType specificationType = (SpecificationType) event.getObject();
            object.setObject(specificationType.getParentType());
            as.setData(specificationType.getName());
            as.setActivity("plm.classification.requirements.specification.delete");
            object.setType("specType");
        }
        if (event.getType().equals(PLMObjectType.MANUFACTURERTYPE)) {
            PLMManufacturerType manufacturerType = (PLMManufacturerType) event.getObject();
            object.setObject(manufacturerType.getParentType());
            as.setData(manufacturerType.getName());
            as.setActivity("plm.classification.oem.manufacturer.delete");
            object.setType("manufacturerType");
        }
        if (event.getType().equals(PLMObjectType.PLANTTYPE)) {
            MESPlantType plantType = (MESPlantType) event.getObject();
            object.setObject(plantType.getParentType());
            as.setData(plantType.getName());
            as.setActivity("plm.classification.manufacturing.plant.delete");
            object.setType("platType");
        }
        if (event.getType().equals(PLMObjectType.ASSEMBLYLINETYPE)) {
            MESAssemblyLineType assemblyLineType = (MESAssemblyLineType) event.getObject();
            object.setObject(assemblyLineType.getParentType());
            as.setData(assemblyLineType.getName());
            as.setActivity("plm.classification.manufacturing.assemblyline.delete");
            object.setType("assemblyLineType");
        }
        if (event.getType().equals(PLMObjectType.PMOBJECTTYPE)) {
            PMObjectType plantType = (PMObjectType) event.getObject();
            object.setObject(plantType.getParent());
            as.setData(plantType.getName());
            as.setActivity("plm.classification.projectManagement.delete");
            object.setType("pmType");
        }
        if (event.getType().equals(PLMObjectType.REQUIREMENTDOCUMENTTYPE)) {
            PLMRequirementDocumentType plantType = (PLMRequirementDocumentType) event.getObject();
            object.setObject(plantType.getParent());
            as.setData(plantType.getName());
            as.setActivity("plm.classification.projectManagement.requirementDocument.delete");
            object.setType("reqDocType");
        }
        if (event.getType().equals(PLMObjectType.REQUIREMENTTYPE)) {
            PLMRequirementType plantType = (PLMRequirementType) event.getObject();
            object.setObject(plantType.getParent());
            as.setData(plantType.getName());
            as.setActivity("plm.classification.projectManagement.requirement.delete");
            object.setType("reqType");
        }
        if (event.getType().equals(PLMObjectType.MANPOWERTYPE)) {
            MESManpowerType manpowerType = (MESManpowerType) event.getObject();
            object.setObject(manpowerType.getParentType());
            as.setData(manpowerType.getName());
            as.setActivity("plm.classification.manufacturing.manpower.delete");
            object.setType("manpowerType");
        }
        if (event.getType().equals(PLMObjectType.MACHINETYPE)) {
            MESMachineType machineType = (MESMachineType) event.getObject();
            object.setObject(machineType.getParentType());
            as.setData(machineType.getName());
            as.setActivity("plm.classification.manufacturing.machine.delete");
            object.setType("machineType");
        }
        if (event.getType().equals(PLMObjectType.EQUIPMENTTYPE)) {
            MESEquipmentType equipmentType = (MESEquipmentType) event.getObject();
            object.setObject(equipmentType.getParentType());
            as.setData(equipmentType.getName());
            as.setActivity("plm.classification.manufacturing.equipment.delete");
            object.setType("equipmentType");
        }
        if (event.getType().equals(PLMObjectType.INSTRUMENTTYPE)) {
            MESInstrumentType instrumentType = (MESInstrumentType) event.getObject();
            object.setObject(instrumentType.getParentType());
            as.setData(instrumentType.getName());
            as.setActivity("plm.classification.manufacturing.instrument.delete");
            object.setType("instrumentType");
        }
        if (event.getType().equals(PLMObjectType.WORKCENTERTYPE)) {
            MESWorkCenterType workCenterType = (MESWorkCenterType) event.getObject();
            object.setObject(workCenterType.getParentType());
            as.setData(workCenterType.getName());
            as.setActivity("plm.classification.manufacturing.workCenter.delete");
            object.setType("workCenterType");
        }
        if (event.getType().equals(PLMObjectType.TOOLTYPE)) {
            MESToolType toolType = (MESToolType) event.getObject();
            object.setObject(toolType.getParentType());
            as.setData(toolType.getName());
            as.setActivity("plm.classification.manufacturing.tool.delete");
            object.setType("toolType");
        }
        if (event.getType().equals(PLMObjectType.OPERATIONTYPE)) {
            MESOperationType mesOperationType = (MESOperationType) event.getObject();
            object.setObject(mesOperationType.getParentType());
            as.setData(mesOperationType.getName());
            as.setActivity("plm.classification.manufacturing.operation.delete");
            object.setType("operationType");
        }
        if (event.getType().equals(PLMObjectType.MANUFACTURERPARTTYPE)) {
            PLMManufacturerPartType manufacturerPartType = (PLMManufacturerPartType) event.getObject();
            object.setObject(manufacturerPartType.getParentType());
            as.setData(manufacturerPartType.getName());
            as.setActivity("plm.classification.oem.manufacturerpart.delete");
            object.setType("manufacturerPartType");
        }


        if (event.getType().equals(PLMObjectType.SUPPLIERTYPE)) {
            PLMSupplierType supplierType = (PLMSupplierType) event.getObject();
            object.setObject(supplierType.getParentType());
            as.setData(supplierType.getName());
            as.setActivity("plm.classification.oem.supplier.delete");
            object.setType("supplierType");
        }
        if (event.getType().equals(PLMObjectType.PGCSUBSTANCETYPE)) {
            PGCSubstanceType substanceType = (PGCSubstanceType) event.getObject();
            object.setObject(substanceType.getParentType());
            as.setData(substanceType.getName());
            as.setActivity("plm.classification.compliance.substance.delete");
            object.setType("substanceType");
        }
        if (event.getType().equals(PLMObjectType.PGCSUBSTANCEGROUPTYPE)) {
            PGCSubstanceGroupType substanceGroupType = (PGCSubstanceGroupType) event.getObject();
            object.setObject(substanceGroupType.getParentType());
            as.setData(substanceGroupType.getName());
            as.setActivity("plm.classification.compliance.substanceGroup.delete");
            object.setType("substanceGroupType");
        }
        if (event.getType().equals(PLMObjectType.PGCSPECIFICATIONTYPE)) {
            PGCSpecificationType specificationType = (PGCSpecificationType) event.getObject();
            object.setObject(specificationType.getParentType());
            as.setData(specificationType.getName());
            as.setActivity("plm.classification.compliance.specification.delete");
            object.setType("specificationType");
        }
        if (event.getType().equals(PLMObjectType.PGCDECLARATIONTYPE)) {
            PGCDeclarationType declarationType = (PGCDeclarationType) event.getObject();
            object.setObject(declarationType.getParentType());
            as.setData(declarationType.getName());
            as.setActivity("plm.classification.compliance.declaration.delete");
            object.setType("declarationType");
        }
        if (event.getType().equals(PLMObjectType.WORKFLOWTYPE)) {
            PLMWorkflowType workflowType = (PLMWorkflowType) event.getObject();
            object.setObject(workflowType.getParentType());
            as.setData(workflowType.getName());
            as.setActivity("plm.classification.workflow.delete");
            object.setType("workflowType");
        }
        if (event.getType().equals(PLMObjectType.PRODUCTIONORDERTYPE)) {
            MESProductionOrderType mesProductionOrderType = (MESProductionOrderType) event.getObject();
            object.setObject(mesProductionOrderType.getId());
            as.setActivity("plm.classification.manufacturing.productionOrder.delete");
            object.setType("productionOrderType");
        }
        if (event.getType().equals(PLMObjectType.SERVICEORDERTYPE)) {
            MESServiceOrderType mesServiceOrderType = (MESServiceOrderType) event.getObject();
            object.setObject(mesServiceOrderType.getId());
            as.setActivity("plm.classification.manufacturing.serviceOrder.delete");
            object.setType("serviceOrderType");
        }
        if (event.getType().equals(PLMObjectType.MBOMTYPE)) {
            MESMBOMType mesMBOMType = (MESMBOMType) event.getObject();
            object.setObject(mesMBOMType.getId());
            as.setActivity("plm.classification.manufacturing.mbom.delete");
            object.setType("mbomType");
        }

        if (event.getType().equals(PLMObjectType.BOPTYPE)) {
            MESBOPType mesBOPType = (MESBOPType) event.getObject();
            object.setObject(mesBOPType.getId());
            as.setActivity("plm.classification.manufacturing.bop.delete");
            object.setType("bopType");
        }

        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void classificationTypeUpdated(ClassificationEvents.ClassificationTypeUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals(PLMObjectType.ITEMTYPE)) {
            PLMItemType oldType = (PLMItemType) event.getOldType();
            PLMItemType newType = (PLMItemType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("itemType");
            as.setActivity("plm.classification.item.update");
            as.setData(getItemTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.CHANGETYPE)) {
            PLMChangeType oldType = (PLMChangeType) event.getOldType();
            PLMChangeType newType = (PLMChangeType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("changeType");
            as.setActivity("plm.classification.change.update");
            as.setData(getChangeTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.QUALITY_TYPE)) {
            PQMQualityType oldType = (PQMQualityType) event.getOldType();
            PQMQualityType newType = (PQMQualityType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("qualityType");
            as.setActivity("plm.classification.quality.update");
            as.setData(getQualityTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.MATERIALTYPE)) {
            MESMaterialType oldType = (MESMaterialType) event.getOldType();
            MESMaterialType newType = (MESMaterialType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("materialType");
            as.setActivity("plm.classification.manufacturing.material.update");
            as.setData(getMaterialTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.ASSETTYPE)) {
            MROAssetType oldType = (MROAssetType) event.getOldType();
            MROAssetType newType = (MROAssetType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("assetType");
            as.setActivity("plm.classification.manufacturing.mro.asset.update");
            as.setData(getMROAssetTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.METERTYPE)) {
            MROMeterType oldType = (MROMeterType) event.getOldType();
            MROMeterType newType = (MROMeterType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("meterType");
            as.setActivity("plm.classification.manufacturing.mro.meter.update");
            as.setData(getMROMeterTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.SPAREPARTTYPE)) {
            MROSparePartType oldType = (MROSparePartType) event.getOldType();
            MROSparePartType newType = (MROSparePartType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("sparePartType");
            as.setActivity("plm.classification.manufacturing.mro.sparePart.update");
            as.setData(getMROSparePartTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.WORKREQUESTTYPE)) {
            MROWorkRequestType oldType = (MROWorkRequestType) event.getOldType();
            MROWorkRequestType newType = (MROWorkRequestType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("workRequestType");
            as.setActivity("plm.classification.manufacturing.mro.workRequest.update");
            as.setData(getMROWorkRequestTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.WORKORDERTYPE)) {
            MROWorkOrderType oldType = (MROWorkOrderType) event.getOldType();
            MROWorkOrderType newType = (MROWorkOrderType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("workOrderType");
            as.setActivity("plm.classification.manufacturing.mro.workOrder.update");
            as.setData(getMROWorkOrderTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.JIGFIXTURETYPE)) {
            MESJigsFixtureType oldType = (MESJigsFixtureType) event.getOldType();
            MESJigsFixtureType newType = (MESJigsFixtureType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("jigsFixType");
            as.setActivity("plm.classification.manufacturing.jigsFix.update");
            as.setData(getJigsFixTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.SPECIFICATIONTYPE)) {
            SpecificationType oldType = (SpecificationType) event.getOldType();
            SpecificationType newType = (SpecificationType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("specType");
            as.setActivity("plm.classification.requirements.specification.update");
            as.setData(getSpecTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.MANUFACTURERTYPE)) {
            PLMManufacturerType oldType = (PLMManufacturerType) event.getOldType();
            PLMManufacturerType newType = (PLMManufacturerType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("manufacturerType");
            as.setActivity("plm.classification.oem.manufacturer.update");
            as.setData(getMfrTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.MANUFACTURERPARTTYPE)) {
            PLMManufacturerPartType oldType = (PLMManufacturerPartType) event.getOldType();
            PLMManufacturerPartType newType = (PLMManufacturerPartType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("manufacturerPartType");
            as.setActivity("plm.classification.oem.manufacturerpart.update");
            as.setData(getMfrPartTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.SUPPLIERTYPE)) {
            PLMSupplierType oldType = (PLMSupplierType) event.getOldType();
            PLMSupplierType newType = (PLMSupplierType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("supplierType");
            as.setActivity("plm.classification.oem.supplier.update");
            as.setData(getSupplierTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.PGCSUBSTANCETYPE)) {
            PGCSubstanceType oldType = (PGCSubstanceType) event.getOldType();
            PGCSubstanceType newType = (PGCSubstanceType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("substanceType");
            as.setActivity("plm.classification.compliance.substance.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.PGCSUBSTANCEGROUPTYPE)) {
            PGCSubstanceGroupType oldType = (PGCSubstanceGroupType) event.getOldType();
            PGCSubstanceGroupType newType = (PGCSubstanceGroupType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("substanceGroupType");
            as.setActivity("plm.classification.compliance.substanceGroup.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.PGCSPECIFICATIONTYPE)) {
            PGCSpecificationType oldType = (PGCSpecificationType) event.getOldType();
            PGCSpecificationType newType = (PGCSpecificationType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("specificationType");
            as.setActivity("plm.classification.compliance.specification.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.PGCDECLARATIONTYPE)) {
            PGCDeclarationType oldType = (PGCDeclarationType) event.getOldType();
            PGCDeclarationType newType = (PGCDeclarationType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("declarationType");
            as.setActivity("plm.classification.compliance.declaration.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.PLANTTYPE)) {
            MESPlantType oldType = (MESPlantType) event.getOldType();
            MESPlantType newType = (MESPlantType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("plantType");
            as.setActivity("plm.classification.manufacturing.plant.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.ASSEMBLYLINETYPE)) {
            MESAssemblyLineType oldType = (MESAssemblyLineType) event.getOldType();
            MESAssemblyLineType newType = (MESAssemblyLineType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("assemblyLineType");
            as.setActivity("plm.classification.manufacturing.assemblyline.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.PMOBJECTTYPE)) {
            PMObjectType oldType = (PMObjectType) event.getOldType();
            PMObjectType newType = (PMObjectType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("pmType");
            as.setActivity("plm.classification.projectManagement.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.REQUIREMENTDOCUMENTTYPE)) {
            PLMRequirementDocumentType oldType = (PLMRequirementDocumentType) event.getOldType();
            PLMRequirementDocumentType newType = (PLMRequirementDocumentType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("reqDocType");
            as.setActivity("plm.classification.projectManagement.requirementDocument.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.REQUIREMENTTYPE)) {
            PLMRequirementType oldType = (PLMRequirementType) event.getOldType();
            PLMRequirementType newType = (PLMRequirementType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("reqType");
            as.setActivity("plm.classification.projectManagement.requirement.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.MANPOWERTYPE)) {
            MESManpowerType oldType = (MESManpowerType) event.getOldType();
            MESManpowerType newType = (MESManpowerType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("manpowerType");
            as.setActivity("plm.classification.manufacturing.manpower.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.MACHINETYPE)) {
            MESMachineType oldType = (MESMachineType) event.getOldType();
            MESMachineType newType = (MESMachineType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("machineType");
            as.setActivity("plm.classification.manufacturing.machine.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.EQUIPMENTTYPE)) {
            MESEquipmentType oldType = (MESEquipmentType) event.getOldType();
            MESEquipmentType newType = (MESEquipmentType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("equipmentType");
            as.setActivity("plm.classification.manufacturing.equipment.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.INSTRUMENTTYPE)) {
            MESInstrumentType oldType = (MESInstrumentType) event.getOldType();
            MESInstrumentType newType = (MESInstrumentType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("instrumentType");
            as.setActivity("plm.classification.manufacturing.instrument.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.WORKCENTERTYPE)) {
            MESWorkCenterType oldType = (MESWorkCenterType) event.getOldType();
            MESWorkCenterType newType = (MESWorkCenterType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("workCenterType");
            as.setActivity("plm.classification.manufacturing.workCenter.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.TOOLTYPE)) {
            MESToolType oldType = (MESToolType) event.getOldType();
            MESToolType newType = (MESToolType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("toolType");
            as.setActivity("plm.classification.manufacturing.tool.update");
            as.setData(getToolTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.OPERATIONTYPE)) {
            MESOperationType oldType = (MESOperationType) event.getOldType();
            MESOperationType newType = (MESOperationType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("operationType");
            as.setActivity("plm.classification.manufacturing.operation.update");
            as.setData(getTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.WORKFLOWTYPE)) {
            PLMWorkflowType oldType = (PLMWorkflowType) event.getOldType();
            PLMWorkflowType newType = (PLMWorkflowType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("workflowType");
            as.setActivity("plm.classification.workflow.update");
            as.setData(getWorkflowTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.PRODUCTIONORDERTYPE)) {
            MESProductionOrderType oldType = (MESProductionOrderType) event.getOldType();
            MESProductionOrderType newType = (MESProductionOrderType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("productionOrderType");
            as.setActivity("plm.classification.manufacturing.productionOrder.update");
            as.setData(getProductionOrderTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.SERVICEORDERTYPE)) {
            MESServiceOrderType oldType = (MESServiceOrderType) event.getOldType();
            MESServiceOrderType newType = (MESServiceOrderType) event.getNewType();
            object.setType("serviceOrderType");
            object.setObject(newType.getId());
            as.setActivity("plm.classification.manufacturing.serviceOrder.update");
            as.setData(getServiceOrderTypeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.MBOMTYPE)) {
            MESMBOMType oldType = (MESMBOMType) event.getOldType();
            MESMBOMType newType = (MESMBOMType) event.getNewType();
            object.setType("mbomType");
            object.setObject(newType.getId());
            as.setActivity("plm.classification.manufacturing.mbom.update");
            as.setData(getMBOMTypeUpdatedJson(oldType, newType));
        }

        if (event.getType().equals(PLMObjectType.BOPTYPE)) {
            MESBOPType oldType = (MESBOPType) event.getOldType();
            MESBOPType newType = (MESBOPType) event.getNewType();
            object.setType("bopType");
            object.setObject(newType.getId());
            as.setActivity("plm.classification.manufacturing.bop.update");
            as.setData(getBOPTypeUpdatedJson(oldType, newType));
        }

        as.setObject(object);
        as.setConverter(getConverterKey());
        if (!as.getData().equals("[]")) activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void classificationTypeAttributeCreated(ClassificationEvents.ClassificationTypeAttributeCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        ActivityStream as = new ActivityStream();
        if (event.getType().equals(PLMObjectType.ITEMTYPE)) {
            PLMItemTypeAttribute itemType = (PLMItemTypeAttribute) event.getObject();
            object.setObject(itemType.getItemType());
            as.setData(itemType.getName());
            as.setActivity("plm.classification.item.attribute.add");
            object.setType("itemType");
        }
        if (event.getType().equals(PLMObjectType.CHANGETYPE)) {
            PLMChangeTypeAttribute changeType = (PLMChangeTypeAttribute) event.getObject();
            object.setObject(changeType.getChangeType());
            as.setData(changeType.getName());
            as.setActivity("plm.classification.change.attribute.add");
            object.setType("changeType");
        }
        if (event.getType().equals(PLMObjectType.QUALITY_TYPE)) {
            PQMQualityTypeAttribute qualityType = (PQMQualityTypeAttribute) event.getObject();
            object.setObject(qualityType.getType());
            as.setData(qualityType.getName());
            as.setActivity("plm.classification.quality.attribute.add");
            object.setType("qualityType");
        }
        if (event.getType().equals(PLMObjectType.SPECIFICATIONTYPE)) {
            RmObjectTypeAttribute specificationType = (RmObjectTypeAttribute) event.getObject();
            object.setObject(specificationType.getRmObjectType());
            as.setData(specificationType.getName());
            as.setActivity("plm.classification.requirements.specification.attribute.add");
            object.setType("specType");
        }
        if (event.getType().equals(PLMObjectType.MANUFACTURERTYPE)) {
            PLMManufacturerTypeAttribute manufacturerType = (PLMManufacturerTypeAttribute) event.getObject();
            object.setObject(manufacturerType.getMfrType());
            as.setData(manufacturerType.getName());
            as.setActivity("plm.classification.oem.manufacturer.attribute.add");
            object.setType("manufacturerType");
        }
        if (event.getType().equals(PLMObjectType.MANUFACTURERPARTTYPE)) {
            PLMManufacturerPartTypeAttribute manufacturerPartType = (PLMManufacturerPartTypeAttribute) event.getObject();
            object.setObject(manufacturerPartType.getMfrPartType());
            as.setData(manufacturerPartType.getName());
            as.setActivity("plm.classification.oem.manufacturerpart.attribute.add");
            object.setType("manufacturerPartType");
        }
        if (event.getType().equals(PLMObjectType.SUPPLIERTYPE)) {
            PLMSupplierTypeAttribute supplierTypeAttribute = (PLMSupplierTypeAttribute) event.getObject();
            object.setObject(supplierTypeAttribute.getType());
            as.setData(supplierTypeAttribute.getName());
            as.setActivity("plm.classification.oem.supplier.attribute.add");
            object.setType("supplierType");
        }
        if (event.getType().equals(PLMObjectType.PMOBJECTTYPE)) {
            PMObjectTypeAttribute plantTypeAttribute = (PMObjectTypeAttribute) event.getObject();
            object.setObject(plantTypeAttribute.getType());
            as.setData(plantTypeAttribute.getName());
            as.setActivity("plm.classification.projectManagement.attribute.add");
            object.setType("pmType");
        }
        if (event.getType().equals(PLMObjectType.REQUIREMENTDOCUMENTTYPE)) {
            PLMRequirementObjectTypeAttribute plantTypeAttribute = (PLMRequirementObjectTypeAttribute) event.getObject();
            object.setObject(plantTypeAttribute.getType());
            as.setData(plantTypeAttribute.getName());
            as.setActivity("plm.classification.projectManagement.requirementDocument.attribute.add");
            object.setType("reqDocType");
        }
        if (event.getType().equals(PLMObjectType.REQUIREMENTTYPE)) {
            PLMRequirementObjectTypeAttribute plantTypeAttribute = (PLMRequirementObjectTypeAttribute) event.getObject();
            object.setObject(plantTypeAttribute.getType());
            as.setData(plantTypeAttribute.getName());
            as.setActivity("plm.classification.projectManagement.requirement.attribute.add");
            object.setType("reqType");
        }

        if (event.getType().equals(PLMObjectType.PGCSUBSTANCETYPE)) {
            PGCObjectTypeAttribute pgcObjectTypeAttribute = (PGCObjectTypeAttribute) event.getObject();
            object.setObject(pgcObjectTypeAttribute.getType());
            as.setData(pgcObjectTypeAttribute.getName());
            as.setActivity("plm.classification.compliance.substance.attribute.add");
            object.setType("substanceType");
        }
        if (event.getType().equals(PLMObjectType.PGCSUBSTANCEGROUPTYPE)) {
            PGCObjectTypeAttribute pgcObjectTypeAttribute = (PGCObjectTypeAttribute) event.getObject();
            object.setObject(pgcObjectTypeAttribute.getType());
            as.setData(pgcObjectTypeAttribute.getName());
            as.setActivity("plm.classification.compliance.substanceGroup.attribute.add");
            object.setType("substanceGroupType");
        }

        if (event.getType().equals(PLMObjectType.PGCSPECIFICATIONTYPE)) {
            PGCObjectTypeAttribute pgcObjectTypeAttribute = (PGCObjectTypeAttribute) event.getObject();
            object.setObject(pgcObjectTypeAttribute.getType());
            as.setData(pgcObjectTypeAttribute.getName());
            as.setActivity("plm.classification.compliance.specification.attribute.add");
            object.setType("specificationType");
        }

        if (event.getType().equals(PLMObjectType.PGCDECLARATIONTYPE)) {
            PGCObjectTypeAttribute pgcObjectTypeAttribute = (PGCObjectTypeAttribute) event.getObject();
            object.setObject(pgcObjectTypeAttribute.getType());
            as.setData(pgcObjectTypeAttribute.getName());
            as.setActivity("plm.classification.compliance.declaration.attribute.add");
            object.setType("declarationType");
        }
        if (event.getType().equals(PLMObjectType.PLANTTYPE)) {
            MESObjectTypeAttribute plantTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(plantTypeAttribute.getType());
            as.setData(plantTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.plant.attribute.add");
            object.setType("plantType");
        }
        if (event.getType().equals(PLMObjectType.ASSEMBLYLINETYPE)) {
            MESObjectTypeAttribute plantTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(plantTypeAttribute.getType());
            as.setData(plantTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.assemblyline.attribute.add");
            object.setType("assemblyLineType");
        }
        if (event.getType().equals(PLMObjectType.MANPOWERTYPE)) {
            MESObjectTypeAttribute manpowerTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(manpowerTypeAttribute.getType());
            as.setData(manpowerTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.manpower.attribute.add");
            object.setType("manpowerType");
        }
        if (event.getType().equals(PLMObjectType.TOOLTYPE)) {
            MESObjectTypeAttribute toolTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(toolTypeAttribute.getType());
            as.setData(toolTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.tool.attribute.add");
            object.setType("toolType");
        }
        if (event.getType().equals(PLMObjectType.MATERIALTYPE)) {
            MESObjectTypeAttribute materialTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(materialTypeAttribute.getType());
            as.setData(materialTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.material.attribute.add");
            object.setType("materialType");
        }
        if (event.getType().equals(PLMObjectType.SPAREPARTTYPE)) {
            MROObjectTypeAttribute mroObjectTypeAttribute = (MROObjectTypeAttribute) event.getObject();
            object.setObject(mroObjectTypeAttribute.getType());
            as.setData(mroObjectTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.mro.sparePart.attribute.add");
            object.setType("sparePartType");
        }
        if (event.getType().equals(PLMObjectType.ASSETTYPE)) {
            MROObjectTypeAttribute mroObjectTypeAttribute = (MROObjectTypeAttribute) event.getObject();
            object.setObject(mroObjectTypeAttribute.getType());
            as.setData(mroObjectTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.mro.asset.attribute.add");
            object.setType("assetType");
        }
        if (event.getType().equals(PLMObjectType.METERTYPE)) {
            MROObjectTypeAttribute mroObjectTypeAttribute = (MROObjectTypeAttribute) event.getObject();
            object.setObject(mroObjectTypeAttribute.getType());
            as.setData(mroObjectTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.mro.meter.attribute.add");
            object.setType("meterType");
        }
        if (event.getType().equals(PLMObjectType.WORKREQUESTTYPE)) {
            MROObjectTypeAttribute mroObjectTypeAttribute = (MROObjectTypeAttribute) event.getObject();
            object.setObject(mroObjectTypeAttribute.getType());
            as.setData(mroObjectTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.mro.workRequest.attribute.add");
            object.setType("workRequestType");
        }
        if (event.getType().equals(PLMObjectType.WORKORDERTYPE)) {
            MROObjectTypeAttribute mroObjectTypeAttribute = (MROObjectTypeAttribute) event.getObject();
            object.setObject(mroObjectTypeAttribute.getType());
            as.setData(mroObjectTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.mro.workOrder.attribute.add");
            object.setType("workOrderType");
        }
        if (event.getType().equals(PLMObjectType.JIGFIXTURETYPE)) {
            MESObjectTypeAttribute jigsFixTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(jigsFixTypeAttribute.getType());
            as.setData(jigsFixTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.jigsFix.attribute.add");
            object.setType("jigsFixType");
        }
        if (event.getType().equals(PLMObjectType.MACHINETYPE)) {
            MESObjectTypeAttribute machineTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(machineTypeAttribute.getType());
            as.setData(machineTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.machine.attribute.add");
            object.setType("machineType");
        }
        if (event.getType().equals(PLMObjectType.EQUIPMENTTYPE)) {
            MESObjectTypeAttribute machineTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(machineTypeAttribute.getType());
            as.setData(machineTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.equipment.attribute.add");
            object.setType("equipmentType");
        }
        if (event.getType().equals(PLMObjectType.INSTRUMENTTYPE)) {
            MESObjectTypeAttribute machineTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(machineTypeAttribute.getType());
            as.setData(machineTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.instrument.attribute.add");
            object.setType("instrumentType");
        }
        if (event.getType().equals(PLMObjectType.WORKCENTERTYPE)) {
            MESObjectTypeAttribute workCenterTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(workCenterTypeAttribute.getType());
            as.setData(workCenterTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.workCenter.attribute.add");
            object.setType("workCenterType");
        }
        if (event.getType().equals(PLMObjectType.OPERATIONTYPE)) {
            MESObjectTypeAttribute operationTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(operationTypeAttribute.getType());
            as.setData(operationTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.operation.attribute.add");
            object.setType("operationType");
        }
        if (event.getType().equals(PLMObjectType.WORKFLOWTYPE)) {
            PLMWorkflowTypeAttribute workflowType = (PLMWorkflowTypeAttribute) event.getObject();
            object.setObject(workflowType.getWorkflowType());
            as.setData(workflowType.getName());
            as.setActivity("plm.classification.workflow.attribute.add");
            object.setType("workflowType");
        }
        if (event.getType().equals(PLMObjectType.PRODUCTIONORDERTYPE)) {
            MESObjectTypeAttribute plantTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(plantTypeAttribute.getType());
            as.setData(plantTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.productionOrder.attribute.add");
            object.setType("productionOrderType");
        }
        if (event.getType().equals(PLMObjectType.SERVICEORDERTYPE)) {
            MESObjectTypeAttribute plantTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(plantTypeAttribute.getType());
            as.setData(plantTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.serviceOrder.attribute.add");
            object.setType("serviceOrderType");
        }
        if (event.getType().equals(PLMObjectType.MBOMTYPE)) {
            MESObjectTypeAttribute mbomTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(mbomTypeAttribute.getType());
            as.setData(mbomTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.mbom.attribute.add");
            object.setType("mbomType");
        }
        if (event.getType().equals(PLMObjectType.BOPTYPE)) {
            MESObjectTypeAttribute bopTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(bopTypeAttribute.getType());
            as.setData(bopTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.bop.attribute.add");
            object.setType("bopType");
        }

        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void classificationTypeAttributeDeleted(ClassificationEvents.ClassificationTypeAttributeDeletedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        ActivityStream as = new ActivityStream();
        if (event.getType().equals(PLMObjectType.ITEMTYPE)) {
            PLMItemTypeAttribute itemType = (PLMItemTypeAttribute) event.getObject();
            object.setObject(itemType.getItemType());
            as.setData(itemType.getName());
            as.setActivity("plm.classification.item.attribute.delete");
            object.setType("itemType");
        }
        if (event.getType().equals(PLMObjectType.CHANGETYPE)) {
            PLMChangeTypeAttribute changeType = (PLMChangeTypeAttribute) event.getObject();
            object.setObject(changeType.getChangeType());
            as.setData(changeType.getName());
            as.setActivity("plm.classification.change.attribute.delete");
            object.setType("changeType");
        }
        if (event.getType().equals(PLMObjectType.QUALITY_TYPE)) {
            PQMQualityTypeAttribute qualityType = (PQMQualityTypeAttribute) event.getObject();
            object.setObject(qualityType.getType());
            as.setData(qualityType.getName());
            as.setActivity("plm.classification.quality.attribute.delete");
            object.setType("qualityType");
        }
        if (event.getType().equals(PLMObjectType.SPECIFICATIONTYPE)) {
            RmObjectTypeAttribute specificationType = (RmObjectTypeAttribute) event.getObject();
            object.setObject(specificationType.getRmObjectType());
            as.setData(specificationType.getName());
            as.setActivity("plm.classification.requirements.specification.attribute.delete");
            object.setType("specType");
        }
        if (event.getType().equals(PLMObjectType.MANUFACTURERTYPE)) {
            PLMManufacturerTypeAttribute manufacturerType = (PLMManufacturerTypeAttribute) event.getObject();
            object.setObject(manufacturerType.getMfrType());
            as.setData(manufacturerType.getName());
            as.setActivity("plm.classification.oem.manufacturer.attribute.delete");
            object.setType("manufacturerTypeAttribute");
        }
        if (event.getType().equals(PLMObjectType.MANUFACTURERPARTTYPE)) {
            PLMManufacturerPartTypeAttribute manufacturerPartType = (PLMManufacturerPartTypeAttribute) event.getObject();
            object.setObject(manufacturerPartType.getMfrPartType());
            as.setData(manufacturerPartType.getName());
            as.setActivity("plm.classification.oem.manufacturerpart.attribute.delete");
            object.setType("manufacturerPartType");
        }
        if (event.getType().equals(PLMObjectType.SUPPLIERTYPE)) {
            PLMSupplierTypeAttribute supplierTypeAttribute = (PLMSupplierTypeAttribute) event.getObject();
            object.setObject(supplierTypeAttribute.getType());
            as.setData(supplierTypeAttribute.getName());
            as.setActivity("plm.classification.oem.supplier.attribute.delete");
            object.setType("supplierType");
        }

        if (event.getType().equals(PLMObjectType.PGCSUBSTANCETYPE)) {
            PGCObjectTypeAttribute pgcObjectTypeAttribute = (PGCObjectTypeAttribute) event.getObject();
            object.setObject(pgcObjectTypeAttribute.getType());
            as.setData(pgcObjectTypeAttribute.getName());
            as.setActivity("plm.classification.compliance.substance.attribute.delete");
            object.setType("substanceType");
        }
        if (event.getType().equals(PLMObjectType.PGCSUBSTANCEGROUPTYPE)) {
            PGCObjectTypeAttribute pgcObjectTypeAttribute = (PGCObjectTypeAttribute) event.getObject();
            object.setObject(pgcObjectTypeAttribute.getType());
            as.setData(pgcObjectTypeAttribute.getName());
            as.setActivity("plm.classification.compliance.substanceGroup.attribute.delete");
            object.setType("substanceGroupType");
        }
        if (event.getType().equals(PLMObjectType.PGCSPECIFICATIONTYPE)) {
            PGCObjectTypeAttribute pgcObjectTypeAttribute = (PGCObjectTypeAttribute) event.getObject();
            object.setObject(pgcObjectTypeAttribute.getType());
            as.setData(pgcObjectTypeAttribute.getName());
            as.setActivity("plm.classification.compliance.specification.attribute.delete");
            object.setType("specificationType");
        }
        if (event.getType().equals(PLMObjectType.PGCDECLARATIONTYPE)) {
            PGCObjectTypeAttribute pgcObjectTypeAttribute = (PGCObjectTypeAttribute) event.getObject();
            object.setObject(pgcObjectTypeAttribute.getType());
            as.setData(pgcObjectTypeAttribute.getName());
            as.setActivity("plm.classification.compliance.declaration.attribute.delete");
            object.setType("declarationType");
        }
        if (event.getType().equals(PLMObjectType.PLANTTYPE)) {
            MESObjectTypeAttribute plantTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(plantTypeAttribute.getType());
            as.setData(plantTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.plant.attribute.delete");
            object.setType("plantType");
        }
        if (event.getType().equals(PLMObjectType.ASSEMBLYLINETYPE)) {
            MESObjectTypeAttribute plantTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(plantTypeAttribute.getType());
            as.setData(plantTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.assemblyline.attribute.delete");
            object.setType("assemblyLineType");
        }
        if (event.getType().equals(PLMObjectType.MANPOWERTYPE)) {
            MESObjectTypeAttribute plantTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(plantTypeAttribute.getType());
            as.setData(plantTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.manpower.attribute.delete");
            object.setType("manpowerType");
        }
        if (event.getType().equals(PLMObjectType.TOOLTYPE)) {
            MESObjectTypeAttribute toolTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(toolTypeAttribute.getType());
            as.setData(toolTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.tool.attribute.delete");
            object.setType("toolType");
        }
        if (event.getType().equals(PLMObjectType.MATERIALTYPE)) {
            MESObjectTypeAttribute materialTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(materialTypeAttribute.getType());
            as.setData(materialTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.material.attribute.delete");
            object.setType("materialType");
        }
        if (event.getType().equals(PLMObjectType.SPAREPARTTYPE)) {
            MROObjectTypeAttribute mroObjectTypeAttribute = (MROObjectTypeAttribute) event.getObject();
            object.setObject(mroObjectTypeAttribute.getType());
            as.setData(mroObjectTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.mro.sparePart.attribute.delete");
            object.setType("sparePartType");
        }
        if (event.getType().equals(PLMObjectType.ASSETTYPE)) {
            MROObjectTypeAttribute mroObjectTypeAttribute = (MROObjectTypeAttribute) event.getObject();
            object.setObject(mroObjectTypeAttribute.getType());
            as.setData(mroObjectTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.mro.asset.attribute.delete");
            object.setType("assetType");
        }
        if (event.getType().equals(PLMObjectType.METERTYPE)) {
            MROObjectTypeAttribute mroObjectTypeAttribute = (MROObjectTypeAttribute) event.getObject();
            object.setObject(mroObjectTypeAttribute.getType());
            as.setData(mroObjectTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.mro.meter.attribute.delete");
            object.setType("meterType");
        }
        if (event.getType().equals(PLMObjectType.WORKREQUESTTYPE)) {
            MROObjectTypeAttribute mroObjectTypeAttribute = (MROObjectTypeAttribute) event.getObject();
            object.setObject(mroObjectTypeAttribute.getType());
            as.setData(mroObjectTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.mro.workRequest.attribute.delete");
            object.setType("workRequestType");
        }
        if (event.getType().equals(PLMObjectType.WORKORDERTYPE)) {
            MROObjectTypeAttribute mroObjectTypeAttribute = (MROObjectTypeAttribute) event.getObject();
            object.setObject(mroObjectTypeAttribute.getType());
            as.setData(mroObjectTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.mro.workOrder.attribute.delete");
            object.setType("workOrderType");
        }
        if (event.getType().equals(PLMObjectType.JIGFIXTURETYPE)) {
            MESObjectTypeAttribute jigsFixTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(jigsFixTypeAttribute.getType());
            as.setData(jigsFixTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.jigsFix.attribute.delete");
            object.setType("jigsFixType");
        }
        if (event.getType().equals(PLMObjectType.MACHINETYPE)) {
            MESObjectTypeAttribute machineTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(machineTypeAttribute.getType());
            as.setData(machineTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.machine.attribute.delete");
            object.setType("machineType");
        }
        if (event.getType().equals(PLMObjectType.EQUIPMENTTYPE)) {
            MESObjectTypeAttribute machineTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(machineTypeAttribute.getType());
            as.setData(machineTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.equipment.attribute.delete");
            object.setType("equipmentType");
        }
        if (event.getType().equals(PLMObjectType.INSTRUMENTTYPE)) {
            MESObjectTypeAttribute machineTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(machineTypeAttribute.getType());
            as.setData(machineTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.instrument.attribute.delete");
            object.setType("instrumentType");
        }
        if (event.getType().equals(PLMObjectType.WORKCENTERTYPE)) {
            MESObjectTypeAttribute workCenterTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(workCenterTypeAttribute.getType());
            as.setData(workCenterTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.workCenter.attribute.delete");
            object.setType("workCenterType");
        }
        if (event.getType().equals(PLMObjectType.PMOBJECTTYPE)) {
            PMObjectTypeAttribute workCenterTypeAttribute = (PMObjectTypeAttribute) event.getObject();
            object.setObject(workCenterTypeAttribute.getType());
            as.setData(workCenterTypeAttribute.getName());
            as.setActivity("plm.classification.projectManagement.attribute.delete");
            object.setType("pmType");
        }
        if (event.getType().equals(PLMObjectType.REQUIREMENTDOCUMENTTYPE)) {
            PLMRequirementObjectTypeAttribute workCenterTypeAttribute = (PLMRequirementObjectTypeAttribute) event.getObject();
            object.setObject(workCenterTypeAttribute.getType());
            as.setData(workCenterTypeAttribute.getName());
            as.setActivity("plm.classification.projectManagement.requirementDocument.attribute.delete");
            object.setType("reqDocType");
        }
        if (event.getType().equals(PLMObjectType.REQUIREMENTTYPE)) {
            PLMRequirementObjectTypeAttribute workCenterTypeAttribute = (PLMRequirementObjectTypeAttribute) event.getObject();
            object.setObject(workCenterTypeAttribute.getType());
            as.setData(workCenterTypeAttribute.getName());
            as.setActivity("plm.classification.projectManagement.requirement.attribute.delete");
            object.setType("reqType");
        }
        if (event.getType().equals(PLMObjectType.OPERATIONTYPE)) {
            MESObjectTypeAttribute operationTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(operationTypeAttribute.getType());
            as.setData(operationTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.operation.attribute.delete");
            object.setType("operationType");
        }
        if (event.getType().equals(PLMObjectType.WORKFLOWTYPE)) {
            PLMWorkflowTypeAttribute workflowType = (PLMWorkflowTypeAttribute) event.getObject();
            object.setObject(workflowType.getWorkflowType());
            as.setData(workflowType.getName());
            as.setActivity("plm.classification.workflow.attribute.delete");
            object.setType("workflowType");
        }
        if (event.getType().equals(PLMObjectType.PRODUCTIONORDERTYPE)) {
            MESObjectTypeAttribute plantTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(plantTypeAttribute.getType());
            as.setData(plantTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.productionOrder.attribute.delete");
            object.setType("productionOrderType");
        }
        if (event.getType().equals(PLMObjectType.SERVICEORDERTYPE)) {
            MESObjectTypeAttribute plantTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(plantTypeAttribute.getType());
            as.setData(plantTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.serviceOrder.attribute.delete");
            object.setType("serviceOrderType");
        }
        if (event.getType().equals(PLMObjectType.MBOMTYPE)) {
            MESObjectTypeAttribute mbomTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(mbomTypeAttribute.getType());
            as.setData(mbomTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.mbom.attribute.delete");
            object.setType("mbomType");
        }

        if (event.getType().equals(PLMObjectType.BOPTYPE)) {
            MESObjectTypeAttribute bopTypeAttribute = (MESObjectTypeAttribute) event.getObject();
            object.setObject(bopTypeAttribute.getType());
            as.setData(bopTypeAttribute.getName());
            as.setActivity("plm.classification.manufacturing.bop.attribute.delete");
            object.setType("bopType");
        }

        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void classificationTypeAttributeUpdated(ClassificationEvents.ClassificationTypeAttributeUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals(PLMObjectType.ITEMTYPE)) {
            PLMItemTypeAttribute oldType = (PLMItemTypeAttribute) event.getOldValue();
            PLMItemTypeAttribute newType = (PLMItemTypeAttribute) event.getNewValue();
            object.setObject(newType.getItemType());
            object.setType("itemType");
            as.setActivity("plm.classification.item.attribute.update");
            as.setData(getItemTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.CHANGETYPE)) {
            PLMChangeTypeAttribute oldType = (PLMChangeTypeAttribute) event.getOldValue();
            PLMChangeTypeAttribute newType = (PLMChangeTypeAttribute) event.getNewValue();
            object.setObject(newType.getChangeType());
            object.setType("changeType");
            as.setActivity("plm.classification.change.attribute.update");
            as.setData(getChangeTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.QUALITY_TYPE)) {
            PQMQualityTypeAttribute oldType = (PQMQualityTypeAttribute) event.getOldValue();
            PQMQualityTypeAttribute newType = (PQMQualityTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("qualityType");
            as.setActivity("plm.classification.quality.attribute.update");
            as.setData(getQualityTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.SPECIFICATIONTYPE)) {
            RmObjectTypeAttribute oldType = (RmObjectTypeAttribute) event.getOldValue();
            RmObjectTypeAttribute newType = (RmObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getRmObjectType());
            object.setType("specType");
            as.setActivity("plm.classification.requirements.specification.attribute.update");
            as.setData(getRmTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.MANUFACTURERTYPE)) {
            PLMManufacturerTypeAttribute oldType = (PLMManufacturerTypeAttribute) event.getOldValue();
            PLMManufacturerTypeAttribute newType = (PLMManufacturerTypeAttribute) event.getNewValue();
            object.setObject(newType.getMfrType());
            object.setType("manufacturerType");
            as.setActivity("plm.classification.oem.manufacturer.attribute.update");
            as.setData(getMfrTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.MANUFACTURERPARTTYPE)) {
            PLMManufacturerPartTypeAttribute oldType = (PLMManufacturerPartTypeAttribute) event.getOldValue();
            PLMManufacturerPartTypeAttribute newType = (PLMManufacturerPartTypeAttribute) event.getNewValue();
            object.setObject(newType.getMfrPartType());
            object.setType("manufacturerPartType");
            as.setActivity("plm.classification.oem.manufacturerpart.attribute.update");
            as.setData(getMfrPartTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.SUPPLIERTYPE)) {
            PLMSupplierTypeAttribute oldType = (PLMSupplierTypeAttribute) event.getOldValue();
            PLMSupplierTypeAttribute newType = (PLMSupplierTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("supplierType");
            as.setActivity("plm.classification.oem.supplier.attribute.update");
            as.setData(getSupplierTypeAttributeUpdatedJson(oldType, newType));
        }

        if (event.getType().equals(PLMObjectType.PGCSUBSTANCETYPE)) {
            PGCObjectTypeAttribute oldType = (PGCObjectTypeAttribute) event.getOldValue();
            PGCObjectTypeAttribute newType = (PGCObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("substanceType");
            as.setActivity("plm.classification.compliance.substance.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.PGCSUBSTANCEGROUPTYPE)) {
            PGCObjectTypeAttribute oldType = (PGCObjectTypeAttribute) event.getOldValue();
            PGCObjectTypeAttribute newType = (PGCObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("substanceGroupType");
            as.setActivity("plm.classification.compliance.substanceGroup.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.PGCSPECIFICATIONTYPE)) {
            PGCObjectTypeAttribute oldType = (PGCObjectTypeAttribute) event.getOldValue();
            PGCObjectTypeAttribute newType = (PGCObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("specificationType");
            as.setActivity("plm.classification.compliance.specification.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.PGCDECLARATIONTYPE)) {
            PGCObjectTypeAttribute oldType = (PGCObjectTypeAttribute) event.getOldValue();
            PGCObjectTypeAttribute newType = (PGCObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("declarationType");
            as.setActivity("plm.classification.compliance.declaration.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.PLANTTYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("plantType");
            as.setActivity("plm.classification.manufacturing.plant.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.ASSEMBLYLINETYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("assemblyLineType");
            as.setActivity("plm.classification.manufacturing.assemblyline.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.MANPOWERTYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("manpowerType");
            as.setActivity("plm.classification.manufacturing.manpower.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.TOOLTYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("toolType");
            as.setActivity("plm.classification.manufacturing.tool.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.MATERIALTYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("materialType");
            as.setActivity("plm.classification.manufacturing.material.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.SPAREPARTTYPE)) {
            MROObjectTypeAttribute oldType = (MROObjectTypeAttribute) event.getOldValue();
            MROObjectTypeAttribute newType = (MROObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("sparePartType");
            as.setActivity("plm.classification.manufacturing.mro.sparePart.attribute.update");
            as.setData(getMROTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.ASSETTYPE)) {
            MROObjectTypeAttribute oldType = (MROObjectTypeAttribute) event.getOldValue();
            MROObjectTypeAttribute newType = (MROObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("assetType");
            as.setActivity("plm.classification.manufacturing.mro.asset.attribute.update");
            as.setData(getMROTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.METERTYPE)) {
            MROObjectTypeAttribute oldType = (MROObjectTypeAttribute) event.getOldValue();
            MROObjectTypeAttribute newType = (MROObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("meterType");
            as.setActivity("plm.classification.manufacturing.mro.meter.attribute.update");
            as.setData(getMROTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.WORKREQUESTTYPE)) {
            MROObjectTypeAttribute oldType = (MROObjectTypeAttribute) event.getOldValue();
            MROObjectTypeAttribute newType = (MROObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("workRequestType");
            as.setActivity("plm.classification.manufacturing.mro.workRequest.attribute.update");
            as.setData(getMROTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.WORKORDERTYPE)) {
            MROObjectTypeAttribute oldType = (MROObjectTypeAttribute) event.getOldValue();
            MROObjectTypeAttribute newType = (MROObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("workOrderType");
            as.setActivity("plm.classification.manufacturing.mro.workOrder.attribute.update");
            as.setData(getMROTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.PMOBJECTTYPE)) {
            PMObjectTypeAttribute oldType = (PMObjectTypeAttribute) event.getOldValue();
            PMObjectTypeAttribute newType = (PMObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("pmType");
            as.setActivity("plm.classification.projectManagement.attribute.update");
            as.setData(getPMTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.REQUIREMENTDOCUMENTTYPE)) {
            PLMRequirementObjectTypeAttribute oldType = (PLMRequirementObjectTypeAttribute) event.getOldValue();
            PLMRequirementObjectTypeAttribute newType = (PLMRequirementObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("reqDocType");
            as.setActivity("plm.classification.projectManagement.requirementDocument.attribute.update");
            as.setData(getReqTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.REQUIREMENTTYPE)) {
            PLMRequirementObjectTypeAttribute oldType = (PLMRequirementObjectTypeAttribute) event.getOldValue();
            PLMRequirementObjectTypeAttribute newType = (PLMRequirementObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("reqType");
            as.setActivity("plm.classification.projectManagement.requirement.attribute.update");
            as.setData(getReqTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.JIGFIXTURETYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("jigsFixType");
            as.setActivity("plm.classification.manufacturing.jigsFix.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.MACHINETYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("machineType");
            as.setActivity("plm.classification.manufacturing.machine.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.EQUIPMENTTYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("equipmentType");
            as.setActivity("plm.classification.manufacturing.equipment.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.INSTRUMENTTYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("instrumentType");
            as.setActivity("plm.classification.manufacturing.instrument.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.WORKCENTERTYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("workCenterType");
            as.setActivity("plm.classification.manufacturing.workCenter.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.OPERATIONTYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            object.setType("operationType");
            as.setActivity("plm.classification.manufacturing.operation.attribute.update");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.WORKFLOWTYPE)) {
            PLMWorkflowTypeAttribute oldType = (PLMWorkflowTypeAttribute) event.getOldValue();
            PLMWorkflowTypeAttribute newType = (PLMWorkflowTypeAttribute) event.getNewValue();
            object.setObject(newType.getWorkflowType());
            object.setType("workflowType");
            as.setActivity("plm.classification.workflow.attribute.update");
            as.setData(getWorkflowTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.PRODUCTIONORDERTYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            as.setActivity("plm.classification.manufacturing.productionOrder.attribute.update");
            object.setType("productionOrderType");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.SERVICEORDERTYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            as.setActivity("plm.classification.manufacturing.serviceOrder.attribute.update");
            object.setType("serviceOrderType");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.MBOMTYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            as.setActivity("plm.classification.manufacturing.mbom.attribute.update");
            object.setType("mbomType");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }
        if (event.getType().equals(PLMObjectType.BOPTYPE)) {
            MESObjectTypeAttribute oldType = (MESObjectTypeAttribute) event.getOldValue();
            MESObjectTypeAttribute newType = (MESObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getType());
            as.setActivity("plm.classification.manufacturing.bop.attribute.update");
            object.setType("bopType");
            as.setData(getTypeAttributeUpdatedJson(oldType, newType));
        }


        as.setObject(object);
        as.setConverter(getConverterKey());
        if (!as.getData().equals("[]")) activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "plm.classification";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            String name = null;
            if (object.getType().equals("itemType")) name = itemTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("changeType"))
                name = changeTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("specType"))
                name = specificationTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("manufacturerType"))
                name = manufacturerTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("manufacturerPartType"))
                name = manufacturerPartTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("qualityType"))
                name = qualityTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("materialType"))
                name = materialTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("jigsFixType"))
                name = jigsFixtureTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("plantType"))
                name = plantTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("assemblyLineType"))
                name = assemblyLineTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("machineType"))
                name = machineTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("workCenterType"))
                name = workCenterTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("toolType"))
                name = toolTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("operationType"))
                name = operationTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("workflowType"))
                name = workflowTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("productionOrderType"))
                name = productionOrderTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("serviceOrderType"))
                name = serviceOrderTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("manpowerType"))
                name = manpowerTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("equipmentType"))
                name = equipmentTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("instrumentType"))
                name = instrumentTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("pmType"))
                name = pmObjectTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("reqDocType"))
                name = requirementDocumentTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("reqType"))
                name = plmRequirementTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("sparePartType"))
                name = sparePartTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("workRequestType"))
                name = workRequestTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("workOrderType"))
                name = workOrderTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("assetType"))
                name = assetTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("meterType"))
                name = meterTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("supplierType"))
                name = supplierTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("substanceType"))
                name = substanceTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("substanceGroupType"))
                name = substanceGroupTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("specificationType"))
                name = pgcSpecificationTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("declarationType"))
                name = declarationTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("bopType"))
                name = bopTypeRepository.findOne(object.getObject()).getName();
            if (object.getType().equals("mbomType"))
                name = mbomTypeRepository.findOne(object.getObject()).getName();
                
            String activity = as.getActivity();
            switch (activity) {
                case "plm.classification.item.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.change.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.quality.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.material.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.manpower.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.jigsFix.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.requirements.requirement.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.requirements.specification.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.oem.manufacturer.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.oem.manufacturerpart.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.oem.supplier.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.compliance.substance.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.compliance.substanceGroup.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.compliance.specification.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.compliance.declaration.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.plant.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.assemblyline.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.machine.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.equipment.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.instrument.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.mro.asset.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.mro.meter.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.mro.sparePart.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.mro.workRequest.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.mro.workOrder.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.workCenter.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.tool.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.operation.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.projectManagement.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.projectManagement.requirementDocument.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.projectManagement.requirement.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.workflow.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.item.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.productionOrder.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.serviceOrder.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.manufacturing.mbom.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                 case "plm.classification.manufacturing.bop.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "plm.classification.change.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.quality.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.material.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.asset.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.meter.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.sparePart.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.workRequest.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.workOrder.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.jigsFix.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.requirements.requirement.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.requirements.specification.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.oem.manufacturer.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.oem.manufacturerpart.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.oem.supplier.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.substance.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.substanceGroup.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.specification.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.declaration.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.projectManagement.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.projectManagement.requirementDocument.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.projectManagement.requirement.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.plant.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.assemblyline.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.manpower.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.machine.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.equipment.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.instrument.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.workCenter.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.tool.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.operation.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.workflow.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.productionOrder.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.serviceOrder.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mbom.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.bop.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.item.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.change.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.quality.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.material.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.asset.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.meter.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.sparePart.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.workRequest.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.workOrder.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.jigsFix.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.requirements.requirement.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.requirements.specification.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.oem.manufacturer.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.oem.manufacturerpart.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.oem.supplier.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.substance.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.substanceGroup.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.specification.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.declaration.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.plant.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.assemblyline.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.manpower.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.machine.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.equipment.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.instrument.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.workCenter.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.tool.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.operation.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.workflow.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.productionOrder.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.serviceOrder.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mbom.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.bop.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mbom.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.mbom.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name,  as);
                    break;
                case "plm.classification.manufacturing.mbom.attribute.delete":
                     convertedString = getTypeDeleteString(messageString, actor, name,  as);
                     break;
                     case "plm.classification.manufacturing.bop.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.bop.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name,  as);
                    break;
                case "plm.classification.manufacturing.bop.attribute.delete":
                     convertedString = getTypeDeleteString(messageString, actor, name,  as);
                     break;
                case "plm.classification.c.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.projectManagement.requirement.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "plm.classification.item.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.change.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.quality.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.requirements.requirement.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.requirements.specification.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.oem.manufacturer.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.oem.manufacturerpart.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.oem.supplier.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.compliance.substance.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.compliance.substanceGroup.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.compliance.specification.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.compliance.declaration.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.plant.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.assemblyline.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.projectManagement.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.projectManagement.requirementDocument.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.projectManagement.requirement.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.manpower.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.tool.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.material.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.mro.asset.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.mro.meter.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.mro.sparePart.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.mro.workRequest.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.mro.workOrder.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.jigsFix.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.machine.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.equipment.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.instrument.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.workCenter.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.operation.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.workflow.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.productionOrder.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.manufacturing.serviceOrder.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "plm.classification.item.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.change.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.quality.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.requirements.requirement.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.requirements.specification.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.oem.manufacturer.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.oem.supplier.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.substance.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.substanceGroup.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.specification.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.declaration.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.oem.manufacturerpart.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.plant.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.assemblyline.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.projectManagement.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.projectManagement.requirementDocument.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.projectManagement.requirement.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.manpower.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.tool.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.asset.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.meter.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.sparePart.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.workRequest.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.workOrder.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.material.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.jigsFix.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.machine.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.equipment.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.instrument.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.workCenter.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.operation.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.workflow.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.productionOrder.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.serviceOrder.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "plm.classification.item.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.change.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.quality.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.requirements.requirement.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.requirements.specification.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.oem.manufacturer.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.oem.manufacturerpart.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.oem.supplier.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.substance.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.substanceGroup.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.specification.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.compliance.declaration.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.workflow.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.plant.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.assemblyline.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.projectManagement.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.projectManagement.requirementDocument.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.projectManagement.requirement.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.manpower.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.tool.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.material.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.jigsFix.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.asset.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.meter.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.sparePart.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.workRequest.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.mro.workOrder.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.machine.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.equipment.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.instrument.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.workCenter.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.operation.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.productionOrder.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                case "plm.classification.manufacturing.serviceOrder.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getTypeCreatedString(String messageString, Person actor, String name) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), name);
    }

    private String getTypeDeleteString(String messageString, Person actor, String name, ActivityStream as) {
        String json = as.getData();
        return MessageFormat.format(messageString, actor.getFullName().trim(), json, name);
    }

    private String getTypeUpdatedString(String messageString, Person actor, String name, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), name);

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.classification.item.update.property");
        String displayTabString = activityStreamResourceBundle.getString("plm.classification.item.update.property").substring(0, 20);

        String json = as.getData();
        try {
            List<ASPropertyChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASPropertyChangeDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = null;
                if (p.getProperty().equals("Display Tabs")) {
                    s = addMarginToMessage(MessageFormat.format(displayTabString, highlightValue(p.getProperty())));
                    sb.append(s);
                } else {
                    s = addMarginToMessage(MessageFormat.format(updateString,
                            highlightValue(p.getProperty()),
                            highlightValue(p.getOldValue()),
                            highlightValue(p.getNewValue())));
                    sb.append(s);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getTypeAttributeUpdatedString(String messageString, Person actor, String name, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), name);

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.classification.item.attribute.update.property");

        String json = as.getData();
        try {
            List<ASPropertyChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASPropertyChangeDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(p.getProperty()),
                        highlightValue(p.getOldValue()),
                        highlightValue(p.getNewValue())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getTypeAttributeCreatedString(String messageString, Person actor, ActivityStream as) {
        String name = as.getData();
        return MessageFormat.format(messageString, actor.getFullName().trim(), name);
    }

    private String getTypeAttributeDeletedString(String messageString, Person actor, String name, ActivityStream as) {
        String data = as.getData();
        return MessageFormat.format(messageString, actor.getFullName().trim(), data, name);
    }

    private String getItemTypeUpdatedJson(PLMItemType oldType, PLMItemType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getItemNumberSource().getName();
        String newValue2 = newType.getItemNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String oldValue3 = oldType.getRevisionSequence().getName();
        String newValue3 = newType.getRevisionSequence().getName();
        if (oldValue3 == null) {
            oldValue3 = "";
        }
        if (newValue3 == null) {
            newValue3 = "";
        }
        if (!newValue3.equals(oldValue3)) {
            changes.add(new ASPropertyChangeDTO("Revision sequence", oldValue3, newValue3));
        }

        String oldValue4 = oldType.getLifecycle().getName();
        String newValue4 = newType.getLifecycle().getName();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getSoftwareType().toString();
        String newValue5 = newType.getSoftwareType().toString();
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Is software type", oldValue5, newValue5));
        }

        String oldValue6 = oldType.getRequiredEco().toString();
        String newValue6 = newType.getRequiredEco().toString();
        if (!newValue6.equals(oldValue6)) {
            changes.add(new ASPropertyChangeDTO("Required ECO", oldValue6, newValue6));
        }

        String[] oldValue7 = oldType.getTabs();
        String[] newValue7 = newType.getTabs();
        if (!Arrays.equals(oldValue7, newValue7)) {
            changes.add(new ASPropertyChangeDTO("Display Tabs", null, null));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getTypeUpdatedJson(PLMRequirementDocumentType oldType, PLMRequirementDocumentType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String[] oldValue7 = oldType.getTabs();
        String[] newValue7 = newType.getTabs();
        if (!Arrays.equals(oldValue7, newValue7)) {
            changes.add(new ASPropertyChangeDTO("Display Tabs", null, null));
        }

        String oldValue3 = oldType.getRevisionSequence().getName();
        String newValue3 = newType.getRevisionSequence().getName();
        if (oldValue3 == null) {
            oldValue3 = "";
        }
        if (newValue3 == null) {
            newValue3 = "";
        }
        if (!newValue3.equals(oldValue3)) {
            changes.add(new ASPropertyChangeDTO("Revision sequence", oldValue3, newValue3));
        }

        String oldValue4 = oldType.getLifecycle().getName();
        String newValue4 = newType.getLifecycle().getName();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue4, newValue4));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getTypeUpdatedJson(PLMRequirementType oldType, PLMRequirementType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String[] oldValue7 = oldType.getTabs();
        String[] newValue7 = newType.getTabs();
        if (!Arrays.equals(oldValue7, newValue7)) {
            changes.add(new ASPropertyChangeDTO("Display Tabs", null, null));
        }

        if (oldType.getPriorityList() != null) {
            String oldValue3 = oldType.getPriorityList().getName();
            String newValue3 = newType.getPriorityList().getName();
            if (oldValue3 == null) {
                oldValue3 = "";
            }
            if (newValue3 == null) {
                newValue3 = "";
            }
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Priority List", oldValue3, newValue3));
            }
        }
        String oldValue4 = oldType.getLifecycle().getName();
        String newValue4 = newType.getLifecycle().getName();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue4, newValue4));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTypeUpdatedJson(MESAssemblyLineType oldType, MESAssemblyLineType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getChangeTypeUpdatedJson(PLMChangeType oldType, PLMChangeType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = null;
        String newValue2 = null;
        if (oldType.getAutoNumberSource() != null) {
            oldValue2 = oldType.getAutoNumberSource().getName();
        }
        if (newType.getAutoNumberSource() != null) {
            newValue2 = newType.getAutoNumberSource().getName();
        }
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }


        if (oldType.getChangeReasonTypes() != null && newType.getChangeReasonTypes() != null) {
            String oldValue3 = oldType.getChangeReasonTypes().getName();
            String newValue3 = newType.getChangeReasonTypes().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Change Reason", oldValue3, newValue3));
            }
        }


        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQualityTypeUpdatedJson(PQMQualityType oldType, PQMQualityType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }


        String oldValue3 = oldType.getNumberSource().getName();
        String newValue3 = newType.getNumberSource().getName();
        if (!newValue3.equals(oldValue3)) {
            changes.add(new ASPropertyChangeDTO("Number source", oldValue3, newValue3));
        }

        if (oldType instanceof PQMProductInspectionPlanType && newType instanceof PQMProductInspectionPlanType) {
            PQMProductInspectionPlanType oldType1 = (PQMProductInspectionPlanType) oldType;
            PQMProductInspectionPlanType newType1 = (PQMProductInspectionPlanType) newType;
            String oldValue7;
            String newValue7;
            PLMItemType oldValue4 = oldType1.getProductType();
            PLMItemType newValue4 = newType1.getProductType();
            if (oldValue4 == null) {
                oldValue7 = "";
            } else {
                oldValue7 = oldType1.getProductType().getName();
            }
            if (newValue4 == null) {
                newValue7 = "";
            } else {
                newValue7 = newType1.getProductType().getName();
            }
            if (!newValue7.equals(oldValue7)) {
                changes.add(new ASPropertyChangeDTO("Product type", oldValue7, newValue7));
            }

            String oldValue5 = oldType1.getLifecycle().getName();
            String newValue5 = newType1.getLifecycle().getName();
            if (!newValue5.equals(oldValue5)) {
                changes.add(new ASPropertyChangeDTO("Lifecycle", oldValue5, newValue5));
            }

            String oldValue6 = oldType1.getInspectionNumberSource().getName();
            String newValue6 = newType1.getInspectionNumberSource().getName();
            if (!newValue6.equals(oldValue6)) {
                changes.add(new ASPropertyChangeDTO("Inspection number source", oldValue6, newValue6));
            }
        }

        if (oldType instanceof PQMMaterialInspectionPlanType && newType instanceof PQMMaterialInspectionPlanType) {
            PQMMaterialInspectionPlanType oldType1 = (PQMMaterialInspectionPlanType) oldType;
            PQMMaterialInspectionPlanType newType1 = (PQMMaterialInspectionPlanType) newType;
            String oldValue7;
            String newValue7;
            PLMManufacturerPartType oldValue4 = oldType1.getPartType();
            PLMManufacturerPartType newValue4 = newType1.getPartType();
            if (oldValue4 == null) {
                oldValue7 = "";
            } else {
                oldValue7 = oldType1.getPartType().getName();
            }
            if (newValue4 == null) {
                newValue7 = "";
            } else {
                newValue7 = newType1.getPartType().getName();
            }
            if (!newValue7.equals(oldValue7)) {
                changes.add(new ASPropertyChangeDTO("Part type", oldValue7, newValue7));
            }

            String oldValue5 = oldType1.getLifecycle().getName();
            String newValue5 = newType1.getLifecycle().getName();
            if (!newValue5.equals(oldValue5)) {
                changes.add(new ASPropertyChangeDTO("Lifecycle", oldValue5, newValue5));
            }

            String oldValue6 = oldType1.getInspectionNumberSource().getName();
            String newValue6 = newType1.getInspectionNumberSource().getName();
            if (!newValue5.equals(oldValue5)) {
                changes.add(new ASPropertyChangeDTO("Inspection number source", oldValue6, newValue6));
            }
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getReqTypeUpdatedJson(RequirementType oldType, RequirementType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }


        String oldValue3 = oldType.getNumberSource().getName();
        String newValue3 = newType.getNumberSource().getName();
        if (oldValue3 == null) {
            oldValue3 = "";
        }
        if (newValue3 == null) {
            newValue3 = "";
        }
        if (!newValue3.equals(oldValue3)) {
            changes.add(new ASPropertyChangeDTO("Number source", oldValue3, newValue3));
        }

        String oldValue4 = oldType.getRevisionSequence().getName();
        String newValue4 = newType.getRevisionSequence().getName();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Revision sequence", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getLifecycle().getName();
        String newValue5 = newType.getLifecycle().getName();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue5, newValue5));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSpecTypeUpdatedJson(SpecificationType oldType, SpecificationType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue3 = oldType.getNumberSource().getName();
        String newValue3 = newType.getNumberSource().getName();
        if (oldValue3 == null) {
            oldValue3 = "";
        }
        if (newValue3 == null) {
            newValue3 = "";
        }
        if (!newValue3.equals(oldValue3)) {
            changes.add(new ASPropertyChangeDTO("Number source", oldValue3, newValue3));
        }

        String oldValue4 = oldType.getRevisionSequence().getName();
        String newValue4 = newType.getRevisionSequence().getName();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Revision sequence", oldValue3, newValue3));
        }

        String oldValue5 = oldType.getLifecycle().getName();
        String newValue5 = newType.getLifecycle().getName();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue5, newValue5));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMfrTypeUpdatedJson(PLMManufacturerType oldType, PLMManufacturerType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }
        String oldValue3 = oldType.getLifecycle().getName();
        String newValue3 = newType.getLifecycle().getName();
        if (oldValue3 == null) {
            oldValue3 = "";
        }
        if (newValue3 == null) {
            newValue3 = "";
        }
        if (!newValue3.equals(oldValue3)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue3, newValue3));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMfrPartTypeUpdatedJson(PLMManufacturerPartType oldType, PLMManufacturerPartType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue3 = oldType.getLifecycle().getName();
        String newValue3 = newType.getLifecycle().getName();
        if (oldValue3 == null) {
            oldValue3 = "";
        }
        if (newValue3 == null) {
            newValue3 = "";
        }
        if (!newValue3.equals(oldValue3)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue3, newValue3));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierTypeUpdatedJson(PLMSupplierType oldType, PLMSupplierType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue3 = oldType.getLifecycle().getName();
        String newValue3 = newType.getLifecycle().getName();
        if (oldValue3 == null) {
            oldValue3 = "";
        }
        if (newValue3 == null) {
            newValue3 = "";
        }
        if (!newValue3.equals(oldValue3)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue3, newValue3));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getTypeUpdatedJson(PGCSubstanceType oldType, PGCSubstanceType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }
        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTypeUpdatedJson(PGCSubstanceGroupType oldType, PGCSubstanceGroupType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTypeUpdatedJson(PGCSpecificationType oldType, PGCSpecificationType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTypeUpdatedJson(PGCDeclarationType oldType, PGCDeclarationType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTypeUpdatedJson(MESPlantType oldType, MESPlantType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getTypeUpdatedJson(PMObjectType oldType, PMObjectType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String[] oldValue7 = oldType.getTabs();
        String[] newValue7 = newType.getTabs();
        if (!Arrays.equals(oldValue7, newValue7)) {
            changes.add(new ASPropertyChangeDTO("Display Tabs", null, null));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTypeUpdatedJson(MESEquipmentType oldType, MESEquipmentType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTypeUpdatedJson(MESInstrumentType oldType, MESInstrumentType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTypeUpdatedJson(MESMachineType oldType, MESMachineType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTypeUpdatedJson(MESWorkCenterType oldType, MESWorkCenterType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTypeUpdatedJson(MESManpowerType oldType, MESManpowerType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMaterialTypeUpdatedJson(MESMaterialType oldType, MESMaterialType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getJigsFixTypeUpdatedJson(MESJigsFixtureType oldType, MESJigsFixtureType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = null;
        if(oldType.getAutoNumberSource() != null) {
            oldValue2 = oldType.getAutoNumberSource().getName();
        }
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getToolTypeUpdatedJson(MESToolType oldType, MESToolType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMROSparePartTypeUpdatedJson(MROSparePartType oldType, MROSparePartType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMROMeterTypeUpdatedJson(MROMeterType oldType, MROMeterType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMROAssetTypeUpdatedJson(MROAssetType oldType, MROAssetType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMROWorkRequestTypeUpdatedJson(MROWorkRequestType oldType, MROWorkRequestType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMROWorkOrderTypeUpdatedJson(MROWorkOrderType oldType, MROWorkOrderType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTypeUpdatedJson(MESOperationType oldType, MESOperationType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getAutoNumberSource().getName();
        String newValue2 = newType.getAutoNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProductionOrderTypeUpdatedJson(MESProductionOrderType oldType, MESProductionOrderType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = null;
        String newValue2 = null;
        if (oldType.getAutoNumberSource() != null) {
            oldValue2 = oldType.getAutoNumberSource().getName();
        }
        if (newType.getAutoNumberSource() != null) {
            newValue2 = newType.getAutoNumberSource().getName();
        }
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String oldValue5 = null;
        String newValue5 = null;
        if (oldType.getLifecycle() != null) {
            oldValue5 = oldType.getLifecycle().getName();
        }
        if (newType.getLifecycle() != null) {
            newValue5 = newType.getLifecycle().getName();
        }
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue5, newValue5));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getServiceOrderTypeUpdatedJson(MESServiceOrderType oldType, MESServiceOrderType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = null;
        String newValue2 = null;
        if (oldType.getAutoNumberSource() != null) {
            oldValue2 = oldType.getAutoNumberSource().getName();
        }
        if (newType.getAutoNumberSource() != null) {
            newValue2 = newType.getAutoNumberSource().getName();
        }
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String oldValue5 = oldType.getLifecycle().getName();
        String newValue5 = newType.getLifecycle().getName();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue5, newValue5));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getMBOMTypeUpdatedJson(MESMBOMType oldType, MESMBOMType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = null;
        String newValue2 = null;
        if (oldType.getAutoNumberSource() != null) {
            oldValue2 = oldType.getAutoNumberSource().getName();
        }
        if (newType.getAutoNumberSource() != null) {
            newValue2 = newType.getAutoNumberSource().getName();
        }
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String oldValue3 = oldType.getRevisionSequence().getName();
        String newValue3 = newType.getRevisionSequence().getName();
        if (oldValue3 == null) {
            oldValue3 = "";
        }
        if (newValue3 == null) {
            newValue3 = "";
        }
        if (!newValue3.equals(oldValue3)) {
            changes.add(new ASPropertyChangeDTO("Revision sequence", oldValue3, newValue3));
        }

        String oldValue4 = oldType.getLifecycle().getName();
        String newValue4 = newType.getLifecycle().getName();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue4, newValue4));
        }

    
        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getBOPTypeUpdatedJson(MESBOPType oldType, MESBOPType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = null;
        String newValue2 = null;
        if (oldType.getAutoNumberSource() != null) {
            oldValue2 = oldType.getAutoNumberSource().getName();
        }
        if (newType.getAutoNumberSource() != null) {
            newValue2 = newType.getAutoNumberSource().getName();
        }
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String oldValue3 = oldType.getRevisionSequence().getName();
        String newValue3 = newType.getRevisionSequence().getName();
        if (oldValue3 == null) {
            oldValue3 = "";
        }
        if (newValue3 == null) {
            newValue3 = "";
        }
        if (!newValue3.equals(oldValue3)) {
            changes.add(new ASPropertyChangeDTO("Revision sequence", oldValue3, newValue3));
        }

        String oldValue4 = oldType.getLifecycle().getName();
        String newValue4 = newType.getLifecycle().getName();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue4, newValue4));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
    private String getWorkflowTypeUpdatedJson(PLMWorkflowType oldType, PLMWorkflowType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }


        String oldValue3 = oldType.getLifecycle().getName();
        String newValue3 = newType.getLifecycle().getName();
        if (oldValue3 == null) {
            oldValue3 = "";
        }
        if (newValue3 == null) {
            newValue3 = "";
        }
        if (!newValue3.equals(oldValue3)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue3, newValue3));
        }

        String oldValue4 = oldType.getNumberSource().getName();
        String newValue4 = newType.getNumberSource().getName();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getAssignable();
        String newValue5 = newType.getAssignable();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Assignable To", oldValue5, newValue5));
        }

        String oldValue6 = "";
        String newValue6 = "";
        if (oldType.getAssignedType() != null) {
            if (oldType.getAssignable().equals("ITEMS"))
                oldValue6 = itemTypeRepository.findOne(oldType.getAssignedType()).getName();
            if (oldType.getAssignable().equals("CHANGES"))
                oldValue6 = changeTypeRepository.findOne(oldType.getAssignedType()).getName();
            if (oldType.getAssignable().equals("QUALITY"))
                oldValue6 = qualityTypeRepository.findOne(oldType.getAssignedType()).getName();
            if (oldType.getAssignable().equals("REQUIREMENTS"))
                oldValue6 = objectTypeRepository.findOne(oldType.getAssignedType()).getName();
            if (oldType.getAssignable().equals("MANUFACTURERS"))
                oldValue6 = manufacturerTypeRepository.findOne(oldType.getAssignedType()).getName();
            if (oldType.getAssignable().equals("MANUFACTURER PARTS"))
                oldValue6 = manufacturerPartTypeRepository.findOne(oldType.getAssignedType()).getName();
        }
        if (newType.getAssignedType() != null) {
            if (newType.getAssignable().equals("ITEMS"))
                newValue6 = itemTypeRepository.findOne(newType.getAssignedType()).getName();
            if (newType.getAssignable().equals("CHANGES"))
                newValue6 = changeTypeRepository.findOne(newType.getAssignedType()).getName();
            if (newType.getAssignable().equals("QUALITY"))
                newValue6 = qualityTypeRepository.findOne(newType.getAssignedType()).getName();
            if (newType.getAssignable().equals("REQUIREMENTS"))
                newValue6 = objectTypeRepository.findOne(newType.getAssignedType()).getName();
            if (newType.getAssignable().equals("MANUFACTURERS"))
                newValue6 = manufacturerTypeRepository.findOne(newType.getAssignedType()).getName();
            if (newType.getAssignable().equals("MANUFACTURER PARTS"))
                newValue6 = manufacturerPartTypeRepository.findOne(newType.getAssignedType()).getName();
        }
        if (!newValue6.equals(oldValue6)) {
            changes.add(new ASPropertyChangeDTO("Assigned Type", oldValue6, newValue6));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getItemTypeAttributeUpdatedJson(PLMItemTypeAttribute oldType, PLMItemTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
          oldGroupName = "";
        }
         if (newGroupName == null) {
        newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
        changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }


        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }

        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }

        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultListValue();
        String newValue5 = newType.getDefaultListValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }

        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        Boolean oldValue6 = oldType.getConfigurable();
        Boolean newValue6 = newType.getConfigurable();
        if (newValue6 != oldValue6) {
            changes.add(new ASPropertyChangeDTO("Configurable", oldValue6.toString(), newValue6.toString()));
        }

        Boolean oldValue7 = oldType.getRevisionSpecific();
        Boolean newValue7 = newType.getRevisionSpecific();
        if (newValue7 != oldValue7) {
            changes.add(new ASPropertyChangeDTO("Revision specific", oldValue7.toString(), newValue7.toString()));
        }

        Boolean oldValue8 = oldType.getChangeControlled();
        Boolean newValue8 = newType.getChangeControlled();
        if (newValue8 != oldValue8) {
            changes.add(new ASPropertyChangeDTO("Change controlled", oldValue8.toString(), newValue8.toString()));
        }

        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getChangeTypeAttributeUpdatedJson(PLMChangeTypeAttribute oldType, PLMChangeTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
          oldGroupName = "";
        }
         if (newGroupName == null) {
        newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
        changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }


        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }


        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }


        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultListValue();
        String newValue5 = newType.getDefaultListValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }

        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQualityTypeAttributeUpdatedJson(PQMQualityTypeAttribute oldType, PQMQualityTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
          oldGroupName = "";
        }
         if (newGroupName == null) {
        newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
        changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }


        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }


        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }

        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultListValue();
        String newValue5 = newType.getDefaultListValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }


        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        Boolean oldValue7 = oldType.getRevisionSpecific();
        Boolean newValue7 = newType.getRevisionSpecific();
        if (newValue7 != oldValue7) {
            changes.add(new ASPropertyChangeDTO("Revision specific", oldValue7.toString(), newValue7.toString()));
        }

        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getRmTypeAttributeUpdatedJson(RmObjectTypeAttribute oldType, RmObjectTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
          oldGroupName = "";
        }
         if (newGroupName == null) {
        newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
        changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }


        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }


        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }

        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultListValue();
        String newValue5 = newType.getDefaultListValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }

        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        Boolean oldValue7 = oldType.getRevisionSpecific();
        Boolean newValue7 = newType.getRevisionSpecific();
        if (newValue7 != oldValue7) {
            changes.add(new ASPropertyChangeDTO("Revision specific", oldValue7.toString(), newValue7.toString()));
        }

        Boolean oldValue8 = oldType.getChangeControlled();
        Boolean newValue8 = newType.getChangeControlled();
        if (newValue8 != oldValue8) {
            changes.add(new ASPropertyChangeDTO("Change controlled", oldValue8.toString(), newValue8.toString()));
        }

        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMfrTypeAttributeUpdatedJson(PLMManufacturerTypeAttribute oldType, PLMManufacturerTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
          oldGroupName = "";
        }
         if (newGroupName == null) {
        newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
        changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }

        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }


        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }

        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultListValue();
        String newValue5 = newType.getDefaultListValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }

        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierTypeAttributeUpdatedJson(PLMSupplierTypeAttribute oldType, PLMSupplierTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
          oldGroupName = "";
        }
         if (newGroupName == null) {
        newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
        changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }

        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }


        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }

        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultListValue();
        String newValue5 = newType.getDefaultListValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }


        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMfrPartTypeAttributeUpdatedJson(PLMManufacturerPartTypeAttribute oldType, PLMManufacturerPartTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
          oldGroupName = "";
        }
         if (newGroupName == null) {
        newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
        changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }

        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }


        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }

        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultListValue();
        String newValue5 = newType.getDefaultListValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }


        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTypeAttributeUpdatedJson(PGCObjectTypeAttribute oldType, PGCObjectTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
          oldGroupName = "";
        }
         if (newGroupName == null) {
        newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
        changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }

        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }


        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }

        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultListValue();
        String newValue5 = newType.getDefaultListValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }


        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTypeAttributeUpdatedJson(MESObjectTypeAttribute oldType, MESObjectTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
          oldGroupName = "";
        }
         if (newGroupName == null) {
        newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
        changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }

        

        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }


        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }

        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultListValue();
        String newValue5 = newType.getDefaultListValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }


        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMROTypeAttributeUpdatedJson(MROObjectTypeAttribute oldType, MROObjectTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
          oldGroupName = "";
        }
         if (newGroupName == null) {
        newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
        changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }

        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }


        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }

        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultListValue();
        String newValue5 = newType.getDefaultListValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }


        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getPMTypeAttributeUpdatedJson(PMObjectTypeAttribute oldType, PMObjectTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
          oldGroupName = "";
        }
         if (newGroupName == null) {
        newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
        changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }

        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }


        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }

        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultListValue();
        String newValue5 = newType.getDefaultListValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }


        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getReqTypeAttributeUpdatedJson(PLMRequirementObjectTypeAttribute oldType, PLMRequirementObjectTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
          oldGroupName = "";
        }
         if (newGroupName == null) {
        newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
        changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }

        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }


        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }

        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultListValue();
        String newValue5 = newType.getDefaultListValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }


        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getWorkflowTypeAttributeUpdatedJson(PLMWorkflowTypeAttribute oldType, PLMWorkflowTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
          oldGroupName = "";
        }
         if (newGroupName == null) {
        newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
        changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }

        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }


        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }

        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultTextValue();
        String newValue5 = newType.getDefaultTextValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }


        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
}
