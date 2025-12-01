package com.cassinisys.is.repo.procm;
/**
 * The Class is for ProjectBoqRepository
 **/

import com.cassinisys.is.model.procm.ISProjectBoq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectBoqRepository extends
        JpaRepository<ISProjectBoq, Integer> {
    /**
     * The method used to findByProject of ISProjectBoq
     **/
    public List<ISProjectBoq> findByProject(Integer projectId);

    /**
     * The method used to findByIdIn of ISProjectBoq
     **/
    public List<ISProjectBoq> findByIdIn(Iterable<Integer> ids);

    public ISProjectBoq findByNameAndProject(String name, Integer projectI);

}
