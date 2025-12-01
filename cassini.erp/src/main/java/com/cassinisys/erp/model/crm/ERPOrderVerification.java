package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.converters.CustomDateDeserializer;
import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
 * Created by reddy on 11/3/15.
 */
@Entity
@Table(name = "ERP_ORDERVERIFICATION")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "CRM")
public class ERPOrderVerification implements Serializable {
    @ApiObjectField
    private Integer id;
    @ApiObjectField
    private ERPCustomerOrder order;
    @ApiObjectField
    private Date assignedDate;
    @ApiObjectField
    private ERPEmployee assignedBy;
    @ApiObjectField
    private Date verifiedDate;
    @ApiObjectField
    private ERPEmployee verifiedBy;
    @ApiObjectField
    private OrderVerificationStatus status = OrderVerificationStatus.PENDING;

    @Id
    @SequenceGenerator(name = "OBJECT_ID_GEN", sequenceName = "OBJECT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OBJECT_ID_GEN")
    @Column(name = "VERIFICATION_ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    public ERPCustomerOrder getOrder() {
        return order;
    }

    public void setOrder(ERPCustomerOrder order) {
        this.order = order;
    }

    @Column(name = "ASSIGNED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ASSIGNED_BY", nullable = false)
    public ERPEmployee getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(ERPEmployee assignedBy) {
        this.assignedBy = assignedBy;
    }

    @Column(name = "VERIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    public Date getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(Date verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VERIFIED_BY", nullable = false)
    public ERPEmployee getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(ERPEmployee verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    @Column(name = "STATUS")
    @Type(type = "com.cassinisys.erp.converters.EnumUserType",
            parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.erp.model.crm.OrderVerificationStatus") })
    public OrderVerificationStatus getStatus() {
        return status;
    }

    public void setStatus(OrderVerificationStatus status) {
        this.status = status;
    }
}
