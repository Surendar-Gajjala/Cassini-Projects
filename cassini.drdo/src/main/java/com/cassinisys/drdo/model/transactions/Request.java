package com.cassinisys.drdo.model.transactions;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.bom.BomInstance;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@Entity
@Table(name = "REQUEST")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class Request extends CassiniObject {

    @ApiObjectField
    @Column(name = "REQ_NUMBER")
    private String reqNumber;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOM_INSTANCE")
    private BomInstance bomInstance;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REQUESTED_BY")
    private Person requestedBy;

    @ApiObjectField
    @Column(name = "REQUESTED_DATE")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedDate;

    @ApiObjectField
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.transactions.RequestStatus")})
    @Column(name = "STATUS")
    private RequestStatus status = RequestStatus.REQUESTED;

    @ApiObjectField
    @Column(name = "NOTES")
    private String notes;

    @ApiObjectField
    @Column(name = "ISSUED")
    private Boolean issued = Boolean.FALSE;

    @Column(name = "REASON")
    private String reason;

    @Column(name = "VERSITY")
    private Boolean versity = Boolean.FALSE;

    @Transient
    private List<RequestStatusHistory> statusHistories = new ArrayList<>();

    @Transient
    private Boolean newRequest = Boolean.FALSE;

    @Transient
    private Boolean showApprove = Boolean.FALSE;

    @Transient
    private Boolean showAcceptAll = Boolean.FALSE;

    @Transient
    private Integer section;

    @Transient
    private Integer subsystem;

    @Transient
    private Integer unit;

    @Transient
    private List<Integer> unitIds = new ArrayList<>();

    @Transient
    private String message;

    public Request() {
        super(DRDOObjectType.REQUEST);
    }

    public String getReqNumber() {
        return reqNumber;
    }

    public void setReqNumber(String reqNumber) {
        this.reqNumber = reqNumber;
    }

    public BomInstance getBomInstance() {
        return bomInstance;
    }

    public void setBomInstance(BomInstance bomInstance) {
        this.bomInstance = bomInstance;
    }

    public Person getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Person requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<RequestStatusHistory> getStatusHistories() {
        return statusHistories;
    }

    public void setStatusHistories(List<RequestStatusHistory> statusHistories) {
        this.statusHistories = statusHistories;
    }

    public Boolean getIssued() {
        return issued;
    }

    public void setIssued(Boolean issued) {
        this.issued = issued;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getNewRequest() {
        return newRequest;
    }

    public void setNewRequest(Boolean newRequest) {
        this.newRequest = newRequest;
    }

    public Boolean getShowApprove() {
        return showApprove;
    }

    public void setShowApprove(Boolean showApprove) {
        this.showApprove = showApprove;
    }

    public Boolean getShowAcceptAll() {
        return showAcceptAll;
    }

    public void setShowAcceptAll(Boolean showAcceptAll) {
        this.showAcceptAll = showAcceptAll;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public Integer getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(Integer subsystem) {
        this.subsystem = subsystem;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Integer> getUnitIds() {
        return unitIds;
    }

    public void setUnitIds(List<Integer> unitIds) {
        this.unitIds = unitIds;
    }

    public Boolean getVersity() {
        return versity;
    }

    public void setVersity(Boolean versity) {
        this.versity = versity;
    }
}
