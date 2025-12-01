package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_ITEM")
@PrimaryKeyJoinColumn(name = "ITEM_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMItem extends CassiniObject {

    @Transient
    List<ObjectAttribute> objectAttributes = new ArrayList<>();
    @Transient
    List<PLMItemAttribute> itemAttributes = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM_TYPE")
    private PLMItemType itemType;
    @Column(name = "ITEM_NUMBER")
    private String itemNumber;
    @Column(name = "ITEM_NAME")
    private String itemName;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "UNITS")
    private String units = "Each";
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters =
            {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.plm.MakeOrBuy")})
    @Column(name = "MAKE_OR_BUY")
    private MakeOrBuy makeOrBuy = MakeOrBuy.BUY;
    @Column(name = "THUMBNAIL")
    private byte[] thumbnail;
    @Column(name = "LATEST_REVISION")
    private Integer latestRevision;
    @Column(name = "LATEST_RELEASED_REVISION")
    private Integer latestReleasedRevision;
    @Column(name = "LOCK_OBJECT")
    private Boolean lockObject = Boolean.FALSE;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LOCKED_BY")
    private Person lockedBy;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOCKED_DATE", nullable = false)
    private Date lockedDate;
    @Column(name = "ITEM_FILE")
    private Integer itemImage = null;
    @Column(name = "CONFIGURABLE", nullable = false)
    private Boolean configurable = Boolean.FALSE;
    @Column(name = "CONFIGURED", nullable = false)
    private Boolean configured = Boolean.FALSE;
    @Column(name = "INSTANCE")
    private Integer instance;
    @Column(name = "REQUIRE_COMPLIANCE")
    private Boolean requireCompliance = Boolean.FALSE;
    @Column(name = "HAS_ALTERNATES")
    private Boolean hasAlternates = Boolean.FALSE;
    @Column(name = "DESIGN_OBJECT") //References PDM_MASTER_OBJECT (ID)
    private Integer designObject;
    @Column(name = "IS_TEMPORARY")
    private Boolean isTemporary = Boolean.FALSE;
    @Transient
    private List<PLMItemFile> itemFiles = new ArrayList<>();
    @Transient
    private PLMItemFile itemImageObj;
    @Transient
    private List<PLMSubscribe> subscribes = new ArrayList<>();
    @Transient
    private Boolean hasBom = Boolean.FALSE;
    @Transient
    private Boolean hasChanges = Boolean.FALSE;
    @Transient
    private Boolean hasVariance = Boolean.FALSE;
    @Transient
    private Boolean hasQuality = Boolean.FALSE;
    @Transient
    private Boolean hasMfrParts = Boolean.FALSE;
    @Transient
    private Integer typeId;
    @Transient
    private String typeName;
    @Transient
    private String ecoNumber;
    @Transient
    private Integer changeId;
    @Transient
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromDate;
    @Transient
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDate;

    @Transient
    private PLMItemRevision rev;

    @Transient
    private Integer workflowDefId;

    @Transient
    private Integer bomConfiguration;

    @Transient
    private String configurationName;

    @Transient
    private Integer count = 0;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private String toRevision;
    @Transient
    private String createdPerson;

    @Transient
    private String modifiedPerson;

    @Transient
    private String thumbnailString;

    @Transient
    private String iClass;

    @Transient
    private String make;

    @Transient
    private List<PLMLifeCyclePhase> toLifecyclePhases = new ArrayList<>();
    @Transient
    private PLMLifeCyclePhase lifeCyclePhase;
    @Transient
    private Boolean pendingEco = false;
    @Transient
    private Integer asReleasedRevision;
    @Transient
    private Boolean alreadyExist = false;
    @Transient
    private PLMItemRevision latestRevisionObject;
    @Transient
    private PLMItemRevision latestReleasedRevisionObject;


    public PLMItem() {
        super(PLMObjectType.ITEM);
    }


    public void setiClass(String iClass) {
        this.iClass = iClass;
    }

    public PLMLifeCyclePhase getLifecyclePhaseByName(String name) {
        PLMLifeCyclePhase phase = null;
        List<PLMLifeCyclePhase> phases = itemType.getLifecycle().getPhases();
        for (PLMLifeCyclePhase p : phases) {
            if (p.getPhase().equalsIgnoreCase(name)) {
                phase = p;
                break;
            }
        }
        return phase;
    }

    public String toCsvRow() {
        String csvRow = "";
        for (String value : Arrays.asList(super.getId().toString(), itemType.getName(),
                itemNumber, description, units, latestRevision.toString())) {
            String processed = value;
            if (value.contains("\"") || value.contains(",")) {
                processed = "\"" + value.replaceAll("\"", "\"\"") + "\"";
            }
            csvRow += "," + processed;
        }
        return csvRow.substring(1);
    }


}
