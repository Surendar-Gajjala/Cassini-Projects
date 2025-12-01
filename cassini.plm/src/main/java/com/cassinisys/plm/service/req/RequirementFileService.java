package com.cassinisys.plm.service.req;

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
import com.cassinisys.plm.event.RequirementEvents;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.req.PLMRequirementDocumentChildren;
import com.cassinisys.plm.model.req.PLMRequirementFile;
import com.cassinisys.plm.model.req.PLMRequirementVersion;
import com.cassinisys.plm.repo.plm.FileDownloadHistoryRepository;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.req.PLMRequirementFileRepository;
import com.cassinisys.plm.repo.req.PLMRequirementVersionRepository;
import com.cassinisys.plm.repo.req.RequirementDocumentChildrenRepository;
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
 * Created by Suresh Cassini on 25-11-2020.
 */
@Service
@Transactional
public class RequirementFileService implements CrudService<PLMRequirementFile, Integer>, PageableService<PLMRequirementFile, Integer> {

    @Autowired
    private PLMRequirementFileRepository requirementFileRepository;
    @Autowired
    private PLMRequirementVersionRepository requirementVersionRepository;
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
    @Autowired
    private RequirementDocumentChildrenRepository requirementDocumentChildrenRepository;

    @Override
    public PLMRequirementFile create(PLMRequirementFile requirementFile) {
        checkNotNull(requirementFile);
        requirementFile.setId(null);
        requirementFile = requirementFileRepository.save(requirementFile);
        PLMRequirementVersion requirementVersion = requirementVersionRepository.findOne(requirementFile.getRequirement().getId());
        return requirementFile;
    }

    @Override
    public PLMRequirementFile update(PLMRequirementFile requirementFile) {
        checkNotNull(requirementFile);
        requirementFile.setId(null);
        requirementFile = requirementFileRepository.save(requirementFile);
        return requirementFile;
    }

    @Transactional
    public PLMRequirementFile updateFile(Integer id, PLMRequirementFile requirementFile) {
        PLMFile file = fileRepository.findOne(id);
        PLMRequirementFile requirementFile1 = requirementFileRepository.findOne(file.getId());
        PLMRequirementVersion version = requirementVersionRepository.findOne(requirementFile1.getRequirement().getId());
        if (!file.getLocked().equals(requirementFile.getLocked())) {
                /* App events */
            if (requirementFile.getLocked()) {
                applicationEventPublisher.publishEvent(new RequirementEvents.RequirementFileLockedEvent(version, requirementFile1));
            } else {
                applicationEventPublisher.publishEvent(new RequirementEvents.RequirementFileUnlockedEvent(version, requirementFile1));
            }
        }
        requirementFile1.setDescription(requirementFile.getDescription());
        requirementFile1.setLocked(requirementFile.getLocked());
        requirementFile1.setLockedBy(requirementFile.getLockedBy());
        requirementFile1.setLockedDate(requirementFile.getLockedDate());
        requirementFile = requirementFileRepository.save(requirementFile1);
        return requirementFile;
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        PLMRequirementFile requirementFile = requirementFileRepository.findOne(id);
        if (requirementFile == null) {
            throw new ResourceNotFoundException();
        }
        requirementFileRepository.delete(id);
        PLMRequirementVersion version = requirementVersionRepository.findOne(requirementFile.getRequirement().getId());
    }

    public PLMRequirementFile updateFileName(Integer id, Integer fileId, String newFileName) throws IOException {
        PLMRequirementFile file1 = requirementFileRepository.findOne(fileId);
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(id);
        String oldName = file1.getName();
        file1.setLatest(false);
        PLMRequirementFile documentFile = requirementFileRepository.save(file1);
        PLMRequirementFile requirementFile = (PLMRequirementFile) Utils.cloneObject(documentFile, PLMRequirementFile.class);
        if (requirementFile != null) {
            requirementFile.setId(null);
            requirementFile.setName(newFileName);
            requirementFile.setVersion(file1.getVersion() + 1);
            requirementFile.setLatest(true);
            requirementFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            requirementFile = requirementFileRepository.save(requirementFile);
            if (requirementFile.getParentFile() != null) {
                PLMRequirementFile parent = requirementFileRepository.findOne(requirementFile.getParentFile());
                parent.setModifiedDate(requirementFile.getModifiedDate());
                parent = requirementFileRepository.save(parent);
            }
            qualityFileService.copyFileAttributes(file1.getId(), requirementFile.getId());
            String dir = "";
            if (requirementFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(requirementFile.getRequirement().getId(), fileId);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + requirementFile.getRequirement().getId();
            }
            dir = dir + File.separator + requirementFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + requirementFile.getRequirement().getId() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            PLMRequirementVersion requirementVersion = requirementVersionRepository.findOne(requirementFile.getRequirement().getId());
            /* App Events */

            applicationEventPublisher.publishEvent(new RequirementEvents.RequirementFileRenamedEvent(requirementVersion, documentChildren, file1, requirementFile, "Rename"));
        }
        return requirementFile;
    }

    public List<PLMRequirementFile> replaceRequirementFiles(Integer reqId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMRequirementFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        PLMRequirementFile supFile = null;
        PLMRequirementFile oldFile = null;
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(reqId);
        PLMRequirementVersion requirementVersion = documentChildren.getRequirementVersion();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        PLMRequirementFile plmRequirementFile = null;
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
                    PLMRequirementFile requirementFile = null;
                    plmRequirementFile = requirementFileRepository.findOne(fileId);
                    String oldName = "";
                    if (plmRequirementFile != null && plmRequirementFile.getParentFile() != null) {
                        requirementFile = requirementFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        requirementFile = requirementFileRepository.findByRequirementAndNameAndParentFileIsNullAndLatestTrue(requirementVersion, name);
                    }
                    if (requirementFile != null) {
                        comments = commentRepository.findAllByObjectId(requirementFile.getId());
                    }
                    if (plmRequirementFile != null) {
                        oldName = plmRequirementFile.getName();
                        plmRequirementFile.setLatest(false);
                        plmRequirementFile = requirementFileRepository.save(plmRequirementFile);
                    }
                    if (requirementFile != null) {
                        comments = commentRepository.findAllByObjectId(requirementFile.getId());
                    }
                    requirementFile = new PLMRequirementFile();
                    requirementFile.setName(name);
                    if (plmRequirementFile != null && plmRequirementFile.getParentFile() != null) {
                        requirementFile.setParentFile(plmRequirementFile.getParentFile());
                    }
                    if (plmRequirementFile != null) {
                        requirementFile.setFileNo(plmRequirementFile.getFileNo());
                        requirementFile.setVersion(plmRequirementFile.getVersion() + 1);
                        requirementFile.setReplaceFileName(plmRequirementFile.getName() + " Replaced to " + name);
                        oldFile = JsonUtils.cloneEntity(plmRequirementFile, PLMRequirementFile.class);
                    }
                    requirementFile.setCreatedBy(login.getPerson().getId());
                    requirementFile.setModifiedBy(login.getPerson().getId());
                    requirementFile.setRequirement(requirementVersion);
                    requirementFile.setSize(file.getSize());
                    requirementFile.setFileType("FILE");
                    requirementFile = requirementFileRepository.save(requirementFile);
                    if (requirementFile.getParentFile() != null) {
                        PLMRequirementFile parent = requirementFileRepository.findOne(requirementFile.getParentFile());
                        parent.setModifiedDate(requirementFile.getModifiedDate());
                        parent = requirementFileRepository.save(parent);
                    }
                    if (plmRequirementFile != null) {
                        qualityFileService.copyFileAttributes(plmRequirementFile.getId(), requirementFile.getId());
                    }
                    String dir = "";
                    if (plmRequirementFile != null && plmRequirementFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(requirementVersion.getId(), fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + requirementVersion.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + requirementFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(requirementFile);
                      /* App Events */
                    applicationEventPublisher.publishEvent(new RequirementEvents.RequirementFileRenamedEvent(requirementVersion, documentChildren, plmRequirementFile, requirementFile, "Replace"));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    private String getReplaceFileSystemPath(Integer mfrPartId, Integer fileId) {
        String path = "";
        PLMRequirementFile requirementVersionPartFile = requirementFileRepository.findOne(fileId);
        if (requirementVersionPartFile.getParentFile() != null) {
            path = utilService.visitParentFolder(mfrPartId, requirementVersionPartFile.getParentFile(), path);
        } else {
            path = File.separator + mfrPartId;
        }
        return path;
    }

    public void deleteRequirementFile(Integer reqId, Integer id) {
        checkNotNull(id);
        PLMRequirementFile requirementFile = requirementFileRepository.findOne(id);
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(reqId);
        PLMRequirementVersion requirementVersion = documentChildren.getRequirementVersion();
        List<PLMRequirementFile> requirementFiles = requirementFileRepository.findByRequirementAndFileNo(requirementVersion, requirementFile.getFileNo());
        for (PLMRequirementFile file : requirementFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(requirementVersion.getId(), file.getId());
            fileSystemService.deleteDocumentFromDiskFolder(requirementFile.getId(), dir);
            requirementFileRepository.delete(file.getId());
        }
        if (requirementFile.getParentFile() != null) {
            PLMRequirementFile parent = requirementFileRepository.findOne(requirementFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = requirementFileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementFileDeletedEvent(requirementVersion, documentChildren, requirementFile));
    }

    @Override
    public PLMRequirementFile get(Integer id) {
        checkNotNull(id);
        PLMRequirementFile requirementFile = requirementFileRepository.findOne(id);
        if (requirementFile == null) {
            throw new ResourceNotFoundException();
        }
        return requirementFile;
    }

    @Override
    public List<PLMRequirementFile> getAll() {
        return requirementFileRepository.findAll();
    }

    public List<PLMRequirementFile> getAllFileVersions(Integer requirementVersionId, String name) {
        PLMRequirementVersion requirementVersion = requirementVersionRepository.findOne(requirementVersionId);
        return requirementFileRepository.findAllByRequirementAndNameOrderByCreatedDateDesc(requirementVersion, name);
    }

    @Override
    public Page<PLMRequirementFile> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null)
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order("id")));
        return requirementFileRepository.findAll(pageable);
    }

    public List<PLMRequirementFile> findByRequirement(Integer requirementVersion) {
        PLMRequirementVersion version = requirementVersionRepository.findOne(requirementVersion);
        List<PLMRequirementFile> requirementFiles = requirementFileRepository.findByRequirementAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(version);
        requirementFiles.forEach(requirementFile -> {
            requirementFile.setParentObject(PLMObjectType.REQUIREMENT);
            if (requirementFile.getFileType().equals("FOLDER")) {
                requirementFile.setCount(requirementFileRepository.getChildrenCountByParentFileAndLatestTrue(requirementFile.getId()));
                requirementFile.setCount(requirementFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(requirementFile.getRequirement().getId(), requirementFile.getId()));
            }
        });
        return requirementFiles;
    }

    public List<PLMRequirementFile> findByRequirementLatest(Integer requirementVersion) {
        PLMRequirementVersion version = requirementVersionRepository.findOne(requirementVersion);
        List<PLMRequirementFile> requirementFiles = requirementFileRepository.findByRequirementAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(version);
        requirementFiles.forEach(requirementFile -> {
            requirementFile.setParentObject(PLMObjectType.REQUIREMENT);
            if (requirementFile.getFileType().equals("FOLDER")) {
                requirementFile.setCount(requirementFileRepository.getChildrenCountByParentFileAndLatestTrue(requirementFile.getId()));
            }
        });
        return requirementFiles;
    }

    public List<PLMRequirementFile> uploadFiles(Integer reqId, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMRequirementFile> uploadedFiles = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(reqId);
        PLMRequirementVersion requirementVersion = documentChildren.getRequirementVersion();
        String[] fileExtension = null;
        List<PLMRequirementFile> newFiles = new ArrayList<>();
        List<PLMRequirementFile> versionedFiles = new ArrayList<>();
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
                    PLMRequirementFile supFile = null;
                    if (folderId == 0) {
                        supFile = requirementFileRepository.findByRequirementAndNameAndParentFileIsNullAndLatestTrue(requirementVersion, name);
                    } else {
                        supFile = requirementFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer version = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (supFile != null) {
                        supFile.setLatest(false);
                        Integer oldVersion = supFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = supFile.getFileNo();
                        oldFile = supFile.getId();
                /*supFile.setVersion(newVersion);*/
                        versioned = true;
                        requirementFileRepository.save(supFile);
                    }
                    if (supFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    supFile = new PLMRequirementFile();
                    supFile.setName(name);
                    supFile.setFileNo(autoNumber1);
                    supFile.setCreatedBy(login.getPerson().getId());
                    supFile.setModifiedBy(login.getPerson().getId());
            /*supFile.setVersion(1);*/
                    supFile.setVersion(version);
                    supFile.setRequirement(requirementVersion);
                    supFile.setSize(file.getSize());
                    supFile.setFileType("FILE");
                    if (folderId != 0) {
                        supFile.setParentFile(folderId);
                    }
                    supFile = requirementFileRepository.save(supFile);
                    if (supFile.getParentFile() != null) {
                        PLMRequirementFile parent = requirementFileRepository.findOne(supFile.getParentFile());
                        parent.setModifiedDate(supFile.getModifiedDate());
                        parent = requirementFileRepository.save(parent);
                    }
                    if (supFile.getVersion() > 1) {
                        qualityFileService.copyFileAttributes(oldFile, supFile.getId());
                    }
                    if (fNames == null) {
                        fNames = supFile.getName();

                    } else {
                        fNames = fNames + "  , " + supFile.getName();
                    }
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + requirementVersion.getId();
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getParentFileSystemPath(requirementVersion.getId(), folderId);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + supFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploadedFiles.add(supFile);
                    if (versioned) {
                        versionedFiles.add(supFile);
                    } else {
                        newFiles.add(supFile);
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
          /* App Events */
        if (newFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new RequirementEvents.RequirementFilesAddedEvent(requirementVersion, documentChildren, newFiles));
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new RequirementEvents.RequirementFilesVersionedEvent(requirementVersion, documentChildren, versionedFiles));
        }
        return uploadedFiles;
    }

    public File getRequirementFile(Integer reqId, Integer fileId) {
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(reqId);
        PLMRequirementVersion requirementVersion = documentChildren.getRequirementVersion();
        if (requirementVersion == null) {
            throw new ResourceNotFoundException();
        }
        PLMRequirementFile supFile = requirementFileRepository.findOne(fileId);
        if (supFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(requirementVersion.getId(), fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<PLMRequirementFile> findByRequirementFilesName(Integer reqId, String name) {
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(reqId);
        PLMRequirementVersion requirementVersion = documentChildren.getRequirementVersion();
        return requirementFileRepository.findByRequirementAndNameContainingIgnoreCaseAndLatestTrue(requirementVersion, name);
    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer requirementVersionId, Integer fileId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMRequirementFile plmItemFile = requirementFileRepository.findOne(fileId);
        PLMRequirementVersion requirementVersion = requirementVersionRepository.findOne(plmItemFile.getRequirement().getId());
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementFileDownloadedEvent(requirementVersion, plmItemFile));
        return plmFileDownloadHistory;
    }

    public List<PLMRequirementFile> getAllFileVersionComments(Integer requirementVersionId, Integer fileId, ObjectType objectType) {
        List<PLMRequirementFile> itemFiles = new ArrayList<>();
        PLMRequirementFile supFile = requirementFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(supFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(supFile.getId());
        if (comments.size() > 0) {
            supFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            supFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(supFile);
        List<PLMRequirementFile> files = requirementFileRepository.findByRequirementAndFileNoAndLatestFalseOrderByCreatedDateDesc(supFile.getRequirement(), supFile.getFileNo());
        if (files.size() > 0) {
            for (PLMRequirementFile file : files) {
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

    public PLMRequirementFile createRequirementFolder(Integer reqId, PLMRequirementFile requirementFile) {
        requirementFile.setId(null);
        String folderNumber = null;
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(reqId);
        PLMRequirementVersion requirementVersion = documentChildren.getRequirementVersion();
        PLMRequirementFile existFolderName = null;
        if (requirementFile.getParentFile() != null) {
            existFolderName = requirementFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndRequirementAndLatestTrue(requirementFile.getName(), requirementFile.getParentFile(), requirementVersion);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(requirementFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", requirementFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = requirementFileRepository.findByNameEqualsIgnoreCaseAndRequirementAndLatestTrue(requirementFile.getName(), requirementVersion);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", requirementFile.getName());
                throw new CassiniException(result);
            }
        }

        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        requirementFile.setRequirement(requirementVersion);
        requirementFile.setFileNo(folderNumber);
        requirementFile.setFileType("FOLDER");
        requirementFile = requirementFileRepository.save(requirementFile);
        if (requirementFile.getParentFile() != null) {
            PLMRequirementFile parent = requirementFileRepository.findOne(requirementFile.getParentFile());
            parent.setModifiedDate(requirementFile.getModifiedDate());
            parent = requirementFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(requirementVersion.getId(), requirementFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementFoldersAddedEvent(requirementVersion, documentChildren, requirementFile));
        return requirementFile;
    }

    private String getParentFileSystemPath(Integer requirementVersionId, Integer fileId) {
        String path = "";
        PLMRequirementFile requirementFile = requirementFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (requirementFile.getParentFile() != null) {
            path = utilService.visitParentFolder(requirementVersionId, requirementFile.getParentFile(), path);
        } else {
            path = File.separator + requirementVersionId + File.separator + requirementFile.getId();
        }
        return path;
    }

    public PLMFile moveRequirementFileToFolder(Integer id, PLMRequirementFile plmRequirementFile) throws Exception {
        PLMRequirementFile file = requirementFileRepository.findOne(plmRequirementFile.getId());
        PLMRequirementFile existFile = (PLMRequirementFile) Utils.cloneObject(file, PLMRequirementFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getRequirement().getId(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getRequirement().getId() + File.separator + existFile.getId();
        }
        if (plmRequirementFile.getParentFile() != null) {
            PLMRequirementFile existItemFile = requirementFileRepository.findByParentFileAndNameAndLatestTrue(plmRequirementFile.getParentFile(), plmRequirementFile.getName());
            PLMRequirementFile folder = requirementFileRepository.findOne(plmRequirementFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmRequirementFile = requirementFileRepository.save(plmRequirementFile);
            }
        } else {
            PLMRequirementFile existItemFile = requirementFileRepository.findByRequirementAndNameAndParentFileIsNullAndLatestTrue(plmRequirementFile.getRequirement(), plmRequirementFile.getName());
            PLMRequirementVersion requirementVersion = requirementVersionRepository.findOne(plmRequirementFile.getRequirement().getId());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                plmRequirementFile = requirementFileRepository.save(plmRequirementFile);
            }
        }
        if (plmRequirementFile != null) {
            String dir = "";
            if (plmRequirementFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(plmRequirementFile.getRequirement().getId(), plmRequirementFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmRequirementFile.getRequirement().getId() + File.separator + plmRequirementFile.getId();
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
            List<PLMRequirementFile> oldVersionFiles = requirementFileRepository.findByRequirementAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getRequirement(), existFile.getFileNo());
            for (PLMRequirementFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getRequirement().getId(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getRequirement().getId() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(plmRequirementFile.getRequirement().getId(), plmRequirementFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmRequirementFile.getParentFile());
                oldVersionFile = requirementFileRepository.save(oldVersionFile);
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
        return plmRequirementFile;
    }

    public List<PLMRequirementFile> getRequirementFolderChildren(Integer folderId) {
        List<PLMRequirementFile> requirementFiles = requirementFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(folderId);
        requirementFiles.forEach(requirementFile -> {
            requirementFile.setParentObject(PLMObjectType.REQUIREMENT);
            if (requirementFile.getFileType().equals("FOLDER")) {
                requirementFile.setCount(requirementFileRepository.getChildrenCountByParentFileAndLatestTrue(requirementFile.getId()));
                requirementFile.setCount(requirementFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(requirementFile.getRequirement().getId(), requirementFile.getId()));
            }
        });
        return requirementFiles;
    }

    public void deleteFolder(Integer reqId, Integer folderId) {
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(reqId);
        PLMRequirementVersion requirementVersion = documentChildren.getRequirementVersion();
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(requirementVersion.getId(), folderId);
        PLMRequirementFile file = requirementFileRepository.findOne(folderId);
        List<PLMRequirementFile> requirementFiles = requirementFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) requirementFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        requirementFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementFoldersDeletedEvent(requirementVersion, documentChildren, file));
    }

    public void generateZipFile(Integer reqId, HttpServletResponse response) throws FileNotFoundException, IOException {
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(reqId);
        PLMRequirementVersion requirementVersion = documentChildren.getRequirementVersion();
        List<PLMRequirementFile> plmRequirementFiles = requirementFileRepository.findByRequirementAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(requirementVersion);
        ArrayList<String> fileList = new ArrayList<>();
        plmRequirementFiles.forEach(plmRequirementFile -> {
            File file = getRequirementFile(requirementVersion.getId(), plmRequirementFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = requirementVersion.getName() + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "REQUIREMENT",reqId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional
    public PLMRequirementFile getLatestUploadedRequirementFile(Integer requirementVersionId, Integer fileId) {
        PLMRequirementFile requirementFile = requirementFileRepository.findOne(fileId);
        PLMRequirementFile plmRequirementFile = requirementFileRepository.findByRequirementAndFileNoAndLatestTrue(requirementFile.getRequirement(), requirementFile.getFileNo());
        return plmRequirementFile;
    }

    public PLMFile updateFileDescription(Integer requirementVersionId, Integer id, PLMRequirementFile plmRequirementFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            file.setDescription(plmRequirementFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<PLMRequirementFile> pasteFromClipboard(Integer reqId, Integer fileId, List<PLMFile> files) {
        List<PLMRequirementFile> fileList = new ArrayList<>();
        List<Integer> folderIds = new ArrayList<>();
        files.forEach(file -> {
            if (file.getFileType().equals("FOLDER")) {
                folderIds.add(file.getId());
            }
        });
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(reqId);
        PLMRequirementVersion version = documentChildren.getRequirementVersion();
        for (PLMFile file : files) {
            Boolean canCreate = true;
            if (file.getParentFile() != null && folderIds.indexOf(file.getParentFile()) != -1) {
                canCreate = false;
            }
            if (file.getFileType().equals("FILE") && canCreate) {
                PLMRequirementFile requirementFile = new PLMRequirementFile();
                PLMRequirementFile existFile = null;
                if (fileId != 0) {
                    requirementFile.setParentFile(fileId);
                    existFile = requirementFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndRequirementAndLatestTrue(file.getName(), fileId, version);
                } else {
                    existFile = requirementFileRepository.findByRequirementAndNameAndParentFileIsNullAndLatestTrue(version, file.getName());
                }
                if (existFile == null) {
                    requirementFile.setName(file.getName());
                    requirementFile.setDescription(file.getDescription());
                    requirementFile.setRequirement(version);
                    requirementFile.setVersion(1);
                    requirementFile.setSize(file.getSize());
                    requirementFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    requirementFile.setFileNo(autoNumber1);
                    requirementFile.setFileType("FILE");
                    requirementFile = requirementFileRepository.save(requirementFile);
                    requirementFile.setParentObject(PLMObjectType.REQUIREMENT);
                    fileList.add(requirementFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + version.getId();
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (requirementFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(version.getId(), requirementFile.getId());
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + version.getId() + File.separator + requirementFile.getId();
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
                PLMRequirementFile requirementFile = new PLMRequirementFile();
                PLMRequirementFile existFile = null;
                if (fileId != 0) {
                    requirementFile.setParentFile(fileId);
                    existFile = requirementFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndRequirementAndLatestTrue(file.getName(), fileId, version);
                } else {
                    existFile = requirementFileRepository.findByRequirementAndNameAndParentFileIsNullAndLatestTrue(version, file.getName());
                }
                if (existFile == null) {
                    requirementFile.setName(file.getName());
                    requirementFile.setDescription(file.getDescription());
                    String folderNumber = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    requirementFile.setVersion(1);
                    requirementFile.setSize(0L);
                    requirementFile.setRequirement(version);
                    requirementFile.setFileNo(folderNumber);
                    requirementFile.setFileType("FOLDER");
                    requirementFile = requirementFileRepository.save(requirementFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + version.getId();
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(version.getId(), requirementFile.getId());
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(requirementFile);
                    copyFolderFiles(version, file.getParentObject(), file.getId(), requirementFile.getId());
                }
            }
        }
        return fileList;
    }

    private void copyFolderFiles(PLMRequirementVersion version, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        plmFiles.forEach(plmFile -> {
            PLMRequirementFile requirementFile = new PLMRequirementFile();
            requirementFile.setParentFile(parent);
            requirementFile.setName(plmFile.getName());
            requirementFile.setDescription(plmFile.getDescription());
            requirementFile.setRequirement(version);
            String folderNumber = null;
            if (plmFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                requirementFile.setVersion(1);
                requirementFile.setFileNo(folderNumber);
                requirementFile.setSize(plmFile.getSize());
                requirementFile.setFileType("FILE");
                requirementFile = requirementFileRepository.save(requirementFile);
                requirementFile.setParentObject(PLMObjectType.REQUIREMENT);
                plmFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmFile);

                String dir = "";
                if (requirementFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(version.getId(), requirementFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + version.getId() + File.separator + requirementFile.getId();
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
                requirementFile.setVersion(1);
                requirementFile.setSize(0L);
                requirementFile.setFileNo(folderNumber);
                requirementFile.setFileType("FOLDER");
                requirementFile = requirementFileRepository.save(requirementFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + version.getId();
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(version.getId(), requirementFile.getId());
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(version, objectType, plmFile.getId(), requirementFile.getId());
            }
        });
    }

    public void undoCopiedRequirementFiles(Integer ecoId, List<PLMRequirementFile> requirementFiles) {
        requirementFiles.forEach(requirementFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(ecoId, requirementFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(requirementFile.getId(), dir);
            requirementFileRepository.delete(requirementFile.getId());
        });
    }
}