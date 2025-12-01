package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by swapna on 13/08/18.
 */
@Entity
@Table(name = "IS_LOANRECEIVEITEM")
@PrimaryKeyJoinColumn(name = "ROWID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "LOAN")
public class ISLoanReceiveItem extends ISTopStockMovement {

    @Column
    private Integer loan;

    {
    }

    public ISLoanReceiveItem() {
        super(ISObjectType.LOANRECEIVEITEM);
    }

    public Integer getLoan() {
        return loan;
    }

    public void setLoan(Integer loan) {
        this.loan = loan;
    }
}
