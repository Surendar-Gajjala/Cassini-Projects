package com.cassinisys.plm.model.pdm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "PDM_BOM_OCCURRENCE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMBOMOccurrence extends PDMObject {
    @Column(name = "PARENT")
    private Integer parent;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "NOTES")
    private String notes;

    public PDMBOMOccurrence() {
        super(PDMObjectType.PDM_BOMOCCURRENCE);
    }
}
