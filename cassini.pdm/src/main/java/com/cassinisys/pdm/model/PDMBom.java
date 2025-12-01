package com.cassinisys.pdm.model;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Entity
@Table(name = "PDM_BOM")
@PrimaryKeyJoinColumn(name = "BOMITEM_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PDM")
public class PDMBom extends CassiniObject{

    @Column(name = "ITEM")
    private Integer item;

    @Column(name = "PARENT")
    private Integer parent;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "REFDES")
    private String refDes;

    @Column(name = "NOTES")
    private String notes;

    public PDMBom(){super(PDMObjectType.BOMITEM);}

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRefDes() {
        return refDes;
    }

    public void setRefDes(String refDes) {
        this.refDes = refDes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
