package com.cassinisys.pdm.service;

import com.cassinisys.pdm.filtering.FolderCriteria;
import com.cassinisys.pdm.filtering.FolderPredicateBuilder;
import com.cassinisys.pdm.model.*;
import com.cassinisys.pdm.model.QPDMFolder;
import com.cassinisys.pdm.repo.*;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.ActionType;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Service
@Transactional
public class PDMFolderService implements CrudService<PDMFolder, Integer> {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private PDMFileRepository pdmFileRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private VaultRepository vaultRepository;

    @Autowired
    private FolderAttributeRepository folderAttributeRepository;

    @Autowired
    private PDMObjectPermissionRepository objectPermissionRepository;

    @Autowired
    private FolderPredicateBuilder folderPredicateBuilder;

    @Override
    public PDMFolder create(PDMFolder pdmFolder) {
        checkNotNull(pdmFolder);

        pdmFolder.setTimeStamp(new Date());
        pdmFolder= folderRepository.save(pdmFolder);

        PDMFolder folder = null;
        if (pdmFolder.getParent() != null) {
            folder = folderRepository.getOne(pdmFolder.getParent());
        }
        if (folder != null) {
            pdmFolder.setPath(folder.getPath() + "/" + pdmFolder.getName());
            pdmFolder.setIdPath(folder.getIdPath() + "/" + pdmFolder.getId());

        } else {
            PDMVault vault = vaultRepository.getOne(pdmFolder.getVault());
            pdmFolder.setPath(vault.getName()+ "/" + pdmFolder.getName());
            pdmFolder.setIdPath(vault.getId() + "/" + pdmFolder.getId());
        }

        pdmFolder= folderRepository.save(pdmFolder);

        PDMObjectPermission pdmObjectPermission= new PDMObjectPermission();
        pdmObjectPermission.setActionTypes(new String[]{"ALL"});
        pdmObjectPermission.setObjectType(ObjectType.FOLDER);
        pdmObjectPermission.setObjectId(pdmFolder.getId());
        pdmObjectPermission.setPermissionLevel(PermissionLevel.PERSON);
        pdmObjectPermission.setPermissionAssignedTo(sessionWrapper.getSession().getLogin().getPerson().getId());

        objectPermissionRepository.save(pdmObjectPermission);

        return  pdmFolder;
    }

    public PDMFolderAttribute createAttribute(PDMFolderAttribute folderAttribute) {
        checkNotNull(folderAttribute);
        return folderAttributeRepository.save(folderAttribute);
    }

    @Override
    public PDMFolder update(PDMFolder pdmFolder) {
        checkNotNull(pdmFolder);
        return folderRepository.save(pdmFolder);
    }

    public PDMFolderAttribute updateAttribte(PDMFolderAttribute folderAttribute) {
        checkNotNull(folderAttribute);
        return folderAttributeRepository.save(folderAttribute);
    }

    @Override
    public void delete(Integer folderId) {
        checkNotNull(folderId);
        PDMFolder pdmFolder = folderRepository.findOne(folderId);
        if (pdmFolder == null) {
            throw new ResourceNotFoundException();
        }
        folderRepository.delete(folderId);
    }

    @Override
    public PDMFolder get(Integer folderId) {
        checkNotNull(folderId);
        return folderRepository.findOne(folderId);
    }


    @Transactional(readOnly = true)
    public List<PDMFile> getFiles(Integer vaultFolderId) {
        checkNotNull(vaultFolderId);
        PDMFolder folder = folderRepository
                .findOne(vaultFolderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        return pdmFileRepository.findByFolderAndLatestTrueOrderByModifiedDateDesc(vaultFolderId);
    }
    @Override
    public List<PDMFolder> getAll() {
        return folderRepository.findAll();
    }

    public List<PDMFolder> getByVault(Integer vaultId) {
        return folderRepository.findByVault(vaultId);
    }

    public List<PDMFolder> getRootFolders(Integer vaultId) {
        List<PDMFolder> parents = folderRepository.findRootFolders(vaultId);
        visitFoldersTree(parents);
        return parents;
    }

    /**
     * The method used to visitFoldersTree
     **/
    private void visitFoldersTree(List<PDMFolder> parents) {
        for (PDMFolder folder : parents) {
            List<PDMFolder> children = folderRepository.findByParentOrderByCreatedDateAsc(folder.getId());
            visitFoldersTree(children);
            for (PDMFolder child : children) {
                folder.getChildren().add(child);
            }
        }
    }

    public List<PDMFolder> getChildren(Integer folderId) {
        return folderRepository.findByParentOrderByCreatedDateAsc(folderId);
    }



    public Page<PDMFolder> findAll(Pageable pageable) {
        return folderRepository.findAll(pageable);
    }

    /**
     * The method used to updateRolePermissions  for the list of PDMObjectPermission
     **/
    public List<PDMObjectPermission> updateRolePermissions(Integer folderId, List<PDMObjectPermission> objectPermissions) {
        checkNotNull(folderId);
        checkNotNull(objectPermissions);

        List<PDMObjectPermission>  existingObjpermissions=getObjectPermissionsByFolder(folderId);

        objectPermissionRepository.delete(existingObjpermissions);

        PDMFolder folder = folderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        return objectPermissionRepository.save(objectPermissions);
    }

    /**
     * The method used to getObjectPermissionsByFolder for the list PDMObjectPermission
     **/
    public List<PDMObjectPermission> getObjectPermissionsByFolder(Integer folderId) {
        checkNotNull(folderId);
        PDMFolder folder = folderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        return objectPermissionRepository.findByObjectId(folderId);
    }

    public Page<PDMFolder> freeTextSearch(Pageable pageable, FolderCriteria folderCriteria) {
        Predicate predicate = folderPredicateBuilder.build(folderCriteria, QPDMFolder.pDMFolder);
        Page<PDMFolder> pdmFolders = folderRepository.findAll(predicate, pageable);
        return pdmFolders;
    }

    public Page<PDMFolder> freeTextSearchAll(Pageable pageable, FolderCriteria folderCriteria) {
        Predicate predicate = folderPredicateBuilder.getFreeTextSearchPredicateForAll(folderCriteria, QPDMFolder.pDMFolder);
        Page<PDMFolder> pdmFolders = folderRepository.findAll(predicate, pageable);
        return pdmFolders;
    }

    public PDMFolder createByPath(Integer vaultId, String path, String separator) {
        if(separator.equalsIgnoreCase("\\")) {
            separator = "\\\\";
        }

        PDMFolder pdmFolder = null;
        Integer parentFolderId = null;
        String paths[] = path.split(separator);
        for(int i=0; i<paths.length; i++) {
            String name = paths[i];
            pdmFolder = folderRepository.findByVaultAndParentAndName(vaultId, parentFolderId, name);
            if(pdmFolder == null) {
                pdmFolder = new PDMFolder();
                pdmFolder.setVault(vaultId);
                pdmFolder.setParent(parentFolderId);
                pdmFolder.setName(name);
                pdmFolder = create(pdmFolder);
            }

            parentFolderId = pdmFolder.getId();
        }

        return pdmFolder;
    }
}
