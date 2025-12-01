package com.cassinisys.plm.model.cm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.cm.dto.RequestedItemDto;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_CHANGE_REQUEST")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMChangeRequest extends PLMChange {

    @Column(name = "CR_TYPE", nullable = true)
    private Integer crType;

    @Column(name = "CR_NUMBER")
    private String crNumber;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION_OF_CHANGE")
    private String descriptionOfChange;

    @Column(name = "REASON_FOR_CHANGE")
    private String reasonForChange;

    @Column(name = "PROPOSED_CHANGES")
    private String proposedChanges;

    @Column(name = "URGENCY", nullable = false)
    private String urgency;

    @Column(name = "ORIGINATOR")
    private Integer originator;

    @Column(name = "REQUESTED_BY")
    private Integer requestedBy;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REQUESTED_DATE")
    private Date requestedDate;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    @Column(name = "STATUS_TYPE", nullable = false)
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;


    @Column(name = "STATUS")
    private String status = "NONE";

    @Column(name = "CHANGE_ANALYST")
    private Integer changeAnalyst;

    @Column(name = "IS_APPROVED")
    private Boolean isApproved = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPROVED_DATE")
    private Date approvedDate;

    @Column(name = "IS_IMPLEMENTED")
    private Boolean isImplemented = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "IMPLEMENTED_DATE")
    private Date implementedDate;

    @Column(name = "REJECTION_REASON")
    private String rejectionReason;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "OTHER_REQUESTED")
    private String otherRequested;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.cm.RequesterType")})
    @Column(name = "REQUESTER_TYPE", nullable = true)
    private RequesterType requesterType = RequesterType.INTERNAL;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private List<RequestedItemDto> requestedItemDtos = new LinkedList<>();


}
