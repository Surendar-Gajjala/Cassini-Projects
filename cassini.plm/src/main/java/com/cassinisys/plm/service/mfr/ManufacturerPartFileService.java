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
import com.cassinisys.plm.event.ManufacturerPartEvents;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartFile;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mfr.ManufacturerPartFileRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.plm.FileDownloadHistoryRepository;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.activitystream.ManufacturerPartActivityStream;
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
 * Created by Home on 4/25/2016.
 */
@Service
@Transactional
public class ManufacturerPartFileService implements CrudService<PLMManufacturerPartFile, Integer>, PageableService<PLMManufacturerPartFile, Integer> {

    @Autowired
    private ManufacturerPartFileRepository manufacturerPartFileRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
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
    private FileHelpers fileHelpers;
    @Autowired
    private ItemFileService itemFileService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ObjectFileService qualityFileService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private UtilService utilService;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private ManufacturerPartService manufacturerPartService;
    @Autowired
    private ManufacturerPartActivityStream manufacturerPartActivityStream;

    @Override
    public PLMManufacturerPartFile create(PLMManufacturerPartFile manufacturerPartFile) {
        checkNotNull(manufacturerPartFile);
        manufacturerPartFile.setId(null);
        manufacturerPartFile = manufacturerPartFileRepository.save(manufacturerPartFile);
        return manufacturerPartFile;
    }

    @Override
    public PLMManufacturerPartFile update(PLMManufacturerPartFile manufacturerPartFile) {
        PLMFile file = fileRepository.findOne(manufacturerPartFile.getId());
        PLMManufacturerPart part = manufacturerPartRepository.findOne(manufacturerPartFile.getManufacturerPart());
        PLMManufacturerPartFile plmManufacturerPartFile = manufacturerPartFileRepository.findOne(manufacturerPartFile.getId());
        if (!file.getLocked().equals(manufacturerPartFile.getLocked())) {
                /* App events */
            if (manufacturerPartFile.getLocked()) {
                applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartFileLockedEvent(part, manufacturerPartFile));
            } else {
                applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartFileUnlockedEvent(part, manufacturerPartFile));
            }
        }
        plmManufacturerPartFile.setDescription(manufacturerPartFile.getDescription());
        plmManufacturerPartFile.setLocked(manufacturerPartFile.getLocked());
        plmManufacturerPartFile.setLockedBy(manufacturerPartFile.getLockedBy());
        plmManufacturerPartFile.setLockedDate(manufacturerPartFile.getLockedDate());
        manufacturerPartFile = manufacturerPartFileRepository.save(plmManufacturerPartFile);
        return manufacturerPartFile;
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileRepository.findOne(id);
        if (manufacturerPartFile == null) {
            throw new ResourceNotFoundException();
        }
        manufacturerPartFileRepository.delete(id);
    }

    public void deletePartFile(Integer partId, Integer id) throws JsonProcessingException {
        checkNotNull(id);
        PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileRepository.findOne(id);
        String fNames = null;
        List<PLMManufacturerPartFile> partFiles = manufacturerPartFileRepository.findByManufacturerPartAndFileNo(partId, manufacturerPartFile.getFileNo());
        for (PLMManufacturerPartFile partFile : partFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(partId, partFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(partFile.getId(), dir);
            if (fNames == null) {
                fNames = partFile.getName();
            } else {
                fNames = fNames + " , " + partFile.getName();
            }
            manufacturerPartFileRepository.delete(partFile);
        }
        if (manufacturerPartFile.getParentFile() != null) {
            PLMManufacturerPartFile parent = manufacturerPartFileRepository.findOne(manufacturerPartFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = manufacturerPartFileRepository.save(parent);
        }
        PLMManufacturerPart part = manufacturerPartRepository.findOne(partId);
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartFileDeletedEvent(part, manufacturerPartFile));
        manufacturerPartService.sendMfrPartSubscribeNotification(part, manufacturerPartFile.getName(), "fileDeleted");
    }

    @Override
    public PLMManufacturerPartFile get(Integer id) {
        checkNotNull(id);
        PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileRepository.findOne(id);
        if (manufacturerPartFile == null) {
            throw new ResourceNotFoundException();
        }
        return manufacturerPartFile;
    }

    @Override
    public List<PLMManufacturerPartFile> getAll() {
        return manufacturerPartFileRepository.findAll();
    }

    public List<PLMManufacturerPartFile> getAllFileVersions(Integer partId, String name) {
        return manufacturerPartFileRepository.findAllByManufacturerPartAndNameOrderByCreatedDateDesc(partId, name);
    }

    @Override
    public Page<PLMManufacturerPartFile> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        return manufacturerPartFileRepository.findAll(pageable);
    }

    public List<PLMManufacturerPartFile> findByMfrPart(Integer mfrPart) {
        List<PLMManufacturerPartFile> manufacturerPartFiles = manufacturerPartFileRepository.findByManufacturerPartAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(mfrPart);
        manufacturerPartFiles.forEach(manufacturerPartFile -> {
            manufacturerPartFile.setParentObject(PLMObjectType.MANUFACTURERPART);
            if (manufacturerPartFile.getFileType().equals("FOLDER")) {
                manufacturerPartFile.setCount(manufacturerPartFileRepository.getChildrenCountByParentFileAndLatestTrue(manufacturerPartFile.getId()));
                manufacturerPartFile.setCount(manufacturerPartFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(manufacturerPartFile.getManufacturerPart(), manufacturerPartFile.getId()));
            }
        });
        return manufacturerPartFiles;
    }

    public List<PLMManufacturerPartFile> findByMfrPartLatestFiles(Integer mfrPart) {
        List<PLMManufacturerPartFile> manufacturerPartFiles = manufacturerPartFileRepository.findByManufacturerPartAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(mfrPart);
        manufacturerPartFiles.forEach(manufacturerPartFile -> {
            manufacturerPartFile.setParentObject(PLMObjectType.MANUFACTURERPART);
            if (manufacturerPartFile.getFileType().equals("FOLDER")) {
                manufacturerPartFile.setCount(manufacturerPartFileRepository.getChildrenCountByParentFileAndLatestTrue(manufacturerPartFile.getId()));
            }
        });
        return manufacturerPartFiles;
    }

    public List<PLMManufacturerPartFile> uploadPartFiles(Integer partId, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException, JsonProcessingException {
        List<PLMManufacturerPartFile> uploadedPartFiles = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        List<PLMManufacturerPartFile> newFiles = new ArrayList<>();
        List<PLMManufacturerPartFile> versionedFiles = new ArrayList<>();
        PLMManufacturerPart part = manufacturerPartRepository.findOne(partId);
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        Login login = sessionWrapper.getSession().getLogin();
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
                    PLMManufacturerPartFile partFile = null;
                    if (folderId == 0) {
                        partFile = manufacturerPartFileRepository.findByManufacturerPartAndNameAndParentFileIsNullAndLatestTrue(partId, name);
                    } else {
                        partFile = manufacturerPartFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer newVersion = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (partFile != null) {
                        partFile.setLatest(false);
                        Integer oldVersion = partFile.getVersion();
                        newVersion = oldVersion + 1;
                        autoNumber1 = partFile.getFileNo();
                        oldFile = partFile.getId();
                        versioned = true;
                        manufacturerPartFileRepository.save(partFile);

                    }
                    if (partFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    partFile = new PLMManufacturerPartFile();
                    partFile.setName(name);
                    partFile.setFileNo(autoNumber1);
                    partFile.setCreatedBy(login.getPerson().getId());
                    partFile.setModifiedBy(login.getPerson().getId());
                    partFile.setVersion(newVersion);
                    partFile.setSize(file.getSize());
                    partFile.setFileType("FILE");
                    partFile.setManufacturerPart(partId);
                    if (partFile.getVersion() > 1) {
                        qualityFileService.copyFileAttributes(oldFile, partFile.getId());
                    }
                    if (fNames == null) {
                        fNames = partFile.getName();

                    } else {
                        fNames = fNames + "  , " + partFile.getName();
                    }
                    if (folderId != 0) {
                        partFile.setParentFile(folderId);
                    }
                    partFile = manufacturerPartFileRepository.save(partFile);
                    if (partFile.getParentFile() != null) {
                        PLMManufacturerPartFile parent = manufacturerPartFileRepository.findOne(partFile.getParentFile());
                        parent.setModifiedDate(partFile.getModifiedDate());
                        parent = manufacturerPartFileRepository.save(parent);
                    }
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + partId;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getParentFileSystemPath(partId, folderId);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + partFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                   /* Map<String, String> map = forgeService.uploadForgeFile(file.getOriginalFilename(), path);
                    if (map != null) {
                        partFile.setUrn(map.get("urn"));
                        partFile.setThumbnail(map.get("thumbnail"));
                        partFile = manufacturerPartFileRepository.save(partFile);
                    }*/
                    uploadedPartFiles.add(partFile);
                    if (versioned) {
                        versionedFiles.add(partFile);
                    } else {
                        newFiles.add(partFile);
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
          /* App Events */
        if (newFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartFilesAddedEvent(part, newFiles));
            manufacturerPartService.sendMfrPartSubscribeNotification(part, manufacturerPartActivityStream.getManufacturerFilesAddedJson(newFiles), "fileAdded");
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartFilesVersionedEvent(part, versionedFiles));
            manufacturerPartService.sendMfrPartSubscribeNotification(part, manufacturerPartActivityStream.getManufacturerFilesVersionedJson(versionedFiles), "fileVersioned");
        }
        return uploadedPartFiles;
    }

    public File getMfrPartFile(Integer partId, Integer fileId) {
        checkNotNull(partId);
        checkNotNull(fileId);
        PLMManufacturerPart mfrPart = manufacturerPartRepository.findOne(partId);
        if (mfrPart == null) {
            throw new ResourceNotFoundException();
        }
        PLMManufacturerPartFile mfrFile = manufacturerPartFileRepository.findOne(fileId);
        if (mfrFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(partId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<PLMManufacturerPartFile> findByFilesName(Integer partId, String name) {
        return manufacturerPartFileRepository.findByManufacturerPartAndNameContainingIgnoreCaseAndLatestTrue(partId, name);
    }


    public PLMFileDownloadHistory fileDownloadHistory(Integer mfrId, Integer fileId) throws JsonProcessingException {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMManufacturerPartFile plmItemFile = manufacturerPartFileRepository.findOne(fileId);
        PLMManufacturerPart mfrPart = manufacturerPartRepository.findOne(plmItemFile.getManufacturerPart());
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartFileDownloadedEvent(mfrPart, plmItemFile));
        manufacturerPartService.sendMfrPartSubscribeNotification(mfrPart, plmItemFile.getName(), "fileDownloaded");
        return plmFileDownloadHistory;
    }

    public List<PLMManufacturerPartFile> getAllFileVersionAndCommentsAndDownloads(Integer partId, Integer fileId, ObjectType objectType) {
        List<PLMManufacturerPartFile> itemFiles = new ArrayList<>();
        PLMManufacturerPartFile partFile = manufacturerPartFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(partFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(partFile.getId());
        if (comments.size() > 0) {
            partFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            partFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(partFile);
        List<PLMManufacturerPartFile> files = manufacturerPartFileRepository.findByManufacturerPartAndFileNoAndLatestFalseOrderByCreatedDateDesc(partFile.getManufacturerPart(), partFile.getFileNo());
        if (files.size() > 0) {
            for (PLMManufacturerPartFile file : files) {
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

    public PLMManufacturerPartFile renamePartFileName(Integer id, String newFileName) throws IOException, JsonProcessingException {
        PLMManufacturerPartFile file1 = manufacturerPartFileRepository.findOne(id);
        String oldName = file1.getName();
        file1.setLatest(false);
        PLMManufacturerPartFile oldFile = (PLMManufacturerPartFile) Utils.cloneObject(file1, PLMManufacturerPartFile.class);
        PLMManufacturerPartFile plmManufacturerPartFile = manufacturerPartFileRepository.save(file1);
        PLMManufacturerPartFile manufacturerPartFile = (PLMManufacturerPartFile) Utils.cloneObject(plmManufacturerPartFile, PLMManufacturerPartFile.class);
        if (manufacturerPartFile != null) {
            manufacturerPartFile.setId(null);
            manufacturerPartFile.setName(newFileName);
            manufacturerPartFile.setVersion(file1.getVersion() + 1);
            manufacturerPartFile.setLatest(true);
            manufacturerPartFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            manufacturerPartFile = manufacturerPartFileRepository.save(manufacturerPartFile);
            if (manufacturerPartFile.getParentFile() != null) {
                PLMManufacturerPartFile parent = manufacturerPartFileRepository.findOne(manufacturerPartFile.getParentFile());
                parent.setModifiedDate(manufacturerPartFile.getModifiedDate());
                parent = manufacturerPartFileRepository.save(parent);
            }
            qualityFileService.copyFileAttributes(file1.getId(), manufacturerPartFile.getId());
            String dir = "";
            if (manufacturerPartFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(manufacturerPartFile.getManufacturerPart(), id);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + manufacturerPartFile.getManufacturerPart();
            }
            dir = dir + File.separator + manufacturerPartFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + manufacturerPartFile.getManufacturerPart() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);

            PLMManufacturerPart part = manufacturerPartRepository.findOne(manufacturerPartFile.getManufacturerPart());
            /* App Events */
            applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartFileRenamedEvent(part, oldFile, manufacturerPartFile, "Rename"));
            manufacturerPartService.sendMfrPartSubscribeNotification(part, oldFile.getName() + "-" + manufacturerPartFile.getName(), "fileRename");
        }
        return manufacturerPartFile;
    }

    public List<PLMManufacturerPartFile> replaceMfrPartFiles(Integer mfrPartId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException, JsonProcessingException {
        List<PLMManufacturerPartFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        PLMManufacturerPartFile oldFile = null;
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(mfrPartId);
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        PLMManufacturerPartFile plmManufacturerPartFile = null;
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
                    PLMManufacturerPartFile manufacturerPartFile = null;
                    plmManufacturerPartFile = manufacturerPartFileRepository.findOne(fileId);
                    String oldName = "";
                    if (plmManufacturerPartFile != null && plmManufacturerPartFile.getParentFile() != null) {
                        manufacturerPartFile = manufacturerPartFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        manufacturerPartFile = manufacturerPartFileRepository.findByManufacturerPartAndNameAndParentFileIsNullAndLatestTrue(mfrPartId, name);
                    }
                    if (manufacturerPartFile != null) {
                        comments = commentRepository.findAllByObjectId(manufacturerPartFile.getId());
                    }
                    if (plmManufacturerPartFile != null) {
                        oldName = plmManufacturerPartFile.getName();
                        plmManufacturerPartFile.setLatest(false);
                        plmManufacturerPartFile = manufacturerPartFileRepository.save(plmManufacturerPartFile);
                    }
                    if (manufacturerPartFile != null) {
                        comments = commentRepository.findAllByObjectId(manufacturerPartFile.getId());
                    }
                    manufacturerPartFile = new PLMManufacturerPartFile();
                    manufacturerPartFile.setName(name);
                    if (plmManufacturerPartFile != null && plmManufacturerPartFile.getParentFile() != null) {
                        manufacturerPartFile.setParentFile(plmManufacturerPartFile.getParentFile());
                    }
                    if (plmManufacturerPartFile != null) {
                        manufacturerPartFile.setFileNo(plmManufacturerPartFile.getFileNo());
                        manufacturerPartFile.setVersion(plmManufacturerPartFile.getVersion() + 1);
                        manufacturerPartFile.setReplaceFileName(plmManufacturerPartFile.getName() + " Replaced to " + name);
                        oldFile = JsonUtils.cloneEntity(manufacturerPartFile, PLMManufacturerPartFile.class);
                    }
                    manufacturerPartFile.setCreatedBy(login.getPerson().getId());
                    manufacturerPartFile.setModifiedBy(login.getPerson().getId());
                    manufacturerPartFile.setManufacturerPart(mfrPartId);
                    manufacturerPartFile.setSize(file.getSize());
                    manufacturerPartFile.setFileType("FILE");
                    manufacturerPartFile = manufacturerPartFileRepository.save(manufacturerPartFile);
                    if (manufacturerPartFile.getParentFile() != null) {
                        PLMManufacturerPartFile parent = manufacturerPartFileRepository.findOne(manufacturerPartFile.getParentFile());
                        parent.setModifiedDate(manufacturerPartFile.getModifiedDate());
                        parent = manufacturerPartFileRepository.save(parent);
                    }
                    if (manufacturerPartFile != null) {
                        qualityFileService.copyFileAttributes(plmManufacturerPartFile.getId(), manufacturerPartFile.getId());
                    }
                    String dir = "";
                    if (plmManufacturerPartFile != null && plmManufacturerPartFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(mfrPartId, fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + mfrPartId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + manufacturerPartFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(manufacturerPartFile);
                    PLMManufacturerPart part = manufacturerPartRepository.findOne(manufacturerPartFile.getManufacturerPart());
                    applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartFileRenamedEvent(part, plmManufacturerPartFile, manufacturerPartFile, "Replace"));
                    manufacturerPartService.sendMfrPartSubscribeNotification(part, oldFile.getName() + "-" + manufacturerPartFile.getName(), "fileReplace");
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    private String getReplaceFileSystemPath(Integer mfrPartId, Integer fileId) {
        String path = "";
        PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileRepository.findOne(fileId);
        if (manufacturerPartFile.getParentFile() != null) {
            path = utilService.visitParentFolder(mfrPartId, manufacturerPartFile.getParentFile(), path);
        } else {
            path = File.separator + mfrPartId;
        }
        return path;
    }

    public PLMManufacturerPartFile createMfrPartFolder(Integer mfrPartId, PLMManufacturerPartFile manufacturerFile) throws JsonProcessingException {
        manufacturerFile.setId(null);
        String folderNumber = null;
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(mfrPartId);
        PLMManufacturerPartFile existFolderName = null;
        if (manufacturerFile.getParentFile() != null) {
            existFolderName = manufacturerPartFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndManufacturerPartAndLatestTrue(manufacturerFile.getName(), manufacturerFile.getParentFile(), mfrPartId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(manufacturerFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", manufacturerFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = manufacturerPartFileRepository.findByNameEqualsIgnoreCaseAndManufacturerPartAndLatestTrue(manufacturerFile.getName(), mfrPartId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existFolderName.getName());
                throw new CassiniException(result);
            }
        }
        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        manufacturerFile.setManufacturerPart(mfrPartId);
        manufacturerFile.setFileNo(folderNumber);
        manufacturerFile.setFileType("FOLDER");
        manufacturerFile = manufacturerPartFileRepository.save(manufacturerFile);
        if (manufacturerFile.getParentFile() != null) {
            PLMManufacturerPartFile parent = manufacturerPartFileRepository.findOne(manufacturerFile.getParentFile());
            parent.setModifiedDate(manufacturerFile.getModifiedDate());
            parent = manufacturerPartFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(mfrPartId, manufacturerFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartFoldersAddedEvent(manufacturerPart, manufacturerFile));
        manufacturerPartService.sendMfrPartSubscribeNotification(manufacturerPart, manufacturerFile.getName(), "folderAdded");
        return manufacturerFile;
    }

    private String getParentFileSystemPath(Integer mfrPartId, Integer fileId) {
        String path = "";
        PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (manufacturerPartFile.getParentFile() != null) {
            path = utilService.visitParentFolder(mfrPartId, manufacturerPartFile.getParentFile(), path);
        } else {
            path = File.separator + mfrPartId + File.separator + manufacturerPartFile.getId();
        }
        return path;
    }

    public PLMFile moveMfrPartFileToFolder(Integer id, PLMManufacturerPartFile plmManufacturerFile) throws Exception {
        PLMManufacturerPartFile file = manufacturerPartFileRepository.findOne(plmManufacturerFile.getId());
        PLMManufacturerPartFile existFile = (PLMManufacturerPartFile) Utils.cloneObject(file, PLMManufacturerPartFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getManufacturerPart(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getManufacturerPart() + File.separator + existFile.getId();
        }
        if (plmManufacturerFile.getParentFile() != null) {
            PLMManufacturerPartFile existItemFile = manufacturerPartFileRepository.findByParentFileAndNameAndLatestTrue(plmManufacturerFile.getParentFile(), plmManufacturerFile.getName());
            PLMManufacturerPartFile folder = manufacturerPartFileRepository.findOne(plmManufacturerFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmManufacturerFile = manufacturerPartFileRepository.save(plmManufacturerFile);
            }
        } else {
            PLMManufacturerPartFile existItemFile = manufacturerPartFileRepository.findByManufacturerPartAndNameAndParentFileIsNullAndLatestTrue(plmManufacturerFile.getManufacturerPart(), plmManufacturerFile.getName());
            PLMManufacturerPart manufacturer = manufacturerPartRepository.findOne(plmManufacturerFile.getManufacturerPart());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist ", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                plmManufacturerFile = manufacturerPartFileRepository.save(plmManufacturerFile);
            }
        }
        if (plmManufacturerFile != null) {
            String dir = "";
            if (plmManufacturerFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(plmManufacturerFile.getManufacturerPart(), plmManufacturerFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmManufacturerFile.getManufacturerPart() + File.separator + plmManufacturerFile.getId();
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
            List<PLMManufacturerPartFile> oldVersionFiles = manufacturerPartFileRepository.findByManufacturerPartAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getManufacturerPart(), existFile.getFileNo());
            for (PLMManufacturerPartFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getManufacturerPart(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getManufacturerPart() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(plmManufacturerFile.getManufacturerPart(), plmManufacturerFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmManufacturerFile.getParentFile());
                oldVersionFile = manufacturerPartFileRepository.save(oldVersionFile);
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
        return plmManufacturerFile;
    }

    public List<PLMManufacturerPartFile> getMfrPartFolderChildren(Integer folderId) {
        List<PLMManufacturerPartFile> manufacturerFiles = manufacturerPartFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(folderId);
        manufacturerFiles.forEach(manufacturerFile -> {
            manufacturerFile.setParentObject(PLMObjectType.MANUFACTURERPART);
            if (manufacturerFile.getFileType().equals("FOLDER")) {
                manufacturerFile.setCount(manufacturerPartFileRepository.getChildrenCountByParentFileAndLatestTrue(manufacturerFile.getId()));
                manufacturerFile.setCount(manufacturerFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(manufacturerFile.getManufacturerPart(), manufacturerFile.getId()));
            }
        });
        return manufacturerFiles;
    }

    public void deleteMfrPartFolder(Integer mfrPartId, Integer folderId) throws JsonProcessingException {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(mfrPartId, folderId);
        PLMManufacturerPartFile file = manufacturerPartFileRepository.findOne(folderId);
        PLMManufacturerPart part = manufacturerPartRepository.findOne(mfrPartId);
        List<PLMManufacturerPartFile> manufacturerFiles = manufacturerPartFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) manufacturerFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        manufacturerPartFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMManufacturerPartFile parent = manufacturerPartFileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = manufacturerPartFileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartFoldersDeletedEvent(part, file));
        manufacturerPartService.sendMfrPartSubscribeNotification(part, file.getName(), "folderDeleted");
    }

    public void generateZipFile(Integer partId, HttpServletResponse response) throws FileNotFoundException, IOException {
        PLMManufacturerPart plmManufacturerPart = manufacturerPartRepository.findOne(partId);
        List<PLMManufacturerPartFile> plmManufacturerPartFiles = manufacturerPartFileRepository.findByManufacturerPartAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(partId);
        ArrayList<String> fileList = new ArrayList<>();
        plmManufacturerPartFiles.forEach(plmManufacturerPartFile -> {
            File file = getMfrPartFile(partId, plmManufacturerPartFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = plmManufacturerPart.getPartName() + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "MFRPART",partId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    public PLMManufacturerPartFile getLatestUploadedMfrPartFile(Integer mfrId, Integer fileId) {
        PLMManufacturerPartFile manufacturerFile = manufacturerPartFileRepository.findOne(fileId);
        PLMManufacturerPartFile plmManufacturerFile = manufacturerPartFileRepository.findByManufacturerPartAndFileNoAndLatestTrue(manufacturerFile.getManufacturerPart(), manufacturerFile.getFileNo());
        return plmManufacturerFile;
    }

    public PLMFile updateFileDescription(Integer mfrId, Integer id, PLMManufacturerPartFile plmManufacturerFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            file.setDescription(plmManufacturerFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<PLMManufacturerPartFile> pasteFromClipboard(Integer mfrId, Integer fileId, List<PLMFile> files) {
        List<PLMManufacturerPartFile> fileList = new ArrayList<>();
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
                PLMManufacturerPartFile manufacturerPartFile = new PLMManufacturerPartFile();
                PLMManufacturerPartFile existFile = null;
                if (fileId != 0) {
                    manufacturerPartFile.setParentFile(fileId);
                    existFile = manufacturerPartFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndManufacturerPartAndLatestTrue(file.getName(), fileId, mfrId);
                } else {
                    existFile = manufacturerPartFileRepository.findByManufacturerPartAndNameAndParentFileIsNullAndLatestTrue(mfrId, file.getName());
                }
                if (existFile == null) {
                    manufacturerPartFile.setName(file.getName());
                    manufacturerPartFile.setDescription(file.getDescription());
                    manufacturerPartFile.setManufacturerPart(mfrId);
                    manufacturerPartFile.setVersion(1);
                    manufacturerPartFile.setSize(file.getSize());
                    manufacturerPartFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    manufacturerPartFile.setFileNo(autoNumber1);
                    manufacturerPartFile.setFileType("FILE");
                    manufacturerPartFile = manufacturerPartFileRepository.save(manufacturerPartFile);
                    manufacturerPartFile.setParentObject(PLMObjectType.MANUFACTURERPART);
                    fileList.add(manufacturerPartFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + mfrId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (manufacturerPartFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(mfrId, manufacturerPartFile.getId());
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + mfrId + File.separator + manufacturerPartFile.getId();
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
                PLMManufacturerPartFile manufacturerFile = new PLMManufacturerPartFile();
                PLMManufacturerPartFile existFile = null;
                if (fileId != 0) {
                    manufacturerFile.setParentFile(fileId);
                    existFile = manufacturerPartFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndManufacturerPartAndLatestTrue(file.getName(), fileId, mfrId);
                } else {
                    existFile = manufacturerPartFileRepository.findByManufacturerPartAndNameAndParentFileIsNullAndLatestTrue(mfrId, file.getName());
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
                    manufacturerFile.setManufacturerPart(mfrId);
                    manufacturerFile.setFileNo(folderNumber);
                    manufacturerFile.setFileType("FOLDER");
                    manufacturerFile = manufacturerPartFileRepository.save(manufacturerFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + mfrId;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(mfrId, manufacturerFile.getId());
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(manufacturerFile);
                    copyFolderFiles(mfrId, file.getParentObject(), file.getId(), manufacturerFile.getId());
                }
            }
        }
        return fileList;
    }

    private void copyFolderFiles(Integer id, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        plmFiles.forEach(plmFile -> {
            PLMManufacturerPartFile manufacturerFile = new PLMManufacturerPartFile();
            manufacturerFile.setParentFile(parent);
            manufacturerFile.setName(plmFile.getName());
            manufacturerFile.setDescription(plmFile.getDescription());
            manufacturerFile.setManufacturerPart(id);
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
                manufacturerFile = manufacturerPartFileRepository.save(manufacturerFile);
                manufacturerFile.setParentObject(PLMObjectType.MANUFACTURERPART);
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
                manufacturerFile = manufacturerPartFileRepository.save(manufacturerFile);

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

    public void undoCopiedMfrPartFiles(Integer ecoId, List<PLMManufacturerPartFile> plmManufacturerPartFiles) {
        plmManufacturerPartFiles.forEach(plmManufacturerPartFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(ecoId, plmManufacturerPartFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(plmManufacturerPartFile.getId(), dir);
            manufacturerPartFileRepository.delete(plmManufacturerPartFile.getId());
        });
    }
}