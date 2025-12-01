package com.cassinisys.plm.model.pdm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "PDM_ASSEMBLY")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMAssembly extends PDMRevisionedObject {
    @Column(name = "DEFAULT_CONFIGURATION")
    private Integer defaultConfiguration;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "DRAWING_MASTER")
    private PDMRevisionMaster drawingMaster;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "DRAWING")
    private PDMDrawing drawingRevision;

    public PDMAssembly() {
        super(PDMObjectType.PDM_ASSEMBLY);
    }
}
