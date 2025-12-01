package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.model.cm.PLMECO;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.FolderDto;
import com.cassinisys.plm.repo.cm.ECORepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.plm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@Service
public class FolderService implements CrudService<PLMFolder, Integer>,
        PageableService<PLMFolder, Integer> {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderObjectRepository folderObjectRepository;

    @Autowired
    private SharedFolderRepository sharedFolderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRevisionRepository itemRevisionRepository;

    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#plmFolder,'create')")
    public PLMFolder create(PLMFolder plmFolder) {
        if (plmFolder.getParent() == null) {
            PLMFolder existFolder = null;
            if (plmFolder.getType().equals(FolderType.PUBLIC)) {
                existFolder = folderRepository.findByParentIsNullAndTypeAndNameEqualsIgnoreCase(FolderType.PUBLIC, plmFolder.getName());
            } else {
                existFolder = folderRepository.findByParentIsNullAndOwnerAndTypeAndNameEqualsIgnoreCase(plmFolder.getOwner(), FolderType.PRIVATE, plmFolder.getName());
            }
            if (existFolder != null) {
                if (existFolder.getId().equals(plmFolder.getId())) {
                    plmFolder = folderRepository.save(plmFolder);
                } else {
                    throw new CassiniException(messageSource.getMessage("folder_name_already_exist",
                            null, "Folder name already exist", LocaleContextHolder.getLocale()));
                }

            } else {
                plmFolder = folderRepository.save(plmFolder);
            }
        }
        if (plmFolder.getParent() != null) {
            PLMFolder existFolder1 = null;
            if (plmFolder.getType().equals(FolderType.PUBLIC)) {
                existFolder1 = folderRepository.findByParentAndTypeAndNameEqualsIgnoreCase(plmFolder.getParent(), FolderType.PUBLIC, plmFolder.getName());
            } else {
                existFolder1 = folderRepository.findByParentAndOwnerAndTypeAndNameEqualsIgnoreCase(plmFolder.getParent(), plmFolder.getOwner(), FolderType.PRIVATE, plmFolder.getName());
            }

            PLMFolder parentFolder = folderRepository.findOne(plmFolder.getParent());
            if (parentFolder.getName().equals(plmFolder.getName())) {
                throw new CassiniException(messageSource.getMessage("folder_name_already_exist",
                        null, "Folder name already exist", LocaleContextHolder.getLocale()));
            }
            if (existFolder1 != null) {
                throw new CassiniException(messageSource.getMessage("folder_name_already_exist",
                        null, "Folder name already exist", LocaleContextHolder.getLocale()));
            } else {
                plmFolder = folderRepository.save(plmFolder);
            }
        }
        return plmFolder;
    }

    @Transactional
    public List<PLMFolderObject> createFolderObject(List<PLMFolderObject> PLMFolderObjects, ObjectType objectType) {
        String items = null;
        PLMFolder folder = null;
        for (PLMFolderObject PLMFolderObject : PLMFolderObjects) {
            folder = folderRepository.findOne(PLMFolderObject.getFolder());
            if (objectType.toString().equals("ITEM")) {
                PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(PLMFolderObject.getObjectId());
                PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
                if (items == null) {
                    items = plmItem.getItemNumber() + "[" + plmItem.getItemName() + "]";
                } else {
                    items = items + " ," + plmItem.getItemNumber() + "[" + plmItem.getItemName() + "]";
                }
            }
            if (objectType.toString().equals("CHANGE")) {
                PLMECO eco = ecoRepository.findOne(PLMFolderObject.getObjectId());
                if (items == null) {
                    items = eco.getEcoNumber() + "[" + eco.getTitle() + "]";
                } else {
                    items = items + " ," + eco.getEcoNumber() + "[" + eco.getTitle() + "]";
                }
            }
            if (objectType.toString().equals("MANUFACTURER")) {
                PLMManufacturer manufacturer = manufacturerRepository.findOne(PLMFolderObject.getObjectId());
                if (items == null) {
                    items = manufacturer.getName();
                } else {
                    items = items + " ," + manufacturer.getName();
                }
            }
            PLMFolderObject.setObjectType(objectType);
        }
        return folderObjectRepository.save(PLMFolderObjects);
    }

    @Transactional
    public PLMSharedFolder createSharedFolder(PLMSharedFolder PLMSharedFolder) {
        return sharedFolderRepository.save(PLMSharedFolder);
    }

    @Override
    @Transactional(
            readOnly = false,
            propagation = Propagation.REQUIRES_NEW
    )
    @PreAuthorize("hasPermission(#plmFolder.id ,'edit')")
    public PLMFolder update(PLMFolder plmFolder) {
        if (plmFolder.getParent() == null) {
            PLMFolder existFolder = null;
            if (plmFolder.getType().equals(FolderType.PUBLIC)) {
                existFolder = folderRepository.findByParentIsNullAndTypeAndNameEqualsIgnoreCase(FolderType.PUBLIC, plmFolder.getName());
            } else {
                existFolder = folderRepository.findByParentIsNullAndOwnerAndTypeAndNameEqualsIgnoreCase(plmFolder.getOwner(), FolderType.PRIVATE, plmFolder.getName());
            }
            if (existFolder != null) {
                if (existFolder.getId().equals(plmFolder.getId())) {
                    plmFolder = folderRepository.save(plmFolder);
                } else {
                    throw new CassiniException(messageSource.getMessage("folder_name_already_exist",
                            null, "Folder name already exist", LocaleContextHolder.getLocale()));
                }

            } else {
                plmFolder = folderRepository.save(plmFolder);
            }
        }
        if (plmFolder.getParent() != null) {
            PLMFolder existFolder1 = null;
            if (plmFolder.getType().equals(FolderType.PUBLIC)) {
                existFolder1 = folderRepository.findByParentAndTypeAndNameEqualsIgnoreCase(plmFolder.getParent(), FolderType.PUBLIC, plmFolder.getName());
            } else {
                existFolder1 = folderRepository.findByParentAndOwnerAndTypeAndNameEqualsIgnoreCase(plmFolder.getParent(), plmFolder.getOwner(), FolderType.PRIVATE, plmFolder.getName());
            }
            PLMFolder parentFolder = folderRepository.findOne(plmFolder.getParent());
            if (parentFolder.getName().equals(plmFolder.getName())) {
                throw new CassiniException(messageSource.getMessage("folder_name_already_exist",
                        null, "Folder name already exist", LocaleContextHolder.getLocale()));
            }
            if (existFolder1 != null) {
                if (existFolder1.getId().equals(plmFolder.getId())) {
                    plmFolder = folderRepository.save(plmFolder);
                } else {
                    throw new CassiniException(messageSource.getMessage("folder_name_already_exist",
                            null, "Folder name already exist", LocaleContextHolder.getLocale()));
                }
            } else {
                plmFolder = folderRepository.save(plmFolder);
            }
        }
        return plmFolder;
    }

    @Transactional
    public PLMFolderObject updateFolderObject(PLMFolderObject PLMFolderObject) {
        return folderObjectRepository.save(PLMFolderObject);
    }

    @Transactional
    public PLMSharedFolder updateSharedFolder(PLMSharedFolder PLMSharedFolder) {
        return sharedFolderRepository.save(PLMSharedFolder);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        PLMFolder plmFolder = folderRepository.findOne(id);
        folderRepository.delete(id);
    }

    @Transactional
    public void deleteFolderObject(Integer id) {
        folderObjectRepository.delete(id);
    }

    @Transactional
    public void deleteSharedFolder(Integer id) {
        sharedFolderRepository.delete(id);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMFolder get(Integer id) {
        return folderRepository.findOne(id);
    }

    public PLMFolderObject getFolderObject(Integer id) {
        return folderObjectRepository.findOne(id);
    }

    public PLMSharedFolder getSharedFolder(Integer id) {
        return sharedFolderRepository.findOne(id);
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMFolder> getAll() {
        return folderRepository.findAll();
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMFolder> getRootFolders() {
        return folderRepository.findByParent(null);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMFolder> getRootFoldersByType(FolderType folderType) {
        return folderRepository.findByParentIsNullAndType(folderType);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMFolder> getRootFoldersByTypeAndPerson(FolderType folderType, Integer person) {
        return folderRepository.findByParentIsNullAndTypeAndOwner(folderType, person);
    }

    public List<PLMFolder> getTreeFolders() {
        return folderRepository.findAll();
    }

    public List<PLMFolder> getChildren(Integer folderId) {
        return folderRepository.findByParent(folderId);
    }

    public List<PLMFolderObject> getFolderObjects(Integer folderId) {
        return folderObjectRepository.findByFolder(folderId);
    }

    public List<PLMFolderObject> getAllFolderObjects() {
        return folderObjectRepository.findAll();
    }

    public List<PLMSharedFolder> getAllSharedFolders() {
        return sharedFolderRepository.findAll();
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMFolder> findAll(Pageable pageable) {
        return folderRepository.findAll(pageable);
    }

    public Page<PLMFolderObject> findAllFolderObjects(Pageable pageable) {
        return folderObjectRepository.findAll(pageable);
    }

    public Page<PLMSharedFolder> findAllSharedFolders(Pageable pageable) {
        return sharedFolderRepository.findAll(pageable);
    }

    public List<PLMFolder> getClassificationTree() {
        List<PLMFolder> folders = getRootFolders();

        for (PLMFolder folder : folders) {
            visitChildren(folder);
        }
        return folders;
    }

    private void visitChildren(PLMFolder parent) {
        List<PLMFolder> children = getChildren(parent.getId());
        for (PLMFolder child : children) {
            List<PLMFolderObject> folderObjects = folderObjectRepository.findByFolder(child.getId());
            if (folderObjects.size() > 0) {
                child.setObjectsExist(true);
            }
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    @Transactional(readOnly = true)
    public FolderDto getFoldersTree(Integer person) {
        FolderDto folderDto = new FolderDto();
        List<PLMFolder> publicFolders = getRootFoldersByType(FolderType.PUBLIC);
        List<PLMFolder> myFolders = getRootFoldersByTypeAndPerson(FolderType.PRIVATE, person);

        for (PLMFolder folder : publicFolders) {
            List<PLMFolderObject> folderObjects = folderObjectRepository.findByFolder(folder.getId());
            if (folderObjects.size() > 0) {
                folder.setObjectsExist(true);
            }
            visitChildren(folder);
        }

        for (PLMFolder folder : myFolders) {
            List<PLMFolderObject> folderObjects = folderObjectRepository.findByFolder(folder.getId());
            if (folderObjects.size() > 0) {
                folder.setObjectsExist(true);
            }
            visitChildren(folder);
        }
        folderDto.getPublicFolders().addAll(publicFolders);
        folderDto.getMyFolders().addAll(myFolders);

        return folderDto;
    }
}


