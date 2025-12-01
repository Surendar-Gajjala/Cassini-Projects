package com.cassinisys.platform.repo.core;

import com.cassinisys.platform.model.core.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 18-05-2020.
 */
@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    Measurement findByName(String name);

    List<Measurement> findAllByOrderByIdAsc();
}
