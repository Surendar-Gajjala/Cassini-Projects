package com.cassinisys.plm.model.rm;

import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "RM_SPECREQUIREMENT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecRequirement extends SpecElement {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REQUIREMENT")
    private Requirement requirement;

    @Transient
    private Integer reqEdits;

    public SpecRequirement() {
        super(PLMObjectType.SPECREQUIREMENT);
        setType(SpecElementType.REQUIREMENT);
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public Integer getReqEdits() {
        return reqEdits;
    }

    public void setReqEdits(Integer reqEdits) {
        this.reqEdits = reqEdits;
    }
}
