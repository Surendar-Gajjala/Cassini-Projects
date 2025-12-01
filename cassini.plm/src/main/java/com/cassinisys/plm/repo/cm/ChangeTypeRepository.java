package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMChangeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 1/3/2016.
 */
@Repository
public interface ChangeTypeRepository extends JpaRepository<PLMChangeType, Integer> {
    List<PLMChangeType> findByParentTypeIsNullOrderByIdAsc();

    List<PLMChangeType> findByParentTypeOrderByIdAsc(Integer parent);

    PLMChangeType findByName(String itemType);

    List<PLMChangeType> findByIdIn(Iterable<Integer> ids);

    List<PLMChangeType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);

    List<PLMChangeType> findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    List<PLMChangeType> findByAutoNumberSourceId(Integer autoNumber);

    List<PLMChangeType> findByChangeReasonTypesId(Integer autoNumber);

    @Query("select count (i) from PLMChangeType i where i.changeReasonTypes.id= :lovId")
    Integer getChangeTypeCountByLov(@Param("lovId") Integer lovId);

    @Query("select count (i) from PLMChangeType i where i.autoNumberSource.id= :autoNumber")
    Integer getChangeTypeAutoNumberValueCount(@Param("autoNumber") Integer autoNumber);
}
