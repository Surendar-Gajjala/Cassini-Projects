package com.cassinisys.plm.model.mro;

import com.cassinisys.plm.model.mes.MESType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Table(name = "MRO_ASSET")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MROAsset extends MROObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MROAssetType type;

    @Column(name = "METERED")
    private Boolean metered = Boolean.FALSE;
    @Column(name = "RESOURCE")
    private Integer resource;
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mes.MESType")})
    @Column(name = "RESOURCE_TYPE", nullable = true)
    private MESType resourceType;
    @Transient
    private List<MROAssetMeter> assetMeters = new ArrayList<>();
    @Transient
    private Integer meters[];
    @Transient
    private Object resourceObject;
    @Transient
    private String createPerson;
    @Transient
    private String typeName;
    @Transient
    private String imageValue;

    @Transient
    private String modifiedPerson;
    @Transient
    private List<MROMeter> meterObjects = new LinkedList<>();

    public MROAsset() {
        super.setObjectType(MROEnumObject.MROASSET);
    }

}
