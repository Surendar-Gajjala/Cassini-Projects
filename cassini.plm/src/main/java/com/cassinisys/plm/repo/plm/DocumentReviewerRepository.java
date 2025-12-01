package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.DocumentApprovalStatus;
import com.cassinisys.plm.model.plm.PLMDocumentReviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 06-11-2021.
 */
@Repository
public interface DocumentReviewerRepository extends JpaRepository<PLMDocumentReviewer, Integer>, QueryDslPredicateExecutor<PLMDocumentReviewer> {

    List<PLMDocumentReviewer> findByDocumentOrderByIdDesc(Integer document);

    PLMDocumentReviewer findByDocumentAndReviewer(Integer document, Integer person);

    @Query("select count (i) from PLMDocumentReviewer i where i.document= :documentId")
    Integer getDocumentReviewersCount(@Param("documentId") Integer documentId);

    @Query("select i from PLMDocumentReviewer i where i.document= :documentId")
    List<PLMDocumentReviewer> getDocumentReviewerIds(@Param("documentId") Integer documentId);

    @Query("select count (i) from PLMDocumentReviewer i where i.document= :documentId and i.approver = true and (i.status = 'NONE' or i.status = 'REJECTED')")
    Integer getNoneApprovedCount(@Param("documentId") Integer documentId);

    @Query("select count (i) from PLMDocumentReviewer i where i.document= :documentId and i.approver = true and i.status = 'REJECTED'")
    Integer getRejectedCount(@Param("documentId") Integer documentId);

    @Query("select count (i) from PLMDocumentReviewer i where i.document= :documentId and i.approver = true and i.status = 'APPROVED'")
    Integer getApprovedCount(@Param("documentId") Integer documentId);

    @Query("select i.reviewer from PLMDocumentReviewer i where i.document= :documentId")
    List<Integer> getReviewerIdsByDocument(@Param("documentId") Integer documentId);

    List<PLMDocumentReviewer> findByDocumentAndStatusOrderByIdDesc(Integer document, DocumentApprovalStatus status);
}
