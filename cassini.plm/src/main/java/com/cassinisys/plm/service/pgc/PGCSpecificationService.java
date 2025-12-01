package com.cassinisys.plm.service.pgc;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.Measurement;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.SpecificationEvents;
import com.cassinisys.plm.filtering.PGCSpecificationCriteria;
import com.cassinisys.plm.filtering.PGCSpecificationPredicateBuilder;
import com.cassinisys.plm.filtering.SpecificationSubstanceCriteria;
import com.cassinisys.plm.filtering.SpecificationSubstancePredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.pgc.*;
import com.cassinisys.plm.repo.pgc.*;
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
 * Created by GSR on 09-06-2020.
 */
@Service
public class PGCSpecificationService implements CrudService<PGCSpecification, Integer> {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PGCSpecificationRepository specificationRepository;
    @Autowired
    private PGCSpecificationTypeRepository specificationTypeRepository;
    @Autowired
    private PGCObjectAttributeRepository pgcObjectAttributeRepository;
    @Autowired
    private PGCSpecificationPredicateBuilder specificationPredicateBuilder;
    @Autowired
    private PGCSubstanceRepository substanceRepository;
    @Autowired
    private SpecificationSubstancePredicateBuilder specificationSubstancePredicateBuilder;
    @Autowired
    private PGCSpecificationSubstanceRepository specificationSubstanceRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PGCDeclarationSpecificationRepository pgcDeclarationSpecificationRepository;
    @Autowired
    private SubstanceService substanceService;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#specification,'create')")
    public PGCSpecification create(PGCSpecification specification) {
        List<PGCObjectAttribute> pgcObjectAttributes = specification.getPgcObjectAttributes();
        List<ObjectAttribute> objectAttributes = specification.getObjectAttributes();
        PGCSpecification existSpecification = specificationRepository.findByNumber(specification.getNumber());
        PGCSpecification existSpecificationName = specificationRepository.findByName(specification.getName());
        if (existSpecification != null) {
            String message = messageSource.getMessage("specification_number_already_exists", null, "{0} Specification Number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existSpecification.getNumber());
            throw new CassiniException(result);
        }
        if (existSpecificationName != null) {
            String message = messageSource.getMessage("specification_name_already_exists", null, "Specification name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existSpecificationName.getName());
            throw new CassiniException(result);
        }
        autoNumberService.saveNextNumber(specification.getType().getAutoNumberSource().getId(), specification.getNumber());
        specification = specificationRepository.save(specification);
        substanceService.savePGCObjectAttributes(specification.getId(), pgcObjectAttributes);
        substanceService.saveObjectAttributes(specification.getId(), objectAttributes);
        applicationEventPublisher.publishEvent(new SpecificationEvents.SpecificationCreatedEvent(specification));
        return specification;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#specification.id ,'edit')")
    public PGCSpecification update(PGCSpecification specification) {
        PGCSpecification oldSpecification = JsonUtils.cloneEntity(specificationRepository.findOne(specification.getId()), PGCSpecification.class);
        specification = specificationRepository.save(specification);
        applicationEventPublisher.publishEvent(new SpecificationEvents.SpecificationBasicInfoUpdatedEvent(oldSpecification, specification));
        return specification;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        PGCSpecification specification = specificationRepository.findOne(id);
        List<PGCDeclarationSpecification> declarationSpecifications = pgcDeclarationSpecificationRepository.findBySpecification(specification);
        if (declarationSpecifications.size() > 0) {
            throw new CassiniException(messageSource.getMessage("specification_already_used_in_declarations", null, "Specification already used in declarations", LocaleContextHolder.getLocale()));
        }
        specificationRepository.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PGCSpecification get(Integer id) {
        PGCSpecification specification = specificationRepository.findOne(id);
        return specification;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCSpecification> getAll() {
        return specificationRepository.findAll();
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCSpecification> findMultiple(List<Integer> ids) {
        return specificationRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PGCSpecification> getAllSpecifications(Pageable pageable, PGCSpecificationCriteria specificationCriteria) {
        Predicate predicate = specificationPredicateBuilder.build(specificationCriteria, QPGCSpecification.pGCSpecification);
        Page<PGCSpecification> specifications = specificationRepository.findAll(predicate, pageable);
        return specifications;
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getTabCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setSubstances(specificationSubstanceRepository.getSpecificationSubstacesCount(id));
        return detailsDto;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PGCSpecification> getObjectsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        PGCSpecificationType type = specificationTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return specificationRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, PGCSpecificationType type) {
        if (type != null) {
            collector.add(type.getId());
            List<PGCSpecificationType> children = specificationTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (PGCSpecificationType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PGCSubstance> getAllSubstances(Pageable pageable, SpecificationSubstanceCriteria specificationSubstanceCriteria) {
        Predicate predicate = specificationSubstancePredicateBuilder.build(specificationSubstanceCriteria, QPGCSubstance.pGCSubstance);
        Page<PGCSubstance> substances = substanceRepository.findAll(predicate, pageable);
        return substances;
    }

    @Transactional
    public PGCSpecificationSubstance createSpecSubstance(Integer id, PGCSpecificationSubstance specificationSubstance) {
        PGCSpecification specification = specificationRepository.findOne(id);
        specificationSubstance.setSpecification(id);
        MeasurementUnit measurementUnit = measurementUnitRepository.findOne(specificationSubstance.getUom());
        MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(measurementUnit.getMeasurement());

        if (!measurementUnit.getId().equals(baseUnit.getId())) {
            specificationSubstance.setThresholdMass(specificationSubstance.getThresholdMass() / measurementUnit.getConversionFactor());
        } else {
            specificationSubstance.setThresholdMass(specificationSubstance.getThresholdMass());
        }
        specificationSubstance = specificationSubstanceRepository.save(specificationSubstance);
        applicationEventPublisher.publishEvent(new SpecificationEvents.SubstanceAddEvent(specificationSubstance.getSubstance(), specification));

        return specificationSubstance;
    }

    @Transactional
    public List<PGCSpecificationSubstance> createSpecSubstances(Integer id, List<PGCSpecificationSubstance> specificationSubstances) {
        specificationSubstances.forEach(substance -> {
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(substance.getUom());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(measurementUnit.getMeasurement());

            if (!measurementUnit.getId().equals(baseUnit.getId())) {
                substance.setThresholdMass(substance.getThresholdMass() / measurementUnit.getConversionFactor());
            } else {
                substance.setThresholdMass(substance.getThresholdMass());
            }
            substance.setSpecification(id);
        });
        return specificationSubstanceRepository.save(specificationSubstances);
    }

    @Transactional
    public PGCSpecificationSubstance updateSpecSubstance(Integer id, PGCSpecificationSubstance specificationSubstance) {
        specificationSubstance.setSpecification(id);
        MeasurementUnit measurementUnit = measurementUnitRepository.findOne(specificationSubstance.getUom());
        MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(measurementUnit.getMeasurement());

        if (!measurementUnit.getId().equals(baseUnit.getId())) {
            specificationSubstance.setThresholdMass(specificationSubstance.getThresholdMass() / measurementUnit.getConversionFactor());
        } else {
            specificationSubstance.setThresholdMass(specificationSubstance.getThresholdMass());
        }
        return specificationSubstanceRepository.save(specificationSubstance);
    }

    @Transactional(readOnly = true)
    public List<PGCSpecificationSubstance> getAllSpecSubstances(Integer id) {
        List<PGCSpecificationSubstance> specificationSubstances = specificationSubstanceRepository.findBySpecification(id);
        specificationSubstances.forEach(pgcSpecificationSubstance -> {
            if (pgcSpecificationSubstance.getUom() != null) {
                MeasurementUnit measurementUnit = measurementUnitRepository.findOne(pgcSpecificationSubstance.getUom());
                if (measurementUnit != null) {
                    Measurement measurement = measurementRepository.findOne(measurementUnit.getMeasurement());
                    MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(measurement.getId());
                    if (!baseUnit.getId().equals(measurementUnit.getId())) {
                        pgcSpecificationSubstance.setThresholdMass(pgcSpecificationSubstance.getThresholdMass() * measurementUnit.getConversionFactor());
                    } else {
                        pgcSpecificationSubstance.setThresholdMass(pgcSpecificationSubstance.getThresholdMass());
                    }
                    pgcSpecificationSubstance.setUnitName(measurementUnit.getName());
                    pgcSpecificationSubstance.setUnitSymbol(measurementUnit.getSymbol());
                }
            }
        });
        return specificationSubstances;
    }

    @Transactional
    public void deleteSpecSubstance(Integer specId, Integer subId) {
        PGCSubstance substance = substanceRepository.findOne(subId);
        PGCSpecification specification = specificationRepository.findOne(specId);
        applicationEventPublisher.publishEvent(new SpecificationEvents.SubstanceDeletedEvent(substance, specification));
        specificationSubstanceRepository.deleteBySpecificationAndSubstance(specId, substance);
    }
}
