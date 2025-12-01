package com.cassinisys.documents.service;

import com.cassinisys.documents.model.dm.DMDocument;
import com.cassinisys.documents.model.dm.DMFolder;
import com.cassinisys.documents.repo.DMDocumentRepository;
import com.cassinisys.documents.repo.DMFolderRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by swapna on 11/12/18.
 */
@Service
public class DocumentsService {

    @Autowired
    private DMDocumentRepository documentRepository;

    @Autowired
    private DMFolderRepository folderRepository;

    @Transactional(readOnly = false)
    public DMDocument create(DMDocument document) {
        return documentRepository.save(document);
    }

    @Transactional(readOnly = false)

    public DMDocument update(DMDocument document) {
        return documentRepository.save(document);
    }

    @Transactional(readOnly = false)
    public void delete(Integer id) {
        documentRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public DMDocument get(Integer id) {
        return documentRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<DMDocument> getAll() {
        return documentRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<DMDocument> getDocumentsByFolder(Integer folderId) {
        return documentRepository.findByFolderAndLatestTrueOrderByModifiedDateDesc(folderId);
    }

    @Transactional(readOnly = true)
    public List<DMDocument> getDocumentVersions(Integer folderId,
                                                   Integer documentId) {
        checkNotNull(folderId);
        checkNotNull(documentId);
        DMFolder folder = folderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        DMDocument document = documentRepository.findOne(documentId);
        if (document == null) {
            throw new ResourceNotFoundException();
        }
        return documentRepository.findByFolderAndNameOrderByVersionDesc(
                folderId, document.getName());
    }
}
