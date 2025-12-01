package com.cassinisys.is.repo.procm;
/**
 * The Class is for RfqRepository
 **/

import com.cassinisys.is.model.procm.ISRfq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RfqRepository extends JpaRepository<ISRfq, Integer> {
    /**
     * The method used to findByIdIn of ISRfq
     **/
    public List<ISRfq> findByIdIn(Iterable<Integer> ids);
}
