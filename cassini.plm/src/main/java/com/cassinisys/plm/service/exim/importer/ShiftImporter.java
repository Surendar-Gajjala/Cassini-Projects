package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.plm.model.mes.MESShift;
import com.cassinisys.plm.repo.mes.MESShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Lenovo on 12-11-2021.
 */
@Service
@Scope("prototype")
public class ShiftImporter {

    @Autowired
    private MESShiftRepository shiftRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;


    public void importShifts(TableData tableData) throws ParseException {
        Map<String, MESShift> dbShiftMap = new LinkedHashMap();
        List<MESShift> shifts = shiftRepository.findAll();
        dbShiftMap = shifts.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        List<MESShift> shifts1 = createShifts(tableData, dbShiftMap);
    }

    private List<MESShift> createShifts(TableData tableData, Map<String, MESShift> dbShiftsMap) {
        List<MESShift> shifts2 = new LinkedList<>();
        int i = 0;
        for (RowData stringListHashMap : tableData.getRows()) {
            if (stringListHashMap.containsKey("Shift Number")) {
                String name = stringListHashMap.get("Shift Number");
                if (name != null && !name.trim().equals("")) {
                    String shiftNumber = stringListHashMap.get("Shift Number");
                    if (shiftNumber != null) {
                        MESShift shift = dbShiftsMap.get(shiftNumber);
                        if (shift != null) {
                            shift = updateShift(i, shift, stringListHashMap);
                            shifts2.add(shift);
                        } else {
                            MESShift shift1 = createShift(i, name, stringListHashMap, dbShiftsMap);
                            shifts2.add(shift1);
                        }
                    } else {
                        throw new CassiniException(messageSource.getMessage("please_provide_shift_number_for_row_number" + (i),
                                null, "Please provide Shift Number for row number:" + (i), LocaleContextHolder.getLocale()));
                    }
                } else {
                    shifts2.add(null);
                }
            } else {
                throw new CassiniException("Please provide Shift Number Column also");
            }
            i++;
        }
        if (shifts2.size() > 0) {
            shiftRepository.save(shifts2);
        }
        return shifts2;
    }

    private MESShift createShift(Integer i, String name, RowData stringListHashMap, Map<String, MESShift> dbShiftsMap) {
        MESShift shift = new MESShift();
        String shiftNumber = stringListHashMap.get("Shift Number");
        String shiftName = stringListHashMap.get("Shift Name");
        String shiftDescription = stringListHashMap.get("Shift Description");
        String startTime = stringListHashMap.get("Start Time");
        String endTime = stringListHashMap.get("End Time");
//        String startTime = "13:50";
//        String endTime = "14:50";
        shift.setNumber(shiftNumber);
        shift.setName(shiftName);
        shift.setDescription(shiftDescription);

        LocalTime localStartTime = LocalTime.parse(startTime,
                DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime localEndTime = LocalTime.parse(endTime,
                DateTimeFormatter.ofPattern("HH:mm"));

        shift.setStartTime(localStartTime);
        shift.setEndTime(localEndTime);

//        shift = shiftRepository.save(shift);

        dbShiftsMap.put(shift.getNumber(), shift);

        return shift;
    }


    private MESShift updateShift(Integer i, MESShift mesShift, RowData stringListHashMap) {
        String equipmentName = stringListHashMap.get("Shift Name");
        String equipmentDescription = stringListHashMap.get("Shift Description");
        String startTime = stringListHashMap.get("Start Time");
        String endTime = stringListHashMap.get("End Time");

        mesShift.setName(equipmentName);
        mesShift.setDescription(equipmentDescription);
        LocalTime localStartTime = LocalTime.parse(startTime,
                DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime localEndTime = LocalTime.parse(endTime,
                DateTimeFormatter.ofPattern("HH:mm"));
        mesShift.setStartTime(localStartTime);
        mesShift.setEndTime(localEndTime);

        return mesShift;
    }

}
