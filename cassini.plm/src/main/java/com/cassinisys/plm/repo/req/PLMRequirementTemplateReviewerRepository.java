package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementTemplateReviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 25-06-2021.
 */
@Repository
public interface PLMRequirementTemplateReviewerRepository extends JpaRepository<PLMRequirementTemplateReviewer, Integer> {

    List<PLMRequirementTemplateReviewer> findByRequirementTemplate(Integer id);
}
