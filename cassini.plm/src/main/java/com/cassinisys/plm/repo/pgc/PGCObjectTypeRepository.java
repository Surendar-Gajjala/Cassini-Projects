package com.cassinisys.plm.repo.pgc;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.pgc.PGCObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 17-09-2020.
 */
@Repository
public interface PGCObjectTypeRepository extends JpaRepository<PGCObjectType, Integer> {

    List<PGCObjectType> findByIdIn(Iterable<Integer> ids);

    List<PGCObjectType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PGCObjectType> findByObjectTypeAndParentTypeIsNullOrderByCreatedDateAsc(ObjectType type);

    List<PGCObjectType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    @Query("select count (i) from PGCObjectType i where i.autoNumberSource.id= :autoNumber")
    Integer getPGCTypeAutoNumberValueCount(@Param("autoNumber") Integer autoNumber);
}
