package com.cassinisys.plm.model.pdm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PDM_DRAWING_TEMPLATE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMDrawingTemplate extends PDMObject {

    @Column(name = "TEMPLATE_FILE")
    private Integer templateFile;

    @Transient
    private Integer attachmentId;

    @Transient
    private String attachmentName;

    public PDMDrawingTemplate() {
        super(PDMObjectType.PDM_DRAWINGTEMPLATE);
    }
}
