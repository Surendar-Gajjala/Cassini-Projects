package com.cassinisys.plm.service.classification;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.repo.custom.CustomObjectTypeRepository;
import com.cassinisys.platform.service.col.AttributeAttachmentService;
import com.cassinisys.platform.service.custom.CustomObjectTypeService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.model.cm.PLMChange;
import com.cassinisys.plm.model.cm.PLMChangeAttribute;
import com.cassinisys.plm.model.cm.PLMChangeType;
import com.cassinisys.plm.model.cm.PLMChangeTypeAttribute;
import com.cassinisys.plm.model.dto.ClassificationTypesDto;
import com.cassinisys.plm.model.mes.MESObject;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESObjectType;
import com.cassinisys.plm.model.mes.MESObjectTypeAttribute;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.mro.MROObject;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.model.mro.MROObjectType;
import com.cassinisys.plm.model.mro.MROObjectTypeAttribute;
import com.cassinisys.plm.model.pgc.PGCObject;
import com.cassinisys.plm.model.pgc.PGCObjectAttribute;
import com.cassinisys.plm.model.pgc.PGCObjectType;
import com.cassinisys.plm.model.pgc.PGCObjectTypeAttribute;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pm.PMObjectType;
import com.cassinisys.plm.model.pm.PMObjectTypeAttribute;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.QualityTypeAttributeDto;
import com.cassinisys.plm.model.req.*;
import com.cassinisys.plm.model.rm.RequirementType;
import com.cassinisys.plm.model.rm.RmObjectAttribute;
import com.cassinisys.plm.model.rm.RmObjectTypeAttribute;
import com.cassinisys.plm.model.rm.SpecificationType;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.repo.cm.ChangeAttributeRepository;
import com.cassinisys.plm.repo.cm.ChangeTypeAttributeRepository;
import com.cassinisys.plm.repo.cm.ChangeTypeRepository;
import com.cassinisys.plm.repo.mes.MESObjectAttributeRepository;
import com.cassinisys.plm.repo.mes.MESObjectTypeRepository;
import com.cassinisys.plm.repo.mfr.*;
import com.cassinisys.plm.repo.mro.MROObjectAttributeRepository;
import com.cassinisys.plm.repo.mro.MROObjectTypeRepository;
import com.cassinisys.plm.repo.pgc.PGCObjectAttributeRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.PMObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.req.PLMRequirementDocumentTypeRepository;
import com.cassinisys.plm.repo.req.PLMRequirementObjectAttributeRepository;
import com.cassinisys.plm.repo.req.PLMRequirementTypeRepository;
import com.cassinisys.plm.repo.rm.*;
import com.cassinisys.plm.repo.wf.*;
import com.cassinisys.plm.service.plm.ItemTypeService;
import com.cassinisys.plm.service.pm.PMObjectTypeService;
import com.cassinisys.plm.service.req.RequirementTypeService;
import com.cassinisys.plm.service.rm.RequirementsService;
import com.cassinisys.plm.service.rm.SpecificationsService;
import com.cassinisys.plm.service.wf.PLMWorkflowTypeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by reddy on 6/13/17.
 */
@Service
public class ClassificationService {
    Logger logger = LoggerFactory.getLogger(ClassificationService.class);
    ExpressionParser parser = new SpelExpressionParser();
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RequirementsService requirementsService;
    @Autowired
    private SpecificationsService specificationsService;
    @Autowired
    private ChangeTypeService changeTypeService;
    @Autowired
    private ManufacturerTypeService manufacturerTypeService;
    @Autowired
    private ManufacturerPartTypeService manufacturerPartTypeService;
    @Autowired
    private PLMWorkflowTypeService workflowTypeService;
    @Autowired
    private QualityTypeService qualityTypeService;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private ChangeTypeRepository changeTypeRepository;
    @Autowired
    private RmObjectTypeRepository rmObjectTypeRepository;
    @Autowired
    private RequirementTypeRepository requirementTypeRepository;
    @Autowired
    private SpecificationTypeRepository specificationTypeRepository;
    @Autowired
    private ManufacturerTypeRepository manufacturerTypeRepository;
    @Autowired
    private ManufacturerPartTypeRepository manufacturerPartTypeRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private MESObjectTypeService mesObjectTypeService;
    @Autowired
    private RequirementTypeService requirementTypeService;
    @Autowired
    private PMObjectTypeService pmObjectTypeService;
    @Autowired
    private CustomObjectTypeService customObjectTypeService;
    @Autowired
    private MROObjectTypeService mroObjectTypeService;
    @Autowired
    private PGCObjectTypeService pgcObjectTypeService;
    @Autowired
    private PLMSupplierTypeService supplierTypeService;
    @Autowired
    private MROObjectTypeRepository mroObjectTypeRepository;
    @Autowired
    private QualityTypeRepository qualityTypeRepository;
    @Autowired
    private MESObjectTypeRepository mesObjectTypeRepository;
    @Autowired
    private ManufacturerPartTypeAttributeRepository manufacturerPartTypeAttributeRepository;
    @Autowired
    private ManufacturerTypeAttributeRepository manufacturerTypeAttributeRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private ChangeTypeAttributeRepository changeTypeAttributeRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemConfigurableAttributesRepository itemConfigurableAttributesRepository;
    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;
    @Autowired
    private RmObjectTypeAttributeRepository rmObjectTypeAttributeRepository;
    @Autowired
    private SupplierTypeAttributeRepository supplierTypeAttributeRepository;
    @Autowired
    private PMObjectTypeAttributeRepository pmObjectTypeAttributeRepository;
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
    private ChangeAttributeRepository changeAttributeRepository;
    @Autowired
    private ItemAttributeRepository itemAttributeRepository;
    @Autowired
    private ItemRevisionAttributeRepository itemRevisionAttributeRepository;
    @Autowired
    private ManufacturerAttributeRepository manufacturerAttributeRepository;
    @Autowired
    private ManufacturerPartAttributeRepository manufacturerPartAttributeRepository;
    @Autowired
    private RmObjectAttributeRepository rmObjectAttributeRepository;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private MROObjectAttributeRepository mroObjectAttributeRepository;
    @Autowired
    private PGCObjectAttributeRepository pgcObjectAttributeRepository;
    @Autowired
    private PLMRequirementObjectAttributeRepository requirementObjectAttributeRepository;
    @Autowired
    private SupplierAttributeRepository supplierAttributeRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private AttributeAttachmentService attributeAttachmentService;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private WorkflowTypeAttributeRepository workflowTypeAttributeRepository;
    @Autowired
    private PLMWorkflowAttributeRepository workflowAttributeRepository;
    @Autowired
    private CustomObjectTypeRepository customObjectTypeRepository;
    @Autowired
    private PLMWorkflowActivityFormFieldsRepository workflowActivityFormFieldsRepository;
    @Autowired
    private PLMWorkflowActivityFormDataRepository workflowActivityFormDataRepository;
    @Autowired
    private PLMRequirementTypeRepository plmRequirementTypeRepository;
    @Autowired
    private PLMRequirementDocumentTypeRepository requirementDocumentTypeRepository;
    @Autowired
    private SessionWrapper sessionWrapper;

    private Map<Enum, ClassificationTypeService> mapServices = new HashMap<>();
    private Map<Enum, ClassificationObjectConverter> mapConverters = new HashMap<>();

    private QualityTypeAttributeDto qualityTypeAttributeDto1 = null;
    private List<ObjectAttribute> objectAttributes1 = null;
    private List<ObjectAttribute> objectRevisionAttributes1 = new ArrayList();
    private List<ObjectTypeAttribute> objectTypeAttributes1 = null;
    private Map<Integer, ObjectTypeAttribute> stringStringHashMap = new HashMap<>();
    private ClassificationObjectConverter<PLMItemType, PLMItemTypeAttribute> itemTypeConvertor =
            new ClassificationObjectConverter<>(PLMItemType.class, PLMItemTypeAttribute.class);
    private ClassificationObjectConverter<PLMChangeType, PLMChangeTypeAttribute> changeTypeConvertor =
            new ClassificationObjectConverter<>(PLMChangeType.class, PLMChangeTypeAttribute.class);
    private ClassificationObjectConverter<RequirementType, RmObjectTypeAttribute> requirementTypeConvertor =
            new ClassificationObjectConverter<>(RequirementType.class, RmObjectTypeAttribute.class);
    private ClassificationObjectConverter<SpecificationType, RmObjectTypeAttribute> specificationTypeConvertor =
            new ClassificationObjectConverter<>(SpecificationType.class, RmObjectTypeAttribute.class);
    private ClassificationObjectConverter<PLMManufacturerType, PLMManufacturerTypeAttribute> mfrTypeConvertor =
            new ClassificationObjectConverter<>(PLMManufacturerType.class, PLMManufacturerTypeAttribute.class);
    private ClassificationObjectConverter<PLMManufacturerPartType, PLMManufacturerPartTypeAttribute> mfrPartTypeConvertor =
            new ClassificationObjectConverter<>(PLMManufacturerPartType.class, PLMManufacturerPartTypeAttribute.class);
    private ClassificationObjectConverter<PLMWorkflowType, PLMWorkflowTypeAttribute> workflowTypeConvertor =
            new ClassificationObjectConverter<>(PLMWorkflowType.class, PLMWorkflowTypeAttribute.class);
    private ClassificationObjectConverter<PQMQualityType, PQMQualityTypeAttribute> qualityTypeConverter =
            new ClassificationObjectConverter<>(PQMQualityType.class, PQMQualityTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> plantTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> workCenterTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> operationTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> mbomTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> bopTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> machineTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> toolTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> materialTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> jigsFixTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> productionOrderTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> serviceOrderTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> manpowerTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> instrumentTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> equipmentTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MROObjectType, MROObjectTypeAttribute> sparePartTypeConverter =
            new ClassificationObjectConverter<>(MROObjectType.class, MROObjectTypeAttribute.class);
    private ClassificationObjectConverter<PLMRequirementObjectType, PLMRequirementObjectTypeAttribute> requirementsTypeConverter =
            new ClassificationObjectConverter<>(PLMRequirementObjectType.class, PLMRequirementObjectTypeAttribute.class);
    private ClassificationObjectConverter<PLMRequirementType, PLMRequirementObjectTypeAttribute> requirementDocumentTypeConverter =
            new ClassificationObjectConverter<>(PLMRequirementType.class, PLMRequirementObjectTypeAttribute.class);
    private ClassificationObjectConverter<PMObjectType, PMObjectTypeAttribute> projectTypeConverter =
            new ClassificationObjectConverter<>(PMObjectType.class, PMObjectTypeAttribute.class);
    private ClassificationObjectConverter<MROObjectType, MROObjectTypeAttribute> workRequestTypeConverter =
            new ClassificationObjectConverter<>(MROObjectType.class, MROObjectTypeAttribute.class);
    private ClassificationObjectConverter<MROObjectType, MROObjectTypeAttribute> workOrderTypeConverter =
            new ClassificationObjectConverter<>(MROObjectType.class, MROObjectTypeAttribute.class);
    private ClassificationObjectConverter<MROObjectType, MROObjectTypeAttribute> assetTypeConverter =
            new ClassificationObjectConverter<>(MROObjectType.class, MROObjectTypeAttribute.class);
    private ClassificationObjectConverter<MROObjectType, MROObjectTypeAttribute> meterTypeConverter =
            new ClassificationObjectConverter<>(MROObjectType.class, MROObjectTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> assemblyLineTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<PGCObjectType, PGCObjectTypeAttribute> substanceTypeConverter =
            new ClassificationObjectConverter<>(PGCObjectType.class, PGCObjectTypeAttribute.class);
    private ClassificationObjectConverter<PGCObjectType, PGCObjectTypeAttribute> substanceGroupTypeConverter =
            new ClassificationObjectConverter<>(PGCObjectType.class, PGCObjectTypeAttribute.class);
    private ClassificationObjectConverter<PGCObjectType, PGCObjectTypeAttribute> specificationTypeConverter =
            new ClassificationObjectConverter<>(PGCObjectType.class, PGCObjectTypeAttribute.class);
    private ClassificationObjectConverter<PGCObjectType, PGCObjectTypeAttribute> declarationTypeConverter =
            new ClassificationObjectConverter<>(PGCObjectType.class, PGCObjectTypeAttribute.class);
    private ClassificationObjectConverter<PLMSupplierType, PLMSupplierTypeAttribute> supplierTypeConverter =
            new ClassificationObjectConverter<>(PLMSupplierType.class, PLMSupplierTypeAttribute.class);
    private ClassificationObjectConverter<MESObjectType, MESObjectTypeAttribute> mesObjectTypeConverter =
            new ClassificationObjectConverter<>(MESObjectType.class, MESObjectTypeAttribute.class);
    private ClassificationObjectConverter<MROObjectType, MROObjectTypeAttribute> mroObjectTypeConverter =
            new ClassificationObjectConverter<>(MROObjectType.class, MROObjectTypeAttribute.class);
    private ClassificationObjectConverter<PGCObjectType, PGCObjectTypeAttribute> pgcObjectTypeConverter =
            new ClassificationObjectConverter<>(PGCObjectType.class, PGCObjectTypeAttribute.class);
    private ClassificationObjectConverter<PLMItem, PLMItemAttribute> itemConverter =
            new ClassificationObjectConverter<>(PLMItem.class, PLMItemAttribute.class);
    private ClassificationObjectConverter<PLMItemRevision, PLMItemRevisionAttribute> itemRevisionConverter =
            new ClassificationObjectConverter<>(PLMItemRevision.class, PLMItemRevisionAttribute.class);
    private ClassificationObjectConverter<PLMChange, PLMChangeAttribute> changeConverter =
            new ClassificationObjectConverter<>(PLMChange.class, PLMChangeAttribute.class);
    private ClassificationObjectConverter<PQMInspectionPlan, PQMInspectionPlanAttribute> inspectionPlanConverter =
            new ClassificationObjectConverter<>(PQMInspectionPlan.class, PQMInspectionPlanAttribute.class);
    private ClassificationObjectConverter<PQMInspectionPlanRevision, PQMInspectionPlanRevisionAttribute> inspectionPlanRevisionConverter =
            new ClassificationObjectConverter<>(PQMInspectionPlanRevision.class, PQMInspectionPlanRevisionAttribute.class);
    private ClassificationObjectConverter<PQMInspection, PQMInspectionAttribute> inspectionConverter =
            new ClassificationObjectConverter<>(PQMInspection.class, PQMInspectionAttribute.class);
    private ClassificationObjectConverter<PQMProblemReport, PQMProblemReportAttribute> problemReportConverter =
            new ClassificationObjectConverter<>(PQMProblemReport.class, PQMProblemReportAttribute.class);
    private ClassificationObjectConverter<PQMNCR, PQMNCRAttribute> ncrConverter =
            new ClassificationObjectConverter<>(PQMNCR.class, PQMNCRAttribute.class);
    private ClassificationObjectConverter<PQMQCR, PQMQCRAttribute> qcrConverter =
            new ClassificationObjectConverter<>(PQMQCR.class, PQMQCRAttribute.class);
    private ClassificationObjectConverter<PQMPPAP, PQMPPAPAttribute> ppapConverter =
            new ClassificationObjectConverter<>(PQMPPAP.class, PQMPPAPAttribute.class);
    private ClassificationObjectConverter<PLMRequirementDocumentRevision, PLMRequirementObjectAttribute> reqDocConverter =
            new ClassificationObjectConverter<>(PLMRequirementDocumentRevision.class, PLMRequirementObjectAttribute.class);
    private ClassificationObjectConverter<PLMRequirement, PLMRequirementObjectAttribute> reqConverter =
            new ClassificationObjectConverter<>(PLMRequirement.class, PLMRequirementObjectAttribute.class);
    private ClassificationObjectConverter<MESObject, MESObjectAttribute> mesObjectConverter =
            new ClassificationObjectConverter<>(MESObject.class, MESObjectAttribute.class);
    private ClassificationObjectConverter<MROObject, MROObjectAttribute> mroObjectConverter =
            new ClassificationObjectConverter<>(MROObject.class, MROObjectAttribute.class);
    private ClassificationObjectConverter<PGCObject, PGCObjectAttribute> pgcObjectConverter =
            new ClassificationObjectConverter<>(PGCObject.class, PGCObjectAttribute.class);
    private ClassificationObjectConverter<PLMManufacturer, PLMManufacturerAttribute> mfrConverter =
            new ClassificationObjectConverter<>(PLMManufacturer.class, PLMManufacturerAttribute.class);
    private ClassificationObjectConverter<PLMManufacturerPart, PLMManufacturerPartAttribute> mfrPartConverter =
            new ClassificationObjectConverter<>(PLMManufacturerPart.class, PLMManufacturerPartAttribute.class);
    private ClassificationObjectConverter<PLMSupplier, PLMSupplierAttribute> supplierConverter =
            new ClassificationObjectConverter<>(PLMSupplier.class, PLMSupplierAttribute.class);

    static List<ObjectAttribute> castList(List list) {
        List<ObjectAttribute> result1 = (List<ObjectAttribute>) list;
        return result1;
    }

    static List<ObjectTypeAttribute> castTypeList(List list) {
        List<ObjectTypeAttribute> result2 = (List<ObjectTypeAttribute>) list;
        return result2;
    }

    @PostConstruct
    public void initMap() {
        mapServices.put(PLMObjectType.ITEMTYPE, itemTypeService);
        mapServices.put(PLMObjectType.CHANGETYPE, changeTypeService);
        mapServices.put(PLMObjectType.REQUIREMENTTYPE, requirementsService);
        mapServices.put(PLMObjectType.SPECIFICATIONTYPE, specificationsService);
        mapServices.put(PLMObjectType.MANUFACTURERTYPE, manufacturerTypeService);
        mapServices.put(PLMObjectType.MANUFACTURERPARTTYPE, manufacturerPartTypeService);
        mapServices.put(PLMObjectType.WORKFLOWTYPE, workflowTypeService);
        mapServices.put(PLMObjectType.PLANTTYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.TOOLTYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.MATERIALTYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.JIGFIXTURETYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.WORKCENTERTYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.OPERATIONTYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.MBOMTYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.BOPTYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.MACHINETYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.QUALITY_TYPE, qualityTypeService);
        mapServices.put(PLMObjectType.PRODUCTIONORDERTYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.SERVICEORDERTYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.MANPOWERTYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.INSTRUMENTTYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.SPAREPARTTYPE, mroObjectTypeService);
        mapServices.put(PLMObjectType.PMOBJECTTYPE, pmObjectTypeService);
        mapServices.put(PLMObjectType.REQUIREMENTTYPE, requirementTypeService);
        mapServices.put(PLMObjectType.REQUIREMENTDOCUMENTTYPE, requirementTypeService);
        mapServices.put(PLMObjectType.EQUIPMENTTYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.WORKREQUESTTYPE, mroObjectTypeService);
        mapServices.put(PLMObjectType.WORKORDERTYPE, mroObjectTypeService);
        mapServices.put(PLMObjectType.ASSETTYPE, mroObjectTypeService);
        mapServices.put(PLMObjectType.METERTYPE, mroObjectTypeService);
        mapServices.put(PLMObjectType.PGCSUBSTANCETYPE, pgcObjectTypeService);
        mapServices.put(PLMObjectType.PGCSUBSTANCEGROUPTYPE, pgcObjectTypeService);
        mapServices.put(PLMObjectType.PGCSPECIFICATIONTYPE, pgcObjectTypeService);
        mapServices.put(PLMObjectType.PGCDECLARATIONTYPE, pgcObjectTypeService);
        mapServices.put(PLMObjectType.SUPPLIERTYPE, supplierTypeService);
        mapServices.put(PLMObjectType.ASSEMBLYLINETYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.MESOBJECTTYPE, mesObjectTypeService);
        mapServices.put(PLMObjectType.MROOBJECTTYPE, mroObjectTypeService);
        mapServices.put(PLMObjectType.PGCOBJECTTYPE, pgcObjectTypeService);

        mapServices.put(PLMObjectType.ITEM, itemTypeService);
        mapServices.put(PLMObjectType.ITEMREVISION, itemTypeService);
        mapServices.put(PLMObjectType.CHANGE, changeTypeService);
        mapServices.put(PLMObjectType.INSPECTIONPLAN, qualityTypeService);
        mapServices.put(PLMObjectType.INSPECTION, qualityTypeService);
        mapServices.put(PLMObjectType.PROBLEMREPORT, qualityTypeService);
        mapServices.put(PLMObjectType.NCR, qualityTypeService);
        mapServices.put(PLMObjectType.QCR, qualityTypeService);
        mapServices.put(PLMObjectType.PPAP, qualityTypeService);
        mapServices.put(PLMObjectType.REQUIREMENTDOCUMENT, requirementTypeService);
        mapServices.put(PLMObjectType.REQUIREMENT, requirementTypeService);
        mapServices.put(PLMObjectType.MESOBJECT, mesObjectTypeService);
        mapServices.put(PLMObjectType.MROOBJECT, mroObjectTypeService);
        mapServices.put(PLMObjectType.PGCOBJECT, pgcObjectTypeService);
        mapServices.put(PLMObjectType.MANUFACTURER, manufacturerTypeService);
        mapServices.put(PLMObjectType.MANUFACTURERPART, manufacturerTypeService);
        mapServices.put(PLMObjectType.MFRSUPPLIER, supplierTypeService);

        mapConverters.put(PLMObjectType.ITEMTYPE, itemTypeConvertor);
        mapConverters.put(PLMObjectType.CHANGETYPE, changeTypeConvertor);
        mapConverters.put(PLMObjectType.REQUIREMENTTYPE, requirementTypeConvertor);
        mapConverters.put(PLMObjectType.SPECIFICATIONTYPE, specificationTypeConvertor);
        mapConverters.put(PLMObjectType.MANUFACTURERTYPE, mfrTypeConvertor);
        mapConverters.put(PLMObjectType.MANUFACTURERPARTTYPE, mfrPartTypeConvertor);
        mapConverters.put(PLMObjectType.WORKFLOWTYPE, workflowTypeConvertor);
        mapConverters.put(PLMObjectType.QUALITY_TYPE, qualityTypeConverter);
        mapConverters.put(PLMObjectType.PLANTTYPE, plantTypeConverter);
        mapConverters.put(PLMObjectType.WORKCENTERTYPE, workCenterTypeConverter);
        mapConverters.put(PLMObjectType.OPERATIONTYPE, operationTypeConverter);
        mapConverters.put(PLMObjectType.MBOMTYPE, mbomTypeConverter);
        mapConverters.put(PLMObjectType.BOPTYPE, bopTypeConverter);
        mapConverters.put(PLMObjectType.MACHINETYPE, machineTypeConverter);
        mapConverters.put(PLMObjectType.TOOLTYPE, toolTypeConverter);
        mapConverters.put(PLMObjectType.MATERIALTYPE, materialTypeConverter);
        mapConverters.put(PLMObjectType.JIGFIXTURETYPE, jigsFixTypeConverter);
        mapConverters.put(PLMObjectType.PRODUCTIONORDERTYPE, productionOrderTypeConverter);
        mapConverters.put(PLMObjectType.SERVICEORDERTYPE, serviceOrderTypeConverter);
        mapConverters.put(PLMObjectType.MANPOWERTYPE, manpowerTypeConverter);
        mapConverters.put(PLMObjectType.EQUIPMENTTYPE, equipmentTypeConverter);
        mapConverters.put(PLMObjectType.INSTRUMENTTYPE, instrumentTypeConverter);
        mapConverters.put(PLMObjectType.SPAREPARTTYPE, sparePartTypeConverter);
        mapConverters.put(PLMObjectType.REQUIREMENTTYPE, requirementsTypeConverter);
        mapConverters.put(PLMObjectType.REQUIREMENTDOCUMENTTYPE, requirementDocumentTypeConverter);
        mapConverters.put(PLMObjectType.PMOBJECTTYPE, projectTypeConverter);
        mapConverters.put(PLMObjectType.WORKREQUESTTYPE, workRequestTypeConverter);
        mapConverters.put(PLMObjectType.WORKORDERTYPE, workOrderTypeConverter);
        mapConverters.put(PLMObjectType.ASSETTYPE, assetTypeConverter);
        mapConverters.put(PLMObjectType.METERTYPE, meterTypeConverter);
        mapConverters.put(PLMObjectType.PGCSUBSTANCETYPE, substanceTypeConverter);
        mapConverters.put(PLMObjectType.PGCSUBSTANCEGROUPTYPE, substanceGroupTypeConverter);
        mapConverters.put(PLMObjectType.PGCSPECIFICATIONTYPE, specificationTypeConverter);
        mapConverters.put(PLMObjectType.PGCDECLARATIONTYPE, declarationTypeConverter);
        mapConverters.put(PLMObjectType.SUPPLIERTYPE, supplierTypeConverter);
        mapConverters.put(PLMObjectType.ASSEMBLYLINETYPE, assemblyLineTypeConverter);
        mapConverters.put(PLMObjectType.MESOBJECTTYPE, mesObjectTypeConverter);
        mapConverters.put(PLMObjectType.MROOBJECTTYPE, mroObjectTypeConverter);
        mapConverters.put(PLMObjectType.PGCOBJECTTYPE, pgcObjectTypeConverter);

        mapConverters.put(PLMObjectType.ITEM, itemConverter);
        mapConverters.put(PLMObjectType.ITEMREVISION, itemRevisionConverter);
        mapConverters.put(PLMObjectType.CHANGE, changeConverter);
        mapConverters.put(PLMObjectType.INSPECTIONPLAN, inspectionPlanConverter);
        mapConverters.put(PLMObjectType.INSPECTION, inspectionConverter);
        mapConverters.put(PLMObjectType.PROBLEMREPORT, problemReportConverter);
        mapConverters.put(PLMObjectType.NCR, ncrConverter);
        mapConverters.put(PLMObjectType.QCR, qcrConverter);
        mapConverters.put(PLMObjectType.PPAP, ppapConverter);
        mapConverters.put(PLMObjectType.REQUIREMENTDOCUMENT, reqDocConverter);
        mapConverters.put(PLMObjectType.REQUIREMENT, reqConverter);
        mapConverters.put(PLMObjectType.MESOBJECT, mesObjectConverter);
        mapConverters.put(PLMObjectType.MROOBJECT, mroObjectConverter);
        mapConverters.put(PLMObjectType.PGCOBJECT, pgcObjectConverter);
        mapConverters.put(PLMObjectType.MANUFACTURER, mfrConverter);
        mapConverters.put(PLMObjectType.MANUFACTURERPART, mfrPartConverter);
        mapConverters.put(PLMObjectType.MFRSUPPLIER, supplierConverter);

    }

    private String convertDate(JsonNode jsonNode) {
        String df = "dd/MM/yyyy, HH:mm:ss";
        String preferredDateFormat = null;
        if (sessionWrapper != null && sessionWrapper.getSession() != null && sessionWrapper.getSession().getPreferredDateFormat() != null) {
            preferredDateFormat = sessionWrapper.getSession().getPreferredDateFormat();
        }
        String date = jsonNode.asText();
        if (preferredDateFormat != null) {
            Date e = null;
            try {
                e = (new SimpleDateFormat(preferredDateFormat)).parse(date);
                date = (new SimpleDateFormat(df)).format(e);
            } catch (ParseException var10) {
                var10.printStackTrace();
            }
        }
        return date;
    }

    public Object validate(PLMObjectType type, ObjectNode json, String method) {
        List<PLMItemType> plmItemTypes = new ArrayList<>();
        List<PLMChangeType> changeTypes = new ArrayList<>();
        List<RequirementType> requirementTypes = new ArrayList<>();
        List<SpecificationType> specificationTypes = new ArrayList<>();
        List<PLMManufacturerType> manufacturerTypes = new ArrayList<>();
        List<PLMManufacturerPartType> manufacturerPartTypes = new ArrayList<>();
        List<PLMWorkflowType> workflowTypes = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        json.put("createdDate", convertDate(json.get("createdDate")));
        json.put("modifiedDate", convertDate(json.get("modifiedDate")));
        if (type == PLMObjectType.ITEMTYPE) {
            PLMItemType itemType = mapper.convertValue(json, PLMItemType.class);
            if (itemType.getParentType() == null) {
                plmItemTypes = itemTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(itemType.getName());
            } else {
                plmItemTypes = itemTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(itemType.getParentType(), itemType.getName());
            }
            if (plmItemTypes.size() > 1) {
                throw new RuntimeException("Duplicate name");
            } else if ((plmItemTypes.size() == 1 && plmItemTypes.get(0).getId().equals(itemType.getId())) || plmItemTypes.size() == 0) {
                if (method.equalsIgnoreCase("create")) {
                    return mapServices.get(type).create(mapConverters.get(type).convertToType(json));
                } else {
                    return mapServices.get(type).update(mapConverters.get(type).convertToType(json));
                }
            } else {
                throw new RuntimeException("Duplicate name");
            }
        } else if (type == PLMObjectType.CHANGETYPE) {
            PLMChangeType changeType = mapper.convertValue(json, PLMChangeType.class);
            if (changeType.getParentType() == null) {
                changeTypes = changeTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(changeType.getName());
            } else {
                changeTypes = changeTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(changeType.getParentType(), changeType.getName());
            }
            if (changeTypes.size() > 1) {
                throw new RuntimeException("Duplicate name");
            } else if ((changeTypes.size() == 1 && changeTypes.get(0).getId().equals(changeType.getId())) || changeTypes.size() == 0) {
                if (method.equalsIgnoreCase("create")) {
                    return mapServices.get(type).create(mapConverters.get(type).convertToType(json));
                } else {
                    return mapServices.get(type).update(mapConverters.get(type).convertToType(json));
                }
            } else {
                throw new RuntimeException("Duplicate name");
            }
        } else if (type == PLMObjectType.REQUIREMENTTYPE) {
            RequirementType rmObjectType = mapper.convertValue(json, RequirementType.class);
            if (rmObjectType == null) {
                requirementTypes = requirementTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(rmObjectType.getName());
            } else {
                requirementTypes = requirementTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(rmObjectType.getParentType(), rmObjectType.getName());
            }
            if (requirementTypes.size() > 1) {
                throw new RuntimeException("Duplicate name");
            } else if ((requirementTypes.size() == 1 && requirementTypes.get(0).getId().equals(rmObjectType.getId())) || requirementTypes.size() == 0) {
                if (method.equalsIgnoreCase("create")) {
                    return mapServices.get(type).create(mapConverters.get(type).convertToType(json));
                } else {
                    return mapServices.get(type).update(mapConverters.get(type).convertToType(json));
                }
            } else {
                throw new RuntimeException("Duplicate name");
            }
        } else if (type == PLMObjectType.SPECIFICATIONTYPE) {
            SpecificationType specificationType = mapper.convertValue(json, SpecificationType.class);
            if (specificationType.getParentType() == null) {
                specificationTypes = specificationTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(specificationType.getName());
            } else {
                specificationTypes = specificationTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(specificationType.getParentType(), specificationType.getName());
            }
            if (specificationTypes.size() > 1) {
                throw new RuntimeException("Duplicate name");
            } else if ((specificationTypes.size() == 1 && specificationTypes.get(0).getId().equals(specificationType.getId())) || specificationTypes.size() == 0) {
                if (method.equalsIgnoreCase("create")) {
                    return mapServices.get(type).create(mapConverters.get(type).convertToType(json));
                } else {
                    return mapServices.get(type).update(mapConverters.get(type).convertToType(json));
                }
            } else {
                throw new RuntimeException("Duplicate name");
            }
        } else if (type == PLMObjectType.MANUFACTURERTYPE) {
            PLMManufacturerType manufacturerType = mapper.convertValue(json, PLMManufacturerType.class);
            if (manufacturerType.getParentType() == null) {
                manufacturerTypes = manufacturerTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(manufacturerType.getName());
            } else {
                manufacturerTypes = manufacturerTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(manufacturerType.getParentType(), manufacturerType.getName());
            }
            if (manufacturerTypes.size() > 1) {
                throw new RuntimeException("Duplicate name");
            } else if ((manufacturerTypes.size() == 1 && manufacturerTypes.get(0).getId().equals(manufacturerType.getId())) || manufacturerTypes.size() == 0) {
                if (method.equalsIgnoreCase("create")) {
                    return mapServices.get(type).create(mapConverters.get(type).convertToType(json));
                } else {
                    return mapServices.get(type).update(mapConverters.get(type).convertToType(json));
                }
            } else {
                throw new RuntimeException("Duplicate name");
            }
        } else if (type == PLMObjectType.MANUFACTURERPARTTYPE) {
            PLMManufacturerPartType manufacturerPartType = mapper.convertValue(json, PLMManufacturerPartType.class);
            if (manufacturerPartType.getParentType() == null) {
                manufacturerPartTypes = manufacturerPartTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(manufacturerPartType.getName());
            } else {
                manufacturerPartTypes = manufacturerPartTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(manufacturerPartType.getParentType(), manufacturerPartType.getName());
            }
            if (manufacturerPartTypes.size() > 1) {
                throw new RuntimeException("Duplicate name");
            } else if ((manufacturerPartTypes.size() == 1 && manufacturerPartTypes.get(0).getId().equals(manufacturerPartType.getId())) || manufacturerPartTypes.size() == 0) {
                if (method.equalsIgnoreCase("create")) {
                    return mapServices.get(type).create(mapConverters.get(type).convertToType(json));
                } else {
                    return mapServices.get(type).update(mapConverters.get(type).convertToType(json));
                }
            } else {
                throw new RuntimeException("Duplicate name");
            }
        } else if (type == PLMObjectType.WORKFLOWTYPE) {
            PLMWorkflowType workflowType = mapper.convertValue(json, PLMWorkflowType.class);
            if (workflowType.getParentType() == null) {
                workflowTypes = workflowTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(workflowType.getName());
            } else {
                workflowTypes = workflowTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(workflowType.getParentType(), workflowType.getName());
            }
            if (workflowTypes.size() > 1) {
                throw new RuntimeException("Duplicate name");
            } else if ((workflowTypes.size() == 1 && workflowTypes.get(0).getId().equals(workflowType.getId())) || workflowTypes.size() == 0) {
                if (method.equalsIgnoreCase("create")) {
                    return mapServices.get(type).create(mapConverters.get(type).convertToType(json));
                } else {
                    return mapServices.get(type).update(mapConverters.get(type).convertToType(json));
                }
            } else {
                throw new RuntimeException("Duplicate name");
            }
        }
        return null;
    }

    @Transactional
    public Object createType(PLMObjectType type, ObjectNode json) {
        return mapServices.get(type).create(mapConverters.get(type).convertToType(json));
    }

    @Transactional
    public Object updateType(PLMObjectType type, ObjectNode json) {
        return validate(type, json, "update");
    }

    @Transactional
    public void deleteType(PLMObjectType type, Integer id) {
        mapServices.get(type).delete(id);
    }

    public Object getType(PLMObjectType type, Integer id) {
        return mapServices.get(type).get(id);
    }

    public List<Object> getAllTypes(PLMObjectType type) {
        return mapServices.get(type).getAll();
    }

    public List<Object> getChildren(PLMObjectType type, Integer id) {
        return mapServices.get(type).getChildren(id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public List<Object> getClassificationTree(PLMObjectType objectType) throws InterruptedException {
        Thread.sleep(100);
        List<Object> classification = new ArrayList<>();
        ClassificationTypeService service = mapServices.get(objectType);
        if (service != null) {
            classification.addAll(service.getClassificationTree());
        }
        return classification;
    }

    @Transactional
    public Object createTypeAttribute(PLMObjectType type, ObjectNode json) {
        JsonNode formula1 = json.findValue("formula");

        if (formula1 != null && !formula1.toString().trim().equals("") && !formula1.toString().trim().equals("null")) {
            String value = formula1.toString();
            if (value.contains("[") && value.contains("]")) {
                String val = type.toString().toLowerCase().replace("type", "Type");
                if (val.equals("manufacturerType")) val = "mfrType";
                if (val.equals("manufacturerpartType")) val = "mfrPartType";
                JsonNode typeId = json.findValue(new String(val));
                if (typeId == null) typeId = json.findValue(new String("type"));
                if (typeId != null) {
                    String val2 = typeId.toString();
                    Integer integer = Integer.valueOf(val2);
                    ClassificationTypeService jpaRepository = mapServices.get(type);
                    List list = jpaRepository.getAttributes(integer, true);
                    List<ObjectTypeAttribute> objectAttributes2 = castTypeList(list);
                    List<String> stringNames = new ArrayList();
                    for (ObjectTypeAttribute a : objectAttributes2) {
                        if (a.getDataType().toString().equals("INTEGER") || a.getDataType().toString().equals("DOUBLE") ||
                                a.getDataType().toString().equals("LONGTEXT") || a.getDataType().toString().equals("TEXT")) {
                            stringNames.add(a.getName());
                        }
                    }

                    ArrayList<String> arL = new ArrayList();
                    ArrayList<String> inL = new ArrayList();
                    Matcher mat = Pattern.compile("\\(\\w+\\)").matcher(value.replaceAll("\\['", "\\(").replaceAll("']", "\\)"));

                    while (mat.find()) {
                        arL.add(mat.group());
                    }
                    for (String sx : arL) {
                        Pattern p = Pattern.compile("(\\w+)");
                        Matcher m = p.matcher(sx);
                        while (m.find()) {
                            inL.add(m.group());
                        }
                    }

                    for (String va2 : inL) {
                        if (!stringNames.contains(va2)) {
                            throw new CassiniException(messageSource.getMessage("attribute_formula_string_validation",
                                    null, "Please provide correct attribute names for formula", LocaleContextHolder.getLocale()));
                        }
                    }
                } else {
                    throw new CassiniException(messageSource.getMessage("attribute_formula_format_validation ",
                            null, "Please provide correct formula format", LocaleContextHolder.getLocale()));
                }

            }

        }

        return mapServices.get(type).createAttribute(mapConverters.get(type).convertToTypeAttribute(json));
    }

    @Transactional
    public Object updateTypeAttribute(PLMObjectType type, ObjectNode json) {
        return mapServices.get(type).updateAttribute(mapConverters.get(type).convertToTypeAttribute(json));
    }

    @Transactional
    public void deleteTypeAttribute(PLMObjectType type, Integer id) {
        mapServices.get(type).deleteAttribute(id);
    }

    public Object getTypeAttribute(PLMObjectType type, Integer id) {
        return mapServices.get(type).getAttribute(id);
    }

    public List<Object> getTypeAttributes(PLMObjectType objectType, Integer typeId, Boolean hier) {
        List<Object> attributes = new ArrayList<>();
        ClassificationTypeService service = mapServices.get(objectType);
        if (service != null) {
            attributes.addAll(service.getAttributes(typeId, hier));
        }
        return attributes;
    }

    @Transactional(readOnly = true)
    public ClassificationTypesDto getAllClassificationTree() {
        ClassificationTypesDto classificationTypesDto = new ClassificationTypesDto();

        classificationTypesDto.getItemTypes().addAll(itemTypeService.getClassificationTree());
        classificationTypesDto.getCustomObjectTypes().addAll(customObjectTypeService.getClassificationTree());
        classificationTypesDto.getChangeTypes().addAll(changeTypeService.getClassificationTree());
        classificationTypesDto.getQualityTypes().addAll(qualityTypeService.getClassificationTree());
        classificationTypesDto.setPmObjectTypesDto(pmObjectTypeService.getAllPMObjectTypesTree());
        classificationTypesDto.setMesObjectTypesDto(mesObjectTypeService.getAllObjectTypesTree());
        classificationTypesDto.setMroObjectTypesDto(mroObjectTypeService.getMROObjectsTypeTree());
        classificationTypesDto.getManufacturerTypes().addAll(manufacturerTypeService.getClassificationTree());
        classificationTypesDto.getManufacturerPartTypes().addAll(manufacturerPartTypeService.getClassificationTree());
        classificationTypesDto.setPgcObjectTypesDto(pgcObjectTypeService.getAllObjectTypesTree());
        classificationTypesDto.getSupplierTypes().addAll(supplierTypeService.getClassificationTree());
        classificationTypesDto.getWorkflowTypes().addAll(workflowTypeService.getClassificationTree());

        return classificationTypesDto;
    }

    @Transactional(readOnly = true)
    public Object getClassificationType(Integer id, String objectType) {
        Object object = null;
        if (objectType.equals("ITEMS") || objectType.equals("ITEM")) {
            object = itemTypeRepository.findOne(id);
        } else if (objectType.equals("CHANGES") || objectType.equals("CHANGE")) {
            object = changeTypeRepository.findOne(id);
        } else if (objectType.equals("QUALITY")) {
            object = qualityTypeRepository.findOne(id);
        } else if (objectType.equals("MANUFACTURERS") || objectType.equals("MANUFACTURER")) {
            object = manufacturerTypeRepository.findOne(id);
        } else if (objectType.equals("MANUFACTURER PARTS") || objectType.equals("MANUFACTURERPART")) {
            object = manufacturerPartTypeRepository.findOne(id);
        } else if (objectType.equals("WORKFLOW")) {
            object = workflowTypeRepository.findOne(id);
        } else if (objectType.equals("MESOBJECT")) {
            object = mesObjectTypeRepository.findOne(id);
        } else if (objectType.equals("MAINTENANCE&REPAIR") || objectType.equals("MROOBJECT")) {
            object = mroObjectTypeRepository.findOne(id);
        } else if (objectType.equals("MANUFACTURING")) {
            object = mesObjectTypeRepository.findOne(id);
        } else if (objectType.equals("CUSTOM OBJECTS")) {
            object = customObjectTypeRepository.findOne(id);
        } else if (objectType.equals("REQUIREMENT DOCUMENTS")) {
            object = requirementDocumentTypeRepository.findOne(id);
        } else if (objectType.equals("REQUIREMENT")) {
            object = plmRequirementTypeRepository.findOne(id);
        }
        return object;
    }

    @Transactional(readOnly = true)
    public QualityTypeAttributeDto getObjectTypeAttributes(PLMObjectType objectType, Integer typeId, Integer objectId, Boolean hierarchy) {

        qualityTypeAttributeDto1 = new QualityTypeAttributeDto();
        objectAttributes1 = new ArrayList<>();
        objectTypeAttributes1 = new ArrayList<>();
        stringStringHashMap = new HashMap();

        if (!hierarchy) {
            if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLANTYPE) || objectType.equals(PLMObjectType.MATERIALINSPECTIONPLANTYPE)) {
                PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(objectId);
                List<PQMQualityTypeAttribute> qualityTypeAttributes = qualityTypeAttributeRepository.findByTypeOrderBySeq(typeId);
                objectTypeAttributes1 = castTypeList(qualityTypeAttributeDto1.getItemTypeAttributes());
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getQualityTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.CHANGETYPE)) {
                List<PLMChangeTypeAttribute> changeTypeAttributes = changeTypeAttributeRepository.findByChangeTypeOrderBySeq(typeId);
                objectTypeAttributes1 = castTypeList(changeTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getChangeTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.ITEMTYPE)) {
                List<PLMItemTypeAttribute> itemTypeAttributes = itemTypeAttributeRepository.findByItemTypeOrderBySeq(typeId);
                for (PLMItemTypeAttribute itemTypeAttribute : itemTypeAttributes) {
                    if (itemTypeAttribute.getConfigurable()) {
                        ItemConfigurableAttributes itemConfigurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(objectId, itemTypeAttribute);
                        if (itemConfigurableAttribute != null) {
                            itemTypeAttribute.setConfigurableAttr(itemConfigurableAttribute.getValues());
                        }
                    }
                    if (itemTypeAttribute.getMeasurement() != null) {
                        itemTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(itemTypeAttribute.getMeasurement().getId()));
                    }
                }
                objectTypeAttributes1 = castTypeList(itemTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getItemTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.ITEMREVISION)) {
                List<PLMItemTypeAttribute> itemTypeAttributes = itemTypeAttributeRepository.findByItemTypeOrderBySeq(typeId);
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(objectId);
                if (itemRevision != null) {
                    for (PLMItemTypeAttribute itemTypeAttribute : itemTypeAttributes) {
                        if (itemTypeAttribute.getConfigurable()) {
                            ItemConfigurableAttributes itemConfigurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(itemRevision.getId(), itemTypeAttribute);
                            if (itemConfigurableAttribute != null) {
                                itemTypeAttribute.setConfigurableAttr(itemConfigurableAttribute.getValues());
                            }
                        }
                        if (itemTypeAttribute.getMeasurement() != null) {
                            itemTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(itemTypeAttribute.getMeasurement().getId()));
                        }
                    }
                }
                objectTypeAttributes1 = castTypeList(itemTypeAttributes);
                qualityTypeAttributeDto1.getItemTypeAttributes().addAll(itemTypeAttributes);
            } else if (objectType.equals(PLMObjectType.MANUFACTURERTYPE)) {
                List<PLMManufacturerTypeAttribute> manufacturerTypeAttributes = manufacturerTypeAttributeRepository.findByMfrTypeOrderBySeq(typeId);
                objectTypeAttributes1 = castTypeList(manufacturerTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getManufacturerTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.MANUFACTURERPARTTYPE)) {
                List<PLMManufacturerPartTypeAttribute> manufacturerPartTypeAttributes = manufacturerPartTypeAttributeRepository.findByMfrPartTypeOrderBySeq(typeId);
                objectTypeAttributes1 = castTypeList(manufacturerPartTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getPartTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.SPECIFICATIONTYPE) || objectType.equals(PLMObjectType.REQUIREMENTTYPE)) {
                List<RmObjectTypeAttribute> objectTypeAttributes = rmObjectTypeAttributeRepository.findByRmObjectTypeOrderBySeq(typeId);
                objectTypeAttributes1 = castTypeList(objectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getRmObjectTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.MESOBJECTTYPE)) {
                List<MESObjectTypeAttribute> objectTypeAttributes = mesObjectTypeService.getAttributes(typeId, false);
                objectTypeAttributes1 = castTypeList(objectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getMesObjectTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.MROOBJECTTYPE)) {
                List<MROObjectTypeAttribute> objectTypeAttributes = mroObjectTypeService.getAttributes(typeId, false);
                objectTypeAttributes1 = castTypeList(objectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getMroObjectTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.PGCOBJECTTYPE)) {
                List<PGCObjectTypeAttribute> pgcObjectTypeAttributes = pgcObjectTypeService.getAttributes(typeId, false);
                objectTypeAttributes1 = castTypeList(pgcObjectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getPgcObjectTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENTTYPE)) {
                List<PLMRequirementObjectTypeAttribute> requirementObjectTypeAttributes = requirementTypeService.getAttributes(typeId, false);
                objectTypeAttributes1 = castTypeList(requirementObjectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getRequirementObjectTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.SUPPLIERTYPE)) {
                List<PLMSupplierTypeAttribute> supplierTypeAttributes = supplierTypeAttributeRepository.findByTypeOrderBySeq(typeId);
                objectTypeAttributes1 = castTypeList(supplierTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getSupplierTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.PMOBJECTTYPE)) {
                List<PMObjectTypeAttribute> pmObjectTypeAttributes = pmObjectTypeAttributeRepository.findByTypeOrderBySequence(typeId);
                objectTypeAttributes1 = castTypeList(pmObjectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getPmObjectTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.WORKFLOWTYPE)) {
                List<PLMWorkflowTypeAttribute> workflowTypeAttributes = workflowTypeAttributeRepository.findByWorkflowTypeOrderBySeq(typeId);
                objectTypeAttributes1 = castTypeList(workflowTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getWorkflowTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.PLMWORKFLOWSTATUS)) {
                List<PLMWorkflowActivityFormFields> workflowActivityFormFields = workflowActivityFormFieldsRepository.findByWorkflowActivityOrderByName(objectId);
                objectTypeAttributes1 = castTypeList(workflowActivityFormFields);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getWorkflowActivityFormFields().addAll(filterObjectTypeAttributes);
            } else {
                List<PQMQualityTypeAttribute> qualityTypeAttributes = qualityTypeAttributeRepository.findByTypeOrderBySeq(typeId);
                objectTypeAttributes1 = castTypeList(qualityTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getQualityTypeAttributes().addAll(filterObjectTypeAttributes);
            }

        } else {
            if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLANTYPE) || objectType.equals(PLMObjectType.MATERIALINSPECTIONPLANTYPE)) {
                List<PQMQualityTypeAttribute> qualityTypeAttributes = qualityTypeService.getInspectionPlanAttributesFromHierarchy(typeId, true);
                objectTypeAttributes1 = castTypeList(qualityTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getQualityTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.CHANGETYPE)) {
                List<PLMChangeTypeAttribute> changeTypeAttributes = qualityTypeService.getChangeAttributesFromHierarchy(typeId);
                objectTypeAttributes1 = castTypeList(changeTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getChangeTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.ITEMTYPE)) {
                List<PLMItemTypeAttribute> itemTypeAttributes = qualityTypeService.getItemTypeAttributesFromHierarchy(typeId, false);
                for (PLMItemTypeAttribute itemTypeAttribute : itemTypeAttributes) {
                    if (itemTypeAttribute.getConfigurable()) {
                        ItemConfigurableAttributes itemConfigurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(objectId, itemTypeAttribute);
                        if (itemConfigurableAttribute != null) {
                            itemTypeAttribute.setConfigurableAttr(itemConfigurableAttribute.getValues());
                        }
                    }
                    if (itemTypeAttribute.getMeasurement() != null) {
                        itemTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(itemTypeAttribute.getMeasurement().getId()));
                    }
                }
                objectTypeAttributes1 = castTypeList(itemTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getItemTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.ITEMREVISION)) {
                List<PLMItemTypeAttribute> itemTypeAttributes = qualityTypeService.getItemTypeAttributesFromHierarchy(typeId, true);
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(objectId);
                if (itemRevision != null) {
                    for (PLMItemTypeAttribute itemTypeAttribute : itemTypeAttributes) {
                        if (itemTypeAttribute.getConfigurable()) {
                            ItemConfigurableAttributes itemConfigurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(itemRevision.getId(), itemTypeAttribute);
                            if (itemConfigurableAttribute != null) {
                                itemTypeAttribute.setConfigurableAttr(itemConfigurableAttribute.getValues());
                            }
                        }
                        if (itemTypeAttribute.getMeasurement() != null) {
                            itemTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(itemTypeAttribute.getMeasurement().getId()));
                        }
                    }
                }
                qualityTypeAttributeDto1.getItemTypeAttributes().addAll(itemTypeAttributes);
                objectTypeAttributes1 = castTypeList(itemTypeAttributes);
            } else if (objectType.equals(PLMObjectType.MANUFACTURERTYPE)) {
                List<PLMManufacturerTypeAttribute> manufacturerTypeAttributes = manufacturerTypeService.getAttributesFromHierarchy(typeId);
                objectTypeAttributes1 = castTypeList(manufacturerTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getManufacturerTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.MANUFACTURERPARTTYPE)) {
                List<PLMManufacturerPartTypeAttribute> manufacturerPartTypeAttributes = manufacturerPartTypeService.getAttributesFromHierarchy(typeId);
                objectTypeAttributes1 = castTypeList(manufacturerPartTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getPartTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.SPECIFICATIONTYPE) || objectType.equals(PLMObjectType.REQUIREMENTTYPE)) {
                List<RmObjectTypeAttribute> objectTypeAttributes = specificationsService.getAttributesFromHierarchy(typeId);
                objectTypeAttributes1 = castTypeList(objectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getRmObjectTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.MESOBJECTTYPE)) {
                List<MESObjectTypeAttribute> objectTypeAttributes = mesObjectTypeService.getAttributesFromHierarchy(typeId);
                objectTypeAttributes1 = castTypeList(objectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getMesObjectTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.MROOBJECTTYPE)) {
                List<MROObjectTypeAttribute> objectTypeAttributes = mroObjectTypeService.getAttributesFromHierarchy(typeId);
                objectTypeAttributes1 = castTypeList(objectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getMroObjectTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.PGCOBJECTTYPE)) {
                List<PGCObjectTypeAttribute> objectTypeAttributes = pgcObjectTypeService.getAttributesFromHierarchy(typeId);
                objectTypeAttributes1 = castTypeList(objectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getPgcObjectTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENTTYPE)) {
                List<PLMRequirementObjectTypeAttribute> requirementObjectTypeAttributes = requirementTypeService.getAttributesFromHierarchy(typeId);
                objectTypeAttributes1 = castTypeList(requirementObjectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getRequirementObjectTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.SUPPLIERTYPE)) {
                List<PLMSupplierTypeAttribute> objectTypeAttributes = supplierTypeService.getAttributesFromHierarchy(typeId);
                objectTypeAttributes1 = castTypeList(objectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getSupplierTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.PMOBJECTTYPE)) {
                List<PMObjectTypeAttribute> objectTypeAttributes = pmObjectTypeService.getAttributesFromHierarchy(typeId);
                objectTypeAttributes1 = castTypeList(objectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getPmObjectTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.WORKFLOWTYPE)) {
                List<PLMWorkflowTypeAttribute> objectTypeAttributes = workflowTypeService.getAttributesFromHierarchy(typeId);
                objectTypeAttributes1 = castTypeList(objectTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getWorkflowTypeAttributes().addAll(filterObjectTypeAttributes);
            } else if (objectType.equals(PLMObjectType.PLMWORKFLOWSTATUS)) {
                List<PLMWorkflowActivityFormFields> workflowActivityFormFields = workflowActivityFormFieldsRepository.findByWorkflowActivityOrderByName(objectId);
                objectTypeAttributes1 = castTypeList(workflowActivityFormFields);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getWorkflowActivityFormFields().addAll(filterObjectTypeAttributes);
            } else {
                List<PQMQualityTypeAttribute> qualityTypeAttributes = qualityTypeService.getAttributesFromHierarchy(typeId);
                objectTypeAttributes1 = castTypeList(qualityTypeAttributes);
                List filterObjectTypeAttributes = qualityTypeService.filterObjectTypeAttributes(objectId, objectTypeAttributes1);
                qualityTypeAttributeDto1.getQualityTypeAttributes().addAll(filterObjectTypeAttributes);
            }
        }
        for (ObjectTypeAttribute typeAttribute : objectTypeAttributes1) {
            stringStringHashMap.put(typeAttribute.getId(), typeAttribute);
        }

        return qualityTypeAttributeDto1;
    }

    @Transactional(readOnly = true)
    public QualityTypeAttributeDto getObjectAttributeValues(PLMObjectType objectType, Integer typeId, Integer objectId) {

        QualityTypeAttributeDto qualityTypeAttributeDto = new QualityTypeAttributeDto();

        if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLANTYPE) || objectType.equals(PLMObjectType.MATERIALINSPECTIONPLANTYPE)) {
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(objectId);
            List<PQMInspectionPlanAttribute> inspectionPlanAttributes = inspectionPlanAttributeRepository.findByInspectionPlanIdIn(inspectionPlanRevision.getPlan().getId());
            List<PQMInspectionPlanRevisionAttribute> inspectionPlanRevisionAttributes = inspectionPlanRevisionAttributeRepository.findByInspectionPlanRevisionIdIn(objectId);
            qualityTypeAttributeDto.getInspectionPlanAttributes().addAll(inspectionPlanAttributes);
            qualityTypeAttributeDto.getInspectionPlanRevisionAttributes().addAll(inspectionPlanRevisionAttributes);
            objectAttributes1 = castList(inspectionPlanAttributes);
        } else if (objectType.equals(PLMObjectType.PRTYPE)) {
            List<PQMProblemReportAttribute> problemReportAttributes = problemReportAttributeRepository.findByProblemReportIdIn(objectId);
            objectAttributes1 = castList(problemReportAttributes);
            qualityTypeAttributeDto.getProblemReportAttributes().addAll(problemReportAttributes);
        } else if (objectType.equals(PLMObjectType.NCRTYPE)) {
            List<PQMNCRAttribute> ncrAttributes = ncrAttributeRepository.findByNcrIdIn(objectId);
            objectAttributes1 = castList(ncrAttributes);
            qualityTypeAttributeDto.getNcrAttributes().addAll(ncrAttributes);
        } else if (objectType.equals(PLMObjectType.QCRTYPE)) {
            List<PQMQCRAttribute> qcrAttributes = qcrAttributeRepository.findByQcrIdIn(objectId);
            objectAttributes1 = castList(qcrAttributes);
            qualityTypeAttributeDto.getQcrAttributes().addAll(qcrAttributes);
        } else if (objectType.equals(PLMObjectType.PPAPTYPE)) {
            List<PQMPPAPAttribute> pqmppapAttributes = ppapAttributeRepository.findByPpapIdIn(objectId);
            objectAttributes1 = castList(pqmppapAttributes);
            qualityTypeAttributeDto.getPpapAttributes().addAll(pqmppapAttributes);
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDITTYPE)) {
            List<PQMSupplierAuditAttribute> supplierAuditAttributes = supplierAuditAttributeRepository.findBySupplierAuditIdIn(objectId);
            objectAttributes1 = castList(supplierAuditAttributes);
            qualityTypeAttributeDto.getSupplierAuditAttributes().addAll(supplierAuditAttributes);
        } else if (objectType.equals(PLMObjectType.CHANGETYPE)) {
            List<PLMChangeAttribute> changeAttributes = changeAttributeRepository.findByChangeIdIn(objectId);
            objectAttributes1 = castList(changeAttributes);
            qualityTypeAttributeDto.getChangeAttributes().addAll(changeAttributes);
        } else if (objectType.equals(PLMObjectType.ITEMTYPE)) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(objectId);
            List<PLMItemAttribute> itemAttributes = itemAttributeRepository.findByItemId(itemRevision.getItemMaster());
            List<PLMItemRevisionAttribute> itemRevisionAttributes = itemRevisionAttributeRepository.findByItemId(objectId);
            objectAttributes1 = castList(itemAttributes);
            objectRevisionAttributes1 = castList(itemRevisionAttributes);
            qualityTypeAttributeDto.getItemAttributes().addAll(itemAttributes);
            qualityTypeAttributeDto.getItemRevisionAttributes().addAll(itemRevisionAttributes);
        } else if (objectType.equals(PLMObjectType.MANUFACTURERTYPE)) {
            List<PLMManufacturerAttribute> manufacturerAttributes = manufacturerAttributeRepository.findByIdIn(objectId);
            objectAttributes1 = castList(manufacturerAttributes);
            qualityTypeAttributeDto.getManufacturerAttributes().addAll(manufacturerAttributes);
        } else if (objectType.equals(PLMObjectType.MANUFACTURERPARTTYPE)) {
            List<PLMManufacturerPartAttribute> manufacturerPartAttributes = manufacturerPartAttributeRepository.findByIdIn(objectId);
            objectAttributes1 = castList(manufacturerPartAttributes);
            qualityTypeAttributeDto.getPartAttributes().addAll(manufacturerPartAttributes);
        } else if (objectType.equals(PLMObjectType.SPECIFICATIONTYPE) || objectType.equals(PLMObjectType.REQUIREMENTTYPE)) {
            List<RmObjectAttribute> rmObjectAttributes = rmObjectAttributeRepository.findByObjectId(objectId);
            objectAttributes1 = castList(rmObjectAttributes);
            qualityTypeAttributeDto.getRmObjectAttributes().addAll(rmObjectAttributes);
        } else if (objectType.equals(PLMObjectType.MESOBJECTTYPE)) {
            List<MESObjectAttribute> mesObjectAttributes = mesObjectAttributeRepository.findByObjectId(objectId);
            objectAttributes1 = castList(mesObjectAttributes);
            qualityTypeAttributeDto.getMesObjectAttributes().addAll(mesObjectAttributes);
        } else if (objectType.equals(PLMObjectType.MROOBJECTTYPE)) {
            List<MROObjectAttribute> mroObjectAttributes = mroObjectAttributeRepository.findByObjectId(objectId);
            objectAttributes1 = castList(mroObjectAttributes);
            qualityTypeAttributeDto.getMroObjectAttributes().addAll(mroObjectAttributes);
        } else if (objectType.equals(PLMObjectType.PGCOBJECTTYPE)) {
            List<PGCObjectAttribute> pgcObjectAttributes = pgcObjectAttributeRepository.findByObjectId(objectId);
            objectAttributes1 = castList(pgcObjectAttributes);
            qualityTypeAttributeDto.getPgcObjectAttributes().addAll(pgcObjectAttributes);
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENTTYPE)) {
            List<PLMRequirementObjectAttribute> requirementObjectAttributes = requirementObjectAttributeRepository.findByObjectId(objectId);
            objectAttributes1 = castList(requirementObjectAttributes);
            qualityTypeAttributeDto.getRequirementObjectAttributes().addAll(requirementObjectAttributes);
        } else if (objectType.equals(PLMObjectType.SUPPLIERTYPE)) {
            List<PLMSupplierAttribute> supplierAttributes = supplierAttributeRepository.findByIdIn(objectId);
            objectAttributes1 = castList(supplierAttributes);
            qualityTypeAttributeDto.getSupplierAttributes().addAll(supplierAttributes);
        } else if (objectType.equals(PLMObjectType.PMOBJECTTYPE)) {
            List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectId(objectId);
            objectAttributes1 = castList(objectAttributes);
            qualityTypeAttributeDto.getObjectAttributes().addAll(objectAttributes);
        } else if (objectType.equals(PLMObjectType.WORKFLOWTYPE)) {
            List<PLMWorkflowAttribute> plmWorkflowAttributes = workflowAttributeRepository.findByWorkflowIdIn(objectId);
            objectAttributes1 = castList(plmWorkflowAttributes);
            qualityTypeAttributeDto.getWorkflowAttributes().addAll(plmWorkflowAttributes);
        } else if (objectType.equals(PLMObjectType.PLMWORKFLOWSTATUS)) {
            List<PLMWorkflowActivityFormData> plmWorkflowAttributes = workflowActivityFormDataRepository.findByWorkflowIdIn(objectId);
            objectAttributes1 = castList(plmWorkflowAttributes);
            qualityTypeAttributeDto.getWorkflowActivityFormData().addAll(plmWorkflowAttributes);
        }

        Map<String, Object> attributes = new HashMap();
        for (ObjectAttribute objectAttribute : objectAttributes1) {
            ObjectTypeAttribute typeAttribute4 = stringStringHashMap.get(objectAttribute.getId().getAttributeDef());
            if (typeAttribute4 != null) {
                if (typeAttribute4.getDataType().toString().equals("INTEGER") && objectAttribute.getIntegerValue() != null)
                    attributes.put(typeAttribute4.getName(), objectAttribute.getIntegerValue());
                if (typeAttribute4.getDataType().toString().equals("DOUBLE") && objectAttribute.getDoubleValue() != null)
                    attributes.put(typeAttribute4.getName(), objectAttribute.getDoubleValue());
                if (typeAttribute4.getDataType().toString().equals("TEXT") && objectAttribute.getStringValue() != null)
                    attributes.put(typeAttribute4.getName(), objectAttribute.getStringValue());
                if (typeAttribute4.getDataType().toString().equals("LONGTEXT") && objectAttribute.getLongTextValue() != null)
                    attributes.put(typeAttribute4.getName(), objectAttribute.getLongTextValue());
            }
        }

        for (ObjectAttribute objectAttribute : objectAttributes1) {
            ObjectTypeAttribute typeAttribute4 = stringStringHashMap.get(objectAttribute.getId().getAttributeDef());
            if (typeAttribute4 != null && typeAttribute4.getDataType().toString().equals("FORMULA")) {
                try {
                    objectAttribute.setFormulaValue(parser.parseExpression(typeAttribute4.getFormula()).getValue(attributes, String.class));
                } catch (SpelEvaluationException e) {
                    logger.info(e.getMessage());
                }
            }
        }

        if (objectRevisionAttributes1 != null) {
            for (ObjectAttribute objectAttribute : objectRevisionAttributes1) {
                ObjectTypeAttribute typeAttribute4 = stringStringHashMap.get(objectAttribute.getId().getAttributeDef());
                if (typeAttribute4 != null) {
                    if (typeAttribute4.getDataType().toString().equals("INTEGER") && objectAttribute.getIntegerValue() != null)
                        attributes.put(typeAttribute4.getName(), objectAttribute.getIntegerValue());
                    if (typeAttribute4.getDataType().toString().equals("DOUBLE") && objectAttribute.getDoubleValue() != null)
                        attributes.put(typeAttribute4.getName(), objectAttribute.getDoubleValue());
                    if (typeAttribute4.getDataType().toString().equals("TEXT") && objectAttribute.getStringValue() != null)
                        attributes.put(typeAttribute4.getName(), objectAttribute.getStringValue());
                    if (typeAttribute4.getDataType().toString().equals("LONGTEXT") && objectAttribute.getLongTextValue() != null)
                        attributes.put(typeAttribute4.getName(), objectAttribute.getLongTextValue());
                }
            }

            for (ObjectAttribute objectAttribute : objectRevisionAttributes1) {
                ObjectTypeAttribute typeAttribute4 = stringStringHashMap.get(objectAttribute.getId().getAttributeDef());
                if (typeAttribute4 != null && typeAttribute4.getDataType().toString().equals("FORMULA")) {
                    try {
                        objectAttribute.setFormulaValue(parser.parseExpression(typeAttribute4.getFormula()).getValue(attributes, String.class));
                    } catch (SpelEvaluationException e) {
                        logger.info(e.getMessage());
                    }
                }
            }
        }


        return qualityTypeAttributeDto;
    }

    @Transactional
    public QualityTypeAttributeDto saveImageAttributeValue(PLMObjectType objectType, Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {

        if (objectType.equals(PLMObjectType.PROBLEMREPORTTYPE)) {
            PQMProblemReportAttribute problemReportAttribute = new PQMProblemReportAttribute();
            problemReportAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, problemReportAttribute);
        } else if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLANTYPE) || objectType.equals(PLMObjectType.MATERIALINSPECTIONPLANTYPE)) {
            PQMInspectionPlanAttribute inspectionPlanAttribute = new PQMInspectionPlanAttribute();
            inspectionPlanAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, inspectionPlanAttribute);
        } else if (objectType.equals(PLMObjectType.NCRTYPE)) {
            PQMNCRAttribute pqmncrAttribute = new PQMNCRAttribute();
            pqmncrAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, pqmncrAttribute);
        } else if (objectType.equals(PLMObjectType.QCRTYPE)) {
            PQMQCRAttribute pqmqcrAttribute = new PQMQCRAttribute();
            pqmqcrAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, pqmqcrAttribute);
        } else if (objectType.equals(PLMObjectType.PPAPTYPE)) {
            PQMPPAPAttribute pqmppapAttribute = new PQMPPAPAttribute();
            pqmppapAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, pqmppapAttribute);
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDITTYPE)) {
            PQMSupplierAuditAttribute supplierAuditAttribute = new PQMSupplierAuditAttribute();
            supplierAuditAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, supplierAuditAttribute);
        } else if (objectType.equals(PLMObjectType.MESOBJECTTYPE)) {
            MESObjectAttribute mesObjectAttribute = new MESObjectAttribute();
            mesObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, mesObjectAttribute);
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT) || objectType.equals(PLMObjectType.REQUIREMENT)) {
            PLMRequirementObjectAttribute requirementObjectAttribute = new PLMRequirementObjectAttribute();
            requirementObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, requirementObjectAttribute);
        } else if (objectType.equals(PLMObjectType.PGCOBJECTTYPE)) {
            PGCObjectAttribute pgcObjectAttribute = new PGCObjectAttribute();
            pgcObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, pgcObjectAttribute);
        } else if (objectType.equals(PLMObjectType.SUPPLIERTYPE)) {
            PLMSupplierAttribute supplierAttribute = new PLMSupplierAttribute();
            supplierAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, supplierAttribute);
        } else if (objectType.equals(PLMObjectType.ITEMTYPE)) {
            PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(attributeId);
            if (itemTypeAttribute.getRevisionSpecific()) {
                PLMItemRevisionAttribute itemRevisionAttribute = new PLMItemRevisionAttribute();
                itemRevisionAttribute.setId(new ObjectAttributeId(objectId, attributeId));
                List<MultipartFile> files = new ArrayList<>(fileMap.values());
                setImage(files, itemRevisionAttribute);
            } else {
                PLMItemAttribute itemAttribute = new PLMItemAttribute();
                itemAttribute.setId(new ObjectAttributeId(objectId, attributeId));
                List<MultipartFile> files = new ArrayList<>(fileMap.values());
                setImage(files, itemAttribute);
            }

        } else {
            ObjectAttribute objectAttribute = new ObjectAttribute();
            objectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, objectAttribute);
        }
        return null;
    }

    public void setImage(List<MultipartFile> files, ObjectAttribute attribute) {
        if (files.size() > 0) {
            MultipartFile file = files.get(0);
            try {
                attribute.setImageValue(file.getBytes());
                attribute = objectAttributeRepository.save(attribute);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public QualityTypeAttributeDto saveAttachmentAttributeValue(PLMObjectType objectType, Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        List<AttributeAttachment> attributeAttachments = null;
        List<Integer> attachmentIds = new ArrayList<>();
        try {
            List<MultipartFile> files = new ArrayList<>();
            fileMap.values().forEach(multipartFile -> {
                files.add(multipartFile);
            });
            String type = objectType.toString();
            if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLANTYPE)) {
                type = "PRODUCTINSPECTIONPLAN";
            } else if (objectType.equals(PLMObjectType.MATERIALINSPECTIONPLANTYPE)) {
                type = "MATERIALINSPECTIONPLAN";
            } else if (objectType.equals(PLMObjectType.INSPECTIONTYPE)) {
                type = "ITEMINSPECTION";
            } else if (objectType.equals(PLMObjectType.PROBLEMREPORTTYPE)) {
                type = "PROBLEMREPORT";
            } else if (objectType.equals(PLMObjectType.NCRTYPE)) {
                type = "NCR";
            } else if (objectType.equals(PLMObjectType.QCRTYPE)) {
                type = "QCR";
            } else if (objectType.equals(PLMObjectType.PPAPTYPE)) {
                type = "PPAP";
            } else if (objectType.equals(PLMObjectType.SUPPLIERAUDITTYPE)) {
                type = "SUPPLIERAUDIT";
            } else if (objectType.equals(PLMObjectType.MESOBJECTTYPE)) {
                CassiniObject cassiniObject = objectRepository.findById(objectId);
                type = cassiniObject.getObjectType().toString();
            } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT) || objectType.equals(PLMObjectType.REQUIREMENT)) {
                CassiniObject cassiniObject = objectRepository.findById(objectId);
                type = cassiniObject.getObjectType().toString();
            } else if (objectType.equals(PLMObjectType.PGCOBJECTTYPE)) {
                CassiniObject cassiniObject = objectRepository.findById(objectId);
                type = cassiniObject.getObjectType().toString();
            } else if (objectType.equals(PLMObjectType.SUPPLIERTYPE)) {
                CassiniObject cassiniObject = objectRepository.findById(objectId);
                type = cassiniObject.getObjectType().toString();
            } else if (objectType.equals(PLMObjectType.ITEMTYPE)) {
                CassiniObject cassiniObject = objectRepository.findById(objectId);
                type = cassiniObject.getObjectType().toString();
            }

            attributeAttachments = attributeAttachmentService.addAttributeMultipleAttachments(objectId, attributeId, ObjectType.valueOf(type), files);
            attributeAttachments.forEach(attributeAttachment -> {
                attachmentIds.add(attributeAttachment.getId());
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLANTYPE) || objectType.equals(PLMObjectType.MATERIALINSPECTIONPLANTYPE)) {
            PQMInspectionPlanAttribute inspectionPlanAttribute = new PQMInspectionPlanAttribute();
            inspectionPlanAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            inspectionPlanAttribute.setAttachmentValues(values);
            inspectionPlanAttribute = inspectionPlanAttributeRepository.save(inspectionPlanAttribute);
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORTTYPE)) {
            PQMProblemReportAttribute problemReportAttribute = new PQMProblemReportAttribute();
            problemReportAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            problemReportAttribute.setAttachmentValues(values);
            problemReportAttribute = problemReportAttributeRepository.save(problemReportAttribute);
        } else if (objectType.equals(PLMObjectType.NCRTYPE)) {
            PQMNCRAttribute pqmncrAttribute = new PQMNCRAttribute();
            pqmncrAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            pqmncrAttribute.setAttachmentValues(values);
            pqmncrAttribute = ncrAttributeRepository.save(pqmncrAttribute);
        } else if (objectType.equals(PLMObjectType.QCRTYPE)) {
            PQMQCRAttribute pqmqcrAttribute = new PQMQCRAttribute();
            pqmqcrAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            pqmqcrAttribute.setAttachmentValues(values);
            pqmqcrAttribute = qcrAttributeRepository.save(pqmqcrAttribute);
        } else if (objectType.equals(PLMObjectType.PPAPTYPE)) {
            PQMPPAPAttribute pqmppapAttribute = new PQMPPAPAttribute();
            pqmppapAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            pqmppapAttribute.setAttachmentValues(values);
            pqmppapAttribute = ppapAttributeRepository.save(pqmppapAttribute);
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDITTYPE)) {
            PQMSupplierAuditAttribute supplierAuditAttribute = new PQMSupplierAuditAttribute();
            supplierAuditAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            supplierAuditAttribute.setAttachmentValues(values);
            supplierAuditAttribute = supplierAuditAttributeRepository.save(supplierAuditAttribute);
        } else if (objectType.equals(PLMObjectType.MESOBJECTTYPE)) {
            MESObjectAttribute mesObjectAttribute = new MESObjectAttribute();
            mesObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            mesObjectAttribute.setAttachmentValues(values);
            mesObjectAttribute = mesObjectAttributeRepository.save(mesObjectAttribute);
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT) || objectType.equals(PLMObjectType.REQUIREMENT)) {
            PLMRequirementObjectAttribute requirementObjectAttribute = new PLMRequirementObjectAttribute();
            requirementObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            requirementObjectAttribute.setAttachmentValues(values);
            requirementObjectAttribute = requirementObjectAttributeRepository.save(requirementObjectAttribute);
        } else if (objectType.equals(PLMObjectType.PGCOBJECTTYPE)) {
            PGCObjectAttribute pgcObjectAttribute = new PGCObjectAttribute();
            pgcObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            pgcObjectAttribute.setAttachmentValues(values);
            pgcObjectAttribute = pgcObjectAttributeRepository.save(pgcObjectAttribute);
        } else if (objectType.equals(PLMObjectType.SUPPLIERTYPE)) {
            PLMSupplierAttribute supplierAttribute = new PLMSupplierAttribute();
            supplierAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            supplierAttribute.setAttachmentValues(values);
            supplierAttribute = supplierAttributeRepository.save(supplierAttribute);
        } else {
            ObjectAttribute objectAttribute = new ObjectAttribute();
            objectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            objectAttribute.setAttachmentValues(values);
            objectAttribute = objectAttributeRepository.save(objectAttribute);
        }

        return null;
    }
}
