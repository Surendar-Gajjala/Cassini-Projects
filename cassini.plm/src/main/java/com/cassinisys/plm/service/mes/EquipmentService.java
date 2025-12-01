package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.EquipmentEvents;
import com.cassinisys.plm.filtering.EquipmentCriteria;
import com.cassinisys.plm.filtering.EquipmentPredicateBuilder;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.plm.PLMItemAttribute;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mro.MROAssetRepository;
import com.cassinisys.plm.service.cm.DCOService;
import com.cassinisys.plm.service.mro.AssetService;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.model.plm.PLMObjectType;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 28-10-2020.
 */
@Service
public class EquipmentService implements CrudService<MESEquipment, Integer> {

    @Autowired
    private MESEquipmentRepository mesEquipmentRepository;
    @Autowired(required = true)
    private EquipmentPredicateBuilder equipmentPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ManufacturerDataRepository manufacturerDataRepository;
    @Autowired
    private AssetService assetService;
    @Autowired
    private MROAssetRepository mroAssetRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ObjectFileService objectFileService;

    @Override
    @PreAuthorize("hasPermission(#mesEquipment,'create')")
    public MESEquipment create(MESEquipment mesEquipment) {
        MROAsset mroAsset = mesEquipment.getAsset();
        MESEquipment existingEquipment = mesEquipmentRepository.findByName(mesEquipment.getName());
        MESManufacturerData manufacturerData = mesEquipment.getManufacturerData();
        MESEquipment existEquipmentNumber = mesEquipmentRepository.findByNumber(mesEquipment.getNumber());
        if (existEquipmentNumber != null) {
            String message = messageSource.getMessage("equipment_number_already_exists", null, "{0} Equipment number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existEquipmentNumber.getNumber());
            throw new CassiniException(result);
        }
        if (existingEquipment != null) {
            String message = messageSource.getMessage("equipment_name_already_exists", null, "{0} Equipment name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingEquipment.getName());
            throw new CassiniException(result);
        } else {
            autoNumberService.saveNextNumber(mesEquipment.getType().getAutoNumberSource().getId(), mesEquipment.getNumber());
            mesEquipment = mesEquipmentRepository.save(mesEquipment);
            if (manufacturerData == null) {
                manufacturerData = new MESManufacturerData();
            }
            manufacturerData.setObject(mesEquipment.getId());
            manufacturerData = manufacturerDataRepository.save(manufacturerData);
        }
        if (mesEquipment.getRequiresMaintenance() && mroAsset != null) {
            mroAsset.setResource(mesEquipment.getId());
            assetService.create(mroAsset);
        }
        applicationEventPublisher.publishEvent(new EquipmentEvents.EquipmentCreatedEvent(mesEquipment));
        return mesEquipment;
    }

    @Override
    @PreAuthorize("hasPermission(#mesEquipment.id ,'edit')")
    public MESEquipment update(MESEquipment mesEquipment) {
        MESEquipment existingEquipment = mesEquipmentRepository.findByName(mesEquipment.getName());
        MESEquipment oldEquipment = JsonUtils.cloneEntity(mesEquipmentRepository.findOne(mesEquipment.getId()), MESEquipment.class);
        MESManufacturerData oldManufacturerData = manufacturerDataRepository.findOne(mesEquipment.getId());
        oldEquipment.setManufacturerData(oldManufacturerData);
        MESManufacturerData manufacturerData = mesEquipment.getManufacturerData();
        if (existingEquipment != null && !existingEquipment.getId().equals(mesEquipment.getId())) {
            throw new CassiniException("EquipmentId name already exist");
        } else {
           
            mesEquipment = mesEquipmentRepository.save(mesEquipment);
            if (manufacturerData == null) {
                manufacturerData = new MESManufacturerData();
            }
            manufacturerData.setObject(mesEquipment.getId());
            manufacturerData = manufacturerDataRepository.save(manufacturerData);
        }
        mesEquipment.setManufacturerData(manufacturerData);
        applicationEventPublisher.publishEvent(new EquipmentEvents.EquipmentBasicInfoUpdatedEvent(oldEquipment, mesEquipment));
        return mesEquipment;
    }

    @Override
    @PreAuthorize("hasPermission(#equipmentId,'delete')")
    public void delete(Integer equipmentId) {
        List<MROAsset> assets = mroAssetRepository.findByResource(equipmentId);
        if (assets.size() > 0) {
            String message = messageSource.getMessage("equipment_already_used_in_asset", null, "Equipment already in use", LocaleContextHolder.getLocale());
            throw new CassiniException(message);
        }
        mesEquipmentRepository.delete(equipmentId);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESEquipment get(Integer equipmentId) {
        MESEquipment mesEquipment = mesEquipmentRepository.findOne(equipmentId);
        mesEquipment.setManufacturerData(manufacturerDataRepository.findOne(equipmentId));
        return mesEquipment;

    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESEquipment> getAll() {
        return mesEquipmentRepository.findAll();
    }


    public void saveEquipmentAttributes(List<MESObjectAttribute> attributes) {
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
    public MESEquipment saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        MESEquipment mesEquipment = mesEquipmentRepository.findOne(objectId);
        if (mesEquipment != null) {
            PLMItemAttribute itemAttribute = new PLMItemAttribute();
            itemAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, itemAttribute);

        }

        return mesEquipment;
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public MESObjectAttribute updateEquipmentAttribute(MESObjectAttribute attribute) {
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

        MESEquipment mesEquipment = mesEquipmentRepository.findOne(attribute.getId().getObjectId());
        return attribute;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESEquipment> getAllEquipmentsByPageable(Pageable pageable, EquipmentCriteria criteria) {
        Predicate predicate = equipmentPredicateBuilder.build(criteria, QMESEquipment.mESEquipment);
        Page<MESEquipment> equipments = mesEquipmentRepository.findAll(predicate, pageable);
        equipments .getContent().forEach(equipment -> {

            ObjectFileDto objectFileDto = objectFileService.getObjectFiles(equipment.getId(), PLMObjectType.MESOBJECT,false);
            equipment.setEquipmentFiles(objectFileDto.getObjectFiles());
        });
        return equipments;
    }


    @Transactional
    public MESEquipment uploadImage(Integer id, MultipartHttpServletRequest request) {
        MESEquipment machine = mesEquipmentRepository.findOne(id);
        if (machine != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    machine.setImage(file.getBytes());
                    machine = mesEquipmentRepository.save(machine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return machine;
    }

    public void downloadImage(Integer equipmentId, HttpServletResponse response) {
        MESEquipment equipment = mesEquipmentRepository.findOne(equipmentId);
        if (equipment != null) {
            InputStream is = new ByteArrayInputStream(equipment.getImage());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESEquipment> getEquipmentsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MESEquipmentType type = equipmentTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return mesEquipmentRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MESEquipmentType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESEquipmentType> children = equipmentTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESEquipmentType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }
}
