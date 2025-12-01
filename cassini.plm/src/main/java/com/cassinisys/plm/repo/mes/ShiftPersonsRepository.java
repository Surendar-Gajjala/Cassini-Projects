package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESManpowerContact;
import com.cassinisys.plm.model.mes.MESShiftPerson;
import com.cassinisys.plm.model.pm.PLMProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ShiftPersonsRepository extends JpaRepository<MESShiftPerson, Integer> {

    @Query("select i.person from MESShiftPerson i where i.shift= :shift")
    List<Integer> findByPersonIdsByShift(@Param("shift") Integer shift);

    Page<MESShiftPerson> findByShift(Integer shiftId, Pageable pageable);

    MESShiftPerson findByShiftAndPerson(Integer shiftId, Integer person);

    @Query("select count (i) from MESShiftPerson i where i.shift= :shift")
    Integer getShiftPersonsCount(@Param("shift") Integer shift);

    List<MESShiftPerson> findShiftsByPerson(Integer person);

    @Query("select count (i) from MESShiftPerson i where i.person= :person")
    Integer getShiftPersonCountByPerson(@Param("person") Integer person);
}