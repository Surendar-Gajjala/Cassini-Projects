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
@Table(name = "RM_RELATEDITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RmRelatedItem extends CassiniObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FROM_OBJECT")
    private RmObject fromItem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TO_OBJECT")
    private RmObject toItem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RELATIONSHIP")
    private RmRelationShip relationship;

    @Column(name = "NOTES")
    private String notes;

    public RmRelatedItem() {
        super(PLMObjectType.RMRELATEDITEM);
    }


}