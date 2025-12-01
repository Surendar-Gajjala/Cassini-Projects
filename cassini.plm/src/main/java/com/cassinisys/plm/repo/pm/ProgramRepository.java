package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMProgram;
import com.cassinisys.plm.model.pm.PLMProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 02-06-2022.
 */

@Repository
public interface ProgramRepository extends JpaRepository<PLMProgram, Integer>, QueryDslPredicateExecutor<PLMProgram> {

    List<PLMProgram> findByIdIn(Iterable<Integer> ids);

    PLMProgram findByNameEqualsIgnoreCase(String name);

    List<PLMProgram> findAllByOrderByIdAsc();

    @Query("select count (i) from PLMProgram i where i.type.id= :typeId")
    Integer getProgramCountByType(@Param("typeId") Integer typeId);

    @Query("select i.id from PLMProgram i where i.type.id= :typeId")
    List<Integer> getObjectIdsByType(@Param("typeId") Integer typeId);

    @Query("select distinct i.programManager from PLMProgram i")
    List<Integer> getProgramManagerIds();

    List<PLMProgram> findByProgramManager(Integer manager);

}
