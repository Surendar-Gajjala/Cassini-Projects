package com.cassinisys.plm.service.mes;

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
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.MBOMEvents;
import com.cassinisys.plm.model.mes.MESMBOMFile;
import com.cassinisys.plm.model.mes.MESMBOMRevision;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mes.MBOMFileRepository;
import com.cassinisys.plm.repo.mes.MESMBOMRevisionRepository;
import com.cassinisys.plm.repo.plm.FileDownloadHistoryRepository;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.plm.FileHelpers;
import com.cassinisys.plm.service.plm.ItemFileService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
 * Created by smukka on 17-05-2022.
 */
@Service
public class MBOMFileService {

    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private MBOMFileRepository mbomFileRepository;
    @Autowired
    private MESMBOMRevisionRepository mbomRevisionRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ObjectFileService objectFileService;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private ItemFileService itemFileService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private FileDownloadHistoryRepository fileDownloadHistoryRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UtilService utilService;
    @Autowired
    private FileHelpers fileHelpers;

    public List<MESMBOMFile> uploadFiles(Integer id, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<MESMBOMFile> uploadedFiles = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        List<MESMBOMFile> newFiles = new ArrayList<>();
        List<MESMBOMFile> versionedFiles = new ArrayList<>();
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
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
                    MESMBOMFile mbomFile = null;
                    if (folderId == 0) {
                        mbomFile = mbomFileRepository.findByMbomRevisionAndNameAndParentFileIsNullAndLatestTrue(id, name);
                    } else {
                        mbomFile = mbomFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer version = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (mbomFile != null) {
                        mbomFile.setLatest(false);
                        Integer oldVersion = mbomFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = mbomFile.getFileNo();
                        oldFile = mbomFile.getId();
                        versioned = true;
                        mbomFileRepository.save(mbomFile);
                    }
                    if (mbomFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    mbomFile = new MESMBOMFile();
                    mbomFile.setName(name);
                    mbomFile.setFileNo(autoNumber1);
                    mbomFile.setCreatedBy(login.getPerson().getId());
                    mbomFile.setModifiedBy(login.getPerson().getId());
                    mbomFile.setVersion(version);
                    mbomFile.setMbomRevision(id);
                    mbomFile.setSize(file.getSize());
                    mbomFile.setFileType("FILE");
                    if (folderId != 0) {
                        mbomFile.setParentFile(folderId);
                    }
                    mbomFile = mbomFileRepository.save(mbomFile);
                    if (mbomFile.getParentFile() != null) {
                        MESMBOMFile parent = mbomFileRepository.findOne(mbomFile.getParentFile());
                        parent.setModifiedDate(mbomFile.getModifiedDate());
                        parent = mbomFileRepository.save(parent);
                    }
                    if (mbomFile.getVersion() > 1) {
                        objectFileService.copyFileAttributes(oldFile, mbomFile.getId());
                    }
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + objectFileService.getParentFileSystemPath(id, folderId, PLMObjectType.MBOMREVISION);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + mbomFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploadedFiles.add(mbomFile);
                    if (versioned) {
                        versionedFiles.add(mbomFile);
                    } else {
                        newFiles.add(mbomFile);
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(id);
          /* App Events */
        if (newFiles.size() > 0) {

            applicationEventPublisher.publishEvent(new MBOMEvents.MbomFilesAddedEvent(mbomRevision, newFiles));
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new MBOMEvents.MbomFilesVersionedEvent(mbomRevision, versionedFiles));
        }
        return uploadedFiles;
    }

    public MESMBOMFile updateFileName(Integer id, String newFileName) throws IOException {
        MESMBOMFile file1 = mbomFileRepository.findOne(id);
        MESMBOMFile oldFile = (MESMBOMFile) Utils.cloneObject(file1, MESMBOMFile.class);
        String oldName = file1.getName();
        file1.setLatest(false);
        MESMBOMFile plmMbomFile = mbomFileRepository.save(file1);
        MESMBOMFile mbomFile = (MESMBOMFile) Utils.cloneObject(plmMbomFile, MESMBOMFile.class);
        if (mbomFile != null) {
            mbomFile.setId(null);
            mbomFile.setName(newFileName);
            mbomFile.setVersion(file1.getVersion() + 1);
            mbomFile.setLatest(true);
            mbomFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            mbomFile = mbomFileRepository.save(mbomFile);
            if (mbomFile.getParentFile() != null) {
                MESMBOMFile parent = mbomFileRepository.findOne(mbomFile.getParentFile());
                parent.setModifiedDate(mbomFile.getModifiedDate());
                parent = mbomFileRepository.save(parent);
            }
            objectFileService.copyFileAttributes(file1.getId(), mbomFile.getId());
            String dir = "";
            if (mbomFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(mbomFile.getMbomRevision(), id, PLMObjectType.MBOMREVISION);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + mbomFile.getMbomRevision();
            }
            dir = dir + File.separator + mbomFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + mbomFile.getMbomRevision() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;
            MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(mbomFile.getMbomRevision());

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            applicationEventPublisher.publishEvent(new MBOMEvents.MbomFileRenamedEvent(mbomRevision, oldFile, mbomFile, "Rename"));
        }
        return mbomFile;
    }

    public List<MESMBOMFile> replaceNprFiles(Integer mbomId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<MESMBOMFile> uploaded = new ArrayList<>();
        MESMBOMFile oldFile = null;
        Login login = sessionWrapper.getSession().getLogin();
        MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(mbomId);
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        MESMBOMFile plmNprFile = null;
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
                    MESMBOMFile mbomFile = null;
                    plmNprFile = mbomFileRepository.findOne(fileId);
                    String oldName = "";
                    if (plmNprFile != null && plmNprFile.getParentFile() != null) {
                        mbomFile = mbomFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        mbomFile = mbomFileRepository.findByMbomRevisionAndNameAndParentFileIsNullAndLatestTrue(mbomId, name);
                    }
                    if (plmNprFile != null) {
                        oldName = plmNprFile.getName();
                        plmNprFile.setLatest(false);
                        plmNprFile = mbomFileRepository.save(plmNprFile);
                    }
                    mbomFile = new MESMBOMFile();
                    mbomFile.setName(name);
                    if (plmNprFile != null && plmNprFile.getParentFile() != null) {
                        mbomFile.setParentFile(plmNprFile.getParentFile());
                    }
                    if (plmNprFile != null) {
                        mbomFile.setFileNo(plmNprFile.getFileNo());
                        mbomFile.setVersion(plmNprFile.getVersion() + 1);
                        mbomFile.setReplaceFileName(plmNprFile.getName() + " Replaced to " + name);
                        oldFile = JsonUtils.cloneEntity(plmNprFile, MESMBOMFile.class);
                    }
                    mbomFile.setCreatedBy(login.getPerson().getId());
                    mbomFile.setModifiedBy(login.getPerson().getId());
                    mbomFile.setMbomRevision(mbomId);
                    mbomFile.setSize(file.getSize());
                    mbomFile.setFileType("FILE");
                    mbomFile = mbomFileRepository.save(mbomFile);
                    if (mbomFile.getParentFile() != null) {
                        MESMBOMFile parent = mbomFileRepository.findOne(mbomFile.getParentFile());
                        parent.setModifiedDate(mbomFile.getModifiedDate());
                        parent = mbomFileRepository.save(parent);
                    }
                    if (plmNprFile != null) {
                        objectFileService.copyFileAttributes(plmNprFile.getId(), mbomFile.getId());
                    }
                    String dir = "";
                    if (plmNprFile != null && plmNprFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(mbomId, fileId, PLMObjectType.MBOMREVISION);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + mbomId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + mbomFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(mbomFile);
                    applicationEventPublisher.publishEvent(new MBOMEvents.MbomFileRenamedEvent(mbomRevision, plmNprFile, mbomFile, "Replace"));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    public void deleteNprFile(Integer mbomId, Integer id) {
        checkNotNull(id);
        MESMBOMFile mbomFile = mbomFileRepository.findOne(id);
        MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(mbomId);
        List<MESMBOMFile> nprFiles = mbomFileRepository.findByMbomRevisionAndFileNo(mbomId, mbomFile.getFileNo());
        for (MESMBOMFile nprFile1 : nprFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(mbomId, nprFile1.getId(), PLMObjectType.MBOMREVISION);
            fileSystemService.deleteDocumentFromDiskFolder(mbomFile.getId(), dir);
            mbomFileRepository.delete(nprFile1.getId());
        }
        if (mbomFile.getParentFile() != null) {
            MESMBOMFile parent = mbomFileRepository.findOne(mbomFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = mbomFileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new MBOMEvents.MbomFileDeletedEvent(mbomRevision, mbomFile));
    }

    public File getNprFile(Integer mbomId, Integer fileId) {
        checkNotNull(mbomId);
        checkNotNull(fileId);
        MESMBOMFile mbomFile = mbomFileRepository.findOne(fileId);
        if (mbomFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(mbomId, fileId, PLMObjectType.MBOMREVISION);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer mbomId, Integer fileId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        MESMBOMFile plmItemFile = mbomFileRepository.findOne(fileId);
        MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(mbomId);
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        applicationEventPublisher.publishEvent(new MBOMEvents.MbomFileDownloadedEvent(mbomRevision, plmItemFile));
        return plmFileDownloadHistory;
    }

    public List<MESMBOMFile> getAllFileVersionComments(Integer mbomId, Integer fileId, ObjectType objectType) {
        List<MESMBOMFile> itemFiles = new ArrayList<>();
        MESMBOMFile mbomFile = mbomFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(mbomFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(mbomFile.getId());
        if (comments.size() > 0) {
            mbomFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            mbomFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(mbomFile);
        List<MESMBOMFile> files = mbomFileRepository.findByMbomRevisionAndFileNoAndLatestFalseOrderByCreatedDateDesc(mbomFile.getMbomRevision(), mbomFile.getFileNo());
        if (files.size() > 0) {
            for (MESMBOMFile file : files) {
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

    public MESMBOMFile createNprFolder(Integer mbomId, MESMBOMFile mbomFile) {
        mbomFile.setId(null);
        String folderNumber = null;
        MESMBOMFile existFolderName = null;
        MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(mbomId);
        if (mbomFile.getParentFile() != null) {
            existFolderName = mbomFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndMbomRevisionAndLatestTrue(mbomFile.getName(), mbomFile.getParentFile(), mbomId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(mbomFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", mbomFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = mbomFileRepository.findByNameEqualsIgnoreCaseAndMbomRevisionAndLatestTrue(mbomFile.getName(), mbomId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", mbomFile.getName());
                throw new CassiniException(result);
            }
        }

        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        mbomFile.setMbomRevision(mbomId);
        mbomFile.setFileNo(folderNumber);
        mbomFile.setFileType("FOLDER");
        mbomFile = mbomFileRepository.save(mbomFile);
        if (mbomFile.getParentFile() != null) {
            MESMBOMFile parent = mbomFileRepository.findOne(mbomFile.getParentFile());
            parent.setModifiedDate(mbomFile.getModifiedDate());
            parent = mbomFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(mbomId, mbomFile.getId(), PLMObjectType.MBOMREVISION);
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new MBOMEvents.MbomFoldersAddedEvent(mbomRevision, mbomFile));
        return mbomFile;
    }

    public PLMFile moveNprFileToFolder(Integer id, MESMBOMFile plmNprFile) throws Exception {
        MESMBOMFile file = mbomFileRepository.findOne(plmNprFile.getId());
        MESMBOMFile existFile = (MESMBOMFile) Utils.cloneObject(file, MESMBOMFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + objectFileService.getParentFileSystemPath(existFile.getMbomRevision(), existFile.getId(), PLMObjectType.MBOMREVISION);
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getMbomRevision() + File.separator + existFile.getId();
        }
        if (plmNprFile.getParentFile() != null) {
            MESMBOMFile existItemFile = mbomFileRepository.findByParentFileAndNameAndLatestTrue(plmNprFile.getParentFile(), plmNprFile.getName());
            MESMBOMFile folder = mbomFileRepository.findOne(plmNprFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmNprFile = mbomFileRepository.save(plmNprFile);
            }
        } else {
            MESMBOMFile existItemFile = mbomFileRepository.findByMbomRevisionAndNameAndParentFileIsNullAndLatestTrue(plmNprFile.getMbomRevision(), plmNprFile.getName());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                plmNprFile = mbomFileRepository.save(plmNprFile);
            }
        }
        if (plmNprFile != null) {
            String dir = "";
            if (plmNprFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getParentFileSystemPath(plmNprFile.getMbomRevision(), plmNprFile.getId(), PLMObjectType.MBOMREVISION);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmNprFile.getMbomRevision() + File.separator + plmNprFile.getId();
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
            List<MESMBOMFile> oldVersionFiles = mbomFileRepository.findByMbomRevisionAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getMbomRevision(), existFile.getFileNo());
            for (MESMBOMFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(oldVersionFile.getMbomRevision(), oldVersionFile.getId(), PLMObjectType.MBOMREVISION);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getMbomRevision() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(plmNprFile.getMbomRevision(), plmNprFile.getId(), PLMObjectType.MBOMREVISION);
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmNprFile.getParentFile());
                oldVersionFile = mbomFileRepository.save(oldVersionFile);
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

    public void deleteFolder(Integer mbomId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(mbomId, folderId, PLMObjectType.MBOMREVISION);
        MESMBOMFile file = mbomFileRepository.findOne(folderId);
        List<MESMBOMFile> nprFiles = mbomFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) nprFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        mbomFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(mbomId);
        applicationEventPublisher.publishEvent(new MBOMEvents.MbomFoldersDeletedEvent(mbomRevision, file));
    }

    public void generateZipFile(Integer mbomId, HttpServletResponse response) throws FileNotFoundException, IOException {
        List<MESMBOMFile> plmNprFiles = mbomFileRepository.findByMbomRevisionAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(mbomId);
        ArrayList<String> fileList = new ArrayList<>();
        plmNprFiles.forEach(plmNprFile -> {
            File file = getNprFile(mbomId, plmNprFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = "NPR" + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "MFR",mbomId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional
    public MESMBOMFile getLatestUploadedNprFile(Integer mbomId, Integer fileId) {
        MESMBOMFile mbomFile = mbomFileRepository.findOne(fileId);
        MESMBOMFile plmNprFile = mbomFileRepository.findByMbomRevisionAndFileNoAndLatestTrue(mbomFile.getMbomRevision(), mbomFile.getFileNo());
        return plmNprFile;
    }

    public PLMFile updateFileDescription(Integer mbomId, Integer id, MESMBOMFile plmNprFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            file.setDescription(plmNprFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<MESMBOMFile> pasteFromClipboard(Integer mbomId, Integer fileId, List<PLMFile> files) {
        List<MESMBOMFile> fileList = new ArrayList<>();
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
                MESMBOMFile mbomFile = new MESMBOMFile();
                MESMBOMFile existFile = null;
                if (fileId != 0) {
                    mbomFile.setParentFile(fileId);
                    existFile = mbomFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndMbomRevisionAndLatestTrue(file.getName(), fileId, mbomId);
                } else {
                    existFile = mbomFileRepository.findByMbomRevisionAndNameAndParentFileIsNullAndLatestTrue(mbomId, file.getName());
                }
                if (existFile == null) {
                    mbomFile.setName(file.getName());
                    mbomFile.setDescription(file.getDescription());
                    mbomFile.setMbomRevision(mbomId);
                    mbomFile.setVersion(1);
                    mbomFile.setSize(file.getSize());
                    mbomFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    mbomFile.setFileNo(autoNumber1);
                    mbomFile.setFileType("FILE");
                    mbomFile = mbomFileRepository.save(mbomFile);
                    mbomFile.setParentObject(PLMObjectType.PLMNPR);
                    fileList.add(mbomFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + mbomId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (mbomFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + objectFileService.getParentFileSystemPath(mbomId, mbomFile.getId(), PLMObjectType.MBOMREVISION);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + mbomId + File.separator + mbomFile.getId();
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
                MESMBOMFile mesmbomFile = new MESMBOMFile();
                MESMBOMFile existFile = null;
                if (fileId != 0) {
                    mesmbomFile.setParentFile(fileId);
                    existFile = mbomFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndMbomRevisionAndLatestTrue(file.getName(), fileId, mbomId);
                } else {
                    existFile = mbomFileRepository.findByMbomRevisionAndNameAndParentFileIsNullAndLatestTrue(mbomId, file.getName());
                }
                if (existFile == null) {
                    mesmbomFile.setName(file.getName());
                    mesmbomFile.setDescription(file.getDescription());
                    String folderNumber = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    mesmbomFile.setVersion(1);
                    mesmbomFile.setSize(0L);
                    mesmbomFile.setMbomRevision(mbomId);
                    mesmbomFile.setFileNo(folderNumber);
                    mesmbomFile.setFileType("FOLDER");
                    mesmbomFile = mbomFileRepository.save(mesmbomFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + mbomId;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(mbomId, mesmbomFile.getId(), PLMObjectType.MBOMREVISION);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(mesmbomFile);
                    copyFolderFiles(mbomId, file.getParentObject(), file.getId(), mesmbomFile.getId());
                }
            }
        }
        return fileList;
    }

    private void copyFolderFiles(Integer id, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        plmFiles.forEach(plmFile -> {
            MESMBOMFile mesbopFile = new MESMBOMFile();
            mesbopFile.setParentFile(parent);
            mesbopFile.setName(plmFile.getName());
            mesbopFile.setDescription(plmFile.getDescription());
            mesbopFile.setMbomRevision(id);
            String folderNumber = null;
            if (plmFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                mesbopFile.setVersion(1);
                mesbopFile.setFileNo(folderNumber);
                mesbopFile.setSize(plmFile.getSize());
                mesbopFile.setFileType("FILE");
                mesbopFile = mbomFileRepository.save(mesbopFile);
                mesbopFile.setParentObject(PLMObjectType.MBOMREVISION);
                plmFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmFile);

                String dir = "";
                if (mesbopFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(id, mesbopFile.getId(), PLMObjectType.MBOMREVISION);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + mesbopFile.getId();
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
                mesbopFile.setVersion(1);
                mesbopFile.setSize(0L);
                mesbopFile.setFileNo(folderNumber);
                mesbopFile.setFileType("FOLDER");
                mesbopFile = mbomFileRepository.save(mesbopFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getParentFileSystemPath(id, mesbopFile.getId(), PLMObjectType.MBOMREVISION);
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(id, objectType, plmFile.getId(), mesbopFile.getId());
            }
        });
    }

    public void undoCopiedNprFiles(Integer ecoId, List<MESMBOMFile> nprFiles) {
        nprFiles.forEach(mbomFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(ecoId, mbomFile.getId(), PLMObjectType.MBOMREVISION);
            fileSystemService.deleteDocumentFromDiskFolder(mbomFile.getId(), dir);
            mbomFileRepository.delete(mbomFile.getId());
        });
    }


    @Transactional
    public MESMBOMFile copyMBOMFile(MESMBOMRevision oldItem, MESMBOMRevision newItem, MESMBOMFile mesmbomFile) {
        MESMBOMFile newItemFile = null;
        File file = getNprFile(oldItem.getId(), mesmbomFile.getId());
        if (file != null) {
            newItemFile = new MESMBOMFile();
            Login login = sessionWrapper.getSession().getLogin();
            newItemFile.setName(mesmbomFile.getName());
            newItemFile.setFileNo(mesmbomFile.getFileNo());
            newItemFile.setFileType(mesmbomFile.getFileType());
            newItemFile.setCreatedBy(login.getPerson().getId());
            newItemFile.setModifiedBy(login.getPerson().getId());
            newItemFile.setMbomRevision(newItem.getId());
            newItemFile.setVersion(mesmbomFile.getVersion());
            newItemFile.setSize(mesmbomFile.getSize());
            newItemFile.setLatest(mesmbomFile.getLatest());
            newItemFile = mbomFileRepository.save(newItemFile);
            if (newItemFile.getFileType().equals("FOLDER")) {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getParentFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.MBOMREVISION);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
            } else {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getReplaceFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.MBOMREVISION);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newItemFile.getId();
                saveFileToDisk(file, dir);
                saveOldVersionItemFiles(oldItem, newItem, mesmbomFile);
            }
        }
        saveItemFileChildren(oldItem, newItem, mesmbomFile, newItemFile);
        return newItemFile;
    }


    private void saveChildrenOldVersionItemFiles(MESMBOMRevision oldRevision, MESMBOMRevision newRevision, MESMBOMFile itemFile, MESMBOMFile plmItemFile) {
        List<MESMBOMFile> oldVersionFiles = mbomFileRepository.findByMbomRevisionAndFileNoAndLatestFalseOrderByCreatedDateDesc(oldRevision.getId(), itemFile.getFileNo());
        for (MESMBOMFile oldVersionFile : oldVersionFiles) {
            MESMBOMFile newItemFile = null;
            File file = getNprFile(oldRevision.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new MESMBOMFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setMbomRevision(newRevision.getId());
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile = mbomFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getReplaceFileSystemPath(newRevision.getId(), newItemFile.getId(), PLMObjectType.MBOMREVISION);
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
    private void saveItemFileChildren(MESMBOMRevision oldItem, MESMBOMRevision newItem, MESMBOMFile itemFile, MESMBOMFile plmItemFile) {
        List<MESMBOMFile> childrenFiles = mbomFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(itemFile.getId());
        for (MESMBOMFile childrenFile : childrenFiles) {
            MESMBOMFile newItemFile = null;
            File file = getNprFile(oldItem.getId(), childrenFile.getId());
            if (file != null) {
                newItemFile = new MESMBOMFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(childrenFile.getName());
                newItemFile.setFileNo(childrenFile.getFileNo());
                newItemFile.setFileType(childrenFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setMbomRevision(newItem.getId());
                newItemFile.setVersion(childrenFile.getVersion());
                newItemFile.setSize(childrenFile.getSize());
                newItemFile.setLatest(childrenFile.getLatest());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile = mbomFileRepository.save(newItemFile);
                if (newItemFile.getFileType().equals("FOLDER")) {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + objectFileService.getParentFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.MBOMREVISION);
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                } else {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + objectFileService.getReplaceFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.MBOMREVISION);
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

    private void saveOldVersionItemFiles(MESMBOMRevision oldItem, MESMBOMRevision newItem, MESMBOMFile mesmbomFile) {
        List<MESMBOMFile> oldVersionFiles = mbomFileRepository.findByMbomRevisionAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(oldItem.getId(), mesmbomFile.getFileNo());
        for (MESMBOMFile oldVersionFile : oldVersionFiles) {
            MESMBOMFile newItemFile = null;
            File file = getNprFile(oldItem.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new MESMBOMFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setMbomRevision(newItem.getId());
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile = mbomFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getReplaceFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.MBOMREVISION);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newItemFile.getId();
                saveFileToDisk(file, dir);
            }
        }
    }

}
