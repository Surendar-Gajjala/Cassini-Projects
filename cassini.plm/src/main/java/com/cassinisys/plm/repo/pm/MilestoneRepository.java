package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMMilestone;
import com.cassinisys.plm.model.pm.PLMTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by swapna on 12/31/17.
 */

@Repository
public interface MilestoneRepository extends JpaRepository<PLMMilestone, Integer> {

    List<PLMMilestone> findByWbs(Integer wbs);

    List<PLMMilestone> findByWbsOrderByCreatedDateAsc(Integer wbs);

    List<PLMMilestone> findByWbsOrderBySequenceNumberAsc(Integer wbs);

    List<PLMMilestone> findByAssignedTo(Integer person);

    List<PLMMilestone> findByIdIn(Iterable<Integer> ids);

    PLMMilestone findByWbsAndNameEqualsIgnoreCase(Integer wbs, String name);

    @Query("select distinct milestone.assignedTo from com.cassinisys.plm.model.pm.PLMMilestone milestone,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where milestone.wbs = wbselement.id and wbselement.project.id in :ids and milestone.assignedTo is not null")
    List<Integer> getUniqueAssignedToByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select distinct milestone from com.cassinisys.plm.model.pm.PLMMilestone milestone,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where milestone.wbs = wbselement.id and wbselement.project.id in :ids")
    List<PLMMilestone> getMilestoneByProjectIds(@Param("ids") List<Integer> ids);

    @Query("select distinct milestone from com.cassinisys.plm.model.pm.PLMMilestone milestone,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where milestone.wbs = wbselement.id and wbselement.project.id in :ids and milestone.assignedTo= :assignedTo and milestone.assignedTo is not null")
    List<PLMMilestone> getMilestoneByProjectIdsAndAssignedTo(@Param("ids") List<Integer> ids, @Param("assignedTo") Integer assignedTo);

    @Query("SELECT count(distinct milestone) FROM com.cassinisys.plm.model.pm.PLMMilestone milestone,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE milestone.wbs = wbselement.id and wbselement.project.id= :project and (milestone.plannedFinishDate < :startDate or milestone.plannedFinishDate > :finishDate)")
    Integer getProjectMilestoneCountByProjectStartFinishDate(@Param("project") Integer project, @Param("startDate") Date startDate, @Param("finishDate") Date finishDate);

    @Query("SELECT count(distinct milestone) FROM com.cassinisys.plm.model.pm.PLMMilestone milestone,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE milestone.wbs = wbselement.id and wbselement.project.id= :project and (milestone.plannedFinishDate < :startDate)")
    Integer getProjectMilestoneCountByProjectStartDate(@Param("project") Integer project, @Param("startDate") Date startDate);

    @Query("SELECT count(distinct milestone) FROM com.cassinisys.plm.model.pm.PLMMilestone milestone,com.cassinisys.plm.model.pm.PLMWbsElement wbselement" +
            " WHERE milestone.wbs = wbselement.id and wbselement.project.id= :project and (milestone.plannedFinishDate > :finishDate)")
    Integer getProjectMilestoneCountByProjectFinishDate(@Param("project") Integer project, @Param("finishDate") Date finishDate);

    @Query("select count (distinct milestone) from com.cassinisys.plm.model.pm.PLMMilestone milestone,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where milestone.wbs = wbselement.id and wbselement.project.id= :id and milestone.assignedTo= :assignedTo and milestone.assignedTo is not null and milestone.status = 'FINISHED'")
    Integer getMilestoneFinishedCountByProjectIdAndAssignedTo(@Param("id") Integer id, @Param("assignedTo") Integer assignedTo);

    @Query("select count (distinct milestone) from com.cassinisys.plm.model.pm.PLMActivity milestone,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where milestone.wbs = wbselement.id and wbselement.project.id= :id and milestone.assignedTo= :assignedTo and milestone.assignedTo is not null")
    Integer getMilestoneCountByProjectIdAndAssignedTo(@Param("id") Integer id, @Param("assignedTo") Integer assignedTo);

    PLMMilestone findByIdAndAssignedTo(Integer id, Integer assignTo);


}
