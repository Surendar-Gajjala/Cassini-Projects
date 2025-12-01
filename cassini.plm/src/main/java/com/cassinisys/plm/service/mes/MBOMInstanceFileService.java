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
import com.cassinisys.plm.event.MBOMInstanceEvents;
import com.cassinisys.plm.model.mes.MESMBOMInstance;
import com.cassinisys.plm.model.mes.MESMBOMInstanceFile;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mes.MBOMInstanceFileRepository;
import com.cassinisys.plm.repo.mes.MBOMInstanceRepository;
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
public class MBOMInstanceFileService {

    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private MBOMInstanceFileRepository mbomInstanceFileRepository;
    @Autowired
    private MBOMInstanceRepository mbomInstanceRepository;
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

    public List<MESMBOMInstanceFile> uploadFiles(Integer id, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<MESMBOMInstanceFile> uploadedFiles = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        List<MESMBOMInstanceFile> newFiles = new ArrayList<>();
        List<MESMBOMInstanceFile> versionedFiles = new ArrayList<>();
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
                    MESMBOMInstanceFile mbomFile = null;
                    if (folderId == 0) {
                        mbomFile = mbomInstanceFileRepository.findByMbomInstanceAndNameAndParentFileIsNullAndLatestTrue(id, name);
                    } else {
                        mbomFile = mbomInstanceFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
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
                        mbomInstanceFileRepository.save(mbomFile);
                    }
                    if (mbomFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    mbomFile = new MESMBOMInstanceFile();
                    mbomFile.setName(name);
                    mbomFile.setFileNo(autoNumber1);
                    mbomFile.setCreatedBy(login.getPerson().getId());
                    mbomFile.setModifiedBy(login.getPerson().getId());
                    mbomFile.setVersion(version);
                    mbomFile.setMbomInstance(id);
                    mbomFile.setSize(file.getSize());
                    mbomFile.setFileType("FILE");
                    if (folderId != 0) {
                        mbomFile.setParentFile(folderId);
                    }
                    mbomFile = mbomInstanceFileRepository.save(mbomFile);
                    if (mbomFile.getParentFile() != null) {
                        MESMBOMInstanceFile parent = mbomInstanceFileRepository.findOne(mbomFile.getParentFile());
                        parent.setModifiedDate(mbomFile.getModifiedDate());
                        parent = mbomInstanceFileRepository.save(parent);
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
          /* App Events */
        if (newFiles.size() > 0) {

            applicationEventPublisher.publishEvent(new MBOMInstanceEvents.MbomInstanceFilesAddedEvent(id, newFiles));
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new MBOMInstanceEvents.MbomInstanceFilesVersionedEvent(id, versionedFiles));
        }
        return uploadedFiles;
    }

    public MESMBOMInstanceFile updateFileName(Integer id, String newFileName) throws IOException {
        MESMBOMInstanceFile file1 = mbomInstanceFileRepository.findOne(id);
        MESMBOMInstanceFile oldFile = (MESMBOMInstanceFile) Utils.cloneObject(file1, MESMBOMInstanceFile.class);
        String oldName = file1.getName();
        file1.setLatest(false);
        MESMBOMInstanceFile plmMbomFile = mbomInstanceFileRepository.save(file1);
        MESMBOMInstanceFile mbomFile = (MESMBOMInstanceFile) Utils.cloneObject(plmMbomFile, MESMBOMInstanceFile.class);
        if (mbomFile != null) {
            mbomFile.setId(null);
            mbomFile.setName(newFileName);
            mbomFile.setVersion(file1.getVersion() + 1);
            mbomFile.setLatest(true);
            mbomFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            mbomFile = mbomInstanceFileRepository.save(mbomFile);
            if (mbomFile.getParentFile() != null) {
                MESMBOMInstanceFile parent = mbomInstanceFileRepository.findOne(mbomFile.getParentFile());
                parent.setModifiedDate(mbomFile.getModifiedDate());
                parent = mbomInstanceFileRepository.save(parent);
            }
            objectFileService.copyFileAttributes(file1.getId(), mbomFile.getId());
            String dir = "";
            if (mbomFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(mbomFile.getMbomInstance(), id, PLMObjectType.MBOMREVISION);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + mbomFile.getMbomInstance();
            }
            dir = dir + File.separator + mbomFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + mbomFile.getMbomInstance() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            applicationEventPublisher.publishEvent(new MBOMInstanceEvents.MbomInstanceFileRenamedEvent(mbomFile.getMbomInstance(), oldFile, mbomFile, "Rename"));
        }
        return mbomFile;
    }

    public List<MESMBOMInstanceFile> replaceMBOMInstanceFiles(Integer mbomId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<MESMBOMInstanceFile> uploaded = new ArrayList<>();
        MESMBOMInstanceFile oldFile = null;
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        MESMBOMInstanceFile mesmbomInstanceFile = null;
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
                    MESMBOMInstanceFile mbomFile = null;
                    mesmbomInstanceFile = mbomInstanceFileRepository.findOne(fileId);
                    String oldName = "";
                    if (mesmbomInstanceFile != null && mesmbomInstanceFile.getParentFile() != null) {
                        mbomFile = mbomInstanceFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        mbomFile = mbomInstanceFileRepository.findByMbomInstanceAndNameAndParentFileIsNullAndLatestTrue(mbomId, name);
                    }
                    if (mesmbomInstanceFile != null) {
                        oldName = mesmbomInstanceFile.getName();
                        mesmbomInstanceFile.setLatest(false);
                        mesmbomInstanceFile = mbomInstanceFileRepository.save(mesmbomInstanceFile);
                    }
                    mbomFile = new MESMBOMInstanceFile();
                    mbomFile.setName(name);
                    if (mesmbomInstanceFile != null && mesmbomInstanceFile.getParentFile() != null) {
                        mbomFile.setParentFile(mesmbomInstanceFile.getParentFile());
                    }
                    if (mesmbomInstanceFile != null) {
                        mbomFile.setFileNo(mesmbomInstanceFile.getFileNo());
                        mbomFile.setVersion(mesmbomInstanceFile.getVersion() + 1);
                        mbomFile.setReplaceFileName(mesmbomInstanceFile.getName() + " Replaced to " + name);
                        oldFile = JsonUtils.cloneEntity(mesmbomInstanceFile, MESMBOMInstanceFile.class);
                    }
                    mbomFile.setCreatedBy(login.getPerson().getId());
                    mbomFile.setModifiedBy(login.getPerson().getId());
                    mbomFile.setMbomInstance(mbomId);
                    mbomFile.setSize(file.getSize());
                    mbomFile.setFileType("FILE");
                    mbomFile = mbomInstanceFileRepository.save(mbomFile);
                    if (mbomFile.getParentFile() != null) {
                        MESMBOMInstanceFile parent = mbomInstanceFileRepository.findOne(mbomFile.getParentFile());
                        parent.setModifiedDate(mbomFile.getModifiedDate());
                        parent = mbomInstanceFileRepository.save(parent);
                    }
                    if (mesmbomInstanceFile != null) {
                        objectFileService.copyFileAttributes(mesmbomInstanceFile.getId(), mbomFile.getId());
                    }
                    String dir = "";
                    if (mesmbomInstanceFile != null && mesmbomInstanceFile.getParentFile() != null) {
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
                    applicationEventPublisher.publishEvent(new MBOMInstanceEvents.MbomInstanceFileRenamedEvent(mbomId, mesmbomInstanceFile, mbomFile, "Replace"));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    public void deleteMBOMInstanceFile(Integer mbomId, Integer id) {
        checkNotNull(id);
        MESMBOMInstanceFile mbomFile = mbomInstanceFileRepository.findOne(id);
        List<MESMBOMInstanceFile> nprFiles = mbomInstanceFileRepository.findByMbomInstanceAndFileNo(mbomId, mbomFile.getFileNo());
        for (MESMBOMInstanceFile nprFile1 : nprFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(mbomId, nprFile1.getId(), PLMObjectType.MBOMREVISION);
            fileSystemService.deleteDocumentFromDiskFolder(mbomFile.getId(), dir);
            mbomInstanceFileRepository.delete(nprFile1.getId());
        }
        if (mbomFile.getParentFile() != null) {
            MESMBOMInstanceFile parent = mbomInstanceFileRepository.findOne(mbomFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = mbomInstanceFileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new MBOMInstanceEvents.MbomInstanceFileDeletedEvent(mbomId, mbomFile));
    }

    public File getMBOMInstanceFile(Integer mbomId, Integer fileId) {
        checkNotNull(mbomId);
        checkNotNull(fileId);
        MESMBOMInstanceFile mbomFile = mbomInstanceFileRepository.findOne(fileId);
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
        MESMBOMInstanceFile plmItemFile = mbomInstanceFileRepository.findOne(fileId);
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        applicationEventPublisher.publishEvent(new MBOMInstanceEvents.MbomInstanceFileDownloadedEvent(mbomId, plmItemFile));
        return plmFileDownloadHistory;
    }

    public List<MESMBOMInstanceFile> getAllFileVersionComments(Integer mbomId, Integer fileId, ObjectType objectType) {
        List<MESMBOMInstanceFile> itemFiles = new ArrayList<>();
        MESMBOMInstanceFile mbomFile = mbomInstanceFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(mbomFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(mbomFile.getId());
        if (comments.size() > 0) {
            mbomFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            mbomFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(mbomFile);
        List<MESMBOMInstanceFile> files = mbomInstanceFileRepository.findByMbomInstanceAndFileNoAndLatestFalseOrderByCreatedDateDesc(mbomFile.getMbomInstance(), mbomFile.getFileNo());
        if (files.size() > 0) {
            for (MESMBOMInstanceFile file : files) {
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

    public MESMBOMInstanceFile createMBOMInstanceFolder(Integer mbomId, MESMBOMInstanceFile mbomFile) {
        mbomFile.setId(null);
        String folderNumber = null;
        MESMBOMInstanceFile existFolderName = null;
        if (mbomFile.getParentFile() != null) {
            existFolderName = mbomInstanceFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndMbomInstanceAndLatestTrue(mbomFile.getName(), mbomFile.getParentFile(), mbomId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(mbomFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", mbomFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = mbomInstanceFileRepository.findByNameEqualsIgnoreCaseAndMbomInstanceAndLatestTrue(mbomFile.getName(), mbomId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", mbomFile.getName());
                throw new CassiniException(result);
            }
        }

        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        mbomFile.setMbomInstance(mbomId);
        mbomFile.setFileNo(folderNumber);
        mbomFile.setFileType("FOLDER");
        mbomFile = mbomInstanceFileRepository.save(mbomFile);
        if (mbomFile.getParentFile() != null) {
            MESMBOMInstanceFile parent = mbomInstanceFileRepository.findOne(mbomFile.getParentFile());
            parent.setModifiedDate(mbomFile.getModifiedDate());
            parent = mbomInstanceFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(mbomId, mbomFile.getId(), PLMObjectType.MBOMREVISION);
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new MBOMInstanceEvents.MbomInstanceFoldersAddedEvent(mbomId, mbomFile));
        return mbomFile;
    }

    public PLMFile moveMBOMInstanceFileToFolder(Integer id, MESMBOMInstanceFile mesmbomInstanceFile) throws Exception {
        MESMBOMInstanceFile file = mbomInstanceFileRepository.findOne(mesmbomInstanceFile.getId());
        MESMBOMInstanceFile existFile = (MESMBOMInstanceFile) Utils.cloneObject(file, MESMBOMInstanceFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + objectFileService.getParentFileSystemPath(existFile.getMbomInstance(), existFile.getId(), PLMObjectType.MBOMREVISION);
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getMbomInstance() + File.separator + existFile.getId();
        }
        if (mesmbomInstanceFile.getParentFile() != null) {
            MESMBOMInstanceFile existItemFile = mbomInstanceFileRepository.findByParentFileAndNameAndLatestTrue(mesmbomInstanceFile.getParentFile(), mesmbomInstanceFile.getName());
            MESMBOMInstanceFile folder = mbomInstanceFileRepository.findOne(mesmbomInstanceFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                mesmbomInstanceFile = mbomInstanceFileRepository.save(mesmbomInstanceFile);
            }
        } else {
            MESMBOMInstanceFile existItemFile = mbomInstanceFileRepository.findByMbomInstanceAndNameAndParentFileIsNullAndLatestTrue(mesmbomInstanceFile.getMbomInstance(), mesmbomInstanceFile.getName());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                mesmbomInstanceFile = mbomInstanceFileRepository.save(mesmbomInstanceFile);
            }
        }
        if (mesmbomInstanceFile != null) {
            String dir = "";
            if (mesmbomInstanceFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getParentFileSystemPath(mesmbomInstanceFile.getMbomInstance(), mesmbomInstanceFile.getId(), PLMObjectType.MBOMREVISION);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + mesmbomInstanceFile.getMbomInstance() + File.separator + mesmbomInstanceFile.getId();
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
            List<MESMBOMInstanceFile> oldVersionFiles = mbomInstanceFileRepository.findByMbomInstanceAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getMbomInstance(), existFile.getFileNo());
            for (MESMBOMInstanceFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(oldVersionFile.getMbomInstance(), oldVersionFile.getId(), PLMObjectType.MBOMREVISION);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getMbomInstance() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(mesmbomInstanceFile.getMbomInstance(), mesmbomInstanceFile.getId(), PLMObjectType.MBOMREVISION);
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(mesmbomInstanceFile.getParentFile());
                oldVersionFile = mbomInstanceFileRepository.save(oldVersionFile);
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
        return mesmbomInstanceFile;
    }

    public void deleteFolder(Integer mbomId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(mbomId, folderId, PLMObjectType.MBOMREVISION);
        MESMBOMInstanceFile file = mbomInstanceFileRepository.findOne(folderId);
        List<MESMBOMInstanceFile> nprFiles = mbomInstanceFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) nprFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        mbomInstanceFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new MBOMInstanceEvents.MbomInstanceFoldersDeletedEvent(mbomId, file));
    }

    public void generateZipFile(Integer mbomId, HttpServletResponse response) throws FileNotFoundException, IOException {
        List<MESMBOMInstanceFile> plmMBOMInstanceFiles = mbomInstanceFileRepository.findByMbomInstanceAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(mbomId);
        ArrayList<String> fileList = new ArrayList<>();
        plmMBOMInstanceFiles.forEach(mesmbomInstanceFile -> {
            File file = getMBOMInstanceFile(mbomId, mesmbomInstanceFile.getId());
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
    public MESMBOMInstanceFile getLatestUploadedMBOMInstanceFile(Integer mbomId, Integer fileId) {
        MESMBOMInstanceFile mbomFile = mbomInstanceFileRepository.findOne(fileId);
        MESMBOMInstanceFile mesmbomInstanceFile = mbomInstanceFileRepository.findByMbomInstanceAndFileNoAndLatestTrue(mbomFile.getMbomInstance(), mbomFile.getFileNo());
        return mesmbomInstanceFile;
    }

    public PLMFile updateFileDescription(Integer mbomId, Integer id, MESMBOMInstanceFile mesmbomInstanceFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            file.setDescription(mesmbomInstanceFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<MESMBOMInstanceFile> pasteFromClipboard(Integer mbomId, Integer fileId, List<PLMFile> files) {
        List<MESMBOMInstanceFile> fileList = new ArrayList<>();
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
                MESMBOMInstanceFile mbomFile = new MESMBOMInstanceFile();
                MESMBOMInstanceFile existFile = null;
                if (fileId != 0) {
                    mbomFile.setParentFile(fileId);
                    existFile = mbomInstanceFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndMbomInstanceAndLatestTrue(file.getName(), fileId, mbomId);
                } else {
                    existFile = mbomInstanceFileRepository.findByMbomInstanceAndNameAndParentFileIsNullAndLatestTrue(mbomId, file.getName());
                }
                if (existFile == null) {
                    mbomFile.setName(file.getName());
                    mbomFile.setDescription(file.getDescription());
                    mbomFile.setMbomInstance(mbomId);
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
                    mbomFile = mbomInstanceFileRepository.save(mbomFile);
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
                MESMBOMInstanceFile mesmbomFile = new MESMBOMInstanceFile();
                MESMBOMInstanceFile existFile = null;
                if (fileId != 0) {
                    mesmbomFile.setParentFile(fileId);
                    existFile = mbomInstanceFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndMbomInstanceAndLatestTrue(file.getName(), fileId, mbomId);
                } else {
                    existFile = mbomInstanceFileRepository.findByMbomInstanceAndNameAndParentFileIsNullAndLatestTrue(mbomId, file.getName());
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
                    mesmbomFile.setMbomInstance(mbomId);
                    mesmbomFile.setFileNo(folderNumber);
                    mesmbomFile.setFileType("FOLDER");
                    mesmbomFile = mbomInstanceFileRepository.save(mesmbomFile);
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
            MESMBOMInstanceFile mesbopFile = new MESMBOMInstanceFile();
            mesbopFile.setParentFile(parent);
            mesbopFile.setName(plmFile.getName());
            mesbopFile.setDescription(plmFile.getDescription());
            mesbopFile.setMbomInstance(id);
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
                mesbopFile = mbomInstanceFileRepository.save(mesbopFile);
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
                mesbopFile = mbomInstanceFileRepository.save(mesbopFile);

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

    public void undoCopiedMBOMInstanceFiles(Integer ecoId, List<MESMBOMInstanceFile> nprFiles) {
        nprFiles.forEach(mbomFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(ecoId, mbomFile.getId(), PLMObjectType.MBOMREVISION);
            fileSystemService.deleteDocumentFromDiskFolder(mbomFile.getId(), dir);
            mbomInstanceFileRepository.delete(mbomFile.getId());
        });
    }


    @Transactional
    public MESMBOMInstanceFile copyMBOMFile(MESMBOMInstance oldItem, MESMBOMInstance newItem, MESMBOMInstanceFile mesmbomFile) {
        MESMBOMInstanceFile newItemFile = null;
        File file = getMBOMInstanceFile(oldItem.getId(), mesmbomFile.getId());
        if (file != null) {
            newItemFile = new MESMBOMInstanceFile();
            Login login = sessionWrapper.getSession().getLogin();
            newItemFile.setName(mesmbomFile.getName());
            newItemFile.setFileNo(mesmbomFile.getFileNo());
            newItemFile.setFileType(mesmbomFile.getFileType());
            newItemFile.setCreatedBy(login.getPerson().getId());
            newItemFile.setModifiedBy(login.getPerson().getId());
            newItemFile.setMbomInstance(newItem.getId());
            newItemFile.setVersion(mesmbomFile.getVersion());
            newItemFile.setSize(mesmbomFile.getSize());
            newItemFile.setLatest(mesmbomFile.getLatest());
            newItemFile = mbomInstanceFileRepository.save(newItemFile);
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


    private void saveChildrenOldVersionItemFiles(MESMBOMInstance oldRevision, MESMBOMInstance newRevision, MESMBOMInstanceFile itemFile, MESMBOMInstanceFile plmItemFile) {
        List<MESMBOMInstanceFile> oldVersionFiles = mbomInstanceFileRepository.findByMbomInstanceAndFileNoAndLatestFalseOrderByCreatedDateDesc(oldRevision.getId(), itemFile.getFileNo());
        for (MESMBOMInstanceFile oldVersionFile : oldVersionFiles) {
            MESMBOMInstanceFile newItemFile = null;
            File file = getMBOMInstanceFile(oldRevision.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new MESMBOMInstanceFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setMbomInstance(newRevision.getId());
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile = mbomInstanceFileRepository.save(newItemFile);
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
    private void saveItemFileChildren(MESMBOMInstance oldItem, MESMBOMInstance newItem, MESMBOMInstanceFile itemFile, MESMBOMInstanceFile plmItemFile) {
        List<MESMBOMInstanceFile> childrenFiles = mbomInstanceFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(itemFile.getId());
        for (MESMBOMInstanceFile childrenFile : childrenFiles) {
            MESMBOMInstanceFile newItemFile = null;
            File file = getMBOMInstanceFile(oldItem.getId(), childrenFile.getId());
            if (file != null) {
                newItemFile = new MESMBOMInstanceFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(childrenFile.getName());
                newItemFile.setFileNo(childrenFile.getFileNo());
                newItemFile.setFileType(childrenFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setMbomInstance(newItem.getId());
                newItemFile.setVersion(childrenFile.getVersion());
                newItemFile.setSize(childrenFile.getSize());
                newItemFile.setLatest(childrenFile.getLatest());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile = mbomInstanceFileRepository.save(newItemFile);
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

    private void saveOldVersionItemFiles(MESMBOMInstance oldItem, MESMBOMInstance newItem, MESMBOMInstanceFile mesmbomFile) {
        List<MESMBOMInstanceFile> oldVersionFiles = mbomInstanceFileRepository.findByMbomInstanceAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(oldItem.getId(), mesmbomFile.getFileNo());
        for (MESMBOMInstanceFile oldVersionFile : oldVersionFiles) {
            MESMBOMInstanceFile newItemFile = null;
            File file = getMBOMInstanceFile(oldItem.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new MESMBOMInstanceFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setMbomInstance(newItem.getId());
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile = mbomInstanceFileRepository.save(newItemFile);
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
