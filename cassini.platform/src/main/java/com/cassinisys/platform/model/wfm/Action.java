package com.cassinisys.platform.model.wfm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lakshmi on 1/31/2016.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "ACTION")
@PrimaryKeyJoinColumn(name = "ID")
public class Action extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SOURCE")
    private Integer source;

    @Column(name = "TARGET")
    private Integer target;

    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    @Column(name = "ACTOR")
    private Integer actor;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp;

    public Action() {
        super(ObjectType.ACTION);
    }

}
