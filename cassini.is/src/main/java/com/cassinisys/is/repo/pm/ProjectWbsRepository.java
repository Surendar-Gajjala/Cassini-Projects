package com.cassinisys.is.repo.pm;
/**
 * The Class is for ProjectWbsRepository
 **/

import com.cassinisys.is.model.pm.ISProjectWbs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectWbsRepository extends
        JpaRepository<ISProjectWbs, Integer> {
    /**
     * The method used to findByProjectAndParentIsNullOrderByCreatedDateAsc of ISProjectWbs
     **/
    public List<ISProjectWbs> findByProjectAndParentIsNullOrderByCreatedDateAsc(Integer projectId);

    ISProjectWbs findByNameAndProjectAndParentIsNull(String name, Integer projectId);

    public List<ISProjectWbs> findByProjectOrderByIdAsc(Integer projectId);
    /**
     * The method used to findByParentOrderByCreatedDateAsc of ISProjectWbs
     **/
    public List<ISProjectWbs> findByParentOrderByCreatedDateAsc(Integer parentId);

    /**
     * The method used to findByIdIn of ISProjectWbs
     **/
    public List<ISProjectWbs> findByIdIn(Iterable<Integer> ids);

    ISProjectWbs findByProjectAndNameEqualsIgnoreCaseAndParent(Integer projectId, String wbsName, Integer parentId);

    ISProjectWbs findByProjectAndNameEqualsIgnoreCase(Integer projectId, String wbsName);

    List<ISProjectWbs> findByProject(Integer projectId);
}
