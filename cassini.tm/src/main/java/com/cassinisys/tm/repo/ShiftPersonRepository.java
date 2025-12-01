package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMShift;
import com.cassinisys.tm.model.TMShiftPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Created by Rajabrahmachary on 21-07-2016.
 */
public interface ShiftPersonRepository extends JpaRepository<TMShiftPerson, Integer>,QueryDslPredicateExecutor<TMShiftPerson> {

    TMShiftPerson findByPersonAndShift(Integer personId, Integer shiftId);
    List<TMShiftPerson> findByShift(Integer shiftId);
}
