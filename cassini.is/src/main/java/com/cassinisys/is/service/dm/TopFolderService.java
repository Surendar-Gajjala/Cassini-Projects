package com.cassinisys.is.service.dm;

import com.cassinisys.is.model.dm.ISObjectPermission;
import com.cassinisys.is.model.dm.ISTopDocument;
import com.cassinisys.is.model.dm.ISTopFolder;
import com.cassinisys.is.repo.dm.ISObjectPermissionRepository;
import com.cassinisys.is.repo.dm.TopDocumentRepository;
import com.cassinisys.is.repo.dm.TopFolderRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.service.common.ForgeService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 018 18-Nov -17.
 */
@Service
public class TopFolderService implements CrudService<ISTopFolder, Integer> {

    @Autowired
    private TopFolderRepository topFolderRepository;

    @Autowired
    private TopDocumentRepository topDocumentRepository;

    @Autowired
    private ISObjectPermissionRepository objectPermissionRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private ForgeService forgeService;

    @Override
    @Transactional(readOnly = false)
    public ISTopFolder create(ISTopFolder topFolder) {
        if (topFolder.getParent() == null) {
            ISTopFolder existFolder = topFolderRepository.findByParentIsNullAndNameEqualsIgnoreCase(topFolder.getName());
            if (existFolder != null) {
                if (existFolder.getId().equals(topFolder.getId())) {
                    topFolder = topFolderRepository.save(topFolder);
                } else {
                    throw new RuntimeException("Folder name already exist");
                }

            } else {
                topFolder = topFolderRepository.save(topFolder);
            }
        }
        if (topFolder.getParent() != null) {
            ISTopFolder existFolder1 = topFolderRepository.findByParentAndNameEqualsIgnoreCase(topFolder.getParent(), topFolder.getName());
            ISTopFolder parentFolder = topFolderRepository.findOne(topFolder.getParent());
            if (parentFolder.getName().equals(topFolder.getName())) {
                throw new RuntimeException("Folder name already exist");
            }
            if (existFolder1 != null) {
                throw new RuntimeException("Folder name already exist");
            } else {
                topFolder = topFolderRepository.save(topFolder);
            }
        }
        return topFolder;
    }
//        return topFolderRepository.save(topFolder);
//    }

    @Override
    @Transactional(readOnly = false)
    public ISTopFolder update(ISTopFolder topFolder) {
        if (topFolder.getParent() == null) {
            ISTopFolder existFolder = topFolderRepository.findByParentIsNullAndNameEqualsIgnoreCase(topFolder.getName());
            if (existFolder != null) {
                if (existFolder.getId().equals(topFolder.getId())) {
                    topFolder = topFolderRepository.save(topFolder);
                } else {
                    throw new CassiniException("Folder name already exist");
                }

            } else {
                topFolder = topFolderRepository.save(topFolder);
            }
        }
        if (topFolder.getParent() != null) {
            ISTopFolder existFolder1 = topFolderRepository.findByParentAndNameEqualsIgnoreCase(topFolder.getParent(), topFolder.getName());
            ISTopFolder parentFolder = topFolderRepository.findOne(topFolder.getParent());
            if (parentFolder.getName().equals(topFolder.getName())) {
                throw new CassiniException("Folder name already exist");
            }
            if (existFolder1 != null) {
                if (existFolder1.getId().equals(topFolder.getId())) {
                    topFolder = topFolderRepository.save(topFolder);
                } else {
                    throw new CassiniException("Folder name already exist");
                }
            }
        }
        return topFolder;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        topFolderRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ISTopFolder get(Integer id) {
        return topFolderRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ISTopFolder> getAll() {
        return topFolderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ISTopFolder> getFolderTree() {
        List<ISTopFolder> types = getRootTypes();
        for (ISTopFolder type : types) {
            visitChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<ISTopFolder> getRootTypes() {
        return topFolderRepository.findByParentIsNullOrderByCreatedDateAsc();
    }

    private void visitChildren(ISTopFolder parent) {
        List<ISTopFolder> children = getChildren(parent.getId());
        for (ISTopFolder child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    @Transactional(readOnly = true)
    public List<ISTopFolder> getChildren(Integer parent) {
        return topFolderRepository.findByParentOrderByCreatedDateAsc(parent);
    }

    public List<ISTopDocument> uploadDocuments(Integer folderId, Map<String, MultipartFile> fileMap) {
        checkNotNull(folderId);
        checkNotNull(fileMap);
        ISTopFolder folder = topFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        List<ISTopDocument> uploaded = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String(file.getOriginalFilename().getBytes("iso-8859-1"), "UTF-8");
                ISTopDocument document = topDocumentRepository.findByFolderAndNameAndLatestTrueOrderByModifiedDateDesc(folderId, file.getOriginalFilename());
                Integer version = 1;
                if (document != null) {
                    if (!document.isLocked()) {
                        document.setLatest(false);
                        Integer oldVersion = document.getVersion();
                        version = oldVersion + 1;
                        topDocumentRepository.save(document);
                    } else {
                        throw new CassiniException("Locked Files cannot be updated");
                    }

                }
                document = new ISTopDocument();
                document.setName(name);
                document.setCreatedBy(login.getPerson().getId());
                document.setModifiedBy(login.getPerson().getId());
                document.setFolder(folder.getId());
                document.setVersion(version);
                document.setSize(file.getSize());
                document.setDocumentType(folder.getFolderType());
                document.setLockedBy(1);
                document = topDocumentRepository.save(document);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + folder.getId();
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                String path = dir + File.separator + document.getId();
                fileSystemService.saveDocumentToDisk(file, path);
                Map<String, String> map = forgeService.uploadForgeFile(file.getOriginalFilename(), path);
                if (map != null) {
                    document.setUrn(map.get("urn"));
                    document.setThumbnail(map.get("thumbnail"));
                    document = topDocumentRepository.save(document);
                }
                uploaded.add(document);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    @Transactional(readOnly = true)
    public ISTopDocument getDocument(Integer folderId, Integer documentId) {
        checkNotNull(folderId);
        checkNotNull(documentId);
        ISTopFolder folder = topFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        ISTopDocument document = topDocumentRepository
                .findByFolderAndIdAndLatestTrueOrderByModifiedDateDesc(folderId, documentId);
        if (document == null) {
            throw new ResourceNotFoundException();
        }
        return document;
    }

    @Transactional(readOnly = true)
    public File getDocumentFile(Integer folderId, Integer documentId) {
        checkNotNull(folderId);
        checkNotNull(documentId);
        ISTopFolder folder = topFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        ISTopDocument document = topDocumentRepository
                .findByFolderAndIdAndLatestTrueOrderByModifiedDateDesc(folderId, documentId);
        if (document == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + folder.getId() + File.separator + document.getId();
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public List<ISObjectPermission> getObjectPermissionsByFolder(Integer folderId) {
        checkNotNull(folderId);
        ISTopFolder folder = topFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        return objectPermissionRepository.findByObjectId(folderId);
    }

    @Transactional(readOnly = false)
    public ISTopDocument updateFolderDocument(Integer folderId, ISTopDocument document) {
        checkNotNull(folderId);
        checkNotNull(document);
        ISTopFolder folder = topFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        document = topDocumentRepository.save(document);
        return document;

    }

    @Transactional(readOnly = false)
    public void deleteDocument(Integer folderId, Integer documentId) {
        checkNotNull(folderId);
        checkNotNull(documentId);
        ISTopFolder folder = topFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        ISTopDocument document = topDocumentRepository.findOne(documentId);
        List<ISTopDocument> topDocuments = topDocumentRepository.findByFolderAndName(folderId, document.getName());
        if (document == null) {
            throw new ResourceNotFoundException();
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + folderId;
        for (ISTopDocument topDocument1 : topDocuments) {
            fileSystemService.deleteDocumentFromDisk(topDocument1.getId(), dir);
            topDocumentRepository.delete(topDocument1);
        }

    }

    @Transactional(readOnly = false)
    public List<ISObjectPermission> updateRolePermissions(Integer folderId, List<ISObjectPermission> objectPermissions) {
        checkNotNull(folderId);
        checkNotNull(objectPermissions);
        ISTopFolder folder = topFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        objectPermissionRepository.delete(objectPermissionRepository.findByObjectId(folderId));
        return objectPermissionRepository.save(objectPermissions);
    }
}
