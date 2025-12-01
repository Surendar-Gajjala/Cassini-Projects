package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.mro.MROAssetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROAssetRepository extends JpaRepository<MROAsset, Integer>, QueryDslPredicateExecutor<MROAsset> {
    MROAsset findByNameContainingIgnoreCase(String name);

    @Query(
            "SELECT i FROM MROAsset i WHERE i.type.id IN :typeIds"
    )
    Page<MROAsset> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query("select i from MROAsset i where i.type.id= :typeId order by i.createdDate asc")
    List<MROAsset> getAssetsByTypeId(@Param("typeId") Integer typeId);

    List<MROAsset> findByResource(Integer resource);

    List<MROAsset> findByType(MROAssetType type);

    MROAsset findByNumber(String number);

    @Query("SELECT count (i) FROM MROAsset i")
    Integer getAssetsCount();

    @Query("select count (i) from MROAsset i where (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getAssetsCountBySearchQuery(@Param("searchText") String searchText);
}
