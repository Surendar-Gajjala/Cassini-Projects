package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.GroupMemberStatus;
import com.cassinisys.platform.model.col.MessageGroup;
import com.cassinisys.platform.model.col.MessageGroupMember;
import com.cassinisys.platform.model.core.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 5/1/2016.
 */
@Repository
public interface MessageGroupMemberRepository extends JpaRepository<MessageGroupMember, Integer> {



    @Query("select mgm from MessageGroupMember mgm where mgm.messageGroup.id = :groupId and mgm.status = :status and mgm.ctxObjectType= :ctxObjectType and mgm.ctxObjectId=:ctxObjectId")
    List<MessageGroupMember> findByMessageGroupIdAndStatus(@Param("groupId")Integer groupId, @Param("status")GroupMemberStatus status,@Param("ctxObjectType")ObjectType ctxObjectType,@Param("ctxObjectId")Integer ctxObjectId);


    @Query("select mgm from MessageGroupMember mgm where mgm.id = :id and mgm.ctxObjectType= :ctxObjectType and mgm.ctxObjectId=:ctxObjectId")
    MessageGroupMember findById(@Param("id")Integer id,@Param("ctxObjectType")ObjectType ctxObjectType,@Param("ctxObjectId")Integer ctxObjectId);


    @Query("select coalesce(count(mgm.id), 0) from MessageGroupMember mgm where mgm.messageGroup.id = :msgGrpId and mgm.status= :status and mgm.ctxObjectType= :ctxObjectType and mgm.ctxObjectId=:ctxObjectId")
    Long getMessageGroupActiveMemberCount(
            @Param(value = "msgGrpId") Integer msgGrpId,@Param(value = "status")GroupMemberStatus status,@Param(value = "ctxObjectType")ObjectType ctxObjectType,@Param(value = "ctxObjectId")Integer ctxObjectId);

    MessageGroupMember findByPersonAndMessageGroupAndStatus(Integer personId, MessageGroup messageGroup, GroupMemberStatus status);
    MessageGroupMember findByPersonAndMessageGroup(Integer personId, MessageGroup messageGroup);

}
