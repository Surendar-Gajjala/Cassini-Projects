package com.cassinisys.pdm.model;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Lov;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Entity
@Table(name = "PDM_ITEMTYPE")
@PrimaryKeyJoinColumn(name = "TYPE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PDM")
public class PDMItemType extends CassiniObject{

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ApiObjectField(required = true)
    @Column(name = "PARENT_TYPE")
    private Integer parentType;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="ITEMNUMBER_SOURCE", nullable = true)
    @ApiObjectField
    private AutoNumber itemNumberSource;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="REVISION_SEQUENCE", nullable = true)
    @ApiObjectField
    private Lov revisionSequence;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name="LIFECYCLE_STATES", nullable = true)
    @ApiObjectField
    private Lov lifeCycleStates;

    @Transient
    List<PDMItemType> children = new ArrayList<PDMItemType>();

    public PDMItemType(){super(PDMObjectType.ITEMTYPE);}

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

    public AutoNumber getItemNumberSource() {
        return itemNumberSource;
    }

    public void setItemNumberSource(AutoNumber itemNumberSource) {
        this.itemNumberSource = itemNumberSource;
    }

    public Lov getRevisionSequence() {
        return revisionSequence;
    }

    public void setRevisionSequence(Lov revisionSequence) {
        this.revisionSequence = revisionSequence;
    }

    public Lov getLifeCycleStates() {
        return lifeCycleStates;
    }

    public void setLifeCycleStates(Lov lifeCycleStates) {
        this.lifeCycleStates = lifeCycleStates;
    }

    public List<PDMItemType> getChildren() {
        return children;
    }

    public void setChildren(List<PDMItemType> children) {
        this.children = children;
    }
}
