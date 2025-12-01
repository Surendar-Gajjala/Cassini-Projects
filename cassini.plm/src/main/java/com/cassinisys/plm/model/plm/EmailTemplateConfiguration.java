package com.cassinisys.plm.model.plm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "EMAIL_TEMPLATE_CONFIGURATION")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailTemplateConfiguration implements Serializable {

    @Id
    @SequenceGenerator(name = "EMAILTEMPLATECONFIGURATION_ID_GEN", sequenceName = "EMAILTEMPLATECONFIGURATION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMAILTEMPLATECONFIGURATION_ID_GEN")
    @Column(name = "ID")
    private Integer templateId;

    @Column(name = "TEMPLATE_NAME")
    private String templateName;
    @Column(name = "TEMPLATE_SOURCECODE")
    private String templateSourceCode;


}
