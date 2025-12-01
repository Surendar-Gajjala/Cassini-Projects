package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerType;
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
public interface ManufacturerRepository extends JpaRepository<PLMManufacturer, Integer>, QueryDslPredicateExecutor<PLMManufacturer> {

    List<PLMManufacturer> findByIdIn(List<Integer> ids);

    PLMManufacturer findByName(String name);

    PLMManufacturer findByNameEqualsIgnoreCase(String name);

    @Query(
            "SELECT i FROM PLMManufacturer i WHERE i.mfrType.id IN :typeIds"
    )
    Page<PLMManufacturer> getByMfrTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    List<PLMManufacturer> findByMfrType(PLMManufacturerType manufacturerType);

    @Query("SELECT count(i) FROM PLMManufacturer i where i.lifeCyclePhase.phaseType= :phaseType")
    Integer getUnqualifiedMfrs(@Param("phaseType") LifeCyclePhaseType phaseType);

    @Query("SELECT count(i) FROM PLMManufacturer i where i.lifeCyclePhase.phaseType= :phaseType")
    Integer getReviewMfrs(@Param("phaseType") LifeCyclePhaseType phaseType);

    @Query("SELECT count(i) FROM PLMManufacturer i where i.lifeCyclePhase.phaseType= :phaseType")
    Integer getApprovedMfrs(@Param("phaseType") LifeCyclePhaseType phaseType);

    @Query("SELECT count(i) FROM PLMManufacturer i where i.lifeCyclePhase.phaseType= :phaseType")
    Integer getDisqualifiedMfrs(@Param("phaseType") LifeCyclePhaseType phaseType);

    @Query("SELECT count (i) FROM PLMManufacturer i")
    Integer getMfrsCount();

    @Query("select count (i) from PLMManufacturer i where (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.mfrType.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.phoneNumber as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.contactPerson as text))) LIKE '%' || :searchText || '%'")
    Integer getMfrsCountBySearchQuery(@Param("searchText") String searchText);

    @Query("select i.id from PLMManufacturer i where (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    List<Integer> getMfrIdsBySearchQuery(@Param("searchText") String searchText);
}
