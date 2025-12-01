package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "MES_BOPINSTANCE_ROUTE_OPERATION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESBOPInstanceRouteOperation extends CassiniObject {

    @Column(name = "SEQUENCE_NUMBER")
    private String sequenceNumber;

    @Column(name = "OPERATION")
    private Integer operation;

    @Column(name = "BOPINSTANCE")
    private Integer bopInstance;

    @Column(name = "PHANTOM")
    private Integer phantom;

    @Column(name = "PARENT")
    private Integer parent;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mes.BOPPlanTypeEnum")})
    @Column(name = "TYPE")
    private BOPPlanTypeEnum type = BOPPlanTypeEnum.OPERATION;

    @Column(name = "SETUP_TIME")
    private Integer setupTime;

    @Column(name = "CYCLE_TIME")
    private Integer cycleTime;

    @Transient
    private String name;
    @Transient
    private String number;
    @Transient
    private String description;

    public MESBOPInstanceRouteOperation() {
        super.setObjectType(MESEnumObject.BOPINSTANCEROUTEOPERATION);
    }
}
