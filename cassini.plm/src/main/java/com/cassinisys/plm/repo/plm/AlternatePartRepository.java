package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.mro.MROWorkOrderPart;
import com.cassinisys.plm.model.plm.PLMAlternatePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 13-12-2020.
 */
@Repository
public interface AlternatePartRepository extends JpaRepository<PLMAlternatePart, Integer> {
    List<PLMAlternatePart> findByPart(Integer partId);

    @Query("select count (i) from PLMAlternatePart i where i.part= :partId")
    Integer getAlternatePartsCount(@Param("partId") Integer partId);

    PLMAlternatePart findByPartAndReplacementPart(Integer partId, Integer replacementPartId);

    List<PLMAlternatePart> findByPartOrderByModifiedDateDesc(Integer part);
}
