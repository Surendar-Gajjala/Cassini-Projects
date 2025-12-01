package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.model.custom.CustomObjectType;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.repo.custom.CustomObjectTypeRepository;
import com.cassinisys.platform.service.core.*;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.model.cm.PLMChangeType;
import com.cassinisys.plm.model.cm.PLMChangeTypeAttribute;
import com.cassinisys.plm.model.dto.ClassificationTypesDto;
import com.cassinisys.plm.model.mes.MESObjectType;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartType;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartTypeAttribute;
import com.cassinisys.plm.model.mfr.PLMManufacturerType;
import com.cassinisys.plm.model.mfr.PLMManufacturerTypeAttribute;
import com.cassinisys.plm.model.mro.MROObjectType;
import com.cassinisys.plm.model.pgc.PGCObjectType;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pqm.PQMInspectionPlanType;
import com.cassinisys.plm.model.pqm.PQMQualityType;
import com.cassinisys.plm.model.req.PLMRequirementDocumentType;
import com.cassinisys.plm.model.req.PLMRequirementType;
import com.cassinisys.plm.model.rm.RmObjectType;
import com.cassinisys.plm.model.rm.RmObjectTypeAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflowType;
import com.cassinisys.plm.model.wf.PLMWorkflowTypeAttribute;
import com.cassinisys.plm.repo.cm.ChangeRequestRepository;
import com.cassinisys.plm.repo.cm.ChangeTypeAttributeRepository;
import com.cassinisys.plm.repo.cm.ChangeTypeRepository;
import com.cassinisys.plm.repo.mes.MESObjectTypeRepository;
import com.cassinisys.plm.repo.mfr.*;
import com.cassinisys.plm.repo.mro.MROMaintenanceOperationRepository;
import com.cassinisys.plm.repo.mro.MROObjectTypeRepository;
import com.cassinisys.plm.repo.mro.MROWorkOrderOperationRepository;
import com.cassinisys.plm.repo.pgc.PGCObjectTypeRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.PMObjectTypeRepository;
import com.cassinisys.plm.repo.pqm.InspectionPlanTypeRepository;
import com.cassinisys.plm.repo.pqm.NCRTypeRepository;
import com.cassinisys.plm.repo.pqm.ProblemReportTypeRepository;
import com.cassinisys.plm.repo.pqm.QualityTypeRepository;
import com.cassinisys.plm.repo.req.PLMRequirementDocumentTypeRepository;
import com.cassinisys.plm.repo.req.PLMRequirementTypeRepository;
import com.cassinisys.plm.repo.rm.RmObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.rm.RmObjectTypeRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeAttributeRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import com.cassinisys.plm.service.classification.ClassificationTypeService;
import com.cassinisys.plm.service.classification.PLMSupplierTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class ItemTypeService implements CrudService<PLMItemType, Integer>,
        PageableService<PLMItemType, Integer>, TypeSystem,
        ClassificationTypeService<PLMItemType, PLMItemTypeAttribute> {

    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private QualityTypeRepository qualityTypeRepository;
    @Autowired
    private InspectionPlanTypeRepository inspectionPlanTypeRepository;
    @Autowired
    private ProblemReportTypeRepository problemReportTypeRepository;
    @Autowired
    private NCRTypeRepository ncrTypeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private MROMaintenanceOperationRepository mroMaintenanceOperationRepository;
    @Autowired
    private MROWorkOrderOperationRepository workOrderOperationRepository;
    @Autowired
    private PLMRequirementDocumentTypeRepository plmRequirementDocumentTypeRepository;
    @Autowired
    private PLMRequirementTypeRepository plmRequirementTypeRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ChangeTypeRepository changeTypeRepository;
    @Autowired
    private ChangeTypeAttributeRepository changeTypeAttributeRepository;
    @Autowired
    private ManufacturerTypeRepository manufacturerTypeRepository;
    @Autowired
    private ManufacturerTypeAttributeRepository manufacturerTypeAttributeRepository;
    @Autowired
    private ManufacturerPartTypeRepository manufacturerPartTypeRepository;
    @Autowired
    private ManufacturerPartTypeAttributeRepository manufacturerPartTypeAttributeRepository;
    @Autowired
    private WorkflowTypeAttributeRepository workflowTypeAttributeRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private RmObjectTypeAttributeRepository rmObjectTypeAttributeRepository;
    @Autowired
    private RmObjectTypeRepository rmObjectTypeRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemConfigurableAttributesRepository itemConfigurableAttributesRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private ChangeRequestRepository changeRequestRepository;
    @Autowired
    private MESObjectTypeRepository mesObjectTypeRepository;
    @Autowired
    private MROObjectTypeRepository mroObjectTypeRepository;
    @Autowired
    private CustomObjectTypeRepository customObjectTypeRepository;
    @Autowired
    private PLMRequirementDocumentTypeRepository requirementDocumentTypeRepository;
    @Autowired
    private PLMRequirementTypeRepository requirementTypeRepository;
    @Autowired
    private ItemAttributeRepository itemAttributeRepository;
    @Autowired
    private PMObjectTypeRepository pmObjectTypeRepository;
    @Autowired
    private SupplierTypeRepository supplierTypeRepository;
    @Autowired
    private PGCObjectTypeRepository pgcObjectTypeRepository;

    @PostConstruct
    public void InitItemTypeService() {
        objectTypeService.registerTypeSystem("itemType", new ItemTypeService());
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#itemType,'create')")
    public PLMItemType create(PLMItemType itemType) {
        checkNotNull(itemType);
        itemType = itemTypeRepository.save(itemType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.ITEMTYPE, itemType));
        return itemType;
    }

    @Transactional
    @Override
    @PreAuthorize("hasPermission(#itemType.id ,'edit')")
    public PLMItemType update(PLMItemType itemType) {
        checkNotNull(itemType);
        checkNotNull(itemType.getId());
        PLMItemType oldItemType = itemTypeRepository.findOne(itemType.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.ITEMTYPE, oldItemType, itemType));
        itemType = itemTypeRepository.save(itemType);
        return itemType;
    }

    @Transactional(readOnly = true)
    public PLMItemTypeAttribute getExclusionAttributes(Integer typeId) {
        PLMItemTypeAttribute plmItemTypeAttribute = new PLMItemTypeAttribute();
        Map<String, PLMItemTypeAttribute> lovsForList = new LinkedHashMap<>();
        List<PLMItemTypeAttribute> types = itemTypeAttributeRepository.findByItemTypeOrderBySeq(typeId);
        for (PLMItemTypeAttribute type : types) {
            if (type.getConfigurable() == true && type.getDataType().toString().equals("LIST")) {
                lovsForList.put(type.getName(), type);
            }
        }
        plmItemTypeAttribute.setLovsForList(lovsForList);
        if (plmItemTypeAttribute.getLovsForList().size() == 0) {
            throw new CassiniException(messageSource.getMessage("no_configurable_items_exists", null, "There is no configurable attributes on this type", LocaleContextHolder.getLocale()));
        }
        return plmItemTypeAttribute;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        checkNotNull(id);
        PLMItemType itemType = itemTypeRepository.findOne(id);
        if (itemType.getParentType() != null) {
            applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeDeletedEvent(PLMObjectType.ITEMTYPE, itemType));
        }

        itemTypeRepository.delete(id);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMItemType get(Integer id) {
        checkNotNull(id);
        PLMItemType itemType = itemTypeRepository.findOne(id);
        if (itemType == null) {
            throw new ResourceNotFoundException();
        }
        return itemType;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemType> findMultiple(List<Integer> ids) {
        return itemTypeRepository.findByIdIn(ids);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemType> getRootTypes() {
        return itemTypeRepository.findByParentTypeIsNullOrderByIdAsc();
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemType> getRootTypesByItemClass(ItemClass itemClass) {
        return itemTypeRepository.findByItemClassAndParentTypeIsNullOrderByIdAsc(itemClass);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemType> getChildren(Integer parent) {
        return itemTypeRepository.findByParentTypeOrderByIdAsc(parent);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemType> getChildrenFroModalBom(Integer parent) {
        List<PLMItemType> itemTypeChildrens = itemTypeRepository.findByParentTypeOrderByIdAsc(parent);
        for (PLMItemType type : itemTypeChildrens) {
            /*type.setLevel(1);*/
            if (type.getParentType() != null) {
                type.setParentTypeReference(itemTypeRepository.findOne(type.getParentType()));
            }
            type.setAttributes(itemTypeAttributeRepository.findByItemTypeOrderByName(type.getId()));
        }
        return itemTypeChildrens;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemType> getClassificationTree() {
        List<PLMItemType> types = getRootTypes();
        for (PLMItemType type : types) {
            visitChildren(type);
        }
        return types;
    }

    /*------ To avoid transaction error --------------*/
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemType> getItemTypeTree() {
        List<PLMItemType> types = getRootTypes();
        for (PLMItemType type : types) {
            visitChildren(type);
        }
        return types;
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemType> getClassificationTreeByClass(ItemClass itemClass) {
        List<PLMItemType> types = getRootTypesByItemClass(itemClass);
        for (PLMItemType type : types) {
            visitChildren(type);
        }
        return types;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemType> getAllItemTypesWithAttributes() {
        List<PLMItemType> types = getAll();
        for (PLMItemType type : types) {
            if (type.getParentType() != null) {
                type.setParentTypeReference(itemTypeRepository.findOne(type.getParentType()));
            }
            type.setAttributes(itemTypeAttributeRepository.findByItemTypeOrderByName(type.getId()));
        }
        return types;
    }

    private void visitChildren(PLMItemType parent) {
        List<PLMItemType> children = getChildren(parent.getId());
        for (PLMItemType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    public List<Integer> getAllSubTypeIds(Integer parent) {
        List<Integer> subtypes = new ArrayList<>();
        subtypes.add(parent);
        List<PLMItemType> children = getChildren(parent);
        children.forEach(item -> {
            List<Integer> childSubtypes = getAllSubTypeIds(item.getId());
            subtypes.addAll(childSubtypes);
        });
        return subtypes;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemType> getAllSubTypesForModalBom(Integer parent) {
        List<PLMItemType> subtypes = new ArrayList<>();
        PLMItemType plmItemType = itemTypeRepository.findOne(parent);
        plmItemType.setAttributes(itemTypeAttributeRepository.findByItemTypeOrderByName(plmItemType.getId()));
        //plmItemType.setLevel(0);
        subtypes.add(plmItemType);
        List<PLMItemType> children = getChildrenFroModalBom(parent);
        if (children.size() > 0) {
            children.forEach(item -> {
                List<PLMItemType> childSubtypes = getAllSubTypesForModalBom(item.getId());
                subtypes.addAll(childSubtypes);
            });
        }
        return subtypes;
    }

    public List<PLMItemTypeAttribute> getTypeAttributesFroModalBom(Integer typeId) {
        List<PLMItemTypeAttribute> collector = new ArrayList<>();
        List<PLMItemTypeAttribute> atts = itemTypeAttributeRepository.findByItemTypeOrderBySeq(typeId);
        collector.addAll(atts);
        return collector;
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemType> getAll() {
        return itemTypeRepository.findAll();
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMItemType> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("id")));
        }
        return itemTypeRepository.findAll(pageable);
    }

    public List<PLMItemTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        List<PLMItemTypeAttribute> itemTypeAttributes = new LinkedList<>();
        if (!hierarchy) {
            itemTypeAttributes = itemTypeAttributeRepository.findByItemTypeOrderBySeq(typeId);
            itemTypeAttributes.forEach(plmItemTypeAttribute -> {
                if (plmItemTypeAttribute.getMeasurement() != null) {
                    plmItemTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(plmItemTypeAttribute.getMeasurement().getId()));
                }
                if (plmItemTypeAttribute.getRefSubType() != null) {
                    plmItemTypeAttribute.setRefSubTypeName(getRefSubType(plmItemTypeAttribute.getRefType().name(), plmItemTypeAttribute.getRefSubType()));
                }
            });
        } else {
            itemTypeAttributes = getAttributesFromHierarchy(typeId);
            itemTypeAttributes.forEach(plmItemTypeAttribute -> {
                if (plmItemTypeAttribute.getMeasurement() != null) {
                    plmItemTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(plmItemTypeAttribute.getMeasurement().getId()));
                }
                if (plmItemTypeAttribute.getRefSubType() != null) {
                    plmItemTypeAttribute.setRefSubTypeName(getRefSubType(plmItemTypeAttribute.getRefType().name(), plmItemTypeAttribute.getRefSubType()));

                }
            });
        }
        return itemTypeAttributes;
    }

    public String getRefSubType(String refType, Integer refSubType) {
        String name = null;
        if (refType.equals("ITEM")) {
            PLMItemType itemType = itemTypeRepository.findOne(refSubType);
            name = itemType.getName();
        } else if (refType.equals("CHANGE")) {
            PLMChangeType changeType = changeTypeRepository.findOne(refSubType);
            name = changeType.getName();
        } else if (refType.equals("WORKFLOW")) {
            PLMWorkflowType workflowType = workflowTypeRepository.findOne(refSubType);
            name = workflowType.getName();
        } else if (refType.equals("MANUFACTURER")) {
            PLMManufacturerType manufacturerType = manufacturerTypeRepository.findOne(refSubType);
            name = manufacturerType.getName();
        } else if (refType.equals("MANUFACTURERPART")) {
            PLMManufacturerPartType partType = manufacturerPartTypeRepository.findOne(refSubType);
            name = partType.getName();
        } else if (refType.equals("MESOBJECT")) {
            MESObjectType mesObjectType = mesObjectTypeRepository.findOne(refSubType);
            name = mesObjectType.getName();
        } else if (refType.equals("MROOBJECT")) {
            MROObjectType mroObjectType = mroObjectTypeRepository.findOne(refSubType);
            name = mroObjectType.getName();
        } else if (refType.equals("CUSTOMOBJECT")) {
            CustomObjectType customObjectType = customObjectTypeRepository.findOne(refSubType);
            name = customObjectType.getName();
        } else if (refType.equals("REQUIREMENT")) {
            PLMRequirementType requirementType = requirementTypeRepository.findOne(refSubType);
            name = requirementType.getName();
        } else if (refType.equals("REQUIREMENTDOCUMENT")) {
            PLMRequirementDocumentType documentType = requirementDocumentTypeRepository.findOne(refSubType);
            name = documentType.getName();
        } else if (refType.equals("QUALITY")) {
            PQMQualityType qualityType = qualityTypeRepository.findOne(refSubType);
            name = qualityType.getName();
        }
        return name;
    }

    public List<PLMItemTypeAttribute> getAttributes(Integer typeId, Integer revisionId, Boolean hierarchy) {
        List<PLMItemTypeAttribute> itemTypeAttributes = new LinkedList<>();
        if (!hierarchy) {
            itemTypeAttributes = itemTypeAttributeRepository.findByItemTypeOrderBySeq(typeId);
            itemTypeAttributes.forEach(plmItemTypeAttribute -> {
                if (plmItemTypeAttribute.getMeasurement() != null) {
                    plmItemTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(plmItemTypeAttribute.getMeasurement().getId()));
                }
            });
        } else {
            itemTypeAttributes = getAttributesFromHierarchy(typeId, revisionId);
            itemTypeAttributes.forEach(plmItemTypeAttribute -> {
                if (plmItemTypeAttribute.getMeasurement() != null) {
                    plmItemTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(plmItemTypeAttribute.getMeasurement().getId()));
                }
            });
        }
        return itemTypeAttributes;
    }

    @Transactional(readOnly = true)
    public PLMItemTypeAttribute getConfigurableAttributesBomRules(Integer typeId) {
        PLMItemTypeAttribute plmItemTypeAttribute = new PLMItemTypeAttribute();
        Map<String, PLMItemTypeAttribute> lovsForList = new LinkedHashMap<>();
        List<PLMItemTypeAttribute> types = itemTypeAttributeRepository.findByItemTypeOrderBySeq(typeId);
        for (PLMItemTypeAttribute type : types) {
            if (type.getConfigurable() == true && type.getDataType().toString().equals("LIST")) {
                lovsForList.put(type.getName(), type);
            }
        }
        plmItemTypeAttribute.setLovsForList(lovsForList);
        return plmItemTypeAttribute;
    }

    private List<PLMItemTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<PLMItemTypeAttribute> collector = new ArrayList<>();
        List<PLMItemTypeAttribute> atts = itemTypeAttributeRepository.findByItemTypeOrderBySeq(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private List<PLMItemTypeAttribute> getAttributesFromHierarchy(Integer typeId, Integer revisionId) {
        List<PLMItemTypeAttribute> collector = new ArrayList<>();
        List<PLMItemTypeAttribute> atts = itemTypeAttributeRepository.findByItemTypeOrderBySeq(typeId);
        atts.forEach(att -> {
            if (att.getConfigurable()) {
                ItemConfigurableAttributes itemConfigurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(revisionId, att);
                if (itemConfigurableAttribute != null) {
                    att.setConfigurableAttr(itemConfigurableAttribute.getValues());
                }
            }
        });
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<PLMItemTypeAttribute> collector, Integer typeId) {
        PLMItemType itemType = itemTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<PLMItemTypeAttribute> atts = itemTypeAttributeRepository.findByItemTypeOrderBySeq(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    public PLMItemTypeAttribute getAttribute(Integer id) {
        return itemTypeAttributeRepository.findOne(id);
    }

    @Transactional
    public PLMItemTypeAttribute createAttribute(PLMItemTypeAttribute attribute) {
        List<PLMItemTypeAttribute> plmItemTypeAttributes = itemTypeAttributeRepository.findByItemType(attribute.getItemType());
        if (plmItemTypeAttributes.size() >= 0) {
            attribute.setSeq(plmItemTypeAttributes.size() + 1);
        }
        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.save(attribute);
        PLMItemType itemType = itemTypeRepository.findOne(itemTypeAttribute.getItemType());

        if (itemTypeAttribute.getConfigurable()) {
            List<PLMItem> items = itemRepository.findByItemTypeAndConfigurableTrue(itemType);
            for (PLMItem item : items) {
                List<PLMItemRevision> itemRevisions = itemRevisionRepository.findByItemMasterAndReleasedFalse(item.getId());
                itemRevisions.forEach(itemRevision -> {
                    ItemConfigurableAttributes configurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(itemRevision.getId(), itemTypeAttribute);
                    if (configurableAttribute == null) {
                        ItemConfigurableAttributes itemConfigurableAttribute = new ItemConfigurableAttributes();
                        itemConfigurableAttribute.setAttribute(itemTypeAttribute);
                        itemConfigurableAttribute.setItem(itemRevision.getId());
                        itemConfigurableAttribute.setValues(itemTypeAttribute.getLov().getValues());
                        itemConfigurableAttribute = itemConfigurableAttributesRepository.save(itemConfigurableAttribute);
                    }
                });
            }
        }
        if (itemTypeAttribute.getDataType().toString().equals("FORMULA")) {
            List<PLMItem> items = itemRepository.findByItemType(itemType);
            for (PLMItem item : items) {
                PLMItemAttribute objectAttribute = new PLMItemAttribute();
                objectAttribute.setId(new ObjectAttributeId(item.getId(), itemTypeAttribute.getId()));
                itemAttributeRepository.save(objectAttribute);
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(PLMObjectType.ITEMTYPE, itemTypeAttribute));
        return itemTypeAttribute;
    }

    @Transactional
    public PLMItemTypeAttribute updateAttribute(PLMItemTypeAttribute attribute) {
        PLMItemType plmItemType = itemTypeRepository.findOne(attribute.getItemType());
        PLMItemTypeAttribute existAttribute = itemTypeAttributeRepository.findOne(attribute.getId());
        Boolean existConfigurableValue = existAttribute.getChangeControlled();
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(PLMObjectType.ITEMTYPE, existAttribute, attribute));
        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.save(attribute);

        if (!itemTypeAttribute.getConfigurable() && existConfigurableValue) {
            List<PLMItem> items = itemRepository.findByItemType(plmItemType);
            for (PLMItem item : items) {
                List<PLMItemRevision> itemRevisions = itemRevisionRepository.findByItemMasterAndReleasedFalse(item.getId());
                itemRevisions.forEach(itemRevision -> {
                    ItemConfigurableAttributes configurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(itemRevision.getId(), itemTypeAttribute);
                    if (configurableAttribute != null) {
                        itemConfigurableAttributesRepository.delete(configurableAttribute.getId());
                    }
                });
            }
        }

        if (!existConfigurableValue && itemTypeAttribute.getConfigurable()) {
            List<PLMItem> items = itemRepository.findByItemTypeAndConfigurableTrue(plmItemType);
            for (PLMItem item : items) {
                List<PLMItemRevision> itemRevisions = itemRevisionRepository.findByItemMasterAndReleasedFalse(item.getId());
                itemRevisions.forEach(itemRevision -> {
                    ItemConfigurableAttributes configurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(itemRevision.getId(), itemTypeAttribute);
                    if (configurableAttribute == null) {
                        ItemConfigurableAttributes itemConfigurableAttribute = new ItemConfigurableAttributes();
                        itemConfigurableAttribute.setAttribute(itemTypeAttribute);
                        itemConfigurableAttribute.setItem(itemRevision.getId());
                        itemConfigurableAttribute.setValues(itemTypeAttribute.getLov().getValues());
                        itemConfigurableAttribute = itemConfigurableAttributesRepository.save(itemConfigurableAttribute);
                    }
                });
            }
        }
        return itemTypeAttribute;
    }

    @Transactional
    public void deleteAttribute(Integer id) {
        PLMItemTypeAttribute attribute = itemTypeAttributeRepository.findOne(id);
        List<PLMItemTypeAttribute> itemTypeAttributes = itemTypeAttributeRepository.findByItemType(attribute.getItemType());
        if (itemTypeAttributes.size() > 0) {
            for (PLMItemTypeAttribute itemTypeAttribute : itemTypeAttributes) {
                if (itemTypeAttribute.getSeq() > attribute.getSeq()) {
                    itemTypeAttribute.setSeq(itemTypeAttribute.getSeq() - 1);
                    itemTypeAttribute = itemTypeAttributeRepository.save(itemTypeAttribute);
                }
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(PLMObjectType.ITEMTYPE, attribute));
        itemTypeAttributeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getAllItemTypeAttributes(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType));
        typeAttributes.forEach(objectTypeAttribute -> {
            if (objectTypeAttribute.getMeasurement() != null) {
                objectTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(objectTypeAttribute.getMeasurement().getId()));
            }
        });
        return typeAttributes;
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMItemType getItemTypeName(String name) {
        return itemTypeRepository.findByName(name);
    }

    public void findItemByAutoNumId(Integer autoNumber) {
        List<PLMItemType> itemTypes = itemTypeRepository.findByItemNumberSourceId(autoNumber);
        List<PLMChangeType> changeTypes = changeTypeRepository.findByAutoNumberSourceId(autoNumber);
        List<PQMQualityType> qualityTypes = qualityTypeRepository.findByNumberSourceId(autoNumber);
        List<RmObjectType> rmObjectTypes = rmObjectTypeRepository.findByNumberSourceId(autoNumber);
        List<PQMInspectionPlanType> inspectionPlanTypes = inspectionPlanTypeRepository.findByInspectionNumberSourceId(autoNumber);
        List<PLMItem> items = new ArrayList<PLMItem>();
        List<Integer> itemIds = new ArrayList<>();
        if (!itemTypes.isEmpty()) {
            for (PLMItemType type : itemTypes) {
                itemIds.add(type.getId());
            }
        }
        items = itemRepository.findByItemTypeIdIn(itemIds);
        if (!items.isEmpty()) {
            throw new CassiniException(messageSource.getMessage("this_autonumber_has_items_cannot_delete_this_autonumber", null, "This auto number has items, Cannot delete this auto number", LocaleContextHolder.getLocale()));
        }
        if (changeTypes.size() > 0) {
            throw new CassiniException(messageSource.getMessage("autonumber_already_in_use", null, "This auto number already in use", LocaleContextHolder.getLocale()));
        }
        if (qualityTypes.size() > 0) {
            throw new CassiniException(messageSource.getMessage("autonumber_already_in_use", null, "This auto number already in use", LocaleContextHolder.getLocale()));
        }

        if (rmObjectTypes.size() > 0) {
            throw new CassiniException(messageSource.getMessage("autonumber_already_in_use", null, "This auto number already in use", LocaleContextHolder.getLocale()));
        }

        if (inspectionPlanTypes.size() > 0) {
            throw new CassiniException(messageSource.getMessage("autonumber_already_in_use", null, "This auto number already in use", LocaleContextHolder.getLocale()));
        }
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItem> getItemTypeItems(Integer itemTypeId) {
        PLMItemType itemType = itemTypeRepository.findOne(itemTypeId);
        List<PLMItem> plmItems = itemRepository.findByItemType(itemType);
        return plmItems;
    }

    public PLMItemTypeAttribute getAttributeByName(Integer typeId, String name) {
        return itemTypeAttributeRepository.findByItemTypeAndName(typeId, name);
    }

    public List<ObjectTypeAttribute> getAttributeName(String name) {
        return objectTypeAttributeRepository.findByName(name);
    }

    @Transactional
    public PLMItemTypeAttribute changeAttributeSeq(Integer targetId, Integer actualId) {
        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(actualId);
        PLMItemTypeAttribute itemTypeAttribute1 = itemTypeAttributeRepository.findOne(targetId);
        List<PLMItemTypeAttribute> plmItemTypeAttributes = itemTypeAttributeRepository.findByItemType(itemTypeAttribute.getItemType());
        if ((itemTypeAttribute.getSeq() > itemTypeAttribute1.getSeq())) {
            for (PLMItemTypeAttribute itemTypeAttribute2 : plmItemTypeAttributes) {
                if (itemTypeAttribute1.getId().equals(itemTypeAttribute2.getId()) || itemTypeAttribute.getId().equals(itemTypeAttribute2.getId())) {
                } else {
                    if ((itemTypeAttribute1.getSeq() < itemTypeAttribute2.getSeq()) && (itemTypeAttribute.getSeq() > itemTypeAttribute2.getSeq())) {
                        itemTypeAttribute2.setSeq(itemTypeAttribute2.getSeq() + 1);
                        itemTypeAttribute2 = itemTypeAttributeRepository.save(itemTypeAttribute2);
                    }
                }
            }
            if (itemTypeAttribute != null) {
                itemTypeAttribute.setSeq(itemTypeAttribute1.getSeq());
                itemTypeAttribute = itemTypeAttributeRepository.save(itemTypeAttribute);

            }
            if (itemTypeAttribute1 != null) {
                itemTypeAttribute1.setSeq(itemTypeAttribute1.getSeq() + 1);
                itemTypeAttribute1 = itemTypeAttributeRepository.save(itemTypeAttribute1);

            }
        } else {
            for (PLMItemTypeAttribute itemTypeAttribute2 : plmItemTypeAttributes) {
                if (itemTypeAttribute1.getId().equals(itemTypeAttribute2.getId()) || itemTypeAttribute.getId().equals(itemTypeAttribute2.getId())) {
                } else {
                    if ((itemTypeAttribute1.getSeq() > itemTypeAttribute2.getSeq()) && (itemTypeAttribute.getSeq() < itemTypeAttribute2.getSeq())) {
                        itemTypeAttribute2.setSeq(itemTypeAttribute2.getSeq() - 1);
                        itemTypeAttribute2 = itemTypeAttributeRepository.save(itemTypeAttribute2);
                    }
                }
            }
            if (itemTypeAttribute != null) {
                itemTypeAttribute.setSeq(itemTypeAttribute1.getSeq());
                itemTypeAttribute = itemTypeAttributeRepository.save(itemTypeAttribute);

            }
            if (itemTypeAttribute1 != null) {
                itemTypeAttribute1.setSeq(itemTypeAttribute1.getSeq() - 1);
                itemTypeAttribute1 = itemTypeAttributeRepository.save(itemTypeAttribute1);

            }
        }
        return null;

    }

    @Transactional
    public PLMChangeTypeAttribute changeEcoAttributeSeq(Integer targetId, Integer actualId) {
        PLMChangeTypeAttribute changeTypeAttribute = changeTypeAttributeRepository.findOne(actualId);
        PLMChangeTypeAttribute changeTypeAttribute1 = changeTypeAttributeRepository.findOne(targetId);
        List<PLMChangeTypeAttribute> plmChangeTypeAttributes = changeTypeAttributeRepository.findByChangeType(changeTypeAttribute.getChangeType());
        if ((changeTypeAttribute.getSeq() > changeTypeAttribute1.getSeq())) {
            for (PLMChangeTypeAttribute changeTypeAttribute2 : plmChangeTypeAttributes) {
                if (changeTypeAttribute1.getId().equals(changeTypeAttribute2.getId()) || changeTypeAttribute.getId().equals(changeTypeAttribute2.getId())) {
                } else {
                    if ((changeTypeAttribute1.getSeq() < changeTypeAttribute2.getSeq()) && (changeTypeAttribute.getSeq() > changeTypeAttribute2.getSeq())) {
                        changeTypeAttribute2.setSeq(changeTypeAttribute2.getSeq() + 1);
                        changeTypeAttribute2 = changeTypeAttributeRepository.save(changeTypeAttribute2);
                    }
                }
            }
            if (changeTypeAttribute != null) {
                changeTypeAttribute.setSeq(changeTypeAttribute1.getSeq());
                changeTypeAttribute = changeTypeAttributeRepository.save(changeTypeAttribute);

            }
            if (changeTypeAttribute1 != null) {
                changeTypeAttribute1.setSeq(changeTypeAttribute1.getSeq() + 1);
                changeTypeAttribute1 = changeTypeAttributeRepository.save(changeTypeAttribute1);

            }
        } else {
            for (PLMChangeTypeAttribute changeTypeAttribute2 : plmChangeTypeAttributes) {
                if (changeTypeAttribute1.getId().equals(changeTypeAttribute2.getId()) || changeTypeAttribute.getId().equals(changeTypeAttribute2.getId())) {
                } else {
                    if ((changeTypeAttribute1.getSeq() > changeTypeAttribute2.getSeq()) && (changeTypeAttribute.getSeq() < changeTypeAttribute2.getSeq())) {
                        changeTypeAttribute2.setSeq(changeTypeAttribute2.getSeq() - 1);
                        changeTypeAttribute2 = changeTypeAttributeRepository.save(changeTypeAttribute2);
                    }
                }
            }
            if (changeTypeAttribute != null) {
                changeTypeAttribute.setSeq(changeTypeAttribute1.getSeq());
                changeTypeAttribute = changeTypeAttributeRepository.save(changeTypeAttribute);

            }
            if (changeTypeAttribute1 != null) {
                changeTypeAttribute1.setSeq(changeTypeAttribute1.getSeq() - 1);
                changeTypeAttribute1 = changeTypeAttributeRepository.save(changeTypeAttribute1);

            }
        }
        return null;

    }

    @Transactional
    public PLMManufacturerTypeAttribute changeMfrAttributeSeq(Integer targetId, Integer actualId) {
        PLMManufacturerTypeAttribute manufacturerTypeAttribute = manufacturerTypeAttributeRepository.findOne(actualId);
        PLMManufacturerTypeAttribute manufacturerTypeAttribute1 = manufacturerTypeAttributeRepository.findOne(targetId);
        List<PLMManufacturerTypeAttribute> plmManufacturerTypeAttributes = manufacturerTypeAttributeRepository.findByMfrType(manufacturerTypeAttribute.getMfrType());
        if ((manufacturerTypeAttribute.getSeq() > manufacturerTypeAttribute1.getSeq())) {
            for (PLMManufacturerTypeAttribute manufacturerTypeAttribute2 : plmManufacturerTypeAttributes) {
                if (manufacturerTypeAttribute1.getId().equals(manufacturerTypeAttribute2.getId()) || manufacturerTypeAttribute.getId().equals(manufacturerTypeAttribute2.getId())) {
                } else {
                    if ((manufacturerTypeAttribute1.getSeq() < manufacturerTypeAttribute2.getSeq()) && (manufacturerTypeAttribute.getSeq() > manufacturerTypeAttribute2.getSeq())) {
                        manufacturerTypeAttribute2.setSeq(manufacturerTypeAttribute2.getSeq() + 1);
                        manufacturerTypeAttribute2 = manufacturerTypeAttributeRepository.save(manufacturerTypeAttribute2);
                    }
                }
            }
            if (manufacturerTypeAttribute != null) {
                manufacturerTypeAttribute.setSeq(manufacturerTypeAttribute1.getSeq());
                manufacturerTypeAttribute = manufacturerTypeAttributeRepository.save(manufacturerTypeAttribute);

            }
            if (manufacturerTypeAttribute1 != null) {
                manufacturerTypeAttribute1.setSeq(manufacturerTypeAttribute1.getSeq() + 1);
                manufacturerTypeAttribute1 = manufacturerTypeAttributeRepository.save(manufacturerTypeAttribute1);

            }
        } else {
            for (PLMManufacturerTypeAttribute manufacturerTypeAttribute2 : plmManufacturerTypeAttributes) {
                if (manufacturerTypeAttribute1.getId().equals(manufacturerTypeAttribute2.getId()) || manufacturerTypeAttribute.getId().equals(manufacturerTypeAttribute2.getId())) {
                } else {
                    if ((manufacturerTypeAttribute1.getSeq() > manufacturerTypeAttribute2.getSeq()) && (manufacturerTypeAttribute.getSeq() < manufacturerTypeAttribute2.getSeq())) {
                        manufacturerTypeAttribute2.setSeq(manufacturerTypeAttribute2.getSeq() - 1);
                        manufacturerTypeAttribute2 = manufacturerTypeAttributeRepository.save(manufacturerTypeAttribute2);
                    }
                }
            }
            if (manufacturerTypeAttribute != null) {
                manufacturerTypeAttribute.setSeq(manufacturerTypeAttribute1.getSeq());
                manufacturerTypeAttribute = manufacturerTypeAttributeRepository.save(manufacturerTypeAttribute);

            }
            if (manufacturerTypeAttribute1 != null) {
                manufacturerTypeAttribute1.setSeq(manufacturerTypeAttribute1.getSeq() - 1);
                manufacturerTypeAttribute1 = manufacturerTypeAttributeRepository.save(manufacturerTypeAttribute1);

            }
        }
        return null;

    }

    @Transactional
    public PLMManufacturerPartTypeAttribute changeMfrPartAttributeSeq(Integer targetId, Integer actualId) {
        PLMManufacturerPartTypeAttribute manufacturerPartTypeAttribute = manufacturerPartTypeAttributeRepository.findOne(actualId);
        PLMManufacturerPartTypeAttribute manufacturerPartTypeAttribute1 = manufacturerPartTypeAttributeRepository.findOne(targetId);
        List<PLMManufacturerPartTypeAttribute> plmManufacturerPartTypeAttributes = manufacturerPartTypeAttributeRepository.findByMfrPartType(manufacturerPartTypeAttribute.getMfrPartType());
        if ((manufacturerPartTypeAttribute.getSeq() > manufacturerPartTypeAttribute1.getSeq())) {
            for (PLMManufacturerPartTypeAttribute manufacturerPartTypeAttribute2 : plmManufacturerPartTypeAttributes) {
                if (manufacturerPartTypeAttribute1.getId().equals(manufacturerPartTypeAttribute2.getId()) || manufacturerPartTypeAttribute.getId().equals(manufacturerPartTypeAttribute2.getId())) {
                } else {
                    if ((manufacturerPartTypeAttribute1.getSeq() < manufacturerPartTypeAttribute2.getSeq()) && (manufacturerPartTypeAttribute.getSeq() > manufacturerPartTypeAttribute2.getSeq())) {
                        manufacturerPartTypeAttribute2.setSeq(manufacturerPartTypeAttribute2.getSeq() + 1);
                        manufacturerPartTypeAttribute2 = manufacturerPartTypeAttributeRepository.save(manufacturerPartTypeAttribute2);
                    }
                }
            }
            if (manufacturerPartTypeAttribute != null) {
                manufacturerPartTypeAttribute.setSeq(manufacturerPartTypeAttribute1.getSeq());
                manufacturerPartTypeAttribute = manufacturerPartTypeAttributeRepository.save(manufacturerPartTypeAttribute);

            }
            if (manufacturerPartTypeAttribute1 != null) {
                manufacturerPartTypeAttribute1.setSeq(manufacturerPartTypeAttribute1.getSeq() + 1);
                manufacturerPartTypeAttribute1 = manufacturerPartTypeAttributeRepository.save(manufacturerPartTypeAttribute1);

            }
        } else {
            for (PLMManufacturerPartTypeAttribute manufacturerPartTypeAttribute2 : plmManufacturerPartTypeAttributes) {
                if (manufacturerPartTypeAttribute1.getId().equals(manufacturerPartTypeAttribute2.getId()) || manufacturerPartTypeAttribute.getId().equals(manufacturerPartTypeAttribute2.getId())) {
                } else {
                    if ((manufacturerPartTypeAttribute1.getSeq() > manufacturerPartTypeAttribute2.getSeq()) && (manufacturerPartTypeAttribute.getSeq() < manufacturerPartTypeAttribute2.getSeq())) {
                        manufacturerPartTypeAttribute2.setSeq(manufacturerPartTypeAttribute2.getSeq() - 1);
                        manufacturerPartTypeAttribute2 = manufacturerPartTypeAttributeRepository.save(manufacturerPartTypeAttribute2);
                    }
                }
            }
            if (manufacturerPartTypeAttribute != null) {
                manufacturerPartTypeAttribute.setSeq(manufacturerPartTypeAttribute1.getSeq());
                manufacturerPartTypeAttribute = manufacturerPartTypeAttributeRepository.save(manufacturerPartTypeAttribute);

            }
            if (manufacturerPartTypeAttribute1 != null) {
                manufacturerPartTypeAttribute1.setSeq(manufacturerPartTypeAttribute1.getSeq() - 1);
                manufacturerPartTypeAttribute = manufacturerPartTypeAttributeRepository.save(manufacturerPartTypeAttribute1);

            }
        }
        return null;

    }

    @Transactional
    public PLMWorkflowTypeAttribute changeWorkflowAttributeSeq(Integer targetId, Integer actualId) {
        PLMWorkflowTypeAttribute workflowTypeAttribute = workflowTypeAttributeRepository.findOne(actualId);
        PLMWorkflowTypeAttribute workflowTypeAttribute1 = workflowTypeAttributeRepository.findOne(targetId);
        List<PLMWorkflowTypeAttribute> plmWorkflowTypeAttributes = workflowTypeAttributeRepository.findByWorkflowType(workflowTypeAttribute.getWorkflowType());
        if ((workflowTypeAttribute.getSeq() > workflowTypeAttribute1.getSeq())) {
            for (PLMWorkflowTypeAttribute workflowTypeAttribute2 : plmWorkflowTypeAttributes) {
                if (workflowTypeAttribute1.getId().equals(workflowTypeAttribute2.getId()) || workflowTypeAttribute.getId().equals(workflowTypeAttribute2.getId())) {
                } else {
                    if ((workflowTypeAttribute1.getSeq() < workflowTypeAttribute2.getSeq()) && (workflowTypeAttribute.getSeq() > workflowTypeAttribute2.getSeq())) {
                        workflowTypeAttribute2.setSeq(workflowTypeAttribute2.getSeq() + 1);
                        workflowTypeAttribute2 = workflowTypeAttributeRepository.save(workflowTypeAttribute2);
                    }
                }
            }
            if (workflowTypeAttribute != null) {
                workflowTypeAttribute.setSeq(workflowTypeAttribute1.getSeq());
                workflowTypeAttribute = workflowTypeAttributeRepository.save(workflowTypeAttribute);

            }
            if (workflowTypeAttribute1 != null) {
                workflowTypeAttribute1.setSeq(workflowTypeAttribute1.getSeq() + 1);
                workflowTypeAttribute1 = workflowTypeAttributeRepository.save(workflowTypeAttribute1);

            }
        } else {
            for (PLMWorkflowTypeAttribute workflowTypeAttribute2 : plmWorkflowTypeAttributes) {
                if (workflowTypeAttribute1.getId().equals(workflowTypeAttribute2.getId()) || workflowTypeAttribute.getId().equals(workflowTypeAttribute2.getId())) {
                } else {
                    if ((workflowTypeAttribute.getSeq() > workflowTypeAttribute2.getSeq()) && (workflowTypeAttribute.getSeq() < workflowTypeAttribute2.getSeq())) {
                        workflowTypeAttribute2.setSeq(workflowTypeAttribute2.getSeq() - 1);
                        workflowTypeAttribute2 = workflowTypeAttributeRepository.save(workflowTypeAttribute2);
                    }
                }
            }
            if (workflowTypeAttribute != null) {
                workflowTypeAttribute.setSeq(workflowTypeAttribute1.getSeq());
                workflowTypeAttribute = workflowTypeAttributeRepository.save(workflowTypeAttribute);

            }
            if (workflowTypeAttribute1 != null) {
                workflowTypeAttribute1.setSeq(workflowTypeAttribute1.getSeq() - 1);
                workflowTypeAttribute1 = workflowTypeAttributeRepository.save(workflowTypeAttribute1);

            }
        }
        return null;

    }

    @Transactional
    public RmObjectTypeAttribute changeRmAttributeSeq(Integer targetId, Integer actualId) {
        RmObjectTypeAttribute rmObjectTypeAttribute = rmObjectTypeAttributeRepository.findOne(actualId);
        RmObjectTypeAttribute rmObjectTypeAttribute1 = rmObjectTypeAttributeRepository.findOne(targetId);
        List<RmObjectTypeAttribute> rmObjectTypeAttributes = rmObjectTypeAttributeRepository.findByRmObjectType(rmObjectTypeAttribute.getRmObjectType());
        if ((rmObjectTypeAttribute.getSeq() > rmObjectTypeAttribute1.getSeq())) {
            for (RmObjectTypeAttribute rmObjectTypeAttribute2 : rmObjectTypeAttributes) {
                if (rmObjectTypeAttribute1.getId().equals(rmObjectTypeAttribute2.getId()) || rmObjectTypeAttribute.getId().equals(rmObjectTypeAttribute2.getId())) {
                } else {
                    if ((rmObjectTypeAttribute1.getSeq() < rmObjectTypeAttribute2.getSeq()) && (rmObjectTypeAttribute.getSeq() > rmObjectTypeAttribute2.getSeq())) {
                        rmObjectTypeAttribute2.setSeq(rmObjectTypeAttribute2.getSeq() + 1);
                        rmObjectTypeAttribute2 = rmObjectTypeAttributeRepository.save(rmObjectTypeAttribute2);
                    }
                }
            }
            if (rmObjectTypeAttribute != null) {
                rmObjectTypeAttribute.setSeq(rmObjectTypeAttribute1.getSeq());
                rmObjectTypeAttribute = rmObjectTypeAttributeRepository.save(rmObjectTypeAttribute);

            }
            if (rmObjectTypeAttribute1 != null) {
                rmObjectTypeAttribute1.setSeq(rmObjectTypeAttribute.getSeq() + 1);
                rmObjectTypeAttribute1 = rmObjectTypeAttributeRepository.save(rmObjectTypeAttribute1);

            }
        } else {
            for (RmObjectTypeAttribute rmObjectTypeAttribute2 : rmObjectTypeAttributes) {
                if (rmObjectTypeAttribute1.getId().equals(rmObjectTypeAttribute2.getId()) || rmObjectTypeAttribute.getId().equals(rmObjectTypeAttribute2.getId())) {
                } else {
                    if ((rmObjectTypeAttribute1.getSeq() > rmObjectTypeAttribute2.getSeq()) && (rmObjectTypeAttribute.getSeq() < rmObjectTypeAttribute2.getSeq())) {
                        rmObjectTypeAttribute2.setSeq(rmObjectTypeAttribute2.getSeq() - 1);
                        rmObjectTypeAttribute2 = rmObjectTypeAttributeRepository.save(rmObjectTypeAttribute2);
                    }
                }
            }
            if (rmObjectTypeAttribute != null) {
                rmObjectTypeAttribute.setSeq(rmObjectTypeAttribute1.getSeq());
                rmObjectTypeAttribute = rmObjectTypeAttributeRepository.save(rmObjectTypeAttribute);

            }
            if (rmObjectTypeAttribute1 != null) {
                rmObjectTypeAttribute1.setSeq(rmObjectTypeAttribute1.getSeq() - 1);
                rmObjectTypeAttribute = rmObjectTypeAttributeRepository.save(rmObjectTypeAttribute1);

            }
        }
        return null;

    }

    @Transactional
    public PLMItemTypeAttribute generateAttributeSeq() {
        List<PLMItemType> itemTypes = itemTypeRepository.findAll();
        if (itemTypes.size() > 0) {
            for (PLMItemType itemType : itemTypes) {
                List<PLMItemTypeAttribute> itemTypeAttributes = itemTypeAttributeRepository.findByItemTypeOrderByName(itemType.getId());
                if (itemTypeAttributes.size() > 0) {
                    Integer count = 0;
                    for (PLMItemTypeAttribute itemTypeAttribute : itemTypeAttributes) {
                        if (itemTypeAttribute.getSeq() == null) {
                            itemTypeAttribute.setSeq(count + 1);
                            count = itemTypeAttribute.getSeq();
                            itemTypeAttribute = itemTypeAttributeRepository.save(itemTypeAttribute);
                        }
                    }
                }
            }
        }
        List<PLMChangeType> changeTypes = changeTypeRepository.findAll();
        if (changeTypes.size() > 0) {
            for (PLMChangeType changeType : changeTypes) {
                List<PLMChangeTypeAttribute> changeTypeAttributes = changeTypeAttributeRepository.findByChangeTypeOrderByName(changeType.getId());
                if (changeTypeAttributes.size() > 0) {
                    Integer count = 0;
                    for (PLMChangeTypeAttribute changeTypeAttribute : changeTypeAttributes) {
                        if (changeTypeAttribute.getSeq() == null) {
                            changeTypeAttribute.setSeq(count + 1);
                            count = changeTypeAttribute.getSeq();
                            changeTypeAttribute = changeTypeAttributeRepository.save(changeTypeAttribute);
                        }
                    }
                }
            }
        }
        List<PLMManufacturerType> manufacturerTypes = manufacturerTypeRepository.findAll();
        if (manufacturerTypes.size() > 0) {
            for (PLMManufacturerType manufacturerType : manufacturerTypes) {
                List<PLMManufacturerTypeAttribute> manufacturerTypeAttributes = manufacturerTypeAttributeRepository.findByMfrTypeOrderByName(manufacturerType.getId());
                if (manufacturerTypeAttributes.size() > 0) {
                    Integer count = 0;
                    for (PLMManufacturerTypeAttribute manufacturerTypeAttribute : manufacturerTypeAttributes) {
                        if (manufacturerTypeAttribute.getSeq() == null) {
                            manufacturerTypeAttribute.setSeq(count + 1);
                            count = manufacturerTypeAttribute.getSeq();
                            manufacturerTypeAttribute = manufacturerTypeAttributeRepository.save(manufacturerTypeAttribute);
                        }
                    }
                }
            }
        }
        List<PLMManufacturerPartType> manufacturerPartTypes = manufacturerPartTypeRepository.findAll();
        if (manufacturerPartTypes.size() > 0) {
            for (PLMManufacturerPartType manufacturerPartType : manufacturerPartTypes) {
                List<PLMManufacturerPartTypeAttribute> manufacturerPartTypeAttributes = manufacturerPartTypeAttributeRepository.findByMfrPartTypeOrderByName(manufacturerPartType.getId());
                if (manufacturerPartTypeAttributes.size() > 0) {
                    Integer count = 0;
                    for (PLMManufacturerPartTypeAttribute manufacturerPartTypeAttribute : manufacturerPartTypeAttributes) {
                        if (manufacturerPartTypeAttribute.getSeq() == null) {
                            manufacturerPartTypeAttribute.setSeq(count + 1);
                            count = manufacturerPartTypeAttribute.getSeq();
                            manufacturerPartTypeAttribute = manufacturerPartTypeAttributeRepository.save(manufacturerPartTypeAttribute);
                        }
                    }
                }
            }
        }
        List<PLMWorkflowType> workflowTypes = workflowTypeRepository.findAll();
        if (workflowTypes.size() > 0) {
            for (PLMWorkflowType workflowType : workflowTypes) {
                List<PLMWorkflowTypeAttribute> workflowTypeAttributes = workflowTypeAttributeRepository.findByWorkflowTypeOrderByName(workflowType.getId());
                if (workflowTypeAttributes.size() > 0) {
                    Integer count = 0;
                    for (PLMWorkflowTypeAttribute workflowTypeAttribute : workflowTypeAttributes) {
                        if (workflowTypeAttribute.getSeq() == null) {
                            workflowTypeAttribute.setSeq(count + 1);
                            count = workflowTypeAttribute.getSeq();
                            workflowTypeAttribute = workflowTypeAttributeRepository.save(workflowTypeAttribute);
                        }
                    }
                }
            }
        }
        List<RmObjectType> rmObjectTypes = rmObjectTypeRepository.findAll();
        if (rmObjectTypes.size() > 0) {
            for (RmObjectType rmObjectType : rmObjectTypes) {
                List<RmObjectTypeAttribute> rmObjectTypeAttributes = rmObjectTypeAttributeRepository.findByRmObjectTypeOrderByName(rmObjectType.getId());
                if (rmObjectTypeAttributes.size() > 0) {
                    Integer count = 0;
                    for (RmObjectTypeAttribute rmObjectTypeAttribute : rmObjectTypeAttributes) {
                        if (rmObjectTypeAttribute.getSeq() == null) {
                            rmObjectTypeAttribute.setSeq(count + 1);
                            count = rmObjectTypeAttribute.getSeq();
                            rmObjectTypeAttribute = rmObjectTypeAttributeRepository.save(rmObjectTypeAttribute);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public ClassificationTypesDto getItemTypesByLov(Integer id) {
        ClassificationTypesDto classificationTypesDto = new ClassificationTypesDto();
        Lov lov = lovRepository.findOne(id);
        List<PLMItemType> itemTypes = itemTypeRepository.findByRevisionSequence(lov);
        List<RmObjectType> objectTypes = rmObjectTypeRepository.findByRevisionSequence(lov);
        classificationTypesDto.getItemTypes().addAll(itemTypes);
        classificationTypesDto.getObjectTypes().addAll(objectTypes);
        return classificationTypesDto;
    }

    @Transactional(readOnly = true)
    public PLMItemTypeAttribute getTypeAttributeName(Integer typeId, String name) {
        PLMItemType itemType = itemTypeRepository.findOne(typeId);
        PLMItemTypeAttribute itemTypeAttribute = null;
        if (itemType.getParentType() != null) {
            itemTypeAttribute = itemTypeAttributeRepository.findByItemTypeAndName(itemType.getParentType(), name);
        }
        return itemTypeAttribute;
    }


    @Transactional(readOnly = true)
    public List<PLMItemTypeAttribute> getBomRollupAttributes() {
        List<PLMItemTypeAttribute> itemTypeAttributes = itemTypeAttributeRepository.getIntegerAndDoubleTypeAttributes(DataType.INTEGER, DataType.DOUBLE);
        HashMap<String, PLMItemTypeAttribute> itemTypeAttributeHashMap = new LinkedHashMap<>();
        itemTypeAttributes.forEach(plmItemTypeAttribute -> {
            if (plmItemTypeAttribute.getMeasurement() != null) {
                plmItemTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(plmItemTypeAttribute.getMeasurement().getId()));
                for (MeasurementUnit measurementUnit : plmItemTypeAttribute.getMeasurement().getMeasurementUnits()) {
                    if (measurementUnit.getBaseUnit()) {
                        plmItemTypeAttribute.setMeasurementUnit(measurementUnit);
                    }
                }
            }
            itemTypeAttributeHashMap.put(plmItemTypeAttribute.getName(), plmItemTypeAttribute);
        });

        List<PLMItemTypeAttribute> typeAttributes = new ArrayList<>();
        for (PLMItemTypeAttribute itemTypeAttribute : itemTypeAttributeHashMap.values()) {
            typeAttributes.add(itemTypeAttribute);
        }
        return typeAttributes;
    }

    @Transactional
    public List<Lov> getAllLovs() {
        List<Lov> lovs = lovRepository.findByOrderByIdDesc();

        lovs.forEach(lov -> {
            Integer itemTypes = itemTypeRepository.getItemTypeCountByRevisionSequence(lov.getId());
            if (itemTypes > 0) {
                lov.setUsedLov(true);
            } else {
                Integer changeTypes = changeTypeRepository.getChangeTypeCountByLov(lov.getId());
                if (changeTypes > 0) {
                    lov.setUsedLov(true);
                } else {
                    Integer inspectionPlanTypes = inspectionPlanTypeRepository.getInspectionPlanTypesByLov(lov.getId());
                    if (inspectionPlanTypes > 0) {
                        lov.setUsedLov(true);
                    } else {
                        Integer problemReportTypes = problemReportTypeRepository.getUsedProblemReportsByLov(lov.getId());
                        if (problemReportTypes > 0) {
                            lov.setUsedLov(true);
                        } else {
                            Integer types = ncrTypeRepository.getUsedProblemReportsByLov(lov.getId());
                            if (types > 0) {
                                lov.setUsedLov(true);
                            } else {
                                Integer rmObjectTypes = rmObjectTypeRepository.getRmObjectTypesByLov(lov.getId());
                                if (rmObjectTypes > 0) {
                                    lov.setUsedLov(true);
                                } else {
                                    Integer objectTypeAttributes = objectTypeAttributeRepository.getObjectTypeAttributesByLov(lov.getId());
                                    if (objectTypeAttributes > 0) {
                                        lov.setUsedLov(true);
                                    } else {
                                        Integer maintenanceOperations = mroMaintenanceOperationRepository.getMaintenanceOperationsByLov(lov.getId());
                                        if (maintenanceOperations > 0) {
                                            lov.setUsedLov(true);
                                        } else {
                                            Integer workOrderOperations = workOrderOperationRepository.getWorkOrderOperationsByLov(lov.getId());
                                            if (workOrderOperations > 0) {
                                                lov.setUsedLov(true);
                                            } else {
                                                Integer requirementDocumentTypes = plmRequirementDocumentTypeRepository.getReqDocTypeByLov(lov.getId());
                                                if (requirementDocumentTypes > 0) {
                                                    lov.setUsedLov(true);
                                                } else {
                                                    Integer requirementTypes = plmRequirementTypeRepository.getRequirementTypesByLov(lov.getId());
                                                    if (requirementTypes > 0) {
                                                        lov.setUsedLov(true);
                                                    } else {
                                                        Integer workflowTypes = workflowTypeRepository.getWorkflowTypesByLov(lov.getId());
                                                        if (workflowTypes > 0) {
                                                            lov.setUsedLov(true);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        return lovs;
    }

    @Transactional
    public Integer getLovValueUsedCount(Integer lov, String lovValue) {
        Integer count = 0;
        count += itemRepository.getItemTypeLovValueCount(lovValue, lov);
        count += changeRequestRepository.getChangeTypeLovValueCount(lovValue, lov);
        count += inspectionPlanTypeRepository.getPlanTypeLovValueCount(lovValue, lov);
        count += problemReportTypeRepository.getPrTypeLovValueCount(lov, lovValue);
        count += ncrTypeRepository.getNcrTypeLovValueCount(lov, lovValue);
        count += workOrderOperationRepository.getLovValueCount(lov, lovValue);
        count += objectAttributeRepository.getAttributeLovValueCount(lov, lovValue);
        count += plmRequirementDocumentTypeRepository.getTypeLovValueCount(lov, lovValue);
        count += plmRequirementTypeRepository.getTypeLovValueCount(lov, lovValue);
        count += workflowTypeRepository.getTypeLovValueCount(lov, lovValue);
        return count;
    }

    @Transactional
    public Integer getAutonumberValueUsedCount(Integer autoNumber, String autoNumberValue) {
        Integer count = 0;
        count += itemTypeRepository.getItemTypeAutoNumberValueCount(autoNumber);
        count += changeTypeRepository.getChangeTypeAutoNumberValueCount(autoNumber);
        count += qualityTypeRepository.getQualityTypeAutoNumberValueCount(autoNumber);
        count += pmObjectTypeRepository.getPMTypeAutoNumberValueCount(autoNumber);
        count += mesObjectTypeRepository.getMesTypeAutoNumberValueCount(autoNumber);
        count += mroObjectTypeRepository.getMROTypeAutoNumberValueCount(autoNumber);
        count += supplierTypeRepository.getSupplierTypeAutoNumberValueCount(autoNumber);
        count += pgcObjectTypeRepository.getPGCTypeAutoNumberValueCount(autoNumber);
        count += workflowTypeRepository.getWorkflowTypeAutoNumberValueCount(autoNumber);
        count += customObjectTypeRepository.getCustomeObjectTypeAutoNumberValueCount(autoNumber);
        return count;
    }

    @Override
    public Boolean isSubtypeOf(String s1, Object s2, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        itemTypeRepository = webApplicationContext.getBean(ItemTypeRepository.class);
        PLMItemType itemType = (PLMItemType) s2;
        if (subTypeId != null && checkWithId(itemType, subTypeId)) {
            return true;
        }
        if (itemType.getName().equalsIgnoreCase(s1)) {
            return true;
        } else {
            if (itemType.getParentType() != null)
                return compareWithParent(itemTypeRepository.findOne(itemType.getParentType()), s1);
        }
        return false;
    }

    public Boolean checkWithId(PLMItemType plmItemType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        itemTypeRepository = webApplicationContext.getBean(ItemTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmItemType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = checkWithId(itemTypeRepository.findOne(plmItemType.getParentType()), typeId);
        }
        return flag;
    }

    private Boolean compareWithParent(PLMItemType plmItemType, String s1) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        itemTypeRepository = webApplicationContext.getBean(ItemTypeRepository.class);
        Boolean flag = false;
        if (plmItemType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = compareWithParent(itemTypeRepository.findOne(plmItemType.getParentType()), s1);
        }
        return flag;
    }

    @Override
    public List<PLMItemTypeAttribute> getTypeAttributes(Integer typeId, Boolean hierarchy) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        itemTypeAttributeRepository = webApplicationContext.getBean(ItemTypeAttributeRepository.class);
        measurementUnitRepository = webApplicationContext.getBean(MeasurementUnitRepository.class);
        itemTypeRepository = webApplicationContext.getBean(ItemTypeRepository.class);
        List<PLMItemTypeAttribute> itemTypeAttributes = new LinkedList<>();
        if (!hierarchy) {
            itemTypeAttributes = itemTypeAttributeRepository.findByItemTypeOrderBySeq(typeId);
            itemTypeAttributes.forEach(plmItemTypeAttribute -> {
                if (plmItemTypeAttribute.getMeasurement() != null) {
                    plmItemTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(plmItemTypeAttribute.getMeasurement().getId()));
                }
            });
        } else {
            itemTypeAttributes = getAttributesFromHierarchy(typeId);
            itemTypeAttributes.forEach(plmItemTypeAttribute -> {
                if (plmItemTypeAttribute.getMeasurement() != null) {
                    plmItemTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(plmItemTypeAttribute.getMeasurement().getId()));
                }
            });
        }
        return itemTypeAttributes;
    }

    @Transactional(readOnly = true)
    public List<ObjectAttribute> getAttributeValues(Integer attributeId) {
        ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(attributeId);
        List<ObjectAttribute> values = objectAttributeRepository.findByAttributeDef(attributeId);
        if (values.size() > 0) {
            Boolean noValues = false;
            for (ObjectAttribute objectAttribute : values) {
                if (objectTypeAttribute.getDataType().equals(DataType.TEXT)) {
                    if (objectAttribute.getStringValue() == null || objectAttribute.getStringValue().equals("")) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.INTEGER)) {
                    if (objectAttribute.getIntegerValue() == null) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.LONGTEXT)) {
                    if (objectAttribute.getLongTextValue() == null || objectAttribute.getLongTextValue().equals("")) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.RICHTEXT)) {
                    if (objectAttribute.getRichTextValue() == null || objectAttribute.getRichTextValue().equals("")) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.BOOLEAN)) {
                    if (objectAttribute.getBooleanValue()) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.DATE)) {
                    if (objectAttribute.getDateValue() == null) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.DOUBLE)) {
                    if (objectAttribute.getDoubleValue() == null) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.TIMESTAMP)) {
                    if (objectAttribute.getTimestampValue() == null) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.TIME)) {
                    if (objectAttribute.getTimeValue() == null) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.ATTACHMENT)) {
                    if (objectAttribute.getAttachmentValues().length == 0) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.IMAGE)) {
                    if (objectAttribute.getImageValue() == null) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.HYPERLINK)) {
                    if (objectAttribute.getImageValue() == null) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.LIST) && !objectTypeAttribute.isListMultiple()) {
                    if (objectAttribute.getListValue() == null) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.LIST) && objectTypeAttribute.isListMultiple()) {
                    if (objectAttribute.getMListValue().length == 0) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.CURRENCY)) {
                    if (objectAttribute.getCurrencyValue() == null) {
                        noValues = true;
                        break;
                    }
                } else if (objectTypeAttribute.getDataType().equals(DataType.OBJECT)) {
                    if (objectAttribute.getRefValue() == null) {
                        noValues = true;
                        break;
                    }
                }

            }
            if (noValues) {
                throw new CassiniException(messageSource.getMessage("this_attribute_no_values_error_msg", null, "This attribute not provided values to some objects", LocaleContextHolder.getLocale()));

            }
        }

        return null;
    }

    @Transactional(readOnly = true)
    public List<PLMLifeCycle> getItemTypeLifecycles() {
        List<PLMLifeCycle> plmLifeCycles = itemTypeRepository.getUniqueItemTypeLifeCycles();
        return plmLifeCycles;
    }


    public List<PLMItemType> getAllSubTypes(Integer pdmItemType) {
        List<Integer> ids = getAllSubTypeIds(pdmItemType);
        return itemTypeRepository.findByIdIn(ids);
    }


}