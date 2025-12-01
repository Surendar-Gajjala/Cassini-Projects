package com.cassinisys.is.model.tm;

import com.cassinisys.platform.model.common.Header;

import java.io.Serializable;

/**
 * Created by Nageshreddy on 24-09-2018.
 */

public class TaskReportDTO implements Serializable {

    private Integer serialNo;
    @Header("Task Name")
    private String task;
    @Header("Unit of work")
    private String units;
    @Header("Total Units")
    private Double totalQty;
    @Header("Units Completed")
    private Double completed;
    @Header("Balance Work Units")
    private Double balanceQty;
    @Header("Percentage Completed")
    private Double percentage;
    private String remarks;

    public TaskReportDTO(Integer serialNo, String task, String units,
                         Double totalQty, Double completed, Double balanceQty, Double percentage) {
        this.serialNo = serialNo;
        this.task = task;
        this.units = units;
        this.totalQty = totalQty;
        this.completed = completed;
        this.balanceQty = balanceQty;
        this.percentage = percentage;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Double totalQty) {
        this.totalQty = totalQty;
    }

    public Double getCompleted() {
        return completed;
    }

    public void setCompleted(Double completed) {
        this.completed = completed;
    }

    public Double getBalanceQty() {
        return balanceQty;
    }

    public void setBalanceQty(Double balanceQty) {
        this.balanceQty = balanceQty;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
