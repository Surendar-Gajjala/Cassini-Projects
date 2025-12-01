package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.Measurement;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Entity
@Table(name = "MES_MATERIAL")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESMaterial extends MESObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MESMaterialType type;

    @Column(name = "QOM")
    private Integer qom;

    @Column(name = "UOM")
    private Integer uom;

    @Column(name = "IMAGE")
    private byte[] image;

    @Transient
    private String qomName;
    @Transient
    private String uomName;
    @Transient
    private Measurement qomObject;
    @Transient
    private MeasurementUnit uomObject;

    @Transient
    private String createPerson;
    @Transient
    private String typeName;
    @Transient
    private String imageValue;
    @Transient
    private String modifiedPerson;

    public MESMaterial() {
        super.setObjectType(MESEnumObject.MATERIAL);
    }

}

