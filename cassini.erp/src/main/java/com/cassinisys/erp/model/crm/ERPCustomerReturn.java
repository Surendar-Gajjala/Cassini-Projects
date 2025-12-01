package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mysema.query.annotations.QueryInit;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ERP_CUSTOMERRETURN")
@PrimaryKeyJoinColumn(name = "RETURN_ID")
@ApiObject(group = "CRM")
public class ERPCustomerReturn extends ERPObject {

    @ApiObjectField(required = true)
    private ERPCustomer customer;
    @ApiObjectField(required = true)
    private Date returnDate;
    @ApiObjectField(required = true)
    private String reason;
    @ApiObjectField(required = true)
    private List<ERPCustomerReturnDetails> details;
    @ApiObjectField(required = true)
    private ReturnStatus status = ReturnStatus.PENDING;

    public ERPCustomerReturn() {
        super.setObjectType(ObjectType.CUSTOMERRETURN);
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    @QueryInit("salesRep")
    public ERPCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(ERPCustomer customer) {
        this.customer = customer;
    }

    @Column(name = "RETURN_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Column(name = "REASON", nullable = false)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Column(name = "STATUS", nullable = false)
    @Type(type = "com.cassinisys.erp.converters.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.erp.model.crm.ReturnStatus")})
    public ReturnStatus getStatus() {
        return status;
    }

    public void setStatus(ReturnStatus status) {
        this.status = status;
    }

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customerReturn", cascade = CascadeType.ALL)
    public List<ERPCustomerReturnDetails> getDetails() {
        return details;
    }

    public void setDetails(List<ERPCustomerReturnDetails> details) {
        this.details = details;
    }

}
