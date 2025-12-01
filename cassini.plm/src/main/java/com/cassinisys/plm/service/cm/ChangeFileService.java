package com.cassinisys.plm.service.cm;

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
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.service.common.ForgeService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.*;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.plm.FileDownloadHistoryRepository;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.plm.FileHelpers;
import com.cassinisys.plm.service.plm.ItemFileService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyam on 09/06/20.
 */
@Service
@Transactional
public class ChangeFileService implements CrudService<PLMFile, Integer> {

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
    private FileRepository fileRepository;
    @Autowired
    private ItemFileService itemFileService;
    @Autowired
    private FileDownloadHistoryRepository fileDownloadHistoryRepository;
    @Autowired
    private FileHelpers fileHelpers;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ChangeFileRepository changeFileRepository;
    @Autowired
    private ChangeRepository changeRepository;
    @Autowired
    private ECRRepository ecrRepository;

    @Autowired
    private ECORepository ecoRepository;

    @Autowired
    private DCRRepository dcrRepository;

    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private VarianceRepository varianceRepository;

    @Autowired
    private ObjectFileService qualityFileService;

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private MCORepository mcoRepository;
    @Autowired
    private UtilService utilService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ChangeFileService changeFileService;
    @Autowired
    private FileDownloadService fileDownloadService;

    @Override
    public PLMFile create(PLMFile plmFile) {
        return null;
    }

    @Override
    public PLMFile update(PLMFile plmFile) {
        return null;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public PLMFile get(Integer integer) {
        return null;
    }

    @Override
    public List<PLMFile> getAll() {
        return null;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','file')")
    public List<PLMChangeFile> uploadChangeFiles(Integer id, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMChangeFile> uploaded = new ArrayList<>();
        List<PLMChangeFile> versionedFiles = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");

        PLMChange change = changeRepository.findOne(id);
        String[] fileExtension = null;
        boolean flag = true;

        List<PLMFile> fileList = new ArrayList<>();
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
                    PLMChangeFile changeFile = null;
                    Integer version = 1;
                    Integer oldVersion = 1;
                    String autoNumber1 = null;
                    PLMFile qualityFile = null;
                    Integer oldfile = null;
                    if (folderId == 0) {
                        changeFile = changeFileRepository.findByChangeAndNameAndParentFileIsNullAndLatestTrue(id, name);
                    } else {
                        changeFile = changeFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }

                    if (changeFile != null) {
                        changeFile.setLatest(false);
                        oldVersion = changeFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = changeFile.getFileNo();
                        oldfile = changeFile.getId();
                        changeFileRepository.save(changeFile);
                        versioned = true;

                    }
                    if (changeFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());

                        }
                    }

                    changeFile = new PLMChangeFile();
                    changeFile.setChange(id);
                    changeFile.setName(name);
                    changeFile.setCreatedBy(login.getPerson().getId());
                    changeFile.setModifiedBy(login.getPerson().getId());
                    changeFile.setSize(file.getSize());
                    changeFile.setFileType("FILE");
                    changeFile.setFileNo(autoNumber1);
                    changeFile.setVersion(version);
                    if (folderId != 0) {
                        changeFile.setParentFile(folderId);
                    }
                    changeFile = changeFileRepository.save(changeFile);
                    if (changeFile.getParentFile() != null) {
                        PLMFile file1 = fileRepository.findOne(changeFile.getParentFile());
                        file1.setModifiedDate(changeFile.getModifiedDate());
                        file1 = fileRepository.save(file1);
                    }
                    qualityFile = fileRepository.findOne(changeFile.getId());
                    qualityFile.setOldVersion(oldVersion);
                    fileList.add(qualityFile);
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
                    String path = dir + File.separator + qualityFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    /*Map<String, String> map = forgeService.uploadForgeFile(file.getOriginalFilename(), path);
                    if (map != null) {
                        changeFile.setUrn(map.get("urn"));
                        changeFile.setThumbnail(map.get("thumbnail"));
                        changeFile = changeFileRepository.save(changeFile);
                    }*/
                    uploaded.add(changeFile);
                    if (changeFile.getVersion() > 1) {
                        qualityFileService.copyFileAttributes(oldfile, changeFile.getId());
                    }
                    if (versioned) {
                        versionedFiles.add(changeFile);
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (uploaded.size() > 0) {
            if (change.getChangeType().equals(ChangeType.ECR)) {
                PLMECR plmecr = ecrRepository.findOne(change.getId());
                applicationEventPublisher.publishEvent(new ECREvents.ECRFilesAddedEvent(plmecr, uploaded));
            } else if (change.getChangeType().equals(ChangeType.ECO)) {
                PLMECO plmeco = ecoRepository.findOne(change.getId());
                applicationEventPublisher.publishEvent(new ECOEvents.ECOFilesAddedEvent(plmeco, uploaded));

            } else if (change.getChangeType().equals(ChangeType.DCR)) {
                PLMDCR plmdcr = dcrRepository.findOne(change.getId());
                applicationEventPublisher.publishEvent(new DCREvents.DCRFilesAddedEvent(plmdcr, uploaded));

            } else if (change.getChangeType().equals(ChangeType.DCO)) {
                PLMDCO plmdco = dcoRepository.findOne(change.getId());
                applicationEventPublisher.publishEvent(new DCOEvents.DCOFilesAddedEvent(plmdco, uploaded));

            } else if (change.getChangeType().equals(ChangeType.MCO)) {
                PLMMCO plmmco = mcoRepository.findOne(change.getId());
                applicationEventPublisher.publishEvent(new MCOEvents.MCOFilesAddedEvent(plmmco, uploaded));

            } else if (change.getChangeType().equals(ChangeType.DEVIATION) || change.getChangeType().equals(ChangeType.WAIVER)) {
                PLMVariance plmVariance = varianceRepository.findOne(change.getId());
                applicationEventPublisher.publishEvent(new VarianceEvents.VarianceFilesAddedEvent(plmVariance, uploaded));

            }
        }
        if (versionedFiles.size() > 0) {
            if (change.getChangeType().equals(ChangeType.ECR)) {
                PLMECR plmecr = ecrRepository.findOne(change.getId());
                applicationEventPublisher.publishEvent(new ECREvents.ECRFilesVersionedEvent(plmecr, versionedFiles));
            } else if (change.getChangeType().equals(ChangeType.ECO)) {
                PLMECO plmeco = ecoRepository.findOne(change.getId());
                applicationEventPublisher.publishEvent(new ECOEvents.ECOFilesVersionedEvent(plmeco, versionedFiles));

            } else if (change.getChangeType().equals(ChangeType.DCR)) {
                PLMDCR plmdcr = dcrRepository.findOne(change.getId());
                applicationEventPublisher.publishEvent(new DCREvents.DCRFilesVersionedEvent(plmdcr, versionedFiles));

            } else if (change.getChangeType().equals(ChangeType.DCO)) {
                PLMDCO plmdco = dcoRepository.findOne(change.getId());
                applicationEventPublisher.publishEvent(new DCOEvents.DCOFilesVersionedEvent(plmdco, versionedFiles));

            } else if (change.getChangeType().equals(ChangeType.MCO)) {
                PLMMCO plmmco = mcoRepository.findOne(change.getId());
                applicationEventPublisher.publishEvent(new MCOEvents.MCOFilesVersionedEvent(plmmco, versionedFiles));

            } else if (change.getChangeType().equals(ChangeType.DEVIATION) || change.getChangeType().equals(ChangeType.WAIVER)) {
                PLMVariance variance = varianceRepository.findOne(change.getId());
                applicationEventPublisher.publishEvent(new VarianceEvents.VarianceFilesVersionedEvent(variance, versionedFiles));

            }
        }
        return uploaded;
    }

    @Transactional(readOnly = true)
    public List<PLMChangeFile> getChangeFiles(Integer id) {
        List<PLMChangeFile> changeFiles = changeFileRepository.findByChangeAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(id);
        changeFiles.forEach(changeFile -> {
            Person person = personRepository.findOne(changeFile.getCreatedBy());
            changeFile.setCreatedPerson(person.getFirstName());
            changeFile.setParentObject(PLMObjectType.CHANGE);
            if (changeFile.getFileType().equals("FOLDER")) {
                changeFile.setCount(changeFileRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(changeFile.getId()).size());
            }
        });

        return changeFiles;
    }

    @Transactional
    @PreAuthorize("hasPermission(#fileId ,'edit')")
    public PLMChangeFile updateChangeFile(Integer id, Integer fileId, PLMChangeFile changeFile) {
        PLMChangeFile existFile = changeFileRepository.findOne(fileId);
        PLMChange change = changeRepository.findOne(id);
        if (existFile != null && changeFile != null) {
            if (changeFile.getDescription() != null) {
                existFile.setDescription(changeFile.getDescription());
            }
            if (changeFile.getLocked()) {
                existFile.setLocked(true);
                existFile.setLockedBy(changeFile.getLockedBy());
                existFile.setLockedDate(new Date());
                if (change.getChangeType().equals(ChangeType.ECR)) {
                    PLMECR plmecr = ecrRepository.findOne(change.getId());
                    applicationEventPublisher.publishEvent(new ECREvents.ECRFileLockedEvent(plmecr, existFile));
                } else if (change.getChangeType().equals(ChangeType.DCR)) {
                    PLMDCR plmdcr = dcrRepository.findOne(change.getId());
                    applicationEventPublisher.publishEvent(new DCREvents.DCRFileLockedEvent(plmdcr, existFile));

                } else if (change.getChangeType().equals(ChangeType.DCO)) {
                    PLMDCO plmdco = dcoRepository.findOne(change.getId());
                    applicationEventPublisher.publishEvent(new DCOEvents.DCOFileLockedEvent(plmdco, existFile));

                } else if (change.getChangeType().equals(ChangeType.MCO)) {
                    PLMMCO plmmco = mcoRepository.findOne(change.getId());
                    applicationEventPublisher.publishEvent(new MCOEvents.MCOFileLockedEvent(plmmco, existFile));
                } else if (change.getChangeType().equals(ChangeType.ECO)) {
                    PLMECO plmeco = ecoRepository.findOne(change.getId());
                    applicationEventPublisher.publishEvent(new ECOEvents.ECOFileLockedEvent(plmeco, existFile));

                } else if (change.getChangeType().equals(ChangeType.DEVIATION) || change.getChangeType().equals(ChangeType.WAIVER)) {
                    PLMVariance variance = varianceRepository.findOne(change.getId());
                    applicationEventPublisher.publishEvent(new VarianceEvents.VarianceFileLockedEvent(variance, existFile));

                }
            } else {
                existFile.setLocked(false);
                existFile.setLockedBy(null);
                existFile.setLockedDate(null);
                if (change.getChangeType().equals(ChangeType.ECR)) {
                    PLMECR plmecr = ecrRepository.findOne(change.getId());
                    applicationEventPublisher.publishEvent(new ECREvents.ECRFileUnlockedEvent(plmecr, existFile));
                } else if (change.getChangeType().equals(ChangeType.DCR)) {
                    PLMDCR plmdcr = dcrRepository.findOne(change.getId());
                    applicationEventPublisher.publishEvent(new DCREvents.DCRFileUnlockedEvent(plmdcr, existFile));

                } else if (change.getChangeType().equals(ChangeType.DCO)) {
                    PLMDCO plmdco = dcoRepository.findOne(change.getId());
                    applicationEventPublisher.publishEvent(new DCOEvents.DCOFileUnlockedEvent(plmdco, existFile));

                } else if (change.getChangeType().equals(ChangeType.MCO)) {
                    PLMMCO plmmco = mcoRepository.findOne(change.getId());
                    applicationEventPublisher.publishEvent(new MCOEvents.MCOFileUnlockedEvent(plmmco, existFile));
                } else if (change.getChangeType().equals(ChangeType.ECO)) {
                    PLMECO plmeco = ecoRepository.findOne(change.getId());
                    applicationEventPublisher.publishEvent(new ECOEvents.ECOFileUnlockedEvent(plmeco, existFile));

                } else if (change.getChangeType().equals(ChangeType.DEVIATION) || change.getChangeType().equals(ChangeType.WAIVER)) {
                    PLMVariance variance = varianceRepository.findOne(change.getId());
                    applicationEventPublisher.publishEvent(new VarianceEvents.VarianceFileUnlockedEvent(variance, existFile));

                }
            }
            existFile = changeFileRepository.save(existFile);
        }

        return changeFile;
    }

    @Transactional
    public PLMChangeFile createFolder(Integer id, PLMChangeFile changeFile) {
        String folderNumber = null;
        PLMFile plmFile = null;
        PLMChange change = changeRepository.findOne(id);
        PLMChangeFile existFolderName = null;
        if (changeFile.getParentFile() != null) {
            existFolderName = changeFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndChangeAndLatestTrue(changeFile.getName(), changeFile.getParentFile(), id);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(changeFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", changeFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = changeFileRepository.findByChangeAndNameAndParentFileIsNullAndLatestTrue(id, changeFile.getName());
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", changeFile.getName(), existFolderName.getName());
                throw new CassiniException(result);
            }
        }
        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        changeFile.setChange(id);
        changeFile.setFileNo(folderNumber);
        changeFile.setFileType("FOLDER");
        changeFile = changeFileRepository.save(changeFile);
        if (changeFile.getParentFile() != null) {
            PLMChangeFile parent = changeFileRepository.findOne(changeFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = changeFileRepository.save(parent);
        }
        plmFile = fileRepository.findOne(changeFile.getId());

        if (change.getChangeType().equals(ChangeType.ECR)) {
            PLMECR plmecr = ecrRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new ECREvents.ECRFoldersAddedEvent(plmecr, changeFile));
        } else if (change.getChangeType().equals(ChangeType.DCR)) {
            PLMDCR plmdcr = dcrRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new DCREvents.DCRFoldersAddedEvent(plmdcr, changeFile));

        } else if (change.getChangeType().equals(ChangeType.DCO)) {
            PLMDCO plmdco = dcoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new DCOEvents.DCOFoldersAddedEvent(plmdco, changeFile));

        } else if (change.getChangeType().equals(ChangeType.MCO)) {
            PLMMCO plmmco = mcoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new MCOEvents.MCOFoldersAddedEvent(plmmco, changeFile));
        } else if (change.getChangeType().equals(ChangeType.ECO)) {
            PLMECO plmeco = ecoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new ECOEvents.ECOFoldersAddedEvent(plmeco, changeFile));

        } else if (change.getChangeType().equals(ChangeType.DEVIATION) || change.getChangeType().equals(ChangeType.WAIVER)) {
            PLMVariance variance = varianceRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new VarianceEvents.VarianceFoldersAddedEvent(variance, changeFile));

        }
        if (plmFile != null) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(id, plmFile.getId());
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
        }

        return changeFile;
    }

    private String getParentFileSystemPath(Integer id, Integer fileId) {
        String path = "";
        PLMChangeFile file = changeFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (file.getParentFile() != null) {
            path = utilService.visitParentFolder(id, file.getParentFile(), path);
        } else {
            path = File.separator + id + File.separator + file.getId();
        }
        return path;
    }

    @Transactional(readOnly = true)
    public List<PLMChangeFile> getFolderChildren(Integer id, Integer folderId) {
        List<PLMChangeFile> changeFiles = changeFileRepository.findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(folderId);
        changeFiles.forEach(changeFile -> {
            changeFile.setParentObject(PLMObjectType.CHANGE);
            if (changeFile.getFileType().equals("FOLDER")) {
                changeFile.setCount(changeFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(changeFile.getId()).size());
            }
        });

        return changeFiles;
    }

    @Transactional
    public PLMChangeFile updateFileName(Integer id, Integer fileId, String newFileName) throws IOException {
        String oldFileDir = "";
        String dir = "";
        PLMChangeFile changeFile = changeFileRepository.findOne(fileId);
        PLMChange change = changeRepository.findOne(id);
        changeFile.setLatest(false);
        changeFile = changeFileRepository.save(changeFile);
        PLMChangeFile plmChangeFile = (PLMChangeFile) Utils.cloneObject(changeFile, PLMChangeFile.class);
        if (plmChangeFile != null) {
            plmChangeFile.setId(null);
            plmChangeFile.setName(newFileName);
            plmChangeFile.setVersion(changeFile.getVersion() + 1);
            plmChangeFile.setLatest(true);
            plmChangeFile.setReplaceFileName(changeFile.getName() + " ReName to " + newFileName);
            plmChangeFile = changeFileRepository.save(plmChangeFile);
            if (plmChangeFile.getParentFile() != null) {
                PLMChangeFile file = changeFileRepository.findOne(plmChangeFile.getParentFile());
                file.setModifiedDate(plmChangeFile.getModifiedDate());
                file = changeFileRepository.save(file);
            }
            qualityFileService.copyFileAttributes(changeFile.getId(), plmChangeFile.getId());
            dir = "";
            if (changeFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
            }
            dir = dir + File.separator + plmChangeFile.getId();
            oldFileDir = "";
            if (plmChangeFile.getParentFile() != null) {
                oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(id, fileId);
            } else {
                oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id + File.separator + fileId;
            }
        }

        if (change.getChangeType().equals(ChangeType.ECR)) {
            PLMECR plmecr = ecrRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new ECREvents.ECRFileRenamedEvent(plmecr, changeFile, plmChangeFile, "Rename"));
        } else if (change.getChangeType().equals(ChangeType.DCR)) {
            PLMDCR plmdcr = dcrRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new DCREvents.DCRFileRenamedEvent(plmdcr, changeFile, plmChangeFile, "Rename"));

        } else if (change.getChangeType().equals(ChangeType.DCO)) {
            PLMDCO plmdco = dcoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new DCOEvents.DCOFileRenamedEvent(plmdco, changeFile, plmChangeFile, "Rename"));

        } else if (change.getChangeType().equals(ChangeType.MCO)) {
            PLMMCO plmmco = mcoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new MCOEvents.MCOFileRenamedEvent(plmmco, changeFile, plmChangeFile, "Rename"));
        } else if (change.getChangeType().equals(ChangeType.ECO)) {
            PLMECO plmeco = ecoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new ECOEvents.ECOFileRenamedEvent(plmeco, changeFile, plmChangeFile, "Rename"));

        } else if (change.getChangeType().equals(ChangeType.DEVIATION) || change.getChangeType().equals(ChangeType.WAIVER)) {
            PLMVariance variance = varianceRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new VarianceEvents.VarianceFileRenamedEvent(variance, changeFile, plmChangeFile, "Rename"));

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

        return plmChangeFile;
    }

    private String getReplaceFileSystemPath(Integer id, Integer fileId) {
        String path = "";
        PLMChangeFile file = changeFileRepository.findOne(fileId);
        if (file.getParentFile() != null) {
            path = utilService.visitParentFolder(id, file.getParentFile(), path);
        } else {
            path = File.separator + id;
        }
        return path;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'replace','file')")
    public PLMChangeFile replaceChangeFile(Integer id, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        PLMChange change = changeRepository.findOne(id);
        String[] fileExtension = null;
        String htmlTable = null;
        boolean flag = true;
        String name = null;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }

        PLMChangeFile changeFile = null;
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
                    PLMChangeFile newChangeFile = null;
                    changeFile = changeFileRepository.findOne(fileId);
                    if (changeFile != null) {
                        if (changeFile.getParentFile() != null) {
                            PLMChangeFile folder = changeFileRepository.findOne(changeFile.getParentFile());
                            PLMChangeFile existFile = changeFileRepository.findByParentFileAndNameAndLatestTrue(changeFile.getParentFile(), name);
                            if (existFile != null && !existFile.getId().equals(changeFile.getId())) {
                                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                                String result = MessageFormat.format(message + ".", changeFile.getName(), folder.getName());
                                throw new CassiniException(result);
                            }
                        } else {
                            PLMChangeFile existFile = changeFileRepository.findByChangeAndNameAndParentFileIsNullAndLatestTrue(changeFile.getChange(), name);
                            if (existFile != null && !existFile.getId().equals(changeFile.getId())) {
                                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                                String result = MessageFormat.format(message + ".", existFile.getName());
                                throw new CassiniException(result);
                            }
                        }

                        changeFile.setLatest(false);
                        changeFile = changeFileRepository.save(changeFile);
                    }

                    newChangeFile = new PLMChangeFile();
                    newChangeFile.setName(name);
                    if (changeFile != null && changeFile.getParentFile() != null) {
                        newChangeFile.setParentFile(changeFile.getParentFile());
                    }
                    if (changeFile != null) {
                        newChangeFile.setFileNo(changeFile.getFileNo());
                        newChangeFile.setVersion(changeFile.getVersion() + 1);
                        newChangeFile.setReplaceFileName(changeFile.getName() + " Replaced to " + name);
                    }
                    newChangeFile.setCreatedBy(login.getPerson().getId());
                    newChangeFile.setModifiedBy(login.getPerson().getId());
                    newChangeFile.setChange(id);
                    newChangeFile.setFileType("FILE");
                    newChangeFile.setSize(file.getSize());
                    newChangeFile = changeFileRepository.save(newChangeFile);
                    if (newChangeFile.getParentFile() != null) {
                        PLMChangeFile plmChangeFile = changeFileRepository.findOne(newChangeFile.getParentFile());
                        plmChangeFile.setModifiedDate(newChangeFile.getModifiedDate());
                        plmChangeFile = changeFileRepository.save(plmChangeFile);
                    }
                    if (changeFile != null) {
                        qualityFileService.copyFileAttributes(changeFile.getId(), newChangeFile.getId());
                    }
                    String dir = "";
                    if (changeFile != null && changeFile.getParentFile() != null) {
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
                    String path = dir + File.separator + newChangeFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    if (change.getChangeType().equals(ChangeType.ECR)) {
                        PLMECR plmecr = ecrRepository.findOne(change.getId());
                        applicationEventPublisher.publishEvent(new ECREvents.ECRFileRenamedEvent(plmecr, changeFile, newChangeFile, "Replace"));
                    } else if (change.getChangeType().equals(ChangeType.DCR)) {
                        PLMDCR plmdcr = dcrRepository.findOne(change.getId());
                        applicationEventPublisher.publishEvent(new DCREvents.DCRFileRenamedEvent(plmdcr, changeFile, newChangeFile, "Replace"));

                    } else if (change.getChangeType().equals(ChangeType.DCO)) {
                        PLMDCO plmdco = dcoRepository.findOne(change.getId());
                        applicationEventPublisher.publishEvent(new DCOEvents.DCOFileRenamedEvent(plmdco, changeFile, newChangeFile, "Replace"));

                    } else if (change.getChangeType().equals(ChangeType.MCO)) {
                        PLMMCO plmmco = mcoRepository.findOne(change.getId());
                        applicationEventPublisher.publishEvent(new MCOEvents.MCOFileRenamedEvent(plmmco, changeFile, newChangeFile, "Replace"));
                    } else if (change.getChangeType().equals(ChangeType.ECO)) {
                        PLMECO plmeco = ecoRepository.findOne(change.getId());
                        applicationEventPublisher.publishEvent(new ECOEvents.ECOFileRenamedEvent(plmeco, changeFile, newChangeFile, "Replace"));

                    } else if (change.getChangeType().equals(ChangeType.DEVIATION) || change.getChangeType().equals(ChangeType.WAIVER)) {
                        PLMVariance variance = varianceRepository.findOne(change.getId());
                        applicationEventPublisher.publishEvent(new VarianceEvents.VarianceFileRenamedEvent(variance, changeFile, newChangeFile, "Replace"));
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return changeFile;
    }

    @Transactional(readOnly = true)
    public PLMChangeFile getLatestUploadedFile(Integer id, Integer fileId) {
        PLMChangeFile changeFile = changeFileRepository.findOne(fileId);
        PLMChangeFile latestFile = changeFileRepository.findByChangeAndFileNoAndLatestTrue(changeFile.getChange(), changeFile.getFileNo());
        return latestFile;
    }

    @Transactional
    @PreAuthorize("hasPermission(#fileId,'delete')")
    public void deleteChangeFile(Integer id, Integer fileId) {
        String fileName = "";
        PLMChangeFile plmItemFile = changeFileRepository.findOne(fileId);
        PLMChange change = changeRepository.findOne(id);
        List<PLMChangeFile> inspectionPlanFileList = changeFileRepository.findByChangeAndFileNo(id, plmItemFile.getFileNo());
        fileName = plmItemFile.getName();
        for (PLMChangeFile changeFile : inspectionPlanFileList) {
            if (changeFile == null) {
                throw new ResourceNotFoundException();
            }
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(changeFile.getChange(), changeFile.getId());
            fileSystemService.deleteDocumentFromDisk(changeFile.getId(), dir);
            changeFileRepository.delete(changeFile.getId());
        }

        if (change.getChangeType().equals(ChangeType.ECR)) {
            PLMECR plmecr = ecrRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new ECREvents.ECRFileDeletedEvent(plmecr, plmItemFile));
        } else if (change.getChangeType().equals(ChangeType.DCR)) {
            PLMDCR plmdcr = dcrRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new DCREvents.DCRFileDeletedEvent(plmdcr, plmItemFile));

        } else if (change.getChangeType().equals(ChangeType.DCO)) {
            PLMDCO plmdco = dcoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new DCOEvents.DCOFileDeletedEvent(plmdco, plmItemFile));

        } else if (change.getChangeType().equals(ChangeType.MCO)) {
            PLMMCO plmmco = mcoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new MCOEvents.MCOFileDeletedEvent(plmmco, plmItemFile));
        } else if (change.getChangeType().equals(ChangeType.ECO)) {
            PLMECO plmeco = ecoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new ECOEvents.ECOFileDeletedEvent(plmeco, plmItemFile));
        } else if (change.getChangeType().equals(ChangeType.DEVIATION) || change.getChangeType().equals(ChangeType.WAIVER)) {
            PLMVariance variance = varianceRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new VarianceEvents.VarianceFileDeletedEvent(variance, plmItemFile));

        }

        if (plmItemFile.getParentFile() != null) {
            PLMChangeFile plmChangeFile = changeFileRepository.findOne(plmItemFile.getParentFile());
            plmChangeFile.setModifiedDate(new Date());
            plmChangeFile = changeFileRepository.save(plmChangeFile);
        }
    }

    private String getFolderName(String folderName, Integer folderId) {
        PLMFile folder = changeFileRepository.findOne(folderId);
        if (folder.getParentFile() != null) {
            PLMFile parentFolder = changeFileRepository.findOne(folder.getParentFile());
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
        PLMChange change = changeRepository.findOne(id);
        PLMFile file = fileRepository.findOne(folderId);
        List<PLMChangeFile> changeFiles = changeFileRepository.findByParentFileOrderByFileTypeDescModifiedDateDesc(folderId);
        utilService.removeFileIfExist((List) changeFiles, dir);
        if (change.getChangeType().equals(ChangeType.ECR)) {
            PLMECR plmecr = ecrRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new ECREvents.ECRFoldersDeletedEvent(plmecr, file));
        } else if (change.getChangeType().equals(ChangeType.DCR)) {
            PLMDCR plmdcr = dcrRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new DCREvents.DCRFoldersDeletedEvent(plmdcr, file));

        } else if (change.getChangeType().equals(ChangeType.DCO)) {
            PLMDCO plmdco = dcoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new DCOEvents.DCOFoldersDeletedEvent(plmdco, file));

        } else if (change.getChangeType().equals(ChangeType.MCO)) {
            PLMMCO plmmco = mcoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new MCOEvents.MCOFileDeletedEvent(plmmco, file));
        } else if (change.getChangeType().equals(ChangeType.ECO)) {
            PLMECO plmeco = ecoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new ECOEvents.ECOFoldersDeletedEvent(plmeco, file));

        } else if (change.getChangeType().equals(ChangeType.DEVIATION) || change.getChangeType().equals(ChangeType.WAIVER)) {
            PLMVariance variance = varianceRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new VarianceEvents.VarianceFileDeletedEvent(variance, file));

        }
        changeFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        File fDir = new File(dir);
        Utils.removeDirIfExist(fDir);
    }

    @Transactional
    public PLMChangeFile moveChangeFileToFolder(Integer id, PLMChangeFile changeFile) throws Exception {

        PLMChangeFile file = changeFileRepository.findOne(changeFile.getId());
        PLMChangeFile existFile = (PLMChangeFile) Utils.cloneObject(file, PLMChangeFile.class);
        PLMFile plmFile = fileRepository.findOne(changeFile.getId());

        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getChange(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getChange() + File.separator + existFile.getId();
        }
        if (changeFile.getParentFile() != null) {
            PLMChangeFile existItemFile = changeFileRepository.findByParentFileAndNameAndLatestTrue(changeFile.getParentFile(), changeFile.getName());
            PLMChangeFile folder = changeFileRepository.findOne(changeFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                changeFile.setParentFile(changeFile.getParentFile());
                changeFile = changeFileRepository.save(changeFile);
            }
        } else {
            PLMChangeFile existItemFile = changeFileRepository.findByChangeAndNameAndParentFileIsNullAndLatestTrue(changeFile.getChange(), changeFile.getName());
            if (existItemFile != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                changeFile = changeFileRepository.save(changeFile);
            }
        }

        if (changeFile != null) {
            String dir = "";
            if (changeFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(changeFile.getChange(), changeFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + changeFile.getChange() + File.separator + changeFile.getId();
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
            List<PLMChangeFile> oldVersionFiles = changeFileRepository.findByChangeAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getChange(), existFile.getFileNo());
            for (PLMChangeFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getChange(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getChange() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(changeFile.getChange(), changeFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(changeFile.getParentFile());
                oldVersionFile = changeFileRepository.save(oldVersionFile);
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

        return changeFile;
    }

    @Transactional
    public List<PLMChangeFile> pasteFromClipboard(Integer id, Integer fileId, List<PLMFile> files) {
        List<PLMChangeFile> fileList = new ArrayList<>();
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
                PLMChangeFile changeFile = new PLMChangeFile();
                PLMChangeFile existFile = null;
                if (fileId != 0) {
                    changeFile.setParentFile(fileId);
                    existFile = changeFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndChangeAndLatestTrue(file.getName(), fileId, id);
                } else {
                    existFile = changeFileRepository.findByChangeAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
                }
                if (existFile == null) {
                    changeFile.setName(file.getName());
                    changeFile.setDescription(file.getDescription());
                    changeFile.setChange(id);
                    changeFile.setVersion(1);
                    changeFile.setSize(file.getSize());
                    changeFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    changeFile.setFileNo(autoNumber1);
                    changeFile.setFileType("FILE");
                    changeFile = changeFileRepository.save(changeFile);
                    PLMFile plmFile = fileRepository.findOne(changeFile.getId());
                    plmFile.setOldVersion(plmFile.getVersion());
                    fileList.add(changeFile);

                    changeFile.setOldVersion(changeFile.getVersion());

                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (changeFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(id, changeFile.getId());
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + id + File.separator + changeFile.getId();
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
                PLMChangeFile changeFile = new PLMChangeFile();
                PLMChangeFile existFile = null;
                if (fileId != 0) {
                    changeFile.setParentFile(fileId);
                    existFile = changeFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndChangeAndLatestTrue(file.getName(), fileId, id);
                } else {
                    existFile = changeFileRepository.findByChangeAndNameAndParentFileIsNullAndLatestTrue(id, file.getName());
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
                    changeFile.setChange(id);
                    changeFile.setFileNo(folderNumber);
                    changeFile.setFileType("FOLDER");
                    changeFile = changeFileRepository.save(changeFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, changeFile.getId());
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(changeFile);
                    copyFolderFiles(id, file.getParentObject(), file.getId(), changeFile.getId());
                }
            }
        }

        return fileList;
    }

    private void copyFolderFiles(Integer id, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        plmFiles.forEach(plmFile -> {
            PLMChangeFile changeFile = new PLMChangeFile();
            changeFile.setParentFile(parent);
            changeFile.setName(plmFile.getName());
            changeFile.setDescription(plmFile.getDescription());
            changeFile.setChange(id);
            String folderNumber = null;
            if (plmFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                changeFile.setVersion(1);
                changeFile.setFileNo(folderNumber);
                changeFile.setSize(plmFile.getSize());
                changeFile.setFileType("FILE");
                changeFile = changeFileRepository.save(changeFile);
                changeFile.setParentObject(PLMObjectType.CHANGE);
                plmFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmFile);

                String dir = "";
                if (changeFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, changeFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + changeFile.getId();
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
                changeFile.setVersion(1);
                changeFile.setSize(0L);
                changeFile.setFileNo(folderNumber);
                changeFile.setFileType("FOLDER");
                changeFile = changeFileRepository.save(changeFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(id, changeFile.getId());
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(id, objectType, plmFile.getId(), changeFile.getId());
            }
        });
    }

    @Transactional
    public void undoCopiedItemFiles(Integer id, List<PLMChangeFile> changeFiles) {
        changeFiles.forEach(inspectionPlanFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(id, inspectionPlanFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(inspectionPlanFile.getId(), dir);
            changeFileRepository.delete(inspectionPlanFile.getId());
        });
    }

    @Transactional
    public PLMFileDownloadHistory fileDownloadHistory(Integer id, Integer fileId) {
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        PLMFile plmFile = fileRepository.findOne(fileId);
        PLMChange change = changeRepository.findOne(id);
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        if (change.getChangeType().equals(ChangeType.ECR)) {
            PLMECR plmecr = ecrRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new ECREvents.ECRFileDownloadedEvent(plmecr, plmFile));
        } else if (change.getChangeType().equals(ChangeType.DCR)) {
            PLMDCR plmdcr = dcrRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new DCREvents.DCRFileDownloadedEvent(plmdcr, plmFile));

        } else if (change.getChangeType().equals(ChangeType.DCO)) {
            PLMDCO plmdco = dcoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new DCOEvents.DCOFileDownloadedEvent(plmdco, plmFile));

        } else if (change.getChangeType().equals(ChangeType.MCO)) {
            PLMMCO plmmco = mcoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new MCOEvents.MCOFileDownloadedEvent(plmmco, plmFile));
        } else if (change.getChangeType().equals(ChangeType.ECO)) {
            PLMECO plmeco = ecoRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new ECOEvents.ECOFileDownloadedEvent(plmeco, plmFile));

        } else if (change.getChangeType().equals(ChangeType.DEVIATION) || change.getChangeType().equals(ChangeType.WAIVER)) {
            PLMVariance variance = varianceRepository.findOne(change.getId());
            applicationEventPublisher.publishEvent(new VarianceEvents.VarianceFileDownloadedEvent(variance, plmFile));

        }
        return plmFileDownloadHistory;
    }

    @Transactional
    public PLMFile getChangeFile(Integer id) {
        return fileRepository.findOne(id);
    }

    @Transactional
    public File getFile(Integer id, Integer fileId) {
        checkNotNull(id);
        checkNotNull(fileId);

        PLMFile plmFile = fileRepository.findOne(fileId);
        if (plmFile == null) {
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
    public void generateZipFile(Integer id, PLMObjectType objectType, HttpServletResponse response) throws FileNotFoundException, IOException {
        ArrayList<String> fileList = new ArrayList<>();
        String number = "";
        if (objectType.equals(PLMObjectType.ECO)) {
            PLMECO plmeco = ecoRepository.findOne(id);
            number = plmeco.getEcoNumber();
        }
        if (objectType.equals(PLMObjectType.ECR)) {
            PLMECR plmecr = ecrRepository.findOne(id);
            number = plmecr.getCrNumber();
        }
        if (objectType.equals(PLMObjectType.DCR)) {
            PLMDCR plmdcr = dcrRepository.findOne(id);
            number = plmdcr.getCrNumber();
        }
        if (objectType.equals(PLMObjectType.DCO)) {
            PLMDCO plmdco = dcoRepository.findOne(id);
            number = plmdco.getDcoNumber();
        }
        if (objectType.equals(PLMObjectType.VARIANCE)) {
            PLMVariance plmVariance = varianceRepository.findOne(id);
            number = plmVariance.getVarianceNumber();
        }

        List<PLMChangeFile> inspectionPlanFileList = changeFileRepository.findByChangeAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(id);
        inspectionPlanFileList.forEach(inspectionPlanFile -> {
            File file = getFile(id, inspectionPlanFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = number + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "CHANGE",id);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional(readOnly = true)
    public List<PLMChangeFile> findByTypeAndFileName(Integer id, String name) {

        List<PLMChangeFile> changeFiles = changeFileRepository.findByChangeAndNameContainingIgnoreCaseAndLatestTrueOrderByFileTypeDescModifiedDateDesc(id, name);

        return changeFiles;
    }

    @Transactional(readOnly = true)
    public List<PLMChangeFile> getAllFileVersionComments(Integer fileId, ObjectType objectType) {
        List<PLMChangeFile> changeFiles = new ArrayList<>();
        PLMChangeFile inspectionPlanFile = changeFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(inspectionPlanFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(inspectionPlanFile.getId());
        if (comments.size() > 0) {
            inspectionPlanFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            inspectionPlanFile.setDownloadHistories(fileDownloadHistories);
        }
        changeFiles.add(inspectionPlanFile);
        List<PLMChangeFile> files = changeFileRepository.findByChangeAndFileNoAndLatestFalseOrderByCreatedDateDesc(inspectionPlanFile.getChange(), inspectionPlanFile.getFileNo());
        if (files.size() > 0) {
            for (PLMChangeFile file : files) {
                List<Comment> oldVersionComments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType);
                if (oldVersionComments.size() > 0) {
                    file.setComments(oldVersionComments);
                }
                List<PLMFileDownloadHistory> oldFileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId());
                if (oldFileDownloadHistories.size() > 0) {
                    file.setDownloadHistories(oldFileDownloadHistories);
                }
                changeFiles.add(file);
            }
        }

        return changeFiles;
    }

    @Transactional(readOnly = true)
    public List<PLMChangeFile> getAllFileVersions(Integer fileId) {
        List<PLMChangeFile> changeFiles = new ArrayList<>();
        PLMChangeFile plmItemFile = changeFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(plmItemFile.getId(), ObjectType.valueOf("CHANGE"));
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(plmItemFile.getId());
        if (comments.size() > 0) {
            plmItemFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            plmItemFile.setDownloadHistories(fileDownloadHistories);
        }
        changeFiles.add(plmItemFile);
        List<PLMChangeFile> files = changeFileRepository.findByChangeAndFileNoAndLatestFalseOrderByCreatedDateDesc(plmItemFile.getChange(), plmItemFile.getFileNo());
        if (files.size() > 0) {
            for (PLMChangeFile file : files) {
                List<Comment> oldVersionComments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), ObjectType.valueOf("CHANGE"));
                if (oldVersionComments.size() > 0) {
                    file.setComments(oldVersionComments);
                }
                List<PLMFileDownloadHistory> oldFileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId());
                if (oldFileDownloadHistories.size() > 0) {
                    file.setDownloadHistories(oldFileDownloadHistories);
                }
                changeFiles.add(file);
            }
        }

        return changeFiles;
    }

    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'download','file')")
    public void downloadFile(Integer id, Integer fileId, HttpServletResponse response) {
        PLMFile plmFile = changeFileService.getChangeFile(fileId);
        File file = changeFileService.getFile(id, fileId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, '"' + plmFile.getName() + '"', file);
        }
    }

    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'preview','file')")
    public void previewFile(Integer id, Integer fileId, HttpServletResponse response) throws Exception {
        PLMFile plmFile = changeFileService.getChangeFile(fileId);
        File file = changeFileService.getFile(id, fileId);
        String fileName = plmFile.getName();
        if (file != null) {
            try {
                String e = URLDecoder.decode(fileName, "UTF-8");
                response.setHeader("Content-disposition", "inline; filename=" + e);
            } catch (UnsupportedEncodingException var6) {
                response.setHeader("Content-disposition", "inline; filename=" + fileName);
            }
            ServletOutputStream e1 = response.getOutputStream();
            IOUtils.copy(new FileInputStream(file), e1);
            e1.flush();
        }
    }

}
