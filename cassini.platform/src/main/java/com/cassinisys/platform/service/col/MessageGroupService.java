package com.cassinisys.platform.service.col;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.filtering.GroupMessageCriteria;
import com.cassinisys.platform.filtering.GroupMessagePredicateBuilder;
import com.cassinisys.platform.filtering.MessageGroupCriteria;
import com.cassinisys.platform.filtering.MessageGroupPredicateBuilder;
import com.cassinisys.platform.model.col.*;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.GroupMessageDTO;
import com.cassinisys.platform.repo.col.GroupMessageRepository;
import com.cassinisys.platform.repo.col.MessageGroupMemberRepository;
import com.cassinisys.platform.repo.col.MessageGroupRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by lakshmi on 5/1/2016.
 */

@Service
public class MessageGroupService implements CrudService<MessageGroup, Integer>,
        PageableService<MessageGroup, Integer> {
    @Override
    public MessageGroup get(Integer integer) {
        return null;
    }

    @Autowired
    private MessageGroupRepository messageGroupRepository;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private GroupMessageRepository groupMessageRepository;

    @Autowired
    private MessageGroupMemberRepository messageGroupMemberRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private MessageGroupPredicateBuilder messageGroupPredicateBuilder;

    @Autowired
    private GroupMessagePredicateBuilder groupMessagePredicateBuilder;

    protected SessionWrapper getSessionWrapper() {
        return sessionWrapper;
    }

    @Override
    @Transactional
    public MessageGroup create(MessageGroup messageGroup) {
        checkNotNull(messageGroup);
        messageGroup.setId(null);
        MessageGroup group = messageGroupRepository.findByNameAndCtxObjectTypeAndCtxObjectId(messageGroup.getName(), messageGroup.getCtxObjectType(), messageGroup.getCtxObjectId());
        if(group != null) {
            throw new CassiniException("Group Name already exists");
        }
        else {
            Integer loggedInPersonId = sessionWrapper.getSession().getLogin().getPerson().getId();
            //set the logged in person as admin member as default
            for (MessageGroupMember member : messageGroup.getMsgGrpMembers()) {

                if (member != null && member.getPerson() == loggedInPersonId) {

                    member.setAdmin(true);

                }
            }

            messageGroup = messageGroupRepository.save(messageGroup);
        }

        return messageGroup;
    }

    @Override
    @Transactional
    public MessageGroup update(MessageGroup messageGroup) {
        checkNotNull(messageGroup);
        checkNotNull(messageGroup.getId());
        MessageGroup group = messageGroupRepository.findByNameAndCtxObjectTypeAndCtxObjectId(messageGroup.getName(), messageGroup.getCtxObjectType(), messageGroup.getCtxObjectId());
        if (group != null) {
            throw new CassiniException("Group Name already exists");
        } else {

            messageGroup = messageGroupRepository.save(messageGroup);

            messageGroup.setMessagesCount(groupMessageRepository.getMessageCountByMessageGrpId(messageGroup.getId(), messageGroup.getCtxObjectType(), messageGroup.getCtxObjectId()));

            messageGroup.setActiveUsersCount(messageGroupMemberRepository.getMessageGroupActiveMemberCount(messageGroup.getId(), GroupMemberStatus.ACTIVE, messageGroup.getCtxObjectType(), messageGroup.getCtxObjectId()));

            return messageGroup;
        }
    }


    @Override
    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        MessageGroup messageGroup = messageGroupRepository.findOne(id);
        messageGroupRepository.delete(id);
    }

    @Transactional
    public void uploadGroupIcon(Integer groupId, MultipartHttpServletRequest request) {
        MessageGroup messageGroup = messageGroupRepository.findOne(groupId);
        Map<String,MultipartFile> map = request.getFileMap();
        List<MultipartFile> files = new ArrayList<>(map.values());
        if(files.size() > 0) {
            MultipartFile file = files.get(0);
            try {
                byte[] picData = file.getBytes();
                if(messageGroup != null) {
                    messageGroup.setGroupIcon(picData);
                    messageGroupRepository.save(messageGroup);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void getGroupIcon(Integer groupId,HttpServletResponse response) {
        MessageGroup messageGroup = messageGroupRepository.findOne(groupId);
        if(messageGroup != null && messageGroup.getGroupIcon() != null) {
            InputStream is = new ByteArrayInputStream(messageGroup.getGroupIcon());
            try {
                org.apache.commons.io.IOUtils.copy(is,response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public MessageGroupMember createGrpMember(MessageGroupMember messageGroupMember) {
        MessageGroupMember groupMember = messageGroupMemberRepository.findByPersonAndMessageGroup(messageGroupMember.getPerson(), messageGroupMember.getMessageGroup());
        if(groupMember != null && (groupMember.getStatus() == GroupMemberStatus.DELETED || groupMember.getStatus() == GroupMemberStatus.DISABLED)) {
            groupMember.setStatus(GroupMemberStatus.ACTIVE);
            messageGroupMember = groupMember;
        }
       return messageGroupMemberRepository.save(messageGroupMember);
    }

    @Transactional
    public void deleteGrpMember(Integer id) {
        checkNotNull(id);
        MessageGroupMember groupMember = messageGroupMemberRepository.findOne(id);
        groupMember.setStatus(GroupMemberStatus.DELETED);
        messageGroupMemberRepository.save(groupMember);

    }
    @Transactional(readOnly = true)
    public MessageGroup getByID(Integer id,ObjectType ctxObjectType,Integer ctxObjectId) {
        checkNotNull(id);
        MessageGroup messageGroup = messageGroupRepository.findByIdAndCtxObjectTypeAndCtxObjectId(id, ctxObjectType, ctxObjectId);
        if (messageGroup == null) {
            throw new ResourceNotFoundException();
        }
        return messageGroup;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageGroup> getAll() {
        return messageGroupRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Page<MessageGroup> findAll(Pageable pagable){
        return null;
    }

    @Transactional(readOnly = true)
    public Page<MessageGroup> findAllByCtx(ObjectType ctxObjectType,Integer ctxObjectId,Pageable pageable) {
        List<MessageGroup> messageGroupList = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();

        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.ASC,
                    "name")));
        }
        List<MessageGroup> msgGrps= messageGroupRepository.findByCtxObjectTypeAndCtxObjectId(ctxObjectType, ctxObjectId);

        for( MessageGroup msgGrp: msgGrps){

            if(msgGrp!=null){

                //get the messages count for group

                msgGrp.setMessagesCount(groupMessageRepository.getMessageCountByMessageGrpId(msgGrp.getId(),ctxObjectType,ctxObjectId));

                msgGrp.setActiveUsersCount(messageGroupMemberRepository.getMessageGroupActiveMemberCount(msgGrp.getId(), GroupMemberStatus.ACTIVE,ctxObjectType,ctxObjectId));
                //set the name of the group member
                List<MessageGroupMember> msgGpMbrs = messageGroupMemberRepository.findByMessageGroupIdAndStatus(msgGrp.getId(), GroupMemberStatus.ACTIVE, ctxObjectType, ctxObjectId);
                MessageGroupMember groupMember = messageGroupMemberRepository.findByPersonAndMessageGroupAndStatus(login.getPerson().getId(), msgGrp, GroupMemberStatus.ACTIVE);
                if(groupMember != null) {
                    messageGroupList.add(msgGrp);
                }
                for(MessageGroupMember member: msgGpMbrs){

                    if(member!=null){

                        Person person = personRepository.findOne(member.getPerson());

                        if (person != null) {

                            StringBuffer fullName = new StringBuffer();
                            if (person.getFirstName() != null && person.getFirstName().length() > 0) {
                                fullName.append(person.getFirstName()).append(" ");
                            }
                            if (person.getMiddleName() != null && person.getMiddleName().length() > 0) {
                                fullName.append(person.getMiddleName()).append(" ");
                            }
                            if (person.getLastName() != null && person.getLastName().length() > 0) {
                                fullName.append(person.getLastName());
                            }
                            member.setPersonName(fullName.toString());

                        }
                    }
                }

                msgGrp.setMsgGrpMembers(msgGpMbrs);

            }
        }

        int start =  pageable.getOffset();
        int end = (start + pageable.getPageSize()) > messageGroupList.size() ? messageGroupList.size() : (start + pageable.getPageSize());

        return new PageImpl<MessageGroup>(messageGroupList.subList(start, end), pageable, messageGroupList.size());
    }


    @Transactional
    public GroupMessage createGroupMessage(GroupMessage groupMessage) {
        checkNotNull(groupMessage);
        groupMessage.setId(null);
        groupMessage.setPostedDate(new Date());
        MessageGroup messageGroup = messageGroupRepository.findOne(groupMessage.getMsgGrpId());
        MessageGroupMember messageGroupMember= messageGroupMemberRepository.findByPersonAndMessageGroupAndStatus(groupMessage.getPostedBy(), messageGroup, GroupMemberStatus.ACTIVE);
        groupMessage.setPostedBy(messageGroupMember.getId());
        groupMessage= groupMessageRepository.save(groupMessage);
        Person person = personRepository.findOne(messageGroupMember.getPerson());

        if (person != null) {

            StringBuffer fullName = new StringBuffer();
            if (person.getFirstName() != null && person.getFirstName().length() > 0) {
                fullName.append(person.getFirstName()).append(" ");
            }
            if (person.getMiddleName() != null && person.getMiddleName().length() > 0) {
                fullName.append(person.getMiddleName()).append(" ");
            }
            if (person.getLastName() != null && person.getLastName().length() > 0) {
                fullName.append(person.getLastName());
            }
            groupMessage.setPostedByName(fullName.toString());

        }

        return groupMessage;
    }
    @Transactional(readOnly = true)
    public GroupMessageDTO getRecentGroupMessages(Integer gId,Integer msgId,ObjectType ctxObjectType,Integer ctxObjectId){

        GroupMessageDTO grpMsgDTO= new GroupMessageDTO();
        List<GroupMessage> grpMessages= groupMessageRepository.getRecentMessages(gId,msgId,ctxObjectType,ctxObjectId);

        grpMsgDTO.setMessageCount(groupMessageRepository.getMessageCountByMessageGrpId(gId, ctxObjectType, ctxObjectId));

        grpMsgDTO.setActiveUsersCount(messageGroupMemberRepository.getMessageGroupActiveMemberCount(gId, GroupMemberStatus.ACTIVE, ctxObjectType, ctxObjectId));

        grpMsgDTO.setListGroupMessages(grpMessages);

        return grpMsgDTO;

    }
    @Transactional(readOnly = true)
    public GroupMessageDTO getAllGroupMessagesByGroupId(Integer id,ObjectType ctxObjectType,Integer ctxObjectId,Pageable pagable, GroupMessageCriteria criteria) {

        GroupMessageDTO grpMsgDTO= new GroupMessageDTO();
        Predicate predicate = groupMessagePredicateBuilder.build(criteria, QGroupMessage.groupMessage);

        Page<GroupMessage> grpMessages = groupMessageRepository.findAll(predicate, pagable);
        for( GroupMessage grpMsg: grpMessages) {

            if (grpMsg != null) {

                if(grpMsg.getPostedBy()>0) {

                    MessageGroupMember member= messageGroupMemberRepository.findById(grpMsg.getPostedBy(), ctxObjectType, ctxObjectId);

                    Person person = personRepository.findOne(member.getPerson());

                    if (person != null) {

                        StringBuffer fullName = new StringBuffer();
                        if (person.getFirstName() != null && person.getFirstName().length() > 0) {
                            fullName.append(person.getFirstName()).append(" ");
                        }
                        if (person.getMiddleName() != null && person.getMiddleName().length() > 0) {
                            fullName.append(person.getMiddleName()).append(" ");
                        }
                        if (person.getLastName() != null && person.getLastName().length() > 0) {
                            fullName.append(person.getLastName());
                        }
                        grpMsg.setPostedByName(fullName.toString());

                    }
                }
                //add the attachments also

                List<Attachment> attchments= attachmentService.findAll(grpMsg.getId(), ObjectType.GROUPMESSAGE);
                grpMsg.setAttachments(attchments);
                    }
                }
        grpMsgDTO.setMessageCount(groupMessageRepository.getMessageCountByMessageGrpId(id, ctxObjectType, ctxObjectId));
        grpMsgDTO.setActiveUsersCount(messageGroupMemberRepository.getMessageGroupActiveMemberCount(id, GroupMemberStatus.ACTIVE, ctxObjectType, ctxObjectId));
        grpMsgDTO.setGroupMessages(grpMessages);
        List<GroupMessage> list = new ArrayList(grpMessages.getContent());
        grpMsgDTO.setListGroupMessages(list);

        return grpMsgDTO;
    }

    @Transactional(readOnly = true)
    public Page<GroupMessage> getGroupMessagesByDateAndTime(GroupMessage grpMsg,Pageable pageable){
        List<GroupMessage> groupMessageList = groupMessageRepository.getMessagesByPostedDateAndTime(grpMsg.getPostedDate());
        for(GroupMessage groupMessage: groupMessageList) {
            List<Attachment> attachments = attachmentService.findAll(groupMessage.getId(), ObjectType.GROUPMESSAGE);
            groupMessage.setAttachments(attachments);
        }
        int start =  pageable.getOffset();
        int end = (start + pageable.getPageSize()) > groupMessageList.size() ? groupMessageList.size() : (start + pageable.getPageSize());

        return new PageImpl<GroupMessage>(groupMessageList.subList(start, end), pageable, groupMessageList.size());

    }
    @Transactional
    public  List<Attachment> createGroupMessageAttachment(Integer messageId, Integer objectId,ObjectType objectType,Map<String, MultipartFile> fileMap){

        List<Attachment> attachments= attachmentService.addAttachments(messageId, ObjectType.GROUPMESSAGE,
                fileMap);

        return attachments;
    }

    public Page<MessageGroup> messageGroupFreeTextSearch(MessageGroupCriteria criteria, Pageable pageable) {
        Predicate predicate = messageGroupPredicateBuilder.build(criteria, QMessageGroup.messageGroup);
        Iterable<MessageGroup> messageGroupList = messageGroupRepository.findAll(predicate);
        List<MessageGroup> messageGroups = new ArrayList<>();
        for(MessageGroup messageGroup: messageGroupList) {
            if(messageGroup!=null){

                //get the messages count for group

                messageGroup.setMessagesCount(groupMessageRepository.getMessageCountByMessageGrpId(messageGroup.getId(), criteria.getCtxObjectType(), criteria.getCtxObjectId()));

                messageGroup.setActiveUsersCount(messageGroupMemberRepository.getMessageGroupActiveMemberCount(messageGroup.getId(), GroupMemberStatus.ACTIVE, criteria.getCtxObjectType(), criteria.getCtxObjectId()));
                //set the name of the group member
                List<MessageGroupMember> msgGpMbrs = messageGroupMemberRepository.findByMessageGroupIdAndStatus(messageGroup.getId(), GroupMemberStatus.ACTIVE, criteria.getCtxObjectType(), criteria.getCtxObjectId());
                MessageGroupMember groupMember = messageGroupMemberRepository.findByPersonAndMessageGroupAndStatus(sessionWrapper.getSession().getLogin().getPerson().getId(), messageGroup, GroupMemberStatus.ACTIVE);
                if(groupMember != null) {
                    messageGroups.add(messageGroup);
                }
                for(MessageGroupMember member: msgGpMbrs){

                    if(member!=null){

                        Person person = personRepository.findOne(member.getPerson());

                        if (person != null) {

                            StringBuffer fullName = new StringBuffer();
                            if (person.getFirstName() != null && person.getFirstName().length() > 0) {
                                fullName.append(person.getFirstName()).append(" ");
                            }
                            if (person.getMiddleName() != null && person.getMiddleName().length() > 0) {
                                fullName.append(person.getMiddleName()).append(" ");
                            }
                            if (person.getLastName() != null && person.getLastName().length() > 0) {
                                fullName.append(person.getLastName());
                            }
                            member.setPersonName(fullName.toString());

                        }
                    }
                }

                messageGroup.setMsgGrpMembers(msgGpMbrs);

            }
        }
        int start =  pageable.getOffset();
        int end = (start + pageable.getPageSize()) > messageGroups.size() ? messageGroups.size() : (start + pageable.getPageSize());

        return new PageImpl<MessageGroup>(messageGroups.subList(start, end), pageable, messageGroups.size());
    }

}
