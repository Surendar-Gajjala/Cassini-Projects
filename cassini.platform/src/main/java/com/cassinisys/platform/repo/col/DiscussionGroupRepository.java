package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.DiscussionGroup;
import com.cassinisys.platform.model.core.ObjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lakshmi on 5/25/2016.
 */
@Repository
public interface DiscussionGroupRepository extends JpaRepository<DiscussionGroup, Integer> {

    Page<DiscussionGroup> findByCtxObjectTypeAndCtxObjectId(ObjectType ctxObjectType,Integer ctxObjectId,Pageable pageable);


}
