package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.Tag;
import com.cassinisys.platform.model.core.ObjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author reddy
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Integer>, QueryDslPredicateExecutor<Tag> {
    List<Tag> findByIdIn(Iterable<Integer> ids);

    List<Tag> findByObjectOrderByIdDesc(Integer object);

    @Query("Select count(i) from Tag i where i.object= :objectId")
    Integer getObjectTagCount(@Param("objectId") Integer objectId);

    Page<Tag> findByLabelContainingIgnoreCase(String name, Pageable pageable);

    Tag findByObjectAndLabelEqualsIgnoreCase(Integer object, String name);

    @Query("SELECT DISTINCT t.objectType FROM Tag t order by t.objectType asc")
    List<ObjectType> getUniqueObjectTypes();
}
