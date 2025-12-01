package com.cassinisys.is.model.store;

import com.cassinisys.is.model.procm.ISMaterialItem;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Entity
@Table(name = "CUSTOM_INDENTITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class CustomIndentItem implements Serializable {

    @ApiObjectField(required = true)
    @Id
    @SequenceGenerator(name = "CUSTOMINDENTITEM_ID_GEN", sequenceName = "CUSTOMINDENTITEM_ID_SEQ ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMINDENTITEM_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INDENT", nullable = false, updatable = false)
    @ApiObjectField(required = true)
    @JsonBackReference
    private CustomIndent customIndent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REQUISITION", nullable = false, updatable = false)
    @ApiObjectField(required = true)
    private CustomRequisition requisition;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public CustomRequisition getRequisition() {
        return requisition;
    }

    public void setRequisition(CustomRequisition requisition) {
        this.requisition = requisition;
    }

    public CustomIndent getCustomIndent() {
        return customIndent;
    }

    public void setCustomIndent(CustomIndent customIndent) {
        this.customIndent = customIndent;
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
