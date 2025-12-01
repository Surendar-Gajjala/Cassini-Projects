package com.cassinisys.platform.service.custom;

import com.cassinisys.platform.events.CustomObjectEvents;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.CustomFile;
import com.cassinisys.platform.model.custom.CustomFileDownloadHistory;
import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.model.custom.CustomObjectFile;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.custom.CustomFileDownloadHistoryRepository;
import com.cassinisys.platform.repo.custom.CustomFileRepository;
import com.cassinisys.platform.repo.custom.CustomObjectFileRepository;
import com.cassinisys.platform.repo.custom.CustomObjectRepository;
import com.cassinisys.platform.service.common.ForgeService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Utils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyam on 09/06/20.
 */
@Service
@Transactional
public class CustomObjectFileService implements CrudService<CustomFile, Integer> {

    static final int BUFFER = 2048;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private ForgeService forgeService;
    @Autowired
    private CustomObjectFileRepository customObjectFileRepository;
    @Autowired
    private CustomObjectRepository customObjectRepository;
    @Autowired
    private CustomFileRepository customFileRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CustomFileDownloadHistoryRepository customFileDownloadHistoryRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public CustomFile create(CustomFile plmFile) {
        return null;
    }

    @Override
    public CustomFile update(CustomFile plmFile) {
        return null;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public CustomFile get(Integer integer) {
        return null;
    }

    @Override
    public List<CustomFile> getAll() {
        return null;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','file')")
    public List<CustomFile> uploadCustomObjectFiles(Integer id, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<CustomFile> uploaded = new ArrayList<>();
        List<CustomFile> newFiles = new ArrayList<>();
        List<CustomFile> versionedFiles = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");

        CustomObject customObject = customObjectRepository.findOne(id);
        String[] fileExtension = null;
        boolean flag = true;

        List<CustomFile> fileList = new ArrayList<>();
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        String fNames = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                Boolean versioned = false;
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    CustomObjectFile customObjectFile = null;
                    Integer version = 1;
                    Integer oldVersion = 1;
                    String autoNumber1 = null;
                    CustomFile customFile = null;
                    Integer oldfile = null;
                    if (folderId == 0) {
                        customObjectFile = customObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, name);
                    } else {
                        customObjectFile = customObjectFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }

                    if (customObjectFile != null) {
                        customObjectFile.setLatest(false);
                        oldVersion = customObjectFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = customObjectFile.getNumber();
                        oldfile = customObjectFile.getId();
                        customObjectFileRepository.save(customObjectFile);
                        versioned = true;

                    }
                    if (customObjectFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                        }
                    }

                    customObjectFile = new CustomObjectFile();
                    customObjectFile.setObject(id);
                    customObjectFile.setName(name);
                    customObjectFile.setCreatedBy(login.getPerson().getId());
                    customObjectFile.setModifiedBy(login.getPerson().getId());
                    customObjectFile.setSize(file.getSize());
                    customObjectFile.setFileType("FILE");
                    customObjectFile.setNumber(autoNumber1);
                    customObjectFile.setVersion(version);
                    if (folderId != 0) {
                        customObjectFile.setParentFile(folderId);
                    }
                    customObjectFile = customObjectFileRepository.save(customObjectFile);
                    customFile = customFileRepository.findOne(customObjectFile.getId());
                    customFile.setOldVersion(oldVersion);
                    fileList.add(customFile);
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getParentFileSystemPath(id, folderId);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + customFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(customFile);

                    if (versioned) {
                        versionedFiles.add(customFile);
                    } else {
                        newFiles.add(customFile);
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

            /* App Events */
        if (newFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectFilesAddedEvent(customObject.getId(), newFiles));
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectFilesVersionedEvent(customObject.getId(), versionedFiles));
        }

        return uploaded;
    }

    @Transactional(readOnly = true)
    public List<CustomObjectFile> getCustomObjectFiles(Integer id) {
        List<CustomObjectFile> customObjectFiles = customObjectFileRepository.findByObjectAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(id);
        customObjectFiles.forEach(customObjectFile -> {
            customObjectFile.setParentObject(ObjectType.CUSTOMOBJECT);
            if (customObjectFile.getFileType().equals("FOLDER")) {
                customObjectFile.setCount(customObjectFileRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(customObjectFile.getId()).size());
            }
        });

        return customObjectFiles;
    }

    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'edit','file')")
    public CustomObjectFile updateCustomObjectFile(Integer id, Integer fileId, CustomObjectFile customObjectFile) {
        CustomFile file = customFileRepository.findOne(fileId);
        if (file != null) {
            CustomObjectFile customObjectFile1 = customObjectFileRepository.findOne(file.getId());
            if (!file.getLocked().equals(customObjectFile.getLocked())) {
                /* App events */
                if (customObjectFile.getLocked()) {
                    applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectFileLockedEvent(id, customObjectFile1));
                } else {
                    applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectFileUnlockedEvent(id, customObjectFile1));
                }
            }
            file.setDescription(customObjectFile.getDescription());
            file.setLocked(customObjectFile.getLocked());
            file.setLockedBy(customObjectFile.getLockedBy());
            file.setLockedDate(customObjectFile.getLockedDate());
            file = customFileRepository.save(file);
        }
        return customObjectFile;
    }

    @Transactional
    public CustomObjectFile updateFolder(Integer id, Integer folderId, CustomObjectFile fileDto) {
        CustomObjectFile objectFileDto = new CustomObjectFile();
        CustomObjectFile plmFile = customObjectFileRepository.findOne(fileDto.getId());
        plmFile.setName(fileDto.getName());
        plmFile.setDescription(fileDto.getDescription());
        plmFile = customObjectFileRepository.save(plmFile);
        return plmFile;
    }

    @Transactional
    public CustomObjectFile createFolder(Integer id, CustomObjectFile customObjectFile) {
        String folderNumber = null;
        CustomFile customFile = null;
        CustomObject customObject = customObjectRepository.findOne(id);
        CustomObjectFile existFolderName = null;
        if (customObjectFile.getParentFile() != null) {
            existFolderName = customObjectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(customObjectFile.getName(), customObjectFile.getParentFile(), id);
            if (existFolderName != null) {
                CustomFile file = customFileRepository.findOne(customObjectFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", customObjectFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = customObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, customObjectFile.getName());
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", customObjectFile.getName(), existFolderName.getName());
                throw new CassiniException(result);
            }
        }
        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        customObjectFile.setObject(id);
        customObjectFile.setNumber(folderNumber);
        customObjectFile.setFileType("FOLDER");
        customObjectFile = customObjectFileRepository.save(customObjectFile);

        customFile = customFileRepository.findOne(customObjectFile.getId());

        if (customFile != null) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(id, customFile.getId());
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
        }
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectFoldersAddedEvent(customObject.getId(), customObjectFile));
        return customObjectFile;
    }

    private String getParentFileSystemPath(Integer id, Integer fileId) {
        String path = "";
        CustomObjectFile file = customObjectFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (file.getParentFile() != null) {
            path = visitParentFolder(id, file.getParentFile(), path);
        } else {
            path = File.separator + id + File.separator + file.getId();
        }
        return path;
    }

    @Transactional(readOnly = true)
    public List<CustomObjectFile> getFolderChildren(Integer id, Integer folderId) {
        List<CustomObjectFile> customObjectFiles = customObjectFileRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
        customObjectFiles.forEach(customObjectFile -> {
            customObjectFile.setParentObject(ObjectType.CUSTOMOBJECT);
            if (customObjectFile.getFileType().equals("FOLDER")) {
                customObjectFile.setCount(customObjectFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(customObjectFile.getId()).size());
            }
        });

        return customObjectFiles;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'rename','file')")
    public CustomObjectFile updateFileName(Integer id, Integer fileId, String newFileName) throws IOException {
        String oldFileDir = "";
        String dir = "";
        CustomObjectFile customObjectFile = customObjectFileRepository.findOne(fileId);
        CustomObject customObject = customObjectRepository.findOne(id);
        customObjectFile.setLatest(false);
        customObjectFile = customObjectFileRepository.save(customObjectFile);
        CustomObjectFile customObjectFile1 = (CustomObjectFile) Utils.cloneObject(customObjectFile, CustomObjectFile.class);
        if (customObjectFile1 != null) {
            customObjectFile1.setId(null);
            customObjectFile1.setName(newFileName);
            customObjectFile1.setVersion(customObjectFile.getVersion() + 1);
            customObjectFile1.setLatest(true);
            customObjectFile1.setReplaceFileName(customObjectFile.getName() + " ReName to " + newFileName);
            customObjectFile1 = customObjectFileRepository.save(customObjectFile1);
            dir = "";
            if (customObjectFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
            }
            dir = dir + File.separator + customObjectFile1.getId();
            oldFileDir = "";
            if (customObjectFile1.getParentFile() != null) {
                oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(id, fileId);
            } else {
                oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id + File.separator + fileId;
            }
        }
        if (!dir.equals("")) {
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

        return customObjectFile1;
    }

    private String getReplaceFileSystemPath(Integer id, Integer fileId) {
        String path = "";
        CustomObjectFile file = customObjectFileRepository.findOne(fileId);
        if (file.getParentFile() != null) {
            path = visitParentFolder(id, file.getParentFile(), path);
        } else {
            path = File.separator + id;
        }
        return path;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'replace','file')")
    public CustomObjectFile replaceCustomObjectFile(Integer id, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        CustomObject customObject = customObjectRepository.findOne(id);
        String[] fileExtension = null;
        String htmlTable = null;
        boolean flag = true;
        String name = null;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }

        CustomObjectFile customObjectFile = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    CustomObjectFile newCustomObjectFile = null;
                    customObjectFile = customObjectFileRepository.findOne(fileId);
                    if (customObjectFile != null) {
                        if (customObjectFile.getParentFile() != null) {
                            CustomObjectFile folder = customObjectFileRepository.findOne(customObjectFile.getParentFile());
                            CustomObjectFile existFile = customObjectFileRepository.findByParentFileAndNameAndLatestTrue(customObjectFile.getParentFile(), name);
                            if (existFile != null && !existFile.getId().equals(customObjectFile.getId())) {
                                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                String result = MessageFormat.format(message + ".", customObjectFile.getName(), folder.getName());
                                throw new CassiniException(result);
                            }
                        } else {
                            CustomObjectFile existFile = customObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(customObjectFile.getObject(), name);
                            if (existFile != null && !existFile.getId().equals(customObjectFile.getId())) {
                                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                                String result = MessageFormat.format(message + ".", existFile.getName());
                                throw new CassiniException(result);
                            }
                        }

                        customObjectFile.setLatest(false);
                        customObjectFile = customObjectFileRepository.save(customObjectFile);
                    }

                    newCustomObjectFile = new CustomObjectFile();
                    newCustomObjectFile.setName(name);
                    if (customObjectFile != null && customObjectFile.getParentFile() != null) {
                        newCustomObjectFile.setParentFile(customObjectFile.getParentFile());
                    }
                    if (customObjectFile != null) {
                        newCustomObjectFile.setNumber(customObjectFile.getNumber());
                        newCustomObjectFile.setVersion(customObjectFile.getVersion() + 1);
                        newCustomObjectFile.setReplaceFileName(customObjectFile.getName() + " Replaced to " + name);
                    }
                    newCustomObjectFile.setCreatedBy(login.getPerson().getId());
                    newCustomObjectFile.setModifiedBy(login.getPerson().getId());
                    newCustomObjectFile.setObject(id);
                    newCustomObjectFile.setFileType("FILE");
                    newCustomObjectFile.setSize(file.getSize());
                    newCustomObjectFile = customObjectFileRepository.save(newCustomObjectFile);
                    String dir = "";
                    if (customObjectFile != null && customObjectFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + newCustomObjectFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectFileRenamedEvent(id, customObjectFile, newCustomObjectFile, "Replace"));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return customObjectFile;
    }

    @Transactional(readOnly = true)
    public CustomObjectFile getLatestUploadedFile(Integer id, Integer fileId) {
        CustomObjectFile customObjectFile = customObjectFileRepository.findOne(fileId);
        CustomObjectFile latestFile = customObjectFileRepository.findByObjectAndNumberAndLatestTrue(customObjectFile.getObject(), customObjectFile.getNumber());
        return latestFile;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','file')")
    public void deleteCustomObjectFile(Integer id, Integer fileId) {
        String fileName = "";
        CustomObjectFile customObjectFile = customObjectFileRepository.findOne(fileId);
        CustomObject customObject = customObjectRepository.findOne(id);
        List<CustomObjectFile> customObjectFiles = customObjectFileRepository.findByObjectAndNumber(id, customObjectFile.getNumber());
        fileName = customObjectFile.getName();
        for (CustomObjectFile objectFile : customObjectFiles) {
            if (objectFile == null) {
                throw new ResourceNotFoundException();
            }
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + objectFile.getObject();
            fileSystemService.deleteDocumentFromDisk(objectFile.getId(), dir);
            customObjectFileRepository.delete(objectFile.getId());
        }
    }

    private String getFolderName(String folderName, Integer folderId) {
        CustomFile folder = customObjectFileRepository.findOne(folderId);
        if (folder.getParentFile() != null) {
            CustomFile parentFolder = customObjectFileRepository.findOne(folder.getParentFile());
            if (folderName == null || folderName.equals("")) {
                folderName = folder.getName();
            } else {
                folderName = folder.getName() + " / " + folderName;
            }
            folderName = getFolderName(folderName, parentFolder.getId());
        } else {
            if (folderName == null || folderName.equals("")) {
                folderName = folder.getName();
            } else {
                folderName = folder.getName() + " / " + folderName;
            }
        }

        return folderName;
    }

    @Transactional
    public void deleteFolder(Integer id, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(id, folderId);
        CustomObject customObject = customObjectRepository.findOne(id);
        CustomFile file = customFileRepository.findOne(folderId);
        List<CustomObjectFile> customObjectFiles = customObjectFileRepository.findByParentFileOrderByFileTypeDescModifiedDateDesc(folderId);
        removeFileIfExist((List) customObjectFiles, dir);
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectFoldersDeletedEvent(customObject.getId(), file));
        customObjectFileRepository.delete(folderId);

        File fDir = new File(dir);
        Utils.removeDirIfExist(fDir);
    }

    @Transactional
    public CustomObjectFile moveCustomObjectFileToFolder(Integer id, CustomObjectFile objectFile) throws Exception {

        CustomObjectFile file = customObjectFileRepository.findOne(objectFile.getId());
        CustomObjectFile existFile = (CustomObjectFile) Utils.cloneObject(file, CustomObjectFile.class);
        CustomFile customFile = customFileRepository.findOne(objectFile.getId());

        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getObject(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getObject() + File.separator + existFile.getId();
        }
        if (objectFile.getParentFile() != null) {
            CustomObjectFile existItemFile = customObjectFileRepository.findByParentFileAndNameAndLatestTrue(objectFile.getParentFile(), objectFile.getName());
            CustomObjectFile folder = customObjectFileRepository.findOne(objectFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                objectFile.setParentFile(objectFile.getParentFile());
                objectFile = customObjectFileRepository.save(objectFile);
            }
        } else {
            CustomObjectFile existItemFile = customObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(objectFile.getObject(), objectFile.getName());
            if (existItemFile != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                objectFile = customObjectFileRepository.save(objectFile);
            }
        }

        if (objectFile != null) {
            String dir = "";
            if (objectFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(objectFile.getObject(), objectFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectFile.getObject() + File.separator + objectFile.getId();
            }
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            FileInputStream instream = null;
            FileOutputStream outstream = null;
            try {
                File infile = new File(oldFileDir);
                File outfile = new File(dir);
                instream = new FileInputStream(infile);
                outstream = new FileOutputStream(outfile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = instream.read(buffer)) > 0) {
                    outstream.write(buffer, 0, length);
                }
                instream.close();
                outstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File e = new File(oldFileDir);
            System.gc();
            Thread.sleep(1000L);
            FileUtils.deleteQuietly(e);
            List<CustomObjectFile> oldVersionFiles = customObjectFileRepository.findByObjectAndNumberAndLatestFalseOrderByCreatedDateDesc(existFile.getObject(), existFile.getNumber());
            for (CustomObjectFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getObject(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getObject() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(objectFile.getObject(), objectFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(objectFile.getParentFile());
                oldVersionFile = customObjectFileRepository.save(oldVersionFile);
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

        return objectFile;
    }

    @Transactional
    public List<CustomFile> pasteFromClipboard(Integer id, Integer fileId, List<CustomFile> files) {
        List<CustomFile> fileList = new ArrayList<>();
        for (CustomFile file : files) {

            CustomObjectFile customObjectFile = new CustomObjectFile();
            CustomObjectFile existFile = null;
            if (fileId != 0) {
                customObjectFile.setParentFile(fileId);
                existFile = customObjectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(file.getName(), fileId, id);
            } else {
                existFile = customObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
            }
            if (existFile == null) {
                customObjectFile.setName(file.getName());
                customObjectFile.setDescription(file.getDescription());
                customObjectFile.setObject(id);
                customObjectFile.setVersion(1);
                customObjectFile.setSize(file.getSize());
                customObjectFile.setLatest(file.getLatest());
                String autoNumber1 = null;
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                }
                customObjectFile.setNumber(autoNumber1);
                customObjectFile.setFileType("FILE");
                customObjectFile = customObjectFileRepository.save(customObjectFile);
                CustomFile customFile = customFileRepository.findOne(customObjectFile.getId());
                customFile.setOldVersion(customFile.getVersion());
                fileList.add(customFile);

                customObjectFile.setOldVersion(customObjectFile.getVersion());

                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getCopyFilePath(file);
                String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
                File fDirectory = new File(directory);
                if (!fDirectory.exists()) {
                    fDirectory.mkdirs();
                }
                String dir = "";
                if (customObjectFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, customObjectFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + customObjectFile.getId();
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
        }

        return fileList;
    }

    @Transactional
    public void undoCopiedItemFiles(Integer id, List<CustomObjectFile> customObjectFiles) {
        customObjectFiles.forEach(inspectionPlanFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(id, inspectionPlanFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(inspectionPlanFile.getId(), dir);
            customObjectFileRepository.delete(inspectionPlanFile.getId());
        });
    }

  /*  @Transactional
    public CustomFileDownloadHistory fileDownloadHistory(Integer id, Integer fileId) {
        CustomFileDownloadHistory plmFileDownloadHistory = new CustomFileDownloadHistory();
        CustomFile plmFile = fileRepository.findOne(fileId);
        PLMCustomObject change = changeRepository.findOne(id);
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        return plmFileDownloadHistory;
    }*/

    @Transactional
    public CustomFile getCustomObjectFile(Integer id) {
        return customFileRepository.findOne(id);
    }

    @Transactional
    public File getFile(Integer id, Integer fileId) {
        checkNotNull(id);
        checkNotNull(fileId);

        CustomFile customFile = customFileRepository.findOne(fileId);
        if (customFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(id, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    @Transactional
    public void generateZipFile(Integer id, HttpServletResponse response) throws FileNotFoundException, IOException {
        ArrayList<String> fileList = new ArrayList<>();
        String number = "";
        CustomObject object = customObjectRepository.findOne(id);
        number = object.getNumber();

        List<CustomObjectFile> customObjectFiles = customObjectFileRepository.findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(id);
        customObjectFiles.forEach(customObjectFile -> {
            File file = getFile(id, customObjectFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = number + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "CUSTOMOBJECT");
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional(readOnly = true)
    public List<CustomObjectFile> findByTypeAndFileName(Integer id, String name) {

        List<CustomObjectFile> customObjectFiles = customObjectFileRepository.findByObjectAndNameContainingIgnoreCaseAndLatestTrueOrderByFileTypeDescModifiedDateDesc(id, name);

        return customObjectFiles;
    }

    @Transactional(readOnly = true)
    public List<CustomObjectFile> getAllFileVersionComments(Integer fileId, ObjectType objectType) {
        List<CustomObjectFile> customObjectFiles = new ArrayList<>();
        CustomObjectFile customObjectFile = customObjectFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(customObjectFile.getId(), objectType);
        List<CustomFileDownloadHistory> fileDownloadHistories = customFileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(customObjectFile.getId());
        if (comments.size() > 0) {
            customObjectFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            customObjectFile.setDownloadHistories(fileDownloadHistories);
        }
        customObjectFiles.add(customObjectFile);
        List<CustomObjectFile> files = customObjectFileRepository.findByObjectAndNumberAndLatestFalseOrderByCreatedDateDesc(customObjectFile.getObject(), customObjectFile.getNumber());
        if (files.size() > 0) {
            for (CustomObjectFile file : files) {
                List<Comment> oldVersionComments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType);
                if (oldVersionComments.size() > 0) {
                    file.setComments(oldVersionComments);
                }
                List<CustomFileDownloadHistory> oldFileDownloadHistories = customFileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId());
                if (oldFileDownloadHistories.size() > 0) {
                    file.setDownloadHistories(oldFileDownloadHistories);
                }
                customObjectFiles.add(file);
            }
        }

        return customObjectFiles;
    }

    @Transactional(readOnly = true)
    public List<CustomObjectFile> getAllFileVersions(Integer ecoId, String name) {
        return customObjectFileRepository.findAllByObjectAndNameOrderByCreatedDateDesc(ecoId, name);
    }


    public String getParentPath(CustomFile file, String path, Integer id) {
        if (file.getParentFile() != null) {
            path = File.separator + file.getId() + path;
            path = visitParentFolder(id, file.getParentFile(), path);
        } else {
            path = File.separator + id + File.separator + file.getId() + path;
        }
        return path;
    }

    public String visitParentFolder(Integer itemId, Integer fileId, String path) {
        CustomFile file = customFileRepository.findOne(fileId);
        path = getParentPath(file, path, itemId);
        return path;
    }

    public void removeFileIfExist(List<CustomFile> files, String dir) {
        files.forEach(file -> {
            String filePath = dir + File.separator + file.getId();
            File fDir = new File(filePath);
            FileUtils.deleteQuietly(fDir);
            customFileRepository.delete(file.getId());
        });
    }

    public String getCopyFilePath(CustomFile file) {
        String path = "";
        if (file.getParentObject().equals(ObjectType.CUSTOMOBJECT)) {
            CustomObjectFile objectFile = customObjectFileRepository.findOne(file.getId());
            if (objectFile.getParentFile() != null) {
                path = visitCopyFileParentFolder(objectFile.getId(), path, file.getParentObject());
            } else {
                path = File.separator + objectFile.getObject() + File.separator + objectFile.getId();
            }
        }
        return path;
    }

    private String visitCopyFileParentFolder(Integer fileId, String path, ObjectType type) {
        if (type.equals(ObjectType.CUSTOMOBJECT)) {
            CustomObjectFile customObjectFile = customObjectFileRepository.findOne(fileId);
            if (customObjectFile.getParentFile() != null) {
                path = File.separator + customObjectFile.getId() + path;
                path = visitCopyFileParentFolder(customObjectFile.getParentFile(), path, type);
            } else {
                path = File.separator + customObjectFile.getId() + File.separator + customObjectFile.getId() + path;
                return path;
            }
        }

        return path;
    }

    public List<CustomFileDownloadHistory> getDownloadHistory(Integer fileId) {
        List<CustomFileDownloadHistory> fileDownloadHistories = customFileDownloadHistoryRepository.findByFileIdOrderByDownloadDateAsc(fileId);
        return fileDownloadHistories;
    }

    @Transactional
    public CustomFileDownloadHistory fileDownloadHistory(Integer id, Integer fileId) {
        CustomFileDownloadHistory customFileDownloadHistory = new CustomFileDownloadHistory();
        CustomFile customFile = customFileRepository.findOne(fileId);
        CustomObject customObject = customObjectRepository.findOne(id);
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        customFileDownloadHistory.setFileId(fileId);
        customFileDownloadHistory.setPerson(person);
        customFileDownloadHistory.setDownloadDate(new Date());
        customFileDownloadHistory = customFileDownloadHistoryRepository.save(customFileDownloadHistory);
        return customFileDownloadHistory;
    }

    public long Zip(String zipFilename, String[] files, String type) throws FileNotFoundException, IOException {
        return ZipFile(zipFilename, files, false, type);
    }

    public long ZipFile(String zipFilename, String[] files, Boolean includePath, String type) throws FileNotFoundException, IOException {
        BufferedInputStream origin = null;
        FileOutputStream dest = new FileOutputStream(zipFilename);
        CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
        //out.setMethod(ZipOutputStream.DEFLATED);
        byte data[] = new byte[BUFFER];
        // get a list of files from current directory
        for (int i = 0; i < files.length; i++) {
            generateFileList(data, out, "", files[i], type);

        }
        out.close();
        return checksum.getChecksum().getValue();
    }


    public void generateFileList(byte data[], ZipOutputStream out, String sourceFolder, String file, String type) throws IOException {
        // add file only
        File node = new File(file);
//        if (sourceFolder == "" || sourceFolder == null)
        sourceFolder = node.getAbsolutePath().substring(0, node.getAbsolutePath().lastIndexOf(File.separator));
        if (node.isFile()) {
            //System.out.println("Adding: "+file);
            FileInputStream fi = new FileInputStream(file);
            BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
            String zipfname = file.substring(sourceFolder.length() + 1, file.length());
            Integer fileId = Integer.parseInt(zipfname);
            if (type.equals("CUSTOMOBJECT")) {
                CustomObjectFile customObjectFile = customObjectFileRepository.findOne(fileId);
                if (customObjectFile != null && customObjectFile.getParentFile() != null) {
                    String path = getZipFileParentFileSystemPath(customObjectFile.getId(), type);
                    if (customObjectFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (customObjectFile != null && customObjectFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(customObjectFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            }
        }
        if (node.isDirectory()) {
            File[] subNote = node.listFiles();
            for (File filename : subNote) {
                generateFileList(data, out, sourceFolder, filename.getAbsolutePath(), type);
            }
            if (subNote.length == 0) {
                String zipfname = file.substring(sourceFolder.length() + 1, file.length());
                Integer fileId = Integer.parseInt(zipfname);
                String path = null;
                if (type.equals("CUSTOMOBJECT")) {
                    CustomObjectFile customObjectFile = customObjectFileRepository.findOne(fileId);
                    if (customObjectFile != null) {
                        if (customObjectFile.getParentFile() != null) {
                            path = getZipFileParentFileSystemPath(customObjectFile.getId(), "CUSTOMOBJECT") + File.separator;
                        } else {
                            path = customObjectFile.getName() + File.separator;
                        }
                    }
                }
                ZipEntry entry = new ZipEntry(path);
                out.putNextEntry(entry);
            }
        }

    }

    private String getZipFileParentFileSystemPath(Integer fileId, String type) {
        String path = "";
        if (type.equals("CUSTOMOBJECT")) {
            CustomObjectFile customObjectFile = customObjectFileRepository.findOne(fileId);
            path = customObjectFile.getName();
            if (customObjectFile.getParentFile() != null) {
                path = visitParentFileFolder(customObjectFile.getParentFile(), path, type);
            }
        }
        return path;
    }


    private String visitParentFileFolder(Integer fileId, String path, String type) {
        if (type.equals("CUSTOMOBJECT")) {
            CustomObjectFile customObjectFile = customObjectFileRepository.findOne(fileId);
            if (customObjectFile.getParentFile() != null) {
                path = customObjectFile.getName() + File.separator + path;
                path = visitParentFileFolder(customObjectFile.getParentFile(), path, type);
            } else {
                path = customObjectFile.getName() + File.separator + path;
                return path;
            }
        }
        return path;
    }
}
