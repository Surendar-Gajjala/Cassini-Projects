package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCItemSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 17-09-2020.
 */
@Repository
public interface PGCItemSpecificationRepository extends JpaRepository<PGCItemSpecification, Integer> {
    @Query("select i.specification.id from PGCItemSpecification i where i.item= :item")
    List<Integer> getSpecificationIdsByItem(@Param("item") Integer item);

    @Query("select i from PGCItemSpecification i where i.item= :item")
    List<PGCItemSpecification> getItemSpecifications(@Param("item") Integer item);

    @Query("select count(i) from PGCItemSpecification i where i.item= :item")
    Integer getItemSpecificationCount(@Param("item") Integer item);

}
