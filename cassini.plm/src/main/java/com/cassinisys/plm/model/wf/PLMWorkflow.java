package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.ObjectTypeDeserializer;
import com.cassinisys.plm.model.cm.PLMECO;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PLMActivity;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.rm.Requirement;
import com.cassinisys.plm.model.rm.Specification;
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
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_WORKFLOW")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkflow extends CassiniObject {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DIAGRAM")
    private String diagram;

    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    @Column(name = "STARTED", nullable = false)
    private Boolean started = false;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STARTED_ON")
    private Date startedOn;

    @Column(name = "FINISHED", nullable = false)
    private Boolean finished = false;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINISHED_ON")
    private Date finishedOn;

    @Column(name = "ONHOLD", nullable = false)
    private Boolean onhold = false;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ONHOLD_ON")
    private Date onholdOn;

    @Column(name = "CANCELLED", nullable = false)
    private Boolean cancelled = false;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CANCELLED_ON")
    private Date cancelledOn;

    @Column(name = "ATTACHED_TO")
    private Integer attachedTo;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.plm.model.plm.PLMObjectType")})
    @Column(name = "ATTACHED_TO_TYPE", nullable = false)
    @JsonDeserialize(using = ObjectTypeDeserializer.class)
    private Enum attachedToType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "START")
    private PLMWorkflowStart start;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "FINISH")
    private PLMWorkflowFinish finish;

    @Column(name = "CURRENT_STATUS")
    private Integer currentStatus;

    @Column(name = "HOLD_BY")
    private Integer holdBy;

    @Column(name = "WORKFLOW_REVISION")
    private Integer workflowRevision;

    @Transient
    private List<PLMWorkflowStatus> statuses = new ArrayList<>();

    @Transient
    private List<PLMWorkflowTransition> transitions = new ArrayList<>();

    @Transient
    private PLMItem item;

    @Transient
    private PLMECO eco;

    @Transient
    private PLMProject project;

    @Transient
    private PLMManufacturer manufacturer;

    @Transient
    private PLMManufacturerPart manufacturerPart;

    @Transient
    private Specification specification;

    @Transient
    private Requirement requirement;

    @Transient
    private PLMActivity activity;

    @Transient
    private PLMTask task;

    @Transient
    private String status;

    @Transient
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;

    @Transient
    private Integer workflowTypeId;

    public PLMWorkflow() {
        super(PLMObjectType.PLMWORKFLOW);
    }

}
