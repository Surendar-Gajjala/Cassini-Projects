package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementVersionStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 023 23-May -17.
 */
@Repository
public interface RequirementVersionStatusHistoryRepository extends JpaRepository<PLMRequirementVersionStatusHistory, Integer> {
    List<PLMRequirementVersionStatusHistory> findByRequirementVersionOrderByTimestampDesc(Integer id);
}
