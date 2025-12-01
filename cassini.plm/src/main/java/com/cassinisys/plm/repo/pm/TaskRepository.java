package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.pm.ProjectTaskStatus;
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
public interface TaskRepository extends JpaRepository<PLMTask, Integer>, QueryDslPredicateExecutor<PLMTask> {

    List<PLMTask> findByActivity(Integer activity);

    List<PLMTask> findByActivityOrderByCreatedDateAsc(Integer activity);

    List<PLMTask> findByActivityOrderByIdAsc(Integer activity);

    List<PLMTask> findByActivityOrderBySequenceNumberAsc(Integer activity);

    List<PLMTask> findByIdIn(Iterable<Integer> ids);

    Page<PLMTask> findByAssignedTo(Integer assignedTo, Pageable pageable);

    @Query("select t from PLMTask t where t.activity= :activity and t.status != :status")
    List<PLMTask> findByActivityAndNotStatus(@Param("activity") Integer activity,
                                             @Param("status") ProjectTaskStatus status);

    @Query("select t from PLMTask t where t.assignedTo= :assignedTo and t.status != :status")
    Page<PLMTask> getActivityAndNotStatus(@Param("assignedTo") Integer assignedTo,
                                          @Param("status") ProjectTaskStatus status, Pageable pageable);

    PLMTask findByActivityAndNameEqualsIgnoreCase(Integer activityId, String taskName);

    @Query("select count(i) from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id = project.id and project.program is null and i.status =:status")
    Integer getByStatusTypeTask(@Param("status") ProjectTaskStatus status);

    @Query("select count(distinct i) from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id in :ids and (i.status = 'PENDING' or i.status = 'INPROGRESS')")
    Integer getPendingTaskIdsByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select count(distinct i) from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :id and (i.status = 'PENDING' or i.status = 'INPROGRESS')")
    Integer getPendingTaskIdsByProjectId(@Param("id") Integer id);

    @Query("select count(distinct i) from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id in :ids and i.status = 'FINISHED'")
    Integer getFinishedTaskIdsByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select count(distinct i) from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :id and i.status = 'FINISHED'")
    Integer getFinishedTaskIdsByProjectId(@Param("id") Integer id);

    @Query("select distinct i from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id in :ids and (i.status = 'PENDING' or i.status = 'INPROGRESS')")
    List<PLMTask> getPendingTaskByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select distinct i.assignedTo from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id in :ids and (i.status = 'PENDING' or i.status = 'INPROGRESS') and i.assignedTo is not null")
    List<Integer> getUniquePendingAssignedToByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select distinct i.assignedTo from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id in :ids and i.assignedTo is not null")
    List<Integer> getUniqueAssignedToByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select distinct activity.id from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id in :ids and (i.status = 'PENDING' or i.status = 'INPROGRESS') and i.assignedTo is not null")
    List<Integer> getUniqueActivityByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select distinct wbselement.id from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id in :ids and (i.status = 'PENDING' or i.status = 'INPROGRESS') and i.assignedTo is not null")
    List<Integer> getUniqueWbsByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select distinct activity.id from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :id and (i.status = 'PENDING' or i.status = 'INPROGRESS') and i.assignedTo is not null")
    List<Integer> getUniqueActivityByProjectId(@Param("id") Integer id);

    @Query("select distinct wbselement.id from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :id and (i.status = 'PENDING' or i.status = 'INPROGRESS') and i.assignedTo is not null")
    List<Integer> getUniqueWbsByProjectId(@Param("id") Integer id);

    @Query("select distinct i.assignedTo from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :id and (i.status = 'PENDING' or i.status = 'INPROGRESS') and i.assignedTo is not null")
    List<Integer> getUniqueAssignedToByProjectId(@Param("id") Integer id);

    @Query("select distinct i from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id in :ids and (i.status = 'PENDING' or i.status = 'INPROGRESS') and i.assignedTo= :assignedTo")
    List<PLMTask> getPendingTaskByProjectIdsAndAssignedTo(@Param("ids") List<Integer> ids, @Param("assignedTo") Integer assignedTo);

    @Query("select distinct i from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :id and (i.status = 'PENDING' or i.status = 'INPROGRESS') and i.assignedTo= :assignedTo")
    List<PLMTask> getTaskByProjectIdAndAssignedTo(@Param("id") Integer id, @Param("assignedTo") Integer assignedTo);

    @Query("select count (distinct i) from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :id and i.assignedTo= :assignedTo and i.assignedTo is not null")
    Integer getTaskCountByProjectIdAndAssignedTo(@Param("id") Integer id, @Param("assignedTo") Integer assignedTo);

    @Query("select count (distinct i) from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :id and i.assignedTo= :assignedTo and i.assignedTo is not null and i.percentComplete = 100")
    Integer getTaskFinishedCountByProjectIdAndAssignedTo(@Param("id") Integer id, @Param("assignedTo") Integer assignedTo);

    @Query("select distinct i from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :id and (i.status = 'PENDING' or i.status = 'INPROGRESS')")
    List<PLMTask> getPendingTaskByProject(@Param("id") Integer id);

    @Query("select count(i) from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id = project.id and project.program is null and i.status =:status and i.actualFinishDate <= i.plannedFinishDate")
    Integer getFinishedTask(@Param("status") ProjectTaskStatus status);

    @Query("select count(i) from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id = project.id and project.program is null and i.actualFinishDate > i.plannedFinishDate")
    Integer getOverdueTask();

    @Query("SELECT count(t) FROM com.cassinisys.plm.model.pm.PLMTask t, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE t.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :project")
    Integer getProjectTaskCount(@Param("project") Integer project);

    @Query("SELECT count(t) FROM com.cassinisys.plm.model.pm.PLMTask t, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE t.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :project and t.percentComplete = 100")
    Integer getProjectTaskCompletedCount(@Param("project") Integer project);

    @Query("SELECT count(distinct t) FROM com.cassinisys.plm.model.pm.PLMTask t, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE t.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :project and (t.plannedStartDate < :startDate or t.plannedStartDate > :finishDate or t.plannedFinishDate < :startDate or t.plannedFinishDate > :finishDate)")
    Integer getProjectTaskCountByProjectStartFinishDate(@Param("project") Integer project, @Param("startDate") Date startDate, @Param("finishDate") Date finishDate);

    @Query("SELECT count(distinct t) FROM com.cassinisys.plm.model.pm.PLMTask t, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE t.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :project and (t.plannedStartDate > :finishDate or t.plannedFinishDate > :finishDate)")
    Integer getProjectTaskCountByProjectFinishDate(@Param("project") Integer project, @Param("finishDate") Date finishDate);

    @Query("SELECT count(distinct t) FROM com.cassinisys.plm.model.pm.PLMTask t, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE t.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :project and (t.plannedStartDate < :startDate or t.plannedFinishDate < :startDate)")
    Integer getProjectTaskCountByProjectStartDate(@Param("project") Integer project, @Param("startDate") Date startDate);

    @Query("SELECT distinct t.activity FROM com.cassinisys.plm.model.pm.PLMTask t, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE t.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :project")
    List<Integer> getProjectTaskActivityIdsCount(@Param("project") Integer project);

    @Query("SELECT distinct t.activity FROM com.cassinisys.plm.model.pm.PLMTask t, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE t.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id in :projectIds")
    List<Integer> getProjectsTaskActivityIdsCount(@Param("projectIds") List<Integer> projectIds);

    /*@Query(value = "SELECT t.id FROM plm_task t JOIN plm_activity activity ON activity.id = t.activity JOIN plm_wbselement wbselement ON wbselement.id = activity.wbs WHERE wbselement.project= :project", nativeQuery = true)
    Integer getProjectTaskCount(@Param("project") Integer project);*/


    @Query("select count (i) from PLMTask i where i.type.id= :typeId")
    Integer getTaskCountByType(@Param("typeId") Integer typeId);

    @Query("select i.id from PLMTask i where i.type.id= :typeId")
    List<Integer> getObjectIdsByType(@Param("typeId") Integer typeId);

    @Query("select distinct i from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id in :ids")
    List<PLMTask> getTaskByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select distinct i from com.cassinisys.plm.model.pm.PLMTask i, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id in :ids and i.assignedTo= :assignedTo and i.assignedTo is not null")
    List<PLMTask> getTaskByProjectIdsAndAssignedTo(@Param("ids") List<Integer> ids, @Param("assignedTo") Integer assignedTo);

    @Query("SELECT distinct t.id FROM com.cassinisys.plm.model.pm.PLMTask t, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE t.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id= :project")
    List<Integer> getProjectTaskIds(@Param("project") Integer project);

    @Query("SELECT distinct t.id FROM com.cassinisys.plm.model.pm.PLMTask t, com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE t.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id in :projectIds")
    List<Integer> getProjectsTaskIds(@Param("projectIds") List<Integer> projectIds);

    @Query("select t from PLMTask t where t.assignedTo= :assignedTo ")
    List<PLMTask> getAssignToTasks(@Param("assignedTo") Integer assignedTo);

    PLMTask findByIdAndAssignedTo(Integer id,Integer assignTo);

}
