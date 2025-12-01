package com.cassinisys.irste.model;

import lombok.Data;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 19-11-2018.
 */
@Embeddable
@Data
public class ResponderUtilityId implements Serializable {

    @Column(name = "UTILITY")
    @ApiObjectField(required = true)
    private String utility;

    @Column(name = "PERSON_ID")
    @ApiObjectField(required = true)
    private Integer personId;

    public ResponderUtilityId() {
    }

    public ResponderUtilityId(String utility, Integer personId) {
        this.utility = utility;
        this.personId = personId;
    }

}
