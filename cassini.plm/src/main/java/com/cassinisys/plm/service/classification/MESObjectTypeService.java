package com.cassinisys.plm.service.classification;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.cassinisys.platform.service.core.TypeSystem;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.event.PlantEvents;
import com.cassinisys.plm.filtering.MESObjectCriteria;
import com.cassinisys.plm.filtering.MESObjectPredicateBuilder;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mfr.SupplierTypeRepository;
import com.cassinisys.plm.repo.mro.*;
import com.cassinisys.plm.service.plm.ItemTypeService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by GSR on 03-06-2020.
 */
@Service
public class MESObjectTypeService implements CrudService<MESObjectType, Integer>,
        TypeSystem, ClassificationTypeService<MESObjectType, MESObjectTypeAttribute> {

    @Autowired
    private MESObjectTypeRepository objectTypeRepository;
    @Autowired
    private MESObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private MaterialTypeRepository materialTypeRepository;
    @Autowired
    private JigsFixtureTypeRepository jigsFixtureTypeRepository;
    @Autowired
    private ToolTypeRepository toolTypeRepository;
    @Autowired
    private WorkCenterTypeRepository workCenterTypeRepository;
    @Autowired
    private PlantTypeRepository plantTypeRepository;
    @Autowired
    private OperationTypeRepository operationTypeRepository;
    @Autowired
    private MachineTypeRepository machineTypeRepository;
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
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private MESPlantRepository plantRepository;
    @Autowired
    private MESObjectRepository mesObjectRepository;
    @Autowired
    private MESWorkCenterRepository workCenterRepository;
    @Autowired
    private MESMachineRepository machineRepository;
    @Autowired
    private MESEquipmentRepository equipmentRepository;
    @Autowired
    private MESInstrumentRepository instrumentRepository;
    @Autowired
    private MESToolRepository toolRepository;
    @Autowired
    private MESJigsFixtureRepository jigsFixtureRepository;
    @Autowired
    private MESMaterialRepository materialRepository;
    @Autowired
    private MESManpowerRepository manpowerRepository;
    @Autowired
    private ProductionOrderRepository productionOrderRepository;
    @Autowired
    private MESOperationRepository operationRepository;
    @Autowired
    private MESMBOMRepository mesmbomRepository;
    @Autowired
    private MROObjectTypeService mroObjectTypeService;
    @Autowired
    private MROSparePartTypeRepository mroSparePartTypeRepository;
    @Autowired
    private MROWorkRequestTypeRepository mroWorkRequestTypeRepository;
    @Autowired
    private MROWorkOrderTypeRepository mroWorkOrderTypeRepository;
    @Autowired
    private MROAssetTypeRepository assetTypeRepository;
    @Autowired
    private MROAssetRepository assetRepository;
    @Autowired
    private MROSparePartRepository sparePartRepository;
    @Autowired
    private MROWorkRequestRepository workRequestRepository;
    @Autowired
    private MROWorkOrderRepository workOrderRepository;
    @Autowired
    private MROMeterTypeRepository meterTypeRepository;
    @Autowired
    private MROMeterRepository meterRepository;
    @Autowired
    private SupplierTypeRepository supplierTypeRepository;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private MESObjectTypeService mesObjectTypeService;
    @Autowired
    private AssemblyLineTypeRepository assemblyLineTypeRepository;
    @Autowired
    private MESAssemblyLineRepository assemblyLineRepository;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private MESObjectPredicateBuilder objectPredicateBuilder;
    @Autowired
    private MESObjectRepository objectRepository;
    @Autowired
    private MESServiceOrderRepo serviceOrderRepo;
    @Autowired
    private MBOMTypeRepository mbomTypeRepository;
    @Autowired
    private BOPTypeRepository bopTypeRepository;
    @Autowired
    private MESBOPRepository mesbopRepository;
    @Autowired
    private MessageSource messageSource;

    @PostConstruct
    public void InitMESTypeService() {
        objectTypeService.registerTypeSystem("MESObjectType", new MESObjectTypeService());
    }

    @Override
    @PreAuthorize("hasPermission(#productionResourceType,'create')")
    public MESObjectType create(MESObjectType productionResourceType) {
        MESObjectType resourceType = objectTypeRepository.save(productionResourceType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.PRODUCTIONRESOURCETYPE, resourceType));
        return resourceType;
    }

    @Override
    @PreAuthorize("hasPermission(#resourceType.id ,'edit')")
    public MESObjectType update(MESObjectType resourceType) {
        MESObjectType productionResourceType = objectTypeRepository.findOne(resourceType.getId());
        return objectTypeRepository.save(productionResourceType);
    }

    @Override
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        objectTypeRepository.delete(id);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESObjectType get(Integer id) {
        return objectTypeRepository.findOne(id);
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESObjectType> getAll() {
        return objectTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESObjectType> findMultipleTypes(List<Integer> ids) {
        return objectTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<MESObjectType> getTypeTree() {
        List<MESObjectType> types = objectTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (MESObjectType type : types) {
            visitResourceTypeChildren(type);
        }
        return types;
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESObjectType> getRootTypes() {
        return objectTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
    }

    @Override
    public List<MESObjectType> getChildren(Integer parent) {
        return objectTypeRepository.findByParentTypeOrderByCreatedDateAsc(parent);
    }

    @Override
    public List<MESObjectType> getClassificationTree() {
        List<MESObjectType> types = getRootTypes();
        for (MESObjectType type : types) {
            visitResourceTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESObjectType> getTypeChildren(Integer id) {
        return objectTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitResourceTypeChildren(MESObjectType parent) {
        List<MESObjectType> childrens = getTypeChildren(parent.getId());
        for (MESObjectType child : childrens) {
            visitResourceTypeChildren(child);
        }
        parent.setChildren(childrens);
    }

    @Override
    public MESObjectTypeAttribute createAttribute(MESObjectTypeAttribute attribute) {
        List<MESObjectTypeAttribute> qualityTypeAttributes = objectTypeAttributeRepository.findByTypeOrderBySeq(attribute.getType());
        if (qualityTypeAttributes.size() >= 0) {
            attribute.setSeq(qualityTypeAttributes.size() + 1);
        }
        attribute = objectTypeAttributeRepository.save(attribute);
        MESObjectType mesObjectType = objectTypeRepository.findOne(attribute.getType());
        PLMObjectType objectType = getAttributeObjectType(attribute);
        if (attribute.getDataType().toString().equals("FORMULA")) {
            createFormulaAttribute(attribute, mesObjectType);
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(objectType, attribute));
        return attribute;

    }

    private void createFormulaAttribute(MESObjectTypeAttribute attribute, MESObjectType mesObjectType) {
        if (attribute.getObjectType().name().equals("PLANTTYPE")) {
            MESPlantType plantType = plantTypeRepository.findOne(mesObjectType.getId());
            List<MESPlant> plants = plantRepository.findByType(plantType);
            for (MESPlant plant : plants) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(plant.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("MACHINETYPE")) {
            MESMachineType machineType = machineTypeRepository.findOne(mesObjectType.getId());
            List<MESMachine> machines = machineRepository.findByType(machineType);
            for (MESMachine machine : machines) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(machine.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("MATERIALTYPE")) {
            MESMaterialType materialType = materialTypeRepository.findOne(mesObjectType.getId());
            List<MESMaterial> materials = materialRepository.findByType(materialType);
            for (MESMaterial material : materials) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(material.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("TOOLTYPE")) {
            MESToolType toolType = toolTypeRepository.findOne(mesObjectType.getId());
            List<MESTool> tools = toolRepository.findByType(toolType);
            for (MESTool tool : tools) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(tool.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("WORKCENTERTYPE")) {
            MESWorkCenterType workCenterType = workCenterTypeRepository.findOne(mesObjectType.getId());
            List<MESWorkCenter> workCenters = workCenterRepository.findByType(workCenterType);
            for (MESWorkCenter workCenter : workCenters) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(workCenter.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("JIGFIXTURETYPE")) {
            MESJigsFixtureType jigsFixtureType = jigsFixtureTypeRepository.findOne(mesObjectType.getId());
            List<MESJigsFixture> jigsFixtures = jigsFixtureRepository.findByType(jigsFixtureType);
            for (MESJigsFixture jigsFixture : jigsFixtures) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(jigsFixture.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("OPERATIONTYPE")) {
            MESOperationType operationType = operationTypeRepository.findOne(mesObjectType.getId());
            List<MESOperation> operations = operationRepository.findByType(operationType);
            for (MESOperation operation : operations) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(operation.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("MBOMTYPE")) {
            MESMBOMType mesmbomType = mbomTypeRepository.findOne(mesObjectType.getId());
            List<MESMBOM> mesmboms = mesmbomRepository.findByType(mesmbomType);
            for (MESMBOM mesmbom : mesmboms) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(mesmbom.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("BOPTYPE")) {
            MESBOPType mesbopType = bopTypeRepository.findOne(mesObjectType.getId());
            List<MESBOP> mesbops = mesbopRepository.findByType(mesbopType);
            for (MESBOP mesbop : mesbops) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(mesbop.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("PRODUCTIONORDERTYPE")) {
            MESProductionOrderType productionOrderType = productionOrderTypeRepository.findOne(mesObjectType.getId());
            List<MESProductionOrder> productionOrders = productionOrderRepository.findByType(productionOrderType);
            for (MESProductionOrder productionOrder : productionOrders) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(productionOrder.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("SERVICEORDERTYPE")) {
            MESServiceOrderType serviceOrderType = serviceOrderTypeRepository.findOne(mesObjectType.getId());
            List<MESServiceOrder> serviceOrders = serviceOrderRepo.findByType(serviceOrderType);
            for (MESServiceOrder serviceOrder : serviceOrders) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(serviceOrder.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("MANPOWERTYPE")) {
            MESManpowerType manpowerType = manpowerTypeRepository.findOne(mesObjectType.getId());
            List<MESManpower> manpowers = manpowerRepository.findByType(manpowerType);
            for (MESManpower manpower : manpowers) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(manpower.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("EQUIPMENTTYPE")) {
            MESEquipmentType equipmentType = equipmentTypeRepository.findOne(mesObjectType.getId());
            List<MESEquipment> equipments = equipmentRepository.findByType(equipmentType);
            for (MESEquipment equipment : equipments) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(equipment.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("INSTRUMENTTYPE")) {
            MESInstrumentType instrumentType = instrumentTypeRepository.findOne(mesObjectType.getId());
            List<MESInstrument> instruments = instrumentRepository.findByType(instrumentType);
            for (MESInstrument instrument : instruments) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(instrument.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("ASSEMBLYLINETYPE")) {
            MESAssemblyLineType assemblyLineType = assemblyLineTypeRepository.findOne(mesObjectType.getId());
            List<MESAssemblyLine> assemblyLines = assemblyLineRepository.findByType(assemblyLineType);
            for (MESAssemblyLine assemblyLine : assemblyLines) {
                MESObjectAttribute objectAttribute = new MESObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(assemblyLine.getId(), attribute.getId()));
                mesObjectAttributeRepository.save(objectAttribute);
            }
        }
    }

    private PLMObjectType getAttributeObjectType(MESObjectTypeAttribute attribute) {
        PLMObjectType objectType = null;
        if (attribute.getObjectType().name().equals("PLANTTYPE")) {
            objectType = PLMObjectType.PLANTTYPE;
        } else if (attribute.getObjectType().name().equals("MACHINETYPE")) {
            objectType = PLMObjectType.MACHINETYPE;
        } else if (attribute.getObjectType().name().equals("MATERIALTYPE")) {
            objectType = PLMObjectType.MATERIALTYPE;
        } else if (attribute.getObjectType().name().equals("TOOLTYPE")) {
            objectType = PLMObjectType.TOOLTYPE;
        } else if (attribute.getObjectType().name().equals("WORKCENTERTYPE")) {
            objectType = PLMObjectType.WORKCENTERTYPE;
        } else if (attribute.getObjectType().name().equals("JIGFIXTURETYPE")) {
            objectType = PLMObjectType.JIGFIXTURETYPE;
        } else if (attribute.getObjectType().name().equals("OPERATIONTYPE")) {
            objectType = PLMObjectType.OPERATIONTYPE;
        } else if (attribute.getObjectType().name().equals("MBOMTYPE")) {
            objectType = PLMObjectType.MBOMTYPE;
        } else if (attribute.getObjectType().name().equals("PRODUCTIONORDERTYPE")) {
            objectType = PLMObjectType.PRODUCTIONORDERTYPE;
        } else if (attribute.getObjectType().name().equals("SERVICEORDERTYPE")) {
            objectType = PLMObjectType.SERVICEORDERTYPE;
        } else if (attribute.getObjectType().name().equals("MANPOWERTYPE")) {
            objectType = PLMObjectType.MANPOWERTYPE;
        } else if (attribute.getObjectType().name().equals("EQUIPMENTTYPE")) {
            objectType = PLMObjectType.EQUIPMENTTYPE;
        } else if (attribute.getObjectType().name().equals("INSTRUMENTTYPE")) {
            objectType = PLMObjectType.INSTRUMENTTYPE;
        } else if (attribute.getObjectType().name().equals("ASSEMBLYLINETYPE")) {
            objectType = PLMObjectType.ASSEMBLYLINETYPE;
        } else if (attribute.getObjectType().name().equals("BOPTYPE")) {
            objectType = PLMObjectType.BOPTYPE;
        }
        return objectType;
    }

    @Override
    public MESObjectTypeAttribute updateAttribute(MESObjectTypeAttribute attribute) {
        MESObjectTypeAttribute plantTypeAttribute = objectTypeAttributeRepository.findOne(attribute.getId());
        PLMObjectType objectType = getAttributeObjectType(attribute);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(objectType, plantTypeAttribute, attribute));
        return objectTypeAttributeRepository.save(attribute);
    }

    @Override
    public void deleteAttribute(Integer id) {
        MESObjectTypeAttribute attribute = objectTypeAttributeRepository.findOne(id);
        List<MESObjectTypeAttribute> plantTypeAttributes = objectTypeAttributeRepository.findByTypeOrderBySeq(attribute.getType());
        if (plantTypeAttributes.size() > 0) {
            for (MESObjectTypeAttribute plantTypeAttribute : plantTypeAttributes) {
                if (plantTypeAttribute.getSeq() > attribute.getSeq()) {
                    plantTypeAttribute.setSeq(plantTypeAttribute.getSeq() - 1);
                    plantTypeAttribute = objectTypeAttributeRepository.save(plantTypeAttribute);
                }
            }
        }
        PLMObjectType objectType = getAttributeObjectType(attribute);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(objectType, attribute));
        objectTypeAttributeRepository.delete(id);
    }

    @Override
    public MESObjectTypeAttribute getAttribute(Integer id) {
        return objectTypeAttributeRepository.findOne(id);
    }


    @Transactional
    public MESObjectAttribute createMESObjectAttribute(MESObjectAttribute attribute) {
        return mesObjectAttributeRepository.save(attribute);
    }


    @Transactional
    public MESObjectAttribute updateMESObjectAttribute(MESObjectAttribute attribute) {
        MESObjectAttribute oldValue = mesObjectAttributeRepository.findByObjectAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, MESObjectAttribute.class);
        attribute = mesObjectAttributeRepository.save(attribute);
        MESObject mesObject = mesObjectRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new PlantEvents.PlantAttributesUpdatedEvent("mes", mesObject.getId(), mesObject.getObjectType(), oldValue, attribute));

        //applicationEventPublisher.publishEvent(new PlantEvents.PlantAttributesUpdatedEvent(pqmqcr, oldValue, attribute));
        return attribute;
    }

    @Transactional(readOnly = true)
    public List<MESObjectAttribute> getUsedMESObjectTypeAttributes(Integer attributeId) {
        return mesObjectAttributeRepository.findByAttributeDef(attributeId);
    }


    @Override
    @Transactional(readOnly = true)
    public List<MESObjectTypeAttribute> getAttributes(Integer typeId, Boolean hier) {
        List<MESObjectTypeAttribute> mesObjectTypeAttributes = new ArrayList<>();
        if (!hier) {
            mesObjectTypeAttributes = objectTypeAttributeRepository.findByTypeOrderBySeq(typeId);
            mesObjectTypeAttributes.forEach(mesObjectTypeAttribute -> {
                if (mesObjectTypeAttribute.getRefSubType() != null) {
                    mesObjectTypeAttribute.setRefSubTypeName(itemTypeService.getRefSubType(mesObjectTypeAttribute.getRefType().name(), mesObjectTypeAttribute.getRefSubType()));
                }
            });
        } else {
            mesObjectTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return mesObjectTypeAttributes;
    }

    @Transactional(readOnly = true)
    public List<MESObjectTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<MESObjectTypeAttribute> collector = new ArrayList<>();
        List<MESObjectTypeAttribute> atts = objectTypeAttributeRepository.findByTypeOrderBySeq(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<MESObjectTypeAttribute> collector, Integer typeId) {
        MESObjectType objectType = objectTypeRepository.findOne(typeId);
        if (objectType != null) {
            Integer parentType = objectType.getParentType();
            if (parentType != null) {
                List<MESObjectTypeAttribute> atts = objectTypeAttributeRepository.findByTypeOrderBySeq(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Transactional(readOnly = true)
    public Object getMESObjectTypeIdAndType(Integer id, PLMObjectType objectType) {
        Object object = null;
        if (objectType.equals(PLMObjectType.MATERIALTYPE)) {
            object = materialTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.JIGFIXTURETYPE)) {
            object = jigsFixtureTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.TOOLTYPE)) {
            object = toolTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.WORKCENTERTYPE)) {
            object = workCenterTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.MACHINETYPE)) {
            object = machineTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.PLANTTYPE)) {
            object = plantTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.PRODUCTIONORDERTYPE)) {
            object = productionOrderTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.SERVICEORDERTYPE)) {
            object = serviceOrderTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.OPERATIONTYPE)) {
            object = operationTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.MANPOWERTYPE)) {
            object = manpowerTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.EQUIPMENTTYPE)) {
            object = equipmentTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.INSTRUMENTTYPE)) {
            object = instrumentTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.SPAREPARTTYPE)) {
            object = mroSparePartTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.WORKREQUESTTYPE)) {
            object = mroWorkRequestTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.WORKORDERTYPE)) {
            object = mroWorkOrderTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.ASSETTYPE)) {
            object = assetTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.METERTYPE)) {
            object = meterTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.SUPPLIERTYPE)) {
            object = supplierTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.ASSEMBLYLINETYPE)) {
            object = assemblyLineTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.MBOMTYPE)) {
            object = mbomTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.BOPTYPE)) {
            object = bopTypeRepository.findOne(id);
        }
        return object;
    }


    @Transactional(readOnly = true)
    public Integer getObjectsByType(Integer id, PLMObjectType objectType) {
        Integer count = 0;
        if (objectType.equals(PLMObjectType.MATERIALTYPE)) {
            MESMaterialType materialType = materialTypeRepository.findOne(id);
            count = materialRepository.findByType(materialType).size();
        } else if (objectType.equals(PLMObjectType.JIGFIXTURETYPE)) {
            MESJigsFixtureType jigsFixtureType = jigsFixtureTypeRepository.findOne(id);
            count = jigsFixtureRepository.findByType(jigsFixtureType).size();
        } else if (objectType.equals(PLMObjectType.TOOLTYPE)) {
            MESToolType toolType = toolTypeRepository.findOne(id);
            count = toolRepository.findByType(toolType).size();
        } else if (objectType.equals(PLMObjectType.WORKCENTERTYPE)) {
            MESWorkCenterType workCenterType = workCenterTypeRepository.findOne(id);
            count = workCenterRepository.findByType(workCenterType).size();
        } else if (objectType.equals(PLMObjectType.MACHINETYPE)) {
            MESMachineType machineType = machineTypeRepository.findOne(id);
            count = machineRepository.findByType(machineType).size();
        } else if (objectType.equals(PLMObjectType.PLANTTYPE)) {
            MESPlantType plantType = plantTypeRepository.findOne(id);
            count = plantRepository.findByType(plantType).size();
        } else if (objectType.equals(PLMObjectType.ASSEMBLYLINETYPE)) {
            MESAssemblyLineType assemblyLineType = assemblyLineTypeRepository.findOne(id);
            count = assemblyLineRepository.findByType(assemblyLineType).size();
        } else if (objectType.equals(PLMObjectType.PRODUCTIONORDERTYPE)) {
            MESProductionOrderType productionOrderType = productionOrderTypeRepository.findOne(id);
            count = productionOrderRepository.findByType(productionOrderType).size();
        } else if (objectType.equals(PLMObjectType.OPERATIONTYPE)) {
            MESOperationType operationType = operationTypeRepository.findOne(id);
            count = operationRepository.findByType(operationType).size();
        } else if (objectType.equals(PLMObjectType.MBOMTYPE)) {
            MESMBOMType mesmbomType = mbomTypeRepository.findOne(id);
            count = mesmbomRepository.getMBOMCountByType(mesmbomType.getId());
        } else if (objectType.equals(PLMObjectType.BOPTYPE)) {
            MESBOPType mesbopType = bopTypeRepository.findOne(id);
            count = mesbopRepository.getBOPCountByType(mesbopType.getId());
        } else if (objectType.equals(PLMObjectType.MANPOWERTYPE)) {
            MESManpowerType manpowerType = manpowerTypeRepository.findOne(id);
            count = manpowerRepository.findByType(manpowerType).size();
        } else if (objectType.equals(PLMObjectType.EQUIPMENTTYPE)) {
            MESEquipmentType equipmentType = equipmentTypeRepository.findOne(id);
            count = equipmentRepository.findByType(equipmentType).size();
        } else if (objectType.equals(PLMObjectType.INSTRUMENTTYPE)) {
            MESInstrumentType instrumentType = instrumentTypeRepository.findOne(id);
            count = instrumentRepository.findByType(instrumentType).size();
        } else if (objectType.equals(PLMObjectType.SPAREPARTTYPE)) {
            MROSparePartType sparePartType = mroSparePartTypeRepository.findOne(id);
            count = sparePartRepository.findByType(sparePartType).size();
        } else if (objectType.equals(PLMObjectType.WORKREQUESTTYPE)) {
            MROWorkRequestType workRequestType = mroWorkRequestTypeRepository.findOne(id);
            count = workRequestRepository.findByType(workRequestType).size();
        } else if (objectType.equals(PLMObjectType.WORKORDERTYPE)) {
            MROWorkOrderType workOrderType = mroWorkOrderTypeRepository.findOne(id);
            count = workOrderRepository.findByType(workOrderType).size();
        } else if (objectType.equals(PLMObjectType.ASSETTYPE)) {
            MROAssetType assetType = assetTypeRepository.findOne(id);
            count = assetRepository.findByType(assetType).size();
        } else if (objectType.equals(PLMObjectType.METERTYPE)) {
            MROMeterType meterType = meterTypeRepository.findOne(id);
            count = meterRepository.findByType(meterType).size();
        }
        return count;
    }

		/*-------------------------   Material Types ----------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#materialType,'create')")
    public MESMaterialType createMaterialType(MESMaterialType materialType) {
        MESMaterialType mesMaterialType = materialTypeRepository.save(materialType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.MATERIALTYPE, mesMaterialType));
        return mesMaterialType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#materialType,'create')")
    public MESMaterialType updateMaterialType(Integer id, MESMaterialType materialType) {
        MESMaterialType oldQualityType = materialTypeRepository.findOne(materialType.getId());
        MESMaterialType existingMesType;
        if (materialType.getParentType() == null) {
            existingMesType = materialTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(materialType.getName());
        } else {
            existingMesType = materialTypeRepository.findByNameEqualsIgnoreCaseAndParentType(materialType.getName(), materialType.getParentType());
        }
        if ( existingMesType != null && !materialType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.MATERIALTYPE, oldQualityType, materialType));
        return materialTypeRepository.save(materialType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteMaterialType(Integer id) {
        materialTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public MESMaterialType getMaterialType(Integer id) {
        return materialTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMaterialType> getAllMaterialTypes() {
        return materialTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMaterialType> findMultipleMaterialTypes(List<Integer> ids) {
        return materialTypeRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMaterialType> getMaterialTypeTree() {
        List<MESMaterialType> types = materialTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESMaterialType type : types) {
            visitMaterialTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESMaterialType> getMaterialTypeChildren(Integer id) {
        return materialTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitMaterialTypeChildren(MESMaterialType parent) {
        List<MESMaterialType> childrens = getMaterialTypeChildren(parent.getId());
        for (MESMaterialType child : childrens) {
            visitMaterialTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    	/*-------------------------  Tool Types ----------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#toolType,'create')")
    public MESToolType createToolType(MESToolType toolType) {
        MESToolType toolType1 = toolTypeRepository.save(toolType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.TOOLTYPE, toolType));
        return toolType1;
    }

    @Transactional
    @PreAuthorize("hasPermission(#toolType.id ,'edit')")
    public MESToolType updateToolType(Integer id, MESToolType toolType) {
        MESToolType oldToolType = toolTypeRepository.findOne(toolType.getId());
        MESToolType existingMesType;
        if (toolType.getParentType() == null) {
            existingMesType = toolTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(toolType.getName());
        } else {
            existingMesType = toolTypeRepository.findByNameEqualsIgnoreCaseAndParentType(toolType.getName(), toolType.getParentType());
        }
        if ( existingMesType != null && !toolType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.TOOLTYPE, oldToolType, toolType));
        return toolTypeRepository.save(toolType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteToolType(Integer id) {
        toolTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESToolType getToolType(Integer id) {
        return toolTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESToolType> getAllToolTypes() {
        return toolTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESToolType> findMultipleToolTypes(List<Integer> ids) {
        return toolTypeRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESToolType> getToolTypeTree() {
        List<MESToolType> types = toolTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESToolType type : types) {
            visitToolTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESToolType> getToolTypeChildren(Integer id) {
        return toolTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitToolTypeChildren(MESToolType parent) {
        List<MESToolType> childrens = getToolTypeChildren(parent.getId());
        for (MESToolType child : childrens) {
            visitToolTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    	/*-------------------------  JigsFixtures Types ----------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#jigsFixtureType,'create')")
    public MESJigsFixtureType createJigsFixType(MESJigsFixtureType jigsFixtureType) {
        MESJigsFixtureType jigsFixtureType1 = jigsFixtureTypeRepository.save(jigsFixtureType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.JIGFIXTURETYPE, jigsFixtureType));
        return jigsFixtureType1;
    }

    @Transactional
    @PreAuthorize("hasPermission(#jigsFixtureType.id ,'edit')")
    public MESJigsFixtureType updateJigsFixType(Integer id, MESJigsFixtureType jigsFixtureType) {
        MESJigsFixtureType oldJigsFixType = jigsFixtureTypeRepository.findOne(jigsFixtureType.getId());
        MESJigsFixtureType existingMesType;
        if (jigsFixtureType.getParentType() == null) {
            existingMesType = jigsFixtureTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(jigsFixtureType.getName());
        } else {
            existingMesType = jigsFixtureTypeRepository.findByNameEqualsIgnoreCaseAndParentType(jigsFixtureType.getName(), jigsFixtureType.getParentType());
        }
        if ( existingMesType != null && !jigsFixtureType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.JIGFIXTURETYPE, oldJigsFixType, jigsFixtureType));
        return jigsFixtureTypeRepository.save(jigsFixtureType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteJigsFixType(Integer id) {
        jigsFixtureTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESJigsFixtureType getJigsFixType(Integer id) {
        return jigsFixtureTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESJigsFixtureType> getAllJigsFixTypes() {
        return jigsFixtureTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MESJigsFixtureType> findMultipleJigsFixTypes(List<Integer> ids) {
        return jigsFixtureTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESJigsFixtureType> getJigsFixTypeTree() {
        List<MESJigsFixtureType> types = jigsFixtureTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESJigsFixtureType type : types) {
            visitJigsFixTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESJigsFixtureType> getJigsFixTypeChildren(Integer id) {
        return jigsFixtureTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitJigsFixTypeChildren(MESJigsFixtureType parent) {
        List<MESJigsFixtureType> childrens = getJigsFixTypeChildren(parent.getId());
        for (MESJigsFixtureType child : childrens) {
            visitJigsFixTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }


    /*-------------------------- WorkCenterType -------------------- */
    @Transactional
    @PreAuthorize("hasPermission(#workCenterType,'create')")
    public MESWorkCenterType createWorkCenter(MESWorkCenterType workCenterType) {
        MESWorkCenterType centerType = workCenterTypeRepository.save(workCenterType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.WORKCENTERTYPE, centerType));
        return centerType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#workCenterType.id ,'edit')")
    public MESWorkCenterType updateWorkCenterType(Integer id, MESWorkCenterType workCenterType) {
        MESWorkCenterType oldWorkCenterType = workCenterTypeRepository.findOne(id);
        MESWorkCenterType existingMesType;
        if (workCenterType.getParentType() == null) {
            existingMesType = workCenterTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(workCenterType.getName());
        } else {
            existingMesType = workCenterTypeRepository.findByNameEqualsIgnoreCaseAndParentType(workCenterType.getName(), workCenterType.getParentType());
        }
        if ( existingMesType != null && !workCenterType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.WORKCENTERTYPE, oldWorkCenterType, workCenterType));
        return workCenterTypeRepository.save(workCenterType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteWorkCenterType(Integer id) {
        workCenterTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESWorkCenterType getWorkCenterType(Integer id) {
        return workCenterTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESWorkCenterType> getAllWorkCenterType() {
        return workCenterTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESWorkCenterType> findMultipleWorkCenterTypes(List<Integer> ids) {
        return workCenterTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESWorkCenterType> getWorkCenterTypeTree() {
        List<MESWorkCenterType> types = workCenterTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESWorkCenterType type : types) {
            visitWorkCenterTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESWorkCenterType> getWorkCenterTypeChildren(Integer id) {
        return workCenterTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitWorkCenterTypeChildren(MESWorkCenterType parent) {
        List<MESWorkCenterType> childrens = getWorkCenterTypeChildren(parent.getId());
        for (MESWorkCenterType child : childrens) {
            visitWorkCenterTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }


    /* -------------------- plantType -------------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#plantType,'create')")
    public MESPlantType createPlantType(MESPlantType plantType) {
        MESPlantType mesPlantType = plantTypeRepository.save(plantType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.PLANTTYPE, mesPlantType));
        return mesPlantType;
    }


    @Transactional
    @PreAuthorize("hasPermission(#plantType.id ,'edit')")
    public MESPlantType updatePlantType(Integer id, MESPlantType plantType) {
        MESPlantType oldPlantType = plantTypeRepository.findOne(id);
        MESPlantType existingMesType;
        if (plantType.getParentType() == null) {
            existingMesType = plantTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(plantType.getName());
        } else {
            existingMesType = plantTypeRepository.findByNameEqualsIgnoreCaseAndParentType(plantType.getName(), plantType.getParentType());
        }
        if ( existingMesType != null && !plantType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.PLANTTYPE, oldPlantType, plantType));
        return plantTypeRepository.save(plantType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deletePlantType(Integer id) {
        plantTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESPlantType getPlantType(Integer id) {
        return plantTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESPlantType> getAllPlantType() {
        return plantTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESPlantType> findMultiplePlantTypes(List<Integer> ids) {
        return plantTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESPlantType> getPlantTypeTree() {
        List<MESPlantType> types = plantTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESPlantType type : types) {
            visitPlantTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESPlantType> getPlantTypeChildren(Integer id) {
        return plantTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitPlantTypeChildren(MESPlantType parent) {
        List<MESPlantType> childrens = getPlantTypeChildren(parent.getId());
        for (MESPlantType child : childrens) {
            visitPlantTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }


    /* ------------------------- opreation --------------------*/
    @Transactional
    @PreAuthorize("hasPermission(#operationType,'create')")
    public MESOperationType createOperationType(MESOperationType operationType) {
        MESOperationType mesOperationType = operationTypeRepository.save(operationType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.OPERATIONTYPE, mesOperationType));
        return operationTypeRepository.save(mesOperationType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#mesOperationType.id ,'edit')")
    public MESOperationType updateOperationType(Integer id, MESOperationType mesOperationType) {
        MESOperationType oldOperationType = operationTypeRepository.findOne(id);
        MESOperationType existingMesType;
        if (mesOperationType.getParentType() == null) {
            existingMesType = operationTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(mesOperationType.getName());
        } else {
            existingMesType = operationTypeRepository.findByNameEqualsIgnoreCaseAndParentType( mesOperationType.getName(), mesOperationType.getParentType());
        }
        if ( existingMesType != null && !mesOperationType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.OPERATIONTYPE, oldOperationType, mesOperationType));
        return operationTypeRepository.save(mesOperationType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteOperationType(Integer id) {
        operationTypeRepository.delete(id);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public MESOperationType getOperationType(Integer id) {
        return operationTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESOperationType> getAllOperationType() {
        return operationTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESOperationType> findMultipleOperationTypes(List<Integer> ids) {
        return operationTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESOperationType> getOperationTypeTree() {
        List<MESOperationType> types = operationTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESOperationType type : types) {
            visitOperationTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESOperationType> getOperationTypeChildren(Integer id) {
        return operationTypeRepository.findByParentTypeOrderByIdAsc(id);
    }


    private void visitOperationTypeChildren(MESOperationType parent) {
        List<MESOperationType> childrens = getOperationTypeChildren(parent.getId());
        for (MESOperationType child : childrens) {
            visitOperationTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    /* ----------------------------- mesObjectTypesTree -----------------------*/

    @Transactional
    public List<MESObjectType> getObjectRootTypes() {
        return objectTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESObjectType> getMesObjectTypeTree() {
        List<MESObjectType> types = getObjectRootTypes();
        for (MESObjectType objectType : types) {
            visitObjectTypeChildren(objectType);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESObjectType> getObjectTypeChildren(Integer id) {
        return objectTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitObjectTypeChildren(MESObjectType parent) {
        List<MESObjectType> childrens = getObjectTypeChildren(parent.getId());
        for (MESObjectType child : childrens) {
            visitObjectTypeChildren(child);
        }
        parent.setChildren(childrens);
    }

    /*------------------------ machine type --------------*/
    @Transactional
    @PreAuthorize("hasPermission(#machineType,'create')")
    public MESMachineType createMachineType(MESMachineType machineType) {
        MESMachineType mesMachineType = machineTypeRepository.save(machineType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.MACHINETYPE, mesMachineType));
        return mesMachineType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#machineType.id ,'edit')")
    public MESMachineType updateMachineType(Integer id, MESMachineType machineType) {
        MESMachineType oldmachineType = machineTypeRepository.findOne(id);
        MESMachineType existingMesType;
        if (machineType.getParentType() == null) {
            existingMesType = machineTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(machineType.getName());
        } else {
            existingMesType = machineTypeRepository.findByNameEqualsIgnoreCaseAndParentType( machineType.getName(), machineType.getParentType());
        }
        if ( existingMesType != null && !machineType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.MACHINETYPE, oldmachineType, machineType));
        return machineTypeRepository.save(machineType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteMachineType(Integer id) {
        machineTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESMachineType getMachineType(Integer id) {
        return machineTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMachineType> getAllMachineTypes() {
        return machineTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMachineType> findMultipleMachineTypes(List<Integer> ids) {
        return machineTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMachineType> getMachineTypeTree() {
        List<MESMachineType> types = machineTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESMachineType type : types) {
            visitMachineTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESMachineType> getMachineTypeChildren(Integer id) {
        return machineTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitMachineTypeChildren(MESMachineType parent) {
        List<MESMachineType> childrens = getMachineTypeChildren(parent.getId());
        for (MESMachineType child : childrens) {
            visitMachineTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }


    /*------------------------ ProductionOrder type --------------*/
    @Transactional
    @PreAuthorize("hasPermission(#productionOrderType,'create')")
    public MESProductionOrderType createProductionOrderType(MESProductionOrderType productionOrderType) {
        MESProductionOrderType productionOrderType1 = productionOrderTypeRepository.save(productionOrderType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.PRODUCTIONORDERTYPE, productionOrderType1));
        return productionOrderType1;
    }

    @Transactional
    @PreAuthorize("hasPermission(#productionOrderType.id ,'edit')")
    public MESProductionOrderType updateProductionOrderType(Integer id, MESProductionOrderType productionOrderType) {
        MESProductionOrderType oldmachineType = productionOrderTypeRepository.findOne(id);
        MESProductionOrderType existingMesType;
        if (productionOrderType.getParentType() == null) {
            existingMesType = productionOrderTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(productionOrderType.getName());
        } else {
            existingMesType = productionOrderTypeRepository.findByNameEqualsIgnoreCaseAndParentType( productionOrderType.getName(), productionOrderType.getParentType());
        }
        if ( existingMesType != null && !productionOrderType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.PRODUCTIONORDERTYPE, oldmachineType, productionOrderType));
        return productionOrderTypeRepository.save(productionOrderType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteProductionOrderType(Integer id) {
        productionOrderTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESProductionOrderType getProductionOrderType(Integer id) {
        return productionOrderTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESProductionOrderType> getAllProductionOrderTypes() {
        return productionOrderTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESProductionOrderType> findMultipleProductionOrderTypes(List<Integer> ids) {
        return productionOrderTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESProductionOrderType> getProductionOrderTypeTree() {
        List<MESProductionOrderType> types = productionOrderTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESProductionOrderType type : types) {
            visitProductionTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESProductionOrderType> getProductionTypeChildren(Integer id) {
        return productionOrderTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitProductionTypeChildren(MESProductionOrderType parent) {
        List<MESProductionOrderType> childrens = getProductionTypeChildren(parent.getId());
        for (MESProductionOrderType child : childrens) {
            visitProductionTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    /*------------------------ ServiceOrder type --------------*/
    @Transactional
    @PreAuthorize("hasPermission(#machineType,'create')")
    public MESServiceOrderType createServiceOrderType(MESServiceOrderType machineType) {
        MESServiceOrderType mesMachineType = serviceOrderTypeRepository.save(machineType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.SERVICEORDERTYPE, mesMachineType));
        return mesMachineType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#machineType.id ,'edit')")
    public MESServiceOrderType updateServiceOrderType(Integer id, MESServiceOrderType machineType) {
        MESServiceOrderType oldmachineType = serviceOrderTypeRepository.findOne(id);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.SERVICEORDERTYPE, oldmachineType, machineType));
        return serviceOrderTypeRepository.save(machineType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteServiceOrderType(Integer id) {
        serviceOrderTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESServiceOrderType getServiceOrderType(Integer id) {
        return serviceOrderTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESServiceOrderType> getAllServiceOrderTypes() {
        return serviceOrderTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESServiceOrderType> getMultipleServiceOrderTypes(List<Integer> ids) {
        return serviceOrderTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESServiceOrderType> getServiceOrderTypeTree() {
        List<MESServiceOrderType> types = serviceOrderTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (MESServiceOrderType type : types) {
            visitServiceOrderTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESServiceOrderType> getServiceOrderTypeChildren(Integer id) {
        return serviceOrderTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitServiceOrderTypeChildren(MESServiceOrderType parent) {
        List<MESServiceOrderType> childrens = getServiceOrderTypeChildren(parent.getId());
        for (MESServiceOrderType child : childrens) {
            visitServiceOrderTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    /*------------------------ ManPower type --------------*/
    @Transactional
    @PreAuthorize("hasPermission(#manPowerType,'create')")
    public MESManpowerType createManpowerType(MESManpowerType manPowerType) {
        MESManpowerType mesManpowerType = manpowerTypeRepository.save(manPowerType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.MANPOWERTYPE, mesManpowerType));
        return mesManpowerType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#manPowerType.id ,'edit')")
    public MESManpowerType updateManpowerType(Integer id, MESManpowerType manPowerType) {
        MESManpowerType oldmanPowerType = manpowerTypeRepository.findOne(id);
        MESManpowerType existingMesType;
        if (manPowerType.getParentType() == null) {
            existingMesType = manpowerTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(manPowerType.getName());
        } else {
            existingMesType = manpowerTypeRepository.findByNameEqualsIgnoreCaseAndParentType( manPowerType.getName(), manPowerType.getParentType());
        }
        if ( existingMesType != null && !manPowerType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.MANPOWERTYPE, oldmanPowerType, manPowerType));
        return manpowerTypeRepository.save(manPowerType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteManpowerType(Integer id) {
        manpowerTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESManpowerType getManpowerType(Integer id) {
        return manpowerTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESManpowerType> getAllManpowerTypes() {
        return manpowerTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESManpowerType> findMultipleManpowerTypes(List<Integer> ids) {
        return manpowerTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESManpowerType> getManpowerTypeTree() {
        List<MESManpowerType> types = manpowerTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESManpowerType type : types) {
            visitManpowerTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESManpowerType> getManpowerTypeChildren(Integer id) {
        return manpowerTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitManpowerTypeChildren(MESManpowerType parent) {
        List<MESManpowerType> childrens = getManpowerTypeChildren(parent.getId());
        for (MESManpowerType child : childrens) {
            visitManpowerTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    /*------------------------ equipment type --------------*/
    @Transactional
    @PreAuthorize("hasPermission(#equipmentType,'create')")
    public MESEquipmentType createEquipmentType(MESEquipmentType equipmentType) {
        MESEquipmentType equipmentType1 = equipmentTypeRepository.save(equipmentType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.EQUIPMENTTYPE, equipmentType1));
        return equipmentType1;
    }

    @Transactional
    @PreAuthorize("hasPermission(#equipmentType.id ,'edit')")
    public MESEquipmentType updateEquipmentType(Integer id, MESEquipmentType equipmentType) {
        MESEquipmentType oldEquipmentType = equipmentTypeRepository.findOne(id);
        MESEquipmentType existingMesType;
        if (equipmentType.getParentType() == null) {
            existingMesType = equipmentTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(equipmentType.getName());
        } else {
            existingMesType = equipmentTypeRepository.findByNameEqualsIgnoreCaseAndParentType( equipmentType.getName(), equipmentType.getParentType());
        }
        if ( existingMesType != null && !equipmentType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.EQUIPMENTTYPE, oldEquipmentType, equipmentType));
        return equipmentTypeRepository.save(equipmentType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteEquipmentType(Integer id) {
        equipmentTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESEquipmentType getEquipmentType(Integer id) {
        return equipmentTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESEquipmentType> getAllEquipmentTypes() {
        return equipmentTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESEquipmentType> findMultipleEquipmentTypes(List<Integer> ids) {
        return equipmentTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESEquipmentType> getEquipmentTypeTree() {
        List<MESEquipmentType> types = equipmentTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESEquipmentType type : types) {
            visitEquipmentTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESEquipmentType> getEquipmentTypeChildren(Integer id) {
        return equipmentTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitEquipmentTypeChildren(MESEquipmentType parent) {
        List<MESEquipmentType> childrens = getEquipmentTypeChildren(parent.getId());
        for (MESEquipmentType child : childrens) {
            visitEquipmentTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    /*------------------------ Instrument type --------------*/
    @Transactional
    @PreAuthorize("hasPermission(#instrumentType,'create')")
    public MESInstrumentType createInstrumentType(MESInstrumentType instrumentType) {
        MESInstrumentType instrumentType1 = instrumentTypeRepository.save(instrumentType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.INSTRUMENTTYPE, instrumentType1));
        return instrumentType1;
    }

    @Transactional
    @PreAuthorize("hasPermission(#instrumentType.id ,'edit')")
    public MESInstrumentType updateInstrumentType(Integer id, MESInstrumentType instrumentType) {
        MESInstrumentType oldInstrumentType = instrumentTypeRepository.findOne(id);
        MESInstrumentType existingMesType;
        if (instrumentType.getParentType() == null) {
            existingMesType = instrumentTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(instrumentType.getName());
        } else {
            existingMesType = instrumentTypeRepository.findByNameEqualsIgnoreCaseAndParentType( instrumentType.getName(), instrumentType.getParentType());
        }
        if ( existingMesType != null && !instrumentType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.INSTRUMENTTYPE, oldInstrumentType, instrumentType));
        return instrumentTypeRepository.save(instrumentType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteInstrumentType(Integer id) {
        instrumentTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESInstrumentType getInstrumentType(Integer id) {
        return instrumentTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESInstrumentType> getAllInstrumentTypes() {
        return instrumentTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESInstrumentType> findMultipleInstrumentTypes(List<Integer> ids) {
        return instrumentTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESInstrumentType> getInstrumentTypeTree() {
        List<MESInstrumentType> types = instrumentTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESInstrumentType type : types) {
            visitInstrumentTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESInstrumentType> getInstrumentTypeChildren(Integer id) {
        return instrumentTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitInstrumentTypeChildren(MESInstrumentType parent) {
        List<MESInstrumentType> childrens = getInstrumentTypeChildren(parent.getId());
        for (MESInstrumentType child : childrens) {
            visitInstrumentTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    public MESObjectTypesDto getAllObjectTypesTree() {
        MESObjectTypesDto mesObjectTypesDto = new MESObjectTypesDto();
        mesObjectTypesDto.getPlantTypes().addAll(mesObjectTypeService.getPlantTypeTree());
        mesObjectTypesDto.getWorkCenterTypes().addAll(mesObjectTypeService.getWorkCenterTypeTree());
        mesObjectTypesDto.getMachineTypes().addAll(mesObjectTypeService.getMachineTypeTree());
        mesObjectTypesDto.getEquipmentTypes().addAll(mesObjectTypeService.getEquipmentTypeTree());
        mesObjectTypesDto.getInstrumentTypes().addAll(mesObjectTypeService.getInstrumentTypeTree());
        mesObjectTypesDto.getToolTypes().addAll(mesObjectTypeService.getToolTypeTree());
        mesObjectTypesDto.getJigsFixtureTypes().addAll(mesObjectTypeService.getJigsFixTypeTree());
        mesObjectTypesDto.getMaterialTypes().addAll(mesObjectTypeService.getMaterialTypeTree());
        mesObjectTypesDto.getManpowerTypes().addAll(mesObjectTypeService.getManpowerTypeTree());
        mesObjectTypesDto.getOperationTypes().addAll(mesObjectTypeService.getOperationTypeTree());
        mesObjectTypesDto.getProductionOrderTypes().addAll(mesObjectTypeService.getProductionOrderTypeTree());
        mesObjectTypesDto.getAssemblyLineTypes().addAll(mesObjectTypeService.getAssemblyLineTypeTree());
        mesObjectTypesDto.getMbomTypes().addAll(mesObjectTypeService.getMBOMTypeTree());
        mesObjectTypesDto.getBopTypes().addAll(mesObjectTypeService.getBOPTypeTree());
        return mesObjectTypesDto;
    }

    @Override
    public Boolean isSubtypeOf(String s1, Object s2, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        objectTypeRepository = webApplicationContext.getBean(MESObjectTypeRepository.class);
        MESObjectType mesObjectType = (MESObjectType) s2;
        if (subTypeId != null && checkWithId(mesObjectType, subTypeId)) {
            return true;
        }
        if (mesObjectType.getName().equalsIgnoreCase(s1)) {
            return true;
        } else {
            if (mesObjectType.getParentType() != null)
                return compareWithParent(objectTypeRepository.findOne(mesObjectType.getParentType()), s1);
        }
        return false;
    }

    public Boolean checkWithId(MESObjectType plmItemType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        objectTypeRepository = webApplicationContext.getBean(MESObjectTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmItemType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = checkWithId(objectTypeRepository.findOne(plmItemType.getParentType()), typeId);
        }
        return flag;
    }

    private Boolean compareWithParent(MESObjectType mesObjectType, String s1) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        objectTypeRepository = webApplicationContext.getBean(MESObjectTypeRepository.class);
        Boolean flag = false;
        if (mesObjectType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (mesObjectType.getParentType() != null)
                flag = compareWithParent(objectTypeRepository.findOne(mesObjectType.getParentType()), s1);
        }
        return flag;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MESObjectTypeAttribute> getTypeAttributes(Integer typeId, Boolean hier) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        objectTypeAttributeRepository = webApplicationContext.getBean(MESObjectTypeAttributeRepository.class);
        objectTypeRepository = webApplicationContext.getBean(MESObjectTypeRepository.class);
        List<MESObjectTypeAttribute> mesObjectTypeAttributes = new ArrayList<>();
        if (!hier) {
            mesObjectTypeAttributes = objectTypeAttributeRepository.findByTypeOrderBySeq(typeId);
        } else {
            mesObjectTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return mesObjectTypeAttributes;
    }

    /*------------------------ AssemblyLine type --------------*/
    @Transactional
    @PreAuthorize("hasPermission(#assemblyLineType,'create')")
    public MESAssemblyLineType createAssemblyLineType(MESAssemblyLineType assemblyLineType) {
        MESAssemblyLineType assemblyLineType1 = assemblyLineTypeRepository.save(assemblyLineType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.ASSEMBLYLINETYPE, assemblyLineType1));
        return assemblyLineType1;
    }

    @Transactional
    @PreAuthorize("hasPermission(#assemblyLineType.id ,'edit')")
    public MESAssemblyLineType updateAssemblyLineType(Integer id, MESAssemblyLineType assemblyLineType) {
        MESAssemblyLineType oldAssemblyLineType = assemblyLineTypeRepository.findOne(id);
        MESAssemblyLineType existingMesType;
        if (assemblyLineType.getParentType() == null) {
            existingMesType = assemblyLineTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(assemblyLineType.getName());
        } else {
            existingMesType = assemblyLineTypeRepository.findByNameEqualsIgnoreCaseAndParentType(assemblyLineType.getName(), assemblyLineType.getParentType());
        }
        if ( existingMesType != null && !assemblyLineType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.ASSEMBLYLINETYPE, oldAssemblyLineType, assemblyLineType));
        return assemblyLineTypeRepository.save(assemblyLineType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteAssemblyLineType(Integer id) {
        assemblyLineTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESAssemblyLineType getAssemblyLineType(Integer id) {
        return assemblyLineTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESAssemblyLineType> getAllAssemblyLineTypes() {
        return assemblyLineTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESAssemblyLineType> findMultipleAssemblyLineTypes(List<Integer> ids) {
        return assemblyLineTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESAssemblyLineType> getAssemblyLineTypeTree() {
        List<MESAssemblyLineType> types = assemblyLineTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESAssemblyLineType type : types) {
            visitAssemblyLineTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESAssemblyLineType> getAssemblyLineTypeChildren(Integer id) {
        return assemblyLineTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitAssemblyLineTypeChildren(MESAssemblyLineType parent) {
        List<MESAssemblyLineType> childrens = getAssemblyLineTypeChildren(parent.getId());
        for (MESAssemblyLineType child : childrens) {
            visitAssemblyLineTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    @Transactional(readOnly = true)
    public Page<MESObject> getMESObjects(Pageable pageable, MESObjectCriteria mesObjectCriteria) {
        Predicate predicate = objectPredicateBuilder.getDefaultPredicates(mesObjectCriteria, QMESObject.mESObject);
        return mesObjectRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public Object getMESObjectById(Integer id) {
        return mesObjectRepository.findOne(id);
    }

    /*------------------------ MBOM type --------------*/
    @Transactional
    @PreAuthorize("hasPermission(#mbomType,'create')")
    public MESMBOMType createMBOMType(MESMBOMType mbomType) {
        MESMBOMType mbomType1 = mbomTypeRepository.save(mbomType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.MBOMTYPE, mbomType1));
        return mbomType1;
    }

    @Transactional
    @PreAuthorize("hasPermission(#mbomType.id ,'edit')")
    public MESMBOMType updateMBOMType(Integer id, MESMBOMType mbomType) {
        MESMBOMType oldMBOMType = mbomTypeRepository.findOne(id);
        MESMBOMType existingMesType;
        if (mbomType.getParentType() == null) {
            existingMesType = mbomTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(mbomType.getName());
        } else {
            existingMesType = mbomTypeRepository.findByNameEqualsIgnoreCaseAndParentType(mbomType.getName(), mbomType.getParentType());
        }
        if ( existingMesType != null && !mbomType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.MBOMTYPE, oldMBOMType, mbomType));
        return mbomTypeRepository.save(mbomType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteMBOMType(Integer id) {
        mbomTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESMBOMType getMBOMType(Integer id) {
        return mbomTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMBOMType> getAllMBOMTypes() {
        return mbomTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMBOMType> findMultipleMBOMTypes(List<Integer> ids) {
        return mbomTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMBOMType> getMBOMTypeTree() {
        List<MESMBOMType> types = mbomTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESMBOMType type : types) {
            visitMBOMTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESMBOMType> getMBOMTypeChildren(Integer id) {
        return mbomTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitMBOMTypeChildren(MESMBOMType parent) {
        List<MESMBOMType> childrens = getMBOMTypeChildren(parent.getId());
        for (MESMBOMType child : childrens) {
            visitMBOMTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    /*------------------------ BOP type --------------*/
    @Transactional
    @PreAuthorize("hasPermission(#bopType,'create')")
    public MESBOPType createBOPType(MESBOPType bopType) {
        MESBOPType bopType1 =bopTypeRepository.save(bopType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.BOPTYPE, bopType1));
        return bopType1;
    }
    @Transactional
    @PreAuthorize("hasPermission(#bopType.id ,'edit')")
    public MESBOPType updateBOPType(Integer id, MESBOPType bopType) {
        MESBOPType oldBOPType = bopTypeRepository.findOne(id);
        MESBOPType existingMesType;
        if (bopType.getParentType() == null) {
            existingMesType = bopTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(bopType.getName());
        } else {
            existingMesType = bopTypeRepository.findByNameEqualsIgnoreCaseAndParentType(bopType.getName(), bopType.getParentType());
        }
        if ( existingMesType != null && !bopType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.BOPTYPE, oldBOPType, bopType));
        return bopTypeRepository.save(bopType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteBOPType(Integer id) {
        bopTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESBOPType getBOPType(Integer id) {
        return bopTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESBOPType> getAllBOPTypes() {
        return bopTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESBOPType> findMultipleBOPTypes(List<Integer> ids) {
        return bopTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESBOPType> getBOPTypeTree() {
        List<MESBOPType> types = bopTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MESBOPType type : types) {
            visitBOPTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MESBOPType> getBOPTypeChildren(Integer id) {
        return bopTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitBOPTypeChildren(MESBOPType parent) {
        List<MESBOPType> childrens = getBOPTypeChildren(parent.getId());
        for (MESBOPType child : childrens) {
            visitBOPTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    @Transactional(readOnly = true)
    public List<PLMLifeCycle> getMBOMTypeLifecycles() {
        List<PLMLifeCycle> plmLifeCycles = mbomTypeRepository.getUniqueItemTypeLifeCycles();
        return plmLifeCycles;
    }
}

