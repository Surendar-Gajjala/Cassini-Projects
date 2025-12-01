package com.cassinisys.is.repo.pm;
/**
 * The Class is for ISProjectPersonRepository
 **/

import com.cassinisys.is.model.pm.ISProjectPerson;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISProjectPersonRepository extends JpaRepository<ISProjectPerson, Integer>, QueryDslPredicateExecutor<ISProjectPerson> {
    /**
     * The method used to findByProject of ISProjectPerson
     **/
    List<ISProjectPerson> findByProject(Integer projectId);

    /**
     * The method used to findByProjectAndPerson of ISProjectPerson
     **/
    ISProjectPerson findByProjectAndPerson(Integer projectId, Integer personId);

    /**
     * The method used to findAllByProject of ISProjectPerson
     **/
    Page<ISProjectPerson> findByProject(Integer projectId, Pageable pageable);

    List<ISProjectPerson> findByPerson(Integer person);

    Iterable<ISProjectPerson> findByProject(Predicate predicate, Integer projectId);

    @Query("SELECT distinct p.person FROM ISProjectPerson p")
    List<Integer> findPersonIdsByDistint();
}

