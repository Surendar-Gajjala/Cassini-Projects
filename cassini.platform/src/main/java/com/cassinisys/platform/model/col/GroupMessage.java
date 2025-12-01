package com.cassinisys.platform.model.col;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by lakshmi on 5/1/2016.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "GROUPMESSAGE")
@PrimaryKeyJoinColumn(name = "GRP_MSG_ID")
public class GroupMessage extends CassiniObject {

    @Column(name = "MSG_GROUP_ID", nullable = false)
    private Integer msgGrpId;

    @Column(name = "MESSAGE_TEXT", nullable = false)
    private String msgText;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "POSTED_DATE")
    private Date postedDate;

    @Column(name = "POSTED_BY", nullable = false)
    private Integer postedBy;

    @Transient
    private List<Attachment> attachments;

     @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.core.ObjectType") })
    @Column(name = "CTX_OBJECT_TYPE", nullable = true)
    private ObjectType ctxObjectType;

    @Column(name = "CTX_OBJECT_ID", nullable = true)
    private Integer ctxObjectId;


    @Transient
    private String postedByName;

    public GroupMessage(){

        super(ObjectType.GROUPMESSAGE);
    }


}


