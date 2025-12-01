package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementReviewer;
import com.cassinisys.plm.model.req.RequirementApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PLMRequirementReviewerRepository extends JpaRepository<PLMRequirementReviewer, Integer> {

   /* List<PLMRequirementReviewer> findByRequirement(Integer req);

    PLMRequirementReviewer findByRequirementAndReviewerAndApproverTrue(Integer req, Integer person);

    PLMRequirementReviewer findByRequirementAndReviewer(Integer req, Integer person);

    List<PLMRequirementReviewer> findByRequirementAndApproverTrue(Integer req);

    List<PLMRequirementReviewer> findByRequirementAndApproverFalse(Integer req);

    List<PLMRequirementReviewer> findByRequirementAndStatusAndApproverTrue(Integer req, RequirementApprovalStatus status);

    List<PLMRequirementReviewer> findByRequirementAndStatus(Integer req, RequirementApprovalStatus status);*/


 List<PLMRequirementReviewer> findByRequirementVersion(Integer req);

 PLMRequirementReviewer findByRequirementVersionAndReviewerAndApproverTrue(Integer req, Integer person);

 PLMRequirementReviewer findByRequirementVersionAndReviewer(Integer req, Integer person);

 List<PLMRequirementReviewer> findByRequirementVersionAndApproverTrue(Integer req);

 List<PLMRequirementReviewer> findByRequirementVersionAndApproverFalse(Integer req);

 List<PLMRequirementReviewer> findByRequirementVersionAndStatusAndApproverTrue(Integer req, RequirementApprovalStatus status);

 List<PLMRequirementReviewer> findByRequirementVersionAndStatus(Integer req, RequirementApprovalStatus status);

}
