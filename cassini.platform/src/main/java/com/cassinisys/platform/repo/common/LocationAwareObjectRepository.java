package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.LocationAwareObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationAwareObjectRepository extends JpaRepository<LocationAwareObject, Integer> {

}