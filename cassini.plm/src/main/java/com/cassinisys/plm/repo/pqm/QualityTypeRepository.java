package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMQualityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface QualityTypeRepository extends JpaRepository<PQMQualityType, Integer> {
    List<PQMQualityType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PQMQualityType> findByParentTypeIsNullOrderByIdAsc();

    List<PQMQualityType> findByParentTypeOrderByCreatedDateAsc(Integer type);

    List<PQMQualityType> findByParentTypeOrderByIdAsc(Integer type);

    List<PQMQualityType> findByNumberSourceId(Integer autoNumber);

    List<PQMQualityType> findByIdIn(Iterable<Integer> ids);

    @Query("select count (i) from PQMQualityType i where i.numberSource.id= :autoNumber")
    Integer getQualityTypeAutoNumberValueCount(@Param("autoNumber") Integer autoNumber);
}
