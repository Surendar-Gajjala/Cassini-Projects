package com.cassinisys.plm.model.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Lenovo on 25-06-2021.
 */
@Entity
@Table(name = "PLM_REQUIREMENTTEMPLATEREVIEWER")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PLMRequirementTemplateReviewer implements Serializable {
    @Id
    @SequenceGenerator(name = "REQUIREMENTTEMPLATEREVIEWER_ID_GEN", sequenceName = "REQUIREMENTTEMPLATEREVIEWER_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUIREMENTTEMPLATEREVIEWER_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "REQUIREMENT_TEMPLATE", nullable = false)
    private Integer requirementTemplate;

    @Column(name = "REVIEWER", nullable = false)
    private Integer reviewer;

    @Column(name = "APPROVER", nullable = false)
    private Boolean approver = Boolean.FALSE;

}
