package com.cassinisys.is.model.pm;

import com.cassinisys.platform.model.common.Header;

/**
 * Created by Nageshreddy on 28-09-2018.
 */
public class ProjectMaterialReportDTO {

    private String itemNo;
    private String item;
    private String unit;
    @Header("BOQ Qty")
    private String boqQty;
    private String suppliedQty;
    private String totalSuppliedQty;
    private String suppliedChallan;
    private String suppliedDate;
    private String isuQty;
    private String isuChallan;
    private String isuDate;
    private String consumeQty;
    private String returnQty;
    private String returnChallan;
    private String returnDate;
    private String balanceQty;
    private String totalReturnedQty;
    private Boolean hideRow = true;
    private Integer noOfRows = 0;

    public ProjectMaterialReportDTO(String itemNo, String item, String unit, String boqQty, String suppliedQty, String suppliedChallan, String suppliedDate, String isuQty,
                                    String isuChallan, String isuDate, String returnQty, String returnChallan, String returnDate, String balanceQty, Boolean hideRow, Integer noOfRows) {
        this.itemNo = itemNo;
        this.item = item;
        this.unit = unit;
        this.boqQty = boqQty;
        this.suppliedQty = suppliedQty;
        this.suppliedChallan = suppliedChallan;
        this.suppliedDate = suppliedDate;
        this.isuQty = isuQty;
        this.isuChallan = isuChallan;
        this.isuDate = isuDate;
        this.returnQty = returnQty;
        this.returnChallan = returnChallan;
        this.returnDate = returnDate;
        this.balanceQty = balanceQty;
        this.hideRow = hideRow;
        this.noOfRows = noOfRows;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBoqQty() {
        return boqQty;
    }

    public void setBoqQty(String boqQty) {
        this.boqQty = boqQty;
    }

    public String getSuppliedQty() {
        return suppliedQty;
    }

    public void setSuppliedQty(String suppliedQty) {
        this.suppliedQty = suppliedQty;
    }

    public String getSuppliedChallan() {
        return suppliedChallan;
    }

    public void setSuppliedChallan(String suppliedChallan) {
        this.suppliedChallan = suppliedChallan;
    }

    public String getSuppliedDate() {
        return suppliedDate;
    }

    public void setSuppliedDate(String suppliedDate) {
        this.suppliedDate = suppliedDate;
    }

    public String getIsuQty() {
        return isuQty;
    }

    public void setIsuQty(String isuQty) {
        this.isuQty = isuQty;
    }

    public String getIsuChallan() {
        return isuChallan;
    }

    public void setIsuChallan(String isuChallan) {
        this.isuChallan = isuChallan;
    }

    public String getIsuDate() {
        return isuDate;
    }

    public void setIsuDate(String isuDate) {
        this.isuDate = isuDate;
    }

    public String getConsumeQty() {
        return consumeQty;
    }

    public void setConsumeQty(String consumeQty) {
        this.consumeQty = consumeQty;
    }

    public String getBalanceQty() {
        return balanceQty;
    }

    public void setBalanceQty(String balanceQty) {
        this.balanceQty = balanceQty;
    }

    public String getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(String returnQty) {
        this.returnQty = returnQty;
    }

    public String getReturnChallan() {
        return returnChallan;
    }

    public void setReturnChallan(String returnChallan) {
        this.returnChallan = returnChallan;
    }

    public String getTotalReturnedQty() {
        return totalReturnedQty;
    }

    public void setTotalReturnedQty(String totalReturnedQty) {
        this.totalReturnedQty = totalReturnedQty;
    }

    public String getTotalSuppliedQty() {
        return totalSuppliedQty;
    }

    public void setTotalSuppliedQty(String totalSuppliedQty) {
        this.totalSuppliedQty = totalSuppliedQty;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public Boolean getHideRow() {
        return hideRow;
    }

    public void setHideRow(Boolean hideRow) {
        this.hideRow = hideRow;
    }

    public Integer getNoOfRows() {
        return noOfRows;
    }

    public void setNoOfRows(Integer noOfRows) {
        this.noOfRows = noOfRows;
    }
}
