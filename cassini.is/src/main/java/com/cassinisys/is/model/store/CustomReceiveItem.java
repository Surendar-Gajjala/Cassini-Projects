package com.cassinisys.is.model.store;

import com.cassinisys.is.model.procm.ISMaterialItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Entity
@Table(name = "CUSTOM_RECIEVEITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class CustomReceiveItem implements Serializable {

    @ApiObjectField(required = true)
    @Id
    @SequenceGenerator(name = "CUSTOMRECEIVEITEM_ID_GEN", sequenceName = "CUSTOMRECEIVEITEM_ID_SEQ ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMRECEIVEITEM_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RECEIVE_CHALAN")
    @ApiObjectField(required = true)
    private CustomReceiveChalan receiveChalan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MATERIAL")
    @ApiObjectField(required = true)
    private ISMaterialItem materialItem;

    @ApiObjectField(required = true)
    @Column(name = "QUANTITY")
    private Double quantity = 1.0;

    @ApiObjectField(required = true)
    @Column(name = "NOTES")
    private String notes;

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomReceiveChalan getReceiveChalan() {
        return receiveChalan;
    }

    public void setReceiveChalan(CustomReceiveChalan receiveChalan) {
        this.receiveChalan = receiveChalan;
    }

    public ISMaterialItem getMaterialItem() {
        return materialItem;
    }

    public void setMaterialItem(ISMaterialItem materialItem) {
        this.materialItem = materialItem;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
