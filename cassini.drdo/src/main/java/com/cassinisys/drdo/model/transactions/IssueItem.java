package com.cassinisys.drdo.model.transactions;

import com.cassinisys.drdo.model.bom.BomItemInstance;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by subra on 17-10-2018.
 */
@Entity
@Table(name = "ISSUEITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class IssueItem implements Serializable {

    @Id
    @ApiObjectField(required = true)
    @SequenceGenerator(name = "ISSUE_ITEM_ID_GEN", sequenceName = "ISSUE_ITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ISSUE_ITEM_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ISSUE")
    private Issue issue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOMITEMINSTANCE")
    private BomItemInstance bomItemInstance;

    @Column(name = "QUANTITY")
    private Integer quantity = 0;

    @Column(name = "FRACTIONAL_QUANTITY")
    private Double fractionalQuantity = 0.0;

    @Column(name = "REQUEST_ITEM")
    private Integer requestItem;

    @Column(name = "APPROVED")
    private Boolean approved = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "APPROVED_BY")
    private Person approvedBy;

    @ApiObjectField
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.transactions.IssueItemStatus")})
    @Column(name = "STATUS")
    private IssueItemStatus status = IssueItemStatus.PENDING;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPROVED_DATE")
    private Date approvedDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECEIVED_DATE")
    private Date receivedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public BomItemInstance getBomItemInstance() {
        return bomItemInstance;
    }

    public void setBomItemInstance(BomItemInstance bomItemInstance) {
        this.bomItemInstance = bomItemInstance;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getFractionalQuantity() {
        return fractionalQuantity;
    }

    public void setFractionalQuantity(Double fractionalQuantity) {
        this.fractionalQuantity = fractionalQuantity;
    }

    public Integer getRequestItem() {
        return requestItem;
    }

    public void setRequestItem(Integer requestItem) {
        this.requestItem = requestItem;
    }

    public Person getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Person approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public IssueItemStatus getStatus() {
        return status;
    }

    public void setStatus(IssueItemStatus status) {
        this.status = status;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }
}
