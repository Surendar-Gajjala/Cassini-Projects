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
@Table(name = "IS_MACHINETYPE")
@PrimaryKeyJoinColumn(name = "TYPE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISMachineType extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARENT_TYPE")
    @ApiObjectField(required = true)
    private Integer parentType;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "MACHINENUMBER_SOURCE", nullable = true)
    @ApiObjectField
    private AutoNumber machineNumberSource;

    @Transient
    private List<ISMachineType> children = new ArrayList<>();

    public ISMachineType() {
        super(ISObjectType.MACHINETYPE);
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

    public AutoNumber getMachineNumberSource() {
        return machineNumberSource;
    }

    public void setMachineNumberSource(AutoNumber machineNumberSource) {
        this.machineNumberSource = machineNumberSource;
    }

    public List<ISMachineType> getChildren() {
        return children;
    }

    public void setChildren(List<ISMachineType> children) {
        this.children = children;
    }

}
