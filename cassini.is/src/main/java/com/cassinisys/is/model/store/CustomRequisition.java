package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "CUSTOM_REQUISITION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class CustomRequisition extends CassiniObject {

    @Transient
    List<CustomRequisitionItem> customRequisitionItems;
    @ApiObjectField(required = true)
    @Column(name = "REQUISITION_NUMBER")
    private String requisitionNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROJECT")
    @ApiObjectField(required = true)
    private ISProject project;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STORE")
    @ApiObjectField(required = true)
    private ISTopStore store;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @ApiObjectField(required = true)
    @Temporal(TemporalType.DATE)
    @Column(name = "REQUESTED_DATE")
    private Date requestedDate = new Date();
    @ApiObjectField(required = true)
    @Column(name = "REQUESTED_BY")
    private String requestedBy;
    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.store.RequisitionStatus")})
    @Column(name = "STATUS", nullable = false)
    private RequisitionStatus status = RequisitionStatus.NEW;
    @ApiObjectField(required = true)
    @Column(name = "APPROVED_BY")
    private String approvedBy;
    @ApiObjectField(required = true)
    @Column(name = "NOTES")
    private String notes;

    public CustomRequisition() {
        super(ISObjectType.CUSTOM_REQUISITION);
    }

    public List<CustomRequisitionItem> getCustomRequisitionItems() {
        return customRequisitionItems;
    }

    public void setCustomRequisitionItems(List<CustomRequisitionItem> customRequisitionItems) {
        this.customRequisitionItems = customRequisitionItems;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public ISProject getProject() {
        return project;
    }

    public void setProject(ISProject project) {
        this.project = project;
    }

    public ISTopStore getStore() {
        return store;
    }

    public void setStore(ISTopStore store) {
        this.store = store;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public RequisitionStatus getStatus() {
        return status;
    }

    public void setStatus(RequisitionStatus status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}
