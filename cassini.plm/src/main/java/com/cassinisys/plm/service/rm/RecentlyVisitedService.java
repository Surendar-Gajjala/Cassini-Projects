package com.cassinisys.plm.service.rm;

import com.cassinisys.platform.filtering.CustomObjectCriteria;
import com.cassinisys.platform.filtering.TagCriteria;
import com.cassinisys.platform.filtering.TagPredicateBuilder;
import com.cassinisys.platform.model.common.QTag;
import com.cassinisys.platform.model.common.Tag;
import com.cassinisys.platform.model.common.TagDto;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.model.custom.CustomObjectFile;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.TagRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.custom.CustomObjectFileRepository;
import com.cassinisys.platform.repo.custom.CustomObjectRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.custom.CustomObjectService;
import com.cassinisys.platform.service.security.SecurityPermissionService;
import com.cassinisys.plm.filtering.*;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.cm.dto.DCODto;
import com.cassinisys.plm.model.cm.dto.DCRDto;
import com.cassinisys.plm.model.cm.dto.ECRDto;
import com.cassinisys.plm.model.dto.PersonsDto;
import com.cassinisys.plm.model.dto.VarianceDto;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mes.dto.JigsFixtureDto;
import com.cassinisys.plm.model.mes.dto.MaterialDto;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.mro.dto.MaintenancePlanDto;
import com.cassinisys.plm.model.mro.dto.WorkOrderDto;
import com.cassinisys.plm.model.pdm.PDMAssembly;
import com.cassinisys.plm.model.pdm.PDMDrawing;
import com.cassinisys.plm.model.pdm.PDMPart;
import com.cassinisys.plm.model.pdm.PDMVault;
import com.cassinisys.plm.model.pgc.*;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.ObjectCountsDto;
import com.cassinisys.plm.model.plm.dto.TopSearchDto;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.*;
import com.cassinisys.plm.model.req.*;
import com.cassinisys.plm.model.rm.*;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mfr.*;
import com.cassinisys.plm.repo.mro.*;
import com.cassinisys.plm.repo.pdm.*;
import com.cassinisys.plm.repo.pgc.*;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.req.*;
import com.cassinisys.plm.repo.rm.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.WorkflowDefinitionMasterRepository;
import com.cassinisys.plm.service.cm.*;
import com.cassinisys.plm.service.mes.*;
import com.cassinisys.plm.service.mfr.ManufacturerPartService;
import com.cassinisys.plm.service.mfr.ManufacturerService;
import com.cassinisys.plm.service.mfr.SupplierService;
import com.cassinisys.plm.service.mro.*;
import com.cassinisys.plm.service.pdm.PDMService;
import com.cassinisys.plm.service.pdm.PDMVaultService;
import com.cassinisys.plm.service.pgc.DeclarationService;
import com.cassinisys.plm.service.pgc.PGCSpecificationService;
import com.cassinisys.plm.service.pgc.SubstanceService;
import com.cassinisys.plm.service.plm.ItemFileService;
import com.cassinisys.plm.service.plm.ItemService;
import com.cassinisys.plm.service.plm.NprService;
import com.cassinisys.plm.service.pm.ProgramService;
import com.cassinisys.plm.service.pm.ProjectService;
import com.cassinisys.plm.service.pqm.*;
import com.cassinisys.plm.service.req.ReqDocumentService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subra on 21-09-2018.
 */
@Service
public class RecentlyVisitedService implements CrudService<RecentlyVisited, Integer> {

    @Autowired
    private RecentlyVisitedRepository recentlyVisitedRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private DCRRepository dcrRepository;
    @Autowired
    private ECRRepository ecrRepository;
    @Autowired
    private MCORepository mcoRepository;
    @Autowired
    private VarianceRepository varianceRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private WorkflowDefinitionMasterRepository workflowDefinitionMasterRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private GlossaryRepository glossaryRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private WbsElementRepository wbsElementRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectTemplateRepository projectTemplateRepository;
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
    private RmObjectFileRepository rmObjectFileRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ManufacturerFileRepository manufacturerFileRepository;
    @Autowired
    private ManufacturerPartFileRepository manufacturerPartFileRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private RmObjectRepository objectRepository;
    @Autowired
    private SpecificationRepository specificationRepository;

    @Autowired
    private SpecRequirementRepository specRequirementRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ChangeRepository changeRepository;
    @Autowired
    private InspectionPlanRepository inspectionPlanRepository;
    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;
    @Autowired
    private InspectionRepository inspectionRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private NCRRepository ncrRepository;
    @Autowired
    private QCRRepository qcrRepository;
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
    private ProgramService programService;
    @Autowired
    private ECOService ecoService;
    @Autowired
    private MCOService mcoService;
    @Autowired
    private ECRService ecrService;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private DCRService dcrService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private RecentlyVisitedService recentlyVisitedService;
    @Autowired
    private ReqDocumentService reqDocumentService;
    @Autowired
    private PLMVarianceService varianceService;
    @Autowired
    private ItemFileService itemFileService;
    @Autowired
    private ItemPredicateBuilder predicateBuilder;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
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
    private RmObjectRepository rmObjectRepository;
    @Autowired
    private PlantService plantService;
    @Autowired
    private NprService nprService;
    @Autowired
    private AssemblyLineService assemblyLineService;
    @Autowired
    private WorkCenterService workCenterService;
    @Autowired
    private MachineService machineService;
    @Autowired
    private InstrumentService instrumentService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private ToolService toolService;
    @Autowired
    private JigsFixtureService jigsFixtureService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private ManpowerService manpowerService;
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private OperationService operationService;
    @Autowired
    private MESObjectFileRepository mesObjectFileRepository;
    @Autowired
    private MROObjectFileRepository mroObjectFileRepository;
    @Autowired
    private PGCObjectFileRepository pgcObjectFileRepository;
    @Autowired
    private SupplierFileRepository supplierFileRepository;
    @Autowired
    private MESObjectRepository mesObjectRepository;
    @Autowired
    private MROObjectRepository mroObjectRepository;
    @Autowired
    private PGCObjectRepository pgcObjectRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private ManufacturerPartService manufacturerPartService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierAuditService supplierAuditService;
    @Autowired
    private AssetService assetService;
    @Autowired
    private MeterService meterService;
    @Autowired
    private SparePartsService sparePartsService;
    @Autowired
    private MaintenancePlanService maintenancePlanService;
    @Autowired
    private MROMaintenancePlanRepository mroMaintenancePlanRepository;
    @Autowired
    private WorkRequestService workRequestService;
    @Autowired
    private MROWorkRequestRepository mroWorkRequestRepository;
    @Autowired
    private MROWorkOrderRepository mroWorkOrderRepository;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private SubstanceService substanceService;
    @Autowired
    private PGCSpecificationService pgcSpecificationService;
    @Autowired
    private DeclarationService declarationService;
    @Autowired
    private PDMService pdmService;
    @Autowired
    private PDMVaultService pdmVaultService;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private PQMCustomerRepository pqmCustomerRepository;
    @Autowired
    private SupplierContactRepository supplierContactRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMRequirementDocumentFileRepository requirementDocumentFileRepository;
    @Autowired
    private PLMRequirementFileRepository requirementFileRepository;
    @Autowired
    private PLMRequirementDocumentRepository requirementDocumentRepository;
    @Autowired
    private CustomerFileRepository customerFileRepository;
    @Autowired
    private NprFileRepository nprFileRepository;
    @Autowired
    private NprRepository nprRepository;
    @Autowired
    private PLMRequirementRepository requirementRepository;
    @Autowired
    private PLMRequirementVersionRepository requirementVersionRepository;
    @Autowired
    private PGCSpecificationRepository pgcSpecificationRepository;
    @Autowired
    private PGCDeclarationRepository declarationRepository;
    @Autowired
    private MROAssetRepository mroAssetRepository;
    @Autowired
    private ProductionOrderRepository productionOrderRepository;
    @Autowired
    private PDMVaultRepository pdmVaultRepository;
    @Autowired
    private PDMAssemblyRepository pdmAssemblyRepository;
    @Autowired
    private PDMPartRepository pdmPartRepository;
    @Autowired
    private PDMDrawingRepository pdmDrawingRepository;
    @Autowired
    private CustomObjectService customObjectService;
    @Autowired
    private MESPlantRepository mesPlantRepository;
    @Autowired
    private MESAssemblyLineRepository mesAssemblyLineRepository;
    @Autowired
    private MESWorkCenterRepository workCenterRepository;
    @Autowired
    private MESEquipmentRepository mesEquipmentRepository;
    @Autowired
    private MESInstrumentRepository mesInstrumentRepository;
    @Autowired
    private MESToolRepository mesToolRepository;
    @Autowired
    private MESJigsFixtureRepository mesJigsFixtureRepository;
    @Autowired
    private MESMachineRepository mesMachineRepository;
    @Autowired
    private MESMaterialRepository mesMaterialRepository;
    @Autowired
    private MESShiftRepository mesShiftRepository;
    @Autowired
    private MESOperationRepository mesOperationRepository;
    @Autowired
    private MESManpowerRepository mesManpowerRepository;
    @Autowired
    private PGCSubstanceRepository pgcSubstanceRepository;
    @Autowired
    private MROMeterRepository mroMeterRepository;
    @Autowired
    private MROSparePartRepository mroSparePartRepository;
    @Autowired
    private SecurityPermissionService securityPermissionService;
    @Autowired
    private PLMDocumentRepository plmDocumentRepository;
    @Autowired
    private CustomObjectFileRepository customObjectFileRepository;
    @Autowired
    private CustomObjectRepository customObjectRepository;
    @Autowired
    private ProductInspectionPlanRepository productInspectionPlanRepository;
    @Autowired
    private MaterialInspectionPlanRepository materialInspectionPlanRepository;
    @Autowired
    private ItemInspectionRepository itemInspectionRepository;
    @Autowired
    private MaterialInspectionRepository materialInspectionRepository;
    @Autowired
    private PDMFileVersionRepository pdmFileVersionRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagPredicateBuilder tagPredicateBuilder;
    @Autowired
    private MfrPartInspectionReportRepository mfrPartInspectionReportRepository;
    @Autowired
    private MBOMFileRepository mbomFileRepository;
    @Autowired
    private MESMBOMRevisionRepository mbomRevisionRepository;
    @Autowired
    private MESMBOMRepository mbomRepository;
    @Autowired
    private PPAPChecklistRepository ppapChecklistRepository;
    @Autowired
    private PPAPRepository ppapRepository;
    @Autowired
    private SupplierAuditFileRepository supplierAuditFileRepository;
    @Autowired
    private SupplierAuditRepository supplierAuditRepository;
    @Autowired
    private MESMBOMRepository mesmbomRepository;
    @Autowired
    private ManpowerContactRepository manpowerContactRepository;
    @Autowired
    private ProgramRepository programRepository;

    @Override
    @Transactional
    public RecentlyVisited create(RecentlyVisited recentlyVisited) {
        List<RecentlyVisited> recentlyVisitedList = recentlyVisitedRepository.findByPersonOrderByVisitedDateAsc(recentlyVisited.getPerson());
        RecentlyVisited existRecentlyVisited = recentlyVisitedRepository.
                findByObjectIdAndObjectTypeAndPerson(recentlyVisited.getObjectId(), recentlyVisited.getObjectType(), recentlyVisited.getPerson());
        if (existRecentlyVisited == null) {
            recentlyVisited.setVisitedDate(new Date());
            recentlyVisited = recentlyVisitedRepository.save(recentlyVisited);
        } else {
            existRecentlyVisited.setVisitedDate(new Date());
            existRecentlyVisited = recentlyVisitedRepository.save(existRecentlyVisited);
        }
        if (recentlyVisitedList.size() == 20 || recentlyVisitedList.size() > 20) {
            recentlyVisitedRepository.delete(recentlyVisitedList.get(0).getId());
        }
        deleteNotExistObjects(recentlyVisited.getPerson());
        return recentlyVisited;
    }

    @Override
    @Transactional
    public RecentlyVisited update(RecentlyVisited recentlyVisited) {
        return recentlyVisitedRepository.save(recentlyVisited);
    }

    @Override
    @Transactional
    public void delete(Integer visitedId) {
        recentlyVisitedRepository.delete(visitedId);
    }

    @Override
    @Transactional(readOnly = true)
    public RecentlyVisited get(Integer visitedId) {
        return recentlyVisitedRepository.findOne(visitedId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecentlyVisited> getAll() {
        return recentlyVisitedRepository.findAll();
    }

    private void deleteNotExistObjects(Integer personId) {
        List<RecentlyVisited> recentlyVisited = recentlyVisitedRepository.findByPersonOrderByVisitedDateAsc(personId);
        for (RecentlyVisited visited : recentlyVisited) {
            if (visited.getObjectType().equals("ITEM")) {
                visited.setType("ITEM");
                PLMItem item = itemRepository.findOne(visited.getObjectId());
                if (item == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }

            } else if (visited.getObjectType().equals("ITEMREVISION")) {
                visited.setType("ITEM");
                PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(visited.getObjectId());
                PLMItem item = itemRepository.findOne(plmItemRevision.getItemMaster());
                if (item == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }

            } else if (visited.getObjectType().equals("CHANGE")) {
                PLMECO plmeco = ecoRepository.findOne(visited.getObjectId());
                if (plmeco == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            } else if (visited.getObjectType().equals("PLMWORKFLOWDEFINITION")) {
                PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.getOne(visited.getObjectId());
                if (workflowDefinition == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            } else if (visited.getObjectType().equals("PROJECT")) {
                PLMProject plmProject = projectRepository.findOne(visited.getObjectId());
                if (plmProject == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            } else if (visited.getObjectType().equals("TEMPLATE")) {
                ProjectTemplate projectTemplate = projectTemplateRepository.findOne(visited.getObjectId());
                if (projectTemplate == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            } else if (visited.getObjectType().equals("TERMINOLOGY")) {
                PLMGlossary glossary = glossaryRepository.findOne(visited.getObjectId());
                if (glossary == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            } else if (visited.getObjectType().equals("MANUFACTURER")) {
                PLMManufacturer plmManufacturer = manufacturerRepository.findOne(visited.getObjectId());
                if (plmManufacturer == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            } else if (visited.getObjectType().equals("MANUFACTURERPART")) {
                PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(visited.getObjectId());
                if (manufacturerPart == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            } else if (visited.getObjectType().equals("SPECIFICATION")) {
                RmObject rmObject = objectRepository.findOne(visited.getObjectId());
                if (rmObject == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            } /*else if (visited.getObjectType().equals("REQUIREMENT")) {
                Requirement requirement = requirementRepository.findOne(visited.getObjectId());
                if (requirement == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            }*/
            if (visited.getObjectType().equals(PLMObjectType.ECR.toString())) {
                PLMECR plmecr = ecrRepository.findOne(visited.getObjectId());
                if (plmecr == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.DCO.toString())) {
                PLMDCO plmdco = dcoRepository.findOne(visited.getObjectId());
                if (plmdco == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.DCR.toString())) {
                PLMDCR plmdcr = dcrRepository.findOne(visited.getObjectId());
                if (plmdcr == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.MCO.toString())) {
                PLMMCO plmmco = mcoRepository.findOne(visited.getObjectId());
                if (plmmco == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.VARIANCE.toString())) {
                PLMVariance variance = varianceRepository.findOne(visited.getObjectId());
                if (variance == null) {
                    recentlyVisitedRepository.delete(visited.getId());
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<RecentlyVisited> getAllRecentlyVisited(Integer personId, Pageable pageable) {
        Page<RecentlyVisited> recentlyVisited = recentlyVisitedRepository.findByPersonOrderByVisitedDateDesc(personId, pageable);
        recentlyVisited.getContent().forEach(visited -> {
            if (visited.getObjectType().equals("ITEM")) {
                visited.setType("ITEM");
                PLMItem item = itemRepository.findOne(visited.getObjectId());
                if (item != null) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
                    visited.setName(item.getItemName() + "(" + itemRevision.getRevision() + ")");
                    visited.setItem(item);
                    visited.setConfigurable(item.getConfigurable());
                    visited.setConfigured(item.getConfigured());
                    visited.setHasBom(itemRevision.getHasBom());
                    visited.setHasFiles(itemRevision.getHasFiles());
                }

            } else if (visited.getObjectType().equals("ITEMREVISION")) {
                visited.setType("ITEM");
                PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(visited.getObjectId());
                PLMItem item = itemRepository.findOne(plmItemRevision.getItemMaster());
                if (item != null) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(visited.getObjectId());
                    visited.setName(item.getItemName() + "(" + plmItemRevision.getRevision() + ")");
                    visited.setItem(item);
                    visited.setConfigurable(item.getConfigurable());
                    visited.setConfigured(item.getConfigured());
                    visited.setHasBom(itemRevision.getHasBom());
                    visited.setHasFiles(itemRevision.getHasFiles());
                }

            } else if (visited.getObjectType().equals("CHANGE")) {
                PLMECO plmeco = ecoRepository.findOne(visited.getObjectId());
                if (plmeco != null) {
                    visited.setType("ECO");
                    visited.setName(plmeco.getTitle());
                }
            } else if (visited.getObjectType().equals("PLMWORKFLOWDEFINITION")) {
                visited.setType("WORKFLOW");
                visited.setName(workFlowDefinitionRepository.findOne(visited.getObjectId()).getMaster().getNumber());
            } else if (visited.getObjectType().equals("PROJECT")) {
                visited.setType("PROJECT");
                visited.setName(projectRepository.findOne(visited.getObjectId()).getName());
            } else if (visited.getObjectType().equals("TEMPLATE")) {
                visited.setType("TEMPLATE");
                visited.setName(projectTemplateRepository.findOne(visited.getObjectId()).getName());
            } else if (visited.getObjectType().equals("TERMINOLOGY")) {
                visited.setType("TERMINOLOGY");
                PLMGlossary glossary = glossaryRepository.findOne(visited.getObjectId());
                visited.setName(glossary.getDefaultDetail().getName() + " (" + glossary.getRevision() + ")");
            } else if (visited.getObjectType().equals("MANUFACTURER")) {
                visited.setType("MANUFACTURER");
                visited.setName(manufacturerRepository.findOne(visited.getObjectId()).getName());
            } else if (visited.getObjectType().equals("MANUFACTURERPART")) {
                visited.setType("MANUFACTURERPART");
                PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(visited.getObjectId());
                visited.setName(manufacturerPartRepository.findOne(visited.getObjectId()).getPartNumber());
                visited.setManufacturer(manufacturerRepository.findOne(manufacturerPart.getManufacturer()));
            } else if (visited.getObjectType().equals("SPECIFICATION")) {
                RmObject rmObject = objectRepository.findOne(visited.getObjectId());
                if (rmObject.getObjectType().toString().equals("SPECIFICATION")) {
                    Specification specification = specificationRepository.findOne(rmObject.getId());
                    if (specification != null) {
                        visited.setType("SPECIFICATION");
                        visited.setName(specification.getName() + "(" + specification.getRevision() + ")");

                    }
                }

            } /*else if (visited.getObjectType().equals("REQUIREMENT")) {
                RmObject rmObject = objectRepository.findOne(visited.getObjectId());
                if (rmObject.getObjectType().toString().equals("REQUIREMENT")) {
                    Requirement requirement = requirementRepository.findOne(rmObject.getId());
                    if (requirement != null) {
                        visited.setType("REQUIREMENT");
                        visited.setName(requirement.getName() + "(" + requirement.getVersion() + ")");
                    }
                }
            }*/ else if (visited.getObjectType().equals(PLMObjectType.PRODUCTINSPECTIONPLAN.toString()) || visited.getObjectType().equals(PLMObjectType.MATERIALINSPECTIONPLAN.toString())) {
                PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(visited.getObjectId());
                if (inspectionPlanRevision != null) {
                    PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
                    if (inspectionPlan != null) {
                        visited.setType("INSPECTIONPLAN");
                        visited.setName(inspectionPlan.getName() + "(" + inspectionPlanRevision.getRevision() + ")");
                    }
                }
            } else if (visited.getObjectType().equals(PLMObjectType.ITEMINSPECTION.toString()) || visited.getObjectType().equals(PLMObjectType.MATERIALINSPECTION.toString())) {
                PQMInspection inspection = inspectionRepository.findOne(visited.getObjectId());
                if (inspection != null) {
                    visited.setType("INSPECTION");
                    visited.setName(inspection.getInspectionNumber());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.PROBLEMREPORT.toString())) {
                PQMProblemReport problemReport = problemReportRepository.findOne(visited.getObjectId());
                if (problemReport != null) {
                    visited.setType("PROBLEMREPORT");
                    visited.setName(problemReport.getPrNumber());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.NCR.toString())) {
                PQMNCR pqmncr = ncrRepository.findOne(visited.getObjectId());
                if (pqmncr != null) {
                    visited.setType("NCR");
                    visited.setName(pqmncr.getNcrNumber());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.QCR.toString())) {
                PQMQCR pqmqcr = qcrRepository.findOne(visited.getObjectId());
                if (pqmqcr != null) {
                    visited.setType("QCR");
                    visited.setName(pqmqcr.getQcrNumber());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.ECR.toString())) {
                PLMECR plmecr = ecrRepository.findOne(visited.getObjectId());
                if (plmecr != null) {
                    visited.setType("ECR");
                    visited.setName(plmecr.getCrNumber());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.DCO.toString())) {
                PLMDCO plmdco = dcoRepository.findOne(visited.getObjectId());
                if (plmdco != null) {
                    visited.setType("DCO");
                    visited.setName(plmdco.getDcoNumber());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.DCR.toString())) {
                PLMDCR plmdcr = dcrRepository.findOne(visited.getObjectId());
                if (plmdcr != null) {
                    visited.setType("DCR");
                    visited.setName(plmdcr.getCrNumber());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.MCO.toString())) {
                PLMMCO plmmco = mcoRepository.findOne(visited.getObjectId());
                if (plmmco != null) {
                    visited.setType("MCO");
                    visited.setName(plmmco.getMcoNumber());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.VARIANCE.toString())) {
                PLMVariance variance = varianceRepository.findOne(visited.getObjectId());
                if (variance != null) {
                    if (variance.getChangeType().equals(ChangeType.DEVIATION)) {
                        visited.setType(ChangeType.DEVIATION.toString());
                    } else if (variance.getChangeType().equals(ChangeType.WAIVER)) {
                        visited.setType(ChangeType.WAIVER.toString());
                    }
                    visited.setName(variance.getVarianceNumber());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.PLANT.toString())) {
                MESPlant plant = plantService.get(visited.getObjectId());
                if (plant != null) {
                    visited.setType(PLMObjectType.PLANT.toString());
                    visited.setName(plant.getNumber());
                }
            } else if (visited.getObjectType().equals(PLMObjectType.WORKCENTER.toString())) {
                MESWorkCenter workCenter = workCenterService.get(visited.getObjectId());
                if (workCenter != null) {
                    visited.setType(PLMObjectType.WORKCENTER.toString());
                    visited.setName(workCenter.getNumber());
                }
            } else if (visited.getObjectType().equals(MESEnumObject.MACHINE.toString())) {
                MESMachine machine = machineService.get(visited.getObjectId());
                if (machine != null) {
                    visited.setType(MESEnumObject.MACHINE.toString());
                    visited.setName(machine.getNumber());
                }
            } else if (visited.getObjectType().equals(MESEnumObject.EQUIPMENT.toString())) {
                MESEquipment equipment = equipmentService.get(visited.getObjectId());
                if (equipment != null) {
                    visited.setType(MESEnumObject.EQUIPMENT.toString());
                    visited.setName(equipment.getNumber());
                }
            } else if (visited.getObjectType().equals(MESEnumObject.INSTRUMENT.toString())) {
                MESInstrument instrument = instrumentService.get(visited.getObjectId());
                if (instrument != null) {
                    visited.setType(MESEnumObject.INSTRUMENT.toString());
                    visited.setName(instrument.getNumber());
                }
            } else if (visited.getObjectType().equals(MESEnumObject.TOOL.toString())) {
                MESTool tool = toolService.get(visited.getObjectId());
                if (tool != null) {
                    visited.setType(MESEnumObject.TOOL.toString());
                    visited.setName(tool.getNumber());
                }
            } else if (visited.getObjectType().equals(MESEnumObject.JIGFIXTURE.toString())) {
                MESJigsFixture jigsFixture = jigsFixtureService.get(visited.getObjectId());
                if (jigsFixture != null) {
                    visited.setType(MESEnumObject.JIGFIXTURE.toString());
                    visited.setName(jigsFixture.getNumber());
                }
            } else if (visited.getObjectType().equals(MESEnumObject.MATERIAL.toString())) {
                MESMaterial material = materialService.get(visited.getObjectId());
                if (material != null) {
                    visited.setType(MESEnumObject.MATERIAL.toString());
                    visited.setName(material.getNumber());
                }
            } else if (visited.getObjectType().equals(MESEnumObject.MANPOWER.toString())) {
                MESManpower manpower = manpowerService.get(visited.getObjectId());
                if (manpower != null) {
                    visited.setType(MESEnumObject.MANPOWER.toString());
                    visited.setName(manpower.getNumber());
                }
            } else if (visited.getObjectType().equals(MESEnumObject.SHIFT.toString())) {
                MESShift shift = shiftService.get(visited.getObjectId());
                if (shift != null) {
                    visited.setType(MESEnumObject.SHIFT.toString());
                    visited.setName(shift.getNumber());
                }
            } else if (visited.getObjectType().equals(MESEnumObject.OPERATION.toString())) {
                MESOperation operation = operationService.get(visited.getObjectId());
                if (operation != null) {
                    visited.setType(MESEnumObject.OPERATION.toString());
                    visited.setName(operation.getNumber());
                }
            }
        });

        return recentlyVisited;
    }

    @Transactional(readOnly = true)
    public Page<CommonFileDto> getFreeTextSearchFiles(String searchText, Pageable pageable) {
        List<CommonFileDto> commonFileDtoList = new ArrayList<>();
        Page<PLMFile> plmFiles = fileRepository.findByLatestTrueAndNameContainsIgnoreCaseAndFileTypeAndObjectTypeNot(searchText, "FILE", ObjectType.valueOf("DOCUMENT"), pageable);
        if (plmFiles.getContent().size() > 0) {
            for (PLMFile plmFile : plmFiles) {
                CommonFileDto commonFileDto = new CommonFileDto();
                PLMItemFile itemFile = itemFileRepository.findOne(plmFile.getId());
                PLMChangeFile changeFile = changeFileRepository.findOne(plmFile.getId());
                PLMProjectFile projectFile = projectFileRepository.findOne(plmFile.getId());
                PLMActivityFile activityFile = activityFileRepository.findOne(plmFile.getId());
                PLMTaskFile taskFile = taskFileRepository.findOne(plmFile.getId());
                PLMGlossaryFile glossaryFile = glossaryFileRepository.findOne(plmFile.getId());
                PLMManufacturerFile manufacturerFile = manufacturerFileRepository.findOne(plmFile.getId());
                PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileRepository.findOne(plmFile.getId());
                PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(plmFile.getId());
                PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(plmFile.getId());
                PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(plmFile.getId());
                PQMNCRFile ncrFile = ncrFileRepository.findOne(plmFile.getId());
                PQMQCRFile qcrFile = qcrFileRepository.findOne(plmFile.getId());
                RmObjectFile rmObjectFile = rmObjectFileRepository.findOne(plmFile.getId());
                MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(plmFile.getId());
                MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(plmFile.getId());
                PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(plmFile.getId());
                PLMSupplierFile plmSupplierFile = supplierFileRepository.findOne(plmFile.getId());
                PLMRequirementDocumentFile requirementDocumentFile = requirementDocumentFileRepository.findOne(plmFile.getId());
                PLMRequirementFile requirementFile = requirementFileRepository.findOne(plmFile.getId());
                PQMCustomerFile customerFile = customerFileRepository.findOne(plmFile.getId());
                PLMNprFile plmNprFile = nprFileRepository.findOne(plmFile.getId());
                CustomObjectFile customObjectFile = customObjectFileRepository.findOne(plmFile.getId());
                PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(plmFile.getId());

                if (plmFile.getObjectType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
                    PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(plmFile.getId());
                    if (mfrPartInspectionReport != null) {
                        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(mfrPartInspectionReport.getManufacturerPart());
                        commonFileDto.setObjectId(mfrPartInspectionReport.getId());
                        commonFileDto.setParentObjectId(mfrPartInspectionReport.getManufacturerPart());
                        commonFileDto.setName(manufacturerPart.getPartName());
                        commonFileDto.setFile(plmFile);
                        commonFileDto.setType(manufacturerPart.getObjectType().toString());
                        commonFileDto.setParentObjectType(PLMObjectType.MANUFACTURERPART.name());
                        commonFileDtoList.add(commonFileDto);
                    }
                } else if (plmFile.getObjectType().name().equals(PLMObjectType.MBOMFILE.name())) {
                    MESMBOMFile mesmbomFile = mbomFileRepository.findOne(plmFile.getId());
                    if (mesmbomFile != null) {
                        MESMBOMRevision mesmbomRevision = mbomRevisionRepository.findOne(mesmbomFile.getMbomRevision());
                        MESMBOM mesmbom = mbomRepository.findOne(mesmbomRevision.getMaster());
                        commonFileDto.setObjectId(mesmbomFile.getId());
                        commonFileDto.setParentObjectId(mesmbomFile.getMbomRevision());
                        commonFileDto.setName(mesmbom.getName());
                        commonFileDto.setFile(plmFile);
                        commonFileDto.setType(PLMObjectType.MBOMREVISION.name());
                        commonFileDto.setParentObjectType(PLMObjectType.MBOMREVISION.name());
                        commonFileDtoList.add(commonFileDto);
                    }
                } else if (plmFile.getObjectType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
                    PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(plmFile.getId());
                    if (pqmppapChecklist != null) {
                        PQMPPAP ppap = ppapRepository.findOne(pqmppapChecklist.getPpap());
                        commonFileDto.setObjectId(pqmppapChecklist.getId());
                        commonFileDto.setParentObjectId(pqmppapChecklist.getPpap());
                        commonFileDto.setName(ppap.getName());
                        commonFileDto.setFile(plmFile);
                        commonFileDto.setType(ppap.getObjectType().toString());
                        commonFileDto.setParentObjectType(PLMObjectType.PPAP.name());
                        commonFileDtoList.add(commonFileDto);
                    }
                } else if (itemFile != null) {
                    PLMItem item = itemRepository.findOne(itemFile.getItem().getItemMaster());
                    if (itemFile.getFileType().equals("FILE")) {
                        commonFileDto.setObjectId(item.getId());
                        commonFileDto.setParentObjectId(itemFile.getItem().getId());
                        commonFileDto.setName(item.getItemName());
                        commonFileDto.setFile(plmFile);
                        commonFileDto.setType(item.getObjectType().toString());
                        commonFileDto.setParentObjectType(itemFile.getItem().getObjectType().toString());
                        commonFileDto.setRevision(itemFile.getItem().getRevision());
                        commonFileDtoList.add(commonFileDto);
                    }
                } else if (changeFile != null) {
                    PLMChange plmChange = changeRepository.findOne(changeFile.getChange());
                    if (plmChange != null) {
                        if (plmChange.getChangeType().equals(ChangeType.ECO)) {
                            PLMECO plmeco = ecoRepository.findById(plmChange.getId());
                            if (plmeco != null) {
                                commonFileDto.setName(plmeco.getTitle());
                                commonFileDto.setType("ECO");
                                commonFileDto.setParentObjectType(PLMObjectType.CHANGE.toString());
                            }
                        } else if (plmChange.getChangeType().equals(ChangeType.DCO)) {
                            PLMDCO plmdco = dcoRepository.findOne(plmChange.getId());
                            commonFileDto.setName(plmdco.getTitle());
                            commonFileDto.setType("DCO");
                            commonFileDto.setParentObjectType(PLMObjectType.DCO.toString());
                        } else if (plmChange.getChangeType().equals(ChangeType.DCR)) {
                            PLMDCR plmdcr = dcrRepository.findOne(plmChange.getId());
                            commonFileDto.setName(plmdcr.getTitle());
                            commonFileDto.setType("DCR");
                            commonFileDto.setParentObjectType(PLMObjectType.DCR.toString());
                        } else if (plmChange.getChangeType().equals(ChangeType.ECR)) {
                            PLMECR plmecr = ecrRepository.findOne(plmChange.getId());
                            commonFileDto.setName(plmecr.getTitle());
                            commonFileDto.setType("ECR");
                            commonFileDto.setParentObjectType(PLMObjectType.ECR.toString());
                        } else if (plmChange.getChangeType().equals(ChangeType.MCO)) {
                            PLMMCO plmmco = mcoRepository.findOne(plmChange.getId());
                            commonFileDto.setName(plmmco.getTitle());
                            commonFileDto.setType("MCO");
                            commonFileDto.setParentObjectType(PLMObjectType.MCO.toString());
                        } else if (plmChange.getChangeType().equals(ChangeType.DEVIATION)) {
                            PLMVariance plmVariance = varianceRepository.findOne(plmChange.getId());
                            commonFileDto.setName(plmVariance.getTitle());
                            commonFileDto.setType("DEVIATION");
                            commonFileDto.setParentObjectType(PLMObjectType.DEVIATION.toString());
                        } else if (plmChange.getChangeType().equals(ChangeType.WAIVER)) {
                            PLMVariance plmVariance = varianceRepository.findOne(plmChange.getId());
                            commonFileDto.setName(plmVariance.getTitle());
                            commonFileDto.setType("WAIVER");
                            commonFileDto.setParentObjectType(PLMObjectType.WAIVER.toString());
                        }
                    }
                    commonFileDto.setObjectId(changeFile.getChange());
                    commonFileDto.setFile(plmFile);
                    commonFileDtoList.add(commonFileDto);
                } else if (projectFile != null) {
                    PLMProject project = projectRepository.findOne(projectFile.getProject());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setObjectId(projectFile.getProject());
                    commonFileDto.setName(project.getName());
                    commonFileDto.setType(project.getObjectType().toString());
                    commonFileDto.setParentObjectType(project.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);

                } else if (activityFile != null) {
                    PLMActivity activity = activityRepository.findOne(activityFile.getActivity());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setObjectId(activity.getId());
                    commonFileDto.setName(activity.getName());
                    commonFileDto.setType(activity.getObjectType().toString());
                    commonFileDto.setParentObjectType(activity.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);

                } else if (taskFile != null) {
                    PLMTask task = taskRepository.findOne(taskFile.getTask());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setObjectId(task.getId());
                    commonFileDto.setName(task.getName());
                    commonFileDto.setType(task.getObjectType().toString());
                    commonFileDto.setParentObjectType(task.getObjectType().toString());
                    commonFileDto.setParentObjectId(task.getActivity());
                    commonFileDtoList.add(commonFileDto);

                } else if (glossaryFile != null) {
                    PLMGlossary glossary = glossaryRepository.findOne(glossaryFile.getGlossary());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(glossary.getDefaultDetail().getName());
                    commonFileDto.setObjectId(glossary.getId());
                    commonFileDto.setType(glossary.getObjectType().toString());
                    commonFileDto.setParentObjectType(glossary.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (manufacturerFile != null) {
                    PLMManufacturer manufacturer = manufacturerRepository.findOne(manufacturerFile.getManufacturer());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(manufacturer.getName());
                    commonFileDto.setObjectId(manufacturer.getId());
                    commonFileDto.setType(manufacturer.getObjectType().toString());
                    commonFileDto.setParentObjectType(manufacturer.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (manufacturerPartFile != null) {
                    PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(manufacturerPartFile.getManufacturerPart());
                    PLMManufacturer manufacturer = manufacturerRepository.findOne(manufacturerPart.getManufacturer());
                    commonFileDto.setParentObjectId(manufacturer.getId());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(manufacturerPart.getPartName());
                    commonFileDto.setObjectId(manufacturerPart.getId());
                    commonFileDto.setType(manufacturerPart.getObjectType().toString());
                    commonFileDto.setParentObjectId(manufacturer.getId());
                    commonFileDto.setParentObjectType(manufacturerPart.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (inspectionPlanFile != null) {
                    PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspectionPlanFile.getInspectionPlan());
                    PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
                    commonFileDto.setParentObjectId(inspectionPlan.getId());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(inspectionPlan.getName());
                    commonFileDto.setObjectId(inspectionPlan.getLatestRevision());
                    commonFileDto.setType(inspectionPlan.getObjectType().toString());
                    commonFileDto.setParentObjectId(inspectionPlan.getId());
                    commonFileDto.setParentObjectType(inspectionPlanRevision.getObjectType().toString());
                    commonFileDto.setRevision(inspectionPlanRevision.getRevision());
                    commonFileDtoList.add(commonFileDto);
                } else if (inspectionFile != null) {
                    PQMInspection inspection = inspectionRepository.findOne(inspectionFile.getInspection());
                    commonFileDto.setParentObjectId(inspection.getId());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(inspection.getInspectionNumber());
                    commonFileDto.setObjectId(inspection.getId());
                    commonFileDto.setType(inspection.getObjectType().toString());
                    commonFileDto.setParentObjectId(inspection.getId());
                    commonFileDto.setParentObjectType(inspection.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (problemReportFile != null) {
                    PQMProblemReport problemReport = problemReportRepository.findOne(problemReportFile.getProblemReport());
                    commonFileDto.setParentObjectId(problemReport.getId());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(problemReport.getProblem());
                    commonFileDto.setObjectId(problemReport.getId());
                    commonFileDto.setType(problemReport.getObjectType().toString());
                    commonFileDto.setParentObjectId(problemReport.getId());
                    commonFileDto.setParentObjectType(problemReport.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (ncrFile != null) {
                    PQMNCR pqmncr = ncrRepository.findOne(ncrFile.getNcr());
                    commonFileDto.setParentObjectId(pqmncr.getId());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(pqmncr.getTitle());
                    commonFileDto.setObjectId(pqmncr.getId());
                    commonFileDto.setType(pqmncr.getObjectType().toString());
                    commonFileDto.setParentObjectId(pqmncr.getId());
                    commonFileDto.setParentObjectType(pqmncr.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (qcrFile != null) {
                    PQMQCR pqmncr = qcrRepository.findOne(qcrFile.getQcr());
                    commonFileDto.setParentObjectId(pqmncr.getId());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(pqmncr.getTitle());
                    commonFileDto.setObjectId(pqmncr.getId());
                    commonFileDto.setType(pqmncr.getObjectType().toString());
                    commonFileDto.setParentObjectId(pqmncr.getId());
                    commonFileDto.setParentObjectType(pqmncr.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (rmObjectFile != null) {
                    RmObject rmObject = rmObjectRepository.findOne(rmObjectFile.getObject());
                    commonFileDto.setParentObjectId(rmObject.getId());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(rmObject.getName());
                    commonFileDto.setObjectId(rmObject.getId());
                    commonFileDto.setType(rmObject.getObjectType().toString());
                    commonFileDto.setParentObjectId(rmObject.getId());
                    commonFileDto.setParentObjectType(rmObject.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (mesObjectFile != null) {
                    MESObject mesObject = mesObjectRepository.findOne(mesObjectFile.getObject());
                    commonFileDto.setParentObjectId(mesObject.getId());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(mesObject.getName());
                    commonFileDto.setObjectId(mesObject.getId());
                    commonFileDto.setType(mesObject.getObjectType().toString());
                    commonFileDto.setParentObjectId(mesObject.getId());
                    commonFileDto.setParentObjectType(mesObject.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (mroObjectFile != null) {
                    MROObject mroObject = mroObjectRepository.findOne(mroObjectFile.getObject());
                    commonFileDto.setParentObjectId(mroObject.getId());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(mroObject.getName());
                    commonFileDto.setObjectId(mroObject.getId());
                    commonFileDto.setType(mroObject.getObjectType().toString());
                    commonFileDto.setParentObjectId(mroObject.getId());
                    commonFileDto.setParentObjectType(mroObject.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (pgcObjectFile != null) {
                    PGCObject pgcObject = pgcObjectRepository.findOne(pgcObjectFile.getObject());
                    commonFileDto.setParentObjectId(pgcObject.getId());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(pgcObject.getName());
                    commonFileDto.setObjectId(pgcObject.getId());
                    commonFileDto.setType(pgcObject.getObjectType().toString());
                    commonFileDto.setParentObjectId(pgcObject.getId());
                    commonFileDto.setParentObjectType(pgcObject.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (plmSupplierFile != null) {
                    PLMSupplier plmSupplier = supplierRepository.findOne(plmSupplierFile.getSupplier());
                    commonFileDto.setParentObjectId(plmSupplier.getId());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(plmSupplier.getName());
                    commonFileDto.setObjectId(plmSupplier.getId());
                    commonFileDto.setType(plmSupplier.getObjectType().toString());
                    commonFileDto.setParentObjectId(plmSupplier.getId());
                    commonFileDto.setParentObjectType(plmSupplier.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (requirementDocumentFile != null) {
                    commonFileDto.setParentObjectId(requirementDocumentFile.getDocumentRevision().getMaster().getId());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(requirementDocumentFile.getDocumentRevision().getMaster().getName());
                    commonFileDto.setObjectId(requirementDocumentFile.getDocumentRevision().getMaster().getId());
                    commonFileDto.setType(requirementDocumentFile.getDocumentRevision().getMaster().getObjectType().toString());
                    commonFileDto.setParentObjectId(requirementDocumentFile.getDocumentRevision().getMaster().getId());
                    commonFileDto.setParentObjectType(requirementDocumentFile.getDocumentRevision().getMaster().getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (requirementFile != null) {
                    commonFileDto.setParentObjectId(requirementFile.getRequirement().getId());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(requirementFile.getRequirement().getName());
                    commonFileDto.setObjectId(requirementFile.getRequirement().getId());
                    commonFileDto.setType(requirementFile.getRequirement().getObjectType().toString());
                    commonFileDto.setParentObjectId(requirementFile.getRequirement().getId());
                    commonFileDto.setParentObjectType(requirementFile.getRequirement().getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (customerFile != null) {
                    PQMCustomer pqmCustomer = pqmCustomerRepository.findOne(customerFile.getCustomer());
                    commonFileDto.setParentObjectId(customerFile.getCustomer());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(pqmCustomer.getName());
                    commonFileDto.setObjectId(customerFile.getCustomer());
                    commonFileDto.setType(pqmCustomer.getObjectType().toString());
                    commonFileDto.setParentObjectId(pqmCustomer.getId());
                    commonFileDto.setParentObjectType(pqmCustomer.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (plmNprFile != null) {
                    PLMNpr plmNpr = nprRepository.findOne(plmNprFile.getNpr());
                    commonFileDto.setParentObjectId(plmNprFile.getNpr());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(plmNpr.getNumber());
                    commonFileDto.setObjectId(plmNprFile.getNpr());
                    commonFileDto.setType(plmNpr.getObjectType().toString());
                    commonFileDto.setParentObjectId(plmNpr.getId());
                    commonFileDto.setParentObjectType(plmNpr.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (customObjectFile != null) {
                    CustomObject customObject = customObjectRepository.findOne(customObjectFile.getObject());
                    commonFileDto.setParentObjectId(customObjectFile.getObject());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(customObject.getNumber());
                    commonFileDto.setObjectId(customObjectFile.getObject());
                    commonFileDto.setType(customObject.getObjectType().toString());
                    commonFileDto.setParentObjectId(customObject.getId());
                    commonFileDto.setParentObjectType(customObject.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                } else if (supplierAuditFile != null) {
                    PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(supplierAuditFile.getSupplierAudit());
                    commonFileDto.setParentObjectId(supplierAuditFile.getSupplierAudit());
                    commonFileDto.setFile(plmFile);
                    commonFileDto.setName(supplierAudit.getNumber());
                    commonFileDto.setObjectId(supplierAuditFile.getSupplierAudit());
                    commonFileDto.setType(supplierAudit.getObjectType().toString());
                    commonFileDto.setParentObjectId(supplierAudit.getId());
                    commonFileDto.setParentObjectType(supplierAudit.getObjectType().toString());
                    commonFileDtoList.add(commonFileDto);
                }
            }
        }
        return new PageImpl<CommonFileDto>(commonFileDtoList, pageable, plmFiles.getTotalElements());
    }

    @Transactional
    public void updateFileNo() {
        List<PLMItemFile> files = itemFileRepository.findByFileNoIsNullAndLatestTrueOrderByIdAsc();
        List<PLMItemFile> itemFiles = itemFileRepository.findAll();
        List<PLMChangeFile> changeFiles = changeFileRepository.findByFileNoIsNullAndLatestTrueOrderByIdAsc();
        List<PLMManufacturerFile> manufacturerFiles = manufacturerFileRepository.findByFileNoIsNullAndLatestTrueOrderByIdAsc();
        List<PLMManufacturerPartFile> manufacturerPartFiles = manufacturerPartFileRepository.findByFileNoIsNullAndLatestTrueOrderByIdAsc();
        List<PLMProjectFile> projectFiles = projectFileRepository.findByFileNoIsNullAndLatestTrueOrderByIdAsc();
        List<PLMActivityFile> activityFiles = activityFileRepository.findByFileNoIsNullAndLatestTrueOrderByIdAsc();
        List<PLMTaskFile> taskFiles = taskFileRepository.findByFileNoIsNullAndLatestTrueOrderByIdAsc();
        List<PLMGlossaryFile> glossaryFiles = glossaryFileRepository.findByFileNoIsNullAndLatestTrueOrderByIdAsc();
        List<RmObjectFile> objectFiles = rmObjectFileRepository.findByFileNoIsNullAndLatestTrueOrderByIdAsc();
        if (files.size() > 0) {
            for (PLMItemFile file : files) {
                if (file.getFileNo() == null || file.getFileNo() == "" || file.getFileNo().isEmpty()) {
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    file.setFileNo(autoNumber1);
                    file = itemFileRepository.save(file);
                    List<PLMItemFile> oldVersionFiles = itemFileRepository.findByItemAndNameAndLatestFalseOrderByCreatedDateDesc(file.getItem(), file.getName());
                    if (oldVersionFiles.size() > 0) {
                        for (PLMItemFile oldVersionFile : oldVersionFiles) {
                            oldVersionFile.setFileNo(file.getFileNo());
                            oldVersionFile = itemFileRepository.save(oldVersionFile);
                        }
                    }

                }
            }
        }
        if (itemFiles.size() > 0) {
            itemFiles.forEach(plmItemFile -> {
                plmItemFile.setFileType(plmItemFile.getType());
                plmItemFile.setParentFile(plmItemFile.getParent());
                plmItemFile = itemFileRepository.save(plmItemFile);
            });
        }
        if (changeFiles.size() > 0) {
            for (PLMChangeFile file : changeFiles) {
                if (file.getFileNo() == null || file.getFileNo() == "" || file.getFileNo().isEmpty()) {
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    file.setFileNo(autoNumber1);
                    file = changeFileRepository.save(file);
                    List<PLMChangeFile> oldVersionFiles = changeFileRepository.findByChangeAndNameAndLatestFalseOrderByCreatedDateDesc(file.getChange(), file.getName());
                    if (oldVersionFiles.size() > 0) {
                        for (PLMChangeFile oldVersionFile : oldVersionFiles) {
                            oldVersionFile.setFileNo(file.getFileNo());
                            oldVersionFile = changeFileRepository.save(oldVersionFile);
                        }
                    }

                }

            }
        }
        if (manufacturerFiles.size() > 0) {
            for (PLMManufacturerFile file : manufacturerFiles) {
                if (file.getFileNo() == null || file.getFileNo() == "" || file.getFileNo().isEmpty()) {
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    file.setFileNo(autoNumber1);
                    file = manufacturerFileRepository.save(file);
                    List<PLMManufacturerFile> oldVersionFiles = manufacturerFileRepository.findByManufacturerAndNameAndLatestFalseOrderByCreatedDateDesc(file.getManufacturer(), file.getName());
                    if (oldVersionFiles.size() > 0) {
                        for (PLMManufacturerFile oldVersionFile : oldVersionFiles) {
                            oldVersionFile.setFileNo(file.getFileNo());
                            oldVersionFile = manufacturerFileRepository.save(oldVersionFile);
                        }
                    }

                }

            }
        }
        if (manufacturerPartFiles.size() > 0) {
            for (PLMManufacturerPartFile file : manufacturerPartFiles) {
                if (file.getFileNo() == null || file.getFileNo() == "" || file.getFileNo().isEmpty()) {
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    file.setFileNo(autoNumber1);
                    file = manufacturerPartFileRepository.save(file);
                    List<PLMManufacturerPartFile> oldVersionFiles = manufacturerPartFileRepository.findByManufacturerPartAndNameAndLatestFalseOrderByCreatedDateDesc(file.getManufacturerPart(), file.getName());
                    if (oldVersionFiles.size() > 0) {
                        for (PLMManufacturerPartFile oldVersionFile : oldVersionFiles) {
                            oldVersionFile.setFileNo(file.getFileNo());
                            oldVersionFile = manufacturerPartFileRepository.save(oldVersionFile);
                        }
                    }

                }

            }
        }
        if (glossaryFiles.size() > 0) {
            for (PLMGlossaryFile file : glossaryFiles) {
                if (file.getFileNo() == null || file.getFileNo() == "" || file.getFileNo().isEmpty()) {
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    file.setFileNo(autoNumber1);
                    file = glossaryFileRepository.save(file);
                    List<PLMGlossaryFile> oldVersionFiles = glossaryFileRepository.findByGlossaryAndNameAndLatestFalseOrderByCreatedDateDesc(file.getGlossary(), file.getName());
                    if (oldVersionFiles.size() > 0) {
                        for (PLMGlossaryFile oldVersionFile : oldVersionFiles) {
                            oldVersionFile.setFileNo(file.getFileNo());
                            oldVersionFile = glossaryFileRepository.save(oldVersionFile);
                        }
                    }

                }

            }
        }
        if (projectFiles.size() > 0) {
            for (PLMProjectFile file : projectFiles) {
                if (file.getFileNo() == null || file.getFileNo() == "" || file.getFileNo().isEmpty()) {
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    file.setFileNo(autoNumber1);
                    file = projectFileRepository.save(file);
                    List<PLMProjectFile> oldVersionFiles = projectFileRepository.findByProjectAndNameAndLatestFalseOrderByCreatedDateDesc(file.getProject(), file.getName());
                    if (oldVersionFiles.size() > 0) {
                        for (PLMProjectFile oldVersionFile : oldVersionFiles) {
                            oldVersionFile.setFileNo(file.getFileNo());
                            oldVersionFile = projectFileRepository.save(oldVersionFile);
                        }
                    }

                }

            }
        }
        if (activityFiles.size() > 0) {
            for (PLMActivityFile file : activityFiles) {
                if (file.getFileNo() == null || file.getFileNo() == "" || file.getFileNo().isEmpty()) {
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    file.setFileNo(autoNumber1);
                    file = activityFileRepository.save(file);
                    List<PLMActivityFile> oldVersionFiles = activityFileRepository.findByActivityAndNameAndLatestFalseOrderByCreatedDateDesc(file.getActivity(), file.getName());
                    if (oldVersionFiles.size() > 0) {
                        for (PLMActivityFile oldVersionFile : oldVersionFiles) {
                            oldVersionFile.setFileNo(file.getFileNo());
                            oldVersionFile = activityFileRepository.save(oldVersionFile);
                        }
                    }

                }

            }
        }
        if (taskFiles.size() > 0) {
            for (PLMTaskFile file : taskFiles) {
                if (file.getFileNo() == null || file.getFileNo() == "" || file.getFileNo().isEmpty()) {
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    file.setFileNo(autoNumber1);
                    file = taskFileRepository.save(file);
                    List<PLMTaskFile> oldVersionFiles = taskFileRepository.findByTaskAndNameAndLatestFalseOrderByCreatedDateDesc(file.getTask(), file.getName());
                    if (oldVersionFiles.size() > 0) {
                        for (PLMTaskFile oldVersionFile : oldVersionFiles) {
                            oldVersionFile.setFileNo(file.getFileNo());
                            oldVersionFile = taskFileRepository.save(oldVersionFile);
                        }
                    }

                }

            }
        }
        if (objectFiles.size() > 0) {
            for (RmObjectFile file : objectFiles) {
                if (file.getFileNo() == null || file.getFileNo() == "" || file.getFileNo().isEmpty()) {
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    file.setFileNo(autoNumber1);
                    file = rmObjectFileRepository.save(file);
                    List<RmObjectFile> oldVersionFiles = rmObjectFileRepository.findByObjectAndNameAndLatestFalseOrderByCreatedDateDesc(file.getObject(), file.getName());
                    if (oldVersionFiles.size() > 0) {
                        for (RmObjectFile oldVersionFile : oldVersionFiles) {
                            oldVersionFile.setFileNo(file.getFileNo());
                            oldVersionFile = rmObjectFileRepository.save(oldVersionFile);
                        }
                    }

                }

            }
        }
    }

    @Transactional(readOnly = true)
    public TopSearchDto getItemTopSearch(Pageable pageable, ItemCriteria criteria, String type) {
        TopSearchDto topSearchDto = new TopSearchDto();

        RequirementDocumentCriteria requirementDocumentCriteria = new RequirementDocumentCriteria();
        requirementDocumentCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<PLMRequirementDocument> requirementDocuments = reqDocumentService.getAllReqDocuments(pageable, requirementDocumentCriteria);

        RequirementCriteria requirementCriteria = new RequirementCriteria();
        requirementCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<PLMRequirementVersion> requirements = reqDocumentService.getAllRequirementVersions(pageable, requirementCriteria);

        WorkflowDefinitionCriteria workflowDefinitionCriteria = new WorkflowDefinitionCriteria();
        workflowDefinitionCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<PLMWorkflowDefinition> workflowDefinitions = workflowDefinitionService.freeTextSearch(pageable, workflowDefinitionCriteria);

        ProjectCriteria projectCriteria = new ProjectCriteria();
        projectCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<PLMProject> projects = projectService.freeTextSearch(pageable, projectCriteria);

        InspectionPlanCriteria inspectionPlanCriteria = new InspectionPlanCriteria();
        inspectionPlanCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<InspectionPlansDto> inspectionPlans = inspectionPlanService.getAllProductInspectionPlans(pageable, inspectionPlanCriteria);
        Page<InspectionPlansDto> materialInspectionPlans = inspectionPlanService.getAllMaterialInspectionPlans(pageable, inspectionPlanCriteria);
        Page<InspectionsDto> inspections = inspectionService.getAllItemInspections(pageable, inspectionPlanCriteria);
        Page<InspectionsDto> materialInspections = inspectionService.getAllMaterialInspections(pageable, inspectionPlanCriteria);

        ProblemReportCriteria reportCriteria = new ProblemReportCriteria();
        reportCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<ProblemReportsDto> problemReports = problemReportService.getAllProblemReports(pageable, reportCriteria);

        NCRCriteria ncrCriteria = new NCRCriteria();
        ncrCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<NCRsDto> allNcrs = ncrService.getAllNcrs(pageable, ncrCriteria);

        QCRCriteria qcrCriteria = new QCRCriteria();
        qcrCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<QCRsDto> allQcrs = qcrService.getAllQcrs(pageable, qcrCriteria);

        PPAPCriteria ppapCriteria = new PPAPCriteria();
        ppapCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<PQMPPAP> pqmppaps = ppapService.getAllPPAPsByPageable(pageable, ppapCriteria);

        ProjectCriteria programCriteria = new ProjectCriteria();
        programCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<ProgramDto> programs = programService.getAllPrograms(pageable, programCriteria);

        topSearchDto.setProductInspectionPlanCount(inspectionPlans.getTotalElements());
        topSearchDto.setMaterialInspectionPlanCount(materialInspectionPlans.getTotalElements());
        topSearchDto.setItemInspectionCount(inspections.getTotalElements());
        topSearchDto.setMaterialInspectionCount(materialInspections.getTotalElements());
        topSearchDto.setProblemReportCount(problemReports.getTotalElements());
        topSearchDto.setNcrCount(allNcrs.getTotalElements());
        topSearchDto.setQcrCount(allQcrs.getTotalElements());
        topSearchDto.setPpapCount(pqmppaps.getTotalElements());

        ECOCriteria ecoCriteria = new ECOCriteria();
        ecoCriteria.setSearchQuery(criteria.getSearchQuery());
        ecoCriteria.setFreeTextSearch(true);
        Page<PLMECO> plmecos = ecoService.find(ecoCriteria, pageable);

        DCOCriteria dcoCriteria = new DCOCriteria();
        DCRCriteria dcrCriteria = new DCRCriteria();
        MCOCriteria mcoCriteria = new MCOCriteria();
        VarianceCriteria varianceCriteria = new VarianceCriteria();
        dcoCriteria.setSearchQuery(criteria.getSearchQuery());
        dcrCriteria.setSearchQuery(criteria.getSearchQuery());
        mcoCriteria.setSearchQuery(criteria.getSearchQuery());
        varianceCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<ECRDto> allEcrs = ecrService.getAllECRs(pageable, dcoCriteria);
        Page<DCODto> allDcos = dcoService.getAllDCOS(pageable, dcoCriteria);
        Page<DCRDto> allDcrs = dcrService.getAllDCRS(pageable, dcrCriteria);
        Page<PLMMCO> allMcos = mcoService.getAllMCOs(pageable, mcoCriteria);
        varianceCriteria.setVarianceType(VarianceType.DEVIATION);
        Page<VarianceDto> deviations = varianceService.getVarianceByType(pageable, varianceCriteria);
        varianceCriteria.setVarianceType(VarianceType.WAIVER);
        Page<VarianceDto> waivers = varianceService.getVarianceByType(pageable, varianceCriteria);
        topSearchDto.setEcoCount(plmecos.getTotalElements());
        topSearchDto.setEcrCount(allEcrs.getTotalElements());
        topSearchDto.setDcoCount(allDcos.getTotalElements());
        topSearchDto.setDcrCount(allDcrs.getTotalElements());
        topSearchDto.setMcoCount(allMcos.getTotalElements());
        topSearchDto.setDeviationCount(deviations.getTotalElements());
        topSearchDto.setWaiverCount(waivers.getTotalElements());

        Page<CommonFileDto> commonFileDtoList = recentlyVisitedService.getFreeTextSearchFiles(criteria.getSearchQuery(), pageable);

        Page<PLMDocument> documents = plmDocumentRepository.findByNameContainingIgnoreCaseAndLatestTrueAndFileType(criteria.getSearchQuery(), "FILE", pageable);

        Predicate predicate = predicateBuilder.build(criteria, QPLMItem.pLMItem);
        Page<PLMItem> plmItems = itemService.searchItems(predicate, pageable);

        NprCriteria nprCriteria = new NprCriteria();
        nprCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<PLMNpr> nprs = nprService.getAllNprs(pageable, nprCriteria);


        PlantCriteria plantCriteria = new PlantCriteria();
        plantCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MESPlant> plants = plantService.getAllPlantsByPageable(pageable, plantCriteria);

        AssemblyLineCriteria assemblyLineCriteria = new AssemblyLineCriteria();
        assemblyLineCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MESAssemblyLine> assemblyLines = assemblyLineService.getAllAssemblyLines(pageable, assemblyLineCriteria);

        WorkCenterCriteria workCenterCriteria = new WorkCenterCriteria();
        workCenterCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MESWorkCenter> workCenters = workCenterService.getPageableWorkCenters(pageable, workCenterCriteria);

        MachineCriteria machineCriteria = new MachineCriteria();
        machineCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MESMachine> machines = machineService.getAllMachinesByPageable(pageable, machineCriteria);

        InstrumentCriteria instrumentCriteria = new InstrumentCriteria();
        instrumentCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MESInstrument> instruments = instrumentService.getAllInstrumentsByPageable(pageable, instrumentCriteria);

        EquipmentCriteria equipmentCriteria = new EquipmentCriteria();
        equipmentCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MESEquipment> equipments = equipmentService.getAllEquipmentsByPageable(pageable, equipmentCriteria);

        ToolCriteria toolCriteria = new ToolCriteria();
        toolCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MESTool> tools = toolService.getAllToolsByPageable(pageable, toolCriteria);

        JigsFixtureCriteria jigsFixtureCriteria = new JigsFixtureCriteria();
        jigsFixtureCriteria.setSearchQuery(criteria.getSearchQuery());
        jigsFixtureCriteria.setJigType("JIG");
        Page<JigsFixtureDto> jigs = jigsFixtureService.getAllJigsFixtures(pageable, jigsFixtureCriteria);

        jigsFixtureCriteria = new JigsFixtureCriteria();
        jigsFixtureCriteria.setSearchQuery(criteria.getSearchQuery());
        jigsFixtureCriteria.setJigType("FIXTURE");
        Page<JigsFixtureDto> fixtures = jigsFixtureService.getAllJigsFixtures(pageable, jigsFixtureCriteria);

        MaterialCriteria materialCriteria = new MaterialCriteria();
        materialCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MaterialDto> materials = materialService.getAllMaterials(pageable, materialCriteria);

        ManpowerCriteria manpowerCriteria = new ManpowerCriteria();
        manpowerCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MESManpower> manpowers = manpowerService.getAllManpowersByPageable(pageable, manpowerCriteria);

        ShiftCriteria shiftCriteria = new ShiftCriteria();
        shiftCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MESShift> shifts = shiftService.getAllShiftsByPageable(pageable, shiftCriteria);

        OperationCriteria operationCriteria = new OperationCriteria();
        operationCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MESOperation> operations = operationService.getAllOperationsByPageable(pageable, operationCriteria);

        CustomObjectCriteria customObjectCriteria = new CustomObjectCriteria();
        customObjectCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<CustomObject> customObjects = customObjectService.getCustomObjects(pageable, customObjectCriteria);

        PDMObjectCriteria objectCriteria = new PDMObjectCriteria();
        objectCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<PDMVault> vaults = pdmVaultService.getVaultsByPageable(pageable, objectCriteria);
        Page<PDMAssembly> assemblies = pdmService.getAssembliesByPageable(pageable, objectCriteria);
        Page<PDMPart> pdmParts = pdmService.getPartsByPageable(pageable, objectCriteria);
        Page<PDMDrawing> pdmDrawings = pdmService.getDrawingsByPageable(pageable, objectCriteria);

        topSearchDto.setPlantCount(plants.getTotalElements());
        topSearchDto.setAssemblyLineCount(assemblyLines.getTotalElements());
        topSearchDto.setWorkCenterCount(workCenters.getTotalElements());
        topSearchDto.setMachineCount(machines.getTotalElements());
        topSearchDto.setInstrumentCount(instruments.getTotalElements());
        topSearchDto.setEquipmentCount(equipments.getTotalElements());
        topSearchDto.setToolCount(tools.getTotalElements());
        topSearchDto.setJigCount(jigs.getTotalElements());
        topSearchDto.setFixtureCount(fixtures.getTotalElements());
        topSearchDto.setMaterialCount(materials.getTotalElements());
        topSearchDto.setManPowerCount(manpowers.getTotalElements());
        topSearchDto.setShiftCount(shifts.getTotalElements());
        topSearchDto.setOperationCount(operations.getTotalElements());


        ManufacturerCriteria manufacturerCriteria = new ManufacturerCriteria();
        manufacturerCriteria.setSearchQuery(criteria.getSearchQuery());
        manufacturerCriteria.setFreeTextSearch(true);
        Page<PLMManufacturer> manufacturers = manufacturerService.find(manufacturerCriteria, pageable);

        ManufacturerPartCriteria manufacturerPartCriteria = new ManufacturerPartCriteria();
        manufacturerPartCriteria.setSearchQuery(criteria.getSearchQuery());
        manufacturerPartCriteria.setFreeTextSearch(true);
        Page<PLMManufacturerPart> manufacturerParts = manufacturerPartService.getAllMfrParts(pageable, manufacturerPartCriteria);

        PLMSupplierCriteria supplierCriteria = new PLMSupplierCriteria();
        supplierCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<PLMSupplier> suppliers = supplierService.getAllSuppliersByPageable(pageable, supplierCriteria);

        SupplierAuditCriteria supplierAuditCriteria = new SupplierAuditCriteria();
        supplierAuditCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<PQMSupplierAudit> supplierAudits = supplierAuditService.getAllSupplierAuditsByPageable(pageable, supplierAuditCriteria);

        topSearchDto.setMfrCount(manufacturers.getTotalElements());
        topSearchDto.setMfrPartCount(manufacturerParts.getTotalElements());
        topSearchDto.setSupplierCount(suppliers.getTotalElements());
        topSearchDto.setSupplierAuditCount(supplierAudits.getTotalElements());


        AssetCriteria assetCriteria = new AssetCriteria();
        assetCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MROAsset> assets = assetService.getAllAssetsByPageable(pageable, assetCriteria);

        MeterCriteria meterCriteria = new MeterCriteria();
        meterCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MROMeter> meters = meterService.getAllMetersByPageable(pageable, meterCriteria);

        SparePartCriteria sparePartCriteria = new SparePartCriteria();
        sparePartCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MROSparePart> spareParts = sparePartsService.getAllPartsByPageable(pageable, sparePartCriteria);

        MaintenancePlanCriteria maintenancePlanCriteria = new MaintenancePlanCriteria();
        maintenancePlanCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<MaintenancePlanDto> maintenancePlans = maintenancePlanService.getAllMaintenancePlans(pageable, maintenancePlanCriteria);

        WorkRequestCriteria workRequestCriteria = new WorkRequestCriteria();
        workRequestCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<WorkRequestDto> workRequests = workRequestService.getAllWorkRequests(pageable, workRequestCriteria);

        WorkOrderCriteria workOrderCriteria = new WorkOrderCriteria();
        workOrderCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<WorkOrderDto> workOrders = workOrderService.getAllWorkOrders(pageable, workOrderCriteria);
        topSearchDto.setAssetCount(assets.getTotalElements());
        topSearchDto.setMeterCount(meters.getTotalElements());
        topSearchDto.setSparePartCount(spareParts.getTotalElements());
        topSearchDto.setMaintenancePlanCount(maintenancePlans.getTotalElements());
        topSearchDto.setWorkRequestCount(workRequests.getTotalElements());
        topSearchDto.setWorkOrderCount(workOrders.getTotalElements());


        SubstanceCriteria substanceCriteria = new SubstanceCriteria();
        substanceCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<PGCSubstance> substances = substanceService.getAllSubstances(pageable, substanceCriteria);

        PGCSpecificationCriteria pgcSpecificationCriteria = new PGCSpecificationCriteria();
        pgcSpecificationCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<PGCSpecification> pgcSpecifications = pgcSpecificationService.getAllSpecifications(pageable, pgcSpecificationCriteria);

        DeclarationCriteria declarationCriteria = new DeclarationCriteria();
        declarationCriteria.setSearchQuery(criteria.getSearchQuery());
        Page<PGCDeclaration> pgcDeclarations = declarationService.getAllDeclarations(pageable, declarationCriteria);
        topSearchDto.setPgcSubstanceCount(substances.getTotalElements());
        topSearchDto.setPgcSpecificationCount(pgcSpecifications.getTotalElements());
        topSearchDto.setPgcDeclarationCount(pgcDeclarations.getTotalElements());

        TagCriteria tagCriteria = new TagCriteria();
        tagCriteria.setSearchQuery(criteria.getSearchQuery());
        tagCriteria.setObjectType(criteria.getTagType());
        Predicate tagPredicate = tagPredicateBuilder.build(tagCriteria, QTag.tag);
        Pageable tagPageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(new Sort.Order(Sort.Direction.ASC, "label")));
        Page<Tag> tags = tagRepository.findAll(tagPredicate, tagPageable);
        Page<TagDto> tagDtos = getTags(tags, tagPageable);

        topSearchDto.setItemsCount(plmItems.getTotalElements());
        topSearchDto.setNprCount(nprs.getTotalElements());
        topSearchDto.setEcoCount(plmecos.getTotalElements());
        topSearchDto.setProjectsCount(projects.getTotalElements());
        topSearchDto.setProgramCount(programs.getTotalElements());
        topSearchDto.setFilesCount(commonFileDtoList.getTotalElements());
        topSearchDto.setRequirementDocumentsCount(requirementDocuments.getTotalElements());
        topSearchDto.setRequirementsCount(requirements.getTotalElements());
        topSearchDto.setWorkflowCount(workflowDefinitions.getTotalElements());
        topSearchDto.setProductInspectionPlanCount(inspectionPlans.getTotalElements());
        topSearchDto.setMaterialInspectionPlanCount(materialInspectionPlans.getTotalElements());
        topSearchDto.setItemInspectionCount(inspections.getTotalElements());
        topSearchDto.setMaterialInspectionCount(materialInspections.getTotalElements());
        topSearchDto.setProblemReportCount(problemReports.getTotalElements());
        topSearchDto.setNcrCount(allNcrs.getTotalElements());
        topSearchDto.setQcrCount(allQcrs.getTotalElements());
        topSearchDto.setEcrCount(allEcrs.getTotalElements());
        topSearchDto.setDcoCount(allDcos.getTotalElements());
        topSearchDto.setDcrCount(allDcrs.getTotalElements());
        topSearchDto.setMcoCount(allMcos.getTotalElements());
        topSearchDto.setDeviationCount(deviations.getTotalElements());
        topSearchDto.setWaiverCount(waivers.getTotalElements());
        topSearchDto.setPlantCount(plants.getTotalElements());
        topSearchDto.setAssemblyLineCount(assemblyLines.getTotalElements());
        topSearchDto.setWorkCenterCount(workCenters.getTotalElements());
        topSearchDto.setMachineCount(machines.getTotalElements());
        topSearchDto.setInstrumentCount(instruments.getTotalElements());
        topSearchDto.setEquipmentCount(equipments.getTotalElements());
        topSearchDto.setToolCount(tools.getTotalElements());
        topSearchDto.setJigCount(jigs.getTotalElements());
        topSearchDto.setFixtureCount(fixtures.getTotalElements());
        topSearchDto.setMaterialCount(materials.getTotalElements());
        topSearchDto.setManPowerCount(manpowers.getTotalElements());
        topSearchDto.setShiftCount(shifts.getTotalElements());
        topSearchDto.setOperationCount(operations.getTotalElements());
        topSearchDto.setMfrCount(manufacturers.getTotalElements());
        topSearchDto.setMfrPartCount(manufacturerParts.getTotalElements());
        topSearchDto.setSupplierCount(suppliers.getTotalElements());
        topSearchDto.setSupplierAuditCount(supplierAudits.getTotalElements());
        topSearchDto.setAssetCount(assets.getTotalElements());
        topSearchDto.setMeterCount(meters.getTotalElements());
        topSearchDto.setSparePartCount(spareParts.getTotalElements());
        topSearchDto.setMaintenancePlanCount(maintenancePlans.getTotalElements());
        topSearchDto.setWorkRequestCount(workRequests.getTotalElements());
        topSearchDto.setWorkOrderCount(workOrders.getTotalElements());
        topSearchDto.setPdmVaultCount(vaults.getTotalElements());
        topSearchDto.setPdmAssemblyCount(assemblies.getTotalElements());
        topSearchDto.setPdmPartCount(pdmParts.getTotalElements());
        topSearchDto.setPdmDrawingCount(pdmDrawings.getTotalElements());
        topSearchDto.setCustomObjectCount(customObjects.getTotalElements());
        topSearchDto.setDocumentCount(documents.getTotalElements());
        topSearchDto.setTagsCount(tags.getTotalElements());

        if (type.equals("CHANGES")) {
            if (topSearchDto.getEcrCount() > 0) {
                topSearchDto.setEcrs(allEcrs);
                topSearchDto.setFirst(allEcrs.isFirst());
                topSearchDto.setLast(allEcrs.isLast());
            } else if (topSearchDto.getEcoCount() > 0) {
                topSearchDto.setEcos(plmecos);
                topSearchDto.setFirst(plmecos.isFirst());
                topSearchDto.setLast(plmecos.isLast());
            } else if (topSearchDto.getDcrCount() > 0) {
                topSearchDto.setDcrs(allDcrs);
                topSearchDto.setFirst(allDcrs.isFirst());
                topSearchDto.setLast(allDcrs.isLast());
            } else if (topSearchDto.getDcoCount() > 0) {
                topSearchDto.setDcos(allDcos);
                topSearchDto.setFirst(allDcos.isFirst());
                topSearchDto.setLast(allDcos.isLast());
            } else if (topSearchDto.getMcoCount() > 0) {
                topSearchDto.setMcos(allMcos);
                topSearchDto.setFirst(allMcos.isFirst());
                topSearchDto.setLast(allMcos.isLast());
            } else if (topSearchDto.getDeviationCount() > 0) {
                topSearchDto.setVariances(deviations);
                topSearchDto.setFirst(deviations.isFirst());
                topSearchDto.setLast(deviations.isLast());
            } else if (topSearchDto.getWaiverCount() > 0) {
                topSearchDto.setVariances(waivers);
                topSearchDto.setFirst(waivers.isFirst());
                topSearchDto.setLast(waivers.isLast());
            }
        } else if (type.equals("OEM")) {
            if (topSearchDto.getMfrCount() > 0) {
                topSearchDto.setManufacturers(manufacturers);
                topSearchDto.setFirst(manufacturers.isFirst());
                topSearchDto.setLast(manufacturers.isLast());
            } else if (topSearchDto.getMfrPartCount() > 0) {
                topSearchDto.setManufacturerParts(manufacturerParts);
                topSearchDto.setFirst(manufacturerParts.isFirst());
                topSearchDto.setLast(manufacturerParts.isLast());
            } else if (topSearchDto.getSupplierCount() > 0) {
                topSearchDto.setSuppliers(suppliers);
                topSearchDto.setFirst(suppliers.isFirst());
                topSearchDto.setLast(suppliers.isLast());
            }
        } else if (type.equals("DESIGN_DATA")) {
            if (topSearchDto.getPdmVaultCount() > 0) {
                topSearchDto.setPdmVaults(vaults);
                topSearchDto.setFirst(vaults.isFirst());
                topSearchDto.setLast(vaults.isLast());
            } else if (topSearchDto.getPdmAssemblyCount() > 0) {
                topSearchDto.setPdmAssemblies(assemblies);
                topSearchDto.setFirst(assemblies.isFirst());
                topSearchDto.setLast(assemblies.isLast());
            } else if (topSearchDto.getPdmPartCount() > 0) {
                topSearchDto.setPdmParts(pdmParts);
                topSearchDto.setFirst(pdmParts.isFirst());
                topSearchDto.setLast(pdmParts.isLast());
            } else if (topSearchDto.getPdmDrawingCount() > 0) {
                topSearchDto.setPdmDrawings(pdmDrawings);
                topSearchDto.setFirst(pdmDrawings.isFirst());
                topSearchDto.setLast(pdmDrawings.isLast());
            }
        } else if (type.equals("QUALITY")) {
            if (topSearchDto.getProductInspectionPlanCount() > 0) {
                topSearchDto.setInspectionPlans(inspectionPlans);
                topSearchDto.setFirst(inspectionPlans.isFirst());
                topSearchDto.setLast(inspectionPlans.isLast());
            } else if (topSearchDto.getMaterialInspectionPlanCount() > 0) {
                topSearchDto.setInspectionPlans(materialInspectionPlans);
                topSearchDto.setFirst(materialInspectionPlans.isFirst());
                topSearchDto.setLast(materialInspectionPlans.isLast());
            } else if (topSearchDto.getItemInspectionCount() > 0) {
                topSearchDto.setInspections(inspections);
                topSearchDto.setFirst(inspections.isFirst());
                topSearchDto.setLast(inspections.isLast());
            } else if (topSearchDto.getMaterialInspectionCount() > 0) {
                topSearchDto.setInspections(materialInspections);
                topSearchDto.setFirst(materialInspections.isFirst());
                topSearchDto.setLast(materialInspections.isLast());
            } else if (topSearchDto.getProblemReportCount() > 0) {
                topSearchDto.setProblemReports(problemReports);
                topSearchDto.setFirst(problemReports.isFirst());
                topSearchDto.setLast(problemReports.isLast());
            } else if (topSearchDto.getNcrCount() > 0) {
                topSearchDto.setNcrs(allNcrs);
                topSearchDto.setFirst(allNcrs.isFirst());
                topSearchDto.setLast(allNcrs.isLast());
            } else if (topSearchDto.getQcrCount() > 0) {
                topSearchDto.setQcrs(allQcrs);
                topSearchDto.setFirst(allQcrs.isFirst());
                topSearchDto.setLast(allQcrs.isLast());
            } else if (topSearchDto.getPpapCount() > 0) {
                topSearchDto.setPpaps(pqmppaps);
                topSearchDto.setFirst(pqmppaps.isFirst());
                topSearchDto.setLast(pqmppaps.isLast());
            } else if (topSearchDto.getSupplierAuditCount() > 0) {
                topSearchDto.setSupplierAudits(supplierAudits);
                topSearchDto.setFirst(supplierAudits.isFirst());
                topSearchDto.setLast(supplierAudits.isLast());
            }
        } else if (type.equals("MANUFACTURING")) {
            if (topSearchDto.getPlantCount() > 0) {
                topSearchDto.setPlants(plants);
                topSearchDto.setFirst(plants.isFirst());
                topSearchDto.setLast(plants.isLast());
            } else if (topSearchDto.getAssemblyLineCount() > 0) {
                topSearchDto.setAssemblyLines(assemblyLines);
                topSearchDto.setFirst(assemblyLines.isFirst());
                topSearchDto.setLast(assemblyLines.isLast());
            } else if (topSearchDto.getWorkCenterCount() > 0) {
                topSearchDto.setWorkCenters(workCenters);
                topSearchDto.setFirst(workCenters.isFirst());
                topSearchDto.setLast(workCenters.isLast());
            } else if (topSearchDto.getMachineCount() > 0) {
                topSearchDto.setMachines(machines);
                topSearchDto.setFirst(machines.isFirst());
                topSearchDto.setLast(machines.isLast());
            } else if (topSearchDto.getEquipmentCount() > 0) {
                topSearchDto.setEquipments(equipments);
                topSearchDto.setFirst(equipments.isFirst());
                topSearchDto.setLast(equipments.isLast());
            } else if (topSearchDto.getInstrumentCount() > 0) {
                topSearchDto.setInstruments(instruments);
                topSearchDto.setFirst(instruments.isFirst());
                topSearchDto.setLast(instruments.isLast());
            } else if (topSearchDto.getToolCount() > 0) {
                topSearchDto.setTools(tools);
                topSearchDto.setFirst(tools.isFirst());
                topSearchDto.setLast(tools.isLast());
            } else if (topSearchDto.getJigCount() > 0) {
                topSearchDto.setJigs(jigs);
                topSearchDto.setFirst(jigs.isFirst());
                topSearchDto.setLast(jigs.isLast());
            } else if (topSearchDto.getFixtureCount() > 0) {
                topSearchDto.setJigs(fixtures);
                topSearchDto.setFirst(fixtures.isFirst());
                topSearchDto.setLast(fixtures.isLast());
            } else if (topSearchDto.getMaterialCount() > 0) {
                topSearchDto.setMaterials(materials);
                topSearchDto.setFirst(materials.isFirst());
                topSearchDto.setLast(materials.isLast());
            } else if (topSearchDto.getManPowerCount() > 0) {
                topSearchDto.setManpowers(manpowers);
                topSearchDto.setFirst(manpowers.isFirst());
                topSearchDto.setLast(manpowers.isLast());
            } else if (topSearchDto.getShiftCount() > 0) {
                topSearchDto.setShifts(shifts);
                topSearchDto.setFirst(shifts.isFirst());
                topSearchDto.setLast(shifts.isLast());
            } else if (topSearchDto.getOperationCount() > 0) {
                topSearchDto.setOperations(operations);
                topSearchDto.setFirst(operations.isFirst());
                topSearchDto.setLast(operations.isLast());
            }
        } else if (type.equals("MAINTENANCE")) {
            if (topSearchDto.getAssetCount() > 0) {
                topSearchDto.setAssets(assets);
                topSearchDto.setFirst(assets.isFirst());
                topSearchDto.setLast(assets.isLast());
            } else if (topSearchDto.getMeterCount() > 0) {
                topSearchDto.setMeters(meters);
                topSearchDto.setFirst(meters.isFirst());
                topSearchDto.setLast(meters.isLast());
            } else if (topSearchDto.getSparePartCount() > 0) {
                topSearchDto.setSpareParts(spareParts);
                topSearchDto.setFirst(spareParts.isFirst());
                topSearchDto.setLast(spareParts.isLast());
            } else if (topSearchDto.getMaintenancePlanCount() > 0) {
                topSearchDto.setMaintenancePlans(maintenancePlans);
                topSearchDto.setFirst(maintenancePlans.isFirst());
                topSearchDto.setLast(maintenancePlans.isLast());
            } else if (topSearchDto.getWorkRequestCount() > 0) {
                topSearchDto.setWorkRequests(workRequests);
                topSearchDto.setFirst(workRequests.isFirst());
                topSearchDto.setLast(workRequests.isLast());
            } else if (topSearchDto.getWorkOrderCount() > 0) {
                topSearchDto.setWorkOrders(workOrders);
                topSearchDto.setFirst(workOrders.isFirst());
                topSearchDto.setLast(workOrders.isLast());
            }
        } else if (type.equals("COMPLIANCE")) {
            if (topSearchDto.getPgcSubstanceCount() > 0) {
                topSearchDto.setPgcSubstances(substances);
                topSearchDto.setFirst(substances.isFirst());
                topSearchDto.setLast(substances.isLast());
            } else if (topSearchDto.getPgcSpecificationCount() > 0) {
                topSearchDto.setPgcSpecifications(pgcSpecifications);
                topSearchDto.setFirst(pgcSpecifications.isFirst());
                topSearchDto.setLast(pgcSpecifications.isLast());
            } else if (topSearchDto.getPgcDeclarationCount() > 0) {
                topSearchDto.setPgcDeclarations(pgcDeclarations);
                topSearchDto.setFirst(pgcDeclarations.isFirst());
                topSearchDto.setLast(pgcDeclarations.isLast());
            }
        } else if (type.equals("ITEM") || (type.equals("ALL") && topSearchDto.getItemsCount() > 0)) {
            topSearchDto.setItems(plmItems);
            topSearchDto.setFirst(plmItems.isFirst());
            topSearchDto.setLast(plmItems.isLast());
        } else if (type.equals("PDM_VAULT") || ((type.equals("ALL") || type.equals("DESIGN_DATA")) && topSearchDto.getPdmVaultCount() > 0)) {
            topSearchDto.setPdmVaults(vaults);
            topSearchDto.setFirst(vaults.isFirst());
            topSearchDto.setLast(vaults.isLast());
        } else if (type.equals("PDM_ASSEMBLY") || ((type.equals("ALL") || type.equals("DESIGN_DATA")) && topSearchDto.getPdmAssemblyCount() > 0)) {
            topSearchDto.setPdmAssemblies(assemblies);
            topSearchDto.setFirst(assemblies.isFirst());
            topSearchDto.setLast(assemblies.isLast());
        } else if (type.equals("PDM_PART") || ((type.equals("ALL") || type.equals("DESIGN_DATA")) && topSearchDto.getPdmPartCount() > 0)) {
            topSearchDto.setPdmParts(pdmParts);
            topSearchDto.setFirst(pdmParts.isFirst());
            topSearchDto.setLast(pdmParts.isLast());
        } else if (type.equals("PDM_DRAWING") || ((type.equals("ALL") || type.equals("DESIGN_DATA")) && topSearchDto.getPdmDrawingCount() > 0)) {
            topSearchDto.setPdmDrawings(pdmDrawings);
            topSearchDto.setFirst(pdmDrawings.isFirst());
            topSearchDto.setLast(pdmDrawings.isLast());
        } else if (type.equals("ECR") || ((type.equals("ALL") || type.equals("CHANGES")) && topSearchDto.getEcrCount() > 0)) {
            topSearchDto.setEcrs(allEcrs);
            topSearchDto.setFirst(allEcrs.isFirst());
            topSearchDto.setLast(allEcrs.isLast());
        } else if (type.equals("ECO") || ((type.equals("ALL") || type.equals("CHANGES")) && topSearchDto.getEcoCount() > 0)) {
            topSearchDto.setEcos(plmecos);
            topSearchDto.setFirst(plmecos.isFirst());
            topSearchDto.setLast(plmecos.isLast());
        } else if (type.equals("DCR") || ((type.equals("ALL") || type.equals("CHANGES")) && topSearchDto.getDcrCount() > 0)) {
            topSearchDto.setDcrs(allDcrs);
            topSearchDto.setFirst(allDcrs.isFirst());
            topSearchDto.setLast(allDcrs.isLast());
        } else if (type.equals("DCO") || ((type.equals("ALL") || type.equals("CHANGES")) && topSearchDto.getDcoCount() > 0)) {
            topSearchDto.setDcos(allDcos);
            topSearchDto.setFirst(allDcos.isFirst());
            topSearchDto.setLast(allDcos.isLast());
        } else if (type.equals("MCO") || ((type.equals("ALL") || type.equals("CHANGES")) && topSearchDto.getMcoCount() > 0)) {
            topSearchDto.setMcos(allMcos);
            topSearchDto.setFirst(allMcos.isFirst());
            topSearchDto.setLast(allMcos.isLast());
        } else if (type.equals("DEVIATION") || ((type.equals("ALL") || type.equals("CHANGES")) && topSearchDto.getDeviationCount() > 0)) {
            topSearchDto.setVariances(deviations);
            topSearchDto.setFirst(deviations.isFirst());
            topSearchDto.setLast(deviations.isLast());
        } else if (type.equals("WAIVER") || ((type.equals("ALL") || type.equals("CHANGES")) && topSearchDto.getWaiverCount() > 0)) {
            topSearchDto.setVariances(waivers);
            topSearchDto.setFirst(waivers.isFirst());
            topSearchDto.setLast(waivers.isLast());
        } else if (type.equals("PRODUCTINSPECTIONPLAN") || ((type.equals("ALL") || type.equals("QUALITY")) && topSearchDto.getProductInspectionPlanCount() > 0)) {
            topSearchDto.setInspectionPlans(inspectionPlans);
            topSearchDto.setFirst(inspectionPlans.isFirst());
            topSearchDto.setLast(inspectionPlans.isLast());
        } else if (type.equals("MATERIALINSPECTIONPLAN") || ((type.equals("ALL") || type.equals("QUALITY")) && topSearchDto.getMaterialInspectionPlanCount() > 0)) {
            topSearchDto.setInspectionPlans(materialInspectionPlans);
            topSearchDto.setFirst(materialInspectionPlans.isFirst());
            topSearchDto.setLast(materialInspectionPlans.isLast());
        } else if (type.equals("ITEMINSPECTION") || ((type.equals("ALL") || type.equals("QUALITY")) && topSearchDto.getItemInspectionCount() > 0)) {
            topSearchDto.setInspections(inspections);
            topSearchDto.setFirst(inspections.isFirst());
            topSearchDto.setLast(inspections.isLast());
        } else if (type.equals("MATERIALINSPECTION") || ((type.equals("ALL") || type.equals("QUALITY")) && topSearchDto.getMaterialInspectionCount() > 0)) {
            topSearchDto.setInspections(materialInspections);
            topSearchDto.setFirst(materialInspections.isFirst());
            topSearchDto.setLast(materialInspections.isLast());
        } else if (type.equals("PROBLEMREPORT") || ((type.equals("ALL") || type.equals("QUALITY")) && topSearchDto.getProblemReportCount() > 0)) {
            topSearchDto.setProblemReports(problemReports);
            topSearchDto.setFirst(problemReports.isFirst());
            topSearchDto.setLast(problemReports.isLast());
        } else if (type.equals("NCR") || ((type.equals("ALL") || type.equals("QUALITY")) && topSearchDto.getNcrCount() > 0)) {
            topSearchDto.setNcrs(allNcrs);
            topSearchDto.setFirst(allNcrs.isFirst());
            topSearchDto.setLast(allNcrs.isLast());
        } else if (type.equals("QCR") || ((type.equals("ALL") || type.equals("QUALITY")) && topSearchDto.getQcrCount() > 0)) {
            topSearchDto.setQcrs(allQcrs);
            topSearchDto.setFirst(allQcrs.isFirst());
            topSearchDto.setLast(allQcrs.isLast());
        } else if (type.equals("PPAP") || ((type.equals("ALL") || type.equals("QUALITY")) && topSearchDto.getPpapCount() > 0)) {
            topSearchDto.setPpaps(pqmppaps);
            topSearchDto.setFirst(pqmppaps.isFirst());
            topSearchDto.setLast(pqmppaps.isLast());
        } else if (type.equals("SUPPLIERAUDIT") || ((type.equals("ALL") || type.equals("QUALITY")) && topSearchDto.getSupplierAuditCount() > 0)) {
            topSearchDto.setSupplierAudits(supplierAudits);
            topSearchDto.setFirst(supplierAudits.isFirst());
            topSearchDto.setLast(supplierAudits.isLast());
        } else if (type.equals("PLANT") || ((type.equals("ALL") || type.equals("MANUFACTURING")) && topSearchDto.getPlantCount() > 0)) {
            topSearchDto.setPlants(plants);
            topSearchDto.setFirst(plants.isFirst());
            topSearchDto.setLast(plants.isLast());
        } else if (type.equals("ASSEMBLYLINE") || ((type.equals("ALL") || type.equals("MANUFACTURING")) && topSearchDto.getAssemblyLineCount() > 0)) {
            topSearchDto.setAssemblyLines(assemblyLines);
            topSearchDto.setFirst(assemblyLines.isFirst());
            topSearchDto.setLast(assemblyLines.isLast());
        } else if (type.equals("WORKCENTER") || ((type.equals("ALL") || type.equals("MANUFACTURING")) && topSearchDto.getWorkCenterCount() > 0)) {
            topSearchDto.setWorkCenters(workCenters);
            topSearchDto.setFirst(workCenters.isFirst());
            topSearchDto.setLast(workCenters.isLast());
        } else if (type.equals("MACHINE") || ((type.equals("ALL") || type.equals("MANUFACTURING")) && topSearchDto.getMachineCount() > 0)) {
            topSearchDto.setMachines(machines);
            topSearchDto.setFirst(machines.isFirst());
            topSearchDto.setLast(machines.isLast());
        } else if (type.equals("EQUIPMENT") || ((type.equals("ALL") || type.equals("MANUFACTURING")) && topSearchDto.getEquipmentCount() > 0)) {
            topSearchDto.setEquipments(equipments);
            topSearchDto.setFirst(equipments.isFirst());
            topSearchDto.setLast(equipments.isLast());
        } else if (type.equals("INSTRUMENT") || ((type.equals("ALL") || type.equals("MANUFACTURING")) && topSearchDto.getInstrumentCount() > 0)) {
            topSearchDto.setInstruments(instruments);
            topSearchDto.setFirst(instruments.isFirst());
            topSearchDto.setLast(instruments.isLast());
        } else if (type.equals("TOOL") || ((type.equals("ALL") || type.equals("MANUFACTURING")) && topSearchDto.getToolCount() > 0)) {
            topSearchDto.setTools(tools);
            topSearchDto.setFirst(tools.isFirst());
            topSearchDto.setLast(tools.isLast());
        } else if (type.equals("JIG") || ((type.equals("ALL") || type.equals("MANUFACTURING")) && topSearchDto.getJigCount() > 0)) {
            topSearchDto.setJigs(jigs);
            topSearchDto.setFirst(jigs.isFirst());
            topSearchDto.setLast(jigs.isLast());
        } else if (type.equals("FIXTURE") || ((type.equals("ALL") || type.equals("MANUFACTURING")) && topSearchDto.getFixtureCount() > 0)) {
            topSearchDto.setFixtures(fixtures);
            topSearchDto.setFirst(fixtures.isFirst());
            topSearchDto.setLast(fixtures.isLast());
        } else if (type.equals("MATERIAL") || ((type.equals("ALL") || type.equals("MANUFACTURING")) && topSearchDto.getMaterialCount() > 0)) {
            topSearchDto.setMaterials(materials);
            topSearchDto.setFirst(materials.isFirst());
            topSearchDto.setLast(materials.isLast());
        } else if (type.equals("MANPOWER") || ((type.equals("ALL") || type.equals("MANUFACTURING")) && topSearchDto.getManPowerCount() > 0)) {
            topSearchDto.setManpowers(manpowers);
            topSearchDto.setFirst(manpowers.isFirst());
            topSearchDto.setLast(manpowers.isLast());
        } else if (type.equals("SHIFT") || ((type.equals("ALL") || type.equals("MANUFACTURING")) && topSearchDto.getShiftCount() > 0)) {
            topSearchDto.setShifts(shifts);
            topSearchDto.setFirst(shifts.isFirst());
            topSearchDto.setLast(shifts.isLast());
        } else if (type.equals("OPERATION") || ((type.equals("ALL") || type.equals("MANUFACTURING")) && topSearchDto.getOperationCount() > 0)) {
            topSearchDto.setOperations(operations);
            topSearchDto.setFirst(operations.isFirst());
            topSearchDto.setLast(operations.isLast());
        } else if (type.equals("ASSET") || ((type.equals("ALL") || type.equals("MAINTENANCE")) && topSearchDto.getAssetCount() > 0)) {
            topSearchDto.setAssets(assets);
            topSearchDto.setFirst(assets.isFirst());
            topSearchDto.setLast(assets.isLast());
        } else if (type.equals("METER") || ((type.equals("ALL") || type.equals("MAINTENANCE")) && topSearchDto.getMeterCount() > 0)) {
            topSearchDto.setMeters(meters);
            topSearchDto.setFirst(meters.isFirst());
            topSearchDto.setLast(meters.isLast());
        } else if (type.equals("SPAREPART") || ((type.equals("ALL") || type.equals("MAINTENANCE")) && topSearchDto.getSparePartCount() > 0)) {
            topSearchDto.setSpareParts(spareParts);
            topSearchDto.setFirst(spareParts.isFirst());
            topSearchDto.setLast(spareParts.isLast());
        } else if (type.equals("MAINTENANCEPLAN") || ((type.equals("ALL") || type.equals("MAINTENANCE")) && topSearchDto.getMaintenancePlanCount() > 0)) {
            topSearchDto.setMaintenancePlans(maintenancePlans);
            topSearchDto.setFirst(maintenancePlans.isFirst());
            topSearchDto.setLast(maintenancePlans.isLast());
        } else if (type.equals("WORKREQUEST") || ((type.equals("ALL") || type.equals("MAINTENANCE")) && topSearchDto.getWorkRequestCount() > 0)) {
            topSearchDto.setWorkRequests(workRequests);
            topSearchDto.setFirst(workRequests.isFirst());
            topSearchDto.setLast(workRequests.isLast());
        } else if (type.equals("WORKORDER") || ((type.equals("ALL") || type.equals("MAINTENANCE")) && topSearchDto.getWorkOrderCount() > 0)) {
            topSearchDto.setWorkOrders(workOrders);
            topSearchDto.setFirst(workOrders.isFirst());
            topSearchDto.setLast(workOrders.isLast());
        } else if (type.equals("MANUFACTURER") || ((type.equals("ALL") || type.equals("OEM")) && topSearchDto.getMfrCount() > 0)) {
            topSearchDto.setManufacturers(manufacturers);
            topSearchDto.setFirst(manufacturers.isFirst());
            topSearchDto.setLast(manufacturers.isLast());
        } else if (type.equals("MANUFACTURERPART") || ((type.equals("ALL") || type.equals("OEM")) && topSearchDto.getMfrPartCount() > 0)) {
            topSearchDto.setManufacturerParts(manufacturerParts);
            topSearchDto.setFirst(manufacturerParts.isFirst());
            topSearchDto.setLast(manufacturerParts.isLast());
        } else if (type.equals("SUPPLIER") || ((type.equals("ALL") || type.equals("OEM")) && topSearchDto.getSupplierCount() > 0)) {
            topSearchDto.setSuppliers(suppliers);
            topSearchDto.setFirst(suppliers.isFirst());
            topSearchDto.setLast(suppliers.isLast());
        } else if (type.equals("SUBSTANCE") || ((type.equals("ALL") || type.equals("COMPLIANCE")) && topSearchDto.getPgcSubstanceCount() > 0)) {
            topSearchDto.setPgcSubstances(substances);
            topSearchDto.setFirst(substances.isFirst());
            topSearchDto.setLast(substances.isLast());
        } else if (type.equals("PGCSPECIFICATION") || ((type.equals("ALL") || type.equals("COMPLIANCE")) && topSearchDto.getPgcSpecificationCount() > 0)) {
            topSearchDto.setPgcSpecifications(pgcSpecifications);
            topSearchDto.setFirst(pgcSpecifications.isFirst());
            topSearchDto.setLast(pgcSpecifications.isLast());
        } else if (type.equals("PGCDECLARATION") || ((type.equals("ALL") || type.equals("COMPLIANCE")) && topSearchDto.getPgcDeclarationCount() > 0)) {
            topSearchDto.setPgcDeclarations(pgcDeclarations);
            topSearchDto.setFirst(pgcDeclarations.isFirst());
            topSearchDto.setLast(pgcDeclarations.isLast());
        } else if (type.equals("PROJECT") || (type.equals("ALL") && topSearchDto.getProjectsCount() > 0)) {
            topSearchDto.setProjects(projects);
            topSearchDto.setFirst(projects.isFirst());
            topSearchDto.setLast(projects.isLast());
        } else if (type.equals("PROGRAM") || (type.equals("ALL") && topSearchDto.getProgramCount() > 0)) {
            topSearchDto.setPrograms(programs);
            topSearchDto.setFirst(programs.isFirst());
            topSearchDto.setLast(programs.isLast());
        } else if (type.equals("REQUIREMENTDOCUMENT") || (type.equals("ALL") && topSearchDto.getRequirementDocumentsCount() > 0)) {
            topSearchDto.setRequirementDocuments(requirementDocuments);
            topSearchDto.setFirst(requirementDocuments.isFirst());
            topSearchDto.setLast(requirementDocuments.isLast());
        } else if (type.equals("REQUIREMENT") || (type.equals("ALL") && topSearchDto.getRequirementsCount() > 0)) {
            topSearchDto.setRequirements(requirements);
            topSearchDto.setFirst(requirements.isFirst());
            topSearchDto.setLast(requirements.isLast());
        } else if (type.equals("WORKFLOW") || (type.equals("ALL") && topSearchDto.getWorkflowCount() > 0)) {
            topSearchDto.setWorkflows(workflowDefinitions);
            topSearchDto.setFirst(workflowDefinitions.isFirst());
            topSearchDto.setLast(workflowDefinitions.isLast());
        } else if (type.equals("FILE") || (type.equals("ALL") && topSearchDto.getFilesCount() > 0)) {
            topSearchDto.setFiles(commonFileDtoList);
            topSearchDto.setFirst(commonFileDtoList.isFirst());
            topSearchDto.setLast(commonFileDtoList.isLast());
        } else if (type.equals("DOCUMENT") || (type.equals("ALL") && topSearchDto.getDocumentCount() > 0)) {
            documents.getContent().forEach(document -> {
                document.setFilePath(getDocumentFilePath(document));
            });
            topSearchDto.setFirst(documents.isFirst());
            topSearchDto.setLast(documents.isLast());
            topSearchDto.setDocuments(documents);
        } else if (type.equals("CUSTOMOBJECT") || (type.equals("ALL") && topSearchDto.getCustomObjectCount() > 0)) {
            topSearchDto.setCustomObjects(customObjects);
            topSearchDto.setFirst(customObjects.isFirst());
            topSearchDto.setLast(customObjects.isLast());
        } else if (type.equals("NPR") || (type.equals("ALL") && topSearchDto.getNprCount() > 0)) {
            topSearchDto.setNprs(nprs);
            topSearchDto.setFirst(nprs.isFirst());
            topSearchDto.setLast(nprs.isLast());
        } else if (type.equals("TAG") || (type.equals("ALL") && topSearchDto.getTagsCount() > 0)) {
            topSearchDto.setTags(tagDtos);
            topSearchDto.setFirst(tagDtos.isFirst());
            topSearchDto.setLast(tagDtos.isLast());

            topSearchDto.setTagObjectTypes(tagRepository.getUniqueObjectTypes());
        }

        return topSearchDto;
    }


    public String getDocumentFilePath(PLMDocument plmDocument) {
        String filePath = "";
        if (plmDocument.getParentFile() != null) {
            filePath = visitParentDocumentPath(plmDocument.getParentFile(), filePath);
        }
        return filePath;
    }

    private String visitParentDocumentPath(Integer fileId, String filePath) {
        PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
        if (filePath != "") {
            filePath = plmDocument.getName() + " / " + filePath;
        } else {
            filePath = plmDocument.getName();
        }
        if (plmDocument.getParentFile() != null) {
            filePath = visitParentDocumentPath(plmDocument.getParentFile(), filePath);
        }
        return filePath;
    }

    @Transactional(readOnly = true)
    public PersonsDto getPersonsWithoutLogin() {
        PersonsDto personsDto = new PersonsDto();
        List<Integer> personIds = loginRepository.getLoginPersonIds();

        if (personIds.size() > 0) {
            List<Integer> customerIds = pqmCustomerRepository.getCustomerByIdNotIn(personIds);
            List<Integer> supplierContactsIds = supplierContactRepository.getContactsByIdNotIn(personIds);
            List<Integer> manpowerContactsIds = manpowerContactRepository.getContactsByIdNotIn(personIds);

            if (customerIds.size() > 0) {
                personsDto.setCustomers(personRepository.findByIdIn(customerIds));
            }
            if (supplierContactsIds.size() > 0) {
                personsDto.setSupplierContacts(personRepository.findByIdIn(supplierContactsIds));
            }
           /* if (manpowerIds.size() > 0) {
                personsDto.setManpowers(personRepository.findByIdIn(manpowerIds));
            }*/

            if (manpowerContactsIds.size() > 0) {
                personsDto.setManpowerContacts(personRepository.findByIdIn(manpowerContactsIds));
            }
        }

        return personsDto;
    }

    @Transactional(readOnly = true)
    public ObjectCountsDto getObjectCounts() {
        ObjectCountsDto objectCountsDto = new ObjectCountsDto();
        if (securityPermissionService.checkPermission("item", null, "view"))
            objectCountsDto.setItemsCount(itemRepository.count());
        if (securityPermissionService.checkPermission("change", "eco", "view"))
            objectCountsDto.setEcoCount(ecoRepository.count());
        if (securityPermissionService.checkPermission("change", "ecr", "view"))
            objectCountsDto.setEcrCount(ecrRepository.count());
        if (securityPermissionService.checkPermission("change", "dco", "view"))
            objectCountsDto.setDcoCount(dcoRepository.count());
        if (securityPermissionService.checkPermission("change", "dcr", "view"))
            objectCountsDto.setDcrCount(dcrRepository.count());
        if (securityPermissionService.checkPermission("change", "mco", "view"))
            objectCountsDto.setMcoCount(mcoRepository.count());
        if (securityPermissionService.checkPermission("problemreport", null, "view"))
            objectCountsDto.setProblemReportCount(problemReportRepository.count());
        if (securityPermissionService.checkPermission("inspectionplan", null, "view"))
            objectCountsDto.setInspectionPlanCount(inspectionPlanRepository.count());
        if (securityPermissionService.checkPermission("inspection", null, "view"))
            objectCountsDto.setInspectionCount(inspectionRepository.count());
        if (securityPermissionService.checkPermission("ncr", null, "view"))
            objectCountsDto.setNcrCount(ncrRepository.count());
        if (securityPermissionService.checkPermission("qcr", null, "view"))
            objectCountsDto.setQcrCount(qcrRepository.count());
        if (securityPermissionService.checkPermission("project", null, "view"))
            objectCountsDto.setProjectsCount(projectRepository.getProjectCount().longValue());
        if (securityPermissionService.checkPermission("requirementdocument", null, "view"))
            objectCountsDto.setRequirementDocumentsCount(requirementDocumentRepository.count());
        if (securityPermissionService.checkPermission("manufacturer", null, "view"))
            objectCountsDto.setMfrCount(manufacturerRepository.count());
        if (securityPermissionService.checkPermission("manufacturerpart", null, "view"))
            objectCountsDto.setMfrPartCount(manufacturerPartRepository.count());
        if (securityPermissionService.checkPermission("mfrsupplier", null, "view"))
            objectCountsDto.setSupplierCount(supplierRepository.count());
        if (securityPermissionService.checkPermission("pgcspecification", null, "view"))
            objectCountsDto.setSpecificationCount(pgcSpecificationRepository.count());
        if (securityPermissionService.checkPermission("pgcdeclaration", null, "view"))
            objectCountsDto.setDeclarationCount(declarationRepository.count());
        if (securityPermissionService.checkPermission("mroasset", null, "view"))
            objectCountsDto.setAssetCount(mroAssetRepository.count());
        if (securityPermissionService.checkPermission("mroworkrequest", null, "view"))
            objectCountsDto.setWorkRequestCount(mroWorkRequestRepository.count());
        if (securityPermissionService.checkPermission("mroworkorder", null, "view"))
            objectCountsDto.setWorkOrderCount(mroWorkOrderRepository.count());
        if (securityPermissionService.checkPermission("mromaintenanceplan", null, "view"))
            objectCountsDto.setMaintenancePlanCount(mroMaintenancePlanRepository.count());
        if (securityPermissionService.checkPermission("plmworkflowdefinition", null, "view"))
            objectCountsDto.setWorkflowCount(workFlowDefinitionRepository.count());
        if (securityPermissionService.checkPermission("productionorder", null, "view"))
            objectCountsDto.setProductionOrderCount(productionOrderRepository.count());
        if (securityPermissionService.checkPermission("pdm_vault", null, "view"))
            objectCountsDto.setVaultCount(pdmVaultRepository.count());
        if (securityPermissionService.checkPermission("plant", null, "view"))
            objectCountsDto.setPlantCount(mesPlantRepository.count());
        if (securityPermissionService.checkPermission("assemblyline", null, "view"))
            objectCountsDto.setAssemblyLineCount(mesAssemblyLineRepository.count());
        if (securityPermissionService.checkPermission("workcenter", null, "view"))
            objectCountsDto.setWorkCenterCount(workCenterRepository.count());
        if (securityPermissionService.checkPermission("machine", null, "view"))
            objectCountsDto.setMachineCount(mesMachineRepository.count());
        if (securityPermissionService.checkPermission("instrument", null, "view"))
            objectCountsDto.setInstrumentCount(mesInstrumentRepository.count());
        if (securityPermissionService.checkPermission("equipment", null, "view"))
            objectCountsDto.setEquipmentCount(mesEquipmentRepository.count());
        if (securityPermissionService.checkPermission("tool", null, "view"))
            objectCountsDto.setToolCount(mesToolRepository.count());
        if (securityPermissionService.checkPermission("material", null, "view"))
            objectCountsDto.setMaterialCount(mesMaterialRepository.count());
        if (securityPermissionService.checkPermission("manpower", null, "view"))
            objectCountsDto.setManPowerCount(mesManpowerRepository.count());
        if (securityPermissionService.checkPermission("jigfixture", null, "view"))
            objectCountsDto.setJigAndFixtureCount(mesJigsFixtureRepository.count());
        if (securityPermissionService.checkPermission("shift", null, "view"))
            objectCountsDto.setShiftCount(mesShiftRepository.count());
        if (securityPermissionService.checkPermission("pgcsubstance", null, "view"))
            objectCountsDto.setSubstanceCount(pgcSubstanceRepository.count());
        if (securityPermissionService.checkPermission("mrometer", null, "view"))
            objectCountsDto.setMeterCount(mroMeterRepository.count());
        if (securityPermissionService.checkPermission("mrosparepart", null, "view"))
            objectCountsDto.setSparePartCount(mroSparePartRepository.count());
        if (securityPermissionService.checkPermission("ppap", null, "view"))
            objectCountsDto.setPpapCount(ppapRepository.count());
        if (securityPermissionService.checkPermission("supplieraudit", null, "view"))
            objectCountsDto.setSupplierAuditCount(supplierAuditRepository.count());
        if (securityPermissionService.checkPermission("mbom", null, "view"))
            objectCountsDto.setMbomCount(mbomRepository.count());
        if (securityPermissionService.checkPermission("program", null, "view"))
            objectCountsDto.setProgramCount(programRepository.count());

        return objectCountsDto;
    }

    private Page<TagDto> getTags(Page<Tag> tags, Pageable pageable) {
        List<TagDto> tagDtos = new LinkedList<>();
        tags.getContent().forEach(tag -> {
            TagDto tagDto = new TagDto();
            tagDto.setId(tag.getId());
            tagDto.setObject(tag.getObject());
            tagDto.setObjectType(tag.getObjectType());
            tagDto.setLabel(tag.getLabel());
            if (tag.getObjectType().name().equals(PLMObjectType.ITEM.name())) {
                PLMItem item = itemRepository.findOne(tag.getObject());
                tagDto.setName(item.getItemName());
                tagDto.setNumber(item.getItemNumber());
                tagDto.setDescription(item.getDescription());
                tagDto.setRevisionId(item.getLatestRevision());
            } else if (tag.getObjectType().name().equals(PLMObjectType.CHANGE.name())) {
                PLMChange plmChange = changeRepository.findOne(tag.getObject());
                if (plmChange != null) {
                    if (plmChange.getChangeType().equals(ChangeType.ECO)) {
                        PLMECO plmeco = ecoRepository.findById(plmChange.getId());
                        if (plmeco != null) {
                            tagDto.setName(plmeco.getTitle());
                            tagDto.setNumber(plmeco.getEcoNumber());
                            tagDto.setDescription(plmeco.getDescription());
                        }
                    } else if (plmChange.getChangeType().equals(ChangeType.DCO)) {
                        PLMDCO plmdco = dcoRepository.findOne(plmChange.getId());
                        tagDto.setName(plmdco.getTitle());
                        tagDto.setNumber(plmdco.getDcoNumber());
                        tagDto.setDescription(plmdco.getDescription());
                    } else if (plmChange.getChangeType().equals(ChangeType.DCR)) {
                        PLMDCR plmdcr = dcrRepository.findOne(plmChange.getId());
                        tagDto.setName(plmdcr.getTitle());
                        tagDto.setNumber(plmdcr.getCrNumber());
                        tagDto.setDescription(plmdcr.getDescriptionOfChange());
                    } else if (plmChange.getChangeType().equals(ChangeType.ECR)) {
                        PLMECR plmecr = ecrRepository.findOne(plmChange.getId());
                        tagDto.setName(plmecr.getTitle());
                        tagDto.setNumber(plmecr.getCrNumber());
                        tagDto.setDescription(plmecr.getDescriptionOfChange());
                    } else if (plmChange.getChangeType().equals(ChangeType.MCO)) {
                        PLMMCO plmmco = mcoRepository.findOne(plmChange.getId());
                        tagDto.setName(plmmco.getTitle());
                        tagDto.setNumber(plmmco.getMcoNumber());
                        tagDto.setDescription(plmmco.getDescription());
                    } else if (plmChange.getChangeType().equals(ChangeType.DEVIATION) || plmChange.getChangeType().equals(ChangeType.WAIVER)) {
                        PLMVariance plmVariance = varianceRepository.findOne(plmChange.getId());
                        tagDto.setName(plmVariance.getTitle());
                        tagDto.setNumber(plmVariance.getVarianceNumber());
                        tagDto.setDescription(plmVariance.getDescription());
                    }
                    tagDto.setObjectType(ObjectType.valueOf(plmChange.getChangeType().toString()));
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.PRODUCTINSPECTIONPLAN.name()) || tag.getObjectType().name().equals(PLMObjectType.MATERIALINSPECTIONPLAN.name())) {
                PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(tag.getObject());
                if (inspectionPlan != null) {
                    tagDto.setName(inspectionPlan.getName());
                    tagDto.setNumber(inspectionPlan.getNumber());
                    tagDto.setDescription(inspectionPlan.getDescription());
                    tagDto.setRevisionId(inspectionPlan.getLatestRevision());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.ITEMINSPECTION.name()) || tag.getObjectType().name().equals(PLMObjectType.MATERIALINSPECTION.name())) {
                PQMInspection inspection = inspectionRepository.findOne(tag.getObject());
                if (inspection != null) {
                    tagDto.setName(inspection.getInspectionNumber());
                    PQMInspectionPlanRevision inspectionPlan = inspectionPlanRevisionRepository.findOne(inspection.getInspectionPlan());
                    if (inspectionPlan != null) {
                        tagDto.setName(inspectionPlan.getPlan().getName());
                    }
                    tagDto.setNumber(inspection.getInspectionNumber());
                    tagDto.setDescription(inspection.getDeviationSummary());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.PROBLEMREPORT.name())) {
                PQMProblemReport problemReport = problemReportRepository.findOne(tag.getObject());
                if (problemReport != null) {
                    tagDto.setName(problemReport.getProblem());
                    tagDto.setNumber(problemReport.getPrNumber());
                    tagDto.setDescription(problemReport.getDescription());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.NCR.name())) {
                PQMNCR pqmncr = ncrRepository.findOne(tag.getObject());
                if (pqmncr != null) {
                    tagDto.setName(pqmncr.getTitle());
                    tagDto.setNumber(pqmncr.getNcrNumber());
                    tagDto.setDescription(pqmncr.getDescription());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.QCR.name())) {
                PQMQCR pqmqcr = qcrRepository.findOne(tag.getObject());
                if (pqmqcr != null) {
                    tagDto.setName(pqmqcr.getTitle());
                    tagDto.setNumber(pqmqcr.getQcrNumber());
                    tagDto.setDescription(pqmqcr.getDescription());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.PPAP.name())) {
                PQMPPAP pqmppap = ppapRepository.findOne(tag.getObject());
                if (pqmppap != null) {
                    tagDto.setName(pqmppap.getName());
                    tagDto.setNumber(pqmppap.getNumber());
                    tagDto.setDescription(pqmppap.getDescription());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.PROJECT.name())) {
                PLMProject project = projectRepository.findOne(tag.getObject());
                if (project != null) {
                    tagDto.setName(project.getName());
                    tagDto.setDescription(project.getDescription());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.PROJECTACTIVITY.name())) {
                PLMActivity activity = activityRepository.findOne(tag.getObject());
                if (activity != null) {
                    PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
                    tagDto.setName(activity.getName());
                    tagDto.setDescription(activity.getDescription());
                    tagDto.setRevisionId(wbsElement.getProject().getId());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.PROJECTTASK.name())) {
                PLMTask task = taskRepository.findOne(tag.getObject());
                if (task != null) {
                    tagDto.setName(task.getName());
                    tagDto.setDescription(task.getDescription());
                    tagDto.setRevisionId(task.getActivity());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.REQUIREMENTDOCUMENT.name())) {
                PLMRequirementDocument requirementDocument = requirementDocumentRepository.findOne(tag.getObject());
                if (requirementDocument != null) {
                    tagDto.setName(requirementDocument.getName());
                    tagDto.setNumber(requirementDocument.getNumber());
                    tagDto.setDescription(requirementDocument.getDescription());
                    tagDto.setRevisionId(requirementDocument.getLatestRevision());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.REQUIREMENT.name())) {
                PLMRequirement requirement = requirementRepository.findOne(tag.getObject());
                if (requirement != null) {
                    tagDto.setName(requirement.getName());
                    tagDto.setNumber(requirement.getNumber());
                    tagDto.setDescription(requirement.getDescription());
                    tagDto.setRevisionId(requirement.getLatestVersion());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.MANUFACTURER.name())) {
                PLMManufacturer manufacturer = manufacturerRepository.findOne(tag.getObject());
                if (manufacturer != null) {
                    tagDto.setName(manufacturer.getName());
                    tagDto.setDescription(manufacturer.getDescription());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.MANUFACTURERPART.name())) {
                PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(tag.getObject());
                if (manufacturerPart != null) {
                    tagDto.setName(manufacturerPart.getPartName());
                    tagDto.setNumber(manufacturerPart.getPartNumber());
                    tagDto.setDescription(manufacturerPart.getDescription());
                    tagDto.setRevisionId(manufacturerPart.getManufacturer());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.MFRSUPPLIER.name())) {
                PLMSupplier plmSupplier = supplierRepository.findOne(tag.getObject());
                if (plmSupplier != null) {
                    tagDto.setName(plmSupplier.getName());
                    tagDto.setDescription(plmSupplier.getDescription());
                }
            } else if (tag.getObjectType().name().equals(PGCEnumObject.PGCSUBSTANCE.name())) {
                PGCSubstance substance = pgcSubstanceRepository.findOne(tag.getObject());
                if (substance != null) {
                    tagDto.setName(substance.getName());
                    tagDto.setNumber(substance.getNumber());
                    tagDto.setDescription(substance.getDescription());
                }
            } else if (tag.getObjectType().name().equals(PGCEnumObject.PGCSPECIFICATION.name())) {
                PGCSpecification pgcSpecification = pgcSpecificationRepository.findOne(tag.getObject());
                if (pgcSpecification != null) {
                    tagDto.setName(pgcSpecification.getName());
                    tagDto.setNumber(pgcSpecification.getNumber());
                    tagDto.setDescription(pgcSpecification.getDescription());
                }
            } else if (tag.getObjectType().name().equals(PGCEnumObject.PGCDECLARATION.name())) {
                PGCDeclaration declaration = declarationRepository.findOne(tag.getObject());
                if (declaration != null) {
                    tagDto.setName(declaration.getName());
                    tagDto.setNumber(declaration.getNumber());
                    tagDto.setDescription(declaration.getDescription());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.CUSTOMOBJECT.name())) {
                CustomObject customObject = customObjectRepository.findOne(tag.getObject());
                if (customObject != null) {
                    tagDto.setName(customObject.getName());
                    tagDto.setNumber(customObject.getNumber());
                    tagDto.setDescription(customObject.getDescription());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.CUSTOMER.name())) {
                PQMCustomer customer = pqmCustomerRepository.findOne(tag.getObject());
                if (customer != null) {
                    tagDto.setName(customer.getName());
                }
            } else if (tag.getObjectType().name().equals(PLMObjectType.PLANT.name()) || tag.getObjectType().name().equals(MESEnumObject.ASSEMBLYLINE.name())
                    || tag.getObjectType().name().equals(MESEnumObject.WORKCENTER.name()) || tag.getObjectType().name().equals(MESEnumObject.INSTRUMENT.name())
                    || tag.getObjectType().name().equals(MESEnumObject.EQUIPMENT.name()) || tag.getObjectType().name().equals(MESEnumObject.TOOL.name())
                    || tag.getObjectType().name().equals(MESEnumObject.MACHINE.name()) || tag.getObjectType().name().equals(MESEnumObject.MATERIAL.name())
                    || tag.getObjectType().name().equals(MESEnumObject.JIGFIXTURE.name()) || tag.getObjectType().name().equals(MESEnumObject.MANPOWER.name())
                    || tag.getObjectType().name().equals(MESEnumObject.SHIFT.name())) {
                MESObject plant = mesObjectRepository.findOne(tag.getObject());
                if (plant != null) {
                    tagDto.setName(plant.getName());
                    tagDto.setNumber(plant.getNumber());
                    tagDto.setDescription(plant.getDescription());
                }
            } else if (tag.getObjectType().name().equals(MROEnumObject.MROASSET.name()) || tag.getObjectType().name().equals(MROEnumObject.MROMETER.name())
                    || tag.getObjectType().name().equals(MROEnumObject.MROSPAREPART.name()) || tag.getObjectType().name().equals(MROEnumObject.MROMAINTENANCEPLAN.name())
                    || tag.getObjectType().name().equals(MROEnumObject.MROWORKREQUEST.name()) || tag.getObjectType().name().equals(MROEnumObject.MROWORKORDER.name())) {
                MROObject mroObject = mroObjectRepository.findOne(tag.getObject());
                if (mroObject != null) {
                    tagDto.setName(mroObject.getName());
                    tagDto.setNumber(mroObject.getNumber());
                    tagDto.setDescription(mroObject.getDescription());
                }
            }

            tagDtos.add(tagDto);
        });
        return new PageImpl<TagDto>(tagDtos, pageable, tags.getTotalElements());
    }
}
