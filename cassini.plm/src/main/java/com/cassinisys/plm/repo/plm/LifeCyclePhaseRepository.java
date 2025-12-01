package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by  GSR on 18-05-2017.
 */
@Repository
public interface LifeCyclePhaseRepository extends JpaRepository<PLMLifeCyclePhase, Integer> {
    List<PLMLifeCyclePhase> findByLifeCycle(Integer id);

    PLMLifeCyclePhase findByPhaseType(LifeCyclePhaseType phaseType);

    PLMLifeCyclePhase findByPhaseTypeAndLifeCycle(LifeCyclePhaseType phaseType, Integer lifeCycle);

    List<PLMLifeCyclePhase> findByLifeCycleAndPhaseType(Integer lifeCycle, LifeCyclePhaseType phaseType);

    List<PLMLifeCyclePhase> findByLifeCycleAndPhaseTypeOrderByIdAsc(Integer lifeCycle, LifeCyclePhaseType phaseType);

    PLMLifeCyclePhase findByPhaseAndLifeCycle(String name, Integer lifeCycle);

    PLMLifeCyclePhase findByPhaseAndLifeCycleAndPhaseType(String name, Integer lifeCycle, LifeCyclePhaseType phaseType);

    PLMLifeCyclePhase findByPhase(String phase);

    @Query("SELECT t.id FROM PLMLifeCyclePhase t where t.phase = :phase")
    List<Integer> findIdByPhase(@Param("phase") String phase);

    @Query("SELECT distinct l.phase from PLMLifeCyclePhase l")
    List<PLMLifeCyclePhase> getAllPhases();

    List<PLMLifeCyclePhase> findByLifeCycleOrderByIdAsc(Integer id);

}
