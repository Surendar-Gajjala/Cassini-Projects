package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROMeterReading;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROMeterReadingRepository extends JpaRepository<MROMeterReading, Integer> {

    /*@Query(
            "SELECT a FROM MROMeterReading a WHERE a.assetMeter= :assetMeterId ORDER BY a.currentDate DESC LIMIT 1"
    )*/
    @Query(value = "select mr.id from MRO_METER_READING mr WHERE mr.ASSET_METER=?1 order by TIMESTAMP desc LIMIT 1", nativeQuery = true)
    Integer getLastAssetMeterReadings(@Param("assetMeterId") Integer assetMeterId);

}
