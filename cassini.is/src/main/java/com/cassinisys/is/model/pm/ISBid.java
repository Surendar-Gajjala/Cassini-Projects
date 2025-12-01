package com.cassinisys.is.model.pm;
/**
 * Model for ISBid
 */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_BID")
@PrimaryKeyJoinColumn(name = "BID_ID")
@ApiObject(name = "PM")
public class ISBid extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "CUSTOMER", nullable = false)
    @ApiObjectField(required = true)
    private Integer customer;

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION")
    @ApiObjectField
    private String description;

    @Column(name = "STATUS", nullable = false)
    @ApiObjectField(required = true)
    private String status;

    @Column(name = "STATUS_LOV", nullable = false)
    @ApiObjectField(required = true)
    private Integer statusLov;

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public ISBid() {
        super(ISObjectType.BID);
    }

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusLov() {
        return statusLov;
    }

    public void setStatusLov(Integer statusLov) {
        this.statusLov = statusLov;
    }

}
