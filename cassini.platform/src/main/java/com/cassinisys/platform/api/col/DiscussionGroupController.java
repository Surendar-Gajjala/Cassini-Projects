package com.cassinisys.platform.api.col;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.DiscussionGroup;
import com.cassinisys.platform.model.col.DiscussionGroupMessage;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.col.DiscussionGroupService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * Created by lakshmi on 5/25/2016.
 */

@RestController
@RequestMapping("/col/discussions")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class DiscussionGroupController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private DiscussionGroupService discussionGroupService;

    @RequestMapping(method = RequestMethod.POST)
    public DiscussionGroup create(@RequestBody DiscussionGroup discussionGroup) {
        discussionGroup.setId(null);
        return discussionGroupService.create(discussionGroup);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public DiscussionGroup update(@PathVariable("id") Integer id,
                               @RequestBody DiscussionGroup discussionGroup) {
        discussionGroup.setId(id);
        return discussionGroupService.update(discussionGroup);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        discussionGroupService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DiscussionGroup get(@PathVariable("id") Integer id) {
        return discussionGroupService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<DiscussionGroup> getAll(@RequestParam(value="ctxObjectId", required = false) Integer ctxObjectId,
                                        @RequestParam(value="ctxObjectType", required = false) ObjectType ctxObjectType,PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return discussionGroupService.findAllByCtx(ctxObjectType,ctxObjectId,pageable);
    }

    @RequestMapping(value= "/messages",method = RequestMethod.POST)
    public DiscussionGroupMessage createDiscussionGroupMsg(@RequestBody DiscussionGroupMessage discussionGroupMessage) {
        discussionGroupMessage.setId(null);
        return discussionGroupService.createDiscussionGroupMessage(discussionGroupMessage);
    }

    @RequestMapping(value = "/messages/{groupid}", method = RequestMethod.GET)
    public Page<DiscussionGroupMessage> getDiscussionGroupMessages(@PathVariable("groupid") Integer groupid,@RequestParam(value="ctxObjectId", required = false) Integer ctxObjectId,
                                                                   @RequestParam(value="ctxObjectType", required = false) ObjectType ctxObjectType,PageRequest pageRequest) {


        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return discussionGroupService.getAllDiscussionGroupMessagesByGroupId(groupid,ctxObjectType,ctxObjectId, pageable);
    }

    @RequestMapping(value = "/messages/discussion/{discussionmsgid}", method = RequestMethod.GET)
    public DiscussionGroupMessage getDiscussionGroupMessage(@PathVariable("discussionmsgid") Integer discussionId) {



        return discussionGroupService.getDiscussionGroupMessage(discussionId);
    }


    @RequestMapping(value="/messages/{groupid}/getAll", method = RequestMethod.GET)
    public Page<DiscussionGroupMessage> getAllRootGrpMessages(@PathVariable("groupid") Integer groupid,@RequestParam(value="ctxObjectId", required = false) Integer ctxObjectId,
                                                              @RequestParam(value="ctxObjectType", required = false) ObjectType ctxObjectType,
                                PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return discussionGroupService.getRootDiscussionGrpMessages(groupid,ctxObjectType, ctxObjectId, pageable);
    }

    @RequestMapping(value = "messages/{id}/replies", method = RequestMethod.GET)
    public Page<DiscussionGroupMessage> getReplies(@PathVariable("id") Integer id,
                                    PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return discussionGroupService.getRepliesGroupMessages(id, pageable);
    }



}
