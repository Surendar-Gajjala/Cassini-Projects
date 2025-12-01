package com.cassinisys.is.model.procm;
/* Model for ISRfqResponse */

import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "IS_RFQRESPONSE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "PROCM")
public class ISRfqResponse {

    @Id
    @SequenceGenerator(name = "RFQRESPONSE_ID_GEN",
            sequenceName = "ROW_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "RFQRESPONSE_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "RFQ", nullable = false)
    @ApiObjectField(required = true)
    private Integer rfq;

    @Column(name = "SUPPLIER", nullable = false)
    @ApiObjectField(required = true)
    private Integer supplier;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RESPONSE_DATE", nullable = false)
    @ApiObjectField(required = true)
    private Date responseDate;

    public ISRfqResponse() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getRfq() {
        return rfq;
    }

    public void setRfq(Integer rfq) {
        this.rfq = rfq;
    }

    public Integer getSupplier() {
        return supplier;
    }

    public void setSupplier(Integer supplier) {
        this.supplier = supplier;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
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
        ISRfqResponse other = (ISRfqResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
