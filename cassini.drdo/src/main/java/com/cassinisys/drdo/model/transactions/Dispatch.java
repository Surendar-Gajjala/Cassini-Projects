package com.cassinisys.drdo.model.transactions;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.bom.Bom;
import com.cassinisys.drdo.model.bom.ItemInstance;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subra on 26-11-2018.
 */
@Entity
@Table(name = "DISPATCH")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class Dispatch extends CassiniObject {

    @ApiObjectField
    @Column(name = "NUMBER")
    private String number;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOM")
    private Bom bom;

    @ApiObjectField
    @Column(name = "GATEPASS_NUMBER")
    private String gatePassNumber;

    @ApiObjectField
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.transactions.DispatchStatus")})
    @Column(name = "STATUS")
    private DispatchStatus status = DispatchStatus.NEW;

    @ApiObjectField
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.transactions.DispatchType")})
    @Column(name = "TYPE")
    private DispatchType type = DispatchType.REJECTED;

    @ApiObjectField
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DISPATCH_DATE")
    private Date dispatchDate;

    @Transient
    private List<ItemInstance> itemInstances = new ArrayList<>();

    @Transient
    private List<DispatchItem> dispatchItems = new ArrayList<>();

    public Dispatch() {
        super(DRDOObjectType.DISPATCH);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Bom getBom() {
        return bom;
    }

    public void setBom(Bom bom) {
        this.bom = bom;
    }

    public String getGatePassNumber() {
        return gatePassNumber;
    }

    public void setGatePassNumber(String gatePassNumber) {
        this.gatePassNumber = gatePassNumber;
    }

    public DispatchStatus getStatus() {
        return status;
    }

    public void setStatus(DispatchStatus status) {
        this.status = status;
    }

    public Date getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public List<ItemInstance> getItemInstances() {
        return itemInstances;
    }

    public void setItemInstances(List<ItemInstance> itemInstances) {
        this.itemInstances = itemInstances;
    }

    public List<DispatchItem> getDispatchItems() {
        return dispatchItems;
    }

    public void setDispatchItems(List<DispatchItem> dispatchItems) {
        this.dispatchItems = dispatchItems;
    }

    public DispatchType getType() {
        return type;
    }

    public void setType(DispatchType type) {
        this.type = type;
    }
}
