package com.cassinisys.plm.service.pqm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.GroupMember;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.model.security.Privilege;
import com.cassinisys.platform.repo.col.AttributeAttachmentRepository;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.common.GroupMemberRepository;
import com.cassinisys.platform.repo.common.PersonGroupRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.*;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.repo.security.PrivilegeRepository;
import com.cassinisys.platform.service.col.AttributeAttachmentService;
import com.cassinisys.platform.service.common.ForgeService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.*;
import com.cassinisys.plm.model.cm.PLMChange;
import com.cassinisys.plm.model.cm.PLMChangeFile;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.mro.MROObject;
import com.cassinisys.plm.model.mro.MROObjectFile;
import com.cassinisys.plm.model.pdm.PDMConstants;
import com.cassinisys.plm.model.pgc.PGCObject;
import com.cassinisys.plm.model.pgc.PGCObjectFile;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.model.req.PLMRequirementDocumentChildren;
import com.cassinisys.plm.model.req.PLMRequirementDocumentFile;
import com.cassinisys.plm.model.req.PLMRequirementFile;
import com.cassinisys.plm.model.rm.PLMGlossaryFile;
import com.cassinisys.plm.repo.cm.ChangeFileRepository;
import com.cassinisys.plm.repo.cm.ChangeRepository;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mfr.*;
import com.cassinisys.plm.repo.mro.MROObjectFileRepository;
import com.cassinisys.plm.repo.mro.MROObjectRepository;
import com.cassinisys.plm.repo.pgc.PGCObjectFileRepository;
import com.cassinisys.plm.repo.pgc.PGCObjectRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.req.PLMRequirementDocumentFileRepository;
import com.cassinisys.plm.repo.req.PLMRequirementDocumentRevisionRepository;
import com.cassinisys.plm.repo.req.PLMRequirementFileRepository;
import com.cassinisys.plm.repo.req.RequirementDocumentChildrenRepository;
import com.cassinisys.plm.repo.rm.GlossaryFileRepository;
import com.cassinisys.plm.repo.rm.RmObjectFileRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.cm.ChangeFileService;
import com.cassinisys.plm.service.mes.*;
import com.cassinisys.plm.service.mfr.ManufacturerFileService;
import com.cassinisys.plm.service.mfr.ManufacturerPartFileService;
import com.cassinisys.plm.service.mfr.SupplierFileService;
import com.cassinisys.plm.service.plm.DocumentService;
import com.cassinisys.plm.service.plm.FileHelpers;
import com.cassinisys.plm.service.plm.ItemFileService;
import com.cassinisys.plm.service.plm.NprFileService;
import com.cassinisys.plm.service.pm.*;
import com.cassinisys.plm.service.req.ReqDocumentFileService;
import com.cassinisys.plm.service.req.RequirementFileService;
import com.cassinisys.plm.service.rm.GlossaryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyam on 09/06/20.
 */
@Service
@Transactional
public class ObjectFileService implements CrudService<PLMFile, Integer> {

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private AutoNumberService autoNumberService;

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private ForgeService forgeService;

    @Autowired
    private FileRepository fileRepository;

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
    private InspectionRepository inspectionRepository;
    @Autowired
    private InspectionPlanRepository inspectionPlanRepository;

    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;

    @Autowired
    private ProblemReportRepository problemReportRepository;

    @Autowired
    private NCRRepository ncrRepository;

    @Autowired
    private QCRRepository qcrRepository;
    @Autowired
    private ItemFileService itemFileService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProgramTemplateService programTemplateService;
    @Autowired
    private ProjectTemplateService projectTemplateService;
    @Autowired
    private ProjectTemplateActivityService projectTemplateActivityService;
    @Autowired
    private ItemFileRepository itemFileRepository;
    @Autowired
    private FileDownloadHistoryRepository fileDownloadHistoryRepository;
    @Autowired
    private FileHelpers fileHelpers;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AttributeAttachmentService attributeAttachmentService;
    @Autowired
    private AttributeAttachmentRepository attributeAttachmentRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ProjectFileRepository projectFileRepository;
    @Autowired
    private ProgramTemplateFileRepository programTemplateFileRepository;
    @Autowired
    private ProjectTemplateFileRepository projectTemplateFileRepository;
    @Autowired
    private ProjectTemplateActivityFileRepository projectTemplateActivityFileRepository;
    @Autowired
    private ProjectTemplateTaskFileRepository projectTemplateTaskFileRepository;
    @Autowired
    private ActivityFileRepository activityFileRepository;
    @Autowired
    private TaskFileRepository taskFileRepository;
    @Autowired
    private ManufacturerFileRepository manufacturerFileRepository;
    @Autowired
    private ManufacturerPartFileRepository manufacturerPartFileRepository;
    @Autowired
    private ManufacturerFileService manufacturerFileService;
    @Autowired
    private ManufacturerPartFileService manufacturerPartFileService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private GlossaryService glossaryService;
    @Autowired
    private BOPFileService bopFileService;
    @Autowired
    private BOPPlanFileService bopPlanFileService;
    @Autowired
    private BOPInstanceOperationFileService bopInstanceOperationFileService;
    @Autowired
    private BOPInstanceOperationFileRepository bopInstanceOperationFileRepository;
    @Autowired
    private GlossaryFileRepository glossaryFileRepository;
    @Autowired
    private RmObjectFileRepository rmObjectFileRepository;
    @Autowired
    private ChangeFileRepository changeFileRepository;
    @Autowired
    private ChangeRepository changeRepository;
    @Autowired
    private ChangeFileService changeFileService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private MESObjectFileRepository mesObjectFileRepository;
    @Autowired
    private MESObjectRepository mesObjectRepository;
    @Autowired
    private MROObjectRepository mroObjectRepository;
    @Autowired
    private MROObjectFileRepository mroObjectFileRepository;
    @Autowired
    private PGCObjectRepository pgcObjectRepository;
    @Autowired
    private SupplierAuditRepository supplierAuditRepository;
    @Autowired
    private PGCObjectFileRepository pgcObjectFileRepository;
    @Autowired
    private PPAPChecklistRepository ppapChecklistRepository;
    @Autowired
    private SupplierFileService supplierFileService;
    @Autowired
    private SupplierFileRepository supplierFileRepository;
    @Autowired
    private ReqDocumentFileService reqDocumentFileService;
    @Autowired
    private PLMRequirementDocumentFileRepository requirementDocumentFileRepository;
    @Autowired
    private RequirementFileService requirementFileService;
    @Autowired
    private PLMRequirementFileRepository requirementFileRepository;
    @Autowired
    private CustomerFileService customerFileService;
    @Autowired
    private CustomerFileRepository customerFileRepository;
    @Autowired
    private NprFileService nprFileService;
    @Autowired
    private MBOMFileService mbomFileService;
    @Autowired
    private MBOMInstanceFileService mbomInstanceFileService;
    @Autowired
    private ProgramFileService programFileService;
    @Autowired
    private NprFileRepository nprFileRepository;
    @Autowired
    private MBOMFileRepository mbomFileRepository;
    @Autowired
    private MBOMInstanceFileRepository mbomInstanceFileRepository;
    @Autowired
    private ProgramFileRepository programFileRepository;
    @Autowired
    private ObjectFileService qualityFileService;
    @Autowired
    private FileDownloadService fileDownloadService;
    @Autowired
    private PLMDocumentRepository plmDocumentRepository;
    @Autowired
    private MfrPartInspectionReportRepository mfrPartInspectionReportRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private PLMRequirementDocumentRevisionRepository requirementDocumentRevisionRepository;
    @Autowired
    private NprRepository nprRepository;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private DocumentReviewerRepository documentReviewerRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private RequirementDocumentChildrenRepository requirementDocumentChildrenRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private SupplierAuditFileRepository supplierAuditFileRepository;
    @Autowired
    private PPAPRepository ppapRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private PersonGroupRepository personGroupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private BOPFileRepository bopFileRepository;
    @Autowired
    private BOPOperationFileRepository bopOperationFileRepository;
    @Autowired
    private SharedObjectRepository sharedObjectRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public PLMFile create(PLMFile plmFile) {
        return null;
    }

    @Override
    public PLMFile update(PLMFile plmFile) {
        return null;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public PLMFile get(Integer integer) {
        return null;
    }

    @Override
    public List<PLMFile> getAll() {
        return null;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create',#type) || @documentService.checkDMPemrmissions(authentication, 'create', #type, #folderId) || @customePrivilegeFilter.filterPrivilage(authentication,'teamCreate',#type)")
    public ObjectFileDto uploadObjectFiles(Integer id, PLMObjectType objectType, Integer folderId, Map<String, MultipartFile> fileMap, String type) throws CassiniException, JsonProcessingException {
        ObjectFileDto objectFileDto = new ObjectFileDto();

        List<PLMFile> uploaded = new ArrayList<>();
        List<PLMFile> versionedFiles = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");

        PQMInspectionPlanRevision planRevision = null;
        PQMInspectionPlan plan = null;
        PQMInspection inspection = null;
        PQMProblemReport problemReport = null;
        PQMNCR ncr = null;
        PQMQCR qcr = null;
        MESObject mesObject = null;
        PLMDocument document = null;
        MROObject mroObject = null;
        PGCObject pgcObject = null;
        PQMSupplierAudit supplierAudit = null;

        if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
            planRevision = inspectionPlanRevisionRepository.findOne(id);
            plan = inspectionPlanRepository.findOne(planRevision.getPlan().getId());
        } else if (objectType.equals(PLMObjectType.INSPECTION)) {
            inspection = inspectionRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            problemReport = problemReportRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.NCR)) {
            ncr = ncrRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.QCR)) {
            qcr = qcrRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
            mesObject = mesObjectRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
            document = plmDocumentRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
            mroObject = mroObjectRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
            pgcObject = pgcObjectRepository.findOne(id);
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            supplierAudit = supplierAuditRepository.findOne(id);
        }

        String[] fileExtension = null;
        boolean flag = true;

        List<PLMFile> fileList = new ArrayList<>();
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        String fNames = null;
        try {
            if (objectType.equals(PLMObjectType.INSPECTIONPLAN) || objectType.equals(PLMObjectType.INSPECTION) || objectType.equals(PLMObjectType.PROBLEMREPORT)
                    || objectType.equals(PLMObjectType.NCR) || objectType.equals(PLMObjectType.QCR) || objectType.equals(PLMObjectType.MESOBJECT) || objectType.equals(PLMObjectType.DOCUMENT)
                    || objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT) || objectType.equals(PLMObjectType.MROOBJECT) || objectType.equals(PLMObjectType.PGCOBJECT)
                    || objectType.equals(PLMObjectType.PPAPCHECKLIST) || objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
                for (MultipartFile file : fileMap.values()) {
                    Boolean versioned = false;

                    String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                    if (fileExtension != null) {
                        for (String ext : fileExtension) {
                            if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        PQMInspectionPlanFile inspectionPlanFile = null;
                        PQMInspectionFile inspectionFile = null;
                        PQMProblemReportFile problemReportFile = null;
                        PQMNCRFile ncrFile = null;
                        PQMQCRFile qcrFile = null;
                        MESObjectFile mesObjectFile = null;
                        PLMDocument plmDocument = null;
                        PLMMfrPartInspectionReport plmMfrPartInspectionReport = null;
                        MROObjectFile mroObjectFile = null;
                        PGCObjectFile pgcObjectFile = null;
                        PQMSupplierAuditFile supplierAuditFile = null;
                        PQMPPAPChecklist ppapChecklist = null;

                        Integer version = 1;
                        Integer oldVersion = 1;
                        String autoNumber1 = null;
                        PLMFile qualityFile = null;
                        Integer oldFile = null;

                        if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
                            if (folderId == 0) {
                                inspectionPlanFile = inspectionPlanFileRepository.findByInspectionPlanAndNameAndParentFileIsNullAndLatestTrue(id, name);
                            } else {
                                inspectionPlanFile = inspectionPlanFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                            }

                            if (inspectionPlanFile != null) {
                                inspectionPlanFile.setLatest(false);
                                oldVersion = inspectionPlanFile.getVersion();
                                version = oldVersion + 1;
                                autoNumber1 = inspectionPlanFile.getFileNo();
                                oldFile = inspectionPlanFile.getId();
                                inspectionPlanFileRepository.save(inspectionPlanFile);
                                versioned = true;

                            }
                            if (inspectionPlanFile == null) {
                                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                                if (autoNumber != null) {
                                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                                }
                            }

                            inspectionPlanFile = new PQMInspectionPlanFile();
                            inspectionPlanFile.setInspectionPlan(id);
                            inspectionPlanFile.setName(name);
                            inspectionPlanFile.setCreatedBy(login.getPerson().getId());
                            inspectionPlanFile.setModifiedBy(login.getPerson().getId());
                            inspectionPlanFile.setSize(file.getSize());
                            inspectionPlanFile.setFileType("FILE");
                            inspectionPlanFile.setFileNo(autoNumber1);
                            inspectionPlanFile.setVersion(version);
                            if (folderId != 0) {
                                inspectionPlanFile.setParentFile(folderId);
                            }
                            inspectionPlanFile = inspectionPlanFileRepository.save(inspectionPlanFile);
                            if (inspectionPlanFile.getParentFile() != null) {
                                PQMInspectionPlanFile planFile = inspectionPlanFileRepository.findOne(inspectionPlanFile.getParentFile());
                                planFile.setModifiedDate(inspectionPlanFile.getModifiedDate());
                                planFile = inspectionPlanFileRepository.save(planFile);
                            }
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(id, objectType, inspectionPlanFile.getId()));
                            qualityFile = fileRepository.findOne(inspectionPlanFile.getId());
                            qualityFile.setPrevisionFileId(oldFile);
                            if (inspectionPlanFile.getVersion() > 1) {
                                copyFileAttributes(oldFile, inspectionPlanFile.getId());
                            }
                        } else if (objectType.equals(PLMObjectType.INSPECTION)) {
                            if (folderId == 0) {
                                inspectionFile = inspectionFileRepository.findByInspectionAndNameAndParentFileIsNullAndLatestTrue(id, name);
                            } else {
                                inspectionFile = inspectionFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                            }
                            if (inspectionFile != null) {
                                inspectionFile.setLatest(false);
                                oldVersion = inspectionFile.getVersion();
                                version = oldVersion + 1;
                                autoNumber1 = inspectionFile.getFileNo();
                                oldFile = inspectionFile.getId();
                                inspectionFileRepository.save(inspectionFile);
                                versioned = true;

                            }
                            if (inspectionFile == null) {
                                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                                if (autoNumber != null) {
                                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                                }
                            }

                            inspectionFile = new PQMInspectionFile();
                            inspectionFile.setInspection(id);
                            inspectionFile.setName(name);
                            inspectionFile.setCreatedBy(login.getPerson().getId());
                            inspectionFile.setModifiedBy(login.getPerson().getId());
                            inspectionFile.setSize(file.getSize());
                            inspectionFile.setFileType("FILE");
                            inspectionFile.setFileNo(autoNumber1);
                            inspectionFile.setVersion(version);
                            if (folderId != 0) {
                                inspectionFile.setParentFile(folderId);
                            }
                            inspectionFile = inspectionFileRepository.save(inspectionFile);
                            if (inspectionFile.getParentFile() != null) {
                                PLMFile file1 = fileRepository.findOne(inspectionFile.getParentFile());
                                file1.setModifiedDate(inspectionFile.getModifiedDate());
                                file1 = fileRepository.save(file1);
                            }
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(inspectionFile.getInspection(), objectType, inspectionFile.getId()));
                            qualityFile = fileRepository.findOne(inspectionFile.getId());
                            qualityFile.setPrevisionFileId(oldFile);
                            if (inspectionFile.getVersion() > 1) {
                                copyFileAttributes(oldFile, inspectionFile.getId());
                            }
                        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
                            if (folderId == 0) {
                                problemReportFile = problemReportFileRepository.findByProblemReportAndNameAndParentFileIsNullAndLatestTrue(id, name);
                            } else {
                                problemReportFile = problemReportFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                            }
                            if (problemReportFile != null) {
                                problemReportFile.setLatest(false);
                                oldVersion = problemReportFile.getVersion();
                                version = oldVersion + 1;
                                autoNumber1 = problemReportFile.getFileNo();
                                oldFile = problemReportFile.getId();
                                problemReportFileRepository.save(problemReportFile);
                                versioned = true;

                            }
                            if (problemReportFile == null) {
                                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                                if (autoNumber != null) {
                                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                                }
                            }
                            problemReportFile = new PQMProblemReportFile();
                            problemReportFile.setProblemReport(id);
                            problemReportFile.setName(name);
                            problemReportFile.setCreatedBy(login.getPerson().getId());
                            problemReportFile.setModifiedBy(login.getPerson().getId());
                            problemReportFile.setSize(file.getSize());
                            problemReportFile.setFileType("FILE");
                            problemReportFile.setFileNo(autoNumber1);
                            problemReportFile.setVersion(version);
                            if (folderId != 0) {
                                problemReportFile.setParentFile(folderId);
                            }
                            problemReportFile = problemReportFileRepository.save(problemReportFile);
                            if (problemReportFile.getParentFile() != null) {
                                PQMProblemReportFile reportFile = problemReportFileRepository.findOne(problemReportFile.getParentFile());
                                reportFile.setModifiedDate(problemReportFile.getModifiedDate());
                                reportFile = problemReportFileRepository.save(reportFile);
                            }
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(problemReportFile.getProblemReport(), objectType, problemReportFile.getId()));
                            qualityFile = fileRepository.findOne(problemReportFile.getId());
                            qualityFile.setPrevisionFileId(oldFile);
                            if (problemReportFile.getVersion() > 1) {
                                copyFileAttributes(oldFile, problemReportFile.getId());
                            }
                        } else if (objectType.equals(PLMObjectType.NCR)) {
                            if (folderId == 0) {
                                ncrFile = ncrFileRepository.findByNcrAndNameAndParentFileIsNullAndLatestTrue(id, name);
                            } else {
                                ncrFile = ncrFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                            }
                            if (ncrFile != null) {
                                ncrFile.setLatest(false);
                                oldVersion = ncrFile.getVersion();
                                version = oldVersion + 1;
                                autoNumber1 = ncrFile.getFileNo();
                                oldFile = ncrFile.getId();
                                ncrFileRepository.save(ncrFile);
                                versioned = true;
                            }
                            if (ncrFile == null) {
                                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                                if (autoNumber != null) {
                                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                                }
                            }

                            ncrFile = new PQMNCRFile();
                            ncrFile.setNcr(id);
                            ncrFile.setName(name);
                            ncrFile.setCreatedBy(login.getPerson().getId());
                            ncrFile.setModifiedBy(login.getPerson().getId());
                            ncrFile.setSize(file.getSize());
                            ncrFile.setFileType("FILE");
                            ncrFile.setFileNo(autoNumber1);
                            ncrFile.setVersion(version);
                            if (folderId != 0) {
                                ncrFile.setParentFile(folderId);
                            }
                            ncrFile = ncrFileRepository.save(ncrFile);
                            if (ncrFile.getParentFile() != null) {
                                PQMNCRFile pqmncrFile = ncrFileRepository.findOne(ncrFile.getParentFile());
                                pqmncrFile.setModifiedDate(ncrFile.getModifiedDate());
                                pqmncrFile = ncrFileRepository.save(pqmncrFile);
                            }
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(ncrFile.getNcr(), objectType, ncrFile.getId()));
                            qualityFile = fileRepository.findOne(ncrFile.getId());
                            qualityFile.setPrevisionFileId(oldFile);
                            if (ncrFile.getVersion() > 1) {
                                copyFileAttributes(oldFile, ncrFile.getId());
                            }
                        } else if (objectType.equals(PLMObjectType.QCR)) {
                            if (folderId == 0) {
                                qcrFile = qcrFileRepository.findByQcrAndNameAndParentFileIsNullAndLatestTrue(id, name);
                            } else {
                                qcrFile = qcrFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                            }
                            if (qcrFile != null) {
                                qcrFile.setLatest(false);
                                oldVersion = qcrFile.getVersion();
                                version = oldVersion + 1;
                                autoNumber1 = qcrFile.getFileNo();
                                oldFile = qcrFile.getId();
                                qcrFileRepository.save(qcrFile);
                                versioned = true;

                            }
                            if (qcrFile == null) {
                                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                                if (autoNumber != null) {
                                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                                }
                            }
                            qcrFile = new PQMQCRFile();
                            qcrFile.setQcr(id);
                            qcrFile.setName(name);
                            qcrFile.setCreatedBy(login.getPerson().getId());
                            qcrFile.setModifiedBy(login.getPerson().getId());
                            qcrFile.setSize(file.getSize());
                            qcrFile.setFileType("FILE");
                            qcrFile.setFileNo(autoNumber1);
                            qcrFile.setVersion(version);
                            if (folderId != 0) {
                                qcrFile.setParentFile(folderId);
                            }
                            qcrFile = qcrFileRepository.save(qcrFile);
                            if (qcrFile.getParentFile() != null) {
                                PQMQCRFile pqmqcrFile = qcrFileRepository.findOne(qcrFile.getParentFile());
                                pqmqcrFile.setModifiedDate(qcrFile.getModifiedDate());
                                pqmqcrFile = qcrFileRepository.save(pqmqcrFile);
                            }
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(qcrFile.getQcr(), objectType, qcrFile.getId()));
                            qualityFile = fileRepository.findOne(qcrFile.getId());
                            qualityFile.setPrevisionFileId(oldFile);
                            if (qcrFile.getVersion() > 1) {
                                copyFileAttributes(oldFile, qcrFile.getId());
                            }
                        } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
                            if (folderId == 0) {
                                mesObjectFile = mesObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, name);
                            } else {
                                mesObjectFile = mesObjectFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                            }
                            if (mesObjectFile != null) {
                                mesObjectFile.setLatest(false);
                                oldVersion = mesObjectFile.getVersion();
                                version = oldVersion + 1;
                                autoNumber1 = mesObjectFile.getFileNo();
                                oldFile = mesObjectFile.getId();
                                mesObjectFileRepository.save(mesObjectFile);
                                versioned = true;

                            }
                            if (mesObjectFile == null) {
                                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                                if (autoNumber != null) {
                                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                                }
                            }
                            mesObjectFile = new MESObjectFile();
                            mesObjectFile.setObject(id);
                            mesObjectFile.setName(name);
                            mesObjectFile.setCreatedBy(login.getPerson().getId());
                            mesObjectFile.setModifiedBy(login.getPerson().getId());
                            mesObjectFile.setSize(file.getSize());
                            mesObjectFile.setFileType("FILE");
                            mesObjectFile.setFileNo(autoNumber1);
                            mesObjectFile.setVersion(version);
                            if (folderId != 0) {
                                mesObjectFile.setParentFile(folderId);
                            }
                            mesObjectFile = mesObjectFileRepository.save(mesObjectFile);
                            if (mesObjectFile.getParentFile() != null) {
                                MESObjectFile objectFile = mesObjectFileRepository.findOne(mesObjectFile.getParentFile());
                                objectFile.setModifiedDate(mesObjectFile.getModifiedDate());
                                objectFile = mesObjectFileRepository.save(objectFile);
                            }
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(mesObjectFile.getObject(), objectType, mesObjectFile.getId()));
                            qualityFile = fileRepository.findOne(mesObjectFile.getId());
                            qualityFile.setPrevisionFileId(oldFile);
                            if (mesObjectFile.getVersion() > 1) {
                                copyFileAttributes(oldFile, mesObjectFile.getId());
                            }
                        } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
                            String revision = null;
                            PLMLifeCyclePhase lifeCyclePhase = null;
                            PLMDocument oldDocument = null;
                            if (folderId == 0) {
                                plmDocument = plmDocumentRepository.findByNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(name);
                            } else {
                                plmDocument = plmDocumentRepository.findByNameEqualsIgnoreCaseAndParentFileAndLatestTrue(name, folderId);
                            }
                            if (plmDocument != null) {
                                if (plmDocument.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                                    String message = messageSource.getMessage("document_already_released", null, "Released document(s) cannot be updated", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", plmDocument.getName());
                                    throw new CassiniException(result);
                                }
                                plmDocument.setLatest(false);
                                oldVersion = plmDocument.getVersion();
                                version = oldVersion + 1;
                                revision = plmDocument.getRevision();
                                lifeCyclePhase = plmDocument.getLifeCyclePhase();
                                autoNumber1 = plmDocument.getFileNo();
                                oldFile = plmDocument.getId();
                                plmDocument = plmDocumentRepository.save(plmDocument);
                                versioned = true;
                                oldDocument = JsonUtils.cloneEntity(plmDocument, PLMDocument.class);

                            }
                            if (plmDocument == null) {
                                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                                if (autoNumber != null) {
                                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                                }
                            }
                            plmDocument = new PLMDocument();
                            plmDocument.setName(name);
                            plmDocument.setCreatedBy(login.getPerson().getId());
                            plmDocument.setModifiedBy(login.getPerson().getId());
                            plmDocument.setSize(file.getSize());
                            plmDocument.setFileType("FILE");
                            plmDocument.setFileNo(autoNumber1);
                            plmDocument.setVersion(version);
                            if (revision == null) {
                                String newRevision = setRevisionAndLifecyclePhase();
                                PLMLifeCyclePhase plmLifeCyclePhase = setLifecyclePhase();
                                plmDocument.setRevision(newRevision);
                                plmDocument.setLifeCyclePhase(plmLifeCyclePhase);
                            } else {
                                plmDocument.setRevision(revision);
                                List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(lifeCyclePhase.getLifeCycle(), LifeCyclePhaseType.PRELIMINARY);
                                if (lifeCyclePhases.size() > 0) {
                                    plmDocument.setLifeCyclePhase(lifeCyclePhases.get(0));
                                }
                            }
                            if (folderId != 0) {
                                plmDocument.setParentFile(folderId);
                            }
                            plmDocument = plmDocumentRepository.save(plmDocument);
                            if (oldDocument != null) {
                                documentService.copyReviewers(oldDocument.getId(), plmDocument.getId());
                            }
                            if (plmDocument.getParentFile() != null) {
                                PLMDocument objectFile = plmDocumentRepository.findOne(plmDocument.getParentFile());
                                objectFile.setModifiedDate(plmDocument.getModifiedDate());
                                objectFile = plmDocumentRepository.save(objectFile);
                            }
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(0, objectType, plmDocument.getId()));
                            qualityFile = fileRepository.findOne(plmDocument.getId());
                            qualityFile.setPrevisionFileId(oldFile);
                            if (plmDocument.getVersion() > 1) {
                                copyFileAttributes(oldFile, plmDocument.getId());
                            }

                        } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                            String revision = null;
                            PLMLifeCyclePhase lifeCyclePhase = null;
                            PLMMfrPartInspectionReport oldMfrPartInspectionReport = null;
                            if (folderId == 0) {
                                plmMfrPartInspectionReport = mfrPartInspectionReportRepository.findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(id, name);
                            } else {
                                plmMfrPartInspectionReport = mfrPartInspectionReportRepository.findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileAndLatestTrue(id, name, folderId);
                            }
                            if (plmMfrPartInspectionReport != null) {
                                if (plmMfrPartInspectionReport.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                                    String message = messageSource.getMessage("document_already_released", null, "Released document(s) cannot be updated", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", plmDocument.getName());
                                    throw new CassiniException(result);
                                }
                                plmMfrPartInspectionReport.setLatest(false);
                                oldVersion = plmMfrPartInspectionReport.getVersion();
                                version = oldVersion + 1;
                                revision = plmMfrPartInspectionReport.getRevision();
                                lifeCyclePhase = plmMfrPartInspectionReport.getLifeCyclePhase();
                                autoNumber1 = plmMfrPartInspectionReport.getFileNo();
                                oldFile = plmMfrPartInspectionReport.getId();
                                plmMfrPartInspectionReport = mfrPartInspectionReportRepository.save(plmMfrPartInspectionReport);
                                versioned = true;
                                oldMfrPartInspectionReport = JsonUtils.cloneEntity(plmMfrPartInspectionReport, PLMMfrPartInspectionReport.class);

                            }
                            if (plmMfrPartInspectionReport == null) {
                                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                                if (autoNumber != null) {
                                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                                }
                            }
                            plmMfrPartInspectionReport = new PLMMfrPartInspectionReport();
                            plmMfrPartInspectionReport.setName(name);
                            plmMfrPartInspectionReport.setCreatedBy(login.getPerson().getId());
                            plmMfrPartInspectionReport.setModifiedBy(login.getPerson().getId());
                            plmMfrPartInspectionReport.setSize(file.getSize());
                            plmMfrPartInspectionReport.setFileType("FILE");
                            plmMfrPartInspectionReport.setFileNo(autoNumber1);
                            plmMfrPartInspectionReport.setVersion(version);
                            plmMfrPartInspectionReport.setManufacturerPart(id);
                            if (revision == null) {
                                String newRevision = setRevisionAndLifecyclePhase();
                                PLMLifeCyclePhase plmLifeCyclePhase = setLifecyclePhase();
                                plmMfrPartInspectionReport.setRevision(newRevision);
                                plmMfrPartInspectionReport.setLifeCyclePhase(plmLifeCyclePhase);
                            } else {
                                plmMfrPartInspectionReport.setRevision(revision);
                                List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(lifeCyclePhase.getLifeCycle(), LifeCyclePhaseType.PRELIMINARY);
                                if (lifeCyclePhases.size() > 0) {
                                    plmMfrPartInspectionReport.setLifeCyclePhase(lifeCyclePhases.get(0));
                                }
                            }
                            if (folderId != 0) {
                                plmMfrPartInspectionReport.setParentFile(folderId);
                            }
                            plmMfrPartInspectionReport = mfrPartInspectionReportRepository.save(plmMfrPartInspectionReport);
                            if (oldMfrPartInspectionReport != null) {
                                documentService.copyReviewers(oldMfrPartInspectionReport.getId(), plmMfrPartInspectionReport.getId());
                            }
                            if (plmMfrPartInspectionReport.getParentFile() != null) {
                                PLMMfrPartInspectionReport objectFile = mfrPartInspectionReportRepository.findOne(plmMfrPartInspectionReport.getParentFile());
                                objectFile.setModifiedDate(plmMfrPartInspectionReport.getModifiedDate());
                                objectFile = mfrPartInspectionReportRepository.save(objectFile);
                            }
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(0, objectType, plmMfrPartInspectionReport.getId()));
                            qualityFile = fileRepository.findOne(plmMfrPartInspectionReport.getId());
                            qualityFile.setPrevisionFileId(oldFile);
                            if (plmMfrPartInspectionReport.getVersion() > 1) {
                                copyFileAttributes(oldFile, plmMfrPartInspectionReport.getId());
                            }

                        } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
                            if (folderId == 0) {
                                mroObjectFile = mroObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, name);
                            } else {
                                mroObjectFile = mroObjectFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                            }
                            if (mroObjectFile != null) {
                                mroObjectFile.setLatest(false);
                                oldVersion = mroObjectFile.getVersion();
                                version = oldVersion + 1;
                                autoNumber1 = mroObjectFile.getFileNo();
                                oldFile = mroObjectFile.getId();
                                mroObjectFileRepository.save(mroObjectFile);
                                versioned = true;

                            }
                            if (mroObjectFile == null) {
                                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                                if (autoNumber != null) {
                                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                                }
                            }
                            mroObjectFile = new MROObjectFile();
                            mroObjectFile.setObject(id);
                            mroObjectFile.setName(name);
                            mroObjectFile.setCreatedBy(login.getPerson().getId());
                            mroObjectFile.setModifiedBy(login.getPerson().getId());
                            mroObjectFile.setSize(file.getSize());
                            mroObjectFile.setFileType("FILE");
                            mroObjectFile.setFileNo(autoNumber1);
                            mroObjectFile.setVersion(version);
                            if (folderId != 0) {
                                mroObjectFile.setParentFile(folderId);
                            }
                            mroObjectFile = mroObjectFileRepository.save(mroObjectFile);
                            if (mroObjectFile.getParentFile() != null) {
                                MROObjectFile parent = mroObjectFileRepository.findOne(mroObjectFile.getParentFile());
                                parent.setModifiedDate(mroObjectFile.getModifiedDate());
                                parent = mroObjectFileRepository.save(parent);
                            }
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(mroObjectFile.getObject(), objectType, mroObjectFile.getId()));
                            qualityFile = fileRepository.findOne(mroObjectFile.getId());
                            qualityFile.setPrevisionFileId(oldFile);
                            if (mroObjectFile.getVersion() > 1) {
                                copyFileAttributes(oldFile, mroObjectFile.getId());
                            }
                        } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
                            if (folderId == 0) {
                                pgcObjectFile = pgcObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, name);
                            } else {
                                pgcObjectFile = pgcObjectFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                            }
                            if (pgcObjectFile != null) {
                                pgcObjectFile.setLatest(false);
                                oldVersion = pgcObjectFile.getVersion();
                                version = oldVersion + 1;
                                autoNumber1 = pgcObjectFile.getFileNo();
                                oldFile = pgcObjectFile.getId();
                                pgcObjectFileRepository.save(pgcObjectFile);
                                versioned = true;

                            }
                            if (pgcObjectFile == null) {
                                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                                if (autoNumber != null) {
                                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                                }
                            }
                            pgcObjectFile = new PGCObjectFile();
                            pgcObjectFile.setObject(id);
                            pgcObjectFile.setName(name);
                            pgcObjectFile.setCreatedBy(login.getPerson().getId());
                            pgcObjectFile.setModifiedBy(login.getPerson().getId());
                            pgcObjectFile.setSize(file.getSize());
                            pgcObjectFile.setFileType("FILE");
                            pgcObjectFile.setFileNo(autoNumber1);
                            pgcObjectFile.setVersion(version);
                            if (folderId != 0) {
                                pgcObjectFile.setParentFile(folderId);
                            }
                            pgcObjectFile = pgcObjectFileRepository.save(pgcObjectFile);
                            if (pgcObjectFile.getParentFile() != null) {
                                PGCObjectFile parent = pgcObjectFileRepository.findOne(pgcObjectFile.getParentFile());
                                parent.setModifiedDate(pgcObjectFile.getModifiedDate());
                                parent = pgcObjectFileRepository.save(parent);
                            }
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(pgcObjectFile.getObject(), objectType, pgcObjectFile.getId()));
                            qualityFile = fileRepository.findOne(pgcObjectFile.getId());
                            qualityFile.setPrevisionFileId(oldFile);
                            if (pgcObjectFile.getVersion() > 1) {
                                copyFileAttributes(oldFile, pgcObjectFile.getId());
                            }
                        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
                            if (folderId == 0) {
                                supplierAuditFile = supplierAuditFileRepository.findBySupplierAuditAndNameAndParentFileIsNullAndLatestTrue(id, name);
                            } else {
                                supplierAuditFile = supplierAuditFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                            }
                            if (supplierAuditFile != null) {
                                supplierAuditFile.setLatest(false);
                                oldVersion = supplierAuditFile.getVersion();
                                version = oldVersion + 1;
                                autoNumber1 = supplierAuditFile.getFileNo();
                                oldFile = supplierAuditFile.getId();
                                supplierAuditFileRepository.save(supplierAuditFile);
                                versioned = true;

                            }
                            if (supplierAuditFile == null) {
                                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                                if (autoNumber != null) {
                                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                                }
                            }
                            supplierAuditFile = new PQMSupplierAuditFile();
                            supplierAuditFile.setSupplierAudit(id);
                            supplierAuditFile.setName(name);
                            supplierAuditFile.setCreatedBy(login.getPerson().getId());
                            supplierAuditFile.setModifiedBy(login.getPerson().getId());
                            supplierAuditFile.setSize(file.getSize());
                            supplierAuditFile.setFileType("FILE");
                            supplierAuditFile.setFileNo(autoNumber1);
                            supplierAuditFile.setVersion(version);
                            if (folderId != 0) {
                                supplierAuditFile.setParentFile(folderId);
                            }
                            supplierAuditFile = supplierAuditFileRepository.save(supplierAuditFile);
                            if (supplierAuditFile.getParentFile() != null) {
                                PQMSupplierAuditFile parent = supplierAuditFileRepository.findOne(supplierAuditFile.getParentFile());
                                parent.setModifiedDate(supplierAuditFile.getModifiedDate());
                                parent = supplierAuditFileRepository.save(parent);
                            }
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(supplierAuditFile.getSupplierAudit(), objectType, supplierAuditFile.getId()));
                            qualityFile = fileRepository.findOne(supplierAuditFile.getId());
                            qualityFile.setPrevisionFileId(oldFile);
                            if (supplierAuditFile.getVersion() > 1) {
                                copyFileAttributes(oldFile, supplierAuditFile.getId());
                            }
                        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                            String revision = null;
                            PLMLifeCyclePhase lifeCyclePhase = null;
                            PQMPPAPChecklist checklist = null;
                            if (folderId == 0) {
                                ppapChecklist = ppapChecklistRepository.findByPpapAndNameAndParentFileIsNullAndLatestTrue(id, name);
                            } else {
                                ppapChecklist = ppapChecklistRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                            }
                            if (ppapChecklist != null) {
                                if (ppapChecklist.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                                    String message = messageSource.getMessage("document_already_released", null, "Released document(s) cannot be updated", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", ppapChecklist.getName());
                                    throw new CassiniException(result);
                                }
                                ppapChecklist.setLatest(false);
                                oldVersion = ppapChecklist.getVersion();
                                version = oldVersion + 1;
                                revision = ppapChecklist.getRevision();
                                lifeCyclePhase = ppapChecklist.getLifeCyclePhase();
                                autoNumber1 = ppapChecklist.getFileNo();
                                oldFile = ppapChecklist.getId();
                                ppapChecklistRepository.save(ppapChecklist);
                                versioned = true;
                                checklist = JsonUtils.cloneEntity(ppapChecklist, PQMPPAPChecklist.class);
                            }
                            if (ppapChecklist == null) {
                                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                                if (autoNumber != null) {
                                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                                }
                            }
                            ppapChecklist = new PQMPPAPChecklist();
                            ppapChecklist.setPpap(id);
                            ppapChecklist.setName(name);
                            ppapChecklist.setCreatedBy(login.getPerson().getId());
                            ppapChecklist.setModifiedBy(login.getPerson().getId());
                            ppapChecklist.setSize(file.getSize());
                            ppapChecklist.setFileType("FILE");
                            ppapChecklist.setFileNo(autoNumber1);
                            ppapChecklist.setVersion(version);
                            if (revision == null) {
                                String newRevision = setRevisionAndLifecyclePhase();
                                PLMLifeCyclePhase plmLifeCyclePhase = setLifecyclePhase();
                                ppapChecklist.setRevision(newRevision);
                                ppapChecklist.setLifeCyclePhase(plmLifeCyclePhase);
                            } else {
                                ppapChecklist.setRevision(revision);
                                List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(lifeCyclePhase.getLifeCycle(), LifeCyclePhaseType.PRELIMINARY);
                                if (lifeCyclePhases.size() > 0) {
                                    ppapChecklist.setLifeCyclePhase(lifeCyclePhases.get(0));
                                }
                            }
                            if (folderId != 0) {
                                ppapChecklist.setParentFile(folderId);
                            }
                            ppapChecklist = ppapChecklistRepository.save(ppapChecklist);
                            if (checklist != null) {
                                documentService.copyReviewers(checklist.getId(), ppapChecklist.getId());
                            }
                            if (ppapChecklist.getParentFile() != null) {
                                PQMPPAPChecklist parent = ppapChecklistRepository.findOne(ppapChecklist.getParentFile());
                                parent.setModifiedDate(parent.getModifiedDate());
                                parent.setModifiedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                                parent = ppapChecklistRepository.save(parent);

                                Integer rejectedCount = documentReviewerRepository.getRejectedCount(ppapChecklist.getParentFile());
                                if (rejectedCount > 0) {
                                    List<PLMDocumentReviewer> documentReviewers = documentReviewerRepository.findByDocumentAndStatusOrderByIdDesc(ppapChecklist.getParentFile(), DocumentApprovalStatus.REJECTED);
                                    documentReviewers.forEach(plmDocumentReviewer -> {
                                        plmDocumentReviewer.setStatus(DocumentApprovalStatus.NONE);
                                    });
                                    documentReviewerRepository.save(documentReviewers);
                                }
                            }
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(ppapChecklist.getPpap(), objectType, ppapChecklist.getId()));
                            qualityFile = fileRepository.findOne(ppapChecklist.getId());
                            qualityFile.setPrevisionFileId(oldFile);
                            if (ppapChecklist.getVersion() > 1) {
                                copyFileAttributes(oldFile, ppapChecklist.getId());
                            }
                        }

                        qualityFile.setOldVersion(oldVersion);
                        fileList.add(qualityFile);
                        String dir = "";
                        if (objectType.equals(PLMObjectType.DOCUMENT)) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + "documents";
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            if (folderId == 0) {
                                dir = dir + File.separator + qualityFile.getId();
                            } else {
                                dir = dir + getDocumentParentFileSystemPath(qualityFile.getId());
                            }
                            fileSystemService.saveDocumentToDisk(file, dir);
                        } else {
                            if (folderId == 0) {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + id;
                            } else {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + getParentFileSystemPath(id, folderId, objectType);
                            }
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            String path = dir + File.separator + qualityFile.getId();
                            fileSystemService.saveDocumentToDisk(file, path);
                        }
                        /*Map<String, String> map = forgeService.uploadForgeFile(file.getOriginalFilename(), path);
                        if (map != null) {
                            if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
                                inspectionPlanFile.setUrn(map.get("urn"));
                                inspectionPlanFile.setThumbnail(map.get("thumbnail"));
                                inspectionPlanFile = inspectionPlanFileRepository.save(inspectionPlanFile);
                            } else if (objectType.equals(PLMObjectType.INSPECTION)) {
                                inspectionFile.setUrn(map.get("urn"));
                                inspectionFile.setThumbnail(map.get("thumbnail"));
                                inspectionFile = inspectionFileRepository.save(inspectionFile);
                            } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
                                problemReportFile.setUrn(map.get("urn"));
                                problemReportFile.setThumbnail(map.get("thumbnail"));
                                problemReportFile = problemReportFileRepository.save(problemReportFile);
                            } else if (objectType.equals(PLMObjectType.NCR)) {
                                ncrFile.setUrn(map.get("urn"));
                                ncrFile.setThumbnail(map.get("thumbnail"));
                                ncrFile = ncrFileRepository.save(ncrFile);
                            } else if (objectType.equals(PLMObjectType.QCR)) {
                                qcrFile.setUrn(map.get("urn"));
                                qcrFile.setThumbnail(map.get("thumbnail"));
                                qcrFile = qcrFileRepository.save(qcrFile);
                            } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
                                mesObjectFile.setUrn(map.get("urn"));
                                mesObjectFile.setThumbnail(map.get("thumbnail"));
                                mesObjectFile = mesObjectFileRepository.save(mesObjectFile);
                            } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
                                mroObjectFile.setUrn(map.get("urn"));
                                mroObjectFile.setThumbnail(map.get("thumbnail"));
                                mroObjectFile = mroObjectFileRepository.save(mroObjectFile);
                            } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
                                pgcObjectFile.setUrn(map.get("urn"));
                                pgcObjectFile.setThumbnail(map.get("thumbnail"));
                                pgcObjectFile = pgcObjectFileRepository.save(pgcObjectFile);
                            }
                        }*/
                        if (versioned) {
                            versionedFiles.add(qualityFile);
                        } else {
                            uploaded.add(qualityFile);
                        }
                    }
                }
            } else if (objectType.equals(PLMObjectType.ITEM)) {
                List<PLMItemFile> itemFiles = itemFileService.uploadItemFiles(id, folderId, fileMap);
                itemFiles.forEach(itemFile -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(itemFile.getItem().getId(), objectType, itemFile.getId()));
                });
            } else if (objectType.equals(PLMObjectType.PROJECT)) {
                List<PLMProjectFile> projectFiles = projectService.uploadProjectFiles(id, folderId, fileMap);
                projectFiles.forEach(projectFile -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(projectFile.getProject(), objectType, projectFile.getId()));
                });
            } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
                List<PLMManufacturerFile> manufacturerFiles = manufacturerFileService.uploadFiles(id, folderId, fileMap);
                manufacturerFiles.forEach(manufacturerFile -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(manufacturerFile.getManufacturer(), objectType, manufacturerFile.getId()));
                });
            } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
                List<PLMManufacturerPartFile> manufacturerPartFiles = manufacturerPartFileService.uploadPartFiles(id, folderId, fileMap);
                manufacturerPartFiles.forEach(manufacturerFile -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(manufacturerFile.getManufacturerPart(), objectType, manufacturerFile.getId()));
                });
            } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
                List<PLMActivityFile> activityFiles = activityService.uploadActivityFiles(id, folderId, fileMap);
                activityFiles.forEach(activityFile -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(activityFile.getActivity(), objectType, activityFile.getId()));
                });
            } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
                List<PLMTaskFile> taskFiles = activityService.uploadTaskFiles(id, folderId, fileMap);
                taskFiles.forEach(taskFile -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(taskFile.getTask(), objectType, taskFile.getId()));
                });
            } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
                List<PLMGlossaryFile> glossaryFiles = glossaryService.uploadGlossaryFiles(id, folderId, fileMap);
                glossaryFiles.forEach(glossaryFile -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(glossaryFile.getGlossary(), objectType, glossaryFile.getId()));
                });
            } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
                List<PLMSupplierFile> supplierFiles = supplierFileService.uploadFiles(id, folderId, fileMap);
                supplierFiles.forEach(supplierFile -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(supplierFile.getSupplier(), objectType, supplierFile.getId()));
                });
            } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
                List<PQMCustomerFile> customerFiles = customerFileService.uploadFiles(id, folderId, fileMap);
                customerFiles.forEach(customerFile -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(customerFile.getCustomer(), objectType, customerFile.getId()));
                });
            } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
                List<PLMRequirementDocumentFile> documentFiles = reqDocumentFileService.uploadFiles(id, folderId, fileMap);
                documentFiles.forEach(documentFile -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(documentFile.getDocumentRevision().getId(), objectType, documentFile.getId()));
                });
            } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
                List<PLMRequirementFile> requirementFiles = requirementFileService.uploadFiles(id, folderId, fileMap);
                requirementFiles.forEach(requirementFile -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(requirementFile.getRequirement().getId(), objectType, requirementFile.getId()));
                });
            } else if (objectType.equals(PLMObjectType.PLMNPR)) {
                List<PLMNprFile> nprFiles = nprFileService.uploadFiles(id, folderId, fileMap);
                nprFiles.forEach(nprFile -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(nprFile.getNpr(), objectType, nprFile.getId()));
                });
            } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
                List<MESMBOMFile> mesmbomFiles = mbomFileService.uploadFiles(id, folderId, fileMap);
                mesmbomFiles.forEach(file -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(file.getMbomRevision(), objectType, file.getId()));
                });
            } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
                List<MESMBOMInstanceFile> mesmbomFiles = mbomInstanceFileService.uploadFiles(id, folderId, fileMap);
                mesmbomFiles.forEach(file -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(file.getMbomInstance(), objectType, file.getId()));
                });
            } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
                List<MESBOPFile> mesbopFiles = bopFileService.uploadFiles(id, folderId, fileMap);
                mesbopFiles.forEach(file -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(file.getBop(), objectType, file.getId()));
                });
            } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
                List<MESBOPOperationFile> mesbopFiles = bopPlanFileService.uploadFiles(id, folderId, fileMap);
                mesbopFiles.forEach(file -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(file.getBopOperation(), objectType, file.getId()));
                });
            } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
                List<MESBOPInstanceOperationFile> mesbopFiles = bopInstanceOperationFileService.uploadFiles(id, folderId, fileMap);
                mesbopFiles.forEach(file -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(file.getBopOperation(), objectType, file.getId()));
                });
            } else if (objectType.equals(PLMObjectType.PROGRAM)) {
                List<PLMProgramFile> programFiles = programFileService.uploadFiles(id, folderId, fileMap);
                programFiles.forEach(file -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(file.getProgram(), objectType, file.getId()));
                });
            } else if (objectType.equals(PLMObjectType.CHANGE)) {
                List<PLMChangeFile> changeFiles = changeFileService.uploadChangeFiles(id, folderId, fileMap);
                changeFiles.forEach(changeFile -> {
                    objectFileDto.getObjectFiles().add(convertFileIdToDto(changeFile.getChange(), objectType, changeFile.getId()));
                });
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (uploaded.size() > 0) {
            if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
                applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanFilesAddedEvent(planRevision, uploaded));
            } else if (objectType.equals(PLMObjectType.INSPECTION)) {
                applicationEventPublisher.publishEvent(new InspectionEvents.InspectionFilesAddedEvent(inspection, uploaded));
            } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
                applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportFilesAddedEvent(problemReport, uploaded));
            } else if (objectType.equals(PLMObjectType.NCR)) {
                applicationEventPublisher.publishEvent(new NCREvents.NCRFilesAddedEvent(ncr, uploaded));
            } else if (objectType.equals(PLMObjectType.QCR)) {
                applicationEventPublisher.publishEvent(new QCREvents.QCRFilesAddedEvent(qcr, uploaded));
            } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
                applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFilesAddedEvent("mes", id, uploaded, mesObject.getObjectType()));
            } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
                applicationEventPublisher.publishEvent(new DocumentEvents.DocumentFilesAddedEvent(uploaded));
            } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartReportAddedEvent(id, uploaded));
                if (login.getExternal()) {
                    sendDocumentUploadedNotification(uploaded, "new", id, login, objectType);
                }
            } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
                applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFilesAddedEvent("mro", id, uploaded, mroObject.getObjectType()));
            } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
                applicationEventPublisher.publishEvent(new SubstanceEvents.SubstanceFilesAddedEvent("pgc", id, uploaded, pgcObject.getObjectType()));
            } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
                applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditFilesAddedEvent(id, uploaded));
            } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                if (login.getExternal()) {
                    sendDocumentUploadedNotification(uploaded, "new", id, login, objectType);
                }
                applicationEventPublisher.publishEvent(new PPAPEvents.PPAPFilesAddedEvent(id, uploaded));
            }

        }
        if (versionedFiles.size() > 0) {
            if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
                applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanFilesVersionedEvent(planRevision, versionedFiles));
            } else if (objectType.equals(PLMObjectType.INSPECTION)) {
                applicationEventPublisher.publishEvent(new InspectionEvents.InspectionFilesVersionedEvent(inspection, versionedFiles));
            } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
                applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportFilesVersionedEvent(problemReport, versionedFiles));
            } else if (objectType.equals(PLMObjectType.NCR)) {
                applicationEventPublisher.publishEvent(new NCREvents.NCRFilesVersionedEvent(ncr, versionedFiles));
            } else if (objectType.equals(PLMObjectType.QCR)) {
                applicationEventPublisher.publishEvent(new QCREvents.QCRFilesVersionedEvent(qcr, versionedFiles));
            } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
                applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFilesVersionedEvent("mes", id, uploaded, mesObject.getObjectType()));
            } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
                applicationEventPublisher.publishEvent(new DocumentEvents.DocumentFilesVersionedEvent(versionedFiles));
            } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartReportVersionedEvent(id, versionedFiles));
                if (login.getExternal()) {
                    sendDocumentUploadedNotification(versionedFiles, "update", id, login, objectType);
                }
            } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
                applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFilesVersionedEvent("mro", id, versionedFiles, mroObject.getObjectType()));
            } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
                applicationEventPublisher.publishEvent(new SubstanceEvents.SubstanceFilesVersionedEvent("pgc", id, versionedFiles, pgcObject.getObjectType()));
            } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
                applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditFilesVersionedEvent(id, versionedFiles));
            } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                if (login.getExternal()) {
                    sendDocumentUploadedNotification(versionedFiles, "update", id, login, objectType);
                }
                applicationEventPublisher.publishEvent(new PPAPEvents.PPAPFilesVersionedEvent(id, versionedFiles));
            }
        }

        return objectFileDto;
    }

    private void updateParentFile(PQMPPAPChecklist ppapChecklist) {
        ppapChecklist.setModifiedDate(ppapChecklist.getModifiedDate());

        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(ppapChecklist.getLifeCyclePhase().getLifeCycle());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = ppapChecklist.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                findFirst().get();
        Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
        PLMLifeCyclePhase nextPhase = lifeCyclePhases.get(index + 1);
        if (index == 0 && nextPhase.getPhaseType().equals(LifeCyclePhaseType.PRELIMINARY)) {
            ppapChecklist.setLifeCyclePhase(nextPhase);
        }
        ppapChecklist = ppapChecklistRepository.save(ppapChecklist);
        if (ppapChecklist.getParentFile() != null) {
            PQMPPAPChecklist checklist = ppapChecklistRepository.findOne(ppapChecklist.getParentFile());
            updateParentFile(checklist);
        }
    }

    public String setRevisionAndLifecyclePhase() {
        String[] values = new String[0];
        String revision = "";
        Preference pref = preferenceRepository.findByPreferenceKey("DEFAULT_DOCUMENT_REVISION_SEQUENCE");
        if (pref != null) {
            String json = pref.getJsonValue();
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = objectMapper.readTree(json);
                    Integer typeId = jsonNode.get("typeId").asInt();
                    if (typeId != null) {
                        Lov lov = lovRepository.findOne(typeId);
                        if (lov != null) {
                            values = lov.getValues();
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        if (values.length == 0) {
            values = PDMConstants.RevisionSequence.stream().toArray(String[]::new);
        }
        if (values.length > 0) {
            revision = values[0];
        }
        return revision;
    }

    public PLMLifeCyclePhase setLifecyclePhase() {
        PLMLifeCyclePhase lifeCyclePhase = null;
        Preference pref = preferenceRepository.findByPreferenceKey("DEFAULT_DOCUMENT_MANAGEMENT_LIFECYCLE");
        if (pref != null) {
            String json = pref.getJsonValue();
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = objectMapper.readTree(json);
                    Integer typeId = jsonNode.get("typeId").asInt();
                    if (typeId != null) {
                        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(typeId);
                        if (lifeCycle != null) {
                            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(lifeCycle.getId(), LifeCyclePhaseType.PRELIMINARY);
                            if (lifeCyclePhases.size() > 0) {
                                lifeCyclePhase = lifeCyclePhases.get(0);
                            }
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        return lifeCyclePhase;
    }

    public PLMLifeCyclePhase setPPAPLifecyclePhase() {
        PLMLifeCyclePhase lifeCyclePhase = null;
        Preference pref = preferenceRepository.findByPreferenceKey("DEFAULT_PPAP_CHECKLIST_LIFECYCLE");
        if (pref != null) {
            String json = pref.getJsonValue();
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = objectMapper.readTree(json);
                    Integer typeId = jsonNode.get("typeId").asInt();
                    if (typeId != null) {
                        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(typeId);
                        if (lifeCycle != null) {
                            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(lifeCycle.getId(), LifeCyclePhaseType.PRELIMINARY);
                            if (lifeCyclePhases.size() > 0) {
                                lifeCyclePhase = lifeCyclePhases.get(0);
                            }
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        return lifeCyclePhase;
    }

    @Transactional
    @PreAuthorize("hasPermission(#fileId ,'edit') || @documentService.checkDMPemrmissions(authentication, 'edit', #objectType.name(), #fileId)")
    public ObjectFileDto updateObjectFile(Integer id, Integer fileId, PLMObjectType objectType, ObjectFileDto qualityFile) throws JsonProcessingException {
        if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
            PQMInspectionPlanFile existFile = inspectionPlanFileRepository.findOne(fileId);
            FileDto inspectionPlanFile = qualityFile.getObjectFile();
            if (inspectionPlanFile != null && existFile != null) {
                existFile.setLocked(inspectionPlanFile.getLocked());
                PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(existFile.getInspectionPlan());
                if (inspectionPlanFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanFileLockedEvent(inspectionPlanRevision, existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanFileUnlockedEvent(inspectionPlanRevision, existFile));
                }
                existFile.setDescription(inspectionPlanFile.getDescription());
                existFile = inspectionPlanFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(id, objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.INSPECTION)) {
            PQMInspectionFile existFile = inspectionFileRepository.findOne(fileId);
            FileDto inspectionFile = qualityFile.getObjectFile();
            if (inspectionFile != null && existFile != null) {
                PQMInspection inspection = inspectionRepository.findOne(existFile.getInspection());
                existFile.setLocked(inspectionFile.getLocked());
                if (inspectionFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    applicationEventPublisher.publishEvent(new InspectionEvents.InspectionFileLockedEvent(inspection, existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new InspectionEvents.InspectionFileUnlockedEvent(inspection, existFile));
                }
                existFile.setDescription(inspectionFile.getDescription());
                existFile = inspectionFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getInspection(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            PQMProblemReportFile existFile = problemReportFileRepository.findOne(fileId);
            FileDto problemReportFile = qualityFile.getObjectFile();
            PQMProblemReport problemReport = problemReportRepository.findOne(id);

            if (problemReportFile != null && existFile != null) {
                existFile.setDescription(problemReportFile.getDescription());
                existFile.setLocked(problemReportFile.getLocked());
                if (problemReportFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportFileLockedEvent(problemReport, existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportFileUnlockedEvent(problemReport, existFile));
                }
                existFile = problemReportFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getProblemReport(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.NCR)) {
            PQMNCRFile existFile = ncrFileRepository.findOne(fileId);
            FileDto pqmncrFile = qualityFile.getObjectFile();
            PQMNCR pqmncr = ncrRepository.findOne(id);
            if (pqmncrFile != null && existFile != null) {
                existFile.setDescription(pqmncrFile.getDescription());
                existFile.setLocked(pqmncrFile.getLocked());
                if (pqmncrFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    applicationEventPublisher.publishEvent(new NCREvents.NCRFileLockedEvent(pqmncr, existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new NCREvents.NCRFileUnlockedEvent(pqmncr, existFile));
                }
                existFile = ncrFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getNcr(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.QCR)) {
            PQMQCRFile existFile = qcrFileRepository.findOne(fileId);
            FileDto pqmqcrFile = qualityFile.getObjectFile();
            PQMQCR pqmqcr = qcrRepository.findOne(id);
            if (pqmqcrFile != null && existFile != null) {
                existFile.setLocked(pqmqcrFile.getLocked());
                if (pqmqcrFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    applicationEventPublisher.publishEvent(new QCREvents.QCRFileLockedEvent(pqmqcr, existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new QCREvents.QCRFileUnlockedEvent(pqmqcr, existFile));
                }
                existFile.setDescription(pqmqcrFile.getDescription());
                existFile = qcrFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getQcr(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
            MESObjectFile existFile = mesObjectFileRepository.findOne(fileId);
            FileDto mesObjectFile = qualityFile.getObjectFile();
            MESObject mesObject = mesObjectRepository.findOne(id);
            if (mesObjectFile != null && existFile != null) {
                existFile.setLocked(mesObjectFile.getLocked());
                if (mesObjectFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFileLockedEvent("mes", mesObject.getId(), existFile, mesObject.getObjectType()));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFileUnlockedEvent("mes", mesObject.getId(), existFile, mesObject.getObjectType()));
                }
                existFile.setDescription(mesObjectFile.getDescription());
                existFile = mesObjectFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getObject(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
            PLMDocument existFile = plmDocumentRepository.findOne(fileId);
            FileDto plmDocument = qualityFile.getObjectFile();
            if (plmDocument != null && existFile != null) {
                existFile.setLocked(plmDocument.getLocked());
                if (plmDocument.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new DocumentEvents.DocumentFileLockedEvent(existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new DocumentEvents.DocumentFileUnlockedEvent(existFile));
                }
                existFile.setDescription(plmDocument.getDescription());
                existFile = plmDocumentRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(0, objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            PLMMfrPartInspectionReport existFile = mfrPartInspectionReportRepository.findOne(fileId);
            FileDto plmDocument = qualityFile.getObjectFile();
            if (plmDocument != null && existFile != null) {
                existFile.setLocked(plmDocument.getLocked());
                if (plmDocument.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartReportLockedEvent(id, existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartReportUnlockedEvent(id, existFile));
                }
                existFile.setDescription(plmDocument.getDescription());
                existFile = mfrPartInspectionReportRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getManufacturerPart(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
            PGCObjectFile existFile = pgcObjectFileRepository.findOne(fileId);
            FileDto pgcObjectFile = qualityFile.getObjectFile();
            PGCObject pgcObject = pgcObjectRepository.findOne(id);
            if (pgcObjectFile != null && existFile != null) {
                existFile.setLocked(pgcObjectFile.getLocked());
                if (pgcObjectFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    applicationEventPublisher.publishEvent(new SubstanceEvents.SubstanceFileLockedEvent("pgc", pgcObject.getId(), existFile, pgcObject.getObjectType()));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new SubstanceEvents.SubstanceFileUnlockedEvent("pgc", pgcObject.getId(), existFile, pgcObject.getObjectType()));
                }
                existFile.setDescription(pgcObjectFile.getDescription());
                existFile = pgcObjectFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getObject(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            PQMSupplierAuditFile existFile = supplierAuditFileRepository.findOne(fileId);
            FileDto supplierAuditFile = qualityFile.getObjectFile();
            PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(id);
            if (supplierAuditFile != null && existFile != null) {
                existFile.setLocked(supplierAuditFile.getLocked());
                if (supplierAuditFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditFileLockedEvent(supplierAudit.getId(), existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditFileUnlockedEvent(supplierAudit.getId(), existFile));
                }
                existFile.setDescription(supplierAuditFile.getDescription());
                existFile = supplierAuditFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getSupplierAudit(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            PQMPPAPChecklist existFile = ppapChecklistRepository.findOne(fileId);
            FileDto ppapChecklist = qualityFile.getObjectFile();
            PQMPPAP ppap = ppapRepository.findOne(id);
            if (ppapChecklist != null && existFile != null) {
                existFile.setLocked(ppapChecklist.getLocked());
                if (ppapChecklist.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    applicationEventPublisher.publishEvent(new PPAPEvents.PPAPFileLockedEvent(ppap, existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new PPAPEvents.PPAPFileUnlockedEvent(ppap, existFile));
                }
                existFile.setDescription(ppapChecklist.getDescription());
                existFile = ppapChecklistRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getPpap(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
            MROObjectFile existFile = mroObjectFileRepository.findOne(fileId);
            FileDto mroObjectFile = qualityFile.getObjectFile();
            MROObject mroObject = mroObjectRepository.findOne(id);
            if (mroObjectFile != null && existFile != null) {
                existFile.setLocked(mroObjectFile.getLocked());
                if (mroObjectFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFileLockedEvent("mro", mroObject.getId(), existFile, mroObject.getObjectType()));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFileUnlockedEvent("mro", mroObject.getId(), existFile, mroObject.getObjectType()));
                }
                existFile.setDescription(mroObjectFile.getDescription());
                existFile = mroObjectFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getObject(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.ITEM)) {
            PLMItemFile existFile = itemFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new ItemEvents.ItemFileLockedEvent(existFile.getItem(), existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new ItemEvents.ItemFileUnlockedEvent(existFile.getItem(), existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = itemFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getItem().getId(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.PROJECT)) {
            PLMProjectFile existFile = projectFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                PLMProject project = projectService.getProject(existFile.getProject());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFileLockedEvent(project, existFile));
                    projectService.sendProjectSubscribeNotification(project, existFile.getName(), "fileLocked");
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFileUnlockedEvent(project, existFile));
                    projectService.sendProjectSubscribeNotification(project, existFile.getName(), "fileUnLocked");
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = projectFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getProject(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
            PLMManufacturerFile existFile = manufacturerFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                PLMManufacturer manufacturer = manufacturerRepository.getOne(existFile.getManufacturer());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerFileLockedEvent(manufacturer, existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerFileUnlockedEvent(manufacturer, existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = manufacturerFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getManufacturer(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
            PLMManufacturerPartFile existFile = manufacturerPartFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(existFile.getManufacturerPart());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartFileLockedEvent(manufacturerPart, existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartFileUnlockedEvent(manufacturerPart, existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = manufacturerPartFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getManufacturerPart(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            PLMActivityFile existFile = activityFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                PLMActivity activity = activityRepository.findOne(existFile.getActivity());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new ActivityEvents.ActivityFileLockedEvent(activity, existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new ActivityEvents.ActivityFileUnlockedEvent(activity, existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = activityFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getActivity(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            PLMTaskFile existFile = taskFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            PLMTask task = taskRepository.findOne(existFile.getTask());
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new TaskEvents.TaskFileLockedEvent(task, existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new TaskEvents.TaskFileUnlockedEvent(task, existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = taskFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getTask(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
            PLMGlossaryFile existFile = glossaryFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
//                    applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFileLockedEvent(project, existFile));
                } else {
                    existFile.setLockedBy(null);
//                    applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFileUnlockedEvent(project, existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = glossaryFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getGlossary(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
            PLMSupplierFile existFile = supplierFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new SupplierEvents.SupplierFileLockedEvent(existFile.getSupplier(), existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new SupplierEvents.SupplierFileUnlockedEvent(existFile.getSupplier(), existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = supplierFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getSupplier(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
            PQMCustomerFile existFile = customerFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new CustomerEvents.CustomerFileLockedEvent(existFile.getCustomer(), existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new CustomerEvents.CustomerFileUnlockedEvent(existFile.getCustomer(), existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = customerFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getCustomer(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
            PLMRequirementDocumentFile existFile = requirementDocumentFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentFileLockedEvent(existFile.getDocumentRevision(), existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentFileUnlockedEvent(existFile.getDocumentRevision(), existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = requirementDocumentFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getDocumentRevision().getId(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
            PLMRequirementFile existFile = requirementFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new RequirementEvents.RequirementFileLockedEvent(existFile.getRequirement(), existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new RequirementEvents.RequirementFileUnlockedEvent(existFile.getRequirement(), existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = requirementFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getRequirement().getId(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.PLMNPR)) {
            PLMNprFile existFile = nprFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                PLMNpr npr = nprRepository.findOne(existFile.getNpr());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new NprEvents.NprFileLockedEvent(npr, existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new NprEvents.NprFileUnlockedEvent(npr, existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = nprFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getNpr(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
            MESMBOMFile existFile = mbomFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
//                    applicationEventPublisher.publishEvent(new NprEvents.NprFileLockedEvent(existFile.getMbomRevision(), existFile));
                } else {
                    existFile.setLockedBy(null);
//                    applicationEventPublisher.publishEvent(new NprEvents.NprFileUnlockedEvent(existFile.getMbomRevision(), existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = mbomFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getMbomRevision(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
            MESMBOMInstanceFile existFile = mbomInstanceFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
//                    applicationEventPublisher.publishEvent(new NprEvents.NprFileLockedEvent(existFile.getMbomRevision(), existFile));
                } else {
                    existFile.setLockedBy(null);
//                    applicationEventPublisher.publishEvent(new NprEvents.NprFileUnlockedEvent(existFile.getMbomRevision(), existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = mbomInstanceFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getMbomInstance(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
            MESBOPFile existFile = bopFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new BOPEvents.BOPFileLockedEvent(existFile.getBop(), existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new BOPEvents.BOPFileUnlockedEvent(existFile.getBop(), existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = bopFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getBop(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
            MESBOPOperationFile existFile = bopOperationFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationFileLockedEvent(existFile.getBopOperation(), existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationFileUnlockedEvent(existFile.getBopOperation(), existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = bopOperationFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getBopOperation(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
            MESBOPInstanceOperationFile existFile = bopInstanceOperationFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new BOPInstanceOperationEvents.BOPOperationFileLockedEvent(existFile.getBopOperation(), existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new BOPInstanceOperationEvents.BOPOperationFileUnlockedEvent(existFile.getBopOperation(), existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = bopInstanceOperationFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getBopOperation(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            PLMProgramFile existFile = programFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            if (itemFile != null && existFile != null) {
                existFile.setLocked(itemFile.getLocked());
                if (itemFile.getLocked()) {
                    existFile.setLockedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                    existFile.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new ProgramEvents.ProgramFileLockedEvent(existFile.getProgram(), existFile));
                } else {
                    existFile.setLockedBy(null);
                    applicationEventPublisher.publishEvent(new ProgramEvents.ProgramFileUnlockedEvent(existFile.getProgram(), existFile));
                }
                existFile.setDescription(itemFile.getDescription());
                existFile = programFileRepository.save(existFile);
                qualityFile.setObjectFile(convertFileIdToDto(existFile.getProgram(), objectType, existFile.getId()));
            }
        } else if (objectType.equals(PLMObjectType.CHANGE)) {
            PLMChangeFile changeFile = changeFileRepository.findOne(fileId);
            FileDto itemFile = qualityFile.getObjectFile();
            changeFile.setDescription(itemFile.getDescription());
            changeFile.setLocked(itemFile.getLocked());
            changeFile = changeFileService.updateChangeFile(id, changeFile.getId(), changeFile);
            qualityFile.setObjectFile(convertFileIdToDto(changeFile.getChange(), objectType, changeFile.getId()));
        }

        return qualityFile;
    }

    @Transactional
    public ObjectFileDto updateFolder(Integer id, Integer folderId, PLMObjectType objectType, ObjectFileDto fileDto) {
        ObjectFileDto objectFileDto = new ObjectFileDto();
        PLMFile plmFile = fileRepository.findOne(fileDto.getObjectFile().getId());
        plmFile.setName(fileDto.getObjectFile().getName());
        plmFile.setDescription(fileDto.getObjectFile().getDescription());
        plmFile = fileRepository.save(plmFile);
        objectFileDto.setObjectFile(convertFileIdToDto(id, objectType, plmFile.getId()));
        return objectFileDto;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','folder')")
    public ObjectFileDto createFolder(Integer id, PLMObjectType objectType, ObjectFileDto objectFileDto) throws JsonProcessingException {
        String folderNumber = null;
        PLMFile plmFile = null;
        if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
            PQMInspectionPlanRevision planRevision = inspectionPlanRevisionRepository.findOne(id);
            PQMInspectionPlanFile existFolderName = null;
            FileDto fileDto = objectFileDto.getObjectFile();
            PQMInspectionPlanFile inspectionPlanFile = new PQMInspectionPlanFile();
            inspectionPlanFile.setName(fileDto.getName());
            inspectionPlanFile.setParentFile(fileDto.getParentFile());
            inspectionPlanFile.setDescription(fileDto.getDescription());
            if (inspectionPlanFile.getParentFile() != null) {
                existFolderName = inspectionPlanFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndInspectionPlanAndLatestTrue(inspectionPlanFile.getName(), inspectionPlanFile.getParentFile(), id);
                if (existFolderName != null) {
                    PLMFile file = fileRepository.findOne(inspectionPlanFile.getParentFile());
                    String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", inspectionPlanFile.getName(), file.getName());
                    throw new CassiniException(result);
                }
            } else {
                existFolderName = inspectionPlanFileRepository.findByInspectionPlanAndNameAndParentFileIsNullAndLatestTrue(id, inspectionPlanFile.getName());
                if (existFolderName != null) {
                    String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", inspectionPlanFile.getName());
                    throw new CassiniException(result);
                }
            }
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            inspectionPlanFile.setVersion(1);
            inspectionPlanFile.setSize(0L);
            inspectionPlanFile.setInspectionPlan(id);
            inspectionPlanFile.setFileNo(folderNumber);
            inspectionPlanFile.setFileType("FOLDER");
            inspectionPlanFile = inspectionPlanFileRepository.save(inspectionPlanFile);
            if (inspectionPlanFile.getParentFile() != null) {
                PQMInspectionPlanFile parent = inspectionPlanFileRepository.findOne(inspectionPlanFile.getParentFile());
                parent.setModifiedDate(inspectionPlanFile.getModifiedDate());
                parent = inspectionPlanFileRepository.save(parent);
            }
            plmFile = fileRepository.findOne(inspectionPlanFile.getId());
            objectFileDto.setObjectFile(convertFileIdToDto(id, objectType, inspectionPlanFile.getId()));

            applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanFoldersAddedEvent(planRevision, inspectionPlanFile));
        } else if (objectType.equals(PLMObjectType.INSPECTION)) {
            PQMInspectionFile existFolderName = null;
            FileDto fileDto = objectFileDto.getObjectFile();
            PQMInspectionFile inspectionFile = new PQMInspectionFile();
            inspectionFile.setName(fileDto.getName());
            inspectionFile.setDescription(fileDto.getDescription());
            inspectionFile.setParentFile(fileDto.getParentFile());
            if (inspectionFile.getParentFile() != null) {
                existFolderName = inspectionFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndInspectionAndLatestTrue(inspectionFile.getName(), inspectionFile.getParentFile(), id);
                if (existFolderName != null) {
                    PLMFile file = fileRepository.findOne(inspectionFile.getParentFile());
                    String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", inspectionFile.getName(), file.getName());
                    throw new CassiniException(result);
                }
            } else {
                existFolderName = inspectionFileRepository.findByInspectionAndNameAndParentFileIsNullAndLatestTrue(id, inspectionFile.getName());
                if (existFolderName != null) {
                    String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", inspectionFile.getName());
                    throw new CassiniException(result);
                }
            }
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            inspectionFile.setVersion(1);
            inspectionFile.setSize(0L);
            inspectionFile.setInspection(id);
            inspectionFile.setFileNo(folderNumber);
            inspectionFile.setFileType("FOLDER");
            inspectionFile = inspectionFileRepository.save(inspectionFile);
            if (inspectionFile.getParentFile() != null) {
                PQMInspectionFile parent = inspectionFileRepository.findOne(inspectionFile.getParentFile());
                parent.setModifiedDate(inspectionFile.getModifiedDate());
                parent = inspectionFileRepository.save(parent);
            }

            plmFile = fileRepository.findOne(inspectionFile.getId());
            objectFileDto.setObjectFile(convertFileIdToDto(inspectionFile.getInspection(), objectType, inspectionFile.getId()));
            PQMInspection inspection = inspectionRepository.findOne(id);
            applicationEventPublisher.publishEvent(new InspectionEvents.InspectionFoldersAddedEvent(inspection, inspectionFile));
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            PQMProblemReportFile existFolderName = null;
            FileDto fileDto = objectFileDto.getObjectFile();
            PQMProblemReportFile problemReportFile = new PQMProblemReportFile();
            problemReportFile.setName(fileDto.getName());
            problemReportFile.setDescription(fileDto.getDescription());
            problemReportFile.setParentFile(fileDto.getParentFile());
            if (problemReportFile.getParentFile() != null) {
                existFolderName = problemReportFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndProblemReportAndLatestTrue(problemReportFile.getName(), problemReportFile.getParentFile(), id);
                if (existFolderName != null) {
                    PLMFile file = fileRepository.findOne(problemReportFile.getParentFile());
                    String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", problemReportFile.getName(), file.getName());
                    throw new CassiniException(result);
                }
            } else {
                existFolderName = problemReportFileRepository.findByProblemReportAndNameAndParentFileIsNullAndLatestTrue(id, problemReportFile.getName());
                if (existFolderName != null) {
                    String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", problemReportFile.getName());
                    throw new CassiniException(result);
                }
            }
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            problemReportFile.setVersion(1);
            problemReportFile.setSize(0L);
            problemReportFile.setProblemReport(id);
            problemReportFile.setFileNo(folderNumber);
            problemReportFile.setFileType("FOLDER");
            problemReportFile = problemReportFileRepository.save(problemReportFile);
            if (problemReportFile.getParentFile() != null) {
                PQMProblemReportFile parent = problemReportFileRepository.findOne(problemReportFile.getParentFile());
                parent.setModifiedDate(problemReportFile.getModifiedDate());
                parent = problemReportFileRepository.save(parent);
            }
            plmFile = fileRepository.findOne(problemReportFile.getId());
            objectFileDto.setObjectFile(convertFileIdToDto(problemReportFile.getProblemReport(), objectType, problemReportFile.getId()));
            PQMProblemReport problemReport = problemReportRepository.findOne(id);
            applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportFoldersAddedEvent(problemReport, problemReportFile));
        } else if (objectType.equals(PLMObjectType.NCR)) {
            PQMNCRFile existFolderName = null;
            FileDto fileDto = objectFileDto.getObjectFile();
            PQMNCRFile ncrFile = new PQMNCRFile();
            ncrFile.setName(fileDto.getName());
            ncrFile.setDescription(fileDto.getDescription());
            ncrFile.setParentFile(fileDto.getParentFile());
            if (ncrFile.getParentFile() != null) {
                existFolderName = ncrFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndNcrAndLatestTrue(ncrFile.getName(), ncrFile.getParentFile(), id);
                if (existFolderName != null) {
                    PLMFile file = fileRepository.findOne(ncrFile.getParentFile());
                    String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", ncrFile.getName(), file.getName());
                    throw new CassiniException(result);
                }
            } else {
                existFolderName = ncrFileRepository.findByNcrAndNameAndParentFileIsNullAndLatestTrue(id, ncrFile.getName());
                if (existFolderName != null) {
                    String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", ncrFile.getName());
                    throw new CassiniException(result);
                }
            }
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            ncrFile.setVersion(1);
            ncrFile.setSize(0L);
            ncrFile.setNcr(id);
            ncrFile.setFileNo(folderNumber);
            ncrFile.setFileType("FOLDER");
            ncrFile = ncrFileRepository.save(ncrFile);
            if (ncrFile.getParentFile() != null) {
                PQMNCRFile parent = ncrFileRepository.findOne(ncrFile.getParentFile());
                parent.setModifiedDate(ncrFile.getModifiedDate());
                parent = ncrFileRepository.save(parent);
            }
            plmFile = fileRepository.findOne(ncrFile.getId());
            objectFileDto.setObjectFile(convertFileIdToDto(ncrFile.getNcr(), objectType, ncrFile.getId()));
            PQMNCR pqmncr = ncrRepository.findOne(id);
            applicationEventPublisher.publishEvent(new NCREvents.NCRFoldersAddedEvent(pqmncr, ncrFile));
        } else if (objectType.equals(PLMObjectType.QCR)) {
            PQMQCRFile existFolderName = null;
            FileDto fileDto = objectFileDto.getObjectFile();
            PQMQCRFile qcrFile = new PQMQCRFile();
            qcrFile.setName(fileDto.getName());
            qcrFile.setDescription(fileDto.getDescription());
            qcrFile.setParentFile(fileDto.getParentFile());
            if (qcrFile.getParentFile() != null) {
                existFolderName = qcrFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndQcrAndLatestTrue(qcrFile.getName(), qcrFile.getParentFile(), id);
                if (existFolderName != null) {
                    PLMFile file = fileRepository.findOne(qcrFile.getParentFile());
                    String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", qcrFile.getName(), file.getName());
                    throw new CassiniException(result);
                }
            } else {
                existFolderName = qcrFileRepository.findByQcrAndNameAndParentFileIsNullAndLatestTrue(id, qcrFile.getName());
                if (existFolderName != null) {
                    String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", qcrFile.getName());
                    throw new CassiniException(result);
                }
            }
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            qcrFile.setVersion(1);
            qcrFile.setSize(0L);
            qcrFile.setQcr(id);
            qcrFile.setFileNo(folderNumber);
            qcrFile.setFileType("FOLDER");
            qcrFile = qcrFileRepository.save(qcrFile);
            if (qcrFile.getParentFile() != null) {
                PQMQCRFile parent = qcrFileRepository.findOne(qcrFile.getParentFile());
                parent.setModifiedDate(qcrFile.getModifiedDate());
                parent = qcrFileRepository.save(parent);
            }
            plmFile = fileRepository.findOne(qcrFile.getId());
            objectFileDto.setObjectFile(convertFileIdToDto(qcrFile.getQcr(), objectType, qcrFile.getId()));
            PQMQCR pqmqcr = qcrRepository.findOne(id);
            applicationEventPublisher.publishEvent(new QCREvents.QCRFoldersAddedEvent(pqmqcr, qcrFile));
        } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
            MESObjectFile existFolderName = null;
            FileDto fileDto = objectFileDto.getObjectFile();
            MESObjectFile mesObjectFile = new MESObjectFile();
            mesObjectFile.setName(fileDto.getName());
            mesObjectFile.setDescription(fileDto.getDescription());
            mesObjectFile.setParentFile(fileDto.getParentFile());
            if (mesObjectFile.getParentFile() != null) {
                existFolderName = mesObjectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(mesObjectFile.getName(), mesObjectFile.getParentFile(), id);
                if (existFolderName != null) {
                    PLMFile file = fileRepository.findOne(mesObjectFile.getParentFile());
                    String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", mesObjectFile.getName(), file.getName());
                    throw new CassiniException(result);
                }
            } else {
                existFolderName = mesObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, mesObjectFile.getName());
                if (existFolderName != null) {
                    String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", mesObjectFile.getName());
                    throw new CassiniException(result);
                }
            }
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            mesObjectFile.setVersion(1);
            mesObjectFile.setSize(0L);
            mesObjectFile.setObject(id);
            mesObjectFile.setFileNo(folderNumber);
            mesObjectFile.setFileType("FOLDER");
            mesObjectFile = mesObjectFileRepository.save(mesObjectFile);
            if (mesObjectFile.getParentFile() != null) {
                MESObjectFile parent = mesObjectFileRepository.findOne(mesObjectFile.getParentFile());
                parent.setModifiedDate(mesObjectFile.getModifiedDate());
                parent = mesObjectFileRepository.save(parent);
            }
            plmFile = fileRepository.findOne(mesObjectFile.getId());
            objectFileDto.setObjectFile(convertFileIdToDto(mesObjectFile.getObject(), objectType, mesObjectFile.getId()));
            MESObject mesObject = mesObjectRepository.findOne(id);
            applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFoldersAddedEvent("mes", id, mesObjectFile, mesObject.getObjectType()));
        } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
            PLMDocument existFolderName = null;
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMDocument plmDocument = new PLMDocument();
            plmDocument.setName(fileDto.getName());
            plmDocument.setDescription(fileDto.getDescription());
            plmDocument.setParentFile(fileDto.getParentFile());
            if (plmDocument.getParentFile() != null) {
                existFolderName = plmDocumentRepository.findByNameEqualsIgnoreCaseAndParentFileAndLatestTrue(plmDocument.getName(), plmDocument.getParentFile());
                if (existFolderName != null) {
                    PLMFile file = fileRepository.findOne(plmDocument.getParentFile());
                    String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", plmDocument.getName(), file.getName());
                    throw new CassiniException(result);
                }
            } else {
                existFolderName = plmDocumentRepository.findByNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(plmDocument.getName());
                if (existFolderName != null) {
                    String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", plmDocument.getName());
                    throw new CassiniException(result);
                }
            }
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            plmDocument.setVersion(1);
            plmDocument.setSize(0L);
            plmDocument.setFileNo(folderNumber);
            plmDocument.setFileType("FOLDER");
            String newRevision = setRevisionAndLifecyclePhase();
            PLMLifeCyclePhase plmLifeCyclePhase = setLifecyclePhase();
            plmDocument.setRevision(newRevision);
            plmDocument.setLifeCyclePhase(plmLifeCyclePhase);
            plmDocument = plmDocumentRepository.save(plmDocument);
            if (plmDocument.getParentFile() != null) {
                PLMDocument parent = plmDocumentRepository.findOne(plmDocument.getParentFile());
                parent.setModifiedDate(plmDocument.getModifiedDate());
                parent = plmDocumentRepository.save(parent);
            }
            plmFile = fileRepository.findOne(plmDocument.getId());
            objectFileDto.setObjectFile(convertFileIdToDto(0, objectType, plmDocument.getId()));
            applicationEventPublisher.publishEvent(new DocumentEvents.DocumentFoldersAddedEvent(plmDocument));
        } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            PLMMfrPartInspectionReport existFolderName = null;
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMMfrPartInspectionReport plmMfrPartInspectionReport = new PLMMfrPartInspectionReport();
            plmMfrPartInspectionReport.setName(fileDto.getName());
            plmMfrPartInspectionReport.setDescription(fileDto.getDescription());
            plmMfrPartInspectionReport.setParentFile(fileDto.getParentFile());
            plmMfrPartInspectionReport.setManufacturerPart(id);
            if (plmMfrPartInspectionReport.getParentFile() != null) {
                existFolderName = mfrPartInspectionReportRepository.findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileAndLatestTrue(id, plmMfrPartInspectionReport.getName(), plmMfrPartInspectionReport.getParentFile());
                if (existFolderName != null) {
                    PLMFile file = fileRepository.findOne(plmMfrPartInspectionReport.getParentFile());
                    String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", plmMfrPartInspectionReport.getName(), file.getName());
                    throw new CassiniException(result);
                }
            } else {
                existFolderName = mfrPartInspectionReportRepository.findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(id, plmMfrPartInspectionReport.getName());
                if (existFolderName != null) {
                    String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", plmMfrPartInspectionReport.getName());
                    throw new CassiniException(result);
                }
            }
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            plmMfrPartInspectionReport.setVersion(1);
            plmMfrPartInspectionReport.setSize(0L);
            plmMfrPartInspectionReport.setFileNo(folderNumber);
            plmMfrPartInspectionReport.setFileType("FOLDER");
            plmMfrPartInspectionReport.setRevision(setRevisionAndLifecyclePhase());
            plmMfrPartInspectionReport.setLifeCyclePhase(setLifecyclePhase());
            plmMfrPartInspectionReport = mfrPartInspectionReportRepository.save(plmMfrPartInspectionReport);
            if (plmMfrPartInspectionReport.getParentFile() != null) {
                PLMMfrPartInspectionReport parent = mfrPartInspectionReportRepository.findOne(plmMfrPartInspectionReport.getParentFile());
                parent.setModifiedDate(plmMfrPartInspectionReport.getModifiedDate());
                parent = mfrPartInspectionReportRepository.save(parent);
            }
            plmFile = fileRepository.findOne(plmMfrPartInspectionReport.getId());
            objectFileDto.setObjectFile(convertFileIdToDto(0, objectType, plmMfrPartInspectionReport.getId()));
            applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartReportFoldersAddedEvent(id, plmMfrPartInspectionReport));
        } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
            MROObjectFile existFolderName = null;
            FileDto fileDto = objectFileDto.getObjectFile();
            MROObjectFile mroObjectFile = new MROObjectFile();
            mroObjectFile.setName(fileDto.getName());
            mroObjectFile.setDescription(fileDto.getDescription());
            mroObjectFile.setParentFile(fileDto.getParentFile());
            if (mroObjectFile.getParentFile() != null) {
                existFolderName = mroObjectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(mroObjectFile.getName(), mroObjectFile.getParentFile(), id);
                if (existFolderName != null) {
                    PLMFile file = fileRepository.findOne(mroObjectFile.getParentFile());
                    String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", mroObjectFile.getName(), file.getName());
                    throw new CassiniException(result);
                }
            } else {
                existFolderName = mroObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, mroObjectFile.getName());
                if (existFolderName != null) {
                    String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", mroObjectFile.getName());
                    throw new CassiniException(result);
                }
            }
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            mroObjectFile.setVersion(1);
            mroObjectFile.setSize(0L);
            mroObjectFile.setObject(id);
            mroObjectFile.setFileNo(folderNumber);
            mroObjectFile.setFileType("FOLDER");
            mroObjectFile = mroObjectFileRepository.save(mroObjectFile);
            if (mroObjectFile.getParentFile() != null) {
                MROObjectFile parent = mroObjectFileRepository.findOne(mroObjectFile.getParentFile());
                parent.setModifiedDate(mroObjectFile.getModifiedDate());
                parent = mroObjectFileRepository.save(parent);
            }
            plmFile = fileRepository.findOne(mroObjectFile.getId());
            objectFileDto.setObjectFile(convertFileIdToDto(mroObjectFile.getObject(), objectType, mroObjectFile.getId()));
            MROObject mroObject = mroObjectRepository.findOne(id);
            applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFoldersAddedEvent("mro", id, mroObjectFile, mroObject.getObjectType()));
        } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
            PGCObjectFile existFolderName = null;
            FileDto fileDto = objectFileDto.getObjectFile();
            PGCObjectFile pgcObjectFile = new PGCObjectFile();
            pgcObjectFile.setName(fileDto.getName());
            pgcObjectFile.setDescription(fileDto.getDescription());
            pgcObjectFile.setParentFile(fileDto.getParentFile());
            if (pgcObjectFile.getParentFile() != null) {
                existFolderName = pgcObjectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(pgcObjectFile.getName(), pgcObjectFile.getParentFile(), id);
                if (existFolderName != null) {
                    PLMFile file = fileRepository.findOne(pgcObjectFile.getParentFile());
                    String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", pgcObjectFile.getName(), file.getName());
                    throw new CassiniException(result);
                }
            } else {
                existFolderName = pgcObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, pgcObjectFile.getName());
                if (existFolderName != null) {
                    String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", pgcObjectFile.getName());
                    throw new CassiniException(result);
                }
            }
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            pgcObjectFile.setVersion(1);
            pgcObjectFile.setSize(0L);
            pgcObjectFile.setObject(id);
            pgcObjectFile.setFileNo(folderNumber);
            pgcObjectFile.setFileType("FOLDER");
            pgcObjectFile = pgcObjectFileRepository.save(pgcObjectFile);
            if (pgcObjectFile.getParentFile() != null) {
                PGCObjectFile parent = pgcObjectFileRepository.findOne(pgcObjectFile.getParentFile());
                parent.setModifiedDate(pgcObjectFile.getModifiedDate());
                parent = pgcObjectFileRepository.save(parent);
            }
            plmFile = fileRepository.findOne(pgcObjectFile.getId());
            objectFileDto.setObjectFile(convertFileIdToDto(pgcObjectFile.getObject(), objectType, pgcObjectFile.getId()));
            PGCObject pgcObject = pgcObjectRepository.findOne(id);
            applicationEventPublisher.publishEvent(new SubstanceEvents.SubstanceFoldersAddedEvent("pgc", id, pgcObjectFile, pgcObject.getObjectType()));
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            PQMSupplierAuditFile existFolderName = null;
            FileDto fileDto = objectFileDto.getObjectFile();
            PQMSupplierAuditFile supplierAuditFile = new PQMSupplierAuditFile();
            supplierAuditFile.setName(fileDto.getName());
            supplierAuditFile.setDescription(fileDto.getDescription());
            supplierAuditFile.setParentFile(fileDto.getParentFile());
            if (supplierAuditFile.getParentFile() != null) {
                existFolderName = supplierAuditFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndSupplierAuditAndLatestTrue(supplierAuditFile.getName(), supplierAuditFile.getParentFile(), id);
                if (existFolderName != null) {
                    PLMFile file = fileRepository.findOne(supplierAuditFile.getParentFile());
                    String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", supplierAuditFile.getName(), file.getName());
                    throw new CassiniException(result);
                }
            } else {
                existFolderName = supplierAuditFileRepository.findBySupplierAuditAndNameAndParentFileIsNullAndLatestTrue(id, supplierAuditFile.getName());
                if (existFolderName != null) {
                    String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", supplierAuditFile.getName());
                    throw new CassiniException(result);
                }
            }
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            supplierAuditFile.setVersion(1);
            supplierAuditFile.setSize(0L);
            supplierAuditFile.setSupplierAudit(id);
            supplierAuditFile.setFileNo(folderNumber);
            supplierAuditFile.setFileType("FOLDER");
            supplierAuditFile = supplierAuditFileRepository.save(supplierAuditFile);
            if (supplierAuditFile.getParentFile() != null) {
                PQMSupplierAuditFile parent = supplierAuditFileRepository.findOne(supplierAuditFile.getParentFile());
                parent.setModifiedDate(supplierAuditFile.getModifiedDate());
                parent = supplierAuditFileRepository.save(parent);
            }
            plmFile = fileRepository.findOne(supplierAuditFile.getId());
            objectFileDto.setObjectFile(convertFileIdToDto(supplierAuditFile.getSupplierAudit(), objectType, supplierAuditFile.getId()));
            PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(id);
            applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditFoldersAddedEvent(id, supplierAuditFile));
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            PQMPPAPChecklist existFolderName = null;
            FileDto fileDto = objectFileDto.getObjectFile();
            PQMPPAPChecklist pqmppapChecklist = new PQMPPAPChecklist();
            pqmppapChecklist.setName(fileDto.getName());
            pqmppapChecklist.setDescription(fileDto.getDescription());
            pqmppapChecklist.setParentFile(fileDto.getParentFile());
            if (pqmppapChecklist.getParentFile() != null) {
                existFolderName = ppapChecklistRepository.findByNameEqualsIgnoreCaseAndParentFileAndPpapAndLatestTrue(pqmppapChecklist.getName(), pqmppapChecklist.getParentFile(), id);
                if (existFolderName != null) {
                    PLMFile file = fileRepository.findOne(pqmppapChecklist.getParentFile());
                    String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", pqmppapChecklist.getName(), file.getName());
                    throw new CassiniException(result);
                }
            } else {
                existFolderName = ppapChecklistRepository.findByPpapAndNameAndParentFileIsNullAndLatestTrue(id, pqmppapChecklist.getName());
                if (existFolderName != null) {
                    String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", pqmppapChecklist.getName());
                    throw new CassiniException(result);
                }
            }
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            pqmppapChecklist.setVersion(1);
            pqmppapChecklist.setSize(0L);
            pqmppapChecklist.setPpap(id);
            pqmppapChecklist.setFileNo(folderNumber);
            pqmppapChecklist.setFileType("FOLDER");
            pqmppapChecklist.setRevision(setRevisionAndLifecyclePhase());
            pqmppapChecklist.setLifeCyclePhase(setPPAPLifecyclePhase());
            pqmppapChecklist = ppapChecklistRepository.save(pqmppapChecklist);
            if (pqmppapChecklist.getParentFile() != null) {
                PQMPPAPChecklist parent = ppapChecklistRepository.findOne(pqmppapChecklist.getParentFile());
                parent.setModifiedDate(pqmppapChecklist.getModifiedDate());
                parent = ppapChecklistRepository.save(parent);
            }
            plmFile = fileRepository.findOne(pqmppapChecklist.getId());
            objectFileDto.setObjectFile(convertFileIdToDto(pqmppapChecklist.getPpap(), objectType, pqmppapChecklist.getId()));
            PQMPPAP ppap = ppapRepository.findOne(id);
            applicationEventPublisher.publishEvent(new PPAPEvents.PPAPFoldersAddedEvent(id, plmFile));
        } else if (objectType.equals(PLMObjectType.ITEM)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMItemFile itemFile = new PLMItemFile();
            itemFile.setName(fileDto.getName());
            itemFile.setDescription(fileDto.getDescription());
            itemFile.setParentFile(fileDto.getParentFile());
            itemFile.setVersion(1);
            itemFile.setSize(0L);
            itemFile = itemFileService.createFolder(id, itemFile);
            objectFileDto.setObjectFile(convertFileIdToDto(itemFile.getItem().getId(), objectType, itemFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROJECT)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMProjectFile projectFile = new PLMProjectFile();
            projectFile.setName(fileDto.getName());
            projectFile.setDescription(fileDto.getDescription());
            projectFile.setParentFile(fileDto.getParentFile());
            projectFile.setVersion(1);
            projectFile.setSize(0L);
            projectFile = projectService.createProjectFolder(id, projectFile);
            objectFileDto.setObjectFile(convertFileIdToDto(projectFile.getProject(), objectType, projectFile.getId()));
        } else if (objectType.equals(PLMObjectType.TEMPLATE)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            ProjectTemplateFile projectFile = new ProjectTemplateFile();
            projectFile.setName(fileDto.getName());
            projectFile.setDescription(fileDto.getDescription());
            projectFile.setParentFile(fileDto.getParentFile());
            projectFile.setVersion(1);
            projectFile.setSize(0L);
            projectFile = projectTemplateService.createProjectTemplateFolder(id, projectFile);
            objectFileDto.setObjectFile(convertFileIdToDto(projectFile.getTemplate(), objectType, projectFile.getId()));
        } else if (objectType.equals(PLMObjectType.TEMPLATEACTIVITY)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            ProjectTemplateActivityFile projectFile = new ProjectTemplateActivityFile();
            projectFile.setName(fileDto.getName());
            projectFile.setDescription(fileDto.getDescription());
            projectFile.setParentFile(fileDto.getParentFile());
            projectFile.setVersion(1);
            projectFile.setSize(0L);
            projectFile = projectTemplateActivityService.createProjectTemplateActivityFolder(id, projectFile);
            objectFileDto.setObjectFile(convertFileIdToDto(projectFile.getActivity(), objectType, projectFile.getId()));
        } else if (objectType.equals(PLMObjectType.TEMPLATETASK)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            ProjectTemplateTaskFile projectFile = new ProjectTemplateTaskFile();
            projectFile.setName(fileDto.getName());
            projectFile.setDescription(fileDto.getDescription());
            projectFile.setParentFile(fileDto.getParentFile());
            projectFile.setVersion(1);
            projectFile.setSize(0L);
            projectFile = projectTemplateActivityService.createProjectTemplateTaskFolder(id, projectFile);
            objectFileDto.setObjectFile(convertFileIdToDto(projectFile.getTask(), objectType, projectFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROGRAMTEMPLATE)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            ProgramTemplateFile templateFile = new ProgramTemplateFile();
            templateFile.setName(fileDto.getName());
            templateFile.setDescription(fileDto.getDescription());
            templateFile.setParentFile(fileDto.getParentFile());
            templateFile.setVersion(1);
            templateFile.setSize(0L);
            templateFile = programTemplateService.createProgramTemplateFolder(id, templateFile);
            objectFileDto.setObjectFile(convertFileIdToDto(templateFile.getTemplate(), objectType, templateFile.getId()));
        } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMManufacturerFile projectFile = new PLMManufacturerFile();
            projectFile.setName(fileDto.getName());
            projectFile.setDescription(fileDto.getDescription());
            projectFile.setParentFile(fileDto.getParentFile());
            projectFile.setVersion(1);
            projectFile.setSize(0L);
            projectFile = manufacturerFileService.createMfrFolder(id, projectFile);
            objectFileDto.setObjectFile(convertFileIdToDto(projectFile.getManufacturer(), objectType, projectFile.getId()));
        } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMManufacturerPartFile projectFile = new PLMManufacturerPartFile();
            projectFile.setName(fileDto.getName());
            projectFile.setDescription(fileDto.getDescription());
            projectFile.setParentFile(fileDto.getParentFile());
            projectFile.setVersion(1);
            projectFile.setSize(0L);
            projectFile = manufacturerPartFileService.createMfrPartFolder(id, projectFile);
            objectFileDto.setObjectFile(convertFileIdToDto(projectFile.getManufacturerPart(), objectType, projectFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMActivityFile projectFile = new PLMActivityFile();
            projectFile.setName(fileDto.getName());
            projectFile.setDescription(fileDto.getDescription());
            projectFile.setParentFile(fileDto.getParentFile());
            projectFile.setVersion(1);
            projectFile.setSize(0L);
            projectFile = activityService.createActivityFolder(id, projectFile);
            objectFileDto.setObjectFile(convertFileIdToDto(projectFile.getActivity(), objectType, projectFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMTaskFile projectFile = new PLMTaskFile();
            projectFile.setName(fileDto.getName());
            projectFile.setDescription(fileDto.getDescription());
            projectFile.setParentFile(fileDto.getParentFile());
            projectFile.setVersion(1);
            projectFile.setSize(0L);
            projectFile = activityService.createActivityTaskFolder(id, projectFile);
            objectFileDto.setObjectFile(convertFileIdToDto(projectFile.getTask(), objectType, projectFile.getId()));
        } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMGlossaryFile projectFile = new PLMGlossaryFile();
            projectFile.setName(fileDto.getName());
            projectFile.setDescription(fileDto.getDescription());
            projectFile.setParentFile(fileDto.getParentFile());
            projectFile.setVersion(1);
            projectFile.setSize(0L);
            projectFile = glossaryService.createGlossaryFolder(id, projectFile);
            objectFileDto.setObjectFile(convertFileIdToDto(projectFile.getGlossary(), objectType, projectFile.getId()));
        } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMSupplierFile supplierFile = new PLMSupplierFile();
            supplierFile.setName(fileDto.getName());
            supplierFile.setDescription(fileDto.getDescription());
            supplierFile.setParentFile(fileDto.getParentFile());
            supplierFile.setVersion(1);
            supplierFile.setSize(0L);
            supplierFile = supplierFileService.createSupplierFolder(id, supplierFile);
            objectFileDto.setObjectFile(convertFileIdToDto(supplierFile.getSupplier(), objectType, supplierFile.getId()));
        } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PQMCustomerFile customerFile = new PQMCustomerFile();
            customerFile.setName(fileDto.getName());
            customerFile.setDescription(fileDto.getDescription());
            customerFile.setParentFile(fileDto.getParentFile());
            customerFile.setVersion(1);
            customerFile.setSize(0L);
            customerFile = customerFileService.createCustomerFolder(id, customerFile);
            objectFileDto.setObjectFile(convertFileIdToDto(customerFile.getCustomer(), objectType, customerFile.getId()));
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMRequirementDocumentFile documentFile = new PLMRequirementDocumentFile();
            documentFile.setName(fileDto.getName());
            documentFile.setDescription(fileDto.getDescription());
            documentFile.setParentFile(fileDto.getParentFile());
            documentFile.setVersion(1);
            documentFile.setSize(0L);
            documentFile = reqDocumentFileService.createReqDocumentFolder(id, documentFile);
            objectFileDto.setObjectFile(convertFileIdToDto(documentFile.getDocumentRevision().getId(), objectType, documentFile.getId()));
        } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMRequirementFile requirementFile = new PLMRequirementFile();
            requirementFile.setName(fileDto.getName());
            requirementFile.setDescription(fileDto.getDescription());
            requirementFile.setParentFile(fileDto.getParentFile());
            requirementFile.setVersion(1);
            requirementFile.setSize(0L);
            requirementFile = requirementFileService.createRequirementFolder(id, requirementFile);
            objectFileDto.setObjectFile(convertFileIdToDto(requirementFile.getRequirement().getId(), objectType, requirementFile.getId()));
        } else if (objectType.equals(PLMObjectType.PLMNPR)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMNprFile nprFile = new PLMNprFile();
            nprFile.setName(fileDto.getName());
            nprFile.setDescription(fileDto.getDescription());
            nprFile.setParentFile(fileDto.getParentFile());
            nprFile.setVersion(1);
            nprFile.setSize(0L);
            nprFile = nprFileService.createNprFolder(id, nprFile);
            objectFileDto.setObjectFile(convertFileIdToDto(nprFile.getNpr(), objectType, nprFile.getId()));
        } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            MESMBOMFile mesmbomFile = new MESMBOMFile();
            mesmbomFile.setName(fileDto.getName());
            mesmbomFile.setDescription(fileDto.getDescription());
            mesmbomFile.setParentFile(fileDto.getParentFile());
            mesmbomFile.setVersion(1);
            mesmbomFile.setSize(0L);
            mesmbomFile = mbomFileService.createNprFolder(id, mesmbomFile);
            objectFileDto.setObjectFile(convertFileIdToDto(mesmbomFile.getMbomRevision(), objectType, mesmbomFile.getId()));
        } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            MESMBOMInstanceFile mesmbomFile = new MESMBOMInstanceFile();
            mesmbomFile.setName(fileDto.getName());
            mesmbomFile.setDescription(fileDto.getDescription());
            mesmbomFile.setParentFile(fileDto.getParentFile());
            mesmbomFile.setVersion(1);
            mesmbomFile.setSize(0L);
            mesmbomFile = mbomInstanceFileService.createMBOMInstanceFolder(id, mesmbomFile);
            objectFileDto.setObjectFile(convertFileIdToDto(mesmbomFile.getMbomInstance(), objectType, mesmbomFile.getId()));
        } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            MESBOPFile mesbopFile = new MESBOPFile();
            mesbopFile.setName(fileDto.getName());
            mesbopFile.setDescription(fileDto.getDescription());
            mesbopFile.setParentFile(fileDto.getParentFile());
            mesbopFile.setVersion(1);
            mesbopFile.setSize(0L);
            mesbopFile = bopFileService.createNprFolder(id, mesbopFile);
            objectFileDto.setObjectFile(convertFileIdToDto(mesbopFile.getBop(), objectType, mesbopFile.getId()));
        } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            MESBOPOperationFile mesbopFile = new MESBOPOperationFile();
            mesbopFile.setName(fileDto.getName());
            mesbopFile.setDescription(fileDto.getDescription());
            mesbopFile.setParentFile(fileDto.getParentFile());
            mesbopFile.setVersion(1);
            mesbopFile.setSize(0L);
            mesbopFile = bopPlanFileService.createBOPPlanFolder(id, mesbopFile);
            objectFileDto.setObjectFile(convertFileIdToDto(mesbopFile.getBopOperation(), objectType, mesbopFile.getId()));
        } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            MESBOPInstanceOperationFile mesbopFile = new MESBOPInstanceOperationFile();
            mesbopFile.setName(fileDto.getName());
            mesbopFile.setDescription(fileDto.getDescription());
            mesbopFile.setParentFile(fileDto.getParentFile());
            mesbopFile.setVersion(1);
            mesbopFile.setSize(0L);
            mesbopFile = bopInstanceOperationFileService.createBOPPlanFolder(id, mesbopFile);
            objectFileDto.setObjectFile(convertFileIdToDto(mesbopFile.getBopOperation(), objectType, mesbopFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMProgramFile programFile = new PLMProgramFile();
            programFile.setName(fileDto.getName());
            programFile.setDescription(fileDto.getDescription());
            programFile.setParentFile(fileDto.getParentFile());
            programFile.setVersion(1);
            programFile.setSize(0L);
            programFile = programFileService.createNprFolder(id, programFile);
            objectFileDto.setObjectFile(convertFileIdToDto(programFile.getProgram(), objectType, programFile.getId()));
        } else if (objectType.equals(PLMObjectType.CHANGE)) {
            FileDto fileDto = objectFileDto.getObjectFile();
            PLMChangeFile changeFile = new PLMChangeFile();
            changeFile.setName(fileDto.getName());
            changeFile.setDescription(fileDto.getDescription());
            changeFile.setParentFile(fileDto.getParentFile());
            changeFile.setVersion(1);
            changeFile.setSize(0L);
            changeFile = changeFileService.createFolder(id, changeFile);
            objectFileDto.setObjectFile(convertFileIdToDto(changeFile.getChange(), objectType, changeFile.getId()));
        }

        if (plmFile != null) {
            String dir = "";
            if (objectType.equals(PLMObjectType.DOCUMENT)) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + "documents" + getDocumentParentFileSystemPath(plmFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, plmFile.getId(), objectType);
            }
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
        }

        return objectFileDto;
    }

    public String getDocumentParentFileSystemPath(Integer fileId) {
        String path = "";
        PLMFile file = plmDocumentRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (file.getParentFile() != null) {
            path = visitDocumentParentFolder(file.getParentFile(), path);
        } else {
            path = File.separator + file.getId();
        }
        return path;
    }


    private String visitDocumentParentFolder(Integer fileId, String path) {
        PLMFile file = plmDocumentRepository.findOne(fileId);
        if (file.getParentFile() != null) {
            path = File.separator + file.getId() + path;
            path = visitDocumentParentFolder(file.getParentFile(), path);
        } else {
            path = File.separator + file.getId() + path;
            return path;
        }
        return path;
    }

    public String getParentFileSystemPath(Integer id, Integer fileId, PLMObjectType objectType) {
        String path = "";
        PLMFile file = getFileObject(fileId, objectType);
        path = File.separator + fileId + "";
        if (file.getParentFile() != null) {
            path = visitParentFolder(id, file.getParentFile(), path, objectType);
        } else {
            path = File.separator + id + File.separator + file.getId();
        }
        return path;
    }

    public String getParentFileNamePath(String name, Integer fileId, PLMObjectType objectType) {
        String path = "";
        PLMFile file = getFileObject(fileId, objectType);
        path = File.separator + file.getName() + "";
        if (file.getParentFile() != null) {
            path = visitParentFolderName(name, file.getParentFile(), path, objectType);
        } else {
            path = File.separator + name + File.separator + file.getName();
        }
        return path;
    }

    private String visitParentFolderName(String name, Integer fileId, String path, PLMObjectType objectType) {
        PLMFile file = getFileObject(fileId, objectType);
        if (file.getParentFile() != null) {
            path = File.separator + file.getName() + path;
            path = visitParentFolderName(name, file.getParentFile(), path, objectType);
        } else {
            path = File.separator + name + File.separator + file.getName() + path;
            return path;
        }
        return path;
    }

    private String visitParentFolder(Integer id, Integer fileId, String path, PLMObjectType objectType) {
        PLMFile file = getFileObject(fileId, objectType);
        if (file.getParentFile() != null) {
            path = File.separator + file.getId() + path;
            path = visitParentFolder(id, file.getParentFile(), path, objectType);
        } else {
            path = File.separator + id + File.separator + file.getId() + path;
            return path;
        }
        return path;
    }

    private PLMFile getFileObject(Integer id, PLMObjectType objectType) {
        PLMFile file = null;
        if (objectType.equals(PLMObjectType.INSPECTIONPLAN) || objectType.equals(PLMObjectType.INSPECTIONPLANREVISION)) {
            PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(id);
            file = fileRepository.findOne(inspectionPlanFile.getId());
        } else if (objectType.equals(PLMObjectType.INSPECTION) || objectType.equals(PLMObjectType.ITEMINSPECTION) || objectType.equals(PLMObjectType.MATERIALINSPECTION)) {
            PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(id);
            file = fileRepository.findOne(inspectionFile.getId());
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(id);
            file = fileRepository.findOne(problemReportFile.getId());
        } else if (objectType.equals(PLMObjectType.NCR)) {
            PQMNCRFile pqmncrFile = ncrFileRepository.findOne(id);
            file = fileRepository.findOne(pqmncrFile.getId());
        } else if (objectType.equals(PLMObjectType.QCR)) {
            PQMQCRFile pqmqcrFile = qcrFileRepository.findOne(id);
            file = fileRepository.findOne(pqmqcrFile.getId());
        } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
            MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(id);
            file = fileRepository.findOne(mesObjectFile.getId());
        } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
            PLMDocument plmDocument = plmDocumentRepository.findOne(id);
            file = fileRepository.findOne(plmDocument.getId());
        } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(id);
            file = fileRepository.findOne(mfrPartInspectionReport.getId());
        } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
            MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(id);
            file = fileRepository.findOne(mroObjectFile.getId());
        } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
            PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(id);
            file = fileRepository.findOne(pgcObjectFile.getId());
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(id);
            file = fileRepository.findOne(supplierAuditFile.getId());
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(id);
            file = fileRepository.findOne(pqmppapChecklist.getId());
        } else if (objectType.equals(PLMObjectType.ITEM)) {
            PLMItemFile itemFile = itemFileRepository.findOne(id);
            file = fileRepository.findOne(itemFile.getId());
        } else if (objectType.equals(PLMObjectType.PROJECT)) {
            PLMProjectFile projectFile = projectFileRepository.findOne(id);
            file = fileRepository.findOne(projectFile.getId());
        } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
            PLMManufacturerFile manufacturerFile = manufacturerFileRepository.findOne(id);
            file = fileRepository.findOne(manufacturerFile.getId());
        } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
            PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileRepository.findOne(id);
            file = fileRepository.findOne(manufacturerPartFile.getId());
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            PLMActivityFile activityFile = activityFileRepository.findOne(id);
            file = fileRepository.findOne(activityFile.getId());
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            PLMTaskFile taskFile = taskFileRepository.findOne(id);
            file = fileRepository.findOne(taskFile.getId());
        } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
            PLMGlossaryFile glossaryFile = glossaryFileRepository.findOne(id);
            file = fileRepository.findOne(glossaryFile.getId());
        }/* else if (objectType.equals(PLMObjectType.SPECIFICATION) || objectType.equals(PLMObjectType.REQUIREMENT)) {
            RmObjectFile glossaryFile = rmObjectFileRepository.findOne(id);
            file = fileRepository.findOne(glossaryFile.getId());
        }*/ else if (objectType.equals(PLMObjectType.CHANGE) || objectType.equals(PLMObjectType.ECO) || objectType.equals(PLMObjectType.ECR) || objectType.equals(PLMObjectType.DCR)
                || objectType.equals(PLMObjectType.DCO) || objectType.equals(PLMObjectType.MCO) || objectType.equals(PLMObjectType.DEVIATION) || objectType.equals(PLMObjectType.WAIVER)) {
            PLMChangeFile changeFile = changeFileRepository.findOne(id);
            file = fileRepository.findOne(changeFile.getId());
        } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
            PLMSupplierFile supplierFile = supplierFileRepository.findOne(id);
            file = fileRepository.findOne(supplierFile.getId());
        } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
            PQMCustomerFile customerFile = customerFileRepository.findOne(id);
            file = fileRepository.findOne(customerFile.getId());
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
            PLMRequirementDocumentFile requirementDocumentFile = requirementDocumentFileRepository.findOne(id);
            file = fileRepository.findOne(requirementDocumentFile.getId());
        } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
            PLMRequirementFile requirementFile = requirementFileRepository.findOne(id);
            file = fileRepository.findOne(requirementFile.getId());
        } else if (objectType.equals(PLMObjectType.PLMNPR)) {
            PLMNprFile nprFile = nprFileRepository.findOne(id);
            file = fileRepository.findOne(nprFile.getId());
        } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
            MESMBOMFile mesmbomFile = mbomFileRepository.findOne(id);
            file = fileRepository.findOne(mesmbomFile.getId());
        } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
            MESMBOMInstanceFile mesmbomFile = mbomInstanceFileRepository.findOne(id);
            file = fileRepository.findOne(mesmbomFile.getId());
        } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
            MESBOPFile mesbopFile = bopFileRepository.findOne(id);
            file = fileRepository.findOne(mesbopFile.getId());
        } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
            MESBOPOperationFile mesbopFile = bopOperationFileRepository.findOne(id);
            file = fileRepository.findOne(mesbopFile.getId());
        } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
            MESBOPInstanceOperationFile mesbopFile = bopInstanceOperationFileRepository.findOne(id);
            file = fileRepository.findOne(mesbopFile.getId());
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            PLMProgramFile programFile = programFileRepository.findOne(id);
            file = fileRepository.findOne(programFile.getId());
        }
        return file;
    }

    @Transactional(readOnly = true)
    public ObjectFileDto getFolderChildren(Integer id, PLMObjectType objectType, Integer folderId, Boolean hierarchy) {
        ObjectFileDto objectFileDto = new ObjectFileDto();
        List<FileDto> folders = new ArrayList<>();
        List<FileDto> files = new ArrayList<>();
        if (objectType.equals(PLMObjectType.PROGRAM) || objectType.equals(PLMObjectType.PROGRAMTEMPLATE) || objectType.equals(PLMObjectType.PROJECT) || objectType.equals(PLMObjectType.TEMPLATE)
                || objectType.equals(PLMObjectType.TEMPLATEACTIVITY) || objectType.equals(PLMObjectType.TEMPLATETASK) || objectType.equals(PLMObjectType.PROJECTACTIVITY) || objectType.equals(PLMObjectType.PROJECTTASK)) {
            objectFileDto = getObjectFileChildrensByType(folderId, objectType, hierarchy);
        } else {
            List<Integer> foldersList = new ArrayList<>();
            List<Integer> filesList = new ArrayList<>();
            foldersList = fileRepository.getByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(folderId, "FOLDER");
            filesList = fileRepository.getByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(folderId, "FILE");
            if (foldersList.size() > 0) {
                folders = convertFilesIdsToDtoList(id, objectType, foldersList, hierarchy);
            }
            if (filesList.size() > 0) {
                files = convertFilesIdsToDtoList(id, objectType, filesList, hierarchy);
            }
            String documentType = "FILE";
            if (objectType.name().equals("MFRPARTINSPECTIONREPORT")) {
                documentType = objectType.name();
            }
            List<PLMObjectDocument> objectDocuments = objectDocumentRepository.findByObjectAndFolderAndDocumentType(id, folderId, documentType);
            for (PLMObjectDocument objectDocument : objectDocuments) {
                files.add(convertObjectDocumentToDto(objectDocument));
            }

            objectFileDto.getObjectFiles().addAll(folders);
            objectFileDto.getObjectFiles().addAll(files);
        }

        return objectFileDto;
    }

    @Transactional
    @PreAuthorize("hasPermission(#fileId ,'rename') || @documentService.checkDMPemrmissions(authentication, 'rename', #objectType.name(), #fileId)")
    public ObjectFileDto updateFileName(Integer id, PLMObjectType objectType, Integer fileId, String newFileName) throws IOException {
        ObjectFileDto objectFileDto = new ObjectFileDto();
        String oldFileDir = "";
        String dir = "";
        Login login = sessionWrapper.getSession().getLogin();
        if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(id);
            PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(fileId);
            inspectionPlanFile.setLatest(false);
            inspectionPlanFile = inspectionPlanFileRepository.save(inspectionPlanFile);
            PQMInspectionPlanFile newInspectionPlanFile = (PQMInspectionPlanFile) Utils.cloneObject(inspectionPlanFile, PQMInspectionPlanFile.class);
            if (newInspectionPlanFile != null) {
                newInspectionPlanFile.setId(null);
                newInspectionPlanFile.setName(newFileName);
                newInspectionPlanFile.setVersion(inspectionPlanFile.getVersion() + 1);
                newInspectionPlanFile.setLatest(true);
                newInspectionPlanFile.setReplaceFileName(inspectionPlanFile.getName() + " ReName to " + newFileName);
                newInspectionPlanFile = inspectionPlanFileRepository.save(newInspectionPlanFile);
                if (newInspectionPlanFile.getParentFile() != null) {
                    PQMInspectionPlanFile parent = inspectionPlanFileRepository.findOne(newInspectionPlanFile.getParentFile());
                    parent.setModifiedDate(newInspectionPlanFile.getModifiedDate());
                    parent = inspectionPlanFileRepository.save(parent);
                }
                copyFileAttributes(inspectionPlanFile.getId(), newInspectionPlanFile.getId());

                dir = "";
                if (inspectionPlanFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                }
                dir = dir + File.separator + newInspectionPlanFile.getId();

                oldFileDir = "";
                if (newInspectionPlanFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, fileId, objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + fileId;
                }
                objectFileDto.setObjectFile(convertFileIdToDto(inspectionPlanRevision.getId(), objectType, newInspectionPlanFile.getId()));
                applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanFileRenamedEvent(inspectionPlanRevision, inspectionPlanFile, newInspectionPlanFile, "Replace"));
            }
        } else if (objectType.equals(PLMObjectType.INSPECTION)) {
            PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(fileId);
            inspectionFile.setLatest(false);
            inspectionFile = inspectionFileRepository.save(inspectionFile);
            PQMInspectionFile newInspectionFile = (PQMInspectionFile) Utils.cloneObject(inspectionFile, PQMInspectionFile.class);
            if (newInspectionFile != null) {
                newInspectionFile.setId(null);
                newInspectionFile.setName(newFileName);
                newInspectionFile.setVersion(inspectionFile.getVersion() + 1);
                newInspectionFile.setLatest(true);
                newInspectionFile.setReplaceFileName(inspectionFile.getName() + " ReName to " + newFileName);
                newInspectionFile = inspectionFileRepository.save(newInspectionFile);
                if (newInspectionFile.getParentFile() != null) {
                    PQMInspectionFile parent = inspectionFileRepository.findOne(newInspectionFile.getParentFile());
                    parent.setModifiedDate(newInspectionFile.getModifiedDate());
                    parent = inspectionFileRepository.save(parent);
                }
                copyFileAttributes(inspectionFile.getId(), newInspectionFile.getId());

                dir = "";
                if (inspectionFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                }
                dir = dir + File.separator + newInspectionFile.getId();

                oldFileDir = "";
                if (newInspectionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, fileId, objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + fileId;
                }
                objectFileDto.setObjectFile(convertFileIdToDto(newInspectionFile.getInspection(), objectType, newInspectionFile.getId()));
                PQMInspection inspection = inspectionRepository.findOne(newInspectionFile.getInspection());
                applicationEventPublisher.publishEvent(new InspectionEvents.InspectionFileRenamedEvent(inspection, inspectionFile, newInspectionFile, "Rename"));
            }
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(fileId);
            problemReportFile.setLatest(false);
            problemReportFile = problemReportFileRepository.save(problemReportFile);
            PQMProblemReportFile newProblemReportFile = (PQMProblemReportFile) Utils.cloneObject(problemReportFile, PQMProblemReportFile.class);
            if (newProblemReportFile != null) {
                newProblemReportFile.setId(null);
                newProblemReportFile.setName(newFileName);
                newProblemReportFile.setVersion(problemReportFile.getVersion() + 1);
                newProblemReportFile.setLatest(true);
                newProblemReportFile.setReplaceFileName(problemReportFile.getName() + " ReName to " + newFileName);
                newProblemReportFile = problemReportFileRepository.save(newProblemReportFile);
                if (newProblemReportFile.getParentFile() != null) {
                    PQMProblemReportFile parent = problemReportFileRepository.findOne(newProblemReportFile.getParentFile());
                    parent.setModifiedDate(newProblemReportFile.getModifiedDate());
                    parent = problemReportFileRepository.save(parent);
                }
                copyFileAttributes(problemReportFile.getId(), newProblemReportFile.getId());

                dir = "";
                if (problemReportFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                }
                dir = dir + File.separator + newProblemReportFile.getId();

                oldFileDir = "";
                if (newProblemReportFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, fileId, objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + fileId;
                }
                objectFileDto.setObjectFile(convertFileIdToDto(newProblemReportFile.getProblemReport(), objectType, newProblemReportFile.getId()));
                PQMProblemReport problemReport = problemReportRepository.findOne(newProblemReportFile.getProblemReport());
                applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportFileRenamedEvent(problemReport, problemReportFile, newProblemReportFile, "Rename"));
            }
        } else if (objectType.equals(PLMObjectType.NCR)) {
            PQMNCRFile ncrFile = ncrFileRepository.findOne(fileId);
            ncrFile.setLatest(false);
            ncrFile = ncrFileRepository.save(ncrFile);
            PQMNCRFile newNcrFile = (PQMNCRFile) Utils.cloneObject(ncrFile, PQMNCRFile.class);
            if (newNcrFile != null) {
                newNcrFile.setId(null);
                newNcrFile.setName(newFileName);
                newNcrFile.setVersion(ncrFile.getVersion() + 1);
                newNcrFile.setLatest(true);
                newNcrFile.setReplaceFileName(ncrFile.getName() + " ReName to " + newFileName);
                newNcrFile = ncrFileRepository.save(newNcrFile);
                if (newNcrFile.getParentFile() != null) {
                    PQMNCRFile parent = ncrFileRepository.findOne(newNcrFile.getParentFile());
                    parent.setModifiedDate(newNcrFile.getModifiedDate());
                    parent = ncrFileRepository.save(parent);
                }
                copyFileAttributes(ncrFile.getId(), newNcrFile.getId());

                dir = "";
                if (ncrFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                }
                dir = dir + File.separator + newNcrFile.getId();

                oldFileDir = "";
                if (newNcrFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, fileId, objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + fileId;
                }
                objectFileDto.setObjectFile(convertFileIdToDto(newNcrFile.getNcr(), objectType, newNcrFile.getId()));
                PQMNCR pqmncr = ncrRepository.findOne(newNcrFile.getNcr());
                applicationEventPublisher.publishEvent(new NCREvents.NCRFileRenamedEvent(pqmncr, ncrFile, newNcrFile, "Rename"));
            }
        } else if (objectType.equals(PLMObjectType.QCR)) {
            PQMQCRFile qcrFile = qcrFileRepository.findOne(fileId);
            qcrFile.setLatest(false);
            qcrFile = qcrFileRepository.save(qcrFile);
            PQMQCRFile newQcrFile = (PQMQCRFile) Utils.cloneObject(qcrFile, PQMQCRFile.class);
            if (newQcrFile != null) {
                newQcrFile.setId(null);
                newQcrFile.setName(newFileName);
                newQcrFile.setVersion(qcrFile.getVersion() + 1);
                newQcrFile.setLatest(true);
                newQcrFile.setReplaceFileName(qcrFile.getName() + " ReName to " + newFileName);
                newQcrFile = qcrFileRepository.save(newQcrFile);
                if (newQcrFile.getParentFile() != null) {
                    PQMQCRFile parent = qcrFileRepository.findOne(newQcrFile.getParentFile());
                    parent.setModifiedDate(newQcrFile.getModifiedDate());
                    parent = qcrFileRepository.save(parent);
                }
                copyFileAttributes(qcrFile.getId(), newQcrFile.getId());

                dir = "";
                if (qcrFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                }
                dir = dir + File.separator + newQcrFile.getId();

                oldFileDir = "";
                if (newQcrFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, fileId, objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + fileId;
                }
                objectFileDto.setObjectFile(convertFileIdToDto(newQcrFile.getQcr(), objectType, newQcrFile.getId()));
                PQMQCR pqmqcr = qcrRepository.findOne(id);
                applicationEventPublisher.publishEvent(new QCREvents.QCRFileRenamedEvent(pqmqcr, qcrFile, newQcrFile, "Rename"));
            }
        } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
            MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(fileId);
            mesObjectFile.setLatest(false);
            mesObjectFile = mesObjectFileRepository.save(mesObjectFile);
            MESObjectFile newObjectFile = (MESObjectFile) Utils.cloneObject(mesObjectFile, MESObjectFile.class);
            if (newObjectFile != null) {
                newObjectFile.setId(null);
                newObjectFile.setName(newFileName);
                newObjectFile.setVersion(mesObjectFile.getVersion() + 1);
                newObjectFile.setLatest(true);
                newObjectFile.setReplaceFileName(mesObjectFile.getName() + " ReName to " + newFileName);
                newObjectFile = mesObjectFileRepository.save(newObjectFile);
                if (newObjectFile.getParentFile() != null) {
                    MESObjectFile parent = mesObjectFileRepository.findOne(newObjectFile.getParentFile());
                    parent.setModifiedDate(newObjectFile.getModifiedDate());
                    parent = mesObjectFileRepository.save(parent);
                }
                copyFileAttributes(mesObjectFile.getId(), newObjectFile.getId());

                dir = "";
                if (mesObjectFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                }
                dir = dir + File.separator + newObjectFile.getId();

                oldFileDir = "";
                if (newObjectFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, fileId, objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + fileId;
                }
                objectFileDto.setObjectFile(convertFileIdToDto(newObjectFile.getObject(), objectType, newObjectFile.getId()));
                MESObject mesObject = mesObjectRepository.findOne(id);
                applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFileRenamedEvent("mes", id, mesObjectFile, newObjectFile, "Rename", mesObject.getObjectType()));
            }
        } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
            PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
            plmDocument.setLatest(false);
            plmDocument = plmDocumentRepository.save(plmDocument);
            PLMDocument newObjectFile = (PLMDocument) Utils.cloneObject(plmDocument, PLMDocument.class);
            if (newObjectFile != null) {
                newObjectFile.setId(null);
                newObjectFile.setName(newFileName);
                newObjectFile.setVersion(plmDocument.getVersion() + 1);
                newObjectFile.setLatest(true);
                newObjectFile.setReplaceFileName(plmDocument.getName() + " ReName to " + newFileName);
                newObjectFile = plmDocumentRepository.save(newObjectFile);
                if (newObjectFile.getParentFile() != null) {
                    PLMDocument parent = plmDocumentRepository.findOne(newObjectFile.getParentFile());
                    parent.setModifiedDate(newObjectFile.getModifiedDate());
                    parent = plmDocumentRepository.save(parent);
                }
                copyFileAttributes(plmDocument.getId(), newObjectFile.getId());

                dir = "";
                if (plmDocument.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + "documents" + getReplaceDocumentFileSystemPath(fileId);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + "documents";
                }
                dir = dir + File.separator + newObjectFile.getId();

                oldFileDir = "";
                if (newObjectFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + "documents" + getDocumentParentFileSystemPath(fileId);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + "documents" + File.separator + fileId;
                }
                if (plmDocument != null) {
                    documentService.copyReviewers(plmDocument.getId(), newObjectFile.getId());
                }
                objectFileDto.setObjectFile(convertFileIdToDto(0, objectType, newObjectFile.getId()));
                applicationEventPublisher.publishEvent(new DocumentEvents.DocumentFileRenamedEvent(plmDocument, newObjectFile, "Rename"));
            }
        } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            PLMMfrPartInspectionReport plmMfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(fileId);
            plmMfrPartInspectionReport.setLatest(false);
            plmMfrPartInspectionReport = mfrPartInspectionReportRepository.save(plmMfrPartInspectionReport);
            PLMMfrPartInspectionReport newObjectFile = (PLMMfrPartInspectionReport) Utils.cloneObject(plmMfrPartInspectionReport, PLMMfrPartInspectionReport.class);
            if (newObjectFile != null) {
                newObjectFile.setId(null);
                newObjectFile.setName(newFileName);
                newObjectFile.setVersion(plmMfrPartInspectionReport.getVersion() + 1);
                newObjectFile.setLatest(true);
                newObjectFile.setReplaceFileName(plmMfrPartInspectionReport.getName() + " ReName to " + newFileName);
                newObjectFile = mfrPartInspectionReportRepository.save(newObjectFile);
                if (newObjectFile.getParentFile() != null) {
                    PLMMfrPartInspectionReport parent = mfrPartInspectionReportRepository.findOne(newObjectFile.getParentFile());
                    parent.setModifiedDate(newObjectFile.getModifiedDate());
                    parent = mfrPartInspectionReportRepository.save(parent);
                }
                copyFileAttributes(plmMfrPartInspectionReport.getId(), newObjectFile.getId());

                dir = "";
                if (plmMfrPartInspectionReport.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                }
                dir = dir + File.separator + newObjectFile.getId();

                oldFileDir = "";
                if (newObjectFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, fileId, objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + fileId;
                }
                if (plmMfrPartInspectionReport != null) {
                    documentService.copyReviewers(plmMfrPartInspectionReport.getId(), newObjectFile.getId());
                }
                objectFileDto.setObjectFile(convertFileIdToDto(0, objectType, newObjectFile.getId()));
                if (login.getExternal()) {
                    sendReplacedNotification(fileId, plmMfrPartInspectionReport.getName(), newObjectFile, "rename", newObjectFile.getManufacturerPart(), login.getPerson().getFullName(), objectType);
                }
                applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartReportRenamedEvent(id, plmMfrPartInspectionReport, newObjectFile, "Rename"));
            }
        } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
            MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(fileId);
            mroObjectFile.setLatest(false);
            mroObjectFile = mroObjectFileRepository.save(mroObjectFile);
            MROObjectFile newObjectFile = (MROObjectFile) Utils.cloneObject(mroObjectFile, MROObjectFile.class);
            if (newObjectFile != null) {
                newObjectFile.setId(null);
                newObjectFile.setName(newFileName);
                newObjectFile.setVersion(mroObjectFile.getVersion() + 1);
                newObjectFile.setLatest(true);
                newObjectFile.setReplaceFileName(mroObjectFile.getName() + " ReName to " + newFileName);
                newObjectFile = mroObjectFileRepository.save(newObjectFile);
                if (newObjectFile.getParentFile() != null) {
                    MROObjectFile parent = mroObjectFileRepository.findOne(newObjectFile.getParentFile());
                    parent.setModifiedDate(newObjectFile.getModifiedDate());
                    parent = mroObjectFileRepository.save(parent);
                }
                copyFileAttributes(mroObjectFile.getId(), newObjectFile.getId());

                dir = "";
                if (mroObjectFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                }
                dir = dir + File.separator + newObjectFile.getId();

                oldFileDir = "";
                if (newObjectFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, fileId, objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + fileId;
                }
                objectFileDto.setObjectFile(convertFileIdToDto(newObjectFile.getObject(), objectType, newObjectFile.getId()));
                MROObject mroObject = mroObjectRepository.findOne(id);
                applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFileRenamedEvent("mro", id, mroObjectFile, newObjectFile, "Rename", mroObject.getObjectType()));
            }
        } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
            PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(fileId);
            pgcObjectFile.setLatest(false);
            pgcObjectFile = pgcObjectFileRepository.save(pgcObjectFile);
            PGCObjectFile newObjectFile = (PGCObjectFile) Utils.cloneObject(pgcObjectFile, PGCObjectFile.class);
            if (newObjectFile != null) {
                newObjectFile.setId(null);
                newObjectFile.setName(newFileName);
                newObjectFile.setVersion(pgcObjectFile.getVersion() + 1);
                newObjectFile.setLatest(true);
                newObjectFile.setReplaceFileName(pgcObjectFile.getName() + " ReName to " + newFileName);
                newObjectFile = pgcObjectFileRepository.save(newObjectFile);
                if (newObjectFile.getParentFile() != null) {
                    PGCObjectFile parent = pgcObjectFileRepository.findOne(newObjectFile.getParentFile());
                    parent.setModifiedDate(newObjectFile.getModifiedDate());
                    parent = pgcObjectFileRepository.save(parent);
                }
                copyFileAttributes(pgcObjectFile.getId(), newObjectFile.getId());

                dir = "";
                if (pgcObjectFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                }
                dir = dir + File.separator + newObjectFile.getId();

                oldFileDir = "";
                if (newObjectFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, fileId, objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + fileId;
                }
                objectFileDto.setObjectFile(convertFileIdToDto(newObjectFile.getObject(), objectType, newObjectFile.getId()));
                PGCObject pgcObject = pgcObjectRepository.findOne(id);
                applicationEventPublisher.publishEvent(new SubstanceEvents.SubstanceFileRenamedEvent("pgc", id, pgcObjectFile, newObjectFile, "Rename", pgcObject.getObjectType()));
            }
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(fileId);
            supplierAuditFile.setLatest(false);
            supplierAuditFile = supplierAuditFileRepository.save(supplierAuditFile);
            PQMSupplierAuditFile newObjectFile = (PQMSupplierAuditFile) Utils.cloneObject(supplierAuditFile, PQMSupplierAuditFile.class);
            if (newObjectFile != null) {
                newObjectFile.setId(null);
                newObjectFile.setName(newFileName);
                newObjectFile.setVersion(supplierAuditFile.getVersion() + 1);
                newObjectFile.setLatest(true);
                newObjectFile.setReplaceFileName(supplierAuditFile.getName() + " ReName to " + newFileName);
                newObjectFile = supplierAuditFileRepository.save(newObjectFile);
                if (newObjectFile.getParentFile() != null) {
                    PQMSupplierAuditFile parent = supplierAuditFileRepository.findOne(newObjectFile.getParentFile());
                    parent.setModifiedDate(newObjectFile.getModifiedDate());
                    parent = supplierAuditFileRepository.save(parent);
                }
                copyFileAttributes(supplierAuditFile.getId(), newObjectFile.getId());

                dir = "";
                if (supplierAuditFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                }
                dir = dir + File.separator + newObjectFile.getId();

                oldFileDir = "";
                if (newObjectFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, fileId, objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + fileId;
                }
                objectFileDto.setObjectFile(convertFileIdToDto(newObjectFile.getSupplierAudit(), objectType, newObjectFile.getId()));
                PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(id);
                applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditFileRenamedEvent(id, supplierAuditFile, newObjectFile, "Rename"));
            }
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(fileId);
            pqmppapChecklist.setLatest(false);
            pqmppapChecklist = ppapChecklistRepository.save(pqmppapChecklist);
            PQMPPAPChecklist newObjectFile = (PQMPPAPChecklist) Utils.cloneObject(pqmppapChecklist, PQMPPAPChecklist.class);
            if (newObjectFile != null) {
                newObjectFile.setId(null);
                newObjectFile.setName(newFileName);
                newObjectFile.setVersion(pqmppapChecklist.getVersion() + 1);
                newObjectFile.setLatest(true);
                newObjectFile.setReplaceFileName(pqmppapChecklist.getName() + " ReName to " + newFileName);
                newObjectFile = ppapChecklistRepository.save(newObjectFile);
                if (newObjectFile.getParentFile() != null) {
                    PQMPPAPChecklist parent = ppapChecklistRepository.findOne(newObjectFile.getParentFile());
                    parent.setModifiedDate(newObjectFile.getModifiedDate());
                    parent = ppapChecklistRepository.save(parent);
                    Integer rejectedCount = documentReviewerRepository.getRejectedCount(newObjectFile.getParentFile());
                    if (rejectedCount > 0) {
                        List<PLMDocumentReviewer> documentReviewers = documentReviewerRepository.findByDocumentAndStatusOrderByIdDesc(newObjectFile.getParentFile(), DocumentApprovalStatus.REJECTED);
                        documentReviewers.forEach(plmDocumentReviewer -> {
                            plmDocumentReviewer.setStatus(DocumentApprovalStatus.NONE);
                        });
                        documentReviewerRepository.save(documentReviewers);
                    }
                }
                copyFileAttributes(pqmppapChecklist.getId(), newObjectFile.getId());
                documentService.copyReviewers(pqmppapChecklist.getId(), newObjectFile.getId());

                dir = "";
                if (pqmppapChecklist.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                }
                dir = dir + File.separator + newObjectFile.getId();

                oldFileDir = "";
                if (newObjectFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, fileId, objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + fileId;
                }
                objectFileDto.setObjectFile(convertFileIdToDto(newObjectFile.getPpap(), objectType, newObjectFile.getId()));
                PQMPPAP ppap = ppapRepository.findOne(id);
                if (login.getExternal()) {
                    sendReplacedNotification(fileId, pqmppapChecklist.getName(), newObjectFile, "rename", newObjectFile.getPpap(), login.getPerson().getFullName(), objectType);
                }
                applicationEventPublisher.publishEvent(new PPAPEvents.PPAPFileRenamedEvent(ppap, pqmppapChecklist, newObjectFile, "Rename"));
            }
        } else if (objectType.equals(PLMObjectType.ITEM)) {
            PLMItemFile itemFile = itemFileService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(itemFile.getItem().getId(), objectType, itemFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROJECT)) {
            PLMProjectFile projectFile = projectService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(projectFile.getProject(), objectType, projectFile.getId()));
        } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
            PLMManufacturerFile manufacturerFile = manufacturerFileService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(manufacturerFile.getManufacturer(), objectType, manufacturerFile.getId()));
        } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
            PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileService.renamePartFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(manufacturerPartFile.getManufacturerPart(), objectType, manufacturerPartFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            PLMActivityFile activityFile = activityService.renameActivityFile(id, fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(activityFile.getActivity(), objectType, activityFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            PLMTaskFile taskFile = activityService.renameTaskFile(id, fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(taskFile.getTask(), objectType, taskFile.getId()));
        } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
            PLMGlossaryFile glossaryFile = glossaryService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(glossaryFile.getGlossary(), objectType, glossaryFile.getId()));
        } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
            PLMSupplierFile supplierFile = supplierFileService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(supplierFile.getSupplier(), objectType, supplierFile.getId()));
        } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
            PQMCustomerFile customerFile = customerFileService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(customerFile.getCustomer(), objectType, customerFile.getId()));
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
            PLMRequirementDocumentFile documentFile = reqDocumentFileService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(documentFile.getDocumentRevision().getId(), objectType, documentFile.getId()));
        } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
            PLMRequirementFile requirementFile = requirementFileService.updateFileName(id, fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(requirementFile.getRequirement().getId(), objectType, requirementFile.getId()));
        } else if (objectType.equals(PLMObjectType.PLMNPR)) {
            PLMNprFile nprFile = nprFileService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(nprFile.getNpr(), objectType, nprFile.getId()));
        } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
            MESMBOMFile mesmbomFile = mbomFileService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(mesmbomFile.getMbomRevision(), objectType, mesmbomFile.getId()));
        } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
            MESMBOMInstanceFile mesmbomFile = mbomInstanceFileService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(mesmbomFile.getMbomInstance(), objectType, mesmbomFile.getId()));
        } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
            MESBOPFile mesbopFile = bopFileService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(mesbopFile.getBop(), objectType, mesbopFile.getId()));
        } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
            MESBOPOperationFile mesbopFile = bopPlanFileService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(mesbopFile.getBopOperation(), objectType, mesbopFile.getId()));
        } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
            MESBOPInstanceOperationFile mesbopFile = bopInstanceOperationFileService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(mesbopFile.getBopOperation(), objectType, mesbopFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            PLMProgramFile programFile = programFileService.updateFileName(fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(programFile.getProgram(), objectType, programFile.getId()));
        } else if (objectType.equals(PLMObjectType.CHANGE)) {
            PLMChangeFile changeFile = changeFileService.updateFileName(id, fileId, newFileName);
            objectFileDto.setObjectFile(convertFileIdToDto(changeFile.getChange(), objectType, changeFile.getId()));
        }

        if (!dir.equals("")) {
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }

            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
        }

        return objectFileDto;
    }

    public String getReplaceFileSystemPath(Integer id, Integer fileId, PLMObjectType objectType) {
        String path = "";
        PLMFile file = getFileObject(fileId, objectType);
        if (file.getParentFile() != null) {
            path = visitParentFolder(id, file.getParentFile(), path, objectType);
        } else {
            path = File.separator + id;
        }
        return path;
    }

    public String getReplaceDocumentFileSystemPath(Integer fileId) {
        String path = "";
        PLMFile file = plmDocumentRepository.findOne(fileId);
        if (file.getParentFile() != null) {
            path = visitDocumentParentFolder(file.getParentFile(), path);
        }
        return path;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'replace',#type) || @documentService.checkDMPemrmissions(authentication, 'replace', #objectType.name(), #fileId)")
    public ObjectFileDto replaceObjectFile(Integer id, PLMObjectType objectType, Integer fileId, Map<String, MultipartFile> fileMap, String type) throws CassiniException, JsonProcessingException {
        ObjectFileDto objectFileDto = new ObjectFileDto();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        String htmlTable = null;
        String action = null;
        boolean flag = true;
        String name = null;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }

        if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
            PQMInspectionPlanRevision revision = inspectionPlanRevisionRepository.findOne(id);
            PQMInspectionPlanFile inspectionPlanFile = null;
            try {
                for (MultipartFile file : fileMap.values()) {
                    name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                    if (fileExtension != null) {
                        for (String ext : fileExtension) {
                            if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        PQMInspectionPlanFile newInspectionPlanFile = null;
                        inspectionPlanFile = inspectionPlanFileRepository.findOne(fileId);
                        if (inspectionPlanFile != null) {
                            if (inspectionPlanFile.getParentFile() != null) {
                                PQMInspectionPlanFile folder = inspectionPlanFileRepository.findOne(inspectionPlanFile.getParentFile());
                                PQMInspectionPlanFile existFile = inspectionPlanFileRepository.findByParentFileAndNameAndLatestTrue(inspectionPlanFile.getParentFile(), name);
                                if (existFile != null && !existFile.getId().equals(inspectionPlanFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", inspectionPlanFile.getName(), folder.getName());
                                    throw new CassiniException(result);
                                }
                            } else {
                                PQMInspectionPlanFile existFile = inspectionPlanFileRepository.findByInspectionPlanAndNameAndParentFileIsNullAndLatestTrue(inspectionPlanFile.getInspectionPlan(), name);
                                if (existFile != null && !existFile.getId().equals(inspectionPlanFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", inspectionPlanFile.getName());
                                    throw new CassiniException(result);
                                }
                            }

                            inspectionPlanFile.setLatest(false);
                            inspectionPlanFile = inspectionPlanFileRepository.save(inspectionPlanFile);
                        }

                        newInspectionPlanFile = new PQMInspectionPlanFile();
                        newInspectionPlanFile.setName(name);
                        if (inspectionPlanFile != null && inspectionPlanFile.getParentFile() != null) {
                            newInspectionPlanFile.setParentFile(inspectionPlanFile.getParentFile());
                        }
                        if (inspectionPlanFile != null) {
                            newInspectionPlanFile.setFileNo(inspectionPlanFile.getFileNo());
                            newInspectionPlanFile.setVersion(inspectionPlanFile.getVersion() + 1);
                            newInspectionPlanFile.setReplaceFileName(inspectionPlanFile.getName() + " Replaced to " + name);
                        }
                        newInspectionPlanFile.setCreatedBy(login.getPerson().getId());
                        newInspectionPlanFile.setModifiedBy(login.getPerson().getId());
                        newInspectionPlanFile.setInspectionPlan(id);
                        newInspectionPlanFile.setFileType("FILE");
                        newInspectionPlanFile.setSize(file.getSize());
                        newInspectionPlanFile = inspectionPlanFileRepository.save(newInspectionPlanFile);
                        if (newInspectionPlanFile.getParentFile() != null) {
                            PQMInspectionPlanFile parent = inspectionPlanFileRepository.findOne(newInspectionPlanFile.getParentFile());
                            parent.setModifiedDate(newInspectionPlanFile.getModifiedDate());
                            parent = inspectionPlanFileRepository.save(parent);
                        }
                        if (inspectionPlanFile != null) {
                            copyFileAttributes(inspectionPlanFile.getId(), newInspectionPlanFile.getId());
                        }
                        String dir = "";
                        if (inspectionPlanFile != null && inspectionPlanFile.getParentFile() != null) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                        } else {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                        }
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }
                        String path = dir + File.separator + newInspectionPlanFile.getId();
                        fileSystemService.saveDocumentToDisk(file, path);

                        objectFileDto.setObjectFile(convertFileIdToDto(newInspectionPlanFile.getInspectionPlan(), objectType, newInspectionPlanFile.getId()));
                        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanFileRenamedEvent(revision, inspectionPlanFile, newInspectionPlanFile, "Replace"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (objectType.equals(PLMObjectType.INSPECTION)) {
            try {
                for (MultipartFile file : fileMap.values()) {
                    name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                    if (fileExtension != null) {
                        for (String ext : fileExtension) {
                            if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(fileId);
                        if (inspectionFile != null) {
                            if (inspectionFile.getParentFile() != null) {
                                PQMInspectionPlanFile folder = inspectionPlanFileRepository.findOne(inspectionFile.getParentFile());
                                PQMInspectionPlanFile existFile = inspectionPlanFileRepository.findByParentFileAndNameAndLatestTrue(inspectionFile.getParentFile(), name);
                                if (existFile != null && !existFile.getId().equals(inspectionFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", inspectionFile.getName(), folder.getName());
                                    throw new CassiniException(result);
                                }
                            } else {
                                PQMInspectionPlanFile existFile = inspectionPlanFileRepository.findByInspectionPlanAndNameAndParentFileIsNullAndLatestTrue(inspectionFile.getInspection(), name);
                                if (existFile != null && !existFile.getId().equals(inspectionFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", inspectionFile.getName());
                                    throw new CassiniException(result);
                                }
                            }

                            inspectionFile.setLatest(false);
                            inspectionFile = inspectionFileRepository.save(inspectionFile);
                        }

                        PQMInspectionFile newInspectionFile = new PQMInspectionFile();
                        newInspectionFile.setName(name);
                        if (inspectionFile != null && inspectionFile.getParentFile() != null) {
                            newInspectionFile.setParentFile(inspectionFile.getParentFile());
                        }
                        if (inspectionFile != null) {
                            newInspectionFile.setFileNo(inspectionFile.getFileNo());
                            newInspectionFile.setVersion(inspectionFile.getVersion() + 1);
                            newInspectionFile.setReplaceFileName(inspectionFile.getName() + " Replaced to " + name);
                        }
                        newInspectionFile.setCreatedBy(login.getPerson().getId());
                        newInspectionFile.setModifiedBy(login.getPerson().getId());
                        newInspectionFile.setInspection(id);
                        newInspectionFile.setFileType("FILE");
                        newInspectionFile.setSize(file.getSize());
                        newInspectionFile = inspectionFileRepository.save(newInspectionFile);
                        if (newInspectionFile.getParentFile() != null) {
                            PQMInspectionFile parent = inspectionFileRepository.findOne(newInspectionFile.getParentFile());
                            parent.setModifiedDate(newInspectionFile.getModifiedDate());
                            parent = inspectionFileRepository.save(parent);
                        }
                        if (inspectionFile != null) {
                            copyFileAttributes(inspectionFile.getId(), newInspectionFile.getId());
                        }

                        String dir = "";
                        if (inspectionFile != null && inspectionFile.getParentFile() != null) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                        } else {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                        }
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }
                        String path = dir + File.separator + newInspectionFile.getId();
                        fileSystemService.saveDocumentToDisk(file, path);

                        objectFileDto.setObjectFile(convertFileIdToDto(newInspectionFile.getInspection(), objectType, newInspectionFile.getId()));
                        PQMInspection inspection = inspectionRepository.findOne(newInspectionFile.getInspection());
                        applicationEventPublisher.publishEvent(new InspectionEvents.InspectionFileRenamedEvent(inspection, inspectionFile, newInspectionFile, "Replace"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            try {
                for (MultipartFile file : fileMap.values()) {
                    name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                    if (fileExtension != null) {
                        for (String ext : fileExtension) {
                            if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(fileId);
                        if (problemReportFile != null) {
                            if (problemReportFile.getParentFile() != null) {
                                PQMInspectionPlanFile folder = inspectionPlanFileRepository.findOne(problemReportFile.getParentFile());
                                PQMInspectionPlanFile existFile = inspectionPlanFileRepository.findByParentFileAndNameAndLatestTrue(problemReportFile.getParentFile(), name);
                                if (existFile != null && !existFile.getId().equals(problemReportFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", problemReportFile.getName(), folder.getName());
                                    throw new CassiniException(result);
                                }
                            } else {
                                PQMInspectionPlanFile existFile = inspectionPlanFileRepository.findByInspectionPlanAndNameAndParentFileIsNullAndLatestTrue(problemReportFile.getProblemReport(), name);
                                if (existFile != null && !existFile.getId().equals(problemReportFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", problemReportFile.getName());
                                    throw new CassiniException(result);
                                }
                            }

                            problemReportFile.setLatest(false);
                            problemReportFile = problemReportFileRepository.save(problemReportFile);
                        }

                        PQMProblemReportFile newProblemReportFile = new PQMProblemReportFile();
                        newProblemReportFile.setName(name);
                        if (problemReportFile != null && problemReportFile.getParentFile() != null) {
                            newProblemReportFile.setParentFile(problemReportFile.getParentFile());
                        }
                        if (problemReportFile != null) {
                            newProblemReportFile.setFileNo(problemReportFile.getFileNo());
                            newProblemReportFile.setVersion(problemReportFile.getVersion() + 1);
                            newProblemReportFile.setReplaceFileName(problemReportFile.getName() + " Replaced to " + name);
                        }

                        newProblemReportFile.setCreatedBy(login.getPerson().getId());
                        newProblemReportFile.setModifiedBy(login.getPerson().getId());
                        newProblemReportFile.setProblemReport(id);
                        newProblemReportFile.setFileType("FILE");
                        newProblemReportFile.setSize(file.getSize());
                        newProblemReportFile = problemReportFileRepository.save(newProblemReportFile);
                        if (newProblemReportFile.getParentFile() != null) {
                            PQMProblemReportFile parent = problemReportFileRepository.findOne(newProblemReportFile.getParentFile());
                            parent.setModifiedDate(newProblemReportFile.getModifiedDate());
                            parent = problemReportFileRepository.save(parent);
                        }
                        if (problemReportFile != null) {
                            copyFileAttributes(problemReportFile.getId(), newProblemReportFile.getId());
                        }
                        String dir = "";
                        if (problemReportFile != null && problemReportFile.getParentFile() != null) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                        } else {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                        }
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }
                        String path = dir + File.separator + newProblemReportFile.getId();
                        fileSystemService.saveDocumentToDisk(file, path);
                        objectFileDto.setObjectFile(convertFileIdToDto(newProblemReportFile.getProblemReport(), objectType, newProblemReportFile.getId()));
                        PQMProblemReport problemReport = problemReportRepository.findOne(newProblemReportFile.getProblemReport());
                        applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportFileRenamedEvent(problemReport, problemReportFile, newProblemReportFile, "Replace"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (objectType.equals(PLMObjectType.NCR)) {
            try {
                for (MultipartFile file : fileMap.values()) {
                    name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                    if (fileExtension != null) {
                        for (String ext : fileExtension) {
                            if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        PQMNCRFile ncrFile = ncrFileRepository.findOne(fileId);
                        if (ncrFile != null) {
                            if (ncrFile.getParentFile() != null) {
                                PQMInspectionPlanFile folder = inspectionPlanFileRepository.findOne(ncrFile.getParentFile());
                                PQMInspectionPlanFile existFile = inspectionPlanFileRepository.findByParentFileAndNameAndLatestTrue(ncrFile.getParentFile(), name);
                                if (existFile != null && !existFile.getId().equals(ncrFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", ncrFile.getName(), folder.getName());
                                    throw new CassiniException(result);
                                }
                            } else {
                                PQMInspectionPlanFile existFile = inspectionPlanFileRepository.findByInspectionPlanAndNameAndParentFileIsNullAndLatestTrue(ncrFile.getNcr(), name);
                                if (existFile != null && !existFile.getId().equals(ncrFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", ncrFile.getName());
                                    throw new CassiniException(result);
                                }
                            }

                            ncrFile.setLatest(false);
                            ncrFile = ncrFileRepository.save(ncrFile);
                        }

                        PQMNCRFile newNcrFile = new PQMNCRFile();
                        newNcrFile.setName(name);
                        if (ncrFile != null && ncrFile.getParentFile() != null) {
                            newNcrFile.setParentFile(ncrFile.getParentFile());
                        }
                        if (ncrFile != null) {
                            newNcrFile.setFileNo(ncrFile.getFileNo());
                            newNcrFile.setVersion(ncrFile.getVersion() + 1);
                            newNcrFile.setReplaceFileName(ncrFile.getName() + " Replaced to " + name);
                        }
                        newNcrFile.setCreatedBy(login.getPerson().getId());
                        newNcrFile.setModifiedBy(login.getPerson().getId());
                        newNcrFile.setNcr(id);
                        newNcrFile.setFileType("FILE");
                        newNcrFile.setSize(file.getSize());

                        newNcrFile = ncrFileRepository.save(newNcrFile);
                        if (newNcrFile.getParentFile() != null) {
                            PQMNCRFile parent = ncrFileRepository.findOne(newNcrFile.getParentFile());
                            parent.setModifiedDate(newNcrFile.getModifiedDate());
                            parent = ncrFileRepository.save(parent);
                        }
                        if (ncrFile != null) {
                            copyFileAttributes(ncrFile.getId(), newNcrFile.getId());
                        }
                        String dir = "";
                        if (ncrFile != null && ncrFile.getParentFile() != null) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                        } else {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                        }
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }
                        String path = dir + File.separator + newNcrFile.getId();
                        fileSystemService.saveDocumentToDisk(file, path);

                        objectFileDto.setObjectFile(convertFileIdToDto(newNcrFile.getNcr(), objectType, newNcrFile.getId()));
                        PQMNCR pqmncr = ncrRepository.findOne(newNcrFile.getNcr());
                        applicationEventPublisher.publishEvent(new NCREvents.NCRFileRenamedEvent(pqmncr, ncrFile, newNcrFile, "Replace"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (objectType.equals(PLMObjectType.QCR)) {
            try {
                for (MultipartFile file : fileMap.values()) {
                    name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                    if (fileExtension != null) {
                        for (String ext : fileExtension) {
                            if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        PQMQCRFile qcrFile = qcrFileRepository.findOne(fileId);
                        if (qcrFile != null) {
                            if (qcrFile.getParentFile() != null) {
                                PQMQCRFile folder = qcrFileRepository.findOne(qcrFile.getParentFile());
                                PQMQCRFile existFile = qcrFileRepository.findByParentFileAndNameAndLatestTrue(qcrFile.getParentFile(), name);
                                if (existFile != null && !existFile.getId().equals(qcrFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", qcrFile.getName(), folder.getName());
                                    throw new CassiniException(result);
                                }
                            } else {
                                PQMQCRFile existFile = qcrFileRepository.findByQcrAndNameAndParentFileIsNullAndLatestTrue(qcrFile.getQcr(), name);
                                if (existFile != null && !existFile.getId().equals(qcrFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", qcrFile.getName());
                                    throw new CassiniException(result);
                                }
                            }

                            qcrFile.setLatest(false);
                            qcrFile = qcrFileRepository.save(qcrFile);
                        }

                        PQMQCRFile newQcrFile = new PQMQCRFile();
                        newQcrFile.setName(name);
                        if (qcrFile != null && qcrFile.getParentFile() != null) {
                            newQcrFile.setParentFile(qcrFile.getParentFile());
                        }
                        if (qcrFile != null) {
                            newQcrFile.setFileNo(qcrFile.getFileNo());
                            newQcrFile.setVersion(qcrFile.getVersion() + 1);
                            newQcrFile.setReplaceFileName(qcrFile.getName() + " Replaced to " + name);
                        }
                        newQcrFile.setCreatedBy(login.getPerson().getId());
                        newQcrFile.setModifiedBy(login.getPerson().getId());
                        newQcrFile.setQcr(id);
                        newQcrFile.setFileType("FILE");
                        newQcrFile.setSize(file.getSize());
                        newQcrFile = qcrFileRepository.save(newQcrFile);
                        if (newQcrFile.getParentFile() != null) {
                            PQMQCRFile parent = qcrFileRepository.findOne(newQcrFile.getParentFile());
                            parent.setModifiedDate(newQcrFile.getModifiedDate());
                            parent = qcrFileRepository.save(parent);
                        }
                        if (qcrFile != null) {
                            copyFileAttributes(qcrFile.getId(), newQcrFile.getId());
                        }
                        String dir = "";
                        if (qcrFile != null && qcrFile.getParentFile() != null) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                        } else {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                        }
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }
                        String path = dir + File.separator + newQcrFile.getId();
                        fileSystemService.saveDocumentToDisk(file, path);

                        objectFileDto.setObjectFile(convertFileIdToDto(newQcrFile.getQcr(), objectType, newQcrFile.getId()));
                        PQMQCR pqmqcr = qcrRepository.findOne(newQcrFile.getQcr());
                        applicationEventPublisher.publishEvent(new QCREvents.QCRFileRenamedEvent(pqmqcr, qcrFile, newQcrFile, "Replace"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
            try {
                for (MultipartFile file : fileMap.values()) {
                    name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                    if (fileExtension != null) {
                        for (String ext : fileExtension) {
                            if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(fileId);
                        if (mesObjectFile != null) {
                            if (mesObjectFile.getParentFile() != null) {
                                MESObjectFile folder = mesObjectFileRepository.findOne(mesObjectFile.getParentFile());
                                MESObjectFile existFile = mesObjectFileRepository.findByParentFileAndNameAndLatestTrue(mesObjectFile.getParentFile(), name);
                                if (existFile != null && !existFile.getId().equals(mesObjectFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", mesObjectFile.getName(), folder.getName());
                                    throw new CassiniException(result);
                                }
                            } else {
                                MESObjectFile existFile = mesObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(mesObjectFile.getObject(), name);
                                if (existFile != null && !existFile.getId().equals(mesObjectFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", mesObjectFile.getName());
                                    throw new CassiniException(result);
                                }
                            }

                            mesObjectFile.setLatest(false);
                            mesObjectFile = mesObjectFileRepository.save(mesObjectFile);
                        }

                        MESObjectFile newObjectFile = new MESObjectFile();
                        newObjectFile.setName(name);
                        if (mesObjectFile != null && mesObjectFile.getParentFile() != null) {
                            newObjectFile.setParentFile(mesObjectFile.getParentFile());
                        }
                        if (mesObjectFile != null) {
                            newObjectFile.setFileNo(mesObjectFile.getFileNo());
                            newObjectFile.setVersion(mesObjectFile.getVersion() + 1);
                            newObjectFile.setReplaceFileName(mesObjectFile.getName() + " Replaced to " + name);
                        }
                        newObjectFile.setCreatedBy(login.getPerson().getId());
                        newObjectFile.setModifiedBy(login.getPerson().getId());
                        newObjectFile.setObject(id);
                        newObjectFile.setFileType("FILE");
                        newObjectFile.setSize(file.getSize());
                        newObjectFile = mesObjectFileRepository.save(newObjectFile);
                        if (newObjectFile.getParentFile() != null) {
                            MESObjectFile parent = mesObjectFileRepository.findOne(newObjectFile.getParentFile());
                            parent.setModifiedDate(newObjectFile.getModifiedDate());
                            parent = mesObjectFileRepository.save(parent);
                        }
                        if (mesObjectFile != null) {
                            copyFileAttributes(mesObjectFile.getId(), newObjectFile.getId());
                        }
                        String dir = "";
                        if (mesObjectFile != null && mesObjectFile.getParentFile() != null) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                        } else {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                        }
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }
                        String path = dir + File.separator + newObjectFile.getId();
                        fileSystemService.saveDocumentToDisk(file, path);

                        objectFileDto.setObjectFile(convertFileIdToDto(newObjectFile.getObject(), objectType, newObjectFile.getId()));
                        MESObject mesObject = mesObjectRepository.findOne(id);
                        applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFileRenamedEvent("mes", id, mesObjectFile, newObjectFile, "Replace", mesObject.getObjectType()));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
            try {
                for (MultipartFile file : fileMap.values()) {
                    name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                    if (fileExtension != null) {
                        for (String ext : fileExtension) {
                            if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
                        if (plmDocument != null) {
                            if (plmDocument.getParentFile() != null) {
                                PLMDocument folder = plmDocumentRepository.findOne(plmDocument.getParentFile());
                                PLMDocument existFile = plmDocumentRepository.findByNameEqualsIgnoreCaseAndParentFileAndLatestTrue(name, plmDocument.getParentFile());
                                if (existFile != null && !existFile.getId().equals(plmDocument.getId())) {
                                    String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", plmDocument.getName(), folder.getName());
                                    throw new CassiniException(result);
                                }
                            } else {
                                PLMDocument existFile = plmDocumentRepository.findByNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(name);
                                if (existFile != null && !existFile.getId().equals(plmDocument.getId())) {
                                    String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", plmDocument.getName());
                                    throw new CassiniException(result);
                                }
                            }

                            plmDocument.setLatest(false);
                            plmDocument = plmDocumentRepository.save(plmDocument);
                        }

                        PLMDocument newObjectFile = new PLMDocument();
                        newObjectFile.setName(name);
                        if (plmDocument != null && plmDocument.getParentFile() != null) {
                            newObjectFile.setParentFile(plmDocument.getParentFile());
                        }
                        if (plmDocument != null) {
                            newObjectFile.setFileNo(plmDocument.getFileNo());
                            newObjectFile.setRevision(plmDocument.getRevision());
                            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(plmDocument.getLifeCyclePhase().getLifeCycle(), LifeCyclePhaseType.PRELIMINARY);
                            if (lifeCyclePhases.size() > 0) {
                                newObjectFile.setLifeCyclePhase(lifeCyclePhases.get(0));
                            }
                            newObjectFile.setVersion(plmDocument.getVersion() + 1);
                            newObjectFile.setReplaceFileName(plmDocument.getName() + " Replaced to " + name);
                        }
                        newObjectFile.setCreatedBy(login.getPerson().getId());
                        newObjectFile.setModifiedBy(login.getPerson().getId());
                        newObjectFile.setFileType("FILE");
                        newObjectFile.setSize(file.getSize());
                        newObjectFile = plmDocumentRepository.save(newObjectFile);
                        if (newObjectFile.getParentFile() != null) {
                            PLMDocument parent = plmDocumentRepository.findOne(newObjectFile.getParentFile());
                            parent.setModifiedDate(newObjectFile.getModifiedDate());
                            parent = plmDocumentRepository.save(parent);
                        }
                        if (plmDocument != null) {
                            copyFileAttributes(plmDocument.getId(), newObjectFile.getId());
                            documentService.copyReviewers(plmDocument.getId(), newObjectFile.getId());
                        }
                        String dir = "";
                        if (plmDocument != null && plmDocument.getParentFile() != null) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + "documents" + getReplaceDocumentFileSystemPath(fileId);
                        } else {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + "documents";
                        }
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }
                        String path = dir + File.separator + newObjectFile.getId();
                        fileSystemService.saveDocumentToDisk(file, path);

                        objectFileDto.setObjectFile(convertFileIdToDto(0, objectType, newObjectFile.getId()));
                        applicationEventPublisher.publishEvent(new DocumentEvents.DocumentFileRenamedEvent(plmDocument, newObjectFile, "Replace"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            try {
                for (MultipartFile file : fileMap.values()) {
                    name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                    if (fileExtension != null) {
                        for (String ext : fileExtension) {
                            if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        PLMMfrPartInspectionReport plmMfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(fileId);
                        if (plmMfrPartInspectionReport != null) {
                            if (plmMfrPartInspectionReport.getParentFile() != null) {
                                PLMMfrPartInspectionReport folder = mfrPartInspectionReportRepository.findOne(plmMfrPartInspectionReport.getParentFile());
                                PLMMfrPartInspectionReport existFile = mfrPartInspectionReportRepository.findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileAndLatestTrue(id, name, plmMfrPartInspectionReport.getParentFile());
                                if (existFile != null && !existFile.getId().equals(plmMfrPartInspectionReport.getId())) {
                                    String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", plmMfrPartInspectionReport.getName(), folder.getName());
                                    throw new CassiniException(result);
                                }
                            } else {
                                PLMMfrPartInspectionReport existFile = mfrPartInspectionReportRepository.findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(id, name);
                                if (existFile != null && !existFile.getId().equals(plmMfrPartInspectionReport.getId())) {
                                    String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", plmMfrPartInspectionReport.getName());
                                    throw new CassiniException(result);
                                }
                            }

                            plmMfrPartInspectionReport.setLatest(false);
                            plmMfrPartInspectionReport = mfrPartInspectionReportRepository.save(plmMfrPartInspectionReport);
                        }

                        PLMMfrPartInspectionReport newObjectFile = new PLMMfrPartInspectionReport();
                        newObjectFile.setName(name);
                        if (plmMfrPartInspectionReport != null && plmMfrPartInspectionReport.getParentFile() != null) {
                            newObjectFile.setParentFile(plmMfrPartInspectionReport.getParentFile());
                        }
                        if (plmMfrPartInspectionReport != null) {
                            newObjectFile.setFileNo(plmMfrPartInspectionReport.getFileNo());
                            newObjectFile.setRevision(plmMfrPartInspectionReport.getRevision());
                            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(plmMfrPartInspectionReport.getLifeCyclePhase().getLifeCycle(), LifeCyclePhaseType.PRELIMINARY);
                            if (lifeCyclePhases.size() > 0) {
                                newObjectFile.setLifeCyclePhase(lifeCyclePhases.get(0));
                            }
                            newObjectFile.setVersion(plmMfrPartInspectionReport.getVersion() + 1);
                            newObjectFile.setReplaceFileName(plmMfrPartInspectionReport.getName() + " Replaced to " + name);
                        }
                        newObjectFile.setCreatedBy(login.getPerson().getId());
                        newObjectFile.setModifiedBy(login.getPerson().getId());
                        newObjectFile.setFileType("FILE");
                        newObjectFile.setSize(file.getSize());
                        newObjectFile.setManufacturerPart(id);
                        newObjectFile = mfrPartInspectionReportRepository.save(newObjectFile);
                        if (newObjectFile.getParentFile() != null) {
                            PLMMfrPartInspectionReport parent = mfrPartInspectionReportRepository.findOne(newObjectFile.getParentFile());
                            parent.setModifiedDate(newObjectFile.getModifiedDate());
                            parent = mfrPartInspectionReportRepository.save(parent);
                        }
                        if (plmMfrPartInspectionReport != null) {
                            copyFileAttributes(plmMfrPartInspectionReport.getId(), newObjectFile.getId());
                            documentService.copyReviewers(plmMfrPartInspectionReport.getId(), newObjectFile.getId());
                        }
                        String dir = "";
                        if (plmMfrPartInspectionReport != null && plmMfrPartInspectionReport.getParentFile() != null) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                        } else {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                        }
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }
                        String path = dir + File.separator + newObjectFile.getId();
                        fileSystemService.saveDocumentToDisk(file, path);
                        if (login.getExternal() && plmMfrPartInspectionReport != null) {
                            sendReplacedNotification(fileId, plmMfrPartInspectionReport.getName(), newObjectFile, "replace", newObjectFile.getManufacturerPart(), login.getPerson().getFullName(), objectType);
                        }
                        objectFileDto.setObjectFile(convertFileIdToDto(0, objectType, newObjectFile.getId()));
                        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartReportRenamedEvent(id, plmMfrPartInspectionReport, newObjectFile, "Replace"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
            try {
                for (MultipartFile file : fileMap.values()) {
                    name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                    if (fileExtension != null) {
                        for (String ext : fileExtension) {
                            if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(fileId);
                        if (mroObjectFile != null) {
                            if (mroObjectFile.getParentFile() != null) {
                                MROObjectFile folder = mroObjectFileRepository.findOne(mroObjectFile.getParentFile());
                                MROObjectFile existFile = mroObjectFileRepository.findByParentFileAndNameAndLatestTrue(mroObjectFile.getParentFile(), name);
                                if (existFile != null && !existFile.getId().equals(mroObjectFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", mroObjectFile.getName(), folder.getName());
                                    throw new CassiniException(result);
                                }
                            } else {
                                MROObjectFile existFile = mroObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(mroObjectFile.getObject(), name);
                                if (existFile != null && !existFile.getId().equals(mroObjectFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", mroObjectFile.getName());
                                    throw new CassiniException(result);
                                }
                            }

                            mroObjectFile.setLatest(false);
                            mroObjectFile = mroObjectFileRepository.save(mroObjectFile);
                        }

                        MROObjectFile newObjectFile = new MROObjectFile();
                        newObjectFile.setName(name);
                        if (mroObjectFile != null && mroObjectFile.getParentFile() != null) {
                            newObjectFile.setParentFile(mroObjectFile.getParentFile());
                        }
                        if (mroObjectFile != null) {
                            newObjectFile.setFileNo(mroObjectFile.getFileNo());
                            newObjectFile.setVersion(mroObjectFile.getVersion() + 1);
                            newObjectFile.setReplaceFileName(mroObjectFile.getName() + " Replaced to " + name);
                        }
                        newObjectFile.setCreatedBy(login.getPerson().getId());
                        newObjectFile.setModifiedBy(login.getPerson().getId());
                        newObjectFile.setObject(id);
                        newObjectFile.setFileType("FILE");
                        newObjectFile.setSize(file.getSize());
                        newObjectFile = mroObjectFileRepository.save(newObjectFile);
                        if (newObjectFile.getParentFile() != null) {
                            MROObjectFile parent = mroObjectFileRepository.findOne(newObjectFile.getParentFile());
                            parent.setModifiedDate(newObjectFile.getModifiedDate());
                            parent = mroObjectFileRepository.save(parent);
                        }
                        if (mroObjectFile != null) {
                            copyFileAttributes(mroObjectFile.getId(), newObjectFile.getId());
                        }
                        String dir = "";
                        if (mroObjectFile != null && mroObjectFile.getParentFile() != null) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                        } else {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                        }
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }
                        String path = dir + File.separator + newObjectFile.getId();
                        fileSystemService.saveDocumentToDisk(file, path);

                        objectFileDto.setObjectFile(convertFileIdToDto(newObjectFile.getObject(), objectType, newObjectFile.getId()));
                        MROObject mroObject = mroObjectRepository.findOne(id);
                        applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFileRenamedEvent("mro", id, mroObjectFile, newObjectFile, "Replace", mroObject.getObjectType()));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
            try {
                for (MultipartFile file : fileMap.values()) {
                    name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                    if (fileExtension != null) {
                        for (String ext : fileExtension) {
                            if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(fileId);
                        if (pgcObjectFile != null) {
                            if (pgcObjectFile.getParentFile() != null) {
                                PGCObjectFile folder = pgcObjectFileRepository.findOne(pgcObjectFile.getParentFile());
                                PGCObjectFile existFile = pgcObjectFileRepository.findByParentFileAndNameAndLatestTrue(pgcObjectFile.getParentFile(), name);
                                if (existFile != null && !existFile.getId().equals(pgcObjectFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", pgcObjectFile.getName(), folder.getName());
                                    throw new CassiniException(result);
                                }
                            } else {
                                PGCObjectFile existFile = pgcObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(pgcObjectFile.getObject(), name);
                                if (existFile != null && !existFile.getId().equals(pgcObjectFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", pgcObjectFile.getName());
                                    throw new CassiniException(result);
                                }
                            }

                            pgcObjectFile.setLatest(false);
                            pgcObjectFile = pgcObjectFileRepository.save(pgcObjectFile);
                        }

                        PGCObjectFile newObjectFile = new PGCObjectFile();
                        newObjectFile.setName(name);
                        if (pgcObjectFile != null && pgcObjectFile.getParentFile() != null) {
                            newObjectFile.setParentFile(pgcObjectFile.getParentFile());
                        }
                        if (pgcObjectFile != null) {
                            newObjectFile.setFileNo(pgcObjectFile.getFileNo());
                            newObjectFile.setVersion(pgcObjectFile.getVersion() + 1);
                            newObjectFile.setReplaceFileName(pgcObjectFile.getName() + " Replaced to " + name);
                        }
                        newObjectFile.setCreatedBy(login.getPerson().getId());
                        newObjectFile.setModifiedBy(login.getPerson().getId());
                        newObjectFile.setObject(id);
                        newObjectFile.setFileType("FILE");
                        newObjectFile.setSize(file.getSize());
                        newObjectFile = pgcObjectFileRepository.save(newObjectFile);
                        if (newObjectFile.getParentFile() != null) {
                            PGCObjectFile parent = pgcObjectFileRepository.findOne(newObjectFile.getParentFile());
                            parent.setModifiedDate(newObjectFile.getModifiedDate());
                            parent = pgcObjectFileRepository.save(parent);
                        }
                        if (pgcObjectFile != null) {
                            copyFileAttributes(pgcObjectFile.getId(), newObjectFile.getId());
                        }
                        String dir = "";
                        if (pgcObjectFile != null && pgcObjectFile.getParentFile() != null) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                        } else {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                        }
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }
                        String path = dir + File.separator + newObjectFile.getId();
                        fileSystemService.saveDocumentToDisk(file, path);

                        objectFileDto.setObjectFile(convertFileIdToDto(newObjectFile.getObject(), objectType, newObjectFile.getId()));
                        PGCObject pgcObject = pgcObjectRepository.findOne(id);
                        applicationEventPublisher.publishEvent(new SubstanceEvents.SubstanceFileRenamedEvent("pgc", id, pgcObjectFile, newObjectFile, "Replace", pgcObject.getObjectType()));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            try {
                for (MultipartFile file : fileMap.values()) {
                    name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                    if (fileExtension != null) {
                        for (String ext : fileExtension) {
                            if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(fileId);
                        if (supplierAuditFile != null) {
                            if (supplierAuditFile.getParentFile() != null) {
                                PGCObjectFile folder = pgcObjectFileRepository.findOne(supplierAuditFile.getParentFile());
                                PGCObjectFile existFile = pgcObjectFileRepository.findByParentFileAndNameAndLatestTrue(supplierAuditFile.getParentFile(), name);
                                if (existFile != null && !existFile.getId().equals(supplierAuditFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", supplierAuditFile.getName(), folder.getName());
                                    throw new CassiniException(result);
                                }
                            } else {
                                PQMSupplierAuditFile existFile = supplierAuditFileRepository.findBySupplierAuditAndNameAndParentFileIsNullAndLatestTrue(supplierAuditFile.getSupplierAudit(), name);
                                if (existFile != null && !existFile.getId().equals(supplierAuditFile.getId())) {
                                    String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", supplierAuditFile.getName());
                                    throw new CassiniException(result);
                                }
                            }

                            supplierAuditFile.setLatest(false);
                            supplierAuditFile = supplierAuditFileRepository.save(supplierAuditFile);
                        }

                        PQMSupplierAuditFile newObjectFile = new PQMSupplierAuditFile();
                        newObjectFile.setName(name);
                        if (supplierAuditFile != null && supplierAuditFile.getParentFile() != null) {
                            newObjectFile.setParentFile(supplierAuditFile.getParentFile());
                        }
                        if (supplierAuditFile != null) {
                            newObjectFile.setFileNo(supplierAuditFile.getFileNo());
                            newObjectFile.setVersion(supplierAuditFile.getVersion() + 1);
                            newObjectFile.setReplaceFileName(supplierAuditFile.getName() + " Replaced to " + name);
                        }
                        newObjectFile.setCreatedBy(login.getPerson().getId());
                        newObjectFile.setModifiedBy(login.getPerson().getId());
                        newObjectFile.setSupplierAudit(id);
                        newObjectFile.setFileType("FILE");
                        newObjectFile.setSize(file.getSize());
                        newObjectFile = supplierAuditFileRepository.save(newObjectFile);
                        if (newObjectFile.getParentFile() != null) {
                            PQMSupplierAuditFile parent = supplierAuditFileRepository.findOne(newObjectFile.getParentFile());
                            parent.setModifiedDate(newObjectFile.getModifiedDate());
                            parent = supplierAuditFileRepository.save(parent);
                        }
                        if (supplierAuditFile != null) {
                            copyFileAttributes(supplierAuditFile.getId(), newObjectFile.getId());
                        }
                        String dir = "";
                        if (supplierAuditFile != null && supplierAuditFile.getParentFile() != null) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                        } else {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                        }
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }
                        String path = dir + File.separator + newObjectFile.getId();
                        fileSystemService.saveDocumentToDisk(file, path);

                        objectFileDto.setObjectFile(convertFileIdToDto(newObjectFile.getSupplierAudit(), objectType, newObjectFile.getId()));
                        PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(id);
                        applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditFileRenamedEvent(id, supplierAuditFile, newObjectFile, "Replace"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            try {
                for (MultipartFile file : fileMap.values()) {
                    name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                    if (fileExtension != null) {
                        for (String ext : fileExtension) {
                            if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(fileId);
                        if (pqmppapChecklist != null) {
                            if (pqmppapChecklist.getParentFile() != null) {
                                PQMPPAPChecklist folder = ppapChecklistRepository.findOne(pqmppapChecklist.getParentFile());
                                PQMPPAPChecklist existFile = ppapChecklistRepository.findByParentFileAndNameAndLatestTrue(pqmppapChecklist.getParentFile(), name);
                                if (existFile != null && !existFile.getId().equals(pqmppapChecklist.getId())) {
                                    String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", pqmppapChecklist.getName(), folder.getName());
                                    throw new CassiniException(result);
                                }
                            } else {
                                PQMPPAPChecklist existFile = ppapChecklistRepository.findByPpapAndNameAndParentFileIsNullAndLatestTrue(pqmppapChecklist.getPpap(), name);
                                if (existFile != null && !existFile.getId().equals(pqmppapChecklist.getId())) {
                                    String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                                    String result = MessageFormat.format(message + ".", pqmppapChecklist.getName());
                                    throw new CassiniException(result);
                                }
                            }

                            pqmppapChecklist.setLatest(false);
                            pqmppapChecklist = ppapChecklistRepository.save(pqmppapChecklist);
                        }

                        PQMPPAPChecklist newObjectFile = new PQMPPAPChecklist();
                        newObjectFile.setName(name);
                        if (pqmppapChecklist != null && pqmppapChecklist.getParentFile() != null) {
                            newObjectFile.setParentFile(pqmppapChecklist.getParentFile());
                        }
                        if (pqmppapChecklist != null) {
                            newObjectFile.setFileNo(pqmppapChecklist.getFileNo());
                            newObjectFile.setRevision(pqmppapChecklist.getRevision());
                            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(pqmppapChecklist.getLifeCyclePhase().getLifeCycle(), LifeCyclePhaseType.PRELIMINARY);
                            if (lifeCyclePhases.size() > 0) {
                                newObjectFile.setLifeCyclePhase(lifeCyclePhases.get(0));
                            }
                            newObjectFile.setVersion(pqmppapChecklist.getVersion() + 1);
                            newObjectFile.setReplaceFileName(pqmppapChecklist.getName() + " Replaced to " + name);
                        }
                        newObjectFile.setCreatedBy(login.getPerson().getId());
                        newObjectFile.setModifiedBy(login.getPerson().getId());
                        newObjectFile.setPpap(id);
                        newObjectFile.setFileType("FILE");
                        newObjectFile.setSize(pqmppapChecklist.getSize());
                        newObjectFile = ppapChecklistRepository.save(newObjectFile);
                        if (newObjectFile.getParentFile() != null) {
                            PQMPPAPChecklist parent = ppapChecklistRepository.findOne(newObjectFile.getParentFile());
                            parent.setModifiedDate(newObjectFile.getModifiedDate());
                            parent = ppapChecklistRepository.save(parent);
                            Integer rejectedCount = documentReviewerRepository.getRejectedCount(newObjectFile.getParentFile());
                            if (rejectedCount > 0) {
                                List<PLMDocumentReviewer> documentReviewers = documentReviewerRepository.findByDocumentAndStatusOrderByIdDesc(newObjectFile.getParentFile(), DocumentApprovalStatus.REJECTED);
                                documentReviewers.forEach(plmDocumentReviewer -> {
                                    plmDocumentReviewer.setStatus(DocumentApprovalStatus.NONE);
                                });
                                documentReviewerRepository.save(documentReviewers);
                            }
                        }
                        if (pqmppapChecklist != null) {
                            copyFileAttributes(pqmppapChecklist.getId(), newObjectFile.getId());
                            documentService.copyReviewers(pqmppapChecklist.getId(), newObjectFile.getId());
                        }
                        String dir = "";
                        if (pqmppapChecklist != null && pqmppapChecklist.getParentFile() != null) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId, objectType);
                        } else {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                        }
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }
                        String path = dir + File.separator + newObjectFile.getId();
                        fileSystemService.saveDocumentToDisk(file, path);

                        objectFileDto.setObjectFile(convertFileIdToDto(newObjectFile.getPpap(), objectType, newObjectFile.getId()));
                        PQMPPAP ppap = ppapRepository.findOne(id);
                        if (login.getExternal() && pqmppapChecklist != null) {
                            sendReplacedNotification(fileId, pqmppapChecklist.getName(), newObjectFile, "replace", newObjectFile.getPpap(), login.getPerson().getFullName(), objectType);
                        }
                        applicationEventPublisher.publishEvent(new PPAPEvents.PPAPFileRenamedEvent(ppap, pqmppapChecklist, newObjectFile, "Rename"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (objectType.equals(PLMObjectType.ITEM)) {
            PLMItemFile itemFile = itemFileService.replaceItemFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(itemFile.getItem().getId(), objectType, itemFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROJECT)) {
            PLMProjectFile projectFile = projectService.replaceProjectFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(projectFile.getProject(), objectType, projectFile.getId()));
        } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
            PLMManufacturerFile manufacturerFile = manufacturerFileService.replaceMfrFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(manufacturerFile.getManufacturer(), objectType, manufacturerFile.getId()));
        } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
            PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileService.replaceMfrPartFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(manufacturerPartFile.getManufacturerPart(), objectType, manufacturerPartFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            PLMActivityFile activityFile = activityService.replaceActivityFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(activityFile.getActivity(), objectType, activityFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            PLMTaskFile taskFile = activityService.replaceTaskFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(taskFile.getTask(), objectType, taskFile.getId()));
        } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
            PLMGlossaryFile glossaryFile = glossaryService.replaceGlossaryFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(glossaryFile.getGlossary(), objectType, glossaryFile.getId()));
        } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
            PLMSupplierFile supplierFile = supplierFileService.replaceSupplierFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(supplierFile.getSupplier(), objectType, supplierFile.getId()));
        } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
            PQMCustomerFile customerFile = customerFileService.replaceCustomerFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(customerFile.getCustomer(), objectType, customerFile.getId()));
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
            PLMRequirementDocumentFile documentFile = reqDocumentFileService.replaceReqDocumentFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(documentFile.getDocumentRevision().getId(), objectType, documentFile.getId()));
        } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
            PLMRequirementFile requirementFile = requirementFileService.replaceRequirementFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(requirementFile.getRequirement().getId(), objectType, requirementFile.getId()));
        } else if (objectType.equals(PLMObjectType.PLMNPR)) {
            PLMNprFile nprFile = nprFileService.replaceNprFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(nprFile.getNpr(), objectType, nprFile.getId()));
        } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
            MESMBOMFile mesmbomFile = mbomFileService.replaceNprFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(mesmbomFile.getMbomRevision(), objectType, mesmbomFile.getId()));
        } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
            MESMBOMInstanceFile mesmbomFile = mbomInstanceFileService.replaceMBOMInstanceFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(mesmbomFile.getMbomInstance(), objectType, mesmbomFile.getId()));
        } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
            MESBOPFile mesbopFile = bopFileService.replaceNprFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(mesbopFile.getBop(), objectType, mesbopFile.getId()));
        } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
            MESBOPOperationFile mesbopFile = bopPlanFileService.replaceBOPPlanFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(mesbopFile.getBopOperation(), objectType, mesbopFile.getId()));
        } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
            MESBOPInstanceOperationFile mesbopFile = bopInstanceOperationFileService.replaceBOPPlanFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(mesbopFile.getBopOperation(), objectType, mesbopFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            PLMProgramFile programFile = programFileService.replaceNprFiles(id, fileId, fileMap).get(0);
            objectFileDto.setObjectFile(convertFileIdToDto(programFile.getProgram(), objectType, programFile.getId()));
        } else if (objectType.equals(PLMObjectType.CHANGE)) {
            PLMChangeFile changeFile = changeFileService.replaceChangeFile(id, fileId, fileMap);
            objectFileDto.setObjectFile(convertFileIdToDto(changeFile.getChange(), objectType, changeFile.getId()));
        }

        return objectFileDto;
    }

    @Transactional(readOnly = true)
    public ObjectFileDto getLatestUploadedFile(Integer id, PLMObjectType objectType, Integer fileId) {
        ObjectFileDto objectFileDto = new ObjectFileDto();
        if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
            PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(fileId);
            PQMInspectionPlanFile latestFile = inspectionPlanFileRepository.findByInspectionPlanAndFileNoAndLatestTrue(inspectionPlanFile.getInspectionPlan(), inspectionPlanFile.getFileNo());
            objectFileDto.setObjectFile(convertFileIdToDto(latestFile.getInspectionPlan(), objectType, latestFile.getId()));
        } else if (objectType.equals(PLMObjectType.INSPECTION)) {
            PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(fileId);
            PQMInspectionFile latestFile = inspectionFileRepository.findByInspectionAndFileNoAndLatestTrue(inspectionFile.getInspection(), inspectionFile.getFileNo());
            objectFileDto.setObjectFile(convertFileIdToDto(latestFile.getInspection(), objectType, latestFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(fileId);
            PQMProblemReportFile latestFile = problemReportFileRepository.findByProblemReportAndFileNoAndLatestTrue(problemReportFile.getProblemReport(), problemReportFile.getFileNo());
            objectFileDto.setObjectFile(convertFileIdToDto(latestFile.getProblemReport(), objectType, latestFile.getId()));
        } else if (objectType.equals(PLMObjectType.NCR)) {
            PQMNCRFile ncrFile = ncrFileRepository.findOne(fileId);
            PQMNCRFile latestFile = ncrFileRepository.findByNcrAndFileNoAndLatestTrue(ncrFile.getNcr(), ncrFile.getFileNo());
            objectFileDto.setObjectFile(convertFileIdToDto(latestFile.getNcr(), objectType, latestFile.getId()));
        } else if (objectType.equals(PLMObjectType.QCR)) {
            PQMQCRFile qcrFile = qcrFileRepository.findOne(fileId);
            PQMQCRFile latestFile = qcrFileRepository.findByQcrAndFileNoAndLatestTrue(qcrFile.getQcr(), qcrFile.getFileNo());
            objectFileDto.setObjectFile(convertFileIdToDto(latestFile.getQcr(), objectType, latestFile.getId()));
        } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
            MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(fileId);
            MESObjectFile latestFile = mesObjectFileRepository.findByObjectAndFileNoAndLatestTrue(mesObjectFile.getObject(), mesObjectFile.getFileNo());
            objectFileDto.setObjectFile(convertFileIdToDto(latestFile.getObject(), objectType, latestFile.getId()));
        } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
            PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
            PLMDocument latestFile = plmDocumentRepository.findByFileNoAndLatestTrue(plmDocument.getFileNo());
            objectFileDto.setObjectFile(convertFileIdToDto(0, objectType, latestFile.getId()));
        } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            PLMMfrPartInspectionReport plmDocument = mfrPartInspectionReportRepository.findOne(fileId);
            PLMMfrPartInspectionReport latestFile = mfrPartInspectionReportRepository.findByFileNoAndLatestTrue(plmDocument.getFileNo());
            objectFileDto.setObjectFile(convertFileIdToDto(0, objectType, latestFile.getId()));
        } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
            MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(fileId);
            MROObjectFile latestFile = mroObjectFileRepository.findByObjectAndFileNoAndLatestTrue(mroObjectFile.getObject(), mroObjectFile.getFileNo());
            objectFileDto.setObjectFile(convertFileIdToDto(latestFile.getObject(), objectType, latestFile.getId()));
        } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
            PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(fileId);
            PGCObjectFile latestFile = pgcObjectFileRepository.findByObjectAndFileNoAndLatestTrue(pgcObjectFile.getObject(), pgcObjectFile.getFileNo());
            objectFileDto.setObjectFile(convertFileIdToDto(latestFile.getObject(), objectType, latestFile.getId()));
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(fileId);
            PQMSupplierAuditFile latestFile = supplierAuditFileRepository.findBySupplierAuditAndFileNoAndLatestTrue(supplierAuditFile.getSupplierAudit(), supplierAuditFile.getFileNo());
            objectFileDto.setObjectFile(convertFileIdToDto(latestFile.getSupplierAudit(), objectType, latestFile.getId()));
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(fileId);
            PQMPPAPChecklist latestFile = ppapChecklistRepository.findByPpapAndFileNoAndLatestTrue(pqmppapChecklist.getPpap(), pqmppapChecklist.getFileNo());
            objectFileDto.setObjectFile(convertFileIdToDto(latestFile.getPpap(), objectType, latestFile.getId()));
        } else if (objectType.equals(PLMObjectType.ITEM)) {
            PLMItemFile itemFile = itemFileService.getLatestUploadedFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(itemFile.getItem().getId(), objectType, itemFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROJECT)) {
            PLMProjectFile projectFile = projectService.getLatestUploadedProjectFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(projectFile.getProject(), objectType, projectFile.getId()));
        } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
            PLMManufacturerFile manufacturerFile = manufacturerFileService.getLatestUploadedMfrFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(manufacturerFile.getManufacturer(), objectType, manufacturerFile.getId()));
        } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
            PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileService.getLatestUploadedMfrPartFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(manufacturerPartFile.getManufacturerPart(), objectType, manufacturerPartFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            PLMActivityFile activityFile = activityService.getLatestUploadedFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(activityFile.getActivity(), objectType, activityFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            PLMTaskFile taskFile = activityService.getLatestUploadedTaskFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(taskFile.getTask(), objectType, taskFile.getId()));
        } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
            PLMGlossaryFile glossaryFile = glossaryService.getLatestUploadedFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(glossaryFile.getGlossary(), objectType, glossaryFile.getId()));
        } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
            PLMSupplierFile supplierFile = supplierFileService.getLatestUploadedSupplierFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(supplierFile.getSupplier(), objectType, supplierFile.getId()));
        } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
            PQMCustomerFile customerFile = customerFileService.getLatestUploadedCustomerFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(customerFile.getCustomer(), objectType, customerFile.getId()));
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
            PLMRequirementDocumentFile documentFile = reqDocumentFileService.getLatestUploadedReqDocumentFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(documentFile.getDocumentRevision().getId(), objectType, documentFile.getId()));
        } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
            PLMRequirementFile requirementFile = requirementFileService.getLatestUploadedRequirementFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(requirementFile.getRequirement().getId(), objectType, requirementFile.getId()));
        } else if (objectType.equals(PLMObjectType.PLMNPR)) {
            PLMNprFile nprFile = nprFileService.getLatestUploadedNprFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(nprFile.getNpr(), objectType, nprFile.getId()));
        } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
            MESMBOMFile mesmbomFile = mbomFileService.getLatestUploadedNprFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(mesmbomFile.getMbomRevision(), objectType, mesmbomFile.getId()));
        } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
            MESMBOMInstanceFile mesmbomFile = mbomInstanceFileService.getLatestUploadedMBOMInstanceFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(mesmbomFile.getMbomInstance(), objectType, mesmbomFile.getId()));
        } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
            MESBOPFile mesbopFile = bopFileService.getLatestUploadedNprFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(mesbopFile.getBop(), objectType, mesbopFile.getId()));
        } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
            MESBOPOperationFile mesbopFile = bopPlanFileService.getLatestUploadedNprFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(mesbopFile.getBopOperation(), objectType, mesbopFile.getId()));
        } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
            MESBOPInstanceOperationFile mesbopFile = bopInstanceOperationFileService.getLatestUploadedNprFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(mesbopFile.getBopOperation(), objectType, mesbopFile.getId()));
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            PLMProgramFile programFile = programFileService.getLatestUploadedNprFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(programFile.getProgram(), objectType, programFile.getId()));
        } else if (objectType.equals(PLMObjectType.CHANGE)) {
            PLMChangeFile changeFile = changeFileService.getLatestUploadedFile(id, fileId);
            objectFileDto.setObjectFile(convertFileIdToDto(changeFile.getChange(), objectType, changeFile.getId()));
        }
        return objectFileDto;
    }

    @Transactional
    @PreAuthorize("hasPermission(#fileId,'delete') || @documentService.checkDMPemrmissions(authentication, 'delete', #objectType.name(), #fileId)")
    public void deleteObjectFile(Integer id, PLMObjectType objectType, Integer fileId) throws JsonProcessingException {
        String fileName = "";
        if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
            PQMInspectionPlanFile plmItemFile = inspectionPlanFileRepository.findOne(fileId);

            List<PQMInspectionPlanFile> inspectionPlanFileList = inspectionPlanFileRepository.findByInspectionPlanAndFileNo(id, plmItemFile.getFileNo());

            for (PQMInspectionPlanFile inspectionPlanFile : inspectionPlanFileList) {
                if (inspectionPlanFile == null) {
                    throw new ResourceNotFoundException();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(id, inspectionPlanFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDisk(inspectionPlanFile.getId(), dir);
                inspectionPlanFileRepository.delete(inspectionPlanFile.getId());
            }
            if (plmItemFile.getParentFile() != null) {
                PQMInspectionPlanFile parent = inspectionPlanFileRepository.findOne(plmItemFile.getParentFile());
                parent.setModifiedDate(new Date());
                parent = inspectionPlanFileRepository.save(parent);
            }
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(id);
            applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanFileDeletedEvent(inspectionPlanRevision, plmItemFile));
        } else if (objectType.equals(PLMObjectType.INSPECTION)) {
            PQMInspectionFile plmItemFile = inspectionFileRepository.findOne(fileId);
            List<PQMInspectionFile> inspectionFileList = inspectionFileRepository.findByInspectionAndFileNo(id, plmItemFile.getFileNo());
            PQMInspection inspection = inspectionRepository.findOne(plmItemFile.getInspection());
            for (PQMInspectionFile inspectionPlanFile : inspectionFileList) {
                if (inspectionPlanFile == null) {
                    throw new ResourceNotFoundException();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(id, inspectionPlanFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDisk(inspectionPlanFile.getId(), dir);
                inspectionFileRepository.delete(inspectionPlanFile.getId());
            }
            if (plmItemFile.getParentFile() != null) {
                PQMInspectionFile parent = inspectionFileRepository.findOne(plmItemFile.getParentFile());
                parent.setModifiedDate(new Date());
                parent = inspectionFileRepository.save(parent);
            }
            applicationEventPublisher.publishEvent(new InspectionEvents.InspectionFileDeletedEvent(inspection, plmItemFile));
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(fileId);
            List<PQMProblemReportFile> problemReportFileList = problemReportFileRepository.findByProblemReportAndFileNo(id, problemReportFile.getFileNo());
            PQMProblemReport problemReport = problemReportRepository.findOne(problemReportFile.getProblemReport());
            for (PQMProblemReportFile pqmProblemReportFile : problemReportFileList) {
                if (pqmProblemReportFile == null) {
                    throw new ResourceNotFoundException();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(id, pqmProblemReportFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDisk(pqmProblemReportFile.getId(), dir);
                problemReportFileRepository.delete(pqmProblemReportFile.getId());
            }
            if (problemReportFile.getParentFile() != null) {
                PQMProblemReportFile parent = problemReportFileRepository.findOne(problemReportFile.getParentFile());
                parent.setModifiedDate(new Date());
                parent = problemReportFileRepository.save(parent);
            }
            applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportFileDeletedEvent(problemReport, problemReportFile));
        } else if (objectType.equals(PLMObjectType.NCR)) {
            PQMNCRFile pqmncrFile = ncrFileRepository.findOne(fileId);
            List<PQMNCRFile> pqmncrFileList = ncrFileRepository.findByNcrAndFileNo(id, pqmncrFile.getFileNo());
            PQMNCR pqmncr = ncrRepository.findOne(pqmncrFile.getNcr());
            for (PQMNCRFile ncrFile : pqmncrFileList) {
                if (ncrFile == null) {
                    throw new ResourceNotFoundException();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(id, ncrFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDisk(ncrFile.getId(), dir);
                ncrFileRepository.delete(ncrFile.getId());
            }
            if (pqmncrFile.getParentFile() != null) {
                PQMNCRFile parent = ncrFileRepository.findOne(pqmncrFile.getParentFile());
                parent.setModifiedDate(new Date());
                parent = ncrFileRepository.save(parent);
            }
            applicationEventPublisher.publishEvent(new NCREvents.NCRFileDeletedEvent(pqmncr, pqmncrFile));
        } else if (objectType.equals(PLMObjectType.QCR)) {
            PQMQCRFile pqmqcrFile = qcrFileRepository.findOne(fileId);
            List<PQMQCRFile> pqmqcrFileList = qcrFileRepository.findByQcrAndFileNo(id, pqmqcrFile.getFileNo());
            PQMQCR pqmqcr = qcrRepository.findOne(pqmqcrFile.getQcr());
            for (PQMQCRFile qcrFile : pqmqcrFileList) {
                if (qcrFile == null) {
                    throw new ResourceNotFoundException();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(id, qcrFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDisk(qcrFile.getId(), dir);
                qcrFileRepository.delete(qcrFile.getId());
            }
            if (pqmqcrFile.getParentFile() != null) {
                PQMQCRFile parent = qcrFileRepository.findOne(pqmqcrFile.getParentFile());
                parent.setModifiedDate(new Date());
                parent = qcrFileRepository.save(parent);
            }
            applicationEventPublisher.publishEvent(new QCREvents.QCRFileDeletedEvent(pqmqcr, pqmqcrFile));
        } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
            MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(fileId);
            List<MESObjectFile> pqmObjectFileList = mesObjectFileRepository.findByObjectAndFileNo(id, mesObjectFile.getFileNo());
            MESObject mesObject = mesObjectRepository.findOne(mesObjectFile.getObject());
            for (MESObjectFile objectFile : pqmObjectFileList) {
                if (objectFile == null) {
                    throw new ResourceNotFoundException();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(id, objectFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDisk(objectFile.getId(), dir);
                mesObjectFileRepository.delete(objectFile.getId());
            }
            if (mesObjectFile.getParentFile() != null) {
                MESObjectFile parent = mesObjectFileRepository.findOne(mesObjectFile.getParentFile());
                parent.setModifiedDate(new Date());
                parent = mesObjectFileRepository.save(parent);
            }
            applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFileDeletedEvent("mes", id, mesObjectFile, mesObject.getObjectType()));
        } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
            PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
            List<Integer> oldVersionFileIds = plmDocumentRepository.getFileIdsByFileNoAndLatestFalse(plmDocument.getFileNo());
            Integer objectDocuments = objectDocumentRepository.getObjectDocumentCountByDocument(fileId);
            if (objectDocuments > 0) {
                String message = messageSource.getMessage("document_already_in_use", null, "{0} file already in use", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmDocument.getName());
                throw new CassiniException(result);
            }
            if (oldVersionFileIds.size() > 0) {
                Integer oldVersionDocuments = objectDocumentRepository.getObjectDocumentCountByDocumentIds(oldVersionFileIds);
                if (oldVersionDocuments > 0) {
                    String message = messageSource.getMessage("old_document_already_in_use", null, "{0} previous file versions already in use", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", plmDocument.getName());
                    throw new CassiniException(result);
                }
            }
            List<PLMDocument> pqmObjectFileList = plmDocumentRepository.findByFileNo(plmDocument.getFileNo());
            for (PLMDocument objectFile : pqmObjectFileList) {
                if (objectFile == null) {
                    throw new ResourceNotFoundException();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + "documents" + getReplaceDocumentFileSystemPath(objectFile.getId());
                fileSystemService.deleteDocumentFromDisk(objectFile.getId(), dir);
                plmDocumentRepository.delete(objectFile.getId());
            }
            if (plmDocument.getParentFile() != null) {
                PLMDocument parent = plmDocumentRepository.findOne(plmDocument.getParentFile());
                parent.setModifiedDate(new Date());
                parent = plmDocumentRepository.save(parent);
            }
            applicationEventPublisher.publishEvent(new DocumentEvents.DocumentFileDeletedEvent(plmDocument));
        } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            PLMMfrPartInspectionReport plmMfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(fileId);
            List<Integer> oldVersionFileIds = mfrPartInspectionReportRepository.getFileIdsByFileNoAndLatestFalse(plmMfrPartInspectionReport.getFileNo());
            if (oldVersionFileIds.size() > 0) {
                Integer oldVersionDocuments = objectDocumentRepository.getObjectDocumentCountByDocumentIds(oldVersionFileIds);
                if (oldVersionDocuments > 0) {
                    String message = messageSource.getMessage("old_document_already_in_use", null, "{0} previous file versions already in use", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", plmMfrPartInspectionReport.getName());
                    throw new CassiniException(result);
                }
            }
            List<PLMMfrPartInspectionReport> pqmObjectFileList = mfrPartInspectionReportRepository.findByFileNo(plmMfrPartInspectionReport.getFileNo());
            for (PLMMfrPartInspectionReport objectFile : pqmObjectFileList) {
                if (objectFile == null) {
                    throw new ResourceNotFoundException();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(id, objectFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDisk(objectFile.getId(), dir);
                mfrPartInspectionReportRepository.delete(objectFile.getId());
            }
            if (plmMfrPartInspectionReport.getParentFile() != null) {
                PLMMfrPartInspectionReport parent = mfrPartInspectionReportRepository.findOne(plmMfrPartInspectionReport.getParentFile());
                parent.setModifiedDate(new Date());
                parent = mfrPartInspectionReportRepository.save(parent);
            }
            applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartReportDeletedEvent(id, plmMfrPartInspectionReport));
        } else if (objectType.equals(PLMObjectType.OBJECTDOCUMENT)) {
            PLMObjectDocument plmObjectDocument = objectDocumentRepository.findOne(fileId);
            List<PLMDocument> pqmObjectFileList = plmDocumentRepository.findByFileNo(plmObjectDocument.getDocument().getFileNo());
            for (PLMDocument objectFile : pqmObjectFileList) {
                if (objectFile == null) {
                    throw new ResourceNotFoundException();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + "documents" + getReplaceDocumentFileSystemPath(objectFile.getId());
                fileSystemService.deleteDocumentFromDisk(objectFile.getId(), dir);
                plmDocumentRepository.delete(objectFile.getId());
            }
            if (plmObjectDocument.getDocument().getParentFile() != null) {
                PLMDocument parent = plmDocumentRepository.findOne(plmObjectDocument.getDocument().getParentFile());
                parent.setModifiedDate(new Date());
                parent = plmDocumentRepository.save(parent);
            }
            objectDocumentRepository.delete(plmObjectDocument.getId());
        } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
            MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(fileId);
            List<MROObjectFile> pqmObjectFileList = mroObjectFileRepository.findByObjectAndFileNo(id, mroObjectFile.getFileNo());
            MROObject mroObject = mroObjectRepository.findOne(mroObjectFile.getObject());
            for (MROObjectFile objectFile : pqmObjectFileList) {
                if (objectFile == null) {
                    throw new ResourceNotFoundException();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(id, objectFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDisk(objectFile.getId(), dir);
                mroObjectFileRepository.delete(objectFile.getId());
            }
            if (mroObjectFile.getParentFile() != null) {
                MROObjectFile parent = mroObjectFileRepository.findOne(mroObjectFile.getParentFile());
                parent.setModifiedDate(new Date());
                parent = mroObjectFileRepository.save(parent);
            }
            applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFileDeletedEvent("mro", id, mroObjectFile, mroObject.getObjectType()));
        } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
            PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(fileId);
            List<PGCObjectFile> pqmObjectFileList = pgcObjectFileRepository.findByObjectAndFileNo(id, pgcObjectFile.getFileNo());
            PGCObject pgcObject = pgcObjectRepository.findOne(pgcObjectFile.getObject());
            for (PGCObjectFile objectFile : pqmObjectFileList) {
                if (objectFile == null) {
                    throw new ResourceNotFoundException();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(id, objectFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDisk(objectFile.getId(), dir);
                pgcObjectFileRepository.delete(objectFile.getId());
            }
            if (pgcObjectFile.getParentFile() != null) {
                PGCObjectFile parent = pgcObjectFileRepository.findOne(pgcObjectFile.getParentFile());
                parent.setModifiedDate(new Date());
                parent = pgcObjectFileRepository.save(parent);
            }
            applicationEventPublisher.publishEvent(new SubstanceEvents.SubstanceFileDeletedEvent("pgc", id, pgcObjectFile, pgcObject.getObjectType()));
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(fileId);
            List<PQMSupplierAuditFile> pqmObjectFileList = supplierAuditFileRepository.findBySupplierAuditAndFileNo(id, supplierAuditFile.getFileNo());
            PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(supplierAuditFile.getSupplierAudit());
            for (PQMSupplierAuditFile objectFile : pqmObjectFileList) {
                if (objectFile == null) {
                    throw new ResourceNotFoundException();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(id, objectFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDisk(objectFile.getId(), dir);
                supplierAuditFileRepository.delete(objectFile.getId());
            }
            if (supplierAuditFile.getParentFile() != null) {
                PQMSupplierAuditFile parent = supplierAuditFileRepository.findOne(supplierAuditFile.getParentFile());
                parent.setModifiedDate(new Date());
                parent = supplierAuditFileRepository.save(parent);
            }
            applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditFileDeletedEvent(id, supplierAuditFile));
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(fileId);
            List<PQMPPAPChecklist> pqmObjectFileList = ppapChecklistRepository.findByPpapAndFileNo(id, pqmppapChecklist.getFileNo());
            PQMPPAP ppap = ppapRepository.findOne(pqmppapChecklist.getPpap());
            for (PQMPPAPChecklist objectFile : pqmObjectFileList) {
                if (objectFile == null) {
                    throw new ResourceNotFoundException();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(id, objectFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDisk(objectFile.getId(), dir);
                ppapChecklistRepository.delete(objectFile.getId());
            }
            if (pqmppapChecklist.getParentFile() != null) {
                PQMPPAPChecklist parent = ppapChecklistRepository.findOne(pqmppapChecklist.getParentFile());
                parent.setModifiedDate(new Date());
                parent = ppapChecklistRepository.save(parent);
            }
            Integer totalFiles = ppapChecklistRepository.getTotalChecklistCount(pqmppapChecklist.getPpap());
            totalFiles = totalFiles + objectDocumentRepository.getDocumentsCountByObjectId(pqmppapChecklist.getPpap());
            Integer phaseFiles = ppapChecklistRepository.getChecklistCountByPhase(pqmppapChecklist.getPpap(), LifeCyclePhaseType.RELEASED);
            phaseFiles = phaseFiles + objectDocumentRepository.getReleasedDocumentsByObjectAndStatus(pqmppapChecklist.getPpap(), LifeCyclePhaseType.RELEASED);
            if (totalFiles > 0 && totalFiles.equals(phaseFiles)) {
                ppap = ppapRepository.findOne(pqmppapChecklist.getPpap());
                List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(ppap.getStatus().getLifeCycle(), LifeCyclePhaseType.RELEASED);
                if (lifeCyclePhases.size() > 0) {
                    ppap.setStatus(lifeCyclePhases.get(lifeCyclePhases.size() - 1));
                    ppap = ppapRepository.save(ppap);
                    if (ppap.getStatus().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                        documentService.sendPpapApprovedNotification(ppap);
                    }
                }
            }
            applicationEventPublisher.publishEvent(new PPAPEvents.PPAPFileDeletedEvent(ppap, pqmppapChecklist));
        } else if (objectType.equals(PLMObjectType.ITEM)) {
            itemFileService.deleteItemFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.PROJECT)) {
            projectService.deleteProjectFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
            manufacturerFileService.deleteMfrFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
            manufacturerPartFileService.deletePartFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            activityService.deleteActivityFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            activityService.deleteTaskFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
            glossaryService.deleteGlossaryFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
            supplierFileService.deleteSupplierFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
            customerFileService.deleteCustomerFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
            reqDocumentFileService.deleteReqDocumentFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
            requirementFileService.deleteRequirementFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.PLMNPR)) {
            nprFileService.deleteNprFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
            mbomFileService.deleteNprFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
            mbomInstanceFileService.deleteMBOMInstanceFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
            bopFileService.deleteNprFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
            bopPlanFileService.deleteBOPPlanFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
            bopInstanceOperationFileService.deleteBOPPlanFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            programFileService.deleteNprFile(id, fileId);
        } else if (objectType.equals(PLMObjectType.CHANGE)) {
            changeFileService.deleteChangeFile(id, fileId);
        }
    }

    private String getFolderName(String folderName, Integer folderId, PLMObjectType objectType) {
        PLMFile folder = getFileObject(folderId, objectType);
        if (folder.getParentFile() != null) {
            PLMFile parentFolder = getFileObject(folder.getParentFile(), objectType);
            if (folderName == null || folderName.equals("")) {
                folderName = folder.getName();
            } else {
                folderName = folder.getName() + " / " + folderName;
            }
            folderName = getFolderName(folderName, parentFolder.getId(), objectType);
        } else {
            if (folderName == null || folderName.equals("")) {
                folderName = folder.getName();
            } else {
                folderName = folder.getName() + " / " + folderName;
            }
        }

        return folderName;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','folder')")
    public void deleteFolder(Integer id, PLMObjectType objectType, Integer folderId) throws JsonProcessingException {
        if (objectType.equals(PLMObjectType.INSPECTIONPLAN) || objectType.equals(PLMObjectType.INSPECTION) || objectType.equals(PLMObjectType.PROBLEMREPORT)
                || objectType.equals(PLMObjectType.NCR) || objectType.equals(PLMObjectType.QCR) || objectType.equals(PLMObjectType.MESOBJECT) || objectType.equals(PLMObjectType.DOCUMENT)
                || objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT) || objectType.equals(PLMObjectType.MROOBJECT) || objectType.equals(PLMObjectType.PGCOBJECT)
                || objectType.equals(PLMObjectType.PPAPCHECKLIST) || objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            String dir = "";
            if (objectType.equals(PLMObjectType.DOCUMENT)) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + "documents" + getDocumentParentFileSystemPath(folderId);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, folderId, objectType);
            }
            if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
                PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(id);
                PLMFile file = fileRepository.findOne(folderId);
                List<PQMInspectionPlanFile> inspectionPlanFileList = inspectionPlanFileRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
                utilService.removeFileIfExist((List) inspectionPlanFileList, dir);
                inspectionPlanFileRepository.delete(folderId);
                if (file.getParentFile() != null) {
                    PQMInspectionPlanFile parent = inspectionPlanFileRepository.findOne(file.getParentFile());
                    parent.setModifiedDate(new Date());
                    parent = inspectionPlanFileRepository.save(parent);
                }
                applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanFoldersDeletedEvent(inspectionPlanRevision, file));
            } else if (objectType.equals(PLMObjectType.INSPECTION)) {
                List<PQMInspectionFile> inspectionFileList = inspectionFileRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
                utilService.removeFileIfExist((List) inspectionFileList, dir);
                PQMInspection inspection = inspectionRepository.findOne(id);
                PLMFile file = fileRepository.findOne(folderId);
                if (file.getParentFile() != null) {
                    PLMFile parent = fileRepository.findOne(file.getParentFile());
                    parent.setModifiedDate(new Date());
                    parent = fileRepository.save(parent);
                }
                inspectionFileRepository.delete(folderId);
                applicationEventPublisher.publishEvent(new InspectionEvents.InspectionFoldersDeletedEvent(inspection, file));
            } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
                List<PQMProblemReportFile> problemReportFileList = problemReportFileRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
                utilService.removeFileIfExist((List) problemReportFileList, dir);
                PQMProblemReport problemReport = problemReportRepository.findOne(id);
                PLMFile file = fileRepository.findOne(folderId);
                problemReportFileRepository.delete(folderId);
                if (file.getParentFile() != null) {
                    PLMFile parent = fileRepository.findOne(file.getParentFile());
                    parent.setModifiedDate(new Date());
                    parent = fileRepository.save(parent);
                }
                applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportFoldersDeletedEvent(problemReport, file));
            } else if (objectType.equals(PLMObjectType.NCR)) {
                List<PQMNCRFile> pqmncrFileList = ncrFileRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
                utilService.removeFileIfExist((List) pqmncrFileList, dir);
                PQMNCR pqmncr = ncrRepository.findOne(id);
                PLMFile file = fileRepository.findOne(folderId);
                ncrFileRepository.delete(folderId);
                if (file.getParentFile() != null) {
                    PLMFile parent = fileRepository.findOne(file.getParentFile());
                    parent.setModifiedDate(new Date());
                    parent = fileRepository.save(parent);
                }
                applicationEventPublisher.publishEvent(new NCREvents.NCRFoldersDeletedEvent(pqmncr, file));
            } else if (objectType.equals(PLMObjectType.QCR)) {
                List<PQMQCRFile> pqmqcrFileList = qcrFileRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
                utilService.removeFileIfExist((List) pqmqcrFileList, dir);
                PQMQCR pqmqcr = qcrRepository.findOne(id);
                PLMFile file = fileRepository.findOne(folderId);
                qcrFileRepository.delete(folderId);
                if (file.getParentFile() != null) {
                    PLMFile parent = fileRepository.findOne(file.getParentFile());
                    parent.setModifiedDate(new Date());
                    parent = fileRepository.save(parent);
                }
                applicationEventPublisher.publishEvent(new QCREvents.QCRFoldersDeletedEvent(pqmqcr, file));
            } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
                List<MESObjectFile> mesObjectFiles = mesObjectFileRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
                utilService.removeFileIfExist((List) mesObjectFiles, dir);
                PLMFile file = fileRepository.findOne(folderId);
                mesObjectFileRepository.delete(folderId);
                if (file.getParentFile() != null) {
                    PLMFile parent = fileRepository.findOne(file.getParentFile());
                    parent.setModifiedDate(new Date());
                    parent = fileRepository.save(parent);
                }
                MESObject mesObject = mesObjectRepository.findOne(id);
                applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFoldersDeletedEvent("mes", id, file, mesObject.getObjectType()));
            } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
                List<PLMDocument> mesObjectFiles = plmDocumentRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
                utilService.removeFileIfExist((List) mesObjectFiles, dir);
                PLMDocument file = plmDocumentRepository.findOne(folderId);
                plmDocumentRepository.delete(folderId);
                if (file.getParentFile() != null) {
                    PLMFile parent = fileRepository.findOne(file.getParentFile());
                    parent.setModifiedDate(new Date());
                    parent = fileRepository.save(parent);
                }
                applicationEventPublisher.publishEvent(new DocumentEvents.DocumentFoldersDeletedEvent(file));
            } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                List<PLMMfrPartInspectionReport> mfrPartInspectionReports = mfrPartInspectionReportRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
                Integer count = mfrPartInspectionReportRepository.getReleasedReportCountByParentFile(folderId);
                PLMFile plmFile = fileRepository.findOne(folderId);
                if (count > 0) {
                    String message = messageSource.getMessage("some_of_the_reports_already_in_released", null, "{0} inspection reports are in released state. You cannot delete this folder", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", plmFile.getName());
                    throw new CassiniException(result);
                }
                mfrPartInspectionReports.forEach(plmMfrPartInspectionReport -> {
                    if (plmMfrPartInspectionReport.getFileType().equals("FOLDER")) {
                        Integer approvedCount = mfrPartInspectionReportRepository.getReleasedReportCountByParentFile(plmMfrPartInspectionReport.getId());
                        if (approvedCount > 0) {
                            String message = messageSource.getMessage("some_of_the_reports_already_in_released", null, "{0} inspection reports are in released state. You cannot delete this folder", LocaleContextHolder.getLocale());
                            String result = MessageFormat.format(message + ".", plmFile.getName() + "/" + plmMfrPartInspectionReport.getName());
                            throw new CassiniException(result);
                        }
                    }
                });
                utilService.removeFileIfExist((List) mfrPartInspectionReports, dir);
                PLMMfrPartInspectionReport file = mfrPartInspectionReportRepository.findOne(folderId);
                mfrPartInspectionReportRepository.delete(folderId);
                if (file.getParentFile() != null) {
                    PLMFile parent = fileRepository.findOne(file.getParentFile());
                    parent.setModifiedDate(new Date());
                    parent = fileRepository.save(parent);
                }
                applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartReportFoldersDeletedEvent(id, file));
            } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
                List<MROObjectFile> mroObjectFiles = mroObjectFileRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
                utilService.removeFileIfExist((List) mroObjectFiles, dir);
                PLMFile file = fileRepository.findOne(folderId);
                mroObjectFileRepository.delete(folderId);
                if (file.getParentFile() != null) {
                    PLMFile parent = fileRepository.findOne(file.getParentFile());
                    parent.setModifiedDate(new Date());
                    parent = fileRepository.save(parent);
                }
                MROObject mroObject = mroObjectRepository.findOne(id);
                applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFoldersDeletedEvent("mro", id, file, mroObject.getObjectType()));
            } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
                List<PGCObjectFile> pgcObjectFiles = pgcObjectFileRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
                utilService.removeFileIfExist((List) pgcObjectFiles, dir);
                PLMFile file = fileRepository.findOne(folderId);
                pgcObjectFileRepository.delete(folderId);
                if (file.getParentFile() != null) {
                    PLMFile parent = fileRepository.findOne(file.getParentFile());
                    parent.setModifiedDate(new Date());
                    parent = fileRepository.save(parent);
                }
                PGCObject pgcObject = pgcObjectRepository.findOne(id);
                applicationEventPublisher.publishEvent(new SubstanceEvents.SubstanceFoldersDeletedEvent("pgc", id, file, pgcObject.getObjectType()));
            } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
                List<PQMSupplierAuditFile> supplierAuditFiles = supplierAuditFileRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
                utilService.removeFileIfExist((List) supplierAuditFiles, dir);
                PLMFile file = fileRepository.findOne(folderId);
                supplierAuditFileRepository.delete(folderId);
                if (file.getParentFile() != null) {
                    PLMFile parent = fileRepository.findOne(file.getParentFile());
                    parent.setModifiedDate(new Date());
                    parent = fileRepository.save(parent);
                }
                PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(id);
                applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditFoldersDeletedEvent(id, file));
            } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                List<PQMPPAPChecklist> pqmppapChecklists = ppapChecklistRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
                utilService.removeFileIfExist((List) pqmppapChecklists, dir);
                PLMFile file = fileRepository.findOne(folderId);
                ppapChecklistRepository.delete(folderId);
                if (file.getParentFile() != null) {
                    PLMFile parent = fileRepository.findOne(file.getParentFile());
                    parent.setModifiedDate(new Date());
                    parent = fileRepository.save(parent);
                }
                PQMPPAP ppap = ppapRepository.findOne(id);
                applicationEventPublisher.publishEvent(new PPAPEvents.PPAPFoldersDeletedEvent(ppap, file));
            }

            File fDir = new File(dir);
            FileUtils.deleteQuietly(fDir);
        } else if (objectType.equals(PLMObjectType.ITEM)) {
            itemFileService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.PROJECT)) {
            projectService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.TEMPLATE)) {
            projectTemplateService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.TEMPLATEACTIVITY)) {
            projectTemplateActivityService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.TEMPLATETASK)) {
            projectTemplateActivityService.deleteTaskFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.PROGRAMTEMPLATE)) {
            programTemplateService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
            manufacturerFileService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
            manufacturerPartFileService.deleteMfrPartFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            activityService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            activityService.deleteActivityTaskFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
            glossaryService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
            supplierFileService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
            customerFileService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
            reqDocumentFileService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
            requirementFileService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.PLMNPR)) {
            nprFileService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
            mbomFileService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
            mbomInstanceFileService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
            bopFileService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
            bopPlanFileService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
            bopInstanceOperationFileService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            programFileService.deleteFolder(id, folderId);
        } else if (objectType.equals(PLMObjectType.CHANGE)) {
            changeFileService.deleteFolder(id, folderId);
        }
    }

    @Transactional
    public ObjectFileDto moveObjectFileToFolder(Integer id, PLMObjectType objectType, ObjectFileDto objectFileDto) throws Exception {

        if (objectFileDto.getObjectFile().getObjectType().equals(PLMObjectType.OBJECTDOCUMENT)) {
            PLMObjectDocument objectDocument = objectDocumentRepository.findOne(objectFileDto.getObjectFile().getId());
            if (objectFileDto.getObjectFile().getParentFile() != null) {
                objectDocument.setFolder(objectFileDto.getObjectFile().getParentFile());
            } else {
                objectDocument.setFolder(null);
            }
            objectDocument = objectDocumentRepository.save(objectDocument);
            objectFileDto.setObjectFile(convertObjectDocumentToDto(objectDocument));
        } else {
            if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(fileDto.getId());
                PQMInspectionPlanFile existFile = (PQMInspectionPlanFile) Utils.cloneObject(inspectionPlanFile, PQMInspectionPlanFile.class);

                String oldFileDir = "";
                if (existFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(existFile.getInspectionPlan(), existFile.getId(), objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + existFile.getInspectionPlan() + File.separator + existFile.getId();
                }
                if (fileDto.getParentFile() != null) {
                    PQMInspectionPlanFile existItemFile = inspectionPlanFileRepository.findByParentFileAndNameAndLatestTrue(fileDto.getParentFile(), inspectionPlanFile.getName());
                    PQMInspectionPlanFile folder = inspectionPlanFileRepository.findOne(fileDto.getParentFile());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                        throw new CassiniException(result);
                    } else {
                        inspectionPlanFile.setParentFile(fileDto.getParentFile());
                        inspectionPlanFile = inspectionPlanFileRepository.save(inspectionPlanFile);
                    }
                } else {
                    PQMInspectionPlanFile existItemFile = inspectionPlanFileRepository.findByInspectionPlanAndNameAndParentFileIsNullAndLatestTrue(inspectionPlanFile.getInspectionPlan(), inspectionPlanFile.getName());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName());
                        throw new CassiniException(result);
                    } else {
                        inspectionPlanFile.setParentFile(null);
                        inspectionPlanFile = inspectionPlanFileRepository.save(inspectionPlanFile);
                    }
                }

                if (inspectionPlanFile != null) {
                    String dir = "";
                    if (inspectionPlanFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(inspectionPlanFile.getInspectionPlan(), inspectionPlanFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + inspectionPlanFile.getInspectionPlan() + File.separator + inspectionPlanFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                    File e = new File(oldFileDir);
                    System.gc();
                    Thread.sleep(1000L);
                    FileUtils.deleteQuietly(e);
                    List<PQMInspectionPlanFile> oldVersionFiles = inspectionPlanFileRepository.findByInspectionPlanAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getInspectionPlan(), existFile.getFileNo());
                    for (PQMInspectionPlanFile oldVersionFile : oldVersionFiles) {
                        oldFileDir = "";
                        if (oldVersionFile.getParentFile() != null) {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getInspectionPlan(), oldVersionFile.getId(), objectType);
                        } else {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + oldVersionFile.getInspectionPlan() + File.separator + oldVersionFile.getId();
                        }
                        dir = "";
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(inspectionPlanFile.getInspectionPlan(), inspectionPlanFile.getId(), objectType);
                        dir = dir + File.separator + oldVersionFile.getId();
                        oldVersionFile.setParentFile(inspectionPlanFile.getParentFile());
                        oldVersionFile = inspectionPlanFileRepository.save(oldVersionFile);
                        fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.createNewFile();
                        }

                        Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        e = new File(oldFileDir);
                        System.gc();
                        Thread.sleep(1000L);
                        FileUtils.deleteQuietly(e);
                    }
                }
            } else if (objectType.equals(PLMObjectType.INSPECTION)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(fileDto.getId());
                PQMInspectionFile existFile = (PQMInspectionFile) Utils.cloneObject(inspectionFile, PQMInspectionFile.class);
                PLMFile plmFile = fileRepository.findOne(inspectionFile.getId());

                String oldFileDir = "";
                if (existFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(existFile.getInspection(), existFile.getId(), objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + existFile.getInspection() + File.separator + existFile.getId();
                }
                if (fileDto.getParentFile() != null) {
                    PQMInspectionFile existItemFile = inspectionFileRepository.findByParentFileAndNameAndLatestTrue(fileDto.getParentFile(), inspectionFile.getName());
                    PQMInspectionFile folder = inspectionFileRepository.findOne(fileDto.getParentFile());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                        throw new CassiniException(result);
                    } else {
                        inspectionFile.setParentFile(fileDto.getParentFile());
                        inspectionFile = inspectionFileRepository.save(inspectionFile);
                    }
                } else {
                    PQMInspectionFile existItemFile = inspectionFileRepository.findByInspectionAndNameAndParentFileIsNullAndLatestTrue(inspectionFile.getInspection(), inspectionFile.getName());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName());
                        throw new CassiniException(result);
                    } else {
                        inspectionFile.setParentFile(null);
                        inspectionFile = inspectionFileRepository.save(inspectionFile);
                    }
                }

                if (inspectionFile != null) {
                    String dir = "";
                    if (inspectionFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(inspectionFile.getInspection(), inspectionFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + inspectionFile.getInspection() + File.separator + inspectionFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                    File e = new File(oldFileDir);
                    System.gc();
                    Thread.sleep(1000L);
                    FileUtils.deleteQuietly(e);
                    List<PQMInspectionFile> oldVersionFiles = inspectionFileRepository.findByInspectionAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getInspection(), existFile.getFileNo());
                    for (PQMInspectionFile oldVersionFile : oldVersionFiles) {
                        oldFileDir = "";
                        if (oldVersionFile.getParentFile() != null) {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getInspection(), oldVersionFile.getId(), objectType);
                        } else {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + oldVersionFile.getInspection() + File.separator + oldVersionFile.getId();
                        }
                        dir = "";
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(inspectionFile.getInspection(), inspectionFile.getId(), objectType);
                        dir = dir + File.separator + oldVersionFile.getId();
                        oldVersionFile.setParentFile(inspectionFile.getParentFile());
                        oldVersionFile = inspectionFileRepository.save(oldVersionFile);
                        fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.createNewFile();
                        }
                        Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        e = new File(oldFileDir);
                        System.gc();
                        Thread.sleep(1000L);
                        FileUtils.deleteQuietly(e);
                    }
                }
            } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(fileDto.getId());
                PQMProblemReportFile existFile = (PQMProblemReportFile) Utils.cloneObject(problemReportFile, PQMProblemReportFile.class);

                String oldFileDir = "";
                if (existFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(existFile.getProblemReport(), existFile.getId(), objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + existFile.getProblemReport() + File.separator + existFile.getId();
                }
                if (fileDto.getParentFile() != null) {
                    PQMProblemReportFile existItemFile = problemReportFileRepository.findByParentFileAndNameAndLatestTrue(fileDto.getParentFile(), problemReportFile.getName());
                    PQMProblemReportFile folder = problemReportFileRepository.findOne(fileDto.getParentFile());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                        throw new CassiniException(result);
                    } else {
                        problemReportFile.setParentFile(fileDto.getParentFile());
                        problemReportFile = problemReportFileRepository.save(problemReportFile);
                    }
                } else {
                    PQMProblemReportFile existItemFile = problemReportFileRepository.findByProblemReportAndNameAndParentFileIsNullAndLatestTrue(problemReportFile.getProblemReport(), problemReportFile.getName());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName());
                        throw new CassiniException(result);
                    } else {
                        problemReportFile.setParentFile(null);
                        problemReportFile = problemReportFileRepository.save(problemReportFile);
                    }
                }

                if (problemReportFile != null) {
                    String dir = "";
                    if (problemReportFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(problemReportFile.getProblemReport(), problemReportFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + problemReportFile.getProblemReport() + File.separator + problemReportFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                    File e = new File(oldFileDir);
                    System.gc();
                    Thread.sleep(1000L);
                    FileUtils.deleteQuietly(e);
                    List<PQMProblemReportFile> oldVersionFiles = problemReportFileRepository.findByProblemReportAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getProblemReport(), existFile.getFileNo());
                    for (PQMProblemReportFile oldVersionFile : oldVersionFiles) {
                        oldFileDir = "";
                        if (oldVersionFile.getParentFile() != null) {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getProblemReport(), oldVersionFile.getId(), objectType);
                        } else {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + oldVersionFile.getProblemReport() + File.separator + oldVersionFile.getId();
                        }
                        dir = "";
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(problemReportFile.getProblemReport(), problemReportFile.getId(), objectType);
                        dir = dir + File.separator + oldVersionFile.getId();
                        oldVersionFile.setParentFile(problemReportFile.getParentFile());
                        oldVersionFile = problemReportFileRepository.save(oldVersionFile);
                        fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.createNewFile();
                        }

                        Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        e = new File(oldFileDir);
                        System.gc();
                        Thread.sleep(1000L);
                        FileUtils.deleteQuietly(e);
                    }
                }
            } else if (objectType.equals(PLMObjectType.NCR)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PQMNCRFile pqmncrFile = ncrFileRepository.findOne(fileDto.getId());
                PQMNCRFile existFile = (PQMNCRFile) Utils.cloneObject(pqmncrFile, PQMNCRFile.class);

                String oldFileDir = "";
                if (existFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(existFile.getNcr(), existFile.getId(), objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + existFile.getNcr() + File.separator + existFile.getId();
                }
                if (fileDto.getParentFile() != null) {
                    PQMNCRFile existItemFile = ncrFileRepository.findByParentFileAndNameAndLatestTrue(fileDto.getParentFile(), pqmncrFile.getName());
                    PQMNCRFile folder = ncrFileRepository.findOne(fileDto.getParentFile());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                        throw new CassiniException(result);
                    } else {
                        pqmncrFile.setParentFile(fileDto.getParentFile());
                        pqmncrFile = ncrFileRepository.save(pqmncrFile);
                    }
                } else {
                    PQMNCRFile existItemFile = ncrFileRepository.findByNcrAndNameAndParentFileIsNullAndLatestTrue(pqmncrFile.getNcr(), pqmncrFile.getName());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName());
                        throw new CassiniException(result);
                    } else {
                        pqmncrFile.setParentFile(null);
                        pqmncrFile = ncrFileRepository.save(pqmncrFile);
                    }
                }

                if (pqmncrFile != null) {
                    String dir = "";
                    if (pqmncrFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(pqmncrFile.getNcr(), pqmncrFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + pqmncrFile.getNcr() + File.separator + pqmncrFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;

                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                    File e = new File(oldFileDir);
                    System.gc();
                    Thread.sleep(1000L);
                    FileUtils.deleteQuietly(e);
                    List<PQMNCRFile> oldVersionFiles = ncrFileRepository.findByNcrAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getNcr(), existFile.getFileNo());
                    for (PQMNCRFile oldVersionFile : oldVersionFiles) {
                        oldFileDir = "";
                        if (oldVersionFile.getParentFile() != null) {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getNcr(), oldVersionFile.getId(), objectType);
                        } else {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + oldVersionFile.getNcr() + File.separator + oldVersionFile.getId();
                        }
                        dir = "";
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(pqmncrFile.getNcr(), pqmncrFile.getId(), objectType);
                        dir = dir + File.separator + oldVersionFile.getId();
                        oldVersionFile.setParentFile(pqmncrFile.getParentFile());
                        oldVersionFile = ncrFileRepository.save(oldVersionFile);
                        fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.createNewFile();
                        }

                        Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        e = new File(oldFileDir);
                        System.gc();
                        Thread.sleep(1000L);
                        FileUtils.deleteQuietly(e);
                    }
                }
            } else if (objectType.equals(PLMObjectType.QCR)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PQMQCRFile pqmqcrFile = qcrFileRepository.findOne(fileDto.getId());
                PQMQCRFile existFile = (PQMQCRFile) Utils.cloneObject(pqmqcrFile, PQMQCRFile.class);

                String oldFileDir = "";
                if (existFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(existFile.getQcr(), existFile.getId(), objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + existFile.getQcr() + File.separator + existFile.getId();
                }
                if (fileDto.getParentFile() != null) {
                    PQMQCRFile existItemFile = qcrFileRepository.findByParentFileAndNameAndLatestTrue(fileDto.getParentFile(), pqmqcrFile.getName());
                    PQMQCRFile folder = qcrFileRepository.findOne(fileDto.getParentFile());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                        throw new CassiniException(result);
                    } else {
                        pqmqcrFile.setParentFile(fileDto.getParentFile());
                        pqmqcrFile = qcrFileRepository.save(pqmqcrFile);
                    }
                } else {
                    PQMQCRFile existItemFile = qcrFileRepository.findByQcrAndNameAndParentFileIsNullAndLatestTrue(pqmqcrFile.getQcr(), pqmqcrFile.getName());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName());
                        throw new CassiniException(result);
                    } else {
                        pqmqcrFile.setParentFile(null);
                        pqmqcrFile = qcrFileRepository.save(pqmqcrFile);
                    }
                }

                if (pqmqcrFile != null) {
                    String dir = "";
                    if (pqmqcrFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(pqmqcrFile.getQcr(), pqmqcrFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + pqmqcrFile.getQcr() + File.separator + pqmqcrFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;

                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                    File e = new File(oldFileDir);
                    System.gc();
                    Thread.sleep(1000L);
                    FileUtils.deleteQuietly(e);
                    List<PQMQCRFile> oldVersionFiles = qcrFileRepository.findByQcrAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getQcr(), existFile.getFileNo());
                    for (PQMQCRFile oldVersionFile : oldVersionFiles) {
                        oldFileDir = "";
                        if (oldVersionFile.getParentFile() != null) {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getQcr(), oldVersionFile.getId(), objectType);
                        } else {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + oldVersionFile.getQcr() + File.separator + oldVersionFile.getId();
                        }
                        dir = "";
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(pqmqcrFile.getQcr(), pqmqcrFile.getId(), objectType);
                        dir = dir + File.separator + oldVersionFile.getId();
                        oldVersionFile.setParentFile(pqmqcrFile.getParentFile());
                        oldVersionFile = qcrFileRepository.save(oldVersionFile);
                        fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.createNewFile();
                        }

                        Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        e = new File(oldFileDir);
                        System.gc();
                        Thread.sleep(1000L);
                        FileUtils.deleteQuietly(e);
                    }
                }
            } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(fileDto.getId());
                MESObjectFile existFile = (MESObjectFile) Utils.cloneObject(mesObjectFile, MESObjectFile.class);

                String oldFileDir = "";
                if (existFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(existFile.getObject(), existFile.getId(), objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + existFile.getObject() + File.separator + existFile.getId();
                }
                if (fileDto.getParentFile() != null) {
                    MESObjectFile existItemFile = mesObjectFileRepository.findByParentFileAndNameAndLatestTrue(fileDto.getParentFile(), mesObjectFile.getName());
                    MESObjectFile folder = mesObjectFileRepository.findOne(fileDto.getParentFile());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                        throw new CassiniException(result);
                    } else {
                        mesObjectFile.setParentFile(fileDto.getParentFile());
                        mesObjectFile = mesObjectFileRepository.save(mesObjectFile);
                    }
                } else {
                    MESObjectFile existItemFile = mesObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(mesObjectFile.getObject(), mesObjectFile.getName());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName());
                        throw new CassiniException(result);
                    } else {
                        mesObjectFile.setParentFile(null);
                        mesObjectFile = mesObjectFileRepository.save(mesObjectFile);
                    }
                }

                if (mesObjectFile != null) {
                    String dir = "";
                    if (mesObjectFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(mesObjectFile.getObject(), mesObjectFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + mesObjectFile.getObject() + File.separator + mesObjectFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;

                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                    File e = new File(oldFileDir);
                    System.gc();
                    Thread.sleep(1000L);
                    FileUtils.deleteQuietly(e);
                    List<MESObjectFile> oldVersionFiles = mesObjectFileRepository.findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getObject(), existFile.getFileNo());
                    for (MESObjectFile oldVersionFile : oldVersionFiles) {
                        oldFileDir = "";
                        if (oldVersionFile.getParentFile() != null) {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getObject(), oldVersionFile.getId(), objectType);
                        } else {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + oldVersionFile.getObject() + File.separator + oldVersionFile.getId();
                        }
                        dir = "";
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(mesObjectFile.getObject(), mesObjectFile.getId(), objectType);
                        dir = dir + File.separator + oldVersionFile.getId();
                        oldVersionFile.setParentFile(mesObjectFile.getParentFile());
                        oldVersionFile = mesObjectFileRepository.save(oldVersionFile);
                        fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.createNewFile();
                        }

                        Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        e = new File(oldFileDir);
                        System.gc();
                        Thread.sleep(1000L);
                        FileUtils.deleteQuietly(e);
                    }
                }
            } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMDocument plmDocument = plmDocumentRepository.findOne(fileDto.getId());
                PLMDocument existFile = (PLMDocument) Utils.cloneObject(plmDocument, PLMDocument.class);

                String oldFileDir = "";
                if (existFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + "documents" + getDocumentParentFileSystemPath(existFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + "documents" + File.separator + existFile.getId();
                }
                if (fileDto.getParentFile() != null) {
                    PLMDocument existItemFile = plmDocumentRepository.findByNameEqualsIgnoreCaseAndParentFileAndLatestTrue(plmDocument.getName(), fileDto.getParentFile());
                    PLMDocument folder = plmDocumentRepository.findOne(fileDto.getParentFile());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                        throw new CassiniException(result);
                    } else {
                        plmDocument.setParentFile(fileDto.getParentFile());
                        plmDocument = plmDocumentRepository.save(plmDocument);
                    }
                } else {
                    PLMDocument existItemFile = plmDocumentRepository.findByNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(plmDocument.getName());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName());
                        throw new CassiniException(result);
                    } else {
                        plmDocument.setParentFile(null);
                        plmDocument = plmDocumentRepository.save(plmDocument);
                    }
                }

                if (plmDocument != null) {
                    String dir = "";
                    if (plmDocument.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + "documents" + getDocumentParentFileSystemPath(plmDocument.getId());
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + "documents" + File.separator + plmDocument.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;

                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                    File e = new File(oldFileDir);
                    System.gc();
                    Thread.sleep(1000L);
                    FileUtils.deleteQuietly(e);
                    List<PLMDocument> oldVersionFiles = plmDocumentRepository.findByFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getFileNo());
                    for (PLMDocument oldVersionFile : oldVersionFiles) {
                        oldFileDir = "";
                        if (oldVersionFile.getParentFile() != null) {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + "documents" + getDocumentParentFileSystemPath(oldVersionFile.getId());
                        } else {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + "documents" + File.separator + oldVersionFile.getId();
                        }
                        dir = "";
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + "documents" + getReplaceDocumentFileSystemPath(plmDocument.getId());
                        dir = dir + File.separator + oldVersionFile.getId();
                        oldVersionFile.setParentFile(plmDocument.getParentFile());
                        oldVersionFile = plmDocumentRepository.save(oldVersionFile);
                        fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.createNewFile();
                        }

                        Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        e = new File(oldFileDir);
                        System.gc();
                        Thread.sleep(1000L);
                        FileUtils.deleteQuietly(e);
                    }
                }
            } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMMfrPartInspectionReport plmMfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(fileDto.getId());
                PLMMfrPartInspectionReport existFile = (PLMMfrPartInspectionReport) Utils.cloneObject(plmMfrPartInspectionReport, PLMMfrPartInspectionReport.class);

                String oldFileDir = "";
                if (existFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(existFile.getManufacturerPart(), existFile.getId(), objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + existFile.getManufacturerPart() + File.separator + existFile.getId();
                }
                if (fileDto.getParentFile() != null) {
                    PLMMfrPartInspectionReport existItemFile = mfrPartInspectionReportRepository.findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileAndLatestTrue(id, plmMfrPartInspectionReport.getName(), fileDto.getParentFile());
                    PLMMfrPartInspectionReport folder = mfrPartInspectionReportRepository.findOne(fileDto.getParentFile());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                        throw new CassiniException(result);
                    } else {
                        plmMfrPartInspectionReport.setParentFile(fileDto.getParentFile());
                        plmMfrPartInspectionReport = mfrPartInspectionReportRepository.save(plmMfrPartInspectionReport);
                    }
                } else {
                    PLMMfrPartInspectionReport existItemFile = mfrPartInspectionReportRepository.findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(id, plmMfrPartInspectionReport.getName());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName());
                        throw new CassiniException(result);
                    } else {
                        plmMfrPartInspectionReport.setParentFile(null);
                        plmMfrPartInspectionReport = mfrPartInspectionReportRepository.save(plmMfrPartInspectionReport);
                    }
                }

                if (plmMfrPartInspectionReport != null) {
                    String dir = "";
                    if (plmMfrPartInspectionReport.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(plmMfrPartInspectionReport.getManufacturerPart(), plmMfrPartInspectionReport.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + plmMfrPartInspectionReport.getManufacturerPart() + File.separator + plmMfrPartInspectionReport.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;

                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                    File e = new File(oldFileDir);
                    System.gc();
                    Thread.sleep(1000L);
                    FileUtils.deleteQuietly(e);
                    List<PLMMfrPartInspectionReport> oldVersionFiles = mfrPartInspectionReportRepository.findByFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getFileNo());
                    for (PLMMfrPartInspectionReport oldVersionFile : oldVersionFiles) {
                        oldFileDir = "";
                        if (oldVersionFile.getParentFile() != null) {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getManufacturerPart(), oldVersionFile.getId(), objectType);
                        } else {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + oldVersionFile.getManufacturerPart() + File.separator + oldVersionFile.getId();
                        }
                        dir = "";
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(plmMfrPartInspectionReport.getManufacturerPart(), plmMfrPartInspectionReport.getId(), objectType);
                        dir = dir + File.separator + oldVersionFile.getId();
                        oldVersionFile.setParentFile(plmMfrPartInspectionReport.getParentFile());
                        oldVersionFile = mfrPartInspectionReportRepository.save(oldVersionFile);
                        fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.createNewFile();
                        }

                        Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        e = new File(oldFileDir);
                        System.gc();
                        Thread.sleep(1000L);
                        FileUtils.deleteQuietly(e);
                    }
                }
            } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(fileDto.getId());
                MROObjectFile existFile = (MROObjectFile) Utils.cloneObject(mroObjectFile, MROObjectFile.class);

                String oldFileDir = "";
                if (existFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(existFile.getObject(), existFile.getId(), objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + existFile.getObject() + File.separator + existFile.getId();
                }
                if (fileDto.getParentFile() != null) {
                    MROObjectFile existItemFile = mroObjectFileRepository.findByParentFileAndNameAndLatestTrue(fileDto.getParentFile(), mroObjectFile.getName());
                    MROObjectFile folder = mroObjectFileRepository.findOne(fileDto.getParentFile());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                        throw new CassiniException(result);
                    } else {
                        mroObjectFile.setParentFile(fileDto.getParentFile());
                        mroObjectFile = mroObjectFileRepository.save(mroObjectFile);
                    }
                } else {
                    MROObjectFile existItemFile = mroObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(mroObjectFile.getObject(), mroObjectFile.getName());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName());
                        throw new CassiniException(result);
                    } else {
                        mroObjectFile.setParentFile(null);
                        mroObjectFile = mroObjectFileRepository.save(mroObjectFile);
                    }
                }

                if (mroObjectFile != null) {
                    String dir = "";
                    if (mroObjectFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(mroObjectFile.getObject(), mroObjectFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + mroObjectFile.getObject() + File.separator + mroObjectFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;

                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                    File e = new File(oldFileDir);
                    System.gc();
                    Thread.sleep(1000L);
                    FileUtils.deleteQuietly(e);
                    List<MROObjectFile> oldVersionFiles = mroObjectFileRepository.findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getObject(), existFile.getFileNo());
                    for (MROObjectFile oldVersionFile : oldVersionFiles) {
                        oldFileDir = "";
                        if (oldVersionFile.getParentFile() != null) {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getObject(), oldVersionFile.getId(), objectType);
                        } else {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + oldVersionFile.getObject() + File.separator + oldVersionFile.getId();
                        }
                        dir = "";
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(mroObjectFile.getObject(), mroObjectFile.getId(), objectType);
                        dir = dir + File.separator + oldVersionFile.getId();
                        oldVersionFile.setParentFile(mroObjectFile.getParentFile());
                        oldVersionFile = mroObjectFileRepository.save(oldVersionFile);
                        fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.createNewFile();
                        }

                        Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        e = new File(oldFileDir);
                        System.gc();
                        Thread.sleep(1000L);
                        FileUtils.deleteQuietly(e);
                    }
                }
            } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(fileDto.getId());
                PGCObjectFile existFile = (PGCObjectFile) Utils.cloneObject(pgcObjectFile, PGCObjectFile.class);

                String oldFileDir = "";
                if (existFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(existFile.getObject(), existFile.getId(), objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + existFile.getObject() + File.separator + existFile.getId();
                }
                if (fileDto.getParentFile() != null) {
                    PGCObjectFile existItemFile = pgcObjectFileRepository.findByParentFileAndNameAndLatestTrue(fileDto.getParentFile(), pgcObjectFile.getName());
                    PGCObjectFile folder = pgcObjectFileRepository.findOne(fileDto.getParentFile());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                        throw new CassiniException(result);
                    } else {
                        pgcObjectFile.setParentFile(fileDto.getParentFile());
                        pgcObjectFile = pgcObjectFileRepository.save(pgcObjectFile);
                    }
                } else {
                    PGCObjectFile existItemFile = pgcObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(pgcObjectFile.getObject(), pgcObjectFile.getName());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName());
                        throw new CassiniException(result);
                    } else {
                        pgcObjectFile.setParentFile(null);
                        pgcObjectFile = pgcObjectFileRepository.save(pgcObjectFile);
                    }
                }

                if (pgcObjectFile != null) {
                    String dir = "";
                    if (pgcObjectFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(pgcObjectFile.getObject(), pgcObjectFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + pgcObjectFile.getObject() + File.separator + pgcObjectFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;

                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                    File e = new File(oldFileDir);
                    System.gc();
                    Thread.sleep(1000L);
                    FileUtils.deleteQuietly(e);
                    List<PGCObjectFile> oldVersionFiles = pgcObjectFileRepository.findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getObject(), existFile.getFileNo());
                    for (PGCObjectFile oldVersionFile : oldVersionFiles) {
                        oldFileDir = "";
                        if (oldVersionFile.getParentFile() != null) {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getObject(), oldVersionFile.getId(), objectType);
                        } else {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + oldVersionFile.getObject() + File.separator + oldVersionFile.getId();
                        }
                        dir = "";
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(pgcObjectFile.getObject(), pgcObjectFile.getId(), objectType);
                        dir = dir + File.separator + oldVersionFile.getId();
                        oldVersionFile.setParentFile(pgcObjectFile.getParentFile());
                        oldVersionFile = pgcObjectFileRepository.save(oldVersionFile);
                        fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.createNewFile();
                        }

                        Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        e = new File(oldFileDir);
                        System.gc();
                        Thread.sleep(1000L);
                        FileUtils.deleteQuietly(e);
                    }
                }
            } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(fileDto.getId());
                PGCObjectFile existFile = (PGCObjectFile) Utils.cloneObject(supplierAuditFile, PGCObjectFile.class);

                String oldFileDir = "";
                if (existFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(existFile.getObject(), existFile.getId(), objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + existFile.getObject() + File.separator + existFile.getId();
                }
                if (fileDto.getParentFile() != null) {
                    PQMSupplierAuditFile existItemFile = supplierAuditFileRepository.findByParentFileAndNameAndLatestTrue(fileDto.getParentFile(), supplierAuditFile.getName());
                    PQMSupplierAuditFile folder = supplierAuditFileRepository.findOne(fileDto.getParentFile());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                        throw new CassiniException(result);
                    } else {
                        supplierAuditFile.setParentFile(fileDto.getParentFile());
                        supplierAuditFile = supplierAuditFileRepository.save(supplierAuditFile);
                    }
                } else {
                    PQMSupplierAuditFile existItemFile = supplierAuditFileRepository.findBySupplierAuditAndNameAndParentFileIsNullAndLatestTrue(supplierAuditFile.getSupplierAudit(), supplierAuditFile.getName());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName());
                        throw new CassiniException(result);
                    } else {
                        supplierAuditFile.setParentFile(null);
                        supplierAuditFile = supplierAuditFileRepository.save(supplierAuditFile);
                    }
                }

                if (supplierAuditFile != null) {
                    String dir = "";
                    if (supplierAuditFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(supplierAuditFile.getSupplierAudit(), supplierAuditFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + supplierAuditFile.getSupplierAudit() + File.separator + supplierAuditFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;

                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                    File e = new File(oldFileDir);
                    System.gc();
                    Thread.sleep(1000L);
                    FileUtils.deleteQuietly(e);
                    List<PQMSupplierAuditFile> oldVersionFiles = supplierAuditFileRepository.findBySupplierAuditAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getObject(), existFile.getFileNo());
                    for (PQMSupplierAuditFile oldVersionFile : oldVersionFiles) {
                        oldFileDir = "";
                        if (oldVersionFile.getParentFile() != null) {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getSupplierAudit(), oldVersionFile.getId(), objectType);
                        } else {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + oldVersionFile.getSupplierAudit() + File.separator + oldVersionFile.getId();
                        }
                        dir = "";
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(supplierAuditFile.getSupplierAudit(), supplierAuditFile.getId(), objectType);
                        dir = dir + File.separator + oldVersionFile.getId();
                        oldVersionFile.setParentFile(supplierAuditFile.getParentFile());
                        oldVersionFile = supplierAuditFileRepository.save(oldVersionFile);
                        fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.createNewFile();
                        }

                        Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        e = new File(oldFileDir);
                        System.gc();
                        Thread.sleep(1000L);
                        FileUtils.deleteQuietly(e);
                    }
                }
            } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(fileDto.getId());
                PQMPPAPChecklist existFile = (PQMPPAPChecklist) Utils.cloneObject(pqmppapChecklist, PQMPPAPChecklist.class);

                String oldFileDir = "";
                if (existFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(existFile.getPpap(), existFile.getId(), objectType);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + existFile.getPpap() + File.separator + existFile.getId();
                }
                if (fileDto.getParentFile() != null) {
                    PQMPPAPChecklist existItemFile = ppapChecklistRepository.findByParentFileAndNameAndLatestTrue(fileDto.getParentFile(), pqmppapChecklist.getName());
                    PQMPPAPChecklist folder = ppapChecklistRepository.findOne(fileDto.getParentFile());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                        throw new CassiniException(result);
                    } else {
                        pqmppapChecklist.setParentFile(fileDto.getParentFile());
                        pqmppapChecklist = ppapChecklistRepository.save(pqmppapChecklist);
                    }
                } else {
                    PQMPPAPChecklist existItemFile = ppapChecklistRepository.findByPpapAndNameAndParentFileIsNullAndLatestTrue(pqmppapChecklist.getPpap(), pqmppapChecklist.getName());
                    if (existItemFile != null) {
                        String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", existItemFile.getName());
                        throw new CassiniException(result);
                    } else {
                        pqmppapChecklist.setParentFile(null);
                        pqmppapChecklist = ppapChecklistRepository.save(pqmppapChecklist);
                    }
                }

                if (pqmppapChecklist != null) {
                    String dir = "";
                    if (pqmppapChecklist.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(pqmppapChecklist.getPpap(), pqmppapChecklist.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + pqmppapChecklist.getPpap() + File.separator + pqmppapChecklist.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;

                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                    File e = new File(oldFileDir);
                    System.gc();
                    Thread.sleep(1000L);
                    FileUtils.deleteQuietly(e);
                    List<PQMPPAPChecklist> oldVersionFiles = ppapChecklistRepository.findByPpapAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getPpap(), existFile.getFileNo());
                    for (PQMPPAPChecklist oldVersionFile : oldVersionFiles) {
                        oldFileDir = "";
                        if (oldVersionFile.getParentFile() != null) {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getPpap(), oldVersionFile.getId(), objectType);
                        } else {
                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + oldVersionFile.getPpap() + File.separator + oldVersionFile.getId();
                        }
                        dir = "";
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(pqmppapChecklist.getPpap(), pqmppapChecklist.getId(), objectType);
                        dir = dir + File.separator + oldVersionFile.getId();
                        oldVersionFile.setParentFile(pqmppapChecklist.getParentFile());
                        oldVersionFile = ppapChecklistRepository.save(oldVersionFile);
                        fDir = new File(dir);
                        if (!fDir.exists()) {
                            fDir.createNewFile();
                        }

                        Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        e = new File(oldFileDir);
                        System.gc();
                        Thread.sleep(1000L);
                        FileUtils.deleteQuietly(e);
                    }
                }
            } else if (objectType.equals(PLMObjectType.ITEM)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMItemFile itemFile = itemFileRepository.findOne(fileDto.getId());
                PLMItemFile plmItemFile = JsonUtils.cloneEntity(itemFile, PLMItemFile.class);
                plmItemFile.setParentFile(fileDto.getParentFile());
                itemFileService.moveItemFileToFolder(id, plmItemFile);
            } else if (objectType.equals(PLMObjectType.PROJECT)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMProjectFile projectFile = projectFileRepository.findOne(fileDto.getId());
                PLMProjectFile plmProjectFile = JsonUtils.cloneEntity(projectFile, PLMProjectFile.class);
                plmProjectFile.setParentFile(fileDto.getParentFile());
                projectService.moveProjectFileToFolder(id, plmProjectFile);
            } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMManufacturerFile manufacturerFile = manufacturerFileRepository.findOne(fileDto.getId());
                PLMManufacturerFile file = JsonUtils.cloneEntity(manufacturerFile, PLMManufacturerFile.class);
                file.setParentFile(fileDto.getParentFile());
                manufacturerFileService.moveMfrFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileRepository.findOne(fileDto.getId());
                PLMManufacturerPartFile file = JsonUtils.cloneEntity(manufacturerPartFile, PLMManufacturerPartFile.class);
                file.setParentFile(fileDto.getParentFile());
                manufacturerPartFileService.moveMfrPartFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMActivityFile activityFile = activityFileRepository.findOne(fileDto.getId());
                PLMActivityFile file = JsonUtils.cloneEntity(activityFile, PLMActivityFile.class);
                file.setParentFile(fileDto.getParentFile());
                activityService.moveActivityFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMTaskFile taskFile = taskFileRepository.findOne(fileDto.getId());
                PLMTaskFile file = JsonUtils.cloneEntity(taskFile, PLMTaskFile.class);
                file.setParentFile(fileDto.getParentFile());
                activityService.moveActivityTaskFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMGlossaryFile glossaryFile = glossaryFileRepository.findOne(fileDto.getId());
                PLMGlossaryFile file = JsonUtils.cloneEntity(glossaryFile, PLMGlossaryFile.class);
                file.setParentFile(fileDto.getParentFile());
                glossaryService.moveGlossaryFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMSupplierFile supplierFile = supplierFileRepository.findOne(fileDto.getId());
                PLMSupplierFile file = JsonUtils.cloneEntity(supplierFile, PLMSupplierFile.class);
                file.setParentFile(fileDto.getParentFile());
                supplierFileService.moveSupplierFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PQMCustomerFile customerFile = customerFileRepository.findOne(fileDto.getId());
                PQMCustomerFile file = JsonUtils.cloneEntity(customerFile, PQMCustomerFile.class);
                file.setParentFile(fileDto.getParentFile());
                customerFileService.moveCustomerFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMRequirementDocumentFile documentFile = requirementDocumentFileRepository.findOne(fileDto.getId());
                PLMRequirementDocumentFile file = JsonUtils.cloneEntity(documentFile, PLMRequirementDocumentFile.class);
                file.setParentFile(fileDto.getParentFile());
                reqDocumentFileService.moveReqDocumentFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMRequirementFile requirementFile = requirementFileRepository.findOne(fileDto.getId());
                PLMRequirementFile file = JsonUtils.cloneEntity(requirementFile, PLMRequirementFile.class);
                file.setParentFile(fileDto.getParentFile());
                requirementFileService.moveRequirementFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.PLMNPR)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMNprFile nprFile = nprFileRepository.findOne(fileDto.getId());
                PLMNprFile file = JsonUtils.cloneEntity(nprFile, PLMNprFile.class);
                file.setParentFile(fileDto.getParentFile());
                nprFileService.moveNprFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                MESMBOMFile mesmbomFile = mbomFileRepository.findOne(fileDto.getId());
                MESMBOMFile file = JsonUtils.cloneEntity(mesmbomFile, MESMBOMFile.class);
                file.setParentFile(fileDto.getParentFile());
                mbomFileService.moveNprFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                MESMBOMInstanceFile mesmbomFile = mbomInstanceFileRepository.findOne(fileDto.getId());
                MESMBOMInstanceFile file = JsonUtils.cloneEntity(mesmbomFile, MESMBOMInstanceFile.class);
                file.setParentFile(fileDto.getParentFile());
                mbomInstanceFileService.moveMBOMInstanceFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                MESBOPFile mesbopFile = bopFileRepository.findOne(fileDto.getId());
                MESBOPFile file = JsonUtils.cloneEntity(mesbopFile, MESBOPFile.class);
                file.setParentFile(fileDto.getParentFile());
                bopFileService.moveNprFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                MESBOPOperationFile mesbopFile = bopOperationFileRepository.findOne(fileDto.getId());
                MESBOPOperationFile file = JsonUtils.cloneEntity(mesbopFile, MESBOPOperationFile.class);
                file.setParentFile(fileDto.getParentFile());
                bopPlanFileService.moveBOPPlanFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                MESBOPInstanceOperationFile mesbopFile = bopInstanceOperationFileRepository.findOne(fileDto.getId());
                MESBOPInstanceOperationFile file = JsonUtils.cloneEntity(mesbopFile, MESBOPInstanceOperationFile.class);
                file.setParentFile(fileDto.getParentFile());
                bopInstanceOperationFileService.moveBOPPlanFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.PROGRAM)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMProgramFile programFile = programFileRepository.findOne(fileDto.getId());
                PLMProgramFile file = JsonUtils.cloneEntity(programFile, PLMProgramFile.class);
                file.setParentFile(fileDto.getParentFile());
                programFileService.moveNprFileToFolder(id, file);
            } else if (objectType.equals(PLMObjectType.CHANGE)) {
                FileDto fileDto = objectFileDto.getObjectFile();
                PLMChangeFile changeFile = changeFileRepository.findOne(fileDto.getId());
                PLMChangeFile file = JsonUtils.cloneEntity(changeFile, PLMChangeFile.class);
                file.setParentFile(fileDto.getParentFile());
                changeFileService.moveChangeFileToFolder(id, file);
            }
        }

        return objectFileDto;
    }

    @Transactional
    public ObjectFileDto pasteFromClipboard(Integer id, PLMObjectType objectType, Integer fileId, List<PLMFile> files) {
        ObjectFileDto objectFileDto = new ObjectFileDto();

        List<PLMFile> fileList = new ArrayList<>();

        List<PLMFile> normalFiles = new ArrayList<>();
        List<PLMFile> objectDocuments = new ArrayList<>();
        List<PLMObjectDocument> objectDocumentList = new ArrayList<>();
        files.forEach(file -> {
            if (file.getObjectType().name().equals(PLMObjectType.OBJECTDOCUMENT.name()) || file.getObjectType().name().equals(PLMObjectType.DOCUMENT.name())) {
                objectDocuments.add(file);
            } else {
                normalFiles.add(file);
            }
        });

        objectDocuments.forEach(objectDocument -> {
            PLMObjectDocument plmObjectDocument = new PLMObjectDocument();
            PLMDocument document = null;
            if (objectDocument.getObjectType().name().equals(PLMObjectType.OBJECTDOCUMENT.name())) {
                PLMObjectDocument existObjectDocument = objectDocumentRepository.findOne(objectDocument.getId());
                if (existObjectDocument != null) {
                    document = plmDocumentRepository.findOne(existObjectDocument.getDocument().getId());
                }
            } else {
                document = plmDocumentRepository.findOne(objectDocument.getId());
            }
            if (document != null) {
                plmObjectDocument.setDocument(document);
                plmObjectDocument.setObject(id);
                if (fileId != 0) {
                    plmObjectDocument.setFolder(fileId);
                    PLMObjectDocument existDocument = objectDocumentRepository.findByDocumentIdAndObjectAndFolder(plmObjectDocument.getDocument().getId(), plmObjectDocument.getObject(), fileId);
                    if (existDocument == null) {
                        plmObjectDocument = objectDocumentRepository.save(plmObjectDocument);
                        objectDocumentList.add(plmObjectDocument);
                    }
                } else {
                    PLMObjectDocument existDocument = objectDocumentRepository.findByDocumentIdAndObjectAndFolderIsNull(plmObjectDocument.getDocument().getId(), plmObjectDocument.getObject());
                    if (existDocument == null) {
                        plmObjectDocument = objectDocumentRepository.save(plmObjectDocument);
                        objectDocumentList.add(plmObjectDocument);
                    }
                }
            }
        });

        if (objectType.equals(PLMObjectType.INSPECTIONPLAN) || objectType.equals(PLMObjectType.INSPECTION) || objectType.equals(PLMObjectType.PROBLEMREPORT)
                || objectType.equals(PLMObjectType.NCR) || objectType.equals(PLMObjectType.QCR) || objectType.equals(PLMObjectType.MESOBJECT)
                || objectType.equals(PLMObjectType.DOCUMENT) || objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT) || objectType.equals(PLMObjectType.PGCOBJECT)
                || objectType.equals(PLMObjectType.MROOBJECT) || objectType.equals(PLMObjectType.PPAPCHECKLIST) || objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            List<Integer> folderIds = new ArrayList<>();
            files.forEach(file -> {
                if (file.getFileType().equals("FOLDER")) {
                    folderIds.add(file.getId());
                }
            });
            for (PLMFile file : normalFiles) {
                Boolean canCreate = true;
                if (file.getParentFile() != null && folderIds.indexOf(file.getParentFile()) != -1) {
                    canCreate = false;
                }
                if (file.getFileType().equals("FILE") && canCreate) {
                    if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
                        PQMInspectionPlanFile inspectionPlanFile = new PQMInspectionPlanFile();
                        PQMInspectionPlanFile existFile = null;
                        if (fileId != 0) {
                            inspectionPlanFile.setParentFile(fileId);
                            existFile = inspectionPlanFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndInspectionPlanAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = inspectionPlanFileRepository.findByInspectionPlanAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            inspectionPlanFile.setName(file.getName());
                            inspectionPlanFile.setDescription(file.getDescription());
                            inspectionPlanFile.setInspectionPlan(id);
                            inspectionPlanFile.setVersion(1);
                            inspectionPlanFile.setSize(file.getSize());
                            inspectionPlanFile.setLatest(file.getLatest());
                            String autoNumber1 = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                            if (autoNumber != null) {
                                autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            inspectionPlanFile.setFileNo(autoNumber1);
                            inspectionPlanFile.setFileType("FILE");
                            inspectionPlanFile = inspectionPlanFileRepository.save(inspectionPlanFile);
                            PLMFile plmFile = fileRepository.findOne(inspectionPlanFile.getId());
                            plmFile.setOldVersion(plmFile.getVersion());
                            fileList.add(plmFile);
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(id, objectType, inspectionPlanFile.getId()));

                            inspectionPlanFile.setOldVersion(inspectionPlanFile.getVersion());

                            String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + itemFileService.getCopyFilePath(file);
                            String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDirectory = new File(directory);
                            if (!fDirectory.exists()) {
                                fDirectory.mkdirs();
                            }
                            String dir = "";
                            if (inspectionPlanFile.getParentFile() != null) {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + getParentFileSystemPath(id, inspectionPlanFile.getId(), objectType);
                            } else {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + id + File.separator + inspectionPlanFile.getId();
                            }
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                try {
                                    fDir.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            FileInputStream instream = null;
                            FileOutputStream outstream = null;
                            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        }
                    } else if (objectType.equals(PLMObjectType.INSPECTION)) {
                        PQMInspectionFile inspectionFile = new PQMInspectionFile();
                        PQMInspectionFile existFile = null;
                        if (fileId != 0) {
                            inspectionFile.setParentFile(fileId);
                            existFile = inspectionFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndInspectionAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = inspectionFileRepository.findByInspectionAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            inspectionFile.setName(file.getName());
                            inspectionFile.setDescription(file.getDescription());
                            inspectionFile.setInspection(id);
                            inspectionFile.setVersion(1);
                            inspectionFile.setSize(file.getSize());
                            inspectionFile.setLatest(file.getLatest());
                            String autoNumber1 = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                            if (autoNumber != null) {
                                autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            inspectionFile.setFileNo(autoNumber1);
                            inspectionFile.setFileType("FILE");
                            inspectionFile = inspectionFileRepository.save(inspectionFile);
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(inspectionFile.getInspection(), objectType, inspectionFile.getId()));
                            PLMFile plmFile = fileRepository.findOne(inspectionFile.getId());
                            plmFile.setOldVersion(plmFile.getVersion());
                            fileList.add(plmFile);
                            inspectionFile.setOldVersion(inspectionFile.getVersion());

                            String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + itemFileService.getCopyFilePath(file);
                            String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDirectory = new File(directory);
                            if (!fDirectory.exists()) {
                                fDirectory.mkdirs();
                            }
                            String dir = "";
                            if (inspectionFile.getParentFile() != null) {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + getParentFileSystemPath(id, inspectionFile.getId(), objectType);
                            } else {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + id + File.separator + inspectionFile.getId();
                            }
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                try {
                                    fDir.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            FileInputStream instream = null;
                            FileOutputStream outstream = null;
                            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        }
                    } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
                        PQMProblemReportFile problemReportFile = new PQMProblemReportFile();
                        PQMProblemReportFile existFile = null;
                        if (fileId != 0) {
                            problemReportFile.setParentFile(fileId);
                            existFile = problemReportFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndProblemReportAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = problemReportFileRepository.findByProblemReportAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            problemReportFile.setName(file.getName());
                            problemReportFile.setDescription(file.getDescription());
                            problemReportFile.setProblemReport(id);
                            problemReportFile.setVersion(1);
                            problemReportFile.setSize(file.getSize());
                            problemReportFile.setLatest(file.getLatest());
                            String autoNumber1 = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                            if (autoNumber != null) {
                                autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            problemReportFile.setFileNo(autoNumber1);
                            problemReportFile.setFileType("FILE");
                            problemReportFile = problemReportFileRepository.save(problemReportFile);
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(problemReportFile.getProblemReport(), objectType, problemReportFile.getId()));
                            PLMFile plmFile = fileRepository.findOne(problemReportFile.getId());
                            plmFile.setOldVersion(plmFile.getVersion());
                            fileList.add(plmFile);
                            problemReportFile.setOldVersion(problemReportFile.getVersion());

                            String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + itemFileService.getCopyFilePath(file);
                            String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDirectory = new File(directory);
                            if (!fDirectory.exists()) {
                                fDirectory.mkdirs();
                            }
                            String dir = "";
                            if (problemReportFile.getParentFile() != null) {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + getParentFileSystemPath(id, problemReportFile.getId(), objectType);
                            } else {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + id + File.separator + problemReportFile.getId();
                            }
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                try {
                                    fDir.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            FileInputStream instream = null;
                            FileOutputStream outstream = null;
                            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        }
                    } else if (objectType.equals(PLMObjectType.NCR)) {
                        PQMNCRFile pqmncrFile = new PQMNCRFile();
                        PQMNCRFile existFile = null;
                        if (fileId != 0) {
                            pqmncrFile.setParentFile(fileId);
                            existFile = ncrFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndNcrAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = ncrFileRepository.findByNcrAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            pqmncrFile.setName(file.getName());
                            pqmncrFile.setDescription(file.getDescription());
                            pqmncrFile.setNcr(id);
                            pqmncrFile.setVersion(1);
                            pqmncrFile.setSize(file.getSize());
                            pqmncrFile.setLatest(file.getLatest());
                            String autoNumber1 = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                            if (autoNumber != null) {
                                autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            pqmncrFile.setFileNo(autoNumber1);
                            pqmncrFile.setFileType("FILE");
                            pqmncrFile = ncrFileRepository.save(pqmncrFile);
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(pqmncrFile.getNcr(), objectType, pqmncrFile.getId()));
                            PLMFile plmFile = fileRepository.findOne(pqmncrFile.getId());
                            plmFile.setOldVersion(plmFile.getVersion());
                            fileList.add(plmFile);
                            pqmncrFile.setOldVersion(pqmncrFile.getVersion());

                            String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + itemFileService.getCopyFilePath(file);
                            String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDirectory = new File(directory);
                            if (!fDirectory.exists()) {
                                fDirectory.mkdirs();
                            }
                            String dir = "";
                            if (pqmncrFile.getParentFile() != null) {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + getParentFileSystemPath(id, pqmncrFile.getId(), objectType);
                            } else {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + id + File.separator + pqmncrFile.getId();
                            }
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                try {
                                    fDir.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            FileInputStream instream = null;
                            FileOutputStream outstream = null;
                            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        }
                    } else if (objectType.equals(PLMObjectType.QCR)) {
                        PQMQCRFile pqmqcrFile = new PQMQCRFile();
                        PQMQCRFile existFile = null;
                        if (fileId != 0) {
                            pqmqcrFile.setParentFile(fileId);
                            existFile = qcrFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndQcrAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = qcrFileRepository.findByQcrAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            pqmqcrFile.setName(file.getName());
                            pqmqcrFile.setDescription(file.getDescription());
                            pqmqcrFile.setQcr(id);
                            pqmqcrFile.setVersion(1);
                            pqmqcrFile.setSize(file.getSize());
                            pqmqcrFile.setLatest(file.getLatest());
                            String autoNumber1 = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                            if (autoNumber != null) {
                                autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            pqmqcrFile.setFileNo(autoNumber1);
                            pqmqcrFile.setFileType("FILE");
                            pqmqcrFile = qcrFileRepository.save(pqmqcrFile);
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(pqmqcrFile.getQcr(), objectType, pqmqcrFile.getId()));
                            PLMFile plmFile = fileRepository.findOne(pqmqcrFile.getId());
                            plmFile.setOldVersion(plmFile.getVersion());
                            fileList.add(plmFile);
                            pqmqcrFile.setOldVersion(pqmqcrFile.getVersion());

                            String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + itemFileService.getCopyFilePath(file);
                            String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDirectory = new File(directory);
                            if (!fDirectory.exists()) {
                                fDirectory.mkdirs();
                            }
                            String dir = "";
                            if (pqmqcrFile.getParentFile() != null) {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + getParentFileSystemPath(id, pqmqcrFile.getId(), objectType);
                            } else {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + id + File.separator + pqmqcrFile.getId();
                            }
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                try {
                                    fDir.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            FileInputStream instream = null;
                            FileOutputStream outstream = null;
                            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        }
                    } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
                        MESObjectFile mesObjectFile = new MESObjectFile();
                        MESObjectFile existFile = null;
                        if (fileId != 0) {
                            mesObjectFile.setParentFile(fileId);
                            existFile = mesObjectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = mesObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            mesObjectFile.setName(file.getName());
                            mesObjectFile.setDescription(file.getDescription());
                            mesObjectFile.setObject(id);
                            mesObjectFile.setVersion(1);
                            mesObjectFile.setSize(file.getSize());
                            mesObjectFile.setLatest(file.getLatest());
                            String autoNumber1 = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                            if (autoNumber != null) {
                                autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            mesObjectFile.setFileNo(autoNumber1);
                            mesObjectFile.setFileType("FILE");
                            mesObjectFile = mesObjectFileRepository.save(mesObjectFile);
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(mesObjectFile.getObject(), objectType, mesObjectFile.getId()));
                            PLMFile plmFile = fileRepository.findOne(mesObjectFile.getId());
                            plmFile.setOldVersion(plmFile.getVersion());
                            fileList.add(plmFile);
                            mesObjectFile.setOldVersion(mesObjectFile.getVersion());

                            String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + itemFileService.getCopyFilePath(file);
                            String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDirectory = new File(directory);
                            if (!fDirectory.exists()) {
                                fDirectory.mkdirs();
                            }
                            String dir = "";
                            if (mesObjectFile.getParentFile() != null) {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + getParentFileSystemPath(id, mesObjectFile.getId(), objectType);
                            } else {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + id + File.separator + mesObjectFile.getId();
                            }
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                try {
                                    fDir.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            FileInputStream instream = null;
                            FileOutputStream outstream = null;
                            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        }
                    } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
                        PLMDocument plmDocument = new PLMDocument();
                        PLMDocument existFile = null;
                        if (fileId != 0) {
                            plmDocument.setParentFile(fileId);
                            existFile = plmDocumentRepository.findByNameEqualsIgnoreCaseAndParentFileAndLatestTrue(file.getName(), fileId);
                        } else {
                            existFile = plmDocumentRepository.findByNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(file.getName());
                        }
                        if (existFile == null) {
                            plmDocument.setName(file.getName());
                            plmDocument.setDescription(file.getDescription());
                            plmDocument.setVersion(1);
                            plmDocument.setSize(file.getSize());
                            plmDocument.setLatest(file.getLatest());
                            String autoNumber1 = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                            if (autoNumber != null) {
                                autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            plmDocument.setFileNo(autoNumber1);
                            plmDocument.setFileType("FILE");
                            String newRevision = setRevisionAndLifecyclePhase();
                            PLMLifeCyclePhase plmLifeCyclePhase = setLifecyclePhase();
                            plmDocument.setRevision(newRevision);
                            plmDocument.setLifeCyclePhase(plmLifeCyclePhase);
                            plmDocument = plmDocumentRepository.save(plmDocument);
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(0, objectType, plmDocument.getId()));
                            PLMFile plmFile = fileRepository.findOne(plmDocument.getId());
                            plmFile.setOldVersion(plmFile.getVersion());
                            fileList.add(plmFile);
                            plmDocument.setOldVersion(plmDocument.getVersion());

                            String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + itemFileService.getCopyFilePath(file);
                            String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDirectory = new File(directory);
                            if (!fDirectory.exists()) {
                                fDirectory.mkdirs();
                            }
                            String dir = "";
                            if (plmDocument.getParentFile() != null) {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + "documents" + getDocumentParentFileSystemPath(plmDocument.getId());
                            } else {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + "documents" + File.separator + plmDocument.getId();
                            }
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                try {
                                    fDir.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            FileInputStream instream = null;
                            FileOutputStream outstream = null;
                            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        }
                    } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                        PLMMfrPartInspectionReport plmMfrPartInspectionReport = new PLMMfrPartInspectionReport();
                        PLMMfrPartInspectionReport existFile = null;
                        if (fileId != 0) {
                            plmMfrPartInspectionReport.setParentFile(fileId);
                            existFile = mfrPartInspectionReportRepository.findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileAndLatestTrue(id, file.getName(), fileId);
                        } else {
                            existFile = mfrPartInspectionReportRepository.findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            plmMfrPartInspectionReport.setName(file.getName());
                            plmMfrPartInspectionReport.setDescription(file.getDescription());
                            plmMfrPartInspectionReport.setVersion(1);
                            plmMfrPartInspectionReport.setSize(file.getSize());
                            plmMfrPartInspectionReport.setLatest(file.getLatest());
                            plmMfrPartInspectionReport.setManufacturerPart(id);
                            String autoNumber1 = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                            if (autoNumber != null) {
                                autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            plmMfrPartInspectionReport.setFileNo(autoNumber1);
                            plmMfrPartInspectionReport.setFileType("FILE");
                            plmMfrPartInspectionReport.setLifeCyclePhase(setLifecyclePhase());
                            plmMfrPartInspectionReport.setRevision(setRevisionAndLifecyclePhase());
                            plmMfrPartInspectionReport = mfrPartInspectionReportRepository.save(plmMfrPartInspectionReport);
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(0, objectType, plmMfrPartInspectionReport.getId()));
                            PLMFile plmFile = fileRepository.findOne(plmMfrPartInspectionReport.getId());
                            plmFile.setOldVersion(plmFile.getVersion());
                            fileList.add(plmFile);
                            plmMfrPartInspectionReport.setOldVersion(plmMfrPartInspectionReport.getVersion());

                            String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + itemFileService.getCopyFilePath(file);
                            String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDirectory = new File(directory);
                            if (!fDirectory.exists()) {
                                fDirectory.mkdirs();
                            }
                            String dir = "";
                            if (plmMfrPartInspectionReport.getParentFile() != null) {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + getParentFileSystemPath(id, plmMfrPartInspectionReport.getId(), objectType);
                            } else {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + id + File.separator + plmMfrPartInspectionReport.getId();
                            }
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                try {
                                    fDir.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            FileInputStream instream = null;
                            FileOutputStream outstream = null;
                            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        }
                    } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
                        MROObjectFile mroObjectFile = new MROObjectFile();
                        MROObjectFile existFile = null;
                        if (fileId != 0) {
                            mroObjectFile.setParentFile(fileId);
                            existFile = mroObjectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = mroObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            mroObjectFile.setName(file.getName());
                            mroObjectFile.setDescription(file.getDescription());
                            mroObjectFile.setObject(id);
                            mroObjectFile.setVersion(1);
                            mroObjectFile.setSize(file.getSize());
                            mroObjectFile.setLatest(file.getLatest());
                            String autoNumber1 = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                            if (autoNumber != null) {
                                autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            mroObjectFile.setFileNo(autoNumber1);
                            mroObjectFile.setFileType("FILE");
                            mroObjectFile = mroObjectFileRepository.save(mroObjectFile);
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(mroObjectFile.getObject(), objectType, mroObjectFile.getId()));
                            PLMFile plmFile = fileRepository.findOne(mroObjectFile.getId());
                            plmFile.setOldVersion(plmFile.getVersion());
                            fileList.add(plmFile);
                            mroObjectFile.setOldVersion(mroObjectFile.getVersion());

                            String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + itemFileService.getCopyFilePath(file);
                            String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDirectory = new File(directory);
                            if (!fDirectory.exists()) {
                                fDirectory.mkdirs();
                            }
                            String dir = "";
                            if (mroObjectFile.getParentFile() != null) {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + getParentFileSystemPath(id, mroObjectFile.getId(), objectType);
                            } else {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + id + File.separator + mroObjectFile.getId();
                            }
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                try {
                                    fDir.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            FileInputStream instream = null;
                            FileOutputStream outstream = null;
                            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        }
                    } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
                        PGCObjectFile pgcObjectFile = new PGCObjectFile();
                        PGCObjectFile existFile = null;
                        if (fileId != 0) {
                            pgcObjectFile.setParentFile(fileId);
                            existFile = pgcObjectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = pgcObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            pgcObjectFile.setName(file.getName());
                            pgcObjectFile.setDescription(file.getDescription());
                            pgcObjectFile.setObject(id);
                            pgcObjectFile.setVersion(1);
                            pgcObjectFile.setSize(file.getSize());
                            pgcObjectFile.setLatest(file.getLatest());
                            String autoNumber1 = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                            if (autoNumber != null) {
                                autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            pgcObjectFile.setFileNo(autoNumber1);
                            pgcObjectFile.setFileType("FILE");
                            pgcObjectFile = pgcObjectFileRepository.save(pgcObjectFile);
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(pgcObjectFile.getObject(), objectType, pgcObjectFile.getId()));
                            PLMFile plmFile = fileRepository.findOne(pgcObjectFile.getId());
                            plmFile.setOldVersion(plmFile.getVersion());
                            fileList.add(plmFile);
                            pgcObjectFile.setOldVersion(pgcObjectFile.getVersion());

                            String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + itemFileService.getCopyFilePath(file);
                            String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDirectory = new File(directory);
                            if (!fDirectory.exists()) {
                                fDirectory.mkdirs();
                            }
                            String dir = "";
                            if (pgcObjectFile.getParentFile() != null) {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + getParentFileSystemPath(id, pgcObjectFile.getId(), objectType);
                            } else {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + id + File.separator + pgcObjectFile.getId();
                            }
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                try {
                                    fDir.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            FileInputStream instream = null;
                            FileOutputStream outstream = null;
                            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        }
                    } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
                        PQMSupplierAuditFile supplierAuditFile = new PQMSupplierAuditFile();
                        PQMSupplierAuditFile existFile = null;
                        if (fileId != 0) {
                            supplierAuditFile.setParentFile(fileId);
                            existFile = supplierAuditFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndSupplierAuditAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = supplierAuditFileRepository.findBySupplierAuditAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            supplierAuditFile.setName(file.getName());
                            supplierAuditFile.setDescription(file.getDescription());
                            supplierAuditFile.setSupplierAudit(id);
                            supplierAuditFile.setVersion(1);
                            supplierAuditFile.setSize(file.getSize());
                            supplierAuditFile.setLatest(file.getLatest());
                            String autoNumber1 = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                            if (autoNumber != null) {
                                autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            supplierAuditFile.setFileNo(autoNumber1);
                            supplierAuditFile.setFileType("FILE");
                            supplierAuditFile = supplierAuditFileRepository.save(supplierAuditFile);
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(supplierAuditFile.getSupplierAudit(), objectType, supplierAuditFile.getId()));
                            PLMFile plmFile = fileRepository.findOne(supplierAuditFile.getId());
                            plmFile.setOldVersion(plmFile.getVersion());
                            fileList.add(plmFile);
                            supplierAuditFile.setOldVersion(supplierAuditFile.getVersion());

                            String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + itemFileService.getCopyFilePath(file);
                            String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDirectory = new File(directory);
                            if (!fDirectory.exists()) {
                                fDirectory.mkdirs();
                            }
                            String dir = "";
                            if (supplierAuditFile.getParentFile() != null) {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + getParentFileSystemPath(id, supplierAuditFile.getId(), objectType);
                            } else {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + id + File.separator + supplierAuditFile.getId();
                            }
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                try {
                                    fDir.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            FileInputStream instream = null;
                            FileOutputStream outstream = null;
                            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        }
                    } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                        PQMPPAPChecklist pqmppapChecklist = new PQMPPAPChecklist();
                        PQMPPAPChecklist existFile = null;
                        if (fileId != 0) {
                            pqmppapChecklist.setParentFile(fileId);
                            existFile = ppapChecklistRepository.findByNameEqualsIgnoreCaseAndParentFileAndPpapAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = ppapChecklistRepository.findByPpapAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            pqmppapChecklist.setName(file.getName());
                            pqmppapChecklist.setDescription(file.getDescription());
                            pqmppapChecklist.setPpap(id);
                            pqmppapChecklist.setVersion(1);
                            pqmppapChecklist.setSize(file.getSize());
                            pqmppapChecklist.setLatest(file.getLatest());
                            String autoNumber1 = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                            if (autoNumber != null) {
                                autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            pqmppapChecklist.setFileNo(autoNumber1);
                            pqmppapChecklist.setFileType("FILE");
                            pqmppapChecklist.setLifeCyclePhase(setLifecyclePhase());
                            pqmppapChecklist.setRevision(setRevisionAndLifecyclePhase());
                            pqmppapChecklist = ppapChecklistRepository.save(pqmppapChecklist);
                            objectFileDto.getObjectFiles().add(convertFileIdToDto(pqmppapChecklist.getPpap(), objectType, pqmppapChecklist.getId()));
                            PLMFile plmFile = fileRepository.findOne(pqmppapChecklist.getId());
                            plmFile.setOldVersion(plmFile.getVersion());
                            fileList.add(plmFile);
                            pqmppapChecklist.setOldVersion(pqmppapChecklist.getVersion());

                            String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + itemFileService.getCopyFilePath(file);
                            String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDirectory = new File(directory);
                            if (!fDirectory.exists()) {
                                fDirectory.mkdirs();
                            }
                            String dir = "";
                            if (pqmppapChecklist.getParentFile() != null) {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + getParentFileSystemPath(id, pqmppapChecklist.getId(), objectType);
                            } else {
                                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                        "filesystem" + File.separator + id + File.separator + pqmppapChecklist.getId();
                            }
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                try {
                                    fDir.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            FileInputStream instream = null;
                            FileOutputStream outstream = null;
                            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                        }
                    }
                } else if (file.getFileType().equals("FOLDER") && canCreate) {
                    if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
                        PQMInspectionPlanFile inspectionPlanFile = new PQMInspectionPlanFile();
                        PQMInspectionPlanFile existFile = null;
                        if (fileId != 0) {
                            inspectionPlanFile.setParentFile(fileId);
                            existFile = inspectionPlanFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndInspectionPlanAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = inspectionPlanFileRepository.findByInspectionPlanAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            inspectionPlanFile.setName(file.getName());
                            inspectionPlanFile.setDescription(file.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            inspectionPlanFile.setVersion(1);
                            inspectionPlanFile.setSize(0L);
                            inspectionPlanFile.setInspectionPlan(id);
                            inspectionPlanFile.setFileNo(folderNumber);
                            inspectionPlanFile.setFileType("FOLDER");
                            inspectionPlanFile = inspectionPlanFileRepository.save(inspectionPlanFile);
                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(id, inspectionPlanFile.getId(), objectType);
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            fileList.add(inspectionPlanFile);
                            copyFolderFiles(id, file.getParentObject(), file.getId(), inspectionPlanFile.getId());
                        }
                    } else if (objectType.equals(PLMObjectType.INSPECTION)) {
                        PQMInspectionFile inspectionFile = new PQMInspectionFile();
                        PQMInspectionFile existFile = null;
                        if (fileId != 0) {
                            inspectionFile.setParentFile(fileId);
                            existFile = inspectionFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndInspectionAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = inspectionFileRepository.findByInspectionAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            inspectionFile.setName(file.getName());
                            inspectionFile.setDescription(file.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            inspectionFile.setVersion(1);
                            inspectionFile.setSize(0L);
                            inspectionFile.setInspection(id);
                            inspectionFile.setFileNo(folderNumber);
                            inspectionFile.setFileType("FOLDER");
                            inspectionFile = inspectionFileRepository.save(inspectionFile);
                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(id, inspectionFile.getId(), objectType);
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            fileList.add(inspectionFile);
                            copyFolderFiles(id, file.getParentObject(), file.getId(), inspectionFile.getId());
                        }
                    } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
                        PQMProblemReportFile problemReportFile = new PQMProblemReportFile();
                        PQMProblemReportFile existFile = null;
                        if (fileId != 0) {
                            problemReportFile.setParentFile(fileId);
                            existFile = problemReportFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndProblemReportAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = problemReportFileRepository.findByProblemReportAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            problemReportFile.setName(file.getName());
                            problemReportFile.setDescription(file.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            problemReportFile.setVersion(1);
                            problemReportFile.setSize(0L);
                            problemReportFile.setProblemReport(id);
                            problemReportFile.setFileNo(folderNumber);
                            problemReportFile.setFileType("FOLDER");
                            problemReportFile = problemReportFileRepository.save(problemReportFile);
                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(id, problemReportFile.getId(), objectType);
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            fileList.add(problemReportFile);
                            copyFolderFiles(id, file.getParentObject(), file.getId(), problemReportFile.getId());
                        }
                    } else if (objectType.equals(PLMObjectType.NCR)) {
                        PQMNCRFile pqmncrFile = new PQMNCRFile();
                        PQMNCRFile existFile = null;
                        if (fileId != 0) {
                            pqmncrFile.setParentFile(fileId);
                            existFile = ncrFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndNcrAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = ncrFileRepository.findByNcrAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            pqmncrFile.setName(file.getName());
                            pqmncrFile.setDescription(file.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            pqmncrFile.setVersion(1);
                            pqmncrFile.setSize(0L);
                            pqmncrFile.setNcr(id);
                            pqmncrFile.setFileNo(folderNumber);
                            pqmncrFile.setFileType("FOLDER");
                            pqmncrFile = ncrFileRepository.save(pqmncrFile);
                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(id, pqmncrFile.getId(), objectType);
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            fileList.add(pqmncrFile);
                            copyFolderFiles(id, file.getParentObject(), file.getId(), pqmncrFile.getId());
                        }
                    } else if (objectType.equals(PLMObjectType.QCR)) {
                        PQMQCRFile pqmncrFile = new PQMQCRFile();
                        PQMQCRFile existFile = null;
                        if (fileId != 0) {
                            pqmncrFile.setParentFile(fileId);
                            existFile = qcrFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndQcrAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = qcrFileRepository.findByQcrAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            pqmncrFile.setName(file.getName());
                            pqmncrFile.setDescription(file.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            pqmncrFile.setVersion(1);
                            pqmncrFile.setSize(0L);
                            pqmncrFile.setQcr(id);
                            pqmncrFile.setFileNo(folderNumber);
                            pqmncrFile.setFileType("FOLDER");
                            pqmncrFile = qcrFileRepository.save(pqmncrFile);
                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(id, pqmncrFile.getId(), objectType);
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            fileList.add(pqmncrFile);
                            copyFolderFiles(id, file.getParentObject(), file.getId(), pqmncrFile.getId());
                        }
                    } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
                        MESObjectFile mesObjectFile = new MESObjectFile();
                        MESObjectFile existFile = null;
                        if (fileId != 0) {
                            mesObjectFile.setParentFile(fileId);
                            existFile = mesObjectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = mesObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            mesObjectFile.setName(file.getName());
                            mesObjectFile.setDescription(file.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            mesObjectFile.setVersion(1);
                            mesObjectFile.setSize(0L);
                            mesObjectFile.setObject(id);
                            mesObjectFile.setFileNo(folderNumber);
                            mesObjectFile.setFileType("FOLDER");
                            mesObjectFile = mesObjectFileRepository.save(mesObjectFile);
                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(id, mesObjectFile.getId(), objectType);
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            fileList.add(mesObjectFile);
                            copyFolderFiles(id, file.getParentObject(), file.getId(), mesObjectFile.getId());
                        }
                    } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
                        PLMDocument document = new PLMDocument();
                        PLMDocument existFile = null;
                        if (fileId != 0) {
                            document.setParentFile(fileId);
                            existFile = plmDocumentRepository.findByNameEqualsIgnoreCaseAndParentFileAndLatestTrue(file.getName(), fileId);
                        } else {
                            existFile = plmDocumentRepository.findByNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(file.getName());
                        }
                        if (existFile == null) {
                            document.setName(file.getName());
                            document.setDescription(file.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            document.setVersion(1);
                            document.setSize(0L);
                            document.setFileNo(folderNumber);
                            document.setFileType("FOLDER");
                            document = plmDocumentRepository.save(document);
                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(id, document.getId(), objectType);
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            fileList.add(document);
                            copyFolderFiles(id, file.getParentObject(), file.getId(), document.getId());
                        }
                    } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                        PLMMfrPartInspectionReport mfrPartInspectionReport = new PLMMfrPartInspectionReport();
                        PLMMfrPartInspectionReport existFile = null;
                        if (fileId != 0) {
                            mfrPartInspectionReport.setParentFile(fileId);
                            existFile = mfrPartInspectionReportRepository.findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileAndLatestTrue(id, file.getName(), fileId);
                        } else {
                            existFile = mfrPartInspectionReportRepository.findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            mfrPartInspectionReport.setName(file.getName());
                            mfrPartInspectionReport.setDescription(file.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            mfrPartInspectionReport.setVersion(1);
                            mfrPartInspectionReport.setSize(0L);
                            mfrPartInspectionReport.setManufacturerPart(id);
                            mfrPartInspectionReport.setFileNo(folderNumber);
                            mfrPartInspectionReport.setFileType("FOLDER");
                            mfrPartInspectionReport = mfrPartInspectionReportRepository.save(mfrPartInspectionReport);
                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(id, mfrPartInspectionReport.getId(), objectType);
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            fileList.add(mfrPartInspectionReport);
                            copyFolderFiles(id, file.getParentObject(), file.getId(), mfrPartInspectionReport.getId());
                        }
                    } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
                        MROObjectFile mroObjectFile = new MROObjectFile();
                        MROObjectFile existFile = null;
                        if (fileId != 0) {
                            mroObjectFile.setParentFile(fileId);
                            existFile = mroObjectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = mroObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            mroObjectFile.setName(file.getName());
                            mroObjectFile.setDescription(file.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            mroObjectFile.setVersion(1);
                            mroObjectFile.setSize(0L);
                            mroObjectFile.setObject(id);
                            mroObjectFile.setFileNo(folderNumber);
                            mroObjectFile.setFileType("FOLDER");
                            mroObjectFile = mroObjectFileRepository.save(mroObjectFile);
                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(id, mroObjectFile.getId(), objectType);
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            fileList.add(mroObjectFile);
                            copyFolderFiles(id, file.getParentObject(), file.getId(), mroObjectFile.getId());
                        }
                    } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
                        PGCObjectFile pgcObjectFile = new PGCObjectFile();
                        PGCObjectFile existFile = null;
                        if (fileId != 0) {
                            pgcObjectFile.setParentFile(fileId);
                            existFile = pgcObjectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = pgcObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            pgcObjectFile.setName(file.getName());
                            pgcObjectFile.setDescription(file.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            pgcObjectFile.setVersion(1);
                            pgcObjectFile.setSize(0L);
                            pgcObjectFile.setObject(id);
                            pgcObjectFile.setFileNo(folderNumber);
                            pgcObjectFile.setFileType("FOLDER");
                            pgcObjectFile = pgcObjectFileRepository.save(pgcObjectFile);
                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(id, pgcObjectFile.getId(), objectType);
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            fileList.add(pgcObjectFile);
                            copyFolderFiles(id, file.getParentObject(), file.getId(), pgcObjectFile.getId());
                        }
                    } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
                        PQMSupplierAuditFile supplierAuditFile = new PQMSupplierAuditFile();
                        PQMSupplierAuditFile existFile = null;
                        if (fileId != 0) {
                            supplierAuditFile.setParentFile(fileId);
                            existFile = supplierAuditFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndSupplierAuditAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = supplierAuditFileRepository.findBySupplierAuditAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            supplierAuditFile.setName(file.getName());
                            supplierAuditFile.setDescription(file.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            supplierAuditFile.setVersion(1);
                            supplierAuditFile.setSize(0L);
                            supplierAuditFile.setSupplierAudit(id);
                            supplierAuditFile.setFileNo(folderNumber);
                            supplierAuditFile.setFileType("FOLDER");
                            supplierAuditFile = supplierAuditFileRepository.save(supplierAuditFile);
                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(id, supplierAuditFile.getId(), objectType);
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            fileList.add(supplierAuditFile);
                            copyFolderFiles(id, file.getParentObject(), file.getId(), supplierAuditFile.getId());
                        }
                    } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                        PQMPPAPChecklist pqmppapChecklist = new PQMPPAPChecklist();
                        PQMPPAPChecklist existFile = null;
                        if (fileId != 0) {
                            pqmppapChecklist.setParentFile(fileId);
                            existFile = ppapChecklistRepository.findByNameEqualsIgnoreCaseAndParentFileAndPpapAndLatestTrue(file.getName(), fileId, id);
                        } else {
                            existFile = ppapChecklistRepository.findByPpapAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                        }
                        if (existFile == null) {
                            pqmppapChecklist.setName(file.getName());
                            pqmppapChecklist.setDescription(file.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            pqmppapChecklist.setVersion(1);
                            pqmppapChecklist.setSize(0L);
                            pqmppapChecklist.setPpap(id);
                            pqmppapChecklist.setFileNo(folderNumber);
                            pqmppapChecklist.setFileType("FOLDER");
                            pqmppapChecklist = ppapChecklistRepository.save(pqmppapChecklist);
                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + id;
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(id, pqmppapChecklist.getId(), objectType);
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            fileList.add(pqmppapChecklist);
                            copyFolderFiles(id, file.getParentObject(), file.getId(), pqmppapChecklist.getId());
                        }
                    }
                }
            }
        } else if (objectType.equals(PLMObjectType.ITEM)) {
            List<PLMItemFile> itemFiles = itemFileService.pasteFromClipboard(id, fileId, normalFiles);
            itemFiles.forEach(plmItemFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(plmItemFile.getItem().getId(), objectType, plmItemFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.PROJECT)) {
            List<PLMProjectFile> projectFiles = projectService.pasteFromClipboard(id, fileId, normalFiles);
            projectFiles.forEach(projectFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(projectFile.getProject(), objectType, projectFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
            List<PLMManufacturerFile> manufacturerFiles = manufacturerFileService.pasteFromClipboard(id, fileId, normalFiles);
            manufacturerFiles.forEach(manufacturerFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(manufacturerFile.getManufacturer(), objectType, manufacturerFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
            List<PLMManufacturerPartFile> manufacturerPartFiles = manufacturerPartFileService.pasteFromClipboard(id, fileId, normalFiles);
            manufacturerPartFiles.forEach(manufacturerPartFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(manufacturerPartFile.getManufacturerPart(), objectType, manufacturerPartFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            List<PLMActivityFile> activityFiles = activityService.pasteFromClipboardToActivity(id, fileId, normalFiles);
            activityFiles.forEach(activityFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(activityFile.getActivity(), objectType, activityFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            List<PLMTaskFile> taskFiles = activityService.pasteFromClipboardToTask(id, fileId, normalFiles);
            taskFiles.forEach(taskFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(taskFile.getTask(), objectType, taskFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
            List<PLMGlossaryFile> glossaryFiles = glossaryService.pasteFromClipboard(id, fileId, normalFiles);
            glossaryFiles.forEach(glossaryFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(glossaryFile.getGlossary(), objectType, glossaryFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
            List<PLMSupplierFile> supplierFiles = supplierFileService.pasteFromClipboard(id, fileId, normalFiles);
            supplierFiles.forEach(supplierFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(supplierFile.getSupplier(), objectType, supplierFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
            List<PQMCustomerFile> customerFiles = customerFileService.pasteFromClipboard(id, fileId, normalFiles);
            customerFiles.forEach(customerFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(customerFile.getCustomer(), objectType, customerFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
            List<PLMRequirementDocumentFile> documentFiles = reqDocumentFileService.pasteFromClipboard(id, fileId, normalFiles);
            documentFiles.forEach(documentFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(documentFile.getDocumentRevision().getId(), objectType, documentFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
            List<PLMRequirementFile> requirementFiles = requirementFileService.pasteFromClipboard(id, fileId, normalFiles);
            requirementFiles.forEach(requirementFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(requirementFile.getRequirement().getId(), objectType, requirementFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.PLMNPR)) {
            List<PLMNprFile> nprFiles = nprFileService.pasteFromClipboard(id, fileId, normalFiles);
            nprFiles.forEach(nprFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(nprFile.getNpr(), objectType, nprFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
            List<MESMBOMFile> mesmbomFiles = mbomFileService.pasteFromClipboard(id, fileId, normalFiles);
            mesmbomFiles.forEach(mesmbomFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(mesmbomFile.getMbomRevision(), objectType, mesmbomFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
            List<MESMBOMInstanceFile> mesmbomFiles = mbomInstanceFileService.pasteFromClipboard(id, fileId, normalFiles);
            mesmbomFiles.forEach(mesmbomFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(mesmbomFile.getMbomInstance(), objectType, mesmbomFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
            List<MESBOPFile> mesbopFiles = bopFileService.pasteFromClipboard(id, fileId, normalFiles);
            mesbopFiles.forEach(mesbopFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(mesbopFile.getBop(), objectType, mesbopFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
            List<MESBOPOperationFile> mesbopFiles = bopPlanFileService.pasteFromClipboard(id, fileId, normalFiles);
            mesbopFiles.forEach(mesbopFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(mesbopFile.getBopOperation(), objectType, mesbopFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
            List<MESBOPInstanceOperationFile> mesbopFiles = bopInstanceOperationFileService.pasteFromClipboard(id, fileId, normalFiles);
            mesbopFiles.forEach(mesbopFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(mesbopFile.getBopOperation(), objectType, mesbopFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            List<PLMProgramFile> programFiles = programFileService.pasteFromClipboard(id, fileId, normalFiles);
            programFiles.forEach(programFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(programFile.getProgram(), objectType, programFile.getId()));
            });
        } else if (objectType.equals(PLMObjectType.CHANGE)) {
            List<PLMChangeFile> changeFiles = changeFileService.pasteFromClipboard(id, fileId, normalFiles);
            changeFiles.forEach(changeFile -> {
                objectFileDto.getObjectFiles().add(convertFileIdToDto(changeFile.getChange(), objectType, changeFile.getId()));
            });
        }

        objectDocumentList.forEach(objectDocument -> {
            objectFileDto.getObjectFiles().add(convertObjectDocumentToDto(objectDocument));
        });

        return objectFileDto;
    }

    @Transactional
    public void copyFolderFiles(Integer id, PLMObjectType objectType, Integer file, Integer parent) {
        if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
            List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
            plmFiles.forEach(plmFile -> {
                PQMInspectionPlanFile inspectionPlanFile = new PQMInspectionPlanFile();
                inspectionPlanFile.setParentFile(parent);
                inspectionPlanFile.setName(plmFile.getName());
                inspectionPlanFile.setDescription(plmFile.getDescription());
                inspectionPlanFile.setInspectionPlan(id);
                String folderNumber = null;
                if (plmFile.getFileType().equals("FILE")) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    inspectionPlanFile.setVersion(1);
                    inspectionPlanFile.setFileNo(folderNumber);
                    inspectionPlanFile.setSize(plmFile.getSize());
                    inspectionPlanFile.setFileType("FILE");
                    inspectionPlanFile = inspectionPlanFileRepository.save(inspectionPlanFile);
                    inspectionPlanFile.setParentObject(PLMObjectType.INSPECTIONPLAN);
                    plmFile.setParentObject(objectType);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(plmFile);

                    String dir = "";
                    if (inspectionPlanFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(id, inspectionPlanFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id + File.separator + inspectionPlanFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                } else {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    inspectionPlanFile.setVersion(1);
                    inspectionPlanFile.setSize(0L);
                    inspectionPlanFile.setFileNo(folderNumber);
                    inspectionPlanFile.setFileType("FOLDER");
                    inspectionPlanFile = inspectionPlanFileRepository.save(inspectionPlanFile);

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, inspectionPlanFile.getId(), objectType);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    copyFolderFiles(id, objectType, plmFile.getId(), inspectionPlanFile.getId());
                }
            });
        } else if (objectType.equals(PLMObjectType.INSPECTION)) {
            List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
            plmFiles.forEach(plmFile -> {
                PQMInspectionFile inspectionFile = new PQMInspectionFile();
                inspectionFile.setParentFile(parent);
                inspectionFile.setName(plmFile.getName());
                inspectionFile.setDescription(plmFile.getDescription());
                inspectionFile.setInspection(id);
                String folderNumber = null;
                if (plmFile.getFileType().equals("FILE")) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    inspectionFile.setVersion(1);
                    inspectionFile.setFileNo(folderNumber);
                    inspectionFile.setSize(plmFile.getSize());
                    inspectionFile.setFileType("FILE");
                    inspectionFile = inspectionFileRepository.save(inspectionFile);
                    inspectionFile.setParentObject(PLMObjectType.INSPECTION);
                    plmFile.setParentObject(objectType);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(plmFile);

                    String dir = "";
                    if (inspectionFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(id, inspectionFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id + File.separator + inspectionFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                } else {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    inspectionFile.setVersion(1);
                    inspectionFile.setSize(0L);
                    inspectionFile.setFileNo(folderNumber);
                    inspectionFile.setFileType("FOLDER");
                    inspectionFile = inspectionFileRepository.save(inspectionFile);

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, inspectionFile.getId(), objectType);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    copyFolderFiles(id, objectType, plmFile.getId(), inspectionFile.getId());
                }
            });
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
            plmFiles.forEach(plmFile -> {
                PQMProblemReportFile problemReportFile = new PQMProblemReportFile();
                problemReportFile.setParentFile(parent);
                problemReportFile.setName(plmFile.getName());
                problemReportFile.setDescription(plmFile.getDescription());
                problemReportFile.setProblemReport(id);
                String folderNumber = null;
                if (plmFile.getFileType().equals("FILE")) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    problemReportFile.setVersion(1);
                    problemReportFile.setFileNo(folderNumber);
                    problemReportFile.setSize(plmFile.getSize());
                    problemReportFile.setFileType("FILE");
                    problemReportFile = problemReportFileRepository.save(problemReportFile);
                    problemReportFile.setParentObject(PLMObjectType.PROBLEMREPORT);
                    plmFile.setParentObject(objectType);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(plmFile);

                    String dir = "";
                    if (problemReportFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(id, problemReportFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id + File.separator + problemReportFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                } else {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    problemReportFile.setVersion(1);
                    problemReportFile.setSize(0L);
                    problemReportFile.setFileNo(folderNumber);
                    problemReportFile.setFileType("FOLDER");
                    problemReportFile = problemReportFileRepository.save(problemReportFile);

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, problemReportFile.getId(), objectType);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    copyFolderFiles(id, objectType, plmFile.getId(), problemReportFile.getId());
                }
            });
        } else if (objectType.equals(PLMObjectType.NCR)) {
            List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
            plmFiles.forEach(plmFile -> {
                PQMNCRFile pqmncrFile = new PQMNCRFile();
                pqmncrFile.setParentFile(parent);
                pqmncrFile.setName(plmFile.getName());
                pqmncrFile.setDescription(plmFile.getDescription());
                pqmncrFile.setNcr(id);
                String folderNumber = null;
                if (plmFile.getFileType().equals("FILE")) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    pqmncrFile.setVersion(1);
                    pqmncrFile.setFileNo(folderNumber);
                    pqmncrFile.setSize(plmFile.getSize());
                    pqmncrFile.setFileType("FILE");
                    pqmncrFile = ncrFileRepository.save(pqmncrFile);
                    pqmncrFile.setParentObject(PLMObjectType.NCR);
                    plmFile.setParentObject(objectType);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(plmFile);

                    String dir = "";
                    if (pqmncrFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(id, pqmncrFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id + File.separator + pqmncrFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                } else {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    pqmncrFile.setVersion(1);
                    pqmncrFile.setSize(0L);
                    pqmncrFile.setFileNo(folderNumber);
                    pqmncrFile.setFileType("FOLDER");
                    pqmncrFile = ncrFileRepository.save(pqmncrFile);

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, pqmncrFile.getId(), objectType);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    copyFolderFiles(id, objectType, plmFile.getId(), pqmncrFile.getId());
                }
            });
        } else if (objectType.equals(PLMObjectType.QCR)) {
            List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
            plmFiles.forEach(plmFile -> {
                PQMQCRFile pqmqcrFile = new PQMQCRFile();
                pqmqcrFile.setParentFile(parent);
                pqmqcrFile.setName(plmFile.getName());
                pqmqcrFile.setDescription(plmFile.getDescription());
                pqmqcrFile.setQcr(id);
                String folderNumber = null;
                if (plmFile.getFileType().equals("FILE")) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    pqmqcrFile.setVersion(1);
                    pqmqcrFile.setFileNo(folderNumber);
                    pqmqcrFile.setSize(plmFile.getSize());
                    pqmqcrFile.setFileType("FILE");
                    pqmqcrFile = qcrFileRepository.save(pqmqcrFile);
                    pqmqcrFile.setParentObject(PLMObjectType.QCR);
                    plmFile.setParentObject(objectType);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(plmFile);

                    String dir = "";
                    if (pqmqcrFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(id, pqmqcrFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id + File.separator + pqmqcrFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                } else {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    pqmqcrFile.setVersion(1);
                    pqmqcrFile.setSize(0L);
                    pqmqcrFile.setFileNo(folderNumber);
                    pqmqcrFile.setFileType("FOLDER");
                    pqmqcrFile = qcrFileRepository.save(pqmqcrFile);

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, pqmqcrFile.getId(), objectType);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    copyFolderFiles(id, objectType, plmFile.getId(), pqmqcrFile.getId());
                }
            });
        } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
            List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
            plmFiles.forEach(plmFile -> {
                MESObjectFile mesObjectFile = new MESObjectFile();
                mesObjectFile.setParentFile(parent);
                mesObjectFile.setName(plmFile.getName());
                mesObjectFile.setDescription(plmFile.getDescription());
                mesObjectFile.setObject(id);
                String folderNumber = null;
                if (plmFile.getFileType().equals("FILE")) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    mesObjectFile.setVersion(1);
                    mesObjectFile.setFileNo(folderNumber);
                    mesObjectFile.setSize(plmFile.getSize());
                    mesObjectFile.setFileType("FILE");
                    mesObjectFile = mesObjectFileRepository.save(mesObjectFile);
                    mesObjectFile.setParentObject(PLMObjectType.MESOBJECT);
                    plmFile.setParentObject(objectType);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(plmFile);

                    String dir = "";
                    if (mesObjectFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(id, mesObjectFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id + File.separator + mesObjectFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                } else {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    mesObjectFile.setVersion(1);
                    mesObjectFile.setSize(0L);
                    mesObjectFile.setFileNo(folderNumber);
                    mesObjectFile.setFileType("FOLDER");
                    mesObjectFile = mesObjectFileRepository.save(mesObjectFile);

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, mesObjectFile.getId(), objectType);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    copyFolderFiles(id, objectType, plmFile.getId(), mesObjectFile.getId());
                }
            });
        } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
            List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
            plmFiles.forEach(plmFile -> {
                PLMDocument document = new PLMDocument();
                document.setParentFile(parent);
                document.setName(plmFile.getName());
                document.setDescription(plmFile.getDescription());
                String folderNumber = null;
                if (plmFile.getFileType().equals("FILE")) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    document.setVersion(1);
                    document.setFileNo(folderNumber);
                    document.setSize(plmFile.getSize());
                    document.setFileType("FILE");
                    document = plmDocumentRepository.save(document);
                    document.setParentObject(PLMObjectType.DOCUMENT);
                    plmFile.setParentObject(objectType);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(plmFile);

                    String dir = "";
                    if (document.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(id, document.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id + File.separator + document.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                } else {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    document.setVersion(1);
                    document.setSize(0L);
                    document.setFileNo(folderNumber);
                    document.setFileType("FOLDER");
                    document = plmDocumentRepository.save(document);

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, document.getId(), objectType);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    copyFolderFiles(id, objectType, plmFile.getId(), document.getId());
                }
            });
        } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
            plmFiles.forEach(plmFile -> {
                PLMMfrPartInspectionReport mfrPartInspectionReport = new PLMMfrPartInspectionReport();
                mfrPartInspectionReport.setParentFile(parent);
                mfrPartInspectionReport.setName(plmFile.getName());
                mfrPartInspectionReport.setDescription(plmFile.getDescription());
                mfrPartInspectionReport.setManufacturerPart(id);
                String folderNumber = null;
                if (plmFile.getFileType().equals("FILE")) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    mfrPartInspectionReport.setVersion(1);
                    mfrPartInspectionReport.setFileNo(folderNumber);
                    mfrPartInspectionReport.setSize(plmFile.getSize());
                    mfrPartInspectionReport.setFileType("FILE");
                    mfrPartInspectionReport = mfrPartInspectionReportRepository.save(mfrPartInspectionReport);
                    mfrPartInspectionReport.setParentObject(PLMObjectType.MFRPARTINSPECTIONREPORT);
                    plmFile.setParentObject(objectType);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(plmFile);

                    String dir = "";
                    if (mfrPartInspectionReport.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(id, mfrPartInspectionReport.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id + File.separator + mfrPartInspectionReport.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                } else {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    mfrPartInspectionReport.setVersion(1);
                    mfrPartInspectionReport.setSize(0L);
                    mfrPartInspectionReport.setFileNo(folderNumber);
                    mfrPartInspectionReport.setFileType("FOLDER");
                    mfrPartInspectionReport = mfrPartInspectionReportRepository.save(mfrPartInspectionReport);

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, mfrPartInspectionReport.getId(), objectType);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    copyFolderFiles(id, objectType, plmFile.getId(), mfrPartInspectionReport.getId());
                }
            });
        } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
            List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
            plmFiles.forEach(plmFile -> {
                MROObjectFile mroObjectFile = new MROObjectFile();
                mroObjectFile.setParentFile(parent);
                mroObjectFile.setName(plmFile.getName());
                mroObjectFile.setDescription(plmFile.getDescription());
                mroObjectFile.setObject(id);
                String folderNumber = null;
                if (plmFile.getFileType().equals("FILE")) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    mroObjectFile.setVersion(1);
                    mroObjectFile.setFileNo(folderNumber);
                    mroObjectFile.setSize(plmFile.getSize());
                    mroObjectFile.setFileType("FILE");
                    mroObjectFile = mroObjectFileRepository.save(mroObjectFile);
                    mroObjectFile.setParentObject(PLMObjectType.MROOBJECT);
                    plmFile.setParentObject(objectType);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(plmFile);

                    String dir = "";
                    if (mroObjectFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(id, mroObjectFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id + File.separator + mroObjectFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                } else {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    mroObjectFile.setVersion(1);
                    mroObjectFile.setSize(0L);
                    mroObjectFile.setFileNo(folderNumber);
                    mroObjectFile.setFileType("FOLDER");
                    mroObjectFile = mroObjectFileRepository.save(mroObjectFile);

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, mroObjectFile.getId(), objectType);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    copyFolderFiles(id, objectType, plmFile.getId(), mroObjectFile.getId());
                }
            });
        } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
            List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
            plmFiles.forEach(plmFile -> {
                PGCObjectFile pgcObjectFile = new PGCObjectFile();
                pgcObjectFile.setParentFile(parent);
                pgcObjectFile.setName(plmFile.getName());
                pgcObjectFile.setDescription(plmFile.getDescription());
                pgcObjectFile.setObject(id);
                String folderNumber = null;
                if (plmFile.getFileType().equals("FILE")) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    pgcObjectFile.setVersion(1);
                    pgcObjectFile.setFileNo(folderNumber);
                    pgcObjectFile.setSize(plmFile.getSize());
                    pgcObjectFile.setFileType("FILE");
                    pgcObjectFile = pgcObjectFileRepository.save(pgcObjectFile);
                    pgcObjectFile.setParentObject(PLMObjectType.PGCOBJECT);
                    plmFile.setParentObject(objectType);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(plmFile);

                    String dir = "";
                    if (pgcObjectFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(id, pgcObjectFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id + File.separator + pgcObjectFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                } else {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    pgcObjectFile.setVersion(1);
                    pgcObjectFile.setSize(0L);
                    pgcObjectFile.setFileNo(folderNumber);
                    pgcObjectFile.setFileType("FOLDER");
                    pgcObjectFile = pgcObjectFileRepository.save(pgcObjectFile);

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, pgcObjectFile.getId(), objectType);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    copyFolderFiles(id, objectType, plmFile.getId(), pgcObjectFile.getId());
                }
            });
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
            plmFiles.forEach(plmFile -> {
                PQMSupplierAuditFile supplierAuditFile = new PQMSupplierAuditFile();
                supplierAuditFile.setParentFile(parent);
                supplierAuditFile.setName(plmFile.getName());
                supplierAuditFile.setDescription(plmFile.getDescription());
                supplierAuditFile.setSupplierAudit(id);
                String folderNumber = null;
                if (plmFile.getFileType().equals("FILE")) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    supplierAuditFile.setVersion(1);
                    supplierAuditFile.setFileNo(folderNumber);
                    supplierAuditFile.setSize(plmFile.getSize());
                    supplierAuditFile.setFileType("FILE");
                    supplierAuditFile = supplierAuditFileRepository.save(supplierAuditFile);
                    supplierAuditFile.setParentObject(PLMObjectType.SUPPLIERAUDIT);
                    plmFile.setParentObject(objectType);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(plmFile);

                    String dir = "";
                    if (supplierAuditFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(id, supplierAuditFile.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id + File.separator + supplierAuditFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                } else {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    supplierAuditFile.setVersion(1);
                    supplierAuditFile.setSize(0L);
                    supplierAuditFile.setFileNo(folderNumber);
                    supplierAuditFile.setFileType("FOLDER");
                    supplierAuditFile = supplierAuditFileRepository.save(supplierAuditFile);

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, supplierAuditFile.getId(), objectType);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    copyFolderFiles(id, objectType, plmFile.getId(), supplierAuditFile.getId());
                }
            });
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
            plmFiles.forEach(plmFile -> {
                PQMPPAPChecklist pqmppapChecklist = new PQMPPAPChecklist();
                pqmppapChecklist.setParentFile(parent);
                pqmppapChecklist.setName(plmFile.getName());
                pqmppapChecklist.setDescription(plmFile.getDescription());
                pqmppapChecklist.setPpap(id);
                String folderNumber = null;
                if (plmFile.getFileType().equals("FILE")) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    pqmppapChecklist.setVersion(1);
                    pqmppapChecklist.setFileNo(folderNumber);
                    pqmppapChecklist.setSize(plmFile.getSize());
                    pqmppapChecklist.setFileType("FILE");
                    pqmppapChecklist = ppapChecklistRepository.save(pqmppapChecklist);
                    pqmppapChecklist.setParentObject(PLMObjectType.PPAPCHECKLIST);
                    plmFile.setParentObject(objectType);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(plmFile);

                    String dir = "";
                    if (pqmppapChecklist.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(id, pqmppapChecklist.getId(), objectType);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id + File.separator + pqmppapChecklist.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                } else {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    pqmppapChecklist.setVersion(1);
                    pqmppapChecklist.setSize(0L);
                    pqmppapChecklist.setFileNo(folderNumber);
                    pqmppapChecklist.setFileType("FOLDER");
                    pqmppapChecklist = ppapChecklistRepository.save(pqmppapChecklist);

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, pqmppapChecklist.getId(), objectType);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    copyFolderFiles(id, objectType, plmFile.getId(), pqmppapChecklist.getId());
                }
            });
        }
    }

    @Transactional
    public void undoCopiedItemFiles(Integer id, PLMObjectType objectType, ObjectFileDto objectFileDto) {

        List<FileDto> normalFiles = new ArrayList<>();
        List<FileDto> objectDocuments = new ArrayList<>();

        objectFileDto.getObjectFiles().forEach(fileDto -> {
            if (fileDto.getObjectType().equals(PLMObjectType.OBJECTDOCUMENT)) {
                objectDocuments.add(fileDto);
            } else {
                normalFiles.add(fileDto);
            }
        });

        objectDocuments.forEach(objectDocument -> {
            objectDocumentRepository.delete(objectDocument.getId());
        });

        List<Integer> fileIds = new ArrayList<>();
        if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
            normalFiles.forEach(inspectionPlanFile -> {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, inspectionPlanFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDiskFolder(inspectionPlanFile.getId(), dir);
                inspectionPlanFileRepository.delete(inspectionPlanFile.getId());
            });
        } else if (objectType.equals(PLMObjectType.INSPECTION)) {
            normalFiles.forEach(inspectionFile -> {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, inspectionFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDiskFolder(inspectionFile.getId(), dir);
                inspectionFileRepository.delete(inspectionFile.getId());
            });
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            normalFiles.forEach(problemReportFile -> {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, problemReportFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDiskFolder(problemReportFile.getId(), dir);
                problemReportFileRepository.delete(problemReportFile.getId());
            });
        } else if (objectType.equals(PLMObjectType.NCR)) {
            normalFiles.forEach(pqmncrFile -> {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, pqmncrFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDiskFolder(pqmncrFile.getId(), dir);
                ncrFileRepository.delete(pqmncrFile.getId());
            });
        } else if (objectType.equals(PLMObjectType.QCR)) {
            normalFiles.forEach(pqmqcrFile -> {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, pqmqcrFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDiskFolder(pqmqcrFile.getId(), dir);
                qcrFileRepository.delete(pqmqcrFile.getId());
            });
        } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
            normalFiles.forEach(mesObjectFile -> {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, mesObjectFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDiskFolder(mesObjectFile.getId(), dir);
                mesObjectFileRepository.delete(mesObjectFile.getId());
            });
        } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
            normalFiles.forEach(plmDocument -> {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + "documents" + getDocumentParentFileSystemPath(plmDocument.getId());
                fileSystemService.deleteDocumentFromDiskFolder(plmDocument.getId(), dir);
                plmDocumentRepository.delete(plmDocument.getId());
            });
        } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            normalFiles.forEach(plmDocument -> {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, plmDocument.getId(), objectType);
                fileSystemService.deleteDocumentFromDiskFolder(plmDocument.getId(), dir);
                mfrPartInspectionReportRepository.delete(plmDocument.getId());
            });
        } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
            normalFiles.forEach(mesObjectFile -> {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, mesObjectFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDiskFolder(mesObjectFile.getId(), dir);
                mroObjectFileRepository.delete(mesObjectFile.getId());
            });
        } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
            normalFiles.forEach(pgcObjectFile -> {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, pgcObjectFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDiskFolder(pgcObjectFile.getId(), dir);
                pgcObjectFileRepository.delete(pgcObjectFile.getId());
            });
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            normalFiles.forEach(pgcObjectFile -> {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, pgcObjectFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDiskFolder(pgcObjectFile.getId(), dir);
                supplierAuditFileRepository.delete(pgcObjectFile.getId());
            });
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            normalFiles.forEach(pgcObjectFile -> {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, pgcObjectFile.getId(), objectType);
                fileSystemService.deleteDocumentFromDiskFolder(pgcObjectFile.getId(), dir);
                ppapChecklistRepository.delete(pgcObjectFile.getId());
            });
        } else if (objectType.equals(PLMObjectType.ITEM)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PLMItemFile> itemFiles = itemFileRepository.findByIdIn(fileIds);
            itemFileService.undoCopiedItemFiles(id, itemFiles);
        } else if (objectType.equals(PLMObjectType.PROJECT)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PLMProjectFile> projectFiles = projectFileRepository.findByIdIn(fileIds);
            projectService.undoCopiedProjectFiles(id, projectFiles);
        } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PLMManufacturerFile> manufacturerFiles = manufacturerFileRepository.findByIdIn(fileIds);
            manufacturerFileService.undoCopiedMfrFiles(id, manufacturerFiles);
        } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PLMManufacturerPartFile> manufacturerPartFiles = manufacturerPartFileRepository.findByIdIn(fileIds);
            manufacturerPartFileService.undoCopiedMfrPartFiles(id, manufacturerPartFiles);
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PLMActivityFile> activityFiles = activityFileRepository.findByIdIn(fileIds);
            activityService.undoCopiedActivityFiles(id, activityFiles);
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PLMTaskFile> taskFiles = taskFileRepository.findByIdIn(fileIds);
            activityService.undoCopiedTaskFiles(id, taskFiles);
        } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PLMGlossaryFile> glossaryFiles = glossaryFileRepository.findByIdIn(fileIds);
            glossaryService.undoCopiedGlossaryFiles(id, glossaryFiles);
        } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PLMSupplierFile> supplierFiles = supplierFileRepository.findByIdIn(fileIds);
            supplierFileService.undoCopiedSupplierFiles(id, supplierFiles);
        } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PQMCustomerFile> customerFiles = customerFileRepository.findByIdIn(fileIds);
            customerFileService.undoCopiedCustomerFiles(id, customerFiles);
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PLMRequirementDocumentFile> documentFiles = requirementDocumentFileRepository.findByIdIn(fileIds);
            reqDocumentFileService.undoCopiedReqDocumentFiles(id, documentFiles);
        } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PLMRequirementFile> requirementFiles = requirementFileRepository.findByIdIn(fileIds);
            requirementFileService.undoCopiedRequirementFiles(id, requirementFiles);
        } else if (objectType.equals(PLMObjectType.PLMNPR)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PLMNprFile> nprFiles = nprFileRepository.findByIdIn(fileIds);
            nprFileService.undoCopiedNprFiles(id, nprFiles);
        } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<MESMBOMFile> mesmbomFiles = mbomFileRepository.findByIdIn(fileIds);
            mbomFileService.undoCopiedNprFiles(id, mesmbomFiles);
        } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<MESMBOMInstanceFile> mesmbomFiles = mbomInstanceFileRepository.findByIdIn(fileIds);
            mbomInstanceFileService.undoCopiedMBOMInstanceFiles(id, mesmbomFiles);
        } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<MESBOPFile> mesbopFiles = bopFileRepository.findByIdIn(fileIds);
            bopFileService.undoCopiedNprFiles(id, mesbopFiles);
        } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<MESBOPOperationFile> mesbopFiles = bopOperationFileRepository.findByIdIn(fileIds);
            bopPlanFileService.undoCopiedBOPPlanFiles(id, mesbopFiles);
        } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<MESBOPInstanceOperationFile> mesbopFiles = bopInstanceOperationFileRepository.findByIdIn(fileIds);
            bopInstanceOperationFileService.undoCopiedBOPPlanFiles(id, mesbopFiles);
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PLMProgramFile> plmProgramFiles = programFileRepository.findByIdIn(fileIds);
            programFileService.undoCopiedNprFiles(id, plmProgramFiles);
        } else if (objectType.equals(PLMObjectType.CHANGE)) {
            normalFiles.forEach(fileDto -> {
                fileIds.add(fileDto.getId());
            });
            List<PLMChangeFile> changeFiles = changeFileRepository.findByIdIn(fileIds);
            changeFileService.undoCopiedItemFiles(id, changeFiles);
        }
    }

    @Transactional
    public PLMFileDownloadHistory fileDownloadHistory(Integer id, PLMObjectType objectType, Integer fileId) throws JsonProcessingException {
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        CassiniObject cassiniObject = objectRepository.findById(fileId);
        if (!objectType.equals(PLMObjectType.DOCUMENT) && cassiniObject != null && (cassiniObject.getObjectType().name().equals(PLMObjectType.DOCUMENT.name()) || cassiniObject.getObjectType().name().equals(PLMObjectType.OBJECTDOCUMENT.name()))) {
            PLMFile file = null;
            if (cassiniObject.getObjectType().name().equals(PLMObjectType.DOCUMENT.name())) {
                file = fileRepository.findOne(fileId);
            } else {
                PLMObjectDocument objectDocument = objectDocumentRepository.findOne(fileId);
                file = fileRepository.findOne(objectDocument.getDocument().getId());
            }
            Person person = sessionWrapper.getSession().getLogin().getPerson();
            plmFileDownloadHistory.setFileId(file.getId());
            plmFileDownloadHistory.setPerson(person);
            plmFileDownloadHistory.setDownloadDate(new Date());
            plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
            applicationEventPublisher.publishEvent(new DocumentEvents.DocumentFileDownloadedEvent(file));
        } else {
            if (objectType.equals(PLMObjectType.INSPECTIONPLAN) || objectType.equals(PLMObjectType.INSPECTION) || objectType.equals(PLMObjectType.PROBLEMREPORT)
                    || objectType.equals(PLMObjectType.NCR) || objectType.equals(PLMObjectType.QCR) || objectType.equals(PLMObjectType.MESOBJECT) || objectType.equals(PLMObjectType.DOCUMENT)
                    || objectType.equals(PLMObjectType.MROOBJECT) || objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT) || objectType.equals(PLMObjectType.PGCOBJECT)
                    || objectType.equals(PLMObjectType.PPAPCHECKLIST) || objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
                Person person = sessionWrapper.getSession().getLogin().getPerson();
                plmFileDownloadHistory.setFileId(fileId);
                plmFileDownloadHistory.setPerson(person);
                plmFileDownloadHistory.setDownloadDate(new Date());
                plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
                if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
                    PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(id);
                    PLMFile plmFile = fileRepository.findOne(fileId);
                    applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanFileDownloadedEvent(inspectionPlanRevision, plmFile));
                } else if (objectType.equals(PLMObjectType.INSPECTION)) {
                    PQMInspection inspection = inspectionRepository.findOne(id);
                    PLMFile plmFile = fileRepository.findOne(fileId);
                    applicationEventPublisher.publishEvent(new InspectionEvents.InspectionFileDownloadedEvent(inspection, plmFile));
                } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
                    PQMProblemReport problemReport = problemReportRepository.findOne(id);
                    PLMFile plmFile = fileRepository.findOne(fileId);
                    applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportFileDownloadedEvent(problemReport, plmFile));
                } else if (objectType.equals(PLMObjectType.NCR)) {
                    PQMNCR pqmncr = ncrRepository.findOne(id);
                    PLMFile plmFile = fileRepository.findOne(fileId);
                    applicationEventPublisher.publishEvent(new NCREvents.NCRFileDownloadedEvent(pqmncr, plmFile));
                } else if (objectType.equals(PLMObjectType.QCR)) {
                    PQMQCR pqmqcr = qcrRepository.findOne(id);
                    PLMFile plmFile = fileRepository.findOne(fileId);
                    applicationEventPublisher.publishEvent(new QCREvents.QCRFileDownloadedEvent(pqmqcr, plmFile));
                } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
                    PLMFile plmFile = fileRepository.findOne(fileId);
                    MESObject mesObject = mesObjectRepository.findOne(id);
                    applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFileDownloadedEvent("mes", id, plmFile, mesObject.getObjectType()));
                } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
                    PLMFile plmFile = fileRepository.findOne(fileId);
                    applicationEventPublisher.publishEvent(new DocumentEvents.DocumentFileDownloadedEvent(plmFile));
                } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                    PLMFile plmFile = fileRepository.findOne(fileId);
                    applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartReportDownloadedEvent(id, plmFile));
                } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
                    PLMFile plmFile = fileRepository.findOne(fileId);
                    MROObject mroObject = mroObjectRepository.findOne(id);
                    applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterFileDownloadedEvent("mro", id, plmFile, mroObject.getObjectType()));
                } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
                    PLMFile plmFile = fileRepository.findOne(fileId);
                    PGCObject pgcObject = pgcObjectRepository.findOne(id);
                    applicationEventPublisher.publishEvent(new SubstanceEvents.SubstanceFileDownloadedEvent("pgc", id, plmFile, pgcObject.getObjectType()));
                } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
                    PLMFile plmFile = fileRepository.findOne(fileId);
                    PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(id);
                    applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditFileDownloadedEvent(id, plmFile));
                } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                    PLMFile plmFile = fileRepository.findOne(fileId);
                    PQMPPAP ppap = ppapRepository.findOne(id);
                    applicationEventPublisher.publishEvent(new PPAPEvents.PPAPFileDownloadedEvent(ppap, plmFile));
                }
            } else if (objectType.equals(PLMObjectType.ITEM)) {
                plmFileDownloadHistory = itemFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.PROJECT)) {
                plmFileDownloadHistory = projectService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
                plmFileDownloadHistory = manufacturerFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
                plmFileDownloadHistory = manufacturerPartFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
                plmFileDownloadHistory = activityService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
                plmFileDownloadHistory = activityService.taskFileDownloadHistory(0, id, fileId);
            } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
                plmFileDownloadHistory = glossaryService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
                plmFileDownloadHistory = supplierFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
                plmFileDownloadHistory = customerFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
                plmFileDownloadHistory = reqDocumentFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
                plmFileDownloadHistory = requirementFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.PLMNPR)) {
                plmFileDownloadHistory = nprFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
                plmFileDownloadHistory = mbomFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
                plmFileDownloadHistory = mbomInstanceFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
                plmFileDownloadHistory = bopFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
                plmFileDownloadHistory = bopPlanFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
                plmFileDownloadHistory = bopInstanceOperationFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.PROGRAM)) {
                plmFileDownloadHistory = programFileService.fileDownloadHistory(id, fileId);
            } else if (objectType.equals(PLMObjectType.CHANGE)) {
                plmFileDownloadHistory = changeFileService.fileDownloadHistory(id, fileId);
            }
        }
        return plmFileDownloadHistory;
    }

    @Transactional(readOnly = true)
    public FileDto getObjectFile(Integer id, PLMObjectType objectType, Integer fileId) {
        FileDto fileDto = convertFileIdToDto(id, objectType, fileId);
        return fileDto;
    }

    @Transactional
    public File getFile(Integer id, Integer fileId, PLMObjectType objectType) {
        checkNotNull(id);
        checkNotNull(fileId);

        PLMFile plmFile = fileRepository.findOne(fileId);
        if (plmFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = "";
        if (plmFile.getObjectType().name().equals(PLMObjectType.DOCUMENT.name())) {
            path = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + "documents" + getDocumentParentFileSystemPath(fileId);
        } else {
            path = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(id, fileId, objectType);
        }
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    @Transactional
    public void generateZipFile(Integer id, PLMObjectType objectType, HttpServletResponse response) throws IOException {
        if (objectType.equals(PLMObjectType.INSPECTIONPLAN) || objectType.equals(PLMObjectType.INSPECTION) || objectType.equals(PLMObjectType.PROBLEMREPORT)
                || objectType.equals(PLMObjectType.NCR) || objectType.equals(PLMObjectType.QCR) || objectType.equals(PLMObjectType.MESOBJECT) || objectType.equals(PLMObjectType.DOCUMENT)
                || objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT) || objectType.equals(PLMObjectType.MROOBJECT) || objectType.equals(PLMObjectType.PGCOBJECT)
                || objectType.equals(PLMObjectType.PPAPCHECKLIST) || objectType.equals(PLMObjectType.SUPPLIERAUDIT) || objectType.equals(PLMObjectType.SHAREDOBJECT)) {
            ArrayList<String> fileList = new ArrayList<>();
            String number = "";
            if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
                PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(id);
                PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
                number = inspectionPlan.getNumber();
                List<PQMInspectionPlanFile> inspectionPlanFileList = inspectionPlanFileRepository.findByInspectionPlanAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(id);
                inspectionPlanFileList.forEach(inspectionPlanFile -> {
                    File file = getFile(id, inspectionPlanFile.getId(), objectType);
                    fileList.add(file.getAbsolutePath());
                });
            } else if (objectType.equals(PLMObjectType.INSPECTION)) {
                PQMInspection inspection = inspectionRepository.findOne(id);
                number = inspection.getInspectionNumber();
                List<PQMInspectionFile> inspectionFileList = inspectionFileRepository.findByInspectionAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(id);
                inspectionFileList.forEach(inspectionFile -> {
                    File file = getFile(id, inspectionFile.getId(), objectType);
                    fileList.add(file.getAbsolutePath());
                });
            } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
                PQMProblemReport problemReport = problemReportRepository.findOne(id);
                number = problemReport.getPrNumber();
                List<PQMProblemReportFile> problemReportFileList = problemReportFileRepository.findByProblemReportAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(id);
                problemReportFileList.forEach(problemReportFile -> {
                    File file = getFile(id, problemReportFile.getId(), objectType);
                    fileList.add(file.getAbsolutePath());
                });
            } else if (objectType.equals(PLMObjectType.NCR)) {
                PQMNCR pqmncr = ncrRepository.findOne(id);
                number = pqmncr.getNcrNumber();
                List<PQMNCRFile> pqmncrFileList = ncrFileRepository.findByNcrAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(id);
                pqmncrFileList.forEach(pqmncrFile -> {
                    File file = getFile(id, pqmncrFile.getId(), objectType);
                    fileList.add(file.getAbsolutePath());
                });
            } else if (objectType.equals(PLMObjectType.QCR)) {
                PQMQCR pqmqcr = qcrRepository.findOne(id);
                number = pqmqcr.getTitle();
                List<PQMQCRFile> pqmqcrFileList = qcrFileRepository.findByQcrAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(id);
                pqmqcrFileList.forEach(pqmqcrFile -> {
                    File file = getFile(id, pqmqcrFile.getId(), objectType);
                    fileList.add(file.getAbsolutePath());
                });
            } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
                MESObject mesObject = mesObjectRepository.findOne(id);
                number = mesObject.getNumber();
                List<MESObjectFile> mesObjectFiles = mesObjectFileRepository.findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(id);
                mesObjectFiles.forEach(objectFile -> {
                    File file = getFile(id, objectFile.getId(), objectType);
                    fileList.add(file.getAbsolutePath());
                });
            } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
                if( id == 0) {
                    List<PLMDocument> plmDocuments = plmDocumentRepository.findByFileTypeAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc("FILE");
                    number = "Document";
                    plmDocuments.forEach(objectFile -> {
                        File file = getFile(id, objectFile.getId(), objectType);
                        fileList.add(file.getAbsolutePath());
                    });
                }else{
                    PLMDocument plmDocument = plmDocumentRepository.findOne(id);
                    number = plmDocument.getName();
                    List<PLMDocument> plmDocuments = plmDocumentRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(plmDocument.getId());
                    plmDocuments.forEach(objectFile -> {
                        File file = getFile(id, objectFile.getId(), objectType);
                        fileList.add(file.getAbsolutePath());
                    });
                }
            } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(id);
                number = manufacturerPart.getPartNumber();
                List<PLMMfrPartInspectionReport> mroObjectFiles = mfrPartInspectionReportRepository.findByManufacturerPartAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(id);
                mroObjectFiles.forEach(objectFile -> {
                    File file = getFile(id, objectFile.getId(), objectType);
                    fileList.add(file.getAbsolutePath());
                });
            } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
                MROObject mroObject = mroObjectRepository.findOne(id);
                number = mroObject.getName();
                List<MROObjectFile> mroObjectFiles = mroObjectFileRepository.findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(id);
                mroObjectFiles.forEach(objectFile -> {
                    File file = getFile(id, objectFile.getId(), objectType);
                    fileList.add(file.getAbsolutePath());
                });
            } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
                PGCObject pgcObject = pgcObjectRepository.findOne(id);
                number = pgcObject.getName();
                List<PGCObjectFile> pgcObjectFiles = pgcObjectFileRepository.findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(id);
                pgcObjectFiles.forEach(objectFile -> {
                    File file = getFile(id, objectFile.getId(), objectType);
                    fileList.add(file.getAbsolutePath());
                });
            } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
                PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(id);
                number = supplierAudit.getName();
                List<PQMSupplierAuditFile> supplierAuditFiles = supplierAuditFileRepository.findBySupplierAuditAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(id);
                supplierAuditFiles.forEach(objectFile -> {
                    File file = getFile(id, objectFile.getId(), objectType);
                    fileList.add(file.getAbsolutePath());
                });
            } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                PGCObject pgcObject = pgcObjectRepository.findOne(id);
                number = pgcObject.getName();
                List<PQMPPAPChecklist> pqmppapChecklists = ppapChecklistRepository.findByPpapAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(id);
                pqmppapChecklists.forEach(objectFile -> {
                    File file = getFile(id, objectFile.getId(), objectType);
                    fileList.add(file.getAbsolutePath());
                });
            } else if (objectType.equals(PLMObjectType.SHAREDOBJECT)) {
                PLMDocument plmDocument1 = plmDocumentRepository.findOne(id);
                if (plmDocument1 != null){
                        number = plmDocument1.getName();
                        List<PLMDocument> plmDocuments = plmDocumentRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(plmDocument1.getId());
                        plmDocuments.forEach(objectFile -> {
                            File file = getFile(id, objectFile.getId(), objectType);
                            fileList.add(file.getAbsolutePath());
                        });
                }else {
//                    List<PLMSharedObject> sharedObject = sharedObjectRepository.findByObjectId(id);
//                    sharedObject.
                    number = "Shared";
                    List<PLMSharedObject> sharedObjects = sharedObjectRepository.findByObjectId(id);
                    sharedObjects.forEach(sharedObject -> {

                        PLMItemFile itemFile = itemFileRepository.findOne(sharedObject.getObjectId());
                        PLMChangeFile changeFile = changeFileRepository.findOne(sharedObject.getObjectId());
                        PLMProjectFile projectFile = projectFileRepository.findOne(sharedObject.getObjectId());
                        PLMActivityFile activityFile = activityFileRepository.findOne(sharedObject.getObjectId());
                        PLMTaskFile taskFile = taskFileRepository.findOne(sharedObject.getObjectId());
                        PLMProgramFile programFile = programFileRepository.findOne(sharedObject.getObjectId());
                        PLMManufacturerFile manufacturerFile = manufacturerFileRepository.findOne(sharedObject.getObjectId());
                        PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileRepository.findOne(sharedObject.getObjectId());
                        PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(sharedObject.getObjectId());
                        PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(sharedObject.getObjectId());
                        PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(sharedObject.getObjectId());
                        PQMNCRFile ncrFile = ncrFileRepository.findOne(sharedObject.getObjectId());
                        PQMQCRFile qcrFile = qcrFileRepository.findOne(sharedObject.getObjectId());
                        MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(sharedObject.getObjectId());
                        MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(sharedObject.getObjectId());
                        PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(sharedObject.getObjectId());
                        PLMSupplierFile plmSupplierFile = supplierFileRepository.findOne(sharedObject.getObjectId());
                        MESBOPFile mesBopFile = bopFileRepository.findOne(sharedObject.getObjectId());
                        PLMRequirementDocumentFile requirementDocumentFile = requirementDocumentFileRepository.findOne(sharedObject.getObjectId());
                        PLMRequirementFile requirementFile = requirementFileRepository.findOne(sharedObject.getObjectId());
                        PQMCustomerFile customerFile = customerFileRepository.findOne(sharedObject.getObjectId());
                        PLMNprFile plmNprFile = nprFileRepository.findOne(sharedObject.getObjectId());
                        PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(sharedObject.getObjectId());
                        PLMDocument plmDocument = plmDocumentRepository.findOne(sharedObject.getObjectId());
                        MESMBOMFile mbomFile = mbomFileRepository.findOne(sharedObject.getObjectId());

                        if (itemFile != null) {
                            File file = getFile(itemFile.getItem().getId(), sharedObject.getObjectId(), PLMObjectType.ITEMREVISION);
                            fileList.add(file.getAbsolutePath());
                        } else if (changeFile != null) {
                            File file = getFile(changeFile.getChange(), sharedObject.getObjectId(), PLMObjectType.CHANGE);
                            fileList.add(file.getAbsolutePath());
                        } else if (projectFile != null) {
                            File file = getFile(projectFile.getProject(), sharedObject.getObjectId(), PLMObjectType.PROJECT);
                            fileList.add(file.getAbsolutePath());
                        } else if (activityFile != null) {
                            File file = getFile(activityFile.getActivity(), sharedObject.getObjectId(), PLMObjectType.PROJECTACTIVITY);
                            fileList.add(file.getAbsolutePath());
                        } else if (taskFile != null) {
                            File file = getFile(taskFile.getTask(), sharedObject.getObjectId(), PLMObjectType.PROJECTTASK);
                            fileList.add(file.getAbsolutePath());
                        } else if (programFile != null) {
                            File file = getFile(programFile.getProgram(), sharedObject.getObjectId(), PLMObjectType.PROGRAM);
                            fileList.add(file.getAbsolutePath());
                        } else if (manufacturerFile != null) {
                            File file = getFile(manufacturerFile.getManufacturer(), sharedObject.getObjectId(), PLMObjectType.MANUFACTURER);
                            fileList.add(file.getAbsolutePath());
                        } else if (manufacturerPartFile != null) {
                            File file = getFile(manufacturerPartFile.getManufacturerPart(), sharedObject.getObjectId(), PLMObjectType.MANUFACTURERPART);
                            fileList.add(file.getAbsolutePath());
                        } else if (inspectionPlanFile != null) {
                            File file = getFile(inspectionPlanFile.getInspectionPlan(), sharedObject.getObjectId(), PLMObjectType.INSPECTIONPLAN);
                            fileList.add(file.getAbsolutePath());
                        } else if (inspectionFile != null) {
                            File file = getFile(inspectionFile.getInspection(), sharedObject.getObjectId(), PLMObjectType.INSPECTION);
                            fileList.add(file.getAbsolutePath());
                        } else if (problemReportFile != null) {
                            File file = getFile(problemReportFile.getProblemReport(), sharedObject.getObjectId(), PLMObjectType.PROBLEMREPORT);
                            fileList.add(file.getAbsolutePath());
                        } else if (ncrFile != null) {
                            File file = getFile(ncrFile.getNcr(), sharedObject.getObjectId(), PLMObjectType.NCR);
                            fileList.add(file.getAbsolutePath());
                        } else if (qcrFile != null) {
                            File file = getFile(qcrFile.getQcr(), sharedObject.getObjectId(), PLMObjectType.QCR);
                            fileList.add(file.getAbsolutePath());
                        } else if (mesObjectFile != null) {
                            File file = getFile(mesObjectFile.getObject(), sharedObject.getObjectId(), PLMObjectType.MESOBJECT);
                            fileList.add(file.getAbsolutePath());
                        } else if (mroObjectFile != null) {
                            File file = getFile(mroObjectFile.getObject(), sharedObject.getObjectId(), PLMObjectType.MROOBJECT);
                            fileList.add(file.getAbsolutePath());
                        } else if (pgcObjectFile != null) {
                            File file = getFile(pgcObjectFile.getObject(), sharedObject.getObjectId(), PLMObjectType.PGCOBJECT);
                            fileList.add(file.getAbsolutePath());
                        } else if (plmSupplierFile != null) {
                            File file = getFile(plmSupplierFile.getSupplier(), sharedObject.getObjectId(), PLMObjectType.SUPPLIER);
                            fileList.add(file.getAbsolutePath());
                        } else if (mesBopFile != null) {
                            File file = getFile(mesBopFile.getBop(), sharedObject.getObjectId(), PLMObjectType.BOP);
                            fileList.add(file.getAbsolutePath());
                        } else if (requirementDocumentFile != null) {
                            File file = getFile(requirementDocumentFile.getDocumentRevision().getId(), sharedObject.getObjectId(), PLMObjectType.REQUIREMENTDOCUMENT);
                            fileList.add(file.getAbsolutePath());
                        } else if (requirementFile != null) {
                            File file = getFile(requirementFile.getRequirement().getId(), sharedObject.getObjectId(), PLMObjectType.REQUIREMENT);
                            fileList.add(file.getAbsolutePath());
                        } else if (customerFile != null) {
                            File file = getFile(customerFile.getCustomer(), sharedObject.getObjectId(), PLMObjectType.CUSTOMER);
                            fileList.add(file.getAbsolutePath());
                        } else if (plmNprFile != null) {
                            File file = getFile(plmNprFile.getNpr(), sharedObject.getObjectId(), PLMObjectType.PLMNPR);
                            fileList.add(file.getAbsolutePath());
                        } else if (supplierAuditFile != null) {
                            File file = getFile(supplierAuditFile.getSupplierAudit(), sharedObject.getObjectId(), PLMObjectType.SUPPLIERAUDIT);
                            fileList.add(file.getAbsolutePath());
                        } else if (plmDocument != null) {
                            File file = getFile(plmDocument.getId(), sharedObject.getObjectId(), PLMObjectType.DOCUMENT);
                            fileList.add(file.getAbsolutePath());
                        } else if (mbomFile != null) {
                            File file = getFile(mbomFile.getMbomRevision(), sharedObject.getObjectId(), PLMObjectType.MBOM);
                            fileList.add(file.getAbsolutePath());
                        }
                    });
                }
            }
            String zipName = number + "_Files.zip";
            File zipBox = new File(zipName);
            if (zipBox.exists())
                zipBox.delete();
            fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), objectType.toString(),id);
            InputStream inputStream = new FileInputStream(zipBox.getPath());
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
            response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
            response.getOutputStream().flush();
            inputStream.close();
        } else if (objectType.equals(PLMObjectType.ITEM)) {
            itemFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.PROJECT)) {
            projectService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
            manufacturerFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
            manufacturerPartFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            activityService.generateZipFile(id, response, "ACTIVITY");
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            activityService.generateZipFile(id, response, "TASKS");
        } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
            glossaryService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
            supplierFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
            customerFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
            reqDocumentFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
            requirementFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.PLMNPR)) {
            nprFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
            mbomFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
            mbomInstanceFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
            bopFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
            bopPlanFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
            bopInstanceOperationFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            programFileService.generateZipFile(id, response);
        } else if (objectType.equals(PLMObjectType.CHANGE)) {
            PLMChange change = changeRepository.findOne(id);
            changeFileService.generateZipFile(id, PLMObjectType.valueOf(change.getChangeType().toString()), response);
        }
    }

    @Transactional(readOnly = true)
    public ObjectFileDto findByTypeAndFileName(Integer id, PLMObjectType objectType, String name) {
        ObjectFileDto objectFileDto = new ObjectFileDto();
        List<Integer> fileIds = new ArrayList<>();
        if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
            fileIds = inspectionPlanFileRepository.getFileIdsByInspectionPlanAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.INSPECTION)) {
            fileIds = inspectionFileRepository.getFileIdsByInspectionAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            fileIds = problemReportFileRepository.getFileIdsByProblemReportAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.NCR)) {
            fileIds = ncrFileRepository.getFileIdsByNcrAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.QCR)) {
            fileIds = qcrFileRepository.getFileIdsByQcrAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
            fileIds = mesObjectFileRepository.getFileIdsByObjectAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
            if (id == 0) {
                fileIds = plmDocumentRepository.getFileIdsByNameContainingIgnoreCaseAndLatestTrue(name.toLowerCase());
            } else {
                fileIds = plmDocumentRepository.getFileIdsByParentAndNameContainingIgnoreCaseAndLatestTrue(id, name.toLowerCase());
            }
        } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            fileIds = mfrPartInspectionReportRepository.getFileIdsByManufacturerPartAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
            fileIds = mroObjectFileRepository.getFileIdsByObjectAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
            fileIds = pgcObjectFileRepository.getFileIdsByObjectAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
            fileIds = supplierAuditFileRepository.getFileIdsBySupplierAuditAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            fileIds = ppapChecklistRepository.getFileIdsByPpapAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.ITEM)) {
            fileIds = itemFileRepository.getFileIdsByItemAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.PROJECT)) {
            fileIds = projectFileRepository.getFileIdsByProjectAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
            fileIds = manufacturerFileRepository.getFileIdsByManufacturerAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
            fileIds = manufacturerPartFileRepository.getFileIdsByManufacturerPartAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            fileIds = activityFileRepository.getFileIdsByActivityAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            fileIds = taskFileRepository.getFileIdsByTaskAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
            fileIds = glossaryFileRepository.getFileIdsByGlossaryAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
            fileIds = supplierFileRepository.getFileIdsBySupplierAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
            fileIds = customerFileRepository.getFileIdsByCustomerAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
            fileIds = requirementDocumentFileRepository.getFileIdsByDocumentRevisionAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
            fileIds = requirementFileRepository.getFileIdsByRequirementAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.PLMNPR)) {
            fileIds = nprFileRepository.getFileIdsByNprAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
            fileIds = mbomFileRepository.getFileIdsByMbomRevisionAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
            fileIds = mbomInstanceFileRepository.getFileIdsByMbomInstanceAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
            fileIds = bopFileRepository.getFileIdsByBopAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
            fileIds = bopOperationFileRepository.getFileIdsByBopOperationAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
            fileIds = bopInstanceOperationFileRepository.getFileIdsByBopOperationAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            fileIds = programFileRepository.getFileIdsByProgramAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.PROGRAMTEMPLATE)) {
            fileIds = programTemplateFileRepository.getFileIdsByTemplateAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.TEMPLATE)) {
            fileIds = projectTemplateFileRepository.getFileIdsByTemplateAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.TEMPLATETASK)) {
            fileIds = projectTemplateTaskFileRepository.getFileIdsByTaskAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.TEMPLATEACTIVITY)) {
            fileIds = projectTemplateActivityFileRepository.getFileIdsByActivityAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        } else if (objectType.equals(PLMObjectType.CHANGE)) {
            fileIds = changeFileRepository.getFileIdsByChangeAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        }
        List<PLMObjectDocument> objectDocuments = objectDocumentRepository.getFileIdsByObjectAndNameContainingIgnoreCaseAndLatestTrue(id, name);
        objectDocuments.forEach(plmObjectDocument -> {
            objectFileDto.getObjectFiles().add(convertObjectDocumentToDto(plmObjectDocument));
        });
        if (fileIds.size() > 0) {
            objectFileDto.getObjectFiles().addAll(convertFilesIdsToDtoList(id, objectType, fileIds, false));
        }
        Collections.sort(objectFileDto.getObjectFiles(), new Comparator<FileDto>() {
            public int compare(final FileDto object1, final FileDto object2) {
                return object2.getModifiedDate().compareTo(object1.getModifiedDate());
            }
        });
        return objectFileDto;
    }

    @Transactional(readOnly = true)
    public ObjectFileDto getAllFileVersionComments(Integer fileId, PLMObjectType type, ObjectType objectType) {
        ObjectFileDto objectFileDto = new ObjectFileDto();

        if (objectType.name().equals(PLMObjectType.OBJECTDOCUMENT.name())) {
            PLMObjectDocument objectDocument = objectDocumentRepository.findOne(fileId);
            FileDto fileDto = convertFileIdToDto(0, type, objectDocument.getDocument().getId());
            fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(objectDocument.getDocument().getId()));
            fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(objectDocument.getId(), objectType));
            objectFileDto.getObjectFiles().add(fileDto);
            List<PLMDocument> files = plmDocumentRepository.findByFileNoAndLatestFalseOrderByCreatedDateDesc(objectDocument.getDocument().getFileNo());
            if (files.size() > 0) {
                for (PLMDocument file : files) {
                    if (!objectDocument.getDocument().getId().equals(file.getId())) {
                        FileDto dto = convertFileIdToDto(0, type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            }
        } else {
            if (type.equals(PLMObjectType.INSPECTIONPLAN)) {
                PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(inspectionPlanFile.getInspectionPlan(), type, inspectionPlanFile.getId());
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(inspectionPlanFile.getId(), objectType));
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(inspectionPlanFile.getId()));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PQMInspectionPlanFile> files = inspectionPlanFileRepository.findByInspectionPlanAndFileNoAndLatestFalseOrderByCreatedDateDesc(inspectionPlanFile.getInspectionPlan(), inspectionPlanFile.getFileNo());
                if (files.size() > 0) {
                    files.forEach(file -> {
                        FileDto dto = convertFileIdToDto(file.getInspectionPlan(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    });
                }
            } else if (type.equals(PLMObjectType.INSPECTION)) {
                PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(inspectionFile.getInspection(), type, inspectionFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(inspectionFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(inspectionFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PQMInspectionFile> files = inspectionFileRepository.findByInspectionAndFileNoAndLatestFalseOrderByCreatedDateDesc(inspectionFile.getInspection(), inspectionFile.getFileNo());
                if (files.size() > 0) {
                    for (PQMInspectionFile file : files) {
                        fileDto = convertFileIdToDto(file.getInspection(), type, file.getId());
                        fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(fileDto);
                    }
                }
            } else if (type.equals(PLMObjectType.PROBLEMREPORT)) {
                PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(problemReportFile.getProblemReport(), type, problemReportFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(problemReportFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(problemReportFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PQMProblemReportFile> files = problemReportFileRepository.findByProblemReportAndFileNoAndLatestFalseOrderByCreatedDateDesc(problemReportFile.getProblemReport(), problemReportFile.getFileNo());
                if (files.size() > 0) {
                    for (PQMProblemReportFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getProblemReport(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.NCR)) {
                PQMNCRFile pqmncrFile = ncrFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(pqmncrFile.getNcr(), type, pqmncrFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(pqmncrFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(pqmncrFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PQMNCRFile> files = ncrFileRepository.findByNcrAndFileNoAndLatestFalseOrderByCreatedDateDesc(pqmncrFile.getNcr(), pqmncrFile.getFileNo());
                if (files.size() > 0) {
                    for (PQMNCRFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getNcr(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.QCR)) {
                PQMQCRFile pqmqcrFile = qcrFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(pqmqcrFile.getQcr(), type, pqmqcrFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(pqmqcrFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(pqmqcrFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PQMQCRFile> files = qcrFileRepository.findByQcrAndFileNoAndLatestFalseOrderByCreatedDateDesc(pqmqcrFile.getQcr(), pqmqcrFile.getFileNo());
                if (files.size() > 0) {
                    for (PQMQCRFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getQcr(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.MESOBJECT)) {
                MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(mesObjectFile.getObject(), type, mesObjectFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(mesObjectFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(mesObjectFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<MESObjectFile> files = mesObjectFileRepository.findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(mesObjectFile.getObject(), mesObjectFile.getFileNo());
                if (files.size() > 0) {
                    for (MESObjectFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getObject(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.DOCUMENT)) {
                PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(0, type, plmDocument.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(plmDocument.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(plmDocument.getId(), ObjectType.valueOf(PLMObjectType.DOCUMENT.toString())));
                fileDto.setReviewers(documentReviewerRepository.findByDocumentOrderByIdDesc(plmDocument.getId()));
                fileDto.getReviewers().forEach(documentReviewer -> {
                    documentReviewer.setReviewerName(personRepository.findOne(documentReviewer.getReviewer()).getFullName());
                });
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMDocument> files = plmDocumentRepository.findByFileNoAndLatestFalseOrderByCreatedDateDesc(plmDocument.getFileNo());
                if (files.size() > 0) {
                    for (PLMDocument file : files) {
                        FileDto dto = convertFileIdToDto(0, type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), ObjectType.valueOf(PLMObjectType.DOCUMENT.toString())));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        dto.setReviewers(documentReviewerRepository.findByDocumentOrderByIdDesc(file.getId()));
                        dto.getReviewers().forEach(documentReviewer -> {
                            documentReviewer.setReviewerName(personRepository.findOne(documentReviewer.getReviewer()).getFullName());
                        });
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(mfrPartInspectionReport.getManufacturerPart(), type, mfrPartInspectionReport.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(mfrPartInspectionReport.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(mfrPartInspectionReport.getId(), ObjectType.valueOf(PLMObjectType.MFRPARTINSPECTIONREPORT.toString())));
                fileDto.setReviewers(documentReviewerRepository.findByDocumentOrderByIdDesc(mfrPartInspectionReport.getId()));
                fileDto.getReviewers().forEach(documentReviewer -> {
                    documentReviewer.setReviewerName(personRepository.findOne(documentReviewer.getReviewer()).getFullName());
                });
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMMfrPartInspectionReport> files = mfrPartInspectionReportRepository.findByFileNoAndLatestFalseOrderByCreatedDateDesc(mfrPartInspectionReport.getFileNo());
                if (files.size() > 0) {
                    for (PLMMfrPartInspectionReport file : files) {
                        FileDto dto = convertFileIdToDto(file.getManufacturerPart(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), ObjectType.valueOf(PLMObjectType.MFRPARTINSPECTIONREPORT.toString())));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        dto.setReviewers(documentReviewerRepository.findByDocumentOrderByIdDesc(file.getId()));
                        dto.getReviewers().forEach(documentReviewer -> {
                            documentReviewer.setReviewerName(personRepository.findOne(documentReviewer.getReviewer()).getFullName());
                        });
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.MROOBJECT)) {
                MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(mroObjectFile.getObject(), type, mroObjectFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(mroObjectFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(mroObjectFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<MROObjectFile> files = mroObjectFileRepository.findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(mroObjectFile.getObject(), mroObjectFile.getFileNo());
                if (files.size() > 0) {
                    for (MROObjectFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getObject(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.PGCOBJECT)) {
                PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(pgcObjectFile.getObject(), type, pgcObjectFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(pgcObjectFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(pgcObjectFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PGCObjectFile> files = pgcObjectFileRepository.findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(pgcObjectFile.getObject(), pgcObjectFile.getFileNo());
                if (files.size() > 0) {
                    for (PGCObjectFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getObject(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.SUPPLIERAUDIT)) {
                PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(supplierAuditFile.getSupplierAudit(), type, supplierAuditFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(supplierAuditFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(supplierAuditFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PQMSupplierAuditFile> files = supplierAuditFileRepository.findBySupplierAuditAndFileNoAndLatestFalseOrderByCreatedDateDesc(supplierAuditFile.getSupplierAudit(), supplierAuditFile.getFileNo());
                if (files.size() > 0) {
                    for (PQMSupplierAuditFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getSupplierAudit(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.PPAPCHECKLIST)) {
                PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(pqmppapChecklist.getPpap(), type, pqmppapChecklist.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(pqmppapChecklist.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(pqmppapChecklist.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PQMPPAPChecklist> files = ppapChecklistRepository.findByPpapAndFileNoAndLatestFalseOrderByCreatedDateDesc(pqmppapChecklist.getPpap(), pqmppapChecklist.getFileNo());
                if (files.size() > 0) {
                    for (PQMPPAPChecklist file : files) {
                        FileDto dto = convertFileIdToDto(file.getPpap(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.ITEM)) {
                PLMItemFile itemFile = itemFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(itemFile.getItem().getId(), type, itemFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(itemFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(itemFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMItemFile> files = itemFileRepository.findByItemAndFileNoAndLatestFalseOrderByCreatedDateDesc(itemFile.getItem(), itemFile.getFileNo());
                if (files.size() > 0) {
                    for (PLMItemFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getItem().getId(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.PROJECT)) {
                PLMProjectFile projectFile = projectFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(projectFile.getProject(), type, projectFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(projectFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(projectFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMProjectFile> files = projectFileRepository.findByProjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(projectFile.getProject(), projectFile.getFileNo());
                if (files.size() > 0) {
                    for (PLMProjectFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getProject(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.MANUFACTURER)) {
                PLMManufacturerFile manufacturerFile = manufacturerFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(manufacturerFile.getManufacturer(), type, manufacturerFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(manufacturerFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(manufacturerFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMManufacturerFile> files = manufacturerFileRepository.findByManufacturerAndFileNoAndLatestFalseOrderByCreatedDateDesc(manufacturerFile.getManufacturer(), manufacturerFile.getFileNo());
                if (files.size() > 0) {
                    for (PLMManufacturerFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getManufacturer(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.MANUFACTURERPART)) {
                PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(manufacturerPartFile.getManufacturerPart(), type, manufacturerPartFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(manufacturerPartFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(manufacturerPartFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMManufacturerPartFile> files = manufacturerPartFileRepository.findByManufacturerPartAndFileNoAndLatestFalseOrderByCreatedDateDesc(manufacturerPartFile.getManufacturerPart(), manufacturerPartFile.getFileNo());
                if (files.size() > 0) {
                    for (PLMManufacturerPartFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getManufacturerPart(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.PROJECTACTIVITY)) {
                PLMActivityFile activityFile = activityFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(activityFile.getActivity(), type, activityFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(activityFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(activityFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMActivityFile> files = activityFileRepository.findByActivityAndFileNoAndLatestFalseOrderByCreatedDateDesc(activityFile.getActivity(), activityFile.getFileNo());
                if (files.size() > 0) {
                    for (PLMActivityFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getActivity(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.PROJECTTASK)) {
                PLMTaskFile taskFile = taskFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(taskFile.getTask(), type, taskFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(taskFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(taskFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMTaskFile> files = taskFileRepository.findByTaskAndFileNoAndLatestFalseOrderByCreatedDateDesc(taskFile.getTask(), taskFile.getFileNo());
                if (files.size() > 0) {
                    for (PLMTaskFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getTask(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.TERMINOLOGY)) {
                PLMGlossaryFile glossaryFile = glossaryFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(glossaryFile.getGlossary(), type, glossaryFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(glossaryFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(glossaryFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMGlossaryFile> files = glossaryFileRepository.findByGlossaryAndFileNoAndLatestFalseOrderByCreatedDateDesc(glossaryFile.getGlossary(), glossaryFile.getFileNo());
                if (files.size() > 0) {
                    for (PLMGlossaryFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getGlossary(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.MFRSUPPLIER)) {
                PLMSupplierFile supplierFile = supplierFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(supplierFile.getSupplier(), type, supplierFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(supplierFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(supplierFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMSupplierFile> files = supplierFileRepository.findBySupplierAndFileNoAndLatestFalseOrderByCreatedDateDesc(supplierFile.getSupplier(), supplierFile.getFileNo());
                if (files.size() > 0) {
                    for (PLMSupplierFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getSupplier(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.CUSTOMER)) {
                PQMCustomerFile customerFile = customerFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(customerFile.getCustomer(), type, customerFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(customerFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(customerFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PQMCustomerFile> files = customerFileRepository.findByCustomerAndFileNoAndLatestFalseOrderByCreatedDateDesc(customerFile.getCustomer(), customerFile.getFileNo());
                if (files.size() > 0) {
                    for (PQMCustomerFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getCustomer(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
                PLMRequirementDocumentFile documentFile = requirementDocumentFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(documentFile.getDocumentRevision().getId(), type, documentFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(documentFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(documentFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMRequirementDocumentFile> files = requirementDocumentFileRepository.findByDocumentRevisionAndFileNoAndLatestFalseOrderByCreatedDateDesc(documentFile.getDocumentRevision(), documentFile.getFileNo());
                if (files.size() > 0) {
                    for (PLMRequirementDocumentFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getDocumentRevision().getId(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.REQUIREMENT)) {
                PLMRequirementFile requirementFile = requirementFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(requirementFile.getRequirement().getId(), type, requirementFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(requirementFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(requirementFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMRequirementFile> files = requirementFileRepository.findByRequirementAndFileNoAndLatestFalseOrderByCreatedDateDesc(requirementFile.getRequirement(), requirementFile.getFileNo());
                if (files.size() > 0) {
                    for (PLMRequirementFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getRequirement().getId(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.PLMNPR)) {
                PLMNprFile nprFile = nprFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(nprFile.getNpr(), type, nprFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(nprFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(nprFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMNprFile> files = nprFileRepository.findByNprAndFileNoAndLatestFalseOrderByCreatedDateDesc(nprFile.getNpr(), nprFile.getFileNo());
                if (files.size() > 0) {
                    for (PLMNprFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getNpr(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.MBOMREVISION)) {
                MESMBOMFile mesmbomFile = mbomFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(mesmbomFile.getMbomRevision(), type, mesmbomFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(mesmbomFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(mesmbomFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<MESMBOMFile> files = mbomFileRepository.findByMbomRevisionAndFileNoAndLatestFalseOrderByCreatedDateDesc(mesmbomFile.getMbomRevision(), mesmbomFile.getFileNo());
                if (files.size() > 0) {
                    for (MESMBOMFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getMbomRevision(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.MBOMINSTANCE)) {
                MESMBOMInstanceFile mesmbomFile = mbomInstanceFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(mesmbomFile.getMbomInstance(), type, mesmbomFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(mesmbomFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(mesmbomFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<MESMBOMInstanceFile> files = mbomInstanceFileRepository.findByMbomInstanceAndFileNoAndLatestFalseOrderByCreatedDateDesc(mesmbomFile.getMbomInstance(), mesmbomFile.getFileNo());
                if (files.size() > 0) {
                    for (MESMBOMInstanceFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getMbomInstance(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.BOPREVISION)) {
                MESBOPFile mesbopFile = bopFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(mesbopFile.getBop(), type, mesbopFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(mesbopFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(mesbopFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<MESBOPFile> files = bopFileRepository.findByBopAndFileNoAndLatestFalseOrderByCreatedDateDesc(mesbopFile.getBop(), mesbopFile.getFileNo());
                if (files.size() > 0) {
                    for (MESBOPFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getBop(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.BOPROUTEOPERATION)) {
                MESBOPOperationFile mesbopFile = bopOperationFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(mesbopFile.getBopOperation(), type, mesbopFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(mesbopFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(mesbopFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<MESBOPOperationFile> files = bopOperationFileRepository.findByBopOperationAndFileNoAndLatestFalseOrderByCreatedDateDesc(mesbopFile.getBopOperation(), mesbopFile.getFileNo());
                if (files.size() > 0) {
                    for (MESBOPOperationFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getBopOperation(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
                MESBOPInstanceOperationFile mesbopFile = bopInstanceOperationFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(mesbopFile.getBopOperation(), type, mesbopFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(mesbopFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(mesbopFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<MESBOPInstanceOperationFile> files = bopInstanceOperationFileRepository.findByBopOperationAndFileNoAndLatestFalseOrderByCreatedDateDesc(mesbopFile.getBopOperation(), mesbopFile.getFileNo());
                if (files.size() > 0) {
                    for (MESBOPInstanceOperationFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getBopOperation(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.PROGRAM)) {
                PLMProgramFile programFile = programFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(programFile.getProgram(), type, programFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(programFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(programFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMProgramFile> files = programFileRepository.findByProgramAndFileNoAndLatestFalseOrderByCreatedDateDesc(programFile.getProgram(), programFile.getFileNo());
                if (files.size() > 0) {
                    for (PLMProgramFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getProgram(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            } else if (type.equals(PLMObjectType.CHANGE)) {
                PLMChangeFile changeFile = changeFileRepository.findOne(fileId);
                FileDto fileDto = convertFileIdToDto(changeFile.getChange(), type, changeFile.getId());
                fileDto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(changeFile.getId()));
                fileDto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(changeFile.getId(), objectType));
                objectFileDto.getObjectFiles().add(fileDto);
                List<PLMChangeFile> files = changeFileRepository.findByChangeAndFileNoAndLatestFalseOrderByCreatedDateDesc(changeFile.getChange(), changeFile.getFileNo());
                if (files.size() > 0) {
                    for (PLMChangeFile file : files) {
                        FileDto dto = convertFileIdToDto(file.getChange(), type, file.getId());
                        dto.setComments(commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType));
                        dto.setDownloadHistories(fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId()));
                        objectFileDto.getObjectFiles().add(dto);
                    }
                }
            }
        }

        return objectFileDto;
    }

    @Transactional(readOnly = true)
    public Integer getObjectFilesCount(Integer id, PLMObjectType objectType) {
        Integer filesCount = 0;
        if (objectType.equals(PLMObjectType.MESOBJECT)) {
            filesCount = mesObjectFileRepository.getFilesCountByObjectAndFileTypeAndLatestTrue(id, "FILE");
            filesCount = filesCount + objectDocumentRepository.getDocumentsCountByObjectIdAndDocumentType(id, "FILE");
        } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
            filesCount = plmDocumentRepository.getFilesCountByFileTypeAndLatestTrue("FILE");
        } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
            filesCount = mroObjectFileRepository.getFilesCountByObjectAndFileTypeAndLatestTrue(id, "FILE");
            filesCount = filesCount + objectDocumentRepository.getDocumentsCountByObjectIdAndDocumentType(id, "FILE");
        } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
            filesCount = pgcObjectFileRepository.getFilesCountByObjectAndFileTypeAndLatestTrue(id, "FILE");
            filesCount = filesCount + objectDocumentRepository.getDocumentsCountByObjectIdAndDocumentType(id, "FILE");
        }

        return filesCount;
    }

    @Transactional(readOnly = true)
    public ObjectFileDto getObjectFiles(Integer id, PLMObjectType objectType, Boolean hierarchy) {
        ObjectFileDto objectFileDto = new ObjectFileDto();
        List<FileDto> folders = new ArrayList<>();
        List<FileDto> files = new ArrayList<>();
        if (objectType.equals(PLMObjectType.PROGRAM) || objectType.equals(PLMObjectType.PROGRAMTEMPLATE) || objectType.equals(PLMObjectType.PROJECT) || objectType.equals(PLMObjectType.TEMPLATE)
                || objectType.equals(PLMObjectType.TEMPLATEACTIVITY) || objectType.equals(PLMObjectType.TEMPLATETASK) || objectType.equals(PLMObjectType.PROJECTACTIVITY) || objectType.equals(PLMObjectType.PROJECTTASK)) {
            objectFileDto = getObjectFilesByType(id, objectType, hierarchy);
        } else {
            List<Integer> foldersList = new ArrayList<>();
            List<Integer> filesList = new ArrayList<>();
            Map<String, Boolean> dmPermissinons = new HashMap<>();
            if (objectType.equals(PLMObjectType.INSPECTIONPLAN)) {
                foldersList = inspectionPlanFileRepository.getFileIdsByInspectionPlanAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = inspectionPlanFileRepository.getFileIdsByInspectionPlanAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.INSPECTION)) {
                foldersList = inspectionFileRepository.getFileIdsByInspectionAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = inspectionFileRepository.getFileIdsByInspectionAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
                foldersList = problemReportFileRepository.getFileIdsByProblemReportAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = problemReportFileRepository.getFileIdsByProblemReportAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.NCR)) {
                foldersList = ncrFileRepository.getFileIdsByNCRAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = ncrFileRepository.getFileIdsByNCRAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.QCR)) {
                foldersList = qcrFileRepository.getFileIdsByQCRAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = qcrFileRepository.getFileIdsByQCRAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.MESOBJECT)) {
                foldersList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.DOCUMENT)) {
                if (id != 0) {
                    filesList = plmDocumentRepository.getFileIdsByParentAndFileTypeOrderByModifiedDateDesc(id, "FILE");
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    for (Privilege privilege : privilegeRepository.findAll()) {
                        dmPermissinons.put(privilege.getName(), documentService.checkDocumentPermissions(authentication, privilege.getName(), objectType.name(), id));
                    }
                    objectFileDto.setDmPermissions(dmPermissinons);
                } else {
                    filesList = plmDocumentRepository.getFileIdsByFileTypeOrderByModifiedDateDesc("FILE");
                }
            } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                foldersList = mfrPartInspectionReportRepository.getFileIdsByManufacturerPartAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mfrPartInspectionReportRepository.getFileIdsByManufacturerPartAndFileTypeOrderByModifiedDateDesc(id, "FILE");
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                for (Privilege privilege : privilegeRepository.findAll()) {
                    dmPermissinons.put(privilege.getName(), documentService.checkDocumentPermissions(authentication, privilege.getName(), objectType.name(), id));
                }
                objectFileDto.setDmPermissions(dmPermissinons);
            } else if (objectType.equals(PLMObjectType.MROOBJECT)) {
                foldersList = mroObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mroObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.PGCOBJECT)) {
                foldersList = pgcObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = pgcObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.SUPPLIERAUDIT)) {
                foldersList = supplierAuditFileRepository.getFileIdsBySupplierAuditAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = supplierAuditFileRepository.getFileIdsBySupplierAuditAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                foldersList = ppapChecklistRepository.getFileIdsByPpapAndLatestTrueAndParentFileIsNullAndFileTypeOrderByIdAsc(id, "FOLDER");
                filesList = ppapChecklistRepository.getFileIdsByPpapAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                for (Privilege privilege : privilegeRepository.findAll()) {
                    dmPermissinons.put(privilege.getName(), documentService.checkDocumentPermissions(authentication, privilege.getName(), objectType.name(), id));
                }
                objectFileDto.setDmPermissions(dmPermissinons);
            } else if (objectType.equals(PLMObjectType.ITEM)) {
                foldersList = itemFileRepository.getFileIdsByItemAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = itemFileRepository.getFileIdsByItemAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.PROJECT)) {
                foldersList = projectFileRepository.getFileIdsByProjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
                filesList = projectFileRepository.getFileIdsByProjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.TEMPLATE)) {
                foldersList = projectTemplateFileRepository.getFileIdsByTemplateAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
            } else if (objectType.equals(PLMObjectType.TEMPLATEACTIVITY)) {
                foldersList = projectTemplateActivityFileRepository.getFileIdsByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
            } else if (objectType.equals(PLMObjectType.TEMPLATETASK)) {
                foldersList = projectTemplateTaskFileRepository.getFileIdsByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
            } else if (objectType.equals(PLMObjectType.PROGRAMTEMPLATE)) {
                foldersList = programTemplateFileRepository.getFileIdsByTemplateAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
            } else if (objectType.equals(PLMObjectType.MANUFACTURER)) {
                foldersList = manufacturerFileRepository.getFileIdsByMfrAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = manufacturerFileRepository.getFileIdsByMfrAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.MANUFACTURERPART)) {
                foldersList = manufacturerPartFileRepository.getFileIdsByMfrAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = manufacturerPartFileRepository.getFileIdsByMfrAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
                foldersList = activityFileRepository.getFileIdsByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
                filesList = activityFileRepository.getFileIdsByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
                foldersList = taskFileRepository.getFileIdsByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
                filesList = taskFileRepository.getFileIdsByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.TERMINOLOGY)) {
                foldersList = glossaryFileRepository.getFileIdsByGlossaryAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = glossaryFileRepository.getFileIdsByGlossaryAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.MFRSUPPLIER)) {
                foldersList = supplierFileRepository.getFileIdsBySupplierAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = supplierFileRepository.getFileIdsBySupplierAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.CUSTOMER)) {
                foldersList = customerFileRepository.getFileIdsByCustomerAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = customerFileRepository.getFileIdsByCustomerAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.REQUIREMENTDOCUMENT)) {
                foldersList = requirementDocumentFileRepository.getFileIdsByDocumentRevisionAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = requirementDocumentFileRepository.getFileIdsByDocumentRevisionAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.REQUIREMENT)) {
                PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findOne(id);
                foldersList = requirementFileRepository.getFileIdsByRequirementAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(children.getRequirementVersion().getId(), "FOLDER");
                filesList = requirementFileRepository.getFileIdsByRequirementAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(children.getRequirementVersion().getId(), "FILE");
            } else if (objectType.equals(PLMObjectType.PLMNPR)) {
                foldersList = nprFileRepository.getFileIdsByNprAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = nprFileRepository.getFileIdsByNprAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.MBOMREVISION)) {
                foldersList = mbomFileRepository.getFileIdsByMbomRevisionAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mbomFileRepository.getFileIdsByMbomRevisionAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.MBOMINSTANCE)) {
                foldersList = mbomInstanceFileRepository.getFileIdsByMbomInstanceAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mbomInstanceFileRepository.getFileIdsByMbomInstanceAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.MBOM)) {
                foldersList = mbomFileRepository.getFileIdsByMbomRevisionAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mbomFileRepository.getFileIdsByMbomRevisionAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.BOPREVISION)) {
                foldersList = bopFileRepository.getFileIdsByBopAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = bopFileRepository.getFileIdsByBopAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.BOPROUTEOPERATION)) {
                foldersList = bopOperationFileRepository.getFileIdsByBopOperationAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = bopOperationFileRepository.getFileIdsByBopOperationAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.BOPINSTANCEROUTEOPERATION)) {
                foldersList = bopInstanceOperationFileRepository.getFileIdsByBopOperationAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = bopInstanceOperationFileRepository.getFileIdsByBopOperationAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.PROGRAM)) {
                foldersList = programFileRepository.getFileIdsByProgramAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
                filesList = programFileRepository.getFileIdsByProgramAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.CHANGE)) {
                foldersList = changeFileRepository.getFileIdsByChangeAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = changeFileRepository.getFileIdsByChangeAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.OPERATION)) {
                foldersList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.SHIFT)) {
                foldersList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.MATERIAL)) {
                foldersList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.JIGFIXTURE)) {
                foldersList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.TOOL)) {
                foldersList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.INSTRUMENT)) {
                foldersList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.EQUIPMENT)) {
                foldersList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.MACHINE)) {
                foldersList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.WORKCENTER)) {
                foldersList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.ASSEMBLYLINE)) {
                foldersList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            } else if (objectType.equals(PLMObjectType.PLANT)) {
                foldersList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FOLDER");
                filesList = mesObjectFileRepository.getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(id, "FILE");
            }
            if (foldersList.size() > 0) {
                folders = convertFilesIdsToDtoList(id, objectType, foldersList, hierarchy);
            }
            if (filesList.size() > 0) {
                files = convertFilesIdsToDtoList(id, objectType, filesList, hierarchy);
            }
            String documentType = "FILE";
            if (objectType.name() == "MFRPARTINSPECTIONREPORT") {
                documentType = objectType.name();
            }
            List<PLMObjectDocument> objectDocuments = objectDocumentRepository.findByObjectAndFolderIsNullAndDocumentType(id, documentType);
            for (PLMObjectDocument objectDocument : objectDocuments) {
                files.add(convertObjectDocumentToDto(objectDocument));
            }

            objectFileDto.getObjectFiles().addAll(folders);
            objectFileDto.getObjectFiles().addAll(files);
        }
        return objectFileDto;
    }

    @Transactional(readOnly = true)
    public ObjectFileDto getObjectFilesByType(Integer id, PLMObjectType objectType, Boolean hierarchy) {
        ObjectFileDto objectFileDto = new ObjectFileDto();
        List<FileDto> folders = new ArrayList<>();
        List<FileDto> files = new ArrayList<>();
        List<Integer> foldersList = new ArrayList<>();
        List<Integer> filesList = new ArrayList<>();
        if (objectType.equals(PLMObjectType.PROJECT)) {
            foldersList = projectFileRepository.getFileIdsByProjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
            filesList = projectFileRepository.getFileIdsByProjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FILE");
            if (foldersList.size() > 0) {
                folders = projectService.convertFilesIdsToDtoList(id, objectType, foldersList, hierarchy);
            }
            if (filesList.size() > 0) {
                files = projectService.convertFilesIdsToDtoList(id, objectType, filesList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.TEMPLATE)) {
            foldersList = projectTemplateFileRepository.getFileIdsByTemplateAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
            if (foldersList.size() > 0) {
                folders = projectTemplateService.convertFilesIdsToDtoList(id, objectType, foldersList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.TEMPLATEACTIVITY)) {
            foldersList = projectTemplateActivityFileRepository.getFileIdsByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
            if (foldersList.size() > 0) {
                folders = projectTemplateActivityService.convertActivityFilesIdsToDtoList(id, objectType, foldersList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.TEMPLATETASK)) {
            foldersList = projectTemplateTaskFileRepository.getFileIdsByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
            if (foldersList.size() > 0) {
                folders = projectTemplateActivityService.convertFilesIdsToDtoList(id, objectType, foldersList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.PROGRAMTEMPLATE)) {
            foldersList = programTemplateFileRepository.getFileIdsByTemplateAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
            if (foldersList.size() > 0) {
                folders = programTemplateService.convertFilesIdsToDtoList(id, objectType, foldersList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            foldersList = activityFileRepository.getFileIdsByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
            filesList = activityFileRepository.getFileIdsByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FILE");
            if (foldersList.size() > 0) {
                folders = activityService.convertActivityFilesIdsToDtoList(id, objectType, foldersList, hierarchy);
            }
            if (filesList.size() > 0) {
                files = activityService.convertActivityFilesIdsToDtoList(id, objectType, filesList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            foldersList = taskFileRepository.getFileIdsByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
            filesList = taskFileRepository.getFileIdsByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FILE");
            if (foldersList.size() > 0) {
                folders = activityService.convertFilesIdsToDtoList(id, objectType, foldersList, hierarchy);
            }
            if (filesList.size() > 0) {
                files = activityService.convertFilesIdsToDtoList(id, objectType, filesList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            foldersList = programFileRepository.getFileIdsByProgramAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FOLDER");
            filesList = programFileRepository.getFileIdsByProgramAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(id, "FILE");
            if (foldersList.size() > 0) {
                folders = programFileService.convertFilesIdsToDtoList(id, objectType, foldersList, hierarchy);
            }
            if (filesList.size() > 0) {
                files = programFileService.convertFilesIdsToDtoList(id, objectType, filesList, hierarchy);
            }
        }
        String documentType = "FILE";
        List<PLMObjectDocument> objectDocuments = objectDocumentRepository.findByObjectAndFolderIsNullAndDocumentType(id, documentType);
        for (PLMObjectDocument objectDocument : objectDocuments) {
            files.add(convertObjectDocumentToDto(objectDocument));
        }
        if (folders.size() > 0) {
            Collections.sort(folders, new Comparator<FileDto>() {
                private final Comparator<FileDto> NATURAL_SORT = new WindowsExplorerComparator();

                @Override
                public int compare(FileDto o1, FileDto o2) {
                    return NATURAL_SORT.compare(o1, o2);
                }
            });
        }
        if (files.size() > 0) {
            Collections.sort(files, new Comparator<FileDto>() {
                private final Comparator<FileDto> NATURAL_SORT = new WindowsExplorerComparator();

                @Override
                public int compare(FileDto o1, FileDto o2) {
                    return NATURAL_SORT.compare(o1, o2);
                }
            });
        }
        objectFileDto.getObjectFiles().addAll(folders);
        objectFileDto.getObjectFiles().addAll(files);
        return objectFileDto;
    }

    @Transactional(readOnly = true)
    public ObjectFileDto getObjectFileChildrensByType(Integer folderId, PLMObjectType objectType, Boolean hierarchy) {
        ObjectFileDto objectFileDto = new ObjectFileDto();
        List<FileDto> folders = new ArrayList<>();
        List<FileDto> files = new ArrayList<>();
        List<Integer> foldersList = new ArrayList<>();
        List<Integer> filesList = new ArrayList<>();
        Integer objectId = 0;
        if (objectType.equals(PLMObjectType.PROJECT)) {
            foldersList = projectFileRepository.getByParentFileAndLatestTrueAndFileType(folderId, "FOLDER");
            filesList = projectFileRepository.getByParentFileAndLatestTrueAndFileType(folderId, "FILE");
            PLMProjectFile projectFile = projectFileRepository.findOne(folderId);
            objectId = projectFile.getProject();
            if (foldersList.size() > 0) {
                folders = projectService.convertFilesIdsToDtoList(projectFile.getProject(), objectType, foldersList, hierarchy);
            }
            if (filesList.size() > 0) {
                files = projectService.convertFilesIdsToDtoList(projectFile.getProject(), objectType, filesList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.TEMPLATE)) {
            ProjectTemplateFile projectFile = projectTemplateFileRepository.findOne(folderId);
            objectId = projectFile.getTemplate();
            foldersList = projectTemplateFileRepository.getByParentFileAndLatestTrueAndFileType(folderId, "FOLDER");
            if (foldersList.size() > 0) {
                folders = projectTemplateService.convertFilesIdsToDtoList(projectFile.getTemplate(), objectType, foldersList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.TEMPLATEACTIVITY)) {
            ProjectTemplateActivityFile projectTemplateActivityFile = projectTemplateActivityFileRepository.findOne(folderId);
            objectId = projectTemplateActivityFile.getActivity();
            foldersList = projectTemplateActivityFileRepository.getByParentFileAndLatestTrueAndFileType(folderId, "FOLDER");
            if (foldersList.size() > 0) {
                folders = projectTemplateActivityService.convertActivityFilesIdsToDtoList(projectTemplateActivityFile.getActivity(), objectType, foldersList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.TEMPLATETASK)) {
            ProjectTemplateTaskFile templateTaskFile = projectTemplateTaskFileRepository.findOne(folderId);
            objectId = templateTaskFile.getTask();
            foldersList = projectTemplateTaskFileRepository.getByParentFileAndLatestTrueAndFileType(folderId, "FOLDER");
            if (foldersList.size() > 0) {
                folders = projectTemplateActivityService.convertFilesIdsToDtoList(templateTaskFile.getTask(), objectType, foldersList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.PROGRAMTEMPLATE)) {
            ProgramTemplateFile programTemplateFile = programTemplateFileRepository.findOne(folderId);
            objectId = programTemplateFile.getTemplate();
            foldersList = programTemplateFileRepository.getByParentFileAndLatestTrueAndFileType(folderId, "FOLDER");
            if (foldersList.size() > 0) {
                folders = programTemplateService.convertFilesIdsToDtoList(programTemplateFile.getTemplate(), objectType, foldersList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            foldersList = activityFileRepository.getByParentFileAndLatestTrueAndFileType(folderId, "FOLDER");
            filesList = activityFileRepository.getByParentFileAndLatestTrueAndFileType(folderId, "FILE");
            PLMActivityFile activityFile = activityFileRepository.findOne(folderId);
            objectId = activityFile.getActivity();
            if (foldersList.size() > 0) {
                folders = activityService.convertActivityFilesIdsToDtoList(activityFile.getActivity(), objectType, foldersList, hierarchy);
            }
            if (filesList.size() > 0) {
                files = activityService.convertActivityFilesIdsToDtoList(activityFile.getActivity(), objectType, filesList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            foldersList = taskFileRepository.getByParentFileAndLatestTrueAndFileType(folderId, "FOLDER");
            filesList = taskFileRepository.getByParentFileAndLatestTrueAndFileType(folderId, "FILE");
            PLMTaskFile taskFile = taskFileRepository.findOne(folderId);
            objectId = taskFile.getTask();
            if (foldersList.size() > 0) {
                folders = activityService.convertFilesIdsToDtoList(taskFile.getTask(), objectType, foldersList, hierarchy);
            }
            if (filesList.size() > 0) {
                files = activityService.convertFilesIdsToDtoList(taskFile.getTask(), objectType, filesList, hierarchy);
            }
        } else if (objectType.equals(PLMObjectType.PROGRAM)) {
            foldersList = programFileRepository.getByParentFileAndLatestTrueAndFileType(folderId, "FOLDER");
            filesList = programFileRepository.getByParentFileAndLatestTrueAndFileType(folderId, "FILE");
            PLMProgramFile programFile = programFileRepository.findOne(folderId);
            objectId = programFile.getProgram();
            if (foldersList.size() > 0) {
                folders = programFileService.convertFilesIdsToDtoList(programFile.getProgram(), objectType, foldersList, hierarchy);
            }
            if (filesList.size() > 0) {
                files = programFileService.convertFilesIdsToDtoList(programFile.getProgram(), objectType, filesList, hierarchy);
            }
        }
        String documentType = "FILE";
        List<PLMObjectDocument> objectDocuments = objectDocumentRepository.findByObjectAndFolderAndDocumentType(objectId, folderId, documentType);
        for (PLMObjectDocument objectDocument : objectDocuments) {
            files.add(convertObjectDocumentToDto(objectDocument));
        }
        if (folders.size() > 0) {
            Collections.sort(folders, new Comparator<FileDto>() {
                private final Comparator<FileDto> NATURAL_SORT = new WindowsExplorerComparator();

                @Override
                public int compare(FileDto o1, FileDto o2) {
                    return NATURAL_SORT.compare(o1, o2);
                }
            });
        }
        if (files.size() > 0) {
            Collections.sort(files, new Comparator<FileDto>() {
                private final Comparator<FileDto> NATURAL_SORT = new WindowsExplorerComparator();

                @Override
                public int compare(FileDto o1, FileDto o2) {
                    return NATURAL_SORT.compare(o1, o2);
                }
            });
        }
        objectFileDto.getObjectFiles().addAll(folders);
        objectFileDto.getObjectFiles().addAll(files);
        return objectFileDto;
    }

    @Transactional
    public void copyFileAttributes(Integer oldFile, Integer newFile) {
        List<ObjectAttribute> oldFileAttributes = objectAttributeRepository.findByObjectId(oldFile);
        if (oldFileAttributes.size() > 0) {
            for (ObjectAttribute objectAttribute : oldFileAttributes) {
                ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(objectAttribute.getId().getAttributeDef());
                ObjectAttribute objectAttribute1 = (ObjectAttribute) Utils.cloneObject(objectAttribute, ObjectAttribute.class);
                objectAttribute1.setId(new ObjectAttributeId(newFile, objectAttribute.getId().getAttributeDef()));
                if (objectAttribute1.getAttachmentValues().length > 0) {
                    List<AttributeAttachment> attachments = attributeAttachmentService.getMultipleAttributeAttachments(Arrays.asList(objectAttribute1.getAttachmentValues()));
                    List<Integer> integers = new ArrayList<>();
                    for (AttributeAttachment attachment : attachments) {
                        AttributeAttachment attachment1 = new AttributeAttachment();
                        attachment1.setObjectId(newFile);
                        attachment1.setAttributeDef(attachment.getAttributeDef());
                        attachment1.setExtension(attachment.getExtension());
                        attachment1.setAddedBy(attachment.getAddedBy());
                        attachment1.setObjectType(attachment.getObjectType());
                        attachment1.setAddedOn(new Date());
                        attachment1.setName(attachment.getName());
                        attachment1.setSize(attachment.getSize());
                        attachment1 = attributeAttachmentRepository.save(attachment1);
                        integers.add(attachment1.getId());
                        try {
                            String dir = "";
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator + "filesystem" + File.separator + "attachments" + File.separator + newFile;
                            File folder = new File(dir);
                            if (!folder.exists()) {
                                folder.mkdirs();
                            }
                            dir = dir + File.separator + attachment1.getId();
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.createNewFile();
                            }
                            String oldFileDir = "";

                            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator + "filesystem" + File.separator + "attachments" + File.separator + oldFile;
                            FileInputStream instream = null;
                            FileOutputStream outstream = null;
                            File infile = new File(oldFileDir);
                            File outfile = new File(dir);
                            instream = new FileInputStream(infile);
                            outstream = new FileOutputStream(outfile);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = instream.read(buffer)) > 0) {
                                outstream.write(buffer, 0, length);
                            }
                            instream.close();
                            outstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    objectAttribute1.setAttachmentValues(integers.stream().filter(Objects::nonNull).toArray(Integer[]::new));
                }
                objectAttribute1 = objectAttributeRepository.save(objectAttribute1);
            }
        }
    }

    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'download',#type) || @documentService.checkDMPemrmissions(authentication, 'download', #objectType.name(), #fileId) || @customePrivilegeFilter.filterPrivilage(authentication,'teamDownload',#type)")
    public void downloadFile(Integer id, PLMObjectType objectType, Integer fileId, HttpServletResponse response, String type) throws DocumentException {
        CassiniObject cassiniObject = objectRepository.findById(fileId);
        PLMFile plmFile = null;
        if (cassiniObject != null && cassiniObject.getObjectType().name().equals(PLMObjectType.OBJECTDOCUMENT.name())) {
            PLMObjectDocument objectDocument = objectDocumentRepository.findOne(fileId);
            plmFile = fileRepository.findOne(objectDocument.getDocument().getId());
        } else {
            plmFile = fileRepository.findOne(fileId);
        }
        File file = getFile(id, plmFile.getId(), objectType);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, '"' + plmFile.getName() + '"', file);
        }
    }

    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'preview',#type) || @documentService.checkDMPemrmissions(authentication, 'preview', #objectType.name(), #fileId)")
    public void previewFile(Integer id, PLMObjectType objectType, Integer fileId, HttpServletResponse response, String type) throws Exception {
        CassiniObject cassiniObject = objectRepository.findById(fileId);
        PLMFile plmFile = null;
        if (cassiniObject != null && cassiniObject.getObjectType().name().equals(PLMObjectType.OBJECTDOCUMENT.name())) {
            PLMObjectDocument objectDocument = objectDocumentRepository.findOne(fileId);
            plmFile = fileRepository.findOne(objectDocument.getDocument().getId());
        } else {
            plmFile = fileRepository.findOne(fileId);
        }
        File file = getFile(id, plmFile.getId(), objectType);
        String fileName = plmFile.getName();
        if (file != null) {
            try {
                String e = URLDecoder.decode(fileName, "UTF-8");
                response.setHeader("Content-disposition", "inline; filename=" + e);
            } catch (UnsupportedEncodingException var6) {
                response.setHeader("Content-disposition", "inline; filename=" + fileName);
            }
            ServletOutputStream e1 = response.getOutputStream();
            IOUtils.copy(new FileInputStream(file), e1);
            e1.flush();
        }
    }

    private List<FileDto> convertFilesIdsToDtoList(Integer object, PLMObjectType objectType, List<Integer> fileIds, Boolean hierarchy) {
        List<FileDto> filesDto = new ArrayList<>();
        List<PLMFile> files = new ArrayList<>();
        if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            files = fileRepository.findByIdInOrderByIdAsc(fileIds);
        } else {
            files = fileRepository.findByIdIn(fileIds);
        }
        HashMap<Integer, PLMDocument> documentMap = new HashMap<>();
        HashMap<Integer, PLMMfrPartInspectionReport> inspectionReportMap = new HashMap<>();
        if (objectType.equals(PLMObjectType.DOCUMENT)) {
            List<PLMDocument> documents = plmDocumentRepository.findByIdIn(fileIds);
            documents.forEach(plmDocument -> {
                documentMap.put(plmDocument.getId(), plmDocument);
            });
        } else if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            List<PLMMfrPartInspectionReport> mfrPartInspectionReports = mfrPartInspectionReportRepository.findByIdIn(fileIds);
            mfrPartInspectionReports.forEach(mfrPartInspectionReport -> {
                inspectionReportMap.put(mfrPartInspectionReport.getId(), mfrPartInspectionReport);
            });
        }

        List<PLMFile> plmFiles = fileRepository.getParentChildrenByFileType(fileIds, "FILE");
        List<String> fileNos = fileRepository.getFileNosByIds(fileIds);
        List<PLMFile> fileNoFiles = fileRepository.findByFileNosOrderByIdAsc(fileNos);
        List<Integer> personIds = fileRepository.getCreatedByIds(fileIds);
        personIds.addAll(fileRepository.getModifiedByIds(fileIds));
        fileNoFiles.forEach(plmFile1 -> {
            personIds.add(plmFile1.getCreatedBy());
        });
        List<Integer> filterIds = personIds.stream().distinct().collect(Collectors.toList());
        List<Person> persons = personRepository.findByIdIn(filterIds);
        List<Login> logins = loginRepository.getLoginsByPersonIds(filterIds);
        List<PLMFile> fileCountList = fileRepository.getChildrenCountByParentFileAndLatestTrueByIds(fileIds);
        List<PLMObjectDocument> objectDocuments = objectDocumentRepository.getDocumentsCountByObjectIdAndFolderIds(object, fileIds);
        Map<Integer, List<PLMFile>> childrenMap = new HashMap<>();
        Map<String, List<PLMFile>> fileNosMap = new HashMap();
        Map<Integer, List<PLMFile>> fileCountsMap = new HashMap();
        Map<Integer, List<PLMObjectDocument>> objectDocumentCountsMap = new HashMap();
        Map<Integer, Person> personsMap = new HashMap();
        Map<Integer, Login> loginsMap = new HashMap();

        personsMap = persons.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
        loginsMap = logins.stream().collect(Collectors.toMap(x -> x.getPerson().getId(), x -> x));
        childrenMap = plmFiles.stream().collect(Collectors.groupingBy(d -> d.getParentFile()));
        objectDocumentCountsMap = objectDocuments.stream().collect(Collectors.groupingBy(d -> d.getFolder()));
        fileNosMap = fileNoFiles.stream().collect(Collectors.groupingBy(d -> d.getFileNo()));
        fileCountsMap = fileCountList.stream().collect(Collectors.groupingBy(d -> d.getParentFile()));

        List<PLMSharedObject> shareObjects = sharedObjectRepository.getSharedObjectByObjectIdsInAndPerson(fileIds, sessionWrapper.getSession().getLogin().getPerson().getId());
        Map<Integer, List<PLMSharedObject>> shareObjsCountMap = new HashMap();
        shareObjsCountMap = shareObjects.stream().collect(Collectors.groupingBy(d -> d.getObjectId()));
        Map<Integer, List<PLMFile>> fileChildrenMap = childrenMap;
        Map<String, List<PLMFile>> fileNoMap = fileNosMap;
        Map<Integer, List<PLMFile>> fileCountMap = fileCountsMap;
        Map<Integer, List<PLMObjectDocument>> objectDocumentCountMap = objectDocumentCountsMap;
        Map<Integer, Person> personMap = personsMap;
        Map<Integer, Login> loginMap = loginsMap;
        Map<Integer, List<PLMSharedObject>> finalShareObjectMap = shareObjsCountMap;
        files.forEach(plmFile -> {
            FileDto fileDto = new FileDto();
            fileDto.setId(plmFile.getId());
            fileDto.setName(plmFile.getName());
            fileDto.setObject(object);
            fileDto.setParentObject(objectType);
            fileDto.setDescription(plmFile.getDescription());
            fileDto.setFileNo(plmFile.getFileNo());
            fileDto.setFileType(plmFile.getFileType());
            fileDto.setParentFile(plmFile.getParentFile());
            fileDto.setSize(plmFile.getSize());
            fileDto.setLatest(plmFile.getLatest());
            fileDto.setVersion(plmFile.getVersion());
            fileDto.setLocked(plmFile.getLocked());
            fileDto.setLockedBy(plmFile.getLockedBy());
            fileDto.setLockedDate(plmFile.getLockedDate());
            fileDto.setThumbnail(plmFile.getThumbnail());
            List<PLMSharedObject> existingShareObjects = finalShareObjectMap.containsKey(plmFile.getId()) ? finalShareObjectMap.get(plmFile.getId()) : new ArrayList<>();
            if (existingShareObjects.size() > 0) {
                fileDto.setShared(true);
            }
            fileDto.setChildFileCount(fileChildrenMap.containsKey(plmFile.getId()) ? fileChildrenMap.get(plmFile.getId()).size() : 0);
            if (fileDto.getLockedBy() != null) {
                fileDto.setLockedByName(personRepository.findOne(plmFile.getLockedBy()).getFullName());
            }
            if (!plmFile.getObjectType().equals(PLMObjectType.FILE)) {
                PLMDocument document = documentMap.get(plmFile.getId());
                PLMMfrPartInspectionReport mfrPartInspectionReport = inspectionReportMap.get(plmFile.getId());
                if (document != null) {
                    fileDto.setSignOffCount(documentReviewerRepository.getDocumentReviewersCount(document.getId()));
                    fileDto.setReviewers(documentReviewerRepository.getDocumentReviewerIds(document.getId()));
                    fileDto.setRevision(document.getRevision());
                    fileDto.setLifeCyclePhase(document.getLifeCyclePhase());
                } else if (mfrPartInspectionReport != null) {
                    fileDto.setSignOffCount(documentReviewerRepository.getDocumentReviewersCount(mfrPartInspectionReport.getId()));
                    fileDto.setReviewers(documentReviewerRepository.getDocumentReviewerIds(mfrPartInspectionReport.getId()));
                    fileDto.setRevision(mfrPartInspectionReport.getRevision());
                    fileDto.setLifeCyclePhase(mfrPartInspectionReport.getLifeCyclePhase());
                } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                    PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(plmFile.getId());
                    fileDto.setSignOffCount(documentReviewerRepository.getDocumentReviewersCount(plmFile.getId()));
                    fileDto.setReviewers(documentReviewerRepository.getDocumentReviewerIds(plmFile.getId()));
                    fileDto.setLifeCyclePhase(pqmppapChecklist.getLifeCyclePhase());
                    fileDto.setRevision(pqmppapChecklist.getRevision());
                }
            }
            fileDto.setObjectType(PLMObjectType.valueOf(plmFile.getObjectType().toString()));
            fileDto.setCreatedDate(plmFile.getCreatedDate());
            fileDto.setModifiedDate(plmFile.getModifiedDate());
            List<PLMFile> initialVersionFiles = fileNoMap.containsKey(plmFile.getFileNo()) ? fileNoMap.get(plmFile.getFileNo()) : new ArrayList<PLMFile>();
            Person person = null;
            if (initialVersionFiles.size() > 0) {
                person = personMap.get(initialVersionFiles.get(0).getCreatedBy());
            } else {
                person = personMap.get(plmFile.getCreatedBy());
            }
            fileDto.setCreatedByName(person.getFullName());
            fileDto.setCreatedBy(person.getId());
            Login login = loginMap.get(person.getId());
            fileDto.setExternal(login.getExternal());
            fileDto.setModifiedByName(personMap.get(plmFile.getModifiedBy()).getFullName());
            fileDto.setReplaceFileName(plmFile.getReplaceFileName());
            fileDto.setUrn(plmFile.getUrn());
            if (fileDto.getFileType().equals("FOLDER")) {
                fileDto.setCount(fileCountMap.containsKey(fileDto.getId()) ? fileCountMap.get(fileDto.getId()).size() : 0);
                fileDto.setCount(fileDto.getCount() + (objectDocumentCountMap.containsKey(fileDto.getId()) ? objectDocumentCountMap.get(fileDto.getId()).size() : 0));
                if (hierarchy) {
                    visitChildren(object, objectType, fileDto, hierarchy);
                }
            }
            filesDto.add(fileDto);
        });
        if (filesDto.size() > 0) {
            Collections.sort(filesDto, new Comparator<FileDto>() {
                private final Comparator<FileDto> NATURAL_SORT = new WindowsExplorerComparator();

                @Override
                public int compare(FileDto o1, FileDto o2) {
                    return NATURAL_SORT.compare(o1, o2);
                }
            });
        }
        return filesDto;
    }


    public FileDto visitChildren(Integer object, PLMObjectType objectType, FileDto fileDto, Boolean hierarchy) {
        List<Integer> foldersList = fileRepository.getByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(fileDto.getId(), "FOLDER");
        List<Integer> filesList = fileRepository.getByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(fileDto.getId(), "FILE");
        if (foldersList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, foldersList, hierarchy));
        }
        if (filesList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, filesList, hierarchy));
        }
        return fileDto;
    }

    public FileDto convertFileIdToDto(Integer object, PLMObjectType objectType, Integer fileId) {
        PLMFile plmFile = fileRepository.findOne(fileId);
        PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
        FileDto fileDto = new FileDto();
        fileDto.setId(plmFile.getId());
        fileDto.setName(plmFile.getName());
        fileDto.setObject(object);
        fileDto.setParentObject(objectType);
        fileDto.setDescription(plmFile.getDescription());
        fileDto.setFileNo(plmFile.getFileNo());
        fileDto.setFileType(plmFile.getFileType());
        fileDto.setParentFile(plmFile.getParentFile());
        fileDto.setSize(plmFile.getSize());
        fileDto.setLatest(plmFile.getLatest());
        fileDto.setVersion(plmFile.getVersion());
        fileDto.setLocked(plmFile.getLocked());
        fileDto.setLockedBy(plmFile.getLockedBy());
        fileDto.setLockedDate(plmFile.getLockedDate());
        fileDto.setThumbnail(plmFile.getThumbnail());
        if (fileDto.getLockedBy() != null) {
            fileDto.setLockedByName(personRepository.findOne(plmFile.getLockedBy()).getFullName());
        }
        if (plmDocument != null) {
            fileDto.setSignOffCount(documentReviewerRepository.getDocumentReviewersCount(plmDocument.getId()));
            fileDto.setReviewers(documentReviewerRepository.getDocumentReviewerIds(plmDocument.getId()));
            fileDto.setLifeCyclePhase(plmDocument.getLifeCyclePhase());
            fileDto.setRevision(plmDocument.getRevision());
        }
        if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(fileId);
            if (mfrPartInspectionReport != null) {
                fileDto.setSignOffCount(documentReviewerRepository.getDocumentReviewersCount(fileId));
                fileDto.setReviewers(documentReviewerRepository.getDocumentReviewerIds(fileId));
                fileDto.setLifeCyclePhase(mfrPartInspectionReport.getLifeCyclePhase());
                fileDto.setRevision(mfrPartInspectionReport.getRevision());
            }
        }
        if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(fileId);
            if (pqmppapChecklist != null) {
                fileDto.setSignOffCount(documentReviewerRepository.getDocumentReviewersCount(fileId));
                fileDto.setReviewers(documentReviewerRepository.getDocumentReviewerIds(fileId));
                fileDto.setLifeCyclePhase(pqmppapChecklist.getLifeCyclePhase());
                fileDto.setRevision(pqmppapChecklist.getRevision());
            }
        }
        fileDto.setObjectType(PLMObjectType.valueOf(plmFile.getObjectType().toString()));
        fileDto.setCreatedDate(plmFile.getCreatedDate());
        fileDto.setModifiedDate(plmFile.getModifiedDate());
        Person person = personRepository.findOne(plmFile.getCreatedBy());
        fileDto.setCreatedByName(person.getFullName());
        fileDto.setCreatedBy(plmFile.getCreatedBy());
        Login login = loginRepository.findByPersonId(person.getId());
        fileDto.setExternal(login.getExternal());
        fileDto.setModifiedByName(personRepository.findOne(plmFile.getModifiedBy()).getFullName());
        fileDto.setReplaceFileName(plmFile.getReplaceFileName());
        fileDto.setUrn(plmFile.getUrn());
        if (fileDto.getFileType().equals("FOLDER")) {
            fileDto.setCount(fileRepository.getChildrenCountByParentFileAndLatestTrue(fileDto.getId()));
            fileDto.setCount(fileDto.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(fileDto.getObject(), fileDto.getId()));
        }
        return fileDto;
    }

    public FileDto convertObjectDocumentToDto(PLMObjectDocument objectDocument) {
        FileDto fileDto = new FileDto();
        fileDto.setId(objectDocument.getId());
        fileDto.setName(objectDocument.getDocument().getName());
        fileDto.setObject(objectDocument.getObject());
        fileDto.setFolder(objectDocument.getFolder());
        fileDto.setDescription(objectDocument.getDocument().getDescription());
        fileDto.setFileNo(objectDocument.getDocument().getFileNo());
        fileDto.setFileType(objectDocument.getDocument().getFileType());
        fileDto.setParentFile(objectDocument.getFolder());
        fileDto.setSize(objectDocument.getDocument().getSize());
        fileDto.setLatest(objectDocument.getDocument().getLatest());
        fileDto.setVersion(objectDocument.getDocument().getVersion());
        fileDto.setLocked(objectDocument.getDocument().getLocked());
        fileDto.setLockedBy(objectDocument.getDocument().getLockedBy());
        fileDto.setLockedDate(objectDocument.getDocument().getLockedDate());
        fileDto.setThumbnail(objectDocument.getDocument().getThumbnail());
        if (fileDto.getLockedBy() != null) {
            fileDto.setLockedByName(personRepository.findOne(objectDocument.getDocument().getLockedBy()).getFullName());
        }
        fileDto.setRevision(objectDocument.getDocument().getRevision());
        fileDto.setLifeCyclePhase(objectDocument.getDocument().getLifeCyclePhase());
        fileDto.setObjectType(PLMObjectType.valueOf(objectDocument.getObjectType().toString()));
        fileDto.setCreatedDate(objectDocument.getCreatedDate());
        fileDto.setModifiedDate(objectDocument.getModifiedDate());
        List<PLMDocument> initialVersionFiles = plmDocumentRepository.findByFileNoOrderByIdAsc(fileDto.getFileNo());
        if (initialVersionFiles.size() > 0) {
            Person person = personRepository.findOne(initialVersionFiles.get(0).getCreatedBy());
            fileDto.setCreatedByName(person.getFullName());
            fileDto.setCreatedBy(objectDocument.getDocument().getCreatedBy());
        } else {
            Person person = personRepository.findOne(objectDocument.getDocument().getCreatedBy());
            fileDto.setCreatedByName(person.getFullName());
            fileDto.setCreatedBy(objectDocument.getDocument().getCreatedBy());
        }
        fileDto.setModifiedByName(personRepository.findOne(objectDocument.getDocument().getModifiedBy()).getFullName());
        fileDto.setReplaceFileName(objectDocument.getDocument().getReplaceFileName());
        fileDto.setUrn(objectDocument.getDocument().getUrn());
        fileDto.setFilePath(getDocumentFilePath(objectDocument.getDocument()));
        return fileDto;
    }

    private String getDocumentFilePath(PLMDocument plmDocument) {
        String filePath = plmDocument.getName();
        if (plmDocument.getParentFile() != null) {
            filePath = visitParentDocumentPath(plmDocument.getParentFile(), filePath);
        }
        return filePath;
    }

    private String visitParentDocumentPath(Integer fileId, String filePath) {
        PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
        filePath = plmDocument.getName() + " / " + filePath;
        if (plmDocument.getParentFile() != null) {
            filePath = visitParentDocumentPath(plmDocument.getParentFile(), filePath);
        }
        return filePath;
    }

    public void sendFileCommentNotification(Integer id, Integer fileId, PLMObjectType objectType, String type, Comment comment) {
        PLMFile plmFile = fileRepository.findOne(fileId);
        List<PLMFile> files = fileRepository.findByFileNoOrderByIdAsc(plmFile.getFileNo());
        String email = "";
        List<Person> persons = new ArrayList<>();

        String notificationType = "QACommentToExternal";
        String[] recipientAddress = new String[0];
        String subject = "";
        if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            subject = "Inspection Report Comment Notification";
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            subject = "PPAP Checklist Comment Notification";
        }
        if (type.equals("qa")) {
            notificationType = "ExternalCommentToQA";
            persons = getQARolePerson("DEFAULT_QUALITY_ADMINISTRATOR_ROLE");
            persons.addAll(getQARolePerson("DEFAULT_QUALITY_ANALYST_ROLE"));

            List<Integer> personIds = documentReviewerRepository.getReviewerIdsByDocument(fileId);
            if (personIds.size() > 0) {
                persons.addAll(personRepository.findByIdIn(personIds));
            }

            if (persons.size() > 0) {
                recipientAddress = new String[persons.size()];
                for (int i = 0; i < persons.size(); i++) {
                    Person subscribe = persons.get(i);
                    if (email.equals("")) {
                        email = subscribe.getEmail();
                    } else {
                        email = email + "," + subscribe.getEmail();
                    }
                }
                String[] recipientList = email.split(",");
                int counter = 0;
                for (String recipient : recipientList) {
                    recipientAddress[counter] = recipient;
                    counter++;
                }
            }
        }
        String objectName = "";
        String fileType = "";
        String typeName = "";
        if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT) || objectType.equals(PLMObjectType.MANUFACTURERPART)) {
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(id);
            objectName = manufacturerPart.getPartNumber();
            typeName = "manufacturer part";
            fileType = "inspection report";
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            PQMPPAP pqmppap = ppapRepository.findOne(id);
            objectName = pqmppap.getNumber();
            typeName = "ppap";
            fileType = "checklist";
        }

        Person person = personRepository.findOne(files.get(0).getCreatedBy());
        final String notificationTypeFinal = notificationType;
        final String objectDetails = objectName;
        final String fileTypeDetails = fileType;
        final String typeNameDetails = typeName;
        final String subjectName = subject;
        final String[] recipientAddressFinal = recipientAddress;
        final List<Person> personList = persons;
        String tenantId = sessionWrapper.getSession().getTenantId();
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String host = url.substring(0, url.indexOf(uri));
        new Thread(() -> {
            Map<String, Object> model = new HashMap<>();
            model.put("host", host);
            model.put("fileName", plmFile.getName());
            model.put("fileType", fileTypeDetails);
            model.put("typeName", typeNameDetails);
            model.put("commentByName", comment.getCommentedBy().getFullName());
            model.put("objectName", objectDetails);
            model.put("comment", comment.getComment());
            model.put("tenantId", tenantId);
            model.put("notificationType", notificationTypeFinal);
            Mail mail = new Mail();
            mail.setMailSubject(subjectName);
            mail.setTemplatePath("email/fileCommentNotification.html");
            mail.setModel(model);
            if (type.equals("qa")) {
                if (recipientAddressFinal.length == 1) {
                    model.put("personName", personList.get(0).getFullName());
                    mail.setMailTo(recipientAddressFinal[0]);
                    mailService.sendEmail(mail);
                } else if (recipientAddressFinal.length > 1) {
                    model.put("personName", "All");
                    mail.setMailToList(recipientAddressFinal);
                    mailService.sendEmail(mail);
                }
            } else {
                model.put("personName", person.getFullName());
                mail.setMailTo(person.getEmail());
                mailService.sendEmail(mail);
            }
        }).start();
    }

    public List<Person> getQARolePerson(String name) {
        List<Person> persons = new ArrayList<>();
        Preference pref = preferenceRepository.findByPreferenceKey(name);
        PersonGroup personGroup = null;
        if (pref != null) {
            String json = pref.getJsonValue();
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = objectMapper.readTree(json);
                    Integer id = jsonNode.get("typeId").asInt();
                    if (id != null) {
                        personGroup = personGroupRepository.findOne(id);
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        if (personGroup != null) {
            List<GroupMember> groupMembers = groupMemberRepository.findByPersonGroup(personGroup);
            groupMembers.forEach(groupMember -> {
                Login login = loginRepository.findByPersonId(groupMember.getPerson().getId());
                if (login != null && login.getIsActive()) {
                    persons.add(groupMember.getPerson());
                }
            });
        }
        return persons;
    }

    public void sendDocumentUploadedNotification(List<PLMFile> files, String uploadType, Integer objectId, Login login, PLMObjectType objectType) {
        List<Person> persons = getQARolePerson("DEFAULT_QUALITY_ADMINISTRATOR_ROLE");
        persons.addAll(getQARolePerson("DEFAULT_QUALITY_ANALYST_ROLE"));
        String email = "";
        String objectName = "";
        String fileType = "";
        String typeName = "";
        String checklist = "";
        String fileNames = "";
        String subject = "";
        String notificationType = "ExternalUploadFiles";
        if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(objectId);
            objectName = manufacturerPart.getPartNumber();
            fileType = "inspection report";
            typeName = "manufacturer part";
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            PQMPPAP pqmppap = ppapRepository.findOne(objectId);
            objectName = pqmppap.getNumber();
            fileType = "checklist";
            typeName = "ppap";
            persons.add(personRepository.findOne(pqmppap.getCreatedBy()));
            if (files.get(0).getParentFile() != null) {
                checklist = fileRepository.findOne(files.get(0).getParentFile()).getName();
            }
        }
        if (uploadType.equals("new")) {
            if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                subject = "Inspection Report Uploaded Notification";
            } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                subject = "PPAP Checklist Uploaded Notification";
            }
            for (PLMFile file : files) {
                if (fileNames.equals("")) {
                    fileNames = file.getName() + ", ";
                } else {
                    fileNames = fileNames + file.getName();
                }
            }
        } else {
            notificationType = "ExternalUpdatedFiles";
            if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                subject = "Inspection Report Updated Notification";
            } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                subject = "PPAP Checklist Updated Notification";
            }
            for (PLMFile file : files) {
                if (file.getPrevisionFileId() != null) {
                    List<Integer> personIds = documentReviewerRepository.getReviewerIdsByDocument(file.getPrevisionFileId());
                    if (personIds.size() > 0) {
                        persons.addAll(personRepository.findByIdIn(personIds));
                    }
                }
                if (fileNames.equals("")) {
                    fileNames = file.getName() + ", ";
                } else {
                    fileNames = fileNames + file.getName();
                }
            }
        }
        String[] recipientAddress = null;
        if (persons.size() > 0) {
            recipientAddress = new String[persons.size()];
            for (int i = 0; i < persons.size(); i++) {
                Person subscribe = persons.get(i);
                if (email.equals("")) {
                    email = subscribe.getEmail();
                } else {
                    email = email + "," + subscribe.getEmail();
                }
            }
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }
        }

        final String notificationTypeFinal = notificationType;
        final String fileNamesFinal = fileNames;
        String tenantId = sessionWrapper.getSession().getTenantId();
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String host = url.substring(0, url.indexOf(uri));
        Map<String, Object> model = new HashMap<>();
        model.put("host", host);
        model.put("fileName", fileNamesFinal);
        model.put("checklist", checklist);
        model.put("objectName", objectName);
        model.put("tenantId", tenantId);
        model.put("notificationType", notificationTypeFinal);
        model.put("externalName", login.getPerson().getFullName());
        model.put("fileType", fileType);
        model.put("typeName", typeName);
        Mail mail = new Mail();
        mail.setMailSubject(subject);
        mail.setTemplatePath("email/fileCommentNotification.html");
        mail.setModel(model);
        if (persons.size() == 1) {
            model.put("personName", persons.get(0).getFullName());
            mail.setMailTo(persons.get(0).getEmail());
            new Thread(() -> {
                mailService.sendEmail(mail);
            }).start();
        } else if (persons.size() > 1) {
            model.put("personName", "All");
            mail.setMailToList(recipientAddress);
            new Thread(() -> {
                mailService.sendEmail(mail);
            }).start();
        }
    }

    public void sendReplacedNotification(Integer fileId, String fileName, PLMFile newFile, String type, Integer objectId, String externalName, PLMObjectType objectType) {
        List<Person> persons = getQARolePerson("DEFAULT_QUALITY_ADMINISTRATOR_ROLE");
        persons.addAll(getQARolePerson("DEFAULT_QUALITY_ANALYST_ROLE"));
        String email = "";

        String fileNames = fileName;
        String newFileName = newFile.getName();
        String notificationType = "ExternalReplacedFile";
        String uploadType = "replaced";
        String subject = "";
        if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            subject = "Inspection Report Updated Notification";
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            subject = "PPAP Checklist Updated Notification";
        }
        if (type.equals("rename")) {
            notificationType = "ExternalRenamedFile";
            uploadType = "renamed";
        }
        String fileType = "";
        String typeName = "";
        String objectName = "";
        if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(objectId);
            objectName = manufacturerPart.getPartNumber() + "-" + manufacturerPart.getPartName();
            fileType = "inspection report";
            typeName = "manufacturer part";
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            PQMPPAP pqmppap = ppapRepository.findOne(objectId);
            objectName = pqmppap.getNumber() + "-" + pqmppap.getName();
            fileType = "checklist";
            typeName = "ppap";
            persons.add(personRepository.findOne(pqmppap.getCreatedBy()));
        }

        List<Integer> personIds = documentReviewerRepository.getReviewerIdsByDocument(fileId);
        if (personIds.size() > 0) {
            persons.addAll(personRepository.findByIdIn(personIds));
        }

        if (persons.size() > 0) {
            String[] recipientAddress = new String[persons.size()];
            for (int i = 0; i < persons.size(); i++) {
                Person subscribe = persons.get(i);
                if (email.equals("")) {
                    email = subscribe.getEmail();
                } else {
                    email = email + "," + subscribe.getEmail();
                }
            }
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }

            final String objectDetails = objectName;
            String tenantId = sessionWrapper.getSession().getTenantId();
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            Map<String, Object> model = new HashMap<>();
            model.put("host", host);
            model.put("fileName", fileNames);
            model.put("fileType", fileType);
            model.put("typeName", typeName);
            model.put("newFileName", newFileName);
            model.put("objectName", objectDetails);
            model.put("tenantId", tenantId);
            model.put("notificationType", notificationType);
            model.put("externalName", externalName);
            Mail mail = new Mail();
            mail.setMailSubject(subject);
            mail.setTemplatePath("email/fileCommentNotification.html");
            mail.setModel(model);
            if (persons.size() == 1) {
                model.put("personName", persons.get(0).getFullName());
                mail.setMailTo(persons.get(0).getEmail());
                new Thread(() -> {
                    mailService.sendEmail(mail);
                }).start();
            } else if (persons.size() > 1) {
                model.put("personName", "All");
                mail.setMailToList(recipientAddress);
                new Thread(() -> {
                    mailService.sendEmail(mail);
                }).start();
            }
        }
    }

    public List<Person> getCreatedByPersons(Integer id, PLMObjectType objectType) {
        List<Person> list = new ArrayList();
        List<Integer> integers = mfrPartInspectionReportRepository.getCreatedByIds(id);
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    @Transactional
    public List<FileDto> createSharedObjectFiles(Integer id, PLMObjectType objectType, ShareObjectFileDto objectFileDto) {
        List<FileDto> fileDtos = new ArrayList<>();
        List<FileDto> files = objectFileDto.getFiles();

        List<FileDto> normalFiles = new ArrayList<>();
        List<FileDto> objectDocuments = new ArrayList<>();
        PLMProject fromProject=projectRepository.findOne(id);

        HashMap<Integer,List<PLMProjectFile>> projectFilesMap = new HashMap();

        List<ProgramProjectFolderDto> folderDtos = objectFileDto.getObjects();
        List<Integer> folderIds = new ArrayList<>();

        files.forEach(file -> {
            if (file.getObjectType().name().equals(PLMObjectType.OBJECTDOCUMENT.name()) || file.getObjectType().name().equals(PLMObjectType.DOCUMENT.name())) {
                objectDocuments.add(file);
            } else {
                normalFiles.add(file);
                if (file.getFileType().equals("FOLDER")) {
                    folderIds.add(file.getId());
                }
            }
        });

        normalFiles.forEach(fileDto -> {
            fileDto.setFilePath(getParentFileNamePath(fromProject.getName(), fileDto.getId(), PLMObjectType.PROJECT));
            folderDtos.forEach(folderDto -> {
                Boolean canCreate = true;
                if (fileDto.getParentFile() != null && folderIds.indexOf(fileDto.getParentFile()) != -1) {
                    canCreate = false;
                }
                if (fileDto.getFileType().equals("FILE") && canCreate) {
                    PLMProjectFile projectFile = new PLMProjectFile();
                    PLMProjectFile existFile = null;
                    if (folderDto.getObjectType().equals("FILE")) {
                        projectFile.setParentFile(folderDto.getId());
                        existFile = projectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndProjectAndLatestTrue(fileDto.getName(), folderDto.getId(), folderDto.getProject());
                    } else {
                        existFile = projectFileRepository.findByProjectAndNameAndParentFileIsNullAndLatestTrue(folderDto.getProject(), fileDto.getName());
                    }
                    if (existFile == null) {
                        projectFile.setName(fileDto.getName());
                        projectFile.setDescription(fileDto.getDescription());
                        projectFile.setProject(folderDto.getProject());
                        projectFile.setVersion(1);
                        projectFile.setSize(fileDto.getSize());
                        projectFile.setLatest(true);
                        String autoNumber1 = null;
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                        projectFile.setFileNo(autoNumber1);
                        projectFile.setFileType("FILE");
                        projectFile = projectFileRepository.save(projectFile);
                        projectFile.setParentObject(PLMObjectType.PROJECT);
                        projectFile.setFilePath(fileDto.getFilePath());
                        List<PLMProjectFile> addedFiles = projectFilesMap.containsKey(folderDto.getProject()) ? projectFilesMap.get(folderDto.getProject()) : new ArrayList();
                        addedFiles.add(projectFile);
                        projectFilesMap.put(folderDto.getProject(), addedFiles);
                        String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getCopyFilePath(fileDto);
                        String dir = "";
                        if (projectFile.getParentFile() != null) {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + getParentFileSystemPath(folderDto.getProject(), projectFile.getId(), PLMObjectType.PROJECT);
                        } else {
                            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + folderDto.getProject() + File.separator + projectFile.getId();
                        }
                        File fDir = new File(dir);
                        if (!fDir.exists()) {
                            try {
                                fDir.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        FileInputStream instream = null;
                        FileOutputStream outstream = null;
                        Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                    }
                } else if (fileDto.getFileType().equals("FOLDER") && canCreate) {
                    PLMProjectFile projectFile = new PLMProjectFile();
                    if (folderDto.getObjectType().equals("FILE")) {
                        projectFile.setParentFile(folderDto.getId());
                    }
                    projectFile.setName(fileDto.getName());
                    projectFile.setDescription(fileDto.getDescription());
                    String folderNumber = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    projectFile.setVersion(1);
                    projectFile.setSize(0L);
                    projectFile.setProject(folderDto.getProject());
                    projectFile.setFileNo(folderNumber);
                    projectFile.setFileType("FOLDER");
                    projectFile.setFilePath(fileDto.getFilePath());
                    List<PLMProjectFile> addedFiles = projectFilesMap.containsKey(folderDto.getProject()) ? projectFilesMap.get(folderDto.getProject()) : new ArrayList();
                    addedFiles.add(projectFile);
                    projectFilesMap.put(folderDto.getProject(), addedFiles);
                    projectFile = projectFileRepository.save(projectFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + folderDto.getProject();
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getProjectFileParentFileSystemPath(folderDto.getProject(), projectFile.getId());
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }

                    copyProjectFolderFiles(folderDto.getProject(), fileDto.getId(), projectFile.getId());
                }
            });
        });

        objectDocuments.forEach(objectDocument -> {
            folderDtos.forEach(folderDto -> {
                PLMObjectDocument plmObjectDocument = new PLMObjectDocument();
                PLMDocument document = null;
                if (objectDocument.getObjectType().name().equals(PLMObjectType.OBJECTDOCUMENT.name())) {
                    PLMObjectDocument existObjectDocument = objectDocumentRepository.findOne(objectDocument.getId());
                    if (existObjectDocument != null) {
                        document = plmDocumentRepository.findOne(existObjectDocument.getDocument().getId());
                    }
                } else {
                    document = plmDocumentRepository.findOne(objectDocument.getId());
                }
                if (document != null) {
                    Boolean canCreate = true;
                    if (objectDocument.getFolder() != null && folderIds.indexOf(objectDocument.getFolder()) != -1) {
                        canCreate = false;
                    }
                    if (canCreate) {
                        if (folderDto.getObjectType().equals("FILE")) {
                            plmObjectDocument.setFolder(folderDto.getId());
                        }
                        plmObjectDocument.setDocument(document);
                        plmObjectDocument.setObject(folderDto.getProject());
                        plmObjectDocument.setDocumentType("FILE");
                        PLMObjectDocument existDocument = objectDocumentRepository.findByDocumentIdAndObjectAndFolderIsNull(plmObjectDocument.getDocument().getId(), folderDto.getProject());
                        if (existDocument == null) {
                            plmObjectDocument = objectDocumentRepository.save(plmObjectDocument);
                        }
                    }
                }
            });
        });

        String name = sessionWrapper.getSession().getLogin().getPerson().getFullName();
        for (Integer key : projectFilesMap.keySet()) {
            List<PLMProjectFile> projectFiles = projectFilesMap.get(key);
            List<String> fileNames = new ArrayList<String>();
            PLMProject project = projectRepository.findOne(key); 
            for(PLMProjectFile file: projectFiles)
            {
                fileNames.add(file.getFilePath());   
            }
            sendProjectFilesSharedNotification(name, fromProject, fileNames, project);
        } 
        return fileDtos;
    }

    public void sendProjectFilesSharedNotification(String name, PLMProject fromProject,  List<String> fileNames, PLMProject project) {
            Person person = personRepository.findOne(project.getProjectManager()); 
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("fromProjectName", fromProject.getName());
                model.put("userName", name);
                model.put("projectManagerName", person.getFirstName());
                model.put("projectName", project.getName());
                model.put("fileNames", fileNames);
                Mail mail = new Mail();
                mail.setMailTo(person.getEmail());
                mail.setMailSubject( project.getName() + " Shared Notification");
                mail.setTemplatePath("email/projectFilesSharedNotification.html");
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();   
    }

    private void copyProjectFolderFiles(Integer project, Integer file, Integer parent) {
        List<PLMProjectFile> projectFiles = projectFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        projectFiles.forEach(plmProjectFile -> {
            PLMProjectFile projectFile = new PLMProjectFile();
            projectFile.setParentFile(parent);
            projectFile.setName(plmProjectFile.getName());
            projectFile.setDescription(plmProjectFile.getDescription());
            projectFile.setProject(project);
            String folderNumber = null;
            if (plmProjectFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                projectFile.setVersion(1);
                projectFile.setFileNo(folderNumber);
                projectFile.setSize(plmProjectFile.getSize());
                projectFile.setFileType("FILE");
                projectFile = projectFileRepository.save(projectFile);
                projectFile.setParentObject(PLMObjectType.PROJECT);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getProjectFileParentFileSystemPath(plmProjectFile.getProject(), plmProjectFile.getId());

                String dir = "";
                if (projectFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(project, projectFile.getId(), PLMObjectType.PROJECT);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + project + File.separator + projectFile.getId();
                }
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    try {
                        fDir.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileInputStream instream = null;
                FileOutputStream outstream = null;
                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            } else {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                projectFile.setVersion(1);
                projectFile.setSize(0L);
                projectFile.setFileNo(folderNumber);
                projectFile.setFileType("FOLDER");
                projectFile = projectFileRepository.save(projectFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getProjectFileParentFileSystemPath(project, projectFile.getId());
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyProjectFolderFiles(project, plmProjectFile.getId(), projectFile.getId());
            }
        });
        PLMProjectFile projectFile = projectFileRepository.findOne(file);
        if (projectFile != null) {
            List<PLMObjectDocument> objectDocuments = objectDocumentRepository.findByObjectAndFolderAndDocumentType(projectFile.getProject(), file, "FILE");
            for (PLMObjectDocument objectDocument : objectDocuments) {
                PLMObjectDocument plmObjectDocument = new PLMObjectDocument();
                plmObjectDocument.setObject(project);
                plmObjectDocument.setFolder(parent);
                plmObjectDocument.setDocument(objectDocument.getDocument());
                plmObjectDocument.setDocumentType("FILE");
                plmObjectDocument = objectDocumentRepository.save(plmObjectDocument);
            }
        }
    }

    public String getProjectFileParentFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        PLMProjectFile projectFile = projectFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (projectFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, projectFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + projectFile.getId();
        }
        return path;
    }

    public String getCopyFilePath(FileDto file) {
        String path = "";
        PLMProjectFile projectFile = projectFileRepository.findOne(file.getId());
        if (projectFile.getParentFile() != null) {
            path = visitCopyFileParentFolder(projectFile.getId(), path, file.getParentObject());
        } else {
            path = File.separator + projectFile.getProject() + File.separator + projectFile.getId();
        }
        return path;
    }

    private String visitCopyFileParentFolder(Integer fileId, String path, PLMObjectType type) {
        PLMProjectFile plmProjectFile = projectFileRepository.findOne(fileId);
        if (plmProjectFile.getParentFile() != null) {
            path = File.separator + plmProjectFile.getId() + path;
            path = visitCopyFileParentFolder(plmProjectFile.getParentFile(), path, type);
        } else {
            path = File.separator + plmProjectFile.getProject() + File.separator + plmProjectFile.getId() + path;
            return path;
        }
        return path;
    }

    public static class WindowsExplorerComparator implements Comparator<FileDto> {

        private static final Pattern splitPattern = Pattern.compile("\\d+|\\.|\\s");

        @Override
        public int compare(FileDto str1, FileDto str2) {
            Iterator<String> i1 = splitStringPreserveDelimiter(str1).iterator();
            Iterator<String> i2 = splitStringPreserveDelimiter(str2).iterator();
            while (true) {
                //Til here all is equal.
                if (!i1.hasNext() && !i2.hasNext()) {
                    return 0;
                }
                //first has no more parts -> comes first
                if (!i1.hasNext() && i2.hasNext()) {
                    return -1;
                }
                //first has more parts than i2 -> comes after
                if (i1.hasNext() && !i2.hasNext()) {
                    return 1;
                }

                String data1 = i1.next();
                String data2 = i2.next();
                int result;
                try {
                    //If both datas are numbers, then compare numbers
                    result = Long.compare(Long.valueOf(data1), Long.valueOf(data2));
                    //If numbers are equal than longer comes first
                    if (result == 0) {
                        result = -Integer.compare(data1.length(), data2.length());
                    }
                } catch (NumberFormatException ex) {
                    //compare text case insensitive
                    result = data1.compareToIgnoreCase(data2);
                }

                if (result != 0) {
                    return result;
                }
            }
        }

        private List<String> splitStringPreserveDelimiter(FileDto str) {
            Matcher matcher = splitPattern.matcher(str.getName());
            List<String> list = new ArrayList<String>();
            int pos = 0;
            while (matcher.find()) {
                list.add(str.getName().substring(pos, matcher.start()));
                list.add(matcher.group());
                pos = matcher.end();
            }
            list.add(str.getName().substring(pos));
            return list;
        }
    }
}