package com.cassinisys.is.repo.pm;
/**
 * The Class is for CustomerRepository
 **/

import com.cassinisys.is.model.pm.ISCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<ISCustomer, Integer> {

    /**
     * The method used to findByIdIn of ISCustomer
     **/
    public List<ISCustomer> findByIdIn(Iterable<Integer> ids);
}
