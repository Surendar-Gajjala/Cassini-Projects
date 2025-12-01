package com.cassinisys.irste.model;

import lombok.Data;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 13-11-2018.
 */
@Embeddable
@Data
public class UtilityLocationId implements Serializable {

    @Column(name = "UTILITY")
    @ApiObjectField(required = true)
    private String utility;

    @Column(name = "LOCATION_ID")
    @ApiObjectField(required = true)
    private Integer locationId;

    public UtilityLocationId() {
    }

    public UtilityLocationId(String utility, Integer locationId) {
        this.utility = utility;
        this.locationId = locationId;
    }

}
