package com.cassinisys.is.repo.pm;
/**
 * The Class is for ISProjectRoleRepository
 **/

import com.cassinisys.is.model.pm.ISProjectRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISProjectRoleRepository extends JpaRepository<ISProjectRole, Integer>, QueryDslPredicateExecutor<ISProjectRole> {
    /**
     * The method used to findByProject of ISProjectRole
     **/
    List<ISProjectRole> findByProject(Integer projectId);

    /**
     * The method used to findAllByProject of ISProjectRole
     **/
    Page<ISProjectRole> findAllByProject(Integer projectId, Pageable pageable);

    ISProjectRole findByProjectAndRoleIgnoreCase(Integer projectId, String role);

    /**
     * The method used to findByIdAndProject of ISProjectRole
     **/
    ISProjectRole findByIdAndProject(Integer projectId, Integer id);

    /**
     * The method used to findByIdIn of ISProjectRole
     **/
    public List<ISProjectRole> findByIdIn(Iterable<Integer> ids);

    ISProjectRole findById(Integer id);
}


