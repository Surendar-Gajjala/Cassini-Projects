package com.cassinisys.plm.service.mfr;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.SupplierEvents;
import com.cassinisys.plm.model.mfr.PLMSupplier;
import com.cassinisys.plm.model.mfr.PLMSupplierFile;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mfr.SupplierFileRepository;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.plm.FileDownloadHistoryRepository;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.activitystream.SupplierActivityStream;
import com.cassinisys.plm.service.plm.FileHelpers;
import com.cassinisys.plm.service.plm.ItemFileService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Suresh Cassini on 25-11-2020.
 */
@Service
@Transactional
public class SupplierFileService
        implements CrudService<PLMSupplierFile, Integer>, PageableService<PLMSupplierFile, Integer> {

    @Autowired
    private SupplierFileRepository supplierFileRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private FileDownloadHistoryRepository fileDownloadHistoryRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private FileHelpers fileHelpers;
    @Autowired
    private ItemFileService itemFileService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ObjectFileService qualityFileService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierActivityStream supplierActivityStream;

    @Override
    public PLMSupplierFile create(PLMSupplierFile supplierFile) {
        checkNotNull(supplierFile);
        supplierFile.setId(null);
        supplierFile = supplierFileRepository.save(supplierFile);
        PLMSupplier supplier = supplierRepository.findOne(supplierFile.getSupplier());
        return supplierFile;
    }

    @Override
    public PLMSupplierFile update(PLMSupplierFile supplierFile) {
        PLMFile file = fileRepository.findOne(supplierFile.getId());
        PLMSupplier mfr = supplierRepository.findOne(supplierFile.getId());
        PLMSupplierFile supplierFile1 = supplierFileRepository.findOne(supplierFile.getId());
        if (!file.getLocked().equals(supplierFile.getLocked())) {
            /* App events */
            if (supplierFile.getLocked()) {
                applicationEventPublisher
                        .publishEvent(new SupplierEvents.SupplierFileLockedEvent(mfr.getId(), supplierFile1));
            } else {
                applicationEventPublisher
                        .publishEvent(new SupplierEvents.SupplierFileUnlockedEvent(mfr.getId(), supplierFile1));
            }
        }
        supplierFile1.setDescription(supplierFile.getDescription());
        supplierFile1.setLocked(supplierFile.getLocked());
        supplierFile1.setLockedBy(supplierFile.getLockedBy());
        supplierFile1.setLockedDate(supplierFile.getLockedDate());
        supplierFile = supplierFileRepository.save(supplierFile1);
        return supplierFile;
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        PLMSupplierFile supplierFile = supplierFileRepository.findOne(id);
        if (supplierFile == null) {
            throw new ResourceNotFoundException();
        }
        supplierFileRepository.delete(id);
        PLMSupplier mfr = supplierRepository.findOne(supplierFile.getSupplier());
    }

    public PLMSupplierFile updateFileName(Integer id, String newFileName) throws IOException {
        PLMSupplierFile file1 = supplierFileRepository.findOne(id);
        PLMSupplierFile oldFile = (PLMSupplierFile) Utils.cloneObject(file1, PLMSupplierFile.class);
        String oldName = file1.getName();
        file1.setLatest(false);
        PLMSupplierFile plmSupplierFile = supplierFileRepository.save(file1);
        PLMSupplierFile supplierFile = (PLMSupplierFile) Utils.cloneObject(plmSupplierFile, PLMSupplierFile.class);
        if (supplierFile != null) {
            supplierFile.setId(null);
            supplierFile.setName(newFileName);
            supplierFile.setVersion(file1.getVersion() + 1);
            supplierFile.setLatest(true);
            supplierFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            supplierFile = supplierFileRepository.save(supplierFile);
            if (supplierFile.getParentFile() != null) {
                PLMSupplierFile parent = supplierFileRepository.findOne(supplierFile.getParentFile());
                parent.setModifiedDate(supplierFile.getModifiedDate());
                parent = supplierFileRepository.save(parent);
            }
            qualityFileService.copyFileAttributes(file1.getId(), supplierFile.getId());
            String dir = "";
            if (supplierFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(supplierFile.getSupplier(), id);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + supplierFile.getSupplier();
            }
            dir = dir + File.separator + supplierFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + supplierFile.getSupplier() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            PLMSupplier supplier = supplierRepository.findOne(supplierFile.getSupplier());
            /* App Events */
            applicationEventPublisher.publishEvent(
                    new SupplierEvents.SupplierFileRenamedEvent(supplier.getId(), oldFile, supplierFile, "Rename"));
            this.supplierService.sendSupplierSubscribeNotification(supplier,
                    oldFile.getName() + "-" + supplierFile.getName(), "fileRename");

        }
        return supplierFile;
    }

    public List<PLMSupplierFile> replaceSupplierFiles(Integer supplierId, Integer fileId,
                                                      Map<String, MultipartFile> fileMap) throws CassiniException, JsonProcessingException {
        List<PLMSupplierFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        PLMSupplierFile supFile = null;
        PLMSupplierFile oldFile = null;
        PLMSupplier supplier = supplierRepository.findOne(supplierId);
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null)
                fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        PLMSupplierFile plmSupplierFile = null;
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
                    PLMSupplierFile supplierFile = null;
                    plmSupplierFile = supplierFileRepository.findOne(fileId);
                    String oldName = "";
                    if (plmSupplierFile != null && plmSupplierFile.getParentFile() != null) {
                        supplierFile = supplierFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        supplierFile = supplierFileRepository
                                .findBySupplierAndNameAndParentFileIsNullAndLatestTrue(supplierId, name);
                    }
                    if (supplierFile != null) {
                        comments = commentRepository.findAllByObjectId(supplierFile.getId());
                    }
                    if (plmSupplierFile != null) {
                        oldName = plmSupplierFile.getName();
                        plmSupplierFile.setLatest(false);
                        plmSupplierFile = supplierFileRepository.save(plmSupplierFile);
                    }
                    if (supplierFile != null) {
                        comments = commentRepository.findAllByObjectId(supplierFile.getId());
                    }
                    supplierFile = new PLMSupplierFile();
                    supplierFile.setName(name);
                    if (plmSupplierFile != null && plmSupplierFile.getParentFile() != null) {
                        supplierFile.setParentFile(plmSupplierFile.getParentFile());
                    }
                    if (plmSupplierFile != null) {
                        supplierFile.setFileNo(plmSupplierFile.getFileNo());
                        supplierFile.setVersion(plmSupplierFile.getVersion() + 1);
                        supplierFile.setReplaceFileName(plmSupplierFile.getName() + " Replaced to " + name);
                        oldFile = JsonUtils.cloneEntity(plmSupplierFile, PLMSupplierFile.class);
                    }
                    supplierFile.setCreatedBy(login.getPerson().getId());
                    supplierFile.setModifiedBy(login.getPerson().getId());
                    supplierFile.setSupplier(supplierId);
                    supplierFile.setSize(file.getSize());
                    supplierFile.setFileType("FILE");
                    supplierFile = supplierFileRepository.save(supplierFile);
                    if (supplierFile.getParentFile() != null) {
                        PLMSupplierFile parent = supplierFileRepository.findOne(supplierFile.getParentFile());
                        parent.setModifiedDate(supplierFile.getModifiedDate());
                        parent = supplierFileRepository.save(parent);
                    }
                    if (plmSupplierFile != null) {
                        qualityFileService.copyFileAttributes(plmSupplierFile.getId(), supplierFile.getId());
                    }
                    String dir = "";
                    if (plmSupplierFile != null && plmSupplierFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(supplierId, fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + supplierId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + supplierFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(supplierFile);
                    /* App Events */
                    applicationEventPublisher.publishEvent(new SupplierEvents.SupplierFileRenamedEvent(supplier.getId(),
                            plmSupplierFile, supplierFile, "Replace"));
                    this.supplierService.sendSupplierSubscribeNotification(supplier,
                            oldFile.getName() + "-" + supplierFile.getName(),
                            "fileReplace");
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    private String getReplaceFileSystemPath(Integer mfrPartId, Integer fileId) {
        String path = "";
        PLMSupplierFile supplierPartFile = supplierFileRepository.findOne(fileId);
        if (supplierPartFile.getParentFile() != null) {
            path = utilService.visitParentFolder(mfrPartId, supplierPartFile.getParentFile(), path);
        } else {
            path = File.separator + mfrPartId;
        }
        return path;
    }

    public void deleteSupplierFile(Integer supplierId, Integer id) throws JsonProcessingException {
        checkNotNull(id);
        PLMSupplierFile supplierFile = supplierFileRepository.findOne(id);
        PLMSupplier supplier = supplierRepository.findOne(supplierId);
        List<PLMSupplierFile> supplierFiles = supplierFileRepository.findBySupplierAndFileNo(supplierId, supplierFile.getFileNo());
        String fNames = null;
        for (PLMSupplierFile supplierFile1 : supplierFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(supplierId, supplierFile1.getId());
            fileSystemService.deleteDocumentFromDiskFolder(supplierFile.getId(), dir);
            if (fNames == null) {
                fNames = supplierFile1.getName();
            } else {
                fNames = fNames + " , " + supplierFile1.getName();
            }
            supplierFileRepository.delete(supplierFile1.getId());
        }
        if (supplierFile.getParentFile() != null) {
            PLMSupplierFile parent = supplierFileRepository.findOne(supplierFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = supplierFileRepository.save(parent);
        }
        applicationEventPublisher
                .publishEvent(new SupplierEvents.SupplierFileDeletedEvent(supplier.getId(), supplierFile));
        this.supplierService.sendSupplierSubscribeNotification(supplier, supplierFile.getName(), "fileDeleted");
    }

    @Override
    public PLMSupplierFile get(Integer id) {
        checkNotNull(id);
        PLMSupplierFile supplierFile = supplierFileRepository.findOne(id);
        if (supplierFile == null) {
            throw new ResourceNotFoundException();
        }
        return supplierFile;
    }

    @Override
    public List<PLMSupplierFile> getAll() {
        return supplierFileRepository.findAll();
    }

    public List<PLMSupplierFile> getAllFileVersions(Integer supplierId, String name) {
        return supplierFileRepository.findAllBySupplierAndNameOrderByCreatedDateDesc(supplierId, name);
    }

    @Override
    public Page<PLMSupplierFile> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null)
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                    new Sort(new Sort.Order("id")));
        return supplierFileRepository.findAll(pageable);
    }

    public List<PLMSupplierFile> findBySupplier(Integer supplier) {
        List<PLMSupplierFile> supplierFiles = supplierFileRepository
                .findBySupplierAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(supplier);
        supplierFiles.forEach(supplierFile -> {
            supplierFile.setParentObject(PLMObjectType.MFRSUPPLIER);
            if (supplierFile.getFileType().equals("FOLDER")) {
                supplierFile.setCount(
                        supplierFileRepository.getChildrenCountByParentFileAndLatestTrue(supplierFile.getId()));
                supplierFile.setCount(supplierFile.getCount() + objectDocumentRepository
                        .getDocumentsCountByObjectIdAndFolder(supplierFile.getSupplier(), supplierFile.getId()));
            }
        });
        return supplierFiles;
    }

    public List<PLMSupplierFile> findBySupplierLatest(Integer supplier) {
        List<PLMSupplierFile> supplierFiles = supplierFileRepository
                .findBySupplierAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(supplier);
        supplierFiles.forEach(supplierFile -> {
            supplierFile.setParentObject(PLMObjectType.MFRSUPPLIER);
            if (supplierFile.getFileType().equals("FOLDER")) {
                supplierFile.setCount(
                        supplierFileRepository.getChildrenCountByParentFileAndLatestTrue(supplierFile.getId()));
            }
        });
        return supplierFiles;
    }

    public List<PLMSupplierFile> uploadFiles(Integer supplierId, Integer folderId, Map<String, MultipartFile> fileMap)
            throws CassiniException, JsonProcessingException {
        List<PLMSupplierFile> uploadedFiles = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        PLMSupplier supplier = supplierRepository.findOne(supplierId);
        String[] fileExtension = null;
        List<PLMSupplierFile> newFiles = new ArrayList<>();
        List<PLMSupplierFile> versionedFiles = new ArrayList<>();
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null)
                fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fNames = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String(file.getOriginalFilename().getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    boolean versioned = false;
                    PLMSupplierFile supFile = null;
                    if (folderId == 0) {
                        supFile = supplierFileRepository
                                .findBySupplierAndNameAndParentFileIsNullAndLatestTrue(supplierId, name);
                    } else {
                        supFile = supplierFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer version = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (supFile != null) {
                        supFile.setLatest(false);
                        Integer oldVersion = supFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = supFile.getFileNo();
                        oldFile = supFile.getId();
                        /* supFile.setVersion(newVersion); */
                        versioned = true;
                        supplierFileRepository.save(supFile);
                    }
                    if (supFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    supFile = new PLMSupplierFile();
                    supFile.setName(name);
                    supFile.setFileNo(autoNumber1);
                    supFile.setCreatedBy(login.getPerson().getId());
                    supFile.setModifiedBy(login.getPerson().getId());
                    /* supFile.setVersion(1); */
                    supFile.setVersion(version);
                    supFile.setSupplier(supplierId);
                    supFile.setSize(file.getSize());
                    supFile.setFileType("FILE");
                    if (folderId != 0) {
                        supFile.setParentFile(folderId);
                    }
                    supFile = supplierFileRepository.save(supFile);
                    if (supFile.getParentFile() != null) {
                        PLMSupplierFile parent = supplierFileRepository.findOne(supFile.getParentFile());
                        parent.setModifiedDate(supFile.getModifiedDate());
                        parent = supplierFileRepository.save(parent);
                    }
                    if (supFile.getVersion() > 1) {
                        qualityFileService.copyFileAttributes(oldFile, supFile.getId());
                    }
                    if (fNames == null) {
                        fNames = supFile.getName();

                    } else {
                        fNames = fNames + "  , " + supFile.getName();
                    }
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + supplierId;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getParentFileSystemPath(supplierId, folderId);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + supFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploadedFiles.add(supFile);
                    if (versioned) {
                        versionedFiles.add(supFile);
                    } else {
                        newFiles.add(supFile);
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /* App Events */
        if (newFiles.size() > 0) {
            applicationEventPublisher
                    .publishEvent(new SupplierEvents.SupplierFilesAddedEvent(supplier.getId(), newFiles));
            this.supplierService.sendSupplierSubscribeNotification(supplier,
                    this.supplierActivityStream.getSupplierFilesAddedJson(newFiles),
                    "fileAdded");
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher
                    .publishEvent(new SupplierEvents.SupplierFilesVersionedEvent(supplier.getId(), versionedFiles));
            this.supplierService.sendSupplierSubscribeNotification(supplier,
                    this.supplierActivityStream.getSupplierFilesAddedJson(versionedFiles),
                    "fileVersioned");
        }
        return uploadedFiles;
    }

    public File getSupplierFile(Integer supplierId, Integer fileId) {
        checkNotNull(supplierId);
        checkNotNull(fileId);
        PLMSupplier mfr = supplierRepository.findOne(supplierId);
        if (mfr == null) {
            throw new ResourceNotFoundException();
        }
        PLMSupplierFile supFile = supplierFileRepository.findOne(fileId);
        if (supFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(supplierId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<PLMSupplierFile> findBySupplierFilesName(Integer supplierId, String name) {
        return supplierFileRepository.findBySupplierAndNameContainingIgnoreCaseAndLatestTrue(supplierId, name);
    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer supplierId, Integer fileId)
            throws JsonProcessingException {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMSupplierFile plmItemFile = supplierFileRepository.findOne(fileId);
        PLMSupplier supplier = supplierRepository.findOne(plmItemFile.getSupplier());
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        applicationEventPublisher
                .publishEvent(new SupplierEvents.SupplierFileDownloadedEvent(supplier.getId(), plmItemFile));
        this.supplierService.sendSupplierSubscribeNotification(supplier, plmItemFile.getName(), "fileDownloaded");
        return plmFileDownloadHistory;
    }

    public List<PLMSupplierFile> getAllFileVersionComments(Integer supplierId, Integer fileId, ObjectType objectType) {
        List<PLMSupplierFile> itemFiles = new ArrayList<>();
        PLMSupplierFile supFile = supplierFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(supFile.getId(),
                objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository
                .findByFileIdOrderByDownloadDateDesc(supFile.getId());
        if (comments.size() > 0) {
            supFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            supFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(supFile);
        List<PLMSupplierFile> files = supplierFileRepository
                .findBySupplierAndFileNoAndLatestFalseOrderByCreatedDateDesc(supFile.getSupplier(),
                        supFile.getFileNo());
        if (files.size() > 0) {
            for (PLMSupplierFile file : files) {
                List<Comment> oldVersionComments = commentRepository
                        .findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType);
                if (oldVersionComments.size() > 0) {
                    file.setComments(oldVersionComments);
                }
                List<PLMFileDownloadHistory> oldFileDownloadHistories = fileDownloadHistoryRepository
                        .findByFileIdOrderByDownloadDateDesc(file.getId());
                if (oldFileDownloadHistories.size() > 0) {
                    file.setDownloadHistories(oldFileDownloadHistories);
                }
                itemFiles.add(file);
            }
        }
        return itemFiles;
    }

    public PLMSupplierFile createSupplierFolder(Integer supplierId, PLMSupplierFile supplierFile) {
        supplierFile.setId(null);
        String folderNumber = null;
        PLMSupplier supplier = supplierRepository.findOne(supplierId);
        PLMSupplierFile existFolderName = null;
        if (supplierFile.getParentFile() != null) {
            existFolderName = supplierFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndSupplierAndLatestTrue(
                    supplierFile.getName(), supplierFile.getParentFile(), supplierId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(supplierFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null,
                        "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", supplierFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = supplierFileRepository
                    .findByNameEqualsIgnoreCaseAndSupplierAndLatestTrue(supplierFile.getName(), supplierId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist",
                        LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", supplierFile.getName());
                throw new CassiniException(result);
            }
        }

        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source",
                "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        supplierFile.setSupplier(supplierId);
        supplierFile.setFileNo(folderNumber);
        supplierFile.setFileType("FOLDER");
        supplierFile = supplierFileRepository.save(supplierFile);
        if (supplierFile.getParentFile() != null) {
            PLMSupplierFile parent = supplierFileRepository.findOne(supplierFile.getParentFile());
            parent.setModifiedDate(supplierFile.getModifiedDate());
            parent = supplierFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(supplierId, supplierFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher
                .publishEvent(new SupplierEvents.SupplierFoldersAddedEvent(supplier.getId(), supplierFile));
        return supplierFile;
    }

    private String getParentFileSystemPath(Integer supplierId, Integer fileId) {
        String path = "";
        PLMSupplierFile supplierFile = supplierFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (supplierFile.getParentFile() != null) {
            path = utilService.visitParentFolder(supplierId, supplierFile.getParentFile(), path);
        } else {
            path = File.separator + supplierId + File.separator + supplierFile.getId();
        }
        return path;
    }

    public PLMFile moveSupplierFileToFolder(Integer id, PLMSupplierFile plmSupplierFile) throws Exception {
        PLMSupplierFile file = supplierFileRepository.findOne(plmSupplierFile.getId());
        PLMSupplierFile existFile = (PLMSupplierFile) Utils.cloneObject(file, PLMSupplierFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getSupplier(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getSupplier() + File.separator + existFile.getId();
        }
        if (plmSupplierFile.getParentFile() != null) {
            PLMSupplierFile existItemFile = supplierFileRepository
                    .findByParentFileAndNameAndLatestTrue(plmSupplierFile.getParentFile(), plmSupplierFile.getName());
            PLMSupplierFile folder = supplierFileRepository.findOne(plmSupplierFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null,
                        "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmSupplierFile = supplierFileRepository.save(plmSupplierFile);
            }
        } else {
            PLMSupplierFile existItemFile = supplierFileRepository
                    .findBySupplierAndNameAndParentFileIsNullAndLatestTrue(plmSupplierFile.getSupplier(),
                            plmSupplierFile.getName());
            PLMSupplier supplier = supplierRepository.findOne(plmSupplierFile.getSupplier());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist",
                        LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                plmSupplierFile = supplierFileRepository.save(plmSupplierFile);
            }
        }
        if (plmSupplierFile != null) {
            String dir = "";
            if (plmSupplierFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator
                        + getParentFileSystemPath(plmSupplierFile.getSupplier(), plmSupplierFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmSupplierFile.getSupplier() + File.separator
                        + plmSupplierFile.getId();
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
            List<PLMSupplierFile> oldVersionFiles = supplierFileRepository
                    .findBySupplierAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getSupplier(),
                            existFile.getFileNo());
            for (PLMSupplierFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator
                            + getParentFileSystemPath(oldVersionFile.getSupplier(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getSupplier() + File.separator
                            + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator
                        + getReplaceFileSystemPath(plmSupplierFile.getSupplier(), plmSupplierFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmSupplierFile.getParentFile());
                oldVersionFile = supplierFileRepository.save(oldVersionFile);
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.createNewFile();
                }
                instream = null;
                outstream = null;
                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                e = new File(oldFileDir);
                System.gc();
                Thread.sleep(1000L);
                FileUtils.deleteQuietly(e);
            }
        }
        return plmSupplierFile;
    }

    public List<PLMSupplierFile> getSupplierFolderChildren(Integer folderId) {
        List<PLMSupplierFile> supplierFiles = supplierFileRepository
                .findByParentFileAndLatestTrueOrderByCreatedDateDesc(folderId);
        supplierFiles.forEach(supplierFile -> {
            supplierFile.setParentObject(PLMObjectType.MFRSUPPLIER);
            if (supplierFile.getFileType().equals("FOLDER")) {
                supplierFile.setCount(
                        supplierFileRepository.getChildrenCountByParentFileAndLatestTrue(supplierFile.getId()));
                supplierFile.setCount(supplierFile.getCount() + objectDocumentRepository
                        .getDocumentsCountByObjectIdAndFolder(supplierFile.getSupplier(), supplierFile.getId()));
            }
        });
        return supplierFiles;
    }

    public void deleteFolder(Integer supplierId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(supplierId, folderId);
        PLMSupplierFile file = supplierFileRepository.findOne(folderId);
        PLMSupplier supplier = supplierRepository.findOne(supplierId);
        List<PLMSupplierFile> supplierFiles = supplierFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) supplierFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        supplierFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new SupplierEvents.SupplierFoldersDeletedEvent(supplier.getId(), file));
    }

    public void generateZipFile(Integer supplierId, HttpServletResponse response)
            throws FileNotFoundException, IOException {
        PLMSupplier plmSupplier = supplierRepository.findOne(supplierId);
        List<PLMSupplierFile> plmSupplierFiles = supplierFileRepository
                .findBySupplierAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(supplierId);
        ArrayList<String> fileList = new ArrayList<>();
        plmSupplierFiles.forEach(plmSupplierFile -> {
            File file = getSupplierFile(supplierId, plmSupplierFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = plmSupplier.getName() + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "SUPPLIER",supplierId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional
    public PLMSupplierFile getLatestUploadedSupplierFile(Integer supplierId, Integer fileId) {
        PLMSupplierFile supplierFile = supplierFileRepository.findOne(fileId);
        PLMSupplierFile plmSupplierFile = supplierFileRepository
                .findBySupplierAndFileNoAndLatestTrue(supplierFile.getSupplier(), supplierFile.getFileNo());
        return plmSupplierFile;
    }

    public PLMFile updateFileDescription(Integer supplierId, Integer id, PLMSupplierFile plmSupplierFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            file.setDescription(plmSupplierFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<PLMSupplierFile> pasteFromClipboard(Integer supplierId, Integer fileId, List<PLMFile> files) {
        List<PLMSupplierFile> fileList = new ArrayList<>();
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
                PLMSupplierFile supplierFile = new PLMSupplierFile();
                PLMSupplierFile existFile = null;
                if (fileId != 0) {
                    supplierFile.setParentFile(fileId);
                    existFile = supplierFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndSupplierAndLatestTrue(
                            file.getName(), fileId, supplierId);
                } else {
                    existFile = supplierFileRepository.findBySupplierAndNameAndParentFileIsNullAndLatestTrue(supplierId,
                            file.getName());
                }
                if (existFile == null) {
                    supplierFile.setName(file.getName());
                    supplierFile.setDescription(file.getDescription());
                    supplierFile.setSupplier(supplierId);
                    supplierFile.setVersion(1);
                    supplierFile.setSize(file.getSize());
                    supplierFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    supplierFile.setFileNo(autoNumber1);
                    supplierFile.setFileType("FILE");
                    supplierFile = supplierFileRepository.save(supplierFile);
                    supplierFile.setParentObject(PLMObjectType.MFRSUPPLIER);
                    fileList.add(supplierFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + supplierId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (supplierFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(supplierId, supplierFile.getId());
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + supplierId + File.separator + supplierFile.getId();
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
                PLMSupplierFile manufacturerFile = new PLMSupplierFile();
                PLMSupplierFile existFile = null;
                if (fileId != 0) {
                    manufacturerFile.setParentFile(fileId);
                    existFile = supplierFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndSupplierAndLatestTrue(file.getName(), fileId, supplierId);
                } else {
                    existFile = supplierFileRepository.findBySupplierAndNameAndParentFileIsNullAndLatestTrue(supplierId, file.getName());
                }
                if (existFile == null) {
                    manufacturerFile.setName(file.getName());
                    manufacturerFile.setDescription(file.getDescription());
                    String folderNumber = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    manufacturerFile.setVersion(1);
                    manufacturerFile.setSize(0L);
                    manufacturerFile.setSupplier(supplierId);
                    manufacturerFile.setFileNo(folderNumber);
                    manufacturerFile.setFileType("FOLDER");
                    manufacturerFile = supplierFileRepository.save(manufacturerFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + supplierId;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(supplierId, manufacturerFile.getId());
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(manufacturerFile);
                    copyFolderFiles(supplierId, file.getParentObject(), file.getId(), manufacturerFile.getId());
                }
            }
        }
        return fileList;
    }

    private void copyFolderFiles(Integer id, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        plmFiles.forEach(plmFile -> {
            PLMSupplierFile manufacturerFile = new PLMSupplierFile();
            manufacturerFile.setParentFile(parent);
            manufacturerFile.setName(plmFile.getName());
            manufacturerFile.setDescription(plmFile.getDescription());
            manufacturerFile.setSupplier(id);
            String folderNumber = null;
            if (plmFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                manufacturerFile.setVersion(1);
                manufacturerFile.setFileNo(folderNumber);
                manufacturerFile.setSize(plmFile.getSize());
                manufacturerFile.setFileType("FILE");
                manufacturerFile = supplierFileRepository.save(manufacturerFile);
                manufacturerFile.setParentObject(PLMObjectType.MFRSUPPLIER);
                plmFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmFile);

                String dir = "";
                if (manufacturerFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, manufacturerFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + manufacturerFile.getId();
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
                manufacturerFile.setVersion(1);
                manufacturerFile.setSize(0L);
                manufacturerFile.setFileNo(folderNumber);
                manufacturerFile.setFileType("FOLDER");
                manufacturerFile = supplierFileRepository.save(manufacturerFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(id, manufacturerFile.getId());
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(id, objectType, plmFile.getId(), manufacturerFile.getId());
            }
        });
    }

    public void undoCopiedSupplierFiles(Integer ecoId, List<PLMSupplierFile> supplierFiles) {
        supplierFiles.forEach(supplierFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(ecoId, supplierFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(supplierFile.getId(), dir);
            supplierFileRepository.delete(supplierFile.getId());
        });
    }
}