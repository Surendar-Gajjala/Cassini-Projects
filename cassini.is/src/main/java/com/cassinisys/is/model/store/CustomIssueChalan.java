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
@Table(name = "CUSTOM_ISSUECHALAN")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class CustomIssueChalan extends CassiniObject {

    @Transient
    List<CustomIssueChalan> customIndentItems;
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
    @Column(name = "ISSUED_DATE")
    private Date issuedDate = new Date();
    @ApiObjectField(required = true)
    @Column(name = "ISSUED_TO")
    private String issuedTo;
    @ApiObjectField(required = true)
    @Column(name = "RELEASE_ORDER_NO")
    private String releaseOrderNumber;
    @ApiObjectField(required = true)
    @Column(name = "APPROVED_BY")
    private String approvedBy;

    protected CustomIssueChalan() {
        super(ISObjectType.CUSTOM_ISSUECHALAN);
    }

    public List<CustomIssueChalan> getCustomIndentItems() {
        return customIndentItems;
    }

    public void setCustomIndentItems(List<CustomIssueChalan> customIndentItems) {
        this.customIndentItems = customIndentItems;
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

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(String issuedTo) {
        this.issuedTo = issuedTo;
    }

    public String getReleaseOrderNumber() {
        return releaseOrderNumber;
    }

    public void setReleaseOrderNumber(String releaseOrderNumber) {
        this.releaseOrderNumber = releaseOrderNumber;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}
