package com.cassinisys.plm.model.plm.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.plm.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 06-07-2021.
 */
@Data
public class BomDto {
    private Integer id;
    private Integer parent;
    private Integer item;
    private Integer substituteItem;
    private String substituteItemNumber;
    private Integer quantity;
    private String refdes;
    private String notes;
    private Integer asReleasedRevision;
    private Integer sequence;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveFrom;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveTo;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date itemEffectiveFrom;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date itemEffectiveTo;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date itemCreatedDate;

    private Boolean hasSubstitutes = Boolean.FALSE;

    private String itemNumber;
    private String itemName;
    private String description;
    private String itemTypeName;
    private String revision = "?";
    private PLMLifeCyclePhase lifeCyclePhase;
    private MakeOrBuy makeOrBuy;
    private Boolean hasBom = Boolean.FALSE;
    private Integer latestRevision;
    private LifeCyclePhaseType parentLifecyclePhaseType;
    private Integer parentItemMaster;
    private Boolean hasThumbnail = Boolean.FALSE;
    private Boolean configurable = Boolean.FALSE;
    private Boolean configured = Boolean.FALSE;
    private ItemClass itemClass;
    private String units;
    private Boolean lockObject = Boolean.FALSE;
    private Integer lockedBy;
    private Integer count = 0;
    private Integer mfrId;
    private Integer mfrPartId;
    private String mfrPartNumber;
    private String mfrPartName;

    private Boolean expanded = false;
    private Boolean isNew = false;
    private Integer level = 0;
    private Boolean editMode = false;
    private Boolean itemExist = false;
    private String thumbnailString;
    private Integer consumedQty = 0;
    private PLMObjectType objectType = PLMObjectType.BOMITEM;
    private List<BomDto> children = new LinkedList<>();
}
