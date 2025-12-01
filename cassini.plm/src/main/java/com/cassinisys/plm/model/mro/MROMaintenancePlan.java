package com.cassinisys.plm.model.mro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Table(name = "MRO_MAINTENANCE_PLAN")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MROMaintenancePlan extends MROObject {

    @Column(name = "ASSET")
    private Integer asset;

    @Transient
    private String createPerson;

    @Transient
    private String modifiedPerson;

    @Transient
    private String assetName;

    public MROMaintenancePlan() {
        super.setObjectType(MROEnumObject.MROMAINTENANCEPLAN);
    }
}
