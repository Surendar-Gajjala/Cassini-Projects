package com.cassinisys.is.repo.pm;

import com.cassinisys.is.model.pm.ISProjectPersonOrganization;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 14-05-2019.
 */
@Repository
public interface ProjectPersonOrganizationRepository extends JpaRepository<ISProjectPersonOrganization, Integer> {
    List<ISProjectPersonOrganization> findByProject(Integer projectId);

    /**
     * The method used to findByProjectAndPerson of ISProjectPerson
     **/
    ISProjectPersonOrganization findByProjectAndPerson(Integer projectId, Integer personId);

    ISProjectPersonOrganization findByProjectAndPersonAndRole(Integer projectId, Integer person, Integer role);

    /**
     * The method used to findAllByProject of ISProjectPerson
     **/
    Page<ISProjectPersonOrganization> findByProject(Integer projectId, Pageable pageable);

    List<ISProjectPersonOrganization> findByPerson(Integer person);

    Iterable<ISProjectPersonOrganization> findByProject(Predicate predicate, Integer projectId);

    ISProjectPersonOrganization findByProjectAndNode(Integer projectId, Integer node);
}
