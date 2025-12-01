package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOPRevisionStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 09-07-2022.
 */
@Repository
public interface BOPRevisionStatusHistoryRepository extends JpaRepository<MESBOPRevisionStatusHistory, Integer> {
    List<MESBOPRevisionStatusHistory> findByBopRevisionOrderByTimestampDesc(Integer id);
}
