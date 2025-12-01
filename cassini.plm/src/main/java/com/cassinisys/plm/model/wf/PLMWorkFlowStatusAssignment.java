package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Rajabrahmachary on 19-05-2017.
 */
@Entity
@Data
@Table(name = "PLM_WORKFLOWSTATUSASSIGNMENT")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkFlowStatusAssignment implements Serializable {

    @Id
    @SequenceGenerator(name = "WORKFLOWSTATUSASSIGNMENT_ID_GEN", sequenceName = "WORKFLOWSTATUSASSIGNMENT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKFLOWSTATUSASSIGNMENT_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "STATUS", nullable = false)
    private Integer status;

    @Column(name = "ASSIGNMENT_TYPE", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.plm.model.wf.WorkflowAssigementType")})
    private WorkflowAssigementType assignmentType;

    @Column(name = "PERSON", nullable = false)
    private Integer person;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_NOTIFIED_ON")
    private Date lastNotifiedOn;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "ITERATION")
    private Integer iteration = 0;

    @Transient
    private String personName;

}
