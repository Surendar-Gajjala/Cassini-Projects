package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.bom.BomItemInstance;
import com.cassinisys.drdo.model.transactions.Issue;
import com.cassinisys.drdo.model.transactions.IssueItem;
import com.cassinisys.drdo.model.transactions.IssueItemStatus;
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
public interface IssueItemRepository extends JpaRepository<IssueItem, Integer>, QueryDslPredicateExecutor<IssueItem> {

    List<IssueItem> findByRequestItem(Integer requestItem);

    @Query("SELECT i from IssueItem i where i.requestItem= :requestItem and (i.status = 'PENDING' or i.status = 'P_APPROVED' or i.status = 'APPROVED' or i.status = 'RECEIVED')")
    List<IssueItem> getApprovedItemsByRequest(@Param("requestItem") Integer requestItem);

    @Query("SELECT i from IssueItem i where i.requestItem= :requestItem and i.status = 'RECEIVED'")
    List<IssueItem> getReceivedItemsByRequest(@Param("requestItem") Integer requestItem);

    List<IssueItem> findByIssue(Issue issue);

    @Query("SELECT i FROM IssueItem i WHERE i.issue.id = :issueId and i.issue.versity = :versity")
    List<IssueItem> getIssueItemByIssuedAndVersity(@Param("issueId") Integer issuedId, @Param("versity") Boolean versity);

    @Query("SELECT i from IssueItem i where i.issue.id= :issue and i.bomItemInstance.bomInstanceItem= :bomInstanceItem")
    List<IssueItem> findByIssueAndBomItemInstance(@Param("issue") Integer issue, @Param("bomInstanceItem") Integer bomInstanceItem);


    @Query("SELECT i from IssueItem i where i.issue.id= :issue and i.bomItemInstance.bomInstanceItem= :bomInstanceItem and (i.status = 'APPROVED' or i.status = 'P_APPROVED')")
    List<IssueItem> getApprovedItemsByIssueAndBomItemInstance(@Param("issue") Integer issue, @Param("bomInstanceItem") Integer bomInstanceItem);

    @Query("SELECT i from IssueItem i where i.issue.id= :issue and i.bomItemInstance.bomInstanceItem= :bomInstanceItem and i.status = 'RECEIVED'")
    List<IssueItem> getReceivedItemsByIssueAndBomItemInstance(@Param("issue") Integer issue, @Param("bomInstanceItem") Integer bomInstanceItem);

    @Query("SELECT i from IssueItem i where i.bomItemInstance.bomInstanceItem= :bomInstanceItem")
    List<IssueItem> getIssuedBomInstanceItem(@Param("bomInstanceItem") Integer bomInstanceItem);

    @Query("SELECT i from IssueItem i where i.bomItemInstance.bomInstanceItem= :bomInstanceItem and (i.status = 'P_APPROVED' or i.status = 'APPROVED')")
    List<IssueItem> getApprovedItemsByBomInstanceItem(@Param("bomInstanceItem") Integer bomInstanceItem);

    @Query("SELECT i from IssueItem i where i.bomItemInstance.bomInstanceItem= :bomInstanceItem and i.status = 'RECEIVED'")
    List<IssueItem> getReceivedItemsByBomInstanceItem(@Param("bomInstanceItem") Integer bomInstanceItem);

    @Query("SELECT i from IssueItem i where i.bomItemInstance.id= :bomItemInstance")
    IssueItem getByBomItemInstance(@Param("bomItemInstance") Integer bomItemInstance);

    List<IssueItem> findByBomItemInstance(BomItemInstance bomItemInstance);

    List<IssueItem> findByIssueAndStatus(Issue issue, IssueItemStatus issueItemStatus);

    List<IssueItem> findByIssueAndRequestItem(Issue issue, Integer reqItem);
}
