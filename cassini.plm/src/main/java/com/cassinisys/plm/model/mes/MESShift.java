package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.util.converter.LocalTimeConverter;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lenovo on 26-10-2020.
 */

@Entity
@Table(name = "MES_SHIFT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESShift extends MESObject {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @Column(name = "START_TIME", nullable = false)
    @Convert(converter = LocalTimeConverter.class)
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @Column(name = "END_TIME", nullable = false)
    @Convert(converter = LocalTimeConverter.class)
    private LocalTime endTime;

    @Transient
    private String localStartTime;
    @Transient
    private String localEndTime;
    @Transient
    private String createPerson;
    @Transient
    private String modifiedPerson;
    @Transient
    private List<FileDto> shiftFiles = new LinkedList<>();

    public MESShift() {
        super.setObjectType(MESEnumObject.SHIFT);
    }

}
