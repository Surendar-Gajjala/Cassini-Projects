package com.cassinisys.plm.model.mro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Table(name = "MRO_SPAREPART")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MROSparePart extends MROObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MROSparePartType type;
    @Transient
    private String createPerson;
    @Transient
    private String typeName;
    @Transient
    private String imageValue;

    @Transient
    private String modifiedPerson;


    public MROSparePart() {
        super.setObjectType(MROEnumObject.MROSPAREPART);
    }
}
