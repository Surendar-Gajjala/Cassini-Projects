package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementDocumentTemplateReviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 25-06-2021.
 */
@Repository
public interface PLMRequirementDocumentTemplateReviewerRepository extends JpaRepository<PLMRequirementDocumentTemplateReviewer, Integer> {

    List<PLMRequirementDocumentTemplateReviewer> findByDocumentTemplate(Integer id);
}
