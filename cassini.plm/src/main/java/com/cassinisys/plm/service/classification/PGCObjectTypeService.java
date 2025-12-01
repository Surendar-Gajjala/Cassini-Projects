package com.cassinisys.plm.service.classification;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.cassinisys.platform.service.core.TypeSystem;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.event.SubstanceEvents;
import com.cassinisys.plm.model.pgc.*;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mro.MROObjectAttributeRepository;
import com.cassinisys.plm.repo.pgc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by GSR on 03-06-2020.
 */
@Service
public class PGCObjectTypeService implements CrudService<PGCObjectType, Integer>,
        TypeSystem, ClassificationTypeService<PGCObjectType, PGCObjectTypeAttribute> {

    @Autowired
    private PGCObjectTypeRepository pgcObjectTypeRepository;
    @Autowired
    private PGCObjectTypeAttributeRepository pgcObjectTypeAttributeRepository;
    @Autowired
    private PGCObjectAttributeRepository pgcObjectAttributeRepository;
    @Autowired
    private PGCObjectRepository pgcObjectRepository;
    @Autowired
    private PGCSubstanceTypeRepository substanceTypeRepository;
    @Autowired
    private PGCSubstanceGroupTypeRepository substanceGroupTypeRepository;
    @Autowired
    private PGCSpecificationTypeRepository specificationTypeRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MROObjectAttributeRepository mroObjectAttributeRepository;
    @Autowired
    private PGCDeclarationTypeRepository declarationTypeRepository;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private PGCObjectTypeService pgcObjectTypeService;
    @Autowired
    private PGCSubstanceRepository substanceRepository;
    @Autowired
    private PGCSubstanceGroupRepository substanceGroupRepository;
    @Autowired
    private PGCSpecificationRepository specificationRepository;
    @Autowired
    private PGCDeclarationRepository declarationRepository;
    @Autowired
    private MessageSource messageSource;


    @PostConstruct
    public void InitPGCTypeService() {
        objectTypeService.registerTypeSystem("PGCObjectType", new PGCObjectTypeService());
    }


    @Override
    @PreAuthorize("hasPermission(#pgcObjectType,'create')")
    public PGCObjectType create(PGCObjectType pgcObjectType) {
        PGCObjectType resourceType = pgcObjectTypeRepository.save(pgcObjectType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.PGCOBJECTTYPE, resourceType));
        return resourceType;
    }

    @Override
    @PreAuthorize("hasPermission(#resourceType.id ,'edit')")
    public PGCObjectType update(PGCObjectType resourceType) {
        PGCObjectType pgcObjectType = pgcObjectTypeRepository.findOne(resourceType.getId());
        return pgcObjectTypeRepository.save(pgcObjectType);
    }

    @Override
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        pgcObjectTypeRepository.delete(id);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PGCObjectType get(Integer id) {
        return pgcObjectTypeRepository.findOne(id);
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCObjectType> getAll() {
        return pgcObjectTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PGCObjectType> findMultipleTypes(List<Integer> ids) {
        return pgcObjectTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<PGCObjectType> getTypeTree() {
        List<PGCObjectType> types = pgcObjectTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PGCObjectType type : types) {
            visitResourceTypeChildren(type);
        }
        return types;
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCObjectType> getRootTypes() {
        return pgcObjectTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
    }

    @Override
    public List<PGCObjectType> getChildren(Integer parent) {
        return pgcObjectTypeRepository.findByParentTypeOrderByCreatedDateAsc(parent);
    }

    @Override
    public List<PGCObjectType> getClassificationTree() {
        List<PGCObjectType> types = getRootTypes();
        for (PGCObjectType type : types) {
            visitResourceTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PGCObjectType> getTypeChildren(Integer id) {
        return pgcObjectTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitResourceTypeChildren(PGCObjectType parent) {
        List<PGCObjectType> childrens = getTypeChildren(parent.getId());
        for (PGCObjectType child : childrens) {
            visitResourceTypeChildren(child);
        }
        parent.setChildren(childrens);
    }

    @Override
    public PGCObjectTypeAttribute createAttribute(PGCObjectTypeAttribute attribute) {
        List<PGCObjectTypeAttribute> qualityTypeAttributes = pgcObjectTypeAttributeRepository.findByTypeOrderBySeq(attribute.getType());
        if (qualityTypeAttributes.size() >= 0) {
            attribute.setSeq(qualityTypeAttributes.size() + 1);
        }
        attribute = pgcObjectTypeAttributeRepository.save(attribute);
        PLMObjectType objectType = getAttributeObjectType(attribute);
        PGCObjectType pgcObjectType = pgcObjectTypeRepository.findOne(attribute.getType());
        if (attribute.getDataType().toString().equals("FORMULA")) {
            createFormulaAttribute(attribute, pgcObjectType);
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(objectType, attribute));
        return attribute;

    }

    private PLMObjectType getAttributeObjectType(PGCObjectTypeAttribute attribute) {
        PLMObjectType objectType = null;
        if (attribute.getObjectType().name().equals("PGCSUBSTANCETYPE")) {
            objectType = PLMObjectType.PGCSUBSTANCETYPE;
        } else if (attribute.getObjectType().name().equals("PGCSUBSTANCEGROUPTYPE")) {
            objectType = PLMObjectType.PGCSUBSTANCEGROUPTYPE;
        } else if (attribute.getObjectType().name().equals("PGCSPECIFICATIONTYPE")) {
            objectType = PLMObjectType.PGCSPECIFICATIONTYPE;
        } else if (attribute.getObjectType().name().equals("PGCDECLARATIONTYPE")) {
            objectType = PLMObjectType.PGCDECLARATIONTYPE;
        }
        return objectType;
    }

    private void createFormulaAttribute(PGCObjectTypeAttribute attribute, PGCObjectType pgcObjectType) {
        if (attribute.getObjectType().name().equals("PGCSUBSTANCETYPE")) {
            PGCSubstanceType substanceType = substanceTypeRepository.findOne(pgcObjectType.getId());
            List<PGCSubstance> substances = substanceRepository.findByType(substanceType);
            for (PGCSubstance substance : substances) {
                PGCObjectAttribute objectAttribute = new PGCObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(substance.getId(), attribute.getId()));
                pgcObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("PGCSPECIFICATIONTYPE")){
            PGCSpecificationType specificationType = specificationTypeRepository.findOne(pgcObjectType.getId());
            List<PGCSpecification> specifications = specificationRepository.findByType(specificationType);
            for (PGCSpecification specification : specifications) {
                PGCObjectAttribute objectAttribute = new PGCObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(specification.getId(), attribute.getId()));
                pgcObjectAttributeRepository.save(objectAttribute);
            }
        }else if (attribute.getObjectType().name().equals("PGCDECLARATIONTYPE")){
            PGCDeclarationType declarationType = declarationTypeRepository.findOne(pgcObjectType.getId());
            List<PGCDeclaration> declarations = declarationRepository.findByType(declarationType);
            for (PGCDeclaration declaration : declarations) {
                PGCObjectAttribute objectAttribute = new PGCObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(declaration.getId(), attribute.getId()));
                pgcObjectAttributeRepository.save(objectAttribute);
            }
        }
    }

    @Override
    public PGCObjectTypeAttribute updateAttribute(PGCObjectTypeAttribute attribute) {
        PGCObjectTypeAttribute substanceGroupTypeAttribute = pgcObjectTypeAttributeRepository.findOne(attribute.getId());
        PLMObjectType objectType = getAttributeObjectType(attribute);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(objectType, substanceGroupTypeAttribute, attribute));
        return pgcObjectTypeAttributeRepository.save(attribute);
    }

    @Override
    public void deleteAttribute(Integer id) {
        PGCObjectTypeAttribute attribute = pgcObjectTypeAttributeRepository.findOne(id);
        List<PGCObjectTypeAttribute> substanceGroupTypeAttributes = pgcObjectTypeAttributeRepository.findByTypeOrderBySeq(attribute.getType());
        if (substanceGroupTypeAttributes.size() > 0) {
            for (PGCObjectTypeAttribute substanceGroupTypeAttribute : substanceGroupTypeAttributes) {
                if (substanceGroupTypeAttribute.getSeq() > attribute.getSeq()) {
                    substanceGroupTypeAttribute.setSeq(substanceGroupTypeAttribute.getSeq() - 1);
                    substanceGroupTypeAttribute = pgcObjectTypeAttributeRepository.save(substanceGroupTypeAttribute);
                }
            }
        }
        PLMObjectType objectType = getAttributeObjectType(attribute);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(objectType, attribute));
        pgcObjectTypeAttributeRepository.delete(id);
    }

    @Override
    public PGCObjectTypeAttribute getAttribute(Integer id) {
        return pgcObjectTypeAttributeRepository.findOne(id);
    }


    @Transactional
    public PGCObjectAttribute createPGCObjectAttribute(PGCObjectAttribute attribute) {
        return pgcObjectAttributeRepository.save(attribute);
    }


    @Transactional
    public PGCObjectAttribute updatePGCObjectAttribute(PGCObjectAttribute attribute) {
        PGCObjectAttribute oldValue = pgcObjectAttributeRepository.findByObjectAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PGCObjectAttribute.class);
        attribute = pgcObjectAttributeRepository.save(attribute);
        PGCObject pgcObject = pgcObjectRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new SubstanceEvents.SubstanceAttributesUpdatedEvent("pgc", pgcObject.getId(), pgcObject.getObjectType(), oldValue, attribute));
        return attribute;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PGCObjectTypeAttribute> getAttributes(Integer typeId, Boolean hier) {
        List<PGCObjectTypeAttribute> mesObjectTypeAttributes = new ArrayList<>();
        if (!hier) {
            mesObjectTypeAttributes = pgcObjectTypeAttributeRepository.findByTypeOrderBySeq(typeId);
        } else {
            mesObjectTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return mesObjectTypeAttributes;
    }

    @Transactional(readOnly = true)
    public List<PGCObjectTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<PGCObjectTypeAttribute> collector = new ArrayList<>();
        List<PGCObjectTypeAttribute> atts = pgcObjectTypeAttributeRepository.findByTypeOrderBySeq(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<PGCObjectTypeAttribute> collector, Integer typeId) {
        PGCObjectType objectType = pgcObjectTypeRepository.findOne(typeId);
        if (objectType != null) {
            Integer parentType = objectType.getParentType();
            if (parentType != null) {
                List<PGCObjectTypeAttribute> atts = pgcObjectTypeAttributeRepository.findByTypeOrderBySeq(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

		/*-------------------------   Substance Types ----------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#substanceType,'create')")
    public PGCSubstanceType createSubstanceType(PGCSubstanceType substanceType) {
        PGCSubstanceType mesSubstanceType = substanceTypeRepository.save(substanceType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.PGCSUBSTANCETYPE, mesSubstanceType));
        return mesSubstanceType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#substanceType.id ,'edit')")
    public PGCSubstanceType updateSubstanceType(Integer id, PGCSubstanceType substanceType) {
        PGCSubstanceType oldSubType = substanceTypeRepository.findOne(substanceType.getId());
        PGCSubstanceType existingMesType;
        if (substanceType.getParentType() == null) {
            existingMesType = substanceTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(substanceType.getName());
        } else {
            existingMesType = substanceTypeRepository.findByNameEqualsIgnoreCaseAndParentType(substanceType.getName(), substanceType.getParentType());
        }
        if ( existingMesType != null && !substanceType.getId().equals(existingMesType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMesType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.PGCSUBSTANCETYPE, oldSubType, substanceType));
        return substanceTypeRepository.save(substanceType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteSubstanceType(Integer id) {
        substanceTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PGCSubstanceType getSubstanceType(Integer id) {
        return substanceTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCSubstanceType> getAllSubstanceTypes() {
        return substanceTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCSubstanceType> findMultipleSubstanceTypes(List<Integer> ids) {
        return substanceTypeRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCSubstanceType> getSubstanceTypeTree() {
        List<PGCSubstanceType> types = substanceTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (PGCSubstanceType type : types) {
            visitSubstanceTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PGCSubstanceType> getSubstanceTypeChildren(Integer id) {
        return substanceTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitSubstanceTypeChildren(PGCSubstanceType parent) {
        List<PGCSubstanceType> childrens = getSubstanceTypeChildren(parent.getId());
        for (PGCSubstanceType child : childrens) {
            visitSubstanceTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }
    
    /* -------------------- SubstanceGroupType -------------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#substanceGroupType,'create')")
    public PGCSubstanceGroupType createSubstanceGroupType(PGCSubstanceGroupType substanceGroupType) {
        PGCSubstanceGroupType pgcSubstanceGroupType = substanceGroupTypeRepository.save(substanceGroupType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.PGCSUBSTANCEGROUPTYPE, pgcSubstanceGroupType));
        return pgcSubstanceGroupType;
    }


    @Transactional
    @PreAuthorize("hasPermission(#substanceGroupType.id ,'edit')")
    public PGCSubstanceGroupType updateSubstanceGroupType(Integer id, PGCSubstanceGroupType substanceGroupType) {
        PGCSubstanceGroupType oldSubstanceGroupType = substanceGroupTypeRepository.findOne(id);
        PGCSubstanceGroupType existingPgcType;
        if (substanceGroupType.getParentType() == null) {
            existingPgcType = substanceGroupTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(substanceGroupType.getName());
        } else {
            existingPgcType = substanceGroupTypeRepository.findByNameEqualsIgnoreCaseAndParentType(substanceGroupType.getName(), substanceGroupType.getParentType());
        }
        if ( existingPgcType != null && !substanceGroupType.getId().equals(existingPgcType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingPgcType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.PGCSUBSTANCEGROUPTYPE, oldSubstanceGroupType, substanceGroupType));
        return substanceGroupTypeRepository.save(substanceGroupType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteSubstanceGroupType(Integer id) {
        substanceGroupTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PGCSubstanceGroupType getSubstanceGroupType(Integer id) {
        return substanceGroupTypeRepository.findOne(id);
    }

    @Transactional
    public List<PGCSubstanceGroupType> getAllSubstanceGroupTypes() {
        return substanceGroupTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PGCSubstanceGroupType> findMultipleSubstanceGroupTypes(List<Integer> ids) {
        return substanceGroupTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCSubstanceGroupType> getSubstanceGroupTypeTree() {
        List<PGCSubstanceGroupType> types = substanceGroupTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (PGCSubstanceGroupType type : types) {
            visitSubstanceGroupTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PGCSubstanceGroupType> getSubstanceGroupTypeChildren(Integer id) {
        return substanceGroupTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitSubstanceGroupTypeChildren(PGCSubstanceGroupType parent) {
        List<PGCSubstanceGroupType> childrens = getSubstanceGroupTypeChildren(parent.getId());
        for (PGCSubstanceGroupType child : childrens) {
            visitSubstanceGroupTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    

    /* ----------------------------- mesObjectTypesTree -----------------------*/

    @Transactional
    public List<PGCObjectType> getObjectRootTypes() {
        return pgcObjectTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCObjectType> getMesObjectTypeTree() {
        List<PGCObjectType> types = getObjectRootTypes();
        for (PGCObjectType objectType : types) {
            visitObjectTypeChildren(objectType);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PGCObjectType> getObjectTypeChildren(Integer id) {
        return pgcObjectTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitObjectTypeChildren(PGCObjectType parent) {
        List<PGCObjectType> childrens = getObjectTypeChildren(parent.getId());
        for (PGCObjectType child : childrens) {
            visitObjectTypeChildren(child);
        }
        parent.setChildren(childrens);
    }
    


    		/*-------------------------   Specification Types ----------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#specificationType,'create')")
    public PGCSpecificationType createSpecificationType(PGCSpecificationType specificationType) {
        PGCSpecificationType pgcSpecificationType = specificationTypeRepository.save(specificationType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.PGCSPECIFICATIONTYPE, pgcSpecificationType));
        return pgcSpecificationType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#specificationType.id ,'edit')")
    public PGCSpecificationType updateSpecificationType(Integer id, PGCSpecificationType specificationType) {
        PGCSpecificationType oldSpecificationType = specificationTypeRepository.findOne(specificationType.getId());
        PGCSpecificationType existingPgcType;
        if (specificationType.getParentType() == null) {
            existingPgcType = specificationTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(specificationType.getName());
        } else {
            existingPgcType = specificationTypeRepository.findByNameEqualsIgnoreCaseAndParentType(specificationType.getName(), specificationType.getParentType());
        }
        if ( existingPgcType != null && !specificationType.getId().equals(existingPgcType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingPgcType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.PGCSPECIFICATIONTYPE, oldSpecificationType, specificationType));
        return specificationTypeRepository.save(specificationType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteSpecificationType(Integer id) {
        specificationTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PGCSpecificationType getSpecificationType(Integer id) {
        return specificationTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<PGCSpecificationType> getAllSpecificationTypes() {
        return specificationTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PGCSpecificationType> findMultipleSpecificationTypes(List<Integer> ids) {
        return specificationTypeRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCSpecificationType> getSpecificationTypeTree() {
        List<PGCSpecificationType> types = specificationTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (PGCSpecificationType type : types) {
            visitSpecificationTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PGCSpecificationType> getSpecificationTypeChildren(Integer id) {
        return specificationTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitSpecificationTypeChildren(PGCSpecificationType parent) {
        List<PGCSpecificationType> childrens = getSpecificationTypeChildren(parent.getId());
        for (PGCSpecificationType child : childrens) {
            visitSpecificationTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

        /* -------------------- declarationType -------------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#declarationType,'create')")
    public PGCDeclarationType createDeclarationType(PGCDeclarationType declarationType) {
        PGCDeclarationType pgcDeclarationType = declarationTypeRepository.save(declarationType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.PGCDECLARATIONTYPE, pgcDeclarationType));
        return pgcDeclarationType;
    }


    @Transactional
    @PreAuthorize("hasPermission(#declarationType.id ,'edit')")
    public PGCDeclarationType updateDeclarationType(Integer id, PGCDeclarationType declarationType) {
        PGCDeclarationType oldDeclarationType = declarationTypeRepository.findOne(id);
        PGCDeclarationType existingPgcType;
        if (declarationType.getParentType() == null) {
            existingPgcType = declarationTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(declarationType.getName());
        } else {
            existingPgcType = declarationTypeRepository.findByNameEqualsIgnoreCaseAndParentType(declarationType.getName(), declarationType.getParentType());
        }
        if ( existingPgcType != null && !declarationType.getId().equals(existingPgcType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingPgcType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.PGCDECLARATIONTYPE, oldDeclarationType, declarationType));
        return declarationTypeRepository.save(declarationType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteDeclarationType(Integer id) {
        declarationTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PGCDeclarationType getDeclarationType(Integer id) {
        return declarationTypeRepository.findOne(id);
    }

    @Transactional
    public List<PGCDeclarationType> getAllDeclarationTypes() {
        return declarationTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PGCDeclarationType> findMultipleDeclarationTypes(List<Integer> ids) {
        return declarationTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCDeclarationType> getDeclarationTypeTree() {
        List<PGCDeclarationType> types = declarationTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (PGCDeclarationType type : types) {
            visitDeclarationTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PGCDeclarationType> getDeclarationTypeChildren(Integer id) {
        return declarationTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitDeclarationTypeChildren(PGCDeclarationType parent) {
        List<PGCDeclarationType> childrens = getDeclarationTypeChildren(parent.getId());
        for (PGCDeclarationType child : childrens) {
            visitDeclarationTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    public PGCObjectTypesDto getAllObjectTypesTree() {
        PGCObjectTypesDto pgcObjectTypesDto = new PGCObjectTypesDto();
        pgcObjectTypesDto.getSubstanceTypes().addAll(pgcObjectTypeService.getSubstanceTypeTree());
        pgcObjectTypesDto.getSubstanceGroupTypes().addAll(getSubstanceGroupTypeTree());
        pgcObjectTypesDto.getSpecificationTypes().addAll(pgcObjectTypeService.getSpecificationTypeTree());
        pgcObjectTypesDto.getDeclarationTypes().addAll(pgcObjectTypeService.getDeclarationTypeTree());
        return pgcObjectTypesDto;
    }

    @Transactional(readOnly = true)
    public Object getPGCObjectTypeIdAndType(Integer id, PLMObjectType objectType) {
        Object object = null;
        if (objectType.equals(PLMObjectType.PGCSUBSTANCETYPE)) {
            object = substanceTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.PGCSUBSTANCEGROUPTYPE)) {
            object = substanceGroupTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.PGCSPECIFICATIONTYPE)) {
            object = specificationTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.PGCDECLARATIONTYPE)) {
            object = declarationTypeRepository.findOne(id);
        }
        return object;
    }

    @Transactional(readOnly = true)
    public Integer getObjectsByType(Integer id, PLMObjectType objectType) {
        Integer count = 0;
        if (objectType.equals(PLMObjectType.PGCSUBSTANCETYPE)) {
            PGCSubstanceType substanceType = substanceTypeRepository.findOne(id);
            count = substanceRepository.findByType(substanceType).size();
        } else if (objectType.equals(PLMObjectType.PGCSUBSTANCEGROUPTYPE)) {
            PGCSubstanceGroupType groupType = substanceGroupTypeRepository.findOne(id);
            count = substanceGroupRepository.findByType(groupType).size();
        } else if (objectType.equals(PLMObjectType.PGCSPECIFICATIONTYPE)) {
            PGCSpecificationType specificationType = specificationTypeRepository.findOne(id);
            count = specificationRepository.findByType(specificationType).size();
        } else if (objectType.equals(PLMObjectType.PGCDECLARATIONTYPE)) {
            PGCDeclarationType declarationType = declarationTypeRepository.findOne(id);
            count = declarationRepository.findByType(declarationType).size();
        }
        return count;
    }

    @Override
    public Boolean isSubtypeOf(String s1, Object s2, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        pgcObjectTypeRepository = webApplicationContext.getBean(PGCObjectTypeRepository.class);
        PGCObjectType itemType = (PGCObjectType) s2;
        if (subTypeId != null && checkWithId(itemType, subTypeId)) {
            return true;
        }
        if (itemType.getName().equalsIgnoreCase(s1)) {
            return true;
        } else {
            if (itemType.getParentType() != null)
                return compareWithParent(pgcObjectTypeRepository.findOne(itemType.getParentType()), s1);
        }
        return false;
    }

    public Boolean checkWithId(PGCObjectType plmItemType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        pgcObjectTypeRepository = webApplicationContext.getBean(PGCObjectTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmItemType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = checkWithId(pgcObjectTypeRepository.findOne(plmItemType.getParentType()), typeId);
        }
        return flag;
    }


    private Boolean compareWithParent(PGCObjectType plmItemType, String s1) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        pgcObjectTypeRepository = webApplicationContext.getBean(PGCObjectTypeRepository.class);
        Boolean flag = false;
        if (plmItemType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = compareWithParent(pgcObjectTypeRepository.findOne(plmItemType.getParentType()), s1);
        }
        return flag;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PGCObjectTypeAttribute> getTypeAttributes(Integer typeId, Boolean hier) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        pgcObjectTypeAttributeRepository = webApplicationContext.getBean(PGCObjectTypeAttributeRepository.class);
        pgcObjectTypeRepository = webApplicationContext.getBean(PGCObjectTypeRepository.class);
        List<PGCObjectTypeAttribute> mesObjectTypeAttributes = new ArrayList<>();
        if (!hier) {
            mesObjectTypeAttributes = pgcObjectTypeAttributeRepository.findByTypeOrderBySeq(typeId);
        } else {
            mesObjectTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return mesObjectTypeAttributes;
    }

    @Transactional(readOnly = true)
    public List<PGCObjectAttribute> getUsedPGCObjectAttributes(Integer attributeId) {
        return pgcObjectAttributeRepository.findByAttributeDef(attributeId);
    }

}

