package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.procm.ISMaterialIssueType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by swapna on 31/07/18.
 */
@Entity
@Table(name = "IS_STOCKISSUE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISStockIssue extends CassiniObject {

    @Column
    private String name;

    @Column
    private String notes;

    @Column
    private Integer store;

    @Column
    private Integer project;

    @Column
    private Integer task;

    @Column(name = "ISSUED_TO")
    private Integer issuedTo;

    @Column(name = "ISSUENUMBER_SOURCE")
    private String issueNumberSource;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ISSUE_TYPE")
    @ApiObjectField(required = true)
    private ISMaterialIssueType materialIssueType;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @ApiObjectField(required = true)
    @Temporal(TemporalType.DATE)
    @Column(name = "ISSUE_DATE")
    private Date issueDate;

    @Transient
    private List<ISTStockIssueItem> stockIssueItems;

    @Transient
    private String issuedToName;

    public ISStockIssue() {
        super(ISObjectType.ISSUE);
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

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
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

    public List<ISTStockIssueItem> getStockIssueItems() {
        return stockIssueItems;
    }

    public void setStockIssueItems(List<ISTStockIssueItem> stockIssueItems) {
        this.stockIssueItems = stockIssueItems;
    }

    public ISMaterialIssueType getMaterialIssueType() {
        return materialIssueType;
    }

    public void setMaterialIssueType(ISMaterialIssueType materialIssueType) {
        this.materialIssueType = materialIssueType;
    }

    public String getIssuedToName() {
        return issuedToName;
    }

    public void setIssuedToName(String issuedToName) {
        this.issuedToName = issuedToName;
    }
}
