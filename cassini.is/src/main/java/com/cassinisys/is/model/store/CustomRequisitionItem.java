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
@Table(name = "CUSTOM_REQUISITIONITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class CustomRequisitionItem implements Serializable {

    @ApiObjectField(required = true)
    @Id
    @SequenceGenerator(name = "CUSTOMREQUISITIONITEM_ID_GEN", sequenceName = "CUSTOMREQUISITIONITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMREQUISITIONITEM_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "REQUISITION")
    @ApiObjectField(required = true)
    private Integer requisition;

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

    @Transient
    private Double indentQuantity = 0.0;

    @Transient
    private String indentNotesObject;

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getIndentQuantity() {
        return indentQuantity;
    }

    public void setIndentQuantity(Double indentQuantity) {
        this.indentQuantity = indentQuantity;
    }

    public String getIndentNotesObject() {
        return indentNotesObject;
    }

    public void setIndentNotesObject(String indentNotesObject) {
        this.indentNotesObject = indentNotesObject;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRequisition() {
        return requisition;
    }

    public void setRequisition(Integer requisition) {
        this.requisition = requisition;
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
