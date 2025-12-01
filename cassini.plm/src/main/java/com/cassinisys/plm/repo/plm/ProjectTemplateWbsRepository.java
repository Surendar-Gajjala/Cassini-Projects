package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.pm.ProjectTemplateWbs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 14-03-2018.
 */
@Repository
public interface ProjectTemplateWbsRepository extends JpaRepository<ProjectTemplateWbs, Integer> {

    ProjectTemplateWbs findByTemplateAndNameEqualsIgnoreCase(Integer templateId, String name);

    List<ProjectTemplateWbs> findByTemplate(Integer templateId);

    List<ProjectTemplateWbs> findByTemplateOrderByCreatedDateAsc(Integer templateId);

    List<ProjectTemplateWbs> findByTemplateOrderBySequenceNumberAsc(Integer templateId);
}
