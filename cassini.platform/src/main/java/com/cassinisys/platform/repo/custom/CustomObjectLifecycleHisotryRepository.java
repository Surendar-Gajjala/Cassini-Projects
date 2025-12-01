package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomObjectLifecycleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomObjectLifecycleHisotryRepository extends JpaRepository<CustomObjectLifecycleHistory, Integer> {
}
