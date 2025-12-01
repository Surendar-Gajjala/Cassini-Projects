package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.transactions.Issue;
import com.cassinisys.drdo.model.transactions.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer>, QueryDslPredicateExecutor<Issue> {

    @Query("SELECT i from Issue i where i.request.id= :request")
    List<Issue> findByRequest(@Param("request") Integer request);

    @Query("SELECT i from Issue i where i.request.id= :request and i.bomInstance.id= :bomInstance")
    List<Issue> findByRequestAndBomInstance(@Param("request") Integer request, @Param("bomInstance") Integer bomInstance);

    @Query("SELECT i from Issue i where i.bomInstance.id= :bomInstance")
    List<Issue> findByBomInstance(@Param("bomInstance") Integer bomInstance);

    @Query("SELECT i from Issue i where i.bomInstance.id= :bomInstance and i.versity = :versity")
    List<Issue> getIssuesByBomInstanceAndVersity(@Param("bomInstance") Integer bomInstance, @Param("versity") Boolean versity);


    @Query("SELECT i from Issue i where i.bomInstance.id= :bomInstance and (i.status = 'PARTIALLY_RECEIVED' or i.status = 'RECEIVED')")
    List<Issue> getReceivedIssuesByBomInstance(@Param("bomInstance") Integer bomInstance);


    List<Issue> findAllByOrderByCreatedDateDesc();

    List<Issue> findByStatus(IssueStatus issueStatus);

    List<Issue> findByStatusAndVersity(IssueStatus issueStatus, Boolean versity);

    List<Issue> findByVersity(Boolean versity);

}
