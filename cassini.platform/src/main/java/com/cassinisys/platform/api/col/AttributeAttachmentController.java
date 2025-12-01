package com.cassinisys.platform.api.col;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.col.AttributeAttachmentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 024 24-Jul -17.
 */
@RestController
@RequestMapping("/col/attributeAttachments")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class AttributeAttachmentController extends BaseController {

    @Autowired
    private AttributeAttachmentService attributeAttachmentService;


    @RequestMapping(value = "/multiple", method = RequestMethod.POST)
    public List<AttributeAttachment> uploadMultipleAttachments(@RequestParam("objectId") Integer objectId,
                                                               @RequestParam("attributeDef") Integer attributeDef,
                                                               @RequestParam("objectType") ObjectType objectType, MultipartHttpServletRequest request) {
        List<MultipartFile> files = request.getFiles("files");
        return attributeAttachmentService.addAttributeMultipleAttachments(objectId, attributeDef, objectType, files);
    }


    @RequestMapping(method = RequestMethod.POST)
    public List<AttributeAttachment> uploadAttachments(
            @RequestParam("objectId") Integer objectId,
            @RequestParam("attributeDef") Integer attributeDef,
            @RequestParam("objectType") ObjectType objectType,
            MultipartHttpServletRequest request) {
        return attributeAttachmentService.addAttributeAttachments(objectId, attributeDef, objectType,
                request.getFileMap());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AttributeAttachment update(@PathVariable("id") Integer id, @RequestBody AttributeAttachment attributeAttachment) {
        checkNotNull(id);
        return attributeAttachmentService.update(attributeAttachment);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        attributeAttachmentService.delete(id);
    }

    @RequestMapping(value = "/{id}/object/{objectId}", method = RequestMethod.DELETE)
    public void deleteAttachment(@PathVariable("id") Integer id, @PathVariable("objectId") Integer objectId) {
        attributeAttachmentService.deleteAttachment(id, objectId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AttributeAttachment get(@PathVariable("id") Integer id) {
        return attributeAttachmentService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<AttributeAttachment> getAll() {
        return attributeAttachmentService.getAll();
    }

    @RequestMapping(value = "/objectId/{objectId}/attributeId/{attributeDef}", method = RequestMethod.GET)
    public List<AttributeAttachment> getAttributeAttachmentsByObjectIdAndAttributeId(@PathVariable("objectId") Integer objectId,
                                                                                     @PathVariable("attributeDef") Integer attributeDef) {
        return attributeAttachmentService.getAttributeAttachmentsByObjectIdAndAttributeId(objectId, attributeDef);

    }

    @RequestMapping(value = "/objectId/{objectId}", method = RequestMethod.GET)
    public List<AttributeAttachment> getAttributeAttachmentsByObjectId(@PathVariable("objectId") Integer objectId) {
        return attributeAttachmentService.getAttributeAttachmentsByObjectId(objectId);
    }

    @RequestMapping(value = "/multiple/[{attachmentIds}]", method = RequestMethod.GET)
    public List<AttributeAttachment> getMultipleAttributeAttachments(@PathVariable("attachmentIds") Integer[] attachmentIds) {
        return attributeAttachmentService.getMultipleAttributeAttachments(Arrays.asList(attachmentIds));
    }
}
