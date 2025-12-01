package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROAssetSparePart;
import com.cassinisys.plm.model.mro.MROSparePart;
import com.cassinisys.plm.model.mro.MROWorkOrderPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROAssetSparePartRepository extends JpaRepository<MROAssetSparePart, Integer> {
    List<MROAssetSparePart> findByAsset(Integer assetId);

    @Query("select i.sparePart.id from MROAssetSparePart i where i.asset= :assetId")
    List<Integer> getSparePartIdsByAsset(@Param("assetId") Integer assetId);
    MROAssetSparePart findByAssetAndSparePart(Integer assetId, MROSparePart sparePart);

    List<MROAssetSparePart> findBySparePart(MROSparePart sparePart);

}
