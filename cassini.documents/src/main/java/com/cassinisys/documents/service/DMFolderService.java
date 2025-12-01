package com.cassinisys.documents.service;

import com.cassinisys.documents.model.dm.DMDocument;
import com.cassinisys.documents.model.dm.DMFolder;
import com.cassinisys.documents.model.dm.DMObjectPermission;
import com.cassinisys.documents.repo.DMDocumentRepository;
import com.cassinisys.documents.repo.DMFolderRepository;
import com.cassinisys.documents.repo.DMObjectPermissionRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by swapna on 11/12/18.
 */
@Service
public class DMFolderService {
    @Autowired
    private DMFolderRepository folderRepository;

    @Autowired
    private DMDocumentRepository documentRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private DMObjectPermissionRepository objectPermissionRepository;

    @Transactional(readOnly = false)
    public DMFolder create(DMFolder topFolder) {


        if (topFolder.getParent() == null) {
            DMFolder existFolder = folderRepository.findByParentIsNullAndNameEqualsIgnoreCase(topFolder.getName());
            if (existFolder != null) {
                if (existFolder.getId().equals(topFolder.getId())) {
                    topFolder = folderRepository.save(topFolder);
                } else {
                    throw new RuntimeException("Folder name already exist");
                }

            } else {
                topFolder = folderRepository.save(topFolder);
            }
        }
        if (topFolder.getParent() != null) {
            DMFolder  existFolder1 = folderRepository.findByParentAndNameEqualsIgnoreCase(topFolder.getParent(), topFolder.getName());
            DMFolder parentFolder = folderRepository.findOne(topFolder.getParent());
            if (parentFolder.getName().equals(topFolder.getName())) {
                throw new RuntimeException("Folder name already exist");
            }
            if (existFolder1 != null) {
                throw new RuntimeException("Folder name already exist");
            } else {
                topFolder = folderRepository.save(topFolder);
            }
        }


        return topFolder;
    }

//        return folderRepository.save(topFolder);
//    }

    @Transactional(readOnly = false)
    public DMFolder update(DMFolder topFolder) {


        if (topFolder.getParent() == null) {
            DMFolder existFolder = folderRepository.findByParentIsNullAndNameEqualsIgnoreCase(topFolder.getName());
            if (existFolder != null) {
                if (existFolder.getId().equals(topFolder.getId())) {
                    topFolder = folderRepository.save(topFolder);
                } else {
                    throw new CassiniException("Folder name already exist");
                }

            } else {
                topFolder = folderRepository.save(topFolder);
            }
        }

        if (topFolder.getParent() != null) {
            DMFolder existFolder1 = folderRepository.findByParentAndNameEqualsIgnoreCase(topFolder.getParent(), topFolder.getName());
            DMFolder parentFolder = folderRepository.findOne(topFolder.getParent());
            if (parentFolder.getName().equals(topFolder.getName())) {
                throw new CassiniException("Folder name already exist");
            }
            if (existFolder1 != null) {
                if (existFolder1.getId().equals(topFolder.getId())) {
                    topFolder =folderRepository.save(topFolder);
                } else {
                    throw new CassiniException("Folder name already exist");
                }
            }
        }


        return topFolder;
    }

    @Transactional(readOnly = false)
    public void delete(Integer id) {
        folderRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public DMFolder get(Integer id) {
        return folderRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<DMFolder> getAll() {
        return folderRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<DMFolder> getFolderTree() {
        List<DMFolder> types = getRootTypes();
        for (DMFolder type : types) {
            visitChildren(type);
        }

        return types;
    }
    @Transactional(readOnly = true)
    public List<DMFolder> getRootTypes() {
        return folderRepository.findByParentIsNullOrderByCreatedDateAsc();
    }

    private void visitChildren(DMFolder parent) {
        List<DMFolder> children = getChildren(parent.getId());

        for (DMFolder child : children) {
            visitChildren(child);
        }

        parent.setChildren(children);
    }
    @Transactional(readOnly = true)
    public List<DMFolder> getChildren(Integer parent) {
        return folderRepository.findByParentOrderByCreatedDateAsc(parent);
    }

    public List<DMDocument> uploadDocuments(Integer folderId, Map<String, MultipartFile> fileMap) {
        checkNotNull(folderId);
        checkNotNull(fileMap);
        DMFolder folder = folderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        List<DMDocument> uploaded = new ArrayList<>();

        Login login = sessionWrapper.getSession().getLogin();
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String(file.getOriginalFilename().getBytes("iso-8859-1"), "UTF-8");

                DMDocument document = documentRepository.findByFolderAndNameAndLatestTrueOrderByModifiedDateDesc(folderId, file.getOriginalFilename());

                Integer version = 1;
                if (document != null) {
                    if(!document.isLocked()) {
                        document.setLatest(false);
                        Integer oldVersion = document.getVersion();
                        version = oldVersion + 1;
                        documentRepository.save(document);
                    }
                    else {
                        throw new CassiniException("Locked Files cannot be updated");
                    }

                }
                document = new DMDocument();
                document.setName(name);
                document.setCreatedBy(login.getPerson().getId());
                document.setModifiedBy(login.getPerson().getId());
                document.setFolder(folder.getId());
                document.setVersion(version);
                document.setSize(file.getSize());
                document.setLockedBy(1);
                document = documentRepository.save(document);


                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + folder.getId();
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }

                String path = dir + File.separator + document.getId();
                saveDocumentToDisk(file, path);
                uploaded.add(document);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return uploaded;
    }

    @Transactional
    protected void saveDocumentToDisk(MultipartFile multipartFile, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    @Transactional(readOnly = true)
    public DMDocument getDocument(Integer folderId, Integer documentId) {
        checkNotNull(folderId);
        checkNotNull(documentId);
        DMFolder folder = folderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        DMDocument document = documentRepository
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
        DMFolder folder = folderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        DMDocument document = documentRepository
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

    @Transactional(readOnly = false)
    public DMDocument updateFolderDocument(Integer folderId, DMDocument document) {
        checkNotNull(folderId);
        checkNotNull(document);
        DMFolder folder = folderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        document = documentRepository.save(document);

        return document;

    }
    
    @Transactional(readOnly = false)
    public void deleteDocument(Integer folderId, Integer documentId) {
        checkNotNull(folderId);
        checkNotNull(documentId);
        DMFolder folder = folderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        DMDocument document = documentRepository.findOne(documentId);
        List<DMDocument> topDocuments = documentRepository.findByFolderAndName(folderId, document.getName());
        if (document == null) {
            throw new ResourceNotFoundException();
        }
        documentRepository.delete(topDocuments);
    }

    @Transactional(readOnly = false)
    public List<DMObjectPermission> updateRolePermissions(Integer folderId, List<DMObjectPermission> objectPermissions) {
        checkNotNull(folderId);
        checkNotNull(objectPermissions);
        DMFolder folder = folderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        objectPermissionRepository.delete(objectPermissionRepository.findByObjectId(folderId));

        return objectPermissionRepository.save(objectPermissions);
    }

    @Transactional(readOnly = true)
    public List<DMObjectPermission> getObjectPermissionsByFolder(Integer folderId) {
        checkNotNull(folderId);
        DMFolder folder = folderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        return objectPermissionRepository.findByObjectId(folderId);
    }
}
