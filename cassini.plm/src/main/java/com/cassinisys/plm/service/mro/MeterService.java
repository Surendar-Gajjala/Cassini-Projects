package com.cassinisys.plm.service.mro;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.MeterEvents;
import com.cassinisys.plm.filtering.MeterCriteria;
import com.cassinisys.plm.filtering.MeterPredicateBuilder;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.repo.mro.MROAssetMeterRepository;
import com.cassinisys.plm.repo.mro.MROMeterRepository;
import com.cassinisys.plm.repo.mro.MROMeterTypeRepository;
import com.cassinisys.plm.repo.mro.MROObjectAttributeRepository;
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
import java.util.List;

/**
 * Created by Lenovo on 18-11-2020.
 */
@Service
public class MeterService implements CrudService<MROMeter, Integer> {

    @Autowired
    private MROMeterRepository mroMeterRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MeterPredicateBuilder meterPredicateBuilder;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private MROMeterTypeRepository meterTypeRepository;
    @Autowired
    private MROAssetMeterRepository mroAssetMeterRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private MROObjectAttributeRepository mroObjectAttributeRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mroMeter,'create')")
    public MROMeter create(MROMeter mroMeter) {
        MROMeter existMeterNumber = mroMeterRepository.findByNumber(mroMeter.getNumber());
        if (existMeterNumber != null) {
            String message = messageSource.getMessage("meter_number_already_exists", null, "{0} Meter Number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existMeterNumber.getNumber());
            throw new CassiniException(result);
        }
        MROMeter existingMeter = mroMeterRepository.findByName(mroMeter.getName());
        if (existingMeter != null) {
            throw new CassiniException(messageSource.getMessage("meter_already_exist", null, "Meter name already exist", LocaleContextHolder.getLocale()));

        } else {
            autoNumberService.saveNextNumber(mroMeter.getType().getAutoNumberSource().getId(), mroMeter.getNumber());
            mroMeter = mroMeterRepository.save(mroMeter);
        }
        applicationEventPublisher.publishEvent(new MeterEvents.MeterCreatedEvent(mroMeter));
        return mroMeter;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mroMeter.id ,'edit')")
    public MROMeter update(MROMeter mroMeter) {
        MROMeter oldMeter = JsonUtils.cloneEntity(mroMeterRepository.findOne(mroMeter.getId()), MROMeter.class);
        MROMeter existingMeter = mroMeterRepository.findByName(mroMeter.getName());
        if (existingMeter != null && !existingMeter.getId().equals(mroMeter.getId())) {
            throw new CassiniException(messageSource.getMessage("meter_already_exist", null, "Meter name already exist", LocaleContextHolder.getLocale()));

        } else {
            mroMeter = mroMeterRepository.save(mroMeter);
        }
        applicationEventPublisher.publishEvent(new MeterEvents.MeterBasicInfoUpdatedEvent(oldMeter, mroMeter));
        return mroMeter;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#meterId,'delete')")
    public void delete(Integer meterId) {
        List<MROAssetMeter> mroAssetMeter = mroAssetMeterRepository.findByMeter(meterId);
        if (mroAssetMeter.size() > 0) {
            throw new CassiniException(messageSource.getMessage("meter_already_exist_in_assets", null, "Meter already used in assets ", LocaleContextHolder.getLocale()));

        } else {
            mroMeterRepository.delete(meterId);
        }

    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MROMeter get(Integer meterId) {
        MROMeter mroMeter = mroMeterRepository.findOne(meterId);
        if (mroMeter.getQom() != null) {
            mroMeter.setMeasurementName(measurementRepository.findOne(mroMeter.getQom()).getName());
        }
        if (mroMeter.getUom() != null) {
            mroMeter.setUnitName(measurementUnitRepository.findOne(mroMeter.getUom()).getName());
        }
        return mroMeter;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROMeter> getAll() {
        return mroMeterRepository.findAll();
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
    public Page<MROMeter> getAllMetersByPageable(Pageable pageable, MeterCriteria criteria) {
        Predicate predicate = meterPredicateBuilder.build(criteria, QMROMeter.mROMeter);
        Page<MROMeter> meters = mroMeterRepository.findAll(predicate, pageable);
        meters.getContent().forEach(meter -> {
            if (meter.getQom() != null) {
                meter.setMeasurementName(measurementRepository.findOne(meter.getQom()).getName());
            }
            if (meter.getUom() != null) {
                meter.setUnitName(measurementUnitRepository.findOne(meter.getUom()).getName());
            }
            List<MROAssetMeter> mroAssetMeter = mroAssetMeterRepository.findByMeter(meter.getId());
            if (mroAssetMeter.size() > 0) {
                meter.setAssignedAsset("true");
            } else {
                meter.setAssignedAsset("false");
            }

        });
        return meters;
    }

    public Page<MROMeter> getMetersByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MROMeterType type = meterTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return mroMeterRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MROMeterType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MROMeterType> children = meterTypeRepository.findByParentTypeOrderByCreatedDateAsc(type.getId());
            for (MROMeterType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }
}
