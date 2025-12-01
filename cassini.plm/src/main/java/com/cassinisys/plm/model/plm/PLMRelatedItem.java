package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by subramanyamreddy on 011 11-Jan -18.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_RELATEDITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMRelatedItem extends CassiniObject {

    @Column(name = "FROM_ITEM")
    private Integer fromItem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TO_ITEM")
    private PLMItemRevision toItem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RELATIONSHIP")
    private PLMRelationship relationship;

    @Column(name = "NOTES")
    private String notes;

    public PLMRelatedItem() {
        super(PLMObjectType.RELATEDITEM);
    }

}
