package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.ProgramTemplateResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 18-06-2022.
 */
@Repository
public interface ProgramTemplateResourceRepository extends JpaRepository<ProgramTemplateResource, Integer> {
    List<ProgramTemplateResource> findByTemplate(Integer program);

    @Query("select count (i) from ProgramTemplateResource i where i.template= :template")
    Integer getResourceCountByTemplate(@Param("template") Integer template);
}
