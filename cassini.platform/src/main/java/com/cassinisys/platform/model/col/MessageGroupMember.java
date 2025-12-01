package com.cassinisys.platform.model.col;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by lakshmi on 5/1/2016.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "MESSAGEGROUPMEMBER")
@PrimaryKeyJoinColumn(name = "ROWID")
public class MessageGroupMember extends CassiniObject {

    @Column(name = "PERSON", nullable = false)
    private Integer person;

    @Column(name = "IS_ADMIN", nullable = false)
    private boolean isAdmin=false;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MSG_GROUP_ID",  insertable = true, updatable = true)
    private MessageGroup messageGroup;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.col.GroupMemberStatus")})
    @Column(name = "STATUS", nullable = true)
    private GroupMemberStatus status=GroupMemberStatus.ACTIVE;


    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.core.ObjectType") })
    @Column(name = "CTX_OBJECT_TYPE", nullable = true)
    private ObjectType ctxObjectType;

    @Column(name = "CTX_OBJECT_ID", nullable = true)
    private Integer ctxObjectId;


    @Transient
    private String personName;



    public MessageGroupMember(){

        super(ObjectType.MESSAGEGROUPMEMBER);
    }


}
