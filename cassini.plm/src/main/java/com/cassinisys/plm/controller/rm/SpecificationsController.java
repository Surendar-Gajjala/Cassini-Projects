package com.cassinisys.plm.controller.rm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.RequirementSearchCriteria;
import com.cassinisys.plm.filtering.SpecificationBuilderCriteria;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMSubscribe;
import com.cassinisys.plm.model.rm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.service.integration.ReqIFService;
import com.cassinisys.plm.service.rm.RequirementsService;
import com.cassinisys.plm.service.rm.SpecificationsService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rm/specifications")
@Api(tags = "PLM.RM", description = "Requirement Related")
public class SpecificationsController extends BaseController {

    @Autowired
    private SpecificationsService specificationsService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private FileDownloadService fileDownloadService;

    @Autowired
    private ReqIFService reqIFService;

    @Autowired
    private RequirementsService requirementsService;

    @Autowired
    private PLMWorkflowService workflowService;

    @RequestMapping(method = RequestMethod.POST)
    public Specification createSpecification(@RequestBody Specification specification) {
        return specificationsService.createSpecification(specification);
    }

    @RequestMapping(value = "/update/{specId}", method = RequestMethod.PUT)
    public Specification updateSpecification(@PathVariable("specId") Integer specId, @RequestBody Specification specification) {
        return specificationsService.updateSpecification(specification);
    }

    @RequestMapping(value = "/{specId}/files/{fileId}/renameFile", method = RequestMethod.PUT)
    public RmObjectFile renameFile(@PathVariable("specId") Integer id,
                                   @PathVariable("fileId") Integer fileId, @RequestBody String newFileName) throws IOException {
        return specificationsService.updateFileName(fileId, newFileName);
    }

    @RequestMapping(value = "/files/{fileId}", method = RequestMethod.PUT)
    public PLMFile updateFile(@PathVariable("fileId") Integer fileId, @RequestBody RmObjectFile rmObjectFile) {
        return specificationsService.updateFileDescription(fileId, rmObjectFile);
    }

    @RequestMapping(value = "/{id}/attributes/multiple", method = RequestMethod.POST)
    public void saveSpecAttributes(@PathVariable("id") Integer id,
                                   @RequestBody List<RmObjectAttribute> attributes) {
        specificationsService.saveSpecAttributes(attributes);
    }

    @RequestMapping(value = "/updateImageValue/{objectId}/{attributeId}", method = RequestMethod.POST)
    public RmObjectAttribute saveImageAttributeValue(@PathVariable("objectId") Integer objectId,
                                                     @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return specificationsService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Specification> findAllSpecifications(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return specificationsService.findAllSpecifications(pageable);
    }

    @RequestMapping(value = "/{specId}", method = RequestMethod.GET)
    public Specification findById(@PathVariable("specId") Integer specId) {
        return specificationsService.findById(specId);
    }

    /*--------------Specification files Methods ---------------------*/
    @RequestMapping(value = "/{objectId}/files", method = RequestMethod.POST)
    public List<RmObjectFile> uploadRmObjectFiles(@PathVariable("objectId") Integer objectId,
                                                  MultipartHttpServletRequest request) throws CassiniException {
        return specificationsService.uploadRmObjectFiles(objectId, request.getFileMap());

    }

    @RequestMapping(value = "/{specId}/files", method = RequestMethod.GET)
    public List<RmObjectFile> getRmObjectFiles(@PathVariable("specId") Integer specId) {
        RmObject item1 = specificationsService.getRevision(specId);
        return specificationsService.findByItem(item1);
    }

    @RequestMapping(value = "/{specId}/byName/{name}", method = RequestMethod.GET)
    public List<RmObjectFile> getRmObjectFilesByName(@PathVariable("specId") Integer specId,
                                                     @PathVariable("name") String name) {
        return specificationsService.findBySpecandFileName(specId, name);
    }

    @RequestMapping(value = "{specId}/files/replaceFile/{fileId}", method = RequestMethod.POST)
    public List<RmObjectFile> replaceProjectFiles(@PathVariable("specId") Integer specId,
                                                  @PathVariable("fileId") Integer fileId,
                                                  MultipartHttpServletRequest request) {
        return specificationsService.replaceRmObjectFiles(specId, fileId, request.getFileMap());
    }

    @RequestMapping(value = "/files/{fileId}", method = RequestMethod.GET)
    public RmObjectFile getRmObjectFile(@PathVariable("fileId") Integer fileId) {
        return specificationsService.getRmObjectFile(fileId);
    }

    @RequestMapping(value = "/{fileId}/versions", method = RequestMethod.GET)
    public List<RmObjectFile> getAllFileVersions(@PathVariable("itemId") Integer itemId,
                                                 @PathVariable("fileId") Integer fileId) {
        RmObjectFile file = specificationsService.getRmObjectFile(fileId);
        return specificationsService.getAllFileVersions(itemId, file.getName());
    }

    @RequestMapping(value = "/{fileId}/downloadHistory", method = RequestMethod.GET)
    public List<PLMFileDownloadHistory> getDownloadHistory(@PathVariable("itemId") Integer itemId,
                                                           @PathVariable("fileId") Integer fileId) {
        return specificationsService.getDownloadHistory(fileId);
    }

    @RequestMapping(value = "/{specId}/files/{fileId}/download", method = RequestMethod.GET)
    public void downloadRmObjectFile(@PathVariable("specId") Integer specId,
                                     @PathVariable("fileId") Integer fileId,
                                     HttpServletResponse response) {
        RmObjectFile itemFile = specificationsService.getRmObjectFile(fileId);
        File file = specificationsService.getRmObjectFile(specId, fileId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, itemFile.getName(), file);
        }
    }

    @RequestMapping(value = "/{specId}/files/{fileId}/preview", method = RequestMethod.GET)
    public void previewRmObjectFile(@PathVariable("specId") Integer specId,
                                    @PathVariable("fileId") Integer fileId,
                                    HttpServletResponse response) throws IOException {
        RmObjectFile itemFile = specificationsService.getRmObjectFile(fileId);
        String fileName = itemFile.getName();
        File file = specificationsService.getRmObjectFile(specId, fileId);
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

    @RequestMapping(value = "{specId}/files/zip", method = RequestMethod.GET, produces = "application/zip")
    public void generateZip(@PathVariable("specId") Integer specId, HttpServletResponse response) throws FileNotFoundException, IOException {
        specificationsService.generateZipFile(specId, response);
    }

    @RequestMapping(value = "/{specId}/files/{fileId}", method = RequestMethod.PUT)
    public PLMFile updateRmObjectFile(@PathVariable("fileId") Integer fileId,
                                      @RequestBody RmObjectFile file) {
        return specificationsService.updateFile(fileId, file);
    }

    @RequestMapping(value = "/{specId}/files/{fileId}", method = RequestMethod.DELETE)
    public void deleteRmObjectFile(@PathVariable("specId") Integer specId, @PathVariable("fileId") Integer fileId) {
        specificationsService.deleteRmObjectFile(specId, fileId);
    }

    @RequestMapping(value = "/{specId}/files/{fileId}/fileDownloadHistory", method = RequestMethod.POST)
    public PLMFileDownloadHistory fileDownloadHistory(@PathVariable("specId") Integer specId, @PathVariable("fileId") Integer fileId) {
        return specificationsService.fileDownloadHistory(specId, fileId);
    }

    @RequestMapping(value = "/{specId}/files/{fileId}/versionComments/{objectType}", method = RequestMethod.GET)
    private List<RmObjectFile> getAllFileVersionComments(@PathVariable("specId") Integer specId, @PathVariable("fileId") Integer fileId,
                                                         @PathVariable("objectType") ObjectType objectType) {
        return specificationsService.getAllFileVersionComments(specId, fileId, objectType);
    }

    /*------------ Specification Attributes Methods -----------------*/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<SpecificationType> getMultiple(@PathVariable Integer[] ids) {
        return specificationsService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/revision/{revisionId}", method = RequestMethod.GET)
    public RmObject getRevision(@PathVariable("revisionId") Integer revisionId) {
        return specificationsService.getRevision(revisionId);
    }

    @RequestMapping(value = "/spec/{specTypeId}/specAttributes", method = RequestMethod.GET)
    public List<RmObjectTypeAttribute> getAttributes(@PathVariable("specTypeId") Integer specTypeId,
                                                     @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return specificationsService.getAttributes(specTypeId, hierarchy);
    }

    @RequestMapping(value = "/{specId}/attributes", method = RequestMethod.GET)
    public List<RmObjectAttribute> getItemAttributes(@PathVariable("specId") Integer specId) {
        return specificationsService.getItemAttributes(specId);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public RmObjectAttribute createItemAttribute(@PathVariable("id") Integer id,
                                                 @RequestBody RmObjectAttribute attribute) {
        return specificationsService.createSpecAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public RmObjectAttribute updateItemAttribute(@PathVariable("id") Integer id,
                                                 @RequestBody RmObjectAttribute attribute) {
        return specificationsService.updateSpecAttribute(attribute);
    }

    /*-----------Spec Section ------------*/

    @RequestMapping(value = "/specSection", method = RequestMethod.POST)
    public SpecSection createSpecSection(@RequestBody SpecSection specSection) {
        return specificationsService.createSpecSection(specSection);
    }

    @RequestMapping(value = "/specRequirement", method = RequestMethod.POST)
    public SpecRequirement createSpecRequirement(@RequestBody SpecRequirement specRequirement) {
        return specificationsService.createSpecRequirement(specRequirement);
    }

    @RequestMapping(value = "/updateRequirement/{reqId}", method = RequestMethod.PUT)
    public Requirement updateRequirement(@PathVariable("reqId") Integer reqId, @RequestBody Requirement requirement) {
        return specificationsService.updateRequirement(requirement);
    }

    @RequestMapping(value = "/finishRequirement/{reqId}", method = RequestMethod.PUT)
    public Requirement finishRequirement(@PathVariable("reqId") Integer reqId, @RequestBody Requirement requirement) {
        return specificationsService.finishRequirement(requirement);
    }

    @RequestMapping(value = "/allSpecSections/{specId}", method = RequestMethod.GET)
    public List<SpecSection> getAllSpecSections(@PathVariable("specId") Integer specId) {
        return specificationsService.getAllSpecSections(specId);
    }

    @RequestMapping(value = "/allSpecSections/{specId}/children/{sectionId}", method = RequestMethod.GET)
    public List<SpecElement> getSectionChildren(@PathVariable("specId") Integer specId, @PathVariable("sectionId") Integer sectionId) {
        return specificationsService.getSectionChildren(specId, sectionId);
    }

    @RequestMapping(value = "/{specId}/requirements/counts", method = RequestMethod.GET)
    public Map<String, Integer> getSpecRequirementsCounts(@PathVariable("specId") Integer specId) {
        return specificationsService.getSpecRequirementsCounts(specId);
    }

    @RequestMapping(value = "/requirementSearch", method = RequestMethod.GET)
    public Page<SpecRequirement> getRequirementSearchResults(RequirementSearchCriteria requirementSearchCriteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return specificationsService.getRequirementSearchResults(requirementSearchCriteria, pageable);
    }

    @RequestMapping(value = "/AttributeSearch", method = RequestMethod.POST)
    public Page<SpecRequirement> getRequirementAttributeSearch(@RequestBody RequirementSearchCriteria requirementSearchCriteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return specificationsService.getRequirementSearchResults(requirementSearchCriteria, pageable);
    }


    /*------------ Specification and Requirement Subscriptions -------------*/

    @RequestMapping(value = "/subscribe/{itemId}", method = RequestMethod.POST)
    public PLMSubscribe subscribeSpecificationAndRequirement(@PathVariable("itemId") Integer itemId) {
        return specificationsService.subscribeSpecificationAndRequirement(itemId);
    }

    @RequestMapping(value = "/subscribe/{itemId}/{personId}", method = RequestMethod.GET)
    public PLMSubscribe getSubscribeSpecificationAndRequirementByPerson(@PathVariable("itemId") Integer itemId, @PathVariable("personId") Integer personId) {
        return specificationsService.getSubscribeSpecificationAndRequirementByPerson(itemId, personId);
    }

    @RequestMapping(value = "/subscribe/person/{personId}/all", method = RequestMethod.GET)
    public Page<PLMSubscribe> getAllProjectTemplates(@PathVariable("personId") Integer personId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return specificationsService.findBySubscribesAssignedTo(personId, pageable);
    }


    @RequestMapping(value = "/pageable/{personId}/subscribes", method = RequestMethod.GET)
    public Page<PLMSubscribe> getSubscribersByPerson(@PathVariable("personId") Integer personId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return specificationsService.getAllSubscribersByPerson(personId, pageable);
    }


     /*------------ Specification and Requirement and Section Deletion -------------*/

    @RequestMapping(value = "/delete/{specId}", method = RequestMethod.DELETE)
    public void deleteSpecification(@PathVariable("specId") Integer specId) {
        specificationsService.deleteSpecification(specId);
    }

    @RequestMapping(value = "/specElement/{id}", method = RequestMethod.DELETE)
    public void deleteSpecElement(@PathVariable("id") Integer id) {
        specificationsService.deleteSpecElement(id);
    }

    @RequestMapping(value = "/parent/section/{id}", method = RequestMethod.GET)
    public SpecSection getParentObject(@PathVariable("id") Integer id) {
        return specificationsService.getParentSection(id);
    }



    /*-------- Requirement Promote Demote ------------*/

    @RequestMapping(value = "/{specId}/promote", method = RequestMethod.PUT)
    public Specification promoteSpecification(@PathVariable("specId") Integer specId) {
        return specificationsService.promoteSpecification(specId);
    }

    @RequestMapping(value = "/{specId}/demote", method = RequestMethod.PUT)
    public Specification demoteSpecification(@PathVariable("specId") Integer specId) {
        return specificationsService.demoteSpecification(specId);
    }

    @RequestMapping(value = "/{specId}/revisionHistory", method = RequestMethod.GET)
    public List<SpecificationRevisionHistoryDto> getSpecificationRevisionHistories(@PathVariable("specId") Integer specId) {
        return specificationsService.getSpecificationRevisionHistories(specId);
    }

    @RequestMapping(value = "/{specId}/revise", method = RequestMethod.PUT)
    public Specification reviseRequirement(@PathVariable("specId") Integer reqId) {
        return specificationsService.reviseSpecification(reqId);
    }

    @RequestMapping(value = "/specSearch", method = RequestMethod.GET)
    public Page<Specification> SpecificationFreeTextSearch(PageRequest pageRequest, SpecificationBuilderCriteria specificationBuilderCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<Specification> specifications = specificationsService.specificationFreeTextSearch(pageable, specificationBuilderCriteria);
        return specifications;
    }

    @RequestMapping(value = "/{reqId}/acceptFinalData", method = RequestMethod.GET)
    public SpecRequirement acceptFinalData(@PathVariable("reqId") Integer reqId) {
        return specificationsService.acceptFinalData(reqId);
    }

    @RequestMapping(value = "/specSection/update/{sectionId}", method = RequestMethod.PUT)
    public SpecSection updateSpecSection(@PathVariable("sectionId") Integer sectionId, @RequestBody SpecSection specSection) {
        return specificationsService.updateSpecSection(specSection);
    }

    @RequestMapping(value = "/update/specRequirement/{specReqId}/{parent}", method = RequestMethod.PUT)
    public SpecRequirement updateSpecRequirement(@PathVariable("specReqId") Integer specReqId, @PathVariable("parent") Integer parent, @RequestBody SpecRequirement specRequirement) {
        return specificationsService.updateSpecRequirement(specRequirement, parent);
    }

    @RequestMapping(value = "/reOrder/specRequirement/{targetId}/{parent}", method = RequestMethod.PUT)
    public SpecRequirement reOrderSpecRequirement(@PathVariable("targetId") Integer targetId, @PathVariable("parent") Integer parent, @RequestBody SpecRequirement specRequirement) {
        return specificationsService.reOrderSpecRequirement(specRequirement, targetId, parent);
    }

    @RequestMapping(value = "/{specId}/permission/add", method = RequestMethod.POST)
    public List<Person> createSpecPersons(@PathVariable("specId") Integer specId,
                                          @RequestBody List<Person> persons) {
        return specificationsService.createSpecPersons(specId, persons);
    }

    @RequestMapping(value = "/{specId}/specPersons", method = RequestMethod.GET)
    public List<SpecPermission> getAllSpecPersons(@PathVariable("specId") Integer specId) {
        return specificationsService.getAllSpecPersons(specId);
    }

    @RequestMapping(value = "/{specId}/persons", method = RequestMethod.GET)
    public List<Login> getAllPersons(@PathVariable("specId") Integer specId) {
        return specificationsService.getAllPersons(specId);
    }

    @RequestMapping(value = "/specPermission", method = RequestMethod.POST)
    public SpecPermission createSpecPermission(@RequestBody SpecPermission specPermission) {
        return specificationsService.createSpecPermission(specPermission);
    }

    @RequestMapping(value = "/delete/specPerson/{specId}", method = RequestMethod.DELETE)
    public void deleteSpecPerson(@PathVariable("specId") Integer specId) {
        specificationsService.deleteSpecPerson(specId);
    }

    @RequestMapping(value = "/{specId}/import")
    public Specification importSpecification(@PathVariable("specId") Integer specId,
                                             @RequestParam("format") String format,
                                             MultipartHttpServletRequest request) {
        Specification specification = null;
        if (format.equalsIgnoreCase("excel")) {
            specification = specificationsService.importSpecificationFromExcel(specId, request);
        } else if (format.equalsIgnoreCase("reqif")) {
            specification = reqIFService.convertToCassini(specId, request);
        }
        return specification;
    }

    @RequestMapping(value = "/{specId}/export")
    public void importSpecification(@PathVariable("specId") Integer specId,
                                    @RequestParam("format") String format,
                                    HttpServletResponse response) {
        if (format.equalsIgnoreCase("excel")) {
            specificationsService.exportSpecificationToExcel(specId, response);
        } else if (format.equalsIgnoreCase("reqif")) {
            reqIFService.convertToReqIF(specId, response);
        }
    }

    @RequestMapping(value = "/specType/{typeId}", method = RequestMethod.GET)
    public Page<Specification> getBySpecType(@PathVariable("typeId") Integer id,
                                             PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return specificationsService.getType(id, pageable);
    }

    @RequestMapping(value = "/requirementType/{typeId}", method = RequestMethod.GET)
    public Page<Requirement> getByReqType(@PathVariable("typeId") Integer id,
                                          PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return requirementsService.getType(id, pageable);
    }

    @RequestMapping(value = "/reqVersions/{specId}", method = RequestMethod.GET)
    public List<Requirement> getReqVersionsBySpecification(@PathVariable("specId") Integer id) {
        return specificationsService.getRequirementVersionsBySpecification(id);
    }

    @RequestMapping(value = "/requirement/versions/{objectNumber}/{specId}", method = RequestMethod.GET)
    public List<Requirement> compareReqVersionAttributes(@PathVariable("objectNumber") String objectNumber, @PathVariable("specId") Integer specId) {
        return specificationsService.getRequirementCurrentAndPreviousVersions(objectNumber, specId);
    }

    @RequestMapping(value = "/version/attributes/[{reqIds}]", method = RequestMethod.GET)
    public List<List<ObjectAttribute>> getVersionAttributes(@PathVariable("reqIds") Integer[] reqIds) {
        return specificationsService.getVersionAttributes(Arrays.asList(reqIds));
    }

    @RequestMapping(value = "/{specId}/folder", method = RequestMethod.POST)
    public RmObjectFile createSpecFolder(@PathVariable("specId") Integer specId, @RequestBody RmObjectFile rmObjectFile) {
        return specificationsService.createSpecFolder(specId, rmObjectFile);
    }

    @RequestMapping(value = "/{specId}/folder/{folderId}/upload", method = RequestMethod.POST)
    public List<RmObjectFile> uploadSpecFolderFiles(@PathVariable("specId") Integer specId, @PathVariable("folderId") Integer folderId,
                                                    MultipartHttpServletRequest request) {
        return specificationsService.uploadSpecFolderFiles(specId, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/{folderId}/move", method = RequestMethod.PUT)
    public PLMFile moveSpecFileToFolder(@PathVariable("folderId") Integer folderId,
                                        @RequestBody RmObjectFile file) throws Exception {
        return specificationsService.moveSpecFileToFolder(folderId, file);
    }

    @RequestMapping(value = "/{folderId}/children", method = RequestMethod.GET)
    public List<RmObjectFile> getSpecFolderChildren(@PathVariable("folderId") Integer folderId) {
        return specificationsService.getSpecFolderChidren(folderId);
    }

    @RequestMapping(value = "/{specId}/folder/{fileId}/delete", method = RequestMethod.DELETE)
    public void deleteFolder(@PathVariable("specId") Integer specId, @PathVariable("fileId") Integer fileId) {
        specificationsService.deleteFolder(specId, fileId);
    }

    @RequestMapping(value = "/{specId}/details", method = RequestMethod.GET)
    public DetailsCount getSpecDetails(@PathVariable("specId") Integer specId) {
        return specificationsService.getSpecDetails(specId);
    }

    @RequestMapping(value = "/{specId}/files/{fileId}/latest/uploaded", method = RequestMethod.GET)
    public RmObjectFile getLatestUploadedObjectFile(@PathVariable("specId") Integer specId, @PathVariable("fileId") Integer fileId) {
        return specificationsService.getLatestUploadedObjectFile(specId, fileId);
    }

    @RequestMapping(value = "/{specId}/files/paste", method = RequestMethod.PUT)
    public List<RmObjectFile> pasteFromClipboard(@PathVariable("specId") Integer specId, @RequestParam("fileId") Integer fileId,
                                                 @RequestBody List<PLMFile> files) {
        return specificationsService.pasteFromClipboard(specId, fileId, files);
    }

    @RequestMapping(value = "/{specId}/files/undo", method = RequestMethod.PUT)
    public void undoCopiedObjectFiles(@PathVariable("specId") Integer specId, @RequestBody List<RmObjectFile> rmObjectFiles) {
        specificationsService.undoCopiedObjectFiles(specId, rmObjectFiles);
    }


    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return specificationsService.attachSpecWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/{specId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("specId") Integer specId) {
        workflowService.deleteWorkflow(specId);
    }

    @RequestMapping(value = "/type/{id}", method = RequestMethod.GET)
    public RmObjectType getObjectType(@PathVariable("id") Integer id) {
        return specificationsService.getObjectType(id);
    }

    @RequestMapping(value = "/workflow/{typeId}/specType/{type}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getWorkflows(@PathVariable("typeId") Integer typeId,
                                                    @PathVariable("type") String type) {
        return specificationsService.getHierarchyWorkflows(typeId, type);
    }

}
