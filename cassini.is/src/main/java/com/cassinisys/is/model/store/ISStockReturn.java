package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
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
import java.util.Date;
import java.util.List;

/**
 * Created by swapna on 04/12/18.
 */
@Entity
@Table(name = "IS_STOCKRETURN")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "STORE")
public class ISStockReturn extends CassiniObject {

    @Column
    private String notes;

    @Column
    private Integer store;

    @Column
    private Integer project;

    @Column(name = "RETURNED_TO")
    private Integer returnedTo;

    @Column(name = "STOCKRETURN_NUMBERSOURCE")
    private String returnNumberSource;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @ApiObjectField(required = true)
    @Temporal(TemporalType.DATE)
    @Column(name = "RETURNED_DATE")
    private Date returnedDate;
    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.store.StockReturnStatus")})
    @Column(name = "STATUS", nullable = false)
    private StockReturnStatus status = StockReturnStatus.NEW;
    @ApiObjectField(required = true)
    @Column(name = "APPROVED_BY")
    private String approvedBy;

    @Transient
    private List<ISStockReturnItem> stockReturnItemList;

    public ISStockReturn() {
        super(ISObjectType.STOCKRETURN);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Integer getReturnedTo() {
        return returnedTo;
    }

    public void setReturnedTo(Integer returnedTo) {
        this.returnedTo = returnedTo;
    }

    public String getReturnNumberSource() {
        return returnNumberSource;
    }

    public void setReturnNumberSource(String returnNumberSource) {
        this.returnNumberSource = returnNumberSource;
    }

    public Date getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(Date returnedDate) {
        this.returnedDate = returnedDate;
    }

    public List<ISStockReturnItem> getStockReturnItemList() {
        return stockReturnItemList;
    }

    public void setStockReturnItemList(List<ISStockReturnItem> stockReturnItemList) {
        this.stockReturnItemList = stockReturnItemList;
    }

    public StockReturnStatus getStatus() {
        return status;
    }

    public void setStatus(StockReturnStatus status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}
