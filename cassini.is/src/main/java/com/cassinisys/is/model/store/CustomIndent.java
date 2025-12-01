package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Entity
@Table(name = "CUSTOM_INDENT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class CustomIndent extends CassiniObject {

    @Transient
    @JsonManagedReference
    List<CustomIndentItem> customIndentItems;
    @ApiObjectField(required = true)
    @Column(name = "INDENT_NUMBER")
    private String indentNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROJECT")
    @ApiObjectField(required = true)
    private ISProject project;
    @Column(name = "STORE")
    @ApiObjectField(required = true)
    private Integer store;
    @ApiObjectField(required = true)
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "RAISED_DATE")
    private Date raisedDate;
    @ApiObjectField(required = true)
    @Column(name = "RAISED_BY")
    private String raisedBy;
    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.store.IndentStatus")})
    @Column(name = "STATUS", nullable = false)
    private IndentStatus status = IndentStatus.NEW;
    @ApiObjectField(required = true)
    @Column(name = "APPROVED_BY")
    private String approvedBy;
    @ApiObjectField(required = true)
    @Column(name = "NOTES")
    private String notes;
    @Transient
    private List<CustomRequisition> customRequisitions;

    protected CustomIndent() {
        super(ISObjectType.CUSTOM_INDENT);
    }

    public List<CustomRequisition> getCustomRequisitions() {
        return customRequisitions;
    }

    public void setCustomRequisitions(List<CustomRequisition> customRequisitions) {
        this.customRequisitions = customRequisitions;
    }

    public List<CustomIndentItem> getCustomIndentItems() {
        return customIndentItems;
    }

    public void setCustomIndentItems(List<CustomIndentItem> customIndentItems) {
        this.customIndentItems = customIndentItems;
    }

    public String getIndentNumber() {
        return indentNumber;
    }

    public void setIndentNumber(String indentNumber) {
        this.indentNumber = indentNumber;
    }

    public ISProject getProject() {
        return project;
    }

    public void setProject(ISProject project) {
        this.project = project;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public Date getRaisedDate() {
        return raisedDate;
    }

    public void setRaisedDate(Date raisedDate) {
        this.raisedDate = raisedDate;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public IndentStatus getStatus() {
        return status;
    }

    public void setStatus(IndentStatus status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
