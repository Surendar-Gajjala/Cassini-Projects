package com.cassinisys.plm.service.classification;

import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.cassinisys.platform.service.core.TypeSystem;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemAttribute;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.service.plm.ItemTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by reddy on 6/13/17.
 */
@Service
public class ChangeTypeService implements CrudService<PLMChangeType, Integer>,
        TypeSystem, ClassificationTypeService<PLMChangeType, PLMChangeTypeAttribute> {
    @Autowired
    private ChangeTypeRepository changeTypeRepository;
    @Autowired
    private ChangeTypeAttributeRepository changeTypeAttributeRepository;
    @Autowired
    private MCOTypeRepository mcoTypeRepository;
    @Autowired
    private ECOTypeRepository ecoTypeRepository;
    @Autowired
    private ECRTypeRepository ecrTypeRepository;

    @Autowired
    private DCRTypeRepository dcrTypeRepository;

    @Autowired
    private DCOTypeRepository dcoTypeRepository;

    @Autowired
    private DeviationTypeRepository deviationTypeRepository;

    @Autowired
    private WaiverTypeRepository waiverTypeRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private ChangeRepository changeRepository;
    @Autowired
    private ChangeAttributeRepository changeAttributeRepository;

    @PostConstruct
    public void InitChangeService() {
        objectTypeService.registerTypeSystem("changeType", new ChangeTypeService());
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#plmChangeType,'create')")
    public PLMChangeType create(PLMChangeType plmChangeType) {
        checkNotNull(plmChangeType);
        PLMChangeType changeType = changeTypeRepository.save(plmChangeType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.CHANGETYPE, plmChangeType));
        return changeType;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#changeType.id ,'edit')")
    public PLMChangeType update(PLMChangeType changeType) {
        checkNotNull(changeType);
        checkNotNull(changeType.getId());
        PLMChangeType oldChangeType = changeTypeRepository.findOne(changeType.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.CHANGETYPE, oldChangeType, changeType));
        changeType = changeTypeRepository.save(changeType);
        return changeType;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        PLMChangeType changeType = changeTypeRepository.findOne(id);
        if (changeType.getParentType() != null) {
            applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeDeletedEvent(PLMObjectType.CHANGETYPE, changeType));
        }
        changeTypeRepository.delete(id);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMChangeType get(Integer id) {
        return changeTypeRepository.findOne(id);
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMChangeType getChangeType(String type) {
        return changeTypeRepository.findByName(type);
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMChangeType> getAll() {
        return changeTypeRepository.findAll();
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMChangeType> getRootTypes() {
        return changeTypeRepository.findByParentTypeIsNullOrderByIdAsc();
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMChangeType> getChildren(Integer parent) {
        return changeTypeRepository.findByParentTypeOrderByIdAsc(parent);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMChangeType> getClassificationTree() {
        List<PLMChangeType> types = getRootTypes();
        for (PLMChangeType type : types) {
            visitChildren(type);
        }
        return types;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMChangeType> getAllChangeTypesWithAttributes() {
        List<PLMChangeType> types = getAll();
        for (PLMChangeType type : types) {
            if (type.getParentType() != null) {
                type.setParentTypeReference(changeTypeRepository.findOne(type.getParentType()));
            }
            type.setAttributes(changeTypeAttributeRepository.findByChangeTypeOrderByName(type.getId()));
        }
        return types;
    }

    private void visitChildren(PLMChangeType parent) {
        List<PLMChangeType> children = getChildren(parent.getId());
        for (PLMChangeType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    @Transactional
    public PLMChangeTypeAttribute createAttribute(PLMChangeTypeAttribute attribute) {
        List<PLMChangeTypeAttribute> changeTypeAttributes = changeTypeAttributeRepository.findByChangeType(attribute.getChangeType());
        if (changeTypeAttributes.size() >= 0) {
            attribute.setSeq(changeTypeAttributes.size() + 1);
        }
        PLMChangeTypeAttribute changeTypeAttribute = changeTypeAttributeRepository.save(attribute);

        PLMChangeType changeType = changeTypeRepository.findOne(changeTypeAttribute.getChangeType());
        if (changeTypeAttribute.getDataType().toString().equals("FORMULA")) {
            List<PLMChange> plmChanges = changeRepository.findByChangeClass(changeType.getId());
            for (PLMChange change : plmChanges) {
                PLMChangeAttribute objectAttribute = new PLMChangeAttribute();
                objectAttribute.setId(new ObjectAttributeId(change.getId(), changeTypeAttribute.getId()));
                changeAttributeRepository.save(objectAttribute);
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(PLMObjectType.CHANGETYPE, changeTypeAttribute));
        return changeTypeAttribute;
    }

    @Transactional
    public PLMChangeTypeAttribute updateAttribute(PLMChangeTypeAttribute attribute) {
        PLMChangeTypeAttribute changeTypeAttribute1 = changeTypeAttributeRepository.findOne(attribute.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(PLMObjectType.CHANGETYPE, changeTypeAttribute1, attribute));
        PLMChangeTypeAttribute changeTypeAttribute = changeTypeAttributeRepository.save(attribute);
        return changeTypeAttribute;
    }

    public PLMChangeTypeAttribute getAttribute(Integer id) {
        return changeTypeAttributeRepository.findOne(id);
    }

    public List<PLMChangeTypeAttribute> getTypeAttributes(Integer id) {
        return changeTypeAttributeRepository.findByChangeTypeOrderByName(id);
    }

    @Transactional(readOnly = true)
    public List<PLMChangeTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            List<PLMChangeTypeAttribute> changeTypeAttribute = changeTypeAttributeRepository.findByChangeTypeOrderBySeq(typeId);
            changeTypeAttribute.forEach(plmChangeTypeAttribute -> {
                if (plmChangeTypeAttribute.getRefSubType() != null) {
                    plmChangeTypeAttribute.setRefSubTypeName(itemTypeService.getRefSubType(plmChangeTypeAttribute.getRefType().name(), plmChangeTypeAttribute.getRefSubType()));
                }
            });
            return changeTypeAttribute;
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    @Override
    public void deleteAttribute(Integer id) {
        PLMChangeTypeAttribute changeTypeAttribute = changeTypeAttributeRepository.findOne(id);
        List<PLMChangeTypeAttribute> changeTypeAttributes = changeTypeAttributeRepository.findByChangeType(changeTypeAttribute.getChangeType());
        if (changeTypeAttributes.size() > 0) {
            for (PLMChangeTypeAttribute plmChangeTypeAttribute : changeTypeAttributes) {
                if (plmChangeTypeAttribute.getSeq() > changeTypeAttribute.getSeq()) {
                    plmChangeTypeAttribute.setSeq(plmChangeTypeAttribute.getSeq() - 1);
                    plmChangeTypeAttribute = changeTypeAttributeRepository.save(changeTypeAttribute);
                }
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(PLMObjectType.CHANGETYPE, changeTypeAttribute));
        changeTypeAttributeRepository.delete(id);
    }

    private List<PLMChangeTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<PLMChangeTypeAttribute> collector = new ArrayList<>();
        List<PLMChangeTypeAttribute> atts = changeTypeAttributeRepository.findByChangeTypeOrderBySeq(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<PLMChangeTypeAttribute> collector, Integer typeId) {
        PLMChangeType itemType = changeTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<PLMChangeTypeAttribute> atts = changeTypeAttributeRepository.findByChangeTypeOrderByName(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMMCOType> getMcoTypeTree() {
        List<PLMMCOType> types = mcoTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PLMMCOType type : types) {
            visitQcrTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMMCOType> getItemMcoTypeTree() {
        List<PLMMCOType> types = mcoTypeRepository.findByParentTypeIsNullAndMcoTypeOrderByCreatedDateAsc(MCOType.ITEMMCO);
        for (PLMMCOType type : types) {
            visitMcoTypeChildren(type, MCOType.ITEMMCO);
        }
        return types;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMMCOType> getManufacturerMcoTypeTree() {
        List<PLMMCOType> types = mcoTypeRepository.findByParentTypeIsNullAndMcoTypeOrderByCreatedDateAsc(MCOType.OEMPARTMCO);
        for (PLMMCOType type : types) {
            visitMcoTypeChildren(type, MCOType.OEMPARTMCO);
        }
        return types;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMECOType> getEcoTypeTree() {
        List<PLMECOType> types = ecoTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PLMECOType type : types) {
            visitEcoTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PLMMCOType> getQcrTypeChildren(Integer id) {
        return mcoTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    @Transactional(readOnly = true)
    public List<PLMMCOType> getMcoTypeChildren(Integer id, MCOType mcoType) {
        return mcoTypeRepository.findByParentTypeAndMcoTypeOrderByCreatedDateAsc(id, mcoType);
    }

    @Transactional(readOnly = true)
    public List<PLMECOType> getEcoTypeChildren(Integer id) {
        return ecoTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitQcrTypeChildren(PLMMCOType parent) {
        List<PLMMCOType> childrens = getQcrTypeChildren(parent.getId());
        for (PLMMCOType child : childrens) {
            visitQcrTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    private void visitMcoTypeChildren(PLMMCOType parent, MCOType mcoType) {
        List<PLMMCOType> childrens = getMcoTypeChildren(parent.getId(), mcoType);
        for (PLMMCOType child : childrens) {
            visitQcrTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    private void visitEcoTypeChildren(PLMECOType parent) {
        List<PLMECOType> childrens = getEcoTypeChildren(parent.getId());
        for (PLMECOType child : childrens) {
            visitEcoTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMECRType> getEcrTypeTree() {
        List<PLMECRType> types = ecrTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PLMECRType type : types) {
            visitEcrTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PLMECRType> getEcrTypeChildren(Integer id) {
        return ecrTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitEcrTypeChildren(PLMECRType parent) {
        List<PLMECRType> childrens = getEcrTypeChildren(parent.getId());
        for (PLMECRType child : childrens) {
            visitEcrTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMDCRType> getDcrTypeTree() {
        List<PLMDCRType> types = dcrTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PLMDCRType type : types) {
            visitDcrTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PLMDCRType> getDcrTypeChildren(Integer id) {
        return dcrTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitDcrTypeChildren(PLMDCRType parent) {
        List<PLMDCRType> childrens = getDcrTypeChildren(parent.getId());
        for (PLMDCRType child : childrens) {
            visitDcrTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMDCOType> getDcoTypeTree() {
        List<PLMDCOType> types = dcoTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PLMDCOType type : types) {
            visitDcoTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PLMDCOType> getDcoTypeChildren(Integer id) {
        return dcoTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitDcoTypeChildren(PLMDCOType parent) {
        List<PLMDCOType> childrens = getDcoTypeChildren(parent.getId());
        for (PLMDCOType child : childrens) {
            visitDcoTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    @Transactional
    public PLMDeviationType createDeviationType(PLMDeviationType deviationType) {
        return deviationTypeRepository.save(deviationType);
    }

    @Transactional
    public PLMDeviationType updateDeviationType(Integer id, PLMDeviationType deviationType) {
        return deviationTypeRepository.save(deviationType);
    }

    @Transactional
    public void deleteDeviationType(Integer id) {
        deviationTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public PLMDeviationType getDeviationType(Integer id) {
        return deviationTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<PLMDeviationType> getAllDeviationTypes() {
        return deviationTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PLMDeviationType> findMultipleDeviationTypes(List<Integer> ids) {
        return deviationTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMDeviationType> getDevitionTypeTree() {
        List<PLMDeviationType> types = deviationTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PLMDeviationType type : types) {
            visitDeviationTypeChildren(type);
        }
        return types;
    }

    private void visitDeviationTypeChildren(PLMDeviationType parent) {
        List<PLMDeviationType> childrens = getDeviationTypeChildren(parent.getId());
        parent.setChildrens(childrens);
        for (PLMDeviationType child : childrens) {
            visitDeviationTypeChildren(child);
        }
    }

    @Transactional(readOnly = true)
    public List<PLMDeviationType> getDeviationTypeChildren(Integer parent) {
        return deviationTypeRepository.findByParentTypeOrderByCreatedDateAsc(parent);
    }


    @Transactional
    public PLMWaiverType createWaiverType(PLMWaiverType waiverType) {
        return waiverTypeRepository.save(waiverType);
    }

    @Transactional
    public PLMWaiverType updateWaiverType(Integer id, PLMWaiverType waiverType) {
        return waiverTypeRepository.save(waiverType);
    }

    @Transactional
    public void deleteWaiverType(Integer id) {
        waiverTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public PLMWaiverType getWaiverType(Integer id) {
        return waiverTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<PLMWaiverType> getAllWaiverTypes() {
        return waiverTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PLMWaiverType> findMultipleWaiverTypes(List<Integer> ids) {
        return waiverTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMWaiverType> getWaiverTypeTree() {
        List<PLMWaiverType> types = waiverTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PLMWaiverType type : types) {
            visitWaiverTypeChildren(type);
        }
        return types;
    }

    private void visitWaiverTypeChildren(PLMWaiverType parent) {
        List<PLMWaiverType> childrens = getWaiverTypeChildren(parent.getId());
        parent.setChildrens(childrens);
        for (PLMWaiverType child : childrens) {
            visitWaiverTypeChildren(child);
        }
    }

    @Transactional(readOnly = true)
    public List<PLMWaiverType> getWaiverTypeChildren(Integer parent) {
        return waiverTypeRepository.findByParentTypeOrderByCreatedDateAsc(parent);
    }

    @Override
    public Boolean isSubtypeOf(String s1, Object s2, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        changeTypeRepository = webApplicationContext.getBean(ChangeTypeRepository.class);
        PLMChangeType changeType = (PLMChangeType) s2;
        if (subTypeId != null && checkWithId(changeType, subTypeId)) {
            return true;
        }
        if (changeType.getName().equalsIgnoreCase(s1)) {
            return true;
        } else {
            if (changeType.getParentType() != null)
                return compareWithParent(changeTypeRepository.findOne(changeType.getParentType()), s1);
        }
        return false;
    }

    public Boolean checkWithId(PLMChangeType plmChangeType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        changeTypeRepository = webApplicationContext.getBean(ChangeTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmChangeType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmChangeType.getParentType() != null)
                flag = checkWithId(changeTypeRepository.findOne(plmChangeType.getParentType()), typeId);
        }
        return flag;
    }

    private Boolean compareWithParent(PLMChangeType plmChangeType, String s1) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        changeTypeRepository = webApplicationContext.getBean(ChangeTypeRepository.class);
        Boolean flag = false;
        if (plmChangeType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (plmChangeType.getParentType() != null)
                flag = compareWithParent(changeTypeRepository.findOne(plmChangeType.getParentType()), s1);
        }
        return flag;
    }

    @Override
    public List<PLMChangeTypeAttribute> getTypeAttributes(Integer typeId, Boolean hierarchy) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        changeTypeAttributeRepository = webApplicationContext.getBean(ChangeTypeAttributeRepository.class);
        changeTypeRepository = webApplicationContext.getBean(ChangeTypeRepository.class);
        if (!hierarchy) {
            return changeTypeAttributeRepository.findByChangeTypeOrderBySeq(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }
}
