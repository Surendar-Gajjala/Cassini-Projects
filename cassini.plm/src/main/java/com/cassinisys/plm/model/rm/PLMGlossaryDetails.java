package com.cassinisys.plm.model.rm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam reddy on 14-09-2018.
 */
@Entity
@Data
@Table(name = "PLM_GLOSSARYDETAILS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMGlossaryDetails implements Serializable {

    @Id
    @SequenceGenerator(name = "GLOSSARY_DETAILS_ID_GEN", sequenceName = "GLOSSARY_DETAILS_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GLOSSARY_DETAILS_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LANGUAGE")
    private PLMGlossaryLanguages language;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "GLOSSARY")
    private Integer glossary;


}
