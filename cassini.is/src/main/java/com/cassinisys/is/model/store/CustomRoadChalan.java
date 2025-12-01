package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Entity
@Table(name = "CUSTOM_ROADCHALAN")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class CustomRoadChalan extends CassiniObject {

    @Transient
    List<CustomRoadChalanItem> customRoadChalanItems;
    @ApiObjectField(required = true)
    @Column(name = "CHALAN_NO")
    private String chalanNumber;
    @ApiObjectField(required = true)
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "CHALAN_DATE")
    private Date chalanDate = new Date();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STORE")
    @ApiObjectField(required = true)
    private ISTopStore store;
    @ApiObjectField(required = true)
    @Column(name = "GOING_FROM")
    private String goingFrom;
    @ApiObjectField(required = true)
    @Column(name = "GOING_TO")
    private String goingTo;
    @ApiObjectField(required = true)
    @Column(name = "MEANS_OF_TRANS")
    private String meansOfTrans;
    @ApiObjectField(required = true)
    @Column(name = "VEHICLE_DETAILS")
    private String vehicleDetails;
    @ApiObjectField(required = true)
    @Column(name = "ISSUING_AUTHORITY")
    private String issuingAuthority;

    protected CustomRoadChalan() {
        super(ISObjectType.CUSTOM_ROADCHALAN);
    }

    public String getChalanNumber() {
        return chalanNumber;
    }

    public void setChalanNumber(String chalanNumber) {
        this.chalanNumber = chalanNumber;
    }

    public Date getChalanDate() {
        return chalanDate;
    }

    public void setChalanDate(Date chalanDate) {
        this.chalanDate = chalanDate;
    }

    public ISTopStore getStore() {
        return store;
    }

    public void setStore(ISTopStore store) {
        this.store = store;
    }

    public String getGoingFrom() {
        return goingFrom;
    }

    public void setGoingFrom(String goingFrom) {
        this.goingFrom = goingFrom;
    }

    public String getGoingTo() {
        return goingTo;
    }

    public void setGoingTo(String goingTo) {
        this.goingTo = goingTo;
    }

    public String getMeansOfTrans() {
        return meansOfTrans;
    }

    public void setMeansOfTrans(String meansOfTrans) {
        this.meansOfTrans = meansOfTrans;
    }

    public String getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(String vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public List<CustomRoadChalanItem> getCustomRoadChalanItems() {
        return customRoadChalanItems;
    }

    public void setCustomRoadChalanItems(List<CustomRoadChalanItem> customRoadChalanItems) {
        this.customRoadChalanItems = customRoadChalanItems;
    }

}
