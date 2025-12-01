package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMActivity;
import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.pm.ProjectActivityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by swapna on 12/31/17.
 */

@Repository
public interface PLMActivityRepository extends JpaRepository<PLMActivity, Integer>, QueryDslPredicateExecutor<PLMActivity> {

    List<PLMActivity> findByWbs(Integer wbs);

    List<PLMActivity> findByWbsOrderByCreatedDateAsc(Integer wbs);

    List<PLMActivity> findByWbsOrderBySequenceNumberAsc(Integer wbs);

    List<PLMActivity> findByWbsAndSequenceNumberIsNullOrderByCreatedDateAsc(Integer wbs);

    Page<PLMActivity> findByAssignedTo(Integer person, Pageable pageable);

    PLMActivity findByWbsAndNameEqualsIgnoreCase(Integer wbs, String name);

    @Query("select t from PLMActivity t where t.assignedTo= :assignedTo and t.status != :status")
    Page<PLMActivity> findByAssignedToAndNotStatus(@Param("assignedTo") Integer assignedTo, @Param("status") ProjectActivityStatus status, Pageable pageable);

    @Query("select count(i) from com.cassinisys.plm.model.pm.PLMActivity i,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.wbs = wbselement.id and wbselement.project.id = project.id and project.program is null and i.status =:status")
    Integer getByStatusTypeActivity(@Param("status") ProjectActivityStatus status);

    @Query("select count(i) from com.cassinisys.plm.model.pm.PLMActivity i,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.wbs = wbselement.id and wbselement.project.id = project.id and project.program is null and i.status =:status and i.actualFinishDate <= i.plannedFinishDate")
    Integer getFinishedActivity(@Param("status") ProjectActivityStatus status);

    @Query("select count(i) from com.cassinisys.plm.model.pm.PLMActivity i,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.wbs = wbselement.id and wbselement.project.id = project.id and project.program is null and i.actualFinishDate > i.plannedFinishDate")
    Integer getOverdueActivity();

    @Query("select count(i) from PLMActivity i where i.status =:status and i.wbs =:wbs")
    Integer getByStatusTypeActivityAndProject(@Param("wbs") Integer wbs, @Param("status") ProjectActivityStatus status);

    @Query("select count(i) from PLMActivity i where i.status =:status  and i.wbs =:wbs and i.actualFinishDate <= i.plannedFinishDate")
    Integer getFinishedActivityAndProject(@Param("wbs") Integer wbs, @Param("status") ProjectActivityStatus status);

    @Query("select count(i) from PLMActivity i where i.wbs =:wbs and i.actualFinishDate > i.plannedFinishDate")
    Integer getOverdueActivityAndProject(@Param("wbs") Integer wbs);

    @Query("select count(i) from com.cassinisys.plm.model.pm.PLMActivity i,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.wbs = wbselement.id and wbselement.project.id = project.id and project.program is null")
    Integer getActivityCountByProgramProgramIsNull();

    List<PLMActivity> findByIdIn(Iterable<Integer> ids);

    @Query("select count (i) from PLMActivity i where i.type.id= :typeId")
    Integer getActivityCountByType(@Param("typeId") Integer typeId);

    @Query("select i.id from PLMActivity i where i.type.id= :typeId")
    List<Integer> getObjectIdsByType(@Param("typeId") Integer typeId);

    @Query("select distinct activity.assignedTo from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id in :ids and (activity.status = 'PENDING' or activity.status = 'INPROGRESS') and activity.assignedTo is not null")
    List<Integer> getUniquePendingAssignedToByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select distinct activity.assignedTo from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id in :ids and activity.assignedTo is not null")
    List<Integer> getUniqueAssignedToByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select distinct activity.assignedTo from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id= :id and (activity.status = 'PENDING' or activity.status = 'INPROGRESS') and activity.assignedTo is not null")
    List<Integer> getUniqueAssignedToByProjectId(@Param("id") Integer id);

    @Query("select distinct activity from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id in :ids and (activity.status = 'PENDING' or activity.status = 'INPROGRESS') and activity.assignedTo= :assignedTo")
    List<PLMActivity> getPendingActivityByProjectIdsAndAssignedTo(@Param("ids") List<Integer> ids, @Param("assignedTo") Integer assignedTo);

    @Query("select distinct activity from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id= :id and (activity.status = 'PENDING' or activity.status = 'INPROGRESS') and activity.assignedTo= :assignedTo")
    List<PLMActivity> getActivityByProjectIdAndAssignedTo(@Param("id") Integer id, @Param("assignedTo") Integer assignedTo);

    @Query("select count(distinct activity) from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id in :ids and (activity.status = 'PENDING' or activity.status = 'INPROGRESS')")
    Integer getPendingActivityIdsByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select count(distinct activity) from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id in :ids and activity.status = 'FINISHED'")
    Integer getFinishedActivityIdsByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select count(distinct activity) from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id= :id and (activity.status = 'PENDING' or activity.status = 'INPROGRESS')")
    Integer getPendingActivityIdsByProjectId(@Param("id") Integer id);

    @Query("select count(distinct activity) from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id= :id and activity.status = 'FINISHED'")
    Integer getFinishedActivityIdsByProjectId(@Param("id") Integer id);

    @Query("select distinct activity from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id in :ids")
    List<PLMActivity> getActivityByProjectIds(@Param("ids") List<Integer> ids);

    @Query("SELECT count(activity) FROM com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE activity.wbs = wbselement.id and wbselement.project.id= :project and activity.id not in :ids")
    Integer getProjectActivityCountWithoutIds(@Param("project") Integer project, @Param("ids") List<Integer> ids);

    @Query("SELECT count(activity) FROM com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE activity.wbs = wbselement.id and wbselement.project.id= :project and activity.percentComplete = 100 and activity.id not in :ids")
    Integer getProjectActivityCompletedCountWithoutIds(@Param("project") Integer project, @Param("ids") List<Integer> ids);

    @Query("select distinct wbselement.id from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id= :id and (activity.status = 'PENDING' or activity.status = 'INPROGRESS') and activity.assignedTo is not null")
    List<Integer> getUniqueWbsByProjectId(@Param("id") Integer id);

    @Query("select distinct wbselement.id from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id in :ids and (activity.status = 'PENDING' or activity.status = 'INPROGRESS') and activity.assignedTo is not null")
    List<Integer> getUniqueWbsByProjectIds(@Param("ids") List<Integer> ids);

    @Query("SELECT count(activity) FROM com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE activity.wbs = wbselement.id and wbselement.project.id= :project")
    Integer getProjectActivityCount(@Param("project") Integer project);

    @Query("SELECT count(activity) FROM com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE activity.wbs = wbselement.id and wbselement.project.id= :project and activity.percentComplete = 100")
    Integer getProjectActivityCompletedCount(@Param("project") Integer project);

    @Query("select count (distinct activity) from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id= :id and activity.assignedTo= :assignedTo and activity.assignedTo is not null")
    Integer getActivityCountByProjectIdAndAssignedTo(@Param("id") Integer id, @Param("assignedTo") Integer assignedTo);

    @Query("SELECT count (distinct activity) FROM com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE activity.wbs = wbselement.id and wbselement.project.id= :project and (activity.plannedStartDate < :startDate or activity.plannedStartDate > :finishDate or activity.plannedFinishDate < :startDate or activity.plannedFinishDate > :finishDate)")
    Integer getProjectActivityCountByProjectStartFinishDate(@Param("project") Integer project, @Param("startDate") Date startDate, @Param("finishDate") Date finishDate);

    @Query("SELECT count(distinct activity) FROM com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE activity.wbs = wbselement.id and wbselement.project.id= :project and (activity.plannedStartDate < :startDate or activity.plannedFinishDate < :startDate)")
    Integer getProjectActivityCountByProjectStartDate(@Param("project") Integer project, @Param("startDate") Date startDate);

    @Query("SELECT count(distinct activity) FROM com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE activity.wbs = wbselement.id and wbselement.project.id= :project and (activity.plannedStartDate > :finishDate or activity.plannedFinishDate > :finishDate)")
    Integer getProjectActivityCountByProjectFinishDate(@Param("project") Integer project, @Param("finishDate") Date finishDate);

    @Query("select count (distinct activity) from com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where activity.wbs = wbselement.id and wbselement.project.id= :id and activity.assignedTo= :assignedTo and activity.assignedTo is not null and activity.percentComplete = 100")
    Integer getActivityFinishedCountByProjectIdAndAssignedTo(@Param("id") Integer id, @Param("assignedTo") Integer assignedTo);

    @Query("select a from PLMActivity a where a.assignedTo= :assignedTo ")
    List<PLMActivity> getAssignToActivities(@Param("assignedTo") Integer assignedTo);

    PLMActivity findByIdAndAssignedTo(Integer id,Integer assignTo);
}
