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
@Table(name = "PDM_DRAWING")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMDrawing extends PDMRevisionedObject {

    @Column(name = "ATTACHED_TO")
    private Integer attachedTo;

    @Column(name = "PDF_FILE")
    private Integer pdfFile;

    public PDMDrawing() {
        super(PDMObjectType.PDM_DRAWING);
    }
}
