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
import com.cassinisys.plm.event.BOPEvents;
import com.cassinisys.plm.model.mes.MESBOPFile;
import com.cassinisys.plm.model.mes.MESBOPRevision;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mes.BOPFileRepository;
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
public class BOPFileService {

    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private BOPFileRepository bopFileRepository;
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

    public List<MESBOPFile> uploadFiles(Integer id, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<MESBOPFile> uploadedFiles = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        List<MESBOPFile> newFiles = new ArrayList<>();
        List<MESBOPFile> versionedFiles = new ArrayList<>();
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
                    MESBOPFile bopFile = null;
                    if (folderId == 0) {
                        bopFile = bopFileRepository.findByBopAndNameAndParentFileIsNullAndLatestTrue(id, name);
                    } else {
                        bopFile = bopFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
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
                        bopFileRepository.save(bopFile);
                    }
                    if (bopFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    bopFile = new MESBOPFile();
                    bopFile.setName(name);
                    bopFile.setFileNo(autoNumber1);
                    bopFile.setCreatedBy(login.getPerson().getId());
                    bopFile.setModifiedBy(login.getPerson().getId());
                    bopFile.setVersion(version);
                    bopFile.setBop(id);
                    bopFile.setSize(file.getSize());
                    bopFile.setFileType("FILE");
                    if (folderId != 0) {
                        bopFile.setParentFile(folderId);
                    }
                    bopFile = bopFileRepository.save(bopFile);
                    if (bopFile.getParentFile() != null) {
                        MESBOPFile parent = bopFileRepository.findOne(bopFile.getParentFile());
                        parent.setModifiedDate(bopFile.getModifiedDate());
                        parent = bopFileRepository.save(parent);
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
                                "filesystem" + objectFileService.getParentFileSystemPath(id, folderId, PLMObjectType.BOPREVISION);
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
            applicationEventPublisher.publishEvent(new BOPEvents.BOPFilesAddedEvent(id, newFiles));
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new BOPEvents.BOPFilesVersionedEvent(id, versionedFiles));
        }
        return uploadedFiles;
    }

    public MESBOPFile updateFileName(Integer id, String newFileName) throws IOException {
        MESBOPFile file1 = bopFileRepository.findOne(id);
        file1.setLatest(false);
        MESBOPFile plmNprFile = bopFileRepository.save(file1);
        MESBOPFile bopFile = (MESBOPFile) Utils.cloneObject(plmNprFile, MESBOPFile.class);
        if (bopFile != null) {
            bopFile.setId(null);
            bopFile.setName(newFileName);
            bopFile.setVersion(file1.getVersion() + 1);
            bopFile.setLatest(true);
            bopFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            bopFile = bopFileRepository.save(bopFile);
            if (bopFile.getParentFile() != null) {
                MESBOPFile parent = bopFileRepository.findOne(bopFile.getParentFile());
                parent.setModifiedDate(bopFile.getModifiedDate());
                parent = bopFileRepository.save(parent);
            }
            objectFileService.copyFileAttributes(file1.getId(), bopFile.getId());
            String dir = "";
            if (bopFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(bopFile.getBop(), id, PLMObjectType.BOPREVISION);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + bopFile.getBop();
            }
            dir = dir + File.separator + bopFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + bopFile.getBop() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            applicationEventPublisher.publishEvent(new BOPEvents.BOPFileRenamedEvent(bopFile.getBop(), plmNprFile, bopFile, "Rename"));
        }
        return bopFile;
    }

    public List<MESBOPFile> replaceNprFiles(Integer bopId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<MESBOPFile> uploaded = new ArrayList<>();
        MESBOPFile oldFile = null;
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        MESBOPFile plmNprFile = null;
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
                    MESBOPFile bopFile = null;
                    plmNprFile = bopFileRepository.findOne(fileId);
                    String oldName = "";
                    if (plmNprFile != null && plmNprFile.getParentFile() != null) {
                        bopFile = bopFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        bopFile = bopFileRepository.findByBopAndNameAndParentFileIsNullAndLatestTrue(bopId, name);
                    }
                    if (plmNprFile != null) {
                        oldName = plmNprFile.getName();
                        plmNprFile.setLatest(false);
                        plmNprFile = bopFileRepository.save(plmNprFile);
                    }
                    bopFile = new MESBOPFile();
                    bopFile.setName(name);
                    if (plmNprFile != null && plmNprFile.getParentFile() != null) {
                        bopFile.setParentFile(plmNprFile.getParentFile());
                    }
                    if (plmNprFile != null) {
                        bopFile.setFileNo(plmNprFile.getFileNo());
                        bopFile.setVersion(plmNprFile.getVersion() + 1);
                        bopFile.setReplaceFileName(plmNprFile.getName() + " Replaced to " + name);
                        oldFile = JsonUtils.cloneEntity(plmNprFile, MESBOPFile.class);
                    }
                    bopFile.setCreatedBy(login.getPerson().getId());
                    bopFile.setModifiedBy(login.getPerson().getId());
                    bopFile.setBop(bopId);
                    bopFile.setSize(file.getSize());
                    bopFile.setFileType("FILE");
                    bopFile = bopFileRepository.save(bopFile);
                    if (bopFile.getParentFile() != null) {
                        MESBOPFile parent = bopFileRepository.findOne(bopFile.getParentFile());
                        parent.setModifiedDate(bopFile.getModifiedDate());
                        parent = bopFileRepository.save(parent);
                    }
                    if (plmNprFile != null) {
                        objectFileService.copyFileAttributes(plmNprFile.getId(), bopFile.getId());
                    }
                    String dir = "";
                    if (plmNprFile != null && plmNprFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(bopId, fileId, PLMObjectType.BOPREVISION);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + bopId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + bopFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(bopFile);
                    applicationEventPublisher.publishEvent(new BOPEvents.BOPFileRenamedEvent(bopId, plmNprFile, bopFile, "Replace"));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    public void deleteNprFile(Integer bopId, Integer id) {
        checkNotNull(id);
        MESBOPFile bopFile = bopFileRepository.findOne(id);
        List<MESBOPFile> nprFiles = bopFileRepository.findByBopAndFileNo(bopId, bopFile.getFileNo());
        for (MESBOPFile nprFile1 : nprFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(bopId, nprFile1.getId(), PLMObjectType.BOPREVISION);
            fileSystemService.deleteDocumentFromDiskFolder(bopFile.getId(), dir);
            bopFileRepository.delete(nprFile1.getId());
        }
        if (bopFile.getParentFile() != null) {
            MESBOPFile parent = bopFileRepository.findOne(bopFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = bopFileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new BOPEvents.BOPFileDeletedEvent(bopFile.getBop(), bopFile));
    }

    public File getNprFile(Integer bopId, Integer fileId) {
        checkNotNull(bopId);
        checkNotNull(fileId);
        MESBOPFile bopFile = bopFileRepository.findOne(fileId);
        if (bopFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(bopId, fileId, PLMObjectType.BOPREVISION);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer bopId, Integer fileId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        MESBOPFile mesbopFile = bopFileRepository.findOne(fileId);
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        applicationEventPublisher.publishEvent(new BOPEvents.BOPFileDownloadedEvent(mesbopFile.getBop(), mesbopFile));
        return plmFileDownloadHistory;
    }

    public List<MESBOPFile> getAllFileVersionComments(Integer bopId, Integer fileId, ObjectType objectType) {
        List<MESBOPFile> itemFiles = new ArrayList<>();
        MESBOPFile bopFile = bopFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(bopFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(bopFile.getId());
        if (comments.size() > 0) {
            bopFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            bopFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(bopFile);
        List<MESBOPFile> files = bopFileRepository.findByBopAndFileNoAndLatestFalseOrderByCreatedDateDesc(bopFile.getBop(), bopFile.getFileNo());
        if (files.size() > 0) {
            for (MESBOPFile file : files) {
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

    public MESBOPFile createNprFolder(Integer bopId, MESBOPFile bopFile) {
        bopFile.setId(null);
        String folderNumber = null;
        MESBOPFile existFolderName = null;
        if (bopFile.getParentFile() != null) {
            existFolderName = bopFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndBopAndLatestTrue(bopFile.getName(), bopFile.getParentFile(), bopId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(bopFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", bopFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = bopFileRepository.findByNameEqualsIgnoreCaseAndBopAndLatestTrue(bopFile.getName(), bopId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", bopFile.getName());
                throw new CassiniException(result);
            }
        }

        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        bopFile.setBop(bopId);
        bopFile.setFileNo(folderNumber);
        bopFile.setFileType("FOLDER");
        bopFile = bopFileRepository.save(bopFile);
        if (bopFile.getParentFile() != null) {
            MESBOPFile parent = bopFileRepository.findOne(bopFile.getParentFile());
            parent.setModifiedDate(bopFile.getModifiedDate());
            parent = bopFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(bopId, bopFile.getId(), PLMObjectType.BOPREVISION);
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new BOPEvents.BOPFoldersAddedEvent(bopFile.getBop(), bopFile));
        return bopFile;
    }

    public PLMFile moveNprFileToFolder(Integer id, MESBOPFile plmNprFile) throws Exception {
        MESBOPFile file = bopFileRepository.findOne(plmNprFile.getId());
        MESBOPFile existFile = (MESBOPFile) Utils.cloneObject(file, MESBOPFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + objectFileService.getParentFileSystemPath(existFile.getBop(), existFile.getId(), PLMObjectType.BOPREVISION);
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getBop() + File.separator + existFile.getId();
        }
        if (plmNprFile.getParentFile() != null) {
            MESBOPFile existItemFile = bopFileRepository.findByParentFileAndNameAndLatestTrue(plmNprFile.getParentFile(), plmNprFile.getName());
            MESBOPFile folder = bopFileRepository.findOne(plmNprFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmNprFile = bopFileRepository.save(plmNprFile);
            }
        } else {
            MESBOPFile existItemFile = bopFileRepository.findByBopAndNameAndParentFileIsNullAndLatestTrue(plmNprFile.getBop(), plmNprFile.getName());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                plmNprFile = bopFileRepository.save(plmNprFile);
            }
        }
        if (plmNprFile != null) {
            String dir = "";
            if (plmNprFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getParentFileSystemPath(plmNprFile.getBop(), plmNprFile.getId(), PLMObjectType.BOPREVISION);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmNprFile.getBop() + File.separator + plmNprFile.getId();
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
            List<MESBOPFile> oldVersionFiles = bopFileRepository.findByBopAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getBop(), existFile.getFileNo());
            for (MESBOPFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(oldVersionFile.getBop(), oldVersionFile.getId(), PLMObjectType.BOPREVISION);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getBop() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(plmNprFile.getBop(), plmNprFile.getId(), PLMObjectType.BOPREVISION);
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmNprFile.getParentFile());
                oldVersionFile = bopFileRepository.save(oldVersionFile);
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

    public void deleteFolder(Integer bopId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(bopId, folderId, PLMObjectType.BOPREVISION);
        MESBOPFile file = bopFileRepository.findOne(folderId);
        List<MESBOPFile> nprFiles = bopFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) nprFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        bopFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new BOPEvents.BOPFoldersDeletedEvent(file.getBop(), file));
    }

    public void generateZipFile(Integer bopId, HttpServletResponse response) throws FileNotFoundException, IOException {
        List<MESBOPFile> plmNprFiles = bopFileRepository.findByBopAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(bopId);
        ArrayList<String> fileList = new ArrayList<>();
        plmNprFiles.forEach(plmNprFile -> {
            File file = getNprFile(bopId, plmNprFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = "NPR" + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "MFR",bopId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional
    public MESBOPFile getLatestUploadedNprFile(Integer bopId, Integer fileId) {
        MESBOPFile bopFile = bopFileRepository.findOne(fileId);
        MESBOPFile plmNprFile = bopFileRepository.findByBopAndFileNoAndLatestTrue(bopFile.getBop(), bopFile.getFileNo());
        return plmNprFile;
    }

    public PLMFile updateFileDescription(Integer bopId, Integer id, MESBOPFile plmNprFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            file.setDescription(plmNprFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<MESBOPFile> pasteFromClipboard(Integer bopId, Integer fileId, List<PLMFile> files) {
        List<MESBOPFile> fileList = new ArrayList<>();
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
                MESBOPFile bopFile = new MESBOPFile();
                MESBOPFile existFile = null;
                if (fileId != 0) {
                    bopFile.setParentFile(fileId);
                    existFile = bopFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndBopAndLatestTrue(file.getName(), fileId, bopId);
                } else {
                    existFile = bopFileRepository.findByBopAndNameAndParentFileIsNullAndLatestTrue(bopId, file.getName());
                }
                if (existFile == null) {
                    bopFile.setName(file.getName());
                    bopFile.setDescription(file.getDescription());
                    bopFile.setBop(bopId);
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
                    bopFile = bopFileRepository.save(bopFile);
                    bopFile.setParentObject(PLMObjectType.PLMNPR);
                    fileList.add(bopFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + bopId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (bopFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + objectFileService.getParentFileSystemPath(bopId, bopFile.getId(), PLMObjectType.BOPREVISION);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + bopId + File.separator + bopFile.getId();
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
                MESBOPFile changeFile = new MESBOPFile();
                MESBOPFile existFile = null;
                if (fileId != 0) {
                    changeFile.setParentFile(fileId);
                    existFile = bopFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndBopAndLatestTrue(file.getName(), fileId, bopId);
                } else {
                    existFile = bopFileRepository.findByBopAndNameAndParentFileIsNullAndLatestTrue(bopId, file.getName());
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
                    changeFile.setBop(bopId);
                    changeFile.setFileNo(folderNumber);
                    changeFile.setFileType("FOLDER");
                    changeFile = bopFileRepository.save(changeFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + bopId;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(bopId, changeFile.getId(), PLMObjectType.BOPREVISION);
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(changeFile);
                    copyFolderFiles(bopId, file.getParentObject(), file.getId(), changeFile.getId());
                }
            }
        }
        return fileList;
    }

    private void copyFolderFiles(Integer id, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        plmFiles.forEach(plmFile -> {
            MESBOPFile mesbopFile = new MESBOPFile();
            mesbopFile.setParentFile(parent);
            mesbopFile.setName(plmFile.getName());
            mesbopFile.setDescription(plmFile.getDescription());
            mesbopFile.setBop(id);
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
                mesbopFile = bopFileRepository.save(mesbopFile);
                mesbopFile.setParentObject(PLMObjectType.BOPREVISION);
                plmFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmFile);

                String dir = "";
                if (mesbopFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(id, mesbopFile.getId(), PLMObjectType.BOPREVISION);
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
                mesbopFile = bopFileRepository.save(mesbopFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getParentFileSystemPath(id, mesbopFile.getId(), PLMObjectType.BOPREVISION);
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(id, objectType, plmFile.getId(), mesbopFile.getId());
            }
        });
    }

    public void undoCopiedNprFiles(Integer ecoId, List<MESBOPFile> nprFiles) {
        nprFiles.forEach(bopFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(ecoId, bopFile.getId(), PLMObjectType.BOPREVISION);
            fileSystemService.deleteDocumentFromDiskFolder(bopFile.getId(), dir);
            bopFileRepository.delete(bopFile.getId());
        });
    }


    @Transactional
    public MESBOPFile copyMBOMFile(MESBOPRevision oldItem, MESBOPRevision newItem, MESBOPFile mesmbomFile) {
        MESBOPFile newItemFile = null;
        File file = getNprFile(oldItem.getId(), mesmbomFile.getId());
        if (file != null) {
            newItemFile = new MESBOPFile();
            Login login = sessionWrapper.getSession().getLogin();
            newItemFile.setName(mesmbomFile.getName());
            newItemFile.setFileNo(mesmbomFile.getFileNo());
            newItemFile.setFileType(mesmbomFile.getFileType());
            newItemFile.setCreatedBy(login.getPerson().getId());
            newItemFile.setModifiedBy(login.getPerson().getId());
            newItemFile.setBop(newItem.getId());
            newItemFile.setVersion(mesmbomFile.getVersion());
            newItemFile.setSize(mesmbomFile.getSize());
            newItemFile.setLatest(mesmbomFile.getLatest());
            newItemFile = bopFileRepository.save(newItemFile);
            if (newItemFile.getFileType().equals("FOLDER")) {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getParentFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPREVISION);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
            } else {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getReplaceFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPREVISION);
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


    private void saveChildrenOldVersionItemFiles(MESBOPRevision oldRevision, MESBOPRevision newRevision, MESBOPFile itemFile, MESBOPFile plmItemFile) {
        List<MESBOPFile> oldVersionFiles = bopFileRepository.findByBopAndFileNoAndLatestFalseOrderByCreatedDateDesc(oldRevision.getId(), itemFile.getFileNo());
        for (MESBOPFile oldVersionFile : oldVersionFiles) {
            MESBOPFile newItemFile = null;
            File file = getNprFile(oldRevision.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new MESBOPFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setBop(newRevision.getId());
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile = bopFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getReplaceFileSystemPath(newRevision.getId(), newItemFile.getId(), PLMObjectType.BOPREVISION);
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
    private void saveItemFileChildren(MESBOPRevision oldItem, MESBOPRevision newItem, MESBOPFile itemFile, MESBOPFile plmItemFile) {
        List<MESBOPFile> childrenFiles = bopFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(itemFile.getId());
        for (MESBOPFile childrenFile : childrenFiles) {
            MESBOPFile newItemFile = null;
            File file = getNprFile(oldItem.getId(), childrenFile.getId());
            if (file != null) {
                newItemFile = new MESBOPFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(childrenFile.getName());
                newItemFile.setFileNo(childrenFile.getFileNo());
                newItemFile.setFileType(childrenFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setBop(newItem.getId());
                newItemFile.setVersion(childrenFile.getVersion());
                newItemFile.setSize(childrenFile.getSize());
                newItemFile.setLatest(childrenFile.getLatest());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile = bopFileRepository.save(newItemFile);
                if (newItemFile.getFileType().equals("FOLDER")) {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + objectFileService.getParentFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPREVISION);
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                } else {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + objectFileService.getReplaceFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPREVISION);
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

    private void saveOldVersionItemFiles(MESBOPRevision oldItem, MESBOPRevision newItem, MESBOPFile mesmbomFile) {
        List<MESBOPFile> oldVersionFiles = bopFileRepository.findByBopAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(oldItem.getId(), mesmbomFile.getFileNo());
        for (MESBOPFile oldVersionFile : oldVersionFiles) {
            MESBOPFile newItemFile = null;
            File file = getNprFile(oldItem.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new MESBOPFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setBop(newItem.getId());
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile = bopFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + objectFileService.getReplaceFileSystemPath(newItem.getId(), newItemFile.getId(), PLMObjectType.BOPREVISION);
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
