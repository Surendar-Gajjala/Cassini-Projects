package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementDocumentReviewer;
import com.cassinisys.plm.model.req.RequirementApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PLMRequirementDocumentReviewerRepository extends JpaRepository<PLMRequirementDocumentReviewer, Integer> {

    List<PLMRequirementDocumentReviewer> findByRequirementDocumentRevision(Integer req);

    PLMRequirementDocumentReviewer findByRequirementDocumentRevisionAndReviewerAndApproverTrue(Integer req, Integer person);

    PLMRequirementDocumentReviewer findByRequirementDocumentRevisionAndReviewer(Integer req, Integer person);

    List<PLMRequirementDocumentReviewer> findByRequirementDocumentRevisionAndApproverTrue(Integer req);

    List<PLMRequirementDocumentReviewer> findByRequirementDocumentRevisionAndApproverFalse(Integer req);

    List<PLMRequirementDocumentReviewer> findByRequirementDocumentRevisionAndStatusAndApproverTrue(Integer req, RequirementApprovalStatus status);

    List<PLMRequirementDocumentReviewer> findByRequirementDocumentRevisionAndStatus(Integer req, RequirementApprovalStatus status);

}
