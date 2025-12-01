package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by swapna on 04/12/18.
 */
@Entity
@Table(name = "IS_STOCKRETURNITEM")
@PrimaryKeyJoinColumn(name = "ROWID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class ISStockReturnItem extends ISTopStockMovement {

    @Column
    private Integer stockReturn;

    public ISStockReturnItem() {
        super(ISObjectType.STOCKRETURNITEM);
    }

    public Integer getStockReturn() {
        return stockReturn;
    }

    public void setStockReturn(Integer stockReturn) {
        this.stockReturn = stockReturn;
    }
}
