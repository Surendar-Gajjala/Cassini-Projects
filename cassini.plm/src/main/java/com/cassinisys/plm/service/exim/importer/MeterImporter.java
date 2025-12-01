package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Measurement;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.mro.MROMeter;
import com.cassinisys.plm.repo.mro.MROMeterReadingRepository;
import com.cassinisys.plm.repo.mro.MROMeterRepository;
import com.cassinisys.plm.repo.mro.MROMeterTypeRepository;
import com.cassinisys.plm.repo.mro.MROMeterRepository;
import com.cassinisys.plm.service.classification.MROObjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Created by Lenovo on 12-11-2021.
 */
@Service
@Scope("prototype")
public class MeterImporter {
    @Autowired
    private MROMeterRepository meterRepository;
    @Autowired
    private MROMeterTypeRepository meterTypeRepository;
    @Autowired
    private MROMeterReadingRepository meterReadingRepository;

    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MROObjectTypeService mroObjectTypeService;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PlantsImporter plantsImporter;

    public static ConcurrentMap<String, MROMeterType> rootMeterTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MROMeterType> meterTypesMapByPath = new ConcurrentHashMap<>();
    Map<String, AutoNumber> autoNumberMap = new HashMap<>();

    private static AutoNumber autoNumber;

    private void getDefaultNumberSource() {
        autoNumber = plantsImporter.getDefaultPlantNumberSource("Default Meter Number Source");
    }

    public void loadMeterClassificationTree() {
        getDefaultNumberSource();
        rootMeterTypesMap = new ConcurrentHashMap<>();
        meterTypesMapByPath = new ConcurrentHashMap<>();
        List<MROMeterType> rootTypes = mroObjectTypeService.getMeterTypeTree();
        for (MROMeterType rootType : rootTypes) {
            rootMeterTypesMap.put(rootType.getName(), rootType);
            meterTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    private MROMeterType getMeterTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MROMeterType meterType = rootMeterTypesMap.get(name);
            if (meterType != null) {
                meterType = meterType.getChildTypeByPath(path.substring(index + 1));
                return meterType;
            } else {
                return meterType;
            }

        } else {
            name = path;
            return rootMeterTypesMap.get(name);
        }
    }

    public MROMeterType getMeterType(String typePath) {
        MROMeterType meterType = meterTypesMapByPath.get(typePath);
        if (meterType == null) {
            meterType = getMeterTypeByPath(typePath);
            if (meterType == null) {
                meterType = createMeterTypeByPath(null, typePath);
            }

            meterTypesMapByPath.put(typePath, meterType);
        }
        return meterType;
    }


    private MROMeterType createMeterTypeByPath(MROMeterType parentType, String path) {
        String name;
        int index = path.indexOf('/');
        String restOfPath = null;
        if (index != -1) {
            name = path.substring(0, index);
            restOfPath = path.substring(index + 1);
        } else {
            name = path;
        }

        if (parentType == null) {
            parentType = rootMeterTypesMap.get(name);
            if (parentType == null) {
                parentType = new MROMeterType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = mroObjectTypeService.createMeterType(parentType);
                rootMeterTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createMeterTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MROMeterType childMeterType = new MROMeterType();
            childMeterType.setParentType(parentType.getId());
            childMeterType.setName(name);
            childMeterType.setDescription(name);
            childMeterType.setAutoNumberSource(parentType.getAutoNumberSource());
            parentType = mroObjectTypeService.createMeterType(childMeterType);
            parentType.getChildren().add(childMeterType);
            if (restOfPath != null) {
                return parentType = createMeterTypeByPath(childMeterType, restOfPath);
            } else {
                return childMeterType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createMeterTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }
        return parentType;
    }


    public void importMeters(TableData tableData) throws ParseException {
        this.loadMeterClassificationTree();
        autoNumberMap = new HashMap<>();
        Map<String, MROMeter> dbMeterMap = new LinkedHashMap();
        Map<String, MROMeterType> dbMeterTypeMap = new LinkedHashMap();
        List<MROMeter> meter = meterRepository.findAll();
        List<MROMeterType> meterTypes = meterTypeRepository.findAll();
        dbMeterMap = meter.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        dbMeterTypeMap = meterTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        autoNumberMap = importer.getCommonAutoNumbers();
        List<MROMeter> meter1 = createMeters(tableData, dbMeterMap, dbMeterTypeMap);

    }


    private List<MROMeter> createMeters(TableData tableData, Map<String, MROMeter> dbMeterMap, Map<String, MROMeterType> dbMeterTypeMap) {
        List<MROMeter> meters2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Meter Name");
            if (name != null && !name.trim().equals("")) {
                String jigsTypeName = stringListHashMap.get("Meter Type");
                String number = stringListHashMap.get("Meter Number");
                String typePath = stringListHashMap.get("Type Path");
                if (jigsTypeName != null && jigsTypeName != "") {
                    MROMeterType meterType = dbMeterTypeMap.get(jigsTypeName);
                    if (meterType != null) {
                        MROMeter meter = dbMeterMap.get(number);
                        if (meter == null) {
                            meters2.add(createMeter(i, number, meterType, stringListHashMap, dbMeterMap));
                        } else {
                            updateMeter(i, meter, stringListHashMap);
                            meters2.add(meter);
                        }

                    } else {
                        meters2.add(createMeterByPath(i, number, typePath, dbMeterMap, dbMeterTypeMap, stringListHashMap));
                    }
                } else {
                    meters2.add(createMeterByPath(i, number, typePath, dbMeterMap, dbMeterTypeMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide Meter Name for row :" + i);
            }
        }

        if (meters2.size() > 0) {
            meterRepository.save(meters2);
        }

        return meters2;
    }

    private MROMeter createMeterByPath(int i, String number, String typePath, Map<String, MROMeter> dbMeterMap,
                                                   Map<String, MROMeterType> dbMeterTypeMap, RowData stringListHashMap) {
        MROMeter meter = null;
        if (typePath != null && typePath != "") {
            MROMeterType mesMeterType = this.getMeterType(typePath);
            if (mesMeterType != null) {
                dbMeterTypeMap.put(mesMeterType.getName(), mesMeterType);
                meter = dbMeterMap.get(number);
                if (meter == null)
                    meter = createMeter(i, number, mesMeterType, stringListHashMap, dbMeterMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                        null, "Please provide Meter Type or Meter Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                    null, "Please provide Meter Type or Meter Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return meter;
    }
    
    

    private MROMeter createMeter(Integer i, String name, MROMeterType type, RowData stringListHashMap, Map<String, MROMeter> dbMetersMap) {
        MROMeter meter = new MROMeter();
        String meterNumber = stringListHashMap.get("Meter Number");
        String meterName = stringListHashMap.get("Meter Name");
        String meterDescription = stringListHashMap.get("Meter Description");
        String meterType = stringListHashMap.get("Meter Type (CONTINUOUS/GUAGE)");
        String meterReadingType = stringListHashMap.get("Meter Reading Type (ABSOLUTE/CHANGE)");
        String qom = stringListHashMap.get("QOM");
        String uom = stringListHashMap.get("UOM");
        if (meterNumber == null || meterNumber.trim().equals("")) {
            meterNumber = importer.getNextNumberWithoutUpdate(type.getAutoNumberSource().getName(), autoNumberMap);
        }
        meter.setType(type);
        meter.setNumber(meterNumber);
        meter.setName(meterName);
        meter.setDescription(meterDescription);
        importer.saveNextNumber(type.getAutoNumberSource().getName(), meter.getNumber(), autoNumberMap);
        if (meterType.equals(MeterType.CONTINUOUS) || meterType.toLowerCase() == "continous") {
            meter.setMeterType(MeterType.CONTINUOUS);
        }
        if (meterType.equals(MeterType.GUAGE) || meterType.toLowerCase() == "guage") {
            meter.setMeterType(MeterType.GUAGE);
        }

        if (meterReadingType.equals(MeterReadingType.ABSOLUTE) || meterReadingType.toLowerCase() == "absolute") {
            meter.setMeterReadingType(MeterReadingType.ABSOLUTE);
        }
        if (meterReadingType.equals(MeterReadingType.CHANGE) || meterReadingType.toLowerCase() == "change") {
            meter.setMeterReadingType(MeterReadingType.CHANGE);
        }

        Measurement measurement = measurementRepository.findByName(qom);
        MeasurementUnit measurementUnit = measurementUnitRepository.findByMeasurementAndName(measurement.getId(), uom);

        meter.setQom(measurement.getId());
        meter.setUom(measurementUnit.getId());

//        meter = meterRepository.save(meter);

        dbMetersMap.put(meter.getNumber(), meter);
        return meter;
    }


    private MROMeter updateMeter(Integer i, MROMeter mesMeter, RowData stringListHashMap) {
        String jigsFixtureName = stringListHashMap.get("Meter Name".trim());
        String jigsFixtureDescription = stringListHashMap.get("Meters Description".trim());

        mesMeter.setName(jigsFixtureName);
        mesMeter.setDescription(jigsFixtureDescription);

        return mesMeter;
    }


}
