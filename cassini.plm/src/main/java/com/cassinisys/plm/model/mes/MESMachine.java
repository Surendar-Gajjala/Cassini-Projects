package com.cassinisys.plm.model.mes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.LinkedList;
import java.util.List;
import com.cassinisys.plm.model.plm.dto.FileDto;

import javax.persistence.*;

/**
 * Created by Lenovo on 26-10-2020.
 */

@Entity
@Table(name = "MES_MACHINE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESMachine extends MESObject {

    @ManyToOne
    @JoinColumn(name = "TYPE")
    private MESMachineType type;

    @Column(name = "REQUIRES_MAINTENANCE", nullable = false)
    private Boolean requiresMaintenance = Boolean.TRUE;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "WORK_CENTER")
    private Integer workCenter;

    @Column(name = "IMAGE")
    private byte[] image;
    @Transient
    private String workCenterName;
    @Transient
    private Integer workCenterId;
    @Transient
    private String createPerson;
    @Transient
    private String typeName;
    @Transient
    private String imageValue;

    @Transient
    private String modifiedPerson;

    public MESMachine() {
        super.setObjectType(MESEnumObject.MACHINE);
    }

    @Transient
    private List<FileDto> machineFiles = new LinkedList<>();

}