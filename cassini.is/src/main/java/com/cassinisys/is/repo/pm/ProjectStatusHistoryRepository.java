package com.cassinisys.is.repo.pm;
/**
 * The Class is for ProjectStatusHistoryRepository
 **/

import com.cassinisys.is.model.pm.ISProjectStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectStatusHistoryRepository extends
        JpaRepository<ISProjectStatusHistory, Integer> {

}
