package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_QCR_CAPA")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMQCRCAPA extends CassiniObject {

    @Column(name = "QCR")
    private Integer qcr;

    @Column(name = "ROOT_CAUSE_ANALYSIS")
    private String rootCauseAnalysis;

    @Column(name = "CORRECTIVE_ACTION")
    private String correctiveAction;

    @Column(name = "PREVENTIVE_ACTION")
    private String preventiveAction;

    @Column(name = "CAPA_NOTES")
    private String capaNotes;

    @Column(name = "LATEST")
    private Boolean latest = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AUDIT_DATE")
    private Date auditDate;

    @Column(name = "AUDITED_BY")
    private Integer auditedBy;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pqm.AuditResult")})
    @Column(name = "AUDIT_RESULT", nullable = true)
    private AuditResult result = AuditResult.NONE;

    @Column(name = "AUDIT_NOTES")
    private String auditNotes;

    @Transient
    private String createdPerson;

    @Transient
    private String modifiedPerson;

    @Transient
    private String auditResult;

    public PQMQCRCAPA() {
        super.setObjectType(PLMObjectType.QCRCAPA);
    }



}
