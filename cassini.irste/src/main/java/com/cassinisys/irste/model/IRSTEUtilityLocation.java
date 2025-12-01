package com.cassinisys.irste.model;

import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 13-11-2018.
 */
@Entity
@Table(name = "IRSTE_UTILITYLOCATION")
public class IRSTEUtilityLocation implements Serializable {

    @EmbeddedId
    @ApiObjectField(required = true)
    private UtilityLocationId id;

    public IRSTEUtilityLocation() {
    }


    public IRSTEUtilityLocation(UtilityLocationId id) {
        this.id = id;
    }

    public UtilityLocationId getId() {
        return id;
    }

    public void setId(UtilityLocationId id) {
        this.id = id;
    }
}
