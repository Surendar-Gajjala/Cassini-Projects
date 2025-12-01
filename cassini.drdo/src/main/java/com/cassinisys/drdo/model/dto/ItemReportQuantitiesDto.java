package com.cassinisys.drdo.model.dto;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by subra on 30-10-2019.
 */
public class ItemReportQuantitiesDto {

    private Double requested = 0.0;

    private Double allocated = 0.0;

    private Double issued = 0.0;

    private Double issueProcess = 0.0;

    private Double failure = 0.0;

    private Double shortage = 0.0;

    private String list;

    @ApiObjectField
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedDate;

    @ApiObjectField
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date allocatedDate;

    public Double getRequested() {
        return requested;
    }

    public void setRequested(Double requested) {
        this.requested = requested;
    }

    public Double getAllocated() {
        return allocated;
    }

    public void setAllocated(Double allocated) {
        this.allocated = allocated;
    }

    public Double getIssued() {
        return issued;
    }

    public void setIssued(Double issued) {
        this.issued = issued;
    }

    public Double getIssueProcess() {
        return issueProcess;
    }

    public void setIssueProcess(Double issueProcess) {
        this.issueProcess = issueProcess;
    }

    public Double getFailure() {
        return failure;
    }

    public void setFailure(Double failure) {
        this.failure = failure;
    }

    public Double getShortage() {
        return shortage;
    }

    public void setShortage(Double shortage) {
        this.shortage = shortage;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Date getAllocatedDate() {
        return allocatedDate;
    }

    public void setAllocatedDate(Date allocatedDate) {
        this.allocatedDate = allocatedDate;
    }
}
