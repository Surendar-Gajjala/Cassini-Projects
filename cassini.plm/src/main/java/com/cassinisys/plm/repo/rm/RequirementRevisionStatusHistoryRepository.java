package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.RequirementRevisionStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementRevisionStatusHistoryRepository extends JpaRepository<RequirementRevisionStatusHistory, Integer> {
    List<RequirementRevisionStatusHistory> findByRequirementOrderByTimeStampDesc(Integer reqId);
}