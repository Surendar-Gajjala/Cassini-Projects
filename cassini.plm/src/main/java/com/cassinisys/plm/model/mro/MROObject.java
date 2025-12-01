package com.cassinisys.plm.model.mro;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Table(name = "MRO_OBJECT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MROObject extends CassiniObject {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "NUMBER")
    private String number;
    @Column(name = "DESCRIPTION")
    private String description;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    public MROObject() {
        super.setObjectType(MROEnumObject.MROOBJECT);
    }

}
