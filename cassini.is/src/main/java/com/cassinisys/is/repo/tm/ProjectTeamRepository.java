package com.cassinisys.is.repo.tm;
/**
 * The Class is for ProjectTeamRepository
 **/

import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.tm.ISProjectTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectTeamRepository extends
        JpaRepository<ISProjectTeam, Integer> {
    /**
     * The method used to findByProjectId of ISProjectTeam
     **/
    public Page<ISProjectTeam> findByProjectId(Integer projectId,
                                               Pageable pageable);

    /**
     * The method used to findByProjectIdAndPersonId of ISProjectTeam
     **/
    public ISProjectTeam findByProjectIdAndPersonId(Integer projectId,
                                                    Integer personId);

    @Query("select pt.project from ISProjectTeam pt where pt.person.id = :personId")
    /**
     * The method used to findProjects of ISProject
     **/
    public Page<ISProject> findProjects(@Param("personId") Integer personId,
                                        Pageable pageable);

}
