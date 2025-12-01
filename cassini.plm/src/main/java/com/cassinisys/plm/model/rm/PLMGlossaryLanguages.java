package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by subramanyam reddy on 14-09-2018.
 */
@Entity
@Data
@Table(name = "PLM_GLOSSARYLANGUAGES")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMGlossaryLanguages implements Serializable {

    @Id
    @SequenceGenerator(name = "GLOSSARY_LANGUAGE_ID_GEN", sequenceName = "GLOSSARY_LANGUAGE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GLOSSARY_LANGUAGE_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "LANGUAGE")
    private String language;

    @Column(name = "DEFAULT_LANGUAGE")
    private Boolean defaultLanguage = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE", nullable = false)
    private Date createdDate = new Date();


}
