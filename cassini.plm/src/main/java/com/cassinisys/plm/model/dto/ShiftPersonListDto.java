package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.converter.LocalTimeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Convert;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgajjala on 20-09-2022.
 */
@Data
public class ShiftPersonListDto {

    private Integer id;
    private String name;
    private String number;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @Convert(converter = LocalTimeConverter.class)
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @Convert(converter = LocalTimeConverter.class)
    private LocalTime endTime;

    private List<Person> persons = new ArrayList<>();
}
