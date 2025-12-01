package com.cassinisys.is.api.procm;
/**
 * The Class is for ProjectBoqController
 **/

import com.cassinisys.is.filtering.BoqItemCriteria;
import com.cassinisys.is.model.pm.ProjectBoqAttachment;
import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.is.model.procm.ISBoqActualCost;
import com.cassinisys.is.model.procm.ISBoqItem;
import com.cassinisys.is.model.procm.ISProjectBoq;
import com.cassinisys.is.repo.pm.ProjectBoqAttachmentsRepository;
import com.cassinisys.is.service.procm.ProjectBoqService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Export;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.apache.commons.io.IOUtils;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

@Api(name = "Project Boq",
        description = "Project Boq endpoint",
        group = "Project")
@RestController
@RequestMapping("/projects/{projectId}/boq")
public class ProjectBoqController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private ProjectBoqService projectBoqService;

    @Autowired
    private ProjectBoqAttachmentsRepository projectBoqAttachmentsRepository;

    @Autowired
    private FileDownloadService fileDownloadService;

    @Autowired
    private FileSystemService fileSystemService;

    /**
     * The method used for getting the ISProjectBoqCostToProject
     **/
    @RequestMapping(value = "/costToProject/{itemId}", method = RequestMethod.GET)
    public List<ISBoqActualCost> getCostToProject(@PathVariable("projectId") Integer projectId,
                                                  @PathVariable("itemId") Integer itemId) {
        return projectBoqService.getCostToProject(itemId);
    }

    /**
     * The method used for deleting the ISProjectBoqCostToProject
     **/
    @RequestMapping(value = "/costToProject/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        projectBoqService.deleteCostToProject(id);
    }

    /**
     * The method used for creating the ISProjectBoqCostToProject
     **/
    @RequestMapping(value = "/costToProject", method = RequestMethod.POST)
    public List<ISBoqActualCost> createCostToProject(@PathVariable("projectId") Integer projectId,
                                                     @RequestBody List<ISBoqActualCost> isBoqActualCosts) {
        return projectBoqService.createCostToProject(isBoqActualCosts);
    }

    /**
     * The method used for creating the ISProjectBoq
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISProjectBoq create(@PathVariable("projectId") Integer projectId,
                               @RequestBody ISProjectBoq projectBoq) {
        return projectBoqService.create(projectBoq);
    }

    /**
     * The method used for updating the ISProjectBoq
     **/
    @RequestMapping(value = "/{boqId}", method = RequestMethod.PUT)
    public ISProjectBoq update(@PathVariable("projectId") Integer projectId,
                               @PathVariable("boqId") Integer boqId,
                               @RequestBody ISProjectBoq projectBoq) {
        projectBoq.setId(boqId);
        return projectBoqService.update(projectBoq);
    }

    /**
     * The method used for deleting the ISProjectBoq
     **/
    @RequestMapping(value = "/{boqId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("projectId") Integer projectId,
                       @PathVariable("boqId") Integer boqId) {
        projectBoqService.delete(boqId);
    }

    /**
     * The method used to get the value of the ISProjectBoq
     **/
    @RequestMapping(value = "/{boqId}", method = RequestMethod.GET)
    public ISProjectBoq get(@PathVariable("projectId") Integer projectId,
                            @PathVariable("boqId") Integer boqId) {
        return projectBoqService.get(boqId);
    }

    /**
     * The method used to getItems from the list of  ISBoqItem
     **/
    @RequestMapping(value = "/{boqId}/items", method = RequestMethod.GET)
    public List<ISBoqItem> getItems(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("boqId") Integer boqId, PageRequest pageRequest) {
        return projectBoqService.getProjectItems(projectId, boqId);
    }

    /**
     * The method used to addItem of ISBoqItem
     **/
    @RequestMapping(value = "/{boqId}/items", method = RequestMethod.POST)
    public ISBoqItem addItem(@PathVariable("projectId") Integer projectId,
                             @PathVariable("boqId") Integer boqId, @RequestBody ISBoqItem item) {
        item.setBoq(boqId);
        return projectBoqService.addItem(item);
    }

    /**
     * The method used to getItem from the list of ISBoqItem
     **/
    @RequestMapping(value = "/{boqId}/items/{itemNumbers}", method = RequestMethod.GET)
    public List<ISBoqItem> getItem(@PathVariable("boqId") Integer boqId,
                                   @PathVariable("itemNumbers") List<String> itemNumbers) {
        return projectBoqService.getItemByItemNumber(itemNumbers);
    }

    /**
     * The method used to addItems to the list of ISBoqItem
     **/
    @RequestMapping(value = "/{boqId}/items/multiple", method = RequestMethod.POST)
    public List<ISBoqItem> addItems(@PathVariable("projectId") Integer projectId,
                                    @PathVariable("boqId") Integer boqId,
                                    @RequestBody List<ISBoqItem> items) {
        for (ISBoqItem item : items) {
            item.setBoq(boqId);
            item.setIsSavedItem(true);
        }
        return projectBoqService.addItems(items);
    }

    /**
     * The method used to updateItem to ISBoqItem
     **/
    @RequestMapping(value = "/{boqId}/items/{itemId}",
            method = RequestMethod.PUT)
    public ISBoqItem updateItem(@PathVariable("projectId") Integer projectId,
                                @PathVariable("boqId") Integer boqId,
                                @PathVariable("itemId") Integer itemId, @RequestBody ISBoqItem item) {
        item.setId(itemId);
        item.setBoq(boqId);
        return projectBoqService.updateItem(item);
    }

    /**
     * The method used to deleteItem
     **/
    @RequestMapping(value = "/{boqId}/items/{itemId}",
            method = RequestMethod.DELETE)
    public void deleteItem(@PathVariable("projectId") Integer projectId,
                           @PathVariable("boqId") Integer boqId,
                           @PathVariable("itemId") String itemId) {
        String[] arr = itemId.split(",");
        for (String id : arr) {
            projectBoqService.deleteItem(Integer.parseInt(id));
        }
    }

    /**
     * The method used to getMultiple values from the list of ISProjectBoq
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISProjectBoq> getMultiple(@PathVariable Integer[] ids) {
        return projectBoqService.findMultiple(Arrays.asList(ids));
    }

    /**
     * The method used to getMultipleItems from the list of ISBoqItem
     **/
    @RequestMapping(value = "/multiple/items/[{ids}]", method = RequestMethod.GET)
    public List<ISBoqItem> getMultipleItems(@PathVariable Integer[] ids) {
        return projectBoqService.findMultipleItems(Arrays.asList(ids));
    }

    /**
     * The method used to getBoqItems from the list of ISBoqItem
     **/
    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public List<ISBoqItem> getBoqItems(
            @PathVariable("projectId") Integer projectId) {
        return projectBoqService.getAllBoqItems(projectId);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<ISBoqItem> searchBomItems(@PathVariable("projectId") Integer projectId, PageRequest pageRequest,
                                          BoqItemCriteria criteria) {
        criteria.setFreeTextSearch(true);
        criteria.setProject(projectId);
        return projectBoqService.searchBomItems(projectId, criteria);
    }

    /**
     * The method used to getBomItemsByItemNumber from the list of ISBoqItem
     **/
    @RequestMapping(value = "itemNumber/{itemNumber}", method = RequestMethod.GET)
    public List<ISBoqItem> getBomItemsByItemNumber(@PathVariable("itemNumber") String itemNumber) {
        return projectBoqService.getBomItemsByItemNumber(itemNumber);
    }

    /**
     * The method used to getPagedBoqItems from the list of ISBoqItem
     **/
    @RequestMapping(value = "/items/pageable", method = RequestMethod.GET)
    public Page<ISBoqItem> getPagedBoqItems(
            @PathVariable("projectId") Integer projectId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectBoqService.getPagedBoqItems(projectId, pageable);
    }

    /**
     * The method used to getBomItemsByItemType from the list of ISBoqItem
     **/
    @RequestMapping(value = "/resourceType/{resourceType}/pageable", method = RequestMethod.GET)
    public Page<ISBoqItem> getBomItemsByItemType(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("resourceType") ResourceType itemType, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectBoqService.getBomItemsByItemType(projectId, itemType, pageable);
    }

    @RequestMapping(value = "task/{taskId}/resourceType/{resourceType}/pageable", method = RequestMethod.GET)
    public Page<ISBoqItem> getItemsForTask(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("taskId") Integer taskId,
            @PathVariable("resourceType") ResourceType itemType, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectBoqService.getItemsForTask(projectId, taskId, itemType, pageable);
    }

    /**
     * The method used to getBomItemsByItemNumberAndboqName from  ISBoqItem
     **/
    @RequestMapping(value = "/itemNumber/{itemNumber}/{boqName}", method = RequestMethod.GET)
    public ISBoqItem getBomItemsByItemNumberAndboqName(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("itemNumber") String itemNumber,
            @PathVariable("boqName") String boqName) {
        return projectBoqService.getBomItemsByItemNumberAndboqName(projectId, itemNumber, boqName);
    }

    @RequestMapping(value = "/boqItem/{itemNumber}", method = RequestMethod.GET)
    public List<ISBoqItem> getBoqItemByProjectAndItemNumber(@PathVariable("projectId") Integer projectId,
                                                            @PathVariable("itemNumber") String itemNumber) {
        return projectBoqService.getBoqItem(projectId, itemNumber);
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public void importBoq(@PathVariable("projectId") Integer projectId, MultipartHttpServletRequest request) throws Exception {
        projectBoqService.importBoq(projectId, request);
    }

    @RequestMapping(value = "/export", method = {RequestMethod.POST}, produces = {"text/html"})
    public String exportBoq(@PathVariable("projectId") Integer projectId, @RequestParam("fileType") String fileType, @RequestBody Export export, HttpServletResponse response) {
        String fileName = projectBoqService.exportBoq(projectId, fileType, export, response);
        return fileName;
    }

    @RequestMapping(method = {RequestMethod.GET}, value = {"/export/file/{fileId}/download"})
    public void downloadExportFile(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        projectBoqService.downloadExportFile(fileId, response);
    }

    @RequestMapping(value = "task/{taskId}/filters", method = RequestMethod.GET)
    public Page<ISBoqItem> getFilteredItems(@PathVariable("projectId") Integer projectId,
                                            @PathVariable("taskId") Integer taskId,
                                            PageRequest pageRequest, BoqItemCriteria boqItemCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectBoqService.getBoqItemsByFilters(projectId, taskId, pageable, boqItemCriteria);
    }

    @RequestMapping(value = "/pdf/import", method = RequestMethod.POST)
    public void importBoqPdf(@PathVariable("projectId") Integer projectId, MultipartHttpServletRequest request) throws Exception {
        projectBoqService.importBoqPdf(projectId, request);
    }

    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public ProjectBoqAttachment getPdfFile(@PathVariable("projectId") Integer projectId) {
        return projectBoqService.getPdfFile(projectId);
    }

    @RequestMapping(value = "/{fileId}/download", method = RequestMethod.GET)
    public void downloadItemFile(@PathVariable("projectId") Integer projectId,
                                 @PathVariable("fileId") Integer fileId,
                                 HttpServletResponse response) {
        ProjectBoqAttachment projectBoqAttachment = projectBoqAttachmentsRepository.findOne(fileId);
        File file = getItemFile(projectId, fileId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, '"' + projectBoqAttachment.getName() + '"', file);
        }
    }

    public File getItemFile(Integer projectId, Integer fileId) {
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem/" + projectId + "/" + fileId;
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(value = "/files/{fileId}/preview", method = RequestMethod.GET)
    public void previewItemFile(@PathVariable("projectId") Integer projectId,
                                @PathVariable("fileId") Integer fileId,
                                HttpServletResponse response) throws Exception {
        ProjectBoqAttachment projectBoqAttachment = projectBoqAttachmentsRepository.findOne(fileId);
        String fileName = projectBoqAttachment.getName();
        File file = getItemFile(projectId, fileId);
        if (file != null) {
            try {
                String e = URLDecoder.decode(fileName, "UTF-8");
                response.setHeader("Content-disposition", "inline; filename=" + e);
            } catch (UnsupportedEncodingException var6) {
                response.setHeader("Content-disposition", "inline; filename=" + fileName);
            }
            ServletOutputStream e1 = response.getOutputStream();
            IOUtils.copy(new FileInputStream(file), e1);
            e1.flush();
        }
    }

}
