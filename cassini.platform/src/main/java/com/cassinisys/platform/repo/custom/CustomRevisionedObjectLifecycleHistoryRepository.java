package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomRevisionedObjectLifecycleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomRevisionedObjectLifecycleHistoryRepository extends
        JpaRepository<CustomRevisionedObjectLifecycleHistory, Integer> {
}
