package com.cassinisys.is.service.dm;

import com.cassinisys.is.model.dm.ISDocument;
import com.cassinisys.is.model.dm.ISFolder;
import com.cassinisys.is.repo.dm.FolderRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

/**
 * The class is for FolderService
 */
@Service
public abstract class FolderService {
    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FileSystemService fileSystemService;

    public FolderService() {
    }

    /**
     * The method used to createFolder
     **/
    @Transactional(readOnly = false)
    public void createFolder(Integer parent, ISFolder folder) {
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + getFolderPath(parent, folder);
        File fsFolder = new File(path);
        if (!fsFolder.exists()) {
            try {
                FileUtils.forceMkdir(fsFolder);
            } catch (IOException e) {
                e.printStackTrace();
                throw new CassiniException("Error creating director: " + path + " REASON: " + e.getCause().getMessage());
            }
        }
    }

    /**
     * The method used to getFolderPath of type string
     **/
    @Transactional(readOnly = true)
    public String getFolderPath(Integer root, ISFolder folder) {
        String path = "";
        Integer id = folder.getId();
        path += "" + id;
        Integer parentId = folder.getParent();
        if (parentId != null) {
            ISFolder parent = folderRepository.findOne(parentId);
            if (parent != null) {
                String parentPath = getFolderPath(root, parent);
                path = parentPath + File.separator + path;
            }
        } else {
            path = root + File.separator + path;
        }
        return path;
    }

    /**
     * The method used to getDocumentPath of type string
     **/
    @Transactional(readOnly = true)
    public String getDocumentPath(Integer root, ISDocument document) {
        String path = "";
        Integer id = document.getId();
        path += "" + id;
        Integer parentId = document.getFolder();
        if (parentId != null) {
            ISFolder parent = folderRepository.findOne(parentId);
            if (parent != null) {
                String parentPath = getFolderPath(root, parent);
                path = parentPath + File.separator + path;
            }
        }
        return path;
    }

}
