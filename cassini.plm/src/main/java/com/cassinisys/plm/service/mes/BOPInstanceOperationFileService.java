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
import com.cassinisys.plm.event.BOPInstanceOperationEvents;
import com.cassinisys.plm.model.mes.MESBOPInstanceOperationFile;
import com.cassinisys.plm.model.mes.MESBOPInstanceOperationFile;
import com.cassinisys.plm.model.mes.MESBOPRouteOperation;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mes.BOPInstanceOperationFileRepository;
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
public class BOPInstanceOperationFileService {

    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private BOPInstanceOperationFileRepository bopInstanceOperationFileRepository;
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

    public List<MESBOPInstanceOperationFile> uploadFiles(Integer id, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<MESBOPInstanceOperationFile> uploadedFiles = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        List<MESBOPInstanceOperationFile> newFiles = new ArrayList<>();
        List<MESBOPInstanceOperationFile> versionedFiles = new ArrayList<>();
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
                    MESBOPInstanceOperationFile bopFile = null;
                    if (folderId == 0) {
                        bopFile = bopInstanceOperationFileRepository.findByBopOperationAndNameAndParentFileIsNullAndLatestTrue(id, name);
                    } else {
                        bopFile = bopInstanceOperationFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
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
                        bopInstanceOperationFileRepository.save(bopFile);
                    }
                    if (bopFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    bopFile = new MESBOPInstanceOperationFile();
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
                    bopFile = bopInstanceOperationFileRepository.save(bopFile);
                    if (bopFile.getParentFile() != null) {
                        MESBOPInstanceOperationFile parent = bopInstanceOperationFileRepository.findOne(bopFile.getParentFile());
                        parent.setModifiedDate(bopFile.getModifiedDate());
                        parent = bopInstanceOperationFileRepository.save(parent);
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
                                "filesystem" + objectFileService.getParentFileSystemPath(id, folderId, PLMObjectType.BOPINSTANCEROUTEOPERATION);
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
            applicationEventPublisher.publishEvent(new BOPInstanceOperationEvents.BOPOperationFilesAddedEvent(id, newFiles));
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new BOPInstanceOperationEvents.BOPOperationFilesVersionedEvent(id, versionedFiles));
        }
        return uploadedFiles;
    }

    public MESBOPInstanceOperationFile updateFileName(Integer id, String newFileName) throws IOException {
        MESBOPInstanceOperationFile file1 = bopInstanceOperationFileRepository.findOne(id);
        file1.setLatest(false);
        MESBOPInstanceOperationFile mESBOPInstanceOperationFile = bopInstanceOperationFileRepository.save(file1);
        MESBOPInstanceOperationFile bopFile = (MESBOPInstanceOperationFile) Utils.cloneObject(mESBOPInstanceOperationFile, MESBOPInstanceOperationFile.class);
        if (bopFile != null) {
            bopFile.setId(null);
            bopFile.setName(newFileName);
            bopFile.setVersion(file1.getVersion() + 1);
            bopFile.setLatest(true);
            bopFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            bopFile = bopInstanceOperationFileRepository.save(bopFile);
            if (bopFile.getParentFile() != null) {
                MESBOPInstanceOperationFile parent = bopInstanceOperationFileRepository.findOne(bopFile.getParentFile());
                parent.setModifiedDate(bopFile.getModifiedDate());
                parent = bopInstanceOperationFileRepository.save(parent);
            }
            objectFileService.copyFileAttributes(file1.getId(), bopFile.getId());
            String dir = "";
            if (bopFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(bopFile.getBopOperation(), id, PLMObjectType.BOPINSTANCEROUTEOPERATION);
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
            applicationEventPublisher.publishEvent(new BOPInstanceOperationEvents.BOPOperationFileRenamedEvent(bopFile.getBopOperation(), mESBOPInstanceOperationFile, bopFile, "Rename"));
        }
        return bopFile;
    }

    public List<MESBOPInstanceOperationFile> replaceBOPPlanFiles(Integer operationId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<MESBOPInstanceOperationFile> uploaded = new ArrayList<>();
        MESBOPInstanceOperationFile oldFile = null;
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        MESBOPInstanceOperationFile MESBOPInstanceOperationFile = null;
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
                    MESBOPInstanceOperationFile bopFile = null;
                    MESBOPInstanceOperationFile = bopInstanceOperationFileRepository.findOne(fileId);
                    String oldName = "";
                    if (MESBOPInstanceOperationFile != null && MESBOPInstanceOperationFile.getParentFile() != null) {
                        bopFile = bopInstanceOperationFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        bopFile = bopInstanceOperationFileRepository.findByBopOperationAndNameAndParentFileIsNullAndLatestTrue(operationId, name);
                    }
                    if (MESBOPInstanceOperationFile != null) {
                        oldName = MESBOPInstanceOperationFile.getName();
                        MESBOPInstanceOperationFile.setLatest(false);
                        MESBOPInstanceOperationFile = bopInstanceOperationFileRepository.save(MESBOPInstanceOperationFile);
                    }
                    bopFile = new MESBOPInstanceOperationFile();
                    bopFile.setName(name);
                    if (MESBOPInstanceOperationFile != null && MESBOPInstanceOperationFile.getParentFile() != null) {
                        bopFile.setParentFile(MESBOPInstanceOperationFile.getParentFile());
                    }
                    if (MESBOPInstanceOperationFile != null) {
                        bopFile.setFileNo(MESBOPInstanceOperationFile.getFileNo());
                        bopFile.setVersion(MESBOPInstanceOperationFile.getVersion() + 1);
                        bopFile.setReplaceFileName(MESBOPInstanceOperationFile.getName() + " Replaced to " + name);
                        oldFile = JsonUtils.cloneEntity(MESBOPInstanceOperationFile, MESBOPInstanceOperationFile.class);
                    }
                    bopFile.setCreatedBy(login.getPerson().getId());
                    bopFile.setModifiedBy(login.getPerson().getId());
                    bopFile.setBopOperation(operationId);
                    bopFile.setSize(file.getSize());
                    bopFile.setFileType("FILE");
                    bopFile = bopInstanceOperationFileRepository.save(bopFile);
                    if (bopFile.getParentFile() != null) {
                        MESBOPInstanceOperationFile parent = bopInstanceOperationFileRepository.findOne(bopFile.getParentFile());
                        parent.setModifiedDate(bopFile.getModifiedDate());
                        parent = bopInstanceOperationFileRepository.save(parent);
                    }
                    if (MESBOPInstanceOperationFile != null) {
                        objectFileService.copyFileAttributes(MESBOPInstanceOperationFile.getId(), bopFile.getId());
                    }
                    String dir = "";
                    if (MESBOPInstanceOperationFile != null && MESBOPInstanceOperationFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(operationId, fileId, PLMObjectType.BOPINSTANCEROUTEOPERATION);
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
                    applicationEventPublisher.publishEvent(new BOPInstanceOperationEvents.BOPOperationFileRenamedEvent(bopFile.getBopOperation(), MESBOPInstanceOperationFile, bopFile, "Replace"));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    public void deleteBOPPlanFile(Integer operationId, Integer id) {
        checkNotNull(id);
        MESBOPInstanceOperationFile bopFile = bopInstanceOperationFileRepository.findOne(id);
        List<MESBOPInstanceOperationFile> nprFiles = bopInstanceOperationFileRepository.findByBopOperationAndFileNo(operationId, bopFile.getFileNo());
        for (MESBOPInstanceOperationFile nprFile1 : nprFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(operationId, nprFile1.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
            fileSystemService.deleteDocumentFromDiskFolder(bopFile.getId(), dir);
            bopInstanceOperationFileRepository.delete(nprFile1.getId());
        }
        if (bopFile.getParentFile() != null) {
            MESBOPInstanceOperationFile parent = bopInstanceOperationFileRepository.findOne(bopFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = bopInstanceOperationFileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new BOPInstanceOperationEvents.BOPOperationFileDeletedEvent(bopFile.getBopOperation(), bopFile));
    }

    public File getBOPPlanFile(Integer operationId, Integer fileId) {
        checkNotNull(operationId);
        checkNotNull(fileId);
        MESBOPInstanceOperationFile bopFile = bopInstanceOperationFileRepository.findOne(fileId);
        if (bopFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(operationId, fileId, PLMObjectType.BOPINSTANCEROUTEOPERATION);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer operationId, Integer fileId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        MESBOPInstanceOperationFile plmItemFile = bopInstanceOperationFileRepository.findOne(fileId);
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        applicationEventPublisher.publishEvent(new BOPInstanceOperationEvents.BOPOperationFileDownloadedEvent(plmItemFile.getBopOperation(), plmItemFile));
        return plmFileDownloadHistory;
    }

    public List<MESBOPInstanceOperationFile> getAllFileVersionComments(Integer operationId, Integer fileId, ObjectType objectType) {
        List<MESBOPInstanceOperationFile> itemFiles = new ArrayList<>();
        MESBOPInstanceOperationFile bopFile = bopInstanceOperationFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(bopFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(bopFile.getId());
        if (comments.size() > 0) {
            bopFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            bopFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(bopFile);
        List<MESBOPInstanceOperationFile> files = bopInstanceOperationFileRepository.findByBopOperationAndFileNoAndLatestFalseOrderByCreatedDateDesc(bopFile.getBopOperation(), bopFile.getFileNo());
        if (files.size() > 0) {
            for (MESBOPInstanceOperationFile file : files) {
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

    public MESBOPInstanceOperationFile createBOPPlanFolder(Integer operationId, MESBOPInstanceOperationFile bopFile) {
        bopFile.setId(null);
        String folderNumber = null;
        MESBOPInstanceOperationFile existFolderName = null;
        if (bopFile.getParentFile() != null) {
            existFolderName = bopInstanceOperationFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndBopOperationAndLatestTrue(bopFile.getName(), bopFile.getParentFile(), operationId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(bopFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", bopFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = bopInstanceOperationFileRepository.findByNameEqualsIgnoreCaseAndBopOperationAndLatestTrue(bopFile.getName(), operationId);
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
        bopFile = bopInstanceOperationFileRepository.save(bopFile);
        if (bopFile.getParentFile() != null) {
            MESBOPInstanceOperationFile parent = bopInstanceOperationFileRepository.findOne(bopFile.getParentFile());
            parent.setModifiedDate(bopFile.getModifiedDate());
            parent = bopInstanceOperationFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(operationId, bopFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new BOPInstanceOperationEvents.BOPOperationFoldersAddedEvent(bopFile.getBopOperation(), bopFile));
        return bopFile;
    }

    public PLMFile moveBOPPlanFileToFolder(Integer id, MESBOPInstanceOperationFile MESBOPInstanceOperationFile) throws Exception {
        MESBOPInstanceOperationFile file = bopInstanceOperationFileRepository.findOne(MESBOPInstanceOperationFile.getId());
        MESBOPInstanceOperationFile existFile = (MESBOPInstanceOperationFile) Utils.cloneObject(file, MESBOPInstanceOperationFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + objectFileService.getParentFileSystemPath(existFile.getBopOperation(), existFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getBopOperation() + File.separator + existFile.getId();
        }
        if (MESBOPInstanceOperationFile.getParentFile() != null) {
            MESBOPInstanceOperationFile existItemFile = bopInstanceOperationFileRepository.findByParentFileAndNameAndLatestTrue(MESBOPInstanceOperationFile.getParentFile(), MESBOPInstanceOperationFile.getName());
            MESBOPInstanceOperationFile folder = bopInstanceOperationFileRepository.findOne(MESBOPInstanceOperationFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                MESBOPInstanceOperationFile = bopInstanceOperationFileRepository.save(MESBOPInstanceOperationFile);
            }
        } else {
            MESBOPInstanceOperationFile existItemFile = bopInstanceOperationFileRepository.findByBopOperationAndNameAndParentFileIsNullAndLatestTrue(MESBOPInstanceOperationFile.getBopOperation(), MESBOPInstanceOperationFile.getName());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                MESBOPInstanceOperationFile = bopInstanceOperationFileRepository.save(MESBOPInstanceOperationFile);
            }
        }
        if (MESBOPInstanceOperationFile != null) {
            String dir = "";
            if (MESBOPInstanceOperationFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getParentFileSystemPath(MESBOPInstanceOperationFile.getBopOperation(), MESBOPInstanceOperationFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + MESBOPInstanceOperationFile.getBopOperation() + File.separator + MESBOPInstanceOperationFile.getId();
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
            List<MESBOPInstanceOperationFile> oldVersionFiles = bopInstanceOperationFileRepository.findByBopOperationAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getBopOperation(), existFile.getFileNo());
            for (MESBOPInstanceOperationFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(oldVersionFile.getBopOperation(), oldVersionFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getBopOperation() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(MESBOPInstanceOperationFile.getBopOperation(), MESBOPInstanceOperationFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(MESBOPInstanceOperationFile.getParentFile());
                oldVersionFile = bopInstanceOperationFileRepository.save(oldVersionFile);
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
        return MESBOPInstanceOperationFile;
    }

    public void deleteFolder(Integer operationId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(operationId, folderId, PLMObjectType.BOPINSTANCEROUTEOPERATION);
        MESBOPInstanceOperationFile file = bopInstanceOperationFileRepository.findOne(folderId);
        List<MESBOPInstanceOperationFile> nprFiles = bopInstanceOperationFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) nprFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        bopInstanceOperationFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new BOPInstanceOperationEvents.BOPOperationFoldersDeletedEvent(file.getBopOperation(), file));
    }

    public void generateZipFile(Integer operationId, HttpServletResponse response) throws FileNotFoundException, IOException {
        List<MESBOPInstanceOperationFile> plmNprFiles = bopInstanceOperationFileRepository.findByBopOperationAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(operationId);
        ArrayList<String> fileList = new ArrayList<>();
        plmNprFiles.forEach(MESBOPInstanceOperationFile -> {
            File file = getBOPPlanFile(operationId, MESBOPInstanceOperationFile.getId());
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
    public MESBOPInstanceOperationFile getLatestUploadedNprFile(Integer operationId, Integer fileId) {
        MESBOPInstanceOperationFile bopFile = bopInstanceOperationFileRepository.findOne(fileId);
        MESBOPInstanceOperationFile MESBOPInstanceOperationFile = bopInstanceOperationFileRepository.findByBopOperationAndFileNoAndLatestTrue(bopFile.getBopOperation(), bopFile.getFileNo());
        return MESBOPInstanceOperationFile;
    }

    public PLMFile updateFileDescription(Integer operationId, Integer id, MESBOPInstanceOperationFile MESBOPInstanceOperationFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            file.setDescription(MESBOPInstanceOperationFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<MESBOPInstanceOperationFile> pasteFromClipboard(Integer operationId, Integer fileId, List<PLMFile> files) {
        List<MESBOPInstanceOperationFile> fileList = new ArrayList<>();
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
                MESBOPInstanceOperationFile bopFile = new MESBOPInstanceOperationFile();
                MESBOPInstanceOperationFile existFile = null;
                if (fileId != 0) {
                    bopFile.setParentFile(fileId);
                    existFile = bopInstanceOperationFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndBopOperationAndLatestTrue(file.getName(), fileId, operationId);
                } else {
                    existFile = bopInstanceOperationFileRepository.findByBopOperationAndNameAndParentFileIsNullAndLatestTrue(operationId, file.getName());
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
                    bopFile = bopInstanceOperationFileRepository.save(bopFile);
                    bopFile.setParentObject(PLMObjectType.BOPINSTANCEROUTEOPERATION);
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
                                "filesystem" + File.separator + objectFileService.getParentFileSystemPath(operationId, bopFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
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
                MESBOPInstanceOperationFile changeFile = new MESBOPInstanceOperationFile();
                MESBOPInstanceOperationFile existFile = null;
                if (fileId != 0) {
                    changeFile.setParentFile(fileId);
                    existFile = bopInstanceOperationFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndBopOperationAndLatestTrue(file.getName(), fileId, operationId);
                } else {
                    existFile = bopInstanceOperationFileRepository.findByBopOperationAndNameAndParentFileIsNullAndLatestTrue(operationId, file.getName());
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
                    changeFile = bopInstanceOperationFileRepository.save(changeFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + operationId;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(operationId, changeFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
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
            MESBOPInstanceOperationFile operationFile = new MESBOPInstanceOperationFile();
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
                operationFile = bopInstanceOperationFileRepository.save(operationFile);
                operationFile.setParentObject(PLMObjectType.BOPINSTANCEROUTEOPERATION);
                plmFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmFile);

                String dir = "";
                if (operationFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(id, operationFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
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
                operationFile = bopInstanceOperationFileRepository.save(operationFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getParentFileSystemPath(id, operationFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(id, objectType, plmFile.getId(), operationFile.getId());
            }
        });
    }

    public void undoCopiedBOPPlanFiles(Integer ecoId, List<MESBOPInstanceOperationFile> nprFiles) {
        nprFiles.forEach(bopFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(ecoId, bopFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
            fileSystemService.deleteDocumentFromDiskFolder(bopFile.getId(), dir);
            bopInstanceOperationFileRepository.delete(bopFile.getId());
        });
    }


    @Transactional
    public MESBOPInstanceOperationFile copyBOPPlanFile(MESBOPRouteOperation oldItem, MESBOPRouteOperation newItem, MESBOPInstanceOperationFile mesmbomFile) {
        MESBOPInstanceOperationFile newItemFile = null;
        File file = getBOPPlanFile(oldItem.getId(), mesmbomFile.getId());
        if (file != null) {
            newItemFile = new MESBOPInstanceOperationFile();
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
            newItemFile = bopInstanceOperationFileRepository.save(newItemFile);
            if (newItemFile.getFileType().equals("FOLDER")) {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getParentFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
            } else {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getReplaceFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
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


    private void saveChildrenOldVersionItemFiles(MESBOPRouteOperation oldRevision, MESBOPRouteOperation newRevision, MESBOPInstanceOperationFile itemFile, MESBOPInstanceOperationFile plmItemFile) {
        List<MESBOPInstanceOperationFile> oldVersionFiles = bopInstanceOperationFileRepository.findByBopOperationAndFileNoAndLatestFalseOrderByCreatedDateDesc(oldRevision.getId(), itemFile.getFileNo());
        for (MESBOPInstanceOperationFile oldVersionFile : oldVersionFiles) {
            MESBOPInstanceOperationFile newItemFile = null;
            File file = getBOPPlanFile(oldRevision.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new MESBOPInstanceOperationFile();
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
                newItemFile = bopInstanceOperationFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getReplaceFileSystemPath(newRevision.getId(), newItemFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
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
    private void saveItemFileChildren(MESBOPRouteOperation oldItem, MESBOPRouteOperation newItem, MESBOPInstanceOperationFile itemFile, MESBOPInstanceOperationFile plmItemFile) {
        List<MESBOPInstanceOperationFile> childrenFiles = bopInstanceOperationFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(itemFile.getId());
        for (MESBOPInstanceOperationFile childrenFile : childrenFiles) {
            MESBOPInstanceOperationFile newItemFile = null;
            File file = getBOPPlanFile(oldItem.getId(), childrenFile.getId());
            if (file != null) {
                newItemFile = new MESBOPInstanceOperationFile();
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
                newItemFile = bopInstanceOperationFileRepository.save(newItemFile);
                if (newItemFile.getFileType().equals("FOLDER")) {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + objectFileService.getParentFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                } else {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + objectFileService.getReplaceFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
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

    private void saveOldVersionItemFiles(MESBOPRouteOperation oldItem, MESBOPRouteOperation newItem, MESBOPInstanceOperationFile mesmbomFile) {
        List<MESBOPInstanceOperationFile> oldVersionFiles = bopInstanceOperationFileRepository.findByBopOperationAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(oldItem.getId(), mesmbomFile.getFileNo());
        for (MESBOPInstanceOperationFile oldVersionFile : oldVersionFiles) {
            MESBOPInstanceOperationFile newItemFile = null;
            File file = getBOPPlanFile(oldItem.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new MESBOPInstanceOperationFile();
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
                newItemFile = bopInstanceOperationFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getReplaceFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPINSTANCEROUTEOPERATION);
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
