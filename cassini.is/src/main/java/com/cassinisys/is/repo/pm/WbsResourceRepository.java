package com.cassinisys.is.repo.pm;
/**
 * The Class is for WbsRepository
 **/

import com.cassinisys.is.model.pm.ISWbsResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WbsResourceRepository extends JpaRepository<ISWbsResource, Integer> {
}
