package com.cassinisys.pdm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Entity
@Table(name = "PDM_ITEMREFERENCE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PDM")
public class PDMItemReference implements Serializable{

    @Id
    @SequenceGenerator(
            name = "ITEMREFERENCE_ID_GEN",
            sequenceName = "ITEMREFERENCE_ID_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ITEMREFERENCE_ID_GEN"
    )
    @Column(name = "ROWID")
    private Integer rowId;

    @ApiObjectField(required = true)
    @Column(name = "ITEM")
    private Integer item;

    @ApiObjectField(required = true)
    @Column(name = "REFERENCE")
    private Integer reference;

    @Column(name = "REVISION")
    private String revision;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "NOTES")
    private String notes;

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
