package com.cassinisys.irste.model;

import lombok.Data;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 21-12-2018.
 */
@Embeddable
@Data
public class GroupUtilityId implements Serializable{

    @Column(name = "UTILITY")
    @ApiObjectField(required = true)
    private String utility;

    @Column(name = "GROUP_NAME")
    @ApiObjectField(required = true)
    private String group;

    public GroupUtilityId() {
    }

    public GroupUtilityId(String utility, String group) {
        this.utility = utility;
        this.group = group;
    }

}
