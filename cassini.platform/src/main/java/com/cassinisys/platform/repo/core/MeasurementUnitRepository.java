package com.cassinisys.platform.repo.core;

import com.cassinisys.platform.model.core.MeasurementUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 18-05-2020.
 */
@Repository
public interface MeasurementUnitRepository extends JpaRepository<MeasurementUnit, Integer> {

    List<MeasurementUnit> findByMeasurement(Integer measurement);

    List<MeasurementUnit> findByMeasurementOrderByIdAsc(Integer measurement);

    MeasurementUnit findByMeasurementAndName(Integer measurement, String name);

    MeasurementUnit findByMeasurementAndBaseUnitTrue(Integer measurement);

    List<MeasurementUnit> findByMeasurementInAndBaseUnitTrue(Iterable<Integer> measurement);

    List<MeasurementUnit> findByMeasurementIn(Iterable<Integer> ids);

    List<MeasurementUnit> findByBaseUnitTrue();
}
