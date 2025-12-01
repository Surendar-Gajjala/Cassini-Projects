package com.cassinisys.is.repo.pm;
/**
 * The Class is for TaskResourceRepository
 **/

import com.cassinisys.is.model.pm.ISProjectResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskResourceRepository extends JpaRepository<ISProjectResource, Integer> {
}
