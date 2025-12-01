package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
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
@Table(name = "CUSTOM_RECEIVECHALAN")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class CustomReceiveChalan extends CassiniObject {

    @Transient
    List<CustomReceiveItem> customReceiveItems;
    @ApiObjectField(required = true)
    @Column(name = "CHALAN_NO")
    private String chalanNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROJECT")
    @ApiObjectField(required = true)
    private ISProject project;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STORE")
    @ApiObjectField(required = true)
    private ISTopStore store;
    @ApiObjectField(required = true)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECEIVED_DATE")
    private Date receivedDate = new Date();
    @ApiObjectField(required = true)
    @Column(name = "RECEIVED_FROM")
    private String receivedFrom;
    @ApiObjectField(required = true)
    @Column(name = "CONSIGNEE")
    private String consignee;

    protected CustomReceiveChalan() {
        super(ISObjectType.CUSTOM_RECEIVECHALAN);
    }

    public List<CustomReceiveItem> getCustomReceiveItems() {
        return customReceiveItems;
    }

    public void setCustomReceiveItems(List<CustomReceiveItem> customReceiveItems) {
        this.customReceiveItems = customReceiveItems;
    }

    public String getChalanNumber() {
        return chalanNumber;
    }

    public void setChalanNumber(String chalanNumber) {
        this.chalanNumber = chalanNumber;
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

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

}
