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
@Table(name = "PLM_RELATIONSHIP")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMRelationship extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FROM_TYPE")
    private PLMItemType fromType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TO_TYPE")
    private PLMItemType toType;

    public PLMRelationship() {
        super(PLMObjectType.RELATIONSHIP);
    }

}
