package com.cassinisys.irste.model;

import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 19-11-2018.
 */
@Entity
@Table(name = "IRSTE_USERUTILITIES")
public class IRSTEUserUtilities implements Serializable {

    @EmbeddedId
    @ApiObjectField(required = true)
    private ResponderUtilityId id;

    @Column(name = "personType")
    private Integer personType;

    public IRSTEUserUtilities() {
    }

    public Integer getPersonType() {
        return personType;
    }

    public void setPersonType(Integer personType) {
        this.personType = personType;
    }

    public IRSTEUserUtilities(ResponderUtilityId id) {
        this.id = id;
    }

    public ResponderUtilityId getId() {
        return id;
    }

    public void setId(ResponderUtilityId id) {
        this.id = id;
    }

}
