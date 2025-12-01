package com.cassinisys.irste.model;

import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 21-12-2018.
 */
@Entity
@Table(name = "IRSTE_GROUPUTILITY")
public class IRSTEGroupUtility implements Serializable {

    @EmbeddedId
    @ApiObjectField(required = true)
    private GroupUtilityId id;

    public IRSTEGroupUtility() {

    }

    public IRSTEGroupUtility(GroupUtilityId id) {
        this.id = id;
    }

    public GroupUtilityId getId() {
        return id;
    }

    public void setId(GroupUtilityId id) {
        this.id = id;
    }

}
