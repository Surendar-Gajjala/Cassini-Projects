package com.cassinisys.is.repo.pm;
/**
 * The Class is for ProjectSiteRepository
 **/

import com.cassinisys.is.model.pm.ISProjectSite;
import com.cassinisys.is.model.tm.ISProjectTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectSiteRepository extends JpaRepository<ISProjectSite, Integer>, QueryDslPredicateExecutor<ISProjectSite> {
    /**
     * The method used to findByProject of ISProjectSite using pageable
     **/
    Page<ISProjectSite> findAllByProject(Integer projectId, Pageable pageable);

  /*  */

    /**
     * The method used to findByProject of ISProjectSite
     **/
    public List<ISProjectSite> findByProject(Integer id);

    /**
     * The method used to findByIdIn of ISProjectTask
     **/
    public List<ISProjectTask> findByIdIn(Iterable<Integer> ids);

    ISProjectSite findByProjectAndNameEqualsIgnoreCase(Integer project, String name);

}
