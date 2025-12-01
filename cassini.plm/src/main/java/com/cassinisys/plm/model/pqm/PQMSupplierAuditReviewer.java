package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.DocumentApprovalStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_SUPPLIER_AUDIT_REVIEWER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMSupplierAuditReviewer implements Serializable {

    @Id
    @SequenceGenerator(name = "PQM_OBJECT_ID_GEN", sequenceName = "PQM_OBJECT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PQM_OBJECT_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PLAN", nullable = false)
    private Integer plan;

    @Column(name = "REVIEWER", nullable = false)
    private Integer reviewer;

    @Column(name = "APPROVER", nullable = false)
    private Boolean approver = Boolean.FALSE;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.plm.DocumentApprovalStatus")})
    @Column(name = "VOTE", nullable = false)
    private DocumentApprovalStatus status = DocumentApprovalStatus.NONE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VOTE_TIMESTAMP")
    private Date voteTimestamp;

    @Column(name = "NOTES")
    private String Notes;

    @Transient
    private String approverName;
}
