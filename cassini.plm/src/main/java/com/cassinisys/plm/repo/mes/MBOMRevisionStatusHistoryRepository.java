package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESMBOMRevisionStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 09-07-2022.
 */
@Repository
public interface MBOMRevisionStatusHistoryRepository extends JpaRepository<MESMBOMRevisionStatusHistory, Integer> {
    List<MESMBOMRevisionStatusHistory> findByMbomRevisionOrderByTimestampDesc(Integer id);
}
