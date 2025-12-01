package com.cassinisys.drdo.repo.failureList;

import com.cassinisys.drdo.model.failureList.FailureSteps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nageshreddy on 02-01-2019.
 */
@Repository
public interface FailureStepsRepository extends
        JpaRepository<FailureSteps, Integer> {

    List<FailureSteps> findByFailureListOrderBySerialNo(Integer id);

    @Modifying
    @Transactional
    @Query("delete from FailureSteps list where list.failureList = :failureList")
    void deleteByFailureList(@Param("failureList") Integer failureList);

    List<FailureSteps> findByIdIn(Iterable<Integer> ids);

}