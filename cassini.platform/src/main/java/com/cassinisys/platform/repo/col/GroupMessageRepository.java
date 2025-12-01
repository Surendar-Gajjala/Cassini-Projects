package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.GroupMessage;
import com.cassinisys.platform.model.core.ObjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by lakshmi on 5/1/2016.
 */
@Repository
public interface GroupMessageRepository extends JpaRepository<GroupMessage, Integer>, QueryDslPredicateExecutor<GroupMessage> {

    @Query("select gm from GroupMessage gm where gm.msgGrpId = :msgGrpId and gm.ctxObjectType= :ctxObjectType and gm.ctxObjectId=:ctxObjectId ORDER BY gm.createdDate")
    Page<GroupMessage> findByGroupMessagesByGrpId(
            @Param(value = "msgGrpId") Integer msgGrpId,@Param(value = "ctxObjectType")ObjectType ctxObjectType,@Param(value = "ctxObjectId")Integer ctxObjectId, Pageable pageable);


    @Query("select coalesce(count(gm.id), 0) from GroupMessage gm where gm.msgGrpId = :msgGrpId and gm.ctxObjectType= :ctxObjectType and gm.ctxObjectId=:ctxObjectId")
    Long getMessageCountByMessageGrpId(
            @Param(value = "msgGrpId") Integer msgGrpId,@Param(value = "ctxObjectType")ObjectType ctxObjectType,@Param(value = "ctxObjectId")Integer ctxObjectId);


    @Query("select gm from GroupMessage gm where gm.msgGrpId=:msgGrpId and gm.id > :id and gm.ctxObjectType= :ctxObjectType and gm.ctxObjectId=:ctxObjectId")
    List<GroupMessage> getRecentMessages(@Param(value = "msgGrpId") Integer msgGrpId,@Param(value = "id") Integer id,@Param(value = "ctxObjectType")ObjectType ctxObjectType,@Param(value = "ctxObjectId")Integer ctxObjectId);


    @Query("select gm from GroupMessage gm where gm.postedDate > :postedDate")
    List<GroupMessage> getMessagesByPostedDateAndTime(
            @Param(value = "postedDate") Date postedDate);

}
