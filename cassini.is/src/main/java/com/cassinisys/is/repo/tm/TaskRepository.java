package com.cassinisys.is.repo.tm;
/**
 * The Class is for TaskRepository
 **/

import com.cassinisys.is.model.tm.ISTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<ISTask, Integer>, QueryDslPredicateExecutor<ISTask> {
    /**
     * The method used to findByWbsItem of ISTask
     **/
    public Page<ISTask> findByWbsItem(Integer wbsItem, Pageable pageable);

    public List<ISTask> findByWbsItemOrderByActualStartDateAsc(Integer wbsItem);

    /**
     * The method used to findByIdIn of ISTask
     **/
    public List<ISTask> findByIdIn(Iterable<Integer> var1);

    public List<ISTask> findByPerson(Integer person);
}
