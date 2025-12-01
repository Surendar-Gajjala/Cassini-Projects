package com.cassinisys.drdo.model.bom;

import com.cassinisys.drdo.model.transactions.Issue;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 19-12-2018.
 */
@Entity
@Table(name = "LOT_INSTANCE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class LotInstance extends CassiniObject {

    @Transient
    String certificateNumber;
    @Column(name = "INSTANCE")
    private Integer instance;
    @ApiObjectField
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.bom.ItemInstanceStatus")})
    @Column(name = "STATUS")
    private ItemInstanceStatus status = ItemInstanceStatus.ISSUE;
    @ApiObjectField
    @Column(name = "UPN_NUMBER")
    private String upnNumber;
    @Column(name = "LOT_QTY")
    private Double lotQty = 0.0;
    @Column(name = "ISSUE_ITEM")
    private Integer issueItem;
    @Column(name = "PRESENT_STATUS")
    private String presentStatus;
    @Column(name = "HAS_FAILED")
    private Boolean hasFailed = Boolean.FALSE;
    @Column(name = "SEQUENCE")
    private Integer sequence;

    @Transient
    private List<LotInstanceHistory> instanceHistories = new ArrayList<>();
    @Transient
    private ItemInstance itemInstance;

    @Transient
    private Issue issue;

    public Integer getInstance() {
        return instance;
    }

    public void setInstance(Integer instance) {
        this.instance = instance;
    }

    public ItemInstanceStatus getStatus() {
        return status;
    }

    public void setStatus(ItemInstanceStatus status) {
        this.status = status;
    }

    public String getUpnNumber() {
        return upnNumber;
    }

    public void setUpnNumber(String upnNumber) {
        this.upnNumber = upnNumber;
    }

    public Double getLotQty() {
        return lotQty;
    }

    public void setLotQty(Double lotQty) {
        this.lotQty = lotQty;
    }

    public Integer getIssueItem() {
        return issueItem;
    }

    public void setIssueItem(Integer issueItem) {
        this.issueItem = issueItem;
    }

    public List<LotInstanceHistory> getInstanceHistories() {
        return instanceHistories;
    }

    public void setInstanceHistories(List<LotInstanceHistory> instanceHistories) {
        this.instanceHistories = instanceHistories;
    }

    public String getPresentStatus() {
        return presentStatus;
    }

    public void setPresentStatus(String presentStatus) {
        this.presentStatus = presentStatus;
    }

    public Boolean getHasFailed() {
        return hasFailed;
    }

    public void setHasFailed(Boolean hasFailed) {
        this.hasFailed = hasFailed;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public ItemInstance getItemInstance() {
        return itemInstance;
    }

    public void setItemInstance(ItemInstance itemInstance) {
        this.itemInstance = itemInstance;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
