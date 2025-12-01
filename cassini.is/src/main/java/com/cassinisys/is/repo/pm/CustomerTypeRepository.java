package com.cassinisys.is.repo.pm;
/**
 * The Class is for CustomerTypeRepository
 **/

import com.cassinisys.is.model.pm.ISCustomerType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerTypeRepository extends
        JpaRepository<ISCustomerType, Integer> {

}
