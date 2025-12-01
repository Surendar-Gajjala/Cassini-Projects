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

@Entity
@Table(name = "IS_MANPOWERTYPE")
@PrimaryKeyJoinColumn(name = "TYPE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISManpowerType extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARENT_TYPE")
    @ApiObjectField(required = true)
    private Integer parentType;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "MANPOWERNUMBER_SOURCE", nullable = true)
    @ApiObjectField
    private AutoNumber manpowerNumberSource;

    @Transient
    private List<ISManpowerType> children = new ArrayList<>();

    public ISManpowerType() {
        super(ISObjectType.MANPOWERTYPE);
    }

    public AutoNumber getManpowerNumberSource() {
        return manpowerNumberSource;
    }

    public void setManpowerNumberSource(AutoNumber manpowerNumberSource) {
        this.manpowerNumberSource = manpowerNumberSource;
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

    public List<ISManpowerType> getChildren() {
        return children;
    }

    public void setChildren(List<ISManpowerType> children) {
        this.children = children;
    }
}
