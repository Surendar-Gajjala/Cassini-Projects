package com.cassinisys.is.model.procm;
/* Model for ISItemType */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 6/13/2016.
 */
@Entity
@Table(name = "IS_ITEMTYPE")
@PrimaryKeyJoinColumn(name = "TYPE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "PROCM")
public class ISItemType extends CassiniObject {

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    @ApiObjectField(required = true)
    private String description;

    @Column(name = "PARENT_TYPE", nullable = false)
    @ApiObjectField(required = true)
    private Integer parentType;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.pm.ResourceType")})
    @Column(name = "RESOURCE_TYPE")
    private ResourceType resourceType;

    @Transient
    private List<ISItemType> children = new ArrayList<>();

    public ISItemType() {
        super(ISObjectType.ITEMTYPE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParentType() {
        return parentType;
    }

    public void setParentType(Integer parentType) {
        this.parentType = parentType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public List<ISItemType> getChildren() {
        return children;
    }

    public void setChildren(List<ISItemType> children) {
        this.children = children;
    }

}
