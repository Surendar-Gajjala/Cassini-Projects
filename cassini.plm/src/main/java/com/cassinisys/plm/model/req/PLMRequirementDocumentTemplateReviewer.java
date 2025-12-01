package com.cassinisys.plm.model.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Lenovo on 25-06-2021.
 */
@Entity
@Table(name = "PLM_REQUIREMENTDOCUMENTTEMPLATEREVIEWER")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PLMRequirementDocumentTemplateReviewer implements Serializable {
    @Id
    @SequenceGenerator(name = "REQUIREMENTDOCUMENTTEMPLATEREVIEWER_ID_GEN", sequenceName = "REQUIREMENTDOCUMENTTEMPLATEREVIEWER_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUIREMENTDOCUMENTTEMPLATEREVIEWER_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "DOCUMENT_TEMPLATE", nullable = false)
    private Integer documentTemplate;

    @Column(name = "REVIEWER", nullable = false)
    private Integer reviewer;

    @Column(name = "APPROVER", nullable = false)
    private Boolean approver = Boolean.FALSE;

}
