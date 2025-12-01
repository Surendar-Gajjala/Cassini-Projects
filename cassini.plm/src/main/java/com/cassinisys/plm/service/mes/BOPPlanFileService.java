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
import com.cassinisys.plm.event.BOPOperationEvents;
import com.cassinisys.plm.model.mes.MESBOPOperationFile;
import com.cassinisys.plm.model.mes.MESBOPRouteOperation;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mes.BOPOperationFileRepository;
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
public class BOPPlanFileService {

    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private BOPOperationFileRepository bopOperationFileRepository;
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

    public List<MESBOPOperationFile> uploadFiles(Integer id, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<MESBOPOperationFile> uploadedFiles = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        List<MESBOPOperationFile> newFiles = new ArrayList<>();
        List<MESBOPOperationFile> versionedFiles = new ArrayList<>();
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
                    MESBOPOperationFile bopFile = null;
                    if (folderId == 0) {
                        bopFile = bopOperationFileRepository.findByBopOperationAndNameAndParentFileIsNullAndLatestTrue(id, name);
                    } else {
                        bopFile = bopOperationFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer version = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (bopFile != null) {
                        bopFile.setLatest(false);
                        Integer oldVersion = bopFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = bopFile.getFileNo();
                        oldFile = bopFile.getId();
                        versioned = true;
                        bopOperationFileRepository.save(bopFile);
                    }
                    if (bopFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    bopFile = new MESBOPOperationFile();
                    bopFile.setName(name);
                    bopFile.setFileNo(autoNumber1);
                    bopFile.setCreatedBy(login.getPerson().getId());
                    bopFile.setModifiedBy(login.getPerson().getId());
                    bopFile.setVersion(version);
                    bopFile.setBopOperation(id);
                    bopFile.setSize(file.getSize());
                    bopFile.setFileType("FILE");
                    if (folderId != 0) {
                        bopFile.setParentFile(folderId);
                    }
                    bopFile = bopOperationFileRepository.save(bopFile);
                    if (bopFile.getParentFile() != null) {
                        MESBOPOperationFile parent = bopOperationFileRepository.findOne(bopFile.getParentFile());
                        parent.setModifiedDate(bopFile.getModifiedDate());
                        parent = bopOperationFileRepository.save(parent);
                    }
                    if (bopFile.getVersion() > 1) {
                        objectFileService.copyFileAttributes(oldFile, bopFile.getId());
                    }
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + objectFileService.getParentFileSystemPath(id, folderId, PLMObjectType.BOPROUTEOPERATION);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + bopFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploadedFiles.add(bopFile);
                    if (versioned) {
                        versionedFiles.add(bopFile);
                    } else {
                        newFiles.add(bopFile);
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
          /* App Events */
        if (newFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationFilesAddedEvent(id, newFiles));
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationFilesVersionedEvent(id, versionedFiles));
        }
        return uploadedFiles;
    }

    public MESBOPOperationFile updateFileName(Integer id, String newFileName) throws IOException {
        MESBOPOperationFile file1 = bopOperationFileRepository.findOne(id);
        file1.setLatest(false);
        MESBOPOperationFile mesBopOperationFile = bopOperationFileRepository.save(file1);
        MESBOPOperationFile bopFile = (MESBOPOperationFile) Utils.cloneObject(mesBopOperationFile, MESBOPOperationFile.class);
        if (bopFile != null) {
            bopFile.setId(null);
            bopFile.setName(newFileName);
            bopFile.setVersion(file1.getVersion() + 1);
            bopFile.setLatest(true);
            bopFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            bopFile = bopOperationFileRepository.save(bopFile);
            if (bopFile.getParentFile() != null) {
                MESBOPOperationFile parent = bopOperationFileRepository.findOne(bopFile.getParentFile());
                parent.setModifiedDate(bopFile.getModifiedDate());
                parent = bopOperationFileRepository.save(parent);
            }
            objectFileService.copyFileAttributes(file1.getId(), bopFile.getId());
            String dir = "";
            if (bopFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(bopFile.getBopOperation(), id, PLMObjectType.BOPROUTEOPERATION);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + bopFile.getBopOperation();
            }
            dir = dir + File.separator + bopFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + bopFile.getBopOperation() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationFileRenamedEvent(bopFile.getBopOperation(), mesBopOperationFile, bopFile, "Rename"));
        }
        return bopFile;
    }

    public List<MESBOPOperationFile> replaceBOPPlanFiles(Integer operationId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<MESBOPOperationFile> uploaded = new ArrayList<>();
        MESBOPOperationFile oldFile = null;
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        MESBOPOperationFile mesBopOperationFile = null;
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
                    MESBOPOperationFile bopFile = null;
                    mesBopOperationFile = bopOperationFileRepository.findOne(fileId);
                    String oldName = "";
                    if (mesBopOperationFile != null && mesBopOperationFile.getParentFile() != null) {
                        bopFile = bopOperationFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        bopFile = bopOperationFileRepository.findByBopOperationAndNameAndParentFileIsNullAndLatestTrue(operationId, name);
                    }
                    if (mesBopOperationFile != null) {
                        oldName = mesBopOperationFile.getName();
                        mesBopOperationFile.setLatest(false);
                        mesBopOperationFile = bopOperationFileRepository.save(mesBopOperationFile);
                    }
                    bopFile = new MESBOPOperationFile();
                    bopFile.setName(name);
                    if (mesBopOperationFile != null && mesBopOperationFile.getParentFile() != null) {
                        bopFile.setParentFile(mesBopOperationFile.getParentFile());
                    }
                    if (mesBopOperationFile != null) {
                        bopFile.setFileNo(mesBopOperationFile.getFileNo());
                        bopFile.setVersion(mesBopOperationFile.getVersion() + 1);
                        bopFile.setReplaceFileName(mesBopOperationFile.getName() + " Replaced to " + name);
                        oldFile = JsonUtils.cloneEntity(mesBopOperationFile, MESBOPOperationFile.class);
                    }
                    bopFile.setCreatedBy(login.getPerson().getId());
                    bopFile.setModifiedBy(login.getPerson().getId());
                    bopFile.setBopOperation(operationId);
                    bopFile.setSize(file.getSize());
                    bopFile.setFileType("FILE");
                    bopFile = bopOperationFileRepository.save(bopFile);
                    if (bopFile.getParentFile() != null) {
                        MESBOPOperationFile parent = bopOperationFileRepository.findOne(bopFile.getParentFile());
                        parent.setModifiedDate(bopFile.getModifiedDate());
                        parent = bopOperationFileRepository.save(parent);
                    }
                    if (mesBopOperationFile != null) {
                        objectFileService.copyFileAttributes(mesBopOperationFile.getId(), bopFile.getId());
                    }
                    String dir = "";
                    if (mesBopOperationFile != null && mesBopOperationFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(operationId, fileId, PLMObjectType.BOPROUTEOPERATION);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + operationId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + bopFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(bopFile);
                    applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationFileRenamedEvent(bopFile.getBopOperation(), mesBopOperationFile, bopFile, "Replace"));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    public void deleteBOPPlanFile(Integer operationId, Integer id) {
        checkNotNull(id);
        MESBOPOperationFile bopFile = bopOperationFileRepository.findOne(id);
        List<MESBOPOperationFile> nprFiles = bopOperationFileRepository.findByBopOperationAndFileNo(operationId, bopFile.getFileNo());
        for (MESBOPOperationFile nprFile1 : nprFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(operationId, nprFile1.getId(), PLMObjectType.BOPROUTEOPERATION);
            fileSystemService.deleteDocumentFromDiskFolder(bopFile.getId(), dir);
            bopOperationFileRepository.delete(nprFile1.getId());
        }
        if (bopFile.getParentFile() != null) {
            MESBOPOperationFile parent = bopOperationFileRepository.findOne(bopFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = bopOperationFileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationFileDeletedEvent(bopFile.getBopOperation(), bopFile));
    }

    public File getBOPPlanFile(Integer operationId, Integer fileId) {
        checkNotNull(operationId);
        checkNotNull(fileId);
        MESBOPOperationFile bopFile = bopOperationFileRepository.findOne(fileId);
        if (bopFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(operationId, fileId, PLMObjectType.BOPROUTEOPERATION);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer operationId, Integer fileId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        MESBOPOperationFile plmItemFile = bopOperationFileRepository.findOne(fileId);
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationFileDownloadedEvent(plmItemFile.getBopOperation(), plmItemFile));
        return plmFileDownloadHistory;
    }

    public List<MESBOPOperationFile> getAllFileVersionComments(Integer operationId, Integer fileId, ObjectType objectType) {
        List<MESBOPOperationFile> itemFiles = new ArrayList<>();
        MESBOPOperationFile bopFile = bopOperationFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(bopFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(bopFile.getId());
        if (comments.size() > 0) {
            bopFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            bopFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(bopFile);
        List<MESBOPOperationFile> files = bopOperationFileRepository.findByBopOperationAndFileNoAndLatestFalseOrderByCreatedDateDesc(bopFile.getBopOperation(), bopFile.getFileNo());
        if (files.size() > 0) {
            for (MESBOPOperationFile file : files) {
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

    public MESBOPOperationFile createBOPPlanFolder(Integer operationId, MESBOPOperationFile bopFile) {
        bopFile.setId(null);
        String folderNumber = null;
        MESBOPOperationFile existFolderName = null;
        if (bopFile.getParentFile() != null) {
            existFolderName = bopOperationFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndBopOperationAndLatestTrue(bopFile.getName(), bopFile.getParentFile(), operationId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(bopFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", bopFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = bopOperationFileRepository.findByNameEqualsIgnoreCaseAndBopOperationAndLatestTrue(bopFile.getName(), operationId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", bopFile.getName());
                throw new CassiniException(result);
            }
        }

        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        bopFile.setBopOperation(operationId);
        bopFile.setFileNo(folderNumber);
        bopFile.setFileType("FOLDER");
        bopFile = bopOperationFileRepository.save(bopFile);
        if (bopFile.getParentFile() != null) {
            MESBOPOperationFile parent = bopOperationFileRepository.findOne(bopFile.getParentFile());
            parent.setModifiedDate(bopFile.getModifiedDate());
            parent = bopOperationFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(operationId, bopFile.getId(), PLMObjectType.BOPROUTEOPERATION);
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationFoldersAddedEvent(bopFile.getBopOperation(), bopFile));
        return bopFile;
    }

    public PLMFile moveBOPPlanFileToFolder(Integer id, MESBOPOperationFile mesBopOperationFile) throws Exception {
        MESBOPOperationFile file = bopOperationFileRepository.findOne(mesBopOperationFile.getId());
        MESBOPOperationFile existFile = (MESBOPOperationFile) Utils.cloneObject(file, MESBOPOperationFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + objectFileService.getParentFileSystemPath(existFile.getBopOperation(), existFile.getId(), PLMObjectType.BOPROUTEOPERATION);
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getBopOperation() + File.separator + existFile.getId();
        }
        if (mesBopOperationFile.getParentFile() != null) {
            MESBOPOperationFile existItemFile = bopOperationFileRepository.findByParentFileAndNameAndLatestTrue(mesBopOperationFile.getParentFile(), mesBopOperationFile.getName());
            MESBOPOperationFile folder = bopOperationFileRepository.findOne(mesBopOperationFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                mesBopOperationFile = bopOperationFileRepository.save(mesBopOperationFile);
            }
        } else {
            MESBOPOperationFile existItemFile = bopOperationFileRepository.findByBopOperationAndNameAndParentFileIsNullAndLatestTrue(mesBopOperationFile.getBopOperation(), mesBopOperationFile.getName());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                mesBopOperationFile = bopOperationFileRepository.save(mesBopOperationFile);
            }
        }
        if (mesBopOperationFile != null) {
            String dir = "";
            if (mesBopOperationFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getParentFileSystemPath(mesBopOperationFile.getBopOperation(), mesBopOperationFile.getId(), PLMObjectType.BOPROUTEOPERATION);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + mesBopOperationFile.getBopOperation() + File.separator + mesBopOperationFile.getId();
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
            List<MESBOPOperationFile> oldVersionFiles = bopOperationFileRepository.findByBopOperationAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getBopOperation(), existFile.getFileNo());
            for (MESBOPOperationFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(oldVersionFile.getBopOperation(), oldVersionFile.getId(), PLMObjectType.BOPROUTEOPERATION);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getBopOperation() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(mesBopOperationFile.getBopOperation(), mesBopOperationFile.getId(), PLMObjectType.BOPROUTEOPERATION);
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(mesBopOperationFile.getParentFile());
                oldVersionFile = bopOperationFileRepository.save(oldVersionFile);
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
        return mesBopOperationFile;
    }

    public void deleteFolder(Integer operationId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(operationId, folderId, PLMObjectType.BOPROUTEOPERATION);
        MESBOPOperationFile file = bopOperationFileRepository.findOne(folderId);
        List<MESBOPOperationFile> nprFiles = bopOperationFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) nprFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        bopOperationFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationFoldersDeletedEvent(file.getBopOperation(), file));
    }

    public void generateZipFile(Integer operationId, HttpServletResponse response) throws FileNotFoundException, IOException {
        List<MESBOPOperationFile> plmNprFiles = bopOperationFileRepository.findByBopOperationAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(operationId);
        ArrayList<String> fileList = new ArrayList<>();
        plmNprFiles.forEach(mesBopOperationFile -> {
            File file = getBOPPlanFile(operationId, mesBopOperationFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = "NPR" + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "MFR",operationId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional
    public MESBOPOperationFile getLatestUploadedNprFile(Integer operationId, Integer fileId) {
        MESBOPOperationFile bopFile = bopOperationFileRepository.findOne(fileId);
        MESBOPOperationFile mesBopOperationFile = bopOperationFileRepository.findByBopOperationAndFileNoAndLatestTrue(bopFile.getBopOperation(), bopFile.getFileNo());
        return mesBopOperationFile;
    }

    public PLMFile updateFileDescription(Integer operationId, Integer id, MESBOPOperationFile mesBopOperationFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            file.setDescription(mesBopOperationFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<MESBOPOperationFile> pasteFromClipboard(Integer operationId, Integer fileId, List<PLMFile> files) {
        List<MESBOPOperationFile> fileList = new ArrayList<>();
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
                MESBOPOperationFile bopFile = new MESBOPOperationFile();
                MESBOPOperationFile existFile = null;
                if (fileId != 0) {
                    bopFile.setParentFile(fileId);
                    existFile = bopOperationFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndBopOperationAndLatestTrue(file.getName(), fileId, operationId);
                } else {
                    existFile = bopOperationFileRepository.findByBopOperationAndNameAndParentFileIsNullAndLatestTrue(operationId, file.getName());
                }
                if (existFile == null) {
                    bopFile.setName(file.getName());
                    bopFile.setDescription(file.getDescription());
                    bopFile.setBopOperation(operationId);
                    bopFile.setVersion(1);
                    bopFile.setSize(file.getSize());
                    bopFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    bopFile.setFileNo(autoNumber1);
                    bopFile.setFileType("FILE");
                    bopFile = bopOperationFileRepository.save(bopFile);
                    bopFile.setParentObject(PLMObjectType.BOPROUTEOPERATION);
                    fileList.add(bopFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + operationId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (bopFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + objectFileService.getParentFileSystemPath(operationId, bopFile.getId(), PLMObjectType.BOPROUTEOPERATION);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + operationId + File.separator + bopFile.getId();
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
                MESBOPOperationFile changeFile = new MESBOPOperationFile();
                MESBOPOperationFile existFile = null;
                if (fileId != 0) {
                    changeFile.setParentFile(fileId);
                    existFile = bopOperationFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndBopOperationAndLatestTrue(file.getName(), fileId, operationId);
                } else {
                    existFile = bopOperationFileRepository.findByBopOperationAndNameAndParentFileIsNullAndLatestTrue(operationId, file.getName());
                }
                if (existFile == null) {
                    changeFile.setName(file.getName());
                    changeFile.setDescription(file.getDescription());
                    String folderNumber = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    changeFile.setVersion(1);
                    changeFile.setSize(0L);
                    changeFile.setBopOperation(operationId);
                    changeFile.setFileNo(folderNumber);
                    changeFile.setFileType("FOLDER");
                    changeFile = bopOperationFileRepository.save(changeFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + operationId;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(operationId, changeFile.getId(), PLMObjectType.BOPROUTEOPERATION);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(changeFile);
                    copyFolderFiles(operationId, file.getParentObject(), file.getId(), changeFile.getId());
                }
            }
        }
        return fileList;
    }

    private void copyFolderFiles(Integer id, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        plmFiles.forEach(plmFile -> {
            MESBOPOperationFile operationFile = new MESBOPOperationFile();
            operationFile.setParentFile(parent);
            operationFile.setName(plmFile.getName());
            operationFile.setDescription(plmFile.getDescription());
            operationFile.setBopOperation(id);
            String folderNumber = null;
            if (plmFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                operationFile.setVersion(1);
                operationFile.setFileNo(folderNumber);
                operationFile.setSize(plmFile.getSize());
                operationFile.setFileType("FILE");
                operationFile = bopOperationFileRepository.save(operationFile);
                operationFile.setParentObject(PLMObjectType.BOPROUTEOPERATION);
                plmFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmFile);

                String dir = "";
                if (operationFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(id, operationFile.getId(), PLMObjectType.BOPROUTEOPERATION);
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + operationFile.getId();
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
                operationFile.setVersion(1);
                operationFile.setSize(0L);
                operationFile.setFileNo(folderNumber);
                operationFile.setFileType("FOLDER");
                operationFile = bopOperationFileRepository.save(operationFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getParentFileSystemPath(id, operationFile.getId(), PLMObjectType.BOPROUTEOPERATION);
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(id, objectType, plmFile.getId(), operationFile.getId());
            }
        });
    }

    public void undoCopiedBOPPlanFiles(Integer ecoId, List<MESBOPOperationFile> nprFiles) {
        nprFiles.forEach(bopFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(ecoId, bopFile.getId(), PLMObjectType.BOPROUTEOPERATION);
            fileSystemService.deleteDocumentFromDiskFolder(bopFile.getId(), dir);
            bopOperationFileRepository.delete(bopFile.getId());
        });
    }


    @Transactional
    public MESBOPOperationFile copyBOPPlanFile(MESBOPRouteOperation oldItem, MESBOPRouteOperation newItem, MESBOPOperationFile mesmbomFile) {
        MESBOPOperationFile newItemFile = null;
        File file = getBOPPlanFile(oldItem.getId(), mesmbomFile.getId());
        if (file != null) {
            newItemFile = new MESBOPOperationFile();
            Login login = sessionWrapper.getSession().getLogin();
            newItemFile.setName(mesmbomFile.getName());
            newItemFile.setFileNo(mesmbomFile.getFileNo());
            newItemFile.setFileType(mesmbomFile.getFileType());
            newItemFile.setCreatedBy(login.getPerson().getId());
            newItemFile.setModifiedBy(login.getPerson().getId());
            newItemFile.setBopOperation(newItem.getId());
            newItemFile.setVersion(mesmbomFile.getVersion());
            newItemFile.setSize(mesmbomFile.getSize());
            newItemFile.setLatest(mesmbomFile.getLatest());
            newItemFile = bopOperationFileRepository.save(newItemFile);
            if (newItemFile.getFileType().equals("FOLDER")) {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getParentFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPROUTEOPERATION);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
            } else {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getReplaceFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPROUTEOPERATION);
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


    private void saveChildrenOldVersionItemFiles(MESBOPRouteOperation oldRevision, MESBOPRouteOperation newRevision, MESBOPOperationFile itemFile, MESBOPOperationFile plmItemFile) {
        List<MESBOPOperationFile> oldVersionFiles = bopOperationFileRepository.findByBopOperationAndFileNoAndLatestFalseOrderByCreatedDateDesc(oldRevision.getId(), itemFile.getFileNo());
        for (MESBOPOperationFile oldVersionFile : oldVersionFiles) {
            MESBOPOperationFile newItemFile = null;
            File file = getBOPPlanFile(oldRevision.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new MESBOPOperationFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setBopOperation(newRevision.getId());
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile = bopOperationFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getReplaceFileSystemPath(newRevision.getId(), newItemFile.getId(), PLMObjectType.BOPROUTEOPERATION);
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
    private void saveItemFileChildren(MESBOPRouteOperation oldItem, MESBOPRouteOperation newItem, MESBOPOperationFile itemFile, MESBOPOperationFile plmItemFile) {
        List<MESBOPOperationFile> childrenFiles = bopOperationFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(itemFile.getId());
        for (MESBOPOperationFile childrenFile : childrenFiles) {
            MESBOPOperationFile newItemFile = null;
            File file = getBOPPlanFile(oldItem.getId(), childrenFile.getId());
            if (file != null) {
                newItemFile = new MESBOPOperationFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(childrenFile.getName());
                newItemFile.setFileNo(childrenFile.getFileNo());
                newItemFile.setFileType(childrenFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setBopOperation(newItem.getId());
                newItemFile.setVersion(childrenFile.getVersion());
                newItemFile.setSize(childrenFile.getSize());
                newItemFile.setLatest(childrenFile.getLatest());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile = bopOperationFileRepository.save(newItemFile);
                if (newItemFile.getFileType().equals("FOLDER")) {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + objectFileService.getParentFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPROUTEOPERATION);
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                } else {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + objectFileService.getReplaceFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPROUTEOPERATION);
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

    private void saveOldVersionItemFiles(MESBOPRouteOperation oldItem, MESBOPRouteOperation newItem, MESBOPOperationFile mesmbomFile) {
        List<MESBOPOperationFile> oldVersionFiles = bopOperationFileRepository.findByBopOperationAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(oldItem.getId(), mesmbomFile.getFileNo());
        for (MESBOPOperationFile oldVersionFile : oldVersionFiles) {
            MESBOPOperationFile newItemFile = null;
            File file = getBOPPlanFile(oldItem.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new MESBOPOperationFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setBopOperation(newItem.getId());
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile = bopOperationFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getReplaceFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPROUTEOPERATION);
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
