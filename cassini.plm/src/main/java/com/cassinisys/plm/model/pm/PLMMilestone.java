package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.common.Person;
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

/**
 * Created by swapna on 12/31/17.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_MILESTONE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMMilestone extends CassiniObject {

    @Transient
    Boolean finishMilestone = false;
    @Column
    private Integer wbs;
    @Column
    private String name;
    @Column
    private String description;
    @Column(name = "SEQUENCE_NUMBER")
    private Integer sequenceNumber;
    @Column
    @Type(
            type = "com.cassinisys.platform.util.converter.IntArrayUserType"
    )
    private Integer[] predecessors;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_FINISHDATE")
    private Date plannedFinishDate;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTUAL_FINISHDATE")
    private Date actualFinishDate;
    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pm.ProjectActivityStatus")})
    @Column(name = "STATUS", nullable = false)
    private ProjectActivityStatus status;
    @Transient
    private Integer level = 0;
    @Transient
    private Person person;
    @Transient
    private Long ganttId;

    public PLMMilestone() {
        super(PLMObjectType.PROJECTMILESTONE);
    }


}
