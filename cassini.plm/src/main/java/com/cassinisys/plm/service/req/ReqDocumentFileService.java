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
import com.cassinisys.plm.event.RequirementDocumentEvents;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.req.PLMRequirementDocumentFile;
import com.cassinisys.plm.model.req.PLMRequirementDocumentRevision;
import com.cassinisys.plm.repo.plm.FileDownloadHistoryRepository;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.req.PLMRequirementDocumentFileRepository;
import com.cassinisys.plm.repo.req.PLMRequirementDocumentRevisionRepository;
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
public class ReqDocumentFileService implements CrudService<PLMRequirementDocumentFile, Integer>, PageableService<PLMRequirementDocumentFile, Integer> {

    @Autowired
    private PLMRequirementDocumentFileRepository requirementDocumentFileRepository;
    @Autowired
    private PLMRequirementDocumentRevisionRepository requirementDocumentRevisionRepository;
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

    @Override
    public PLMRequirementDocumentFile create(PLMRequirementDocumentFile requirementFile) {
        checkNotNull(requirementFile);
        requirementFile.setId(null);
        requirementFile = requirementDocumentFileRepository.save(requirementFile);
        PLMRequirementDocumentRevision requirementDocumentRevision = requirementDocumentRevisionRepository.findOne(requirementFile.getDocumentRevision().getId());
        return requirementFile;
    }

    @Override
    public PLMRequirementDocumentFile update(PLMRequirementDocumentFile requirementFile) {
        checkNotNull(requirementFile);
        requirementFile.setId(null);
        requirementFile = requirementDocumentFileRepository.save(requirementFile);
        return requirementFile;
    }

    @Transactional
    public PLMRequirementDocumentFile updateFile(Integer id, PLMRequirementDocumentFile requirementFile) {
        PLMFile file = fileRepository.findOne(id);
        PLMRequirementDocumentFile requirementFile1 = requirementDocumentFileRepository.findOne(file.getId());
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(requirementFile1.getDocumentRevision().getId());
        if (!file.getLocked().equals(requirementFile.getLocked())) {
                /* App events */
            if (requirementFile.getLocked()) {
                applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentFileLockedEvent(revision, requirementFile1));
            } else {
                applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentFileUnlockedEvent(revision, requirementFile1));
            }
        }
        requirementFile1.setDescription(requirementFile.getDescription());
        requirementFile1.setLocked(requirementFile.getLocked());
        requirementFile1.setLockedBy(requirementFile.getLockedBy());
        requirementFile1.setLockedDate(requirementFile.getLockedDate());
        requirementFile = requirementDocumentFileRepository.save(requirementFile1);
        return requirementFile;
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        PLMRequirementDocumentFile requirementFile = requirementDocumentFileRepository.findOne(id);
        if (requirementFile == null) {
            throw new ResourceNotFoundException();
        }
        requirementDocumentFileRepository.delete(id);
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(requirementFile.getDocumentRevision().getId());
    }

    public PLMRequirementDocumentFile updateFileName(Integer id, String newFileName) throws IOException {
        PLMRequirementDocumentFile file1 = requirementDocumentFileRepository.findOne(id);
        PLMRequirementDocumentFile oldFile = (PLMRequirementDocumentFile) Utils.cloneObject(file1, PLMRequirementDocumentFile.class);
        String oldName = file1.getName();
        file1.setLatest(false);
        PLMRequirementDocumentFile documentFile = requirementDocumentFileRepository.save(file1);
        PLMRequirementDocumentFile requirementFile = (PLMRequirementDocumentFile) Utils.cloneObject(documentFile, PLMRequirementDocumentFile.class);
        if (requirementFile != null) {
            requirementFile.setId(null);
            requirementFile.setName(newFileName);
            requirementFile.setVersion(file1.getVersion() + 1);
            requirementFile.setLatest(true);
            requirementFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            requirementFile = requirementDocumentFileRepository.save(requirementFile);
            if (requirementFile.getParentFile() != null) {
                PLMRequirementDocumentFile parent = requirementDocumentFileRepository.findOne(requirementFile.getParentFile());
                parent.setModifiedDate(requirementFile.getModifiedDate());
                parent = requirementDocumentFileRepository.save(parent);
            }
            qualityFileService.copyFileAttributes(file1.getId(), requirementFile.getId());
            String dir = "";
            if (requirementFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(requirementFile.getDocumentRevision().getId(), id);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + requirementFile.getDocumentRevision().getId();
            }
            dir = dir + File.separator + requirementFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + requirementFile.getDocumentRevision().getId() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            PLMRequirementDocumentRevision requirementDocumentRevision = requirementDocumentRevisionRepository.findOne(requirementFile.getDocumentRevision().getId());
            /* App Events */
            applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentFileRenamedEvent(requirementDocumentRevision, oldFile, requirementFile, "Rename"));
        }
        return requirementFile;
    }

    public List<PLMRequirementDocumentFile> replaceReqDocumentFiles(Integer requirementDocumentRevisionId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMRequirementDocumentFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        PLMRequirementDocumentFile supFile = null;
        PLMRequirementDocumentFile oldFile = null;
        PLMRequirementDocumentRevision requirementDocumentRevision = requirementDocumentRevisionRepository.findOne(requirementDocumentRevisionId);
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        PLMRequirementDocumentFile plmReqDocumentFile = null;
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
                    PLMRequirementDocumentFile requirementFile = null;
                    plmReqDocumentFile = requirementDocumentFileRepository.findOne(fileId);
                    String oldName = "";
                    if (plmReqDocumentFile != null && plmReqDocumentFile.getParentFile() != null) {
                        requirementFile = requirementDocumentFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        requirementFile = requirementDocumentFileRepository.findByDocumentRevisionAndNameAndParentFileIsNullAndLatestTrue(requirementDocumentRevision, name);
                    }
                    if (requirementFile != null) {
                        comments = commentRepository.findAllByObjectId(requirementFile.getId());
                    }
                    if (plmReqDocumentFile != null) {
                        oldName = plmReqDocumentFile.getName();
                        plmReqDocumentFile.setLatest(false);
                        plmReqDocumentFile = requirementDocumentFileRepository.save(plmReqDocumentFile);
                    }
                    if (requirementFile != null) {
                        comments = commentRepository.findAllByObjectId(requirementFile.getId());
                    }
                    requirementFile = new PLMRequirementDocumentFile();
                    requirementFile.setName(name);
                    if (plmReqDocumentFile != null && plmReqDocumentFile.getParentFile() != null) {
                        requirementFile.setParentFile(plmReqDocumentFile.getParentFile());
                    }
                    if (plmReqDocumentFile != null) {
                        requirementFile.setFileNo(plmReqDocumentFile.getFileNo());
                        requirementFile.setVersion(plmReqDocumentFile.getVersion() + 1);
                        requirementFile.setReplaceFileName(plmReqDocumentFile.getName() + " Replaced to " + name);
                        oldFile = JsonUtils.cloneEntity(plmReqDocumentFile, PLMRequirementDocumentFile.class);
                    }
                    requirementFile.setCreatedBy(login.getPerson().getId());
                    requirementFile.setModifiedBy(login.getPerson().getId());
                    requirementFile.setDocumentRevision(requirementDocumentRevision);
                    requirementFile.setSize(file.getSize());
                    requirementFile.setFileType("FILE");
                    requirementFile = requirementDocumentFileRepository.save(requirementFile);
                    if (requirementFile.getParentFile() != null) {
                        PLMRequirementDocumentFile parent = requirementDocumentFileRepository.findOne(requirementFile.getParentFile());
                        parent.setModifiedDate(requirementFile.getModifiedDate());
                        parent = requirementDocumentFileRepository.save(parent);
                    }
                    if (plmReqDocumentFile != null) {
                        qualityFileService.copyFileAttributes(plmReqDocumentFile.getId(), requirementFile.getId());
                    }
                    String dir = "";
                    if (plmReqDocumentFile != null && plmReqDocumentFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(requirementDocumentRevisionId, fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + requirementDocumentRevisionId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + requirementFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(requirementFile);
                      /* App Events */
                    applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentFileRenamedEvent(requirementDocumentRevision, plmReqDocumentFile, requirementFile, "Replace"));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    private String getReplaceFileSystemPath(Integer mfrPartId, Integer fileId) {
        String path = "";
        PLMRequirementDocumentFile requirementDocumentRevisionPartFile = requirementDocumentFileRepository.findOne(fileId);
        if (requirementDocumentRevisionPartFile.getParentFile() != null) {
            path = utilService.visitParentFolder(mfrPartId, requirementDocumentRevisionPartFile.getParentFile(), path);
        } else {
            path = File.separator + mfrPartId;
        }
        return path;
    }

    public void deleteReqDocumentFile(Integer requirementDocumentRevisionId, Integer id) {
        checkNotNull(id);
        PLMRequirementDocumentFile requirementFile = requirementDocumentFileRepository.findOne(id);
        PLMRequirementDocumentRevision requirementDocumentRevision = requirementDocumentRevisionRepository.findOne(requirementDocumentRevisionId);
        List<PLMRequirementDocumentFile> requirementFiles = requirementDocumentFileRepository.findByDocumentRevisionAndFileNo(requirementDocumentRevision, requirementFile.getFileNo());
        for (PLMRequirementDocumentFile requirementFile1 : requirementFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(requirementDocumentRevisionId, requirementFile1.getId());
            fileSystemService.deleteDocumentFromDiskFolder(requirementFile1.getId(), dir);
            requirementDocumentFileRepository.delete(requirementFile1.getId());
        }
        if (requirementFile.getParentFile() != null) {
            PLMRequirementDocumentFile parent = requirementDocumentFileRepository.findOne(requirementFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = requirementDocumentFileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentFileDeletedEvent(requirementDocumentRevision, requirementFile));
    }

    @Override
    public PLMRequirementDocumentFile get(Integer id) {
        checkNotNull(id);
        PLMRequirementDocumentFile requirementFile = requirementDocumentFileRepository.findOne(id);
        if (requirementFile == null) {
            throw new ResourceNotFoundException();
        }
        return requirementFile;
    }

    @Override
    public List<PLMRequirementDocumentFile> getAll() {
        return requirementDocumentFileRepository.findAll();
    }

    public List<PLMRequirementDocumentFile> getAllFileVersions(Integer requirementDocumentRevisionId, String name) {
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(requirementDocumentRevisionId);
        return requirementDocumentFileRepository.findAllByDocumentRevisionAndNameOrderByCreatedDateDesc(revision, name);
    }

    @Override
    public Page<PLMRequirementDocumentFile> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null)
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order("id")));
        return requirementDocumentFileRepository.findAll(pageable);
    }

    public List<PLMRequirementDocumentFile> findByDocumentRevision(Integer requirementDocumentRevision) {
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(requirementDocumentRevision);
        List<PLMRequirementDocumentFile> requirementFiles = requirementDocumentFileRepository.findByDocumentRevisionAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(revision);
        requirementFiles.forEach(requirementFile -> {
            requirementFile.setParentObject(PLMObjectType.REQUIREMENTDOCUMENT);
            if (requirementFile.getFileType().equals("FOLDER")) {
                requirementFile.setCount(requirementDocumentFileRepository.getChildrenCountByParentFileAndLatestTrue(requirementFile.getId()));
                requirementFile.setCount(requirementFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(requirementFile.getDocumentRevision().getId(), requirementFile.getId()));
            }
        });
        return requirementFiles;
    }

    public List<PLMRequirementDocumentFile> findByDocumentRevisionLatest(Integer requirementDocumentRevision) {
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(requirementDocumentRevision);
        List<PLMRequirementDocumentFile> requirementFiles = requirementDocumentFileRepository.findByDocumentRevisionAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(revision);
        requirementFiles.forEach(requirementFile -> {
            requirementFile.setParentObject(PLMObjectType.REQUIREMENTDOCUMENT);
            if (requirementFile.getFileType().equals("FOLDER")) {
                requirementFile.setCount(requirementDocumentFileRepository.getChildrenCountByParentFileAndLatestTrue(requirementFile.getId()));
            }
        });
        return requirementFiles;
    }

    public List<PLMRequirementDocumentFile> uploadFiles(Integer requirementDocumentRevisionId, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMRequirementDocumentFile> uploadedFiles = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        PLMRequirementDocumentRevision requirementDocumentRevision = requirementDocumentRevisionRepository.findOne(requirementDocumentRevisionId);
        String[] fileExtension = null;
        List<PLMRequirementDocumentFile> newFiles = new ArrayList<>();
        List<PLMRequirementDocumentFile> versionedFiles = new ArrayList<>();
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
                    PLMRequirementDocumentFile supFile = null;
                    if (folderId == 0) {
                        supFile = requirementDocumentFileRepository.findByDocumentRevisionAndNameAndParentFileIsNullAndLatestTrue(requirementDocumentRevision, name);
                    } else {
                        supFile = requirementDocumentFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
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
                        requirementDocumentFileRepository.save(supFile);
                    }
                    if (supFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    supFile = new PLMRequirementDocumentFile();
                    supFile.setName(name);
                    supFile.setFileNo(autoNumber1);
                    supFile.setCreatedBy(login.getPerson().getId());
                    supFile.setModifiedBy(login.getPerson().getId());
            /*supFile.setVersion(1);*/
                    supFile.setVersion(version);
                    supFile.setDocumentRevision(requirementDocumentRevision);
                    supFile.setSize(file.getSize());
                    supFile.setFileType("FILE");
                    if (folderId != 0) {
                        supFile.setParentFile(folderId);
                    }
                    supFile = requirementDocumentFileRepository.save(supFile);
                    if (supFile.getParentFile() != null) {
                        PLMRequirementDocumentFile parent = requirementDocumentFileRepository.findOne(supFile.getParentFile());
                        parent.setModifiedDate(supFile.getModifiedDate());
                        parent = requirementDocumentFileRepository.save(parent);
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
                                "filesystem" + File.separator + requirementDocumentRevisionId;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getParentFileSystemPath(requirementDocumentRevisionId, folderId);
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
            applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentFilesAddedEvent(requirementDocumentRevision, newFiles));
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentFilesVersionedEvent(requirementDocumentRevision, versionedFiles));
        }
        return uploadedFiles;
    }

    public File getDocumentRevisionFile(Integer requirementDocumentRevisionId, Integer fileId) {
        checkNotNull(requirementDocumentRevisionId);
        checkNotNull(fileId);
        PLMRequirementDocumentRevision mfr = requirementDocumentRevisionRepository.findOne(requirementDocumentRevisionId);
        if (mfr == null) {
            throw new ResourceNotFoundException();
        }
        PLMRequirementDocumentFile supFile = requirementDocumentFileRepository.findOne(fileId);
        if (supFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(requirementDocumentRevisionId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<PLMRequirementDocumentFile> findByDocumentRevisionFilesName(Integer requirementDocumentRevisionId, String name) {
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(requirementDocumentRevisionId);
        return requirementDocumentFileRepository.findByDocumentRevisionAndNameContainingIgnoreCaseAndLatestTrue(revision, name);
    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer requirementDocumentRevisionId, Integer fileId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMRequirementDocumentFile plmItemFile = requirementDocumentFileRepository.findOne(fileId);
        PLMRequirementDocumentRevision requirementDocumentRevision = requirementDocumentRevisionRepository.findOne(plmItemFile.getDocumentRevision().getId());
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentFileDownloadedEvent(requirementDocumentRevision, plmItemFile));
        return plmFileDownloadHistory;
    }

    public List<PLMRequirementDocumentFile> getAllFileVersionComments(Integer requirementDocumentRevisionId, Integer fileId, ObjectType objectType) {
        List<PLMRequirementDocumentFile> itemFiles = new ArrayList<>();
        PLMRequirementDocumentFile supFile = requirementDocumentFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(supFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(supFile.getId());
        if (comments.size() > 0) {
            supFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            supFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(supFile);
        List<PLMRequirementDocumentFile> files = requirementDocumentFileRepository.findByDocumentRevisionAndFileNoAndLatestFalseOrderByCreatedDateDesc(supFile.getDocumentRevision(), supFile.getFileNo());
        if (files.size() > 0) {
            for (PLMRequirementDocumentFile file : files) {
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

    public PLMRequirementDocumentFile createReqDocumentFolder(Integer requirementDocumentRevisionId, PLMRequirementDocumentFile requirementFile) {
        requirementFile.setId(null);
        String folderNumber = null;
        PLMRequirementDocumentRevision requirementDocumentRevision = requirementDocumentRevisionRepository.findOne(requirementDocumentRevisionId);
        PLMRequirementDocumentFile existFolderName = null;
        if (requirementFile.getParentFile() != null) {
            existFolderName = requirementDocumentFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndDocumentRevisionAndLatestTrue(requirementFile.getName(), requirementFile.getParentFile(), requirementDocumentRevision);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(requirementFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", requirementFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = requirementDocumentFileRepository.findByNameEqualsIgnoreCaseAndDocumentRevisionAndLatestTrue(requirementFile.getName(), requirementDocumentRevision);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", requirementFile.getName());
                throw new CassiniException(result);
            }
        }

        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        requirementFile.setDocumentRevision(requirementDocumentRevision);
        requirementFile.setFileNo(folderNumber);
        requirementFile.setFileType("FOLDER");
        requirementFile = requirementDocumentFileRepository.save(requirementFile);
        if (requirementFile.getParentFile() != null) {
            PLMRequirementDocumentFile parent = requirementDocumentFileRepository.findOne(requirementFile.getParentFile());
            parent.setModifiedDate(requirementFile.getModifiedDate());
            parent = requirementDocumentFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(requirementDocumentRevisionId, requirementFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentFoldersAddedEvent(requirementDocumentRevision, requirementFile));
        return requirementFile;
    }

    private String getParentFileSystemPath(Integer requirementDocumentRevisionId, Integer fileId) {
        String path = "";
        PLMRequirementDocumentFile requirementFile = requirementDocumentFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (requirementFile.getParentFile() != null) {
            path = utilService.visitParentFolder(requirementDocumentRevisionId, requirementFile.getParentFile(), path);
        } else {
            path = File.separator + requirementDocumentRevisionId + File.separator + requirementFile.getId();
        }
        return path;
    }

    public PLMFile moveReqDocumentFileToFolder(Integer id, PLMRequirementDocumentFile plmReqDocumentFile) throws Exception {
        PLMRequirementDocumentFile file = requirementDocumentFileRepository.findOne(plmReqDocumentFile.getId());
        PLMRequirementDocumentFile existFile = (PLMRequirementDocumentFile) Utils.cloneObject(file, PLMRequirementDocumentFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getDocumentRevision().getId(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getDocumentRevision().getId() + File.separator + existFile.getId();
        }
        if (plmReqDocumentFile.getParentFile() != null) {
            PLMRequirementDocumentFile existItemFile = requirementDocumentFileRepository.findByParentFileAndNameAndLatestTrue(plmReqDocumentFile.getParentFile(), plmReqDocumentFile.getName());
            PLMRequirementDocumentFile folder = requirementDocumentFileRepository.findOne(plmReqDocumentFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmReqDocumentFile = requirementDocumentFileRepository.save(plmReqDocumentFile);
            }
        } else {
            PLMRequirementDocumentFile existItemFile = requirementDocumentFileRepository.findByDocumentRevisionAndNameAndParentFileIsNullAndLatestTrue(plmReqDocumentFile.getDocumentRevision(), plmReqDocumentFile.getName());
            PLMRequirementDocumentRevision requirementDocumentRevision = requirementDocumentRevisionRepository.findOne(plmReqDocumentFile.getDocumentRevision().getId());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                plmReqDocumentFile = requirementDocumentFileRepository.save(plmReqDocumentFile);
            }
        }
        if (plmReqDocumentFile != null) {
            String dir = "";
            if (plmReqDocumentFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(plmReqDocumentFile.getDocumentRevision().getId(), plmReqDocumentFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmReqDocumentFile.getDocumentRevision().getId() + File.separator + plmReqDocumentFile.getId();
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
            List<PLMRequirementDocumentFile> oldVersionFiles = requirementDocumentFileRepository.findByDocumentRevisionAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getDocumentRevision(), existFile.getFileNo());
            for (PLMRequirementDocumentFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getDocumentRevision().getId(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getDocumentRevision().getId() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(plmReqDocumentFile.getDocumentRevision().getId(), plmReqDocumentFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmReqDocumentFile.getParentFile());
                oldVersionFile = requirementDocumentFileRepository.save(oldVersionFile);
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
        return plmReqDocumentFile;
    }

    public List<PLMRequirementDocumentFile> getDocumentRevisionFolderChildren(Integer folderId) {
        List<PLMRequirementDocumentFile> requirementFiles = requirementDocumentFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(folderId);
        requirementFiles.forEach(requirementFile -> {
            requirementFile.setParentObject(PLMObjectType.REQUIREMENTDOCUMENT);
            if (requirementFile.getFileType().equals("FOLDER")) {
                requirementFile.setCount(requirementDocumentFileRepository.getChildrenCountByParentFileAndLatestTrue(requirementFile.getId()));
                requirementFile.setCount(requirementFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(requirementFile.getDocumentRevision().getId(), requirementFile.getId()));
            }
        });
        return requirementFiles;
    }

    public void deleteFolder(Integer requirementDocumentRevisionId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(requirementDocumentRevisionId, folderId);
        PLMRequirementDocumentFile file = requirementDocumentFileRepository.findOne(folderId);
        PLMRequirementDocumentRevision requirementDocumentRevision = requirementDocumentRevisionRepository.findOne(requirementDocumentRevisionId);
        List<PLMRequirementDocumentFile> requirementFiles = requirementDocumentFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) requirementFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        requirementDocumentFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentFoldersDeletedEvent(requirementDocumentRevision, file));
    }

    public void generateZipFile(Integer requirementDocumentRevisionId, HttpServletResponse response) throws FileNotFoundException, IOException {
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(requirementDocumentRevisionId);
        List<PLMRequirementDocumentFile> plmReqDocumentFiles = requirementDocumentFileRepository.findByDocumentRevisionAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(revision);
        ArrayList<String> fileList = new ArrayList<>();
        plmReqDocumentFiles.forEach(plmReqDocumentFile -> {
            File file = getDocumentRevisionFile(requirementDocumentRevisionId, plmReqDocumentFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = revision.getName() + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "REQUIREMENTDOCUMENT",requirementDocumentRevisionId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional
    public PLMRequirementDocumentFile getLatestUploadedReqDocumentFile(Integer requirementDocumentRevisionId, Integer fileId) {
        PLMRequirementDocumentFile requirementFile = requirementDocumentFileRepository.findOne(fileId);
        PLMRequirementDocumentFile plmReqDocumentFile = requirementDocumentFileRepository.findByDocumentRevisionAndFileNoAndLatestTrue(requirementFile.getDocumentRevision(), requirementFile.getFileNo());
        return plmReqDocumentFile;
    }

    public PLMFile updateFileDescription(Integer requirementDocumentRevisionId, Integer id, PLMRequirementDocumentFile plmReqDocumentFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            file.setDescription(plmReqDocumentFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<PLMRequirementDocumentFile> pasteFromClipboard(Integer requirementDocumentRevisionId, Integer fileId, List<PLMFile> files) {
        List<Integer> folderIds = new ArrayList<>();
        files.forEach(file -> {
            if (file.getFileType().equals("FOLDER")) {
                folderIds.add(file.getId());
            }
        });
        List<PLMRequirementDocumentFile> fileList = new ArrayList<>();
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(requirementDocumentRevisionId);
        for (PLMFile file : files) {
            Boolean canCreate = true;
            if (file.getParentFile() != null && folderIds.indexOf(file.getParentFile()) != -1) {
                canCreate = false;
            }
            if (file.getFileType().equals("FILE") && canCreate) {
                PLMRequirementDocumentFile requirementFile = new PLMRequirementDocumentFile();
                PLMRequirementDocumentFile existFile = null;
                if (fileId != 0) {
                    requirementFile.setParentFile(fileId);
                    existFile = requirementDocumentFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndDocumentRevisionAndLatestTrue(file.getName(), fileId, revision);
                } else {
                    existFile = requirementDocumentFileRepository.findByDocumentRevisionAndNameAndParentFileIsNullAndLatestTrue(revision, file.getName());
                }
                if (existFile == null) {
                    requirementFile.setName(file.getName());
                    requirementFile.setDescription(file.getDescription());
                    requirementFile.setDocumentRevision(revision);
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
                    requirementFile = requirementDocumentFileRepository.save(requirementFile);
                    requirementFile.setParentObject(PLMObjectType.REQUIREMENTDOCUMENT);
                    fileList.add(requirementFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + requirementDocumentRevisionId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (requirementFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(requirementDocumentRevisionId, requirementFile.getId());
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + requirementDocumentRevisionId + File.separator + requirementFile.getId();
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
                PLMRequirementDocumentFile requirementDocumentFile = new PLMRequirementDocumentFile();
                PLMRequirementDocumentFile existFile = null;
                if (fileId != 0) {
                    requirementDocumentFile.setParentFile(fileId);
                    existFile = requirementDocumentFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndDocumentRevisionAndLatestTrue(file.getName(), fileId, revision);
                } else {
                    existFile = requirementDocumentFileRepository.findByDocumentRevisionAndNameAndParentFileIsNullAndLatestTrue(revision, file.getName());
                }
                if (existFile == null) {
                    requirementDocumentFile.setName(file.getName());
                    requirementDocumentFile.setDescription(file.getDescription());
                    String folderNumber = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    requirementDocumentFile.setVersion(1);
                    requirementDocumentFile.setSize(0L);
                    requirementDocumentFile.setDocumentRevision(revision);
                    requirementDocumentFile.setFileNo(folderNumber);
                    requirementDocumentFile.setFileType("FOLDER");
                    requirementDocumentFile = requirementDocumentFileRepository.save(requirementDocumentFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + revision.getId();
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(revision.getId(), requirementDocumentFile.getId());
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(requirementDocumentFile);
                    copyFolderFiles(revision, file.getParentObject(), file.getId(), requirementDocumentFile.getId());
                }
            }
        }
        return fileList;
    }

    private void copyFolderFiles(PLMRequirementDocumentRevision revision, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        plmFiles.forEach(plmFile -> {
            PLMRequirementDocumentFile requirementDocumentFile = new PLMRequirementDocumentFile();
            requirementDocumentFile.setParentFile(parent);
            requirementDocumentFile.setName(plmFile.getName());
            requirementDocumentFile.setDescription(plmFile.getDescription());
            requirementDocumentFile.setDocumentRevision(revision);
            String folderNumber = null;
            if (plmFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                requirementDocumentFile.setVersion(1);
                requirementDocumentFile.setFileNo(folderNumber);
                requirementDocumentFile.setSize(plmFile.getSize());
                requirementDocumentFile.setFileType("FILE");
                requirementDocumentFile = requirementDocumentFileRepository.save(requirementDocumentFile);
                requirementDocumentFile.setParentObject(PLMObjectType.REQUIREMENTDOCUMENT);
                plmFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmFile);

                String dir = "";
                if (requirementDocumentFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(revision.getId(), requirementDocumentFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + revision.getId() + File.separator + requirementDocumentFile.getId();
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
                requirementDocumentFile.setVersion(1);
                requirementDocumentFile.setSize(0L);
                requirementDocumentFile.setFileNo(folderNumber);
                requirementDocumentFile.setFileType("FOLDER");
                requirementDocumentFile = requirementDocumentFileRepository.save(requirementDocumentFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + revision.getId();
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(revision.getId(), requirementDocumentFile.getId());
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(revision, objectType, plmFile.getId(), requirementDocumentFile.getId());
            }
        });
    }

    public void undoCopiedReqDocumentFiles(Integer ecoId, List<PLMRequirementDocumentFile> requirementFiles) {
        requirementFiles.forEach(requirementFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(ecoId, requirementFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(requirementFile.getId(), dir);
            requirementDocumentFileRepository.delete(requirementFile.getId());
        });
    }
}