package com.cassinisys.plm.model.mro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Table(name = "MRO_WORK_ORDER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MROWorkOrder extends MROObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MROWorkOrderType type;


    @Column(name = "ASSET")
    private Integer asset;

    @Column(name = "REQUEST")
    private Integer request;

    @Column(name = "PLAN")
    private Integer plan;

    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;


    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mro.WorkPriorityType")})
    @Column(name = "PRIORITY", nullable = true)
    private WorkPriorityType priority = WorkPriorityType.LOW;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mro.WorkOrderStatusType")})
    @Column(name = "STATUS", nullable = true)
    private WorkOrderStatusType status = WorkOrderStatusType.OPEN;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "WORKFLOW")
    private Integer workflow;
    @Transient
    private String createPerson;
    @Transient
    private String typeName;
    @Transient
    private String assignedPerson;
    @Transient
    private String maintenancePlan;

    @Transient
    private String requestNumber;

    @Transient
    private String modifiedPerson;
    @Transient
    private String priorityName;

    @Transient
    private String statusName;
    @Transient
    private String assetName;

    public MROWorkOrder() {
        super.setObjectType(MROEnumObject.MROWORKORDER);
    }
}
