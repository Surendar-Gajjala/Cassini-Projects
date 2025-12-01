package com.cassinisys.is.model.procm;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapna on 20/06/19.
 */
@Entity
@Table(name = "IS_MATERIALRECEIVETYPE")
@PrimaryKeyJoinColumn(name = "TYPE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISMaterialReceiveType extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARENT_TYPE")
    @ApiObjectField(required = false)
    private Integer parentType;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "RECEIVENUMBER_SOURCE")
    @ApiObjectField
    private AutoNumber receiveNumberSource;

    @Transient
    private List<ISMaterialReceiveType> children = new ArrayList<>();

    public ISMaterialReceiveType() {
        super(ISObjectType.MATERIALRECEIVETYPE);
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

    public AutoNumber getReceiveNumberSource() {
        return receiveNumberSource;
    }

    public void setReceiveNumberSource(AutoNumber receiveNumberSource) {
        this.receiveNumberSource = receiveNumberSource;
    }

    public List<ISMaterialReceiveType> getChildren() {
        return children;
    }

    public void setChildren(List<ISMaterialReceiveType> children) {
        this.children = children;
    }
}
