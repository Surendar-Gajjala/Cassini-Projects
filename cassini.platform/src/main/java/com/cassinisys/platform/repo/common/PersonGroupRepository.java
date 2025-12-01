package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonGroupRepository extends JpaRepository<PersonGroup, Integer>, QueryDslPredicateExecutor<PersonGroup> {
    /*  List<PersonGroup> findByParent(Integer parent);
      List<PersonGroup> findByParentIsNull();*/
    List<PersonGroup> findByGroupIdIn(Iterable<Integer> ids);

    PersonGroup findByName(String name);

    List<PersonGroup> findAllByOrderByGroupIdAsc();

    List<PersonGroup> findByParentIsNullOrderByGroupIdAsc();

    List<PersonGroup> findByParentIsNotNullOrderByGroupIdAsc();

    @Query(
            "SELECT i FROM PersonGroup i WHERE i.parent is null ORDER BY i.name ASC"
    )
    List<PersonGroup> findByParentIsNull();

    @Query(
            "SELECT i FROM PersonGroup i WHERE i.parent= :parent ORDER BY i.name ASC"
    )
    List<PersonGroup> findByParent(@Param("parent") Integer parent);

    List<PersonGroup> findByParentOrderByGroupIdAsc(Integer groupId);

    List<PersonGroup> findByGroupMember(Integer person);

    PersonGroup findByNameEqualsIgnoreCaseAndParent(String name, Integer parent);

    PersonGroup findByNameEqualsIgnoreCaseAndParentIsNull(String name);
}

