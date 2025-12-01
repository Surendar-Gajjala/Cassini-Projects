package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.DiscussionGroupMessage;
import com.cassinisys.platform.model.core.ObjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 5/25/2016.
 */

@Repository
public interface DiscussionGroupMessageRepository extends JpaRepository<DiscussionGroupMessage, Integer> {


    @Query("select coalesce(count(dgm.id), 0) from DiscussionGroupMessage dgm where dgm.discussionGroupId = :discussionGroupId")
    Long getMessageCountByDiscussionGrpId(
            @Param(value = "discussionGroupId") Integer discussionGroupId);


    @Query("select dgm from DiscussionGroupMessage dgm where dgm.discussionGroupId = :discussionGroupId and dgm.ctxObjectType=:ctxObjectType and dgm.ctxObjectId=:ctxObjectId")
    Page<DiscussionGroupMessage> findByDiscussionGroupMessagesByGrpId(
            @Param(value = "discussionGroupId") Integer discussionGroupId,@Param(value = "ctxObjectType")ObjectType ctxObjectType,@Param(value = "ctxObjectId")Integer ctxObjectId, Pageable pageable);


     Page<DiscussionGroupMessage> findByDiscussionGroupIdAndCtxObjectTypeAndCtxObjectIdAndReplyToIsNull(Integer groupId,
             ObjectType ctxObjectType,Integer ctxObjectId, Pageable pageable);

     Page<DiscussionGroupMessage> findByReplyTo(Integer replyTo, Pageable pageable);


    List<DiscussionGroupMessage> findByReplyToOrderByCommentedDateDesc(Integer replyTo);

}
