package com.cassinisys.is.repo.tm;
/**
 * The Class is for ProjectTaskRepository
 **/

import com.cassinisys.is.model.tm.ISProjectTask;
import com.cassinisys.is.model.tm.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTaskRepository extends JpaRepository<ISProjectTask, Integer>, QueryDslPredicateExecutor<ISProjectTask> {
    /**
     * The method used to findByProject of ISProjectTask using pageable
     **/
    public Page<ISProjectTask> findByProject(Integer projectId,
                                             Pageable pageable);

    public List<ISProjectTask> findByProject(Integer projectId);

    /**
     * The method used to findByProject of ISProjectTask
     **/
    public List<ISProjectTask> findByProjectOrderByActualStartDateAsc(Integer projectId);

    public List<ISProjectTask> findByProjectOrderByActualStartDateDesc(Integer projectId);

    /**
     * The method used to findBySite of ISProjectTask
     **/
    public List<ISProjectTask> findBySite(Integer siteId);

    /**
     * The method used to findByIdIn of ISProjectTask
     **/
    public List<ISProjectTask> findByIdIn(Iterable<Integer> ids);

    /**
     * The method used to findBySiteIn of ISProjectTask
     **/
    public List<ISProjectTask> findBySiteIn(Iterable<Integer> ids);

    /**
     * The method used to findByWbsItem of ISProjectTask
     **/
    public List<ISProjectTask> findByWbsItem(Integer wbsItem);

    /*----------------  For Mobile App  -------------------*/

    List<ISProjectTask> getByPerson(Integer person);

    Page<ISProjectTask> findByPerson(Integer person, Pageable pageable);

    Page<ISProjectTask> findByCreatedBy(Integer person, Pageable pageable);

    public List<ISProjectTask> findByProjectAndSubContract(Integer projectId, Boolean flag);

    @Query(
            "SELECT count(i) FROM ISProjectTask i WHERE i.status= :status and i.project= :projectId"
    )
    Integer findCountBystatusAndProject(@Param("status") TaskStatus status, @Param("projectId") Integer projectId);

    @Query(
            "SELECT count(i) FROM ISProjectTask i WHERE i.status= :status and i.site= :siteId"
    )
    Integer findCountByStatusAndSite(@Param("status") TaskStatus status, @Param("siteId") Integer siteId);

}
