package com.cassinisys.is.repo.pm;
/**
 * The Class is for ProjectRepository
 **/

import com.cassinisys.is.model.pm.ISProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ISProject, Integer>, QueryDslPredicateExecutor<ISProject> {
    /**
     * The method used to findByIdIn of ISProject
     **/
    public List<ISProject> findByIdIn(Iterable<Integer> ids);

    public ISProject findByName(String name);

    public List<ISProject> findByPortfolio(Integer portfolio);

    @Query("SELECT i from ISProject i where i.projectOwner = :person")
    List<ISProject> findByProjectOwner(@Param("person") Integer person);
}
