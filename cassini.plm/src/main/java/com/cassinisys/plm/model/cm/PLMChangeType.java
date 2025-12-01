package com.cassinisys.plm.model.cm;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 6/13/17.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_CHANGETYPE")
@PrimaryKeyJoinColumn(name = "TYPE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PLMDeviationType.class, name = "PLMDeviationType"),
        @JsonSubTypes.Type(value = PLMWaiverType.class, name = "PLMWaiverType"),
        @JsonSubTypes.Type(value = PLMECRType.class, name = "PLMECRType"),
        @JsonSubTypes.Type(value = PLMDCRType.class, name = "PLMDCRType"),
        @JsonSubTypes.Type(value = PLMDCOType.class, name = "PLMDCOType"),
        @JsonSubTypes.Type(value = PLMMCOType.class, name = "PLMMCOType"),
        @JsonSubTypes.Type(value = PLMECOType.class, name = "PLMECOType")
})
public class PLMChangeType extends CassiniObject {

    @Transient
    List<PLMChangeType> children = new ArrayList<>();
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PARENT_TYPE")
    private Integer parentType;
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "AUTONUMBER_SOURCE")
    private AutoNumber autoNumberSource;
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CHANGE_REASON_TYPES")
    private Lov changeReasonTypes;

    @Transient
    private List<PLMChangeTypeAttribute> attributes = new ArrayList<>();

    @Transient
    private PLMChangeType parentTypeReference;

    public PLMChangeType() {
        super(PLMObjectType.CHANGETYPE);
    }


}
