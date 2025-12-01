package com.cassinisys.platform.api.col;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.col.AttachmentService;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author reddy
 */
@RestController
@RequestMapping("/col/attachments")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class AttachmentController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private AttachmentService attachmentService;

    @RequestMapping(value = "/multipleFilesPost", method = RequestMethod.POST)
    public List<Attachment> uploadAttachments(@RequestParam("objectType") ObjectType objectType, @RequestParam("objectIds") Integer[] objectIds, MultipartHttpServletRequest request) {
        List<Attachment> lists = new ArrayList<>();
        Integer index = 0;
        List<MultipartFile> files = request.getFiles("file");
        for (Integer objectId : objectIds) {
            Attachment attachment = attachmentService.multiplePartFilesPost(objectId, objectType, files.get(index));
            index++;
            lists.add(attachment);
        }
        return lists;
    }

    @RequestMapping(method = RequestMethod.POST)
    public List<Attachment> uploadAttachments(@RequestParam("objectId") Integer objectId, @RequestParam("objectType") ObjectType objectType, MultipartHttpServletRequest request) {
        return attachmentService.addAttachments(objectId, objectType,
                request.getFileMap());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        attachmentService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Attachment get(@PathVariable("id") Integer id) {
        return attachmentService.get(id);
    }

    @RequestMapping(value = "/{id}/download", method = RequestMethod.GET)
    public void download(@PathVariable("id") Integer id, HttpServletResponse response) {
        attachmentService.downloadAttachment(id, response);
    }

    @RequestMapping(value = "/{id}/preview", method = RequestMethod.GET)
    public void previewFile(@PathVariable("id") Integer id, HttpServletResponse response) throws Exception {
        attachmentService.previewFile(id, response);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Attachment> getAll(@RequestParam("objectId") Integer objectId,
                                   @RequestParam("objectType") ObjectType objectType) {
        return attachmentService.findAll(objectId, objectType);
    }

    @RequestMapping(value = "/multiple/[{attachmentIds}]", method = RequestMethod.GET)
    public List<Attachment> getMultipleAttachments(@PathVariable("attachmentIds") Integer[] attachmentIds) {
        return attachmentService.getMultipleAttachments(Arrays.asList(attachmentIds));
    }

    @RequestMapping(value = "/ext/{ext}", method = RequestMethod.GET)
    public List<Attachment> getAllByExtension(String ext) {
        return attachmentService.findByExtension(ext);
    }

    @RequestMapping(value = "/exts/[{exts}]")
    public List<Attachment> getAllByExtension(String[] exts) {
        return attachmentService.findByExtensionIn(Arrays.asList(exts));
    }
}
