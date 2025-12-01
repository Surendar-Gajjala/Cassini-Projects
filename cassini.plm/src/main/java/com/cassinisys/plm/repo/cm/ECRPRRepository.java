package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMECRPR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 30-12-2020.
 */
@Repository
public interface ECRPRRepository extends JpaRepository<PLMECRPR, Integer> {

    List<PLMECRPR> findByEcr(Integer ecr);

    PLMECRPR findByEcrAndProblemReport(Integer ecr, Integer problemReport);

    @Query("select i.problemReport from PLMECRPR i where i.ecr= :ecrId")
    List<Integer> getPrIdsByEcr(@Param("ecrId") Integer ecrId);
}
