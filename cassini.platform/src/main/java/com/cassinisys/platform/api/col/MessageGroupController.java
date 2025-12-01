package com.cassinisys.platform.api.col;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.filtering.GroupMessageCriteria;
import com.cassinisys.platform.filtering.MessageGroupCriteria;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.col.GroupMessage;
import com.cassinisys.platform.model.col.MessageGroup;
import com.cassinisys.platform.model.col.MessageGroupMember;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.GroupMessageDTO;
import com.cassinisys.platform.service.col.MessageGroupService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by lakshmi on 5/2/2016.
 */

@RestController
@RequestMapping("/col/messaging/groups")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class MessageGroupController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private MessageGroupService messageGroupService;

    @RequestMapping(method = RequestMethod.POST)
    public MessageGroup create(@RequestBody MessageGroup messageGroup) {
        messageGroup.setId(null);
        return messageGroupService.create(messageGroup);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MessageGroup update(@PathVariable("id") Integer id,
                               @RequestBody MessageGroup messageGroup) {
        messageGroup.setId(id);
        return messageGroupService.update(messageGroup);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        messageGroupService.delete(id);
    }

    @RequestMapping(value = "/{groupId}/icon", method = RequestMethod.POST)
    public void uploadGroupIcon(@PathVariable("groupId") Integer groupId, MultipartHttpServletRequest request) {
        messageGroupService.uploadGroupIcon(groupId, request);
    }

    @RequestMapping(value = "/{groupId}/icon", method = RequestMethod.GET)
    public void getGroupIcon(@PathVariable("groupId") Integer groupId, HttpServletResponse response) {
        messageGroupService.getGroupIcon(groupId, response);
    }

    @RequestMapping(value = "/members/{id}", method = RequestMethod.DELETE)
    public void deleteGrpMember(@PathVariable("id") Integer id) {
        messageGroupService.deleteGrpMember(id);
    }

    @RequestMapping(value = "/members", method = RequestMethod.POST)
    public MessageGroupMember createGrpMember(@RequestBody MessageGroupMember messageGroupMember) {
        return messageGroupService.createGrpMember(messageGroupMember);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MessageGroup get(@PathVariable("id") Integer id, @RequestParam(value = "ctxObjectId", required = false) Integer ctxObjectId,
                            @RequestParam(value = "ctxObjectType", required = false) ObjectType ctxObjectType) {
        return messageGroupService.getByID(id, ctxObjectType, ctxObjectId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<MessageGroup> getAll(@RequestParam(value = "ctxObjectId", required = false) Integer ctxObjectId,
                                     @RequestParam(value = "ctxObjectType", required = false) ObjectType ctxObjectType, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return messageGroupService.findAllByCtx(ctxObjectType, ctxObjectId, pageable);
    }

    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public GroupMessage createGroupMsg(@RequestBody GroupMessage groupMessage) {
        groupMessage.setId(null);
        return messageGroupService.createGroupMessage(groupMessage);
    }

    @RequestMapping(value = "/messages/{groupid}", method = RequestMethod.GET)
    public GroupMessageDTO getGroupMessages(@PathVariable("groupid") Integer groupid, @RequestParam(value = "ctxObjectId", required = false) Integer ctxObjectId,
                                            @RequestParam(value = "ctxObjectType", required = false) ObjectType ctxObjectType, PageRequest pageRequest, GroupMessageCriteria groupMessageCriteria) {

        Pageable pageable = pageRequestConverter.convert(pageRequest);
        groupMessageCriteria.setFreeTextSearch(Boolean.TRUE);
        groupMessageCriteria.setCtxObjectId(ctxObjectId);
        groupMessageCriteria.setCtxObjectType(ctxObjectType);
        groupMessageCriteria.setMsgGrpId(groupid);
        return messageGroupService.getAllGroupMessagesByGroupId(groupid, ctxObjectType, ctxObjectId, pageable, groupMessageCriteria);
    }

    @RequestMapping(value = "/messages/recent/{groupid}/{msgId}", method = RequestMethod.GET)
    public GroupMessageDTO getRecentGroupMessages(@PathVariable("groupid") Integer groupid, @PathVariable("msgId") Integer msgId, @RequestParam(value = "ctxObjectId", required = false) Integer ctxObjectId,
                                                  @RequestParam(value = "ctxObjectType", required = false) ObjectType ctxObjectType) {

        return messageGroupService.getRecentGroupMessages(groupid, msgId, ctxObjectType, ctxObjectId);
    }


    @RequestMapping(value = "/messages/byposteddate", method = RequestMethod.POST)
    public Page<GroupMessage> getGroupMessages(@RequestBody GroupMessage groupMessage) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setSort("postedDate:desc");
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return messageGroupService.getGroupMessagesByDateAndTime(groupMessage, pageable);
    }

    @RequestMapping(value = "/{groupId}/messages/{messageId}/attachments", method = RequestMethod.POST)
    public List<Attachment> uploadAttachments(@PathVariable("messageId") Integer messageId, @RequestParam("objectId") Integer objectId,
                                              @RequestParam("objectType") ObjectType objectType,
                                              MultipartHttpServletRequest request) {

        return messageGroupService.createGroupMessageAttachment(messageId, objectId, objectType, request.getFileMap());

    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<MessageGroup> messageGroupFreeTextSearch(@RequestParam("ctxObjectId") Integer ctxObjectId,
                                                         @RequestParam("ctxObjectType") ObjectType ctxObjectType, MessageGroupCriteria criteria, PageRequest request) {
        Pageable pageable = pageRequestConverter.convert(request);
        criteria.setFreeTextSearch(Boolean.TRUE);
        criteria.setCtxObjectId(ctxObjectId);
        criteria.setCtxObjectType(ctxObjectType);
        return messageGroupService.messageGroupFreeTextSearch(criteria, pageable);
    }
}
