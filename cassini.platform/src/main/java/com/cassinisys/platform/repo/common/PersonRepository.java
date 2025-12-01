package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 7/18/15.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>, QueryDslPredicateExecutor<Person> {
    List<Person> findByIdIn(Iterable<Integer> ids);

    Page<Person> findByPersonType(Integer personType, Pageable pageable);

    List<Person> findAllByPersonType(Integer personType);

    Person findByEmailIgnoreCase(String email);

    Person findByPhoneMobile(String phoneMobile);

    Person findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    Person getByFirstName(String firstName);

    List<Person> findByFirstNameIgnoreCaseAndLastNameIsNull(String firstName);

    List<Person> findByFirstName(String name);
  /*  List<Person> findAll();*/

    @Query("select count (i) from Person i where i.id not in :ids")
    Integer getPersonCountByIdNotIn(@Param("ids") Iterable<Integer> ids);

    @Query("select count (i) from Person i where i.defaultGroup= :id")
    Integer getUsersCountByGroup(@Param("id") Integer id);

}
