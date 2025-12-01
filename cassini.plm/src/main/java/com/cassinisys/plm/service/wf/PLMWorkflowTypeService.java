package com.cassinisys.plm.service.wf;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.cassinisys.platform.service.core.TypeSystem;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.wf.PLMWorkflowActivityFormData;
import com.cassinisys.plm.model.wf.PLMWorkflowActivityFormFields;
import com.cassinisys.plm.model.wf.PLMWorkflowType;
import com.cassinisys.plm.model.wf.PLMWorkflowTypeAttribute;
import com.cassinisys.plm.repo.wf.*;
import com.cassinisys.plm.service.classification.ClassificationTypeService;
import com.cassinisys.plm.service.plm.ItemTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nageshreddy on 04-09-2017.
 */
@Service
public class PLMWorkflowTypeService implements CrudService<PLMWorkflowType, Integer>,
        TypeSystem, ClassificationTypeService<PLMWorkflowType, PLMWorkflowTypeAttribute> {

    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private WorkflowTypeAttributeRepository workflowTypeAttributeRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private PLMWorkflowEventRepository plmWorkflowEventRepository;
    @Autowired
    private PLMWorkflowDefinitionEventRepository plmWorkflowDefinitionEventRepository;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private PLMWorkflowDefinitionStatusRepository plmWorkflowDefinitionStatusRepository;
    @Autowired
    private PLMWorkflowActivityFormFieldsRepository plmWorkflowActivityFormFieldsRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private PLMWorkflowActivityFormDataRepository workflowActivityFormDataRepository;

    @PostConstruct
    public void InitWorkflowTypeService() {
        objectTypeService.registerTypeSystem("wfType", new PLMWorkflowTypeService());
    }

    @Override
    @Transactional
    public PLMWorkflowType create(PLMWorkflowType workflowType) {
        if (workflowType.getRevisionSequence() == null) {
            Lov lov = lovRepository.findByName("Default Revision Sequence");
            workflowType.setRevisionSequence(lov);
        }
        PLMWorkflowType plmWorkflowType = workflowTypeRepository.save(workflowType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.WORKFLOWTYPE, plmWorkflowType));
        return plmWorkflowType;
    }

    @Override
    @Transactional
    public PLMWorkflowType update(PLMWorkflowType wfType) {
        checkNotNull(wfType);
        checkNotNull(wfType.getId());
        PLMWorkflowType oldWorkflowType = workflowTypeRepository.findOne(wfType.getId());
        if (oldWorkflowType.getRevisionSequence() == null) {
            Lov lov = lovRepository.findByName("Default Revision Sequence");
            wfType.setRevisionSequence(lov);
        }
        PLMWorkflowType plmWorkflowType = null;
        if (wfType.getAssignedType() != null) {
            PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(wfType.getAssignable(), wfType.getAssignedType());
            if (workflowType == null || wfType.getId().equals(workflowType.getId())) {
                applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.WORKFLOWTYPE, oldWorkflowType, wfType));
                plmWorkflowType = workflowTypeRepository.save(wfType);
            } else {
                throw new CassiniException(messageSource.getMessage("workflow_assignable_and_type_msg", null, "Workflow assignableTo and assigned type already exit", LocaleContextHolder.getLocale()));
            }
        } else {
            PLMWorkflowType workflowType1 = workflowTypeRepository.findByAssignable(wfType.getAssignable());
            if (workflowType1 == null || wfType.getId().equals(workflowType1.getId())) {
                applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.WORKFLOWTYPE, oldWorkflowType, wfType));
                plmWorkflowType = workflowTypeRepository.save(wfType);
            } else {
                throw new CassiniException(messageSource.getMessage("workflow_assignable_to_msg", null, "Workflow assignableTo already exit", LocaleContextHolder.getLocale()));
            }
        }
        return workflowTypeRepository.save(wfType);
    }

    @Override
    public void delete(Integer id) {
        PLMWorkflowType workflowType = workflowTypeRepository.findOne(id);
        if (workflowType.getParentType() != null) {
            applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeDeletedEvent(PLMObjectType.WORKFLOWTYPE, workflowType));
        }
        workflowTypeRepository.delete(id);
    }

    @Override
    public PLMWorkflowType get(Integer id) {
        return workflowTypeRepository.findOne(id);
    }

    @Override
    public List<PLMWorkflowType> getAll() {
        return workflowTypeRepository.findAll();
    }

    public List<PLMWorkflowType> getRootTypes() {
        return workflowTypeRepository.findByParentTypeIsNullOrderByIdAsc();
    }

    public List<PLMWorkflowType> getChildren(Integer parent) {
        return workflowTypeRepository.findByParentTypeOrderByIdAsc(parent);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMWorkflowType> getClassificationTree() {
        List<PLMWorkflowType> types = getRootTypes();
        for (PLMWorkflowType type : types) {
            visitChildren(type);
        }
        return types;
    }

    public List<PLMWorkflowType> getAllWorkflowTypesWithAttributes() {
        List<PLMWorkflowType> types = getAll();
        for (PLMWorkflowType type : types) {
            if (type.getParentType() != null) {
                type.setParentTypeReference(workflowTypeRepository.findOne(type.getParentType()));
            }
            type.setAttributes(workflowTypeAttributeRepository.findByWorkflowTypeOrderByName(type.getId()));
        }
        return types;
    }

    private void visitChildren(PLMWorkflowType parent) {
        List<PLMWorkflowType> children = getChildren(parent.getId());
        for (PLMWorkflowType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    public PLMWorkflowTypeAttribute createAttribute(PLMWorkflowTypeAttribute attribute) {
        List<PLMWorkflowTypeAttribute> workflowTypeAttributes = workflowTypeAttributeRepository.findByWorkflowType(attribute.getWorkflowType());
        if (workflowTypeAttributes.size() >= 0) {
            attribute.setSeq(workflowTypeAttributes.size() + 1);
        }

        PLMWorkflowTypeAttribute workflowTypeAttribute = workflowTypeAttributeRepository.save(attribute);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(PLMObjectType.WORKFLOWTYPE, workflowTypeAttribute));
        return workflowTypeAttribute;
    }

    public PLMWorkflowTypeAttribute updateAttribute(PLMWorkflowTypeAttribute attribute) {
        PLMWorkflowTypeAttribute exitWorkflowTypeAttribute = workflowTypeAttributeRepository.findOne(attribute.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(PLMObjectType.WORKFLOWTYPE, exitWorkflowTypeAttribute, attribute));
        PLMWorkflowTypeAttribute workflowTypeAttribute = workflowTypeAttributeRepository.save(attribute);
        return workflowTypeAttribute;
    }

    public PLMWorkflowTypeAttribute getAttribute(Integer id) {
        return workflowTypeAttributeRepository.findOne(id);
    }

    public List<PLMWorkflowTypeAttribute> getTypeAttributes(Integer id) {
        return workflowTypeAttributeRepository.findByWorkflowTypeOrderByName(id);
    }

    @Override
    public void deleteAttribute(Integer id) {
        PLMWorkflowTypeAttribute attribute = workflowTypeAttributeRepository.findOne(id);
        List<PLMWorkflowTypeAttribute> workflowTypeAttributes = workflowTypeAttributeRepository.findByWorkflowType(attribute.getWorkflowType());
        if (workflowTypeAttributes.size() > 0) {
            for (PLMWorkflowTypeAttribute workflowTypeAttribute : workflowTypeAttributes) {
                if (workflowTypeAttribute.getSeq() > attribute.getSeq()) {
                    workflowTypeAttribute.setSeq(workflowTypeAttribute.getSeq() - 1);
                    workflowTypeAttribute = workflowTypeAttributeRepository.save(workflowTypeAttribute);
                }
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(PLMObjectType.WORKFLOWTYPE, attribute));
        workflowTypeAttributeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            List<PLMWorkflowTypeAttribute> workflowTypeAttributes = workflowTypeAttributeRepository.findByWorkflowTypeOrderBySeq(typeId);
            workflowTypeAttributes.forEach(workflowTypeAttribute -> {
                if (workflowTypeAttribute.getRefSubType() != null) {
                    workflowTypeAttribute.setRefSubTypeName(itemTypeService.getRefSubType(workflowTypeAttribute.getRefType().name(), workflowTypeAttribute.getRefSubType()));
                }
            });
            return workflowTypeAttributes;
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    public List<PLMWorkflowTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<PLMWorkflowTypeAttribute> collector = new ArrayList<>();
        List<PLMWorkflowTypeAttribute> atts = workflowTypeAttributeRepository.findByWorkflowTypeOrderByName(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<PLMWorkflowTypeAttribute> collector, Integer typeId) {
        PLMWorkflowType itemType = workflowTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<PLMWorkflowTypeAttribute> atts = workflowTypeAttributeRepository.findByWorkflowTypeOrderByName(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Override
    public Boolean isSubtypeOf(String s1, Object s2, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        workflowTypeRepository = webApplicationContext.getBean(WorkflowTypeRepository.class);
        PLMWorkflowType itemType = (PLMWorkflowType) s2;
        if (subTypeId != null && checkWithId(itemType, subTypeId)) {
            return true;
        }
        if (itemType.getName().equalsIgnoreCase(s1)) {
            return true;
        } else {
            if (itemType.getParentType() != null)
                return compareWithParent(workflowTypeRepository.findOne(itemType.getParentType()), s1);
        }
        return false;
    }

    public Boolean checkWithId(PLMWorkflowType plmItemType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        workflowTypeRepository = webApplicationContext.getBean(WorkflowTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmItemType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = checkWithId(workflowTypeRepository.findOne(plmItemType.getParentType()), typeId);
        }
        return flag;
    }

    private Boolean compareWithParent(PLMWorkflowType plmItemType, String s1) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        workflowTypeRepository = webApplicationContext.getBean(WorkflowTypeRepository.class);
        Boolean flag = false;
        if (plmItemType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = compareWithParent(workflowTypeRepository.findOne(plmItemType.getParentType()), s1);
        }
        return flag;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PLMWorkflowTypeAttribute> getTypeAttributes(Integer typeId, Boolean hierarchy) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        workflowTypeAttributeRepository = webApplicationContext.getBean(WorkflowTypeAttributeRepository.class);
        workflowTypeRepository = webApplicationContext.getBean(WorkflowTypeRepository.class);
        if (!hierarchy) {
            return workflowTypeAttributeRepository.findByWorkflowTypeOrderBySeq(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }


    @Transactional(readOnly = true)
    public CassiniObject getWorkflowAssignableTypeObjectType(Integer id) {
        CassiniObject cassiniObject = objectRepository.findById(id);
        return cassiniObject;
    }


    public List<PLMWorkflowActivityFormFields> getWorkflowStatusAttributes(Integer typeId) {
        List<PLMWorkflowActivityFormFields> workflowActivityFormFields = new LinkedList<>();
        workflowActivityFormFields = plmWorkflowActivityFormFieldsRepository.findByWorkflowActivityOrderByName(typeId);
        workflowActivityFormFields.forEach(plmItemTypeAttribute -> {
            if (plmItemTypeAttribute.getMeasurement() != null) {
                plmItemTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(plmItemTypeAttribute.getMeasurement().getId()));
            }
            if (plmItemTypeAttribute.getRefSubType() != null) {
                plmItemTypeAttribute.setRefSubTypeName(itemTypeService.getRefSubType(plmItemTypeAttribute.getRefType().name(), plmItemTypeAttribute.getRefSubType()));
            }
        });
        return workflowActivityFormFields;
    }


    @Transactional
    public PLMWorkflowActivityFormFields createWorkflowStatusAttribute(PLMWorkflowActivityFormFields attribute) {
        PLMWorkflowActivityFormFields itemTypeAttribute = plmWorkflowActivityFormFieldsRepository.save(attribute);
//        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(PLMObjectType.ITEMTYPE, itemTypeAttribute));
        return itemTypeAttribute;
    }

    @Transactional
    public PLMWorkflowActivityFormFields updateWorkflowStatusAttribute(PLMWorkflowActivityFormFields attribute) {

        PLMWorkflowActivityFormFields existAttribute = plmWorkflowActivityFormFieldsRepository.findOne(attribute.getId());
//        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(PLMObjectType.ITEMTYPE, existAttribute, attribute));
        PLMWorkflowActivityFormFields workflowActivityFormFields = plmWorkflowActivityFormFieldsRepository.save(attribute);
        return workflowActivityFormFields;
    }

    @Transactional
    public void deleteWorkflowStatusAttribute(Integer id) {
        PLMWorkflowActivityFormFields attribute = plmWorkflowActivityFormFieldsRepository.findOne(id);
//        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(PLMObjectType.ITEMTYPE, attribute));
        plmWorkflowActivityFormFieldsRepository.delete(id);
    }

    @Transactional
    public PLMWorkflowActivityFormFields getWorkflowStatusAttribute(Integer id) {
        return plmWorkflowActivityFormFieldsRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public PLMWorkflowActivityFormFields getWorkflowStatusAttributeByName(Integer id, String name) {
        return plmWorkflowActivityFormFieldsRepository.findByWorkflowActivityAndName(id, name);
    }


    @Transactional
    public PLMWorkflowActivityFormData createWorkflowStatusFormData(PLMWorkflowActivityFormData attribute) {
        return workflowActivityFormDataRepository.save(attribute);
    }

    @Transactional
    public PLMWorkflowActivityFormData updateWorkflowStatusFormData(PLMWorkflowActivityFormData attribute) {
        PLMWorkflowActivityFormData oldValue = workflowActivityFormDataRepository.findByWorkflowActivityAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        attribute = workflowActivityFormDataRepository.save(attribute);
        return attribute;
    }

}
