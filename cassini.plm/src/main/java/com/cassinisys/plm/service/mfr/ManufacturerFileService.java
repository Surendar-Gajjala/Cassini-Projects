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
import com.cassinisys.plm.event.ManufacturerEvents;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerFile;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mfr.ManufacturerFileRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.plm.FileDownloadHistoryRepository;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.plm.FileHelpers;
import com.cassinisys.plm.service.plm.ItemFileService;
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
public class ManufacturerFileService implements CrudService<PLMManufacturerFile, Integer>, PageableService<PLMManufacturerFile, Integer> {

    @Autowired
    private ManufacturerFileRepository manufacturerFileRepository;
    @Autowired
    private ManufacturerRepository mfrRepository;
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
    private ManufacturerRepository manufacturerRepository;
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
    public PLMManufacturerFile create(PLMManufacturerFile manufacturerFile) {
        checkNotNull(manufacturerFile);
        manufacturerFile.setId(null);
        manufacturerFile = manufacturerFileRepository.save(manufacturerFile);
        PLMManufacturer mfr = mfrRepository.findOne(manufacturerFile.getManufacturer());
        return manufacturerFile;
    }

    @Override
    public PLMManufacturerFile update(PLMManufacturerFile manufacturerFile) {
        PLMFile file = fileRepository.findOne(manufacturerFile.getId());
        PLMManufacturer mfr = mfrRepository.findOne(manufacturerFile.getManufacturer());
        PLMManufacturerFile manufacturerFile1 = manufacturerFileRepository.findOne(manufacturerFile.getId());
        if (!file.getLocked().equals(manufacturerFile.getLocked())) {
                /* App events */
            if (manufacturerFile.getLocked()) {
                applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerFileLockedEvent(mfr, manufacturerFile1));
            } else {
                applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerFileUnlockedEvent(mfr, manufacturerFile1));
            }
        }
        manufacturerFile1.setDescription(manufacturerFile.getDescription());
        manufacturerFile1.setLocked(manufacturerFile.getLocked());
        manufacturerFile1.setLockedBy(manufacturerFile.getLockedBy());
        manufacturerFile1.setLockedDate(manufacturerFile.getLockedDate());
        manufacturerFile = manufacturerFileRepository.save(manufacturerFile1);
        return manufacturerFile;
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        PLMManufacturerFile manufacturerFile = manufacturerFileRepository.findOne(id);
        if (manufacturerFile == null) {
            throw new ResourceNotFoundException();
        }
        manufacturerFileRepository.delete(id);
        PLMManufacturer mfr = mfrRepository.findOne(manufacturerFile.getManufacturer());
    }

    public PLMManufacturerFile updateFileName(Integer id, String newFileName) throws IOException {
        PLMManufacturerFile file1 = manufacturerFileRepository.findOne(id);
        PLMManufacturerFile oldFile = (PLMManufacturerFile) Utils.cloneObject(file1, PLMManufacturerFile.class);
        String oldName = file1.getName();
        file1.setLatest(false);
        PLMManufacturerFile plmManufacturerFile = manufacturerFileRepository.save(file1);
        PLMManufacturerFile manufacturerFile = (PLMManufacturerFile) Utils.cloneObject(plmManufacturerFile, PLMManufacturerFile.class);
        if (manufacturerFile != null) {
            manufacturerFile.setId(null);
            manufacturerFile.setName(newFileName);
            manufacturerFile.setVersion(file1.getVersion() + 1);
            manufacturerFile.setLatest(true);
            manufacturerFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            manufacturerFile = manufacturerFileRepository.save(manufacturerFile);
            if (manufacturerFile.getParentFile() != null) {
                PLMManufacturerFile parent = manufacturerFileRepository.findOne(manufacturerFile.getParentFile());
                parent.setModifiedDate(manufacturerFile.getModifiedDate());
                parent = manufacturerFileRepository.save(parent);
            }
            qualityFileService.copyFileAttributes(file1.getId(), manufacturerFile.getId());
            String dir = "";
            if (manufacturerFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(manufacturerFile.getManufacturer(), id);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + manufacturerFile.getManufacturer();
            }
            dir = dir + File.separator + manufacturerFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + manufacturerFile.getManufacturer() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            PLMManufacturer manufacturer = manufacturerRepository.findOne(manufacturerFile.getManufacturer());
            /* App Events */
            applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerFileRenamedEvent(manufacturer, oldFile, manufacturerFile, "Rename"));
        }
        return manufacturerFile;
    }

    public List<PLMManufacturerFile> replaceMfrFiles(Integer mfrId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMManufacturerFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        PLMManufacturerFile mfrFile = null;
        PLMManufacturerFile oldFile = null;
        PLMManufacturer manufacturer = manufacturerRepository.findOne(mfrId);
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        PLMManufacturerFile plmManufacturerFile = null;
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
                    PLMManufacturerFile manufacturerFile = null;
                    plmManufacturerFile = manufacturerFileRepository.findOne(fileId);
                    String oldName = "";
                    if (plmManufacturerFile != null && plmManufacturerFile.getParentFile() != null) {
                        manufacturerFile = manufacturerFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        manufacturerFile = manufacturerFileRepository.findByManufacturerAndNameAndParentFileIsNullAndLatestTrue(mfrId, name);
                    }
                    if (manufacturerFile != null) {
                        comments = commentRepository.findAllByObjectId(manufacturerFile.getId());
                    }
                    if (plmManufacturerFile != null) {
                        oldName = plmManufacturerFile.getName();
                        plmManufacturerFile.setLatest(false);
                        plmManufacturerFile = manufacturerFileRepository.save(plmManufacturerFile);
                    }
                    if (manufacturerFile != null) {
                        comments = commentRepository.findAllByObjectId(manufacturerFile.getId());
                    }
                    manufacturerFile = new PLMManufacturerFile();
                    manufacturerFile.setName(name);
                    if (plmManufacturerFile != null && plmManufacturerFile.getParentFile() != null) {
                        manufacturerFile.setParentFile(plmManufacturerFile.getParentFile());
                    }
                    if (plmManufacturerFile != null) {
                        manufacturerFile.setFileNo(plmManufacturerFile.getFileNo());
                        manufacturerFile.setVersion(plmManufacturerFile.getVersion() + 1);
                        manufacturerFile.setReplaceFileName(plmManufacturerFile.getName() + " Replaced to " + name);
                        oldFile = JsonUtils.cloneEntity(plmManufacturerFile, PLMManufacturerFile.class);
                    }
                    manufacturerFile.setCreatedBy(login.getPerson().getId());
                    manufacturerFile.setModifiedBy(login.getPerson().getId());
                    manufacturerFile.setManufacturer(mfrId);
                    manufacturerFile.setSize(file.getSize());
                    manufacturerFile.setFileType("FILE");
                    manufacturerFile = manufacturerFileRepository.save(manufacturerFile);
                    if (manufacturerFile.getParentFile() != null) {
                        PLMManufacturerFile parent = manufacturerFileRepository.findOne(manufacturerFile.getParentFile());
                        parent.setModifiedDate(manufacturerFile.getModifiedDate());
                        parent = manufacturerFileRepository.save(parent);
                    }
                    if (plmManufacturerFile != null) {
                        qualityFileService.copyFileAttributes(plmManufacturerFile.getId(), manufacturerFile.getId());
                    }
                    String dir = "";
                    if (plmManufacturerFile != null && plmManufacturerFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(mfrId, fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + mfrId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + manufacturerFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(manufacturerFile);
                      /* App Events */
                    applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerFileRenamedEvent(manufacturer, plmManufacturerFile, manufacturerFile, "Replace"));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    private String getReplaceFileSystemPath(Integer mfrPartId, Integer fileId) {
        String path = "";
        PLMManufacturerFile manufacturerPartFile = manufacturerFileRepository.findOne(fileId);
        if (manufacturerPartFile.getParentFile() != null) {
            path = utilService.visitParentFolder(mfrPartId, manufacturerPartFile.getParentFile(), path);
        } else {
            path = File.separator + mfrPartId;
        }
        return path;
    }

    public void deleteMfrFile(Integer mfrId, Integer id) {
        checkNotNull(id);
        PLMManufacturerFile manufacturerFile = manufacturerFileRepository.findOne(id);
        PLMManufacturer manufacturer = mfrRepository.findOne(mfrId);
        List<PLMManufacturerFile> manufacturerFiles = manufacturerFileRepository.findByManufacturerAndFileNo(mfrId, manufacturerFile.getFileNo());
        String fNames = null;
        for (PLMManufacturerFile manufacturerFile1 : manufacturerFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(mfrId, manufacturerFile1.getId());
            fileSystemService.deleteDocumentFromDiskFolder(manufacturerFile.getId(), dir);
            if (fNames == null) {
                fNames = manufacturerFile1.getName();
            } else {
                fNames = fNames + " , " + manufacturerFile1.getName();
            }
            manufacturerFileRepository.delete(manufacturerFile1.getId());
        }
        if (manufacturerFile.getParentFile() != null) {
            PLMManufacturerFile parent = manufacturerFileRepository.findOne(manufacturerFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = manufacturerFileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerFileDeletedEvent(manufacturer, manufacturerFile));
    }

    @Override
    public PLMManufacturerFile get(Integer id) {
        checkNotNull(id);
        PLMManufacturerFile manufacturerFile = manufacturerFileRepository.findOne(id);
        if (manufacturerFile == null) {
            throw new ResourceNotFoundException();
        }
        return manufacturerFile;
    }

    @Override
    public List<PLMManufacturerFile> getAll() {
        return manufacturerFileRepository.findAll();
    }

    public List<PLMManufacturerFile> getAllFileVersions(Integer mfrId, String name) {
        return manufacturerFileRepository.findAllByManufacturerAndNameOrderByCreatedDateDesc(mfrId, name);
    }

    @Override
    public Page<PLMManufacturerFile> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null)
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order("id")));
        return manufacturerFileRepository.findAll(pageable);
    }

    public List<PLMManufacturerFile> findByMfr(Integer manufacturer) {
        List<PLMManufacturerFile> manufacturerFiles = manufacturerFileRepository.findByManufacturerAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(manufacturer);
        manufacturerFiles.forEach(manufacturerFile -> {
            manufacturerFile.setParentObject(PLMObjectType.MANUFACTURER);
            if (manufacturerFile.getFileType().equals("FOLDER")) {
                manufacturerFile.setCount(manufacturerFileRepository.getChildrenCountByParentFileAndLatestTrue(manufacturerFile.getId()));
                manufacturerFile.setCount(manufacturerFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(manufacturerFile.getManufacturer(), manufacturerFile.getId()));
            }
        });
        return manufacturerFiles;
    }

    public List<PLMManufacturerFile> findByMfrLatest(Integer manufacturer) {
        List<PLMManufacturerFile> manufacturerFiles = manufacturerFileRepository.findByManufacturerAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(manufacturer);
        manufacturerFiles.forEach(manufacturerFile -> {
            manufacturerFile.setParentObject(PLMObjectType.MANUFACTURER);
            if (manufacturerFile.getFileType().equals("FOLDER")) {
                manufacturerFile.setCount(manufacturerFileRepository.getChildrenCountByParentFileAndLatestTrue(manufacturerFile.getId()));
            }
        });
        return manufacturerFiles;
    }

    public List<PLMManufacturerFile> uploadFiles(Integer mfrId, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMManufacturerFile> uploadedFiles = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        PLMManufacturer manufacturer = manufacturerRepository.findOne(mfrId);
        String[] fileExtension = null;
        List<PLMManufacturerFile> newFiles = new ArrayList<>();
        List<PLMManufacturerFile> versionedFiles = new ArrayList<>();
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
                    PLMManufacturerFile mfrFile = null;
                    if (folderId == 0) {
                        mfrFile = manufacturerFileRepository.findByManufacturerAndNameAndParentFileIsNullAndLatestTrue(mfrId, name);
                    } else {
                        mfrFile = manufacturerFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer version = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (mfrFile != null) {
                        mfrFile.setLatest(false);
                        Integer oldVersion = mfrFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = mfrFile.getFileNo();
                        oldFile = mfrFile.getId();
                /*mfrFile.setVersion(newVersion);*/
                        versioned = true;
                        manufacturerFileRepository.save(mfrFile);
                    }
                    if (mfrFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    mfrFile = new PLMManufacturerFile();
                    mfrFile.setName(name);
                    mfrFile.setFileNo(autoNumber1);
                    mfrFile.setCreatedBy(login.getPerson().getId());
                    mfrFile.setModifiedBy(login.getPerson().getId());
            /*mfrFile.setVersion(1);*/
                    mfrFile.setVersion(version);
                    mfrFile.setManufacturer(mfrId);
                    mfrFile.setSize(file.getSize());
                    mfrFile.setFileType("FILE");
                    if (folderId != 0) {
                        mfrFile.setParentFile(folderId);
                    }
                    mfrFile = manufacturerFileRepository.save(mfrFile);
                    if (mfrFile.getParentFile() != null) {
                        PLMManufacturerFile parent = manufacturerFileRepository.findOne(mfrFile.getParentFile());
                        parent.setModifiedDate(mfrFile.getModifiedDate());
                        parent = manufacturerFileRepository.save(parent);
                    }
                    if (mfrFile.getVersion() > 1) {
                        qualityFileService.copyFileAttributes(oldFile, mfrFile.getId());
                    }
                    if (fNames == null) {
                        fNames = mfrFile.getName();

                    } else {
                        fNames = fNames + "  , " + mfrFile.getName();
                    }
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + mfrId;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getParentFileSystemPath(mfrId, folderId);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + mfrFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploadedFiles.add(mfrFile);
                    if (versioned) {
                        versionedFiles.add(mfrFile);
                    } else {
                        newFiles.add(mfrFile);
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
          /* App Events */
        if (newFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerFilesAddedEvent(manufacturer, newFiles));
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerFilesVersionedEvent(manufacturer, versionedFiles));
        }
        return uploadedFiles;
    }

    public File getMfrFile(Integer mfrId, Integer fileId) {
        checkNotNull(mfrId);
        checkNotNull(fileId);
        PLMManufacturer mfr = mfrRepository.findOne(mfrId);
        if (mfr == null) {
            throw new ResourceNotFoundException();
        }
        PLMManufacturerFile mfrFile = manufacturerFileRepository.findOne(fileId);
        if (mfrFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(mfrId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<PLMManufacturerFile> findByMfrFilesName(Integer mfrId, String name) {
        return manufacturerFileRepository.findByManufacturerAndNameContainingIgnoreCaseAndLatestTrue(mfrId, name);
    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer mfrId, Integer fileId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMManufacturerFile plmItemFile = manufacturerFileRepository.findOne(fileId);
        PLMManufacturer mfr = mfrRepository.findOne(plmItemFile.getManufacturer());
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerFileDownloadedEvent(mfr, plmItemFile));
        return plmFileDownloadHistory;
    }

    public List<PLMManufacturerFile> getAllFileVersionComments(Integer mfrId, Integer fileId, ObjectType objectType) {
        List<PLMManufacturerFile> itemFiles = new ArrayList<>();
        PLMManufacturerFile mfrFile = manufacturerFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(mfrFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(mfrFile.getId());
        if (comments.size() > 0) {
            mfrFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            mfrFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(mfrFile);
        List<PLMManufacturerFile> files = manufacturerFileRepository.findByManufacturerAndFileNoAndLatestFalseOrderByCreatedDateDesc(mfrFile.getManufacturer(), mfrFile.getFileNo());
        if (files.size() > 0) {
            for (PLMManufacturerFile file : files) {
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

    public PLMManufacturerFile createMfrFolder(Integer mfrId, PLMManufacturerFile manufacturerFile) {
        manufacturerFile.setId(null);
        String folderNumber = null;
        PLMManufacturer manufacturer = manufacturerRepository.findOne(mfrId);
        PLMManufacturerFile existFolderName = null;
        if (manufacturerFile.getParentFile() != null) {
            existFolderName = manufacturerFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndManufacturerAndLatestTrue(manufacturerFile.getName(), manufacturerFile.getParentFile(), mfrId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(manufacturerFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", manufacturerFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = manufacturerFileRepository.findByNameEqualsIgnoreCaseAndManufacturerAndLatestTrue(manufacturerFile.getName(), mfrId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", manufacturerFile.getName());
                throw new CassiniException(result);
            }
        }

        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        manufacturerFile.setManufacturer(mfrId);
        manufacturerFile.setFileNo(folderNumber);
        manufacturerFile.setFileType("FOLDER");
        manufacturerFile = manufacturerFileRepository.save(manufacturerFile);
        if (manufacturerFile.getParentFile() != null) {
            PLMManufacturerFile parent = manufacturerFileRepository.findOne(manufacturerFile.getParentFile());
            parent.setModifiedDate(manufacturerFile.getModifiedDate());
            parent = manufacturerFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(mfrId, manufacturerFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerFoldersAddedEvent(manufacturer, manufacturerFile));
        return manufacturerFile;
    }

    private String getParentFileSystemPath(Integer mfrId, Integer fileId) {
        String path = "";
        PLMManufacturerFile manufacturerFile = manufacturerFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (manufacturerFile.getParentFile() != null) {
            path = utilService.visitParentFolder(mfrId, manufacturerFile.getParentFile(), path);
        } else {
            path = File.separator + mfrId + File.separator + manufacturerFile.getId();
        }
        return path;
    }

    public PLMFile moveMfrFileToFolder(Integer id, PLMManufacturerFile plmManufacturerFile) throws Exception {
        PLMManufacturerFile file = manufacturerFileRepository.findOne(plmManufacturerFile.getId());
        PLMManufacturerFile existFile = (PLMManufacturerFile) Utils.cloneObject(file, PLMManufacturerFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getManufacturer(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getManufacturer() + File.separator + existFile.getId();
        }
        if (plmManufacturerFile.getParentFile() != null) {
            PLMManufacturerFile existItemFile = manufacturerFileRepository.findByParentFileAndNameAndLatestTrue(plmManufacturerFile.getParentFile(), plmManufacturerFile.getName());
            PLMManufacturerFile folder = manufacturerFileRepository.findOne(plmManufacturerFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmManufacturerFile = manufacturerFileRepository.save(plmManufacturerFile);
            }
        } else {
            PLMManufacturerFile existItemFile = manufacturerFileRepository.findByManufacturerAndNameAndParentFileIsNullAndLatestTrue(plmManufacturerFile.getManufacturer(), plmManufacturerFile.getName());
            PLMManufacturer manufacturer = manufacturerRepository.findOne(plmManufacturerFile.getManufacturer());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                plmManufacturerFile = manufacturerFileRepository.save(plmManufacturerFile);
            }
        }
        if (plmManufacturerFile != null) {
            String dir = "";
            if (plmManufacturerFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(plmManufacturerFile.getManufacturer(), plmManufacturerFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmManufacturerFile.getManufacturer() + File.separator + plmManufacturerFile.getId();
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
            List<PLMManufacturerFile> oldVersionFiles = manufacturerFileRepository.findByManufacturerAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getManufacturer(), existFile.getFileNo());
            for (PLMManufacturerFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getManufacturer(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getManufacturer() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(plmManufacturerFile.getManufacturer(), plmManufacturerFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmManufacturerFile.getParentFile());
                oldVersionFile = manufacturerFileRepository.save(oldVersionFile);
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
        return plmManufacturerFile;
    }

    public List<PLMManufacturerFile> getMfrFolderChildren(Integer folderId) {
        List<PLMManufacturerFile> manufacturerFiles = manufacturerFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(folderId);
        manufacturerFiles.forEach(manufacturerFile -> {
            manufacturerFile.setParentObject(PLMObjectType.MANUFACTURER);
            if (manufacturerFile.getFileType().equals("FOLDER")) {
                manufacturerFile.setCount(manufacturerFileRepository.getChildrenCountByParentFileAndLatestTrue(manufacturerFile.getId()));
                manufacturerFile.setCount(manufacturerFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(manufacturerFile.getManufacturer(), manufacturerFile.getId()));
            }
        });
        return manufacturerFiles;
    }

    public void deleteFolder(Integer mfrId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(mfrId, folderId);
        PLMManufacturerFile file = manufacturerFileRepository.findOne(folderId);
        PLMManufacturer mfr = mfrRepository.findOne(mfrId);
        List<PLMManufacturerFile> manufacturerFiles = manufacturerFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) manufacturerFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        manufacturerFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMManufacturerFile parent = manufacturerFileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = manufacturerFileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerFoldersDeletedEvent(mfr, file));
    }

    public void generateZipFile(Integer mfrId, HttpServletResponse response) throws FileNotFoundException, IOException {
        PLMManufacturer plmManufacturer = manufacturerRepository.findOne(mfrId);
        List<PLMManufacturerFile> plmManufacturerFiles = manufacturerFileRepository.findByManufacturerAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(mfrId);
        ArrayList<String> fileList = new ArrayList<>();
        plmManufacturerFiles.forEach(plmManufacturerFile -> {
            File file = getMfrFile(mfrId, plmManufacturerFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = plmManufacturer.getName() + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "MFR",mfrId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional
    public PLMManufacturerFile getLatestUploadedMfrFile(Integer mfrId, Integer fileId) {
        PLMManufacturerFile manufacturerFile = manufacturerFileRepository.findOne(fileId);
        PLMManufacturerFile plmManufacturerFile = manufacturerFileRepository.findByManufacturerAndFileNoAndLatestTrue(manufacturerFile.getManufacturer(), manufacturerFile.getFileNo());
        return plmManufacturerFile;
    }

    public PLMFile updateFileDescription(Integer mfrId, Integer id, PLMManufacturerFile plmManufacturerFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            file.setDescription(plmManufacturerFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<PLMManufacturerFile> pasteFromClipboard(Integer mfrId, Integer fileId, List<PLMFile> files) {
        List<PLMManufacturerFile> fileList = new ArrayList<>();
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
                PLMManufacturerFile manufacturerFile = new PLMManufacturerFile();
                PLMManufacturerFile existFile = null;
                if (fileId != 0) {
                    manufacturerFile.setParentFile(fileId);
                    existFile = manufacturerFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndManufacturerAndLatestTrue(file.getName(), fileId, mfrId);
                } else {
                    existFile = manufacturerFileRepository.findByManufacturerAndNameAndParentFileIsNullAndLatestTrue(mfrId, file.getName());
                }
                if (existFile == null) {
                    manufacturerFile.setName(file.getName());
                    manufacturerFile.setDescription(file.getDescription());
                    manufacturerFile.setManufacturer(mfrId);
                    manufacturerFile.setVersion(1);
                    manufacturerFile.setSize(file.getSize());
                    manufacturerFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    manufacturerFile.setFileNo(autoNumber1);
                    manufacturerFile.setFileType("FILE");
                    manufacturerFile = manufacturerFileRepository.save(manufacturerFile);
                    manufacturerFile.setParentObject(PLMObjectType.MANUFACTURER);
                    fileList.add(manufacturerFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + mfrId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (manufacturerFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(mfrId, manufacturerFile.getId());
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + mfrId + File.separator + manufacturerFile.getId();
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
                PLMManufacturerFile manufacturerFile = new PLMManufacturerFile();
                PLMManufacturerFile existFile = null;
                if (fileId != 0) {
                    manufacturerFile.setParentFile(fileId);
                    existFile = manufacturerFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndManufacturerAndLatestTrue(file.getName(), fileId, mfrId);
                } else {
                    existFile = manufacturerFileRepository.findByManufacturerAndNameAndParentFileIsNullAndLatestTrue(mfrId, file.getName());
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
                    manufacturerFile.setManufacturer(mfrId);
                    manufacturerFile.setFileNo(folderNumber);
                    manufacturerFile.setFileType("FOLDER");
                    manufacturerFile = manufacturerFileRepository.save(manufacturerFile);
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
            PLMManufacturerFile manufacturerFile = new PLMManufacturerFile();
            manufacturerFile.setParentFile(parent);
            manufacturerFile.setName(plmFile.getName());
            manufacturerFile.setDescription(plmFile.getDescription());
            manufacturerFile.setManufacturer(id);
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
                manufacturerFile = manufacturerFileRepository.save(manufacturerFile);
                manufacturerFile.setParentObject(PLMObjectType.MANUFACTURER);
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
                manufacturerFile = manufacturerFileRepository.save(manufacturerFile);

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

    public void undoCopiedMfrFiles(Integer ecoId, List<PLMManufacturerFile> manufacturerFiles) {
        manufacturerFiles.forEach(manufacturerFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(ecoId, manufacturerFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(manufacturerFile.getId(), dir);
            manufacturerFileRepository.delete(manufacturerFile.getId());
        });
    }
}