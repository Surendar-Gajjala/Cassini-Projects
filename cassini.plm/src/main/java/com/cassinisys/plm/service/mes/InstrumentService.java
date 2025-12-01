package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.InstrumentEvents;
import com.cassinisys.plm.filtering.InstrumentCriteria;
import com.cassinisys.plm.filtering.InstrumentPredicateBuilder;
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
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 27-10-2020.
 */
@Service
public class InstrumentService implements CrudService<MESInstrument, Integer> {

    @Autowired
    private MESInstrumentRepository mesInstrumentRepository;
    @Autowired(required = true)
    private InstrumentPredicateBuilder instrumentPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private InstrumentTypeRepository instrumentTypeRepository;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
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
    @PreAuthorize("hasPermission(#mesInstrument,'create')")
    public MESInstrument create(MESInstrument mesInstrument) {
        MROAsset mroAsset = mesInstrument.getAsset();
        MESInstrument existingInstrument = mesInstrumentRepository.findByName(mesInstrument.getName());
        MESManufacturerData manufacturerData = mesInstrument.getManufacturerData();
        MESInstrument existInstrumentNumber = mesInstrumentRepository.findByNumber(mesInstrument.getNumber());
        if (existInstrumentNumber != null) {
            String message = messageSource.getMessage("instrument_number_already_exists", null, "{0} Instrument number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existInstrumentNumber.getNumber());
            throw new CassiniException(result);
        }
        if (existingInstrument != null) {
            String message = messageSource.getMessage("instrument_name_already_exists", null, "{0} Instrument name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingInstrument.getName());
            throw new CassiniException(result);
        } else {
            autoNumberService.saveNextNumber(mesInstrument.getType().getAutoNumberSource().getId(), mesInstrument.getNumber());
            mesInstrument = mesInstrumentRepository.save(mesInstrument);
            if (manufacturerData == null) {
                manufacturerData = new MESManufacturerData();
            }
            manufacturerData.setObject(mesInstrument.getId());
            manufacturerData = manufacturerDataRepository.save(manufacturerData);
        }
        if (mesInstrument.getRequiresMaintenance() && mroAsset != null) {
            mroAsset.setResource(mesInstrument.getId());
            assetService.create(mroAsset);
        }
        applicationEventPublisher.publishEvent(new InstrumentEvents.InstrumentCreatedEvent(mesInstrument));
        return mesInstrument;
    }

    @Override
    @PreAuthorize("hasPermission(#mesInstrument.id ,'edit')")
    public MESInstrument update(MESInstrument mesInstrument) {
        MESInstrument oldInstrument = mesInstrumentRepository.findOne(mesInstrument.getId());

        MESManufacturerData oldManufacturerData = manufacturerDataRepository.findOne(mesInstrument.getId());
        oldInstrument.setManufacturerData(oldManufacturerData);

        MESInstrument existingInstrument = mesInstrumentRepository.findByName(mesInstrument.getName());
        MESManufacturerData manufacturerData = mesInstrument.getManufacturerData();
        if (existingInstrument != null && !existingInstrument.getId().equals(mesInstrument.getId())) {
            throw new CassiniException("InstrumentId name already exist");
        } else {
            mesInstrument = mesInstrumentRepository.save(mesInstrument);
            if (manufacturerData == null) {
                manufacturerData = new MESManufacturerData();
            }
            manufacturerData.setObject(mesInstrument.getId());
            manufacturerData = manufacturerDataRepository.save(manufacturerData);
        }
        mesInstrument.setManufacturerData(manufacturerData);
        applicationEventPublisher.publishEvent(new InstrumentEvents.InstrumentBasicInfoUpdatedEvent(oldInstrument, mesInstrument));
        return mesInstrument;
    }

    @Override
    @PreAuthorize("hasPermission(#instrumentId,'delete')")
    public void delete(Integer instrumentId) {
        List<MROAsset> assets = mroAssetRepository.findByResource(instrumentId);
        if (assets.size() > 0) {
            String message = messageSource.getMessage("instrument_already_used_in_asset", null, "Instrument already in use", LocaleContextHolder.getLocale());
            throw new CassiniException(message);
        }
        mesInstrumentRepository.delete(instrumentId);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESInstrument get(Integer instrumentId) {
        MESInstrument mesInstrument = mesInstrumentRepository.findOne(instrumentId);
        mesInstrument.setManufacturerData(manufacturerDataRepository.findOne(instrumentId));
        return mesInstrument;

    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESInstrument> getAll() {
        return mesInstrumentRepository.findAll();
    }


    public void saveInstrumentAttributes(List<MESObjectAttribute> attributes) {
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
    public MESInstrument saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        MESInstrument mesInstrument = mesInstrumentRepository.findOne(objectId);
        if (mesInstrument != null) {
            PLMItemAttribute itemAttribute = new PLMItemAttribute();
            itemAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, itemAttribute);

        }

        return mesInstrument;
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public MESObjectAttribute updateInstrumentAttribute(MESObjectAttribute attribute) {
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

        MESInstrument mesInstrument = mesInstrumentRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new InstrumentEvents.InstrumentAttributesUpdatedEvent(mesInstrument, oldValue, attribute));
        return attribute;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESInstrument> getAllInstrumentsByPageable(Pageable pageable, InstrumentCriteria criteria) {
        Predicate predicate = instrumentPredicateBuilder.build(criteria, QMESInstrument.mESInstrument);
        Page<MESInstrument> instruments = mesInstrumentRepository.findAll(predicate, pageable);
        instruments .getContent().forEach(instrument -> {

            ObjectFileDto objectFileDto = objectFileService.getObjectFiles(instrument.getId(), PLMObjectType.MESOBJECT,false);
            instrument.setInstrumentFiles(objectFileDto.getObjectFiles());
        });
        return instruments;
    }

    @Transactional
    public MESInstrument uploadImage(Integer id, MultipartHttpServletRequest request) {
        MESInstrument instrument = mesInstrumentRepository.findOne(id);
        if (instrument != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    instrument.setImage(file.getBytes());
                    instrument = mesInstrumentRepository.save(instrument);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return instrument;
    }

    public void downloadImage(Integer instrumentId, HttpServletResponse response) {
        MESInstrument instrument = mesInstrumentRepository.findOne(instrumentId);
        if (instrument != null) {
            InputStream is = new ByteArrayInputStream(instrument.getImage());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESInstrument> getInstrumentsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MESInstrumentType type = instrumentTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return mesInstrumentRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MESInstrumentType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESInstrumentType> children = instrumentTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESInstrumentType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }
}
