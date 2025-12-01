package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_SUPPLIER_AUDIT_PLAN")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMSupplierAuditPlan extends CassiniObject {

    @Column(name = "SUPPLIER", nullable = false)
    private Integer supplier;

    @Column(name = "SUPPLIER_AUDIT", nullable = false)
    private Integer supplierAudit;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pqm.AuditPlanStatus")})
    @Column(name = "STATUS")
    private AuditPlanStatus status = AuditPlanStatus.NONE;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_START_DATE")
    private Date plannedStartDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTUAL_START_DATE")
    private Date actualStartDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINISHED_DATE")
    private Date finishedDate;

    @Transient
    private PQMSupplierAudit audit;


    @Transient
    private String number;
    @Transient
    private String city;
    @Transient
    private String name;
    @Transient
    private Integer approvedCount = 0;
    @Transient
    private Integer reviewerCount;

    public PQMSupplierAuditPlan() {
        super.setObjectType(PLMObjectType.SUPPLIERAUDIT);
    }
}
