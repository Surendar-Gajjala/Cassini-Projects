package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_SUPPLIER_AUDIT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMSupplierAudit extends CassiniObject {

    @Column(name = "NUMBER", nullable = false)
    private String number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PQMSupplierAuditType type;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ASSIGNED_TO", nullable = false)
    private Integer assignedTo;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @OneToOne
    @JoinColumn(name = "STATUS")
    private PLMLifeCyclePhase status;

    @Column(name = "PLANNED_YEAR")
    private String plannedYear;

    @Transient
    private String assignedToName;
    @Transient
    private Boolean startWorkflow = Boolean.FALSE;
    @Transient
    private String createdPerson;
    @Transient
    private String modifiedPerson;

    public PQMSupplierAudit() {
        super.setObjectType(PLMObjectType.SUPPLIERAUDIT);
    }

}
