package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.mro.MROAsset;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Entity
@Table(name = "MES_OBJECT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESObject extends CassiniObject {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "NUMBER", nullable = false)
    private String number;

    @Column(name = "DESCRIPTION")
    private String description;

    @Transient
    private List<MESObjectAttribute> mesObjectAttributes = new ArrayList<>();

    @Transient
    private MESManufacturerData manufacturerData;
    @Transient
    private MROAsset asset;

    public MESObject() {
        super.setObjectType(MESEnumObject.MESOBJECT);
    }

}
