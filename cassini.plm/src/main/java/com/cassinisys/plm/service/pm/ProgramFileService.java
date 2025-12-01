package com.cassinisys.plm.service.pm;

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
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.ProgramEvents;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.model.pm.PLMProgramFile;
import com.cassinisys.plm.repo.plm.FileDownloadHistoryRepository;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.plm.SharedObjectRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pm.ProgramFileRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.plm.FileHelpers;
import com.cassinisys.plm.service.plm.ItemFileService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import org.apache.commons.io.FileUtils;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by smukka on 17-05-2022.
 */
@Service
public class ProgramFileService {

    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ProgramFileRepository programFileRepository;
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
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private SharedObjectRepository sharedObjectRepository;

    public List<PLMProgramFile> uploadFiles(Integer id, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMProgramFile> uploadedFiles = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        List<PLMProgramFile> newFiles = new ArrayList<>();
        List<PLMProgramFile> versionedFiles = new ArrayList<>();
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
                    PLMProgramFile programFile = null;
                    if (folderId == 0) {
                        programFile = programFileRepository.findByProgramAndNameAndParentFileIsNullAndLatestTrue(id, name);
                    } else {
                        programFile = programFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer version = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (programFile != null) {
                        programFile.setLatest(false);
                        Integer oldVersion = programFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = programFile.getFileNo();
                        oldFile = programFile.getId();
                        versioned = true;
                        programFileRepository.save(programFile);
                    }
                    if (programFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    programFile = new PLMProgramFile();
                    programFile.setName(name);
                    programFile.setFileNo(autoNumber1);
                    programFile.setCreatedBy(login.getPerson().getId());
                    programFile.setModifiedBy(login.getPerson().getId());
                    programFile.setVersion(version);
                    programFile.setProgram(id);
                    programFile.setSize(file.getSize());
                    programFile.setFileType("FILE");
                    if (folderId != 0) {
                        programFile.setParentFile(folderId);
                    }
                    programFile = programFileRepository.save(programFile);
                    if (programFile.getParentFile() != null) {
                        PLMProgramFile parent = programFileRepository.findOne(programFile.getParentFile());
                        parent.setModifiedDate(programFile.getModifiedDate());
                        parent = programFileRepository.save(parent);
                    }
                    if (programFile.getVersion() > 1) {
                        objectFileService.copyFileAttributes(oldFile, programFile.getId());
                    }
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + objectFileService.getParentFileSystemPath(id, folderId, PLMObjectType.PROGRAM);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + programFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploadedFiles.add(programFile);
                    if (versioned) {
                        versionedFiles.add(programFile);
                    } else {
                        newFiles.add(programFile);
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
          /* App Events */
        if (newFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new ProgramEvents.ProgramFilesAddedEvent(id, newFiles));
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new ProgramEvents.ProgramFilesVersionedEvent(id, versionedFiles));
        }
        return uploadedFiles;
    }

    public PLMProgramFile updateFileName(Integer id, String newFileName) throws IOException {
        PLMProgramFile file1 = programFileRepository.findOne(id);
        file1.setLatest(false);
        PLMProgramFile plmProgramFile = programFileRepository.save(file1);
        PLMProgramFile programFile = (PLMProgramFile) Utils.cloneObject(plmProgramFile, PLMProgramFile.class);
        if (programFile != null) {
            programFile.setId(null);
            programFile.setName(newFileName);
            programFile.setVersion(file1.getVersion() + 1);
            programFile.setLatest(true);
            programFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            programFile = programFileRepository.save(programFile);
            if (programFile.getParentFile() != null) {
                PLMProgramFile parent = programFileRepository.findOne(programFile.getParentFile());
                parent.setModifiedDate(programFile.getModifiedDate());
                parent = programFileRepository.save(parent);
            }
            objectFileService.copyFileAttributes(file1.getId(), programFile.getId());
            String dir = "";
            if (programFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(programFile.getProgram(), id, PLMObjectType.PROGRAM);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + programFile.getProgram();
            }
            dir = dir + File.separator + programFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + programFile.getProgram() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            applicationEventPublisher.publishEvent(new ProgramEvents.ProgramFileRenamedEvent(programFile.getProgram(), "Rename", file1, programFile));
        }
        return programFile;
    }

    public List<PLMProgramFile> replaceNprFiles(Integer programId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMProgramFile> uploaded = new ArrayList<>();
        PLMProgramFile oldFile = null;
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        PLMProgramFile plmProgramFile = null;
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
                    PLMProgramFile programFile = null;
                    plmProgramFile = programFileRepository.findOne(fileId);
                    String oldName = "";
                    if (plmProgramFile != null && plmProgramFile.getParentFile() != null) {
                        programFile = programFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        programFile = programFileRepository.findByProgramAndNameAndParentFileIsNullAndLatestTrue(programId, name);
                    }
                    if (plmProgramFile != null) {
                        oldName = plmProgramFile.getName();
                        plmProgramFile.setLatest(false);
                        plmProgramFile = programFileRepository.save(plmProgramFile);
                    }
                    programFile = new PLMProgramFile();
                    programFile.setName(name);
                    if (plmProgramFile != null && plmProgramFile.getParentFile() != null) {
                        programFile.setParentFile(plmProgramFile.getParentFile());
                    }
                    if (plmProgramFile != null) {
                        programFile.setFileNo(plmProgramFile.getFileNo());
                        programFile.setVersion(plmProgramFile.getVersion() + 1);
                        programFile.setReplaceFileName(plmProgramFile.getName() + " Replaced to " + name);
                        oldFile = JsonUtils.cloneEntity(plmProgramFile, PLMProgramFile.class);
                    }
                    programFile.setCreatedBy(login.getPerson().getId());
                    programFile.setModifiedBy(login.getPerson().getId());
                    programFile.setProgram(programId);
                    programFile.setSize(file.getSize());
                    programFile.setFileType("FILE");
                    programFile = programFileRepository.save(programFile);
                    if (programFile.getParentFile() != null) {
                        PLMProgramFile parent = programFileRepository.findOne(programFile.getParentFile());
                        parent.setModifiedDate(programFile.getModifiedDate());
                        parent = programFileRepository.save(parent);
                    }
                    if (plmProgramFile != null) {
                        objectFileService.copyFileAttributes(plmProgramFile.getId(), programFile.getId());
                    }
                    String dir = "";
                    if (plmProgramFile != null && plmProgramFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(programId, fileId, PLMObjectType.PROGRAM);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + programId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + programFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(programFile);
                    applicationEventPublisher.publishEvent(new ProgramEvents.ProgramFileRenamedEvent(programId, "Replace", plmProgramFile, programFile));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    public void deleteNprFile(Integer programId, Integer id) {
        checkNotNull(id);
        PLMProgramFile programFile = programFileRepository.findOne(id);
        List<PLMProgramFile> nprFiles = programFileRepository.findByProgramAndFileNo(programId, programFile.getFileNo());
        for (PLMProgramFile nprFile1 : nprFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(programId, nprFile1.getId(), PLMObjectType.PROGRAM);
            fileSystemService.deleteDocumentFromDiskFolder(programFile.getId(), dir);
            programFileRepository.delete(nprFile1.getId());
        }
        if (programFile.getParentFile() != null) {
            PLMProgramFile parent = programFileRepository.findOne(programFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = programFileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramFileDeletedEvent(programId, programFile));
    }

    public File getNprFile(Integer programId, Integer fileId) {
        checkNotNull(programId);
        checkNotNull(fileId);
        PLMProgramFile programFile = programFileRepository.findOne(fileId);
        if (programFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(programId, fileId, PLMObjectType.PROGRAM);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer programId, Integer fileId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMProgramFile plmProgramFile = programFileRepository.findOne(fileId);
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramFileDownloadedEvent(programId, plmProgramFile));
        return plmFileDownloadHistory;
    }

    public List<PLMProgramFile> getAllFileVersionComments(Integer programId, Integer fileId, ObjectType objectType) {
        List<PLMProgramFile> itemFiles = new ArrayList<>();
        PLMProgramFile programFile = programFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(programFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(programFile.getId());
        if (comments.size() > 0) {
            programFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            programFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(programFile);
        List<PLMProgramFile> files = programFileRepository.findByProgramAndFileNoAndLatestFalseOrderByCreatedDateDesc(programFile.getProgram(), programFile.getFileNo());
        if (files.size() > 0) {
            for (PLMProgramFile file : files) {
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

    public PLMProgramFile createNprFolder(Integer programId, PLMProgramFile programFile) {
        programFile.setId(null);
        String folderNumber = null;
        PLMProgramFile existFolderName = null;
        if (programFile.getParentFile() != null) {
            existFolderName = programFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndProgramAndLatestTrue(programFile.getName(), programFile.getParentFile(), programId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(programFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", programFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = programFileRepository.findByNameEqualsIgnoreCaseAndProgramAndLatestTrue(programFile.getName(), programId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", programFile.getName());
                throw new CassiniException(result);
            }
        }

        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        programFile.setProgram(programId);
        programFile.setFileNo(folderNumber);
        programFile.setFileType("FOLDER");
        programFile = programFileRepository.save(programFile);
        if (programFile.getParentFile() != null) {
            PLMProgramFile parent = programFileRepository.findOne(programFile.getParentFile());
            parent.setModifiedDate(programFile.getModifiedDate());
            parent = programFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(programId, programFile.getId(), PLMObjectType.PROGRAM);
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramFoldersAddedEvent(programId, programFile));
        return programFile;
    }

    public PLMFile moveNprFileToFolder(Integer id, PLMProgramFile plmProgramFile) throws Exception {
        PLMProgramFile file = programFileRepository.findOne(plmProgramFile.getId());
        PLMProgramFile existFile = (PLMProgramFile) Utils.cloneObject(file, PLMProgramFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + objectFileService.getParentFileSystemPath(existFile.getProgram(), existFile.getId(), PLMObjectType.PROGRAM);
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getProgram() + File.separator + existFile.getId();
        }
        if (plmProgramFile.getParentFile() != null) {
            PLMProgramFile existItemFile = programFileRepository.findByParentFileAndNameAndLatestTrue(plmProgramFile.getParentFile(), plmProgramFile.getName());
            PLMProgramFile folder = programFileRepository.findOne(plmProgramFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmProgramFile = programFileRepository.save(plmProgramFile);
            }
        } else {
            PLMProgramFile existItemFile = programFileRepository.findByProgramAndNameAndParentFileIsNullAndLatestTrue(plmProgramFile.getProgram(), plmProgramFile.getName());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                plmProgramFile = programFileRepository.save(plmProgramFile);
            }
        }
        if (plmProgramFile != null) {
            String dir = "";
            if (plmProgramFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getParentFileSystemPath(plmProgramFile.getProgram(), plmProgramFile.getId(), PLMObjectType.PROGRAM);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmProgramFile.getProgram() + File.separator + plmProgramFile.getId();
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
            List<PLMProgramFile> oldVersionFiles = programFileRepository.findByProgramAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getProgram(), existFile.getFileNo());
            for (PLMProgramFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectFileService.getParentFileSystemPath(oldVersionFile.getProgram(), oldVersionFile.getId(), PLMObjectType.PROGRAM);
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getProgram() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFileService.getReplaceFileSystemPath(plmProgramFile.getProgram(), plmProgramFile.getId(), PLMObjectType.PROGRAM);
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmProgramFile.getParentFile());
                oldVersionFile = programFileRepository.save(oldVersionFile);
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
        return plmProgramFile;
    }

    public void deleteFolder(Integer programId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + objectFileService.getParentFileSystemPath(programId, folderId, PLMObjectType.PROGRAM);
        PLMProgramFile file = programFileRepository.findOne(folderId);
        List<PLMProgramFile> nprFiles = programFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) nprFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        programFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramFoldersDeletedEvent(file.getProgram(), file));
    }

    public void generateZipFile(Integer programId, HttpServletResponse response) throws FileNotFoundException, IOException {
        List<PLMProgramFile> plmNprFiles = programFileRepository.findByProgramAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(programId);
        ArrayList<String> fileList = new ArrayList<>();
        plmNprFiles.forEach(plmProgramFile -> {
            File file = getNprFile(programId, plmProgramFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = "NPR" + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "MFR",programId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional
    public PLMProgramFile getLatestUploadedNprFile(Integer programId, Integer fileId) {
        PLMProgramFile programFile = programFileRepository.findOne(fileId);
        PLMProgramFile plmProgramFile = programFileRepository.findByProgramAndFileNoAndLatestTrue(programFile.getProgram(), programFile.getFileNo());
        return plmProgramFile;
    }

    public PLMFile updateFileDescription(Integer programId, Integer id, PLMProgramFile plmProgramFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            file.setDescription(plmProgramFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<PLMProgramFile> pasteFromClipboard(Integer programId, Integer fileId, List<PLMFile> files) {
        List<PLMProgramFile> fileList = new ArrayList<>();
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
                PLMProgramFile programFile = new PLMProgramFile();
                PLMProgramFile existFile = null;
                if (fileId != 0) {
                    programFile.setParentFile(fileId);
                    existFile = programFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndProgramAndLatestTrue(file.getName(), fileId, programId);
                } else {
                    existFile = programFileRepository.findByProgramAndNameAndParentFileIsNullAndLatestTrue(programId, file.getName());
                }
                if (existFile == null) {
                    programFile.setName(file.getName());
                    programFile.setDescription(file.getDescription());
                    programFile.setProgram(programId);
                    programFile.setVersion(1);
                    programFile.setSize(file.getSize());
                    programFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    programFile.setFileNo(autoNumber1);
                    programFile.setFileType("FILE");
                    programFile = programFileRepository.save(programFile);
                    programFile.setParentObject(PLMObjectType.PLMNPR);
                    fileList.add(programFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + programId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (programFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + objectFileService.getParentFileSystemPath(programId, programFile.getId(), PLMObjectType.PROGRAM);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + programId + File.separator + programFile.getId();
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
                PLMProgramFile projectFile = new PLMProgramFile();
                PLMProgramFile existFile = null;
                if (fileId != 0) {
                    projectFile.setParentFile(fileId);
                    existFile = programFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndProgramAndLatestTrue(file.getName(), fileId, programId);
                } else {
                    existFile = programFileRepository.findByProgramAndNameAndParentFileIsNullAndLatestTrue(programId, file.getName());
                }
                if (existFile == null) {
                    projectFile.setName(file.getName());
                    projectFile.setDescription(file.getDescription());
                    String folderNumber = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    projectFile.setVersion(1);
                    projectFile.setSize(0L);
                    projectFile.setProgram(programId);
                    projectFile.setFileNo(folderNumber);
                    projectFile.setFileType("FOLDER");
                    projectFile = programFileRepository.save(projectFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + programId;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(programId, projectFile.getId());
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(projectFile);
                    copyFolderFiles(programId, file.getParentObject(), file.getId(), projectFile.getId());
                }
            }
        }
        return fileList;
    }

    private void copyFolderFiles(Integer project, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> projectFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        projectFiles.forEach(plmProjectFile -> {
            PLMProgramFile projectFile = new PLMProgramFile();
            projectFile.setParentFile(parent);
            projectFile.setName(plmProjectFile.getName());
            projectFile.setDescription(plmProjectFile.getDescription());
            projectFile.setProgram(project);
            String folderNumber = null;
            if (plmProjectFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                projectFile.setVersion(1);
                projectFile.setFileNo(folderNumber);
                projectFile.setSize(plmProjectFile.getSize());
                projectFile.setFileType("FILE");
                projectFile = programFileRepository.save(projectFile);
                projectFile.setParentObject(PLMObjectType.PROGRAM);
                plmProjectFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmProjectFile);

                String dir = "";
                if (projectFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(project, projectFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + project + File.separator + projectFile.getId();
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
                projectFile.setVersion(1);
                projectFile.setSize(0L);
                projectFile.setFileNo(folderNumber);
                projectFile.setFileType("FOLDER");
                projectFile = programFileRepository.save(projectFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + project;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(project, projectFile.getId());
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(project, objectType, plmProjectFile.getId(), projectFile.getId());
            }
        });
    }


    private String getParentFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        PLMProgramFile projectFile = programFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (projectFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, projectFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + projectFile.getId();
        }
        return path;
    }


    public void undoCopiedNprFiles(Integer ecoId, List<PLMProgramFile> nprFiles) {
        nprFiles.forEach(programFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + objectFileService.getParentFileSystemPath(ecoId, programFile.getId(), PLMObjectType.PROGRAM);
            fileSystemService.deleteDocumentFromDiskFolder(programFile.getId(), dir);
            programFileRepository.delete(programFile.getId());
        });
    }

    @Transactional(readOnly = true)
    public List<FileDto> convertFilesIdsToDtoList(Integer object, PLMObjectType objectType, List<Integer> fileIds, Boolean hierarchy) {
        List<FileDto> filesDto = new ArrayList<>();
        List<PLMProgramFile> files = programFileRepository.findByIdIn(fileIds);
        List<PLMProgramFile> plmFiles = programFileRepository.getParentChildrenByFileType(fileIds, "FILE");
        List<String> fileNos = programFileRepository.getFileNosByIds(fileIds);
        List<PLMProgramFile> fileNoFiles = programFileRepository.findByFileNosOrderByIdAsc(fileNos);
        List<Integer> personIds = fileRepository.getCreatedByIds(fileIds);
        personIds.addAll(fileRepository.getModifiedByIds(fileIds));
        fileNoFiles.forEach(plmFile1 -> {
            personIds.add(plmFile1.getCreatedBy());
        });
        List<Integer> filterIds = personIds.stream().distinct().collect(Collectors.toList());
        List<Person> persons = personRepository.findByIdIn(filterIds);
        List<Login> logins = loginRepository.getLoginsByPersonIds(filterIds);
        List<PLMProgramFile> fileCountList = programFileRepository.getChildrenCountByParentFileAndLatestTrueByIds(fileIds);
        List<PLMObjectDocument> objectDocuments = objectDocumentRepository.getDocumentsCountByObjectIdAndFolderIds(object, fileIds);
        Map<Integer, List<PLMFile>> childrenMap = new HashMap<>();
        Map<String, List<PLMFile>> fileNosMap = new HashMap();
        Map<Integer, List<PLMFile>> fileCountsMap = new HashMap();
        Map<Integer, List<PLMObjectDocument>> objectDocumentCountsMap = new HashMap();
        Map<Integer, Person> personsMap = new HashMap();
        Map<Integer, Login> loginsMap = new HashMap();

        personsMap = persons.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
        loginsMap = logins.stream().collect(Collectors.toMap(x -> x.getPerson().getId(), x -> x));
        childrenMap = plmFiles.stream().collect(Collectors.groupingBy(d -> d.getParentFile()));
        objectDocumentCountsMap = objectDocuments.stream().collect(Collectors.groupingBy(d -> d.getFolder()));
        fileNosMap = fileNoFiles.stream().collect(Collectors.groupingBy(d -> d.getFileNo()));
        fileCountsMap = fileCountList.stream().collect(Collectors.groupingBy(d -> d.getParentFile()));

        List<PLMSharedObject> shareObjects = sharedObjectRepository.getSharedObjectByObjectIdsInAndPerson(fileIds, sessionWrapper.getSession().getLogin().getPerson().getId());
        Map<Integer, List<PLMSharedObject>> shareObjsCountMap = new HashMap();
        shareObjsCountMap = shareObjects.stream().collect(Collectors.groupingBy(d -> d.getObjectId()));


        Map<Integer, List<PLMFile>> fileChildrenMap = childrenMap;
        Map<String, List<PLMFile>> fileNoMap = fileNosMap;
        Map<Integer, List<PLMFile>> fileCountMap = fileCountsMap;
        Map<Integer, List<PLMObjectDocument>> objectDocumentCountMap = objectDocumentCountsMap;
        Map<Integer, Person> personMap = personsMap;
        Map<Integer, Login> loginMap = loginsMap;
        Map<Integer, List<PLMSharedObject>> finalShareObjectMap = shareObjsCountMap;
        files.forEach(plmFile -> {
            FileDto fileDto = new FileDto();
            fileDto.setId(plmFile.getId());
            fileDto.setName(plmFile.getName());
            fileDto.setObject(object);
            fileDto.setParentObject(objectType);
            fileDto.setDescription(plmFile.getDescription());
            fileDto.setFileNo(plmFile.getFileNo());
            fileDto.setFileType(plmFile.getFileType());
            fileDto.setParentFile(plmFile.getParentFile());
            fileDto.setSize(plmFile.getSize());
            fileDto.setLatest(plmFile.getLatest());
            fileDto.setVersion(plmFile.getVersion());
            fileDto.setLocked(plmFile.getLocked());
            fileDto.setLockedBy(plmFile.getLockedBy());
            fileDto.setLockedDate(plmFile.getLockedDate());
            fileDto.setThumbnail(plmFile.getThumbnail());
            List<PLMSharedObject> existingShareObjects = finalShareObjectMap.containsKey(plmFile.getId()) ? finalShareObjectMap.get(plmFile.getId()) : new ArrayList<>();
            if (existingShareObjects.size() > 0) {
                fileDto.setShared(true);
            }
            fileDto.setChildFileCount(fileChildrenMap.containsKey(plmFile.getId()) ? fileChildrenMap.get(plmFile.getId()).size() : 0);
            if (fileDto.getLockedBy() != null) {
                fileDto.setLockedByName(personRepository.findOne(plmFile.getLockedBy()).getFullName());
            }
            fileDto.setObjectType(PLMObjectType.valueOf(plmFile.getObjectType().toString()));
            fileDto.setCreatedDate(plmFile.getCreatedDate());
            fileDto.setModifiedDate(plmFile.getModifiedDate());
            List<PLMFile> initialVersionFiles = fileNoMap.containsKey(plmFile.getFileNo()) ? fileNoMap.get(plmFile.getFileNo()) : new ArrayList<PLMFile>();
            Person person = null;
            if (initialVersionFiles.size() > 0) {
                person = personMap.get(initialVersionFiles.get(0).getCreatedBy());
            } else {
                person = personMap.get(plmFile.getCreatedBy());
            }
            fileDto.setCreatedByName(person.getFullName());
            fileDto.setCreatedBy(person.getId());
            Login login = loginMap.get(person.getId());
            fileDto.setExternal(login.getExternal());
            fileDto.setModifiedByName(personMap.get(plmFile.getModifiedBy()).getFullName());
            fileDto.setReplaceFileName(plmFile.getReplaceFileName());
            fileDto.setUrn(plmFile.getUrn());
            if (fileDto.getFileType().equals("FOLDER")) {
                fileDto.setCount(fileCountMap.containsKey(fileDto.getId()) ? fileCountMap.get(fileDto.getId()).size() : 0);
                fileDto.setCount(fileDto.getCount() + (objectDocumentCountMap.containsKey(fileDto.getId()) ? objectDocumentCountMap.get(fileDto.getId()).size() : 0));
                if (hierarchy) {
                    visitChildren(object, objectType, fileDto, hierarchy);
                }
            }
            filesDto.add(fileDto);
        });
        return filesDto;
    }

    public FileDto visitChildren(Integer object, PLMObjectType objectType, FileDto fileDto, Boolean hierarchy) {
        List<Integer> foldersList = programFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FOLDER");
        List<Integer> filesList = programFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FILE");
        if (foldersList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, foldersList, hierarchy));
        }
        if (filesList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, filesList, hierarchy));
        }
        return fileDto;
    }
}
