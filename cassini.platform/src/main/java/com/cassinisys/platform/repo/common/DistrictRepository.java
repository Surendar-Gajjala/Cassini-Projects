package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author reddy
 */
@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {


}
