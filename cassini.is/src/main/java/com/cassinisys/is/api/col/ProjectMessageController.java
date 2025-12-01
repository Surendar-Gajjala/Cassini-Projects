package com.cassinisys.is.api.col;
/**
 * The Class is  for ProjectMessageController
 **/

import com.cassinisys.is.model.col.ISProjectMessage;
import com.cassinisys.is.service.col.ProjectMessageService;
import com.cassinisys.platform.api.core.BaseController;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(name = "Project messages",
        description = "Project messages endpoint",
        group = "Project")
@RestController
@RequestMapping("/projects/{projectId}/messages")
public class ProjectMessageController extends BaseController {

    @Autowired
    private ProjectMessageService projectMessageService;

    /**
     * The method is used to create ISProjectMessage
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISProjectMessage send(@PathVariable("projectId") Integer projectId,
                                 @RequestBody ISProjectMessage message) {
        return projectMessageService.create(message);
    }

    /**
     * The method is used to delete ISProjectMessage with id
     **/
    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("projectId") Integer projectId,
                       @PathVariable("messageId") Integer messageId) {
        projectMessageService.delete(messageId);
    }

    /**
     * The method is used to obtain(or get) ISProjectMessage with id
     **/
    @RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
    public ISProjectMessage get(@PathVariable("projectId") Integer projectId,
                                @PathVariable("messageId") Integer messageId) {
        return projectMessageService.get(messageId);
    }

    /**
     * The method is used to getMultiples of  ISProjectMessage with id
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISProjectMessage> getMultiple(@PathVariable Integer[] ids) {
        return projectMessageService.findMultiple(Arrays.asList(ids));
    }

}
