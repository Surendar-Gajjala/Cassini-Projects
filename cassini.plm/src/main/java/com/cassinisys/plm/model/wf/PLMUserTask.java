package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.ObjectTypeDeserializer;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "PLM_USERTASK")
@PrimaryKeyJoinColumn(name = "ID")
@EqualsAndHashCode(callSuper = false)
public class PLMUserTask extends CassiniObject {
    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;

    @Column(name = "SOURCE")
    private Integer source;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
            value = "com.cassinisys.platform.model.core.ObjectType")})
    @JsonDeserialize(using = ObjectTypeDeserializer.class)
    @Column(name = "SOURCE_TYPE")
    private ObjectType sourceType;

    @Column(name = "CONTEXT")
    private Integer context;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
            value = "com.cassinisys.platform.model.core.ObjectType")})
    @JsonDeserialize(using = ObjectTypeDeserializer.class)
    @Column(name = "CONTEXT_TYPE")
    private ObjectType contextType;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
            value = "com.cassinisys.plm.model.wf.UserTaskStatus")})
    @JsonDeserialize(using = ObjectTypeDeserializer.class)
    @Column(name = "STATUS")
    private UserTaskStatus status = UserTaskStatus.PENDING;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "DUE_DATE")
    private Date dueDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINISHED_ON")
    private Date finishedOn;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CANCELLED_ON")
    private Date cancelledOn;

    @Transient
    private CassiniObject sourceObject;

    @Transient
    private CassiniObject contextObject;


    public PLMUserTask() {
        super.setObjectType(PLMObjectType.USERTASK);
    }
}
