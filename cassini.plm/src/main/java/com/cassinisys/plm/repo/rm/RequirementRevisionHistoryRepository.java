package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.RequirementRevisionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequirementRevisionHistoryRepository extends JpaRepository<RequirementRevisionHistory, Integer> {
}