package com.cassinisys.is.model.procm;
/* Model for ISRfqResponseItem */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

@Entity
@Table(name = "IS_RFQRESPONSEITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "PROCM")
public class ISRfqResponseItem {

    @Id
    @SequenceGenerator(name = "RFQRESPONSEITEM_ID_GEN",
            sequenceName = "ROW_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "RFQRESPONSEITEM_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "RESPONSE", nullable = false)
    @ApiObjectField(required = true)
    private Integer response;

    @Column(name = "SUPPLIER", nullable = false)
    @ApiObjectField(required = true)
    private Integer supplier;

    @Column(name = "ITEM", nullable = false)
    @ApiObjectField(required = true)
    private Integer rfqItem;

    @Column(name = "PRICE", nullable = false)
    @ApiObjectField(required = true)
    private Double price = 0.0;

    public ISRfqResponseItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public Integer getSupplier() {
        return supplier;
    }

    public void setSupplier(Integer supplier) {
        this.supplier = supplier;
    }

    public Integer getRfqItem() {
        return rfqItem;
    }

    public void setRfqItem(Integer rfqItem) {
        this.rfqItem = rfqItem;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ISRfqResponseItem other = (ISRfqResponseItem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
