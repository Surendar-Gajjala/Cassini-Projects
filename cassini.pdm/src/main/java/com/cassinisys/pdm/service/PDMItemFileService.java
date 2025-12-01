package com.cassinisys.pdm.service;

import com.cassinisys.pdm.model.PDMItem;
import com.cassinisys.pdm.model.PDMItemFile;
import com.cassinisys.pdm.model.PDMItemFileVersionHistory;
import com.cassinisys.pdm.repo.ItemFileRepository;
import com.cassinisys.pdm.repo.ItemFileVersionHistoryRepository;
import com.cassinisys.pdm.repo.ItemRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Service
@Transactional
public class PDMItemFileService implements CrudService<PDMItemFile, Integer> {

    @Autowired
    private ItemFileRepository itemFileRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private ItemFileVersionHistoryRepository itemFileVersionHistoryRepository;

    @Override
    public PDMItemFile create(PDMItemFile pdmItemFile) {
        checkNotNull(pdmItemFile);
        return itemFileRepository.save(pdmItemFile);
    }

    @Override
    public PDMItemFile update(PDMItemFile pdmItemFile) {
        checkNotNull(pdmItemFile);
        return itemFileRepository.save(pdmItemFile);
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        PDMItemFile pdmItemFile = itemFileRepository.findOne(id);
        if (pdmItemFile == null) {
            throw new ResourceNotFoundException();
        }
        itemFileRepository.delete(id);
    }

    public void deleteItemFile(Integer itemId,Integer id) {
        checkNotNull(id);
        PDMItemFile pdmItemFile = itemFileRepository.findOne(id);
        List<PDMItemFile> itemFiles = itemFileRepository.findByItemAndName(itemId, pdmItemFile.getName());
        for(PDMItemFile itemFile : itemFiles){
            if (itemFile == null) {
                throw new ResourceNotFoundException();
            }
            itemFileRepository.delete(itemFile.getId());
        }


    }
    @Override
    public PDMItemFile get(Integer id) {
        checkNotNull(id);
        return itemFileRepository.findOne(id);
    }

    @Override
    public List<PDMItemFile> getAll() {
        return itemFileRepository.findAll();
    }

    public Page<PDMItemFile> findAll(Pageable pageable) {
        return itemFileRepository.findAll(pageable);
    }

    public List<PDMItemFile> findByItem(Integer item) {
        return itemFileRepository.findByItemAndLatestTrueOrderByModifiedDateDesc(item);
    }

    public List<PDMItemFile> uploadItemFiles(Integer itemId, Map<String, MultipartFile> fileMap) {
        List<PDMItemFile> uploaded = new ArrayList<>();

        Login login = sessionWrapper.getSession().getLogin();

        for (MultipartFile file : fileMap.values()) {
            String name = file.getOriginalFilename();

            PDMItemFile itemFile = itemFileRepository.findByItemAndNameAndLatestTrue(itemId, name);
            Integer version = 1;
            if (itemFile != null) {
                itemFile.setLatest(false);
                Integer oldVersion = itemFile.getVersion();
                version = oldVersion + 1;
                itemFileRepository.save(itemFile);
            }

            itemFile = new PDMItemFile();
            itemFile.setName(file.getOriginalFilename());
            itemFile.setCreatedBy(login.getPerson().getId());
            itemFile.setModifiedBy(login.getPerson().getId());
            itemFile.setItem(itemId);
            itemFile.setVersion(version);
            itemFile.setSize(file.getSize());
            itemFile.setPath(file.getOriginalFilename());
            itemFile = itemFileRepository.save(itemFile);

            PDMItemFileVersionHistory fileVersionHistory = new PDMItemFileVersionHistory();
            PDMItemFileVersionHistory fileVersionHistory1 = itemFileVersionHistoryRepository.findByItemAndFileName(itemId, file.getOriginalFilename());
            fileVersionHistory.setFile(itemFile.getId());
            fileVersionHistory.setItem(itemId);
            fileVersionHistory.setFileName(file.getOriginalFilename());
            if (fileVersionHistory1 != null) {
                fileVersionHistory.setOldVersion(fileVersionHistory1.getNewVersion());
            } else {
                fileVersionHistory.setOldVersion(itemFile.getVersion());
            }
            fileVersionHistory.setNewVersion(itemFile.getVersion());
            fileVersionHistory.setUpdatedBy(login.getPerson().getId());
            itemFileVersionHistoryRepository.save(fileVersionHistory);

            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + itemId;
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }

            String path = dir + File.separator + itemFile.getId();
            saveDocumentToDisk(file, path);

            uploaded.add(itemFile);
        }

       /* item.setHasFiles(Boolean.TRUE);*/
        /*itemRepository.save(item);*/

        return uploaded;
    }

    @Transactional(readOnly = true)
    public File getItemFile(Integer itemId, Integer fileId) {
        checkNotNull(itemId);
        checkNotNull(fileId);
        PDMItem revision = itemRepository.findOne(itemId);
        if (revision == null) {
            throw new ResourceNotFoundException();
        }
        PDMItemFile itemFile = itemFileRepository.findOne(fileId);
        if (itemFile == null) {
            throw new ResourceNotFoundException();
        }

        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + itemId + File.separator + fileId;
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

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

    public List<PDMItemFileVersionHistory> getAllFileVersions(Integer itemId, String name) {
        List<PDMItemFileVersionHistory> fileVersionHistories = itemFileVersionHistoryRepository.findAllByItemAndFileNameOrderByTimeStampDesc(itemId, name);
        return fileVersionHistories;
    }
}
