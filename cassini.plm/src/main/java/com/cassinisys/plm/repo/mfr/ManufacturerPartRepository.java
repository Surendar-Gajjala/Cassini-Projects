package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartType;
import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Home on 4/25/2016.
 */
@Repository
public interface ManufacturerPartRepository extends JpaRepository<PLMManufacturerPart, Integer>, QueryDslPredicateExecutor<PLMManufacturerPart> {

    List<PLMManufacturerPart> findByManufacturerOrderByModifiedDateDesc(Integer manufacturer);
/*
    @Query(
            "SELECT i FROM PLMManufacturerPart i WHERE i.itemType.id IN :typeIds AND LOWER(i.status) = LOWER('ACTIVE')"
    )*/
  /*  List<PLMManufacturerPart> findByItemTypeIds(@Param("typeIds") List<Integer> typeId);*/

    List<PLMManufacturerPart> findByIdIn(List<Integer> ids);

    PLMManufacturerPart findByPartNumber(String partNumber);

    PLMManufacturerPart findByPartNumberEqualsIgnoreCase(String number);

    PLMManufacturerPart findByPartNumberAndMfrPartType(String partNumber, PLMManufacturerPartType manufacturerPartType);

    PLMManufacturerPart findByManufacturerAndPartNumberAndMfrPartType(Integer mfr, String partNumber, PLMManufacturerPartType manufacturerPartType);

    @Query(
            "SELECT i FROM PLMManufacturerPart i WHERE i.mfrPartType.id IN :typeIds"
    )
    Page<PLMManufacturerPart> getByMfrPartTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    List<PLMManufacturerPart> findByMfrPartType(PLMManufacturerPartType partType);

    @Query("select i from PLMManufacturerPart i where i.mfrPartType.id= :partType and i.lifeCyclePhase.phaseType= :phaseType")
    List<PLMManufacturerPart> findBYMfrPartTypeAndLifeCyclePhase(@Param("partType") Integer partType, @Param("phaseType") LifeCyclePhaseType phaseType);

    @Query("SELECT count(i) FROM PLMManufacturerPart i where i.lifeCyclePhase.phaseType= :phaseType")
    Integer getUnqualifiedParts(@Param("phaseType") LifeCyclePhaseType phaseType);

    @Query("SELECT count(i) FROM PLMManufacturerPart i where i.lifeCyclePhase.phaseType= :phaseType")
    Integer getQualifiedParts(@Param("phaseType") LifeCyclePhaseType phaseType);

    @Query("SELECT count(i) FROM PLMManufacturerPart i where i.lifeCyclePhase.phaseType= :phaseType")
    Integer getDisqualifiedParts(@Param("phaseType") LifeCyclePhaseType phaseType);

    @Query("SELECT count(i) FROM PLMManufacturerPart i where i.lifeCyclePhase.phaseType= :phaseType")
    Integer getObsoleteParts(@Param("phaseType") LifeCyclePhaseType phaseType);

    @Query(value = "select i.manufacturer,count(i.manufacturer) from plm_manufacturerpart i group by i.manufacturer order by count (i.manufacturer) desc LIMIT 10", nativeQuery = true)
    List<Object[]> getManufacturerParts();

    @Query("SELECT count (i) FROM PLMManufacturerPart i")
    Integer getMfrPartsCount();

    @Query("select count (i) from PLMManufacturerPart i where (LOWER(CAST(i.partNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.partName as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.mfrPartType.name as text))) LIKE '%' || :searchText || '%' or i.manufacturer in :mfrIds")
    Integer getMfrPartsCountBySearchQueryAndMfrs(@Param("searchText") String searchText, @Param("mfrIds") Iterable<Integer> mfrIds);

    @Query("select count (i) from PLMManufacturerPart i where (LOWER(CAST(i.partNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.partName as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.mfrPartType.name as text))) LIKE '%' || :searchText || '%'")
    Integer getMfrPartsCountBySearchQuery(@Param("searchText") String searchText);

    @Query("select i.id from PLMManufacturerPart i where (LOWER(CAST(i.partName as text))) LIKE '%' || :searchText || '%'")
    List<Integer> getMfrPartsCountByPartName(@Param("searchText") String searchText);
}
