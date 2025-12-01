package com.cassinisys.platform.model.col;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lakshmi on 5/22/2016.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "DISCUSSIONGROUPMESSAGE")
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "D_GRP_MSG_ID")
public class DiscussionGroupMessage extends CassiniObject {

    private static final long serialVersionUID = 1L;


    @JsonSerialize(using = CustomDateSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COMMENTED_DATE", nullable = false)
    private Date commentedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COMMENTED_BY", nullable = false)
    private Person commentedBy;

    @Column(name = "COMMENT", nullable = false)
    private String comment;

    @Column(name = "REPLY_TO")
    private Integer replyTo;


    @Column(name = "D_GRP_ID", nullable = false)
    private Integer discussionGroupId;


    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.core.ObjectType") })
    @Column(name = "CTX_OBJECT_TYPE", nullable = true)
    private ObjectType ctxObjectType;

    @Column(name = "CTX_OBJECT_ID", nullable = true)
    private Integer ctxObjectId;

    @Transient
    private List<DiscussionGroupMessage> children = new ArrayList<>();

    @Transient
    private List<Attachment> attachments;

    public DiscussionGroupMessage(){
     super(ObjectType.DISCUSSIONGROUPMESSAGE);
    }


}

