package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "RM_OBJECTTYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RmObjectType extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "NUMBER_SOURCE", nullable = true)
    private AutoNumber numberSource;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "REVISION_SEQUENCE", nullable = true)
    private Lov revisionSequence;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE", nullable = true)
    private PLMLifeCycle lifecycle;

    @Transient
    private List<RmObjectTypeAttribute> attributes = new ArrayList<>();

    public RmObjectType(PLMObjectType type) {
        super(type);
    }


}