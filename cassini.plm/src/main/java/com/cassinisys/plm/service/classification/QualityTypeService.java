package com.cassinisys.plm.service.classification;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.GroupMember;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.common.GroupMemberRepository;
import com.cassinisys.platform.repo.common.PersonGroupRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.repo.security.GroupPermissionRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.cassinisys.platform.service.core.TypeSystem;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.filtering.InspectionPlanCriteria;
import com.cassinisys.plm.model.cm.PLMChangeType;
import com.cassinisys.plm.model.cm.PLMChangeTypeAttribute;
import com.cassinisys.plm.model.mes.MESObjectType;
import com.cassinisys.plm.model.mes.MESObjectTypeAttribute;
import com.cassinisys.plm.model.plm.PLMItemType;
import com.cassinisys.plm.model.plm.PLMItemTypeAttribute;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.QualityObjectDto;
import com.cassinisys.plm.model.pqm.dto.QualityTypeAttributeDto;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowType;
import com.cassinisys.plm.repo.cm.ChangeTypeAttributeRepository;
import com.cassinisys.plm.repo.cm.ChangeTypeRepository;
import com.cassinisys.plm.repo.mes.MESObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.mes.MESObjectTypeRepository;
import com.cassinisys.plm.repo.plm.ItemTypeAttributeRepository;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import com.cassinisys.plm.service.plm.ItemTypeService;
import com.cassinisys.plm.service.pqm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import java.text.MessageFormat;
import java.util.*;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Service
public class QualityTypeService implements CrudService<PQMQualityType, Integer>,
        TypeSystem, ClassificationTypeService<PQMQualityType, PQMQualityTypeAttribute> {
    ExpressionParser parser = new SpelExpressionParser();
    Logger logger = LoggerFactory.getLogger(QualityTypeService.class);
    @Autowired
    private QualityTypeRepository qualityTypeRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private InspectionPlanTypeRepository inspectionPlanTypeRepository;
    @Autowired
    private ProductInspectionPlanTypeRepository productInspectionPlanTypeRepository;
    @Autowired
    private MaterialInspectionPlanTypeRepository materialInspectionPlanTypeRepository;
    @Autowired
    private ProductInspectionPlanRepository productInspectionPlanRepository;
    @Autowired
    private MaterialInspectionPlanRepository materialInspectionPlanRepository;
    @Autowired
    private ProblemReportTypeRepository problemReportTypeRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private NCRTypeRepository ncrTypeRepository;
    @Autowired
    private NCRRepository ncrRepository;
    @Autowired
    private QCRTypeRepository qcrTypeRepository;
    @Autowired
    private QCRRepository qcrRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private InspectionPlanAttributeRepository inspectionPlanAttributeRepository;
    @Autowired
    private InspectionAttributeRepository inspectionAttributeRepository;
    @Autowired
    private InspectionPlanRevisionAttributeRepository inspectionPlanRevisionAttributeRepository;
    @Autowired
    private ProblemReportAttributeRepository problemReportAttributeRepository;
    @Autowired
    private NCRAttributeRepository ncrAttributeRepository;
    @Autowired
    private QCRAttributeRepository qcrAttributeRepository;
    @Autowired
    private PPAPAttributeRepository ppapAttributeRepository;
    @Autowired
    private PQMSupplierAuditAttributeRepository supplierAuditAttributeRepository;
    @Autowired
    private InspectionPlanService inspectionPlanService;
    @Autowired
    private InspectionService inspectionService;
    @Autowired
    private ProblemReportService problemReportService;
    @Autowired
    private NCRService ncrService;
    @Autowired
    private QCRService qcrService;
    @Autowired
    private PPAPService ppapService;
    @Autowired
    private SupplierAuditService supplierAuditService;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ChangeTypeAttributeRepository changeTypeAttributeRepository;
    @Autowired
    private ChangeTypeRepository changeTypeRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PersonGroupRepository personGroupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private GroupPermissionRepository groupPermissionRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private MESObjectTypeRepository mesObjectTypeRepository;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private PPAPTypeRepository ppapTypeRepository;
    @Autowired
    private PPAPRepository ppapRepository;
    @Autowired
    private SupplierAuditRepository supplierAuditRepository;
    @Autowired
    private SupplierAuditTypeRepository supplierAuditTypeRepository;
    @Autowired
    private MessageSource messageSource;


    private QualityTypeAttributeDto qualityTypeAttributeDto1 = null;
    private List<ObjectAttribute> objectAttributes1 = null;
    private List<ObjectAttribute> objectRevisionAttributes1 = new ArrayList();
    private List<ObjectTypeAttribute> objectTypeAttributes1 = null;
    private Map<Integer, ObjectTypeAttribute> stringStringHashMap = new HashMap<>();

    static List<ObjectAttribute> castList(List list) {
        List<ObjectAttribute> result1 = (List<ObjectAttribute>) list;
        return result1;
    }

    static List<ObjectTypeAttribute> castTypeList(List list) {
        List<ObjectTypeAttribute> result2 = (List<ObjectTypeAttribute>) list;
        return result2;
    }

    @PostConstruct
    public void InitQualityService() {
        objectTypeService.registerTypeSystem("qualityType", new QualityTypeService());
    }

    @Override
    @PreAuthorize("hasPermission(#pqmQualityType,'create')")
    public PQMQualityType create(PQMQualityType pqmQualityType) {
        PQMQualityType qualityType = qualityTypeRepository.save(pqmQualityType);
        return qualityType;
    }

    @Override
    @PreAuthorize("hasPermission(#pqmQualityType.id ,'edit')")
    public PQMQualityType update(PQMQualityType pqmQualityType) {
        PQMQualityType qualityType = qualityTypeRepository.save(pqmQualityType);
        return qualityType;
    }

    @Override
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        PQMQualityType qualityType = qualityTypeRepository.findOne(id);
        if (qualityType.getParentType() != null) {
            applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeDeletedEvent(PLMObjectType.QUALITY_TYPE, qualityType));
        }
        qualityTypeRepository.delete(id);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMQualityType get(Integer id) {
        return qualityTypeRepository.findOne(id);
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMQualityType> getAll() {
        return qualityTypeRepository.findAll();
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMQualityType> getRootTypes() {
        return qualityTypeRepository.findByParentTypeIsNullOrderByIdAsc();
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMQualityType> getChildren(Integer parent) {
        return qualityTypeRepository.findByParentTypeOrderByIdAsc(parent);
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMQualityType> getClassificationTree() {
        List<PQMQualityType> types = getRootTypes();
        for (PQMQualityType type : types) {
            if(type.getQualityType().equals(QualityType.PRODUCTINSPECTIONPLANTYPE)){
                type.setUsedType((productInspectionPlanRepository.getPlanCountByType(type.getId())) > 0);
            }
            visitChildren(type);
        }
        return types;
    }

    private void visitChildren(PQMQualityType parent) {
        List<PQMQualityType> children = getChildren(parent.getId());
        for (PQMQualityType child : children) {
            if(child.getQualityType().equals(QualityType.PRODUCTINSPECTIONPLANTYPE)){
                child.setUsedType((productInspectionPlanRepository.getPlanCountByType(child.getId())) > 0);
            }
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    @Override
    public PQMQualityTypeAttribute createAttribute(PQMQualityTypeAttribute attribute) {
        List<PQMQualityTypeAttribute> qualityTypeAttributes = qualityTypeAttributeRepository.findByTypeOrderBySeq(attribute.getType());
        if (qualityTypeAttributes.size() >= 0) {
            attribute.setSeq(qualityTypeAttributes.size() + 1);
        }
        attribute = qualityTypeAttributeRepository.save(attribute);
        PQMQualityType qualityType = qualityTypeRepository.findOne(attribute.getType());
        if (attribute.getDataType().toString().equals("FORMULA")) {
            createFormulaAttribute(attribute, qualityType);
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(PLMObjectType.QUALITY_TYPE, attribute));
        return attribute;
    }

    private void createFormulaAttribute(PQMQualityTypeAttribute attribute, PQMQualityType qualityType) {
        if (qualityType.getQualityType().name().equals("PRODUCTINSPECTIONPLANTYPE")) {
            PQMProductInspectionPlanType productInspectionPlanType = productInspectionPlanTypeRepository.findOne(qualityType.getId());
            List<PQMProductInspectionPlan> productInspectionPlans = productInspectionPlanRepository.findByPlanType(productInspectionPlanType);
            for (PQMProductInspectionPlan productInspectionPlan : productInspectionPlans) {
                PQMInspectionPlanAttribute inspectionPlanAttribute = new PQMInspectionPlanAttribute();
                inspectionPlanAttribute.setId(new ObjectAttributeId(productInspectionPlan.getId(), attribute.getId()));
                inspectionPlanAttributeRepository.save(inspectionPlanAttribute);
            }
        } else if (qualityType.getQualityType().name().equals("MATERIALINSPECTIONPLANTYPE")) {
            PQMMaterialInspectionPlanType materialInspectionPlanType = materialInspectionPlanTypeRepository.findOne(qualityType.getId());
            List<PQMMaterialInspectionPlan> materialInspectionPlans = materialInspectionPlanRepository.findByPlanType(materialInspectionPlanType);
            for (PQMMaterialInspectionPlan materialInspectionPlan : materialInspectionPlans) {
                PQMInspectionPlanAttribute inspectionPlanAttribute = new PQMInspectionPlanAttribute();
                inspectionPlanAttribute.setId(new ObjectAttributeId(materialInspectionPlan.getId(), attribute.getId()));
                inspectionPlanAttributeRepository.save(inspectionPlanAttribute);
            }
        } else if (qualityType.getQualityType().name().equals("PRTYPE")) {
            PQMProblemReportType prType = problemReportTypeRepository.findOne(qualityType.getId());
            List<PQMProblemReport> problemReports = problemReportRepository.findByPrType(prType);
            for (PQMProblemReport problemReport : problemReports) {
                PQMProblemReportAttribute objectAttribute = new PQMProblemReportAttribute();
                objectAttribute.setId(new ObjectAttributeId(problemReport.getId(), attribute.getId()));
                problemReportAttributeRepository.save(objectAttribute);
            }
        } else if (qualityType.getQualityType().name().equals("NCRTYPE")) {
            PQMNCRType pqmncrType = ncrTypeRepository.findOne(qualityType.getId());
            List<PQMNCR> ncrs = ncrRepository.findByNcrType(pqmncrType);
            for (PQMNCR ncr : ncrs) {
                PQMNCRAttribute objectAttribute = new PQMNCRAttribute();
                objectAttribute.setId(new ObjectAttributeId(ncr.getId(), attribute.getId()));
                ncrAttributeRepository.save(objectAttribute);
            }
        } else if (qualityType.getQualityType().name().equals("QCRTYPE")) {
            PQMQCRType pqmqcrType = qcrTypeRepository.findOne(qualityType.getId());
            List<PQMQCR> pqmqcrs = qcrRepository.findByQcrType(pqmqcrType);
            for (PQMQCR pqmqcr : pqmqcrs) {
                PQMQCRAttribute objectAttribute = new PQMQCRAttribute();
                objectAttribute.setId(new ObjectAttributeId(pqmqcr.getId(), attribute.getId()));
                qcrAttributeRepository.save(objectAttribute);
            }
        } else if (qualityType.getQualityType().name().equals("PPAPTYPE")) {
            PQMPPAPType pqmppapType = ppapTypeRepository.findOne(qualityType.getId());
            List<PQMPPAP> pqmppaps = ppapRepository.findByType(pqmppapType);
            for (PQMPPAP pqmppap : pqmppaps) {
                PQMPPAPAttribute objectAttribute = new PQMPPAPAttribute();
                objectAttribute.setId(new ObjectAttributeId(pqmppap.getId(), attribute.getId()));
                ppapAttributeRepository.save(objectAttribute);
            }
        } else if (qualityType.getQualityType().name().equals("SUPPLIERAUDITTYPE")) {
            PQMSupplierAuditType supplierAuditType = supplierAuditTypeRepository.findOne(qualityType.getId());
            List<PQMSupplierAudit> supplierAudits = supplierAuditRepository.findByType(supplierAuditType);
            for (PQMSupplierAudit auditType : supplierAudits) {
                PQMSupplierAuditAttribute objectAttribute = new PQMSupplierAuditAttribute();
                objectAttribute.setId(new ObjectAttributeId(auditType.getId(), attribute.getId()));
                supplierAuditAttributeRepository.save(objectAttribute);
            }
        }
    }

    @Override
    public PQMQualityTypeAttribute updateAttribute(PQMQualityTypeAttribute attribute) {
        PQMQualityTypeAttribute qualityTypeAttribute = qualityTypeAttributeRepository.findOne(attribute.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(PLMObjectType.QUALITY_TYPE, qualityTypeAttribute, attribute));
        return qualityTypeAttributeRepository.save(attribute);
    }

    @Override
    public void deleteAttribute(Integer id) {
        PQMQualityTypeAttribute attribute = qualityTypeAttributeRepository.findOne(id);
        List<PQMQualityTypeAttribute> qualityTypeAttributes = qualityTypeAttributeRepository.findByTypeOrderBySeq(attribute.getType());
        if (qualityTypeAttributes.size() > 0) {
            for (PQMQualityTypeAttribute pqmQualityTypeAttribute : qualityTypeAttributes) {
                if (pqmQualityTypeAttribute.getSeq() > attribute.getSeq()) {
                    pqmQualityTypeAttribute.setSeq(pqmQualityTypeAttribute.getSeq() - 1);
                    pqmQualityTypeAttribute = qualityTypeAttributeRepository.save(pqmQualityTypeAttribute);
                }
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(PLMObjectType.QUALITY_TYPE, attribute));
        qualityTypeAttributeRepository.delete(id);
    }

    @Override
    public PQMQualityTypeAttribute getAttribute(Integer id) {
        return qualityTypeAttributeRepository.findOne(id);
    }

    @Override
    public List<PQMQualityTypeAttribute> getAttributes(Integer typeId, Boolean hier) {
        List<PQMQualityTypeAttribute> qualityTypeAttributes = new ArrayList<>();
        if (!hier) {
            qualityTypeAttributes = qualityTypeAttributeRepository.findByTypeOrderBySeq(typeId);
            qualityTypeAttributes.forEach(qualityTypeAttribute -> {
                if (qualityTypeAttribute.getRefSubType() != null) {
                    qualityTypeAttribute.setRefSubTypeName(itemTypeService.getRefSubType(qualityTypeAttribute.getRefType().name(), qualityTypeAttribute.getRefSubType()));
                }
            });
        } else {
            qualityTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return qualityTypeAttributes;
    }

    public List<PQMQualityTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<PQMQualityTypeAttribute> collector = new ArrayList<>();
        List<PQMQualityTypeAttribute> atts = qualityTypeAttributeRepository.findByTypeOrderBySeq(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    public List<PLMChangeTypeAttribute> getChangeAttributesFromHierarchy(Integer typeId) {
        List<PLMChangeTypeAttribute> collector = new ArrayList<>();
        List<PLMChangeTypeAttribute> atts = changeTypeAttributeRepository.findByChangeTypeOrderBySeq(typeId);
        collector.addAll(atts);
        collectChangeAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    public List<PLMItemTypeAttribute> getItemTypeAttributesFromHierarchy(Integer typeId, Boolean revision) {
        List<PLMItemTypeAttribute> collector = new ArrayList<>();
        List<PLMItemTypeAttribute> atts = new ArrayList<>();
        atts = itemTypeAttributeRepository.findByItemTypeOrderBySeq(typeId);
        collector.addAll(atts);
        collectItemTypeAttributesFromHierarchy(collector, typeId, revision);
        return collector;
    }

    public List<PQMQualityTypeAttribute> getInspectionPlanAttributesFromHierarchy(Integer typeId, Boolean revision) {
        List<PQMQualityTypeAttribute> collector = new ArrayList<>();
        List<PQMQualityTypeAttribute> atts = new ArrayList<>();
        atts = qualityTypeAttributeRepository.findByTypeOrderBySeq(typeId);
        collector.addAll(atts);
        collectInspectionPlanAttributesFromHierarchy(collector, typeId, revision);
        return collector;
    }

    /*-------------------------  Inspection Types ----------------------*/

    private void collectAttributesFromHierarchy(List<PQMQualityTypeAttribute> collector, Integer typeId) {
        PQMQualityType itemType = qualityTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<PQMQualityTypeAttribute> atts = qualityTypeAttributeRepository.findByTypeOrderBySeq(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    private void collectChangeAttributesFromHierarchy(List<PLMChangeTypeAttribute> collector, Integer typeId) {
        PLMChangeType itemType = changeTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<PLMChangeTypeAttribute> atts = changeTypeAttributeRepository.findByChangeTypeOrderBySeq(parentType);
                collector.addAll(atts);
                collectChangeAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    private void collectItemTypeAttributesFromHierarchy(List<PLMItemTypeAttribute> collector, Integer typeId, Boolean revision) {
        PLMItemType itemType = itemTypeRepository.findOne(typeId);
        if (itemType != null) {
            List<PLMItemTypeAttribute> atts = new ArrayList<>();
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                atts = itemTypeAttributeRepository.findByItemTypeOrderBySeq(parentType);
                collector.addAll(atts);
                collectItemTypeAttributesFromHierarchy(collector, parentType, revision);
            }
        }
    }

    private void collectInspectionPlanAttributesFromHierarchy(List<PQMQualityTypeAttribute> collector, Integer typeId, Boolean revision) {
        PQMQualityType itemType = qualityTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<PQMQualityTypeAttribute> atts = new ArrayList<>();
                atts = qualityTypeAttributeRepository.findByTypeOrderBySeq(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Transactional(readOnly = true)
    public Object getQualityTypeByIdAndType(Integer id, PLMObjectType objectType) {
        Object object = null;
        if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLANTYPE)) {
            object = productInspectionPlanTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.MATERIALINSPECTIONPLANTYPE)) {
            object = materialInspectionPlanTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.PRTYPE)) {
            object = problemReportTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.NCRTYPE)) {
            object = ncrTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.QCRTYPE)) {
            object = qcrTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.PPAPTYPE)) {
            object = ppapTypeRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDITTYPE)) {
            object = supplierAuditTypeRepository.findOne(id);
        }
        return object;
    }

    @Transactional(readOnly = true)
    public Integer getObjectsByType(Integer id, PLMObjectType objectType) {
        Integer count = 0;
        if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLANTYPE)) {
            PQMProductInspectionPlanType productInspectionPlanType = productInspectionPlanTypeRepository.findOne(id);
            count = productInspectionPlanRepository.findByPlanType(productInspectionPlanType).size();
        } else if (objectType.equals(PLMObjectType.MATERIALINSPECTIONPLANTYPE)) {
            PQMMaterialInspectionPlanType materialInspectionPlanType = materialInspectionPlanTypeRepository.findOne(id);
            count = materialInspectionPlanRepository.findByPlanType(materialInspectionPlanType).size();
        } else if (objectType.equals(PLMObjectType.PRTYPE)) {
            PQMProblemReportType problemReportType = problemReportTypeRepository.findOne(id);
            count = problemReportRepository.findByPrType(problemReportType).size();
        } else if (objectType.equals(PLMObjectType.NCRTYPE)) {
            PQMNCRType pqmncrType = ncrTypeRepository.findOne(id);
            count = ncrRepository.findByNcrType(pqmncrType).size();
        } else if (objectType.equals(PLMObjectType.QCRTYPE)) {
            PQMQCRType pqmqcrType = qcrTypeRepository.findOne(id);
            count = qcrRepository.findByQcrType(pqmqcrType).size();
        } else if (objectType.equals(PLMObjectType.PPAPTYPE)) {
            PQMPPAPType pqmppapType = ppapTypeRepository.findOne(id);
            count = ppapRepository.findByType(pqmppapType).size();
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDITTYPE)) {
            PQMSupplierAuditType supplierAuditType = supplierAuditTypeRepository.findOne(id);
            count = supplierAuditRepository.findByType(supplierAuditType).size();
        }
        return count;
    }

    @Transactional
    @PreAuthorize("hasPermission(#inspectionPlanType,'create')")
    public PQMInspectionPlanType createInspectionPlanType(PQMInspectionPlanType inspectionPlanType) {
        PQMInspectionPlanType inspectionPlanType1 = inspectionPlanTypeRepository.save(inspectionPlanType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.QUALITY_TYPE, inspectionPlanType1));
        return inspectionPlanType1;
    }

    @Transactional
    @PreAuthorize("hasPermission(#inspectionPlanType,'create')")
    public PQMProductInspectionPlanType createProductInspectionPlanType(PQMProductInspectionPlanType inspectionPlanType) {
        PQMProductInspectionPlanType inspectionPlanType1 = inspectionPlanTypeRepository.save(inspectionPlanType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.QUALITY_TYPE, inspectionPlanType1));
        return inspectionPlanType1;
    }

    @Transactional
    @PreAuthorize("hasPermission(#inspectionPlanType,'new')")
    public PQMMaterialInspectionPlanType createMaterialInspectionPlanType(PQMMaterialInspectionPlanType inspectionPlanType) {
        PQMMaterialInspectionPlanType inspectionPlanType1 = inspectionPlanTypeRepository.save(inspectionPlanType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.QUALITY_TYPE, inspectionPlanType1));
        return inspectionPlanType1;
    }

    @Transactional
    @PreAuthorize("hasPermission(#inspectionPlanType.id ,'edit')")
    public PQMInspectionPlanType updateInspectionPlanType(Integer id, PQMInspectionPlanType inspectionPlanType) {
        PQMInspectionPlanType oldQualityType = inspectionPlanTypeRepository.findOne(inspectionPlanType.getId());
        PQMInspectionPlanType existingQualityType;
        if (inspectionPlanType.getParentType() == null) {
            existingQualityType = inspectionPlanTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(inspectionPlanType.getName());
        } else {
            existingQualityType = inspectionPlanTypeRepository.findByNameEqualsIgnoreCaseAndParentType(inspectionPlanType.getName(), inspectionPlanType.getParentType());
        }
        if ( existingQualityType != null && !inspectionPlanType.getId().equals(existingQualityType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingQualityType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.QUALITY_TYPE, oldQualityType, inspectionPlanType));
        return inspectionPlanTypeRepository.save(inspectionPlanType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#productInspectionPlanType.id ,'edit')")
    public PQMProductInspectionPlanType updateProductInspectionPlanType(PQMProductInspectionPlanType productInspectionPlanType) {
        PQMProductInspectionPlanType oldQualityType = productInspectionPlanTypeRepository.findOne(productInspectionPlanType.getId());
        PQMProductInspectionPlanType existingQualityType;
        if (productInspectionPlanType.getParentType() == null) {
            existingQualityType = productInspectionPlanTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(productInspectionPlanType.getName());
        } else {
            existingQualityType = productInspectionPlanTypeRepository.findByNameEqualsIgnoreCaseAndParentType(productInspectionPlanType.getName(), productInspectionPlanType.getParentType());
        }
        if ( existingQualityType != null && !productInspectionPlanType.getId().equals(existingQualityType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingQualityType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.QUALITY_TYPE, oldQualityType, productInspectionPlanType));
        return productInspectionPlanTypeRepository.save(productInspectionPlanType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#materialInspectionPlanType.id ,'edit')")
    public PQMMaterialInspectionPlanType updateMaterialInspectionPlanType(PQMMaterialInspectionPlanType materialInspectionPlanType) {
        PQMMaterialInspectionPlanType oldQualityType = materialInspectionPlanTypeRepository.findOne(materialInspectionPlanType.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.QUALITY_TYPE, oldQualityType, materialInspectionPlanType));
        return materialInspectionPlanTypeRepository.save(materialInspectionPlanType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteInspectionPlanType(Integer id) {
        inspectionPlanTypeRepository.delete(id);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteProductInspectionPlanType(Integer id) {
        productInspectionPlanTypeRepository.delete(id);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteMaterialInspectionPlanType(Integer id) {
        materialInspectionPlanTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMInspectionPlanType getInspectionPlanType(Integer id) {
        return inspectionPlanTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMProductInspectionPlanType getProductInspectionPlanType(Integer id) {
        return productInspectionPlanTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMMaterialInspectionPlanType getMaterialInspectionPlanType(Integer id) {
        return materialInspectionPlanTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMInspectionPlanType> getAllInspectionPlanTypes() {
        return inspectionPlanTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMInspectionPlanType> findMultiple(List<Integer> ids) {
        return inspectionPlanTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<PQMProductInspectionPlanType> findMultipleProductTypes(List<Integer> ids) {
        return productInspectionPlanTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMMaterialInspectionPlanType> getMultipleMaterialTypes(List<Integer> ids) {
        return materialInspectionPlanTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<PQMQualityType> getMultipleQualityTypes(List<Integer> ids) {
        return qualityTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMInspectionPlanType> getInspectionPlanTypeTree() {
        List<PQMInspectionPlanType> types = inspectionPlanTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PQMInspectionPlanType type : types) {
            visitPlanTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMProductInspectionPlanType> getProductInspectionPlanTypeTree() {
        List<PQMProductInspectionPlanType> types = productInspectionPlanTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PQMProductInspectionPlanType type : types) {
            visitProductPlanTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMMaterialInspectionPlanType> getMaterialInspectionPlanTypeTree() {
        List<PQMMaterialInspectionPlanType> types = materialInspectionPlanTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PQMMaterialInspectionPlanType type : types) {
            visitMaterialPlanTypeChildren(type);
        }
        return types;
    }
    /*-------------------------  Problem Report Types ----------------------*/

    private void visitPlanTypeChildren(PQMInspectionPlanType parent) {
        List<PQMInspectionPlanType> childrens = getPlanTypeChildren(parent.getId());
        for (PQMInspectionPlanType child : childrens) {
            visitPlanTypeChildren(child);
        }
//        parent.setChildrens(childrens);
    }

    private void visitProductPlanTypeChildren(PQMProductInspectionPlanType parent) {
        List<PQMProductInspectionPlanType> childrens = getProductPlanTypeChildren(parent.getId());
        for (PQMProductInspectionPlanType child : childrens) {
            visitProductPlanTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    private void visitMaterialPlanTypeChildren(PQMMaterialInspectionPlanType parent) {
        List<PQMMaterialInspectionPlanType> childrens = getMaterialPlanTypeChildren(parent.getId());
        for (PQMMaterialInspectionPlanType child : childrens) {
            visitMaterialPlanTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    @Transactional(readOnly = true)
    public List<PQMInspectionPlanType> getPlanTypeChildren(Integer parent) {
        return inspectionPlanTypeRepository.findByParentTypeOrderByCreatedDateAsc(parent);
    }

    @Transactional(readOnly = true)
    public List<PQMProductInspectionPlanType> getProductPlanTypeChildren(Integer parent) {
        return productInspectionPlanTypeRepository.findByParentTypeOrderByCreatedDateAsc(parent);
    }

    @Transactional(readOnly = true)
    public List<PQMMaterialInspectionPlanType> getMaterialPlanTypeChildren(Integer parent) {
        return materialInspectionPlanTypeRepository.findByParentTypeOrderByCreatedDateAsc(parent);
    }

    @Transactional
    @PreAuthorize("hasPermission(#problemReportType,'create')")
    public PQMProblemReportType createPrType(PQMProblemReportType problemReportType) {
        PQMProblemReportType pqmProblemReportType = problemReportTypeRepository.save(problemReportType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.QUALITY_TYPE, pqmProblemReportType));
        return pqmProblemReportType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#problemReportType.id ,'edit')")
    public PQMProblemReportType updatePrType(Integer id, PQMProblemReportType problemReportType) {
        PQMProblemReportType oldQualityType = problemReportTypeRepository.findOne(problemReportType.getId());
        PQMProblemReportType existingQualityType;
        if (problemReportType.getParentType() == null) {
            existingQualityType = problemReportTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(problemReportType.getName());
        } else {
            existingQualityType = problemReportTypeRepository.findByNameEqualsIgnoreCaseAndParentType( problemReportType.getName(), problemReportType.getParentType());
        }
        if ( existingQualityType != null && !problemReportType.getId().equals(existingQualityType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingQualityType.getName());
            throw new CassiniException(result);  
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.QUALITY_TYPE, oldQualityType, problemReportType));
        return problemReportTypeRepository.save(problemReportType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deletePrType(Integer id) {
        problemReportTypeRepository.delete(id);
    }


    /*-------------------------  NCR Types ----------------------*/

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMProblemReportType getPrType(Integer id) {
        return problemReportTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMProblemReportType> getAllPrTypes() {
        return problemReportTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMProblemReportType> findMultiplePrTypes(List<Integer> ids) {
        return problemReportTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMProblemReportType> getPrTypeTree() {
        List<PQMProblemReportType> types = problemReportTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PQMProblemReportType type : types) {
            visitPrTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PQMProblemReportType> getPrTypeChildren(Integer id) {
        return problemReportTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitPrTypeChildren(PQMProblemReportType parent) {
        List<PQMProblemReportType> childrens = getPrTypeChildren(parent.getId());
        for (PQMProblemReportType child : childrens) {
            visitPrTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    @Transactional
    @PreAuthorize("hasPermission(#ncrType,'create')")
    public PQMNCRType createNcrType(PQMNCRType ncrType) {
        PQMNCRType pqmncrType = ncrTypeRepository.save(ncrType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.QUALITY_TYPE, pqmncrType));
        return pqmncrType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#ncrType.id ,'edit')")
    public PQMNCRType updateNcrType(Integer id, PQMNCRType ncrType) {
        PQMNCRType oldQualityType = ncrTypeRepository.findOne(ncrType.getId());
        PQMNCRType existingQualityType;
        if (ncrType.getParentType() == null) {
            existingQualityType = ncrTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(ncrType.getName());
        } else {
            existingQualityType = ncrTypeRepository.findByNameEqualsIgnoreCaseAndParentType(ncrType.getName(), ncrType.getParentType());
        }
        if ( existingQualityType != null && !ncrType.getId().equals(existingQualityType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingQualityType.getName());
            throw new CassiniException(result);
            
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.QUALITY_TYPE, oldQualityType, ncrType));
        return ncrTypeRepository.save(ncrType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteNcrType(Integer id) {
        ncrTypeRepository.delete(id);
    }

    /*-------------------------  QCR Types ----------------------*/

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMNCRType getNcrType(Integer id) {
        return ncrTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMNCRType> getAllNcrTypes() {
        return ncrTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMNCRType> findMultipleNcrTypes(List<Integer> ids) {
        return ncrTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMNCRType> getNcrTypeTree() {
        List<PQMNCRType> types = ncrTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PQMNCRType type : types) {
            visitNcrTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PQMNCRType> getNcrTypeChildren(Integer id) {
        return ncrTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitNcrTypeChildren(PQMNCRType parent) {
        List<PQMNCRType> childrens = getNcrTypeChildren(parent.getId());
        for (PQMNCRType child : childrens) {
            visitNcrTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    @Transactional
    @PreAuthorize("hasPermission(#qcrType,'create')")
    public PQMQCRType createQcrType(PQMQCRType qcrType) {
        PQMQCRType pqmqcrType = qcrTypeRepository.save(qcrType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.QUALITY_TYPE, pqmqcrType));
        return pqmqcrType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#qcrType.id ,'edit')")
    public PQMQCRType updateQcrType(Integer id, PQMQCRType qcrType) {
        PQMQCRType oldQualityType = qcrTypeRepository.findOne(qcrType.getId());
        PQMQCRType existingQualityType;
        if (qcrType.getParentType() == null) {
            existingQualityType = qcrTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(qcrType.getName());
        } else {
            existingQualityType = qcrTypeRepository.findByNameEqualsIgnoreCaseAndParentType(qcrType.getName(), qcrType.getParentType());
        }
        if ( existingQualityType != null && !qcrType.getId().equals(existingQualityType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingQualityType.getName());
            throw new CassiniException(result);
            
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.QUALITY_TYPE, oldQualityType, qcrType));
        return qcrTypeRepository.save(qcrType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteQcrType(Integer id) {
        qcrTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMQCRType getQcrType(Integer id) {
        return qcrTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMQCRType> getAllQcrTypes() {
        return qcrTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMQCRType> findMultipleQcrTypes(List<Integer> ids) {
        return qcrTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMQCRType> getQcrTypeTree() {
        List<PQMQCRType> types = qcrTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PQMQCRType type : types) {
            visitQcrTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PQMQCRType> getQcrTypeChildren(Integer id) {
        return qcrTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitQcrTypeChildren(PQMQCRType parent) {
        List<PQMQCRType> childrens = getQcrTypeChildren(parent.getId());
        for (PQMQCRType child : childrens) {
            visitQcrTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }

    @Transactional(readOnly = true)
    public List<ObjectAttribute> getUsedQualityTypeAttributes(Integer attributeId) {
        return objectAttributeRepository.findByAttributeDef(attributeId);
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filter(authentication,#objectId, filterObject,'view')")
    public List<ObjectTypeAttribute> filterObjectTypeAttributes(Integer objectId, List<ObjectTypeAttribute> objectTypeAttributes) {
        return objectTypeAttributes;
    }

    @Transactional
    public QualityTypeAttributeDto createQualityObject(PLMObjectType objectType, QualityTypeAttributeDto qualityTypeAttributeDto) {
        if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLAN) || objectType.equals(PLMObjectType.MATERIALINSPECTIONPLAN)) {
            PQMProductInspectionPlan productInspectionPlan = null;
            PQMMaterialInspectionPlan materialInspectionPlan = null;
            Integer revisionId = null;
            Integer planId = null;
            if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLAN)) {
                productInspectionPlan = inspectionPlanService.createProductInspectionPlan(qualityTypeAttributeDto.getProductInspectionPlan());
                revisionId = productInspectionPlan.getLatestRevision();
                planId = productInspectionPlan.getId();
                qualityTypeAttributeDto.setProductInspectionPlan(productInspectionPlan);
            } else {
                materialInspectionPlan = inspectionPlanService.createMaterialInspectionPlan(qualityTypeAttributeDto.getMaterialInspectionPlan());
                revisionId = materialInspectionPlan.getLatestRevision();
                planId = materialInspectionPlan.getId();
                qualityTypeAttributeDto.setMaterialInspectionPlan(materialInspectionPlan);
            }
            List<PQMInspectionPlanAttribute> inspectionPlanAttributes = qualityTypeAttributeDto.getInspectionPlanAttributes();
            List<ObjectAttribute> objectAttributes = qualityTypeAttributeDto.getObjectAttributes();
            List<ObjectAttribute> revisionObjectAttributes = qualityTypeAttributeDto.getRevisionObjectAttributes();
            for (PQMInspectionPlanAttribute attribute : inspectionPlanAttributes) {
                PQMQualityTypeAttribute qualityTypeAttribute = qualityTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (qualityTypeAttribute.getRevisionSpecific()) {
                    if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                            attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                            attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                            attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                        PQMInspectionPlanRevisionAttribute revisionAttribute1 = new PQMInspectionPlanRevisionAttribute();
                        revisionAttribute1.setId(new ObjectAttributeId(revisionId, attribute.getId().getAttributeDef()));
                        revisionAttribute1.setStringValue(attribute.getStringValue());
                        revisionAttribute1.setLongTextValue(attribute.getLongTextValue());
                        revisionAttribute1.setRichTextValue(attribute.getRichTextValue());
                        revisionAttribute1.setIntegerValue(attribute.getIntegerValue());
                        revisionAttribute1.setBooleanValue(attribute.getBooleanValue());
                        revisionAttribute1.setDoubleValue(attribute.getDoubleValue());
                        revisionAttribute1.setDateValue(attribute.getDateValue());
                        revisionAttribute1.setTimeValue(attribute.getTimeValue());
                        revisionAttribute1.setAttachmentValues(attribute.getAttachmentValues());
                        revisionAttribute1.setRefValue(attribute.getRefValue());
                        revisionAttribute1.setCurrencyType(attribute.getCurrencyType());
                        revisionAttribute1.setCurrencyValue(attribute.getCurrencyValue());
                        revisionAttribute1.setTimestampValue(attribute.getTimestampValue());
                        revisionAttribute1.setHyperLinkValue(attribute.getHyperLinkValue());
                        revisionAttribute1.setListValue(attribute.getListValue());
                        revisionAttribute1.setMListValue(attribute.getMListValue());
                        inspectionPlanRevisionAttributeRepository.save(revisionAttribute1);
                    }
                } else {
                    if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                            attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                            attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                            attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                        attribute.setId(new ObjectAttributeId(planId, attribute.getId().getAttributeDef()));
                        inspectionPlanAttributeRepository.save(attribute);
                    }
                }
            }

            for (ObjectAttribute attribute : objectAttributes) {
                ObjectTypeAttribute qualityTypeAttribute = objectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(planId, attribute.getId().getAttributeDef()));
                    objectAttributeRepository.save(attribute);
                }
            }
            for (ObjectAttribute attribute : revisionObjectAttributes) {
                ObjectTypeAttribute qualityTypeAttribute = objectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(revisionId, attribute.getId().getAttributeDef()));
                    objectAttributeRepository.save(attribute);
                }
            }
        } else if (objectType.equals(PLMObjectType.ITEMINSPECTION) || objectType.equals(PLMObjectType.MATERIALINSPECTION)) {
            Integer inspectionId = null;
            if (objectType.equals(PLMObjectType.ITEMINSPECTION)) {
                PQMItemInspection inspection = inspectionService.createItemInspection(qualityTypeAttributeDto.getItemInspection());
                inspectionId = inspection.getId();
                qualityTypeAttributeDto.setItemInspection(inspection);
            } else {
                PQMMaterialInspection inspection = inspectionService.createMaterialInspection(qualityTypeAttributeDto.getMaterialInspection());
                inspectionId = inspection.getId();
                qualityTypeAttributeDto.setMaterialInspection(inspection);
            }
            List<PQMInspectionAttribute> inspectionAttributes = qualityTypeAttributeDto.getInspectionAttributes();
            for (PQMInspectionAttribute attribute : inspectionAttributes) {
                PQMQualityTypeAttribute qualityTypeAttribute = qualityTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(inspectionId, attribute.getId().getAttributeDef()));
                    inspectionAttributeRepository.save(attribute);
                }
            }
            List<ObjectAttribute> objectAttributes = qualityTypeAttributeDto.getObjectAttributes();
            for (ObjectAttribute attribute : objectAttributes) {
                ObjectTypeAttribute qualityTypeAttribute = objectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(inspectionId, attribute.getId().getAttributeDef()));
                    objectAttributeRepository.save(attribute);
                }
            }
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            PQMProblemReport problemReport = problemReportService.create(qualityTypeAttributeDto.getProblemReport());
            List<PQMProblemReportAttribute> problemReportAttributes = qualityTypeAttributeDto.getProblemReportAttributes();
            for (PQMProblemReportAttribute attribute : problemReportAttributes) {
                PQMQualityTypeAttribute qualityTypeAttribute = qualityTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(problemReport.getId(), attribute.getId().getAttributeDef()));
                    problemReportAttributeRepository.save(attribute);
                }
            }
            List<ObjectAttribute> objectAttributes = qualityTypeAttributeDto.getObjectAttributes();
            for (ObjectAttribute attribute : objectAttributes) {
                ObjectTypeAttribute qualityTypeAttribute = objectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(problemReport.getId(), attribute.getId().getAttributeDef()));
                    objectAttributeRepository.save(attribute);
                }
            }
            qualityTypeAttributeDto.setProblemReport(problemReport);
        } else if (objectType.equals(PLMObjectType.NCR)) {
            PQMNCR pqmncr = ncrService.create(qualityTypeAttributeDto.getNcr());
            List<PQMNCRAttribute> ncrAttributes = qualityTypeAttributeDto.getNcrAttributes();
            for (PQMNCRAttribute attribute : ncrAttributes) {
                PQMQualityTypeAttribute qualityTypeAttribute = qualityTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(pqmncr.getId(), attribute.getId().getAttributeDef()));
                    ncrAttributeRepository.save(attribute);
                }
            }
            List<ObjectAttribute> objectAttributes = qualityTypeAttributeDto.getObjectAttributes();
            for (ObjectAttribute attribute : objectAttributes) {
                ObjectTypeAttribute qualityTypeAttribute = objectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(pqmncr.getId(), attribute.getId().getAttributeDef()));
                    objectAttributeRepository.save(attribute);
                }
            }
            qualityTypeAttributeDto.setNcr(pqmncr);
        } else if (objectType.equals(PLMObjectType.QCR)) {
            PQMQCR pqmqcr = qcrService.create(qualityTypeAttributeDto.getQcr());
            List<PQMQCRAttribute> qcrAttributes = qualityTypeAttributeDto.getQcrAttributes();
            for (PQMQCRAttribute attribute : qcrAttributes) {
                PQMQualityTypeAttribute qualityTypeAttribute = qualityTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(pqmqcr.getId(), attribute.getId().getAttributeDef()));
                    qcrAttributeRepository.save(attribute);
                }
            }
            List<ObjectAttribute> objectAttributes = qualityTypeAttributeDto.getObjectAttributes();
            for (ObjectAttribute attribute : objectAttributes) {
                ObjectTypeAttribute qualityTypeAttribute = objectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(pqmqcr.getId(), attribute.getId().getAttributeDef()));
                    objectAttributeRepository.save(attribute);
                }
            }
            qualityTypeAttributeDto.setQcr(pqmqcr);
        } else if (objectType.equals(PLMObjectType.PPAP)) {
            PQMPPAP pqmppap = ppapService.create(qualityTypeAttributeDto.getPpap());
            List<PQMPPAPAttribute> ppapAttributes = qualityTypeAttributeDto.getPpapAttributes();
            for (PQMPPAPAttribute attribute : ppapAttributes) {
                PQMQualityTypeAttribute qualityTypeAttribute = qualityTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(pqmppap.getId(), attribute.getId().getAttributeDef()));
                    ppapAttributeRepository.save(attribute);
                }
            }
            List<ObjectAttribute> objectAttributes = qualityTypeAttributeDto.getObjectAttributes();
            for (ObjectAttribute attribute : objectAttributes) {
                ObjectTypeAttribute qualityTypeAttribute = objectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(pqmppap.getId(), attribute.getId().getAttributeDef()));
                    objectAttributeRepository.save(attribute);
                }
            }
            qualityTypeAttributeDto.setPpap(pqmppap);
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            PQMSupplierAudit supplierAudit = supplierAuditService.create(qualityTypeAttributeDto.getSupplierAudit());
            List<PQMSupplierAuditAttribute> supplierAuditAttributes = qualityTypeAttributeDto.getSupplierAuditAttributes();
            for (PQMSupplierAuditAttribute attribute : supplierAuditAttributes) {
                PQMQualityTypeAttribute qualityTypeAttribute = qualityTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(supplierAudit.getId(), attribute.getId().getAttributeDef()));
                    supplierAuditAttributeRepository.save(attribute);
                }
            }
            List<ObjectAttribute> objectAttributes = qualityTypeAttributeDto.getObjectAttributes();
            for (ObjectAttribute attribute : objectAttributes) {
                ObjectTypeAttribute qualityTypeAttribute = objectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null || qualityTypeAttribute.getDataType().toString().equals("FORMULA") ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(supplierAudit.getId(), attribute.getId().getAttributeDef()));
                    objectAttributeRepository.save(attribute);
                }
            }
            qualityTypeAttributeDto.setSupplierAudit(supplierAudit);
        }

        return qualityTypeAttributeDto;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getHierarchyWorkflows(Integer typeId, String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        PQMQualityType changeType = qualityTypeRepository.findOne(typeId);
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
        List<PLMWorkflowDefinition> workflowDefinition1 = workflowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
        if (workflowDefinition1.size() > 0) {
            workflowDefinition1.forEach(workflowDefinition -> {
                if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                    workflowDefinitions.add(workflowDefinition);
                }
            });
        }
        if (changeType.getParentType() != null) {
            getWorkflowsFromHierarchy(workflowDefinitions, changeType.getParentType(), type);
        }
        return workflowDefinitions;
    }

    private void getWorkflowsFromHierarchy(List<PLMWorkflowDefinition> definitions, Integer typeId, String type) {
        PQMQualityType changeType = qualityTypeRepository.findOne(typeId);
        if (changeType != null) {
            PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
            if (workflowType != null) {
                List<PLMWorkflowDefinition> workflowDefinition2 = workflowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
                if (workflowDefinition2.size() > 0) {
                    workflowDefinition2.forEach(workflowDefinition -> {
                        if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                            definitions.add(workflowDefinition);
                        }
                    });
                }
            }
            if (changeType.getParentType() != null) {
                getWorkflowsFromHierarchy(definitions, changeType.getParentType(), type);
            }
        }
    }

    @Transactional
    public PQMQualityTypeAttribute changeQualityAttributeSeq(Integer targetId, Integer actualId) {
        PQMQualityTypeAttribute qualityTypeAttribute = qualityTypeAttributeRepository.findOne(actualId);
        PQMQualityTypeAttribute qualityTypeAttribute1 = qualityTypeAttributeRepository.findOne(targetId);
        List<PQMQualityTypeAttribute> pqmQualityTypeAttributes = qualityTypeAttributeRepository.findByTypeOrderBySeq(qualityTypeAttribute.getType());
        if ((qualityTypeAttribute.getSeq() > qualityTypeAttribute1.getSeq())) {
            for (PQMQualityTypeAttribute qualityTypeAttribute2 : pqmQualityTypeAttributes) {
                if (qualityTypeAttribute1.getId().equals(qualityTypeAttribute2.getId()) || qualityTypeAttribute.getId().equals(qualityTypeAttribute2.getId())) {
                } else {
                    if ((qualityTypeAttribute1.getSeq() < qualityTypeAttribute2.getSeq()) && (qualityTypeAttribute.getSeq() > qualityTypeAttribute2.getSeq())) {
                        qualityTypeAttribute2.setSeq(qualityTypeAttribute2.getSeq() + 1);
                        qualityTypeAttribute2 = qualityTypeAttributeRepository.save(qualityTypeAttribute2);
                    }
                }
            }
            if (qualityTypeAttribute != null) {
                qualityTypeAttribute.setSeq(qualityTypeAttribute1.getSeq());
                qualityTypeAttribute = qualityTypeAttributeRepository.save(qualityTypeAttribute);

            }
            if (qualityTypeAttribute1 != null) {
                qualityTypeAttribute1.setSeq(qualityTypeAttribute1.getSeq() + 1);
                qualityTypeAttribute1 = qualityTypeAttributeRepository.save(qualityTypeAttribute1);

            }
        } else {
            for (PQMQualityTypeAttribute qualityTypeAttribute2 : pqmQualityTypeAttributes) {
                if (qualityTypeAttribute1.getId().equals(qualityTypeAttribute2.getId()) || qualityTypeAttribute.getId().equals(qualityTypeAttribute2.getId())) {
                } else {
                    if ((qualityTypeAttribute1.getSeq() > qualityTypeAttribute2.getSeq()) && (qualityTypeAttribute.getSeq() < qualityTypeAttribute2.getSeq())) {
                        qualityTypeAttribute2.setSeq(qualityTypeAttribute2.getSeq() - 1);
                        qualityTypeAttribute2 = qualityTypeAttributeRepository.save(qualityTypeAttribute2);
                    }
                }
            }
            if (qualityTypeAttribute != null) {
                qualityTypeAttribute.setSeq(qualityTypeAttribute1.getSeq());
                qualityTypeAttribute = qualityTypeAttributeRepository.save(qualityTypeAttribute);

            }
            if (qualityTypeAttribute1 != null) {
                qualityTypeAttribute1.setSeq(qualityTypeAttribute1.getSeq() - 1);
                qualityTypeAttribute1 = qualityTypeAttributeRepository.save(qualityTypeAttribute1);

            }
        }
        return null;

    }

    @Transactional(readOnly = true)
    public QualityTypeAttributeDto getQualityTypeSelectionAttributes(PLMObjectType objectType, String type) {
        QualityTypeAttributeDto qualityTypeAttributeDto = new QualityTypeAttributeDto();
        if (objectType.equals(PLMObjectType.QUALITY)) {
            List<PQMQualityTypeAttribute> qualityTypeAttributes = qualityTypeAttributeRepository.findByObjectType(ObjectType.valueOf(PLMObjectType.QUALITY_TYPE.toString()));
            if (!type.equals(PLMObjectType.ITEMINSPECTION.toString()) && !type.equals(PLMObjectType.MATERIALINSPECTION.toString())) {
                qualityTypeAttributes.forEach(pqmQualityTypeAttribute -> {
                    PQMQualityType qualityType = qualityTypeRepository.findOne(pqmQualityTypeAttribute.getType());
                    if (qualityType.getQualityType().equals(QualityType.valueOf(type))) {
                        qualityTypeAttributeDto.getQualityTypeAttributes().add(pqmQualityTypeAttribute);
                    }
                });
            }
        } else if (objectType.equals(PLMObjectType.CHANGE)) {
            List<PLMChangeTypeAttribute> changeTypeAttributes = changeTypeAttributeRepository.findByObjectType(ObjectType.valueOf(PLMObjectType.CHANGETYPE.toString()));
            changeTypeAttributes.forEach(changeTypeAttribute -> {
                PLMChangeType changeType = changeTypeRepository.findOne(changeTypeAttribute.getChangeType());
                if (changeType.getObjectType().equals(ObjectType.valueOf(type))) {
                    qualityTypeAttributeDto.getChangeTypeAttributes().add(changeTypeAttribute);
                }
            });
        } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
            List<MESObjectTypeAttribute> mesObjectTypeAttributes = mesObjectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(PLMObjectType.MESOBJECTTYPE.toString()));
            mesObjectTypeAttributes.forEach(mesObjectTypeAttribute -> {
                MESObjectType mesObjectType = mesObjectTypeRepository.findOne(mesObjectTypeAttribute.getType());
                if (mesObjectType.getObjectType().equals(ObjectType.valueOf(type))) {
                    qualityTypeAttributeDto.getMesObjectTypeAttributes().add(mesObjectTypeAttribute);
                }
            });
        }
        qualityTypeAttributeDto.getObjectTypeAttributes().addAll(objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType.toString())));
        return qualityTypeAttributeDto;
    }

    @Transactional(readOnly = true)
    public List<Person> getQualityAnalysts(String groupName, String permission) {
        List<Person> persons = new ArrayList<>();
        PersonGroup personGroup = personGroupRepository.findByNameEqualsIgnoreCaseAndParentIsNull(groupName);
        if (personGroup != null) {
            List<GroupMember> groupMembers = groupMemberRepository.findByPersonGroup(personGroup);
            groupMembers.forEach(groupMember -> {
                Login login = loginRepository.findByPersonId(groupMember.getPerson().getId());
                if (login != null && login.getIsActive()) {
                    persons.add(groupMember.getPerson());
                }
            });
        } else if (permission != null) {
            List<PersonGroup> personGroups = groupPermissionRepository.getPersonGroupsByPermission(permission);
            List<Integer> groupIds = new ArrayList<>();
            personGroups.forEach(group -> {
                groupIds.add(group.getGroupId());
            });
            if (groupIds.size() > 0) {
                List<GroupMember> groupMembers = groupMemberRepository.getGroupsByGroupIds(groupIds);
                groupMembers.forEach(groupMember -> {
                    Login login = loginRepository.findByPersonId(groupMember.getPerson().getId());
                    if (login.getIsActive()) {
                        persons.add(groupMember.getPerson());
                    }
                });
            }
        }
        return persons;
    }

    @Override
    public Boolean isSubtypeOf(String s1, Object s2, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        qualityTypeRepository = webApplicationContext.getBean(QualityTypeRepository.class);
        PQMQualityType qualityType = (PQMQualityType) s2;
        if (subTypeId != null && checkWithId(qualityType, subTypeId)) {
            return true;
        }
        if (qualityType.getName().equalsIgnoreCase(s1)) {
            return true;
        } else {
            if (qualityType.getParentType() != null)
                return compareWithParent(qualityTypeRepository.findOne(qualityType.getParentType()), s1);
        }
        return false;
    }

    public Boolean checkWithId(PQMQualityType plmItemType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        qualityTypeRepository = webApplicationContext.getBean(QualityTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmItemType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = checkWithId(qualityTypeRepository.findOne(plmItemType.getParentType()), typeId);
        }
        return flag;
    }

    private Boolean compareWithParent(PQMQualityType pqmQualityType, String s1) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        qualityTypeRepository = webApplicationContext.getBean(QualityTypeRepository.class);
        Boolean flag = false;
        if (pqmQualityType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (pqmQualityType.getParentType() != null)
                flag = compareWithParent(qualityTypeRepository.findOne(pqmQualityType.getParentType()), s1);
        }
        return flag;
    }

    @Override
    public List<PQMQualityTypeAttribute> getTypeAttributes(Integer typeId, Boolean hier) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        qualityTypeAttributeRepository = webApplicationContext.getBean(QualityTypeAttributeRepository.class);
        qualityTypeRepository = webApplicationContext.getBean(QualityTypeRepository.class);
        List<PQMQualityTypeAttribute> qualityTypeAttributes = new ArrayList<>();
        if (!hier) {
            qualityTypeAttributes = qualityTypeAttributeRepository.findByTypeOrderBySeq(typeId);
        } else {
            qualityTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return qualityTypeAttributes;
    }


    @Transactional(readOnly = true)
    public Page<QualityObjectDto> getQualityObjects(InspectionPlanCriteria criteria, Pageable pageable) {
        List<QualityObjectDto> qualityObjectDtos = new LinkedList<>();
        return null;
    }

    /* ------------------------PPAP--------------------------------*/

    @Transactional
    @PreAuthorize("hasPermission(#ppapType,'create')")
    public PQMPPAPType createPpapType(PQMPPAPType ppapType) {
        PQMPPAPType pqmPpapType = ppapTypeRepository.save(ppapType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.QUALITY_TYPE, pqmPpapType));
        return pqmPpapType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#ppapType.id ,'edit')")
    public PQMPPAPType updatePpapType(Integer id, PQMPPAPType ppapType) {
        PQMPPAPType oldPpapType = ppapTypeRepository.findOne(ppapType.getId());
        PQMPPAPType existingQualityType;
        if (ppapType.getParentType() == null) {
            existingQualityType = ppapTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(ppapType.getName());
        } else {
            existingQualityType = ppapTypeRepository.findByNameEqualsIgnoreCaseAndParentType(ppapType.getName(), ppapType.getParentType());
        }
        if ( existingQualityType != null && !ppapType.getId().equals(existingQualityType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingQualityType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.QUALITY_TYPE, oldPpapType, ppapType));
        return ppapTypeRepository.save(ppapType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deletePpapType(Integer id) {
        ppapTypeRepository.delete(id);
    }


    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMPPAPType getPpapType(Integer id) {
        return ppapTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMPPAPType> getAllPpapTypes() {
        return ppapTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMPPAPType> findMultiplePpapTypes(List<Integer> ids) {
        return ppapTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMPPAPType> getPpapTypeTree() {
        List<PQMPPAPType> types = ppapTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PQMPPAPType type : types) {
            visitPpapTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PQMPPAPType> getPpapTypeChildren(Integer id) {
        return ppapTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitPpapTypeChildren(PQMPPAPType parent) {
        List<PQMPPAPType> childrens = getPpapTypeChildren(parent.getId());
        for (PQMPPAPType child : childrens) {
            visitPpapTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }


    @Transactional
    @PreAuthorize("hasPermission(#supplierAuditType,'create')")
    public PQMSupplierAuditType createSupplierAuditType(PQMSupplierAuditType supplierAuditType) {
        PQMSupplierAuditType pqmqcrType = supplierAuditTypeRepository.save(supplierAuditType);
        //applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.QUALITY_TYPE, pqmqcrType));
        return pqmqcrType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#supplierAuditType.id ,'edit')")
    public PQMSupplierAuditType updateSupplierAuditType(Integer id, PQMSupplierAuditType supplierAuditType) {
        PQMSupplierAuditType oldQualityType = supplierAuditTypeRepository.findOne(supplierAuditType.getId());
        PQMSupplierAuditType existingQualityType;
        if (supplierAuditType.getParentType() == null) {
            existingQualityType = supplierAuditTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(supplierAuditType.getName());
        } else {
            existingQualityType = supplierAuditTypeRepository.findByNameEqualsIgnoreCaseAndParentType(supplierAuditType.getName(), supplierAuditType.getParentType());
        }
        if ( existingQualityType != null && !supplierAuditType.getId().equals(existingQualityType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingQualityType.getName());
            throw new CassiniException(result);    
        }
        //applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.QUALITY_TYPE, oldQualityType, supplierAuditType));
        return supplierAuditTypeRepository.save(supplierAuditType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteSupplierAuditType(Integer id) {
        qcrTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMSupplierAuditType getSupplierAuditType(Integer id) {
        return supplierAuditTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMSupplierAuditType> getAllSupplierAuditTypes() {
        return supplierAuditTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMSupplierAuditType> findMultipleSupplierAuditTypes(List<Integer> ids) {
        return supplierAuditTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMSupplierAuditType> getSupplierAuditTypeTree() {
        List<PQMSupplierAuditType> types = supplierAuditTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
        for (PQMSupplierAuditType type : types) {
            visitSupplierAuditTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PQMSupplierAuditType> getSupplierAuditTypeChildren(Integer id) {
        return supplierAuditTypeRepository.findByParentTypeOrderByCreatedDateAsc(id);
    }

    private void visitSupplierAuditTypeChildren(PQMSupplierAuditType parent) {
        List<PQMSupplierAuditType> childrens = getSupplierAuditTypeChildren(parent.getId());
        for (PQMSupplierAuditType child : childrens) {
            visitSupplierAuditTypeChildren(child);
        }
        parent.setChildrens(childrens);
    }
}