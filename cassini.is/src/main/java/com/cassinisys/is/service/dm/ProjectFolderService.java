package com.cassinisys.is.service.dm;

import com.cassinisys.is.model.dm.ISObjectPermission;
import com.cassinisys.is.model.dm.ISProjectDocument;
import com.cassinisys.is.model.dm.ISProjectFolder;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.repo.dm.ISObjectPermissionRepository;
import com.cassinisys.is.repo.dm.ProjectDocumentRepository;
import com.cassinisys.is.repo.dm.ProjectFolderRepository;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.service.common.ForgeService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for ProjectFolderService
 */
@Service
@Transactional
public class ProjectFolderService extends FolderService implements
        PageableService<ISProjectFolder, Integer> {

    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ProjectFolderRepository projectFolderRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectDocumentRepository documentRepository;
    @Autowired
    private ISObjectPermissionRepository objectPermissionRepository;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private ForgeService forgeService;

    /**
     * The method used to create ISProjectFolder
     **/
    @Transactional(readOnly = false)
    public ISProjectFolder create(Integer projectId, ISProjectFolder folder) {
        checkNotNull(folder);
        folder.setId(null);
        ISProject project = projectRepository.findOne(folder.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        if (folder.getParent() == null) {
            ISProjectFolder existFolder = projectFolderRepository.findByProjectAndParentIsNullAndNameEqualsIgnoreCaseAndFolderType(projectId, folder.getName(), folder.getFolderType());
            if (existFolder != null) {
                if (existFolder.getId().equals(folder.getId())) {
                    folder = projectFolderRepository.save(folder);
                    createFolder(project.getId(), folder);
                } else {
                    throw new RuntimeException("Folder name already exist");
                }

            } else {
                folder = projectFolderRepository.save(folder);
            }
        }
        if (folder.getParent() != null) {
            ISProjectFolder existFolder1 = projectFolderRepository.findByProjectAndParentAndNameEqualsIgnoreCaseAndFolderType(projectId, folder.getParent(), folder.getName(), folder.getFolderType());
            ISProjectFolder parentFolder = projectFolderRepository.findOne(folder.getParent());
            if (parentFolder.getName().equals(folder.getName())) {
                throw new RuntimeException("Folder name already exist");
            }
            if (existFolder1 != null) {
                throw new RuntimeException("Folder name already exist");
            } else {
                folder = projectFolderRepository.save(folder);
                createFolder(project.getId(), folder);
            }
        }
        return folder;
    }

    /**
     * The method used to update ISProjectFolder
     **/
    @Transactional(readOnly = false)
    public ISProjectFolder update(Integer projectId, ISProjectFolder folder) {
        checkNotNull(folder);
        checkNotNull(folder.getId());
        ISProject project = projectRepository.findOne(folder.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        if (folder.getParent() == null) {
            ISProjectFolder existFolder = projectFolderRepository.findByProjectAndParentIsNullAndNameEqualsIgnoreCaseAndFolderType(projectId, folder.getName(), folder.getFolderType());
            if (existFolder != null) {
                if (existFolder.getId().equals(folder.getId())) {
                    folder = projectFolderRepository.save(folder);
                } else {
                    throw new CassiniException("Folder name already exist");
                }

            } else {
                folder = projectFolderRepository.save(folder);
            }
        }
        if (folder.getParent() != null) {
            ISProjectFolder existFolder1 = projectFolderRepository.findByProjectAndParentAndNameEqualsIgnoreCaseAndFolderType(projectId, folder.getParent(), folder.getName(), folder.getFolderType());
            ISProjectFolder parentFolder = projectFolderRepository.findOne(folder.getParent());
            if (parentFolder.getName().equals(folder.getName())) {
                throw new CassiniException("Folder name already exist");
            }
            if (existFolder1 != null) {
                if (existFolder1.getId().equals(folder.getId())) {
                    folder = projectFolderRepository.save(folder);
                } else {
                    throw new CassiniException("Folder name already exist");
                }
            }
        }
        return folder;
    }

    /**
     * The method used to delete folder of ISProjectFolder
     **/
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISProjectFolder folder = projectFolderRepository.findOne(id);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        ISProject project = projectRepository.findOne(folder.getProject());
        projectFolderRepository.delete(folder);

    }

    /**
     * The method used to get ISProjectFolder
     **/
    @Transactional(readOnly = true)
    public ISProjectFolder get(Integer id) {
        checkNotNull(id);
        ISProjectFolder folder = projectFolderRepository.findOne(id);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        return folder;
    }

    /**
     * The method used to getAll for the List of  ISProjectFolder
     **/
    @Transactional(readOnly = true)
    public List<ISProjectFolder> getAll() {
        return projectFolderRepository.findAll();
    }

    /**
     * The method used to findAll for the Page of  ISProjectFolder
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISProjectFolder> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("name")));
        }
        return projectFolderRepository.findAll(pageable);
    }

    /**
     * The method used to getChildren for the list of  ISProjectFolder
     **/
    @Transactional(readOnly = true)
    public List<ISProjectFolder> getChildren(Integer projectFolderId) {
        checkNotNull(projectFolderId);
        ISProjectFolder folder = projectFolderRepository
                .findOne(projectFolderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        return projectFolderRepository.findByParentOrderByCreatedDateAsc(projectFolderId);
    }

    /**
     * The method used to getDocuments for the list of ISProjectDocument
     **/
    @Transactional(readOnly = true)
    public List<ISProjectDocument> getDocuments(Integer projectFolderId) {
        checkNotNull(projectFolderId);
        ISProjectFolder folder = projectFolderRepository
                .findOne(projectFolderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        return documentRepository.findByFolderAndLatestTrueOrderByModifiedDateDesc(projectFolderId);
    }

    /**
     * The method used to uploadDocuments for the list of ISProjectDocument
     **/
    public List<ISProjectDocument> uploadDocuments(Integer folderId,
                                                   Map<String, MultipartFile> fileMap) {
        checkNotNull(folderId);
        checkNotNull(fileMap);
        ISProjectFolder folder = projectFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        List<ISProjectDocument> uploaded = new ArrayList<ISProjectDocument>();
        Login login = sessionWrapper.getSession().getLogin();
        for (MultipartFile file : fileMap.values()) {
            ISProjectDocument document = documentRepository
                    .findByFolderAndNameAndLatestTrueOrderByModifiedDateDesc(folderId,
                            file.getOriginalFilename());
            Integer version = 1;
            if (document != null) {
                if (!document.isLocked()) {
                    document.setLatest(false);
                    Integer oldVersion = document.getVersion();
                    version = oldVersion + 1;
                    documentRepository.save(document);
                } else {
                    throw new CassiniException("Locked Files cannot be updated");
                }

            }
            document = new ISProjectDocument();
            document.setName(file.getOriginalFilename());
            document.setCreatedBy(login.getPerson().getId());
            document.setModifiedBy(login.getPerson().getId());
            document.setFolder(folder.getId());
            document.setVersion(version);
            document.setSize(file.getSize());
            document.setProject(folder.getProject());
            document.setDocumentType(folder.getFolderType());
            document.setLockedBy(1);
            document = documentRepository.save(document);
            ISProject project = projectRepository.findOne(folder.getProject());
            String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getFolderPath(project.getId(), folder);
            File file1 = new File(path);
            try {
                if (!file1.exists()) {
                    FileUtils.forceMkdir(file1);
                }
            } catch (IOException e) {
                throw new CassiniException("Failed to create file " + path + " REASON: " + e.getMessage());
            }
            path = path + File.separator + document.getId();
            fileSystemService.saveDocumentToDisk(file, path);
            Map<String, String> map = forgeService.uploadForgeFile(file.getOriginalFilename(), path);
            if (map != null) {
                document.setUrn(map.get("urn"));
                document.setThumbnail(map.get("thumbnail"));
                document = documentRepository.save(document);
            }
            uploaded.add(document);
        }
        return uploaded;
    }

    /**
     * The method used to getDocument of ISProjectDocument
     **/
    @Transactional(readOnly = true)
    public ISProjectDocument getDocument(Integer folderId, Integer documentId) {
        checkNotNull(folderId);
        checkNotNull(documentId);
        ISProjectFolder folder = projectFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        ISProjectDocument document = documentRepository
                .findByFolderAndIdAndLatestTrueOrderByModifiedDateDesc(folderId, documentId);
        if (document == null) {
            throw new ResourceNotFoundException();
        }
        return document;
    }

    /**
     * The method used to getDocumentFile of file
     **/
    @Transactional(readOnly = true)
    public File getDocumentFile(Integer folderId, Integer documentId) {
        checkNotNull(folderId);
        checkNotNull(documentId);
        ISProjectFolder folder = projectFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        ISProjectDocument document = documentRepository
                .findByFolderAndIdAndLatestTrueOrderByModifiedDateDesc(folderId, documentId);
        if (document == null) {
            throw new ResourceNotFoundException();
        }
        ISProject project = projectRepository.findOne(folder.getProject());
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + getDocumentPath(project.getId(), document);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * The method used to findMultipleDocuments for the list of  ISProjectDocument
     **/
    @Transactional(readOnly = true)
    public List<ISProjectDocument> findMultipleDocuments(List<Integer> ids) {
        return documentRepository.findByIdIn(ids);
    }

    /**
     * The method used to findMultipleFolders for the list of ISProjectFolder
     **/
    @Transactional(readOnly = true)
    public List<ISProjectFolder> findMultipleFolders(List<Integer> ids) {
        return projectFolderRepository.findByIdIn(ids);
    }

    /**
     * The method used to updateDocument for   ISProjectDocument
     **/
    @Transactional(readOnly = false)
    public ISProjectDocument updateDocument(Integer folderId, ISProjectDocument document) {
        checkNotNull(folderId);
        checkNotNull(document);
        ISProjectFolder folder = projectFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        document = documentRepository.save(document);
        return document;

    }

    /**
     * The method used to getDocumentVersions for the list of ISProjectDocument
     **/
    @Transactional(readOnly = true)
    public List<ISProjectDocument> getDocumentVersions(Integer folderId,
                                                       Integer documentId) {
        checkNotNull(folderId);
        checkNotNull(documentId);
        ISProjectFolder folder = projectFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        ISProjectDocument document = documentRepository.findOne(documentId);
        if (document == null) {
            throw new ResourceNotFoundException();
        }
        return documentRepository.findByFolderAndNameOrderByVersionDesc(
                folderId, document.getName());
    }

    /**
     * The method used to updateRolePermissions  for the list of ISObjectPermission
     **/
    @Transactional(readOnly = false)
    public List<ISObjectPermission> updateRolePermissions(Integer folderId, List<ISObjectPermission> objectPermissions) {
        checkNotNull(folderId);
        checkNotNull(objectPermissions);
        objectPermissionRepository.delete(objectPermissionRepository.findByObjectId(folderId));
        ISProjectFolder folder = projectFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        return objectPermissionRepository.save(objectPermissions);
    }

    /**
     * The method used to getObjectPermissionsByFolder for the list ISObjectPermission
     **/
    @Transactional(readOnly = true)
    public List<ISObjectPermission> getObjectPermissionsByFolder(Integer folderId) {
        checkNotNull(folderId);
        ISProjectFolder folder = projectFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        return objectPermissionRepository.findByObjectId(folderId);
    }

    @Transactional(readOnly = false)
    public void deleteDocument(Integer folderId, Integer documentId) {
        checkNotNull(folderId);
        checkNotNull(documentId);
        ISProjectFolder folder = projectFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        ISProjectDocument document = documentRepository.findOne(documentId);
        List<ISProjectDocument> documents = documentRepository.findByFolderAndName(folderId, document.getName());
        if (documents.size() == 0) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + getFolderPath(folder.getProject(), folder);
        fileSystemService.deleteDocumentFromDisk(documentId, path);
        documentRepository.delete(documents);
    }

}