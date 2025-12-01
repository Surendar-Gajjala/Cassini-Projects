package com.cassinisys.pdm.onshape;

import com.cassinisys.pdm.model.PDMFolder;
import com.cassinisys.pdm.repo.FolderRepository;
import com.cassinisys.pdm.service.onshape.OnshapeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestOnshape extends BaseTest{
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private OnshapeService onshapeService;

    @Test
    public void testCreateFolder() throws Exception {
        Integer folderId = 7;
        PDMFolder pdmFolder = folderRepository.findOne(folderId);
        if(pdmFolder != null) {
            //onshapeService.createOnshapeFolder(pdmFolder);
        }
    }

}

