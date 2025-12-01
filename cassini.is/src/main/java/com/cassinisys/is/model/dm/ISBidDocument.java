package com.cassinisys.is.model.dm;
/**
 * Model for ISBidDocument
 */

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_BIDDOCUMENT")
@PrimaryKeyJoinColumn(name = "DOCUMENT_ID")
@ApiObject(name = "DM")
public class ISBidDocument extends ISDocument {

    private static final long serialVersionUID = 1L;

    @Column(name = "BID", nullable = false)
    @ApiObjectField(required = true)
    private Integer bid;

    public ISBidDocument() {
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
