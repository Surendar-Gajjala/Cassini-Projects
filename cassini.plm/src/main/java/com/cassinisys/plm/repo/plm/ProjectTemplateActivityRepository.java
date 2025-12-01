package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.pm.ProjectTemplateActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 14-03-2018.
 */
@Repository
public interface ProjectTemplateActivityRepository extends JpaRepository<ProjectTemplateActivity, Integer> {

    List<ProjectTemplateActivity> findByWbs(Integer wbs);

    List<ProjectTemplateActivity> findByWbsOrderByCreatedDateAsc(Integer wbs);

    List<ProjectTemplateActivity> findByWbsOrderBySequenceNumberAsc(Integer wbs);

    ProjectTemplateActivity findByWbsAndNameEqualsIgnoreCase(Integer wbsId, String name);
}
