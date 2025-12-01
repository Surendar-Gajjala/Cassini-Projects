package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.model.security.IDtoType;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 03-08-2021.
 */
@Data
public class ItemsDto implements IDtoType {
    private Integer id;
    private String itemNumber;
    private String itemName;
    private String itemTypeName;
    private String description;
    private String units;
    private String revision;
    private PLMLifeCyclePhase lifeCyclePhase;
    private ItemClass itemClass;
    private MakeOrBuy makeOrBuy;
    private Boolean released = Boolean.FALSE;
    private Boolean rejected = Boolean.FALSE;
    private Integer latestRevision;
    private Boolean hasThumbnail = Boolean.FALSE;
    private PLMItemType itemType;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date releasedDate;
    private PLMItemRevision latestRevisionObject;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private Integer createdBy;
    private Integer modifiedBy;
    private String createdByName;
    private String modifiedByName;
    private Boolean hasFiles = Boolean.FALSE;
    private Boolean configurable = Boolean.FALSE;
    private Boolean configured = Boolean.FALSE;
    private Boolean hasChanges = Boolean.FALSE;
    private Boolean hasVariance = Boolean.FALSE;
    private Boolean hasQuality = Boolean.FALSE;
    private Boolean hasMfrParts = Boolean.FALSE;
    private Boolean lockObject = Boolean.FALSE;
    private Boolean hasBom = Boolean.FALSE;
    private String lockedByName;
    private Integer lockedBy;
    private Boolean hasAlternates = Boolean.FALSE;
    private List<FileDto> itemFiles = new LinkedList<>();
    private List<PLMObjectDocument> objectDocuments = new LinkedList<>();
    private List<PLMSubscribe> subscribes = new LinkedList<>();
    private String objectType = "ITEM";
    private Integer tagsCount = 0;

    @Override
    public String getObjectType() {
        return "ITEM";
    }
}
