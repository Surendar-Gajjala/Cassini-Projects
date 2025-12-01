package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.GroupMember;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.security.DMFolderPermission;
import com.cassinisys.platform.repo.common.GroupMemberRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.repo.security.DMFolderPermissionRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.security.CustomePrivilegeFilter;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.DocumentEvents;
import com.cassinisys.plm.filtering.DocumentCriteria;
import com.cassinisys.plm.filtering.DocumentPredicateBuilder;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMMfrPartInspectionReport;
import com.cassinisys.plm.model.mfr.PLMSupplier;
import com.cassinisys.plm.model.pdm.PDMConstants;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.model.pqm.PQMPPAP;
import com.cassinisys.plm.model.pqm.PQMPPAPChecklist;
import com.cassinisys.plm.model.wf.PLMUserTask;
import com.cassinisys.plm.model.wf.UserTaskStatus;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.MfrPartInspectionReportRepository;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.PLMDocumentRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pqm.PPAPChecklistRepository;
import com.cassinisys.plm.repo.pqm.PPAPRepository;
import com.cassinisys.plm.repo.wf.PLMUserTaskRepository;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.service.tm.UserTaskEvents;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by subramanyam on 18-09-2021.
 */
@Service
public class DocumentService implements CrudService<PLMDocument, Integer> {

    Boolean folderFlag = false;
    @Autowired
    private PLMDocumentRepository plmDocumentRepository;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private DocumentPredicateBuilder documentPredicateBuilder;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private ObjectFileService objectFileService;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemFileRepository itemFileRepository;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private DocumentReviewerRepository documentReviewerRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMUserTaskRepository plmUserTaskRepository;
    @Autowired
    private DMFolderPermissionRepository dmFolderPermissionRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private CustomePrivilegeFilter customePrivilegeFilter;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private MfrPartInspectionReportRepository mfrPartInspectionReportRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private PPAPChecklistRepository ppapChecklistRepository;
    @Autowired
    private PPAPRepository ppapRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private SupplierRepository supplierRepository;


    @Override
    @PreAuthorize("@documentService.checkDocumentPermissions(authentication, 'create', 'document', #plmDocument.parentFile)")
    public PLMDocument create(PLMDocument plmDocument) {
        return plmDocumentRepository.save(plmDocument);
    }

    @Override
    @PreAuthorize("@documentService.checkDocumentPermissions(authentication,'edit','document', #plmDocument.id)")
    public PLMDocument update(PLMDocument plmDocument) {
        return plmDocumentRepository.save(plmDocument);
    }

    @Override
    @PreAuthorize("@documentService.checkDocumentPermissions(authentication,'delete','document', #id)")
    public void delete(Integer id) {
        plmDocumentRepository.delete(id);
    }

    @Override
    @PreAuthorize("@documentService.checkDocumentPermissions(authentication,'view','document', #id)")
    public PLMDocument get(Integer id) {
        return plmDocumentRepository.findOne(id);
    }

    @Override
    @PostFilter("@documentService.filterDMPermissions(authentication, filterObject, 'view')")
    public List<PLMDocument> getAll() {
        return plmDocumentRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("@documentService.filterDMPermissions(authentication, filterObject, 'view')")
    public List<PLMDocument> getDocumentFolderTree() {
        List<PLMDocument> documents = plmDocumentRepository.findByFileTypeAndParentFileIsNullOrderByModifiedDateDesc("FOLDER");
        documents.forEach(document -> {
            visitFolderChildren(document);
        });
        return documents;
    }

    @Transactional(readOnly = true)
    @PostFilter("@documentService.filterDMPermissions(authentication, filterObject, 'view')")
    public List<PLMDocument> getObjectDocumentFolderTree(Integer object, PLMObjectType objectType, Integer folder) {
        List<PLMDocument> documents = plmDocumentRepository.findByFileTypeAndParentFileIsNullOrderByModifiedDateDesc("FOLDER");
        documents.forEach(document -> {
            visitObjectFolderChildren(document, object, objectType, folder);
        });
        return documents;
    }

    @Transactional(readOnly = true)
    @PostFilter("@documentService.filterDMPermissions(authentication, filterObject, 'view')")
    public List<PLMDocument> getAllDocuments() {
        List<PLMDocument> documents = plmDocumentRepository.findByLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc();
        documents.forEach(document -> {
            document.setCount(plmDocumentRepository.getChildrenCountByParent(document.getId()));
        });
        return documents;
    }

    @Transactional(readOnly = true)
    @PostFilter("@documentService.filterDMPermissions(authentication, filterObject, 'view')")
    public List<PLMDocument> getFilteredDocuments(DocumentCriteria documentCriteria) {
        Predicate predicate = documentPredicateBuilder.build(documentCriteria, QPLMDocument.pLMDocument);
        Iterable<PLMDocument> documents = plmDocumentRepository.findAll(predicate);
        List<PLMDocument> plmDocuments = new LinkedList<>();
        documents.forEach(document -> {
            document.setCount(plmDocumentRepository.getChildrenCountByParent(document.getId()));
            plmDocuments.add(document);
        });
        Collections.sort(plmDocuments, new Comparator<PLMDocument>() {
            public int compare(final PLMDocument object1, final PLMDocument object2) {
                return object2.getModifiedDate().compareTo(object1.getModifiedDate());
            }
        });
        return plmDocuments;
    }

    @Transactional(readOnly = true)
    @PostFilter("@documentService.filterDMPermissions(authentication, filterObject, 'view')")
    public List<PLMDocument> getDocumentChildren(Integer documentId) {
        List<PLMDocument> documents = plmDocumentRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(documentId);
        documents.forEach(document -> {
            document.setCount(plmDocumentRepository.getChildrenCountByParent(document.getId()));
        });
        return documents;
    }

    @Transactional(readOnly = true)
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getTotalDocumentsCount() {
        Integer count = plmDocumentRepository.getCountByFileTypeAndLatestTrueOrderByModifiedDateDesc("FILE");
        return count;
    }

    @Transactional(readOnly = true)
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getFolderDocumentsCount(Integer folder) {
        Integer count = plmDocumentRepository.getChildrenCountByParentAndFileType(folder, "FILE");
        return count;
    }

    private void visitFolderChildren(PLMDocument plmDocument) {
        List<PLMDocument> documents = plmDocumentRepository.findByParentFileAndFileTypeOrderByModifiedDateDesc(plmDocument.getId(), "FOLDER");
        documents.forEach(document -> {
            visitFolderChildren(document);
        });
        plmDocument.getChildren().addAll(documents);
        plmDocument.setCount(plmDocumentRepository.getChildrenCountByParentAndFileType(plmDocument.getId(), "FILE"));
    }

    private void visitObjectFolderChildren(PLMDocument plmDocument, Integer object, PLMObjectType objectType, Integer folder) {
        List<PLMDocument> documents = plmDocumentRepository.findByParentFileAndFileTypeOrderByModifiedDateDesc(plmDocument.getId(), "FOLDER");
        documents.forEach(document -> {
            visitObjectFolderChildren(document, object, objectType, folder);
        });
        plmDocument.getChildren().addAll(documents);
        List<Integer> documentIds = new ArrayList<>();
        if (folder == 0) {
            documentIds = objectDocumentRepository.getDocumentIdsByObjectIdAndFolderIsNullAndDocumentType(object, objectType.name());
            if (documentIds.size() > 0) {
                plmDocument.setCount(plmDocumentRepository.getChildrenCountByParentAndFileTypeAndIdsNotIn(plmDocument.getId(), "FILE", documentIds));
            } else {
                plmDocument.setCount(plmDocumentRepository.getChildrenCountByParentAndFileType(plmDocument.getId(), "FILE"));
            }
        } else {
            documentIds = objectDocumentRepository.getDocumentIdsByObjectIdAndFolderAndDocumentType(object, folder, objectType.name());
            if (documentIds.size() > 0) {
                plmDocument.setCount(plmDocumentRepository.getChildrenCountByParentAndFileTypeAndIdsNotIn(plmDocument.getId(), "FILE", documentIds));
            } else {
                plmDocument.setCount(plmDocumentRepository.getChildrenCountByParentAndFileType(plmDocument.getId(), "FILE"));
            }
        }
    }

    @Transactional
    @PreAuthorize("@documentService.checkDocumentPermissions(authentication, 'create', 'document', #document.parentFile)")
    public PLMDocument createDocumentFolder(PLMDocument document) {
        if (document.getParentFile() == null) {
            PLMDocument existFolder = plmDocumentRepository.findByNameEqualsIgnoreCaseAndFileTypeAndParentFileIsNull(document.getName(), "FOLDER");
            if (existFolder != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", document.getName());
                throw new CassiniException(result);
            }
        } else {
            PLMDocument existFolder = plmDocumentRepository.findByNameEqualsIgnoreCaseAndFileTypeAndParentFile(document.getName(), "FOLDER", document.getParentFile());
            if (existFolder != null) {
                PLMDocument folder = plmDocumentRepository.findOne(document.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", document.getName(), folder.getName());
                throw new CassiniException(result);
            }
        }
        String folderNumber = "";
        AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
        if (autoNumber != null) {
            folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
        }
        document.setFileNo(folderNumber);
        document.setSize(0L);
        document.setVersion(1);
        String newRevision = objectFileService.setRevisionAndLifecyclePhase();
        PLMLifeCyclePhase plmLifeCyclePhase = objectFileService.setLifecyclePhase();
        document.setRevision(newRevision);
        document.setLifeCyclePhase(plmLifeCyclePhase);
        document = plmDocumentRepository.save(document);
        if (document != null) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + "documents";
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            dir = dir + getParentFileSystemPath(document.getId());

            fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }

        }
        applicationEventPublisher.publishEvent(new DocumentEvents.DocumentFoldersAddedEvent(document));
        return document;
    }

    @Transactional
    @PreAuthorize("@documentService.checkDocumentPermissions(authentication, 'edit', 'document', #document.id)")
    public PLMDocument updateDocumentFolder(PLMDocument document) {
        if (document.getParentFile() == null) {
            PLMDocument existFolder = plmDocumentRepository.findByNameEqualsIgnoreCaseAndFileTypeAndParentFileIsNull(document.getName(), "FOLDER");
            if (existFolder != null && !existFolder.getId().equals(document.getId())) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", document.getName());
                throw new CassiniException(result);
            }
        } else {
            PLMDocument existFolder = plmDocumentRepository.findByNameEqualsIgnoreCaseAndFileTypeAndParentFile(document.getName(), "FOLDER", document.getParentFile());
            if (existFolder != null && !existFolder.getId().equals(document.getId())) {
                PLMDocument folder = plmDocumentRepository.findOne(document.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", document.getName(), folder.getName());
                throw new CassiniException(result);
            }
        }
        document = plmDocumentRepository.save(document);
        document.setCount(plmDocumentRepository.getChildrenCountByParentAndFileType(document.getId(), "FILE"));
        return document;
    }

    @Transactional(readOnly = false)
    @PreAuthorize("@documentService.checkDocumentPermissions(authentication,'delete','document', #id)")
    public void deleteDocumentFolder(Integer id) {
        PLMDocument plmDocument = plmDocumentRepository.findOne(id);
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + "documents" + getParentFileSystemPath(id);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        plmDocumentRepository.delete(plmDocument.getId());
        applicationEventPublisher.publishEvent(new DocumentEvents.DocumentFoldersDeletedEvent(plmDocument));
    }


    public String getParentFileSystemPath(Integer fileId) {
        String path = "";
        PLMFile file = plmDocumentRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (file.getParentFile() != null) {
            path = visitParentFolder(file.getParentFile(), path);
        } else {
            path = File.separator + file.getId();
        }
        return path;
    }


    private String visitParentFolder(Integer fileId, String path) {
        PLMFile file = plmDocumentRepository.findOne(fileId);
        if (file.getParentFile() != null) {
            path = File.separator + file.getId() + path;
            path = visitParentFolder(file.getParentFile(), path);
        } else {
            path = File.separator + file.getId() + path;
            return path;
        }
        return path;
    }

    @Transactional
    public List<FileDto> createMultipleDocuments(List<PLMObjectDocument> objectDocuments) {
        List<FileDto> dtoList = new ArrayList<>();
        List<PLMObjectDocument> documents = new ArrayList<>();
        for (PLMObjectDocument objectDocument : objectDocuments) {
            PLMObjectDocument existDocument = null;
            if (objectDocument.getFolder() != null) {
                existDocument = objectDocumentRepository.findByDocumentIdAndObjectAndFolder(objectDocument.getDocument().getId(), objectDocument.getObject(), objectDocument.getFolder());
            } else {
                existDocument = objectDocumentRepository.findByDocumentIdAndObjectAndFolderIsNull(objectDocument.getDocument().getId(), objectDocument.getObject());
            }
            if (existDocument == null) {
                documents.add(objectDocument);
            }
        }

        if (documents.size() > 0) {
            documents = objectDocumentRepository.save(documents);
            documents.forEach(objectDocument -> {
                dtoList.add(objectFileService.convertObjectDocumentToDto(objectDocument));
            });
            CassiniObject cassiniObject = objectRepository.findById(documents.get(0).getObject());
            if (cassiniObject != null && cassiniObject.getObjectType().name().equals(PLMObjectType.ITEMREVISION.name())) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(documents.get(0).getObject());
                if (itemRevision != null) {
                    itemRevision.setHasFiles(true);
                    itemRevision = itemRevisionRepository.save(itemRevision);
                }
            }
        }
        return dtoList;
    }

    @Transactional
    public FileDto updateObjectDocumentToLatest(Integer fileId) {
        PLMObjectDocument objectDocument = objectDocumentRepository.findOne(fileId);

        PLMDocument document = plmDocumentRepository.findByFileNoAndLatestTrue(objectDocument.getDocument().getFileNo());
        objectDocument.setDocument(document);
        objectDocument = objectDocumentRepository.save(objectDocument);
        FileDto fileDto = objectFileService.convertObjectDocumentToDto(objectDocument);
        return fileDto;
    }

    @Transactional
    public void deleteObjectDocument(Integer fileId) {
        PLMObjectDocument objectDocument = objectDocumentRepository.findOne(fileId);
        objectDocumentRepository.delete(fileId);
        Integer count = itemFileRepository.getFileCountByItemAndLatestTrueAndParentFileIsNull(objectDocument.getObject());
        Integer documentCount = objectDocumentRepository.getDocumentsCountByObjectId(objectDocument.getObject());
        if (count == 0 && documentCount == 0) {
            CassiniObject cassiniObject = objectRepository.findById(objectDocument.getObject());
            if (cassiniObject != null && cassiniObject.getObjectType().name().equals(PLMObjectType.ITEMREVISION.name())) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(objectDocument.getObject());
                if (itemRevision != null) {
                    itemRevision.setHasFiles(false);
                    itemRevision = itemRevisionRepository.save(itemRevision);
                }
            }
        }
    }

    @Transactional
    @PreAuthorize("@documentService.checkDocumentPermissions(authentication, 'promote', #file.objectType.name().toLowerCase(), #file.parentFile)")
    public FileDto promoteDocument(Integer fileId, FileDto file) {
        PLMFile plmFile = fileRepository.findOne(fileId);
        FileDto fileDto = new FileDto();
        if (plmFile.getObjectType().name().equals(PLMObjectType.DOCUMENT.name())) {
            PLMDocument document = plmDocumentRepository.findOne(fileId);
            PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(document.getLifeCyclePhase().getLifeCycle());
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
            Integer lifeCyclePhase1 = document.getLifeCyclePhase().getId();
            PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                    filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                    findFirst().get();
            Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
            if (index != -1) {
                PLMLifeCyclePhase oldStatus = document.getLifeCyclePhase();
                index++;
                PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
                if (lifeCyclePhase != null) {
                    if (lifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                        Integer noneApprovedCount = documentReviewerRepository.getNoneApprovedCount(document.getId());
                        if (noneApprovedCount > 0) {
                            String message = messageSource.getMessage("document_approvers_not_approved", null, "{0} : document approvers not approved ", LocaleContextHolder.getLocale());
                            String result = MessageFormat.format(message + ".", document.getName());
                            throw new CassiniException(result);
                        }
                        List<PLMDocumentReviewer> documentReviewers = documentReviewerRepository.findByDocumentOrderByIdDesc(document.getId());
                        for (PLMDocumentReviewer documentReviewer : documentReviewers) {
                            PLMUserTask userTask = plmUserTaskRepository.findBySourceAndAssignedToOrderByIdDesc(document.getId(), documentReviewer.getReviewer());
                            if (userTask != null && userTask.getStatus().equals(UserTaskStatus.PENDING)) {
                                userTask.setStatus(UserTaskStatus.FINISHED);
                                userTask = plmUserTaskRepository.save(userTask);
                            }
                        }
                    }
                    document.setLifeCyclePhase(lifeCyclePhase);
                    applicationEventPublisher.publishEvent(new DocumentEvents.DocumentPromotedEvent(plmFile.getId(), plmFile.getId(), oldStatus, lifeCyclePhase, PLMObjectType.DOCUMENT));
                    document = plmDocumentRepository.save(document);
                }
            }
            fileDto = objectFileService.convertFileIdToDto(0, PLMObjectType.DOCUMENT, document.getId());
        } else if (plmFile.getObjectType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            PLMMfrPartInspectionReport document = mfrPartInspectionReportRepository.findOne(fileId);
            PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(document.getLifeCyclePhase().getLifeCycle());
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
            Integer lifeCyclePhase1 = document.getLifeCyclePhase().getId();
            PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                    filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                    findFirst().get();
            Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
            if (index != -1) {
                PLMLifeCyclePhase oldStatus = document.getLifeCyclePhase();
                index++;
                PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
                if (lifeCyclePhase != null) {
                    if (lifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                        Integer noneApprovedCount = documentReviewerRepository.getNoneApprovedCount(document.getId());
                        if (noneApprovedCount > 0) {
                            String message = messageSource.getMessage("document_approvers_not_approved", null, "{0} : document approvers not approved ", LocaleContextHolder.getLocale());
                            String result = MessageFormat.format(message + ".", document.getName());
                            throw new CassiniException(result);
                        }
                        List<PLMDocumentReviewer> documentReviewers = documentReviewerRepository.findByDocumentOrderByIdDesc(document.getId());
                        for (PLMDocumentReviewer documentReviewer : documentReviewers) {
                            PLMUserTask userTask = plmUserTaskRepository.findBySourceAndAssignedToOrderByIdDesc(document.getId(), documentReviewer.getReviewer());
                            if (userTask != null && userTask.getStatus().equals(UserTaskStatus.PENDING)) {
                                userTask.setStatus(UserTaskStatus.FINISHED);
                                userTask = plmUserTaskRepository.save(userTask);
                            }
                        }
                    }
                    document.setLifeCyclePhase(lifeCyclePhase);
                    applicationEventPublisher.publishEvent(new DocumentEvents.DocumentPromotedEvent(document.getManufacturerPart(), document.getId(), oldStatus, lifeCyclePhase, PLMObjectType.MFRPARTINSPECTIONREPORT));
                    document = mfrPartInspectionReportRepository.save(document);
                }
            }
            fileDto = objectFileService.convertFileIdToDto(document.getManufacturerPart(), PLMObjectType.MFRPARTINSPECTIONREPORT, document.getId());
        } else if (plmFile.getObjectType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            PQMPPAPChecklist document = ppapChecklistRepository.findOne(fileId);
            if (document.getFileType().equals("FOLDER")) {
                Integer fileCount = fileRepository.getChildCountByFileType(document.getId(), "FILE");
                fileCount += objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(document.getPpap(), document.getId());
                if (fileCount == 0) {
                    throw new CassiniException("Add atleast one file to promote this [ " + document.getName() + " ] checklist");
                }

                document = updateLifecyclePhase(document);
                updatePPAPStatus(document.getPpap());
                if (document.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                    List<PQMPPAPChecklist> checklists = ppapChecklistRepository.getChildByFileType(document.getId(), "FILE");
                    List<PLMObjectDocument> objectDocuments = objectDocumentRepository.findByObjectAndFolderAndDocumentType(document.getPpap(), document.getId(), "FILE");
                    objectDocuments.forEach(plmObjectDocument -> {
                        if (!plmObjectDocument.getDocument().getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                            String message = messageSource.getMessage("document_not_released", null, "{0} : document not released ", LocaleContextHolder.getLocale());
                            String result = MessageFormat.format(message + ".", plmObjectDocument.getDocument().getName());
                            throw new CassiniException(result);
                        }
                    });
                    checklists.forEach(pqmppapChecklist -> {
                        Integer noneApprovedCount = documentReviewerRepository.getNoneApprovedCount(pqmppapChecklist.getId());
                        if (noneApprovedCount > 0) {
                            String message = messageSource.getMessage("document_approvers_not_approved", null, "{0} : document approvers not approved ", LocaleContextHolder.getLocale());
                            String result = MessageFormat.format(message + ".", pqmppapChecklist.getName());
                            throw new CassiniException(result);
                        }
                        if (!pqmppapChecklist.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                            PLMLifeCyclePhase oldStatus = pqmppapChecklist.getLifeCyclePhase();
                            List<PLMDocumentReviewer> documentReviewers = documentReviewerRepository.findByDocumentOrderByIdDesc(pqmppapChecklist.getId());
                            for (PLMDocumentReviewer documentReviewer : documentReviewers) {
                                PLMUserTask userTask = plmUserTaskRepository.findBySourceAndAssignedToOrderByIdDesc(pqmppapChecklist.getId(), documentReviewer.getReviewer());
                                if (userTask != null && userTask.getStatus().equals(UserTaskStatus.PENDING)) {
                                    userTask.setStatus(UserTaskStatus.FINISHED);
                                    userTask = plmUserTaskRepository.save(userTask);
                                }
                            }
                            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(pqmppapChecklist.getLifeCyclePhase().getLifeCycle(), LifeCyclePhaseType.RELEASED);
                            if (lifeCyclePhases.size() > 0) {
                                pqmppapChecklist.setLifeCyclePhase(lifeCyclePhases.get(lifeCyclePhases.size() - 1));
                                pqmppapChecklist = ppapChecklistRepository.save(pqmppapChecklist);
                                applicationEventPublisher.publishEvent(new DocumentEvents.DocumentPromotedEvent(pqmppapChecklist.getPpap(), pqmppapChecklist.getId(), oldStatus, pqmppapChecklist.getLifeCyclePhase(), PLMObjectType.PPAPCHECKLIST));
                            }
                        }
                    });
                } else if (document.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.REVIEW)) {
                    List<PQMPPAPChecklist> checklists = ppapChecklistRepository.getChildByFileType(document.getId(), "FILE");
                    checklists.forEach(pqmppapChecklist -> {
                        if (pqmppapChecklist.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.PRELIMINARY)) {
                            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(pqmppapChecklist.getLifeCyclePhase().getLifeCycle(), LifeCyclePhaseType.REVIEW);
                            if (lifeCyclePhases.size() > 0) {
                                PLMLifeCyclePhase oldStatus = pqmppapChecklist.getLifeCyclePhase();
                                pqmppapChecklist.setLifeCyclePhase(lifeCyclePhases.get(lifeCyclePhases.size() - 1));
                                pqmppapChecklist = ppapChecklistRepository.save(pqmppapChecklist);
                                applicationEventPublisher.publishEvent(new DocumentEvents.DocumentPromotedEvent(pqmppapChecklist.getPpap(), pqmppapChecklist.getId(), oldStatus, pqmppapChecklist.getLifeCyclePhase(), PLMObjectType.PPAPCHECKLIST));
                            }
                        }
                    });
                }
            } else {
                document = updateLifecyclePhase(document);
                updatePPAPStatus(document.getPpap());
            }
            Integer totalFiles = ppapChecklistRepository.getTotalChecklistCount(document.getPpap());
            totalFiles = totalFiles + objectDocumentRepository.getDocumentsCountByObjectId(document.getPpap());
            Integer phaseFiles = ppapChecklistRepository.getChecklistCountByPhase(document.getPpap(), LifeCyclePhaseType.RELEASED);
            phaseFiles = phaseFiles + objectDocumentRepository.getReleasedDocumentsByObjectAndStatus(document.getPpap(), LifeCyclePhaseType.RELEASED);
            if (totalFiles > 0 && totalFiles.equals(phaseFiles)) {
                PQMPPAP ppap = ppapRepository.findOne(document.getPpap());
                List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(ppap.getStatus().getLifeCycle(), LifeCyclePhaseType.RELEASED);
                if (lifeCyclePhases.size() > 0) {
                    ppap.setStatus(lifeCyclePhases.get(lifeCyclePhases.size() - 1));
                    ppap = ppapRepository.save(ppap);
                    if (ppap.getStatus().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                        sendPpapApprovedNotification(ppap);
                    }

                }
            }
            fileDto = objectFileService.convertFileIdToDto(document.getPpap(), PLMObjectType.PPAPCHECKLIST, document.getId());
        }
        return fileDto;
    }

    @Transactional
    private PQMPPAP updatePPAPStatus(Integer ppapId) {
        PQMPPAP ppap = ppapRepository.findOne(ppapId);
        if (ppap.getStatus().getPhaseType().equals(LifeCyclePhaseType.PRELIMINARY)) {
            PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(ppap.getStatus().getLifeCycle());
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
            Integer lifeCyclePhase1 = ppap.getStatus().getId();
            PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                    filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                    findFirst().get();
            Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
            if (index != -1) {
                index++;
                ppap.setStatus(lifeCyclePhases.get(index));
                ppap = ppapRepository.save(ppap);
            }
        }
        return ppap;
    }

    @Transactional
    private PQMPPAPChecklist updateLifecyclePhase(PQMPPAPChecklist document) {
        PQMPPAPChecklist plmFile = ppapChecklistRepository.findOne(document.getId());
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(document.getLifeCyclePhase().getLifeCycle());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = document.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                findFirst().get();
        Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
        if (index != -1) {
            PLMLifeCyclePhase oldStatus = document.getLifeCyclePhase();
            index++;
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
            if (lifeCyclePhase != null) {
                if (lifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                    Integer noneApprovedCount = documentReviewerRepository.getNoneApprovedCount(document.getId());
                    if (noneApprovedCount > 0) {
                        String message = messageSource.getMessage("document_approvers_not_approved", null, "{0} : document approvers not approved ", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", document.getName());
                        throw new CassiniException(result);
                    }
                    List<PLMDocumentReviewer> documentReviewers = documentReviewerRepository.findByDocumentOrderByIdDesc(document.getId());
                    for (PLMDocumentReviewer documentReviewer : documentReviewers) {
                        PLMUserTask userTask = plmUserTaskRepository.findBySourceAndAssignedToOrderByIdDesc(document.getId(), documentReviewer.getReviewer());
                        if (userTask != null && userTask.getStatus().equals(UserTaskStatus.PENDING)) {
                            userTask.setStatus(UserTaskStatus.FINISHED);
                            userTask = plmUserTaskRepository.save(userTask);
                        }
                    }
                }
                document.setLifeCyclePhase(lifeCyclePhase);
                applicationEventPublisher.publishEvent(new DocumentEvents.DocumentPromotedEvent(plmFile.getPpap(), plmFile.getId(), oldStatus, lifeCyclePhase, PLMObjectType.PPAPCHECKLIST));
                document = ppapChecklistRepository.save(document);
            }
        }
        return document;
    }

    @Transactional
    @PreAuthorize("@documentService.checkDocumentPermissions(authentication, 'demote', #file.objectType.name().toLowerCase(), #file.parentFile)")
    public FileDto demoteDocument(Integer fileId, FileDto file) {
        PLMFile plmFile = fileRepository.findOne(fileId);
        FileDto fileDto = new FileDto();
        if (plmFile.getObjectType().name().equals(PLMObjectType.DOCUMENT.name())) {
            PLMDocument document = plmDocumentRepository.findOne(fileId);
            PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(document.getLifeCyclePhase().getLifeCycle());
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
            Integer lifeCyclePhase1 = document.getLifeCyclePhase().getId();
            PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                    filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                    findFirst().get();
            Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
            if (index != -1) {
                PLMLifeCyclePhase oldStatus = document.getLifeCyclePhase();
                index--;
                PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
                if (lifeCyclePhase != null) {
                    document.setLifeCyclePhase(lifeCyclePhase);
                    document = plmDocumentRepository.save(document);
                    applicationEventPublisher.publishEvent(new DocumentEvents.DocumentDemotedEvent(plmFile.getId(), plmFile.getId(), oldStatus, lifeCyclePhase, PLMObjectType.DOCUMENT));
                }
            }
            resetDocumentReviewers(document.getId());
            fileDto = objectFileService.convertFileIdToDto(0, PLMObjectType.DOCUMENT, document.getId());
        } else if (plmFile.getObjectType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            PLMMfrPartInspectionReport document = mfrPartInspectionReportRepository.findOne(fileId);
            PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(document.getLifeCyclePhase().getLifeCycle());
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
            Integer lifeCyclePhase1 = document.getLifeCyclePhase().getId();
            PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                    filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                    findFirst().get();
            Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
            if (index != -1) {
                PLMLifeCyclePhase oldStatus = document.getLifeCyclePhase();
                index--;
                PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
                if (lifeCyclePhase != null) {
                    document.setLifeCyclePhase(lifeCyclePhase);
                    document = mfrPartInspectionReportRepository.save(document);
                    applicationEventPublisher.publishEvent(new DocumentEvents.DocumentDemotedEvent(document.getManufacturerPart(), document.getId(), oldStatus, lifeCyclePhase, PLMObjectType.MFRPARTINSPECTIONREPORT));
                }
            }
            resetDocumentReviewers(document.getId());
            List<PLMFile> initialVersionFiles = fileRepository.findByFileNoOrderByIdAsc(document.getFileNo());
            Person person = null;
            if (initialVersionFiles.size() > 0) {
                person = personRepository.findOne(initialVersionFiles.get(0).getCreatedBy());
            } else {
                person = personRepository.findOne(plmFile.getCreatedBy());
            }
            Login login = loginRepository.findByPersonId(person.getId());
            if (login != null && login.getExternal()) {
                sendDemotedNotification(document.getId(), login, PLMObjectType.MFRPARTINSPECTIONREPORT);
            }
            fileDto = objectFileService.convertFileIdToDto(document.getManufacturerPart(), PLMObjectType.MFRPARTINSPECTIONREPORT, document.getId());
        } else if (plmFile.getObjectType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            PQMPPAPChecklist document = ppapChecklistRepository.findOne(fileId);
            PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(document.getLifeCyclePhase().getLifeCycle());
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
            Integer lifeCyclePhase1 = document.getLifeCyclePhase().getId();
            PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                    filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                    findFirst().get();
            Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
            if (index != -1) {
                PLMLifeCyclePhase oldStatus = document.getLifeCyclePhase();
                index--;
                PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
                if (lifeCyclePhase != null) {
                    document.setLifeCyclePhase(lifeCyclePhase);
                    document = ppapChecklistRepository.save(document);
                    applicationEventPublisher.publishEvent(new DocumentEvents.DocumentDemotedEvent(document.getPpap(), document.getId(), oldStatus, lifeCyclePhase, PLMObjectType.PPAPCHECKLIST));
                }
            }
            resetDocumentReviewers(document.getId());
            List<PLMFile> initialVersionFiles = fileRepository.findByFileNoOrderByIdAsc(document.getFileNo());
            Person person = null;
            if (initialVersionFiles.size() > 0) {
                person = personRepository.findOne(initialVersionFiles.get(0).getCreatedBy());
            } else {
                person = personRepository.findOne(plmFile.getCreatedBy());
            }
            Login login = loginRepository.findByPersonId(person.getId());
            if (login != null && login.getExternal()) {
                sendDemotedNotification(document.getId(), login, PLMObjectType.PPAPCHECKLIST);
            }
            fileDto = objectFileService.convertFileIdToDto(document.getPpap(), PLMObjectType.PPAPCHECKLIST, document.getId());
        }
        return fileDto;
    }

    private void resetDocumentReviewers(Integer id) {
        List<PLMDocumentReviewer> documentReviewers = documentReviewerRepository.findByDocumentOrderByIdDesc(id);
        for (PLMDocumentReviewer documentReviewer : documentReviewers) {
            documentReviewer.setStatus(DocumentApprovalStatus.NONE);
            documentReviewer.setVoteTimestamp(null);
            documentReviewer.setNotes(null);
            documentReviewer = documentReviewerRepository.save(documentReviewer);
            PLMUserTask userTask = plmUserTaskRepository.findBySourceAndAssignedToOrderByIdDesc(id, documentReviewer.getReviewer());
            if (userTask != null) {
                userTask.setStatus(UserTaskStatus.PENDING);
                userTask = plmUserTaskRepository.save(userTask);
            }
        }
    }

    @Transactional
    @PreAuthorize("@documentService.checkDocumentPermissions(authentication, 'revise', #file.objectType.name().toLowerCase(), #file.parentFile)")
    public FileDto reviseDocument(Integer fileId, FileDto file) {
        PLMFile plmFile = fileRepository.findOne(fileId);
        if (plmFile.getObjectType().name().equals(PLMObjectType.DOCUMENT.name())) {
            PLMDocument document = plmDocumentRepository.findOne(fileId);
            if (document != null) {
                return reviseRevisionDocument(document, null);
            } else {
                throw new CassiniException(messageSource.getMessage("revise_document_failed_document_not_found", null, Locale.getDefault()));
            }
        } else if (plmFile.getObjectType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            PLMMfrPartInspectionReport document = mfrPartInspectionReportRepository.findOne(fileId);
            if (document != null) {
                return reviseRevisionMfrDocument(document, null);
            } else {
                throw new CassiniException(messageSource.getMessage("revise_document_failed_document_not_found", null, Locale.getDefault()));
            }
        } else {
            PQMPPAPChecklist document = ppapChecklistRepository.findOne(fileId);
            if (document != null) {
                return reviseRevisionPPAPDocument(document, null);
            } else {
                throw new CassiniException(messageSource.getMessage("revise_document_failed_document_not_found", null, Locale.getDefault()));
            }
        }
    }

    public FileDto reviseRevisionDocument(PLMDocument document, String nextRev) {
        PLMDocument plmDocument = plmDocumentRepository.findOne(document.getId());
        if (nextRev == null) {
            nextRev = getNextRevisionSequence(plmDocument);
        }
        if (nextRev != null) {
            PLMDocument copy = createNextRev(plmDocument, nextRev);
            copyReviewers(plmDocument.getId(), copy.getId());
            return objectFileService.convertFileIdToDto(0, PLMObjectType.DOCUMENT, copy.getId());
        } else {
            throw new ItemServiceException(messageSource.getMessage("could_not_retrieve_next_revision_sequence",
                    null, "Could not retrieve next revision sequence", LocaleContextHolder.getLocale()));
        }
    }

    public FileDto reviseRevisionMfrDocument(PLMMfrPartInspectionReport document, String nextRev) {
        if (nextRev == null) {
            nextRev = getNextRevisionSequenceForMfrDocument(document);
        }
        if (nextRev != null) {
            PLMMfrPartInspectionReport copy = createNextRevForMfrDocument(document, nextRev);
            copyReviewers(document.getId(), copy.getId());
            return objectFileService.convertFileIdToDto(document.getManufacturerPart(), PLMObjectType.MFRPARTINSPECTIONREPORT, copy.getId());
        } else {
            throw new ItemServiceException(messageSource.getMessage("could_not_retrieve_next_revision_sequence",
                    null, "Could not retrieve next revision sequence", LocaleContextHolder.getLocale()));
        }
    }

    public FileDto reviseRevisionPPAPDocument(PQMPPAPChecklist document, String nextRev) {
        if (nextRev == null) {
            nextRev = getNextRevisionSequenceForPPAPDocument(document);
        }
        if (nextRev != null) {
            PQMPPAPChecklist copy = createNextRevForPPAPDocument(document, nextRev);
            copyReviewers(document.getId(), copy.getId());
            return objectFileService.convertFileIdToDto(document.getPpap(), PLMObjectType.PPAPCHECKLIST, copy.getId());
        } else {
            throw new ItemServiceException(messageSource.getMessage("could_not_retrieve_next_revision_sequence",
                    null, "Could not retrieve next revision sequence", LocaleContextHolder.getLocale()));
        }
    }

    public void copyReviewers(Integer oldDocument, Integer newDocument) {
        List<PLMDocumentReviewer> documentReviewers = documentReviewerRepository.findByDocumentOrderByIdDesc(oldDocument);
        List<PLMDocumentReviewer> reviewers = new ArrayList<>();
        for (PLMDocumentReviewer documentReviewer : documentReviewers) {
            PLMDocumentReviewer reviewer = new PLMDocumentReviewer();
            reviewer.setDocument(newDocument);
            reviewer.setApprover(documentReviewer.getApprover());
            reviewer.setReviewer(documentReviewer.getReviewer());
            reviewers.add(reviewer);

            PLMUserTask userTask = plmUserTaskRepository.findBySourceAndAssignedToOrderByIdDesc(oldDocument, documentReviewer.getReviewer());
            if (userTask != null && userTask.getStatus().equals(UserTaskStatus.PENDING)) {
                userTask.setStatus(UserTaskStatus.FINISHED);
                userTask = plmUserTaskRepository.save(userTask);
            }

            applicationEventPublisher.publishEvent(new UserTaskEvents.DocumentAssignedEvent(newDocument, reviewer));
        }
        if (reviewers.size() > 0) {
            reviewers = documentReviewerRepository.save(reviewers);
        }
    }

    public String getNextRevisionSequence(PLMDocument document) {
        String nextRev = null;
        List<String> revs = getRevisions(document);
        String lastRev = revs.get(revs.size() - 1);
        String[] values = new String[0];
        Preference pref = preferenceRepository.findByPreferenceKey("DEFAULT_DOCUMENT_REVISION_SEQUENCE");
        if (pref != null) {
            String json = pref.getJsonValue();
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = objectMapper.readTree(json);
                    Integer id = jsonNode.get("typeId").asInt();
                    if (id != null) {
                        Lov lov = lovRepository.findOne(id);
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

        int lastIndex = -1;
        for (int i = 0; i < values.length; i++) {
            String rev = values[i];
            if (rev.equalsIgnoreCase(lastRev)) {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex != -1 && lastIndex < values.length) {
            nextRev = values[lastIndex + 1];
        }
        return nextRev;
    }

    private List<String> getRevisions(PLMDocument document) {
        List<String> revs = new ArrayList<>();
        PLMDocument revisions = plmDocumentRepository.findOne(document.getId());
        String rev = revisions.getRevision();
        if (!revs.contains(rev)) {
            revs.add(rev);
        }
        Collections.sort(revs);
        return revs;
    }

    private PLMDocument createNextRev(PLMDocument document, String nextRev) {
        Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
        PLMDocument copy = JsonUtils.cloneEntity(document, PLMDocument.class);
        document.setLatest(false);
        document = plmDocumentRepository.save(document);
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.PRELIMINARY, document.getLifeCyclePhase().getLifeCycle());
        copy.setId(null);
        copy.setRevision(nextRev);
        copy.setLifeCyclePhase(lifeCyclePhase);
        copy.setVersion(1);
        copy.setModifiedBy(personId);
        copy.setModifiedDate(new Date());
        copy = plmDocumentRepository.save(copy);
        String dir = "";
        if (copy.getParentFile() != null) {
            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + "documents" + objectFileService.getReplaceDocumentFileSystemPath(document.getId());
        } else {
            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + "documents";
        }
        dir = dir + File.separator + copy.getId();

        String oldFileDir = "";
        if (copy.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + "documents" + objectFileService.getDocumentParentFileSystemPath(document.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + "documents" + File.separator + document.getId();
        }
        try {
            if (!dir.equals("")) {
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.createNewFile();
                }

                FileInputStream instream = null;
                FileOutputStream outstream = null;

                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        applicationEventPublisher.publishEvent(new DocumentEvents.DocumentRevisedEvent(document.getId(), document.getId(), document, copy, PLMObjectType.DOCUMENT));
        return copy;
    }

    public String getNextRevisionSequenceForMfrDocument(PLMMfrPartInspectionReport document) {
        String nextRev = null;
        List<String> revs = getRevisionsForMfrDocument(document);
        String lastRev = revs.get(revs.size() - 1);
        String[] values = new String[0];
        Preference pref = preferenceRepository.findByPreferenceKey("DEFAULT_DOCUMENT_REVISION_SEQUENCE");
        if (pref != null) {
            String json = pref.getJsonValue();
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = objectMapper.readTree(json);
                    Integer id = jsonNode.get("typeId").asInt();
                    if (id != null) {
                        Lov lov = lovRepository.findOne(id);
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

        int lastIndex = -1;
        for (int i = 0; i < values.length; i++) {
            String rev = values[i];
            if (rev.equalsIgnoreCase(lastRev)) {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex != -1 && lastIndex < values.length) {
            nextRev = values[lastIndex + 1];
        }
        return nextRev;
    }

    private List<String> getRevisionsForMfrDocument(PLMMfrPartInspectionReport document) {
        List<String> revs = new ArrayList<>();
        PLMMfrPartInspectionReport revisions = mfrPartInspectionReportRepository.findOne(document.getId());
        String rev = revisions.getRevision();
        if (!revs.contains(rev)) {
            revs.add(rev);
        }
        Collections.sort(revs);
        return revs;
    }

    private PLMMfrPartInspectionReport createNextRevForMfrDocument(PLMMfrPartInspectionReport document, String nextRev) {
        Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
        PLMMfrPartInspectionReport copy = JsonUtils.cloneEntity(document, PLMMfrPartInspectionReport.class);
        document.setLatest(false);
        document = mfrPartInspectionReportRepository.save(document);
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.PRELIMINARY, document.getLifeCyclePhase().getLifeCycle());
        copy.setId(null);
        copy.setRevision(nextRev);
        copy.setLifeCyclePhase(lifeCyclePhase);
        copy.setVersion(1);
        copy.setModifiedBy(personId);
        copy.setModifiedDate(new Date());
        copy = mfrPartInspectionReportRepository.save(copy);
        String dir = "";

        if (copy.getParentFile() != null) {
            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getReplaceFileSystemPath(copy.getManufacturerPart(), copy.getId(), PLMObjectType.MFRPARTINSPECTIONREPORT);
        } else {
            dir = fileSystemService.getCurrentTenantRoot() + File.separator + "filesystem" + File.separator + copy.getManufacturerPart();
        }

        dir = dir + File.separator + copy.getId();

        String oldFileDir = "";
        if (copy.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(document.getManufacturerPart(), document.getId(), PLMObjectType.MFRPARTINSPECTIONREPORT);
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator + "filesystem" + File.separator + document.getManufacturerPart() + File.separator + document.getId();
        }

        try {
            if (!dir.equals("")) {
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.createNewFile();
                }

                FileInputStream instream = null;
                FileOutputStream outstream = null;

                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        applicationEventPublisher.publishEvent(new DocumentEvents.DocumentRevisedEvent(document.getManufacturerPart(), document.getId(), document, copy, PLMObjectType.MFRPARTINSPECTIONREPORT));
        return copy;
    }

    public String getNextRevisionSequenceForPPAPDocument(PQMPPAPChecklist document) {
        String nextRev = null;
        List<String> revs = getRevisionsForPPAPDocument(document);
        String lastRev = revs.get(revs.size() - 1);
        String[] values = new String[0];
        Preference pref = preferenceRepository.findByPreferenceKey("DEFAULT_DOCUMENT_REVISION_SEQUENCE");
        if (pref != null) {
            String json = pref.getJsonValue();
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = objectMapper.readTree(json);
                    Integer id = jsonNode.get("typeId").asInt();
                    if (id != null) {
                        Lov lov = lovRepository.findOne(id);
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

        int lastIndex = -1;
        for (int i = 0; i < values.length; i++) {
            String rev = values[i];
            if (rev.equalsIgnoreCase(lastRev)) {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex != -1 && lastIndex < values.length) {
            nextRev = values[lastIndex + 1];
        }
        return nextRev;
    }

    private List<String> getRevisionsForPPAPDocument(PQMPPAPChecklist document) {
        List<String> revs = new ArrayList<>();
        PQMPPAPChecklist revisions = ppapChecklistRepository.findOne(document.getId());
        String rev = revisions.getRevision();
        if (!revs.contains(rev)) {
            revs.add(rev);
        }
        Collections.sort(revs);
        return revs;
    }

    private PQMPPAPChecklist createNextRevForPPAPDocument(PQMPPAPChecklist document, String nextRev) {
        Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
        PQMPPAPChecklist copy = JsonUtils.cloneEntity(document, PQMPPAPChecklist.class);
        document.setLatest(false);
        document = ppapChecklistRepository.save(document);
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.PRELIMINARY, document.getLifeCyclePhase().getLifeCycle());
        copy.setId(null);
        copy.setRevision(nextRev);
        copy.setLifeCyclePhase(lifeCyclePhase);
        copy.setVersion(1);
        copy.setModifiedBy(personId);
        copy.setModifiedDate(new Date());
        copy = ppapChecklistRepository.save(copy);
        String dir = "";

        if (copy.getParentFile() != null) {
            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getReplaceFileSystemPath(copy.getPpap(), copy.getId(), PLMObjectType.PPAPCHECKLIST);
        } else {
            dir = fileSystemService.getCurrentTenantRoot() + File.separator + "filesystem" + File.separator + document.getPpap();
        }

        dir = dir + File.separator + copy.getId();

        String oldFileDir = "";
        if (copy.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(document.getPpap(), document.getId(), PLMObjectType.PPAPCHECKLIST);
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator + "filesystem" + File.separator + document.getPpap() + File.separator + document.getId();
        }

        try {
            if (!dir.equals("")) {
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.createNewFile();
                }

                FileInputStream instream = null;
                FileOutputStream outstream = null;

                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        applicationEventPublisher.publishEvent(new DocumentEvents.DocumentRevisedEvent(document.getPpap(), document.getId(), document, copy, PLMObjectType.PPAPCHECKLIST));
        return copy;
    }

    @Transactional
    public PLMDocumentReviewer addReviewer(Integer id, PLMDocumentReviewer reviewer) {
        reviewer = documentReviewerRepository.save(reviewer);
        PLMFile plmFile = fileRepository.findOne(id);
        Integer object = plmFile.getId();
        if (plmFile.getObjectType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            object = mfrPartInspectionReportRepository.findOne(plmFile.getId()).getManufacturerPart();
        } else if (plmFile.getObjectType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            object = ppapChecklistRepository.findOne(plmFile.getId()).getPpap();
        }
        sendAddedReviewerApproverNotification(reviewer, id, plmFile.getObjectType());
        applicationEventPublisher.publishEvent(new DocumentEvents.DocumentReviewerAddedEvent(object, plmFile, reviewer, plmFile.getObjectType()));
        return reviewer;
    }

    @Transactional
    public PLMDocumentReviewer updateReviewer(Integer id, PLMDocumentReviewer reviewer) {
        reviewer = documentReviewerRepository.save(reviewer);
        PLMFile document = fileRepository.findOne(id);
        Integer object = document.getId();
        if (document.getObjectType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            object = mfrPartInspectionReportRepository.findOne(document.getId()).getManufacturerPart();
        } else if (document.getObjectType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            object = ppapChecklistRepository.findOne(document.getId()).getPpap();
        }
        sendAddedReviewerApproverNotification(reviewer, id, document.getObjectType());
        applicationEventPublisher.publishEvent(new UserTaskEvents.DocumentAssignedEvent(document.getId(), reviewer));
        applicationEventPublisher.publishEvent(new DocumentEvents.DocumentReviewerUpdateEvent(object, document, reviewer, document.getObjectType()));
        return reviewer;
    }

    @Transactional
    public void deleteReviewer(Integer docId, Integer reviewerId) {
        PLMDocumentReviewer documentReviewer = documentReviewerRepository.findOne(reviewerId);
        PLMFile document = fileRepository.findOne(docId);
        Integer object = document.getId();
        if (document.getObjectType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            object = mfrPartInspectionReportRepository.findOne(document.getId()).getManufacturerPart();
        } else if (document.getObjectType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            object = ppapChecklistRepository.findOne(document.getId()).getPpap();
        }
        applicationEventPublisher.publishEvent(new UserTaskEvents.DocumentDeletedEvent(documentReviewer.getDocument(), documentReviewer));
        applicationEventPublisher.publishEvent(new DocumentEvents.DocumentReviewerDeletedEvent(object, document, documentReviewer, document.getObjectType()));
        documentReviewerRepository.delete(reviewerId);
    }

    @Transactional(readOnly = true)
    public List<PLMDocumentReviewer> getReviewers(Integer docId) {
        List<PLMDocumentReviewer> reviewers = documentReviewerRepository.findByDocumentOrderByIdDesc(docId);
        reviewers.forEach(reviewer -> {
            reviewer.setReviewerName(personRepository.findOne(reviewer.getReviewer()).getFullName());
        });
        return reviewers;
    }

    @Transactional
    public PLMDocumentReviewer submitReview(Integer docId, PLMDocumentReviewer documentReviewer) {
        PLMFile document = fileRepository.findOne(docId);
        documentReviewer.setVoteTimestamp(new Date());
        documentReviewer = documentReviewerRepository.save(documentReviewer);
        if (document.getObjectType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name()) || document.getObjectType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            List<PLMFile> initialVersionFiles = fileRepository.findByFileNoOrderByIdAsc(document.getFileNo());
            Person person = null;
            if (initialVersionFiles.size() > 0) {
                person = personRepository.findOne(initialVersionFiles.get(0).getCreatedBy());
            } else {
                person = personRepository.findOne(document.getCreatedBy());
            }
            Login login = loginRepository.findByPersonId(person.getId());
            if (login != null && login.getExternal()) {
                sendFileCommentNotification(docId, documentReviewer, login, document.getObjectType().name());
            }
        }
        Integer object = document.getId();
        if (document.getObjectType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            object = mfrPartInspectionReportRepository.findOne(document.getId()).getManufacturerPart();
        } else if (document.getObjectType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            object = ppapChecklistRepository.findOne(document.getId()).getPpap();
        }
        applicationEventPublisher.publishEvent(new UserTaskEvents.DocumentSubmittedEvent(documentReviewer.getDocument(), documentReviewer));
        applicationEventPublisher.publishEvent(new DocumentEvents.DocumentReviewerSubmittedEvent(object, document, documentReviewer, document.getObjectType()));
        return documentReviewer;
    }

    public boolean checkDocumentPermissions(Authentication authentication, String action, String objectType, Integer docId) {
        Login login = (Login) authentication.getPrincipal();
        boolean dmPermissionCheck = false;
        if (!login.getIsAdmin()) {
            if (docId != null) dmPermissionCheck = checkSubFolderPermission(docId, action, false, "child");
            boolean privilegeCheck = customePrivilegeFilter.filterPrivilage(authentication, action, objectType);
            if (dmPermissionCheck || privilegeCheck) return true;
        } else return true;
        return false;
    }

    public boolean checkSubFolderPermission(Integer folderId, String action, boolean flag1, String type) {
        folderFlag = flag1;
        List<DMFolderPermission> dmFolderPermissions = dmFolderPermissionRepository.findByFolderId(folderId);
        PLMFile document = fileRepository.findOne(folderId);
        if (dmFolderPermissions.size() > 0 && !folderFlag) {
            for (DMFolderPermission dmFolderPermission : dmFolderPermissions) {
                boolean dmPermissionCheck = dmFolderPermission.getActions().contains(action) || dmFolderPermission.getActions().contains("all");
                boolean folderIdCheck = Objects.equals(document.getId(), dmFolderPermission.getFolderId());
                boolean subFolderCheck = dmFolderPermission.getIsSubFolder();
                if (dmPermissionCheck && folderIdCheck && !folderFlag) {
                    if (type.equals("child")) folderFlag = true;
                    else folderFlag = subFolderCheck;
                } else if (document.getParentFile() != null && !folderFlag) {
                    checkSubFolderPermission(document.getParentFile(), action, folderFlag, "parent");
                } else folderFlag = false;
            }
        } else if (document != null && document.getParentFile() != null) {
            checkSubFolderPermission(document.getParentFile(), action, folderFlag, "parent");
        } else folderFlag = false;
        return folderFlag;
    }

    public boolean filterDMPermissions(Authentication authentication, List<PLMDocument> documents, String action) {
        Login login = (Login) authentication.getPrincipal();
        boolean privilegeCheck = customePrivilegeFilter.filterPrivilage(authentication, action, "document");
        if (!login.getIsAdmin() && !privilegeCheck) {
            List<DMFolderPermission> dmFolderPermissions = getDocumentFolderPermissions(login);
            for (DMFolderPermission dmFolderPermission : dmFolderPermissions) {
                for (PLMDocument document : documents) {
                    boolean dmPermissionCheck = Objects.equals(document.getId(), dmFolderPermission.getFolderId())
                            && (dmFolderPermission.getActions().contains("all") || dmFolderPermission.getActions().contains(action));
                    if (dmPermissionCheck) return true;
                }
            }
        } else return true;
        return false;
    }

    public boolean checkDMPemrmissions(Authentication authentication, String action, String objectType, Integer fileId) {
        Login login = (Login) authentication.getPrincipal();
        if (!login.getIsAdmin() && !login.getExternal()) {
            if (objectType.equalsIgnoreCase("document")) {
                boolean dmPermissionCheck;
                PLMFile file = fileRepository.findById(fileId);
                if (!action.equals("create"))
                    dmPermissionCheck = checkDocumentPermissions(authentication, action, objectType, file.getParentFile());
                else dmPermissionCheck = checkDocumentPermissions(authentication, action, objectType, fileId);
                return dmPermissionCheck;
            }
        } else if (login.getExternal()) {
            if (objectType.equalsIgnoreCase("mfrpartinspectionreport")) {
                return true;
            } else {
                boolean dmPermissionCheck;
                PLMFile file = fileRepository.findById(fileId);
                if (!action.equals("create"))
                    dmPermissionCheck = checkDocumentPermissions(authentication, action, objectType, file.getParentFile());
                else dmPermissionCheck = checkDocumentPermissions(authentication, action, objectType, fileId);
                return dmPermissionCheck;
            }
        } else return true;
        return false;
    }

    public List<DMFolderPermission> getDocumentFolderPermissions(Login login) {
        List<DMFolderPermission> dmFolderPermissions = new ArrayList<>();
        List<GroupMember> groupMembers = groupMemberRepository.findByPerson(login.getPerson());
        List<Integer> groupIds = new ArrayList<>();
        groupMembers.forEach(groupMember -> groupIds.add(groupMember.getPersonGroup().getGroupId()));
        if (groupIds.size() > 0)
            dmFolderPermissions = dmFolderPermissionRepository.getDMFolderPermissionsByGroupIds(groupIds);
        return dmFolderPermissions;
    }

    public void sendFileCommentNotification(Integer fileId, PLMDocumentReviewer documentReviewer, Login login, String objectType) {

        String fileType = "";
        String typeName = "";
        String objectName = "";
        String fileName = "";
        String subject = "";
        if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(fileId);
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(mfrPartInspectionReport.getManufacturerPart());
            objectName = manufacturerPart.getPartName();
            fileName = mfrPartInspectionReport.getName();
            typeName = "manufacturer part";
            fileType = "inspection report";
            if (documentReviewer.getStatus().equals(DocumentApprovalStatus.APPROVED)) {
                subject = "Inspection Report Approved Notification";
            } else if (documentReviewer.getStatus().equals(DocumentApprovalStatus.REJECTED)) {
                subject = "Inspection Report Rejected Notification";
            } else if (documentReviewer.getStatus().equals(DocumentApprovalStatus.REVIEWED)) {
                subject = "Inspection Report Reviewed Notification";
            }
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST.name())) {
            PQMPPAPChecklist checklist = ppapChecklistRepository.findOne(fileId);
            PQMPPAP pqmppap = ppapRepository.findOne(checklist.getPpap());
            objectName = pqmppap.getNumber();
            fileName = checklist.getName();
            typeName = "ppap";
            fileType = "checklist";
            if (documentReviewer.getStatus().equals(DocumentApprovalStatus.APPROVED)) {
                subject = "PPAP Checklist Approved Notification";
            } else if (documentReviewer.getStatus().equals(DocumentApprovalStatus.REJECTED)) {
                subject = "PPAP Checklist Rejected Notification";
            } else if (documentReviewer.getStatus().equals(DocumentApprovalStatus.REVIEWED)) {
                subject = "PPAP Checklist Reviewed Notification";
            }
        }

        Person approver = personRepository.findOne(documentReviewer.getReviewer());

        final String notificationTypeFinal = "ApprovedComments";
        final String objectDetails = objectName;
        final String fileTypeDetails = fileType;
        final String typeNameDetails = typeName;
        final String fileNameDetails = fileName;
        final String subjectName = subject;
        String tenantId = sessionWrapper.getSession().getTenantId();
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String host = url.substring(0, url.indexOf(uri));
        new Thread(() -> {
            Map<String, Object> model = new HashMap<>();
            model.put("host", host);
            model.put("fileName", fileNameDetails);
            model.put("fileType", fileTypeDetails);
            model.put("typeName", typeNameDetails);
            model.put("approverName", approver.getFullName());
            model.put("objectName", objectDetails);
            model.put("comment", documentReviewer.getNotes());
            if (documentReviewer.getStatus().equals(DocumentApprovalStatus.APPROVED)) {
                model.put("approvalType", "approved");
            } else if (documentReviewer.getStatus().equals(DocumentApprovalStatus.REJECTED)) {
                model.put("approvalType", "rejected");
            } else if (documentReviewer.getStatus().equals(DocumentApprovalStatus.REVIEWED)) {
                model.put("approvalType", "reviewed");
            }
            model.put("tenantId", tenantId);
            model.put("notificationType", notificationTypeFinal);
            Mail mail = new Mail();
            mail.setMailSubject(subjectName);
            mail.setTemplatePath("email/fileCommentNotification.html");
            mail.setModel(model);
            model.put("personName", login.getPerson().getFullName());
            mail.setMailTo(login.getPerson().getEmail());
            mailService.sendEmail(mail);
        }).start();
    }

    public void sendPpapApprovedNotification(PQMPPAP ppap) {

        PLMSupplier supplier = supplierRepository.findOne(ppap.getSupplier());
        Person person = personRepository.findOne(ppap.getCreatedBy());
        List<String> emails = new ArrayList<>();
        if (supplier != null && supplier.getEmail() != null) {
            emails.add(supplier.getEmail());
        }
        if (person != null && person.getEmail() != null) {
            emails.add(person.getEmail());
        }
        String email = "";
        String[] recipientAddress = null;

        if (emails.size() > 0) {
            recipientAddress = new String[emails.size()];
            for (int i = 0; i < emails.size(); i++) {
                String em = emails.get(i);
                if (email.equals("")) {
                    email = em;
                } else {
                    email = email + "," + em;
                }
            }
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }
            final String[] recipientAddressFinal = recipientAddress;
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("ppapName", ppap.getNumber() + " - " + ppap.getName());
                if (emails.size() == 1) {
                    if (person != null) {
                        model.put("personName", person.getFullName());
                    }
                } else {
                    model.put("personName", "All");
                }
                Mail mail = new Mail();
                mail.setMailSubject("PPAP Approved Notification");
                mail.setTemplatePath("ppapNotification.html");
                mail.setModel(model);
                mail.setMailToList(recipientAddressFinal);
                mailService.sendEmail(mail);
            }).start();
        }
    }

    public void sendDemotedNotification(Integer fileId, Login login, PLMObjectType objectType) {
        String demotedBy = "";
        String objectName = "";
        String documentName = "";
        String typeName = "";
        if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            PLMMfrPartInspectionReport inspectionReport = mfrPartInspectionReportRepository.findOne(fileId);
            documentName = inspectionReport.getName();
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(inspectionReport.getManufacturerPart());
            objectName = manufacturerPart.getPartNumber() + " - " + manufacturerPart.getPartName();
            typeName = "inspection report";
        } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
            PQMPPAPChecklist checklist = ppapChecklistRepository.findOne(fileId);
            documentName = checklist.getName();
            PQMPPAP pqmppap = ppapRepository.findOne(checklist.getPpap());
            objectName = pqmppap.getNumber() + " - " + pqmppap.getName();
            typeName = "checklist";
        }

        if (sessionWrapper != null && sessionWrapper.getSession() != null) {
            demotedBy = sessionWrapper.getSession().getLogin().getPerson().getFullName();
        }
        if (login.getPerson().getEmail() != null && !login.getPerson().getEmail().equals("")) {
            final String typeNameFinal = typeName;
            final String objectNameFinal = objectName;
            final String documentNameFinal = documentName;
            String email = login.getPerson().getEmail();
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            final String demotedByName = demotedBy;
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("documentName", documentNameFinal);
                model.put("personName", login.getPerson().getFullName());
                model.put("demotedBy", demotedByName);
                model.put("objectName", objectNameFinal);
                model.put("typeName", typeNameFinal);
                Mail mail = new Mail();
                if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
                    mail.setMailSubject("Inspection Report DEMOTE Notification");
                } else if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) {
                    mail.setMailSubject("PPAP Checklist DEMOTE Notification");
                }
                mail.setTemplatePath("inspectionReportNotification.html");
                mail.setModel(model);
                mail.setMailTo(email);
                mailService.sendEmail(mail);
            }).start();
        }

    }

    public void sendAddedReviewerApproverNotification(PLMDocumentReviewer documentReviewer, Integer fileId, Enum objectType) {

        String checklistName = "";
        String subject = "";
        PLMFile plmFile = fileRepository.findById(fileId);
        checklistName = plmFile.getName();
        String type = "PPAP Checklist";
        if (documentReviewer.getApprover()) {
            if (objectType.name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
                subject = "PPAP Checklist Approver Notification";
            } else if (objectType.name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
                subject = "Inspection Report Approver Notification";
                type = "Inspection Report";
            } else if (objectType.name().equals(PLMObjectType.DOCUMENT.name())) {
                subject = "Document Approver Notification";
                type = "Document";
            }
        } else {
            if (objectType.name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
                subject = "PPAP Checklist Reviewer Notification";
            } else if (objectType.name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
                subject = "Inspection Report Reviewer Notification";
                type = "Inspection Report";
            } else if (objectType.name().equals(PLMObjectType.DOCUMENT.name())) {
                subject = "Document Reviewer Notification";
                type = "Document";
            }
        }

        Person approver = personRepository.findOne(documentReviewer.getReviewer());
        final String checklistNameFinal = checklistName;
        final String typeName = type;
        final String subjectName = subject;
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String host = url.substring(0, url.indexOf(uri));
        new Thread(() -> {
            Map<String, Object> model = new HashMap<>();
            model.put("host", host);
            model.put("approverName", approver.getFullName());
            model.put("checklistName", checklistNameFinal);
            model.put("type", typeName);
            Mail mail = new Mail();
            if (documentReviewer.getApprover()) {
                model.put("approvalType", " an approver");

            } else {
                model.put("approvalType", " a reviewer");

            }
            mail.setMailSubject(subjectName);
            mail.setTemplatePath("email/ppapChecklistAddReviewersNotification.html");
            mail.setModel(model);
            mail.setMailTo(approver.getEmail());
            mailService.sendEmail(mail);
        }).start();
    }

}
