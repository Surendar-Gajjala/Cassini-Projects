package com.cassinisys.is.repo.pm;
/**
 * The Class is for WbsRepository
 **/

import com.cassinisys.is.model.pm.ISWbs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WbsRepository extends
        JpaRepository<ISWbs, Integer> {
    /**
     * The method used to findByIdIn of ISWbs
     **/
    public List<ISWbs> findByIdIn(Iterable<Integer> ids);

    /**
     * The method used to findByProjectAndParentIsNullOrderByCreatedDateAsc of ISWbs
     **/
    List<ISWbs> findByProjectAndParentIsNullOrderByCreatedDateAsc(Integer project);

    /**
     * The method used to findByProjectAndParentOrderByCreatedDateAsc of ISWbs
     **/
    List<ISWbs> findByProjectAndParentOrderByCreatedDateAsc(Integer project, Integer parent);

    /**
     * The method used to findByProject of ISWbs
     **/
    public Page<ISWbs> findByProject(Integer id, Pageable pageable);

    public List<ISWbs> findByProject(Integer id);

    /**
     * The method used to findByProject of ISWbs
     **/
    public List<ISWbs> findByProjectAndParentOrderByActualStartDateAsc(Integer project, Integer parent);

    public List<ISWbs> findByProjectAndParentOrderByActualFinishDateDesc(Integer project, Integer parent);

    List<ISWbs> findByParentOrderByCreatedDateAsc(Integer parent);

}


