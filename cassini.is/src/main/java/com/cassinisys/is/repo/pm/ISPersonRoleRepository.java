package com.cassinisys.is.repo.pm;
/**
 * The Class is for ISPersonRoleRepository
 **/

import com.cassinisys.is.model.pm.ISPersonRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISPersonRoleRepository extends JpaRepository<ISPersonRole, Integer>, QueryDslPredicateExecutor<ISPersonRole> {
    /**
     * The method used to findByProjectAndPerson of ISPersonRole
     **/
    List<ISPersonRole> findByProjectAndPerson(Integer projectId, Integer personId);

    List<ISPersonRole> findByProjectAndRole(Integer projectId, Integer roleId);

    /**
     * The method used to findByRowId of ISPersonRole
     **/
    ISPersonRole findByRowId(Integer rowId);

}
