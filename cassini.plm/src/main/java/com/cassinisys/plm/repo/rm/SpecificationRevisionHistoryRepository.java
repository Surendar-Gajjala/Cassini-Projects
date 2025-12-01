package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.SpecificationRevisionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificationRevisionHistoryRepository extends JpaRepository<SpecificationRevisionHistory, Integer> {
}