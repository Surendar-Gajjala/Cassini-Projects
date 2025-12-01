package com.cassinisys.irste.model;

import com.cassinisys.platform.model.core.CassiniObject;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by Nageshreddy on 13-11-2018.
 */
@Entity
@Table(name = "IRSTE_GROUPLOCATION")
@PrimaryKeyJoinColumn(name = "GROUP_ID")
@Data
public class IRSTEGroupLocation extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Transient
    private List<IRSTELocation> locations;

    public IRSTEGroupLocation() {
        super(IRSTEObjectType.LOCATION);
    }

}
