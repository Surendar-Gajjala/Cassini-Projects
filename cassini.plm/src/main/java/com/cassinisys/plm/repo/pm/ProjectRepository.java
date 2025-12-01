package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.ProjectTaskStatus;
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
public interface ProjectRepository extends JpaRepository<PLMProject, Integer>, QueryDslPredicateExecutor<PLMProject> {

    List<PLMProject> findByProjectManager(Integer manager);

    List<PLMProject> findByIdIn(Iterable<Integer> ids);

    List<PLMProject> findByName(String name);

    PLMProject findByNameEqualsIgnoreCase(String name);

    PLMProject findByNameEqualsIgnoreCaseAndProgramIsNull(String name);

    PLMProject findByNameEqualsIgnoreCaseAndProgram(String name, Integer program);

    List<PLMProject> findAllByOrderByCreatedDateAsc();

    @Query("select count(i) from PLMProject i where i.actualStartDate is null and i.actualFinishDate is null and i.program is null")
    Integer getNotYetStartedProjects();

    @Query("select count(i) from PLMProject i where i.actualStartDate is not null and i.actualFinishDate is null and i.program is null")
    Integer getInProgressProjects();

    @Query("select count(i) from PLMProject i where i.actualFinishDate is not null and i.actualFinishDate < i.plannedFinishDate and i.program is null")
    Integer getFinishedProjects();

    @Query("select count(i) from PLMProject i where i.actualFinishDate is not null and i.actualFinishDate > i.plannedFinishDate and i.program is null")
    Integer getOverdueProjects();

    @Query("select count(i) from PLMProject i")
    Integer getAllProjectsCount();

    @Query("select p.name from com.cassinisys.plm.model.pm.PLMTask t,com.cassinisys.plm.model.pm.PLMActivity a, com.cassinisys.plm.model.pm.PLMWbsElement w, com.cassinisys.plm.model.pm.PLMProject p where p.id = w.project and w.id = a.wbs and a.id = t.activity and t.status != :status and p.program is null GROUP BY (p.id)")
    List<String> getProjectUnfinishedProjects(@Param("status") ProjectTaskStatus status);

    @Query("select count(t) from com.cassinisys.plm.model.pm.PLMTask t,com.cassinisys.plm.model.pm.PLMActivity a, com.cassinisys.plm.model.pm.PLMWbsElement w, com.cassinisys.plm.model.pm.PLMProject p where p.id = w.project and w.id = a.wbs and a.id = t.activity and t.status != :status and p.program is null GROUP BY (p.id)")
    List<Long> getProjectUnfinishedTaskCount(@Param("status") ProjectTaskStatus status);

    @Query("select i.id from PLMProject i where i.makeConversationPrivate = true")
    List<Integer> getProjectIdsWithConversationPrivate();

    @Query("select count (i) from PLMProject i where (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getProjectsCountBySearchQuery(@Param("searchText") String searchText);

    @Query("select count (i) from PLMProject i where i.program is null or i.program is empty")
    Integer getProjectCount();

    @Query("select i from PLMProject i where i.program is null or i.program is empty")
    List<PLMProject> getProjectsByProgramIsNull();

    List<PLMProject> findByProgramIsNull();

    @Query("select count (i) from PLMProject i where i.type.id= :typeId")
    Integer getProjectCountByType(@Param("typeId") Integer typeId);

    @Query("select i.id from PLMProject i where i.type.id= :typeId")
    List<Integer> getObjectIdsByType(@Param("typeId") Integer typeId);

    @Query("select distinct i.projectManager from PLMProject i where i.program is null")
    List<Integer> getProjectManagerIds();

    @Query("select distinct i.projectManager from PLMProject i where i.program= :program")
    List<Integer> getProjectManagerIdsByProgram(@Param("program") Integer program);
}
