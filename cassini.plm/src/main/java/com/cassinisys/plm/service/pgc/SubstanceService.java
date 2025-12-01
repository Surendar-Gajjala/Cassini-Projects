package com.cassinisys.plm.service.pgc;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.SubstanceEvents;
import com.cassinisys.plm.filtering.SubstanceCriteria;
import com.cassinisys.plm.filtering.SubstancePredicateBuilder;
import com.cassinisys.plm.model.pgc.*;
import com.cassinisys.plm.repo.pgc.PGCObjectAttributeRepository;
import com.cassinisys.plm.repo.pgc.PGCSpecificationSubstanceRepository;
import com.cassinisys.plm.repo.pgc.PGCSubstanceRepository;
import com.cassinisys.plm.repo.pgc.PGCSubstanceTypeRepository;
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
public class SubstanceService implements CrudService<PGCSubstance, Integer> {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PGCSubstanceRepository substanceRepository;
    @Autowired
    private PGCSubstanceTypeRepository substanceTypeRepository;
    @Autowired
    private PGCObjectAttributeRepository pgcObjectAttributeRepository;
    @Autowired
    private SubstancePredicateBuilder substancePredicateBuilder;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PGCSpecificationSubstanceRepository pgcSpecificationSubstanceRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#substance,'create')")
    public PGCSubstance create(PGCSubstance substance) {
        List<PGCObjectAttribute> pgcObjectAttributes = substance.getPgcObjectAttributes();
        List<ObjectAttribute> objectAttributes = substance.getObjectAttributes();
        PGCSubstance existSubstance = substanceRepository.findByNumber(substance.getNumber());
        PGCSubstance existSubstanceName = substanceRepository.findByName(substance.getName());
        if (existSubstance != null) {
            String message = messageSource.getMessage("substance_number_already_exists", null, "{0} Substance Number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existSubstance.getNumber());
            throw new CassiniException(result);
        }
        if (existSubstanceName != null) {
            String message = messageSource.getMessage("substance_name_already_exists", null, "{0} Substance Name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existSubstanceName.getName());
            throw new CassiniException(result);
        }
        autoNumberService.saveNextNumber(substance.getType().getAutoNumberSource().getId(), substance.getNumber());
        substance = substanceRepository.save(substance);
        savePGCObjectAttributes(substance.getId(), pgcObjectAttributes);
        saveObjectAttributes(substance.getId(), objectAttributes);
        applicationEventPublisher.publishEvent(new SubstanceEvents.SubstanceCreatedEvent(substance));
        return substance;
    }

    public void savePGCObjectAttributes(Integer id, List<PGCObjectAttribute> pgcObjectAttributes) {
        for (PGCObjectAttribute attribute : pgcObjectAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(id, attribute.getId().getAttributeDef()));
                pgcObjectAttributeRepository.save(attribute);
            }
        }
    }

    public void saveObjectAttributes(Integer id, List<ObjectAttribute> objectAttributes) {
        for (ObjectAttribute attribute : objectAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(id, attribute.getId().getAttributeDef()));
                objectAttributeRepository.save(attribute);
            }
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#substance.id ,'edit')")
    public PGCSubstance update(PGCSubstance substance) {
        PGCSubstance oldSubstance = JsonUtils.cloneEntity(substanceRepository.findOne(substance.getId()), PGCSubstance.class);
        substance = substanceRepository.save(substance);
        applicationEventPublisher.publishEvent(new SubstanceEvents.SubstanceBasicInfoUpdatedEvent(oldSubstance, substance));
        return substance;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        PGCSubstance substance = substanceRepository.findOne(id);
        List<PGCSpecificationSubstance> specificationSubstances = pgcSpecificationSubstanceRepository.findBySubstance(substance);
        if (specificationSubstances.size() > 0) {
            throw new CassiniException(messageSource.getMessage("substance_already_used_in_specifications", null, "Substance already used in specifications", LocaleContextHolder.getLocale()));
        }
        substanceRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PGCSubstance get(Integer id) {
        PGCSubstance substance = substanceRepository.findOne(id);
        return substance;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCSubstance> getAll() {
        return substanceRepository.findAll();
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCSubstance> findMultiple(List<Integer> ids) {
        return substanceRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PGCSubstance> getAllSubstances(Pageable pageable, SubstanceCriteria substanceCriteria) {
        Predicate predicate = substancePredicateBuilder.build(substanceCriteria, QPGCSubstance.pGCSubstance);
        Page<PGCSubstance> substances = substanceRepository.findAll(predicate, pageable);
        return substances;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PGCSubstance> getObjectsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        PGCSubstanceType type = substanceTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return substanceRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, PGCSubstanceType type) {
        if (type != null) {
            collector.add(type.getId());
            List<PGCSubstanceType> children = substanceTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (PGCSubstanceType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

}
