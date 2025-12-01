package com.cassinisys.erp.model.store;

import com.cassinisys.erp.converters.CustomShortDateDeserializer;
import com.cassinisys.erp.converters.CustomShortDateSerializer;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ERP_STOCKISSUE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ERPStockIssue extends ERPObject {

    @Column
    private String name;

    @Column
    private String notes;

    @Column
    private Integer store;

    @Column(name = "ISSUED_TO")
    private Integer issuedTo;

    @Column(name = "ISSUENUMBER_SOURCE")
    private String issueNumberSource;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @ApiObjectField(required = true)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ISSUE_DATE")
    private Date issueDate;

    public ERPStockIssue() {
        super.setObjectType(ObjectType.ISSUE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public String getIssueNumberSource() {
        return issueNumberSource;
    }

    public void setIssueNumberSource(String issueNumberSource) {
        this.issueNumberSource = issueNumberSource;
    }

    public Integer getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(Integer issuedTo) {
        this.issuedTo = issuedTo;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
}
