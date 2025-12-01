package com.cassinisys.plm.service.tm;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.col.MediaType;
import com.cassinisys.platform.model.col.QComment;
import com.cassinisys.platform.model.col.UserReadComment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.model.custom.CustomObjectFile;
import com.cassinisys.platform.repo.col.AttachmentRepository;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.col.MediaRepository;
import com.cassinisys.platform.repo.col.UserReadCommentRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.repo.custom.CustomObjectFileRepository;
import com.cassinisys.platform.service.core.ObjectService;
import com.cassinisys.platform.service.core.UserTaskSystem;
import com.cassinisys.platform.service.core.UserTaskTypeService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.mro.MROEnumObject;
import com.cassinisys.plm.model.mro.MROObject;
import com.cassinisys.plm.model.mro.MROObjectFile;
import com.cassinisys.plm.model.mro.MROWorkOrder;
import com.cassinisys.plm.model.pgc.PGCEnumObject;
import com.cassinisys.plm.model.pgc.PGCObject;
import com.cassinisys.plm.model.pgc.PGCObjectFile;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.req.*;
import com.cassinisys.plm.model.wf.PLMUserTask;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.UserTaskStatus;
import com.cassinisys.plm.model.wf.dto.TasksByStatusDTO;
import com.cassinisys.plm.model.wf.dto.UserTaskDto;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mfr.*;
import com.cassinisys.plm.repo.mro.MROObjectFileRepository;
import com.cassinisys.plm.repo.mro.MROObjectRepository;
import com.cassinisys.plm.repo.pgc.PGCObjectFileRepository;
import com.cassinisys.plm.repo.pgc.PGCObjectRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.req.*;
import com.cassinisys.plm.repo.rm.GlossaryFileRepository;
import com.cassinisys.plm.repo.rm.RmObjectFileRepository;
import com.cassinisys.plm.repo.wf.PLMUserTaskRepository;
import com.cassinisys.plm.repo.wf.PLMWorkFlowStatusAssignmentRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class UserTaskService implements UserTaskSystem {
    @Autowired
    private PLMUserTaskRepository plmUserTaskRepository;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private ObjectService objectService;
    @Autowired
    private ChangeRepository changeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private UserTaskTypeService userTaskTypeService;
    @Autowired
    private PLMWorkFlowStatusAssignmentRepository plmWorkFlowStatusAssignmentRepository;
    @Autowired
    private PLMRequirementDocumentRevisionRepository requirementDocumentRevisionRepository;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private ECRRepository ecrRepository;
    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private DCRRepository dcrRepository;
    @Autowired
    private MCORepository mcoRepository;
    @Autowired
    private VarianceRepository varianceRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private NCRRepository ncrRepository;
    @Autowired
    private QCRRepository qcrRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private InspectionRepository inspectionRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private NprRepository nprRepository;
    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;
    @Autowired
    private MROObjectRepository mroObjectRepository;
    @Autowired
    private MESObjectRepository mesObjectRepository;
    @Autowired
    private PGCObjectRepository pgcObjectRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private MESBOPRepository mesBopRepository;
    @Autowired
    private PLMRequirementRepository requirementRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserReadCommentRepository userReadCommentRepository;
    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired
    private PLMRequirementVersionRepository requirementVersionRepository;
    @Autowired
    private RequirementDocumentChildrenRepository requirementDocumentChildrenRepository;
    @Autowired
    private PLMDocumentRepository plmDocumentRepository;
    @Autowired
    private MfrPartInspectionReportRepository mfrPartInspectionReportRepository;
    @Autowired
    private PPAPChecklistRepository ppapChecklistRepository;
    @Autowired
    private SupplierAuditRepository supplierAuditRepository;
    @Autowired
    private PQMSupplierAuditPlanRepository supplierAuditPlanRepository;
    @Autowired
    private PPAPRepository ppapRepository;
    @Autowired
    private ItemFileRepository itemFileRepository;
    @Autowired
    private ChangeFileRepository changeFileRepository;
    @Autowired
    private ProjectFileRepository projectFileRepository;
    @Autowired
    private ActivityFileRepository activityFileRepository;
    @Autowired
    private TaskFileRepository taskFileRepository;
    @Autowired
    private GlossaryFileRepository glossaryFileRepository;
    @Autowired
    private ManufacturerFileRepository manufacturerFileRepository;
    @Autowired
    private ManufacturerPartFileRepository manufacturerPartFileRepository;
    @Autowired
    private InspectionPlanFileRepository inspectionPlanFileRepository;
    @Autowired
    private InspectionFileRepository inspectionFileRepository;
    @Autowired
    private ProblemReportFileRepository problemReportFileRepository;
    @Autowired
    private NCRFileRepository ncrFileRepository;
    @Autowired
    private QCRFileRepository qcrFileRepository;
    @Autowired
    private RmObjectFileRepository rmObjectFileRepository;
    @Autowired
    private MESObjectFileRepository mesObjectFileRepository;
    @Autowired
    private MROObjectFileRepository mroObjectFileRepository;
    @Autowired
    private PGCObjectFileRepository pgcObjectFileRepository;
    @Autowired
    private SupplierFileRepository supplierFileRepository;
    @Autowired
    private BOPFileRepository bopFileRepository;
    @Autowired
    private PLMRequirementDocumentFileRepository requirementDocumentFileRepository;
    @Autowired
    private PLMRequirementFileRepository requirementFileRepository;
    @Autowired
    private CustomerFileRepository customerFileRepository;
    @Autowired
    private NprFileRepository nprFileRepository;
    @Autowired
    private SupplierAuditFileRepository supplierAuditFileRepository;
    @Autowired
    private CustomObjectFileRepository customObjectFileRepository;
    @Autowired
    private InspectionPlanRepository inspectionPlanRepository;
    @Autowired
    private ProgramFileRepository programFileRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private MESMBOMRepository mbomRepository;
    @Autowired
    private MBOMFileRepository mbomFileRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private BOPRouteOperationRepository routeItemRepository;

    @Autowired
    private BOPOperationFileRepository mesBopOperationFileRepository;

    @PostConstruct
    public void InitUserTaskService() {
        userTaskTypeService.registerTypeSystem("userTask", new UserTaskService());
    }

    public PLMUserTask create(PLMUserTask task) {
        return plmUserTaskRepository.save(task);
    }

    public PLMUserTask update(PLMUserTask task) {
        return plmUserTaskRepository.save(task);
    }

    public PLMUserTask getById(Integer id) {
        return plmUserTaskRepository.findOne(id);
    }

    public void delete(PLMUserTask task) {
        plmUserTaskRepository.delete(task);
    }

    public List<PLMUserTask> getByIds(List<Integer> ids) {
        return loadTasksWithData(plmUserTaskRepository.findByIdInOrderByIdDesc(ids));
    }

    public List<UserTaskDto> getAllByUser(Integer user) {
        return loadUserTasksWithData(plmUserTaskRepository.findByAssignedToOrderByModifiedDateDesc(user));
    }

    public List<PLMUserTask> getAllByUserToList(Integer user) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        plmUserTaskRepository = webApplicationContext.getBean(PLMUserTaskRepository.class);
        return plmUserTaskRepository.findByAssignedToOrderByModifiedDateDesc(user);
    }

    @Transactional(readOnly = true)
    public List<UserTaskDto> getTasksByUser(Integer user) {
        return loadUserTasksWithData(
                plmUserTaskRepository.findByAssignedToAndStatusOrderByIdDesc(user, UserTaskStatus.PENDING));
    }

    public List<UserTaskDto> getAllByUserAndStatus(Integer user, UserTaskStatus status) {
        return loadUserTasksWithData(plmUserTaskRepository.findByAssignedToAndStatusOrderByIdDesc(user, status));
    }

    public List<PLMUserTask> getAllByStatus(UserTaskStatus status) {
        return loadTasksWithData(plmUserTaskRepository.findByStatusOrderByIdDesc(status));
    }

    public PLMUserTask getBySource(Integer source) {
        return plmUserTaskRepository.findBySourceOrderByIdDesc(source);
    }

    public PLMUserTask getBySourceAndAssigned(Integer source, Integer assigned) {
        return plmUserTaskRepository.findBySourceAndAssignedToOrderByIdDesc(source, assigned);
    }

    private List<PLMUserTask> loadTasksWithData(List<PLMUserTask> tasks) {
        Map<String, List<Integer>> typeIdMap = new HashMap<>();

        tasks.forEach(t -> {
            List<Integer> ids = typeIdMap.computeIfAbsent(t.getSourceType().toString(), k -> new ArrayList<>());
            ids.add(t.getSource());

            ids = typeIdMap.computeIfAbsent(t.getContextType().toString(), k -> new ArrayList<>());
            ids.add(t.getContext());
        });

        Set<String> keys = typeIdMap.keySet();
        Map<Integer, CassiniObject> map = new HashMap<>();
        keys.forEach(key -> {
            List<CassiniObject> objs = objectService.findMultipleByType(typeIdMap.get(key), key);
            objs.forEach(o -> map.put(o.getId(), o));
        });

        tasks.forEach(t -> {
            t.setSourceObject(map.get(t.getSource()));
            t.setContextObject(map.get(t.getContext()));
        });
        return tasks;
    }

    private List<UserTaskDto> loadUserTasksWithData(List<PLMUserTask> tasks) {
        List<UserTaskDto> taskDtos = new ArrayList<>();
        for (PLMUserTask userTask : tasks) {
            UserTaskDto userTaskDto = new UserTaskDto();

            try {
                userTaskDto.setTaskType(userTask.getSourceType().toString());
                userTaskDto.setStatus(userTask.getStatus());
                if (userTask.getSourceType().equals(ObjectType.valueOf(PLMObjectType.PROJECTTASK.toString()))) {
                    PLMTask task = taskRepository.findOne(userTask.getSource());
                    PLMProject project = projectRepository.findOne(userTask.getContext());
                    userTaskDto.setName(project.getName() + "[" + task.getName() + "]");
                    userTaskDto.setObjectId(task.getId());
                    userTaskDto.setParentObjectId(task.getActivity());
                    userTaskDto.setObjectType(PLMObjectType.PROJECTTASK.toString());
                    userTaskDto.setSource(userTask.getSource());
                } else if (userTask.getSourceType()
                        .equals(ObjectType.valueOf(PLMObjectType.PROJECTACTIVITY.toString()))) {
                    PLMActivity activity = activityRepository.findOne(userTask.getSource());
                    PLMProject project = projectRepository.findOne(userTask.getContext());
                    userTaskDto.setName(project.getName() + "[" + activity.getName() + "]");
                    userTaskDto.setObjectId(activity.getId());
                    userTaskDto.setParentObjectId(project.getId());
                    userTaskDto.setObjectType(PLMObjectType.PROJECTACTIVITY.toString());
                    userTaskDto.setSource(userTask.getSource());
                } else if (userTask.getSourceType().equals(ObjectType.valueOf(PLMObjectType.REQUIREMENT.toString()))) {
                    PLMRequirementVersion requirement = requirementVersionRepository.findOne(userTask.getSource());
                    // PLMRequirementDocumentChildren children1 =
                    // requirementDocumentChildrenRepository.findByRequirementVersion(requirement);
                    PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository
                            .findByDocumentAndRequirementVersion(requirement.getRequirementDocumentRevision().getId(),
                                    requirement);
                    PLMRequirementDocumentRevision requirementDocumentRevision = requirementDocumentRevisionRepository
                            .findOne(userTask.getContext());
                    userTaskDto.setName(requirementDocumentRevision.getName() + "[" + requirement.getName() + "]");
                    userTaskDto.setObjectId(children.getId());
                    userTaskDto.setParentObjectId(requirementDocumentRevision.getId());
                    userTaskDto.setObjectType(PLMObjectType.REQUIREMENT.toString());
                    userTaskDto.setSource(userTask.getSource());
                } else if (userTask.getSourceType().equals(ObjectType.valueOf(PLMObjectType.DOCUMENT.toString()))) {
                    PLMDocument document = plmDocumentRepository.findOne(userTask.getSource());
                    userTaskDto.setName(document.getName());
                    userTaskDto.setObjectId(document.getId());
                    userTaskDto.setObjectType(PLMObjectType.DOCUMENT.toString());
                    userTaskDto.setSource(userTask.getSource());
                } else if (userTask.getSourceType()
                        .equals(ObjectType.valueOf(PLMObjectType.MFRPARTINSPECTIONREPORT.toString()))) {
                    PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository
                            .findOne(userTask.getSource());
                    PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(userTask.getContext());
                    userTaskDto
                            .setName(manufacturerPart.getPartName() + " [" + mfrPartInspectionReport.getName() + " ]");
                    userTaskDto.setObjectId(mfrPartInspectionReport.getId());
                    userTaskDto.setObjectType(PLMObjectType.MFRPARTINSPECTIONREPORT.toString());
                    userTaskDto.setParentObjectId(manufacturerPart.getManufacturer());
                    userTaskDto.setSource(userTask.getSource());
                    userTaskDto.setContext(userTask.getContext());
                } else if (userTask.getSourceType()
                        .equals(ObjectType.valueOf(PLMObjectType.PPAPCHECKLIST.toString()))) {
                    PQMPPAPChecklist checklist = ppapChecklistRepository.findOne(userTask.getSource());
                    PQMPPAP pqmppap = ppapRepository.findOne(userTask.getContext());
                    userTaskDto.setName(pqmppap.getName() + " [" + checklist.getName() + " ]");
                    userTaskDto.setObjectId(checklist.getId());
                    userTaskDto.setObjectType(PLMObjectType.PPAPCHECKLIST.toString());
                    userTaskDto.setSource(userTask.getSource());
                    userTaskDto.setContext(userTask.getContext());
                    userTaskDto.setParentObjectId(pqmppap.getId());
                } else if (userTask.getSourceType()
                        .equals(ObjectType.valueOf(PLMObjectType.SUPPLIERAUDITPLAN.toString()))) {
                    PQMSupplierAuditPlan supplierAuditPlan = supplierAuditPlanRepository.findOne(userTask.getSource());
                    PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(userTask.getContext());
                    PLMSupplier supplier = supplierRepository.findOne(supplierAuditPlan.getSupplier());
                    userTaskDto.setName(supplierAudit.getName() + " [" + supplier.getName() + " ]");
                    userTaskDto.setObjectId(supplierAuditPlan.getId());
                    userTaskDto.setObjectType(PLMObjectType.SUPPLIERAUDITPLAN.toString());
                    userTaskDto.setSource(userTask.getSource());
                    userTaskDto.setContext(userTask.getContext());
                    userTaskDto.setParentObjectId(supplierAudit.getId());
                } else if (userTask.getSourceType()
                        .equals(ObjectType.valueOf(PLMObjectType.PLMWORKFLOWSTATUS.toString()))) {
                    PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(userTask.getSource());
                    PLMWorkflow workflow = plmWorkflowRepository.findOne(userTask.getContext());
                    userTaskDto.setWorkflow(workflow.getId());
                    userTaskDto.setSource(workflowStatus.getId());
                    CassiniObject cassiniObject = objectRepository.findById(workflow.getAttachedTo());
                    userTaskDto.setWorkflowStatus(workflow.getCurrentStatus());
                    userTaskDto.setCurrentStatus(userTask.getSource());
                    userTaskDto.setAssignment(plmWorkFlowStatusAssignmentRepository
                            .findByStatusAndPersonOrderByIdDesc(userTask.getSource(), userTask.getAssignedTo()).get(0));
                    if (cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.CHANGE.toString()))) {
                        PLMChange change = changeRepository.findOne(cassiniObject.getId());
                        if (change.getChangeType().equals(ChangeType.ECO)) {
                            userTaskDto.setObjectType(PLMObjectType.ECO.toString());
                            PLMECO eco = (PLMECO) Utils.cloneObject(cassiniObject, PLMECO.class);
                            userTaskDto.setName(eco.getEcoNumber() + "[" + workflowStatus.getName() + "]");
                        } else if (change.getChangeType().equals(ChangeType.ECR)) {
                            userTaskDto.setObjectType(PLMObjectType.ECR.toString());
                            PLMECR ecr = (PLMECR) Utils.cloneObject(cassiniObject, PLMECR.class);
                            userTaskDto.setName(ecr.getCrNumber() + "[" + workflowStatus.getName() + "]");
                        } else if (change.getChangeType().equals(ChangeType.DCO)) {
                            userTaskDto.setObjectType(PLMObjectType.DCO.toString());
                            PLMDCO dco = (PLMDCO) Utils.cloneObject(cassiniObject, PLMDCO.class);
                            userTaskDto.setName(dco.getDcoNumber() + "[" + workflowStatus.getName() + "]");
                        } else if (change.getChangeType().equals(ChangeType.DCR)) {
                            userTaskDto.setObjectType(PLMObjectType.DCR.toString());
                            PLMDCR dcr = (PLMDCR) Utils.cloneObject(cassiniObject, PLMDCR.class);
                            userTaskDto.setName(dcr.getCrNumber() + "[" + workflowStatus.getName() + "]");
                        } else if (change.getChangeType().equals(ChangeType.WAIVER)
                                || change.getChangeType().equals(ChangeType.DEVIATION)) {
                            userTaskDto.setObjectType(change.getChangeType().toString());
                            PLMVariance variance = (PLMVariance) Utils.cloneObject(cassiniObject, PLMVariance.class);
                            userTaskDto.setName(variance.getVarianceNumber() + "[" + workflowStatus.getName() + "]");
                        }
                        userTaskDto.setObjectId(change.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.ITEMMCO.toString()))
                            || cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.OEMPARTMCO.toString()))) {
                        PLMMCO mco = (PLMMCO) Utils.cloneObject(cassiniObject, PLMMCO.class);
                        userTaskDto.setObjectType(cassiniObject.getObjectType().toString());
                        userTaskDto.setName(mco.getMcoNumber() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(mco.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.ITEMREVISION.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.ITEMREVISION.toString());
                        PLMItemRevision itemRevision = (PLMItemRevision) Utils.cloneObject(cassiniObject,
                                PLMItemRevision.class);
                        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                        userTaskDto.setName(item.getItemNumber() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(itemRevision.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.PROBLEMREPORT.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.PROBLEMREPORT.toString());
                        PQMProblemReport problemReport = (PQMProblemReport) Utils.cloneObject(cassiniObject,
                                PQMProblemReport.class);
                        userTaskDto.setName(problemReport.getPrNumber() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(problemReport.getId());
                    } else if (cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.NCR.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.NCR.toString());
                        PQMNCR pqmncr = (PQMNCR) Utils.cloneObject(cassiniObject, PQMNCR.class);
                        userTaskDto.setName(pqmncr.getNcrNumber() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(pqmncr.getId());
                    } else if (cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.QCR.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.QCR.toString());
                        PQMQCR pqmqcr = (PQMQCR) Utils.cloneObject(cassiniObject, PQMQCR.class);
                        userTaskDto.setName(pqmqcr.getQcrNumber() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(pqmqcr.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.INSPECTIONPLANREVISION.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.INSPECTIONPLANREVISION.toString());
                        PQMInspectionPlanRevision inspectionPlanRevision = (PQMInspectionPlanRevision) Utils
                                .cloneObject(cassiniObject, PQMInspectionPlanRevision.class);
                        userTaskDto.setName(
                                inspectionPlanRevision.getPlan().getNumber() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(inspectionPlanRevision.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.ITEMINSPECTION.toString());
                        PQMItemInspection itemInspection = (PQMItemInspection) Utils.cloneObject(cassiniObject,
                                PQMItemInspection.class);
                        userTaskDto
                                .setName(itemInspection.getInspectionNumber() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(itemInspection.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.MATERIALINSPECTION.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.MATERIALINSPECTION.toString());
                        PQMMaterialInspection materialInspection = (PQMMaterialInspection) Utils
                                .cloneObject(cassiniObject, PQMMaterialInspection.class);
                        userTaskDto.setName(
                                materialInspection.getInspectionNumber() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(materialInspection.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.MANUFACTURER.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.MANUFACTURER.toString());
                        PLMManufacturer manufacturer = (PLMManufacturer) Utils.cloneObject(cassiniObject,
                                PLMManufacturer.class);
                        userTaskDto.setName(manufacturer.getName() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(manufacturer.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.MANUFACTURERPART.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.MANUFACTURERPART.toString());
                        PLMManufacturerPart manufacturerPart = (PLMManufacturerPart) Utils.cloneObject(cassiniObject,
                                PLMManufacturerPart.class);
                        userTaskDto.setName(manufacturerPart.getPartNumber() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(manufacturerPart.getId());
                        userTaskDto.setParentObjectId(manufacturerPart.getManufacturer());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.PLMNPR.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.PLMNPR.toString());
                        PLMNpr plmNpr = (PLMNpr) Utils.cloneObject(cassiniObject, PLMNpr.class);
                        userTaskDto.setName(plmNpr.getNumber() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(plmNpr.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(MROEnumObject.MROWORKORDER.toString()))) {
                        userTaskDto.setObjectType(MROEnumObject.MROWORKORDER.toString());
                        MROWorkOrder workOrder = (MROWorkOrder) Utils.cloneObject(cassiniObject, MROWorkOrder.class);
                        userTaskDto.setName(workOrder.getNumber() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(workOrder.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.PROJECT.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.PROJECT.toString());
                        PLMProject project = (PLMProject) Utils.cloneObject(cassiniObject, PLMProject.class);
                        userTaskDto.setName(project.getName() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(project.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.PROJECTACTIVITY.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.PROJECTACTIVITY.toString());
                        PLMActivity activity = (PLMActivity) Utils.cloneObject(cassiniObject, PLMActivity.class);
                        userTaskDto.setName(activity.getName() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(activity.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.SUPPLIERAUDIT.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.SUPPLIERAUDIT.toString());
                        PQMSupplierAudit supplierAudit = (PQMSupplierAudit) Utils.cloneObject(cassiniObject,
                                PQMSupplierAudit.class);
                        userTaskDto.setName(supplierAudit.getName() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(supplierAudit.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.PPAP.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.PPAP.toString());
                        PQMPPAP pqmppap = (PQMPPAP) Utils.cloneObject(cassiniObject, PQMPPAP.class);
                        userTaskDto.setName(pqmppap.getName() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(pqmppap.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.CUSTOMOBJECT.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.CUSTOMOBJECT.toString());
                        CustomObject customObject = (CustomObject) Utils.cloneObject(cassiniObject, CustomObject.class);
                        userTaskDto.setName(customObject.getNumber() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(customObject.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.PROGRAM.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.PROGRAM.toString());
                        PLMProgram program = (PLMProgram) Utils.cloneObject(cassiniObject, PLMProgram.class);
                        userTaskDto.setName(program.getName() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(program.getId());
                    } else if (cassiniObject.getObjectType()
                            .equals(ObjectType.valueOf(PLMObjectType.PROJECTTASK.toString()))) {
                        userTaskDto.setObjectType(PLMObjectType.PROJECTTASK.toString());
                        PLMTask task = (PLMTask) Utils.cloneObject(cassiniObject, PLMTask.class);
                        userTaskDto.setName(task.getName() + "[" + workflowStatus.getName() + "]");
                        userTaskDto.setObjectId(task.getId());
                        userTaskDto.setParentObjectId(task.getActivity());
                    }
                }
            } catch (NullPointerException e) {
                userTaskDto = null;
                plmUserTaskRepository.delete(userTask);
            }

            if (userTaskDto != null) {
                taskDtos.add(userTaskDto);
            }
        }

        return taskDtos;
    }

    public List<TasksByStatusDTO> getTaskCountsByStatus() {
        return plmUserTaskRepository.findCountsByStatus();
    }

    public Integer getUserTaskCountsByStatus() {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        return plmUserTaskRepository.getUserTaskCountsByStatus(UserTaskStatus.PENDING, person.getId());
    }

    public Long getTaskCountByStatus(UserTaskStatus status) {
        return plmUserTaskRepository.countByStatus(status);
    }

    public Integer getConversationCountByPerson(Integer person) {
        List<Integer> projectIdsWithoutPerson = new ArrayList<>();
        List<Integer> projectIds = projectRepository.getProjectIdsWithConversationPrivate();
        if (projectIds.size() > 0) {
            for (Integer projectId : projectIds) {
                PLMProjectMember existPerson = projectMemberRepository.findByProjectAndPerson(projectId, person);
                PLMProject project = projectRepository.findOne(projectId);
                if (existPerson == null && !project.getProjectManager().equals(person)) {
                    projectIdsWithoutPerson.add(projectId);
                }
            }
            if (projectIdsWithoutPerson.size() > 0) {
                List<Integer> commentIds = commentRepository.findByObjectIdIn(projectIdsWithoutPerson);
                if (commentIds.size() > 0) {
                    Integer count = userReadCommentRepository.getUnreadCommentCountWithoutCommentIds(person,
                            commentIds);
                    return count;
                } else {
                    return userReadCommentRepository.getUserUnreadCommentCount(person);
                }
            } else {
                return userReadCommentRepository.getUserUnreadCommentCount(person);
            }
        } else {
            return userReadCommentRepository.getUserUnreadCommentCount(person);
        }
    }

    @Transactional(readOnly = true)
    public Page<Comment> getAllComments(ObjectType objectType,
                                        Integer objectId, Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC,
                    "commentedDate")));
        }

        Integer loginPersonId = null;
        List<Integer> commentIds = new ArrayList<>();
        List<Integer> projectIdsWithoutPerson = new ArrayList<>();
        if (sessionWrapper != null && sessionWrapper.getSession() != null) {
            loginPersonId = sessionWrapper.getSession().getLogin().getPerson().getId();
            List<Integer> projectIds = projectRepository.getProjectIdsWithConversationPrivate();
            for (Integer projectId : projectIds) {
                PLMProjectMember existPerson = projectMemberRepository.findByProjectAndPerson(projectId, loginPersonId);
                PLMProject project = projectRepository.findOne(projectId);
                if (existPerson == null && !project.getProjectManager().equals(loginPersonId)) {
                    projectIdsWithoutPerson.add(projectId);
                }
            }
            if (projectIdsWithoutPerson.size() > 0) {
                commentIds = commentRepository.findByObjectIdIn(projectIdsWithoutPerson);
            }
        }

        Page<Comment> comments = null;
        if (objectId != null) {
            comments = commentRepository.findByObjectIdAndObjectTypeAndReplyToIsNull(
                    objectId, objectType, pageable);
        } else {
            if (commentIds.size() > 0) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(QComment.comment1.id.notIn(commentIds));
                Predicate predicate = ExpressionUtils.allOf(predicates);
                comments = commentRepository.findAll(predicate, pageable);
            } else {
                comments = commentRepository.findAll(pageable);
            }
        }

        if (comments.getContent().size() > 0) {
            comments.getContent().forEach(comment -> {
                if (comment.getObjectType() != null) {
                    setObjectDetails(comment);
                }
                if (sessionWrapper != null && sessionWrapper.getSession() != null) {
                    UserReadComment userReadComment = userReadCommentRepository.getUserReadByCommentAndPerson(
                            comment.getId(), sessionWrapper.getSession().getLogin().getPerson().getId());
                    if (userReadComment != null) {
                        comment.setRead(userReadComment.getRead());
                    }
                }
                comment.getImages().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.IMAGE));
                comment.getVideos().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.VIDEO));
                comment.getAttachments().addAll(
                        attachmentRepository.findByObjectIdAndObjectType(comment.getId(), ObjectType.ATTACHMENT));
            });
        }
        return comments;
    }

    @Transactional(readOnly = true)
    public Page<Comment> getAllUnreadMessageByPerson(Integer personId, Pageable pageable) {
        checkNotNull(pageable);

        Integer loginPersonId = null;
        List<Integer> commentIds = new ArrayList<>();
        List<Integer> projectIdsWithoutPerson = new ArrayList<>();
        if (sessionWrapper != null && sessionWrapper.getSession() != null) {
            loginPersonId = sessionWrapper.getSession().getLogin().getPerson().getId();
            List<Integer> projectIds = projectRepository.getProjectIdsWithConversationPrivate();
            for (Integer projectId : projectIds) {
                PLMProjectMember existPerson = projectMemberRepository.findByProjectAndPerson(projectId, loginPersonId);
                PLMProject project = projectRepository.findOne(projectId);
                if (existPerson == null && !project.getProjectManager().equals(loginPersonId)) {
                    projectIdsWithoutPerson.add(projectId);
                }
            }
            if (projectIdsWithoutPerson.size() > 0) {
                commentIds = commentRepository.findByObjectIdIn(projectIdsWithoutPerson);
            }
        }
        List<Integer> unreadCommentIds = new ArrayList<>();
        if (commentIds.size() > 0) {
            unreadCommentIds = userReadCommentRepository.getUnreadCommentIdsWithoutCommentIds(personId, commentIds);
        } else {
            unreadCommentIds = userReadCommentRepository.getUnreadCommentIdsByPerson(personId);
        }
        Page<Comment> comments = commentRepository.findByIdIn(unreadCommentIds, pageable);
        if (comments.getContent().size() > 0) {
            comments.getContent().forEach(comment -> {
                if (comment.getObjectType() != null) {
                    setObjectDetails(comment);
                }
                if (sessionWrapper != null && sessionWrapper.getSession() != null) {
                    UserReadComment userReadComment = userReadCommentRepository.getUserReadByCommentAndPerson(
                            comment.getId(), sessionWrapper.getSession().getLogin().getPerson().getId());
                    if (userReadComment != null) {
                        comment.setRead(userReadComment.getRead());
                    }
                }
                comment.getImages().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.IMAGE));
                comment.getVideos().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.VIDEO));
                comment.getAttachments().addAll(
                        attachmentRepository.findByObjectIdAndObjectType(comment.getId(), ObjectType.ATTACHMENT));
            });
        }
        return comments;
    }

    @Transactional(readOnly = true)
    public Page<Comment> searchComments(String query, Pageable pageable) {
        List<Predicate> predicates = new ArrayList<>();
        String[] arr = query.split(" ");
        for (String s : arr) {
            predicates.add(QComment.comment1.comment.containsIgnoreCase(s.trim()));
        }
        Integer loginPersonId = null;
        List<Integer> commentIds = new ArrayList<>();
        List<Integer> projectIdsWithoutPerson = new ArrayList<>();
        if (sessionWrapper != null && sessionWrapper.getSession() != null) {
            loginPersonId = sessionWrapper.getSession().getLogin().getPerson().getId();
            List<Integer> projectIds = projectRepository.getProjectIdsWithConversationPrivate();
            for (Integer projectId : projectIds) {
                PLMProjectMember existPerson = projectMemberRepository.findByProjectAndPerson(projectId, loginPersonId);
                PLMProject project = projectRepository.findOne(projectId);
                if (existPerson == null && !project.getProjectManager().equals(loginPersonId)) {
                    projectIdsWithoutPerson.add(projectId);
                }
            }
            if (projectIdsWithoutPerson.size() > 0) {
                commentIds = commentRepository.findByObjectIdIn(projectIdsWithoutPerson);
            }
        }
        if (commentIds.size() > 0) {
            predicates.add(QComment.comment1.id.notIn(commentIds));
        }
        Predicate predicate = ExpressionUtils.allOf(predicates);
        Page<Comment> comments = commentRepository.findAll(predicate, pageable);

        if (comments.getContent().size() > 0) {
            comments.getContent().forEach(comment -> {
                if (comment.getObjectType() != null) {
                    setObjectDetails(comment);
                }
                if (sessionWrapper != null && sessionWrapper.getSession() != null) {
                    UserReadComment userReadComment = userReadCommentRepository.getUserReadByCommentAndPerson(
                            comment.getId(), sessionWrapper.getSession().getLogin().getPerson().getId());
                    if (userReadComment != null) {
                        comment.setRead(userReadComment.getRead());
                    }
                }
                comment.getImages().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.IMAGE));
                comment.getVideos().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.VIDEO));
                comment.getAttachments().addAll(
                        attachmentRepository.findByObjectIdAndObjectType(comment.getId(), ObjectType.ATTACHMENT));
            });
        }

        return comments;
    }

    private Comment setObjectDetails(Comment comment) {
        comment.setType(comment.getObjectType().toString());
        if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.CHANGE.toString()))) {
            PLMChange change = changeRepository.findOne(comment.getObjectId());
            if (change != null) {
                comment.setType(change.getChangeType().toString());
                if (change.getChangeType().equals(ChangeType.ECO)) {
                    PLMECO eco = ecoRepository.findOne(comment.getObjectId());
                    comment.setNumber(eco.getEcoNumber());
                } else if (change.getChangeType().equals(ChangeType.ECR)) {
                    PLMECR ecr = ecrRepository.findOne(comment.getObjectId());
                    comment.setNumber(ecr.getCrNumber());
                } else if (change.getChangeType().equals(ChangeType.DCO)) {
                    PLMDCO dco = dcoRepository.findOne(comment.getObjectId());
                    comment.setNumber(dco.getDcoNumber());
                } else if (change.getChangeType().equals(ChangeType.DCR)) {
                    PLMDCR dcr = dcrRepository.findOne(comment.getObjectId());
                    comment.setNumber(dcr.getCrNumber());
                } else if (change.getChangeType().equals(ChangeType.WAIVER)
                        || change.getChangeType().equals(ChangeType.DEVIATION)) {
                    PLMVariance variance = varianceRepository.findOne(comment.getObjectId());
                    comment.setNumber(variance.getVarianceNumber());
                } else if (change.getChangeType().equals(ChangeType.MCO)) {
                    PLMMCO mco = mcoRepository.findOne(comment.getObjectId());
                    comment.setNumber(mco.getMcoNumber());
                }
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMMCO.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.OEMPARTMCO.toString()))) {
            PLMMCO mco = mcoRepository.findOne(comment.getObjectId());
            if (mco != null) {
                comment.setNumber(mco.getMcoNumber());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEM.toString()))) {
            PLMItem item = itemRepository.findOne(comment.getObjectId());
            if (item != null) {
                comment.setNumber(item.getItemNumber());
                comment.setParentObjectId(item.getLatestRevision());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROBLEMREPORT.toString()))) {
            PQMProblemReport problemReport = problemReportRepository.findOne(comment.getObjectId());
            if (problemReport != null) {
                comment.setNumber(problemReport.getPrNumber());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.NCR.toString()))) {
            PQMNCR pqmncr = ncrRepository.findOne(comment.getObjectId());
            if (pqmncr != null) {
                comment.setNumber(pqmncr.getNcrNumber());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.QCR.toString()))) {
            PQMQCR pqmqcr = qcrRepository.findOne(comment.getObjectId());
            if (pqmqcr != null) {
                comment.setNumber(pqmqcr.getQcrNumber());
            }
        } else if (comment.getObjectType()
                .equals(ObjectType.valueOf(PLMObjectType.INSPECTIONPLANREVISION.toString()))) {
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository
                    .findOne(comment.getObjectId());
            if (inspectionPlanRevision != null) {
                comment.setNumber(inspectionPlanRevision.getPlan().getNumber());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MATERIALINSPECTION.toString()))) {
            PQMInspection itemInspection = inspectionRepository.findOne(comment.getObjectId());
            if (itemInspection != null) {
                comment.setNumber(itemInspection.getInspectionNumber());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MANUFACTURER.toString()))) {
            PLMManufacturer manufacturer = manufacturerRepository.findOne(comment.getObjectId());
            if (manufacturer != null) {
                comment.setNumber(manufacturer.getName());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MANUFACTURERPART.toString()))) {
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(comment.getObjectId());
            if (manufacturerPart != null) {
                comment.setNumber(manufacturerPart.getPartNumber());
                comment.setParentObjectId(manufacturerPart.getManufacturer());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MFRPARTINSPECTIONREPORT.toString()))) {
            PLMMfrPartInspectionReport file = mfrPartInspectionReportRepository.findOne(comment.getObjectId());
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(file.getManufacturerPart());
            comment.setNumber(file.getName());
            comment.setType(PLMObjectType.MFRPARTINSPECTIONREPORT.toString());
            comment.setParentId(manufacturerPart.getManufacturer());
            comment.setParentObjectId(manufacturerPart.getId());

        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PLMNPR.toString()))) {
            PLMNpr plmNpr = nprRepository.findOne(comment.getObjectId());
            if (plmNpr != null) {
                comment.setNumber(plmNpr.getNumber());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(MROEnumObject.MROASSET.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MROEnumObject.MROMETER.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MROEnumObject.MROSPAREPART.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MROEnumObject.MROWORKORDER.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MROEnumObject.MROWORKORDER.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MROEnumObject.MROMAINTENANCEPLAN.toString()))) {
            MROObject mroObject = mroObjectRepository.findOne(comment.getObjectId());
            if (mroObject != null) {
                comment.setNumber(mroObject.getNumber());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(MESEnumObject.PLANT.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MESEnumObject.ASSEMBLYLINE.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MESEnumObject.WORKCENTER.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MESEnumObject.MACHINE.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MESEnumObject.EQUIPMENT.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MESEnumObject.INSTRUMENT.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MESEnumObject.TOOL.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MESEnumObject.JIGFIXTURE.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MESEnumObject.MATERIAL.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MESEnumObject.MANPOWER.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MESEnumObject.SHIFT.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MESEnumObject.OPERATION.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(MESEnumObject.PRODUCTIONORDER.toString()))) {
            MESObject mesObject = mesObjectRepository.findOne(comment.getObjectId());
            if (mesObject != null) {
                comment.setNumber(mesObject.getNumber());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PGCEnumObject.PGCSPECIFICATION.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(PGCEnumObject.PGCSUBSTANCE.toString()))
                || comment.getObjectType().equals(ObjectType.valueOf(PGCEnumObject.PGCDECLARATION.toString()))) {
            PGCObject pgcObject = pgcObjectRepository.findOne(comment.getObjectId());
            if (pgcObject != null) {
                comment.setNumber(pgcObject.getNumber());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MFRSUPPLIER.toString()))) {
            PLMSupplier plmSupplier = supplierRepository.findOne(comment.getObjectId());
            if (plmSupplier != null) {
                comment.setNumber(plmSupplier.getName());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.BOP.toString()))) {
            MESBOP mesBop = mesBopRepository.findOne(comment.getObjectId());
            if (mesBop != null) {
                comment.setNumber(mesBop.getName());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROJECT.toString()))) {
            PLMProject project = projectRepository.findOne(comment.getObjectId());
            if (project != null) {
                comment.setNumber(project.getName());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROGRAM.toString()))) {
            PLMProgram program = programRepository.findOne(comment.getObjectId());
            if (program != null) {
                comment.setNumber(program.getName());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROJECTACTIVITY.toString()))) {
            PLMActivity activity = activityRepository.findOne(comment.getObjectId());
            if (activity != null) {
                comment.setNumber(activity.getName());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MBOM.toString()))) {
            MESMBOM mbom = mbomRepository.findByLatestRevision(comment.getObjectId());
            if (mbom != null) {
                MESObject mesObj = mesObjectRepository.findOne(mbom.getId());
                comment.setNumber(mesObj.getName());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROJECTTASK.toString()))) {
            PLMTask task = taskRepository.findOne(comment.getObjectId());
            if (task != null) {
                comment.setNumber(task.getName());
                comment.setParentObjectId(task.getActivity());
            }
        } else if (comment.getObjectType()
                .equals(ObjectType.valueOf(RequirementEnumObject.REQUIREMENTDOCUMENTREVISION.toString()))) {
            PLMRequirementDocumentRevision documentRevision = requirementDocumentRevisionRepository
                    .findOne(comment.getObjectId());
            if (documentRevision != null) {
                comment.setNumber(documentRevision.getMaster().getNumber());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.REQUIREMENT.toString()))) {
            PLMRequirementDocumentChildren requirement = requirementDocumentChildrenRepository.findOne(comment.getObjectId());
            if (requirement != null) {
                comment.setNumber(requirement.getRequirementVersion().getMaster().getNumber());
                comment.setParentObjectId(requirement.getId());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.SUPPLIERAUDITPLAN.toString()))) {
            PQMSupplierAuditPlan supplierAuditPlan = supplierAuditPlanRepository.findOne(comment.getObjectId());
            if (supplierAuditPlan != null) {
                PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(supplierAuditPlan.getSupplierAudit());
                PLMSupplier supplier = supplierRepository.findOne(supplierAuditPlan.getSupplier());
                comment.setNumber(supplierAudit.getNumber() + "" + supplier.getName());
                comment.setParentObjectId(supplierAuditPlan.getSupplierAudit());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.SUPPLIERAUDIT.toString()))) {
            PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(comment.getObjectId());
            if (supplierAudit != null) {
                comment.setNumber(supplierAudit.getNumber());
                comment.setParentObjectId(supplierAudit.getId());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.DOCUMENT.toString()))) {
            PLMDocument plmDocument = plmDocumentRepository.findOne(comment.getObjectId());
            if (plmDocument != null) {
                comment.setNumber(plmDocument.getName());
                comment.setParentObjectId(plmDocument.getParentFile());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PPAP.toString()))) {
            PQMPPAP pqmppap = ppapRepository.findOne(comment.getObjectId());
            if (pqmppap != null) {
                comment.setNumber(pqmppap.getNumber());
                comment.setParentObjectId(pqmppap.getId());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PPAPCHECKLIST.toString()))) {
            PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(comment.getObjectId());
            if (pqmppapChecklist != null) {
                PQMPPAP ppap = ppapRepository.findOne(pqmppapChecklist.getPpap());
                comment.setParentObjectId(pqmppapChecklist.getPpap());
                comment.setType(ppap.getObjectType().toString());
                comment.setNumber(pqmppapChecklist.getName());
            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROGRAMFILE.toString()))) {
            PLMProgramFile programFile = programFileRepository.findOne(comment.getObjectId());
            if (programFile != null) {
                PLMProgram program = programRepository.findOne(programFile.getProgram());
                comment.setParentObjectId(programFile.getProgram());
                comment.setType(program.getObjectType().toString());
                if (programFile.getFileNo() != null) {
                    comment.setNumber(programFile.getFileNo());
                } else {
                    comment.setNumber(programFile.getName());
                }

            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.OBJECTDOCUMENT.toString()))) {
            PLMObjectDocument objectDocument = objectDocumentRepository.findOne(comment.getObjectId());
            if (objectDocument != null) {
                if (objectDocument.getDocument().getFileNo() != null) {
                    comment.setNumber(objectDocument.getDocument().getFileNo());
                } else {
                    comment.setNumber(objectDocument.getDocument().getName());
                }
                comment.setParentObjectId(objectDocument.getObject());
                Enum cassiniObject = objectRepository.getObjectTypeById(objectDocument.getObject());
                if (cassiniObject.name().equals("BOPROUTEOPERATION")) {
                    MESBOPRouteOperation routeItem = routeItemRepository.findOne(objectDocument.getObject());
                    if (routeItem != null) {
                        comment.setParentId(routeItem.getBop());
                    }
                }
                if (cassiniObject.name().equals("PROJECTTASK")) {
                    PLMTask plmTask = taskRepository.findOne(objectDocument.getObject());
                    if (plmTask != null) {
                        comment.setParentId(plmTask.getActivity());
                    }
                }

                PLMActivity activity = activityRepository.findOne(comment.getObjectId());
                if (activity != null) {
                    comment.setNumber(activity.getName());
                }

                if (cassiniObject != null) {
                    comment.setType(cassiniObject.toString());
                }
                comment.setObjectType(ObjectType.valueOf(PLMObjectType.DOCUMENT.name()));

            }
        } else if (comment.getObjectType().equals(ObjectType.valueOf(PLMObjectType.FILE.toString()))) {
            PLMFile file = fileRepository.findById(comment.getObjectId());
            CustomObjectFile customObjectFile1 = customObjectFileRepository.findOne(comment.getObjectId());
            if (customObjectFile1 != null) {
                if (customObjectFile1.getNumber() != null) {
                    comment.setNumber(customObjectFile1.getNumber());
                } else {
                    comment.setNumber(customObjectFile1.getName());
                }
                comment.setType(PLMObjectType.CUSTOMOBJECT.toString());
                comment.setParentObjectId(customObjectFile1.getObject());

            }
            if (file != null) {
                comment.setNumber(file.getName());
                PLMItemFile itemFile = itemFileRepository.findOne(file.getId());
                PLMChangeFile changeFile = changeFileRepository.findOne(file.getId());
                PLMProjectFile projectFile = projectFileRepository.findOne(file.getId());
                PLMActivityFile activityFile = activityFileRepository.findOne(file.getId());
                PLMTaskFile taskFile = taskFileRepository.findOne(file.getId());
                PLMProgramFile programFile = programFileRepository.findOne(file.getId());
                PLMManufacturerFile manufacturerFile = manufacturerFileRepository.findOne(file.getId());
                PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileRepository.findOne(file.getId());
                PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(file.getId());
                PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(file.getId());
                PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(file.getId());
                PQMNCRFile ncrFile = ncrFileRepository.findOne(file.getId());
                PQMQCRFile qcrFile = qcrFileRepository.findOne(file.getId());
                MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(file.getId());
                MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(file.getId());
                PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(file.getId());
                PLMSupplierFile plmSupplierFile = supplierFileRepository.findOne(file.getId());
                MESBOPFile mesBopFile = bopFileRepository.findOne(file.getId());
                PLMRequirementDocumentFile requirementDocumentFile = requirementDocumentFileRepository
                        .findOne(file.getId());
                PLMRequirementFile requirementFile = requirementFileRepository.findOne(file.getId());
                PQMCustomerFile customerFile = customerFileRepository.findOne(file.getId());
                PLMNprFile plmNprFile = nprFileRepository.findOne(file.getId());
                CustomObjectFile customObjectFile = customObjectFileRepository.findOne(file.getId());
                PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(file.getId());
                PLMDocument plmDocument = plmDocumentRepository.findOne(file.getId());
                MESMBOMFile mbomFile = mbomFileRepository.findOne(file.getId());
                MESBOPOperationFile mesbopOperationFile = mesBopOperationFileRepository.findOne(file.getId());


                if (itemFile != null) {
                    comment.setType(PLMObjectType.ITEMREVISION.name());
                    comment.setParentObjectId(itemFile.getItem().getId());
                } else if (taskFile != null) {
                    PLMTask task = taskRepository.findOne(taskFile.getTask());
                    comment.setType(task.getObjectType().toString());
                    comment.setParentObjectId(task.getId());
                    comment.setParentId(task.getActivity());
                } else if (file.getObjectType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
                    PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository
                            .findOne(file.getId());
                    if (mfrPartInspectionReport != null) {
                        PLMManufacturerPart manufacturerPart = manufacturerPartRepository
                                .findOne(mfrPartInspectionReport.getManufacturerPart());
                        comment.setType(manufacturerPart.getObjectType().toString());
                        comment.setParentObjectId(mfrPartInspectionReport.getManufacturerPart());
                    }
                } else if (file.getObjectType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
                    PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(file.getId());
                    if (pqmppapChecklist != null) {
                        PQMPPAP ppap = ppapRepository.findOne(pqmppapChecklist.getPpap());
                        comment.setParentObjectId(pqmppapChecklist.getPpap());
                        comment.setType(ppap.getObjectType().toString());

                    }
                } else if (changeFile != null) {
                    PLMChange plmChange = changeRepository.findOne(changeFile.getChange());
                    if (plmChange != null) {
                        if (plmChange.getChangeType().equals(ChangeType.ECO)) {
                            PLMECO plmeco = ecoRepository.findById(plmChange.getId());
                            if (plmeco != null) {
                                comment.setType("ECO");
                                comment.setParentObjectId(plmeco.getId());
                            }
                        } else if (plmChange.getChangeType().equals(ChangeType.DCO)) {
                            PLMDCO plmdco = dcoRepository.findOne(plmChange.getId());
                            comment.setParentObjectId(plmdco.getId());
                            comment.setType("DCO");
                        } else if (plmChange.getChangeType().equals(ChangeType.DCR)) {
                            PLMDCR plmdcr = dcrRepository.findOne(plmChange.getId());
                            comment.setParentObjectId(plmdcr.getId());
                            comment.setType("DCR");
                        } else if (plmChange.getChangeType().equals(ChangeType.ECR)) {
                            PLMECR plmecr = ecrRepository.findOne(plmChange.getId());
                            comment.setType("ECR");
                            comment.setParentObjectId(plmecr.getId());
                        } else if (plmChange.getChangeType().equals(ChangeType.MCO)) {
                            PLMMCO plmmco = mcoRepository.findOne(plmChange.getId());
                            comment.setType("MCO");
                            comment.setParentObjectId(plmmco.getId());
                        } else if (plmChange.getChangeType().equals(ChangeType.DEVIATION)) {
                            PLMVariance plmVariance = varianceRepository.findOne(plmChange.getId());
                            comment.setType("DEVIATION");
                            comment.setParentObjectId(plmVariance.getId());
                        } else if (plmChange.getChangeType().equals(ChangeType.WAIVER)) {
                            PLMVariance plmVariance = varianceRepository.findOne(plmChange.getId());
                            comment.setType("WAIVER");
                            comment.setParentObjectId(plmVariance.getId());
                        }

                    }
                } else if (projectFile != null) {
                    comment.setType(PLMObjectType.PROJECT.toString());
                    comment.setParentObjectId(projectFile.getProject());
                } else if (programFile != null) {
                    comment.setType(PLMObjectType.PROGRAM.toString());
                    comment.setParentObjectId(programFile.getProgram());
                } else if (activityFile != null) {

                    comment.setType(PLMObjectType.PROJECTACTIVITY.toString());
                    comment.setParentObjectId(activityFile.getActivity());

                } else if (manufacturerFile != null) {

                    comment.setType(PLMObjectType.MANUFACTURER.toString());
                    comment.setParentObjectId(manufacturerFile.getManufacturer());

                } else if (manufacturerPartFile != null) {
                    PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(manufacturerPartFile.getManufacturerPart());
                    comment.setType(PLMObjectType.MANUFACTURERPART.toString());
                    //comment.setParentId(manufacturerPart.getManufacturer());
                    comment.setParentObjectId(manufacturerPartFile.getManufacturerPart());

                } else if (inspectionPlanFile != null) {

                    comment.setType(PLMObjectType.INSPECTIONPLAN.toString());
                    comment.setParentObjectId(inspectionPlanFile.getInspectionPlan());

                } else if (inspectionFile != null) {

                    comment.setType(PLMObjectType.INSPECTION.toString());
                    comment.setParentObjectId(inspectionFile.getInspection());

                } else if (problemReportFile != null) {

                    comment.setType(PLMObjectType.PROBLEMREPORT.toString());
                    comment.setParentObjectId(problemReportFile.getProblemReport());

                } else if (ncrFile != null) {

                    comment.setType(PLMObjectType.NCR.toString());
                    comment.setParentObjectId(ncrFile.getNcr());

                } else if (qcrFile != null) {

                    comment.setType(PLMObjectType.QCR.toString());
                    comment.setParentObjectId(qcrFile.getQcr());

                } else if (mesObjectFile != null) {

                    comment.setType(PLMObjectType.MESOBJECT.toString());
                    comment.setParentObjectId(mesObjectFile.getObject());
                } else if (mroObjectFile != null) {

                    comment.setType(PLMObjectType.MROOBJECT.toString());
                    comment.setParentObjectId(mroObjectFile.getObject());

                } else if (pgcObjectFile != null) {
                    PGCObject pgcObject = pgcObjectRepository.findOne(pgcObjectFile.getObject());
                    comment.setType(pgcObject.getObjectType().toString());
                    comment.setParentObjectId(pgcObject.getId());

                } else if (plmSupplierFile != null) {

                    comment.setType(PLMObjectType.SUPPLIER.toString());
                    comment.setParentObjectId(plmSupplierFile.getSupplier());

                } else if (mesBopFile != null) {

                    comment.setType(PLMObjectType.BOP.toString());
                    comment.setParentObjectId(mesBopFile.getBop());

                } else if (requirementDocumentFile != null) {
                    comment.setType(PLMObjectType.REQUIREMENTDOCUMENT.toString());
                    comment.setParentObjectId(requirementDocumentFile.getDocumentRevision().getId());

                } else if (requirementFile != null) {

                    comment.setType(PLMObjectType.REQUIREMENT.toString());
                    PLMRequirementVersion version = requirementVersionRepository.findOne(requirementFile.getRequirement().getId());
                    PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findByRequirementVersion(version);
                    if (documentChildren != null) {
                        comment.setParentObjectId(documentChildren.getId());
                    } else {
                        comment.setParentObjectId(version.getId());
                    }

                } else if (customerFile != null) {

                    comment.setType(PLMObjectType.CUSTOMER.toString());
                    comment.setParentObjectId(customerFile.getCustomer());

                } else if (plmNprFile != null) {

                    comment.setType(PLMObjectType.PLMNPR.toString());
                    comment.setParentObjectId(plmNprFile.getNpr());

                } else if (customObjectFile != null) {

                    comment.setType(PLMObjectType.CUSTOMOBJECT.toString());
                    comment.setParentObjectId(customObjectFile.getObject());

                } else if (supplierAuditFile != null) {

                    comment.setType(PLMObjectType.SUPPLIERAUDIT.toString());
                    comment.setParentObjectId(supplierAuditFile.getSupplierAudit());

                } else if (plmDocument != null) {

                    comment.setType(PLMObjectType.DOCUMENT.toString());
                    comment.setParentObjectId(plmDocument.getId());

                } else if (mbomFile != null) {

                    comment.setType(MESEnumObject.MBOM.toString());
                    comment.setParentObjectId(mbomFile.getMbomRevision());

                } else if (mesbopOperationFile != null) {
                    comment.setType(MESEnumObject.BOPROUTEOPERATION.toString());
                    comment.setParentObjectId(mesbopOperationFile.getBopOperation());
                    MESBOPRouteOperation routeItem = routeItemRepository.findOne(mesbopOperationFile.getBopOperation());
                    if (routeItem != null) {
                        comment.setParentId(routeItem.getBop());
                    }

                }
            }

        }
        return comment;

    }
}