package com.cassinisys.is.repo.im;
/**
 * The Class is for IssueRepository
 **/

import com.cassinisys.is.model.im.ISIssue;
import com.cassinisys.is.model.im.IssuePriority;
import com.cassinisys.platform.model.core.ObjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<ISIssue, Integer>, QueryDslPredicateExecutor<ISIssue> {

    @Query(
            "SELECT i FROM ISIssue i WHERE i.targetObjectId= :targetObjectId AND i.targetObjectType= :targetObjectType"
    )
    /**
     * The method used to findByTargetObjectIdAndTargetObjectType of ISStockIssue
     **/
    public Page<ISIssue> findByTargetObjectIdAndTargetObjectType(@Param("targetObjectId") Integer targetObjectId,
                                                                 @Param("targetObjectType") ObjectType targetObjectType, Pageable pageable);

    @Query(
            "SELECT i FROM ISIssue i WHERE i.targetObjectId= :targetObjectId AND i.targetObjectType= :targetObjectType"
    )
    /**
     * The method used to findByTargetObjectIdAndTargetObjectType of ISStockIssue
     **/
    public List<ISIssue> findByObjectIdAndObjectType(@Param("targetObjectId") Integer targetObjectId, @Param("targetObjectType") ObjectType targetObjectType);

    /**
     * The method used to findByIdIn of ISStockIssue
     **/
    public List<ISIssue> findByIdIn(Iterable<Integer> ids);

    @Query(
            "SELECT i FROM ISIssue i WHERE i.targetObjectId= :targetObjectId AND i.targetObjectType= :targetObjectType"
    )
    List<ISIssue> findByTargetObjectIdAndTargetObjectType(@Param("targetObjectId") Integer targetObjectId, @Param("targetObjectType") ObjectType targetObjectType);

    @Query(
            "SELECT count(i) FROM ISIssue i WHERE i.priority= :priority and i.targetObjectId= :targetObjectId"
    )
    Integer findIssuesCountByPriorityAndObjectId(@Param("priority") IssuePriority priority, @Param("targetObjectId") Integer targetObjectId);
}
