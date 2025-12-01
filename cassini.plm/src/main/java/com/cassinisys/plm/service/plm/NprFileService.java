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
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.NprEvents;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.repo.plm.FileDownloadHistoryRepository;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.plm.NprFileRepository;
import com.cassinisys.plm.repo.plm.NprRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
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
public class NprFileService implements CrudService<PLMNprFile, Integer>, PageableService<PLMNprFile, Integer> {

    @Autowired
    private NprFileRepository nprFileRepository;
    @Autowired
    private NprRepository nprRepository;
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

    @Override
    public PLMNprFile create(PLMNprFile nprFile) {
        checkNotNull(nprFile);
        nprFile.setId(null);
        nprFile = nprFileRepository.save(nprFile);
        PLMNpr npr = nprRepository.findOne(nprFile.getNpr());
        return nprFile;
    }

    @Override
    public PLMNprFile update(PLMNprFile nprFile) {
        PLMFile file = fileRepository.findOne(nprFile.getId());
        PLMNprFile nprFile1 = nprFileRepository.findOne(nprFile.getId());
        PLMNpr npr = nprRepository.findOne(nprFile1.getNpr());
        if (!file.getLocked().equals(nprFile.getLocked())) {
                /* App events */
            if (nprFile.getLocked()) {
                applicationEventPublisher.publishEvent(new NprEvents.NprFileLockedEvent(npr, nprFile1));
            } else {
                applicationEventPublisher.publishEvent(new NprEvents.NprFileUnlockedEvent(npr, nprFile1));
            }
        }
        nprFile1.setDescription(nprFile.getDescription());
        nprFile1.setLocked(nprFile.getLocked());
        nprFile1.setLockedBy(nprFile.getLockedBy());
        nprFile1.setLockedDate(nprFile.getLockedDate());
        nprFile = nprFileRepository.save(nprFile1);
        return nprFile;
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        PLMNprFile nprFile = nprFileRepository.findOne(id);
        if (nprFile == null) {
            throw new ResourceNotFoundException();
        }
        nprFileRepository.delete(id);
        PLMNpr npr = nprRepository.findOne(nprFile.getNpr());
    }

    public PLMNprFile updateFileName(Integer id, String newFileName) throws IOException {
        PLMNprFile file1 = nprFileRepository.findOne(id);
        PLMNprFile oldFile = (PLMNprFile) Utils.cloneObject(file1, PLMNprFile.class);
        String oldName = file1.getName();
        file1.setLatest(false);
        PLMNprFile plmNprFile = nprFileRepository.save(file1);
        PLMNprFile nprFile = (PLMNprFile) Utils.cloneObject(plmNprFile, PLMNprFile.class);
        if (nprFile != null) {
            nprFile.setId(null);
            nprFile.setName(newFileName);
            nprFile.setVersion(file1.getVersion() + 1);
            nprFile.setLatest(true);
            nprFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            nprFile = nprFileRepository.save(nprFile);
            if (nprFile.getParentFile() != null) {
                PLMNprFile parent = nprFileRepository.findOne(nprFile.getParentFile());
                parent.setModifiedDate(nprFile.getModifiedDate());
                parent = nprFileRepository.save(parent);
            }
            qualityFileService.copyFileAttributes(file1.getId(), nprFile.getId());
            String dir = "";
            if (nprFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(nprFile.getNpr(), id);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + nprFile.getNpr();
            }
            dir = dir + File.separator + nprFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + nprFile.getNpr() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            PLMNpr manufacturer = nprRepository.findOne(nprFile.getNpr());
            /* App Events */
            applicationEventPublisher.publishEvent(new NprEvents.NprFileRenamedEvent(manufacturer, oldFile, nprFile, "Rename"));
        }
        return nprFile;
    }

    public List<PLMNprFile> replaceNprFiles(Integer nprId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMNprFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        PLMNprFile oldFile = null;
        PLMNpr manufacturer = nprRepository.findOne(nprId);
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        PLMNprFile plmNprFile = null;
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
                    PLMNprFile nprFile = null;
                    plmNprFile = nprFileRepository.findOne(fileId);
                    String oldName = "";
                    if (plmNprFile != null && plmNprFile.getParentFile() != null) {
                        nprFile = nprFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        nprFile = nprFileRepository.findByNprAndNameAndParentFileIsNullAndLatestTrue(nprId, name);
                    }
                    if (nprFile != null) {
                        comments = commentRepository.findAllByObjectId(nprFile.getId());
                    }
                    if (plmNprFile != null) {
                        oldName = plmNprFile.getName();
                        plmNprFile.setLatest(false);
                        plmNprFile = nprFileRepository.save(plmNprFile);
                    }
                    if (nprFile != null) {
                        comments = commentRepository.findAllByObjectId(nprFile.getId());
                    }
                    nprFile = new PLMNprFile();
                    nprFile.setName(name);
                    if (plmNprFile != null && plmNprFile.getParentFile() != null) {
                        nprFile.setParentFile(plmNprFile.getParentFile());
                    }
                    if (plmNprFile != null) {
                        nprFile.setFileNo(plmNprFile.getFileNo());
                        nprFile.setVersion(plmNprFile.getVersion() + 1);
                        nprFile.setReplaceFileName(plmNprFile.getName() + " Replaced to " + name);
                        oldFile = JsonUtils.cloneEntity(plmNprFile, PLMNprFile.class);
                    }
                    nprFile.setCreatedBy(login.getPerson().getId());
                    nprFile.setModifiedBy(login.getPerson().getId());
                    nprFile.setNpr(nprId);
                    nprFile.setSize(file.getSize());
                    nprFile.setFileType("FILE");
                    nprFile = nprFileRepository.save(nprFile);
                    if (nprFile.getParentFile() != null) {
                        PLMNprFile parent = nprFileRepository.findOne(nprFile.getParentFile());
                        parent.setModifiedDate(nprFile.getModifiedDate());
                        parent = nprFileRepository.save(parent);
                    }
                    if (plmNprFile != null) {
                        qualityFileService.copyFileAttributes(plmNprFile.getId(), nprFile.getId());
                    }
                    String dir = "";
                    if (plmNprFile != null && plmNprFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(nprId, fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + nprId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + nprFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(nprFile);
                      /* App Events */
                    applicationEventPublisher.publishEvent(new NprEvents.NprFileRenamedEvent(manufacturer, plmNprFile, nprFile, "Replace"));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    private String getReplaceFileSystemPath(Integer nprPartId, Integer fileId) {
        String path = "";
        PLMNprFile manufacturerPartFile = nprFileRepository.findOne(fileId);
        if (manufacturerPartFile.getParentFile() != null) {
            path = utilService.visitParentFolder(nprPartId, manufacturerPartFile.getParentFile(), path);
        } else {
            path = File.separator + nprPartId;
        }
        return path;
    }

    public void deleteNprFile(Integer nprId, Integer id) {
        checkNotNull(id);
        PLMNprFile nprFile = nprFileRepository.findOne(id);
        PLMNpr manufacturer = nprRepository.findOne(nprId);
        List<PLMNprFile> nprFiles = nprFileRepository.findByNprAndFileNo(nprId, nprFile.getFileNo());
        for (PLMNprFile nprFile1 : nprFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(nprId, nprFile1.getId());
            fileSystemService.deleteDocumentFromDiskFolder(nprFile.getId(), dir);
            nprFileRepository.delete(nprFile1.getId());
        }
        if (nprFile.getParentFile() != null) {
            PLMNprFile parent = nprFileRepository.findOne(nprFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = nprFileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new NprEvents.NprFileDeletedEvent(manufacturer, nprFile));
    }

    @Override
    public PLMNprFile get(Integer id) {
        checkNotNull(id);
        PLMNprFile nprFile = nprFileRepository.findOne(id);
        if (nprFile == null) {
            throw new ResourceNotFoundException();
        }
        return nprFile;
    }

    @Override
    public List<PLMNprFile> getAll() {
        return nprFileRepository.findAll();
    }

    public List<PLMNprFile> getAllFileVersions(Integer nprId, String name) {
        return nprFileRepository.findAllByNprAndNameOrderByCreatedDateDesc(nprId, name);
    }

    @Override
    public Page<PLMNprFile> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null)
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order("id")));
        return nprFileRepository.findAll(pageable);
    }

    public List<PLMNprFile> findByNpr(Integer manufacturer) {
        List<PLMNprFile> nprFiles = nprFileRepository.findByNprAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(manufacturer);
        nprFiles.forEach(nprFile -> {
            nprFile.setParentObject(PLMObjectType.PLMNPR);
            if (nprFile.getFileType().equals("FOLDER")) {
                nprFile.setCount(nprFileRepository.getChildrenCountByParentFileAndLatestTrue(nprFile.getId()));
                nprFile.setCount(nprFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(nprFile.getNpr(), nprFile.getId()));
            }
        });
        return nprFiles;
    }

    public List<PLMNprFile> findByNprLatest(Integer manufacturer) {
        List<PLMNprFile> nprFiles = nprFileRepository.findByNprAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(manufacturer);
        nprFiles.forEach(nprFile -> {
            nprFile.setParentObject(PLMObjectType.PLMNPR);
            if (nprFile.getFileType().equals("FOLDER")) {
                nprFile.setCount(nprFileRepository.getChildrenCountByParentFileAndLatestTrue(nprFile.getId()));
            }
        });
        return nprFiles;
    }

    public List<PLMNprFile> uploadFiles(Integer nprId, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMNprFile> uploadedFiles = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        PLMNpr manufacturer = nprRepository.findOne(nprId);
        String[] fileExtension = null;
        List<PLMNprFile> newFiles = new ArrayList<>();
        List<PLMNprFile> versionedFiles = new ArrayList<>();
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
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
                    PLMNprFile nprFile = null;
                    if (folderId == 0) {
                        nprFile = nprFileRepository.findByNprAndNameAndParentFileIsNullAndLatestTrue(nprId, name);
                    } else {
                        nprFile = nprFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer version = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (nprFile != null) {
                        nprFile.setLatest(false);
                        Integer oldVersion = nprFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = nprFile.getFileNo();
                        oldFile = nprFile.getId();
                /*nprFile.setVersion(newVersion);*/
                        versioned = true;
                        nprFileRepository.save(nprFile);
                    }
                    if (nprFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    nprFile = new PLMNprFile();
                    nprFile.setName(name);
                    nprFile.setFileNo(autoNumber1);
                    nprFile.setCreatedBy(login.getPerson().getId());
                    nprFile.setModifiedBy(login.getPerson().getId());
            /*nprFile.setVersion(1);*/
                    nprFile.setVersion(version);
                    nprFile.setNpr(nprId);
                    nprFile.setSize(file.getSize());
                    nprFile.setFileType("FILE");
                    if (folderId != 0) {
                        nprFile.setParentFile(folderId);
                    }
                    nprFile = nprFileRepository.save(nprFile);
                    if (nprFile.getParentFile() != null) {
                        PLMNprFile parent = nprFileRepository.findOne(nprFile.getParentFile());
                        parent.setModifiedDate(nprFile.getModifiedDate());
                        parent = nprFileRepository.save(parent);
                    }
                    if (nprFile.getVersion() > 1) {
                        qualityFileService.copyFileAttributes(oldFile, nprFile.getId());
                    }
                    if (fNames == null) {
                        fNames = nprFile.getName();

                    } else {
                        fNames = fNames + "  , " + nprFile.getName();
                    }
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + nprId;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getParentFileSystemPath(nprId, folderId);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + nprFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploadedFiles.add(nprFile);
                    if (versioned) {
                        versionedFiles.add(nprFile);
                    } else {
                        newFiles.add(nprFile);
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
          /* App Events */
        if (newFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new NprEvents.NprFilesAddedEvent(manufacturer, newFiles));
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new NprEvents.NprFilesVersionedEvent(manufacturer, versionedFiles));
        }
        return uploadedFiles;
    }

    public File getNprFile(Integer nprId, Integer fileId) {
        checkNotNull(nprId);
        checkNotNull(fileId);
        PLMNpr npr = nprRepository.findOne(nprId);
        if (npr == null) {
            throw new ResourceNotFoundException();
        }
        PLMNprFile nprFile = nprFileRepository.findOne(fileId);
        if (nprFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(nprId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<PLMNprFile> findByNprFilesName(Integer nprId, String name) {
        return nprFileRepository.findByNprAndNameContainingIgnoreCaseAndLatestTrue(nprId, name);
    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer nprId, Integer fileId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMNprFile plmItemFile = nprFileRepository.findOne(fileId);
        PLMNpr npr = nprRepository.findOne(plmItemFile.getNpr());
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        applicationEventPublisher.publishEvent(new NprEvents.NprFileDownloadedEvent(npr, plmItemFile));
        return plmFileDownloadHistory;
    }

    public List<PLMNprFile> getAllFileVersionComments(Integer nprId, Integer fileId, ObjectType objectType) {
        List<PLMNprFile> itemFiles = new ArrayList<>();
        PLMNprFile nprFile = nprFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(nprFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(nprFile.getId());
        if (comments.size() > 0) {
            nprFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            nprFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(nprFile);
        List<PLMNprFile> files = nprFileRepository.findByNprAndFileNoAndLatestFalseOrderByCreatedDateDesc(nprFile.getNpr(), nprFile.getFileNo());
        if (files.size() > 0) {
            for (PLMNprFile file : files) {
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

    public PLMNprFile createNprFolder(Integer nprId, PLMNprFile nprFile) {
        nprFile.setId(null);
        String folderNumber = null;
        PLMNpr manufacturer = nprRepository.findOne(nprId);
        PLMNprFile existFolderName = null;
        if (nprFile.getParentFile() != null) {
            existFolderName = nprFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndNprAndLatestTrue(nprFile.getName(), nprFile.getParentFile(), nprId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(nprFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", nprFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = nprFileRepository.findByNameEqualsIgnoreCaseAndNprAndLatestTrue(nprFile.getName(), nprId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", nprFile.getName());
                throw new CassiniException(result);
            }
        }

        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        nprFile.setNpr(nprId);
        nprFile.setFileNo(folderNumber);
        nprFile.setFileType("FOLDER");
        nprFile = nprFileRepository.save(nprFile);
        if (nprFile.getParentFile() != null) {
            PLMNprFile parent = nprFileRepository.findOne(nprFile.getParentFile());
            parent.setModifiedDate(nprFile.getModifiedDate());
            parent = nprFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(nprId, nprFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new NprEvents.NprFoldersAddedEvent(manufacturer, nprFile));
        return nprFile;
    }

    private String getParentFileSystemPath(Integer nprId, Integer fileId) {
        String path = "";
        PLMNprFile nprFile = nprFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (nprFile.getParentFile() != null) {
            path = utilService.visitParentFolder(nprId, nprFile.getParentFile(), path);
        } else {
            path = File.separator + nprId + File.separator + nprFile.getId();
        }
        return path;
    }

    public PLMFile moveNprFileToFolder(Integer id, PLMNprFile plmNprFile) throws Exception {
        PLMNprFile file = nprFileRepository.findOne(plmNprFile.getId());
        PLMNprFile existFile = (PLMNprFile) Utils.cloneObject(file, PLMNprFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getNpr(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getNpr() + File.separator + existFile.getId();
        }
        if (plmNprFile.getParentFile() != null) {
            PLMNprFile existItemFile = nprFileRepository.findByParentFileAndNameAndLatestTrue(plmNprFile.getParentFile(), plmNprFile.getName());
            PLMNprFile folder = nprFileRepository.findOne(plmNprFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmNprFile = nprFileRepository.save(plmNprFile);
            }
        } else {
            PLMNprFile existItemFile = nprFileRepository.findByNprAndNameAndParentFileIsNullAndLatestTrue(plmNprFile.getNpr(), plmNprFile.getName());
            PLMNpr manufacturer = nprRepository.findOne(plmNprFile.getNpr());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                plmNprFile = nprFileRepository.save(plmNprFile);
            }
        }
        if (plmNprFile != null) {
            String dir = "";
            if (plmNprFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(plmNprFile.getNpr(), plmNprFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmNprFile.getNpr() + File.separator + plmNprFile.getId();
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
            List<PLMNprFile> oldVersionFiles = nprFileRepository.findByNprAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getNpr(), existFile.getFileNo());
            for (PLMNprFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getNpr(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getNpr() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(plmNprFile.getNpr(), plmNprFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmNprFile.getParentFile());
                oldVersionFile = nprFileRepository.save(oldVersionFile);
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
        return plmNprFile;
    }

    public List<PLMNprFile> getNprFolderChildren(Integer folderId) {
        List<PLMNprFile> nprFiles = nprFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(folderId);
        nprFiles.forEach(nprFile -> {
            nprFile.setParentObject(PLMObjectType.PLMNPR);
            if (nprFile.getFileType().equals("FOLDER")) {
                nprFile.setCount(nprFileRepository.getChildrenCountByParentFileAndLatestTrue(nprFile.getId()));
                nprFile.setCount(nprFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(nprFile.getNpr(), nprFile.getId()));
            }
        });
        return nprFiles;
    }

    public void deleteFolder(Integer nprId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(nprId, folderId);
        PLMNprFile file = nprFileRepository.findOne(folderId);
        PLMNpr npr = nprRepository.findOne(nprId);
        List<PLMNprFile> nprFiles = nprFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) nprFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        nprFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new NprEvents.NprFoldersDeletedEvent(npr, file));
    }

    public void generateZipFile(Integer nprId, HttpServletResponse response) throws FileNotFoundException, IOException {
        PLMNpr plmNpr = nprRepository.findOne(nprId);
        List<PLMNprFile> plmNprFiles = nprFileRepository.findByNprAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(nprId);
        ArrayList<String> fileList = new ArrayList<>();
        plmNprFiles.forEach(plmNprFile -> {
            File file = getNprFile(nprId, plmNprFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = "NPR" + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "MFR",nprId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional
    public PLMNprFile getLatestUploadedNprFile(Integer nprId, Integer fileId) {
        PLMNprFile nprFile = nprFileRepository.findOne(fileId);
        PLMNprFile plmNprFile = nprFileRepository.findByNprAndFileNoAndLatestTrue(nprFile.getNpr(), nprFile.getFileNo());
        return plmNprFile;
    }

    public PLMFile updateFileDescription(Integer nprId, Integer id, PLMNprFile plmNprFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            file.setDescription(plmNprFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<PLMNprFile> pasteFromClipboard(Integer nprId, Integer fileId, List<PLMFile> files) {
        List<PLMNprFile> fileList = new ArrayList<>();
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
                PLMNprFile nprFile = new PLMNprFile();
                PLMNprFile existFile = null;
                if (fileId != 0) {
                    nprFile.setParentFile(fileId);
                    existFile = nprFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndNprAndLatestTrue(file.getName(), fileId, nprId);
                } else {
                    existFile = nprFileRepository.findByNprAndNameAndParentFileIsNullAndLatestTrue(nprId, file.getName());
                }
                if (existFile == null) {
                    nprFile.setName(file.getName());
                    nprFile.setDescription(file.getDescription());
                    nprFile.setNpr(nprId);
                    nprFile.setVersion(1);
                    nprFile.setSize(file.getSize());
                    nprFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    nprFile.setFileNo(autoNumber1);
                    nprFile.setFileType("FILE");
                    nprFile = nprFileRepository.save(nprFile);
                    nprFile.setParentObject(PLMObjectType.PLMNPR);
                    fileList.add(nprFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + nprId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (nprFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(nprId, nprFile.getId());
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + nprId + File.separator + nprFile.getId();
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
                PLMNprFile nprFile = new PLMNprFile();
                PLMNprFile existFile = null;
                if (fileId != 0) {
                    nprFile.setParentFile(fileId);
                    existFile = nprFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndNprAndLatestTrue(file.getName(), fileId, nprId);
                } else {
                    existFile = nprFileRepository.findByNprAndNameAndParentFileIsNullAndLatestTrue(nprId, file.getName());
                }
                if (existFile == null) {
                    nprFile.setName(file.getName());
                    nprFile.setDescription(file.getDescription());
                    String folderNumber = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    nprFile.setVersion(1);
                    nprFile.setSize(0L);
                    nprFile.setNpr(nprId);
                    nprFile.setFileNo(folderNumber);
                    nprFile.setFileType("FOLDER");
                    nprFile = nprFileRepository.save(nprFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + nprId;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(nprId, nprFile.getId());
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(nprFile);
                    copyFolderFiles(nprId, file.getParentObject(), file.getId(), nprFile.getId());
                }
            }
        }
        return fileList;
    }

    private void copyFolderFiles(Integer id, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        plmFiles.forEach(plmFile -> {
            PLMNprFile nprFile = new PLMNprFile();
            nprFile.setParentFile(parent);
            nprFile.setName(plmFile.getName());
            nprFile.setDescription(plmFile.getDescription());
            nprFile.setNpr(id);
            String folderNumber = null;
            if (plmFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                nprFile.setVersion(1);
                nprFile.setFileNo(folderNumber);
                nprFile.setSize(plmFile.getSize());
                nprFile.setFileType("FILE");
                nprFile = nprFileRepository.save(nprFile);
                nprFile.setParentObject(PLMObjectType.PLMNPR);
                plmFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmFile);

                String dir = "";
                if (nprFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, nprFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + nprFile.getId();
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
                nprFile.setVersion(1);
                nprFile.setSize(0L);
                nprFile.setFileNo(folderNumber);
                nprFile.setFileType("FOLDER");
                nprFile = nprFileRepository.save(nprFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(id, nprFile.getId());
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(id, objectType, plmFile.getId(), nprFile.getId());
            }
        });
    }

    public void undoCopiedNprFiles(Integer ecoId, List<PLMNprFile> nprFiles) {
        nprFiles.forEach(nprFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(ecoId, nprFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(nprFile.getId(), dir);
            nprFileRepository.delete(nprFile.getId());
        });
    }
}