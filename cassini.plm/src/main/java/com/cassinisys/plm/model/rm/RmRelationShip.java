package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "RM_RELATIONSHIP")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RmRelationShip extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FROM_TYPE")
    private RmObjectType fromType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TO_TYPE")
    private RmObjectType toType;

    public RmRelationShip() {
        super(PLMObjectType.RMRELATIONSHIP);
    }


}
