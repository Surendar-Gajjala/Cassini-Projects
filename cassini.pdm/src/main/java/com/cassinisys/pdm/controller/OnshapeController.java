package com.cassinisys.pdm.controller;

import com.cassinisys.pdm.model.PDMFile;
import com.cassinisys.pdm.model.onshape.FileProperty;
import com.cassinisys.pdm.model.onshape.IdMap;
import com.cassinisys.pdm.model.onshape.OnshapeIds;
import com.cassinisys.pdm.repo.PDMFileRepository;
import com.cassinisys.pdm.service.onshape.OnshapeService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.ObjectAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("pdm/onshape")
public class OnshapeController extends BaseController {
    @Autowired
    private OnshapeService onshapeService;

    @Autowired
    private PDMFileRepository fileRepository;

    @RequestMapping(value = "/ids/[{ids}]", method = RequestMethod.GET)
    public List<IdMap> getOnshapeIds(@PathVariable Integer[] ids) {
        return onshapeService.getFileToOnshapeIdMap(ids);
    }

    @RequestMapping(value = "/translate/{id}", method = RequestMethod.POST)
    public OnshapeIds translateFile(@PathVariable Integer id) {
        PDMFile file = fileRepository.findOne(id);
        if(file == null) {
            throw new ResourceNotFoundException("File does not exist");
        }

        return onshapeService.translateFile(file);
    }

    @RequestMapping(value = "/references/{id}", method = RequestMethod.POST)
    public PDMFile savePartReferences(@PathVariable Integer id, MultipartHttpServletRequest request) {
        PDMFile pdmFile = fileRepository.findOne(id);

        if(pdmFile == null) {
            throw new ResourceNotFoundException("This file does not exist");
        }

        Map<String, MultipartFile> fileMap = request.getFileMap();
        if(fileMap.size() == 1) {
            MultipartFile file = fileMap.values().iterator().next();
            onshapeService.savePartReferences(pdmFile, file);
        }

        return pdmFile;
    }

    @RequestMapping(value = "/properties/{id}", method = RequestMethod.POST)
    public List<ObjectAttribute> saveFileProperties(@PathVariable Integer id, @RequestBody List<FileProperty> properties) {
        return onshapeService.saveFileProperties(id, properties);
    }
}
