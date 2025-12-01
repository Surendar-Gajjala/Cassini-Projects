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
 * Created by reddy on 6/13/17.
 */
@Entity
@Table(name = "IS_MATERIALTYPE")
@PrimaryKeyJoinColumn(name = "TYPE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISMaterialType extends CassiniObject {

    @Transient
    List<ISMaterialType> children = new ArrayList<>();
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PARENT_TYPE")
    @ApiObjectField(required = false)
    private Integer parentType;
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "MATERIALNUMBER_SOURCE")
    @ApiObjectField
    private AutoNumber materialNumberSource;

    public ISMaterialType() {
        super(ISObjectType.MATERIALTYPE);
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

    public List<ISMaterialType> getChildren() {
        return children;
    }

    public void setChildren(List<ISMaterialType> children) {
        this.children = children;
    }

    public AutoNumber getMaterialNumberSource() {
        return materialNumberSource;
    }

    public void setMaterialNumberSource(AutoNumber materialNumberSource) {
        this.materialNumberSource = materialNumberSource;
    }

}
