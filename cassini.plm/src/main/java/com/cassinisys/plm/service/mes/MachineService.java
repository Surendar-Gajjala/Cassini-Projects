package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.MachineEvents;
import com.cassinisys.plm.filtering.MachineCriteria;
import com.cassinisys.plm.filtering.MachinePredicateBuilder;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mro.MROAsset;
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
 * Created by Suresh Cassini on 10/27/2020.
 */
@Service
public class MachineService implements CrudService<MESMachine, Integer> {
    @Autowired
    private MESMachineRepository mesMachineRepository;
    @Autowired
    private MachinePredicateBuilder machinePredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepo;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private MachineTypeRepository machineTypeRepository;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MESWorkCenterRepository workCenterRepository;
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
    @Transactional
    @PreAuthorize("hasPermission(#mesMachine,'create')")
    public MESMachine create(MESMachine mesMachine) {
        MROAsset mroAsset = mesMachine.getAsset();
        MESMachine existingMachine = mesMachineRepository.findByName(mesMachine.getName());
        MESManufacturerData manufacturerData = mesMachine.getManufacturerData();
        MESMachine existMachineNumber = mesMachineRepository.findByNumber(mesMachine.getNumber());
        if (existMachineNumber != null) {
            String message = messageSource.getMessage("machine_number_already_exists", null, "{0} Machine number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existMachineNumber.getNumber());
            throw new CassiniException(result);
        }
        if (existingMachine != null) {
            String message = messageSource.getMessage("machine_name_already_exists", null, "{0} Machine name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMachine.getName());
            throw new CassiniException(result);
        } else {
            autoNumberService.saveNextNumber(mesMachine.getType().getAutoNumberSource().getId(), mesMachine.getNumber());
            mesMachine = mesMachineRepository.save(mesMachine);
            if (manufacturerData == null) {
                manufacturerData = new MESManufacturerData();
            }
            manufacturerData.setObject(mesMachine.getId());
            manufacturerData = manufacturerDataRepository.save(manufacturerData);
        }

        if (mesMachine.getRequiresMaintenance() && mroAsset != null) {
            mroAsset.setResource(mesMachine.getId());
            assetService.create(mroAsset);
        }

        applicationEventPublisher.publishEvent(new MachineEvents.MachineCreatedEvent(mesMachine));
        return mesMachine;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mesMachine.id ,'edit')")
    public MESMachine update(MESMachine mesMachine) {
        MESMachine oldMachine = JsonUtils.cloneEntity(mesMachineRepository.findOne(mesMachine.getId()), MESMachine.class);
       //for Time line
        MESManufacturerData oldManufacturerData = manufacturerDataRepository.findOne(mesMachine.getId());
        oldMachine.setManufacturerData(oldManufacturerData);

        MESMachine existingMachine = mesMachineRepository.findByName(mesMachine.getName());
        MESManufacturerData manufacturerData = mesMachine.getManufacturerData();
        if (existingMachine != null && !existingMachine.getId().equals(mesMachine.getId())) {
            throw new CassiniException("Machine name already exist");
        } else {
            mesMachine = mesMachineRepository.save(mesMachine);
            mesMachine.setManufacturerData(manufacturerData);
            applicationEventPublisher.publishEvent(new MachineEvents.MachineBasicInfoUpdatedEvent(oldMachine, mesMachine));
            manufacturerData = manufacturerDataRepository.save(manufacturerData);
        }
     
        return mesMachine;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#machineId,'delete')")
    public void delete(Integer machineId) {
        List<MROAsset> assets = mroAssetRepository.findByResource(machineId);
        if (assets.size() > 0) {
            String message = messageSource.getMessage("machine_already_used_in_asset", null, "Machine already in use", LocaleContextHolder.getLocale());
            throw new CassiniException(message);
        }
        mesMachineRepository.delete(machineId);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESMachine get(Integer machineId) {
        MESMachine mesMachine = mesMachineRepository.findOne(machineId);
        if (mesMachine != null) {
            MESWorkCenter workCenter = workCenterRepository.findOne(mesMachine.getWorkCenter());
            mesMachine.setWorkCenterId(workCenter.getId());
            mesMachine.setWorkCenterName(workCenter.getName());
        }
        mesMachine.setManufacturerData(manufacturerDataRepository.findOne(machineId));
        return mesMachine;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMachine> getAll() {
        return mesMachineRepository.findAll();
    }

    public void saveMachineAttributes(List<MESObjectAttribute> attributes) {
        for (MESObjectAttribute attribute : attributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {
                mesObjectAttributeRepo.save(attribute);
            }
        }
    }

    @Transactional
    public MESMachine saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        MESMachine machine = mesMachineRepository.findOne(objectId);
        if (machine != null) {
            /*PLMItemAttribute itemAttribute = new PLMItemAttribute();
            itemAttribute.setId(new ObjectAttributeId(objectId, attributeId));*/
            /*List<MultipleFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, itemAttribute);*/

            MESObjectAttribute mesObjectAttribute = new MESObjectAttribute();
            mesObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, mesObjectAttribute);
        }

        return machine;
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public MESObjectAttribute updateMachineAttribute(MESObjectAttribute attribute) {
        MESObjectAttribute oldValue = mesObjectAttributeRepo.findByObjectAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, MESObjectAttribute.class);
        MESObjectTypeAttribute mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());

        if (mesObjectTypeAttribute.getMeasurement() != null) {
            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(mesObjectTypeAttribute.getMeasurement().getId());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(attribute.getMeasurementUnit().getId());
            MeasurementUnit baseunit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(mesObjectTypeAttribute.getMeasurement().getId());

            Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
            Integer baseUnitIndex = measurementUnits.indexOf(baseunit);

            if (!attributeUnitIndex.equals(baseUnitIndex)) {
//                 attribute.setDoubleValue(attribute.getDoubleValue() / measurementUnit.getConversionFactoe());
            } else {
                attribute.setDoubleValue(attribute.getDoubleValue());
            }
        } else {
            attribute.setDoubleValue(attribute.getDoubleValue());
        }
        attribute = mesObjectAttributeRepo.save(attribute);

        MESMachine mesMachine = mesMachineRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new MachineEvents.MachineAttributesUpdatedEvent(mesMachine, oldValue, attribute));
        return attribute;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESMachine> getAllMachinesByPageable(Pageable pageable, MachineCriteria criteria) {
        Predicate predicate = machinePredicateBuilder.build(criteria, QMESMachine.mESMachine);
        Page<MESMachine> machines = mesMachineRepository.findAll(predicate, pageable);
        machines.getContent().forEach(machine -> {
            if (machine.getWorkCenter() != null) {
                MESWorkCenter workCenter = workCenterRepository.findOne(machine.getWorkCenter());
                machine.setWorkCenterId(workCenter.getId());
                machine.setWorkCenterName(workCenter.getName());
            }
            ObjectFileDto objectFileDto = objectFileService.getObjectFiles(machine.getId(), PLMObjectType.MESOBJECT,false);
                machine.setMachineFiles(objectFileDto.getObjectFiles());
        });

        return machines;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESMachine> getMachinesByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MESMachineType type = machineTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return mesMachineRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MESMachineType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESMachineType> children = machineTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESMachineType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional
    public MESMachine uploadImage(Integer id, MultipartHttpServletRequest request) {
        MESMachine machine = mesMachineRepository.findOne(id);
        if (machine != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    machine.setImage(file.getBytes());
                    machine = mesMachineRepository.save(machine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return machine;
    }

    public void downloadImage(Integer machineId, HttpServletResponse response) {
        MESMachine machine = mesMachineRepository.findOne(machineId);
        if (machine != null) {
            InputStream is = new ByteArrayInputStream(machine.getImage());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
