package com.cassinisys.drdo.model.bom;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subra on 04-10-2018.
 */
@Entity
@Table(name = "ITEMTYPEATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class ItemTypeAttribute extends ObjectTypeAttribute {

    @Column(name = "ITEM_TYPE")
    @ApiObjectField(required = true)
    private Integer itemType;

    @Column(name = "REVISION_SPECIFIC", nullable = false)
    @ApiObjectField(required = true)
    private Boolean revisionSpecific;

    @Column(name = "CHANGE_CONTROLLED", nullable = false)
    @ApiObjectField(required = true)
    private Boolean changeControlled;

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Boolean getRevisionSpecific() {
        return revisionSpecific;
    }

    public void setRevisionSpecific(Boolean revisionSpecific) {
        this.revisionSpecific = revisionSpecific;
    }

    public Boolean getChangeControlled() {
        return changeControlled;
    }

    public void setChangeControlled(Boolean changeControlled) {
        this.changeControlled = changeControlled;
    }
}
