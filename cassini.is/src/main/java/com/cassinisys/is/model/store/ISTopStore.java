package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Entity
@Table(name = "IS_TOPSTORE")
@PrimaryKeyJoinColumn(name = "STORE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class ISTopStore extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "STORE_NAME", nullable = false)
    @ApiObjectField(required = true)
    private String storeName;

    @Column(name = "DESCRIPTION", nullable = false)
    @ApiObjectField(required = true)
    private String description;

    @Column(name = "LOCATION_NAME")
    @ApiObjectField
    private String locationName;

    public ISTopStore() {
        super(ISObjectType.STORE);
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
