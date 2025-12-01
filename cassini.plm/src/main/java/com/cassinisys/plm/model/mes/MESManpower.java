package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.common.Person;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Entity
@Table(name = "MES_MANPOWER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESManpower extends MESObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MESManpowerType type;

    // @Column(name = "PERSON")
    // private Integer person;

  /*  @Column(name = "ACTIVE", nullable = false)
    private Boolean active = Boolean.TRUE;*/

    @Transient
    private String personName;

    @Transient
    private String createPerson;
    @Transient
    private String typeName;

    @Transient
    private String modifiedPerson;
    @Transient
    private Boolean newPerson = Boolean.FALSE;
    @Transient
    private Person personDetails;

    public MESManpower() {
        super.setObjectType(MESEnumObject.MANPOWER);
    }

}
