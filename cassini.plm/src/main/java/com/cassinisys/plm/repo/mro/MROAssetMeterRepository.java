package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROAssetMeter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROAssetMeterRepository extends JpaRepository<MROAssetMeter, Integer> {
    List<MROAssetMeter> findByAsset(Integer assetId);

    List<MROAssetMeter> findByMeter(Integer id);

    void deleteByAssetAndMeter(Integer assetId, Integer meterId);

    void deleteByAsset(Integer assetId);

    @Query("select i.meter.id from MROAssetMeter i where i.asset= :asset")
    List<Integer> getMeterIdsByAsset(@Param("asset") Integer asset);

}
