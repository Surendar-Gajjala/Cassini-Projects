package com.cassinisys.is.service.dm;

import com.cassinisys.is.model.dm.ISTopDocument;
import com.cassinisys.is.model.dm.ISTopFolder;
import com.cassinisys.is.repo.dm.TopDocumentRepository;
import com.cassinisys.is.repo.dm.TopFolderRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 018 18-Nov -17.
 */
@Service
public class TopDocumentService implements CrudService<ISTopDocument, Integer> {

    @Autowired
    private TopDocumentRepository topDocumentRepository;

    @Autowired
    private TopFolderRepository topFolderRepository;

    @Autowired
    private FileSystemService fileSystemService;

    @Override
    @Transactional(readOnly = false)
    public ISTopDocument create(ISTopDocument topDocument) {
        return topDocumentRepository.save(topDocument);
    }

    @Override
    @Transactional(readOnly = false)
    public ISTopDocument update(ISTopDocument topDocument) {
        return topDocumentRepository.save(topDocument);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        topDocumentRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ISTopDocument get(Integer id) {
        return topDocumentRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ISTopDocument> getAll() {
        return topDocumentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ISTopDocument> getDocumentsByFolder(Integer folderId) {
        return topDocumentRepository.findByFolderAndLatestTrueOrderByModifiedDateDesc(folderId);
    }

    @Transactional(readOnly = true)
    public List<ISTopDocument> getDocumentVersions(Integer folderId,
                                                   Integer documentId) {
        checkNotNull(folderId);
        checkNotNull(documentId);
        ISTopFolder folder = topFolderRepository.findOne(folderId);
        if (folder == null) {
            throw new ResourceNotFoundException();
        }
        ISTopDocument document = topDocumentRepository.findOne(documentId);
        if (document == null) {
            throw new ResourceNotFoundException();
        }
        return topDocumentRepository.findByFolderAndNameOrderByVersionDesc(
                folderId, document.getName());
    }
}
