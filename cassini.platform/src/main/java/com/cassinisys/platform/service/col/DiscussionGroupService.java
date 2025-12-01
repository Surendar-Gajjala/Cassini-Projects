package com.cassinisys.platform.service.col;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.col.DiscussionGroup;
import com.cassinisys.platform.model.col.DiscussionGroupMessage;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.DiscussionGroupMessageRepository;
import com.cassinisys.platform.repo.col.DiscussionGroupRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by lakshmi on 5/25/2016.
 */

@Service
public class DiscussionGroupService implements CrudService<DiscussionGroup, Integer>,
        PageableService<DiscussionGroup, Integer> {

    @Autowired
    private DiscussionGroupRepository discussionGroupRepository;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private DiscussionGroupMessageRepository discussionGroupMessageRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Override
    @Transactional
    public DiscussionGroup create(DiscussionGroup discussionGroup) {
        checkNotNull(discussionGroup);
        discussionGroup.setId(null);

        discussionGroup= discussionGroupRepository.save(discussionGroup);

        return discussionGroup;
    }

    @Override
    @Transactional
    public DiscussionGroup update(DiscussionGroup discussionGroup) {
        checkNotNull(discussionGroup);
        checkNotNull(discussionGroup.getId());

        discussionGroup= discussionGroupRepository.save(discussionGroup);

        return discussionGroup;
    }


    @Override
    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        DiscussionGroup  discussionGroup= discussionGroupRepository.findOne(id);
        discussionGroupRepository.delete(id);

        }

    @Override
    @Transactional(readOnly = true)
    public DiscussionGroup get(Integer id) {
        checkNotNull(id);
        DiscussionGroup discussionGroup = discussionGroupRepository.findOne(id);
        if (discussionGroup == null) {
            throw new ResourceNotFoundException();
        }
        return discussionGroup;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscussionGroup> getAll() {
        return discussionGroupRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<DiscussionGroup> findAllByCtx(ObjectType ctxObjectType,Integer ctxObjectId, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.ASC,
                    "name")));
        }
        Page<DiscussionGroup> discussionGroups= discussionGroupRepository.findByCtxObjectTypeAndCtxObjectId(ctxObjectType, ctxObjectId,pageable);

        for( DiscussionGroup discussionGroup: discussionGroups){

            if(discussionGroup!=null){

                //get the messages count for group

            //    discussionGroup.setMessagesCount(discussionGroupMessageRepository.getMessageCountByDiscussionGrpId(discussionGroup.getId()));


                Person person = personRepository.findOne(discussionGroup.getModerator());

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
                            discussionGroup.setPersonName(fullName.toString());

                        }
                    }
                }


        return discussionGroups;
    }


    @Transactional
    public DiscussionGroupMessage createDiscussionGroupMessage(DiscussionGroupMessage discussionGroupMessage) {
        checkNotNull(discussionGroupMessage);
        discussionGroupMessage.setId(null);
        discussionGroupMessage.setCommentedDate(new Date());
        discussionGroupMessage= discussionGroupMessageRepository.save(discussionGroupMessage);

        return discussionGroupMessage;
    }

    @Transactional(readOnly = true)
    public Page<DiscussionGroupMessage> getAllDiscussionGroupMessagesByGroupId(Integer id,ObjectType ctxObjectType,Integer ctxObjectId,Pageable pagable) {

        Page<DiscussionGroupMessage> dissGrpMessages= discussionGroupMessageRepository.findByDiscussionGroupMessagesByGrpId(id,ctxObjectType,ctxObjectId, pagable);

        for( DiscussionGroupMessage dissGrpMsg: dissGrpMessages) {

            if (dissGrpMsg != null) {

                //add the attachments also
                List<Attachment> attchments= attachmentService.findAll(dissGrpMsg.getId(), ObjectType.DISCUSSIONGROUPMESSAGE);
                dissGrpMsg.setAttachments(attchments);
            }
        }

        return dissGrpMessages;
    }
    @Transactional(readOnly = true)
    public Page<DiscussionGroupMessage> getRootDiscussionGrpMessages(Integer groupId,ObjectType ctxObjectType,
                                         Integer ctxObjectId, Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC,
                    "commentedDate")));
        }
        Page<DiscussionGroupMessage> discussionGroupMessages= discussionGroupMessageRepository.findByDiscussionGroupIdAndCtxObjectTypeAndCtxObjectIdAndReplyToIsNull(
                groupId,ctxObjectType, ctxObjectId, pageable);

      /*  for (DiscussionGroupMessage discussionGroupMessage : discussionGroupMessages) {
            visitChildren(discussionGroupMessage);
        }*/


        return discussionGroupMessages;
    }

    @Transactional(readOnly = true)
    public List<DiscussionGroupMessage> getChildren(Integer replyTo) {
        return discussionGroupMessageRepository.findByReplyToOrderByCommentedDateDesc(replyTo);
    }

    private void visitChildren(DiscussionGroupMessage replyTo) {

        if (replyTo != null) {

            //add the attachments also
            List<Attachment> attchments= attachmentService.findAll(replyTo.getId(), ObjectType.DISCUSSIONGROUPMESSAGE);
            replyTo.setAttachments(attchments);
        }

         List<DiscussionGroupMessage> children = getChildren(replyTo.getId());

        for (DiscussionGroupMessage child : children) {
            visitChildren(child);
        }

        replyTo.setChildren(children);
    }

    @Transactional(readOnly = true)
    public Page<DiscussionGroupMessage> getRepliesGroupMessages(Integer dissGrpMessId, Pageable pageable) {
        checkNotNull(dissGrpMessId);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC,
                    "commentedDate")));
        }
        return discussionGroupMessageRepository.findByReplyTo(dissGrpMessId, pageable);
    }

    @Transactional(readOnly = true)
    public DiscussionGroupMessage getDiscussionGroupMessage(Integer id){

        return discussionGroupMessageRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DiscussionGroup> findAll(Pageable pageable) {
        return null;
    }
}
