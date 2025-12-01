package com.cassinisys.platform.model.col;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

/**
 * Created by lakshmi on 5/1/2016.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "MESSAGEGROUP")
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "MG_ID")
public class MessageGroup extends CassiniObject{

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "GROUP_ICON")
    private byte[] groupIcon;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;


    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.core.ObjectType") })
    @Column(name = "CTX_OBJECT_TYPE", nullable = true)
    private ObjectType ctxObjectType;

    @Column(name = "CTX_OBJECT_ID", nullable = true)
    private Integer ctxObjectId;

    @Transient
    private Long messagesCount;

    @Transient
    private Long activeUsersCount;


    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "messageGroup",cascade=CascadeType.ALL )
    private List<MessageGroupMember> msgGrpMembers;

    public MessageGroup(){
        super(ObjectType.MESSAGEGROUP);
    }

}
