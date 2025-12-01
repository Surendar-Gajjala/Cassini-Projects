package com.cassinisys.plm.service.req;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.cassinisys.platform.service.core.TypeSystem;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.model.mes.MESObjectType;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PMObjectType;
import com.cassinisys.plm.model.pm.PMType;
import com.cassinisys.plm.model.req.*;
import com.cassinisys.plm.repo.mes.MESObjectTypeRepository;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.req.*;
import com.cassinisys.plm.service.classification.ClassificationTypeService;
import com.cassinisys.plm.service.plm.ItemTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
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
public class RequirementTypeService implements CrudService<PLMRequirementObjectType, Integer>,
        TypeSystem, ClassificationTypeService<PLMRequirementObjectType, PLMRequirementObjectTypeAttribute> {

    @Autowired
    private MESObjectTypeRepository objectTypeRepository;
    @Autowired
    private PLMRequirementObjectTypeAttributeRepository requirementObjectTypeAttributeRepository;
    @Autowired
    private PLMRequirementObjectAttributeRepository requirementObjectAttributeRepository;
    @Autowired
    private PLMRequirementDocumentTypeRepository requirementDocumentTypeRepository;
    @Autowired
    private PLMRequirementTypeRepository requirementTypeRepository;
    @Autowired
    private PLMRequirementObjectTypeRepository requirementObjectTypeRepository;
    @Autowired
    private PMObjectTypeRepository pmObjectTypeRepository;
    @Autowired
    private PLMRequirementObjectRepository requirementObjectRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProgramFileRepository programFileRepository;
    @Autowired
    private ProjectFileRepository projectFileRepository;
    @Autowired
    private ActivityFileRepository activityFileRepository;
    @Autowired
    private TaskFileRepository taskFileRepository;
    @Autowired
    private MessageSource messageSource;

    @PostConstruct
    public void InitReqTypeService() {
        objectTypeService.registerTypeSystem("reqType", new RequirementTypeService());
    }

    @Override
    public PLMRequirementObjectType create(PLMRequirementObjectType productionResourceType) {
        PLMRequirementObjectType resourceType = requirementObjectTypeRepository.save(productionResourceType);
        return resourceType;
    }

    @Override
    public PLMRequirementObjectType update(PLMRequirementObjectType resourceType) {
        PLMRequirementObjectType productionResourceType = requirementObjectTypeRepository.findOne(resourceType.getId());
        return requirementObjectTypeRepository.save(productionResourceType);
    }

    @Override
    public void delete(Integer id) {
        requirementObjectTypeRepository.delete(id);
    }

    @Override
    public PLMRequirementObjectType get(Integer id) {
        return requirementObjectTypeRepository.findOne(id);
    }

    @Override
    public List getTypeAttributes(Integer typeId, Boolean hier) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        requirementObjectTypeAttributeRepository = webApplicationContext.getBean(PLMRequirementObjectTypeAttributeRepository.class);
        List<PLMRequirementObjectTypeAttribute> mesObjectTypeAttributes = new ArrayList<>();
        if (!hier) {
            mesObjectTypeAttributes = requirementObjectTypeAttributeRepository.findByTypeOrderBySequence(typeId);
        } else {
            mesObjectTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return mesObjectTypeAttributes;
    }

    @Override
    public List<PLMRequirementObjectType> getAll() {
        return requirementObjectTypeRepository.findAll();
    }


    @Override
    public List<PLMRequirementObjectType> getRootTypes() {
        return requirementObjectTypeRepository.findAll();
    }

    @Override
    public List<PLMRequirementObjectType> getChildren(Integer parent) {
        return null;
    }

    @Override
    public List<PLMRequirementObjectType> getClassificationTree() {
        List<PLMRequirementObjectType> types = getRootTypes();
        for (PLMRequirementObjectType type : types) {
        }
        return types;
    }

    @Override
    public PLMRequirementObjectTypeAttribute createAttribute(PLMRequirementObjectTypeAttribute attribute) {
        List<PLMRequirementObjectTypeAttribute> qualityTypeAttributes = requirementObjectTypeAttributeRepository.findByTypeOrderBySequence(attribute.getType());
        if (qualityTypeAttributes.size() >= 0) {
            attribute.setSequence(qualityTypeAttributes.size() + 1);
        }
        attribute = requirementObjectTypeAttributeRepository.save(attribute);
        PLMObjectType objectType = null;
        if (attribute.getObjectType().name().equals("REQUIREMENTTYPE")) {
            objectType = PLMObjectType.REQUIREMENTTYPE;
        } else if (attribute.getObjectType().name().equals("REQUIREMENTDOCUMENTTYPE")) {
            objectType = PLMObjectType.REQUIREMENTDOCUMENTTYPE;
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(objectType, attribute));
        return attribute;

    }

    @Override
    public PLMRequirementObjectTypeAttribute updateAttribute(PLMRequirementObjectTypeAttribute attribute) {
        PLMRequirementObjectTypeAttribute plantTypeAttribute = requirementObjectTypeAttributeRepository.findOne(attribute.getId());
        PLMObjectType objectType = null;
        if (attribute.getObjectType().name().equals("REQUIREMENTTYPE")) {
            objectType = PLMObjectType.REQUIREMENTTYPE;
        } else if (attribute.getObjectType().name().equals("REQUIREMENTDOCUMENTTYPE")) {
            objectType = PLMObjectType.REQUIREMENTDOCUMENTTYPE;
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(objectType, plantTypeAttribute, attribute));
        return requirementObjectTypeAttributeRepository.save(attribute);
    }

    @Override
    public void deleteAttribute(Integer id) {
        PLMRequirementObjectTypeAttribute attribute = requirementObjectTypeAttributeRepository.findOne(id);
        List<PLMRequirementObjectTypeAttribute> requirementObjectTypeAttributes = requirementObjectTypeAttributeRepository.findByTypeOrderBySequence(attribute.getType());
        if (requirementObjectTypeAttributes.size() > 0) {
            for (PLMRequirementObjectTypeAttribute requirementObjectTypeAttribute : requirementObjectTypeAttributes) {
                if (requirementObjectTypeAttribute.getSequence() > attribute.getSequence()) {
                    requirementObjectTypeAttribute.setSequence(requirementObjectTypeAttribute.getSequence() - 1);
                    requirementObjectTypeAttribute = requirementObjectTypeAttributeRepository.save(requirementObjectTypeAttribute);
                }
            }
        }
        PLMObjectType objectType = null;
        if (attribute.getObjectType().name().equals("REQUIREMENTTYPE")) {
            objectType = PLMObjectType.REQUIREMENTTYPE;
        } else if (attribute.getObjectType().name().equals("REQUIREMENTDOCUMENTTYPE")) {
            objectType = PLMObjectType.REQUIREMENTDOCUMENTTYPE;
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(objectType, attribute));
        requirementObjectTypeAttributeRepository.delete(id);
    }

    @Override
    public PLMRequirementObjectTypeAttribute getAttribute(Integer id) {
        return requirementObjectTypeAttributeRepository.findOne(id);
    }


    @Transactional
    public PLMRequirementObjectAttribute createReqObjectAttribute(PLMRequirementObjectAttribute attribute) {
        return requirementObjectAttributeRepository.save(attribute);
    }


    @Transactional
    public PLMRequirementObjectAttribute updateReqObjectAttribute(PLMRequirementObjectAttribute attribute) {
        PLMRequirementObjectAttribute oldValue = requirementObjectAttributeRepository.findByObjectAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PLMRequirementObjectAttribute.class);
        attribute = requirementObjectAttributeRepository.save(attribute);
        PLMRequirementObject requirementObject = requirementObjectRepository.findOne(attribute.getId().getObjectId());
        //applicationEventPublisher.publishEvent(new QCREvents.QCRAttributesUpdatedEvent(pgcObject, oldValue, attribute));
        return attribute;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PLMRequirementObjectTypeAttribute> getAttributes(Integer typeId, Boolean hier) {
        List<PLMRequirementObjectTypeAttribute> requirementObjectTypeAttributes = new ArrayList<>();
        if (!hier) {
            requirementObjectTypeAttributes = requirementObjectTypeAttributeRepository.findByTypeOrderBySequence(typeId);
            requirementObjectTypeAttributes.forEach(plmRequirementObjectTypeAttribute -> {
                if (plmRequirementObjectTypeAttribute.getRefSubType() != null) {
                    plmRequirementObjectTypeAttribute.setRefSubTypeName(itemTypeService.getRefSubType(plmRequirementObjectTypeAttribute.getRefType().name(), plmRequirementObjectTypeAttribute.getRefSubType()));
                }
            });
        } else {
            requirementObjectTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return requirementObjectTypeAttributes;
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementObjectTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<PLMRequirementObjectTypeAttribute> collector = new ArrayList<>();
        List<PLMRequirementObjectTypeAttribute> atts = requirementObjectTypeAttributeRepository.findByTypeOrderBySequence(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<PLMRequirementObjectTypeAttribute> collector, Integer typeId) {
        MESObjectType objectType = objectTypeRepository.findOne(typeId);
        if (objectType != null) {
            Integer parentType = objectType.getParentType();
            if (parentType != null) {
                List<PLMRequirementObjectTypeAttribute> atts = requirementObjectTypeAttributeRepository.findByTypeOrderBySequence(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

		/*-------------------------   Requirement Type ----------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#reqType,'create')")
    public PLMRequirementType createRequirementType(PLMRequirementType reqType) {
        PLMRequirementType requirementType = requirementTypeRepository.save(reqType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.REQUIREMENTTYPE, requirementType));
        return requirementType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#reqType.id ,'edit')")
    public PLMRequirementType updateRequirementType(Integer id, PLMRequirementType reqType) {
        PLMRequirementType oldReqType = requirementTypeRepository.findOne(reqType.getId());
        PLMRequirementType existingPmType;
        if (reqType.getParent() == null) {
            existingPmType = requirementTypeRepository.findByParentIsNullAndNameEqualsIgnoreCase(reqType.getName());
        } else {
            existingPmType = requirementTypeRepository.findByNameEqualsIgnoreCaseAndParent(reqType.getName(), reqType.getParent());
        }
        if ( existingPmType != null && !reqType.getId().equals(existingPmType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingPmType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.REQUIREMENTTYPE, oldReqType, reqType));
        return requirementTypeRepository.save(reqType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteRequirementType(Integer id) {
        requirementTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMRequirementType getRequirementType(Integer id) {
        return requirementTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementType> getAllRequirementTypes() {
        return requirementTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementType> findMultipleRequirementTypes(List<Integer> ids) {
        return requirementTypeRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMRequirementType> getRequirementTypeTree() {
        List<PLMRequirementType> types = requirementTypeRepository.findByParentIsNullOrderByIdAsc();
        for (PLMRequirementType type : types) {
            visitRequirementTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementType> getRequirementTypeChildren(Integer id) {
        return requirementTypeRepository.findByParentOrderByIdAsc(id);
    }

    private void visitRequirementTypeChildren(PLMRequirementType parent) {
        List<PLMRequirementType> childrens = getRequirementTypeChildren(parent.getId());
        for (PLMRequirementType child : childrens) {
            visitRequirementTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    	/*------------------------- RequirementDOC Type ----------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#reqDocType,'create')")
    public PLMRequirementDocumentType createReqDocumentType(PLMRequirementDocumentType reqDocType) {
        PLMRequirementDocumentType reqDocType1 = requirementDocumentTypeRepository.save(reqDocType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.REQUIREMENTDOCUMENTTYPE, reqDocType));
        return reqDocType1;
    }

    @Transactional
    @PreAuthorize("hasPermission(#reqDocType.id ,'edit')")
    public PLMRequirementDocumentType updateReqDocumentType(Integer id, PLMRequirementDocumentType reqDocType) {
        PLMRequirementDocumentType oldReqDocumentType = requirementDocumentTypeRepository.findOne(reqDocType.getId());
        PLMRequirementDocumentType existingPmType;
        if (reqDocType.getParent() == null) {
            existingPmType = requirementDocumentTypeRepository.findByParentIsNullAndNameEqualsIgnoreCase(reqDocType.getName());
        } else {
            existingPmType = requirementDocumentTypeRepository.findByNameEqualsIgnoreCaseAndParent(reqDocType.getName(), reqDocType.getParent());
        }
        if ( existingPmType != null && !reqDocType.getId().equals(existingPmType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingPmType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.REQUIREMENTDOCUMENTTYPE, oldReqDocumentType, reqDocType));
        return requirementDocumentTypeRepository.save(reqDocType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteReqDocumentType(Integer id) {
        requirementDocumentTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMRequirementDocumentType getReqDocumentType(Integer id) {
        return requirementDocumentTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementDocumentType> getAllReqDocumentTypes() {
        return requirementDocumentTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementDocumentType> findMultipleReqDocumentTypes(List<Integer> ids) {
        return requirementDocumentTypeRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMRequirementDocumentType> getReqDocumentTypeTree() {
        List<PLMRequirementDocumentType> types = requirementDocumentTypeRepository.findByParentIsNullOrderByIdAsc();
        for (PLMRequirementDocumentType type : types) {
            visitReqDocumentTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementDocumentType> getReqDocumentTypeChildren(Integer id) {
        return requirementDocumentTypeRepository.findByParentOrderByIdAsc(id);
    }

    private void visitReqDocumentTypeChildren(PLMRequirementDocumentType parent) {
        List<PLMRequirementDocumentType> childrens = getReqDocumentTypeChildren(parent.getId());
        for (PLMRequirementDocumentType child : childrens) {
            visitReqDocumentTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    @Transactional(readOnly = true)
    public Object getPMObjectTypeIdAndType(Integer id, PLMObjectType objectType) {
        Object object = null;
        if (objectType.equals(PLMObjectType.PMOBJECTTYPE)) {
            PMObjectType pmObjectType = pmObjectTypeRepository.findOne(id);
            List<Integer> ids = new ArrayList<>();
            if (pmObjectType.getType().equals(PMType.PROGRAM)) {
                ids = programRepository.getObjectIdsByType(id);
                if (ids.size() > 0) {
                    Integer count = programFileRepository.getProgramFileCount(ids);
                    if (count > 0) {
                        pmObjectType.setTypeObjectHasFiles(true);
                    }
                }
            } else if (pmObjectType.getType().equals(PMType.PROJECT)) {
                ids = projectRepository.getObjectIdsByType(id);
                if (ids.size() > 0) {
                    Integer count = projectFileRepository.getProjectFileCount(ids);
                    if (count > 0) {
                        pmObjectType.setTypeObjectHasFiles(true);
                    }
                }
            } else if (pmObjectType.getType().equals(PMType.PROJECTACTIVITY)) {
                ids = activityRepository.getObjectIdsByType(id);
                if (ids.size() > 0) {
                    Integer count = activityFileRepository.getActivitiesFileCount(ids);
                    if (count > 0) {
                        pmObjectType.setTypeObjectHasFiles(true);
                    }
                }
            } else if (pmObjectType.getType().equals(PMType.PROJECTTASK)) {
                ids = taskRepository.getObjectIdsByType(id);
                if (ids.size() > 0) {
                    Integer count = taskFileRepository.getTasksFileCount(ids);
                    if (count > 0) {
                        pmObjectType.setTypeObjectHasFiles(true);
                    }
                }
            }
            object = pmObjectType;
        } else if (objectType.equals(PLMObjectType.REQUIREMENTTYPE)) {
            object = requirementTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENTTYPE)) {
            object = requirementDocumentTypeRepository.findOne(id);
        }
        return object;
    }

    @Override
    public Boolean isSubtypeOf(String s1, Object s2, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        requirementTypeRepository = webApplicationContext.getBean(PLMRequirementTypeRepository.class);
        if (s2 instanceof PLMRequirementType) {
            PLMRequirementType requirementType = (PLMRequirementType) s2;
            if (subTypeId != null && checkWithId(requirementType, subTypeId)) {
                return true;
            }
            if (requirementType.getName().equalsIgnoreCase(s1)) {
                return true;
            } else {
                if (requirementType.getParent() != null)
                    return compareWithParent(requirementTypeRepository.findOne(requirementType.getParent()), s1);
            }
            return false;
        } else {
            PLMRequirementDocumentType requirementDocumentType = (PLMRequirementDocumentType) s2;
            if (subTypeId != null && checkWithId(requirementDocumentType, subTypeId)) {
                return true;
            }
            if (requirementDocumentType.getName().equalsIgnoreCase(s1)) {
                return true;
            } else {
                if (requirementDocumentType.getParent() != null)
                    return compareWithParent(requirementDocumentTypeRepository.findOne(requirementDocumentType.getParent()), s1);
            }
            return false;
        }
    }

    public Boolean checkWithId(PLMRequirementType plmItemType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        requirementTypeRepository = webApplicationContext.getBean(PLMRequirementTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmItemType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmItemType.getParent() != null)
                flag = checkWithId(requirementTypeRepository.findOne(plmItemType.getParent()), typeId);
        }
        return flag;
    }

    public Boolean checkWithId(PLMRequirementDocumentType plmItemType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        requirementDocumentTypeRepository = webApplicationContext.getBean(PLMRequirementDocumentTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmItemType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmItemType.getParent() != null)
                flag = checkWithId(requirementDocumentTypeRepository.findOne(plmItemType.getParent()), typeId);
        }
        return flag;
    }

    private Boolean compareWithParent(PLMRequirementType plmRequirementType, String s1) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        requirementTypeRepository = webApplicationContext.getBean(PLMRequirementTypeRepository.class);
        Boolean flag = false;
        if (plmRequirementType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (plmRequirementType.getParent() != null)
                flag = compareWithParent(requirementTypeRepository.findOne(plmRequirementType.getParent()), s1);
        }
        return flag;
    }

    private Boolean compareWithParent(PLMRequirementDocumentType plmRequirementDocumentType, String s1) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        requirementDocumentTypeRepository = webApplicationContext.getBean(PLMRequirementDocumentTypeRepository.class);
        Boolean flag = false;
        if (plmRequirementDocumentType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (plmRequirementDocumentType.getParent() != null)
                flag = compareWithParent(requirementDocumentTypeRepository.findOne(plmRequirementDocumentType.getParent()), s1);
        }
        return flag;
    }

}

