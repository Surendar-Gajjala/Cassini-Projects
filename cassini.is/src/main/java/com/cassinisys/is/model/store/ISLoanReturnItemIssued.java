package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by swapna on 21/08/18.
 */
@Entity
@Table(name = "IS_LOANRETURNITEMISSUED")
@PrimaryKeyJoinColumn(name = "ROWID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "LOAN")
public class ISLoanReturnItemIssued extends ISTopStockMovement {

    @Column
    private Integer loan;

    @Column
    private Double quantity = 0.0;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @ApiObjectField(required = true)
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date date;

    public ISLoanReturnItemIssued() {
        super(ISObjectType.LOANRETURNITEMISSUED);
    }

    @Override
    public Double getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getLoan() {
        return loan;
    }

    public void setLoan(Integer loan) {
        this.loan = loan;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
