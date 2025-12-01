package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMProgramProject;
import com.cassinisys.plm.model.pm.ProgramProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 15-06-2022.
 */
@Repository
public interface ProgramProjectRepository extends JpaRepository<PLMProgramProject, Integer>, QueryDslPredicateExecutor<PLMProgramProject> {

    List<PLMProgramProject> findByProgramAndParentIsNullOrderByIdAsc(Integer program);

    List<PLMProgramProject> findByParentOrderByIdAsc(Integer parent);

    @Query("select count (i) from PLMProgramProject i where i.program= :program and i.type = 'PROJECT'")
    Integer getProgramProjectsCount(@Param("program") Integer program);

    @Query("select count (i) from PLMProgramProject i where i.parent= :parent")
    Integer getChildCountByParent(@Param("parent") Integer parent);

    @Query("select i.project from PLMProgramProject i where i.program= :program and i.type = 'PROJECT'")
    List<Integer> getProgramProjectIds(@Param("program") Integer program);

    @Query("select i.project from PLMProgramProject i where i.type = 'PROJECT'")
    List<Integer> getProjectIds();

    PLMProgramProject findByProgramAndTypeAndNameEqualsIgnoreCase(Integer program, ProgramProjectType type, String name);

    PLMProgramProject findByParentAndProject(Integer parent, Integer project);

    List<PLMProgramProject> findByIdIn(Iterable<Integer> ids);

    List<PLMProgramProject> findByProgramAndProjectIsNotNull(Integer programId);
}
