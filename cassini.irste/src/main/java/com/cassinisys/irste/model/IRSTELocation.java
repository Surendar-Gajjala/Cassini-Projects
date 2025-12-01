package com.cassinisys.irste.model;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Nageshreddy on 13-11-2018.
 */
@Entity
@Table(name = "IRSTE_LOCATION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class IRSTELocation extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "GROUP_ID")
    private Integer groupId;

    @Transient
    private List<String> utilities;

    public IRSTELocation() {
        super(IRSTEObjectType.LOCATION);
    }

}
