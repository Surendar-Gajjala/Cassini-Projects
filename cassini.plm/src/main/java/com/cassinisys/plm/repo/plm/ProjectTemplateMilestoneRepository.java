package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.pm.ProjectTemplateMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 14-03-2018.
 */
@Repository
public interface ProjectTemplateMilestoneRepository extends JpaRepository<ProjectTemplateMilestone, Integer> {

    List<ProjectTemplateMilestone> findByWbs(Integer wbs);

    List<ProjectTemplateMilestone> findByWbsOrderByCreatedDateAsc(Integer wbs);

    List<ProjectTemplateMilestone> findByWbsOrderBySequenceNumberAsc(Integer wbs);

    ProjectTemplateMilestone findByWbsAndNameEqualsIgnoreCase(Integer wbsId, String name);
}
