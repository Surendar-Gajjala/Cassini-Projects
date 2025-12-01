package com.cassinisys.platform.repo.wfm;

import com.cassinisys.platform.model.wfm.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
}
