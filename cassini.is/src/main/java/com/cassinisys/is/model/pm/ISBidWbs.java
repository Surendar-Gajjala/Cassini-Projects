package com.cassinisys.is.model.pm;
/**
 * Model for ISBidWbs
 */

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_BIDWBS")
@PrimaryKeyJoinColumn(name = "WBS_ID")
@ApiObject(name = "PM")
public class ISBidWbs extends ISWbs {

    private static final long serialVersionUID = 1L;

    @Column(name = "BID", nullable = false)
    @ApiObjectField(required = true)
    private Integer bid;

    public ISBidWbs() {
    }

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

}


