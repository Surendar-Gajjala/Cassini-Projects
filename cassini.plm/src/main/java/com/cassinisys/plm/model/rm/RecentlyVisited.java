package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.cm.PLMECO;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.ProjectTemplate;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by subramanyam reddy on 21-09-2018.
 */
@Entity
@Data
@Table(name = "RECENTLY_VISITED")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecentlyVisited implements Serializable {
    @Id
    @SequenceGenerator(name = "RECENTLY_VISITED_ID_GEN", sequenceName = "RECENTLY_VISITED_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECENTLY_VISITED_ID_GEN")
    @Column(name = "id")
    private Integer id;

    @Column(name = "OBJECT_ID")
    private Integer objectId;

    @Column(name = "OBJECT_TYPE")
    private String objectType;

    @Column(name = "PERSON")
    private Integer person;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VISITED_DATE", nullable = false)
    private Date visitedDate = new Date();

    @Transient
    private String name;

    @Transient
    private String type;

    @Transient
    private PLMItem item;

    @Transient
    private PLMECO eco;

    @Transient
    private PLMWorkflowDefinition workflow;

    @Transient
    private PLMManufacturer manufacturer;

    @Transient
    private PLMGlossary glossary;

    @Transient
    private PLMProject project;

    @Transient
    private ProjectTemplate projectTemplate;

    @Transient
    private Boolean configurable = false;

    @Transient
    private Boolean configured = false;

    @Transient
    private Boolean hasBom = false;

    @Transient
    private Boolean hasFiles = false;


}
