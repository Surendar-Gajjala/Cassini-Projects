package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.service.common.ForgeService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.ItemEvents;
import com.cassinisys.plm.model.cm.PLMChangeFile;
import com.cassinisys.plm.model.mes.MESBOPFile;
import com.cassinisys.plm.model.mes.MESBOPOperationFile;
import com.cassinisys.plm.model.mes.MESMBOMFile;
import com.cassinisys.plm.model.mes.MESObjectFile;
import com.cassinisys.plm.model.mfr.PLMManufacturerFile;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartFile;
import com.cassinisys.plm.model.mfr.PLMMfrPartInspectionReport;
import com.cassinisys.plm.model.mro.MROObjectFile;
import com.cassinisys.plm.model.pgc.PGCObjectFile;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pm.PLMActivityFile;
import com.cassinisys.plm.model.pm.PLMProgramFile;
import com.cassinisys.plm.model.pm.PLMProjectFile;
import com.cassinisys.plm.model.pm.PLMTaskFile;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.rm.PLMGlossaryFile;
import com.cassinisys.plm.model.rm.RmObjectFile;
import com.cassinisys.plm.repo.cm.ChangeFileRepository;
import com.cassinisys.plm.repo.mes.BOPFileRepository;
import com.cassinisys.plm.repo.mes.BOPOperationFileRepository;
import com.cassinisys.plm.repo.mes.MBOMFileRepository;
import com.cassinisys.plm.repo.mes.MESObjectFileRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerFileRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartFileRepository;
import com.cassinisys.plm.repo.mfr.MfrPartInspectionReportRepository;
import com.cassinisys.plm.repo.mro.MROObjectFileRepository;
import com.cassinisys.plm.repo.pgc.PGCObjectFileRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.rm.GlossaryFileRepository;
import com.cassinisys.plm.repo.rm.RmObjectFileRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.mfr.ManufacturerFileService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

;

/**
 * Created by reddy on 22/12/15.
 */
@Service
@Transactional
public class ItemFileService implements CrudService<PLMItemFile, Integer>,
        PageableService<PLMItemFile, Integer> {

    @Autowired
    private ItemFileRepository itemFileRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FileDownloadHistoryRepository fileDownloadHistoryRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private ForgeService forgeService;

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private AutoNumberService autoNumberService;

    @Autowired
    private FileHelpers fileHelpers;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private ItemService itemService;
    @Autowired
    private EMailTemplateConfigRepository eMailTemplateConfigRepository;

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
    private ManufacturerPartFileRepository manufacturerPartFileRepository;

    @Autowired
    private ManufacturerFileRepository manufacturerFileRepository;

    @Autowired
    private RmObjectFileRepository rmObjectFileRepository;
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
    private MessageSource messageSource;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ObjectFileService qualityFileService;
    @Autowired
    private ManufacturerFileService manufacturerFileService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private MESObjectFileRepository mesObjectFileRepository;
    @Autowired
    private MROObjectFileRepository mroObjectFileRepository;
    @Autowired
    private MfrPartInspectionReportRepository mfrPartInspectionReportRepository;
    @Autowired
    private MBOMFileRepository mbomFileRepository;
    @Autowired
    private BOPFileRepository bopFileRepository;
    @Autowired
    private ProgramFileRepository programFileRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FileDownloadService fileDownloadService;
    @Autowired
    private ItemFileService itemFileService;
    @Autowired
    private PLMDocumentRepository plmDocumentRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private ObjectFileService objectFileService;
    @Autowired
    private PGCObjectFileRepository pgcObjectFileRepository;
    @Autowired
    private PPAPChecklistRepository ppapChecklistRepository;
    @Autowired
    private SupplierAuditFileRepository supplierAuditFileRepository;
    @Autowired
    private BOPOperationFileRepository bopOperationFileRepository;

    @Override
    public PLMItemFile create(PLMItemFile pdmItemFile) {
        pdmItemFile.setId(null);
        pdmItemFile = itemFileRepository.save(pdmItemFile);
        return pdmItemFile;
    }

    @Override
    public PLMItemFile update(PLMItemFile pdmItemFile) {
        pdmItemFile = itemFileRepository.save(pdmItemFile);
        return pdmItemFile;
    }

    @PreAuthorize("hasPermission(#id ,'edit')")
    public PLMFile updateFile(Integer id, PLMItemFile plmItemFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            PLMItemFile itemFile = itemFileRepository.findOne(file.getId());
            if (!file.getLocked().equals(plmItemFile.getLocked())) {
                /* App events */
                if (plmItemFile.getLocked()) {
                    applicationEventPublisher.publishEvent(new ItemEvents.ItemFileLockedEvent(itemFile.getItem(), itemFile));
                } else {
                    applicationEventPublisher.publishEvent(new ItemEvents.ItemFileUnlockedEvent(itemFile.getItem(), itemFile));
                }
            }
            file.setDescription(plmItemFile.getDescription());
            file.setLocked(plmItemFile.getLocked());
            file.setLockedBy(plmItemFile.getLockedBy());
            file.setLockedDate(plmItemFile.getLockedDate());
            file = fileRepository.save(file);
            file.setParentObject(PLMObjectType.ITEM);
        }
        return file;
    }

    public PLMFile moveItemFileToFolder(Integer id, PLMItemFile plmItemFile) throws Exception {
        PLMItemFile file = itemFileRepository.findOne(plmItemFile.getId());
        PLMItemFile existFile = (PLMItemFile) Utils.cloneObject(file, PLMItemFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getItem().getId(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getItem().getId() + File.separator + existFile.getId();
        }
        if (plmItemFile.getParentFile() != null) {
            PLMItemFile existItemFile = itemFileRepository.findByParentFileAndNameAndLatestTrue(plmItemFile.getParentFile(), plmItemFile.getName());
            PLMItemFile folder = itemFileRepository.findOne(plmItemFile.getParentFile());
            if (existItemFile != null) {
                throw new CassiniException(" [ " + existItemFile.getName() + " ] File already exist in [ " + folder.getName() + " ] folder");
            } else {
                plmItemFile.setParent(plmItemFile.getParentFile());
                plmItemFile.setParentFile(plmItemFile.getParentFile());
                folder.setModifiedDate(new Date());
                itemFileRepository.save(folder);
                plmItemFile = itemFileRepository.save(plmItemFile);
            }
        } else {
            PLMItemFile existItemFile = itemFileRepository.findByItemAndNameAndParentFileIsNullAndLatestTrue(plmItemFile.getItem(), plmItemFile.getName());
            PLMItem item = itemRepository.findOne(plmItemFile.getItem().getItemMaster());
            if (existItemFile != null) {
                throw new CassiniException(" [ " + existItemFile.getName() + " ] File already exist in [ " + item.getItemNumber() + " ] Item ");
            } else {
                plmItemFile = itemFileRepository.save(plmItemFile);
            }
        }

        if (plmItemFile != null) {
            plmItemFile.setParentObject(PLMObjectType.ITEM);
            String dir = "";
            if (plmItemFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(plmItemFile.getItem().getId(), plmItemFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmItemFile.getItem().getId() + File.separator + plmItemFile.getId();
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
            List<PLMItemFile> oldVersionFiles = itemFileRepository.findByItemAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getItem(), existFile.getFileNo());
            for (PLMItemFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getItem().getId(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getItem().getId() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(plmItemFile.getItem().getId(), plmItemFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParent(plmItemFile.getParentFile());
                oldVersionFile.setParentFile(plmItemFile.getParentFile());
                oldVersionFile = itemFileRepository.save(oldVersionFile);
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
        return plmItemFile;
    }

    private String getFolderName(String folderName, PLMItemFile folder) {

        if (folder.getParentFile() != null) {
            PLMItemFile parentFolder = itemFileRepository.findOne(folder.getParentFile());
            if (folderName == null || folderName.equals("")) {
                folderName = folder.getName();
            } else {
                folderName = folder.getName() + " / " + folderName;
            }
            folderName = getFolderName(folderName, parentFolder);
        } else {
            if (folderName == null || folderName.equals("")) {
                folderName = folder.getName();
            } else {
                folderName = folder.getName() + " / " + folderName;
            }
        }

        return folderName;
    }

    public PLMItemFile updateFileName(Integer id, String newFileName) throws IOException {
        PLMItemFile file1 = itemFileRepository.findOne(id);
        file1.setLatest(false);
        PLMItemFile plmItemFile = itemFileRepository.save(file1);
        PLMItemFile itemFile = (PLMItemFile) Utils.cloneObject(plmItemFile, PLMItemFile.class);
        PLMItemFile oldFile = (PLMItemFile) Utils.cloneObject(plmItemFile, PLMItemFile.class);

        if (itemFile != null) {
            itemFile.setId(null);
            itemFile.setName(newFileName);
            itemFile.setVersion(file1.getVersion() + 1);
            itemFile.setLatest(true);
            itemFile.setReplaceFileName(file1.getName() + " Rename to " + newFileName);
            itemFile = itemFileRepository.save(itemFile);
            if (itemFile.getParentFile() != null) {
                PLMItemFile parent = itemFileRepository.findOne(itemFile.getParentFile());
                parent.setModifiedDate(itemFile.getModifiedDate());
                parent = itemFileRepository.save(parent);
            }
            itemFile.setParentObject(PLMObjectType.ITEM);
            qualityFileService.copyFileAttributes(file1.getId(), itemFile.getId());

            String dir = "";
            if (plmItemFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(itemFile.getItem().getId(), id);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + itemFile.getItem().getId();
            }
            dir = dir + File.separator + itemFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }

            String oldFileDir = "";
            if (plmItemFile.getParentFile() != null) {
                oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(itemFile.getItem().getId(), id);
            } else {
                oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + itemFile.getItem().getId() + File.separator + file1.getId();
            }
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            String fileNames = "";
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemFile.getItem().getId());
            List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(itemRevision.getItemMaster());
            if (subscribes.size() > 0) {
                PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                Person person = sessionWrapper.getSession().getLogin().getPerson();
                fileNames = person.getFullName() + "has Change " + itemFile.getReplaceFileName() + "   to (" + item.getItemNumber() + " - " + item.getItemName() + " : Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " ) Item";
                String mailSubject = item.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
                itemService.sendItemSubscribeNotification(item, fileNames, mailSubject);
            }

            /* App Events */
            applicationEventPublisher.publishEvent(new ItemEvents.ItemFileRenamedEvent(itemRevision, oldFile, itemFile, "Rename"));
        }
        return itemFile;
    }

    @Override
    public void delete(Integer id) {
        PLMItemFile pdmItemFile = itemFileRepository.findOne(id);
        PLMItemRevision item = itemRevisionRepository.findOne(pdmItemFile.getItem().getId());
        itemFileRepository.delete(id);
        List<PLMItemFile> files = findByItem(item);
        if (files.size() == 0) {
            item.setHasFiles(Boolean.FALSE);
            itemRevisionRepository.save(item);
        }
    }

    @PreAuthorize("hasPermission(#fileId,'delete')")
    public void deleteItemFile(Integer itemId, Integer id) {
        checkNotNull(id);
        PLMItemFile plmItemFile = itemFileRepository.findOne(id);
        String fName = plmItemFile.getName();
        String fileName = plmItemFile.getName() + " - Version " + plmItemFile.getVersion();
        List<PLMItemFile> itemFiles = itemFileRepository.findByItemIdAndFileNo(itemId, plmItemFile.getFileNo());
        for (PLMItemFile itemFile : itemFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(itemFile.getItem().getId(), itemFile.getId());
            fileSystemService.deleteDocumentFromDisk(itemFile.getId(), dir);
            itemFileRepository.delete(itemFile.getId());
        }
        if (plmItemFile.getParentFile() != null) {
            PLMItemFile parent = itemFileRepository.findOne(plmItemFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = itemFileRepository.save(parent);
        }
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        applicationEventPublisher.publishEvent(new ItemEvents.ItemFileDeletedEvent(itemRevision, plmItemFile));
        PLMItem item = null;
        String mailSubject = null;
        Person person = sessionWrapper.getSession().getLogin().getPerson();

        if (itemRevision != null) {
            List<PLMItemFile> files = itemFileRepository.findByItem(itemRevision);
            if (files.size() == 0) {
                itemRevision.setHasFiles(false);
            }
            itemRevision = itemRevisionRepository.save(itemRevision);
            item = itemRepository.findOne(itemRevision.getItemMaster());
            fileName = fileName + " File deleted by " + person.getFullName() + " from (" + item.getItemNumber() + " - " + item.getItemName() + " - Rev"
                    + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " ) Item";
            mailSubject = item.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
        }
        itemService.sendItemSubscribeNotification(item, fileName, mailSubject);

    }

    @Override
    public PLMItemFile get(Integer id) {
        return itemFileRepository.findOne(id);
    }

    @Override
    public List<PLMItemFile> getAll() {
        return itemFileRepository.findAll();
    }

    @Override
    public Page<PLMItemFile> findAll(Pageable pageable) {
        return itemFileRepository.findAll(pageable);
    }

    public List<PLMItemFile> findByItem(PLMItemRevision item) {
        List<PLMItemFile> itemFiles = itemFileRepository.findByItemAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(item);
        itemFiles.forEach(plmItemFile -> {
            plmItemFile.setParentObject(PLMObjectType.ITEM);
            Person person = personRepository.findOne(plmItemFile.getCreatedBy());
            plmItemFile.setCreatedPerson(person.getFirstName());
            if (plmItemFile.getFileType().equals("FOLDER")) {
                plmItemFile.setCount(itemFileRepository.getChildrenCountByParentFileAndLatestTrue(plmItemFile.getId()));
                plmItemFile.setCount(plmItemFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(plmItemFile.getItem().getId(), plmItemFile.getId()));
            }
        });
        return itemFiles;
    }

    public List<PLMItemFile> findByItemandFileName(PLMItemRevision item, String name) {
        return itemFileRepository.findByItemAndNameContainingIgnoreCaseAndLatestTrue(item, name);
    }

    public List<PLMItemFile> findAllByItem(PLMItemRevision item) {
        return itemFileRepository.findByItemAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(item);
    }

    public PLMItemFile uploadContentAsFile(Integer itemId, String fileName, byte[] bytes) {
        PLMItemFile itemFile = null;
        try {
            List<Comment> comments = new ArrayList<>();
            PLMItemRevision revision = itemRevisionRepository.findOne(itemId);
            Login login = sessionWrapper.getSession().getLogin();
            itemFile = itemFileRepository.findByItemAndNameAndLatestTrue(revision, fileName);
            if (itemFile != null) {
                comments = commentRepository.findAllByObjectId(itemFile.getId());
            }
            Integer version = 1;
            if (itemFile != null) {
                itemFile.setLatest(false);
                Integer oldVersion = itemFile.getVersion();
                version = oldVersion + 1;
                itemFileRepository.save(itemFile);
            }
            itemFile = new PLMItemFile();
            itemFile.setName(fileName);
            itemFile.setCreatedBy(login.getPerson().getId());
            itemFile.setModifiedBy(login.getPerson().getId());
            itemFile.setItem(revision);
            itemFile.setVersion(version);
            itemFile.setSize((long) bytes.length);
            itemFile = itemFileRepository.save(itemFile);
            for (Comment comment : comments) {
                Comment newComment = new Comment();
                newComment.setCommentedDate(comment.getCommentedDate());
                newComment.setCommentedBy(comment.getCommentedBy());
                newComment.setComment(comment.getComment());
                newComment.setObjectType(comment.getObjectType());
                newComment.setObjectId(itemFile.getId());
                newComment.setReplyTo(comment.getReplyTo());
                commentRepository.save(newComment);
            }
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + revision.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            String path = dir + File.separator + itemFile.getId();
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(bytes, new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemFile;
    }

    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','file')")
    public List<PLMItemFile> uploadItemFiles(Integer itemId, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMItemFile> uploaded = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        String[] fileExtension = null;
        boolean flag = true;
        List<PLMItemFile> plmItemFiles = new ArrayList<>();
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        String fNames = null;

        List<PLMItemFile> newFiles = new ArrayList<>();
        List<PLMItemFile> versionedFiles = new ArrayList<>();

        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    boolean versioned = false;

                    PLMItemFile itemFile = null;
                    if (folderId == 0) {
                        itemFile = itemFileRepository.findByItemAndNameAndParentFileIsNullAndLatestTrue(itemRevision, name);
                    } else {
                        itemFile = itemFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer version = 1;
                    Integer oldVersion = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (itemFile != null) {
                        itemFile.setLatest(false);
                        oldVersion = itemFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = itemFile.getFileNo();
                        oldFile = itemFile.getId();
                        itemFile = itemFileRepository.save(itemFile);

                        versioned = true;
                    }
                    if (itemFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    itemFile = new PLMItemFile();
                    itemFile.setName(name);
                    itemFile.setFileNo(autoNumber1);
                    /*itemFile.setReplaceFileName(name);*/
                    itemFile.setCreatedBy(login.getPerson().getId());
                    itemFile.setModifiedBy(login.getPerson().getId());
                    itemFile.setItem(itemRevision);
                    itemFile.setVersion(version);
                    itemFile.setSize(file.getSize());
                    itemFile.setFileType("FILE");
                    itemFile.setType("FILE");
                    if (folderId != 0) {
                        itemFile.setParentFile(folderId);
                        itemFile.setParent(folderId);
                        PLMItemFile itemFolder = itemFileRepository.findOne(folderId);
                        itemFolder.setModifiedDate(new Date());
                        itemFileRepository.save(itemFolder);
                    }
                    itemFile = itemFileRepository.save(itemFile);
                    if (itemFile.getVersion() > 1) {
                        qualityFileService.copyFileAttributes(oldFile, itemFile.getId());
                    }
                    itemFile.setOldVersion(oldVersion);
                    plmItemFiles.add(itemFile);
                    if (fileNames == null) {
                        fNames = itemFile.getName();
                        fileNames = itemFile.getName() + " - Version : " + itemFile.getVersion();
                    } else {
                        fNames = fNames + "  , " + itemFile.getName();
                        fileNames = fileNames + " , " + itemFile.getName() + " - Version : " + itemFile.getVersion();
                    }
                    itemRevision.setHasFiles(true);
                    itemRevision = itemRevisionRepository.save(itemRevision);
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + itemId;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getParentFileSystemPath(itemId, folderId);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + itemFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    /*Map<String, String> map = forgeService.uploadForgeFile(file.getOriginalFilename(), path);
                    if (map != null) {
                        itemFile.setUrn(map.get("urn"));
                        itemFile.setThumbnail(map.get("thumbnail"));
                        itemFile = itemFileRepository.save(itemFile);
                    }*/
                    uploaded.add(itemFile);
                    List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(itemRevision.getItemMaster());
                    if (subscribes.size() > 0) {
                        Person person = sessionWrapper.getSession().getLogin().getPerson();
                        fileNames = fileNames + " files added by " + person.getFullName() + " to (" + item.getItemNumber() + " - " + item.getItemName() + " : Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " ) Item";
                        String mailSubject = item.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
                        itemService.sendItemSubscribeNotification(item, fileNames, mailSubject);
                    }
                    itemService.updateItem(itemId);

                    if (versioned) {
                        versionedFiles.add(itemFile);
                    } else {
                        newFiles.add(itemFile);
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /* App Events */
        if (newFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new ItemEvents.ItemFilesAddedEvent(itemRevision, newFiles));
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new ItemEvents.ItemFilesVersionedEvent(itemRevision, versionedFiles));
        }

        return uploaded;
    }

    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'replace','file')")
    public List<PLMItemFile> replaceItemFiles(Integer itemId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMItemFile> uploaded = new ArrayList<>();
        PLMItemRevision revision = itemRevisionRepository.findOne(itemId);
        Login login = sessionWrapper.getSession().getLogin();

        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        String fNames = null;
        String name = null;
        PLMItemFile plmItemFile = null;
        PLMItemFile oldFile = null;

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
                    PLMItemFile itemFile = null;
                    plmItemFile = itemFileRepository.findOne(fileId);
                    if (plmItemFile != null && plmItemFile.getParentFile() != null) {
                        itemFile = itemFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        itemFile = itemFileRepository.findByItemAndNameAndParentFileIsNullAndLatestTrue(revision, name);
                    }

                    if (plmItemFile != null) {
                        plmItemFile.setLatest(false);
                        plmItemFile = itemFileRepository.save(plmItemFile);
                        oldFile = JsonUtils.cloneEntity(plmItemFile, PLMItemFile.class);
                    }

                    itemFile = new PLMItemFile();
                    itemFile.setName(name);
                    if (plmItemFile != null && plmItemFile.getParentFile() != null) {
                        itemFile.setParentFile(plmItemFile.getParentFile());
                        itemFile.setParent(plmItemFile.getParentFile());
                    }
                    if (plmItemFile != null) {
                        itemFile.setFileNo(plmItemFile.getFileNo());
                        itemFile.setVersion(plmItemFile.getVersion() + 1);
                        itemFile.setReplaceFileName(plmItemFile.getName() + " Replaced to " + name);
                    }
                    itemFile.setCreatedBy(login.getPerson().getId());
                    itemFile.setModifiedBy(login.getPerson().getId());
                    itemFile.setItem(revision);
                    itemFile.setFileType("FILE");
                    itemFile.setType("FILE");
                    itemFile.setSize(file.getSize());
                    itemFile = itemFileRepository.save(itemFile);
                    if (itemFile.getParentFile() != null) {
                        PLMItemFile parent = itemFileRepository.findOne(itemFile.getParentFile());
                        parent.setModifiedDate(itemFile.getModifiedDate());
                        parent = itemFileRepository.save(parent);
                    }
                    qualityFileService.copyFileAttributes(plmItemFile.getId(), itemFile.getId());
                    if (fileNames == null) {
                        fNames = itemFile.getName();
                        fileNames = itemFile.getName() + " - Version : " + itemFile.getVersion();
                    } else {
                        fNames = fNames + " , " + itemFile.getName();
                        fileNames = fileNames + " , " + itemFile.getName() + " - Version : " + itemFile.getVersion();
                    }
                    revision.setHasFiles(true);
                    revision = itemRevisionRepository.save(revision);
                    String dir = "";
                    if (plmItemFile != null && plmItemFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(itemId, fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + itemId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + itemFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(itemFile);
                    List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(revision.getItemMaster());
                    if (subscribes.size() > 0) {
                        Person person = sessionWrapper.getSession().getLogin().getPerson();
                        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
                        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                        fileNames = person.getFullName() + "has Change " + itemFile.getReplaceFileName() + " to (" + item.getItemNumber() + " - " + item.getItemName() + " : Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " ) Item";
                        String mailSubject = item.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
                        itemService.sendItemSubscribeNotification(item, fileNames, mailSubject);
                    }
                    /* App Events */
                    applicationEventPublisher.publishEvent(new ItemEvents.ItemFileRenamedEvent(revision, oldFile, itemFile, "Replace"));
                }
                break;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    public List<PLMItemFile> getAllFileVersions(Integer itemRevision, String name) {
        List<PLMItemFile> plmItemFiles = itemFileRepository.findAllByItemAndName(itemRevision, name);
        for (PLMItemFile plmItemFile : plmItemFiles) {
            List<Comment> comments = commentRepository.findAllByObjectId(plmItemFile.getId());
            plmItemFile.setComments(comments);
        }
        return plmItemFiles;
    }

    protected void saveFileToDisk(File fileToSave, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            IOUtils.copy(new FileInputStream(fileToSave), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    public File getItemFile(Integer itemId, Integer fileId) {
        checkNotNull(itemId);
        checkNotNull(fileId);
        PLMItemRevision revision = itemRevisionRepository.findOne(itemId);
        if (revision == null) {
            throw new ResourceNotFoundException();
        }
        PLMItemFile itemFile = itemFileRepository.findOne(fileId);
        if (itemFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(itemId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    @Transactional
    public PLMItemFile copyItemFile(PLMItemRevision oldItem, PLMItemRevision newItem, PLMItemFile itemFile) {
        PLMItemFile newItemFile = null;
        File file = getItemFile(oldItem.getId(), itemFile.getId());
        if (file != null) {
            newItemFile = new PLMItemFile();
            Login login = sessionWrapper.getSession().getLogin();
            newItemFile.setName(itemFile.getName());
            newItemFile.setFileNo(itemFile.getFileNo());
            newItemFile.setFileType(itemFile.getFileType());
            newItemFile.setType(itemFile.getType());
            newItemFile.setCreatedBy(login.getPerson().getId());
            newItemFile.setModifiedBy(login.getPerson().getId());
            newItemFile.setItem(newItem);
            newItemFile.setVersion(itemFile.getVersion());
            newItemFile.setSize(itemFile.getSize());
            newItemFile.setLatest(itemFile.getLatest());
            newItemFile = itemFileRepository.save(newItemFile);
            if (newItemFile.getFileType().equals("FOLDER")) {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(newItem.getId(), newItemFile.getId());
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
            } else {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(newItem.getId(), newItemFile.getId());
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newItemFile.getId();
                saveFileToDisk(file, dir);
                saveOldVersionItemFiles(oldItem, newItem, itemFile);
            }
        }
        saveItemFileChildren(oldItem, newItem, itemFile, newItemFile);
        newItem.setHasFiles(true);
        newItem = itemRevisionRepository.save(newItem);
        return newItemFile;
    }

    private void saveOldVersionItemFiles(PLMItemRevision oldItem, PLMItemRevision newItem, PLMItemFile itemFile) {
        List<PLMItemFile> oldVersionFiles = itemFileRepository.findByItemAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(oldItem, itemFile.getFileNo());
        for (PLMItemFile oldVersionFile : oldVersionFiles) {
            PLMItemFile newItemFile = null;
            File file = getItemFile(oldItem.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new PLMItemFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setType(oldVersionFile.getType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setItem(newItem);
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile = itemFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(newItem.getId(), newItemFile.getId());
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newItemFile.getId();
                saveFileToDisk(file, dir);
            }
        }
    }

    private void saveChildrenOldVersionItemFiles(PLMItemRevision oldItem, PLMItemRevision newItem, PLMItemFile itemFile, PLMItemFile plmItemFile) {
        List<PLMItemFile> oldVersionFiles = itemFileRepository.findByItemAndFileNoAndLatestFalseOrderByCreatedDateDesc(oldItem, itemFile.getFileNo());
        for (PLMItemFile oldVersionFile : oldVersionFiles) {
            PLMItemFile newItemFile = null;
            File file = getItemFile(oldItem.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new PLMItemFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setType(oldVersionFile.getType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setItem(newItem);
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile.setParent(plmItemFile.getId());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile = itemFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(newItem.getId(), newItemFile.getId());
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newItemFile.getId();
                saveFileToDisk(file, dir);
            }
        }
    }

    @Transactional
    private void saveItemFileChildren(PLMItemRevision oldItem, PLMItemRevision newItem, PLMItemFile itemFile, PLMItemFile plmItemFile) {
        List<PLMItemFile> childrenFiles = itemFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(itemFile.getId());
        for (PLMItemFile childrenFile : childrenFiles) {
            PLMItemFile newItemFile = null;
            File file = getItemFile(oldItem.getId(), childrenFile.getId());
            if (file != null) {
                newItemFile = new PLMItemFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(childrenFile.getName());
                newItemFile.setFileNo(childrenFile.getFileNo());
                newItemFile.setFileType(childrenFile.getFileType());
                newItemFile.setType(childrenFile.getType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setItem(newItem);
                newItemFile.setVersion(childrenFile.getVersion());
                newItemFile.setSize(childrenFile.getSize());
                newItemFile.setLatest(childrenFile.getLatest());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile.setParent(plmItemFile.getId());
                newItemFile = itemFileRepository.save(newItemFile);
                if (newItemFile.getFileType().equals("FOLDER")) {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getParentFileSystemPath(newItem.getId(), newItemFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                } else {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getReplaceFileSystemPath(newItem.getId(), newItemFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = dir + File.separator + newItemFile.getId();
                    saveFileToDisk(file, dir);
                    saveChildrenOldVersionItemFiles(oldItem, newItem, childrenFile, plmItemFile);
                }
            }
            saveItemFileChildren(oldItem, newItem, childrenFile, newItemFile);
        }

    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer itemId, Integer fileId) {
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        PLMItemFile plmItemFile = itemFileRepository.findOne(fileId);
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());

        /* App events */
        applicationEventPublisher.publishEvent(new ItemEvents.ItemFileDownloadedEvent(plmItemRevision, plmItemFile));

        return plmFileDownloadHistory;
    }

    public List<PLMFileDownloadHistory> getDownloadHistory(Integer fileId) {
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateAsc(fileId);
        return fileDownloadHistories;
    }

    public List<Comment> getAllFileComments(Integer itemId, Integer fileId, ObjectType objectType) {
        List<Comment> comments = new ArrayList<>();
        List<Comment> fileComments = commentRepository.findByObjectIdAndObjectType(fileId, objectType);
        PLMItemFile plmItemFile = itemFileRepository.findOne(fileId);
        if (fileComments.size() > 0) {
            comments.addAll(fileComments);
        }
        List<PLMItemFile> files = itemFileRepository.findByItemAndNameAndLatestFalseOrderByCreatedDateDesc(plmItemFile.getItem(), plmItemFile.getName());
        if (files.size() > 0) {
            for (PLMItemFile file : files) {
                List<Comment> commentList = commentRepository.findByObjectIdAndObjectType(file.getId(), objectType);
                if (commentList.size() > 0) {
                    comments.addAll(commentList);
                }
            }
        }
        return comments;
    }

    public List<PLMItemFile> getAllFileVersionComments(Integer itemId, Integer fileId, ObjectType objectType) {
        List<PLMItemFile> itemFiles = new ArrayList<>();
        PLMItemFile plmItemFile = itemFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(plmItemFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(plmItemFile.getId());
        if (comments.size() > 0) {
            plmItemFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            plmItemFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(plmItemFile);
        List<PLMItemFile> files = itemFileRepository.findByItemAndFileNoAndLatestFalseOrderByCreatedDateDesc(plmItemFile.getItem(), plmItemFile.getFileNo());
        if (files.size() > 0) {
            for (PLMItemFile file : files) {
                List<Comment> oldVersionComments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType);
                if (oldVersionComments.size() > 0) {
                    file.setComments(oldVersionComments);
                }
                List<PLMFileDownloadHistory> oldFileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId());
                if (oldFileDownloadHistories.size() > 0) {
                    file.setDownloadHistories(oldFileDownloadHistories);
                }
                itemFiles.add(file);
            }
        }
        return itemFiles;
    }

    public File getObjectFileOnDisk(Integer objectId, Integer fileId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + objectId;
        File fDir = new File(dir);
        if (fDir.exists()) {
            File file = new File(fDir, "" + fileId);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    public void generateZipFile(Integer itemId, HttpServletResponse response) throws FileNotFoundException, IOException {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        List<PLMItemFile> itemFiles = itemFileRepository.findByItemAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(itemRevision);
        ArrayList<String> fileList = new ArrayList<>();
        itemFiles.forEach(plmItemFile -> {
            File file = getItemFile(itemId, plmItemFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        /*List<PLMObjectDocument> objectDocuments = objectDocumentRepository.findByObjectAndFolderIsNull(itemId);
        objectDocuments.forEach(plmObjectDocument -> {
            File file = objectFileService.getFile(itemId, plmObjectDocument.getDocument().getId(), PLMObjectType.DOCUMENT);
            fileList.add(file.getAbsolutePath());
        });*/
        String zipName = item.getItemNumber() + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "ITEM",itemId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    public PLMItemFile createFolder(Integer itemId, PLMItemFile pdmItemFile) {
        pdmItemFile.setId(null);
        String folderNumber = null;
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItemFile existFolderName = null;
        if (pdmItemFile.getParentFile() != null) {
            existFolderName = itemFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndItemAndLatestTrue(pdmItemFile.getName(), pdmItemFile.getParentFile(), itemRevision);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(pdmItemFile.getParentFile());
                throw new CassiniException(" [ " + pdmItemFile.getName() + " ] Folder already exist in [ " + file.getName() + " ] Folder");
            }
        } else {
            existFolderName = itemFileRepository.findByNameEqualsIgnoreCaseAndItemAndLatestTrue(pdmItemFile.getName(), itemRevision);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", pdmItemFile.getName());
                throw new CassiniException(result);
            }
        }

        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        pdmItemFile.setItem(itemRevision);
        pdmItemFile.setFileNo(folderNumber);
        pdmItemFile.setFileType("FOLDER");
        pdmItemFile.setType("FOLDER");
        pdmItemFile = itemFileRepository.save(pdmItemFile);
        if (pdmItemFile.getParentFile() != null) {
            PLMItemFile parent = itemFileRepository.findOne(pdmItemFile.getParentFile());
            parent.setModifiedDate(pdmItemFile.getModifiedDate());
            parent = itemFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(itemId, pdmItemFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new ItemEvents.ItemFoldersAddedEvent(itemRevision, pdmItemFile));
        return pdmItemFile;
    }

    private String getParentFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        PLMItemFile plmItemFile = itemFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (plmItemFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, plmItemFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + plmItemFile.getId();
        }
        return path;
    }

    private String getReplaceFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        PLMItemFile plmItemFile = itemFileRepository.findOne(fileId);
        if (plmItemFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, plmItemFile.getParentFile(), path);
        } else {
            path = File.separator + itemId;
        }
        return path;
    }

    public List<PLMItemFile> getFolderChidren(Integer itemId, Integer folderId) {
        List<PLMItemFile> itemFiles = itemFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(folderId);
        itemFiles.forEach(plmItemFile -> {
            plmItemFile.setParentObject(PLMObjectType.ITEM);
            if (plmItemFile.getFileType().equals("FOLDER")) {
                plmItemFile.setCount(itemFileRepository.getChildrenCountByParentFileAndLatestTrue(plmItemFile.getId()));
                plmItemFile.setCount(plmItemFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(plmItemFile.getItem().getId(), plmItemFile.getId()));
            }
        });
        return itemFiles;
    }

    public void deleteFolder(Integer itemId, Integer filesId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(itemId, filesId);
        List<PLMItemFile> itemFiles = itemFileRepository.findByParentFileOrderByCreatedDateDesc(filesId);
        utilService.removeFileIfExist((List) itemFiles, dir);
        File fDir = new File(dir);
        Utils.removeDirIfExist(fDir);
        PLMFile file = itemFileRepository.findOne(filesId);
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        applicationEventPublisher.publishEvent(new ItemEvents.ItemFoldersDeletedEvent(itemRevision, file));
        itemFileRepository.delete(filesId);
        if (file.getParentFile() != null) {
            PLMItemFile parent = itemFileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = itemFileRepository.save(parent);
        }
    }

    @Transactional
    public PLMItemFile getLatestUploadedFile(Integer itemId, Integer fileId) {
        PLMItemFile plmItemFile = itemFileRepository.findOne(fileId);
        PLMItemFile itemFile = itemFileRepository.findByItemAndFileNoAndLatestTrue(plmItemFile.getItem(), plmItemFile.getFileNo());
        return itemFile;
    }

    @Transactional
    public List<PLMItemFile> pasteFromClipboard(Integer itemId, Integer fileId, List<PLMFile> files) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        List<PLMItemFile> fileList = new ArrayList<>();
        List<Integer> folderIds = new ArrayList<>();
        files.forEach(file -> {
            if (file.getFileType().equals("FOLDER")) {
                folderIds.add(file.getId());
            }
        });
        for (PLMFile file : files) {
            Boolean canCreate = true;
            if (file.getParentFile() != null && folderIds.indexOf(file.getParentFile()) != -1) {
                canCreate = false;
            }
            if (file.getFileType().equals("FILE") && canCreate) {
                PLMItemFile itemFile = new PLMItemFile();
                PLMItemFile existFile = null;
                if (fileId != 0) {
                    itemFile.setParentFile(fileId);
                    itemFile.setParent(fileId);
                    existFile = itemFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndItemAndLatestTrue(file.getName(), fileId, itemRevision);
                } else {
                    existFile = itemFileRepository.findByItemAndNameAndParentFileIsNullAndLatestTrue(itemRevision, file.getName());
                }
                if (existFile == null) {
                    itemFile.setName(file.getName());
                    itemFile.setDescription(file.getDescription());
                    itemFile.setItem(itemRevision);
                    itemFile.setVersion(1);
                    itemFile.setSize(file.getSize());
                    itemFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    itemFile.setFileNo(autoNumber1);
                    itemFile.setFileType("FILE");
                    itemFile.setType("FILE");
                    itemFile = itemFileRepository.save(itemFile);
                    itemFile.setOldVersion(itemFile.getVersion());
                    itemFile.setParentObject(PLMObjectType.ITEM);
                    fileList.add(itemFile);
                    itemRevision.setHasFiles(true);
                    itemRevision = itemRevisionRepository.save(itemRevision);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + itemId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (itemFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(itemFile.getItem().getId(), itemFile.getId());
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + itemFile.getItem().getId() + File.separator + itemFile.getId();
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
            } else if (file.getFileType().equals("FOLDER") && canCreate) {
                PLMItemFile itemFile = new PLMItemFile();
                PLMItemFile existFile = null;
                if (fileId != 0) {
                    itemFile.setParentFile(fileId);
                    existFile = itemFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndItemAndLatestTrue(file.getName(), fileId, itemRevision);
                } else {
                    existFile = itemFileRepository.findByItemAndNameAndParentFileIsNullAndLatestTrue(itemRevision, file.getName());
                }
                if (existFile == null) {
                    itemFile.setName(file.getName());
                    itemFile.setDescription(file.getDescription());
                    String folderNumber = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    itemFile.setVersion(1);
                    itemFile.setSize(0L);
                    itemFile.setItem(itemRevision);
                    itemFile.setFileNo(folderNumber);
                    itemFile.setFileType("FOLDER");
                    itemFile = itemFileRepository.save(itemFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + itemRevision.getId();
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(itemRevision.getId(), itemFile.getId());
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(itemFile);
                    copyFolderFiles(itemRevision, file.getParentObject(), file.getId(), itemFile.getId());
                }
            }
        }

        return fileList;
    }


    private void copyFolderFiles(PLMItemRevision itemRevision, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> projectFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        projectFiles.forEach(plmProjectFile -> {
            PLMItemFile itemFile = new PLMItemFile();
            itemFile.setParentFile(parent);
            itemFile.setName(plmProjectFile.getName());
            itemFile.setDescription(plmProjectFile.getDescription());
            itemFile.setItem(itemRevision);
            String folderNumber = null;
            if (plmProjectFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                itemFile.setVersion(1);
                itemFile.setFileNo(folderNumber);
                itemFile.setSize(plmProjectFile.getSize());
                itemFile.setFileType("FILE");
                itemFile = itemFileRepository.save(itemFile);
                itemFile.setParentObject(PLMObjectType.ITEM);
                plmProjectFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmProjectFile);

                String dir = "";
                if (itemFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(itemRevision.getId(), itemFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + itemRevision.getId() + File.separator + itemFile.getId();
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
                itemFile.setVersion(1);
                itemFile.setSize(0L);
                itemFile.setFileNo(folderNumber);
                itemFile.setFileType("FOLDER");
                itemFile = itemFileRepository.save(itemFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + itemRevision.getId();
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(itemRevision.getId(), itemFile.getId());
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(itemRevision, objectType, plmProjectFile.getId(), itemFile.getId());
            }
        });
    }


    public String getCopyFilePath(PLMFile file) {
        String path = "";
        if (file.getParentObject().equals(PLMObjectType.ITEM)) {
            PLMItemFile itemFile = itemFileRepository.findOne(file.getId());
            if (itemFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(itemFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + itemFile.getItem().getId() + File.separator + itemFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.CHANGE)) {
            PLMChangeFile changeFile = changeFileRepository.findOne(file.getId());
            if (changeFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(changeFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + changeFile.getChange() + File.separator + changeFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.PROJECT)) {
            PLMProjectFile projectFile = projectFileRepository.findOne(file.getId());
            if (projectFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(projectFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + projectFile.getProject() + File.separator + projectFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.PROJECTACTIVITY)) {
            PLMActivityFile activityFile = activityFileRepository.findOne(file.getId());
            if (activityFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(activityFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + activityFile.getActivity() + File.separator + activityFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.PROJECTTASK)) {
            PLMTaskFile taskFile = taskFileRepository.findOne(file.getId());
            if (taskFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(taskFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + taskFile.getTask() + File.separator + taskFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.MANUFACTURER)) {
            PLMManufacturerFile manufacturerFile = manufacturerFileRepository.findOne(file.getId());
            if (manufacturerFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(manufacturerFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + manufacturerFile.getManufacturer() + File.separator + manufacturerFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.MANUFACTURERPART)) {
            PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileRepository.findOne(file.getId());
            if (manufacturerPartFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(manufacturerPartFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + manufacturerPartFile.getManufacturerPart() + File.separator + manufacturerPartFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.GLOSSARY)) {
            PLMGlossaryFile glossaryFile = glossaryFileRepository.findOne(file.getId());
            if (glossaryFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(glossaryFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + glossaryFile.getGlossary() + File.separator + glossaryFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.SPECIFICATION)) {
            RmObjectFile rmObjectFile = rmObjectFileRepository.findOne(file.getId());
            if (rmObjectFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(rmObjectFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + rmObjectFile.getObject() + File.separator + rmObjectFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.INSPECTIONPLAN)) {
            PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(file.getId());
            if (inspectionPlanFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(inspectionPlanFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + inspectionPlanFile.getInspectionPlan() + File.separator + inspectionPlanFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.INSPECTION)) {
            PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(file.getId());
            if (inspectionFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(inspectionFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + inspectionFile.getInspection() + File.separator + inspectionFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.PROBLEMREPORT)) {
            PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(file.getId());
            if (problemReportFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(problemReportFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + problemReportFile.getProblemReport() + File.separator + problemReportFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.NCR)) {
            PQMNCRFile pqmncrFile = ncrFileRepository.findOne(file.getId());
            if (pqmncrFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(pqmncrFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + pqmncrFile.getNcr() + File.separator + pqmncrFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.QCR)) {
            PQMQCRFile pqmqcrFile = qcrFileRepository.findOne(file.getId());
            if (pqmqcrFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(pqmqcrFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + pqmqcrFile.getQcr() + File.separator + pqmqcrFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.MESOBJECT)) {
            MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(file.getId());
            if (mesObjectFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(mesObjectFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + mesObjectFile.getObject() + File.separator + mesObjectFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.DOCUMENT)) {
            PLMDocument plmDocument = plmDocumentRepository.findOne(file.getId());
            if (plmDocument.getParentFile() != null) {
                path = visitCopyFileParentFolder(plmDocument.getId(), path, file.getParentObject());
            } else {
                path = File.separator + plmDocument.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.MROOBJECT)) {
            MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(file.getId());
            if (mroObjectFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(mroObjectFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + mroObjectFile.getObject() + File.separator + mroObjectFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.PGCOBJECT)) {
            PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(file.getId());
            if (pgcObjectFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(pgcObjectFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + pgcObjectFile.getObject() + File.separator + pgcObjectFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.SUPPLIERAUDIT)) {
            PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(file.getId());
            if (supplierAuditFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(supplierAuditFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + supplierAuditFile.getSupplierAudit() + File.separator + supplierAuditFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.PPAPCHECKLIST)) {
            PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(file.getId());
            if (pqmppapChecklist.getParentFile() != null) {
                path = visitCopyFileParentFolder(pqmppapChecklist.getId(), path, file.getParentObject());
            } else {
                path = File.separator + pqmppapChecklist.getPpap() + File.separator + pqmppapChecklist.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(file.getId());
            if (mfrPartInspectionReport.getParentFile() != null) {
                path = visitCopyFileParentFolder(mfrPartInspectionReport.getId(), path, file.getParentObject());
            } else {
                path = File.separator + mfrPartInspectionReport.getManufacturerPart() + File.separator + mfrPartInspectionReport.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.MBOMREVISION)) {
            MESMBOMFile mesmbomFile = mbomFileRepository.findOne(file.getId());
            if (mesmbomFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(mesmbomFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + mesmbomFile.getMbomRevision() + File.separator + mesmbomFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.BOPREVISION)) {
            MESBOPFile mesbopFile = bopFileRepository.findOne(file.getId());
            if (mesbopFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(mesbopFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + mesbopFile.getBop() + File.separator + mesbopFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.BOPROUTEOPERATION)) {
            MESBOPOperationFile mesbopOperationFile = bopOperationFileRepository.findOne(file.getId());
            if (mesbopOperationFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(mesbopOperationFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + mesbopOperationFile.getBopOperation() + File.separator + mesbopOperationFile.getId();
            }
        } else if (file.getParentObject().equals(PLMObjectType.PROGRAM)) {
            PLMProgramFile programFile = programFileRepository.findOne(file.getId());
            if (programFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(programFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + programFile.getProgram() + File.separator + programFile.getId();
            }
        }
        return path;
    }

    private String visitCopyFileParentFolder(Integer fileId, String path, PLMObjectType type) {
        if (type.equals(PLMObjectType.ITEM)) {
            PLMItemFile plmItemFile = itemFileRepository.findOne(fileId);
            if (plmItemFile.getParentFile() != null) {
                path = File.separator + plmItemFile.getId() + path;
                path = visitCopyFileParentFolder(plmItemFile.getParentFile(), path, type);
            } else {
                path = File.separator + plmItemFile.getItem().getId() + File.separator + plmItemFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.PROJECT)) {
            PLMProjectFile plmProjectFile = projectFileRepository.findOne(fileId);
            if (plmProjectFile.getParentFile() != null) {
                path = File.separator + plmProjectFile.getId() + path;
                path = visitCopyFileParentFolder(plmProjectFile.getParentFile(), path, type);
            } else {
                path = File.separator + plmProjectFile.getProject() + File.separator + plmProjectFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.PROJECTACTIVITY)) {
            PLMActivityFile plmActivityFile = activityFileRepository.findOne(fileId);
            if (plmActivityFile.getParentFile() != null) {
                path = File.separator + plmActivityFile.getId() + path;
                path = visitCopyFileParentFolder(plmActivityFile.getParentFile(), path, type);
            } else {
                path = File.separator + plmActivityFile.getActivity() + File.separator + plmActivityFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.PROJECTTASK)) {
            PLMTaskFile plmTaskFile = taskFileRepository.findOne(fileId);
            if (plmTaskFile.getParentFile() != null) {
                path = File.separator + plmTaskFile.getId() + path;
                path = visitCopyFileParentFolder(plmTaskFile.getParentFile(), path, type);
            } else {
                path = File.separator + plmTaskFile.getTask() + File.separator + plmTaskFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.CHANGE)) {
            PLMChangeFile plmChangeFile = changeFileRepository.findOne(fileId);
            if (plmChangeFile.getParentFile() != null) {
                path = File.separator + plmChangeFile.getId() + path;
                path = visitCopyFileParentFolder(plmChangeFile.getParentFile(), path, type);
            } else {
                path = File.separator + plmChangeFile.getChange() + File.separator + plmChangeFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.MANUFACTURER)) {
            PLMManufacturerFile plmManufacturerFile = manufacturerFileRepository.findOne(fileId);
            if (plmManufacturerFile.getParentFile() != null) {
                path = File.separator + plmManufacturerFile.getId() + path;
                path = visitCopyFileParentFolder(plmManufacturerFile.getParentFile(), path, type);
            } else {
                path = File.separator + plmManufacturerFile.getManufacturer() + File.separator + plmManufacturerFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.MANUFACTURERPART)) {
            PLMManufacturerPartFile plmManufacturerPartFile = manufacturerPartFileRepository.findOne(fileId);
            if (plmManufacturerPartFile.getParentFile() != null) {
                path = File.separator + plmManufacturerPartFile.getId() + path;
                path = visitCopyFileParentFolder(plmManufacturerPartFile.getParentFile(), path, type);
            } else {
                path = File.separator + plmManufacturerPartFile.getManufacturerPart() + File.separator + plmManufacturerPartFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.SPECIFICATION)) {
            RmObjectFile rmObjectFile = rmObjectFileRepository.findOne(fileId);
            if (rmObjectFile.getParentFile() != null) {
                path = File.separator + rmObjectFile.getId() + path;
                path = visitCopyFileParentFolder(rmObjectFile.getParentFile(), path, type);
            } else {
                path = File.separator + rmObjectFile.getObject() + File.separator + rmObjectFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.GLOSSARY)) {
            PLMGlossaryFile plmGlossaryFile = glossaryFileRepository.findOne(fileId);
            if (plmGlossaryFile.getParentFile() != null) {
                path = File.separator + plmGlossaryFile.getId() + path;
                path = visitCopyFileParentFolder(plmGlossaryFile.getParentFile(), path, type);
            } else {
                path = File.separator + plmGlossaryFile.getGlossary() + File.separator + plmGlossaryFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.INSPECTIONPLAN)) {
            PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(fileId);
            if (inspectionPlanFile.getParentFile() != null) {
                path = File.separator + inspectionPlanFile.getId() + path;
                path = visitCopyFileParentFolder(inspectionPlanFile.getParentFile(), path, type);
            } else {
                path = File.separator + inspectionPlanFile.getInspectionPlan() + File.separator + inspectionPlanFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.INSPECTION)) {
            PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(fileId);
            if (inspectionFile.getParentFile() != null) {
                path = File.separator + inspectionFile.getId() + path;
                path = visitCopyFileParentFolder(inspectionFile.getParentFile(), path, type);
            } else {
                path = File.separator + inspectionFile.getInspection() + File.separator + inspectionFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.PROBLEMREPORT)) {
            PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(fileId);
            if (problemReportFile.getParentFile() != null) {
                path = File.separator + problemReportFile.getId() + path;
                path = visitCopyFileParentFolder(problemReportFile.getParentFile(), path, type);
            } else {
                path = File.separator + problemReportFile.getProblemReport() + File.separator + problemReportFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.NCR)) {
            PQMNCRFile pqmncrFile = ncrFileRepository.findOne(fileId);
            if (pqmncrFile.getParentFile() != null) {
                path = File.separator + pqmncrFile.getId() + path;
                path = visitCopyFileParentFolder(pqmncrFile.getParentFile(), path, type);
            } else {
                path = File.separator + pqmncrFile.getNcr() + File.separator + pqmncrFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.QCR)) {
            PQMQCRFile pqmqcrFile = qcrFileRepository.findOne(fileId);
            if (pqmqcrFile.getParentFile() != null) {
                path = File.separator + pqmqcrFile.getId() + path;
                path = visitCopyFileParentFolder(pqmqcrFile.getParentFile(), path, type);
            } else {
                path = File.separator + pqmqcrFile.getQcr() + File.separator + pqmqcrFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.MESOBJECT)) {
            MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(fileId);
            if (mesObjectFile.getParentFile() != null) {
                path = File.separator + mesObjectFile.getId() + path;
                path = visitCopyFileParentFolder(mesObjectFile.getParentFile(), path, type);
            } else {
                path = File.separator + mesObjectFile.getObject() + File.separator + mesObjectFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.DOCUMENT)) {
            PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
            if (plmDocument.getParentFile() != null) {
                path = File.separator + plmDocument.getId() + path;
                path = visitCopyFileParentFolder(plmDocument.getParentFile(), path, type);
            } else {
                path = File.separator + plmDocument.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.MROOBJECT)) {
            MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(fileId);
            if (mroObjectFile.getParentFile() != null) {
                path = File.separator + mroObjectFile.getId() + path;
                path = visitCopyFileParentFolder(mroObjectFile.getParentFile(), path, type);
            } else {
                path = File.separator + mroObjectFile.getObject() + File.separator + mroObjectFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.PGCOBJECT)) {
            PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(fileId);
            if (pgcObjectFile.getParentFile() != null) {
                path = File.separator + pgcObjectFile.getId() + path;
                path = visitCopyFileParentFolder(pgcObjectFile.getParentFile(), path, type);
            } else {
                path = File.separator + pgcObjectFile.getObject() + File.separator + pgcObjectFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.SUPPLIERAUDIT)) {
            PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(fileId);
            if (supplierAuditFile.getParentFile() != null) {
                path = File.separator + supplierAuditFile.getId() + path;
                path = visitCopyFileParentFolder(supplierAuditFile.getParentFile(), path, type);
            } else {
                path = File.separator + supplierAuditFile.getSupplierAudit() + File.separator + supplierAuditFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.PPAPCHECKLIST)) {
            PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(fileId);
            if (pqmppapChecklist.getParentFile() != null) {
                path = File.separator + pqmppapChecklist.getId() + path;
                path = visitCopyFileParentFolder(pqmppapChecklist.getParentFile(), path, type);
            } else {
                path = File.separator + pqmppapChecklist.getPpap() + File.separator + pqmppapChecklist.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) {
            PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(fileId);
            if (mfrPartInspectionReport.getParentFile() != null) {
                path = File.separator + mfrPartInspectionReport.getId() + path;
                path = visitCopyFileParentFolder(mfrPartInspectionReport.getParentFile(), path, type);
            } else {
                path = File.separator + mfrPartInspectionReport.getManufacturerPart() + File.separator + mfrPartInspectionReport.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.MBOMREVISION)) {
            MESMBOMFile mesmbomFile = mbomFileRepository.findOne(fileId);
            if (mesmbomFile.getParentFile() != null) {
                path = File.separator + mesmbomFile.getId() + path;
                path = visitCopyFileParentFolder(mesmbomFile.getParentFile(), path, type);
            } else {
                path = File.separator + mesmbomFile.getMbomRevision() + File.separator + mesmbomFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.BOPREVISION)) {
            MESBOPFile mesbopFile = bopFileRepository.findOne(fileId);
            if (mesbopFile.getParentFile() != null) {
                path = File.separator + mesbopFile.getId() + path;
                path = visitCopyFileParentFolder(mesbopFile.getParentFile(), path, type);
            } else {
                path = File.separator + mesbopFile.getBop() + File.separator + mesbopFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.BOPROUTEOPERATION)) {
            MESBOPOperationFile mesbopOperationFile = bopOperationFileRepository.findOne(fileId);
            if (mesbopOperationFile.getParentFile() != null) {
                path = File.separator + mesbopOperationFile.getId() + path;
                path = visitCopyFileParentFolder(mesbopOperationFile.getParentFile(), path, type);
            } else {
                path = File.separator + mesbopOperationFile.getBopOperation() + File.separator + mesbopOperationFile.getId() + path;
                return path;
            }
        } else if (type.equals(PLMObjectType.PROGRAM)) {
            PLMProgramFile programFile = programFileRepository.findOne(fileId);
            if (programFile.getParentFile() != null) {
                path = File.separator + programFile.getId() + path;
                path = visitCopyFileParentFolder(programFile.getParentFile(), path, type);
            } else {
                path = File.separator + programFile.getProgram() + File.separator + programFile.getId() + path;
                return path;
            }
        }

        return path;
    }

    public void undoCopiedItemFiles(Integer itemId, List<PLMItemFile> itemFiles) {
        itemFiles.forEach(itemFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(itemId, itemFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(itemFile.getId(), dir);
            itemFileRepository.delete(itemFile.getId());
        });
    }

    public PLMFile convertForgeFile(Integer id, PLMFile file1) {

        String dir = "";
        String path = "";
        if (file1 != null) {
            if (file1.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(id, file1.getParentFile());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
            }
            path = dir + File.separator + file1.getId();
        }
        if (file1 != null && file1.getName() != null) {
            Map<String, String> map = forgeService.uploadForgeFile(file1.getName(), path);
            if (map != null) {
                file1 = fileRepository.findOne(file1.getId());
                file1.setUrn(map.get("urn"));
                file1.setThumbnail(map.get("thumbnail"));
                file1 = fileRepository.save(file1);
                return file1;
            }
        }

        return file1;
    }

    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'download','file')")
    public void downloadFile(Integer id, Integer fileId, HttpServletResponse response) {
        PLMItemFile itemFile = itemFileService.get(fileId);
        File file = itemFileService.getItemFile(id, fileId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, '"' + itemFile.getName() + '"', file);
        }
    }

    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'preview','file')")
    public void previewFile(Integer id, Integer fileId, HttpServletResponse response) throws Exception {
        PLMItemFile itemFile = itemFileService.get(fileId);
        String fileName = itemFile.getName();
        File file = itemFileService.getItemFile(id, fileId);
        if (file != null) {
            try {
                String e = URLDecoder.decode(fileName, "UTF-8");
                response.setHeader("Content-disposition", "inline; filename=" + e);
            } catch (UnsupportedEncodingException var6) {
                response.setHeader("Content-disposition", "inline; filename=" + fileName);
            }
            ServletOutputStream e1 = response.getOutputStream();
            org.apache.commons.io.IOUtils.copy(new FileInputStream(file), e1);
            e1.flush();
        }
    }


}