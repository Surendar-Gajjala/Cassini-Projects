package com.cassinisys.plm.model.mes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.cassinisys.plm.model.plm.dto.FileDto;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

/**
 * Created by Lenovo on 26-10-2020.
 */

@Entity
@Table(name = "MES_TOOL")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESTool extends MESObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MESToolType type;

    @Column(name = "REQUIRES_MAINTENANCE", nullable = false)
    private Boolean requiresMaintenance = Boolean.TRUE;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "IMAGE")
    private byte[] image;
    @Transient
    private String createPerson;
    @Transient
    private String typeName;
    @Transient
    private String imageValue;
    @Transient
    private String modifiedPerson;

    public MESTool() {
        super.setObjectType(MESEnumObject.TOOL);
    }

    @Transient
    private List<FileDto> toolFiles = new LinkedList<>();

}
