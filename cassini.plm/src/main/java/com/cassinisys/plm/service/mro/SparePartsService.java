package com.cassinisys.plm.service.mro;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.SparePartsEvents;
import com.cassinisys.plm.filtering.SparePartCriteria;
import com.cassinisys.plm.filtering.SparePartPredicateBuilder;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.repo.mro.*;
import com.cassinisys.plm.service.cm.DCOService;
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
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SparePartsService implements CrudService<MROSparePart, Integer> {
    @Autowired
    private MROSparePartRepository sparePartRepository;
    @Autowired
    private SparePartPredicateBuilder sparePartPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MROObjectAttributeRepository mroObjectAttributeRepository;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private MROSparePartTypeRepository sparePartTypeRepository;
    @Autowired
    private MROAssetSparePartRepository assetSparePartRepository;
    @Autowired
    private MROObjectTypeAttributeRepository mroObjectTypeAttributeRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AutoNumberService autoNumberService;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mroSparePart,'create')")
    public MROSparePart create(MROSparePart mroSparePart) {
        MROSparePart existSparePartNumber = sparePartRepository.findByNumber(mroSparePart.getNumber());
        if (existSparePartNumber != null) {
            String message = messageSource.getMessage("sparepart_number_already_exists", null, "{0} Sparepart Number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existSparePartNumber.getNumber());
            throw new CassiniException(result);
        }
        MROSparePart existingPart = sparePartRepository.findByNameContainingIgnoreCase(mroSparePart.getName());
        if (existingPart != null) {
            throw new CassiniException(messageSource.getMessage("spare_part_already_exist", null, "Spare part name already exist", LocaleContextHolder.getLocale()));

        } else {
            autoNumberService.saveNextNumber(mroSparePart.getType().getAutoNumberSource().getId(), mroSparePart.getNumber());
            mroSparePart = sparePartRepository.save(mroSparePart);
        }
        applicationEventPublisher.publishEvent(new SparePartsEvents.SparePartCreatedEvent(mroSparePart));
        return mroSparePart;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mroSparePart.id ,'edit')")
    public MROSparePart update(MROSparePart mroSparePart) {
        MROSparePart oldPart = JsonUtils.cloneEntity(sparePartRepository.findOne(mroSparePart.getId()), MROSparePart.class);
        MROSparePart existingPart = sparePartRepository.findByNameContainingIgnoreCase(mroSparePart.getName());
        if (existingPart != null && !existingPart.getId().equals(mroSparePart.getId())) {
            throw new CassiniException(messageSource.getMessage("spare_part_already_exist", null, "Spare part name already exist", LocaleContextHolder.getLocale()));

        } else {
            mroSparePart = sparePartRepository.save(mroSparePart);
        }
        applicationEventPublisher.publishEvent(new SparePartsEvents.SparePartBasicInfoUpdatedEvent(oldPart, mroSparePart));
        return mroSparePart;
    }

    @Override
    @PreAuthorize("hasPermission(#partId,'delete')")
    public void delete(Integer partId) {
        MROSparePart sparePart = sparePartRepository.findOne(partId);
        List<MROAssetSparePart> mroAssetSpareParts = assetSparePartRepository.findBySparePart(sparePart);
        if (mroAssetSpareParts.size() > 0) {
            throw new CassiniException(messageSource.getMessage("spare_part_already_used_in_assets", null, "Spare part already used in assets", LocaleContextHolder.getLocale()));
        }
        sparePartRepository.delete(partId);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MROSparePart get(Integer partId) {
        MROSparePart sparePart = sparePartRepository.findOne(partId);
        return sparePart;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROSparePart> getAll() {
        return sparePartRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MROSparePart> getAllPartsByPageable(Pageable pageable) {
        Page<MROSparePart> parts = sparePartRepository.findAll(pageable);
        return parts;

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

    @Transactional
    public MROSparePart saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        MROSparePart sparePart = sparePartRepository.findOne(objectId);
        if (sparePart != null) {
            MROObjectAttribute mroObjectAttribute = new MROObjectAttribute();
            mroObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, mroObjectAttribute);

        }

        return sparePart;
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public MROObjectAttribute updateObjectAttribute(MROObjectAttribute attribute) {
        MROObjectAttribute oldValue = mroObjectAttributeRepository.findByObjectAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, MROObjectAttribute.class);
        MROObjectTypeAttribute mroObjectTypeAttribute = mroObjectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());

        if (mroObjectTypeAttribute.getMeasurement() != null) {
            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(mroObjectTypeAttribute.getMeasurement().getId());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(attribute.getMeasurementUnit().getId());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(mroObjectTypeAttribute.getMeasurement().getId());

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
        attribute = mroObjectAttributeRepository.save(attribute);

        MROSparePart part = sparePartRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new SparePartsEvents.SparePartAttributesUpdatedEvent(part, oldValue, attribute));
        return attribute;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MROSparePart> getAllPartsByPageable(Pageable pageable, SparePartCriteria criteria) {
        Predicate predicate = sparePartPredicateBuilder.build(criteria, QMROSparePart.mROSparePart);
        Page<MROSparePart> parts = sparePartRepository.findAll(predicate, pageable);
        return parts;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MROSparePart> getSparePartsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MROSparePartType type = sparePartTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return sparePartRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MROSparePartType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MROSparePartType> children = sparePartTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MROSparePartType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }


}

