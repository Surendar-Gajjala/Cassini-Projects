package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
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
@Table(name = "CUSTOM_PURCHASEORDER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class CustomPurchaseOrder extends CassiniObject {

    @Transient
    @JsonManagedReference
    List<CustomPurchaseOrderItem> customPurchaseOrdersItems;
    @ApiObjectField(required = true)
    @Column(name = "PO_NUMBER")
    private String poNumber;
    @ApiObjectField(required = true)
    @Column(name = "STORE")
    private Integer store;
    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.store.PurchaseOrderStatus")})
    @Column(name = "STATUS", nullable = false)
    private PurchaseOrderStatus status = PurchaseOrderStatus.NEW;
    @ApiObjectField(required = true)
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "PO_DATE")
    private Date poDate;
    @ApiObjectField(required = true)
    @Column(name = "SUPPLIER")
    private String supplier;
    @ApiObjectField(required = true)
    @Column(name = "APPROVED_BY")
    private String approvedBy;
    @ApiObjectField(required = true)
    @Column(name = "NOTES")
    private String notes;

    protected CustomPurchaseOrder() {
        super(ISObjectType.CUSTOM_PURCHASEORDER);
    }

    public List<CustomPurchaseOrderItem> getCustomPurchaseOrdersItems() {
        return customPurchaseOrdersItems;
    }

    public void setCustomPurchaseOrdersItems(List<CustomPurchaseOrderItem> customPurchaseOrdersItems) {
        this.customPurchaseOrdersItems = customPurchaseOrdersItems;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Date getPoDate() {
        return poDate;
    }

    public void setPoDate(Date poDate) {
        this.poDate = poDate;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
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
