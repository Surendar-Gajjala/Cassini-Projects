package com.cassinisys.drdo.model.transactions;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.bom.Bom;
import com.cassinisys.drdo.model.procurement.Supplier;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 09-10-2018.
 */
@Entity
@Table(name = "INWARD")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class Inward extends CassiniObject {

    @ApiObjectField
    @Column(name = "NUMBER")
    private String number;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOM")
    private Bom bom;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GATEPASS")
    private GatePass gatePass;

    @ApiObjectField
    @Column(name = "NOTES")
    private String notes;

    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.transactions.InwardStatus")})
    @Column(name = "STATUS", nullable = false)
    private InwardStatus status = InwardStatus.SECURITY;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SUPPLIER")
    private Supplier supplier;

    @Column(name = "UNDER_REVIEW")
    private Boolean underReview = Boolean.FALSE;

    @Transient
    private Boolean provisionalAcceptItems = Boolean.FALSE;

    @Transient
    private Boolean itemsExist = Boolean.FALSE;

    @Transient
    private Boolean showNewBadge = Boolean.FALSE;

    @Transient
    private InwardItem inwardItem;

    @Transient
    private List<InwardStatusHistory> statusHistories = new ArrayList<>();

    public Inward() {
        super(DRDOObjectType.INWARD);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Bom getBom() {
        return bom;
    }

    public void setBom(Bom bom) {
        this.bom = bom;
    }

    public GatePass getGatePass() {
        return gatePass;
    }

    public void setGatePass(GatePass gatePass) {
        this.gatePass = gatePass;
    }

    public InwardStatus getStatus() {
        return status;
    }

    public void setStatus(InwardStatus status) {
        this.status = status;
    }

    public List<InwardStatusHistory> getStatusHistories() {
        return statusHistories;
    }

    public void setStatusHistories(List<InwardStatusHistory> statusHistories) {
        this.statusHistories = statusHistories;
    }

    public Boolean getUnderReview() {
        return underReview;
    }

    public void setUnderReview(Boolean underReview) {
        this.underReview = underReview;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Boolean getProvisionalAcceptItems() {
        return provisionalAcceptItems;
    }

    public void setProvisionalAcceptItems(Boolean provisionalAcceptItems) {
        this.provisionalAcceptItems = provisionalAcceptItems;
    }

    public Boolean getItemsExist() {
        return itemsExist;
    }

    public void setItemsExist(Boolean itemsExist) {
        this.itemsExist = itemsExist;
    }

    public Boolean getShowNewBadge() {
        return showNewBadge;
    }

    public void setShowNewBadge(Boolean showNewBadge) {
        this.showNewBadge = showNewBadge;
    }

    public InwardItem getInwardItem() {
        return inwardItem;
    }

    public void setInwardItem(InwardItem inwardItem) {
        this.inwardItem = inwardItem;
    }
}

