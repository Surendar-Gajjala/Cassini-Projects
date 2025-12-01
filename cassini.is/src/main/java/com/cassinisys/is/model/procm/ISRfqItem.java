package com.cassinisys.is.model.procm;
/* Model for ISRfqItem */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "IS_RFQITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "PROCM")
public class ISRfqItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "RFQITEM_ID_GEN",
            sequenceName = "RFQITEM_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "RFQITEM_ID_GEN")
    @Column(name = "RFQITEM_ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "RFQ_ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer rfq;

    @Column(name = "ITEM_NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    @ApiObjectField(required = true)
    private String description;

    @Column(name = "UNITS", nullable = false)
    @ApiObjectField(required = true)
    private String units;

    @Column(name = "QUANTITY", nullable = false)
    @ApiObjectField(required = true)
    private Double quantity = 0.0;

    public ISRfqItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRfq() {
        return rfq;
    }

    public void setRfq(Integer rfq) {
        this.rfq = rfq;
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

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
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
        ISRfqItem other = (ISRfqItem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
