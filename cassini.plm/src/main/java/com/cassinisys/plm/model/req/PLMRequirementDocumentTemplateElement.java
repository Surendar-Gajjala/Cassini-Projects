package com.cassinisys.plm.model.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENTDOCUMENTTEMPLATEELEMENT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PLMRequirementDocumentTemplateElement implements Serializable {

    @Id
    @SequenceGenerator(name = "REQUIREMENTDOCUMENTTEMPLATEELEMENT_ID_GEN", sequenceName = "REQUIREMENTDOCUMENTTEMPLATEELEMENT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUIREMENTDOCUMENTTEMPLATEELEMENT_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TEMPLATE")
    private Integer template;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "PARENT")
    private Integer parent;


}

