package com.cassinisys.platform.service.core;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.Measurement;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by subramanyam on 18-05-2020.
 */
@Service
public class MeasurementService implements CrudService<Measurement, Integer> {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    @Override
    @Transactional
    public Measurement create(Measurement measurement) {
        Measurement existMeasurement = measurementRepository.findByName(measurement.getName());
        if (existMeasurement != null) {
            throw new CassiniException(measurement.getName() + " : Measurement already exist");
        }
        return measurementRepository.save(measurement);
    }

    @Override
    @Transactional
    public Measurement update(Measurement measurement) {
        Measurement existMeasurement = measurementRepository.findByName(measurement.getName());
        if (existMeasurement != null && !existMeasurement.getId().equals(measurement.getId())) {
            throw new CassiniException(measurement.getName() + " : Measurement already exist");
        }
        return measurementRepository.save(measurement);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        measurementRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Measurement get(Integer id) {
        return measurementRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Measurement getMeasurementByName(String name) {
        Measurement measurement = measurementRepository.findByName(name);
        if (measurement != null) {
            measurement.getMeasurementUnits().addAll(measurementUnitRepository.findByMeasurementOrderByIdAsc(measurement.getId()));
        }
        return measurement;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Measurement> getAll() {
        List<Measurement> measurements = measurementRepository.findAllByOrderByIdAsc();
        measurements.forEach(measurement -> {
            measurement.getMeasurementUnits().addAll(measurementUnitRepository.findByMeasurementOrderByIdAsc(measurement.getId()));
        });
        return measurements;
    }

    @Transactional(readOnly = true)
    public List<MeasurementUnit> getMeasurementUnits(Integer id) {
        return measurementUnitRepository.findByMeasurement(id);
    }

    @Transactional
    public MeasurementUnit createMeasurementUnit(Integer id, MeasurementUnit measurementUnit) {
        MeasurementUnit existUnit = measurementUnitRepository.findByMeasurementAndName(id, measurementUnit.getName());
        if (existUnit != null) {
            throw new CassiniException("Measurement Unit already exist");
        }

        return measurementUnitRepository.save(measurementUnit);
    }

    @Transactional
    public MeasurementUnit updateMeasurementUnit(Integer id, Integer unitId, MeasurementUnit measurementUnit) {
        MeasurementUnit existUnit = measurementUnitRepository.findByMeasurementAndName(id, measurementUnit.getName());
        if (existUnit != null && !measurementUnit.getId().equals(existUnit.getId())) {
            throw new CassiniException("Measurement Unit already exist");
        }

        return measurementUnitRepository.save(measurementUnit);
    }

    @Transactional
    public void deleteMeasurementUnit(Integer id, Integer unitId) {
        measurementUnitRepository.delete(unitId);
    }
}
