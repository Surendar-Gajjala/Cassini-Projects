package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMQCRAggregateNCR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface QCRAggregateNCRRepository extends JpaRepository<PQMQCRAggregateNCR, Integer> {
    List<PQMQCRAggregateNCR> findByQcr(Integer qcr);

    @Query("select i from PQMQCRAggregateNCR i where i.ncr.id= :id")
    List<PQMQCRAggregateNCR> findByNcr(@Param("id") Integer id);
}
