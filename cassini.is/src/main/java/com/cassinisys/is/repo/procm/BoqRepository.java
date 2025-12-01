package com.cassinisys.is.repo.procm;
/**
 * The Class is for BoqRepository
 **/

import com.cassinisys.is.model.procm.ISBoq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoqRepository extends JpaRepository<ISBoq, Integer> {
    /**
     * The method used to findByIdIn with the list of ISBoq
     **/
    public List<ISBoq> findByIdIn(Iterable<Integer> ids);
}
