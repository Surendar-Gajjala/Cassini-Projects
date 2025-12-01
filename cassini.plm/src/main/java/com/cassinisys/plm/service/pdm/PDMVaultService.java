package com.cassinisys.plm.service.pdm;

import com.cassinisys.platform.config.AutowiredLogger;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.service.col.AttachmentService;
import com.cassinisys.platform.service.common.ForgeService;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.plm.filtering.*;
import com.cassinisys.plm.model.pdm.*;
import com.cassinisys.plm.model.pdm.dto.ComponentReference;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.repo.pdm.*;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

@Service
@Transactional
public class PDMVaultService {
    @AutowiredLogger
    private Logger LOGGER;

    @Autowired
    private PDMObjectRepository pdmObjectRepository;

    @Autowired
    private PDMObjectFileRepository pdmObjectFileRepository;

    @Autowired
    private PDMVaultRepository pdmVaultRepository;

    @Autowired
    private PDMFolderPredicateBuilder pdmFolderPredicateBuilder;

    @Autowired
    private PDMFolderRepository pdmFolderRepository;

    @Autowired
    private PDMFileRepository pdmFileRepository;

    @Autowired
    private PDMFileVersionRepository pdmFileVersionRepository;

    @Autowired
    private PDMAssemblyRepository pdmAssemblyRepository;

    @Autowired
    private PDMPartRepository pdmPartRepository;

    @Autowired
    private PDMCommitRepository pdmCommitRepository;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PDMService pdmService;

    @Autowired
    private ForgeService forgeService;

    @Autowired
    private PDMRevisionedObjectRepository pdmRevisionedObjectRepository;

    @Autowired
    private PDMFileVersionPredicateBuilder fileVersionPredicateBuilder;

    @Autowired
    private PDMDrawingRepository pdmDrawingRepository;

    @Autowired
    private PDMBOMOccurrenceRepository pdmBOMOccurrenceRepository;

    @Autowired
    private FileDownloadService fileDownloadService;
    @Autowired
    private PDMVaultPredicateBuilder pdmVaultPredicateBuilder;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private PDMThumbnailRepository pdmThumbnailRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private PDMRevisionMasterRepository pdmRevisionMasterRepository;
    @Autowired
    private PDMAssemblyBOMOccurrenceRepository pdmAssemblyBOMOccurrenceRepository;
    @Autowired
    private PDMPartBOMOccurrenceRepository pdmPartBOMOccurrenceRepository;


    /* Vault methods */
    @PreAuthorize("hasPermission(#vault,'create')")
    public PDMVault createVault(PDMVault vault) {
        PDMVault exists = pdmVaultRepository.findByName(vault.getName());
        if (exists != null) {
            throw new PDMException(messageSource.getMessage("vault_already_exists", new Object[]{vault.getName()}, LocaleContextHolder.getLocale()));
        }

        return pdmVaultRepository.save(vault);
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PDMVault getVault(Integer id) {
        return pdmVaultRepository.findOne(id);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PDMVault> getAllVaults() {
        List<PDMVault> pdmVaults = pdmVaultRepository.findAll();
        List<Object[]> counts = pdmVaultRepository.getCountsByType();
        Map<String, Integer> map = new HashMap<>();
        counts.forEach(c -> {
            String s = c[0] + ":" + c[1];
            map.put(s, ((BigInteger) c[2]).intValue());
        });

        List<String> types = Arrays.asList("ASSEMBLY", "PART", "DRAWING");

        pdmVaults.forEach(v -> {
            types.forEach(t -> {
                String s = v.getId() + ":" + t;
                if (map.containsKey(s)) {
                    if (t.equalsIgnoreCase("ASSEMBLY")) {
                        v.setAssemblasCount(map.get(s));
                    } else if (t.equalsIgnoreCase("PART")) {
                        v.setPartsCount(map.get(s));
                    } else if (t.equalsIgnoreCase("DRAWING")) {
                        v.setDrawingsCount(map.get(s));
                    }
                }
            });
        });

        //getChildCount(pdmVaults);
        return pdmVaults;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PDMVault> getSearchVaults(PDMObjectCriteria objectCriteria) {
        Predicate predicate = pdmVaultPredicateBuilder.build(objectCriteria, QPDMVault.pDMVault);
        List<PDMVault> vaults = IterableUtils.toList(pdmVaultRepository.findAll(predicate));
        //getChildCount(vaults);
        return vaults;
    }

    private void getChildCount(List<PDMVault> pdmVaults) {
        for (PDMVault pdmVault : pdmVaults) {
            Integer assemblasCount = 0;
            Integer partsCount = 0;
            Integer drawingsCount = 0;
            Integer normalCount = 0;
            List<PDMFolder> folders = getRootFolders(pdmVault.getId());
            for (PDMFolder folder : folders) {
                List<PDMFolder> folders1 = getFolderChildren(folder.getId());
                if (folders1.size() > 0) {
                    getNestedChildCount(folders1, assemblasCount, partsCount, drawingsCount, normalCount);
                }
                if (folder != null) {
                    List<PDMFileVersion> files = pdmFileVersionRepository.findByFolderAndLatest(folder.getId());
                    folder = this.getCountByType(folder, files);
                    assemblasCount += folder.getAssemblasCount();
                    partsCount += folder.getPartsCount();
                    drawingsCount += folder.getDrawingsCount();
                    normalCount += folder.getCommitsCount();
                }
            }
            pdmVault.setAssemblasCount(assemblasCount);
            pdmVault.setPartsCount(partsCount);
            pdmVault.setDrawingsCount(drawingsCount);
            pdmVault.setCommitsCount(normalCount);
        }
    }

    private void getNestedChildCount(List<PDMFolder> folders, Integer asm, Integer part, Integer drw, Integer nor) {
        for (PDMFolder folder : folders) {
            List<PDMFolder> folders2 = getFolderChildren(folder.getId());
            if (folders2.size() > 0) {
                getNestedChildCount(folders2, asm, part, drw, nor);
            }
            if (folder != null) {
                List<PDMFileVersion> files = pdmFileVersionRepository.findByFolderAndLatest(folder.getId());
                folder = this.getCountByType(folder, files);
                asm += folder.getAssemblasCount();
                part += folder.getPartsCount();
                drw += folder.getDrawingsCount();
                nor += folder.getCommitsCount();
            }
        }
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PDMVault> getVaultsByPageable(Pageable pageable, PDMObjectCriteria objectCriteria) {
        Predicate predicate = pdmVaultPredicateBuilder.build(objectCriteria, QPDMVault.pDMVault);
        Page<PDMVault> vaults = pdmVaultRepository.findAll(predicate, pageable);
        return vaults;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PDMVault> getAllVaultsPageable(Pageable pageable) {
        return pdmVaultRepository.findAll(pageable);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PDMVault> getMultipleVaults(List<Integer> ids) {
        return pdmVaultRepository.findByIdIn(ids);
    }

    @PreAuthorize("hasPermission(#vault.id ,'edit')")
    public PDMVault updateVault(PDMVault vault) {
        return pdmVaultRepository.save(vault);
    }

    @PreAuthorize("hasPermission(#vaultId,'delete')")
    public void deleteVault(Integer vaultId) {
        pdmVaultRepository.delete(vaultId);
    }


    /* Folder methods */
    @PreAuthorize("hasPermission(#folder,'create')")
    public PDMFolder createRootFolder(Integer vaultId, PDMFolder folder) {
        PDMVault vault = pdmVaultRepository.findOne(vaultId);
        PDMFolder found = pdmFolderRepository.findByVaultAndNameAndParentIsNull(vaultId, folder.getName());
        if (found != null) {
            throw new PDMException(messageSource.getMessage("folder_in_vault_already_exists",
                    new Object[]{folder.getName(), vault.getName()}, LocaleContextHolder.getLocale()));
        }
        folder = pdmFolderRepository.save(folder);
        folder.setNamePath(folder.getName());
        folder.setIdPath("" + folder.getId());
        folder = pdmFolderRepository.save(folder);
        return folder;
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PDMFolder getRootFolderByName(Integer vaultId, String folderName) {
        return pdmFolderRepository.findByVaultAndNameAndParentIsNull(vaultId, folderName);
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PDMFolder getFolderByNameInParent(Integer parentId, String folderName) {
        return pdmFolderRepository.findByParentAndName(parentId, folderName);
    }

    @PreAuthorize("hasPermission(#pdmFolder,'create')")
    public PDMFolder createChildFolder(Integer parentFolderId, PDMFolder pdmFolder) {
        PDMFolder parentFolder = pdmFolderRepository.findOne(parentFolderId);
        PDMFolder found = pdmFolderRepository.findByParentAndName(parentFolderId, pdmFolder.getName());
        if (found != null) {
            throw new PDMException(messageSource.getMessage("folder_already_exists",
                    new Object[]{pdmFolder.getName(), parentFolder.getName()}, LocaleContextHolder.getLocale()));
        }

        pdmFolder = pdmFolderRepository.save(pdmFolder);
        pdmFolder.setNamePath(parentFolder.getNamePath() + "/" + pdmFolder.getName());
        pdmFolder.setIdPath(parentFolder.getIdPath() + "/" + pdmFolder.getId());
        pdmFolder = pdmFolderRepository.save(pdmFolder);

        return pdmFolder;
    }

    public PDMFolder createFolderByPath(Integer vaultId, String folderPath) {
        PDMFolder pdmFolder = null;
        folderPath = folderPath.replaceAll("\\\\", "/");
        pdmFolder = pdmFolderRepository.findByVaultAndNamePathIgnoreCase(vaultId, folderPath);

        if (pdmFolder == null) {
            String[] paths = folderPath.split("/");
            PDMFolder parentFolder = null;
            for (String path : paths) {
                PDMFolder folder;
                if (parentFolder == null) {
                    folder = pdmFolderRepository.findByVaultAndNameAndParentIsNull(vaultId, path);
                } else {
                    folder = pdmFolderRepository.findByParentAndNameIgnoreCase(parentFolder.getId(), path);
                }
                if (folder == null) {
                    folder = new PDMFolder();
                    folder.setVault(vaultId);
                    folder.setName(path);
                    parentFolder = createRootFolder(vaultId, folder);
                } else {
                    parentFolder = folder;
                }

                pdmFolder = parentFolder;
            }
        }
        return pdmFolder;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PDMFolder> getRootFolders(Integer vaultId) {
        return pdmFolderRepository.findByVaultAndParentIsNull(vaultId);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PDMFolder> getSearchRootFolders(PDMObjectCriteria objectCriteria) {
        Predicate predicate = pdmFolderPredicateBuilder.build(objectCriteria, QPDMFolder.pDMFolder);
        return IterableUtils.toList(pdmFolderRepository.findAll(predicate));
    }

    /*
    * Categorize File Counts Based On Type
    * */
    private PDMFolder getCountByType(PDMFolder folder, List<PDMFileVersion> files) {
        Integer assemblasCount = 0;
        Integer partsCount = 0;
        Integer drawingsCount = 0;
        Integer normalCount = 0;
        List<Integer> fileVersionIds = new ArrayList<>();
        for (PDMFileVersion version : files) {
            fileVersionIds.add(version.getFile().getId());
            if (version.getFile().getFileType().toString().equals("ASSEMBLY")) {
                assemblasCount++;
            }
            if (version.getFile().getFileType().toString().equals("PART")) {
                partsCount++;
            }
            if (version.getFile().getFileType().toString().equals("DRAWING")) {
                drawingsCount++;
            }
            if (version.getFile().getFileType().toString().equals("NORMAL")) {
                normalCount++;
            }
        }
        if (fileVersionIds.size() > 0) {
            List<PDMFileVersion> fileCommits = pdmFileVersionRepository.getFileCommits(fileVersionIds);
            folder.setCommitsCount(fileCommits.size());
        } else {
            folder.setCommitsCount(0);
        }

        folder.setAssemblasCount(assemblasCount);
        folder.setPartsCount(partsCount);
        folder.setDrawingsCount(drawingsCount);
        folder.setCommitsCount(normalCount);
        return folder;
    }

    public Page<PDMVaultObject> getVaultChildrenByPath(Integer vaultId, Pageable pageable, PDMObjectCriteria objectCriteria) {
        List<PDMVaultObject> children = new ArrayList<>();
        if (Criteria.isEmpty(objectCriteria.getPath())) {
            List<PDMFolder> folders = new ArrayList();
            if (Criteria.isEmpty(objectCriteria.getSearchQuery())) {
                folders = getRootFolders(vaultId);
            } else {
                objectCriteria.setValutID(vaultId.toString());
                objectCriteria.setParent(null);
                folders = getSearchRootFolders(objectCriteria);
            }
            children.addAll(folders);
        } else {
            PDMFolder folder = pdmFolderRepository.findByIdPath(objectCriteria.getPath());
            if (folder != null) {
                Integer assemblasCount = 0;
                Integer partsCount = 0;
                Integer drawingsCount = 0;
                Integer normalCount = 0;
                List<PDMFolder> folders = new ArrayList();
                if (objectCriteria.getSearchQuery() != null) {
                    objectCriteria.setParent(folder.getId().toString());
                    folders = getSearchFolderChildren(objectCriteria);
                } else {
                    folders = getFolderChildren(folder.getId());
                }
                children.addAll(folders);
                if (!Criteria.isEmpty(objectCriteria.getSearchQuery())) {
                    List<PDMFileVersion> files = pdmFileVersionRepository.findByFolderAndLatestAndSearchText(folder.getId(), objectCriteria.getSearchQuery());
                    children.addAll(files);
                } else {
                    List<PDMFileVersion> files = pdmFileVersionRepository.findByFolderAndLatest(folder.getId());
                    children.addAll(files);
                }
            }
        }

//        Page<PDMVaultObject> vaultObjects = new PageImpl<PDMVaultObject>(children, pageable, children.size());
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > children.size() ? children.size() : (start + pageable.getPageSize());
        Page<PDMVaultObject> vaultObjects = new PageImpl<PDMVaultObject>(children.subList(start, end), pageable, children.size());
        return vaultObjects;
    }

    public List<PDMFolder> getFolderChildren(Integer folderId) {
        return pdmFolderRepository.findByParent(folderId);
    }

    public List<PDMFolder> getSearchFolderChildren(PDMObjectCriteria objectCriteria) {
        Predicate predicate = pdmFolderPredicateBuilder.build(objectCriteria, QPDMFolder.pDMFolder);
        return IterableUtils.toList(pdmFolderRepository.findAll(predicate));
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PDMFolder getFolder(Integer folderId) {
        return pdmFolderRepository.findOne(folderId);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PDMFolder> getMultipleFolders(Iterable<Integer> folderIds) {
        return pdmFolderRepository.findByIdIn(folderIds);
    }

    @PreAuthorize("hasPermission(#folder.id ,'edit')")
    public PDMFolder updateFolder(PDMFolder folder) {
        return pdmFolderRepository.save(folder);
    }

    @PreAuthorize("hasPermission(#folderId,'delete')")
    public void deleteFolder(Integer folderId) {
        pdmFolderRepository.delete(folderId);
    }

    /* Commit methods */
    @PreAuthorize("hasPermission(#commit,'create')")
    public PDMCommit createCommit(PDMCommit commit) {
        commit.setHash(getHash());
        return pdmCommitRepository.save(commit);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PDMCommit> getCommits(Integer vaultId, Pageable pageable) {
        if (vaultId == null || vaultId == 0) {
            return pdmCommitRepository.findAll(pageable);
        } else {
            Predicate predicate = QPDMCommit.pDMCommit.vault.eq(vaultId);
            return pdmCommitRepository.findAll(predicate, pageable);
        }
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PDMCommit getCommit(Integer commitId) {
        return pdmCommitRepository.findOne(commitId);
    }

    private String getHash() {
        int len = 10;
        String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }


    /* File methods */
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','pdm_file')")
    public List<PDMFileVersion> addFilesToFolder(Integer commitId, Integer folderId, MultipartHttpServletRequest request) {
        List<PDMFileVersion> filesAdded = new ArrayList<>();

        //Map<String, MultipartFile> fileMap = request.getFileMap();
        List<String> fileNames = IteratorUtils.toList(request.getFileNames());
        for (String fileName : fileNames) {
            List<MultipartFile> files = request.getFiles(fileName);
            for (MultipartFile file : files) {
                filesAdded.add(addFileToFolder(commitId, folderId, file));
            }
        }

        return filesAdded;
    }

    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'edit','pdm_file')")
    public PDMFileVersion updateFile(Integer folderId, Integer fileId, Integer commitId, MultipartHttpServletRequest request) {
        PDMFileVersion fileVersion = pdmFileVersionRepository.findLatestByMaster(fileId);

        fileVersion.setLatest(false);
        fileVersion = pdmFileVersionRepository.save(fileVersion);
        entityManager.detach(fileVersion);
        Integer version = fileVersion.getVersion();
        fileVersion.setId(null);
        fileVersion.setVersion(version + 1);

        PDMFolder folder = pdmFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new PDMException(messageSource.getMessage("folder_doesnot_exist", new Object[]{}, LocaleContextHolder.getLocale()));
        }

        Collection<MultipartFile> files = request.getFileMap().values();
        if (files.size() == 0) {
            throw new PDMException("No files uploaded");
        }

        MultipartFile multipartFile = files.iterator().next();

        fileVersion.setNamePath(folder.getNamePath() + "/" + multipartFile.getOriginalFilename());
        fileVersion.setName(multipartFile.getOriginalFilename());
        fileVersion.setSize(multipartFile.getSize());
        fileVersion.setCommit(commitId);
        fileVersion = pdmFileVersionRepository.save(fileVersion);
        fileVersion.setIdPath(folder.getIdPath() + "/" + fileVersion.getId());
        fileVersion = pdmFileVersionRepository.save(fileVersion);

        fileSystemService.saveObjectFileToDisk(folderId, fileVersion.getId(), multipartFile);

        return fileVersion;

    }

    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','pdm_file')")
    public PDMFileVersion addFileToFolder(Integer commitId, Integer folderId, MultipartFile multipartFile) {
        PDMFolder folder = pdmFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new PDMException(messageSource.getMessage("folder_doesnot_exist", new Object[]{}, LocaleContextHolder.getLocale()));
        }

        PDMFileVersion fileVersion = pdmFileVersionRepository.findByFolderAndNameAndLatest(folderId, multipartFile.getOriginalFilename());

        PDMRevisionMaster revisionMaster = null;

        if (fileVersion == null) {
            PDMFile pdmFile = new PDMFile();
            pdmFile.setVault(folder.getVault());
            pdmFile.setFolder(folder.getId());
            pdmFile.setName(multipartFile.getOriginalFilename());
            pdmFile.setFileType(getFileType(multipartFile.getOriginalFilename()));
            pdmFile = pdmFileRepository.save(pdmFile);
            fileVersion = new PDMFileVersion();
            fileVersion.setFile(pdmFile);

            revisionMaster = createRevisionMaster(fileVersion);
        } else {
            Integer attachedToId = fileVersion.getAttachedTo();
            PDMRevisionedObject revisionedObject = pdmRevisionedObjectRepository.findOne(attachedToId);
            revisionMaster = revisionedObject.getMaster();

            fileVersion.setLatest(false);
            fileVersion = pdmFileVersionRepository.save(fileVersion);

            Integer version = fileVersion.getVersion() + 1;

            PDMFile pdmFile = fileVersion.getFile();
            pdmFile.setLatestVersion(version);
            pdmFile.setVersions(version);
            pdmFile = pdmFileRepository.save(pdmFile);

            fileVersion = new PDMFileVersion();
            fileVersion.setFile(pdmFile);
            fileVersion.setVersion(version);
            fileVersion.setLatest(true);
        }

        fileVersion.setVault(folder.getVault());
        fileVersion.setNamePath(folder.getNamePath() + "/" + multipartFile.getOriginalFilename());
        fileVersion.setName(multipartFile.getOriginalFilename());
        fileVersion.setSize(multipartFile.getSize());
        fileVersion.setCommit(commitId);
        fileVersion = pdmFileVersionRepository.save(fileVersion);
        fileVersion.setIdPath(folder.getIdPath() + "/" + fileVersion.getId());
        fileVersion = pdmFileVersionRepository.save(fileVersion);

        fileSystemService.saveObjectFileToDisk(folderId, fileVersion.getId(), multipartFile);

        createNewAttachedToRevision(revisionMaster, fileVersion);

        return fileVersion;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PDMFileVersion> getFolderFiles(Integer folderId) {
        return pdmFileVersionRepository.findByFolderAndLatest(folderId);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PDMFileVersion> getFolderFiles(Integer folderId, Pageable pageable) {

        Predicate predicate = QPDMFileVersion.pDMFileVersion.file.folder.eq(folderId).and(
                QPDMFileVersion.pDMFileVersion.latest.eq(Boolean.TRUE));
        return pdmFileVersionRepository.findAll(predicate, pageable);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PDMFileVersion> getFolderCommitFiles(Integer folderId, Integer commitId) {
        return pdmFileVersionRepository.findByFolderAndCommit(folderId, commitId);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PDMFileVersion> getFileVersions(Integer fileMasterId) {
        List<PDMFileVersion> pdmFileVersions = pdmFileVersionRepository.findFileVersions(fileMasterId);
        pdmFileVersions.forEach(pdmFileVersion -> {
            pdmFileVersion.setAttachedToRevision(pdmRevisionedObjectRepository.findOne(pdmFileVersion.getAttachedTo()).getRevision());
        });
        return pdmFileVersions;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PDMFileVersion> getCommitFiles(Integer commitId) {
        return pdmFileVersionRepository.findByCommit(commitId);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PDMCommit> getCommits(List<Integer> commitIds) {
        return pdmCommitRepository.findByIdIn(commitIds);
    }

    public Boolean addThumbnail(Integer fileId, MultipartHttpServletRequest request) {
        PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findOne(fileId);
        if (pdmFileVersion != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    PDMObjectThumbnail thumbnail = new PDMObjectThumbnail();
                    thumbnail.setId(pdmFileVersion.getId());
                    thumbnail.setThumbnail(file.getBytes());
                    pdmThumbnailRepository.save(thumbnail);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public void getThumbnail(Integer fileVersionId, HttpServletResponse response) {
        PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findOne(fileVersionId);
        PDMObjectThumbnail pdmThumbnail = pdmThumbnailRepository.findOne(fileVersionId);
        if (pdmFileVersion != null && pdmThumbnail != null) {
            InputStream is = new ByteArrayInputStream(pdmThumbnail.getThumbnail());
            try {
                response.setContentType("image/png");
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                throw new CassiniException("Error reading thumbnail.");
            }
        } else {
            try {
                response.sendRedirect("/app/assets/images/cassini-logo-greyscale-text.png");
            } catch (IOException e) {
            }
        }
    }


    public PDMFileType getFileType(String fileName) {
        PDMFileType fileType = PDMFileType.NORMAL;

        if (fileName.toLowerCase().endsWith(".sldasm") ||
                fileName.toLowerCase().endsWith(".catproduct")) {
            fileType = PDMFileType.ASSEMBLY;
        } else if (fileName.toLowerCase().endsWith(".sldprt") ||
                fileName.toLowerCase().endsWith(".catpart")) {
            fileType = PDMFileType.PART;
        } else if (fileName.toLowerCase().endsWith(".slddrw") ||
                fileName.toLowerCase().endsWith(".catdrawing")) {
            fileType = PDMFileType.DRAWING;
        }
        return fileType;
    }


    @Transactional(readOnly = true)
    public PDMFileVersion getFileVersion(Integer fileVersionId) {
        return pdmFileVersionRepository.findOne(fileVersionId);
    }

    public PDMRevisionMaster createRevisionMaster(PDMFileVersion pdmFileVersion) {
        PDMFile pdmFile = pdmFileVersion.getFile();
        PDMRevisionMaster master = new PDMRevisionMaster();
        master.setName(pdmFile.getName());
        master.setDescription(pdmFile.getName());
        master.setLatestRevision(getRevisionFromSequence());
        master = pdmService.createRevisionMaster(master);

        return master;
    }

    public void createNewAttachedToRevision(PDMRevisionMaster attachedToObject, PDMFileVersion fileVersion) {
        PDMFile pdmFile = fileVersion.getFile();
        if (pdmFile.getFileType() == PDMFileType.ASSEMBLY) {
            createRelatedAssemblyVersion(attachedToObject, fileVersion);
        } else if (pdmFile.getFileType() == PDMFileType.PART) {
            createRelatedPartVersion(attachedToObject, fileVersion);
        } else if (pdmFile.getFileType() == PDMFileType.DRAWING) {
            createRelatedDrawingVersion(attachedToObject, fileVersion);
        }
    }


    public PDMAssembly createRelatedAssemblyVersion(PDMRevisionMaster master, PDMFileVersion fileVersion) {
        PDMFile pdmFile = fileVersion.getFile();

        List<PDMRevisionedObject> revObjs = pdmRevisionedObjectRepository.findByMasterId(master.getId());
        PDMRevisionedObject oldRevObj = null;
        String revision = master.getLatestRevision();
        for (PDMRevisionedObject revObj : revObjs) {
            if(revObj.getLatestRevision()) {
                revision = revObj.getRevision();
                oldRevObj = revObj;
            }
            revObj.setLatestRevision(false);
        }

        PDMAssembly pdmAssembly = new PDMAssembly();
        if(master.getItemObject() != null ) {
            PLMItem plmItem = itemRepository.findById(master.getItemObject());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
            pdmAssembly.setItemObject(itemRevision.getId());
        }

        pdmAssembly.setNamePath(fileVersion.getNamePath());
        pdmAssembly.setMaster(master);
        pdmAssembly.setVault(pdmFile.getVault());
        pdmAssembly.setName(pdmFile.getName());

        if(revision == null) {
            revision = getRevisionFromSequence();
        }
        pdmAssembly.setRevision(revision);
        master.setLatestRevision(revision);
        pdmAssembly.setVersion(fileVersion.getVersion());
        pdmAssembly = pdmService.createAssembly(pdmAssembly);

        master = pdmRevisionMasterRepository.save(master);

        PDMAssemblyConfiguration assemblyConfiguration = new PDMAssemblyConfiguration();
        assemblyConfiguration.setAssembly(pdmAssembly.getId());
        assemblyConfiguration.setName("Default");
        assemblyConfiguration.setDescription("Default configuration");
        assemblyConfiguration = pdmService.createAssemblyConfiguration(assemblyConfiguration);

        pdmAssembly.setDefaultConfiguration(assemblyConfiguration.getId());
        pdmAssembly = pdmService.updateAssembly(pdmAssembly);


        if(oldRevObj != null) {
            PDMAssembly oldAssembly = pdmAssemblyRepository.findOne(oldRevObj.getId());
            if(oldAssembly != null && oldAssembly.getDefaultConfiguration() != null) {
                PDMAssemblyConfiguration oldAssemblyConfig = pdmService.getAssemblyConfiguration(oldAssembly.getDefaultConfiguration());
                if(oldAssemblyConfig != null) {
                    List<PDMBOMOccurrence> occs = pdmBOMOccurrenceRepository.findByParent(oldAssemblyConfig.getId());
                    for (PDMBOMOccurrence occ : occs) {
                        if (occ instanceof PDMAssemblyBOMOccurrence) {
                            PDMAssemblyBOMOccurrence assemblyBOMOccurrence = ((PDMAssemblyBOMOccurrence) occ);
                            PDMAssemblyBOMOccurrence bomOccurrence = JsonUtils.cloneEntity(assemblyBOMOccurrence, PDMAssemblyBOMOccurrence.class);
                            bomOccurrence.setId(null);
                            bomOccurrence.setParent(assemblyConfiguration.getId());
                            bomOccurrence = pdmAssemblyBOMOccurrenceRepository.save(bomOccurrence);
                        } else if (occ instanceof PDMPartBOMOccurrence) {
                            PDMPartBOMOccurrence partBOMOccurrence = ((PDMPartBOMOccurrence) occ);
                            PDMPartBOMOccurrence bomOccurrence = JsonUtils.cloneEntity(partBOMOccurrence, PDMPartBOMOccurrence.class);
                            bomOccurrence.setId(null);
                            bomOccurrence.setParent(assemblyConfiguration.getId());
                            bomOccurrence = pdmPartBOMOccurrenceRepository.save(bomOccurrence);
                        }
                    }
                }
            }
        }

        fileVersion.setAttachedTo(pdmAssembly.getId());
        pdmFileVersionRepository.save(fileVersion);

        return pdmAssembly;
    }

    public PDMPart createRelatedPartVersion(PDMRevisionMaster master, PDMFileVersion fileVersion) {
        PDMFile pdmFile = fileVersion.getFile();

        List<PDMRevisionedObject> revObjs = pdmRevisionedObjectRepository.findByMasterId(master.getId());
        String revision = master.getLatestRevision();
        for (PDMRevisionedObject revObj : revObjs) {
            if(revObj.getLatestRevision()) {
                revision = revObj.getRevision();
            }
            revObj.setLatestRevision(false);
        }

        PDMPart pdmPart = new PDMPart();
        if(master.getItemObject() != null ) {
            PLMItem plmItem = itemRepository.findById(master.getItemObject());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
            pdmPart.setItemObject(itemRevision.getId());
        }

        pdmPart.setNamePath(fileVersion.getNamePath());
        pdmPart.setMaster(master);
        pdmPart.setVault(pdmFile.getVault());
        pdmPart.setName(pdmFile.getName());

        if(revision == null) {
            revision = getRevisionFromSequence();
        }
        pdmPart.setRevision(revision);
        master.setLatestRevision(revision);
        pdmPart.setVersion(fileVersion.getVersion());
        pdmPart = pdmService.createPart(pdmPart);

        master = pdmRevisionMasterRepository.save(master);

        PDMPartConfiguration partConf = new PDMPartConfiguration();
        partConf.setPart(pdmPart.getId());
        partConf.setName("Default");
        partConf.setDescription("Default configuration");
        partConf = pdmService.createPartConfiguration(partConf);

        pdmPart.setDefaultConfiguration(partConf.getId());
        pdmPart = pdmService.updatePart(pdmPart);

        fileVersion.setAttachedTo(pdmPart.getId());
        pdmFileVersionRepository.save(fileVersion);

        return pdmPart;
    }

    public PDMDrawing createRelatedDrawingVersion(PDMRevisionMaster master, PDMFileVersion fileVersion) {
        PDMFile pdmFile = fileVersion.getFile();

        List<PDMRevisionedObject> revObjs = pdmRevisionedObjectRepository.findByMasterId(master.getId());
        String revision = master.getLatestRevision();
        for (PDMRevisionedObject revObj : revObjs) {
            if(revObj.getLatestRevision()) {
                revision = revObj.getRevision();
            }
            revObj.setLatestRevision(false);
        }


        PDMDrawing pdmDrawing = new PDMDrawing();
        if(master.getItemObject() != null ) {
            PLMItem plmItem = itemRepository.findById(master.getItemObject());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
            pdmDrawing.setItemObject(itemRevision.getId());
        }

        pdmDrawing.setNamePath(fileVersion.getNamePath());
        pdmDrawing.setMaster(master);
        pdmDrawing.setVault(pdmFile.getVault());
        pdmDrawing.setName(pdmFile.getName());

        if(revision == null) {
            revision = getRevisionFromSequence();
        }
        pdmDrawing.setRevision(revision);
        master.setLatestRevision(revision);

        pdmDrawing.setRevision(revision);
        master.setLatestRevision(revision);
        pdmDrawing.setVersion(fileVersion.getVersion());
        pdmDrawing.setAttachedTo(fileVersion.getAttachedTo());
        pdmDrawing = pdmService.createDrawing(pdmDrawing);
        master = pdmRevisionMasterRepository.save(master);
        pdmDrawing = pdmService.updateDrawing(pdmDrawing);

        fileVersion.setAttachedTo(pdmDrawing.getId());
        pdmFileVersionRepository.save(fileVersion);

        return pdmDrawing;
    }

    public PDMRevisionedObject getAttachedToObject(Integer fileVersionId) {
        PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findOne(fileVersionId);
        if (pdmFileVersion != null) {
            Integer attachedToId = pdmFileVersion.getAttachedTo();
            PDMRevisionedObject revisionedObject = pdmRevisionedObjectRepository.findOne(attachedToId);
            PLMItem plmItem = itemRepository.findByDesignObject(revisionedObject.getMaster().getId());
            revisionedObject.getMaster().setPlmItem(plmItem);
            return revisionedObject;
        }

        return null;
    }

    public List<PDMRevisionedObject> getAttachedToObjects(List<Integer> attachedToIds) {
        List<PDMRevisionedObject> attachedToObjects = pdmRevisionedObjectRepository.findByIdIn(attachedToIds);
        List<Integer> dsnIds = new ArrayList<>();
        for (PDMRevisionedObject attachedToObject : attachedToObjects) {
            dsnIds.add(attachedToObject.getMaster().getId());

        }

        List<PLMItem> plmItems = itemRepository.findByDesignObjectIn(dsnIds);
        Map<Integer, PLMItem> itemLookup = new HashMap<>();
        for (PLMItem plmItem : plmItems) {
            itemLookup.put(plmItem.getDesignObject(), plmItem);
        }

        for (PDMRevisionedObject attachedToObject : attachedToObjects) {
            PLMItem plmItem = itemLookup.get(attachedToObject.getMaster().getId());
            if(plmItem != null) {
                attachedToObject.getMaster().setPlmItem(plmItem);
            }
        }
        return attachedToObjects;
    }

    public PDMAssembly getAttachedToAssembly(Integer fileVersionId) {
        PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findOne(fileVersionId);
        if (pdmFileVersion != null) {
            Integer attachedToId = pdmFileVersion.getAttachedTo();
            PDMAssembly pdmAssembly = (PDMAssembly) pdmRevisionedObjectRepository.findOne(attachedToId);

            PLMItem plmItem = itemRepository.findByDesignObject(pdmAssembly.getMaster().getId());
            pdmAssembly.getMaster().setPlmItem(plmItem);
            return pdmAssembly;
        }

        return null;
    }

    public PDMPart getAttachedToPart(Integer fileVersionId) {
        PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findOne(fileVersionId);
        if (pdmFileVersion != null) {
            Integer attachedToId = pdmFileVersion.getAttachedTo();
            PDMPart pdmPart = (PDMPart) pdmRevisionedObjectRepository.findOne(attachedToId);

            PLMItem plmItem = itemRepository.findByDesignObject(pdmPart.getMaster().getId());
            pdmPart.getMaster().setPlmItem(plmItem);
            return pdmPart;
        }

        return null;
    }

    public PDMDrawing getAttachedToDrawing(Integer fileVersionId) {
        PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findOne(fileVersionId);
        if (pdmFileVersion != null) {
            Integer attachedToId = pdmFileVersion.getAttachedTo();
            PDMDrawing pdmDrawing = (PDMDrawing) pdmRevisionedObjectRepository.findOne(attachedToId);

            PLMItem plmItem = itemRepository.findByDesignObject(pdmDrawing.getMaster().getId());
            pdmDrawing.getMaster().setPlmItem(plmItem);
            return pdmDrawing;
        }

        return null;
    }


    public PDMFileVersion generateVisualization(Integer fileVersionId) {
        String vizId = null;

        PDMFileVersion fileVersion = pdmFileVersionRepository.findOne(fileVersionId);
        if (fileVersion != null) {
            vizId = fileVersion.getVisualizationId();

            if (vizId == null) {
                Integer folder = fileVersion.getFile().getFolder();
                File file = fileSystemService.getObjectFilePath(folder, fileVersionId);

                if (file.exists()) {
                    try {
                        Map<String, String> map = forgeService.uploadForgeFile(fileVersion.getName(), file.getCanonicalPath());

                        if (map != null) {
                            vizId = map.get("urn");
                            fileVersion.setVisualizationId(vizId);
                            fileVersion = pdmFileVersionRepository.save(fileVersion);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return fileVersion;
    }

    public Page<PDMFileVersion> searchFiles(PDMFileVersionCriteria criteria, Pageable pageable) {
        Predicate predicate = fileVersionPredicateBuilder.build(criteria, QPDMFileVersion.pDMFileVersion);
        return pdmFileVersionRepository.findAll(predicate, pageable);
    }

    public void downloadFile(Integer fileVersionId, HttpServletResponse response) {
        PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findOne(fileVersionId);
        PDMFolder pdmFolder = pdmFolderRepository.findOne(pdmFileVersion.getFile().getFolder());
        File file = fileSystemService.getObjectFilePath(pdmFolder.getId(), pdmFileVersion.getId());
        fileDownloadService.writeFileContentToResponse(response, '"' + pdmFileVersion.getName() + '"', file);
    }

    public Page<PDMFileVersion> getFilesByType(PDMFileType fileType, Pageable pageable) {
        QPDMFileVersion qpdmFileVersion = QPDMFileVersion.pDMFileVersion;
        Predicate predicate = qpdmFileVersion.file.fileType.eq(fileType).
                and(qpdmFileVersion.latest.eq(true));
        return pdmFileVersionRepository.findAll(predicate, pageable);

    }

    public PDMFileVersion saveComponentReferences(Integer fileVersionId, List<ComponentReference> references) {
        PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findOne(fileVersionId);
        if (pdmFileVersion != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = mapper.writeValueAsString(references);
                pdmFileVersion.setPartReferences(json);
                pdmFileVersion = pdmFileVersionRepository.save(pdmFileVersion);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return pdmFileVersion;
    }

    public List<ComponentReference> getComponentReferences(Integer fileVersionId) {
        PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findOne(fileVersionId);
        if (pdmFileVersion != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = pdmFileVersion.getPartReferences();
                return mapper.readValue(
                        json, new TypeReference<List<ComponentReference>>() {
                        });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return new ArrayList<>();
    }

    public PDMFileVersion addDrawing(Integer fileVersionId, MultipartFile multipartFile) {
        PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findOne(fileVersionId);
        if (pdmFileVersion != null) {
            PDMFolder pdmFolder = pdmFolderRepository.findOne(pdmFileVersion.getFile().getFolder());
            PDMFileVersion drwFileVersion = addFileToFolder(pdmFileVersion.getCommit(), pdmFolder.getId(), multipartFile);
            PDMDrawing pdmDrawing = pdmDrawingRepository.findOne(drwFileVersion.getAttachedTo());

            Integer attachedToId = pdmFileVersion.getAttachedTo();
            PDMRevisionedObject pdmRevObj = pdmRevisionedObjectRepository.findOne(attachedToId);

            if (pdmRevObj != null) {
                if (pdmRevObj instanceof PDMAssembly) {
                    PDMAssembly pdmAssembly = (PDMAssembly) pdmRevObj;
                    pdmAssembly.setDrawingMaster(pdmDrawing.getMaster());
                    pdmAssembly.setDrawingRevision(pdmDrawing);

                    pdmAssemblyRepository.save(pdmAssembly);
                } else if (pdmRevObj instanceof PDMPart) {
                    PDMPart pdmPart = (PDMPart) pdmRevObj;
                    pdmPart.setDrawingMaster(pdmDrawing.getMaster());
                    pdmPart.setDrawingRevision(pdmDrawing);

                    pdmPartRepository.save(pdmPart);
                }

                return drwFileVersion;
            }
        }

        return null;
    }

    public Attachment addDrawingPDF(Integer drwFileVersionId, MultipartFile multipartFile) {
        PDMFileVersion drwFileVersion = pdmFileVersionRepository.findOne(drwFileVersionId);

        if (drwFileVersion != null && drwFileVersion.getAttachedTo() != null) {
            PDMDrawing pdmDrawing = pdmDrawingRepository.findOne(drwFileVersion.getAttachedTo());
            if (pdmDrawing != null) {
                Attachment attachment = attachmentService.multiplePartFilesPost(pdmDrawing.getId(),
                        ObjectType.valueOf(pdmDrawing.getObjectType().name()), multipartFile);
                pdmDrawing.setPdfFile(attachment.getId());
                pdmDrawingRepository.save(pdmDrawing);

                return attachment;
            }
        }

        return null;
    }

    public String getNextRevisionSequence(String lastRev) {
        String nextRev = null;
        String[] values = new String[0];
        Preference pref = preferenceRepository.findByPreferenceKey("DEFAULT_REVISION_SEQUENCE");
        if (pref != null) {
            String json = pref.getJsonValue();
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = objectMapper.readTree(json);
                    Integer id = jsonNode.get("typeId").asInt();
                    if (id != null) {
                        Lov lov = lovRepository.findOne(id);
                        if (lov != null) {
                            values = lov.getValues();
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        if (values.length == 0) {
            values = PDMConstants.RevisionSequence.stream().toArray(String[]::new);
        }

        int lastIndex = -1;
        for (int i = 0; i < values.length; i++) {
            String rev = values[i];
            if (rev.equalsIgnoreCase(lastRev)) {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex != -1 && lastIndex < values.length) {
            nextRev = values[lastIndex + 1];
        }
        return nextRev;
    }


    private String getRevisionFromSequence() {
        Preference pref = preferenceRepository.findByPreferenceKey("DEFAULT_REVISION_SEQUENCE");
        if (pref != null) {
            String json = pref.getJsonValue();
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = objectMapper.readTree(json);
                    if (jsonNode.get("typeId") != null) {
                        Integer id = jsonNode.get("typeId").asInt();
                        Lov lov = lovRepository.findOne(id);
                        if (lov != null) {
                            return lov.getValues()[0];
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return PDMConstants.RevisionSequence.get(0);
    }
}
