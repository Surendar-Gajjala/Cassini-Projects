package com.cassinisys.tm.model;

import com.cassinisys.platform.model.common.Person;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by Rajabrahmachary on 05-07-2016.
 */
@Entity
@Table(name = "EMERGENCY_CONTACT")
@PrimaryKeyJoinColumn(name = "CONTACT_ID")
@ApiObject(name = "TM")
public class TMEmergencyContact extends Person {

    @Column(name = "PERSON", nullable = false)
    @ApiObjectField(required = true)
    private Integer person;

    @Column(name = "RELATION", nullable = false)
    @ApiObjectField(required = true)
    private String relation;

    public String getRelation() {return relation;}
    public void setRelation(String relation) {this.relation = relation;}

    public Integer getPerson() {
        return person;
    }
    public void setPerson(Integer person) {
        this.person = person;
    }

}
