package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMProgram;
import com.cassinisys.plm.model.pm.PLMProgramResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 02-06-2022.
 */
@Repository
public interface ProgramResourceRepository extends JpaRepository<PLMProgramResource, Integer> {

    List<PLMProgramResource> findByProgramOrderByIdAsc(Integer program);

    @Query("select count (i) from PLMProgramResource i where i.program= :program")
    Integer getProgramResourceCount(@Param("program") Integer program);

    @Query("select i.person from PLMProgramResource i where i.program= :program and i.person is not null")
    List<Integer> getProjectResourceIds(@Param("program") Integer program);

    PLMProgramResource findByProgramAndPerson(Integer id,Integer person);

    List<PLMProgramResource> findByPerson(Integer manager);


}
