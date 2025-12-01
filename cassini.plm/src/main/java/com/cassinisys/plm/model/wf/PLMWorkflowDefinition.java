package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CassiniSystems on 19-05-2017.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_WORKFLOWDEFINITION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkflowDefinition extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "WORKFLOW_TYPE")
    private PLMWorkflowType workflowType;

    @Column(name = "DIAGRAM")
    private String diagram;

    @Column(name = "DIAGRAM_ID")
    private String diagramID;

    @Column(name = "ASSIGNABLE_TO")
    @Type(type = "com.cassinisys.platform.util.converter.StringArrayType")
    private String[] assignableTo;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "START")
    private PLMWorkflowDefinitionStart start;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "FINISH")
    private PLMWorkflowDefinitionFinish finish;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "MASTER")
    private PLMWorkflowDefinitionMaster master;

    @OneToOne
    @JoinColumn(name = "STATUS")
    private PLMLifeCyclePhase lifeCyclePhase;

    @Column(name = "INSTANCES")
    private Integer instances = 0;

    @Column(name = "REVISION")
    private String revision;

    @Column(name = "IS_RELEASED")
    private Boolean released = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELEASED_DATE", nullable = false)
    private Date releasedDate;

    @Transient
    private String number;

    @Transient
    private Boolean nextRevision = Boolean.FALSE;

    @Transient
    private List<PLMWorkflowDefinitionStatus> statuses = new ArrayList<>();

    @Transient
    private List<PLMWorkflowDefinitionTerminate> terminations = new ArrayList<>();

    @Transient
    private List<PLMWorkflowDefinitionTransition> transitions = new ArrayList<>();

    @Transient
    private List<PLMWorkflowRevisionHistory> statusHistory = new ArrayList<>();

    public PLMWorkflowDefinition() {
        super(PLMObjectType.PLMWORKFLOWDEFINITION);
    }


}
