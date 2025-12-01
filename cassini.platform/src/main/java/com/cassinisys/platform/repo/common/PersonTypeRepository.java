package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.PersonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author reddy
 */
@Repository
public interface PersonTypeRepository extends JpaRepository<PersonType, Integer> {
    List<PersonType> findByIdIn(Iterable<Integer> ids);

    PersonType findById(Integer id);

    PersonType findByName(String name);

    @Query("SELECT type.id FROM PersonType type WHERE type.description IN :types")
    List<Integer> getTypeIdsByNames(@Param("types") String[] types);
}
