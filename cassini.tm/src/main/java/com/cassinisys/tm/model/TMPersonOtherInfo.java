package com.cassinisys.tm.model;

import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Rajabrahmachary on 10-08-2016.
 */
@Entity
@Table(name = "PERSON_OTHERINFO")
public class TMPersonOtherInfo implements Serializable {

    @Id
    @SequenceGenerator(name = "OTHER_INFO_ID_GEN", sequenceName = "OTHER_INFO_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OTHER_INFO_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "ROLE", nullable = false)
    @ApiObjectField(required = true)
    private String role;

    @Column(name = "DEPARTMENT", nullable = false)
    @ApiObjectField(required = true)
    private Integer department;

    @Column(name = "DESIGNATION", nullable = false)
    @ApiObjectField(required = true)
    private String designation;

    @Column(name = "DIVISION", nullable = false)
    @ApiObjectField(required = true)
    private String devision;

    @Column(name = "PERSON", nullable = false)
    private Integer person;

    @Column(name = "PARENT_UNIT", nullable = false)
    private String parentUnit;

    @Column(name = "CONTROLLING_OFFICER", nullable = false)
    private String controllingOfficer;

    @Column(name = "CONTROLLING_OFFICER_CONTACT", nullable = false)
    private String controllingOfficerContact;

    @Column(name = "BLOOD_GROUP", nullable = false)
    private String bloodGroup;

    @Column(name = "MEDICAL_PROBLEMS", nullable = false)
    private String medicalProblems;


    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}


    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}

    public Integer getPerson() {return person;}

    public void setPerson(Integer person) {this.person = person;}

    public Integer getDepartment() {return department;}

    public void setDepartment(Integer department) {this.department = department;}

    public String getDesignation() {return designation;}

    public String getDevision() {return devision;}

    public void setDevision(String devision) {this.devision = devision;}

    public void setDesignation(String designation) {this.designation = designation;}

    public String getParentUnit() {
        return parentUnit;
    }

    public void setParentUnit(String parentUnit) {
        this.parentUnit = parentUnit;
    }

    public String getControllingOfficer() {
        return controllingOfficer;
    }

    public void setControllingOfficer(String controllingOfficer) {
        this.controllingOfficer = controllingOfficer;
    }

    public String getControllingOfficerContact() {
        return controllingOfficerContact;
    }

    public void setControllingOfficerContact(String controllingOfficerContact) {
        this.controllingOfficerContact = controllingOfficerContact;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getMedicalProblems() {
        return medicalProblems;
    }

    public void setMedicalProblems(String medicalProblems) {
        this.medicalProblems = medicalProblems;
    }
}
