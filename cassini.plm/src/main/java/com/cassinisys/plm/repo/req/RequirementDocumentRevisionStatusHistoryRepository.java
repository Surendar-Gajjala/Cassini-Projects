package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementDocumentRevisionStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 023 23-May -17.
 */
@Repository
public interface RequirementDocumentRevisionStatusHistoryRepository extends JpaRepository<PLMRequirementDocumentRevisionStatusHistory, Integer> {
    List<PLMRequirementDocumentRevisionStatusHistory> findByDocumentRevisionOrderByTimestampDesc(Integer id);
}
