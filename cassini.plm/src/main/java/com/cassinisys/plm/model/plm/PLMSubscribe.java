package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.plm.model.cm.PLMECO;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.ProjectTemplate;
import com.cassinisys.plm.model.rm.PLMGlossary;
import com.cassinisys.plm.model.rm.Requirement;
import com.cassinisys.plm.model.rm.Specification;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam reddy on 29-09-2018.
 */
@Entity
@Data
@Table(name = "PLM_SUBSCRIBE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMSubscribe implements Serializable {

    @Id
    @SequenceGenerator(name = "SUBSCRIBE_ID_GEN", sequenceName = "SUBSCRIBE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBSCRIBE_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "OBJECT_ID")
    private Integer objectId;

    @Column(name = "OBJECT_TYPE")
    private String objectType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSON")
    private Person person;

    @Column(name = "SUBSCRIBE")
    private Boolean subscribe = Boolean.FALSE;

    @Transient
    private String name;

  /*  public List<CassiniObject> getCassiniObjects() {
        return cassiniObjects;
    }

    public void setCassiniObjects(List<CassiniObject> cassiniObjects) {
        this.cassiniObjects = cassiniObjects;
    }

    @Transient
    private List<CassiniObject> cassiniObjects;*/

   /* @Transient
    private String objectType;*/
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
    private Specification specification;
    @Transient
    private Requirement requirement;


}
