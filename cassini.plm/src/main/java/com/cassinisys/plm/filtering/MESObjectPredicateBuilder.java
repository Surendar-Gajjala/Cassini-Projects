package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mes.*;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MESObjectPredicateBuilder implements PredicateBuilder<MESObjectCriteria, QMESObject> {

    @Autowired
    private MESObjectTypeRepository mesObjectTypeRepository;
    @Autowired
    private PlantTypeRepository plantTypeRepository;
    @Autowired
    private AssemblyLineTypeRepository assemblyLineTypeRepository;
    @Autowired
    private MESPlantRepository plantRepo;
    @Autowired
    private MESAssemblyLineRepository assemblyLineRepository;
    @Autowired
    private MaterialTypeRepository materialTypeRepository;
    @Autowired
    private JigsFixtureTypeRepository jigsFixtureTypeRepository;
    @Autowired
    private ToolTypeRepository toolTypeRepository;
    @Autowired
    private WorkCenterTypeRepository workCenterTypeRepository;
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

    @Override
    public Predicate build(MESObjectCriteria mesObjectCriteria, QMESObject pathBase) {
        return getDefaultPredicate(mesObjectCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(MESObjectCriteria criteria, QMESObject pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.number.containsIgnoreCase(s).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }

        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }

        return ExpressionUtils.allOf(predicates);
    }

    public Predicate getDefaultPredicates(MESObjectCriteria criteria, QMESObject pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.number.containsIgnoreCase(s).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getType())) {
            MESObjectType objectType = mesObjectTypeRepository.findOne(criteria.getType());
            List<Integer> ids = new ArrayList<>();
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PLANTTYPE.toString()))) {
                MESPlantType type = plantTypeRepository.findOne(objectType.getId());
                List<MESPlant> plants = plantRepo.findByType(type);
                plants.forEach(plant -> {
                    ids.add(plant.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ASSEMBLYLINETYPE.toString()))) {
                MESAssemblyLineType type = assemblyLineTypeRepository.findOne(objectType.getId());
                List<MESAssemblyLine> assemblyLines = assemblyLineRepository.findByType(type);
                assemblyLines.forEach(assemblyLine -> {
                    ids.add(assemblyLine.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.WORKCENTERTYPE.toString()))) {
                MESWorkCenterType type = workCenterTypeRepository.findOne(objectType.getId());
                List<MESWorkCenter> workCenters = workCenterRepository.findByType(type);
                workCenters.forEach(workCenter -> {
                    ids.add(workCenter.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MACHINETYPE.toString()))) {
                MESMachineType type = machineTypeRepository.findOne(objectType.getId());
                List<MESMachine> mesMachines = machineRepository.findByType(type);
                mesMachines.forEach(mesMachine -> {
                    ids.add(mesMachine.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.EQUIPMENTTYPE.toString()))) {
                MESEquipmentType type = equipmentTypeRepository.findOne(objectType.getId());
                List<MESEquipment> equipments = equipmentRepository.findByType(type);
                equipments.forEach(equipment -> {
                    ids.add(equipment.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.INSTRUMENTTYPE.toString()))) {
                MESInstrumentType type = instrumentTypeRepository.findOne(objectType.getId());
                List<MESInstrument> instruments = instrumentRepository.findByType(type);
                instruments.forEach(instrument -> {
                    ids.add(instrument.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.TOOLTYPE.toString()))) {
                MESToolType type = toolTypeRepository.findOne(objectType.getId());
                List<MESTool> tools = toolRepository.findByType(type);
                tools.forEach(tool -> {
                    ids.add(tool.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.JIGFIXTURETYPE.toString()))) {
                MESJigsFixtureType type = jigsFixtureTypeRepository.findOne(objectType.getId());
                List<MESJigsFixture> jigsFixtures = jigsFixtureRepository.findByType(type);
                jigsFixtures.forEach(jigsFixture -> {
                    ids.add(jigsFixture.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MATERIALTYPE.toString()))) {
                MESMaterialType type = materialTypeRepository.findOne(objectType.getId());
                List<MESMaterial> mesMaterials = materialRepository.findByType(type);
                mesMaterials.forEach(material -> {
                    ids.add(material.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MANPOWERTYPE.toString()))) {
                MESManpowerType type = manpowerTypeRepository.findOne(objectType.getId());
                List<MESManpower> manpowers = manpowerRepository.findByType(type);
                manpowers.forEach(manpower -> {
                    ids.add(manpower.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.OPERATIONTYPE.toString()))) {
                MESOperationType type = operationTypeRepository.findOne(objectType.getId());
                List<MESOperation> operations = operationRepository.findByType(type);
                operations.forEach(operation -> {
                    ids.add(operation.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PRODUCTIONORDERTYPE.toString()))) {
                MESProductionOrderType type = productionOrderTypeRepository.findOne(objectType.getId());
                List<MESProductionOrder> productionOrders = productionOrderRepository.findByType(type);
                productionOrders.forEach(productionOrder -> {
                    ids.add(productionOrder.getId());
                });
            }
            predicates.add(pathBase.id.in(ids));
        }

        return ExpressionUtils.allOf(predicates);
    }
}