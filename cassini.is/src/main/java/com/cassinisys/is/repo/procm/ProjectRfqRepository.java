package com.cassinisys.is.repo.procm;
/**
 * The Class is for ProjectRfqRepository
 **/

import com.cassinisys.is.model.procm.ISProjectRfq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRfqRepository extends
        JpaRepository<ISProjectRfq, Integer> {
    /**
     * The method used to findByIdIn of ISProjectRfq
     **/
    public List<ISProjectRfq> findByIdIn(Iterable<Integer> ids);

}
