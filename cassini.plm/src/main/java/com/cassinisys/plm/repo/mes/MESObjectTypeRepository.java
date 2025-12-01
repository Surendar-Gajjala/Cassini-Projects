package com.cassinisys.plm.repo.mes;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.mes.MESObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Repository
public interface MESObjectTypeRepository extends JpaRepository<MESObjectType,Integer>{

    List<MESObjectType> findByIdIn(Iterable<Integer> ids);

    List<MESObjectType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESObjectType> findByObjectTypeAndParentTypeIsNullOrderByCreatedDateAsc(ObjectType type);

    List<MESObjectType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    @Query("select count (i) from MESObjectType i where i.autoNumberSource.id= :autoNumber")
    Integer getMesTypeAutoNumberValueCount(@Param("autoNumber") Integer autoNumber);
}
