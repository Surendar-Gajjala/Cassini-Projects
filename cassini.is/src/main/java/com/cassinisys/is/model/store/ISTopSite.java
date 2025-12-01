package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.common.LocationAwareObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Entity
@Table(name = "IS_TOPSITE")
@PrimaryKeyJoinColumn(name = "SITE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class ISTopSite extends LocationAwareObject {

    @Column(name = "SITE_NAME", nullable = false)
    @ApiObjectField(required = true)
    private String siteName;

    @Column(name = "DESCRIPTION", nullable = false)
    @ApiObjectField(required = true)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STORE")
    @ApiObjectField(required = true)
    private ISTopStore store;

    public ISTopSite() {
        super(ISObjectType.SITE);
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ISTopStore getStore() {
        return store;
    }

    public void setStore(ISTopStore store) {
        this.store = store;
    }
}
