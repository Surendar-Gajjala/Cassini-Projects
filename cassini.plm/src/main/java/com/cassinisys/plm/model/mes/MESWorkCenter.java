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
@Table(name = "MES_WORKCENTER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESWorkCenter extends MESObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MESWorkCenterType type;

    @Column(name = "PLANT", nullable = false)
    private Integer plant;

    @Column(name = "ASSEMBLY_LINE")
    private Integer assemblyLine;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "REQUIRES_MAINTENANCE", nullable = false)
    private Boolean requiresMaintenance = Boolean.TRUE;

    @Transient
    private String plantName;
    @Transient
    private String assemblyLineName;
    @Transient
    private String createPerson;
    @Transient
    private String typeName;

    @Transient
    private String modifiedPerson;

    public MESWorkCenter() {
        super.setObjectType(MESEnumObject.WORKCENTER);
    }

    @Transient
    private List<FileDto> workcenterFiles = new LinkedList<>();
}
