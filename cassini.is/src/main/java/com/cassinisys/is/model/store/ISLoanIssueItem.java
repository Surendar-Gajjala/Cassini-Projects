package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by swapna on 10/08/18.
 */
@Entity
@Table(name = "IS_LOANISSUEITEM")
@PrimaryKeyJoinColumn(name = "ROWID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class ISLoanIssueItem extends ISTopStockMovement {

    @Column
    private Integer loan;

    public ISLoanIssueItem() {
        super(ISObjectType.LOANISSUEITEM);
    }

    public Integer getLoan() {
        return loan;
    }

    public void setLoan(Integer loan) {
        this.loan = loan;
    }
}
