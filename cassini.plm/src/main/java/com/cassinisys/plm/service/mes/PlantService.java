package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.PlantEvents;
import com.cassinisys.plm.filtering.PlantCriteria;
import com.cassinisys.plm.filtering.PlantPredicateBuilder;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mro.MROAssetRepository;
import com.cassinisys.plm.service.cm.DCOService;
import com.cassinisys.plm.service.mro.AssetService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.model.plm.PLMObjectType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PlantService implements CrudService<MESPlant, Integer> {
    @Autowired
    private MESPlantRepository plantRepo;
    @Autowired
    private PlantPredicateBuilder plantPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private PlantTypeRepository plantTypeRepository;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MESWorkCenterRepository workCenterRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AssetService assetService;
    @Autowired
    private MROAssetRepository mroAssetRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ObjectFileService objectFileService;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mesPlant,'create')")
    public MESPlant create(MESPlant mesPlant) {
        MROAsset mroAsset = mesPlant.getAsset();
        MESPlant existingPlant = plantRepo.findByName(mesPlant.getName());
        MESPlant existPlantNumber = plantRepo.findByNumber(mesPlant.getNumber());
        if (existPlantNumber != null) {
            String message = messageSource.getMessage("plant_number_already_exists", null, "{0} Plant number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existPlantNumber.getNumber());
            throw new CassiniException(result);
        }
        if (existingPlant != null) {
            throw new CassiniException(messageSource.getMessage("plant_already_exist", null, "Plant name already exist", LocaleContextHolder.getLocale()));

        } else {
            autoNumberService.saveNextNumber(mesPlant.getType().getAutoNumberSource().getId(), mesPlant.getNumber());
            mesPlant = plantRepo.save(mesPlant);
        }
        if (mesPlant.getRequiresMaintenance() && mroAsset != null) {
            mroAsset.setResource(mesPlant.getId());

            assetService.create(mroAsset);
        }
        applicationEventPublisher.publishEvent(new PlantEvents.PlantCreatedEvent(mesPlant));
        return mesPlant;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mesPlant.id ,'edit')")
    public MESPlant update(MESPlant mesPlant) {
        MESPlant oldPlant = JsonUtils.cloneEntity(plantRepo.findOne(mesPlant.getId()), MESPlant.class);
        MESPlant existingPlant = plantRepo.findByName(mesPlant.getName());
        if (existingPlant != null && !existingPlant.getId().equals(mesPlant.getId())) {
            throw new CassiniException(messageSource.getMessage("plant_already_exist", null, "Plant name already exist", LocaleContextHolder.getLocale()));

        } else {
            mesPlant = plantRepo.save(mesPlant);
            Person person = personRepository.findOne(mesPlant.getPlantPerson());
            mesPlant.setPerson(person.getFirstName());
        }
        applicationEventPublisher.publishEvent(new PlantEvents.PlantBasicInfoUpdatedEvent(oldPlant, mesPlant));
        return mesPlant;
    }

    @Override
    @PreAuthorize("hasPermission(#plantId,'delete')")
    public void delete(Integer plantId) {
        List<MESWorkCenter> workCenter = workCenterRepository.findByPlant(plantId);
        List<MROAsset> assets = mroAssetRepository.findByResource(plantId);
        if (workCenter.size() > 0) {
            String message = messageSource.getMessage("plant_already_exist_in_workcenter", null, "The plant is assigned to workcenter", LocaleContextHolder.getLocale());
            throw new CassiniException(message);
        } else if (assets.size() > 0) {
            String message = messageSource.getMessage("plant_already_used_in_asset", null, "Plant already in use", LocaleContextHolder.getLocale());
            throw new CassiniException(message);
        } else {
            plantRepo.delete(plantId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESPlant get(Integer plantId) {
        MESPlant plant = plantRepo.findOne(plantId);
        if (plant != null && plant.getPlantPerson() != null) {
            Person person = personRepository.findOne(plant.getPlantPerson());
            plant.setPerson(person.getFirstName());
        }
        return plant;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESPlant> getAll() {
        return plantRepo.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESPlant> getAllPlantsByPageable(Pageable pageable) {
        Page<MESPlant> plants = plantRepo.findAll(pageable);
        List<MESPlant> plantList = new ArrayList<>();
        plants.getContent().forEach(plant -> {
            Person person = personRepository.findOne(plant.getPlantPerson());
            plant.setPerson(person.getFirstName());
            plantList.add(plant);

        
        });

        return new PageImpl<MESPlant>(plantList, pageable, plants.getTotalElements());

    }

    @Transactional
    public void savePlantAttributes(List<MESObjectAttribute> attributes) {
        for (MESObjectAttribute attribute : attributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {
                mesObjectAttributeRepository.save(attribute);
            }
        }
    }

    @Transactional
    public MESPlant saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        MESPlant plant = plantRepo.findOne(objectId);
        if (plant != null) {
            MESObjectAttribute mesObjectAttribute = new MESObjectAttribute();
            mesObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, mesObjectAttribute);

        }

        return plant;
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public MESObjectAttribute updatePlantAttribute(MESObjectAttribute attribute) {
        MESObjectAttribute oldValue = mesObjectAttributeRepository.findByObjectAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, MESObjectAttribute.class);
        MESObjectTypeAttribute mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());

        if (mesObjectTypeAttribute.getMeasurement() != null) {
            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(mesObjectTypeAttribute.getMeasurement().getId());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(attribute.getMeasurementUnit().getId());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(mesObjectTypeAttribute.getMeasurement().getId());

            Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
            Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);

            if (!attributeUnitIndex.equals(baseUnitIndex)) {
                attribute.setDoubleValue(attribute.getDoubleValue() / measurementUnit.getConversionFactor());
            } else {
                attribute.setDoubleValue(attribute.getDoubleValue());
            }
        } else {
            attribute.setDoubleValue(attribute.getDoubleValue());
        }
        attribute = mesObjectAttributeRepository.save(attribute);

        MESPlant plant = plantRepo.findOne(attribute.getId().getObjectId());
        return attribute;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESPlant> getAllPlantsByPageable(Pageable pageable, PlantCriteria criteria) {
        Predicate predicate = plantPredicateBuilder.build(criteria, QMESPlant.mESPlant);
        Page<MESPlant> plants = plantRepo.findAll(predicate, pageable);
        List<MESPlant> plantList = new ArrayList<>();
        plants.getContent().forEach(plant -> {
            Person person = personRepository.findOne(plant.getPlantPerson());
            plant.setPerson(person.getFullName());
            List<MESWorkCenter> workCenter = workCenterRepository.findByPlant(plant.getId());
            if (workCenter.size() > 0) {
                plant.setWorkCenterObject(true);
            }

            ObjectFileDto objectFileDto = objectFileService.getObjectFiles(plant.getId(), PLMObjectType.MESOBJECT,false);
            plant.setPlantFiles(objectFileDto.getObjectFiles());
            plantList.add(plant);
        });

        return new PageImpl<MESPlant>(plantList, pageable, plants.getTotalElements());
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESPlant> getPlantsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MESPlantType type = plantTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return plantRepo.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MESPlantType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESPlantType> children = plantTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESPlantType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }
}

