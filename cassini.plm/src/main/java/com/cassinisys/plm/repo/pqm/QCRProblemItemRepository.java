package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMQCRProblemItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface QCRProblemItemRepository extends JpaRepository<PQMQCRProblemItem, Integer> {

    PQMQCRProblemItem findByQcrAndItem(Integer qcr, Integer item);

    PQMQCRProblemItem findByQcrAndItemAndProblemReport(Integer qcr, Integer item, Integer report);

    List<PQMQCRProblemItem> findByQcr(Integer qcr);

    List<PQMQCRProblemItem> findByItem(Integer itemId);

    List<PQMQCRProblemItem> findByQcrAndProblemReport(Integer qcr, Integer problemReport);

    @Query("select distinct count (i.qcr) from PQMQCRProblemItem i where i.item= :itemId")
    Integer getQCRCountByItem(@Param("itemId") Integer itemId);
}
