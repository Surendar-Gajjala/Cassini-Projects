package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.ProjectTemplateMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 19/06/21.
 */

@Repository
public interface ProjectTemplateMemberRepository extends JpaRepository<ProjectTemplateMember, Integer>, QueryDslPredicateExecutor {

    Page<ProjectTemplateMember> findAllByTemplate(Integer template, Pageable pageable);

    List<ProjectTemplateMember> findByIdIn(Iterable<Integer> ids);

    ProjectTemplateMember findByPerson(Integer person);

    ProjectTemplateMember findByTemplateAndPerson(Integer template, Integer person);

    List<ProjectTemplateMember> findByTemplate(Integer template);

    @Query("select i.person from ProjectTemplateMember i where i.template= :template")
    List<Integer> getProjectTemplateMemberIds(@Param("template") Integer template);

}
