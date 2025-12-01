package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.cm.PLMDCO;
import com.cassinisys.plm.model.cm.PLMECO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyamreddy on 002 2-May -17.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_ITEMREVISION")
@PrimaryKeyJoinColumn(name = "ITEM_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMItemRevision extends CassiniObject {

    @Column(name = "ITEM_MASTER")
    private Integer itemMaster;
    @Column(name = "REVISION")
    private String revision;
    @OneToOne
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;
    @Column(name = "IS_RELEASED")
    private Boolean released = Boolean.FALSE;
    @Column(name = "IS_REJECTED")
    private Boolean rejected = Boolean.FALSE;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELEASED_DATE", nullable = false)
    private Date releasedDate;
    @Column(name = "HAS_BOM")
    private Boolean hasBom = Boolean.FALSE;
    @Column(name = "HAS_FILES")
    private Boolean hasFiles = Boolean.FALSE;
    @Column(name = "INSTANCE")
    private Integer instance;
    @Column(name = "BOMINCLRULES")
    private String inclusionRules;
    @Column(name = "ITEMEXCLRULES")
    private String itemExclusionRules;
    @Column(name = "ATTRIBUTE_EXCLUSIONRULES")
    private String attributeExclusionRules;
    @Column(name = "ITEM_EXCLUSIONS")
    private String itemExclusions;
    @Column(name = "BOM_CONFIGURATION")
    private Integer bomConfiguration;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EFFECTIVE_FROM")
    private Date effectiveFrom;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EFFECTIVE_TO")
    private Date effectiveTo;
    @Column(name = "WORKFLOW")
    private Integer workflow;
    @Column(name = "FROM_REVISION")
    private String fromRevision;
    @Column(name = "DESIGN_OBJECT") //References PDM_REVISIONED_OBJECT (ID)
    private Integer designObject;
    @Column(name = "CHANGE_ORDER")
    private Integer changeOrder;
    @Column(name = "INCORPORATE")
    private Boolean incorporate = Boolean.FALSE;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INCORPORATE_DATE")
    private Date incorporateDate;

    @Transient
    private String description;
    @Transient
    private String itemName;
    @Transient
    private PLMItem item;
    @Transient
    private List<PLMItemRevisionStatusHistory> statusHistory = new ArrayList<>();
    @Transient
    private PLMECO plmeco;
    @Transient
    private PLMDCO dco;
    @Transient
    private String oldRevision;
    @Transient
    private List<PLMItemRevision> plmItemRevisions = new ArrayList<>();
    @Transient
    private List<PLMItemAttribute> itemAttributes = new ArrayList<>();
    @Transient
    private List<ObjectAttribute> objectAttributes = new ArrayList<>();
    @Transient
    private List<PLMItemTypeAttribute> plmItemTypeAttributes = new ArrayList<>();

    public PLMItemRevision() {
        super(PLMObjectType.ITEMREVISION);
    }

}
