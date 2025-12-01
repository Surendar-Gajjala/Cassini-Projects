package com.cassinisys.plm.model.req;

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
 * Created by CassiniSystems on 09-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENTREVIEWER")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PLMRequirementReviewer implements Serializable {

    @Id
    @SequenceGenerator(name = "REQUIREMENTREVIEWER_ID_GEN", sequenceName = "REQUIREMENTREVIEWER_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUIREMENTREVIEWER_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "REQUIREMENT_VERSION")
    private Integer requirementVersion;

    @Column(name = "REVIEWER")
    private Integer reviewer;

    @Column(name = "APPROVER")
    private Boolean approver = Boolean.FALSE;

    @Column(name = "VOTE", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.req.RequirementApprovalStatus")})
    private RequirementApprovalStatus status = RequirementApprovalStatus.NONE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VOTE_TIMESTAMP")
    private Date voteTimestamp = new Date();

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private String reviewerName;
    @Transient
    private Integer reqDoc;
}

