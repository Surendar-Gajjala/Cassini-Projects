package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 21-07-2016.
 */
@Repository
public interface ShiftRepository extends JpaRepository<TMShift, Integer>, QueryDslPredicateExecutor<TMShift> {
    public List<TMShift> findByShiftIdIn(Iterable<Integer> ids);
}
