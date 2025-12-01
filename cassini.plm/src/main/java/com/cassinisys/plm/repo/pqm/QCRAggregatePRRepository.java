package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMQCRAggregatePR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface QCRAggregatePRRepository extends JpaRepository<PQMQCRAggregatePR, Integer> {
    List<PQMQCRAggregatePR> findByQcr(Integer qcr);

    @Query("select i from PQMQCRAggregatePR i where i.pr.id= :id")
    List<PQMQCRAggregatePR> findByPr(@Param("id") Integer id);
}
