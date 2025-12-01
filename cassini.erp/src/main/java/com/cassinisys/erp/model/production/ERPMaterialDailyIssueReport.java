package com.cassinisys.erp.model.production;

import com.cassinisys.erp.converters.CustomShortDateDeserializer;
import com.cassinisys.erp.converters.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Nageshreddy on 21-08-2018.
 */

@Entity
@Table(name = "ERP_MATERIALDAILYISSUEREPORT")
@ApiObject(group = "PRODUCTION")
public class ERPMaterialDailyIssueReport {

    @ApiObjectField(required = true)
    private Integer rowId;
    @ApiObjectField(required = true)
    private Integer material;
    @ApiObjectField(required = true)
    private Integer quantity = 0;
    @ApiObjectField(required = true)
    private Integer consumeQty = 0;
    @ApiObjectField(required = true)
    private Integer remainingQty = 0;
    @ApiObjectField(required = true)
    private Date timestamp;


    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ROWID")
    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    @Column(name = "MATERIAL_ID")
    public Integer getMaterial() {
        return material;
    }

    public void setMaterial(Integer material) {
        this.material = material;
    }

    @Column(name = "QUANTITY")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getConsumeQty() {
        return consumeQty;
    }

    public void setConsumeQty(Integer consumeQty) {
        this.consumeQty = consumeQty;
    }

    @Column(name = "REMAININGQTY")
    public Integer getRemainingQty() {
        return remainingQty;
    }

    public void setRemainingQty(Integer remainingQty) {
        this.remainingQty = remainingQty;
    }

    @Column(name = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
