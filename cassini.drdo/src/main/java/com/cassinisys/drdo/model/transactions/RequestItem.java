package com.cassinisys.drdo.model.transactions;

import com.cassinisys.drdo.model.bom.BomInstanceItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@Entity
@Table(name = "REQUESTITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class RequestItem implements Serializable {

    @Id
    @ApiObjectField(required = true)
    @SequenceGenerator(name = "REQUEST_ITEM_ID_GEN", sequenceName = "REQUEST_ITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUEST_ITEM_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REQUEST")
    private Request request;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM")
    private BomInstanceItem item;

    @Column(name = "QUANTITY")
    private Integer quantity = 0;

    @Column(name = "FRACTIONAL_QUANTITY")
    private Double fractionalQuantity = 0.0;

    @Column(name = "REJECTQTY")
    private Integer rejectQty = 0;

    @Column(name = "FRACTIONAL_REJECTQTY")
    private Double fractionalRejectQty = 0.0;

    @Column(name = "FAILUREQTY")
    private Integer failureQty = 0;

    @Column(name = "FRACTIONAL_FAILUREQTY")
    private Double fractionalFailureQty = 0.0;

    @Column(name = "REPLACEDQTY")
    private Integer replaceQty = 0;

    @Column(name = "FRACTIONAL_REPLACEDQTY")
    private Double fractionalReplaceQty = 0.0;

    @Column(name = "ACCEPTED")
    private Boolean accepted = Boolean.FALSE;

    @Column(name = "APPROVED")
    private Boolean approved = Boolean.FALSE;

    @ApiObjectField
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.transactions.RequestItemStatus")})
    @Column(name = "STATUS")
    private RequestItemStatus status = RequestItemStatus.PENDING;

    @Column(name = "REASON")
    private String reason;

    @Transient
    private Double allocatedQuantity = 0.0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public BomInstanceItem getItem() {
        return item;
    }

    public void setItem(BomInstanceItem item) {
        this.item = item;
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

    public Integer getRejectQty() {
        return rejectQty;
    }

    public void setRejectQty(Integer rejectQty) {
        this.rejectQty = rejectQty;
    }

    public Double getFractionalRejectQty() {
        return fractionalRejectQty;
    }

    public void setFractionalRejectQty(Double fractionalRejectQty) {
        this.fractionalRejectQty = fractionalRejectQty;
    }

    public Integer getFailureQty() {
        return failureQty;
    }

    public void setFailureQty(Integer failureQty) {
        this.failureQty = failureQty;
    }

    public Double getFractionalFailureQty() {
        return fractionalFailureQty;
    }

    public void setFractionalFailureQty(Double fractionalFailureQty) {
        this.fractionalFailureQty = fractionalFailureQty;
    }

    public Integer getReplaceQty() {
        return replaceQty;
    }

    public void setReplaceQty(Integer replaceQty) {
        this.replaceQty = replaceQty;
    }

    public Double getFractionalReplaceQty() {
        return fractionalReplaceQty;
    }

    public void setFractionalReplaceQty(Double fractionalReplaceQty) {
        this.fractionalReplaceQty = fractionalReplaceQty;
    }

    public Double getAllocatedQuantity() {
        return allocatedQuantity;
    }

    public void setAllocatedQuantity(Double allocatedQuantity) {
        this.allocatedQuantity = allocatedQuantity;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public RequestItemStatus getStatus() {
        return status;
    }

    public void setStatus(RequestItemStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
