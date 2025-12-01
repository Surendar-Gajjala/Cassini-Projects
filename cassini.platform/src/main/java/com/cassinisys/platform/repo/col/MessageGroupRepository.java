package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.MessageGroup;
import com.cassinisys.platform.model.core.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 5/1/2016.
 */
@Repository
public interface MessageGroupRepository extends JpaRepository<MessageGroup, Integer>, QueryDslPredicateExecutor<MessageGroup> {

    MessageGroup findByIdAndCtxObjectTypeAndCtxObjectId(Integer id,ObjectType ctxObjectType,Integer ctxObjectId);


    List<MessageGroup> findByCtxObjectTypeAndCtxObjectId(ObjectType ctxObjectType,Integer ctxObjectId);

    MessageGroup findByNameAndCtxObjectTypeAndCtxObjectId(String name,ObjectType ctxObjectType,Integer ctxObjectId);
}
