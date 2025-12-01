package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.ProjectTemplateTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 16-05-2019.
 */
@Repository
public interface ProjectTemplateTaskRepository extends JpaRepository<ProjectTemplateTask, Integer> {

    List<ProjectTemplateTask> findByActivity(Integer activity);

    ProjectTemplateTask findByActivityAndNameEqualsIgnoreCase(Integer activity, String name);
}
