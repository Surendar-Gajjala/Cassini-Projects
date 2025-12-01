package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMSubstitutePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 13-12-2020.
 */
@Repository
public interface SubstitutePartRepository extends JpaRepository<PLMSubstitutePart, Integer> {
    List<PLMSubstitutePart> findByPart(Integer partId);

    List<PLMSubstitutePart> findByPartAndParent(Integer part, Integer parent);

    @Query("select count (i) from PLMSubstitutePart i where i.part= :partId")
    Integer getSubstitutePartsCount(@Param("partId") Integer partId);

    PLMSubstitutePart findByPartAndReplacementPart(Integer part, Integer replacementPart);
}
