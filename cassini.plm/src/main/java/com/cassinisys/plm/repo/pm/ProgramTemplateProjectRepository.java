package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.ProgramProjectType;
import com.cassinisys.plm.model.pm.ProgramTemplateProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 18-06-2022.
 */
@Repository
public interface ProgramTemplateProjectRepository extends JpaRepository<ProgramTemplateProject, Integer> {
    List<ProgramTemplateProject> findByTemplateAndParentIsNullOrderByIdAsc(Integer template);

    List<ProgramTemplateProject> findByParentOrderByIdAsc(Integer parent);

    @Query("select count (i) from ProgramTemplateProject i where i.template= :template and i.type = 'PROJECT'")
    Integer getTemplateProjectsCount(@Param("template") Integer template);

    @Query("select i.projectTemplate from ProgramTemplateProject i where i.template= :template and i.type = 'PROJECT'")
    List<Integer> getTemplateProjectIds(@Param("template") Integer template);

    @Query("select i.projectTemplate from ProgramTemplateProject i where i.type = 'PROJECT'")
    List<Integer> getProjectIds();

    ProgramTemplateProject findByTemplateAndTypeAndNameEqualsIgnoreCase(Integer template, ProgramProjectType type, String name);

    ProgramTemplateProject findByParentAndProjectTemplate(Integer parent, Integer projectTemplate);
}
