package com.cassinisys.plm.service.classification;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.cassinisys.platform.service.core.TypeSystem;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.AssetEvents;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.filtering.MROObjectCriteria;
import com.cassinisys.plm.filtering.MROObjectPredicateBuilder;
import com.cassinisys.plm.model.mes.MESObjectTypesDto;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.mro.dto.MROObjectTypesDto;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mro.*;
import com.cassinisys.plm.service.plm.ItemTypeService;
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
public class MROObjectTypeService implements CrudService<MROObjectType, Integer>,
        TypeSystem, ClassificationTypeService<MROObjectType, MROObjectTypeAttribute> {

    @Autowired
    private MROObjectTypeRepository objectTypeRepository;
    @Autowired
    private MROObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private MROWorkRequestTypeRepository workRequestTypeRepository;
    @Autowired
    private MROWorkOrderTypeRepository workOrderTypeRepository;
    @Autowired
    private MROSparePartTypeRepository sparePartTypeRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MROObjectRepository mroObjectRepository;
    @Autowired
    private MROObjectAttributeRepository mroObjectAttributeRepository;
    @Autowired
    private MROAssetTypeRepository mroAssetTypeRepository;
    @Autowired
    private MROMeterTypeRepository mroMeterTypeRepository;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private MROObjectPredicateBuilder mroObjectPredicateBuilder;
    @Autowired
    private MROAssetRepository assetRepository;
    @Autowired
    private MROMeterRepository meterRepository;
    @Autowired
    private MROWorkOrderRepository workOrderRepository;
    @Autowired
    private MROWorkRequestRepository workRequestRepository;
    @Autowired
    private MROSparePartRepository sparePartRepository;
    @Autowired
    private MessageSource messageSource;

    @PostConstruct
    public void InitMROTypeService() {
        objectTypeService.registerTypeSystem("MROObjectType", new MROObjectTypeService());
    }

    @Override
    @PreAuthorize("hasPermission(#mroObjectType,'create')")
    public MROObjectType create(MROObjectType mroObjectType) {
        MROObjectType resourceType = objectTypeRepository.save(mroObjectType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.PRODUCTIONRESOURCETYPE, resourceType));
        return resourceType;
    }

    @Override
    @PreAuthorize("hasPermission(#resourceType.id ,'edit')")
    public MROObjectType update(MROObjectType resourceType) {
        MROObjectType mroObjectType = objectTypeRepository.findOne(resourceType.getId());
        return objectTypeRepository.save(mroObjectType);
    }

    @Override
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        objectTypeRepository.delete(id);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MROObjectType get(Integer id) {
        return objectTypeRepository.findOne(id);
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROObjectType> getAll() {
        return objectTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MROObjectType> findMultipleTypes(List<Integer> ids) {
        return objectTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<MROObjectType> getTypeTree() {
        List<MROObjectType> types = objectTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (MROObjectType type : types) {
            visitResourceTypeChildren(type);
        }
        return types;
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROObjectType> getRootTypes() {
        return objectTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
    }

    @Override
    public List<MROObjectType> getChildren(Integer parent) {
        return objectTypeRepository.findByParentTypeOrderByCreatedDateAsc(parent);
    }

    @Override
    public List<MROObjectType> getClassificationTree() {
        List<MROObjectType> types = getRootTypes();
        for (MROObjectType type : types) {
            visitResourceTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MROObjectType> getTypeChildren(Integer id) {
        return objectTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitResourceTypeChildren(MROObjectType parent) {
        List<MROObjectType> childrens = getTypeChildren(parent.getId());
        for (MROObjectType child : childrens) {
            visitResourceTypeChildren(child);
        }
        parent.setChildren(childrens);
    }

    @Override
    public MROObjectTypeAttribute createAttribute(MROObjectTypeAttribute attribute) {
        List<MROObjectTypeAttribute> qualityTypeAttributes = objectTypeAttributeRepository.findByTypeOrderBySeq(attribute.getType());
        if (qualityTypeAttributes.size() >= 0) {
            attribute.setSeq(qualityTypeAttributes.size() + 1);
        }
        attribute = objectTypeAttributeRepository.save(attribute);
        PLMObjectType objectType = getAttributeObjectType(attribute);
        MROObjectType mroObjectType = objectTypeRepository.findOne(attribute.getType());
        if (attribute.getDataType().toString().equals("FORMULA")) {
            createFormulaAttribute(attribute, mroObjectType);
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(objectType, attribute));
        return attribute;

    }

    private PLMObjectType getAttributeObjectType(MROObjectTypeAttribute attribute) {
        PLMObjectType objectType = null;
        if (attribute.getObjectType().name().equals("SPAREPARTTYPE")) {
            objectType = PLMObjectType.SPAREPARTTYPE;
        } else if (attribute.getObjectType().name().equals("WORKORDERTYPE")) {
            objectType = PLMObjectType.WORKORDERTYPE;
        } else if (attribute.getObjectType().name().equals("WORKREQUESTTYPE")) {
            objectType = PLMObjectType.WORKREQUESTTYPE;
        } else if (attribute.getObjectType().name().equals("ASSETTYPE")) {
            objectType = PLMObjectType.ASSETTYPE;
        } else if (attribute.getObjectType().name().equals("METERTYPE")) {
            objectType = PLMObjectType.METERTYPE;
        }
        return objectType;
    }


    private void createFormulaAttribute(MROObjectTypeAttribute attribute, MROObjectType mroObjectType) {
        if (attribute.getObjectType().name().equals("ASSETTYPE")) {
            MROAssetType assetType = mroAssetTypeRepository.findOne(mroObjectType.getId());
            List<MROAsset> assets = assetRepository.findByType(assetType);
            for (MROAsset asset : assets) {
                MROObjectAttribute objectAttribute = new MROObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(asset.getId(), attribute.getId()));
                mroObjectAttributeRepository.save(objectAttribute);
            }
        } else if (attribute.getObjectType().name().equals("METERTYPE")){
            MROMeterType meterType = mroMeterTypeRepository.findOne(mroObjectType.getId());
            List<MROMeter> meters = meterRepository.findByType(meterType);
            for (MROMeter meter : meters) {
                MROObjectAttribute objectAttribute = new MROObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(meter.getId(), attribute.getId()));
                mroObjectAttributeRepository.save(objectAttribute);
            }
        }else if (attribute.getObjectType().name().equals("WORKORDERTYPE")){
            MROWorkOrderType workOrderType = workOrderTypeRepository.findOne(mroObjectType.getId());
            List<MROWorkOrder> workOrders = workOrderRepository.findByType(workOrderType);
            for (MROWorkOrder workOrder : workOrders) {
                MROObjectAttribute objectAttribute = new MROObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(workOrder.getId(), attribute.getId()));
                mroObjectAttributeRepository.save(objectAttribute);
            }
        }else if (attribute.getObjectType().name().equals("WORKREQUESTTYPE")){
            MROWorkRequestType workRequestType = workRequestTypeRepository.findOne(mroObjectType.getId());
            List<MROWorkRequest> workRequests = workRequestRepository.findByType(workRequestType);
            for (MROWorkRequest workRequest : workRequests) {
                MROObjectAttribute objectAttribute = new MROObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(workRequest.getId(), attribute.getId()));
                mroObjectAttributeRepository.save(objectAttribute);
            }
        }else if (attribute.getObjectType().name().equals("SPAREPARTTYPE")){
            MROSparePartType sparePartType = sparePartTypeRepository.findOne(mroObjectType.getId());
            List<MROSparePart> spareParts = sparePartRepository.findByType(sparePartType);
            for (MROSparePart sparePart : spareParts) {
                MROObjectAttribute objectAttribute = new MROObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(sparePart.getId(), attribute.getId()));
                mroObjectAttributeRepository.save(objectAttribute);
            }
        }
    }

    @Override
    public MROObjectTypeAttribute updateAttribute(MROObjectTypeAttribute attribute) {
        MROObjectTypeAttribute workOrderTypeAttribute = objectTypeAttributeRepository.findOne(attribute.getId());
        PLMObjectType objectType = getAttributeObjectType(attribute);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(objectType, workOrderTypeAttribute, attribute));
        return objectTypeAttributeRepository.save(attribute);
    }

    @Override
    public void deleteAttribute(Integer id) {
        MROObjectTypeAttribute attribute = objectTypeAttributeRepository.findOne(id);
        List<MROObjectTypeAttribute> workOrderTypeAttributes = objectTypeAttributeRepository.findByTypeOrderBySeq(attribute.getType());
        if (workOrderTypeAttributes.size() > 0) {
            for (MROObjectTypeAttribute workOrderTypeAttribute : workOrderTypeAttributes) {
                if (workOrderTypeAttribute.getSeq() > attribute.getSeq()) {
                    workOrderTypeAttribute.setSeq(workOrderTypeAttribute.getSeq() - 1);
                    workOrderTypeAttribute = objectTypeAttributeRepository.save(workOrderTypeAttribute);
                }
            }
        }
        PLMObjectType objectType = getAttributeObjectType(attribute);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(objectType, attribute));
        objectTypeAttributeRepository.delete(id);
    }

    @Override
    public MROObjectTypeAttribute getAttribute(Integer id) {
        return objectTypeAttributeRepository.findOne(id);
    }


    @Transactional
    public MROObjectAttribute createMROObjectAttribute(MROObjectAttribute attribute) {
        return mroObjectAttributeRepository.save(attribute);
    }


    @Transactional
    public MROObjectAttribute updateMROObjectAttribute(MROObjectAttribute attribute) {
        MROObjectAttribute oldValue = mroObjectAttributeRepository.findByObjectAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, MROObjectAttribute.class);
        attribute = mroObjectAttributeRepository.save(attribute);
        MROObject mroObject = mroObjectRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new AssetEvents.AssetAttributesUpdatedEvent("mro", mroObject.getId(), mroObject.getObjectType(), oldValue, attribute));

       /* if (mroObject.getObjectType().toString().equals("MROASSET")) {
            applicationEventPublisher.publishEvent(new AssetEvents.AssetAttributesUpdatedEvent("mro", mroObject.getId(), mroObject.getObjectType(),oldValue,attribute));

            applicationEventPublisher.publishEvent(new AssetEvents.AssetAttributesUpdatedEvent(mroObject, oldValue, attribute));
        } else if (mroObject.getObjectType().toString().equals("MROMETER")) {
            applicationEventPublisher.publishEvent(new MeterEvents.MeterAttributesUpdatedEvent(mroObject, oldValue, attribute));
        } else if (mroObject.getObjectType().toString().equals("MROSPAREPART")) {
            applicationEventPublisher.publishEvent(new SparePartsEvents.SparePartAttributesUpdatedEvent(mroObject, oldValue, attribute));

        } else if (mroObject.getObjectType().toString().equals("MROWORKREQUEST")) {
            applicationEventPublisher.publishEvent(new WorkRequestEvents.WorkRequestAttributesUpdatedEvent(mroObject, oldValue, attribute));

        } else if (mroObject.getObjectType().toString().equals("MROWORKORDER")) {
            applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderAttributesUpdatedEvent(mroObject, oldValue, attribute));

        } else if (mroObject.getObjectType().toString().equals("MROMAINTENANCEPLAN")) {
            applicationEventPublisher.publishEvent(new MaintenancePlanEvents.MaintenancePlanAttributesUpdatedEvent(mroObject, oldValue, attribute));
        }*/
        return attribute;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MROObjectTypeAttribute> getAttributes(Integer typeId, Boolean hier) {
        List<MROObjectTypeAttribute> mroObjectTypeAttributes = new ArrayList<>();
        if (!hier) {
            mroObjectTypeAttributes = objectTypeAttributeRepository.findByTypeOrderBySeq(typeId);
            mroObjectTypeAttributes.forEach(mroObjectTypeAttribute -> {
                if (mroObjectTypeAttribute.getRefSubType() != null) {
                    mroObjectTypeAttribute.setRefSubTypeName(itemTypeService.getRefSubType(mroObjectTypeAttribute.getRefType().name(), mroObjectTypeAttribute.getRefSubType()));
                }
            });
        } else {
            mroObjectTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return mroObjectTypeAttributes;
    }

    @Transactional(readOnly = true)
    public List<MROObjectTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<MROObjectTypeAttribute> collector = new ArrayList<>();
        List<MROObjectTypeAttribute> atts = objectTypeAttributeRepository.findByTypeOrderBySeq(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<MROObjectTypeAttribute> collector, Integer typeId) {
        MROObjectType objectType = objectTypeRepository.findOne(typeId);
        if (objectType != null) {
            Integer parentType = objectType.getParentType();
            if (parentType != null) {
                List<MROObjectTypeAttribute> atts = objectTypeAttributeRepository.findByTypeOrderBySeq(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

		/*-------------------------   WorkRequest Types ----------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#workRequestType,'create')")
    public MROWorkRequestType createWorkRequestType(MROWorkRequestType workRequestType) {
        MROWorkRequestType mesWorkRequestType = workRequestTypeRepository.save(workRequestType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.WORKREQUESTTYPE, mesWorkRequestType));
        return mesWorkRequestType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#workRequestType.id ,'edit')")
    public MROWorkRequestType updateWorkRequestType(Integer id, MROWorkRequestType workRequestType) {
        MROWorkRequestType oldQualityType = workRequestTypeRepository.findOne(workRequestType.getId());
        MROWorkRequestType existingPmType;
        if (workRequestType.getParentType() == null) {
            existingPmType = workRequestTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(workRequestType.getName());
        } else {
            existingPmType = workRequestTypeRepository.findByNameEqualsIgnoreCaseAndParentType(workRequestType.getName(), workRequestType.getParentType());
        }
        if ( existingPmType != null && !workRequestType.getId().equals(existingPmType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingPmType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.WORKREQUESTTYPE, oldQualityType, workRequestType));
        return workRequestTypeRepository.save(workRequestType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteWorkRequestType(Integer id) {
        workRequestTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MROWorkRequestType getWorkRequestType(Integer id) {
        return workRequestTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROWorkRequestType> getAllWorkRequestTypes() {
        return workRequestTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROWorkRequestType> findMultipleWorkRequestTypes(List<Integer> ids) {
        return workRequestTypeRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROWorkRequestType> getWorkRequestTypeTree() {
        List<MROWorkRequestType> types = workRequestTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MROWorkRequestType type : types) {
            visitWorkRequestTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MROWorkRequestType> getWorkRequestTypeChildren(Integer id) {
        return workRequestTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitWorkRequestTypeChildren(MROWorkRequestType parent) {
        List<MROWorkRequestType> childrens = getWorkRequestTypeChildren(parent.getId());
        for (MROWorkRequestType child : childrens) {
            visitWorkRequestTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }
    
    /* -------------------- WorkOrderType -------------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#workOrderType,'create')")
    public MROWorkOrderType createWorkOrderType(MROWorkOrderType workOrderType) {
        MROWorkOrderType mesWorkOrderType = workOrderTypeRepository.save(workOrderType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.WORKORDERTYPE, mesWorkOrderType));
        return mesWorkOrderType;
    }


    @Transactional
    @PreAuthorize("hasPermission(#workOrderType.id ,'edit')")
    public MROWorkOrderType updateWorkOrderType(Integer id, MROWorkOrderType workOrderType) {
        MROWorkOrderType oldWorkOrderType = workOrderTypeRepository.findOne(id);
        MROWorkOrderType existingPmType;
        if (workOrderType.getParentType() == null) {
            existingPmType = workOrderTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(workOrderType.getName());
        } else {
            existingPmType = workOrderTypeRepository.findByNameEqualsIgnoreCaseAndParentType(workOrderType.getName(), workOrderType.getParentType());
        }
        if ( existingPmType != null && !workOrderType.getId().equals(existingPmType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingPmType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.WORKORDERTYPE, oldWorkOrderType, workOrderType));
        return workOrderTypeRepository.save(workOrderType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteWorkOrderType(Integer id) {
        workOrderTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MROWorkOrderType getWorkOrderType(Integer id) {
        return workOrderTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROWorkOrderType> getAllWorkOrderTypes() {
        return workOrderTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROWorkOrderType> findMultipleWorkOrderTypes(List<Integer> ids) {
        return workOrderTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROWorkOrderType> getWorkOrderTypeTree() {
        List<MROWorkOrderType> types = workOrderTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MROWorkOrderType type : types) {
            visitWorkOrderTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROWorkOrderType> getRepairWorkOrderTypeTree() {
        List<MROWorkOrderType> types = workOrderTypeRepository.findByTypeAndParentTypeIsNullOrderByIdAsc(WorkOrderType.REPAIR);
        for (MROWorkOrderType type : types) {
            visitWorkOrderTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MROWorkOrderType> getWorkOrderTypeChildren(Integer id) {
        return workOrderTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitWorkOrderTypeChildren(MROWorkOrderType parent) {
        List<MROWorkOrderType> childrens = getWorkOrderTypeChildren(parent.getId());
        for (MROWorkOrderType child : childrens) {
            visitWorkOrderTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    

    /* ----------------------------- mesObjectTypesTree -----------------------*/

    @Transactional
    public List<MROObjectType> getObjectRootTypes() {
        return objectTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROObjectType> getMROObjectTypeTree() {
        List<MROObjectType> types = getObjectRootTypes();
        for (MROObjectType objectType : types) {
            visitObjectTypeChildren(objectType);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MROObjectType> getObjectTypeChildren(Integer id) {
        return objectTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitObjectTypeChildren(MROObjectType parent) {
        List<MROObjectType> childrens = getObjectTypeChildren(parent.getId());
        for (MROObjectType child : childrens) {
            visitObjectTypeChildren(child);
        }
        parent.setChildren(childrens);
    }
    


    		/*-------------------------   sparePart Types ----------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#sparePartType,'create')")
    public MROSparePartType createSparePartType(MROSparePartType sparePartType) {
        MROSparePartType mesSparePartType = sparePartTypeRepository.save(sparePartType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.SPAREPARTTYPE, mesSparePartType));
        return mesSparePartType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#sparePartType.id ,'edit')")
    public MROSparePartType updateSparePartType(Integer id, MROSparePartType sparePartType) {
        MROSparePartType oldSparePartType = sparePartTypeRepository.findOne(sparePartType.getId());
        MROSparePartType existingPmType;
        if (sparePartType.getParentType() == null) {
            existingPmType = sparePartTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(sparePartType.getName());
        } else {
            existingPmType = sparePartTypeRepository.findByNameEqualsIgnoreCaseAndParentType(sparePartType.getName(), sparePartType.getParentType());
        }
        if ( existingPmType != null && !sparePartType.getId().equals(existingPmType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingPmType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.SPAREPARTTYPE, oldSparePartType, sparePartType));
        return sparePartTypeRepository.save(sparePartType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteSparePartType(Integer id) {
        sparePartTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MROSparePartType getSparePartType(Integer id) {
        return sparePartTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROSparePartType> getAllSparePartTypes() {
        return sparePartTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROSparePartType> findMultipleSparePartTypes(List<Integer> ids) {
        return sparePartTypeRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROSparePartType> getSparePartTypeTree() {
        List<MROSparePartType> types = sparePartTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MROSparePartType type : types) {
            visitSparePartTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MROSparePartType> getSparePartTypeChildren(Integer id) {
        return sparePartTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitSparePartTypeChildren(MROSparePartType parent) {
        List<MROSparePartType> childrens = getSparePartTypeChildren(parent.getId());
        for (MROSparePartType child : childrens) {
            visitSparePartTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

        /* -------------------- assetType -------------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#assetType,'create')")
    public MROAssetType createAssetType(MROAssetType assetType) {
        MROAssetType mroAssetType = mroAssetTypeRepository.save(assetType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.ASSETTYPE, mroAssetType));
        return mroAssetType;
    }


    @Transactional
    @PreAuthorize("hasPermission(#assetType.id ,'edit')")
    public MROAssetType updateAssetType(Integer id, MROAssetType assetType) {
        MROAssetType oldAssetType = mroAssetTypeRepository.findOne(id);
        MROAssetType existingPmType;
        if (assetType.getParentType() == null) {
            existingPmType = mroAssetTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(assetType.getName());
        } else {
            existingPmType = mroAssetTypeRepository.findByNameEqualsIgnoreCaseAndParentType(assetType.getName(), assetType.getParentType());
        }
        if ( existingPmType != null && !assetType.getId().equals(existingPmType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingPmType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.ASSETTYPE, oldAssetType, assetType));
        return mroAssetTypeRepository.save(assetType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteAssetType(Integer id) {
        mroAssetTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MROAssetType getAssetType(Integer id) {
        return mroAssetTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROAssetType> getAllAssetTypes() {
        return mroAssetTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROAssetType> findMultipleAssetTypes(List<Integer> ids) {
        return mroAssetTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROAssetType> getAssetTypeTree() {
        List<MROAssetType> types = mroAssetTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MROAssetType type : types) {
            visitAssetTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MROAssetType> getAssetTypeChildren(Integer id) {
        return mroAssetTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitAssetTypeChildren(MROAssetType parent) {
        List<MROAssetType> childrens = getAssetTypeChildren(parent.getId());
        for (MROAssetType child : childrens) {
            visitAssetTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

        /* -------------------- WorkOrderType -------------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#meterType,'create')")
    public MROMeterType createMeterType(MROMeterType meterType) {
        MROMeterType mroMeterType = mroMeterTypeRepository.save(meterType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.METERTYPE, mroMeterType));
        return mroMeterType;
    }


    @Transactional
    @PreAuthorize("hasPermission(#meterType.id ,'edit')")
    public MROMeterType updateMeterType(Integer id, MROMeterType meterType) {
        MROMeterType oldMeterType = mroMeterTypeRepository.findOne(id);
        MROMeterType existingPmType;
        if (meterType.getParentType() == null) {
            existingPmType = mroMeterTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(meterType.getName());
        } else {
            existingPmType = mroMeterTypeRepository.findByNameEqualsIgnoreCaseAndParentType(meterType.getName(), meterType.getParentType());
        }
        if ( existingPmType != null && !meterType.getId().equals(existingPmType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingPmType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.METERTYPE, oldMeterType, meterType));
        return mroMeterTypeRepository.save(meterType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteMeterType(Integer id) {
        mroMeterTypeRepository.delete(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MROMeterType getMeterType(Integer id) {
        return mroMeterTypeRepository.findOne(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROMeterType> getAllMeterTypes() {
        return mroMeterTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROMeterType> findMultipleMeterTypes(List<Integer> ids) {
        return mroMeterTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROMeterType> getMeterTypeTree() {
        List<MROMeterType> types = mroMeterTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (MROMeterType type : types) {
            visitMeterTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<MROMeterType> getMeterTypeChildren(Integer id) {
        return mroMeterTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitMeterTypeChildren(MROMeterType parent) {
        List<MROMeterType> childrens = getMeterTypeChildren(parent.getId());
        for (MROMeterType child : childrens) {
            visitMeterTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    public MESObjectTypesDto getAllMROObjectTypesTree() {
        MESObjectTypesDto mesObjectTypesDto = new MESObjectTypesDto();
        mesObjectTypesDto.getWorkOrderTypes().addAll(getWorkOrderTypeTree());
        return mesObjectTypesDto;
    }

    @Override
    public Boolean isSubtypeOf(String s1, Object s2, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        objectTypeRepository = webApplicationContext.getBean(MROObjectTypeRepository.class);
        MROObjectType mroObjectType = (MROObjectType) s2;
        if (subTypeId != null && checkWithId(mroObjectType, subTypeId)) {
            return true;
        }
        if (mroObjectType.getName().equalsIgnoreCase(s1)) {
            return true;
        } else {
            if (mroObjectType.getParentType() != null)
                return compareWithParent(objectTypeRepository.findOne(mroObjectType.getParentType()), s1);
        }
        return false;
    }

    public Boolean checkWithId(MROObjectType plmItemType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        objectTypeRepository = webApplicationContext.getBean(MROObjectTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmItemType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = checkWithId(objectTypeRepository.findOne(plmItemType.getParentType()), typeId);
        }
        return flag;
    }

    private Boolean compareWithParent(MROObjectType mroObjectType, String s1) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        objectTypeRepository = webApplicationContext.getBean(MROObjectTypeRepository.class);
        Boolean flag = false;
        if (mroObjectType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (mroObjectType.getParentType() != null)
                flag = compareWithParent(objectTypeRepository.findOne(mroObjectType.getParentType()), s1);
        }
        return flag;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MROObjectTypeAttribute> getTypeAttributes(Integer typeId, Boolean hier) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        objectTypeAttributeRepository = webApplicationContext.getBean(MROObjectTypeAttributeRepository.class);
        objectTypeRepository = webApplicationContext.getBean(MROObjectTypeRepository.class);
        List<MROObjectTypeAttribute> mesObjectTypeAttributes = new ArrayList<>();
        if (!hier) {
            mesObjectTypeAttributes = objectTypeAttributeRepository.findByTypeOrderBySeq(typeId);
        } else {
            mesObjectTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return mesObjectTypeAttributes;
    }


    @Transactional(readOnly = true)
    public List<MROObjectAttribute> getUsedMROObjectAttributes(Integer attributeId) {
        return mroObjectAttributeRepository.findByAttributeDef(attributeId);
    }

    @Transactional(readOnly = true)
    public Page<MROObject> getMROObjects(Pageable pageable, MROObjectCriteria mroObjectCriteria) {
        Predicate predicate = mroObjectPredicateBuilder.getDefaultPredicates(mroObjectCriteria, QMROObject.mROObject);
        return mroObjectRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public Object getMROObjectById(Integer id) {
        return mroObjectRepository.findOne(id);
    }

    public MROObjectTypesDto getMROObjectsTypeTree() {
        MROObjectTypesDto mroObjectTypesDto = new MROObjectTypesDto();
        mroObjectTypesDto.getAssetTypes().addAll(getAssetTypeTree());
        mroObjectTypesDto.getMeterTypes().addAll(getMeterTypeTree());
        mroObjectTypesDto.getMroSparePartTypes().addAll(getSparePartTypeTree());
        mroObjectTypesDto.getWorkRequestTypes().addAll(getWorkRequestTypeTree());
        mroObjectTypesDto.getWorkOrderTypes().addAll(getWorkOrderTypeTree());
        return mroObjectTypesDto;
    }

}

