package com.cassinisys.plm.service.mro;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.AssetEvents;
import com.cassinisys.plm.filtering.AssetCriteria;
import com.cassinisys.plm.filtering.AssetPredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.mro.dto.AssetResourcesDto;
import com.cassinisys.plm.model.mro.dto.WorkOrderDto;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mro.*;
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Suresh Cassini on 18-11-2020.
 */
@Service
public class AssetService implements CrudService<MROAsset, Integer> {
    @Autowired
    private MROAssetRepository mroAssetRepository;
    @Autowired
    private AssetPredicateBuilder assetPredicateBuilder;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PlantTypeRepository plantTypeRepository;
    @Autowired
    private MESPlantRepository plantRepo;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MROWorkOrderRepository mroWorkOrderRepository;
    @Autowired
    private MESWorkCenterRepository workCenterRepository;

    @Autowired
    private MESMachineRepository machineRepository;
    @Autowired
    private MESEquipmentRepository equipmentRepository;
    @Autowired
    private MESInstrumentRepository instrumentRepository;
    @Autowired
    private MROAssetSparePartRepository assetSparePartRepository;

    @Autowired
    private MESToolRepository toolRepo;
    @Autowired
    private WorkCenterTypeRepository workCenterTypeRepository;
    @Autowired
    private MachineTypeRepository machineTypeRepository;
    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;
    @Autowired
    private InstrumentTypeRepository instrumentTypeRepository;
    @Autowired
    private JigsFixtureTypeRepository jigsFixtureTypeRepository;
    @Autowired
    private ToolTypeRepository toolTypeRepository;
    @Autowired
    private MESJigsFixtureRepository jigsFixtureRepository;
    @Autowired
    private MROAssetTypeRepository assetTypeRepository;
    @Autowired
    private MROMeterRepository meterRepository;

    @Autowired
    private MROWorkRequestRepository mroWorkRequestRepository;

    @Autowired
    private MROMaintenancePlanRepository mroMaintenancePlanRepository;

    @Autowired
    private MROAssetMeterRepository mroAssetMeterRepository;

    @Autowired
    private MROObjectAttributeRepository mroObjectAttributeRepository;
    @Autowired
    private MROMeterReadingRepository meterReadingRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private MROWorkOrderPartRepository workOrderPartRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#asset,'create')")
    public MROAsset create(MROAsset asset) {
        Integer[] meters = asset.getMeters();
        MROAsset existAssetNumber = mroAssetRepository.findByNumber(asset.getNumber());
        if (existAssetNumber != null) {
            String message = messageSource.getMessage("asset_number_already_exists", null, "{0} Asset Number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existAssetNumber.getNumber());
            throw new CassiniException(result);
        }
        MROAsset existingAsset = mroAssetRepository.findByNameContainingIgnoreCase(asset.getName());
        if (existingAsset != null) {
            throw new CassiniException(messageSource.getMessage("asset_already_exist", null, "Asset name already exist", LocaleContextHolder.getLocale()));
        } else {
            autoNumberService.saveNextNumber(asset.getType().getAutoNumberSource().getId(), asset.getNumber());
            asset = mroAssetRepository.save(asset);
            if (meters.length > 0) {
                for (Integer meter : meters) {
                    MROAssetMeter meterNewObject = new MROAssetMeter();
                    meterNewObject.setMeter(meter);
                    meterNewObject.setAsset(asset.getId());
                    mroAssetMeterRepository.save(meterNewObject);
                }
            }
        }
        applicationEventPublisher.publishEvent(new AssetEvents.AssetCreatedEvent(asset));
        return asset;

    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#asset.id ,'edit')")
    public MROAsset update(MROAsset asset) {
        MROAsset oldAsset = JsonUtils.cloneEntity(mroAssetRepository.findOne(asset.getId()), MROAsset.class);
        MROAsset existingAsset = mroAssetRepository.findByNameContainingIgnoreCase(asset.getName());
        if (asset.getMetered() == false) {
            mroAssetMeterRepository.deleteByAsset(asset.getId());
        }
        if (existingAsset != null && !existingAsset.getId().equals(asset.getId())) {
            throw new CassiniException(messageSource.getMessage("asset_already_exist", null, "Asset name already exist", LocaleContextHolder.getLocale()));
        } else {
            asset = mroAssetRepository.save(asset);

        }
        applicationEventPublisher.publishEvent(new AssetEvents.AssetBasicInfoUpdatedEvent(oldAsset, asset));
        return asset;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#assetId,'delete')")
    public void delete(Integer assetId) {

        List<MROMaintenancePlan> maintenancePlans = mroMaintenancePlanRepository.findByAsset(assetId);
        List<MROWorkRequest> workRequests = mroWorkRequestRepository.findByAsset(assetId);
        List<MROWorkOrder> workOrders = mroWorkOrderRepository.findByAsset(assetId);
        if (maintenancePlans.size() > 0 || workRequests.size() > 0 || workOrders.size() > 0) {
            throw new CassiniException(messageSource.getMessage("asset_already_in_use", null, "Asset already in use", LocaleContextHolder.getLocale()));
        }

        mroAssetRepository.delete(assetId);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MROAsset get(Integer assetId) {
        List<Integer> meters = new ArrayList<>();
        List<MROAssetMeter> metersObjects = new ArrayList<>();
        List<MROMeter> meterElements = new ArrayList<>();
        MROAsset asset = mroAssetRepository.findOne(assetId);
        if (asset != null) {
            Person person = personRepository.findOne(asset.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(asset.getModifiedBy());
            asset.setCreatePerson(person.getFirstName());
            asset.setTypeName(asset.getType().getName());
            asset.setModifiedPerson(modifiedPerson.getFirstName());
            List<MROAssetMeter> metersElements = mroAssetMeterRepository.findByAsset(assetId);
            for (MROAssetMeter meter : metersElements) {

                MROMeter meterObject = meterRepository.findOne(meter.getMeter());
                Integer meterReadingId = meterReadingRepository.getLastAssetMeterReadings(meter.getId());
                if (meterReadingId != null) {
                    MROMeterReading meterReading = meterReadingRepository.findOne(meterReadingId);
                    meterObject.setLastReadingValue(meterReading.getValue());
                    meterObject.setLastReadingDate(meterReading.getCurrentDate());
                }

                meterElements.add(meterObject);
                meters.add(meter.getId());
                metersObjects.add(meter);
            }
        }
        Integer[] arr = new Integer[meters.size()];
        arr = meters.toArray(arr);
        asset.setMeters(arr);
        asset.setMeterObjects(meterElements);
        if (asset.getResourceType().equals(MESType.PLANTTYPE)) {
            MESPlant plant = plantRepo.findOne(asset.getResource());
            asset.setResourceObject(plant);
        } else if (asset.getResourceType().equals(MESType.WORKCENTERTYPE)) {
            MESWorkCenter workCenter = workCenterRepository.findOne(asset.getResource());
            asset.setResourceObject(workCenter);
        } else if (asset.getResourceType().equals(MESType.MACHINETYPE)) {
            MESMachine machine = machineRepository.findOne(asset.getResource());
            if (machine.getImage() != null) {
                machine.setImageValue(getResourceImage(machine.getImage()));
            } else {
                machine.setImageValue(null);
            }
            asset.setResourceObject(machine);
        } else if (asset.getResourceType().equals(MESType.EQUIPMENTTYPE)) {
            MESEquipment equipment = equipmentRepository.findOne(asset.getResource());
            if (equipment.getImage() != null) {
                equipment.setImageValue(getResourceImage(equipment.getImage()));
            } else {
                equipment.setImageValue(null);
            }
            asset.setResourceObject(equipment);
        } else if (asset.getResourceType().equals(MESType.INSTRUMENTTYPE)) {
            MESInstrument instrument = instrumentRepository.findOne(asset.getResource());
            if (instrument.getImage() != null) {
                instrument.setImageValue(getResourceImage(instrument.getImage()));
            } else {
                instrument.setImageValue(null);
            }
            asset.setResourceObject(instrument);
        } else if (asset.getResourceType().equals(MESType.JIGFIXTURETYPE)) {
            MESJigsFixture jigsFixture = jigsFixtureRepository.findOne(asset.getResource());
            if (jigsFixture.getImage() != null) {
                jigsFixture.setImageValue(getResourceImage(jigsFixture.getImage()));
            } else {
                jigsFixture.setImageValue(null);
            }
            asset.setResourceObject(jigsFixture);
        } else if (asset.getResourceType().equals(MESType.TOOLTYPE)) {
            MESTool tool = toolRepo.findOne(asset.getResource());
            if (tool.getImage() != null) {
                tool.setImageValue(getResourceImage(tool.getImage()));
            } else {
                tool.setImageValue(null);
            }
            asset.setResourceObject(tool);
        }
        return asset;
    }


    private String getResourceImage(byte[] thumbnail) {
        String baseString = "";
        byte[] imgBytesAsBase64 = org.apache.commons.codec.binary.Base64.encodeBase64(thumbnail);
        String imgDataAsBase64 = new String(imgBytesAsBase64);
        baseString = "data:image/png;base64," + imgDataAsBase64;
        return baseString;
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getDetailsCount(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setSpareParts(assetSparePartRepository.findByAsset(id).size());
        detailsDto.setWorkOrders(mroWorkOrderRepository.findByAsset(id).size());

        return detailsDto;
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROAsset> getAll() {
        return mroAssetRepository.findAll();
    }

    @Transactional
    public void savemroObjectAttributes(List<MROObjectAttribute> attributes) {
        for (MROObjectAttribute attribute : attributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {
                mroObjectAttributeRepository.save(attribute);
            }
        }
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MROAsset> getAllAssetsByPageable(Pageable pageable, AssetCriteria criteria) {
        Predicate predicate = assetPredicateBuilder.build(criteria, QMROAsset.mROAsset);
        Page<MROAsset> assets = mroAssetRepository.findAll(predicate, pageable);
        for (MROAsset asset : assets.getContent()) {
            if (asset.getResourceType().equals(MESType.PLANTTYPE)) {
                MESPlant plant = plantRepo.findOne(asset.getResource());
                asset.setResourceObject(plant);
            } else if (asset.getResourceType().equals(MESType.WORKCENTERTYPE)) {
                MESWorkCenter workCenter = workCenterRepository.findOne(asset.getResource());
                asset.setResourceObject(workCenter);
            } else if (asset.getResourceType().equals(MESType.MACHINETYPE)) {
                MESMachine machine = machineRepository.findOne(asset.getResource());
                asset.setResourceObject(machine);
            } else if (asset.getResourceType().equals(MESType.EQUIPMENTTYPE)) {
                MESEquipment equipment = equipmentRepository.findOne(asset.getResource());
                asset.setResourceObject(equipment);
            } else if (asset.getResourceType().equals(MESType.INSTRUMENTTYPE)) {
                MESInstrument instrument = instrumentRepository.findOne(asset.getResource());
                asset.setResourceObject(instrument);
            } else if (asset.getResourceType().equals(MESType.JIGFIXTURETYPE)) {
                MESJigsFixture jigsFixture = jigsFixtureRepository.findOne(asset.getResource());
                asset.setResourceObject(jigsFixture);
            } else if (asset.getResourceType().equals(MESType.TOOLTYPE)) {
                MESTool tool = toolRepo.findOne(asset.getResource());
                asset.setResourceObject(tool);
            }
        }

        return assets;
    }


    @Transactional(readOnly = true)
    public AssetResourcesDto getAssetResourcesByType(Integer typeId, String objectType) {
        AssetResourcesDto dto = new AssetResourcesDto();
        List<Integer> ids = new ArrayList<>();
        if (objectType.equals("PLANTTYPE")) {
            MESPlantType type = plantTypeRepository.findOne(typeId);
            collectHierarchyPlantTypeIds(ids, type);
            List<MESPlant> plants = plantRepo.getByTypeIds(ids);
            List<MESPlant> filteredPlants = new LinkedList<>();
            for (MESPlant plant : plants) {
                List<MROAsset> existingAssets = getAssetsByResource(plant.getId());
                if (existingAssets.size() == 0) {
                    filteredPlants.add(plant);
                }
            }
            dto.setPlants(filteredPlants);
        } else if (objectType.equals("WORKCENTERTYPE")) {
            MESWorkCenterType type = workCenterTypeRepository.findOne(typeId);
            collectHierarchyWorkCenterTypeIds(ids, type);
            List<MESWorkCenter> workCenters = workCenterRepository.getByTypeIds(ids);
            List<MESWorkCenter> filteredWorkCenters = new LinkedList<>();
            for (MESWorkCenter workCenter : workCenters) {
                List<MROAsset> existingAssets = getAssetsByResource(workCenter.getId());
                if (existingAssets.size() == 0) {
                    filteredWorkCenters.add(workCenter);
                }
            }

            dto.setWorkCenters(filteredWorkCenters);
        } else if (objectType.equals("MACHINETYPE")) {
            MESMachineType type = machineTypeRepository.findOne(typeId);
            collectHierarchyMachineTypeIds(ids, type);
            List<MESMachine> machines = machineRepository.getByTypeIds(ids);
            List<MESMachine> filteredMachines = new LinkedList<>();
            for (MESMachine machine : machines) {
                List<MROAsset> existingAssets = getAssetsByResource(machine.getId());
                if (existingAssets.size() == 0) {
                    filteredMachines.add(machine);
                }
            }
            dto.setMachines(filteredMachines);
        } else if (objectType.equals("EQUIPMENTTYPE")) {
            MESEquipmentType type = equipmentTypeRepository.findOne(typeId);
            collectHierarchyEquipmentTypeIds(ids, type);
            List<MESEquipment> equipments = equipmentRepository.getByTypeIds(ids);
            List<MESEquipment> filteredEquipments = new LinkedList<>();
            for (MESEquipment equipment : equipments) {
                List<MROAsset> existingAssets = getAssetsByResource(equipment.getId());
                if (existingAssets.size() == 0) {
                    filteredEquipments.add(equipment);
                }
            }
            dto.setEquipments(filteredEquipments);
        } else if (objectType.equals("INSTRUMENTTYPE")) {
            MESInstrumentType type = instrumentTypeRepository.findOne(typeId);
            collectHierarchyInstrumentTypeIds(ids, type);
            List<MESInstrument> instruments = instrumentRepository.getByTypeIds(ids);
            List<MESInstrument> filteredInstruments = new LinkedList<>();
            for (MESInstrument instrument : instruments) {
                List<MROAsset> existingAssets = getAssetsByResource(instrument.getId());
                if (existingAssets.size() == 0) {
                    filteredInstruments.add(instrument);
                }
            }
            dto.setInstruments(filteredInstruments);
        } else if (objectType.equals("JIGFIXTURETYPE")) {
            MESJigsFixtureType type = jigsFixtureTypeRepository.findOne(typeId);
            collectHierarchyJigsFixtureTypeIds(ids, type);
            List<MESJigsFixture> jigsFixtures = jigsFixtureRepository.getByTypeIds(ids);
            List<MESJigsFixture> filteredJigFixtures = new LinkedList<>();
            for (MESJigsFixture jigsFixture : jigsFixtures) {
                List<MROAsset> existingAssets = getAssetsByResource(jigsFixture.getId());
                if (existingAssets.size() == 0) {
                    filteredJigFixtures.add(jigsFixture);
                }
            }
            dto.setJigsFixtures(filteredJigFixtures);
        } else if (objectType.equals("TOOLTYPE")) {
            MESToolType type = toolTypeRepository.findOne(typeId);
            collectHierarchyToolTypeIds(ids, type);
            List<MESTool> tools = toolRepo.getByTypeIds(ids);
            List<MESTool> filteredTools = new LinkedList<>();
            for (MESTool tool : tools) {
                List<MROAsset> existingAssets = getAssetsByResource(tool.getId());
                if (existingAssets.size() == 0) {
                    filteredTools.add(tool);
                }
            }
            dto.setTools(filteredTools);
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public List<MROAsset> getAssetsByResource(Integer resourceId) {
        List<MROAsset> assets = mroAssetRepository.findByResource(resourceId);
        return assets;
    }

    private void collectHierarchyPlantTypeIds(List<Integer> collector, MESPlantType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESPlantType> children = plantTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESPlantType child : children) {
                collectHierarchyPlantTypeIds(collector, child);
            }
        }
    }


    private void collectHierarchyWorkCenterTypeIds(List<Integer> collector, MESWorkCenterType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESWorkCenterType> children = workCenterTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESWorkCenterType child : children) {
                collectHierarchyWorkCenterTypeIds(collector, child);
            }
        }
    }


    private void collectHierarchyMachineTypeIds(List<Integer> collector, MESMachineType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESMachineType> children = machineTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESMachineType child : children) {
                collectHierarchyMachineTypeIds(collector, child);
            }
        }
    }

    private void collectHierarchyEquipmentTypeIds(List<Integer> collector, MESEquipmentType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESEquipmentType> children = equipmentTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESEquipmentType child : children) {
                collectHierarchyEquipmentTypeIds(collector, child);
            }
        }
    }


    private void collectHierarchyInstrumentTypeIds(List<Integer> collector, MESInstrumentType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESInstrumentType> children = instrumentTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESInstrumentType child : children) {
                collectHierarchyInstrumentTypeIds(collector, child);
            }
        }
    }

    private void collectHierarchyJigsFixtureTypeIds(List<Integer> collector, MESJigsFixtureType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESJigsFixtureType> children = jigsFixtureTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESJigsFixtureType child : children) {
                collectHierarchyJigsFixtureTypeIds(collector, child);
            }
        }
    }


    private void collectHierarchyToolTypeIds(List<Integer> collector, MESToolType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESToolType> children = toolTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESToolType child : children) {
                collectHierarchyToolTypeIds(collector, child);
            }
        }
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROAssetSparePart> getAssetSpareParts(Integer assetId) {
        return assetSparePartRepository.findByAsset(assetId);
    }

    @Transactional
    public List<MROAssetSparePart> createAssetSpareParts(Integer assetId, List<MROAssetSparePart> assetParts) {
        List<MROAssetSparePart> assetPartEle = new ArrayList<>();
        for (MROAssetSparePart workOrderPart : assetParts) {
            MROAssetSparePart existPart = assetSparePartRepository.findByAssetAndSparePart(workOrderPart.getAsset(), workOrderPart.getSparePart());
            if (existPart == null) {
                assetPartEle.add(assetSparePartRepository.save(workOrderPart));
            }
        }
        applicationEventPublisher.publishEvent(new AssetEvents.AssetSparePartCreatedEvent(assetId, assetPartEle));
        return assetParts;
    }

    @Transactional
    public void deleteAssetSparePart(Integer assetPartId) {
        MROAssetSparePart assetPart = assetSparePartRepository.findOne(assetPartId);
        List<MROWorkOrderPart> parts = workOrderPartRepository.findBySparePart(assetPart.getSparePart());
        if (parts.size() > 0) {
            throw new CassiniException(messageSource.getMessage("spare_part_already_used_in_workOrders", null, "Spare part already used in workOrders", LocaleContextHolder.getLocale()));
        }
        applicationEventPublisher.publishEvent(new AssetEvents.AssetSparePartDeletedEvent(assetPart.getAsset(), assetPart));
        assetSparePartRepository.delete(assetPartId);
    }


    @Transactional(readOnly = true)
    public List<WorkOrderDto> getAllWorkOrdersByAsset(Integer assetId) {
        List<MROWorkOrder> workOrders = mroWorkOrderRepository.findByAsset(assetId);
        List<WorkOrderDto> workOrderDtos = new ArrayList<>();
        workOrders.forEach(mroWorkOrder -> {
            WorkOrderDto workOrderDto = new WorkOrderDto();
            workOrderDto.setId(mroWorkOrder.getId());
            workOrderDto.setTypeName(mroWorkOrder.getType().getName());
            workOrderDto.setName(mroWorkOrder.getName());
            workOrderDto.setNumber(mroWorkOrder.getNumber());

            workOrderDto.setObjectType(mroWorkOrder.getObjectType().toString());

            if (mroWorkOrder.getAsset() != null) {
                workOrderDto.setAsset(mroWorkOrder.getAsset());
                workOrderDto.setAssetName(mroAssetRepository.findOne(mroWorkOrder.getAsset()).getName());
            }
            if (mroWorkOrder.getRequest() != null) {
                workOrderDto.setRequest(mroWorkOrder.getRequest());
                workOrderDto.setRequestNumber(mroWorkRequestRepository.findOne(mroWorkOrder.getRequest()).getNumber());
            }

            if (mroWorkOrder.getPlan() != null) {
                workOrderDto.setPlan(mroWorkOrder.getPlan());
                workOrderDto.setPlanNumber(mroMaintenancePlanRepository.findOne(mroWorkOrder.getPlan()).getNumber());
            }
            if (mroWorkOrder.getAssignedTo() != null) {
                workOrderDto.setAssignedToName(personRepository.findOne(mroWorkOrder.getAssignedTo()).getFullName());
            }
            workOrderDto.setPriority(mroWorkOrder.getPriority());
            workOrderDto.setStatus(mroWorkOrder.getStatus());
            workOrderDto.setModifiedDate(mroWorkOrder.getModifiedDate());
            workOrderDto.setModifiedByName(personRepository.findOne(mroWorkOrder.getModifiedBy()).getFullName());
            workOrderDtos.add(workOrderDto);
        });

        return workOrderDtos;
    }

    @Transactional
    public void deleteAssetMeter(Integer assetId, Integer meterId) {
        mroAssetMeterRepository.deleteByAssetAndMeter(assetId, meterId);
    }


    @Transactional
    public MROAsset createAssetMeters(MROAsset asset) {
        Integer[] meters = asset.getMeters();
        if (meters.length > 0) {
            for (Integer meter : meters) {
                MROAssetMeter meterNewObject = new MROAssetMeter();
                meterNewObject.setMeter(meter);
                meterNewObject.setAsset(asset.getId());
                mroAssetMeterRepository.save(meterNewObject);
            }
        }
        return asset;

    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MROAsset> getAssetsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MROAssetType type = assetTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return mroAssetRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MROAssetType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MROAssetType> children = assetTypeRepository.findByParentTypeOrderByCreatedDateAsc(type.getId());
            for (MROAssetType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }
}