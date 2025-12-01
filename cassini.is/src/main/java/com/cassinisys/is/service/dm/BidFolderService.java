package com.cassinisys.is.service.dm;
/**
 * The Class is for BidFolderService
 **/

import com.cassinisys.is.model.dm.ISBidDocument;
import com.cassinisys.is.model.dm.ISBidFolder;
import com.cassinisys.is.model.pm.ISBid;
import com.cassinisys.is.repo.dm.BidDocumentRepository;
import com.cassinisys.is.repo.dm.BidFolderRepository;
import com.cassinisys.is.repo.pm.BidRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class BidFolderService extends FolderService implements
        CrudService<ISBidFolder, Integer>,
        PageableService<ISBidFolder, Integer> {

    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private BidFolderRepository bidFolderRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private BidDocumentRepository documentRepository;
    @Autowired
    private FileSystemService fileSystemService;

    /**
     * The method used to create ISBidFolder
     **/
    @Override
    @Transactional(readOnly = false)
    public ISBidFolder create(ISBidFolder folder) {
        checkNotNull(folder);
        folder.setId(null);
        ISBid bid = bidRepository.findOne(folder.getBid());
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        folder = bidFolderRepository.save(folder);
        createFolder(bid.getId(), folder);
        return folder;
    }

    /**
     * The method used to update ISBidFolder
     **/
    @Override
    @Transactional(readOnly = false)
    public ISBidFolder update(ISBidFolder folder) {
        checkNotNull(folder);
        checkNotNull(folder.getId());
        ISBid bid = bidRepository.findOne(folder.getBid());
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        folder = bidFolderRepository.save(folder);
        return folder;
    }

    /**
     * The method used to delete ISBidFolder
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISBidFolder folder = bidFolderRepository.findOne(id);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        ISBid bid = bidRepository.findOne(folder.getBid());
        bidFolderRepository.delete(folder);
    }

    /**
     * The method used to get the value for ISBidFolder
     **/
    @Transactional(readOnly = true)
    @Override
    public ISBidFolder get(Integer id) {
        checkNotNull(id);
        ISBidFolder folder = bidFolderRepository.findOne(id);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        return folder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ISBidFolder> getAll() {
        return bidFolderRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ISBidFolder> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("name")));
        }
        return bidFolderRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ISBidFolder> getChildren(Integer bidFolderId, Pageable pageable) {
        checkNotNull(bidFolderId);
        checkNotNull(pageable);
        ISBidFolder folder = bidFolderRepository.findOne(bidFolderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("name")));
        }
        return bidFolderRepository.findByParent(bidFolderId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ISBidDocument> getDocuments(Integer bidFolderId,
                                            Pageable pageable) {
        checkNotNull(bidFolderId);
        checkNotNull(pageable);
        ISBidFolder folder = bidFolderRepository.findOne(bidFolderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("name")));
        }
        return documentRepository.findByFolderAndLatestTrue(bidFolderId,
                pageable);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<ISBidDocument> uploadDocuments(Integer folderId,
                                               Map<String, MultipartFile> fileMap) {
        checkNotNull(folderId);
        checkNotNull(fileMap);
        ISBidFolder folder = bidFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        List<ISBidDocument> uploaded = new ArrayList<ISBidDocument>();
        Login login = sessionWrapper.getSession().getLogin();
        for (MultipartFile file : fileMap.values()) {
            ISBidDocument document = documentRepository
                    .findByFolderAndNameAndLatestTrue(folderId,
                            file.getOriginalFilename());
            Integer version = 1;
            if (document != null) {
                document.setLatest(false);
                version = document.getVersion() + 1;
                documentRepository.save(document);

            }
            document = new ISBidDocument();
            document.setName(file.getOriginalFilename());
            document.setCreatedBy(login.getPerson().getId());
            document.setModifiedBy(login.getPerson().getId());
            document.setFolder(folder.getId());
            document.setVersion(version);
            document.setSize(file.getSize());
            document.setBid(folder.getBid());
            document = documentRepository.save(document);
            ISBid bid = bidRepository.findOne(folder.getBid());
            String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getDocumentPath(bid.getId(), document);
            fileSystemService.saveDocumentToDisk(file, path);
            uploaded.add(document);
        }
        return uploaded;
    }

    @Transactional(readOnly = true)
    public ISBidDocument getDocument(Integer folderId, Integer documentId) {
        checkNotNull(folderId);
        checkNotNull(documentId);
        ISBidFolder folder = bidFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        ISBidDocument document = documentRepository
                .findByFolderAndIdAndLatestTrue(folderId, documentId);
        if (document == null) {
            throw new ResourceNotFoundException();
        }
        return document;
    }

    @Transactional(readOnly = true)
    public List<ISBidDocument> findMultipleDocuments(List<Integer> ids) {
        return documentRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<ISBidFolder> findMultipleFolders(List<Integer> ids) {
        return bidFolderRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<ISBidDocument> getDocumentVersions(Integer folderId,
                                                   Integer documentId) {
        checkNotNull(folderId);
        checkNotNull(documentId);
        ISBidFolder folder = bidFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        ISBidDocument document = documentRepository.findOne(documentId);
        if (document == null) {
            throw new ResourceNotFoundException();
        }
        return documentRepository.findByFolderAndNameOrderByVersionDesc(
                folderId, document.getName());
    }
}
