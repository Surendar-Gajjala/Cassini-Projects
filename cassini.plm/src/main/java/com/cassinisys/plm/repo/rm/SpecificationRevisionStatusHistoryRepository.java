package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.SpecificationRevisionStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecificationRevisionStatusHistoryRepository extends JpaRepository<SpecificationRevisionStatusHistory, Integer> {
    List<SpecificationRevisionStatusHistory> findBySpecificationOrderByTimeStampDesc(Integer reqId);
}