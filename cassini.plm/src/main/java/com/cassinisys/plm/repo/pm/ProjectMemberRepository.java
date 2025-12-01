package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 12/31/17.
 */

@Repository
public interface ProjectMemberRepository extends JpaRepository<PLMProjectMember, Integer>, QueryDslPredicateExecutor {

    Page<PLMProjectMember> findAllByProject(Integer project, Pageable pageable);

    List<PLMProjectMember> findByIdIn(Iterable<Integer> ids);

    List<PLMProjectMember> findByPerson(Integer person);

    PLMProjectMember findByProjectAndPerson(Integer project, Integer person);

    List<PLMProjectMember> findByProject(Integer project);

    @Query("select i.person from PLMProjectMember i where i.project= :project")
    List<Integer> getProjectMemberIds(@Param("project") Integer project);

    @Query("select distinct i.project from com.cassinisys.plm.model.pm.PLMProjectMember i,com.cassinisys.plm.model.pm.PLMProject project where i.person != :personId and project.makeConversationPrivate = true")
    List<Integer> getProjectIdsByPersonNotInTeam(@Param("personId") Integer personId);
}
